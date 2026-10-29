package jlkvm.verify;

import jlkvm.classfile.ClassFile;
import jlkvm.classfile.ClassFile.CodeAttr;
import jlkvm.classfile.ClassFile.ExceptionEntry;
import jlkvm.classfile.ClassFile.MethodInfo;
import jlkvm.classfile.ClassFile.StackMapEntry;
import jlkvm.classfile.ClassFormatException;
import jlkvm.classfile.ConstantPool;
import jlkvm.classfile.Descriptors;
import java.util.ArrayList;
import java.util.List;

/**
 * CLDC type-checking verifier for one method (PLAN D6). Strict: uses only the StackMap attribute. Lenient: if the
 * attribute is missing or wrong, frames are inferred by data flow and a warning is recorded.
 *
 * Deviations/limits (documented in docs/verifier-report.md): protected-member access checks and the
 * "uninitialized object across a backward branch" check are not implemented.
 */
final class MethodVerifier {
    private static final int[] LEN = new int[256];   // 0 = invalid opcode, -1 = variable length
    private static final boolean[] FLOAT_OP = new boolean[256];
    static {
        for (int i = 0; i <= 15; i++) LEN[i] = 1;
        LEN[16] = 2; LEN[17] = 3; LEN[18] = 2; LEN[19] = 3; LEN[20] = 3;
        for (int i = 21; i <= 25; i++) LEN[i] = 2;
        for (int i = 26; i <= 53; i++) LEN[i] = 1;
        for (int i = 54; i <= 58; i++) LEN[i] = 2;
        for (int i = 59; i <= 131; i++) LEN[i] = 1;
        LEN[132] = 3;
        for (int i = 133; i <= 152; i++) LEN[i] = 1;
        for (int i = 153; i <= 168; i++) LEN[i] = 3;
        LEN[169] = 2; LEN[170] = -1; LEN[171] = -1;
        for (int i = 172; i <= 177; i++) LEN[i] = 1;
        for (int i = 178; i <= 184; i++) LEN[i] = 3;
        LEN[185] = 5;
        LEN[187] = 3; LEN[188] = 2; LEN[189] = 3; LEN[190] = 1; LEN[191] = 1; LEN[192] = 3; LEN[193] = 3;
        LEN[194] = 1; LEN[195] = 1; LEN[196] = -1; LEN[197] = 4; LEN[198] = 3; LEN[199] = 3; LEN[200] = 5; LEN[201] = 5;
        int[][] fl = {{11, 15}, {23, 24}, {34, 41}, {48, 49}, {56, 57}, {67, 74}, {81, 82}, {98, 99}, {102, 103},
                {106, 107}, {110, 111}, {114, 115}, {118, 119}, {134, 135}, {137, 144}, {149, 152}, {174, 175}};
        for (int[] r : fl) for (int i = r[0]; i <= r[1]; i++) FLOAT_OP[i] = true;
    }

    private final ClassFile cf;
    private final MethodInfo m;
    private final CodeAttr ca;
    private final byte[] code;
    private final ConstantPool cp;
    private final ClassHierarchy h;
    private final boolean allowFloat;
    private final List<String> warnings;

    private final boolean[] isStart;
    private final int[] insnLen;
    private Frame[] declared;      // from StackMap (strict)
    private Frame[] inferred;      // from data flow (lenient)
    private boolean inferMode;
    private boolean[] dirty;
    private int[] work; private int workN;

    /** Per-pc state before the instruction, filled when {@code record} is set. */
    Frame[] recorded;

    private int pc;
    private Frame f;

    MethodVerifier(ClassFile cf, MethodInfo m, ClassHierarchy h, boolean allowFloat, List<String> warnings) {
        this.cf = cf; this.m = m; this.ca = m.code; this.code = ca.code; this.cp = cf.pool; this.h = h;
        this.allowFloat = allowFloat; this.warnings = warnings;
        isStart = new boolean[code.length + 1];
        insnLen = new int[code.length];
    }

    private VerifyException err(int at, String why) { return new VerifyException(cf.name, m.name, m.descriptor, at, why); }
    private VerifyException err(String why) { return err(pc, why); }

    // ---------------------------------------------------------------- entry points

    /** Verifies the method; returns the per-pc frames (before each instruction) when {@code record}. */
    Frame[] verify(boolean strict, boolean record) {
        try {
            scan();
            if (record) recorded = new Frame[code.length];
            checkHandlerShape();
            if (strict) {
                declared = buildDeclared();
                runStrict();
            } else {
                boolean ok = false;
                try {
                    if (ca.stackMap == null && needsFrames()) throw err(-1, "no StackMap attribute");
                    declared = buildDeclared();
                    runStrict();
                    ok = true;
                } catch (VerifyException e) {
                    warnings.add("lenient: " + e.getMessage() + " -> inferring types");
                }
                if (!ok) {
                    if (record) java.util.Arrays.fill(recorded, null);
                    runInfer();
                }
            }
        } catch (ClassFormatException e) {
            throw err(-1, "bad constant pool reference: " + e.getMessage());
        }
        return recorded;
    }

    // ---------------------------------------------------------------- structure

    private void scan() {
        if (ca.maxLocals > 65535) throw err(-1, "max_locals too large");
        int p = 0;
        while (p < code.length) {
            int op = code[p] & 0xff;
            int len = LEN[op];
            if (op == 168 || op == 169 || op == 201) throw err(p, "jsr/ret not allowed in preverified code");
            if (len == 0) throw err(p, "illegal opcode " + op);
            if (!allowFloat && FLOAT_OP[op]) throw err(p, "floating point opcode " + op + " not allowed in CLDC 1.0");
            if (len < 0) {
                if (op == 196) {
                    if (p + 1 >= code.length) throw err(p, "truncated wide");
                    int sub = code[p + 1] & 0xff;
                    if (sub == 132) len = 6;
                    else if ((sub >= 21 && sub <= 25) || (sub >= 54 && sub <= 58)) len = 4;
                    else throw err(p, "illegal wide operand " + sub);
                    if (!allowFloat && FLOAT_OP[sub]) throw err(p, "floating point opcode not allowed in CLDC 1.0");
                } else {
                    int a = (p + 4) & ~3;
                    if (a + 8 > code.length) throw err(p, "truncated switch");
                    if (op == 170) {
                        long lo = s4(a + 4), hi = s4(a + 8 - 0 + 0 - 0);
                        // layout: default(a), low(a+4), high(a+8)
                        if (a + 12 > code.length) throw err(p, "truncated switch");
                        lo = s4(a + 4); hi = s4(a + 8);
                        if (hi < lo || hi - lo > 65535) throw err(p, "bad tableswitch range");
                        len = (int) (a + 12 + 4 * (hi - lo + 1) - p);
                    } else {
                        long n = s4(a + 4);
                        if (n < 0 || n > 65535) throw err(p, "bad lookupswitch size");
                        len = (int) (a + 8 + 8 * n - p);
                    }
                }
            }
            if (p + len > code.length) throw err(p, "instruction runs past end of code");
            isStart[p] = true;
            insnLen[p] = len;
            p += len;
        }
        isStart[code.length] = true;
    }

    private int s4(int a) {
        return ((code[a] & 0xff) << 24) | ((code[a + 1] & 0xff) << 16) | ((code[a + 2] & 0xff) << 8) | (code[a + 3] & 0xff);
    }
    private int u2(int a) { return ((code[a] & 0xff) << 8) | (code[a + 1] & 0xff); }
    private int s2(int a) { return (short) u2(a); }

    private boolean needsFrames() {
        if (ca.exceptionTable.length > 0) return true;
        for (int p = 0; p < code.length; p += insnLen[p]) {
            int op = code[p] & 0xff;
            if ((op >= 153 && op <= 167) || op == 170 || op == 171 || op == 198 || op == 199 || op == 200) return true;
        }
        return false;
    }

    private void checkHandlerShape() {
        for (ExceptionEntry e : ca.exceptionTable) {
            if (!isStart[e.startPc] || !isStart[e.endPc] || !isStart[e.handlerPc] || e.handlerPc >= code.length)
                throw err(-1, "exception table entry does not point at instruction boundaries");
        }
    }

    // ---------------------------------------------------------------- frames

    private VType fromPacked(int v) {
        int arg = v >>> 8;
        switch (v & 0xff) {
            case ClassFile.VT_TOP: return VType.T_TOP;
            case ClassFile.VT_INTEGER: return VType.T_INT;
            case ClassFile.VT_FLOAT: return VType.T_FLOAT;
            case ClassFile.VT_DOUBLE: return VType.T_DOUBLE;
            case ClassFile.VT_LONG: return VType.T_LONG;
            case ClassFile.VT_NULL: return VType.T_NULL;
            case ClassFile.VT_UNINIT_THIS: return VType.T_UNINIT_THIS;
            case ClassFile.VT_OBJECT: return VType.object(cp.className(arg));
            default:
                if (arg >= code.length || (code[arg] & 0xff) != 187) throw err(-1, "uninitialized type does not refer to a new instruction");
                return VType.uninit(arg);
        }
    }

    private Frame buildDeclared0(StackMapEntry e) {
        Frame fr = new Frame(ca.maxLocals, ca.maxStack);
        int li = 0;
        for (int v : e.locals) {
            VType t = fromPacked(v);
            boolean two = t.kind == VType.LONG || t.kind == VType.DOUBLE;
            if (li + (two ? 2 : 1) > ca.maxLocals) throw err(e.offset, "StackMap locals exceed max_locals");
            fr.locals[li++] = t;
            if (two) fr.locals[li++] = t.kind == VType.LONG ? VType.T_LONG2 : VType.T_DOUBLE2;
        }
        for (int v : e.stack) {
            VType t = fromPacked(v);
            boolean two = t.kind == VType.LONG || t.kind == VType.DOUBLE;
            if (fr.sp + (two ? 2 : 1) > ca.maxStack) throw err(e.offset, "StackMap stack exceeds max_stack");
            fr.stack[fr.sp++] = t;
            if (two) fr.stack[fr.sp++] = t.kind == VType.LONG ? VType.T_LONG2 : VType.T_DOUBLE2;
        }
        return fr;
    }

    private Frame[] buildDeclared() {
        Frame[] d = new Frame[code.length + 1];
        if (ca.stackMap != null) {
            for (StackMapEntry e : ca.stackMap) {
                if (!isStart[e.offset]) throw err(e.offset, "StackMap entry is not at an instruction boundary");
                d[e.offset] = buildDeclared0(e);
            }
        }
        return d;
    }

    private Frame initialFrame() {
        Frame fr = new Frame(ca.maxLocals, ca.maxStack);
        int li = 0;
        if (!m.isStatic()) {
            if (ca.maxLocals < 1) throw err(-1, "max_locals too small for this");
            fr.locals[li++] = (m.name.equals("<init>") && !cf.name.equals("java/lang/Object")) ? VType.T_UNINIT_THIS : VType.object(cf.name);
        }
        for (String p : Descriptors.params(m.descriptor)) {
            VType t = typeOf(p);
            boolean two = t.kind == VType.LONG || t.kind == VType.DOUBLE;
            if (li + (two ? 2 : 1) > ca.maxLocals) throw err(-1, "max_locals too small for the arguments");
            fr.locals[li++] = t;
            if (two) fr.locals[li++] = t.kind == VType.LONG ? VType.T_LONG2 : VType.T_DOUBLE2;
        }
        return fr;
    }

    static VType typeOf(String d) {
        switch (d.charAt(0)) {
            case 'B': case 'C': case 'S': case 'Z': case 'I': return VType.T_INT;
            case 'F': return VType.T_FLOAT;
            case 'J': return VType.T_LONG;
            case 'D': return VType.T_DOUBLE;
            case 'L': return VType.object(d.substring(1, d.length() - 1));
            default: return VType.object(d);
        }
    }

    // ---------------------------------------------------------------- assignability

    private boolean classAssignable(String to, String from) {
        if (to.equals(from) || to.equals("java/lang/Object")) return true;
        if (from.charAt(0) == '[') {
            if (to.charAt(0) == '[') return elemAssignable(to.substring(1), from.substring(1));
            return to.equals("java/lang/Cloneable") || to.equals("java/io/Serializable");
        }
        if (to.charAt(0) == '[') return false;
        if (!h.exists(to)) throw err("cannot load class " + to);
        if (h.isInterface(to)) return true;
        String c = from;
        while (c != null) {
            if (c.equals(to)) return true;
            if (!h.exists(c)) throw err("cannot load class " + c);
            c = h.superName(c);
        }
        return false;
    }

    private boolean elemAssignable(String to, String from) {
        char a = to.charAt(0), b = from.charAt(0);
        if (a == '[' && b == '[') return elemAssignable(to.substring(1), from.substring(1));
        if (a == 'L' && b == 'L') return classAssignable(to.substring(1, to.length() - 1), from.substring(1, from.length() - 1));
        if (a == 'L' && b == '[') return to.equals("Ljava/lang/Object;");
        return to.equals(from);
    }

    boolean assignable(VType to, VType from) {
        switch (to.kind) {
            case VType.TOP: return true;
            case VType.OBJECT:
                if (from.kind == VType.NULL) return true;
                return from.kind == VType.OBJECT && classAssignable(to.name, from.name);
            case VType.UNINIT: return from.kind == VType.UNINIT && from.off == to.off;
            default: return from.kind == to.kind;
        }
    }

    // ---------------------------------------------------------------- drivers

    private void runStrict() {
        Frame cur = initialFrame();
        for (pc = 0; pc < code.length; pc += insnLen[pc]) {
            Frame d = declared[pc];
            if (cur == null) {
                if (d == null) throw err("no StackMap entry after an unconditional branch/return");
                cur = d.copy();
            } else if (d != null) {
                checkFlow(cur, d, pc);
                cur = d.copy();
            }
            at(cur);
        }
        if (cur != null) throw err(code.length, "falls off the end of the code");
    }

    // processes instruction at pc given state; sets cur to the successor (loop in runStrict continues)
    private Frame curNext;
    private void at(Frame cur) { /* replaced below */ }

    private void runInfer() { throw err(-1, "not implemented"); }
    private void checkFlow(Frame a, Frame b, int where) { throw err(-1, "not implemented"); }
}

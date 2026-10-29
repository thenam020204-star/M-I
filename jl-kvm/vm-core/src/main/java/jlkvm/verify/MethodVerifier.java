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
 * Deviations/limits are documented in docs/verifier-report.md (protected access, uninit locals under handlers,
 * subroutines: jsr/ret are rejected, not inferred).
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
    private Frame[] states;        // from data flow (lenient inference)
    private boolean inferMode;
    private boolean[] queued;
    private int[] work; private int workN;
    private VType[] catchTypes;

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
                        if (a + 12 > code.length) throw err(p, "truncated switch");
                        long lo = s4(a + 4), hi = s4(a + 8);
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
        ExceptionEntry[] et = ca.exceptionTable;
        catchTypes = new VType[et.length];
        for (int i = 0; i < et.length; i++) {
            ExceptionEntry e = et[i];
            if (!isStart[e.startPc] || !isStart[e.endPc] || !isStart[e.handlerPc] || e.handlerPc >= code.length)
                throw err(-1, "exception table entry does not point at instruction boundaries");
            if (e.catchType == 0) {
                catchTypes[i] = THROWABLE_T;
            } else {
                catchTypes[i] = VType.object(cp.className(e.catchType));
                if (!assignable(THROWABLE_T, catchTypes[i])) throw err(-1, "catch type " + catchTypes[i] + " is not a Throwable");
            }
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
            return false; // CLDC 1.1 Appendix1 (isJavaAssignable, isArrayInterface has no clauses): arrays are subtypes of Object only
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

    private static final VType OBJECT_T = VType.object("java/lang/Object");
    private static final VType THROWABLE_T = VType.object("java/lang/Throwable");
    private static final VType STRING_T = VType.object("java/lang/String");
    private static final VType CLASS_T = VType.object("java/lang/Class");

    private void flows(Frame from, Frame to, int t) {
        if (from.sp != to.sp) throw err("stack depth " + from.sp + " does not match the frame at " + t + " (" + to.sp + ")");
        for (int i = 0; i < to.sp; i++)
            if (!assignable(to.stack[i], from.stack[i]))
                throw err("stack[" + i + "] " + from.stack[i] + " is not assignable to " + to.stack[i] + " of the frame at " + t);
        for (int i = 0; i < to.locals.length; i++)
            if (!assignable(to.locals[i], from.locals[i]))
                throw err("local " + i + " " + from.locals[i] + " is not assignable to " + to.locals[i] + " of the frame at " + t);
    }

    private void runStrict() {
        f = initialFrame();
        boolean live = true;
        for (pc = 0; pc < code.length; pc += insnLen[pc]) {
            Frame d = declared[pc];
            if (!live) {
                if (d == null) throw err("no StackMap entry for code following an unconditional branch/return/throw");
                f = d.copy();
                live = true;
            } else if (d != null) {
                flows(f, d, pc);
                f = d.copy();
            }
            if (recorded != null) recorded[pc] = f.copy();
            checkHandlersStrict();
            live = exec();
        }
        if (live) throw err(code.length, "falls off the end of the code");
    }

    private void checkHandlersStrict() {
        ExceptionEntry[] et = ca.exceptionTable;
        for (int i = 0; i < et.length; i++) {
            ExceptionEntry e = et[i];
            if (pc < e.startPc || pc >= e.endPc) continue;
            Frame d = declared[e.handlerPc];
            if (d == null) throw err("exception handler at " + e.handlerPc + " has no StackMap entry");
            if (d.sp != 1) throw err("StackMap frame of handler " + e.handlerPc + " must have exactly one stack item");
            if (!assignable(d.stack[0], catchTypes[i]))
                throw err("caught " + catchTypes[i] + " is not assignable to " + d.stack[0] + " of the handler frame at " + e.handlerPc);
            for (int k = 0; k < d.locals.length; k++)
                if (!assignable(d.locals[k], f.locals[k]))
                    throw err("local " + k + " " + f.locals[k] + " is not assignable to " + d.locals[k] + " of the handler frame at " + e.handlerPc);
        }
    }

    // ---- inference (lenient)

    private void enqueue(int t) {
        if (!queued[t]) { queued[t] = true; work[workN++] = t; }
    }

    private void mergeInto(int t, Frame src) {
        Frame cur = states[t];
        if (cur == null) { states[t] = src.copy(); enqueue(t); return; }
        if (cur.sp != src.sp) throw err("stack depth " + src.sp + " differs from " + cur.sp + " at merge point " + t);
        boolean ch = false;
        for (int i = 0; i < cur.locals.length; i++) {
            VType m = merge(cur.locals[i], src.locals[i]);
            if (!m.equals(cur.locals[i])) { cur.locals[i] = m; ch = true; }
        }
        for (int i = 0; i < cur.sp; i++) {
            VType m = merge(cur.stack[i], src.stack[i]);
            if (m.kind == VType.TOP) throw err("incompatible stack item " + i + " at merge point " + t + ": " + cur.stack[i] + " / " + src.stack[i]);
            if (!m.equals(cur.stack[i])) { cur.stack[i] = m; ch = true; }
        }
        if (ch) enqueue(t);
    }

    private VType merge(VType a, VType b) {
        if (a.equals(b)) return a;
        if (a.kind == VType.NULL && b.kind == VType.OBJECT) return b;
        if (b.kind == VType.NULL && a.kind == VType.OBJECT) return a;
        if (a.kind == VType.OBJECT && b.kind == VType.OBJECT) return VType.object(commonSuper(a.name, b.name));
        return VType.T_TOP;
    }

    private String commonSuper(String a, String b) {
        if (a.equals(b)) return a;
        if (classAssignable(a, b)) return a;
        if (classAssignable(b, a)) return b;
        boolean aa = a.charAt(0) == '[', ba = b.charAt(0) == '[';
        if (aa && ba) {
            char ea = a.charAt(1), eb = b.charAt(1);
            if ((ea == 'L' || ea == '[') && (eb == 'L' || eb == '[')) {
                String r = commonSuper(elemName(a.substring(1)), elemName(b.substring(1)));
                return "[" + (r.charAt(0) == '[' ? r : "L" + r + ";");
            }
            return "java/lang/Object";
        }
        if (aa || ba) return "java/lang/Object";
        if (h.isInterface(a) || h.isInterface(b)) return "java/lang/Object";
        java.util.HashSet<String> sup = new java.util.HashSet<String>();
        for (String c = a; c != null; c = h.superName(c)) sup.add(c);
        for (String c = b; c != null; c = h.superName(c)) if (sup.contains(c)) return c;
        return "java/lang/Object";
    }

    private static String elemName(String d) { return d.charAt(0) == 'L' ? d.substring(1, d.length() - 1) : d; }

    private void runInfer() {
        inferMode = true;
        states = new Frame[code.length];
        queued = new boolean[code.length];
        work = new int[code.length * 4 + 16];
        workN = 0;
        states[0] = initialFrame();
        enqueue(0);
        long budget = 200L * code.length + 10000;
        while (workN > 0) {
            if (--budget < 0) throw err(-1, "type inference does not converge");
            int p = work[--workN];
            queued[p] = false;
            pc = p;
            f = states[p].copy();
            ExceptionEntry[] et = ca.exceptionTable;
            for (int i = 0; i < et.length; i++) {
                ExceptionEntry e = et[i];
                if (p < e.startPc || p >= e.endPc) continue;
                Frame hf = new Frame(ca.maxLocals, ca.maxStack);
                System.arraycopy(f.locals, 0, hf.locals, 0, f.locals.length);
                if (ca.maxStack < 1) throw err("max_stack too small for an exception handler");
                hf.stack[0] = catchTypes[i];
                hf.sp = 1;
                mergeInto(e.handlerPc, hf);
            }
            if (exec()) {
                int n = p + insnLen[p];
                if (n >= code.length) throw err(code.length, "falls off the end of the code");
                mergeInto(n, f);
            }
        }
        if (recorded != null) for (int i = 0; i < code.length; i++) recorded[i] = states[i];
    }

    // ---------------------------------------------------------------- operand helpers

    private VType pop() {
        if (f.sp <= 0) throw err("operand stack underflow");
        return f.stack[--f.sp];
    }
    private void push(VType t) {
        if (f.sp >= f.stack.length) throw err("operand stack overflow (exceeds max_stack)");
        f.stack[f.sp++] = t;
    }
    private void pushWide(boolean isLong) {
        push(isLong ? VType.T_LONG : VType.T_DOUBLE);
        push(isLong ? VType.T_LONG2 : VType.T_DOUBLE2);
    }
    private void popKind(int k, String what) {
        VType t = pop();
        if (t.kind != k) throw err("expected " + what + " on the stack but found " + t);
    }
    private void popWide(int first, int second, String what) {
        VType b = pop(), a = pop();
        if (a.kind != first || b.kind != second) throw err("expected " + what + " on the stack but found " + a + "," + b);
    }
    private void popInt() { popKind(VType.INT, "int"); }
    private void popLong() { popWide(VType.LONG, VType.LONG2, "long"); }
    private void popFloat() { popKind(VType.FLOAT, "float"); }
    private void popDouble() { popWide(VType.DOUBLE, VType.DOUBLE2, "double"); }
    private VType popRef() {
        VType t = pop();
        if (t.kind != VType.NULL && t.kind != VType.OBJECT) throw err("expected a reference on the stack but found " + t);
        return t;
    }
    private void popK(char k) {
        switch (k) {
            case 'I': popInt(); break;
            case 'J': popLong(); break;
            case 'F': popFloat(); break;
            case 'D': popDouble(); break;
            default: popRef();
        }
    }
    private void pushK(char k) {
        switch (k) {
            case 'I': push(VType.T_INT); break;
            case 'J': pushWide(true); break;
            case 'F': push(VType.T_FLOAT); break;
            case 'D': pushWide(false); break;
            default: throw err("internal: bad kind");
        }
    }
    private void popTyped(VType want) {
        switch (want.kind) {
            case VType.LONG: popLong(); break;
            case VType.DOUBLE: popDouble(); break;
            case VType.OBJECT: {
                VType t = pop();
                if (!assignable(want, t)) throw err("expected " + want + " but found " + t);
                break;
            }
            default: popKind(want.kind, want.toString());
        }
    }
    private void pushTyped(VType t) {
        if (t.kind == VType.LONG) pushWide(true);
        else if (t.kind == VType.DOUBLE) pushWide(false);
        else push(t);
    }
    private static boolean c1(VType t) {
        switch (t.kind) {
            case VType.INT: case VType.FLOAT: case VType.NULL: case VType.UNINIT_THIS: case VType.OBJECT: case VType.UNINIT: return true;
            default: return false;
        }
    }
    private static boolean pair(VType lo, VType hi) {
        return (lo.kind == VType.LONG && hi.kind == VType.LONG2) || (lo.kind == VType.DOUBLE && hi.kind == VType.DOUBLE2);
    }
    private static boolean grp(VType lo, VType hi) { return (c1(lo) && c1(hi)) || pair(lo, hi); }

    private void needLocal(int n, int w) {
        if (n < 0 || n + w > f.locals.length) throw err("local variable index " + n + " out of range (max_locals " + f.locals.length + ")");
    }
    private void load(int n, char k) {
        switch (k) {
            case 'I': needLocal(n, 1); if (f.locals[n].kind != VType.INT) throw err("local " + n + " is " + f.locals[n] + ", not int"); push(VType.T_INT); break;
            case 'F': needLocal(n, 1); if (f.locals[n].kind != VType.FLOAT) throw err("local " + n + " is " + f.locals[n] + ", not float"); push(VType.T_FLOAT); break;
            case 'J': needLocal(n, 2);
                if (f.locals[n].kind != VType.LONG || f.locals[n + 1].kind != VType.LONG2) throw err("local " + n + " is " + f.locals[n] + ", not long");
                pushWide(true); break;
            case 'D': needLocal(n, 2);
                if (f.locals[n].kind != VType.DOUBLE || f.locals[n + 1].kind != VType.DOUBLE2) throw err("local " + n + " is " + f.locals[n] + ", not double");
                pushWide(false); break;
            default: needLocal(n, 1);
                if (!f.locals[n].isRef()) throw err("local " + n + " is " + f.locals[n] + ", not a reference");
                push(f.locals[n]);
        }
    }
    private void clearFor(int k) {
        VType[] l = f.locals;
        if (k > 0 && (l[k - 1].kind == VType.LONG || l[k - 1].kind == VType.DOUBLE)) l[k - 1] = VType.T_TOP;
        if ((l[k].kind == VType.LONG || l[k].kind == VType.DOUBLE) && k + 1 < l.length) l[k + 1] = VType.T_TOP;
    }
    private void store(int n, char k) {
        switch (k) {
            case 'I': needLocal(n, 1); popInt(); clearFor(n); f.locals[n] = VType.T_INT; break;
            case 'F': needLocal(n, 1); popFloat(); clearFor(n); f.locals[n] = VType.T_FLOAT; break;
            case 'J': needLocal(n, 2); popLong(); clearFor(n); clearFor(n + 1); f.locals[n] = VType.T_LONG; f.locals[n + 1] = VType.T_LONG2; break;
            case 'D': needLocal(n, 2); popDouble(); clearFor(n); clearFor(n + 1); f.locals[n] = VType.T_DOUBLE; f.locals[n + 1] = VType.T_DOUBLE2; break;
            default: {
                needLocal(n, 1);
                VType t = pop();
                if (!t.isRef()) throw err("astore of " + t);
                clearFor(n);
                f.locals[n] = t;
            }
        }
    }

    private boolean hasUninit() {
        for (int i = 0; i < f.sp; i++) if (f.stack[i].kind == VType.UNINIT || f.stack[i].kind == VType.UNINIT_THIS) return true;
        for (VType t : f.locals) if (t.kind == VType.UNINIT || t.kind == VType.UNINIT_THIS) return true;
        return false;
    }

    private void jump(int t) {
        if (t < 0 || t >= code.length || !isStart[t]) throw err("branch target " + t + " is not an instruction boundary");
        if (t <= pc && hasUninit()) throw err("uninitialized object on a backward branch");
        if (inferMode) {
            mergeInto(t, f);
        } else {
            Frame d = declared[t];
            if (d == null) throw err("branch target " + t + " has no StackMap entry");
            flows(f, d, t);
        }
    }

    /** Array operand check: null or an array whose descriptor equals one of {@code ok}. */
    private void popArray(String ok1, String ok2) {
        VType a = pop();
        if (a.kind == VType.NULL) return;
        if (a.kind != VType.OBJECT || !(a.name.equals(ok1) || a.name.equals(ok2))) throw err("expected array " + ok1 + " but found " + a);
    }

    private VType refArrayElem(VType a) {
        if (a.kind == VType.NULL) return VType.T_NULL;
        if (a.kind != VType.OBJECT || !a.isArray() || (a.name.charAt(1) != 'L' && a.name.charAt(1) != '['))
            throw err("expected a reference array but found " + a);
        return VType.object(a.name.charAt(1) == 'L' ? a.name.substring(2, a.name.length() - 1) : a.name.substring(1));
    }

    private int memberIndex(int expectTag, int alt) {
        int idx = u2(pc + 1);
        int tag = cp.tag(idx);
        if (tag != expectTag && tag != alt) throw err("constant pool entry " + idx + " has the wrong type for " + (code[pc] & 0xff));
        return idx;
    }

    private static boolean isFloatDesc(String d) {
        int i = 0;
        while (i < d.length() && d.charAt(i) == '[') i++;
        return i < d.length() && (d.charAt(i) == 'F' || d.charAt(i) == 'D');
    }

    private void checkIntReturnType(String r) {
        if (r.length() != 1 || "BCSZI".indexOf(r.charAt(0)) < 0) throw err("method does not return int");
    }

    // ---------------------------------------------------------------- the instruction set

    private static final char[] KIND4 = {'I', 'J', 'F', 'D'};
    private static final char[] KIND5 = {'I', 'J', 'F', 'D', 'A'};
    private static final String[] CONV = {"IJ", "IF", "ID", "JI", "JF", "JD", "FI", "FJ", "FD", "DI", "DJ", "DF", "II", "II", "II"};

    /** Executes the instruction at {@code pc} on {@code f}; returns true if control can fall through to the next one. */
    private boolean exec() {
        int op = code[pc] & 0xff;
        if (op == 0) return true;
        if (op <= 20) {
            switch (op) {
                case 1: push(VType.T_NULL); break;
                case 9: case 10: pushWide(true); break;
                case 11: case 12: case 13: push(VType.T_FLOAT); break;
                case 14: case 15: pushWide(false); break;
                case 18: case 19: {
                    int idx = op == 18 ? code[pc + 1] & 0xff : u2(pc + 1);
                    switch (cp.tag(idx)) {
                        case ConstantPool.INTEGER: push(VType.T_INT); break;
                        case ConstantPool.FLOAT:
                            if (!allowFloat) throw err("floating point constant not allowed in CLDC 1.0");
                            push(VType.T_FLOAT); break;
                        case ConstantPool.STRING: push(STRING_T); break;
                        case ConstantPool.CLASS:
                            if (cf.major < 49) throw err("ldc of a class constant requires class file version 49+ (have " + cf.major + ")");
                            push(CLASS_T); break;
                        default: throw err("ldc of an unsupported constant pool entry " + idx);
                    }
                    break;
                }
                case 20: {
                    int idx = u2(pc + 1);
                    int tg = cp.tag(idx);
                    if (tg == ConstantPool.LONG) pushWide(true);
                    else if (tg == ConstantPool.DOUBLE) pushWide(false);
                    else throw err("ldc2_w of a non long/double constant");
                    break;
                }
                default: push(VType.T_INT);   // iconst_*, bipush, sipush
            }
            return true;
        }
        if (op <= 45) {
            if (op <= 25) load(code[pc + 1] & 0xff, KIND5[op - 21]);
            else load((op - 26) & 3, KIND5[(op - 26) >> 2]);
            return true;
        }
        if (op <= 53) { aload(op); return true; }
        if (op <= 78) {
            if (op <= 58) store(code[pc + 1] & 0xff, KIND5[op - 54]);
            else store((op - 59) & 3, KIND5[(op - 59) >> 2]);
            return true;
        }
        if (op <= 86) { astore(op); return true; }
        if (op <= 95) { stackOp(op); return true; }
        if (op <= 119) {
            char k = KIND4[(op - 96) & 3];
            if (op >= 116) { popK(k); pushK(k); }
            else { popK(k); popK(k); pushK(k); }
            return true;
        }
        if (op <= 125) {
            char k = (op & 1) == 0 ? 'I' : 'J';
            popInt(); popK(k); pushK(k);
            return true;
        }
        if (op <= 131) {
            char k = (op & 1) == 0 ? 'I' : 'J';
            popK(k); popK(k); pushK(k);
            return true;
        }
        if (op == 132) {
            int n = code[pc + 1] & 0xff;
            needLocal(n, 1);
            if (f.locals[n].kind != VType.INT) throw err("iinc on non-int local " + n);
            return true;
        }
        if (op <= 147) {
            String c = CONV[op - 133];
            popK(c.charAt(0)); pushK(c.charAt(1));
            return true;
        }
        if (op <= 152) {
            char k = op == 148 ? 'J' : (op <= 150 ? 'F' : 'D');
            popK(k); popK(k); push(VType.T_INT);
            return true;
        }
        if (op <= 158) { popInt(); jump(pc + s2(pc + 1)); return true; }
        if (op <= 164) { popInt(); popInt(); jump(pc + s2(pc + 1)); return true; }
        if (op <= 166) { popRef(); popRef(); jump(pc + s2(pc + 1)); return true; }
        if (op == 167) { jump(pc + s2(pc + 1)); return false; }
        if (op == 170 || op == 171) { doSwitch(op); return false; }
        if (op <= 177) { doReturn(op); return false; }
        if (op <= 181) { fieldOp(op); return true; }
        if (op <= 185) { invoke(op); return true; }
        switch (op) {
            case 187: {
                String n = cp.className(u2(pc + 1));
                if (n.charAt(0) == '[') throw err("new of an array class");
                VType u = VType.uninit(pc);
                for (int i = 0; i < f.sp; i++) if (f.stack[i].equals(u)) throw err("uninitialized object from this new is still live");
                for (VType t : f.locals) if (t.equals(u)) throw err("uninitialized object from this new is still live");
                push(u);
                break;
            }
            case 188: {
                popInt();
                String t;
                switch (code[pc + 1] & 0xff) {
                    case 4: t = "[Z"; break; case 5: t = "[C"; break; case 6: t = "[F"; break; case 7: t = "[D"; break;
                    case 8: t = "[B"; break; case 9: t = "[S"; break; case 10: t = "[I"; break; case 11: t = "[J"; break;
                    default: throw err("bad newarray type " + (code[pc + 1] & 0xff));
                }
                if (!allowFloat && (t.equals("[F") || t.equals("[D"))) throw err("floating point array not allowed in CLDC 1.0");
                push(VType.object(t));
                break;
            }
            case 189: {
                popInt();
                String n = cp.className(u2(pc + 1));
                push(VType.object("[" + (n.charAt(0) == '[' ? n : "L" + n + ";")));
                break;
            }
            case 190: {
                VType a = pop();
                if (a.kind != VType.NULL && !(a.kind == VType.OBJECT && a.isArray())) throw err("arraylength of " + a);
                push(VType.T_INT);
                break;
            }
            case 191: {
                VType t = popRef();
                if (!assignable(THROWABLE_T, t)) throw err("athrow of non-Throwable " + t);
                return false;
            }
            case 192: {
                String n = cp.className(u2(pc + 1));
                popRef();
                push(VType.object(n));
                break;
            }
            case 193: cp.className(u2(pc + 1)); popRef(); push(VType.T_INT); break;
            case 194: case 195: popRef(); break;
            case 196: wide(); break;
            case 197: {
                String n = cp.className(u2(pc + 1));
                int dims = code[pc + 3] & 0xff;
                int d = 0;
                while (d < n.length() && n.charAt(d) == '[') d++;
                if (dims < 1 || dims > d) throw err("multianewarray dimensions " + dims + " do not fit " + n);
                for (int i = 0; i < dims; i++) popInt();
                push(VType.object(n));
                break;
            }
            case 198: case 199: popRef(); jump(pc + s2(pc + 1)); break;
            case 200: jump(pc + s4(pc + 1)); return false;
            default: throw err("illegal opcode " + op);
        }
        return true;
    }

    private void aload(int op) {
        switch (op) {
            case 46: popInt(); popArray("[I", "[I"); push(VType.T_INT); break;
            case 47: popInt(); popArray("[J", "[J"); pushWide(true); break;
            case 48: popInt(); popArray("[F", "[F"); push(VType.T_FLOAT); break;
            case 49: popInt(); popArray("[D", "[D"); pushWide(false); break;
            case 50: popInt(); push(refArrayElem(pop())); break;
            case 51: popInt(); popArray("[B", "[Z"); push(VType.T_INT); break;
            case 52: popInt(); popArray("[C", "[C"); push(VType.T_INT); break;
            default: popInt(); popArray("[S", "[S"); push(VType.T_INT);
        }
    }

    private void astore(int op) {
        switch (op) {
            case 79: popInt(); popInt(); popArray("[I", "[I"); break;
            case 80: popLong(); popInt(); popArray("[J", "[J"); break;
            case 81: popFloat(); popInt(); popArray("[F", "[F"); break;
            case 82: popDouble(); popInt(); popArray("[D", "[D"); break;
            case 83: popRef(); popInt(); refArrayElem(pop()); break;
            case 84: popInt(); popInt(); popArray("[B", "[Z"); break;
            case 85: popInt(); popInt(); popArray("[C", "[C"); break;
            default: popInt(); popInt(); popArray("[S", "[S");
        }
    }

    private void stackOp(int op) {
        VType v1, v2, v3, v4;
        switch (op) {
            case 87: if (!c1(v1 = pop())) throw err("pop of " + v1); break;
            case 88: v1 = pop(); v2 = pop(); if (!grp(v2, v1)) throw err("pop2 would split a long/double"); break;
            case 89: v1 = pop(); if (!c1(v1)) throw err("dup of " + v1); push(v1); push(v1); break;
            case 90: v1 = pop(); v2 = pop();
                if (!c1(v1) || !c1(v2)) throw err("dup_x1 on " + v2 + "," + v1);
                push(v1); push(v2); push(v1); break;
            case 91: v1 = pop(); v2 = pop(); v3 = pop();
                if (!c1(v1) || !grp(v3, v2)) throw err("dup_x2 on " + v3 + "," + v2 + "," + v1);
                push(v1); push(v3); push(v2); push(v1); break;
            case 92: v1 = pop(); v2 = pop();
                if (!grp(v2, v1)) throw err("dup2 would split a long/double");
                push(v2); push(v1); push(v2); push(v1); break;
            case 93: v1 = pop(); v2 = pop(); v3 = pop();
                if (!grp(v2, v1) || !c1(v3)) throw err("dup2_x1 on " + v3 + "," + v2 + "," + v1);
                push(v2); push(v1); push(v3); push(v2); push(v1); break;
            case 94: v1 = pop(); v2 = pop(); v3 = pop(); v4 = pop();
                if (!grp(v2, v1) || !grp(v4, v3)) throw err("dup2_x2 would split a long/double");
                push(v2); push(v1); push(v4); push(v3); push(v2); push(v1); break;
            default: v1 = pop(); v2 = pop();
                if (!c1(v1) || !c1(v2)) throw err("swap on " + v2 + "," + v1);
                push(v1); push(v2);
        }
    }

    private void wide() {
        int sub = code[pc + 1] & 0xff;
        int n = u2(pc + 2);
        if (sub == 132) {
            needLocal(n, 1);
            if (f.locals[n].kind != VType.INT) throw err("iinc on non-int local " + n);
        } else if (sub <= 25) {
            load(n, KIND5[sub - 21]);
        } else {
            store(n, KIND5[sub - 54]);
        }
    }

    private void doSwitch(int op) {
        popInt();
        int a = (pc + 4) & ~3;
        jump(pc + s4(a));
        if (op == 170) {
            int lo = s4(a + 4), hi = s4(a + 8);
            for (int i = 0; i <= hi - lo; i++) jump(pc + s4(a + 12 + 4 * i));
        } else {
            int n = s4(a + 4);
            int prev = 0;
            for (int i = 0; i < n; i++) {
                int key = s4(a + 8 + 8 * i);
                if (i > 0 && key <= prev) throw err("lookupswitch keys are not sorted");
                prev = key;
                jump(pc + s4(a + 12 + 8 * i));
            }
        }
    }

    private void doReturn(int op) {
        String r = Descriptors.returnType(m.descriptor);
        switch (op) {
            case 172: checkIntReturnType(r); popInt(); break;
            case 173: if (!r.equals("J")) throw err("lreturn in a method not returning long"); popLong(); break;
            case 174: if (!r.equals("F")) throw err("freturn in a method not returning float"); popFloat(); break;
            case 175: if (!r.equals("D")) throw err("dreturn in a method not returning double"); popDouble(); break;
            case 176: {
                if (r.charAt(0) != 'L' && r.charAt(0) != '[') throw err("areturn in a method not returning a reference");
                VType t = pop();
                if (!assignable(typeOf(r), t)) throw err("returning " + t + " from a method returning " + r);
                break;
            }
            default:
                if (!r.equals("V")) throw err("return in a method not returning void");
                if (m.name.equals("<init>") && f.locals.length > 0 && f.locals[0].kind == VType.UNINIT_THIS)
                    throw err("return from <init> before the superclass constructor was called");
        }
    }

    private void fieldOp(int op) {
        int idx = memberIndex(ConstantPool.FIELDREF, ConstantPool.FIELDREF);
        String cn = cp.refClass(idx);
        VType t = typeOf(cp.refDescriptor(idx));
        if (!allowFloat && (t.kind == VType.FLOAT || t.kind == VType.DOUBLE)) throw err("floating point field not allowed in CLDC 1.0");
        switch (op) {
            case 178: pushTyped(t); break;
            case 179: popTyped(t); break;
            case 180: popTyped(VType.object(cn)); pushTyped(t); break;
            default: {
                popTyped(t);
                VType r = pop();
                boolean ok = r.kind == VType.UNINIT_THIS ? cn.equals(cf.name) : assignable(VType.object(cn), r);
                if (!ok) throw err("putfield receiver " + r + " is not a " + cn);
            }
        }
    }

    private void invoke(int op) {
        int idx = memberIndex(op == 185 ? ConstantPool.IMETHODREF : ConstantPool.METHODREF, op == 185 ? ConstantPool.IMETHODREF : ConstantPool.METHODREF);
        String cn = cp.refClass(idx), name = cp.refName(idx), desc = cp.refDescriptor(idx);
        if (!Descriptors.isValidMethod(desc)) throw err("bad method descriptor " + desc);
        if (name.charAt(0) == '<' && !(name.equals("<init>") && op == 183)) throw err("illegal call of " + name);
        java.util.List<String> ps = Descriptors.params(desc);
        if (op == 185) {
            if ((code[pc + 3] & 0xff) != Descriptors.paramWords(desc) + 1 || code[pc + 4] != 0) throw err("invokeinterface count/zero operand is wrong");
        }
        for (int i = ps.size() - 1; i >= 0; i--) popTyped(typeOf(ps.get(i)));
        String ret = Descriptors.returnType(desc);
        if (!allowFloat) {
            boolean fp = isFloatDesc(ret);
            for (String p : ps) fp |= isFloatDesc(p);
            if (fp) throw err("floating point method descriptor not allowed in CLDC 1.0");
        }
        if (op != 184) {
            if (name.equals("<init>")) {
                if (!ret.equals("V")) throw err("<init> must return void");
                VType u = pop();
                VType init;
                if (u.kind == VType.UNINIT) {
                    if ((code[u.off] & 0xff) != 187 || !cp.className(u2(u.off + 1)).equals(cn))
                        throw err("<init> of " + cn + " on an object created as a different class");
                    init = VType.object(cn);
                } else if (u.kind == VType.UNINIT_THIS) {
                    if (!(cn.equals(cf.name) || cn.equals(cf.superName))) throw err("<init> of " + cn + " is not this class or its superclass");
                    init = VType.object(cf.name);
                } else {
                    throw err("<init> called on an already initialized object " + u);
                }
                for (int i = 0; i < f.sp; i++) if (f.stack[i].equals(u)) f.stack[i] = init;
                for (int i = 0; i < f.locals.length; i++) if (f.locals[i].equals(u)) f.locals[i] = init;
                return;
            }
            VType r = pop();
            VType want = VType.object(op == 183 ? cf.name : cn);
            if (!assignable(want, r)) throw err("receiver " + r + " is not a " + want.name);
            if (op == 183 && !cn.equals(cf.name) && !classAssignable(cn, cf.name)) throw err("invokespecial of " + cn + ", not a superclass of " + cf.name);
        }
        if (!ret.equals("V")) pushTyped(typeOf(ret));
    }
}

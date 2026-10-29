package jlkvm.interp;

import jlkvm.Vm;
import jlkvm.classfile.ClassFile;
import jlkvm.classfile.ConstantPool;
import jlkvm.heap.Heap;
import jlkvm.link.ClassInfo;
import jlkvm.link.Errors;
import jlkvm.link.Linker;
import jlkvm.link.RField;
import jlkvm.link.RMethod;
import jlkvm.link.VmThrow;
import jlkvm.thread.Monitors;
import jlkvm.thread.VmThread;

/**
 * Bytecode interpreter (PLAN T11): every JVM opcode 0..201 (jsr/ret, wide, switches, multianewarray, monitors included).
 * Registers live in locals; they are written back to the {@link VmThread} before anything that can throw, block or
 * switch (so stack traces and the collector see them). Rare transitions (class initialisation, thread switch, native-pushed
 * frames) leave the hot loop through {@code break inner} with a "slow" code.
 */
public final class Interpreter {
    private static final int S_NONE = 0, S_INIT = 1, S_SWITCH = 2, S_NFRAME = 3;
    private static final int CLASS_MASK = Heap.CLASS_MASK;
    /** D15 debug hook (off in release): -Djlkvm.trace=true prints every executed instruction to stderr. */
    private static final boolean TRACE = Boolean.getBoolean("jlkvm.trace");
    /** D15 debug hook: -Djlkvm.opstats=true counts executed opcodes in {@link #OPSTATS} (coverage of the opcode set). */
    private static final boolean STATS = Boolean.getBoolean("jlkvm.opstats");
    public static final long[] OPSTATS = new long[256];

    private final Vm vm;
    private final Heap heap;
    private final int[] mem;
    private final Linker lk;
    private final Monitors mons;
    private final NativeCtx ctx;
    private final int[] primArr = new int[10];
    private ClassInfo errorClass;

    public Interpreter(Vm vm) {
        this.vm = vm;
        this.heap = vm.heap;
        this.mem = vm.heap.mem;
        this.lk = vm.linker;
        this.mons = vm.monitors;
        this.ctx = new NativeCtx(vm);
    }

    public NativeCtx nativeCtx() { return ctx; }

    // ------------------------------------------------------------------ helpers

    private static long getL(int[] st, int i) { return ((long) st[i] << 32) | (st[i + 1] & 0xFFFFFFFFL); }
    private static void setL(int[] st, int i, long v) { st[i] = (int) (v >>> 32); st[i + 1] = (int) v; }
    private static float getF(int[] st, int i) { return Float.intBitsToFloat(st[i]); }
    private static void setF(int[] st, int i, float v) { st[i] = Float.floatToRawIntBits(v); }
    private static double getD(int[] st, int i) { return Double.longBitsToDouble(getL(st, i)); }
    private static void setD(int[] st, int i, double v) { setL(st, i, Double.doubleToRawLongBits(v)); }
    private static int s16(byte[] c, int p) { return (short) (((c[p] & 0xff) << 8) | (c[p + 1] & 0xff)); }
    private static int u16(byte[] c, int p) { return ((c[p] & 0xff) << 8) | (c[p + 1] & 0xff); }
    private static int s32(byte[] c, int p) { return ((c[p] & 0xff) << 24) | ((c[p + 1] & 0xff) << 16) | ((c[p + 2] & 0xff) << 8) | (c[p + 3] & 0xff); }

    private static VmThrow npe() { return new VmThrow("java/lang/NullPointerException", null); }
    private static VmThrow aioobe(int i) { return new VmThrow("java/lang/ArrayIndexOutOfBoundsException", "Array index out of range: " + i); }
    private static VmThrow icce(String m) { return Errors.error("java/lang/IncompatibleClassChangeError", m); }
    private static VmThrow oom(String m) { return new VmThrow("java/lang/OutOfMemoryError", m); }

    private static boolean needsInit(ClassInfo c, VmThread th) {
        return c.state != ClassInfo.INITIALIZED && !(c.state == ClassInfo.INITIALIZING && c.initThread == th.id);
    }

    private ClassInfo classOf(int ref) { return lk.byId(mem[ref] & CLASS_MASK); }

    private ClassInfo primArray(int kind) {
        ClassInfo c = lk.primitiveArray(kind);
        return c;
    }

    private int allocArray(ClassInfo ac, int len) {
        if (len < 0) throw new VmThrow("java/lang/NegativeArraySizeException", String.valueOf(len));
        int r = heap.allocArray(ac.id, ac.elemKind, len);
        if (r == 0) throw vm.oomThrow("array of " + len);
        return r;
    }

    private int multi(ClassInfo ac, int[] counts, int d) {
        int arr = allocArray(ac, counts[d]);
        if (d + 1 < counts.length) {
            int mark = vm.roots.mark();
            int ri = vm.roots.push(arr);
            ClassInfo comp = ac.component;
            for (int i = 0; i < counts[d]; i++) {
                int sub = multi(comp, counts, d + 1);
                heap.putRefElem(vm.roots.get(ri), i, sub);
            }
            arr = vm.roots.get(ri);
            vm.roots.release(mark);
        }
        return arr;
    }

    // ------------------------------------------------------------------ main loop

    /** Runs {@code th} for at most {@code slice} bytecodes. @return bytecodes executed (>= 0). */
    public int run(VmThread th, int slice) {
        final int[] st = th.stack;
        final int[] mem = this.mem;
        int fp = th.fp, sp = th.sp, pc = th.pc;
        RMethod m = th.method;
        byte[] code = m.code;
        ClassInfo cls = m.owner;
        int budget = slice, ipc = pc, slow = S_NONE;
        ClassInfo slowCls = null;
        RMethod nframe = null;
        int nframeArg = 0;

        for (;;) {
            try {
                if (th.pendingThrow != null) {
                    String c = th.pendingThrow;
                    th.pendingThrow = null;
                    ipc = th.excPc;
                    throw new VmThrow(c, null);
                }
                if (th.initFirst != null) { slowCls = th.initFirst; th.initFirst = null; slow = S_INIT; ipc = pc; }
                if (slow == S_NONE) inner:
                for (;;) {
                    if (budget-- <= 0) { slow = S_SWITCH; break inner; }
                    ipc = pc;
                    int op = code[pc++] & 0xff;
                    if (STATS) OPSTATS[op]++;
                    if (TRACE) System.err.println("T" + th.id + " " + m + " @" + ipc + " op=" + op + " sp=" + (sp - fp));
                    switch (op) {
                    case 0: break; // nop
                    case 1: st[sp++] = 0; break;
                    case 2: case 3: case 4: case 5: case 6: case 7: case 8: st[sp++] = op - 3; break;
                    case 9: case 10: st[sp++] = 0; st[sp++] = op - 9; break;
                    case 11: case 12: case 13: setF(st, sp++, op - 11); break;
                    case 14: case 15: setD(st, sp, op - 14); sp += 2; break;
                    case 16: st[sp++] = code[pc++]; break;
                    case 17: st[sp++] = s16(code, pc); pc += 2; break;
                    case 18: case 19: case 20: {
                        int idx;
                        if (op == 18) idx = code[pc++] & 0xff; else { idx = u16(code, pc); pc += 2; }
                        ConstantPool cp = cls.cf.pool;
                        th.fp = fp; th.sp = sp; th.pc = ipc; th.method = m;   // GC safepoint: resolving a string or class literal allocates
                        switch (cp.tag(idx)) {
                        case ConstantPool.INTEGER: st[sp++] = cp.intValue(idx); break;
                        case ConstantPool.FLOAT: st[sp++] = Float.floatToRawIntBits(cp.floatValue(idx)); break;
                        case ConstantPool.STRING: st[sp++] = lk.resolveString(cls, idx); break;
                        case ConstantPool.CLASS: st[sp++] = lk.mirror(lk.resolveClass(cls, idx)); break;
                        case ConstantPool.LONG: setL(st, sp, cp.longValue(idx)); sp += 2; break;
                        case ConstantPool.DOUBLE: setD(st, sp, cp.doubleValue(idx)); sp += 2; break;
                        default: throw Errors.error("java/lang/VerifyError", "bad ldc constant");
                        }
                        break;
                    }
                    case 21: case 23: case 25: st[sp++] = st[fp + (code[pc++] & 0xff)]; break;
                    case 22: case 24: { int i = fp + (code[pc++] & 0xff); st[sp++] = st[i]; st[sp++] = st[i + 1]; break; }
                    case 26: case 27: case 28: case 29: st[sp++] = st[fp + op - 26]; break;
                    case 30: case 31: case 32: case 33: { int i = fp + op - 30; st[sp++] = st[i]; st[sp++] = st[i + 1]; break; }
                    case 34: case 35: case 36: case 37: st[sp++] = st[fp + op - 34]; break;
                    case 38: case 39: case 40: case 41: { int i = fp + op - 38; st[sp++] = st[i]; st[sp++] = st[i + 1]; break; }
                    case 42: case 43: case 44: case 45: st[sp++] = st[fp + op - 42]; break;
                    case 46: case 48: case 50: { // iaload faload aaload
                        int i = st[--sp], a = st[sp - 1];
                        if (a == 0) throw npe();
                        if (i < 0 || i >= mem[a + 2]) throw aioobe(i);
                        st[sp - 1] = mem[a + 3 + i];
                        break;
                    }
                    case 47: case 49: { // laload daload
                        int i = st[--sp], a = st[sp - 1];
                        if (a == 0) throw npe();
                        if (i < 0 || i >= mem[a + 2]) throw aioobe(i);
                        int k = a + 3 + (i << 1);
                        st[sp - 1] = mem[k]; st[sp++] = mem[k + 1];
                        break;
                    }
                    case 51: { // baload
                        int i = st[--sp], a = st[sp - 1];
                        if (a == 0) throw npe();
                        if (i < 0 || i >= mem[a + 2]) throw aioobe(i);
                        st[sp - 1] = (byte) (mem[a + 3 + (i >> 2)] >> ((i & 3) << 3));
                        break;
                    }
                    case 52: { // caload
                        int i = st[--sp], a = st[sp - 1];
                        if (a == 0) throw npe();
                        if (i < 0 || i >= mem[a + 2]) throw aioobe(i);
                        st[sp - 1] = (mem[a + 3 + (i >> 1)] >>> ((i & 1) << 4)) & 0xFFFF;
                        break;
                    }
                    case 53: { // saload
                        int i = st[--sp], a = st[sp - 1];
                        if (a == 0) throw npe();
                        if (i < 0 || i >= mem[a + 2]) throw aioobe(i);
                        st[sp - 1] = (short) (mem[a + 3 + (i >> 1)] >>> ((i & 1) << 4));
                        break;
                    }
                    case 54: case 56: case 58: st[fp + (code[pc++] & 0xff)] = st[--sp]; break;
                    case 55: case 57: { int i = fp + (code[pc++] & 0xff); st[i + 1] = st[--sp]; st[i] = st[--sp]; break; }
                    case 59: case 60: case 61: case 62: st[fp + op - 59] = st[--sp]; break;
                    case 63: case 64: case 65: case 66: { int i = fp + op - 63; st[i + 1] = st[--sp]; st[i] = st[--sp]; break; }
                    case 67: case 68: case 69: case 70: st[fp + op - 67] = st[--sp]; break;
                    case 71: case 72: case 73: case 74: { int i = fp + op - 71; st[i + 1] = st[--sp]; st[i] = st[--sp]; break; }
                    case 75: case 76: case 77: case 78: st[fp + op - 75] = st[--sp]; break;
                    case 79: case 81: { // iastore fastore
                        int v = st[--sp], i = st[--sp], a = st[--sp];
                        if (a == 0) throw npe();
                        if (i < 0 || i >= mem[a + 2]) throw aioobe(i);
                        mem[a + 3 + i] = v;
                        break;
                    }
                    case 80: case 82: { // lastore dastore
                        int lo = st[--sp], hi = st[--sp], i = st[--sp], a = st[--sp];
                        if (a == 0) throw npe();
                        if (i < 0 || i >= mem[a + 2]) throw aioobe(i);
                        int k = a + 3 + (i << 1);
                        mem[k] = hi; mem[k + 1] = lo;
                        break;
                    }
                    case 83: { // aastore
                        int v = st[--sp], i = st[--sp], a = st[--sp];
                        if (a == 0) throw npe();
                        if (i < 0 || i >= mem[a + 2]) throw aioobe(i);
                        if (v != 0 && !lk.isAssignable(classOf(v), classOf(a).component))
                            throw new VmThrow("java/lang/ArrayStoreException", classOf(v).name);
                        mem[a + 3 + i] = v;
                        break;
                    }
                    case 84: { // bastore
                        int v = st[--sp], i = st[--sp], a = st[--sp];
                        if (a == 0) throw npe();
                        if (i < 0 || i >= mem[a + 2]) throw aioobe(i);
                        if (((mem[a] & Heap.KIND_MASK) >>> Heap.KIND_SHIFT) == Heap.K_BOOLEAN) v &= 1;
                        int w = a + 3 + (i >> 2), s = (i & 3) << 3;
                        mem[w] = (mem[w] & ~(0xFF << s)) | ((v & 0xFF) << s);
                        break;
                    }
                    case 85: case 86: { // castore sastore
                        int v = st[--sp], i = st[--sp], a = st[--sp];
                        if (a == 0) throw npe();
                        if (i < 0 || i >= mem[a + 2]) throw aioobe(i);
                        int w = a + 3 + (i >> 1), s = (i & 1) << 4;
                        mem[w] = (mem[w] & ~(0xFFFF << s)) | ((v & 0xFFFF) << s);
                        break;
                    }
                    case 87: sp--; break;
                    case 88: sp -= 2; break;
                    case 89: st[sp] = st[sp - 1]; sp++; break;
                    case 90: { int a = st[sp - 1], b = st[sp - 2]; st[sp - 2] = a; st[sp - 1] = b; st[sp++] = a; break; }
                    case 91: { int a = st[sp - 1], b = st[sp - 2], c = st[sp - 3]; st[sp - 3] = a; st[sp - 2] = c; st[sp - 1] = b; st[sp++] = a; break; }
                    case 92: st[sp] = st[sp - 2]; st[sp + 1] = st[sp - 1]; sp += 2; break;
                    case 93: { int a = st[sp - 1], b = st[sp - 2], c = st[sp - 3]; st[sp - 3] = b; st[sp - 2] = a; st[sp - 1] = c; st[sp] = b; st[sp + 1] = a; sp += 2; break; }
                    case 94: { int a = st[sp - 1], b = st[sp - 2], c = st[sp - 3], d = st[sp - 4]; st[sp - 4] = b; st[sp - 3] = a; st[sp - 2] = d; st[sp - 1] = c; st[sp] = b; st[sp + 1] = a; sp += 2; break; }
                    case 95: { int a = st[sp - 1]; st[sp - 1] = st[sp - 2]; st[sp - 2] = a; break; }
                    case 96: sp--; st[sp - 1] += st[sp]; break;
                    case 97: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a + b); break; }
                    case 98: sp--; setF(st, sp - 1, getF(st, sp - 1) + getF(st, sp)); break;
                    case 99: { double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 2; setD(st, sp - 2, a + b); break; }
                    case 100: sp--; st[sp - 1] -= st[sp]; break;
                    case 101: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a - b); break; }
                    case 102: sp--; setF(st, sp - 1, getF(st, sp - 1) - getF(st, sp)); break;
                    case 103: { double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 2; setD(st, sp - 2, a - b); break; }
                    case 104: sp--; st[sp - 1] *= st[sp]; break;
                    case 105: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a * b); break; }
                    case 106: sp--; setF(st, sp - 1, getF(st, sp - 1) * getF(st, sp)); break;
                    case 107: { double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 2; setD(st, sp - 2, a * b); break; }
                    case 108: { int b = st[--sp]; if (b == 0) throw new VmThrow("java/lang/ArithmeticException", "/ by zero"); st[sp - 1] /= b; break; }
                    case 109: { long b = getL(st, sp - 2), a = getL(st, sp - 4); if (b == 0) throw new VmThrow("java/lang/ArithmeticException", "/ by zero"); sp -= 2; setL(st, sp - 2, a / b); break; }
                    case 110: sp--; setF(st, sp - 1, getF(st, sp - 1) / getF(st, sp)); break;
                    case 111: { double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 2; setD(st, sp - 2, a / b); break; }
                    case 112: { int b = st[--sp]; if (b == 0) throw new VmThrow("java/lang/ArithmeticException", "/ by zero"); st[sp - 1] %= b; break; }
                    case 113: { long b = getL(st, sp - 2), a = getL(st, sp - 4); if (b == 0) throw new VmThrow("java/lang/ArithmeticException", "/ by zero"); sp -= 2; setL(st, sp - 2, a % b); break; }
                    case 114: sp--; setF(st, sp - 1, getF(st, sp - 1) % getF(st, sp)); break;
                    case 115: { double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 2; setD(st, sp - 2, a % b); break; }
                    case 116: st[sp - 1] = -st[sp - 1]; break;
                    case 117: setL(st, sp - 2, -getL(st, sp - 2)); break;
                    case 118: setF(st, sp - 1, -getF(st, sp - 1)); break;
                    case 119: setD(st, sp - 2, -getD(st, sp - 2)); break;
                    case 120: sp--; st[sp - 1] <<= st[sp]; break;
                    case 121: { int s = st[--sp]; setL(st, sp - 2, getL(st, sp - 2) << s); break; }
                    case 122: sp--; st[sp - 1] >>= st[sp]; break;
                    case 123: { int s = st[--sp]; setL(st, sp - 2, getL(st, sp - 2) >> s); break; }
                    case 124: sp--; st[sp - 1] >>>= st[sp]; break;
                    case 125: { int s = st[--sp]; setL(st, sp - 2, getL(st, sp - 2) >>> s); break; }
                    case 126: sp--; st[sp - 1] &= st[sp]; break;
                    case 127: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a & b); break; }
                    case 128: sp--; st[sp - 1] |= st[sp]; break;
                    case 129: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a | b); break; }
                    case 130: sp--; st[sp - 1] ^= st[sp]; break;
                    case 131: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a ^ b); break; }
                    case 132: { int i = fp + (code[pc] & 0xff); st[i] += code[pc + 1]; pc += 2; break; }
                    case 133: { int v = st[sp - 1]; setL(st, sp - 1, v); sp++; break; }
                    case 134: setF(st, sp - 1, (float) st[sp - 1]); break;
                    case 135: { int v = st[sp - 1]; setD(st, sp - 1, v); sp++; break; }
                    case 136: sp--; st[sp - 1] = st[sp]; break; // l2i: low word is the top one
                    case 137: sp--; setF(st, sp - 1, (float) getL(st, sp - 1)); break;
                    case 138: setD(st, sp - 2, (double) getL(st, sp - 2)); break;
                    case 139: st[sp - 1] = (int) getF(st, sp - 1); break;
                    case 140: { float f = getF(st, sp - 1); setL(st, sp - 1, (long) f); sp++; break; }
                    case 141: { float f = getF(st, sp - 1); setD(st, sp - 1, f); sp++; break; }
                    case 142: sp--; st[sp - 1] = (int) getD(st, sp - 1); break;
                    case 143: setL(st, sp - 2, (long) getD(st, sp - 2)); break;
                    case 144: sp--; setF(st, sp - 1, (float) getD(st, sp - 1)); break;
                    case 145: st[sp - 1] = (byte) st[sp - 1]; break;
                    case 146: st[sp - 1] = (char) st[sp - 1]; break;
                    case 147: st[sp - 1] = (short) st[sp - 1]; break;
                    case 148: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 3; st[sp - 1] = a < b ? -1 : a > b ? 1 : 0; break; }
                    case 149: case 150: {
                        float b = getF(st, sp - 1), a = getF(st, sp - 2); sp--;
                        st[sp - 1] = a > b ? 1 : a == b ? 0 : a < b ? -1 : (op == 150 ? 1 : -1);
                        break;
                    }
                    case 151: case 152: {
                        double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 3;
                        st[sp - 1] = a > b ? 1 : a == b ? 0 : a < b ? -1 : (op == 152 ? 1 : -1);
                        break;
                    }
                    case 153: if (st[--sp] == 0) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 154: if (st[--sp] != 0) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 155: if (st[--sp] < 0) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 156: if (st[--sp] >= 0) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 157: if (st[--sp] > 0) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 158: if (st[--sp] <= 0) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 159: case 165: sp -= 2; if (st[sp] == st[sp + 1]) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 160: case 166: sp -= 2; if (st[sp] != st[sp + 1]) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 161: sp -= 2; if (st[sp] < st[sp + 1]) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 162: sp -= 2; if (st[sp] >= st[sp + 1]) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 163: sp -= 2; if (st[sp] > st[sp + 1]) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 164: sp -= 2; if (st[sp] <= st[sp + 1]) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 167: pc = ipc + s16(code, pc); break;
                    case 168: st[sp++] = pc + 2; pc = ipc + s16(code, pc); break; // jsr
                    case 169: pc = st[fp + (code[pc] & 0xff)]; break; // ret
                    case 170: { // tableswitch
                        int p = (ipc + 4) & ~3;
                        int def = s32(code, p), lo = s32(code, p + 4), hi = s32(code, p + 8);
                        int key = st[--sp];
                        if (key < lo || key > hi) pc = ipc + def; else pc = ipc + s32(code, p + 12 + (key - lo) * 4);
                        break;
                    }
                    case 171: { // lookupswitch
                        int p = (ipc + 4) & ~3;
                        int def = s32(code, p), n = s32(code, p + 4);
                        int key = st[--sp];
                        int target = def;
                        int l = 0, h = n - 1;
                        while (l <= h) {
                            int mid = (l + h) >>> 1, k = s32(code, p + 8 + mid * 8);
                            if (k == key) { target = s32(code, p + 12 + mid * 8); break; }
                            if (k < key) l = mid + 1; else h = mid - 1;
                        }
                        pc = ipc + target;
                        break;
                    }
                    case 172: case 173: case 174: case 175: case 176: case 177: {
                        int rw = op == 177 ? 0 : (op == 173 || op == 175) ? 2 : 1;
                        int hdr = fp + m.maxLocals;
                        if (m.isSync) { int lockRef = st[hdr + Frames.SYNC]; if (lockRef != 0) mons.exit(th, lockRef); }
                        if (th.lockTop > st[hdr + Frames.LOCKS]) {
                            th.fp = fp; th.sp = sp; th.method = m; th.pc = ipc;
                            releaseFrameLocks(th, st[hdr + Frames.LOCKS]);
                            throw new VmThrow("java/lang/IllegalMonitorStateException", "method returned holding a monitor");
                        }
                        int v1 = rw == 2 ? st[sp - 2] : 0, v0 = rw >= 1 ? st[sp - 1] : 0;
                        int pfp = st[hdr + Frames.PREV_FP];
                        if (pfp < 0) { sp = fp; th.state = VmThread.DEAD; slow = S_SWITCH; break inner; }
                        int flags = st[hdr + Frames.FLAGS], extra = st[hdr + Frames.EXTRA];
                        m = lk.method(st[hdr + Frames.PREV_MID]);
                        int ppc = st[hdr + Frames.PREV_PC];
                        sp = fp; fp = pfp; code = m.code; cls = m.owner;
                        if ((flags & Frames.F_INIT) != 0) {
                            ClassInfo ic = lk.byId(extra);
                            ic.state = ClassInfo.INITIALIZED; ic.initThread = 0;
                            pc = ppc;
                        } else {
                            pc = ppc + ((code[ppc] & 0xff) == 185 ? 5 : 3);
                            if ((flags & Frames.F_PUSH_REF) != 0) st[sp++] = extra;
                            else if (rw == 1) st[sp++] = v0;
                            else if (rw == 2) { st[sp++] = v1; st[sp++] = v0; }
                        }
                        break;
                    }
                    case 178: case 179: { // getstatic putstatic
                        RField f = (RField) (cls.resolved[u16(code, pc)] instanceof RField ? cls.resolved[u16(code, pc)] : lk.resolveField(cls, u16(code, pc)));
                        if (!f.isStatic) throw icce("not a static field: " + f.name);
                        if (op == 179 && f.isFinal && f.owner != cls) throw Errors.error("java/lang/IllegalAccessError", "final field " + f.owner.name + "." + f.name + " assigned from " + cls.name);
                        ClassInfo oc = f.owner;
                        if (needsInit(oc, th)) { slow = S_INIT; slowCls = oc; pc = ipc; break inner; }
                        pc += 2;
                        int[] stat = oc.statics;
                        if (op == 178) {
                            st[sp++] = stat[f.slot];
                            if (f.words == 2) st[sp++] = stat[f.slot + 1];
                        } else if (f.words == 2) {
                            stat[f.slot + 1] = st[--sp]; stat[f.slot] = st[--sp];
                        } else {
                            stat[f.slot] = narrow(f.kind, st[--sp]);
                        }
                        break;
                    }
                    case 180: { // getfield
                        RField f = (RField) (cls.resolved[u16(code, pc)] instanceof RField ? cls.resolved[u16(code, pc)] : lk.resolveField(cls, u16(code, pc)));
                        int ref = st[sp - 1];
                        if (ref == 0) throw npe();
                        if (f.isStatic) throw icce("static field " + f.name);
                        pc += 2;
                        int w = ref + 2 + f.slot;
                        st[sp - 1] = mem[w];
                        if (f.words == 2) st[sp++] = mem[w + 1];
                        break;
                    }
                    case 181: { // putfield
                        RField f = (RField) (cls.resolved[u16(code, pc)] instanceof RField ? cls.resolved[u16(code, pc)] : lk.resolveField(cls, u16(code, pc)));
                        if (f.isStatic) throw icce("static field " + f.name);
                        if (f.isFinal && f.owner != cls) throw Errors.error("java/lang/IllegalAccessError", "final field " + f.owner.name + "." + f.name + " assigned from " + cls.name);
                        if (f.words == 2) {
                            int lo = st[--sp], hi = st[--sp], ref = st[--sp];
                            if (ref == 0) throw npe();
                            mem[ref + 2 + f.slot] = hi; mem[ref + 3 + f.slot] = lo;
                        } else {
                            int v = st[--sp], ref = st[--sp];
                            if (ref == 0) throw npe();
                            mem[ref + 2 + f.slot] = narrow(f.kind, v);
                        }
                        pc += 2;
                        break;
                    }
                    case 182: case 183: case 184: case 185: {
                        int idx = u16(code, pc);
                        Object o = cls.resolved[idx];
                        RMethod ref = o instanceof RMethod ? (RMethod) o : lk.resolveMethod(cls, idx);
                        RMethod t = ref;
                        if (op == 184) {
                            if (!ref.isStatic) throw icce("not a static method: " + ref);
                            if (needsInit(ref.owner, th)) { slow = S_INIT; slowCls = ref.owner; pc = ipc; break inner; }
                        } else {
                            if (ref.isStatic) throw icce("static method " + ref);
                            int recv = st[sp - ref.argWords];
                            if (recv == 0) throw npe();
                            if (op == 183) {
                                t = lk.specialTarget(cls, idx, ref);   // ACC_SUPER lookup only when the calling class has the flag (T12)
                            } else if (op == 185) {
                                t = lk.selectInterface(classOf(recv), ref);
                            } else if ((ref.access & ClassFile.ACC_PRIVATE) == 0) {
                                t = classOf(recv).selectVirtual(ref);
                                if (t == null) throw Errors.error("java/lang/AbstractMethodError", ref.toString());
                            }
                        }
                        pc += op == 185 ? 4 : 2;
                        if (t.isNative) {
                            if (t.nat == null) throw Errors.error("java/lang/UnsatisfiedLinkError", t.nativeKey);
                            th.fp = fp; th.sp = sp; th.pc = ipc; th.method = m;
                            NativeCtx c = ctx;
                            c.begin(th, st, sp - t.argWords, t);
                            th.inNative = t;   // GC: the arguments are still on the operand stack
                            t.nat.invoke(c);
                            th.inNative = null;
                            if (c.exClass != null) throw new VmThrow(c.exClass, c.exMsg);
                            if (c.exRef != 0) throw new VmThrow(c.exRef);
                            if (c.initCls != null) { slow = S_INIT; slowCls = c.initCls; pc = ipc; break inner; }
                            sp -= t.argWords;
                            if (c.callM != null) {
                                nframe = c.callM; nframeArg = c.callArg;
                                st[sp++] = nframeArg;
                                slow = S_NFRAME; break inner;
                            }
                            if (c.blocker != null) {
                                th.blocker = c.blocker; th.state = VmThread.BLOCKED_NATIVE; th.blockerRet = t.ret; th.excPc = ipc;
                                vm.sched.addBlockedNative(th);
                                slow = S_SWITCH; break inner;
                            }
                            switch (t.ret) {
                            case 'V': break;
                            case 'J': case 'D': st[sp++] = c.rHi; st[sp++] = c.rLo; break;
                            default: st[sp++] = c.rLo; break;
                            }
                            budget -= c.charge;
                            if (th.state != VmThread.RUNNING || c.yield) { th.excPc = ipc; slow = S_SWITCH; break inner; }
                            break;
                        }
                        if (t.code == null) throw Errors.error("java/lang/AbstractMethodError", t.toString());
                        int nfp = sp - t.argWords, nhdr = nfp + t.maxLocals;
                        if (nhdr + Frames.HDR + t.maxStack > st.length) throw Errors.error("java/lang/StackOverflowError", null);
                        int lockRef = 0;
                        if (t.isSync) {
                            if (t.isStatic) { th.fp = fp; th.sp = sp; th.pc = ipc; th.method = m; lockRef = lk.mirror(t.owner); }   // may allocate: before the frame is built
                            else lockRef = st[nfp];
                        }
                        st[nhdr + Frames.PREV_FP] = fp; st[nhdr + Frames.PREV_MID] = m.id; st[nhdr + Frames.PREV_PC] = ipc;
                        st[nhdr + Frames.SYNC] = 0; st[nhdr + Frames.FLAGS] = 0; st[nhdr + Frames.LOCKS] = th.lockTop;
                        fp = nfp; m = t; code = t.code; cls = t.owner; sp = nhdr + Frames.HDR; pc = 0;
                        if (t.isSync) {
                            st[nhdr + Frames.SYNC] = lockRef;
                            if (!mons.enter(th, lockRef)) { slow = S_SWITCH; break inner; }
                        }
                        break;
                    }
                    case 187: { // new
                        int idx = u16(code, pc);
                        Object o = cls.resolved[idx];
                        ClassInfo c = o instanceof ClassInfo ? (ClassInfo) o : lk.resolveClass(cls, idx);
                        if (c.isInterface || (c.access & ClassFile.ACC_ABSTRACT) != 0 || c.isArray)
                            throw Errors.error("java/lang/InstantiationError", c.name);
                        if (needsInit(c, th)) { slow = S_INIT; slowCls = c; pc = ipc; break inner; }
                        th.fp = fp; th.sp = sp; th.pc = ipc; th.method = m;   // GC safepoint
                        int r = heap.allocObject(c.id);
                        if (r == 0) throw vm.oomThrow(c.name);
                        st[sp++] = r;
                        pc += 2;
                        break;
                    }
                    case 188: { // newarray
                        int kind;
                        switch (code[pc++]) {
                            case 4: kind = Heap.K_BOOLEAN; break; case 5: kind = Heap.K_CHAR; break; case 6: kind = Heap.K_FLOAT; break;
                            case 7: kind = Heap.K_DOUBLE; break; case 8: kind = Heap.K_BYTE; break; case 9: kind = Heap.K_SHORT; break;
                            case 10: kind = Heap.K_INT; break; default: kind = Heap.K_LONG; break;
                        }
                        if (primArr[kind] == 0) primArr[kind] = primArray(kind).id;
                        int len = st[sp - 1];
                        if (len < 0) throw new VmThrow("java/lang/NegativeArraySizeException", String.valueOf(len));
                        th.fp = fp; th.sp = sp; th.pc = ipc; th.method = m;   // GC safepoint (pc = this instruction: its operand is still on the stack)
                        int r = heap.allocArray(primArr[kind], kind, len);
                        if (r == 0) throw vm.oomThrow("array of " + len);
                        st[sp - 1] = r;
                        break;
                    }
                    case 189: { // anewarray
                        ClassInfo ac = lk.arrayOf(lk.resolveClass(cls, u16(code, pc)));
                        th.fp = fp; th.sp = sp; th.pc = ipc; th.method = m;
                        pc += 2;
                        st[sp - 1] = allocArray(ac, st[sp - 1]);
                        break;
                    }
                    case 190: { int a = st[sp - 1]; if (a == 0) throw npe(); st[sp - 1] = mem[a + 2]; break; }
                    case 191: { int a = st[sp - 1]; if (a == 0) throw npe(); throw new VmThrow(a); }
                    case 192: { // checkcast
                        int r = st[sp - 1];
                        if (r == 0) { pc += 2; break; }   // null: the class is not resolved (JVMS)
                        ClassInfo c = lk.resolveClass(cls, u16(code, pc));
                        pc += 2;
                        if (!lk.isAssignable(classOf(r), c)) throw new VmThrow("java/lang/ClassCastException", classOf(r).name.replace('/', '.'));
                        break;
                    }
                    case 193: { // instanceof
                        int r = st[sp - 1];
                        if (r == 0) { pc += 2; break; }   // null: the class is not resolved (JVMS)
                        ClassInfo c = lk.resolveClass(cls, u16(code, pc));
                        pc += 2;
                        st[sp - 1] = lk.isAssignable(classOf(r), c) ? 1 : 0;
                        break;
                    }
                    case 194: { // monitorenter
                        int r = st[--sp];
                        if (r == 0) throw npe();
                        if (th.lockTop == th.locks.length) th.locks = java.util.Arrays.copyOf(th.locks, th.lockTop * 2);
                        th.locks[th.lockTop++] = r;
                        if (!mons.enter(th, r)) { slow = S_SWITCH; break inner; }
                        break;
                    }
                    case 195: { // monitorexit
                        int r = st[--sp];
                        if (r == 0) throw npe();
                        mons.exit(th, r);
                        int lb = st[fp + m.maxLocals + Frames.LOCKS], li = th.lockTop - 1;
                        while (li >= lb && th.locks[li] != r) li--;
                        if (li >= lb) { System.arraycopy(th.locks, li + 1, th.locks, li, th.lockTop - li - 1); th.lockTop--; }
                        break;
                    }
                    case 196: { // wide
                        int op2 = code[pc] & 0xff;
                        int idx = u16(code, pc + 1);
                        if (op2 == 132) { st[fp + idx] += s16(code, pc + 3); pc += 5; break; }
                        pc += 3;
                        switch (op2) {
                        case 21: case 23: case 25: st[sp++] = st[fp + idx]; break;
                        case 22: case 24: st[sp++] = st[fp + idx]; st[sp++] = st[fp + idx + 1]; break;
                        case 54: case 56: case 58: st[fp + idx] = st[--sp]; break;
                        case 55: case 57: st[fp + idx + 1] = st[--sp]; st[fp + idx] = st[--sp]; break;
                        case 169: pc = st[fp + idx]; break;
                        default: throw Errors.error("java/lang/VerifyError", "bad wide opcode " + op2);
                        }
                        break;
                    }
                    case 197: { // multianewarray
                        ClassInfo ac = lk.resolveClass(cls, u16(code, pc));
                        int dims = code[pc + 2] & 0xff;
                        pc += 3;
                        int[] counts = new int[dims];
                        for (int i = dims - 1; i >= 0; i--) counts[i] = st[--sp];
                        for (int i = 0; i < dims; i++) if (counts[i] < 0) throw new VmThrow("java/lang/NegativeArraySizeException", String.valueOf(counts[i]));
                        sp += dims;   // the counts stay on the operand stack (as before the instruction) while the arrays are allocated
                        th.fp = fp; th.sp = sp; th.pc = ipc; th.method = m;
                        sp -= dims;
                        st[sp++] = multi(ac, counts, 0);
                        break;
                    }
                    case 198: if (st[--sp] == 0) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 199: if (st[--sp] != 0) pc = ipc + s16(code, pc); else pc += 2; break;
                    case 200: pc = ipc + s32(code, pc); break;
                    case 201: st[sp++] = pc + 4; pc = ipc + s32(code, pc); break;
                    default:
                        throw Errors.error("java/lang/VerifyError", "illegal opcode " + op + " in " + m);
                    }
                }
                // ---- slow paths
                switch (slow) {
                case S_INIT: {
                    th.fp = fp; th.sp = sp; th.method = m; th.pc = ipc;
                    slow = S_NONE;
                    int r = vm.initStep(th, slowCls);
                    if (r == 0) { pc = ipc; continue; }
                    if (r == 2) { th.excPc = ipc; pc = ipc; th.fp = fp; th.sp = sp; th.method = m; th.pc = pc; return slice - budget; }
                    RMethod t = vm.initRun.clinit;
                    int nhdr = sp + t.maxLocals;
                    if (nhdr + Frames.HDR + t.maxStack > st.length) {
                        vm.initRun.state = ClassInfo.ERROR; vm.initRun.initThread = 0;   // the class cannot be initialised any more
                        throw Errors.error("java/lang/StackOverflowError", null);
                    }
                    st[nhdr + Frames.PREV_FP] = fp; st[nhdr + Frames.PREV_MID] = m.id; st[nhdr + Frames.PREV_PC] = ipc;
                    st[nhdr + Frames.SYNC] = 0; st[nhdr + Frames.FLAGS] = Frames.F_INIT; st[nhdr + Frames.EXTRA] = vm.initRun.id; st[nhdr + Frames.LOCKS] = th.lockTop;
                    fp = sp; m = t; code = t.code; cls = t.owner; sp = nhdr + Frames.HDR; pc = 0;
                    continue;
                }
                case S_NFRAME: {
                    slow = S_NONE;
                    RMethod t = nframe;
                    int nfp = sp - t.argWords, nhdr = nfp + t.maxLocals;
                    if (nhdr + Frames.HDR + t.maxStack > st.length) throw Errors.error("java/lang/StackOverflowError", null);
                    st[nhdr + Frames.PREV_FP] = fp; st[nhdr + Frames.PREV_MID] = m.id; st[nhdr + Frames.PREV_PC] = ipc;
                    st[nhdr + Frames.SYNC] = 0; st[nhdr + Frames.FLAGS] = Frames.F_PUSH_REF | Frames.F_EXTRA_REF; st[nhdr + Frames.EXTRA] = nframeArg; st[nhdr + Frames.LOCKS] = th.lockTop;
                    fp = nfp; m = t; code = t.code; cls = t.owner; sp = nhdr + Frames.HDR; pc = 0;
                    continue;
                }
                default:
                    th.fp = fp; th.sp = sp; th.pc = pc; th.method = m;
                    return Math.max(0, slice - budget);
                }
            } catch (VmThrow t) {
                th.inNative = null;
                th.fp = fp; th.sp = sp; th.method = m; th.pc = ipc;
                slow = S_NONE;
                int ex = vm.materialize(t, th);
                if (!unwind(th, ex)) return Math.max(0, slice - budget);
                fp = th.fp; sp = th.sp; pc = th.pc; m = th.method; code = m.code; cls = m.owner;
            }
        }
    }

    private static int narrow(char kind, int v) {
        switch (kind) {
            case 'B': return (byte) v;
            case 'S': return (short) v;
            case 'C': return (char) v;
            case 'Z': return v & 1;
            default: return v;
        }
    }

    // ------------------------------------------------------------------ exceptions

    /** Finds a handler for {@code ex} starting at the top frame of {@code th}; false when the thread died. */
    private boolean unwind(VmThread th, int ex) {
        final int[] st = th.stack;
        int fp = th.fp, pc = th.pc;
        RMethod m = th.method;
        if (errorClass == null) errorClass = lk.find("java/lang/Error");
        ClassInfo ec = classOf(ex);
        for (;;) {
            if (m.code != null) {
                ClassFile.ExceptionEntry[] tab = m.mi.code.exceptionTable;
                for (int i = 0; i < tab.length; i++) {
                    ClassFile.ExceptionEntry h = tab[i];
                    if (pc < h.startPc || pc >= h.endPc) continue;
                    if (h.catchType != 0) {
                        ClassInfo ct;
                        try {
                            ct = lk.resolveClass(m.owner, h.catchType);
                        } catch (VmThrow x) {
                            // JVMS 5.4.3: the resolution error is thrown instead; it leaves this frame (no further handler of it is tried)
                            ex = vm.materialize(x, th);
                            ec = classOf(ex);
                            break;
                        }
                        if (!lk.isAssignable(ec, ct)) continue;
                    }
                    th.fp = fp; th.method = m;
                    th.sp = fp + m.maxLocals + Frames.HDR;
                    st[th.sp++] = ex;
                    th.pc = h.handlerPc;
                    return true;
                }
            }
            int hdr = fp + m.maxLocals;
            if (m.isSync) {
                int lockRef = st[hdr + Frames.SYNC];
                if (lockRef != 0) { try { mons.exit(th, lockRef); } catch (VmThrow x) { /* ignore */ } }
            }
            if (th.lockTop > st[hdr + Frames.LOCKS]) releaseFrameLocks(th, st[hdr + Frames.LOCKS]);
            int flags = st[hdr + Frames.FLAGS];
            if ((flags & Frames.F_INIT) != 0) {
                ClassInfo ic = lk.byId(st[hdr + Frames.EXTRA]);
                ic.state = ClassInfo.ERROR; ic.initThread = 0;
                if (!ec.isSubclassOf(errorClass)) {   // ExceptionInInitializerError is not in CLDC: mapped to java.lang.Error (CLDC 4.2/5.1.4)
                    ex = vm.wrapInitializerError(ex);
                    ec = classOf(ex);
                }
            }
            int pfp = st[hdr + Frames.PREV_FP];
            if (pfp < 0) {
                th.fp = fp; th.method = m;
                th.state = VmThread.DEAD;
                vm.reportUncaught(th, ex);
                return false;
            }
            m = lk.method(st[hdr + Frames.PREV_MID]);
            pc = st[hdr + Frames.PREV_PC];
            fp = pfp;
        }
    }

    /** Releases (innermost first) every monitorenter lock above {@code base} and trims the lock list. */
    private void releaseFrameLocks(VmThread th, int base) {
        while (th.lockTop > base) {
            int r = th.locks[--th.lockTop];
            try { mons.exit(th, r); } catch (VmThrow x) { /* not owner any more */ }
        }
    }

    /** Completes a thread parked by {@code NativeContext.block}: @return true when it may run again. */
    public boolean completeBlocked(VmThread t) {
        ctx.begin(t, t.stack, t.sp, null);
        if (!t.blocker.poll(ctx)) return false;
        t.blocker = null;
        if (ctx.exClass != null) { t.pendingThrow = ctx.exClass; return true; }
        int[] st = t.stack;
        switch (t.blockerRet) {
            case 'V': break;
            case 'J': case 'D': st[t.sp++] = ctx.rHi; st[t.sp++] = ctx.rLo; break;
            default: st[t.sp++] = ctx.rLo; break;
        }
        return true;
    }
}

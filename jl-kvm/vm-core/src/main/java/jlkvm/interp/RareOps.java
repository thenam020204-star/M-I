package jlkvm.interp;

/**
 * Rarely executed opcodes moved out of {@link Interpreter#run} (PLAN T11a): the hot loop stays below HotSpot's/ART's
 * "huge method" threshold (8000 bytes of bytecode) so that it can be JIT-compiled. Pure code motion: each method is
 * the body of the former {@code case} and returns the new operand stack pointer (or pc).
 */
final class RareOps {
    private RareOps() {}

    private static long getL(int[] st, int i) { return ((long) st[i] << 32) | (st[i + 1] & 0xFFFFFFFFL); }
    private static void setL(int[] st, int i, long v) { st[i] = (int) (v >>> 32); st[i + 1] = (int) v; }
    private static float getF(int[] st, int i) { return Float.intBitsToFloat(st[i]); }
    private static void setF(int[] st, int i, float v) { st[i] = Float.floatToRawIntBits(v); }
    private static double getD(int[] st, int i) { return Double.longBitsToDouble(getL(st, i)); }
    private static void setD(int[] st, int i, double v) { setL(st, i, Double.doubleToRawLongBits(v)); }
    static int s32(byte[] c, int p) { return ((c[p] & 0xff) << 24) | ((c[p + 1] & 0xff) << 16) | ((c[p + 2] & 0xff) << 8) | (c[p + 3] & 0xff); }

    /** dup_x1 (90), dup_x2 (91), dup2_x1 (93), dup2_x2 (94), swap (95). */
    static int stack(int[] st, int sp, int op) {
        switch (op) {
        case 90: { int a = st[sp - 1], b = st[sp - 2]; st[sp - 2] = a; st[sp - 1] = b; st[sp++] = a; return sp; }
        case 91: { int a = st[sp - 1], b = st[sp - 2], c = st[sp - 3]; st[sp - 3] = a; st[sp - 2] = c; st[sp - 1] = b; st[sp++] = a; return sp; }
        case 93: { int a = st[sp - 1], b = st[sp - 2], c = st[sp - 3]; st[sp - 3] = b; st[sp - 2] = a; st[sp - 1] = c; st[sp] = b; st[sp + 1] = a; return sp + 2; }
        case 94: { int a = st[sp - 1], b = st[sp - 2], c = st[sp - 3], d = st[sp - 4]; st[sp - 4] = b; st[sp - 3] = a; st[sp - 2] = d; st[sp - 1] = c; st[sp] = b; st[sp + 1] = a; return sp + 2; }
        default: { int a = st[sp - 1]; st[sp - 1] = st[sp - 2]; st[sp - 2] = a; return sp; }
        }
    }

    /** long / float / double arithmetic, conversions and comparisons. */
    static int arith(int[] st, int sp, int op) {
        switch (op) {
        case 97: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a + b); return sp; }
        case 98: sp--; setF(st, sp - 1, getF(st, sp - 1) + getF(st, sp)); return sp;
        case 99: { double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 2; setD(st, sp - 2, a + b); return sp; }
        case 101: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a - b); return sp; }
        case 102: sp--; setF(st, sp - 1, getF(st, sp - 1) - getF(st, sp)); return sp;
        case 103: { double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 2; setD(st, sp - 2, a - b); return sp; }
        case 105: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a * b); return sp; }
        case 106: sp--; setF(st, sp - 1, getF(st, sp - 1) * getF(st, sp)); return sp;
        case 107: { double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 2; setD(st, sp - 2, a * b); return sp; }
        case 109: { long b = getL(st, sp - 2), a = getL(st, sp - 4); if (b == 0) throw new jlkvm.link.VmThrow("java/lang/ArithmeticException", "/ by zero"); sp -= 2; setL(st, sp - 2, a / b); return sp; }
        case 110: sp--; setF(st, sp - 1, getF(st, sp - 1) / getF(st, sp)); return sp;
        case 111: { double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 2; setD(st, sp - 2, a / b); return sp; }
        case 113: { long b = getL(st, sp - 2), a = getL(st, sp - 4); if (b == 0) throw new jlkvm.link.VmThrow("java/lang/ArithmeticException", "/ by zero"); sp -= 2; setL(st, sp - 2, a % b); return sp; }
        case 114: sp--; setF(st, sp - 1, getF(st, sp - 1) % getF(st, sp)); return sp;
        case 115: { double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 2; setD(st, sp - 2, a % b); return sp; }
        case 117: setL(st, sp - 2, -getL(st, sp - 2)); return sp;
        case 118: setF(st, sp - 1, -getF(st, sp - 1)); return sp;
        case 119: setD(st, sp - 2, -getD(st, sp - 2)); return sp;
        case 121: { int s = st[--sp]; setL(st, sp - 2, getL(st, sp - 2) << s); return sp; }
        case 123: { int s = st[--sp]; setL(st, sp - 2, getL(st, sp - 2) >> s); return sp; }
        case 125: { int s = st[--sp]; setL(st, sp - 2, getL(st, sp - 2) >>> s); return sp; }
        case 127: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a & b); return sp; }
        case 129: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a | b); return sp; }
        case 131: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 2; setL(st, sp - 2, a ^ b); return sp; }
        case 133: { int v = st[sp - 1]; setL(st, sp - 1, v); return sp + 1; }
        case 134: setF(st, sp - 1, (float) st[sp - 1]); return sp;
        case 135: { int v = st[sp - 1]; setD(st, sp - 1, v); return sp + 1; }
        case 136: sp--; st[sp - 1] = st[sp]; return sp; // l2i: low word is the top one
        case 137: sp--; setF(st, sp - 1, (float) getL(st, sp - 1)); return sp;
        case 138: setD(st, sp - 2, (double) getL(st, sp - 2)); return sp;
        case 139: st[sp - 1] = (int) getF(st, sp - 1); return sp;
        case 140: { float f = getF(st, sp - 1); setL(st, sp - 1, (long) f); return sp + 1; }
        case 141: { float f = getF(st, sp - 1); setD(st, sp - 1, f); return sp + 1; }
        case 142: sp--; st[sp - 1] = (int) getD(st, sp - 1); return sp;
        case 143: setL(st, sp - 2, (long) getD(st, sp - 2)); return sp;
        case 144: sp--; setF(st, sp - 1, (float) getD(st, sp - 1)); return sp;
        case 148: { long b = getL(st, sp - 2), a = getL(st, sp - 4); sp -= 3; st[sp - 1] = a < b ? -1 : a > b ? 1 : 0; return sp; }
        case 149: case 150: {
            float b = getF(st, sp - 1), a = getF(st, sp - 2); sp--;
            st[sp - 1] = a > b ? 1 : a == b ? 0 : a < b ? -1 : (op == 150 ? 1 : -1);
            return sp;
        }
        default: { // 151, 152
            double b = getD(st, sp - 2), a = getD(st, sp - 4); sp -= 3;
            st[sp - 1] = a > b ? 1 : a == b ? 0 : a < b ? -1 : (op == 152 ? 1 : -1);
            return sp;
        }
        }
    }

    /** tableswitch (170) / lookupswitch (171): the new pc; {@code key} is already popped. */
    static int switchTarget(byte[] code, int ipc, int op, int key) {
        int p = (ipc + 4) & ~3;
        int def = s32(code, p);
        if (op == 170) {
            int lo = s32(code, p + 4), hi = s32(code, p + 8);
            if (key < lo || key > hi) return ipc + def;
            return ipc + s32(code, p + 12 + (key - lo) * 4);
        }
        int n = s32(code, p + 4);
        int target = def;
        int l = 0, h = n - 1;
        while (l <= h) {
            int mid = (l + h) >>> 1, k = s32(code, p + 8 + mid * 8);
            if (k == key) { target = s32(code, p + 12 + mid * 8); break; }
            if (k < key) l = mid + 1; else h = mid - 1;
        }
        return ipc + target;
    }
}

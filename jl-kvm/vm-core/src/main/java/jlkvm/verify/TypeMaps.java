package jlkvm.verify;

import jlkvm.classfile.ClassFile;
import jlkvm.classfile.ClassFile.MethodInfo;
import jlkvm.classfile.ConstantPool;
import jlkvm.classfile.Descriptors;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * GC type maps (PLAN T09, D4): for a method and a bytecode offset, which locals and which operand-stack words hold a heap
 * reference. The maps come from the verifier's per-pc type state, so they are exact (precise GC, no conservative scan).
 *
 * <ul>
 * <li>{@link #at}: the state <em>before</em> the instruction at {@code pc} executes (what a frame looks like when a thread
 *     is switched out or a GC is triggered by that instruction, e.g. an allocation).</li>
 * <li>{@link #atCall}: the state of a caller frame <em>while the invoke at pc runs</em>: the receiver and arguments have
 *     been popped (they are the callee's locals now), everything below them is still the caller's.</li>
 * </ul>
 * A slot counts as a reference when its verification type is null, an object/array type, an uninitialized object or
 * uninitializedThis (all are heap addresses). Long/double halves, ints, floats and top are not references.
 *
 * Maps are computed once per method (first query, which verifies the method) and cached. Not thread-safe: used from the
 * single vm-thread (D5).
 */
public final class TypeMaps {
    /** Immutable reference bitmap of one frame: bit i = local i, bit (maxLocals + j) = operand stack word j. */
    public static final class RefMap {
        private final int locals, sp;
        private final int[] bits;

        RefMap(int locals, int sp, int[] bits) { this.locals = locals; this.sp = sp; this.bits = bits; }

        public int numLocals() { return locals; }
        /** Number of occupied 32-bit operand stack words (long/double count 2). */
        public int stackDepth() { return sp; }
        public boolean localIsRef(int i) { return i >= 0 && i < locals && bit(i); }
        public boolean stackIsRef(int j) { return j >= 0 && j < sp && bit(locals + j); }
        private boolean bit(int k) { return (bits[k >> 5] >>> (k & 31) & 1) != 0; }
        /** Packed bitmap, locals first then stack (copy). */
        public int[] bitmap() { return bits.clone(); }

        public int refCount() {
            int n = 0;
            for (int b : bits) n += Integer.bitCount(b);
            return n;
        }

        @Override public boolean equals(Object o) {
            if (!(o instanceof RefMap)) return false;
            RefMap r = (RefMap) o;
            return locals == r.locals && sp == r.sp && Arrays.equals(bits, r.bits);
        }
        @Override public int hashCode() { return (locals * 31 + sp) * 31 + Arrays.hashCode(bits); }

        /** Compact text form used by the unit tests, e.g. {@code "L:0,2 S:1 sp=3"}. */
        @Override public String toString() {
            StringBuilder b = new StringBuilder("L:");
            boolean any = false;
            for (int i = 0; i < locals; i++) if (localIsRef(i)) { b.append(any ? "," : "").append(i); any = true; }
            if (!any) b.append('-');
            b.append(" S:");
            any = false;
            for (int j = 0; j < sp; j++) if (stackIsRef(j)) { b.append(any ? "," : "").append(j); any = true; }
            if (!any) b.append('-');
            return b.append(" sp=").append(sp).toString();
        }
    }

    private static final class Entry {
        RefMap[] before;     // indexed by pc; null where there is no instruction or the code is unreachable
        RefMap[] call;       // lazily filled for invoke instructions
    }

    private final Verifier verifier;
    private final Map<MethodInfo, Entry> cache = new IdentityHashMap<MethodInfo, Entry>();

    public TypeMaps(Verifier verifier) { this.verifier = verifier; }

    /** @return number of methods whose maps are cached. */
    public int cachedMethods() { return cache.size(); }

    /**
     * Reference map before the instruction at {@code pc}.
     * @return null if {@code pc} is not the start of an instruction or the instruction is unreachable
     * @throws VerifyException if the method does not verify
     */
    public RefMap at(ClassFile cf, MethodInfo m, int pc) {
        Entry e = entry(cf, m);
        return pc >= 0 && pc < e.before.length ? e.before[pc] : null;
    }

    /**
     * Reference map of the caller frame while the invoke instruction at {@code pc} executes (receiver and arguments
     * removed). @return null as for {@link #at}; throws IllegalArgumentException if pc is not an invoke instruction.
     */
    public RefMap atCall(ClassFile cf, MethodInfo m, int pc) {
        Entry e = entry(cf, m);
        RefMap b = pc >= 0 && pc < e.before.length ? e.before[pc] : null;
        if (b == null) return null;
        if (e.call[pc] != null) return e.call[pc];
        byte[] code = m.code.code;
        int op = code[pc] & 0xff;
        if (op < 182 || op > 185) throw new IllegalArgumentException("not an invoke instruction at pc " + pc);
        ConstantPool cp = cf.pool;
        int idx = ((code[pc + 1] & 0xff) << 8) | (code[pc + 2] & 0xff);
        int words = Descriptors.paramWords(cp.refDescriptor(idx)) + (op == 184 ? 0 : 1);
        int sp = b.sp - words;
        if (sp < 0) throw new IllegalStateException("verifier state shallower than the call arguments at pc " + pc);
        int[] bits = new int[(b.locals + sp + 31) >> 5];
        for (int k = 0; k < b.locals + sp; k++) if (b.bit(k)) bits[k >> 5] |= 1 << (k & 31);
        RefMap r = new RefMap(b.locals, sp, bits);
        e.call[pc] = r;
        return r;
    }

    private Entry entry(ClassFile cf, MethodInfo m) {
        Entry e = cache.get(m);
        if (e != null) return e;
        Frame[] fr = verifier.verifyMethod(cf, m, true);
        e = new Entry();
        e.before = new RefMap[fr.length];
        e.call = new RefMap[fr.length];
        Map<RefMap, RefMap> canon = new HashMap<RefMap, RefMap>();
        for (int pc = 0; pc < fr.length; pc++) {
            Frame f = fr[pc];
            if (f == null) continue;
            int n = f.locals.length;
            int[] bits = new int[(n + f.sp + 31) >> 5];
            for (int i = 0; i < n; i++) if (f.locals[i].isRef()) bits[i >> 5] |= 1 << (i & 31);
            for (int j = 0; j < f.sp; j++) if (f.stack[j].isRef()) { int k = n + j; bits[k >> 5] |= 1 << (k & 31); }
            RefMap r = new RefMap(n, f.sp, bits);
            RefMap c = canon.get(r);
            if (c == null) { canon.put(r, r); c = r; }
            e.before[pc] = c;
        }
        cache.put(m, e);
        return e;
    }
}

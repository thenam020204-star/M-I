package jlkvm.heap;

/**
 * Guest heap (PLAN D3, T10): one {@code int[]} arena. A reference is the arena index of the object's first header word;
 * {@code 0} is null (words 0..{@link #BASE}-1 are never used). Layout is documented in {@code docs/heap-layout.md}.
 *
 * <pre>
 * object: [h0 flags|elemKind|classId] [h1 identity hash] [field words ...]
 * array : [h0 flags|elemKind|classId] [h1 identity hash] [length] [element words ...]
 * </pre>
 * Not thread-safe: only the vm-thread (D5) touches the heap.
 */
public final class Heap {
    public static final int BASE = 2;
    public static final int HEADER = 2;
    public static final int ARRAY_HEADER = 3;

    /** h0 bits 0..19: class id. */
    public static final int CLASS_MASK = 0xFFFFF;
    /** h0 bits 20..23: element kind, 0 for a plain object. */
    public static final int KIND_SHIFT = 20, KIND_MASK = 0xF << KIND_SHIFT;
    /** h0 bit 28: reserved for the collector (forwarded / pinned). bit 30: a monitor record exists. bit 31: GC mark. */
    public static final int FLAG_GC_RESERVED = 1 << 28, FLAG_MONITOR = 1 << 30, FLAG_MARK = 1 << 31;

    public static final int K_OBJECT = 0, K_BOOLEAN = 1, K_BYTE = 2, K_CHAR = 3, K_SHORT = 4, K_INT = 5, K_FLOAT = 6,
            K_LONG = 7, K_DOUBLE = 8, K_REF = 9;

    /** Called when an allocation does not fit; may compact so that {@code neededWords} become free (T13). */
    public interface Collector {
        void collect(Heap heap, int neededWords);
    }

    /** The arena. Public so that the interpreter and the collector can index it directly; the array is never replaced. */
    public final int[] mem;
    private final int limit;
    private int top = BASE;
    private int[] instWords = new int[64];
    private Collector collector;
    private int hashState;
    private int stressInterval, stressCount;
    private long allocatedWords, allocatedObjects;

    /** @param bytes heap budget (from the device profile); rounded down to whole words. */
    public Heap(long bytes, int hashSeed) {
        long w = bytes / 4;
        if (w < 64 || w > Integer.MAX_VALUE - 8) throw new IllegalArgumentException("heap size " + bytes);
        mem = new int[(int) w];
        limit = mem.length;
        hashState = hashSeed == 0 ? 0x2545F491 : hashSeed;
    }

    public void setCollector(Collector c) { collector = c; }

    /** Debug (VmConfig.gcStressInterval): run the collector before every {@code n}-th allocation; 0 = off. */
    public void setStressInterval(int n) { stressInterval = n; stressCount = 0; }

    /** Linker registers the number of field words (without header) of an instance class. */
    public void setInstanceWords(int classId, int words) {
        if (classId >= instWords.length) instWords = java.util.Arrays.copyOf(instWords, Math.max(classId + 1, instWords.length * 2));
        instWords[classId] = words;
    }

    public int instanceWords(int classId) { return instWords[classId]; }

    // ------------------------------------------------------------------ accounting (bytes)

    public long totalMemory() { return (long) (limit - BASE) * 4; }
    public long freeMemory() { return (long) (limit - top) * 4; }
    public int top() { return top; }
    public int limit() { return limit; }
    public long allocatedWordsTotal() { return allocatedWords; }
    public long allocatedObjectsTotal() { return allocatedObjects; }

    /** Collector support: set the allocation pointer after sliding objects down. */
    public void setTop(int t) {
        if (t < BASE || t > limit) throw new IllegalArgumentException();
        top = t;
    }

    // ------------------------------------------------------------------ allocation

    private int reserve(int words) {
        if (stressInterval > 0 && collector != null && ++stressCount >= stressInterval) {
            stressCount = 0;
            collector.collect(this, words);
        }
        if (words > limit - top) {
            if (collector != null) collector.collect(this, words);
            if (words > limit - top) return 0;
        }
        int r = top;
        top += words;
        allocatedWords += words;
        allocatedObjects++;
        return r;
    }

    /** @return object reference, or 0 when the heap is exhausted even after a collection (guest OutOfMemoryError). */
    public int allocObject(int classId) {
        int n = HEADER + instWords[classId];
        int r = reserve(n);
        if (r == 0) return 0;
        mem[r] = classId;
        mem[r + 1] = 0;
        java.util.Arrays.fill(mem, r + HEADER, r + n, 0);
        return r;
    }

    public static int dataWords(int kind, int length) {
        switch (kind) {
            case K_BOOLEAN: case K_BYTE: return (length + 3) >>> 2;
            case K_CHAR: case K_SHORT: return (length + 1) >>> 1;
            case K_LONG: case K_DOUBLE: return length << 1;
            default: return length;
        }
    }

    /** @param length must be >= 0 (the caller throws NegativeArraySizeException). @return 0 on exhaustion. */
    public int allocArray(int classId, int kind, int length) {
        long n = (long) ARRAY_HEADER + (kind == K_LONG || kind == K_DOUBLE ? 2L * length : kind == K_BOOLEAN || kind == K_BYTE ? (length + 3L) / 4 : kind == K_CHAR || kind == K_SHORT ? (length + 1L) / 2 : length);
        if (n > Integer.MAX_VALUE - 8) return 0;
        int r = reserve((int) n);
        if (r == 0) return 0;
        mem[r] = classId | (kind << KIND_SHIFT);
        mem[r + 1] = 0;
        mem[r + 2] = length;
        java.util.Arrays.fill(mem, r + ARRAY_HEADER, r + (int) n, 0);
        return r;
    }

    // ------------------------------------------------------------------ header

    public int classId(int ref) { return mem[ref] & CLASS_MASK; }
    public int kind(int ref) { return (mem[ref] & KIND_MASK) >>> KIND_SHIFT; }
    public boolean isArray(int ref) { return (mem[ref] & KIND_MASK) != 0; }
    public int arrayLength(int ref) { return mem[ref + 2]; }
    public boolean hasMonitor(int ref) { return (mem[ref] & FLAG_MONITOR) != 0; }
    public void setHasMonitor(int ref, boolean on) { if (on) mem[ref] |= FLAG_MONITOR; else mem[ref] &= ~FLAG_MONITOR; }
    public boolean isMarked(int ref) { return mem[ref] < 0; }
    public void setMarked(int ref, boolean on) { if (on) mem[ref] |= FLAG_MARK; else mem[ref] &= ~FLAG_MARK; }

    /** Size in words of the object at {@code ref} (header included). Works for dead objects too (heap is parsable). */
    public int sizeWords(int ref) {
        int h = mem[ref];
        int k = (h & KIND_MASK) >>> KIND_SHIFT;
        if (k == K_OBJECT) return HEADER + instWords[h & CLASS_MASK];
        return ARRAY_HEADER + dataWords(k, mem[ref + 2]);
    }

    /** First object of the heap, or 0 when empty. */
    public int firstObject() { return top > BASE ? BASE : 0; }
    /** Next object after {@code ref} in address order, or 0 at the end. */
    public int nextObject(int ref) { int n = ref + sizeWords(ref); return n < top ? n : 0; }

    /** Identity hash: assigned on first use, stored in the header, therefore stable across compaction. Never 0. */
    public int identityHash(int ref) {
        int h = mem[ref + 1];
        if (h == 0) {
            int x = hashState;
            x ^= x << 13; x ^= x >>> 17; x ^= x << 5;
            hashState = x;
            h = x & 0x7FFFFFFF;
            if (h == 0) h = 1;
            mem[ref + 1] = h;
        }
        return h;
    }

    // ------------------------------------------------------------------ instance fields (slot = word index within the field area)

    public int getInt(int ref, int slot) { return mem[ref + HEADER + slot]; }
    public void putInt(int ref, int slot, int v) { mem[ref + HEADER + slot] = v; }
    public float getFloat(int ref, int slot) { return Float.intBitsToFloat(mem[ref + HEADER + slot]); }
    public void putFloat(int ref, int slot, float v) { mem[ref + HEADER + slot] = Float.floatToRawIntBits(v); }
    public int getRef(int ref, int slot) { return mem[ref + HEADER + slot]; }
    public void putRef(int ref, int slot, int v) { mem[ref + HEADER + slot] = v; }
    /** long/double fields: high word first. */
    public long getLong(int ref, int slot) { int i = ref + HEADER + slot; return ((long) mem[i] << 32) | (mem[i + 1] & 0xFFFFFFFFL); }
    public void putLong(int ref, int slot, long v) { int i = ref + HEADER + slot; mem[i] = (int) (v >>> 32); mem[i + 1] = (int) v; }
    public double getDouble(int ref, int slot) { return Double.longBitsToDouble(getLong(ref, slot)); }
    public void putDouble(int ref, int slot, double v) { putLong(ref, slot, Double.doubleToRawLongBits(v)); }

    // ------------------------------------------------------------------ array elements (no bounds checks: the caller checks)

    public byte getByte(int a, int i) { return (byte) (mem[a + ARRAY_HEADER + (i >> 2)] >> ((i & 3) << 3)); }
    public void putByte(int a, int i, int v) {
        int w = a + ARRAY_HEADER + (i >> 2), s = (i & 3) << 3;
        mem[w] = (mem[w] & ~(0xFF << s)) | ((v & 0xFF) << s);
    }
    public boolean getBoolean(int a, int i) { return getByte(a, i) != 0; }
    public void putBoolean(int a, int i, boolean v) { putByte(a, i, v ? 1 : 0); }
    public char getChar(int a, int i) { return (char) (mem[a + ARRAY_HEADER + (i >> 1)] >>> ((i & 1) << 4)); }
    public void putChar(int a, int i, int v) {
        int w = a + ARRAY_HEADER + (i >> 1), s = (i & 1) << 4;
        mem[w] = (mem[w] & ~(0xFFFF << s)) | ((v & 0xFFFF) << s);
    }
    public short getShort(int a, int i) { return (short) getChar(a, i); }
    public void putShort(int a, int i, int v) { putChar(a, i, v); }
    public int getIntElem(int a, int i) { return mem[a + ARRAY_HEADER + i]; }
    public void putIntElem(int a, int i, int v) { mem[a + ARRAY_HEADER + i] = v; }
    public float getFloatElem(int a, int i) { return Float.intBitsToFloat(mem[a + ARRAY_HEADER + i]); }
    public void putFloatElem(int a, int i, float v) { mem[a + ARRAY_HEADER + i] = Float.floatToRawIntBits(v); }
    public int getRefElem(int a, int i) { return mem[a + ARRAY_HEADER + i]; }
    public void putRefElem(int a, int i, int v) { mem[a + ARRAY_HEADER + i] = v; }
    public long getLongElem(int a, int i) { int k = a + ARRAY_HEADER + (i << 1); return ((long) mem[k] << 32) | (mem[k + 1] & 0xFFFFFFFFL); }
    public void putLongElem(int a, int i, long v) { int k = a + ARRAY_HEADER + (i << 1); mem[k] = (int) (v >>> 32); mem[k + 1] = (int) v; }
    public double getDoubleElem(int a, int i) { return Double.longBitsToDouble(getLongElem(a, i)); }
    public void putDoubleElem(int a, int i, double v) { putLongElem(a, i, Double.doubleToRawLongBits(v)); }
}

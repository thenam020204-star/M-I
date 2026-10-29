package jlkvm.heap;

/**
 * Stack of references the host (natives, VM helpers) holds across an allocation. The collector treats every entry as a
 * root and may rewrite it (compaction moves objects), so host code must re-read the value with {@link #get} after any
 * call that can allocate. Usage: {@code int m = roots.mark(); int i = roots.push(ref); ...; roots.release(m);}
 */
public final class TempRoots {
    private int[] refs = new int[32];
    private int size;

    public int mark() { return size; }
    public void release(int mark) { size = mark; }
    public int push(int ref) {
        if (size == refs.length) refs = java.util.Arrays.copyOf(refs, size * 2);
        refs[size] = ref;
        return size++;
    }
    public int pop() { return refs[--size]; }
    public int get(int index) { return refs[index]; }
    public void set(int index, int ref) { refs[index] = ref; }
    public int size() { return size; }
}

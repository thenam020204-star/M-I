package jlkvm.natives;

/**
 * Host handle table (D8): guest objects keep an {@code int peer} that indexes this table. Ids are never 0.
 * {@code owner} is the guest reference whose death releases the entry (T13 sweeps entries of dead owners); 0 = never.
 */
public final class Handles {
    private Object[] objs = new Object[16];
    private int[] owners = new int[16];
    private int live;
    private int free = -1;
    private int[] next = new int[16];

    public int alloc(Object o, int ownerRef) {
        int id;
        if (free >= 0) { id = free; free = next[id]; }
        else {
            id = Math.max(1, firstUnused());
            if (id >= objs.length) {
                int n = objs.length * 2;
                objs = java.util.Arrays.copyOf(objs, n);
                owners = java.util.Arrays.copyOf(owners, n);
                next = java.util.Arrays.copyOf(next, n);
            }
        }
        objs[id] = o;
        owners[id] = ownerRef;
        live++;
        return id;
    }
    private int used = 1;
    private int firstUnused() { return used++; }

    public Object get(int id) { return id > 0 && id < objs.length ? objs[id] : null; }
    public void release(int id) {
        if (id <= 0 || id >= objs.length || objs[id] == null) return;
        objs[id] = null;
        owners[id] = 0;
        next[id] = free;
        free = id;
        live--;
    }
    public int liveCount() { return live; }
    public int capacity() { return used; }
    public int owner(int id) { return owners[id]; }
    public void setOwner(int id, int ref) { owners[id] = ref; }
    public Object rawAt(int id) { return objs[id]; }
}

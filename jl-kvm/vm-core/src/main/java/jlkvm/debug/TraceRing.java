package jlkvm.debug;

/** Ring buffer of the last executed instructions (D15 debug hook): thread, method id, pc, opcode. Size is a power of two. */
public final class TraceRing {
    private final int mask;
    private final int[] tid, mid, pc, op;
    private long n;

    public TraceRing(int size) {
        int s = 16;
        while (s < size) s <<= 1;
        mask = s - 1;
        tid = new int[s]; mid = new int[s]; pc = new int[s]; op = new int[s];
    }

    public void put(int t, int m, int p, int o) {
        int i = (int) n & mask;
        tid[i] = t; mid[i] = m; pc[i] = p; op[i] = o;
        n++;
    }

    public long count() { return n; }

    /** The last {@code max} entries, oldest first: "T<thread> <method id> @<pc> op=<opcode>" (the caller maps ids to names). */
    public int[][] last(int max) {
        int k = (int) Math.min(Math.min(n, max), mask + 1);
        int[][] r = new int[k][];
        for (int j = 0; j < k; j++) {
            int i = (int) (n - k + j) & mask;
            r[j] = new int[] {tid[i], mid[i], pc[i], op[i]};
        }
        return r;
    }
}

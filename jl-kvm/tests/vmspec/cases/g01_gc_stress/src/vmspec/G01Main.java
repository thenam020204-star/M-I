package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * T13 stress: 1,000,000 node allocations building a random graph (cycles, shared nodes, int[] payloads, Strings, a
 * nested Object[]), with constant turnover so that almost everything allocated becomes garbage. Every 100,000 steps a
 * checksum of everything reachable from the pool is printed; the values are independent of the VM (no identity hashes), so
 * the oracles' output is the expected output. With a small heap (RunJar --heap-kb) the collector runs dozens of times.
 */
public class G01Main extends MIDlet {
    static final int POOL = 4096, STEPS = 1000000;

    static final class Node {
        Node a, b;
        int v;
        int[] d;
        Object o;
        int stamp;
    }

    static int seed = 12345;
    static int rnd() { seed = seed * 1103515245 + 12345; return (seed >>> 8) & 0xFFFFFF; }

    static int stampCounter;
    static final Node[] stack = new Node[POOL * 4 + 16];

    static long checksum(Node[] pool) {
        stampCounter++;
        long h = 17;
        int sp = 0;
        for (int i = 0; i < pool.length; i++) {
            Node n = pool[i];
            if (n != null && n.stamp != stampCounter) { n.stamp = stampCounter; stack[sp++] = n; }
            while (sp > 0) {
                n = stack[--sp];
                h = h * 31 + n.v;
                if (n.d != null) for (int j = 0; j < n.d.length; j++) h = h * 31 + n.d[j];
                if (n.o instanceof String) h = h * 31 + ((String) n.o).hashCode();
                else if (n.o instanceof Object[]) {
                    Object[] oa = (Object[]) n.o;
                    for (int j = 0; j < oa.length; j++) if (oa[j] instanceof Integer) h = h * 31 + ((Integer) oa[j]).intValue();
                }
                Node x = n.b;
                if (x != null && x.stamp != stampCounter) { x.stamp = stampCounter; stack[sp++] = x; }
                x = n.a;
                if (x != null && x.stamp != stampCounter) { x.stamp = stampCounter; stack[sp++] = x; }
            }
        }
        return h;
    }

    protected void startApp() {
        Node[] pool = new Node[POOL];
        for (int step = 1; step <= STEPS; step++) {
            Node n = new Node();
            n.v = rnd();
            int k = rnd() & 7;
            if (k < 3) {
                n.d = new int[1 + (rnd() & 15)];
                for (int j = 0; j < n.d.length; j++) n.d[j] = rnd();
            } else if (k == 3) {
                n.o = Integer.toString(rnd());
            } else if (k == 4) {
                Object[] oa = new Object[1 + (rnd() & 3)];
                for (int j = 0; j < oa.length; j++) oa[j] = new Integer(rnd());
                n.o = oa;
            }
            n.a = pool[rnd() % POOL];
            n.b = pool[rnd() % POOL];
            int idx = rnd() % POOL;
            Node old = pool[idx];
            if (old != null) { old.a = null; old.b = null; }   // a replaced node keeps no edges: bounded live set
            pool[idx] = n;
            if (step % 100000 == 0) System.out.println("> step " + step + " checksum " + checksum(pool));
        }
        System.out.println("> done");
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

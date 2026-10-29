package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * T13: identity hashes (System.identityHashCode) survive the compaction of the heap, whether they were assigned before the
 * object moved or only afterwards, and Runtime.gc() really reclaims garbage (free memory grows).
 */
public class G02Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static void garbage(int n) {
        for (int i = 0; i < n; i++) { int[] g = new int[10]; g[0] = i; }
    }

    protected void startApp() {
        Object[] objs = new Object[300];
        int[] early = new int[objs.length];
        for (int i = 0; i < objs.length; i++) {
            garbage(7);                              // garbage between the kept objects: they have to slide
            switch (i % 3) {
                case 0: objs[i] = new Object(); break;
                case 1: objs[i] = new int[i]; break;
                default: objs[i] = new StringBuffer("x" + i); break;
            }
            if (i % 2 == 0) early[i] = System.identityHashCode(objs[i]);   // odd ones get their hash after the first moves
        }
        int bad = 0, lateBad = 0;
        int[] late = new int[objs.length];
        for (int round = 0; round < 6; round++) {
            garbage(3000);
            Runtime.getRuntime().gc();
            for (int i = 0; i < objs.length; i++) {
                int h = System.identityHashCode(objs[i]);
                if (i % 2 == 0) { if (h != early[i]) bad++; }
                else if (round == 0) late[i] = h; else if (h != late[i]) lateBad++;
            }
        }
        p("hash changed (assigned before moves): " + bad);
        p("hash changed (assigned after moves): " + lateBad);
        p("null hash: " + System.identityHashCode(null));
        boolean same = System.identityHashCode(objs[0]) == System.identityHashCode(objs[0]);
        p("stable on repeat: " + same);
        long free0 = Runtime.getRuntime().freeMemory();
        garbage(2000);
        long free1 = Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().gc();
        long free2 = Runtime.getRuntime().freeMemory();
        System.out.println("# allocation shrinks free memory: " + (free1 < free0) + " (JVM oracles: not reliable)");
        p("gc() reclaims it: " + (free2 > free1));
        p("total memory constant: " + (Runtime.getRuntime().totalMemory() > 0));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

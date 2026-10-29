package vmspec;

import javax.microedition.midlet.MIDlet;

/** T13/D3: when live data fills the heap the collector runs and, still short, the allocation throws OutOfMemoryError; after the data is dropped the VM allocates again. */
public class G06Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static final class Link { Link next; int[] block = new int[1000]; }

    protected void startApp() {
        long total = Runtime.getRuntime().totalMemory();
        Link head = null;
        int n = 0;
        boolean oom = false;
        long fr = 0;
        try {
            for (;;) { Link l = new Link(); l.next = head; head = l; n++; }
        } catch (OutOfMemoryError e) {
            oom = true;
            fr = Runtime.getRuntime().freeMemory();   // before anything allocates: `head` is a dead local after the loop, a later collection would free the chain
        }
        p("OutOfMemoryError thrown: " + oom);
        long linked = (long) n * 4020;      // bytes of live data: header 2 + 1 ref + array (3 + 1000) words
        p("filled most of the heap: " + (linked > total / 2 && linked <= total));
        p("free memory small: " + (fr < 8000));
        head = null;
        Runtime.getRuntime().gc();
        p("free memory after dropping: " + (Runtime.getRuntime().freeMemory() > total / 2));
        int[] again = new int[100000];
        p("allocates again: " + (again.length == 100000));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * Class initialisation and threads (JVMS 2nd ed. 5.5 steps 1-9): a thread that requests a class being initialised by another
 * thread waits until that initialisation completes (success: it sees the initialised class; failure: NoClassDefFoundError).
 * Lines that depend on thread timing are printed after join().
 */
public class I05Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String kind(Throwable t) {
        String n = t.getClass().getName();
        if (n.equals("java.lang.Error")) {
            String m = t.getMessage();
            int c = m == null ? -1 : m.indexOf(':');
            if (c > 0) return m.substring(0, c);
        }
        return n.substring(n.lastIndexOf('.') + 1);
    }

    static class Slow {
        static int x;
        static {
            p("Slow.clinit begin");
            try { Thread.sleep(300); } catch (InterruptedException e) { }
            x = 5;
            p("Slow.clinit end");
        }
    }

    static class SlowFail {
        static int y;
        static {
            p("SlowFail.clinit begin");
            try { Thread.sleep(300); } catch (InterruptedException e) { }
            if (true) throw new RuntimeException("boom");
            y = 1;
        }
    }

    static String t1a, t1b, ma, mb;

    protected void startApp() {
        Thread t = new Thread() {
            public void run() {
                p("T1 touches Slow");
                t1a = "T1 Slow.x=" + Slow.x;
                try { t1b = "T1 SlowFail.y=" + SlowFail.y; } catch (Throwable e) { t1b = "T1 SlowFail " + kind(e); }
            }
        };
        t.start();
        try { Thread.sleep(100); } catch (InterruptedException e) { }
        p("main touches Slow");
        ma = "main Slow.x=" + Slow.x;
        try { Thread.sleep(100); } catch (InterruptedException e) { }   // T1 is inside SlowFail's initialiser by now
        try { mb = "main SlowFail.y=" + SlowFail.y; } catch (Throwable e) { mb = "main SlowFail " + kind(e); }
        try { t.join(); } catch (InterruptedException e) { }
        p(t1a);
        p(t1b);
        p(ma);
        p(mb);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

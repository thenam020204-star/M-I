package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * T15: native methods registered by the host (keys "vmspec/Nat.add(II)I" ...): argument kinds, results, exceptions, a missing
 * native (guest Error on the call), and blocking natives that park ONLY the calling guest thread while the others keep running.
 */
public class Q01Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static volatile boolean stop;
    static int counter;
    static String order = "";

    static synchronized void done(String s) { order += s; }

    protected void startApp() {
        p("add " + Nat.add(40, 2));
        p("mix " + Nat.mix(1, 2L, 3.5f, 4.25));
        p("mixl " + Nat.mixl(-3, 1L << 40));
        byte[] b = new byte[6];
        p("fill " + Nat.fill(b, 10) + " " + b[0] + " " + b[5]);
        try { Nat.fill(null, 0); } catch (NullPointerException e) { p("fill(null) NPE"); }
        try { Nat.fail("boom"); } catch (IllegalArgumentException e) { p("fail " + e.getMessage()); }
        p("string " + Nat.newString(7) + " " + (Nat.newString(7) != Nat.newString(7)));
        try { Nat.nothing(); p("nothing returned"); } catch (Error e) { p("missing native -> " + e.getClass().getName()); }

        // a blocking native parks only this thread: the counter thread (which also allocates garbage, so collections run) goes on
        Thread cnt = new Thread(new Runnable() { public void run() {
            while (!stop) { counter++; byte[] g = new byte[48]; g[0] = 1; }
        } });
        cnt.start();
        Thread.yield();
        for (int i = 0; i < 5; i++) { Object junk = new int[100]; }
        byte[] buf = new byte[5];
        int before = counter;
        int r = Nat.blockFill(300, buf);
        int during = counter - before;
        p("blockFill returned " + r + " buf " + buf[0] + " " + buf[4]);
        p("other thread ran while blocked " + (during > 100));

        // two threads blocked at once: the shorter wait ends first
        final byte[] b1 = new byte[3], b2 = new byte[3];
        Thread t1 = new Thread(new Runnable() { public void run() { Nat.blockFill(250, b1); done("A"); } });
        Thread t2 = new Thread(new Runnable() { public void run() { Nat.blockFill(80, b2); done("B"); } });
        t1.start(); t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { }
        p("order " + order + " " + b1[0] + " " + b2[0]);

        try { Nat.blockThrow(3); p("no exception"); } catch (IllegalArgumentException e) { p("blockThrow " + e.getMessage()); }
        p("blockLong " + Nat.blockLong(21L));
        stop = true;
        try { cnt.join(); } catch (InterruptedException e) { }
        notifyDestroyed();
    }
    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

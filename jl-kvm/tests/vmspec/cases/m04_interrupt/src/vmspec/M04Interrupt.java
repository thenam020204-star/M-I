package vmspec;

import javax.microedition.midlet.MIDlet;

/** Thread.interrupt() is CLDC 1.1 (JSR-139). Neither isInterrupted() nor interrupted() exist in CLDC. */
public class M04Interrupt extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static final Object L = new Object();

    protected void startApp() {
        Thread s = new Thread(new Runnable() {
            public void run() {
                try { Thread.sleep(5000); p("sleep finished (wrong)"); } catch (InterruptedException e) { p("sleep InterruptedException"); }
            }
        });
        s.start();
        sleep(200);
        s.interrupt();
        join(s);
        Thread w = new Thread(new Runnable() {
            public void run() {
                synchronized (L) {
                    try { L.wait(); p("wait returned (wrong)"); } catch (InterruptedException e) { p("wait InterruptedException"); }
                }
            }
        });
        w.start();
        sleep(200);
        w.interrupt();
        join(w);
        Thread j = new Thread(new Runnable() {
            public void run() {
                try { Thread.sleep(300); } catch (InterruptedException e) { }
            }
        });
        Thread jj = new Thread(new Runnable() {
            public void run() {
                try { j2.join(); p("join returned"); } catch (InterruptedException e) { p("join InterruptedException"); }
            }
        });
        j2 = j;
        j.start();
        jj.start();
        sleep(100);
        jj.interrupt();
        join(jj);
        join(j);
        notifyDestroyed();
    }

    static Thread j2;

    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }

    static void join(Thread t) {
        try { t.join(); } catch (InterruptedException e) { }
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

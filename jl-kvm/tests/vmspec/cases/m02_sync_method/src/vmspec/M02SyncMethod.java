package vmspec;

import javax.microedition.midlet.MIDlet;

public class M02SyncMethod extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static int counter;
    static final Object LOCK = new Object();

    synchronized void inst(String who) {
        p(who + " enter inst");
        sleep(300);
        p(who + " leave inst");
    }

    static synchronized void stat(String who) {
        p(who + " enter static");
        sleep(300);
        p(who + " leave static");
    }

    synchronized void reentrant(int n) {
        if (n > 0) reentrant(n - 1); else p("reentrant bottom");
    }

    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { p("interrupted"); }
    }

    protected void startApp() {
        final M02SyncMethod self = this;
        reentrant(5);
        Thread t = new Thread(new Runnable() {
            public void run() { self.inst("T"); }
        });
        t.start();
        sleep(100);                 // T is inside inst()
        self.inst("main");          // must wait for T
        try { t.join(); } catch (InterruptedException e) { }
        // static synchronized locks the Class object, independent of the instance lock
        Thread t2 = new Thread(new Runnable() {
            public void run() { stat("T2"); }
        });
        t2.start();
        sleep(100);
        synchronized (self) { p("main holds instance lock while T2 is in static"); }
        stat("main");
        try { t2.join(); } catch (InterruptedException e) { }
        // 4 threads incrementing under the lock: no lost updates
        Thread[] ts = new Thread[4];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(new Runnable() {
                public void run() {
                    for (int k = 0; k < 2000; k++) synchronized (LOCK) { int c = counter; counter = c + 1; }
                }
            });
            ts[i].start();
        }
        for (int i = 0; i < ts.length; i++) try { ts[i].join(); } catch (InterruptedException e) { }
        p("counter " + counter);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

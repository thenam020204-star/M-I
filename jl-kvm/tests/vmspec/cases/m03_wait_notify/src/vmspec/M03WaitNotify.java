package vmspec;

import javax.microedition.midlet.MIDlet;

public class M03WaitNotify extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static final Object L = new Object();
    static boolean ready;

    protected void startApp() {
        // 1) timed wait with no notify: returns after >= ~timeout
        long t0 = System.currentTimeMillis();
        synchronized (L) {
            try { L.wait(200); } catch (InterruptedException e) { p("interrupted"); }
        }
        long dt = System.currentTimeMillis() - t0;
        p("timed wait elapsed ok " + (dt >= 180 && dt < 1500));
        // 2) notify wakes a waiter, which re-acquires the lock only after the notifier leaves
        Thread w = new Thread(new Runnable() {
            public void run() {
                synchronized (L) {
                    p("W waiting");
                    while (!ready) {
                        try { L.wait(); } catch (InterruptedException e) { p("W interrupted"); }
                    }
                    p("W woke");
                }
            }
        });
        w.start();
        sleep(200);
        synchronized (L) {
            ready = true;
            L.notify();
            p("N notified, still holding lock");
            sleep(200);
            p("N leaving");
        }
        try { w.join(); } catch (InterruptedException e) { }
        // 3) notifyAll wakes all waiters
        final int[] woke = new int[1];
        Thread[] ws = new Thread[3];
        ready = false;
        for (int i = 0; i < ws.length; i++) {
            ws[i] = new Thread(new Runnable() {
                public void run() {
                    synchronized (L) {
                        while (!ready) {
                            try { L.wait(); } catch (InterruptedException e) { }
                        }
                        woke[0]++;
                    }
                }
            });
            ws[i].start();
        }
        sleep(200);
        synchronized (L) { ready = true; L.notifyAll(); }
        for (int i = 0; i < ws.length; i++) try { ws[i].join(); } catch (InterruptedException e) { }
        p("notifyAll woke " + woke[0]);
        // 4) wait(0) == wait forever; wait(-1) -> IllegalArgumentException
        synchronized (L) {
            try { L.wait(-1); p("wait(-1) ok"); } catch (IllegalArgumentException e) { p("wait(-1) IllegalArgumentException"); } catch (InterruptedException e) { }
        }
        notifyDestroyed();
    }

    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

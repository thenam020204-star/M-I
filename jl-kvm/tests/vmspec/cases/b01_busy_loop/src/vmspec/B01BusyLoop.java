package vmspec;

import javax.microedition.midlet.MIDlet;

/** A thread that spins without sleep/yield must be pre-empted (time slicing by bytecode count on KVM). */
public class B01BusyLoop extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static boolean done;
    static int spins;

    static synchronized boolean isDone() { return done; }

    static synchronized void setDone() { done = true; }

    protected void startApp() {
        Thread spinner = new Thread(new Runnable() {
            public void run() {
                while (!isDone()) spins++;
                p("spinner ended");
            }
        });
        spinner.start();
        try { Thread.sleep(300); } catch (InterruptedException e) { }
        p("main after sleep, spinner made progress " + (spins > 0));
        setDone();
        try { spinner.join(); } catch (InterruptedException e) { }
        // two spinners and a main that only counts: all make progress
        final int[] c = new int[2];
        final boolean[] stop = new boolean[1];
        Thread[] ts = new Thread[2];
        for (int i = 0; i < 2; i++) {
            final int k = i;
            ts[i] = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        synchronized (stop) { if (stop[0]) break; }
                        c[k]++;
                    }
                }
            });
            ts[i].start();
        }
        try { Thread.sleep(300); } catch (InterruptedException e) { }
        synchronized (stop) { stop[0] = true; }
        for (int i = 0; i < 2; i++) try { ts[i].join(); } catch (InterruptedException e) { }
        p("both spinners ran " + (c[0] > 0 && c[1] > 0));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * T16 / D14: a game-like loop that takes input events (type 0 = quit) while another thread ticks on a timer. Printed lines start
 * with "# " (the whole run is compared by vm-core InputReplayTest: record once, replay twice, outputs must be identical).
 */
public class D01Main extends MIDlet {
    static volatile int ticks;
    static volatile boolean stop;

    protected void startApp() {
        Thread ticker = new Thread(new Runnable() { public void run() {
            while (!stop) { try { Thread.sleep(10); } catch (InterruptedException e) { } ticks++; }
        } });
        ticker.start();
        int[] e = new int[4];
        long t0 = System.currentTimeMillis();
        int n = 0, sum = 0;
        for (;;) {
            Ev.nextEvent(e);
            if (e[0] == 0) break;
            n++;
            sum += e[1] * 31 + e[2];
            System.out.println("# event " + e[0] + " " + e[1] + " " + e[2] + " " + e[3] + " ticks=" + ticks + " dt=" + (System.currentTimeMillis() - t0));
        }
        stop = true;
        try { ticker.join(); } catch (InterruptedException x) { }
        System.out.println("# events " + n + " sum " + sum + " ticks=" + ticks);
        System.out.println("> done");
        notifyDestroyed();
    }
    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

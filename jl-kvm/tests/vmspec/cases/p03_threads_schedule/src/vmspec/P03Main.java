package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * T14: threads that depend on the scheduler (busy loops without sleep, sleeps of different length, yield, identity hashes,
 * the clock). With the deterministic mode and the same seed every run must print exactly the same lines (vm-core ThreadTest
 * runs it 10 times); the output is NOT meant to match another VM, the case is spec-only.
 */
public class P03Main extends MIDlet {
    static final StringBuffer log = new StringBuffer();
    static synchronized void note(String s) { log.append(s).append('\n'); }

    static Thread worker(final String name, final int loops, final int sleepMs, final int prio) {
        Thread t = new Thread(new Runnable() { public void run() {
            int x = 0;
            for (int i = 0; i < loops; i++) {
                for (int j = 0; j < 3000; j++) x += j ^ i;      // busy: only preemption by bytecode count separates the threads
                note(name + " step " + i + " t=" + (System.currentTimeMillis() / 10 % 1000));
                if (sleepMs > 0) { try { Thread.sleep(sleepMs); } catch (InterruptedException e) { } }
                else Thread.yield();
            }
            note(name + " done x=" + x);
        } });
        t.setPriority(prio);
        return t;
    }

    protected void startApp() {
        Thread[] ts = {worker("A", 6, 0, 5), worker("B", 5, 3, 5), worker("C", 4, 7, 5), worker("D", 5, 0, 4), worker("E", 3, 2, 6)};
        for (int i = 0; i < ts.length; i++) ts[i].start();
        for (int i = 0; i < ts.length; i++) { try { ts[i].join(); } catch (InterruptedException e) { } }
        Object o = new Object();
        note("hash " + o.hashCode() + " " + new Object().hashCode());
        String all = log.toString();
        StringBuffer out = new StringBuffer();
        for (int i = 0, st = 0; i < all.length(); i++) {
            if (all.charAt(i) == '\n') { out.append("# ").append(all.substring(st, i)).append('\n'); st = i + 1; }
        }
        System.out.print(out.toString());   // "# " lines are informational (compared only between runs)
        System.out.println();
        System.out.println("> finished");
        notifyDestroyed();
    }
    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

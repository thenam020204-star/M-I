package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * T14: deliberate deadlock (two threads, two locks, opposite order, each holds its first lock before asking for the second).
 * A debug VM reports it (Vm.lastDeadlock, tests/.../ThreadTest); the program itself ends through Runtime.exit after 500 ms.
 */
public class P02Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static final Object A = new Object(), B = new Object();

    static Thread locker(final Object first, final Object second, final String name) {
        return new Thread(new Runnable() { public void run() {
            synchronized (first) {
                p(name + " holds first");
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                synchronized (second) { p(name + " got second (never printed)"); }
            }
        } });
    }

    protected void startApp() {
        locker(A, B, "T1").start();
        locker(B, A, "T2").start();
        try { Thread.sleep(500); } catch (InterruptedException e) { }
        p("main exits while T1 and T2 are deadlocked");
        Runtime.getRuntime().exit(0);
    }
    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

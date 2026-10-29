package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * Deep recursion. CLDC has no StackOverflowError in its minimum class list, so the guest-visible class of the
 * thrown Error is implementation-specific: only the fact that a Throwable is thrown and the VM survives is checked
 * (the class name goes on an informational '# ' line).
 */
public class T01StackOverflow extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static int depth;

    static void rec() {
        depth++;
        rec();
    }

    protected void startApp() {
        Throwable caught = null;
        try { rec(); } catch (Throwable t) { caught = t; }
        p("thrown " + (caught != null));
        p("error-subclass " + (caught instanceof Error));
        p("depth>100 " + (depth > 100));
        System.out.println("# class=" + (caught == null ? "-" : caught.getClass().getName()) + " depth=" + depth);
        // VM is still usable afterwards, and a second overflow behaves the same
        depth = 0;
        Throwable again = null;
        try { rec(); } catch (Throwable t) { again = t; }
        p("second thrown " + (again != null));
        // another thread is not affected
        final boolean[] ok = new boolean[1];
        Thread t = new Thread(new Runnable() {
            public void run() { ok[0] = true; }
        });
        t.start();
        try { t.join(); } catch (InterruptedException e) { }
        p("thread ok " + ok[0]);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

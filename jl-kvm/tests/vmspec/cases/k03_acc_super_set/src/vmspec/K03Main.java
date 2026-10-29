package vmspec;

import javax.microedition.midlet.MIDlet;

/** Loads the generated raw-bytecode class vmspec.K03C (see tests/vmspec/gen/RawGen.java) and runs it. */
public class K03Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        try {
            Runnable r = (Runnable) Class.forName("vmspec.K03C").newInstance();
            r.run();
            p("main completed");
        } catch (Throwable t) {
            p("main caught " + t.getClass().getName());
        }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

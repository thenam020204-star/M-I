package vmspec;

import javax.microedition.midlet.MIDlet;

/** Loads the generated raw-bytecode class vmspec.M05D (see tests/vmspec/gen/RawGen.java) and runs it. */
public class M05Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        try {
            Runnable r = (Runnable) Class.forName("vmspec.M05D").newInstance();
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

package vmspec;

import javax.microedition.midlet.MIDlet;

/** Loads the generated raw-bytecode class vmspec.O02D (see tests/vmspec/gen/RawGen.java) and runs it. */
public class O02Main extends MIDlet {
    protected void startApp() {
        try {
            ((Runnable) Class.forName("vmspec.O02D").newInstance()).run();
            System.out.println("> main completed");
        } catch (Throwable t) {
            System.out.println("> main caught " + t.getClass().getName());
        }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

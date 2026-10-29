package vmspec;

import javax.microedition.midlet.MIDlet;

/** Loads the generated raw-bytecode class vmspec.O03D (see tests/vmspec/gen/RawGen.java) and runs it. */
public class O03Main extends MIDlet {
    protected void startApp() {
        try {
            ((Runnable) Class.forName("vmspec.O03D").newInstance()).run();
            System.out.println("> main completed");
        } catch (Throwable t) {
            System.out.println("> main caught " + t.getClass().getName());
        }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

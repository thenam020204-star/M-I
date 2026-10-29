package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * CLDC 1.1 spec 3.4.2.2: "the application programmer cannot override, modify, or add any classes to these protected system
 * packages" (java.*, javax.microedition.*, manufacturer packages). The jar contains java/lang/L03Evil, javax/microedition/L03Evil,
 * com/nokia/mid/L03Evil (valid class files) and vmspec/L03Ok (control). The system ones must not load.
 */
public class L03Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static void load(String n) {
        try {
            Class.forName(n);
            p(n + " loaded");
        } catch (Throwable t) {
            String c = t.getClass().getName();
            p(n + " refused");
            System.out.println("# " + c);
        }
    }

    protected void startApp() {
        load("vmspec.L03Ok");
        load("java.lang.L03Evil");
        load("javax.microedition.L03Evil");
        load("com.nokia.mid.L03Evil");
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

package vmspec;

import javax.microedition.midlet.MIDlet;

/** T18: ROM-private helper classes (jlkvm.internal.*) do not exist for the application; the CLDC classes do. */
public class Cl08RomPrivate extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String load(String n) {
        try { return "found " + Class.forName(n).getName(); } catch (ClassNotFoundException e) { return "ClassNotFoundException"; } catch (Throwable t) { return t.getClass().getName(); }
    }

    protected void startApp() {
        p("jlkvm.internal.Encoding " + load("jlkvm.internal.Encoding"));
        p("jlkvm.internal.io.ConnectionBase " + load("jlkvm.internal.io.ConnectionBase"));
        p("jlkvm.Anything " + load("jlkvm.Anything"));
        p("java.util.Vector " + load("java.util.Vector"));
        p("java.util.ArrayList " + load("java.util.ArrayList"));
        p("java.lang.String[] " + load("[Ljava.lang.String;"));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

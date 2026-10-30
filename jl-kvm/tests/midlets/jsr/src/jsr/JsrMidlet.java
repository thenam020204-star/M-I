package jsr;

import javax.microedition.midlet.MIDlet;

/**
 * T32a host test (JsrHostTest): the layers exist only when the profile declares them (D10); sms in the mode of the profile (client send, server
 * receive / listener with messages pushed by the test host through the script); FileConnection against the host file system (sandbox).
 * Each line starts with "> ". The compile-time stubs are the open jars (J2MEC_EXTRA by build.sh).
 */
public class JsrMidlet extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    static boolean has(String cls) {
        try { Class.forName(cls); return true; } catch (ClassNotFoundException e) { return false; }
    }

    public void startApp() {
        p("layers wma " + has("javax.wireless.messaging.MessageConnection") + " fc " + has("javax.microedition.io.file.FileConnection") + " pim " + has("javax.microedition.pim.PIM"));
        // the parts that use an API live in their own classes: a layer the profile does not declare is a NoClassDefFoundError only when they are loaded
        if (has("javax.wireless.messaging.MessageConnection")) new SmsPart().run();
        if (has("javax.microedition.io.file.FileConnection")) new FilePart().run();
        p("done");
        notifyDestroyed();
    }

    public void pauseApp() {}
    public void destroyApp(boolean u) {}
}

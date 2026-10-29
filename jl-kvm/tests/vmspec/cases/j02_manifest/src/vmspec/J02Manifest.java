package vmspec;

import javax.microedition.midlet.MIDlet;

/** Manifest attributes: continuation lines (leading space) are joined; lookups are case-insensitive on the name. */
public class J02Manifest extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        p("MIDlet-Name=" + getAppProperty("MIDlet-Name"));
        p("Long=" + getAppProperty("Long-Attribute"));
        p("Missing=" + getAppProperty("No-Such-Attribute"));
        p("Empty=[" + getAppProperty("Empty-Attribute") + "]");
        p("Spaces=[" + getAppProperty("Spaces-Attribute") + "]");
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

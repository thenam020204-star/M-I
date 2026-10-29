package vmspec;

import javax.microedition.midlet.MIDlet;

/** Class names differing only in case, and names of obfuscated games ($, _, single letters, digits). */
public class J03ClassNames extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        p(new Aa().id() + " " + new aA().id() + " " + new AA().id() + " " + new aa().id());
        p(new a().id() + " " + new A().id() + " " + new _().id() + " " + new $().id() + " " + new a1().id());
        p("forName " + load("vmspec.Aa") + load("vmspec.aA") + load("vmspec.AA") + load("vmspec.aa") + load("vmspec.AA2"));
        notifyDestroyed();
    }

    static String load(String n) {
        try { return Class.forName(n).newInstance() != null ? "T" : "?"; } catch (Exception e) { return "[" + e.getClass().getName() + "]"; }
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

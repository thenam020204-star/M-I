package bad;

import javax.microedition.midlet.MIDlet;

/** Deliberately NOT J2ME-SAFE: uses String.isEmpty() (Java 6) and java.util.ArrayList (not in CLDC). */
public class BadMIDlet extends MIDlet {
    protected void startApp() {
        java.util.ArrayList list = new java.util.ArrayList();
        list.add("x");
        if ("".isEmpty()) System.out.println("empty");
        System.out.println(list.size());
        notifyDestroyed();
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean u) {
    }
}

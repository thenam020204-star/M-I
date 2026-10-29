package clean;

import javax.microedition.midlet.MIDlet;

/** Uses only CLDC 1.1 / MIDP 2.0 API (StringBuffer, Vector, Hashtable). */
public class CleanMIDlet extends MIDlet {
    protected void startApp() {
        java.util.Vector v = new java.util.Vector();
        v.addElement("x");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < v.size(); i++) sb.append(v.elementAt(i));
        if (sb.length() == 0) System.out.println("empty");
        System.out.println(sb.toString());
        notifyDestroyed();
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean u) {
    }
}

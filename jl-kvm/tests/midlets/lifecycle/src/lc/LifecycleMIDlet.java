package lc;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Life cycle order (T21). Driven by a script (pause / resume / destroy) on host-desktop; keys: 1 = notifyPaused (+ resumeRequest 100 ms
 * later from another thread), 2 = refuse the next conditional destroy, 3 = notifyDestroyed.
 */
public class LifecycleMIDlet extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    int starts;
    boolean refuse;

    class C extends Canvas {
        protected void paint(Graphics g) { g.setColor(0x00FF00); g.fillRect(0, 0, getWidth(), getHeight()); }
        protected void showNotify() { p("showNotify"); }
        protected void hideNotify() { p("hideNotify"); }
        protected void keyPressed(int k) {
            p("key " + k);
            if (k == 49) {
                p("notifyPaused");
                notifyPaused();
                new Thread() { public void run() { try { Thread.sleep(100); } catch (InterruptedException e) { } p("resumeRequest"); resumeRequest(); } }.start();
            } else if (k == 50) refuse = true;
            else if (k == 51) { p("notifyDestroyed"); notifyDestroyed(); }
        }
    }

    public LifecycleMIDlet() { p("constructor"); }

    protected void startApp() throws MIDletStateChangeException {
        starts++;
        p("startApp #" + starts);
        if (starts > 1) return;
        p("name=" + getAppProperty("MIDlet-Name") + " vendor=" + getAppProperty("MIDlet-Vendor") + " shared=" + getAppProperty("Shared")
            + " jadOnly=" + getAppProperty("Jad-Only") + " manifestOnly=" + getAppProperty("Manifest-Only") + " missing=" + getAppProperty("No-Such"));
        p("name lower-case lookup=" + getAppProperty("midlet-name"));
        try { getAppProperty(null); } catch (NullPointerException e) { p("getAppProperty(null) NullPointerException"); }
        try { p("platformRequest http=" + platformRequest("http://example.org/")); } catch (ConnectionNotFoundException e) { p("platformRequest http CNFE"); }
        try { platformRequest("foo:bar"); p("platformRequest foo no exception"); } catch (ConnectionNotFoundException e) { p("platformRequest foo ConnectionNotFoundException"); }
        p("permission=" + checkPermission("javax.microedition.io.Connector.http"));
        Display.getDisplay(this).setCurrent(new C());
    }

    protected void pauseApp() { p("pauseApp"); }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        p("destroyApp unconditional=" + unconditional);
        if (refuse && !unconditional) { refuse = false; p("refuses"); throw new MIDletStateChangeException("not now"); }
    }
}

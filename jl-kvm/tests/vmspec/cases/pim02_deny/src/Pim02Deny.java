package vmspec;

import javax.microedition.midlet.MIDlet;
import javax.microedition.pim.PIM;

/** T32a: profile pim.permission deny: the lists cannot be listed or opened. */
public class Pim02Deny extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    protected void startApp() {
        PIM pim = PIM.getInstance();
        try { pim.listPIMLists(PIM.CONTACT_LIST); p("list: ok"); } catch (Throwable t) { p("list: " + cn(t)); }
        try { pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY); p("open: ok"); } catch (Throwable t) { p("open: " + cn(t)); }
        try { pim.openPIMList(PIM.EVENT_LIST, PIM.READ_ONLY, "Calendar"); p("open named: ok"); } catch (Throwable t) { p("open named: " + cn(t)); }
        try { pim.openPIMList(9, PIM.READ_ONLY); p("bad type: ok"); } catch (Throwable t) { p("bad type: " + cn(t)); }
        notifyDestroyed();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}

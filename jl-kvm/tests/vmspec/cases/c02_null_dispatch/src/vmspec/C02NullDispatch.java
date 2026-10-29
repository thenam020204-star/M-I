package vmspec;

import javax.microedition.midlet.MIDlet;

public class C02NullDispatch extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    interface I { void m(); }
    static class C { int f; void v() { } }
    static I ni = null;
    static C nc = null;
    static int[] na = null;
    static Object no = null;
    static Throwable nt = null;

    protected void startApp() {
        try { ni.m(); p("invokeinterface ok"); } catch (NullPointerException e) { p("invokeinterface NullPointerException"); }
        try { nc.v(); p("invokevirtual ok"); } catch (NullPointerException e) { p("invokevirtual NullPointerException"); }
        try { p("getfield " + nc.f); } catch (NullPointerException e) { p("getfield NullPointerException"); }
        try { nc.f = 1; p("putfield ok"); } catch (NullPointerException e) { p("putfield NullPointerException"); }
        try { p("arraylength " + na.length); } catch (NullPointerException e) { p("arraylength NullPointerException"); }
        try { p("iaload " + na[0]); } catch (NullPointerException e) { p("iaload NullPointerException"); }
        try { throw nt; } catch (NullPointerException e) { p("athrow-null NullPointerException"); } catch (Throwable t) { p("athrow-null other"); }
        try { synchronized (no) { p("monitorenter ok"); } } catch (NullPointerException e) { p("monitorenter NullPointerException"); }
        try { p("hashCode " + no.hashCode()); } catch (NullPointerException e) { p("hashCode NullPointerException"); }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

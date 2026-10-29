package vmspec;

import javax.microedition.midlet.MIDlet;

public class I03ClinitFailure extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static boolean fail = true;

    static class F {
        static int v = compute();
        static int compute() {
            if (I03ClinitFailure.fail) throw new RuntimeException("boom");
            return 1;
        }
        static void touch() { p("F.touch"); }
    }

    static class G extends F { }

    // NOTE: HotSpot-based oracles throw ExceptionInInitializerError first and NoClassDefFoundError afterwards.
    // CLDC 1.1 has no ExceptionInInitializerError (see PLAN D7/T12): the target mapping is UNVERIFIED.
    static String cls(Throwable t) {
        String n = t.getClass().getName();
        return n;
    }

    protected void startApp() {
        try { F.touch(); p("first ok"); } catch (Throwable t) { p("first " + cls(t)); }
        try { F.touch(); p("second ok"); } catch (Throwable t) { p("second " + cls(t)); }
        try { new G(); p("sub ok"); } catch (Throwable t) { p("sub " + cls(t)); }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

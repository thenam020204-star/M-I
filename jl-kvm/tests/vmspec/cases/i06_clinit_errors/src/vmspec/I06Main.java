package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * Failing <clinit> (JVMS 2nd ed. 5.5 step 5-6, 9-11): an Error thrown by the initialiser is rethrown as it is, any other
 * exception becomes ExceptionInInitializerError (not in CLDC: java.lang.Error with the JVMS name at the start of the message,
 * CLDC 1.1 spec 4.2/5.1.4); the class is then unusable and every later use gives NoClassDefFoundError; a subclass of such a
 * class fails the same way; an exception caught inside <clinit> does not fail the initialisation.
 */
public class I06Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String kind(Throwable t) {
        String n = t.getClass().getName();
        if (n.equals("java.lang.Error")) {
            String m = t.getMessage();
            int c = m == null ? -1 : m.indexOf(':');
            if (c > 0) return m.substring(0, c);
        }
        return n.substring(n.lastIndexOf('.') + 1);
    }

    static class MyErr extends Error { }

    static class ThrowsError { static int v = 1; static { if (v == 1) throw new MyErr(); } static void touch() { } }
    static class ThrowsOom { static int v = 1; static { if (v == 1) throw new OutOfMemoryError(); } static void touch() { } }
    static class Base { static int v = 1; static { if (v == 1) throw new IllegalArgumentException("x"); } static void touch() { } }
    static class Sub extends Base { static void subTouch() { } }
    static class Sub2 extends Base { }
    static class ViaForName { static int v = 1; static { if (v == 1) throw new RuntimeException(); } }
    static class Catches {
        static int state;
        static {
            try { throw new RuntimeException(); } catch (RuntimeException e) { state = 7; }
        }
    }
    static class Order {
        static int a = log("Order.a");
        static int log(String s) { p(s); return 1; }
    }

    interface Step { void run() throws Throwable; }

    static void attempt(String label, Step s) {
        try {
            s.run();
            p(label + " ok");
        } catch (Throwable t) {
            p(label + " " + kind(t));
        }
    }

    protected void startApp() {
        attempt("ThrowsError 1", new Step() { public void run() { ThrowsError.touch(); } });
        attempt("ThrowsError 2", new Step() { public void run() { ThrowsError.touch(); } });
        attempt("ThrowsOom 1", new Step() { public void run() { ThrowsOom.touch(); } });
        attempt("ThrowsOom 2", new Step() { public void run() { ThrowsOom.touch(); } });
        attempt("new Sub 1", new Step() { public void run() { Sub.subTouch(); } });
        attempt("new Sub 2", new Step() { public void run() { Sub.subTouch(); } });
        attempt("Base 1", new Step() { public void run() { Base.touch(); } });
        attempt("new Sub2", new Step() { public void run() { new Sub2(); } });
        attempt("forName 1", new Step() { public void run() throws Throwable { Class.forName("vmspec.I06Main$ViaForName"); } });
        attempt("forName 2", new Step() { public void run() throws Throwable { Class.forName("vmspec.I06Main$ViaForName"); } });
        attempt("Catches", new Step() { public void run() { p("state=" + Catches.state); } });
        attempt("Order", new Step() { public void run() { p("a=" + Order.a); } });
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

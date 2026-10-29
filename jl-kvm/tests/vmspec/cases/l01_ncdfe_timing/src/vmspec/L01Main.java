package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * When NoClassDefFoundError is thrown (JVMS 5.3 / 5.4: classes are resolved at first execution of the instruction that names
 * them; the failure is catchable at that point and repeats on every attempt). vmspec.Gone and vmspec.GoneSuper were removed
 * from the jar after compilation (build.sh). Mentioning them in signatures, fields or unexecuted branches must not fail.
 */
public class L01Main extends MIDlet {
    static boolean never = false;
    static Gone fieldOfMissingType;                 // field type is never loaded
    static void takesMissing(Gone g) { }           // parameter type is never loaded

    static void p(String s) { System.out.println("> " + s); }

    static String name(Throwable t) {
        String n = t.getClass().getName();
        return n.substring(n.lastIndexOf('.') + 1);
    }

    static Object useNew() { return new Gone(); }
    static void useStatic() { Gone.sm(); }
    static int useField() { return Gone.F; }
    static boolean useInstanceof(Object o) { return o instanceof Gone; }
    static Object useCast(Object o) { return (Gone) o; }
    static Object useArray() { return new Gone[1]; }
    static Object useSub() { return new Sub(); }

    interface Step { void run() throws Throwable; }

    static void attempt(String label, Step s) {
        try {
            s.run();
            p(label + " ok");
        } catch (Throwable t) {
            p(label + " " + name(t));
        }
    }

    protected void startApp() {
        p("start");
        if (never) { useNew(); useStatic(); }     // the instructions naming Gone are not executed
        p("unexecuted branch ok");
        attempt("new", new Step() { public void run() { useNew(); } });
        attempt("new again", new Step() { public void run() { useNew(); } });
        attempt("invokestatic", new Step() { public void run() { useStatic(); } });
        attempt("getstatic", new Step() { public void run() { useField(); } });
        attempt("instanceof null", new Step() { public void run() { useInstanceof(null); } });
        attempt("instanceof object", new Step() { public void run() { useInstanceof("x"); } });
        attempt("checkcast null", new Step() { public void run() { useCast(null); } });
        attempt("checkcast object", new Step() { public void run() { useCast("x"); } });
        attempt("anewarray", new Step() { public void run() { useArray(); } });
        attempt("new Sub (superclass missing)", new Step() { public void run() { useSub(); } });
        attempt("new Sub again", new Step() { public void run() { useSub(); } });
        attempt("forName missing", new Step() { public void run() throws Throwable { Class.forName("vmspec.Gone"); } });
        attempt("forName Sub (superclass missing)", new Step() { public void run() throws Throwable { Class.forName("vmspec.Sub"); } });
        try {
            useNew();
        } catch (Throwable t) {
            p("caught in the same method " + name(t));
        }
        p("end");
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

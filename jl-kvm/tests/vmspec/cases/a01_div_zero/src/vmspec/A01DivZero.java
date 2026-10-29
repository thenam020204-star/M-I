package vmspec;

import javax.microedition.midlet.MIDlet;

public class A01DivZero extends MIDlet {
    static int zi = 0, one = 1;
    static long zl = 0L, onel = 1L;
    static float zf = 0f;
    static double zd = 0d;

    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        try { p("idiv " + (one / zi)); } catch (ArithmeticException e) { p("idiv ArithmeticException"); }
        try { p("irem " + (one % zi)); } catch (ArithmeticException e) { p("irem ArithmeticException"); }
        try { p("ldiv " + (onel / zl)); } catch (ArithmeticException e) { p("ldiv ArithmeticException"); }
        try { p("lrem " + (onel % zl)); } catch (ArithmeticException e) { p("lrem ArithmeticException"); }
        // floating point division by zero must NOT throw
        p("fdiv " + (1f / zf));
        p("ddiv " + (1d / zd));
        p("frem-nan " + Float.isNaN(1f % zf));
        p("drem-nan " + Double.isNaN(1d % zd));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

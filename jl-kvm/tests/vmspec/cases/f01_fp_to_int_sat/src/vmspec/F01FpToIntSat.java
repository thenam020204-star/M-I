package vmspec;

import javax.microedition.midlet.MIDlet;

public class F01FpToIntSat extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static float fz = 0f, fbig = 1e20f, fnbig = -1e20f, f3 = 3.99f, fn3 = -3.99f;
    static double dz = 0d, dbig = 1e300, dnbig = -1e300, d3 = 3.99d;
    static String nan(float x) { return x != x ? "nan" : "num"; }

    protected void startApp() {
        float fnan = fz / fz;
        double dnan = dz / dz;
        p("f2i-nan " + (int) fnan);
        p("f2i-big " + (int) fbig);
        p("f2i-nbig " + (int) fnbig);
        p("f2i-3.99 " + (int) f3);
        p("f2i--3.99 " + (int) fn3);
        p("f2i-inf " + (int) (1f / fz));
        p("f2i--inf " + (int) (-1f / fz));
        p("f2l-nan " + (long) fnan);
        p("f2l-big " + (long) fbig);
        p("f2l-nbig " + (long) fnbig);
        p("d2i-nan " + (int) dnan);
        p("d2i-big " + (int) dbig);
        p("d2i-nbig " + (int) dnbig);
        p("d2l-nan " + (long) dnan);
        p("d2l-big " + (long) dbig);
        p("d2l-nbig " + (long) dnbig);
        p("d2l-3.99 " + (long) d3);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

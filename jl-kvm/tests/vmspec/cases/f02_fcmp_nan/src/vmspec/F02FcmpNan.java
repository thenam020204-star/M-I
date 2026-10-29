package vmspec;

import javax.microedition.midlet.MIDlet;

public class F02FcmpNan extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static float fz = 0f, f1 = 1f;
    static double dz = 0d, d1 = 1d;
    static String b(boolean v) { return v ? "T" : "F"; }

    protected void startApp() {
        float x = fz / fz;
        double y = dz / dz;
        p("f< " + b(x < f1));
        p("f<= " + b(x <= f1));
        p("f> " + b(x > f1));
        p("f>= " + b(x >= f1));
        p("f== " + b(x == x));
        p("f!= " + b(x != x));
        p("d< " + b(y < d1));
        p("d<= " + b(y <= d1));
        p("d> " + b(y > d1));
        p("d>= " + b(y >= d1));
        p("d== " + b(y == y));
        p("d!= " + b(y != y));
        p("f-0==0 " + b(-fz == fz));
        p("isNaN " + b(Float.isNaN(x)) + b(Double.isNaN(y)));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

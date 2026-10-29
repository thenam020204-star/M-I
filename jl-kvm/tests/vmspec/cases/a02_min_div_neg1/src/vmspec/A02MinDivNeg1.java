package vmspec;

import javax.microedition.midlet.MIDlet;

public class A02MinDivNeg1 extends MIDlet {
    static int imin = Integer.MIN_VALUE, m1 = -1;
    static long lmin = Long.MIN_VALUE, lm1 = -1L;

    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        p("idiv " + (imin / m1));
        p("irem " + (imin % m1));
        p("ldiv " + (lmin / lm1));
        p("lrem " + (lmin % lm1));
        p("ineg " + (-imin));
        p("lneg " + (-lmin));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

package vmspec;

import javax.microedition.midlet.MIDlet;

public class I04ConstantInline extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static class K {
        static final int I = 42;
        static final long L = 1L << 40;
        static final String S = "lit";
        static final float F = 1.5f;
        static final Object O = new Object();       // not a constant variable
        static final int NI = Integer.parseInt("7"); // not a constant variable
        static { p("K.clinit"); }
    }

    protected void startApp() {
        p("I=" + K.I);          // inlined by javac: must not initialise K
        p("L=" + K.L);
        p("S=" + K.S);
        p("F=" + K.F);
        p("before NI");
        p("NI=" + K.NI);        // initialises K
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

package vmspec;

import javax.microedition.midlet.MIDlet;

public class I01ClinitOrder extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static class A {
        static int ax = init("A.ax");
        static { p("A.clinit"); }
        static int init(String s) { p("init " + s); return 1; }
        static void sm() { p("A.sm"); }
        int inst = init("A.inst");
    }

    static class B extends A {
        static int bx = init("B.bx");
        static { p("B.clinit"); }
        static void bsm() { p("B.bsm"); }
    }

    static class C extends B {
        static { p("C.clinit"); }
        C() { p("C.ctor"); }
    }

    static class D {
        static { p("D.clinit"); }
        static int dx = 5;
    }

    interface I {
        int V = D.dx + 1;   // non-constant: initialised lazily with the interface
    }

    static class E implements I {
        static { p("E.clinit"); }
    }

    protected void startApp() {
        p("start");
        int v = B.ax;                 // field declared in A: initialises A only (qualifying type B)
        p("read B.ax=" + v);
        B.bsm();                      // initialises B
        p("new C");
        new C();                      // initialises C (A, B already done)
        p("new E");
        new E();                      // E init does not init interface I (no default methods)
        p("read I.V");
        p("I.V=" + I.V);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

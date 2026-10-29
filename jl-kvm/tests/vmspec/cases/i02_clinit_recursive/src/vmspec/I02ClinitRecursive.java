package vmspec;

import javax.microedition.midlet.MIDlet;

public class I02ClinitRecursive extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static class A {
        static int ax = 10;
        static int ay;
        static {
            p("A.clinit begin bx=" + B.bx);   // triggers B.clinit, which reads A.ax while A is in progress
            ay = B.bx + 1;
            p("A.clinit end ay=" + ay);
        }
    }

    static class B {
        static int bx;
        static {
            p("B.clinit begin ax=" + A.ax + " ay=" + A.ay);   // same thread: A in progress, no deadlock, sees defaults/assigned
            bx = 20;
            p("B.clinit end");
        }
    }

    static class S {
        static int count;
        static {
            count++;
            p("S.clinit count=" + count);
        }
        static void touch() { p("S.touch"); }
    }

    protected void startApp() {
        p("A.ay=" + A.ay);
        p("B.bx=" + B.bx);
        S.touch();
        S.touch();
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

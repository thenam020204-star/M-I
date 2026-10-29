package vmspec;

import javax.microedition.midlet.MIDlet;

public class A03RemSign extends MIDlet {
    static int a = -7, b = 7, c = 3, d = -3, two = 2;
    static long la = -7L, lb = 7L, lc = 3L, ld = -3L;

    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        p("-7%3 " + (a % c));
        p("7%-3 " + (b % d));
        p("-7%-3 " + (a % d));
        p("-7/2 " + (a / two));
        p("7/-3 " + (b / d));
        p("-7L%3 " + (la % lc));
        p("7L%-3 " + (lb % ld));
        p("-7L%-3 " + (la % ld));
        p("-7L/3 " + (la / lc));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

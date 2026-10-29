package vmspec;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

/**
 * Exceptions escaping guest callbacks (paint(), Thread.run(), Runnable via callSerially).
 * What the system does with them is implementation-dependent (PLAN: "Exceptions in paint()/callback"): the
 * lines below record what the oracles do; they are not a specification. Stack traces go to stderr and are ignored.
 */
public class E01UncaughtException extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static int paints;

    static class C extends Canvas {
        protected void paint(Graphics g) {
            paints++;
            if (paints == 1) throw new RuntimeException("paint boom");
        }
    }

    protected void startApp() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                p("thread running");
                throw new IllegalArgumentException("thread boom");
            }
        });
        t.start();
        try { t.join(); } catch (InterruptedException e) { }
        p("main alive after thread exception");
        Display d = Display.getDisplay(this);
        C c = new C();
        d.setCurrent(c);
        sleep(300);
        c.repaint();
        try { c.serviceRepaints(); p("serviceRepaints returned"); } catch (Throwable x) { p("serviceRepaints threw"); }
        sleep(300);
        c.repaint();
        try { c.serviceRepaints(); p("serviceRepaints 2 returned"); } catch (Throwable x) { p("serviceRepaints 2 threw"); }
        sleep(300);
        p("paint count>=2 " + (paints >= 2));
        d.callSerially(new Runnable() {
            public void run() {
                p("callSerially running");
                throw new RuntimeException("serial boom");
            }
        });
        sleep(300);
        p("end");
        notifyDestroyed();
    }

    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

package vmspec;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

/**
 * Order of the display callbacks (T22, D13): setCurrent -> showNotify -> paint, repaint coalescing, an exception in paint does not stop
 * the event thread, repaint/callSerially FIFO, setCurrent between canvases (hideNotify before showNotify). Only the order is printed.
 */
public class Ev01DisplayOrder extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    final Object lock = new Object();
    boolean done;

    class C extends Canvas {
        final String name;
        int paints;
        boolean shown;
        C(String name) { this.name = name; }
        protected void paint(Graphics g) {
            paints++;
            if (armed && name.equals("c1")) p(name + " paint #" + (paints - base0) + " clipIsFull=" + (g.getClipWidth() == getWidth() && g.getClipHeight() == getHeight()));
            g.setColor(0xFF0000);
            g.fillRect(0, 0, 10, 10);
            synchronized (lock) { lock.notifyAll(); }
            if (armed && name.equals("c1") && paints == base0 + 1) throw new RuntimeException("boom");
        }
        protected void showNotify() { p(name + " showNotify isShown=" + isShown()); synchronized (lock) { shown = true; lock.notifyAll(); } }
        protected void hideNotify() { p(name + " hideNotify"); }
    }

    int base0;
    boolean armed;

    class R implements Runnable {
        final String n;
        R(String n) { this.n = n; }
        public void run() {
            p("run " + n);
            if (n.equals("R2")) synchronized (lock) { done = true; lock.notifyAll(); }
        }
    }

    protected void startApp() {
        p("startApp");
        // the work runs in its own thread: MicroEmulator / FreeJ2ME hold back display events while startApp executes (spec: the
        // MIDlet is only Active once startApp has returned), a device-like VM does not - so startApp must return first
        p("startApp returns");
        new Thread() { public void run() { work(); } }.start();
    }

    /** waits for showNotify, asks for a paint (FreeJ2ME does not paint on its own), lets everything settle, returns the paint count. */
    int settle(C c) {
        synchronized (lock) {
            while (!c.shown) {
                try { lock.wait(100); } catch (InterruptedException e) { }
            }
        }
        c.repaint();
        awaitPaints(c, 1);
        try { Thread.sleep(300); } catch (InterruptedException e) { }
        return c.paints;
    }

    void awaitPaints(C c, int n) {
        synchronized (lock) {
            while (c.paints < n) {
                try { lock.wait(100); } catch (InterruptedException e) { }
            }
        }
    }

    void work() {
        Display d = Display.getDisplay(this);
        C c1 = new C("c1"), c2 = new C("c2");
        p("current null before=" + (d.getCurrent() == null));
        p("c1 isShown before=" + c1.isShown());
        d.setCurrent(c1);
        int base = settle(c1);
        base0 = base;
        armed = true;
        p("A shown and painted, isShown=" + c1.isShown());
        c1.repaint();
        c1.repaint();
        c1.repaint(5, 5, 3, 3);
        c1.serviceRepaints();
        p("B serviced +paints=" + (c1.paints - base));
        c1.repaint();
        c1.serviceRepaints();
        p("C serviced (event thread survived the exception) +paints=" + (c1.paints - base));
        c1.repaint();
        d.callSerially(new R("R1"));
        c1.repaint();
        d.callSerially(new R("R2"));
        synchronized (lock) {
            while (!done) {
                try { lock.wait(); } catch (InterruptedException e) { }
            }
        }
        c1.serviceRepaints();
        p("D done +paints=" + (c1.paints - base));
        d.setCurrent(c2);
        settle(c2);
        p("E c1 isShown=" + c1.isShown() + " c2 isShown=" + c2.isShown());
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

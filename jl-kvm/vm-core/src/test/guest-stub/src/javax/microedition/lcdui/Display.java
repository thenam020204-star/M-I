package javax.microedition.lcdui;

import java.util.Vector;
import javax.microedition.midlet.MIDlet;

/** TEMPORARY (T22): one event thread paints canvases and runs callSerially runnables, exceptions are logged and survived. */
public class Display implements Runnable {
    private static Display instance;
    private static final Vector queue = new Vector();
    private Displayable current;

    private Display() { }

    public static synchronized Display getDisplay(MIDlet m) {
        if (instance == null) {
            instance = new Display();
            new Thread(instance).start();
        }
        return instance;
    }

    static void post(Object o) {
        synchronized (queue) {
            queue.addElement(o);
            queue.notify();
        }
    }

    public void setCurrent(Displayable d) {
        current = d;
        if (d instanceof Canvas) {
            ((Canvas) d).shown();
            ((Canvas) d).repaint();
        }
    }

    public Displayable getCurrent() { return current; }

    public void callSerially(Runnable r) { post(r); }

    public void run() {
        for (;;) {
            Object ev;
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try { queue.wait(); } catch (InterruptedException e) { }
                }
                ev = queue.firstElement();
                queue.removeElementAt(0);
            }
            try {
                if (ev instanceof Runnable) ((Runnable) ev).run();
                else if (ev instanceof Canvas) ((Canvas) ev).doPaint();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}

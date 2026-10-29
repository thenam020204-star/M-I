package vmspec;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

/** A long native call (image allocation/raster/getRGB) must not stop the other guest threads (scheduler). */
public class N01LongNative extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static int ticks;
    static boolean stop;

    static synchronized void tick() { ticks++; }

    static synchronized boolean stopped() { return stop; }

    static synchronized int ticks() { return ticks; }

    protected void startApp() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (!stopped()) {
                    tick();
                    Thread.yield();
                }
            }
        });
        t.start();
        try { Thread.sleep(100); } catch (InterruptedException e) { }
        int before = ticks();
        int sum = 0;
        for (int round = 0; round < 3; round++) {
            Image img = Image.createImage(800, 800);
            Graphics g = img.getGraphics();
            g.setColor(0x336699);
            g.fillRect(0, 0, 800, 800);
            int[] px = new int[800 * 800];
            img.getRGB(px, 0, 800, 0, 0, 800, 800);
            sum += px[12345] & 0xFFFFFF;
        }
        int after = ticks();
        synchronized (N01LongNative.class) { stop = true; }
        try { t.join(); } catch (InterruptedException e) { }
        p("pixel sum " + sum);
        p("other thread progressed during natives " + (after > before));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

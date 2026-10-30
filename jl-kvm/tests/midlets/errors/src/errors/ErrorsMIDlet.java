package errors;

import java.io.InputStream;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

/** PLAN T35b: provokes every kind of line game-errors.log keeps: missing resource (twice), undecodable image, missing class, OOM, uncaught exception. */
public class ErrorsMIDlet extends MIDlet {
    static class Boom extends Thread {
        public void run() { explode(7); }
        static void explode(int n) { if (n == 0) throw new IllegalStateException("deliberate failure"); explode(n - 1); }
    }

    protected void startApp() {
        try {
            InputStream a = getClass().getResourceAsStream("/missing/sprite.png");
            InputStream b = getClass().getResourceAsStream("/missing/sprite.png");
            System.out.println("> resources " + (a == null) + " " + (b == null));
        } catch (Throwable t) { System.out.println("> res " + t); }
        try {
            Image.createImage(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}, 0, 8);
        } catch (Throwable t) { System.out.println("> image " + t.getClass().getName()); }
        try {
            Class.forName("errors.DoesNotExist");
        } catch (Throwable t) { System.out.println("> forName " + t.getClass().getName()); }
        try {
            int[][] big = new int[64][];
            for (int i = 0; i < big.length; i++) big[i] = new int[1000000];
        } catch (Throwable t) { System.out.println("> oom " + t.getClass().getName()); }
        Boom th = new Boom();
        th.start();
        try { th.join(); } catch (InterruptedException e) { }
        System.out.println("> done");
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean unconditional) { }
}

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/** Man splash "Viet hoa boi NAMP": thay Display.setCurrent(game) trong ForeverHero._d_d() (chi lan dau). */
public final class VL extends Canvas implements Runnable {
    private final Display d;
    private final Displayable next;
    private Image logo;
    private boolean done;

    private VL(Display d, Displayable next) {
        this.d = d;
        this.next = next;
        setFullScreenMode(true);
        try { logo = Image.createImage("/vh_logo.png"); } catch (Throwable t) { logo = null; }
    }

    private static boolean shown;

    public static void show(Display d, Displayable next) {
        if (shown) { d.setCurrent(next); return; }
        shown = true;
        VL v = new VL(d, next);
        d.setCurrent(v);
        new Thread(v).start();
    }

    public void run() {
        long t0 = System.currentTimeMillis();
        while (!done && System.currentTimeMillis() - t0 < 2500) {
            repaint();
            try { Thread.sleep(100); } catch (Throwable t) { }
        }
        finish();
    }

    private void finish() {
        done = true;
        logo = null;
        d.setCurrent(next);
    }

    public void keyPressed(int k) { done = true; }

    public void showNotify() { repaint(); }

    public void paint(Graphics g) {
        int w = getWidth(), h = getHeight();
        g.setColor(0);
        g.fillRect(0, 0, w, h);
        if (logo != null) g.drawImage(logo, w >> 1, h >> 1, Graphics.HCENTER | Graphics.VCENTER);
    }
}

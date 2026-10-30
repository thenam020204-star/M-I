package vmspec;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

/**
 * MIDP 2.0 Graphics javadoc: "Using zero for the anchor point value gives results identical to using TOP | LEFT."
 * Draws with anchor 0 and with TOP|LEFT on fresh mutable images and compares the pixels (getRGB); invalid combinations must
 * still throw IllegalArgumentException.
 */
public class Gfx02AnchorZero extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static final int TL = Graphics.TOP | Graphics.LEFT;

    static int[] px(Image im) {
        int w = im.getWidth(), h = im.getHeight();
        int[] a = new int[w * h];
        im.getRGB(a, 0, w, 0, 0, w, h);
        return a;
    }

    static boolean same(int[] a, int[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) if (a[i] != b[i]) return false;
        return true;
    }

    static boolean blank(int[] a) {
        for (int i = 0; i < a.length; i++) if (a[i] != 0xFFFFFFFF && a[i] != 0xFFFFFF) return false;
        return true;
    }

    static Graphics fresh(Image[] out) {
        Image im = Image.createImage(40, 30);
        Graphics g = im.getGraphics();
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, 40, 30);
        g.setColor(0x102030);
        out[0] = im;
        return g;
    }

    static Image sprite() {
        int[] d = new int[6 * 5];
        for (int i = 0; i < d.length; i++) d[i] = 0xFF000000 | ((i * 37 & 255) << 16) | ((i * 91 & 255) << 8) | (i * 13 & 255);
        return Image.createRGBImage(d, 6, 5, false);
    }

    static String tryAnchor(int kind, int anchor) {
        Image[] o = new Image[1];
        Graphics g = fresh(o);
        Image s = sprite();
        try {
            switch (kind) {
                case 0: g.drawImage(s, 10, 10, anchor); break;
                case 1: g.drawRegion(s, 1, 1, 4, 3, 0, 10, 10, anchor); break;
                case 2: g.drawString("Ab", 10, 10, anchor); break;
                case 3: g.drawChar('A', 10, 10, anchor); break;
                case 4: g.drawChars(new char[] {'A', 'b'}, 0, 2, 10, 10, anchor); break;
                case 5: g.drawSubstring("xAbx", 1, 2, 10, 10, anchor); break;
                default: o[0].getGraphics().copyArea(0, 0, 5, 5, 10, 10, anchor); break;
            }
            return "ok";
        } catch (IllegalArgumentException e) {
            return "IAE";
        }
    }

    protected void startApp() {
        Image s = sprite();
        Image[] a = new Image[1], b = new Image[1];
        Graphics ga, gb;

        ga = fresh(a); gb = fresh(b);
        ga.drawImage(s, 7, 9, 0); gb.drawImage(s, 7, 9, TL);
        p("drawImage 0==TL " + same(px(a[0]), px(b[0])) + " drawn " + !blank(px(a[0])));

        ga = fresh(a); gb = fresh(b);
        ga.drawRegion(s, 1, 1, 4, 3, 0, 7, 9, 0); gb.drawRegion(s, 1, 1, 4, 3, 0, 7, 9, TL);
        p("drawRegion 0==TL " + same(px(a[0]), px(b[0])) + " drawn " + !blank(px(a[0])));

        ga = fresh(a); gb = fresh(b);
        ga.drawRegion(s, 0, 0, 6, 5, 5, 7, 9, 0); gb.drawRegion(s, 0, 0, 6, 5, 5, 7, 9, TL);
        p("drawRegion rot90 0==TL " + same(px(a[0]), px(b[0])) + " drawn " + !blank(px(a[0])));

        ga = fresh(a); gb = fresh(b);
        ga.drawRegion(s, 0, 0, 6, 5, 0, 7, 9, 0); gb.drawRegion(s, 0, 0, 6, 5, 0, 7, 9, TL);
        p("drawRegion whole 0==TL " + same(px(a[0]), px(b[0])));

        ga = fresh(a); gb = fresh(b);
        ga.drawString("Ab", 5, 5, 0); gb.drawString("Ab", 5, 5, TL);
        p("drawString 0==TL " + same(px(a[0]), px(b[0])) + " drawn " + !blank(px(a[0])));

        ga = fresh(a); gb = fresh(b);
        ga.drawSubstring("xAbx", 1, 2, 5, 5, 0); gb.drawSubstring("xAbx", 1, 2, 5, 5, TL);
        p("drawSubstring 0==TL " + same(px(a[0]), px(b[0])));

        ga = fresh(a); gb = fresh(b);
        ga.drawChar('A', 5, 5, 0); gb.drawChar('A', 5, 5, TL);
        p("drawChar 0==TL " + same(px(a[0]), px(b[0])));

        ga = fresh(a); gb = fresh(b);
        ga.drawChars(new char[] {'A', 'b'}, 0, 2, 5, 5, 0); gb.drawChars(new char[] {'A', 'b'}, 0, 2, 5, 5, TL);
        p("drawChars 0==TL " + same(px(a[0]), px(b[0])));

        notifyDestroyed();
    }
    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}

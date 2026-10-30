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
public class Gfx03AnchorInvalid extends MIDlet {
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
        Image[] a = new Image[1], b = new Image[1];
        Graphics ga, gb;
        ga = fresh(a); gb = fresh(b);
        ga.fillRect(0, 0, 8, 8); gb.fillRect(0, 0, 8, 8);
        ga.copyArea(0, 0, 8, 8, 20, 12, 0); gb.copyArea(0, 0, 8, 8, 20, 12, TL);
        p("copyArea 0==TL " + same(px(a[0]), px(b[0])));

        String[] k = {"drawImage", "drawRegion", "drawString", "drawChar", "drawChars", "drawSubstring"};
        // invalid for every method: two horizontal / two vertical values, unknown bits
        int[] bad = {Graphics.LEFT | Graphics.RIGHT, Graphics.TOP | Graphics.BOTTOM, Graphics.HCENTER | Graphics.LEFT | Graphics.TOP,
                     Graphics.TOP | Graphics.LEFT | Graphics.RIGHT, 128, TL | 256, -1};
        for (int i = 0; i < k.length; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < bad.length; j++) sb.append(tryAnchor(i, bad[j])).append(' ');
            p(k[i] + " invalid " + sb);
        }
        // BASELINE: invalid for images, valid for text; VCENTER: valid for images, invalid for text
        int bl = Graphics.BASELINE | Graphics.LEFT, vc = Graphics.VCENTER | Graphics.LEFT;
        p("BASELINE img=" + tryAnchor(0, bl) + " region=" + tryAnchor(1, bl)
          + " string=" + tryAnchor(2, bl) + " char=" + tryAnchor(3, bl));
        p("VCENTER img=" + tryAnchor(0, vc) + " region=" + tryAnchor(1, vc)
          + " string=" + tryAnchor(2, vc));
        // all other valid combinations stay valid
        int[] hs = {Graphics.LEFT, Graphics.HCENTER, Graphics.RIGHT}, vs = {Graphics.TOP, Graphics.BOTTOM};
        boolean allOk = true;
        for (int i = 0; i < hs.length; i++) for (int j = 0; j < vs.length; j++)
            for (int m = 0; m < k.length; m++) if (!"ok".equals(tryAnchor(m, hs[i] | vs[j]))) allOk = false;
        p("valid combinations ok " + allOk);
        notifyDestroyed();
    }
    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}

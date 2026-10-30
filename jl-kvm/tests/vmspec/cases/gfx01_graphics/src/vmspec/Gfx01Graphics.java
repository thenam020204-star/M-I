package vmspec;

import java.io.IOException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

/**
 * Graphics / Image (T23): every step draws on a mutable image and prints an FNV-1a hash of getRGB(), so the output is a pixel
 * fingerprint comparable between jl-kvm and the oracles (they use AWT: anti-aliasing off, but ellipse / text rasterisation differ -
 * see docs/lcdui-natives.md for the measured differences). Lines starting with "# " are informational.
 */
public class Gfx01Graphics extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static void info(String s) { System.out.println("# " + s); }

    static String hash(Image im) {
        int w = im.getWidth(), h = im.getHeight();
        int[] px = new int[w * h];
        im.getRGB(px, 0, w, 0, 0, w, h);
        int hh = 0x811c9dc5;
        for (int i = 0; i < px.length; i++) { hh ^= px[i]; hh *= 0x01000193; }
        return Integer.toHexString(hh);
    }

    static void step(String name, Image im) { p(name + " " + hash(im)); }

    static Image fresh() {
        Image im = Image.createImage(64, 48);
        Graphics g = im.getGraphics();
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, 64, 48);
        return im;
    }

    static String hex(int v) { return Integer.toHexString(v); }

    static Image pattern(boolean alpha) {
        int[] d = new int[4 * 3];
        for (int y = 0; y < 3; y++) for (int x = 0; x < 4; x++) {
            int a = alpha ? (((x + y) % 3 == 0) ? 0xFF : ((x + y) % 3 == 1) ? 0x80 : 0x00) : 0xFF;
            d[y * 4 + x] = (a << 24) | ((x * 60 + 20) << 16) | ((y * 80 + 30) << 8) | (x * 20 + y * 40);
        }
        return Image.createRGBImage(d, 4, 3, alpha);
    }

    protected void startApp() {
        Image im = fresh();
        p("init " + hash(im) + " size " + im.getWidth() + "x" + im.getHeight() + " mutable " + im.isMutable());
        Graphics g = im.getGraphics();
        p("defaults color=" + hex(g.getColor()) + " tx=" + g.getTranslateX() + " clip=" + g.getClipX() + "," + g.getClipY() + "," + g.getClipWidth() + "," + g.getClipHeight() + " stroke=" + g.getStrokeStyle());

        g.setColor(0xFF0000); g.fillRect(10, 10, 20, 15); step("fillRect", im);
        g.setColor(0x0000FF); g.drawRect(5, 5, 30, 20); step("drawRect", im);
        g.setColor(0x00A000); g.drawLine(0, 0, 63, 47); g.drawLine(63, 0, 0, 47); g.drawLine(10, 40, 50, 40); g.drawLine(32, 0, 32, 47); step("drawLine", im);
        g.setStrokeStyle(Graphics.DOTTED); g.setColor(0x800080); g.drawLine(2, 45, 60, 45); g.drawRect(40, 30, 15, 10); g.setStrokeStyle(Graphics.SOLID); step("dotted", im);
        g.fillRect(-5, -5, 3, 3); g.fillRect(62, 46, 10, 10); g.drawRect(-10, -10, 100, 100); g.fillRect(5, 5, 0, 5); g.fillRect(5, 5, -3, 5); step("offscreen+empty", im);

        Image c = fresh();
        g = c.getGraphics();
        g.setClip(8, 8, 20, 12);
        p("clip1 " + g.getClipX() + "," + g.getClipY() + "," + g.getClipWidth() + "," + g.getClipHeight());
        g.setColor(0xFF00FF); g.fillRect(0, 0, 64, 48); step("clipFill", c);
        g.clipRect(15, 10, 30, 30);
        p("clip2 " + g.getClipX() + "," + g.getClipY() + "," + g.getClipWidth() + "," + g.getClipHeight());
        g.setColor(0x00FFFF); g.fillRect(0, 0, 64, 48); step("clipRect", c);
        g.translate(10, 5);
        p("tr " + g.getTranslateX() + "," + g.getTranslateY() + " clip=" + g.getClipX() + "," + g.getClipY() + "," + g.getClipWidth() + "," + g.getClipHeight());
        g.setClip(0, 0, 64, 48);
        g.setColor(0x202020); g.drawRect(0, 0, 10, 10); step("translate", c);
        g.clipRect(100, 100, 5, 5);
        p("emptyclip " + g.getClipWidth() + "x" + g.getClipHeight());
        g.fillRect(0, 0, 200, 200); step("emptyclip-fill", c);
        g.setClip(-100, -100, 1000, 1000);
        p("bigclip " + g.getClipX() + "," + g.getClipY() + "," + g.getClipWidth() + "," + g.getClipHeight());

        Image s = fresh(); g = s.getGraphics();
        g.setColor(0x404040); g.drawRoundRect(4, 4, 30, 20, 8, 8); step("drawRoundRect", s);
        g.setColor(0xC04000); g.fillRoundRect(10, 10, 30, 20, 10, 6); step("fillRoundRect", s);
        g.setColor(0x0040C0); g.drawArc(30, 5, 28, 28, 0, 360); g.drawArc(5, 25, 20, 16, 30, 120); step("drawArc", s);
        g.setColor(0x00C040); g.fillArc(35, 25, 25, 20, 45, 200); g.fillArc(2, 2, 14, 14, 0, 360); step("fillArc", s);
        g.setColor(0x808000); g.fillTriangle(5, 40, 25, 30, 20, 46); g.fillTriangle(50, 2, 60, 2, 55, 12); step("fillTriangle", s);

        Image t = fresh(); g = t.getGraphics();
        Font f = Font.getDefaultFont();
        info("font height=" + f.getHeight() + " baseline=" + f.getBaselinePosition() + " width(Hello)=" + f.stringWidth("Hello"));
        g.setColor(0); g.drawString("Hi", 2, 2, Graphics.TOP | Graphics.LEFT);
        g.drawString("Mid", 32, 20, Graphics.TOP | Graphics.HCENTER);
        g.drawString("R", 62, 40, Graphics.BASELINE | Graphics.RIGHT);
        g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_SMALL)); g.drawChar('x', 40, 4, Graphics.TOP | Graphics.LEFT);
        step("text (differs between rasterisers)", t);

        // images: immutable, alpha, 8 transforms
        Image src = pattern(true), opaque = pattern(false);
        p("src " + src.getWidth() + "x" + src.getHeight() + " mutable " + src.isMutable() + " px0=" + hex(rgbOf(src, 0, 0)) + " px1=" + hex(rgbOf(src, 1, 0)) + " px2=" + hex(rgbOf(src, 2, 0)));
        Image d = fresh(); g = d.getGraphics();
        g.setColor(0xFFFF00); g.fillRect(0, 0, 64, 48);
        g.drawImage(src, 2, 2, Graphics.TOP | Graphics.LEFT);
        g.drawImage(src, 62, 2, Graphics.TOP | Graphics.RIGHT);
        g.drawImage(src, 32, 24, Graphics.VCENTER | Graphics.HCENTER);
        g.drawImage(opaque, 2, 46, Graphics.BOTTOM | Graphics.LEFT);
        step("drawImage anchors", d);
        for (int tr = 0; tr < 8; tr++) {
            Image dt = fresh(); Graphics gt = dt.getGraphics();
            gt.drawRegion(opaque, 0, 0, 4, 3, tr, 10, 10, Graphics.TOP | Graphics.LEFT);
            gt.drawRegion(src, 1, 1, 2, 2, tr, 40, 30, Graphics.BOTTOM | Graphics.RIGHT);
            step("drawRegion t" + tr, dt);
            Image ci = Image.createImage(opaque, 0, 0, 4, 3, tr);
            p("createImage t" + tr + " " + ci.getWidth() + "x" + ci.getHeight() + " first=" + hex(rgbOf(ci, 0, 0)) + " last=" + hex(rgbOf(ci, ci.getWidth() - 1, ci.getHeight() - 1)));
        }
        Image r = fresh(); g = r.getGraphics();
        int[] data = new int[10 * 6];
        for (int i = 0; i < data.length; i++) data[i] = ((i * 17) % 256 << 24) | (0xFF0000 >> ((i % 3) * 8));
        g.drawRGB(data, 0, 10, 5, 5, 10, 6, true); step("drawRGB alpha", r);
        g.drawRGB(data, 0, 10, 30, 5, 10, 6, false); step("drawRGB opaque", r);
        g.drawRGB(data, 3, 10, 5, 30, 5, 3, true); step("drawRGB offset", r);
        int[] out = new int[20];
        r.getRGB(out, 2, 5, 3, 5, 5, 2);
        p("getRGB " + hex(out[2]) + " " + hex(out[6]) + " " + hex(out[7]) + " " + hex(out[11]) + " untouched=" + out[0] + "," + out[1] + "," + out[5]);
        Image cp = fresh(); g = cp.getGraphics();
        g.setColor(0xFF0000); g.fillRect(0, 0, 10, 10); g.setColor(0x0000FF); g.fillRect(5, 5, 10, 10);
        g.copyArea(0, 0, 15, 15, 20, 20, Graphics.TOP | Graphics.LEFT); step("copyArea", cp);
        g.copyArea(0, 0, 15, 15, 5, 5, Graphics.TOP | Graphics.LEFT); step("copyArea overlap", cp);
        Image self = fresh(); g = self.getGraphics(); g.setColor(0x00FF00); g.fillRect(0, 0, 8, 8); g.drawImage(self, 4, 4, Graphics.TOP | Graphics.LEFT); step("drawImage self", self);

        // resource PNGs (decoded by the host)
        String[] pngs = {"/gfx/rgba.png", "/gfx/pal.png", "/gfx/grey.png", "/gfx/rgb.png"};
        for (int i = 0; i < pngs.length; i++) {
            try {
                Image pi = Image.createImage(pngs[i]);
                StringBuffer sb = new StringBuffer(pngs[i] + " " + pi.getWidth() + "x" + pi.getHeight() + " mutable=" + pi.isMutable() + ":");
                for (int y = 0; y < pi.getHeight(); y++) for (int x = 0; x < pi.getWidth(); x++) sb.append(' ').append(hex(rgbOf(pi, x, y)));
                p(sb.toString());
            } catch (IOException e) {
                p(pngs[i] + " IOException " + e.getMessage());
            }
        }
        try { Image.createImage("/gfx/missing.png"); p("missing: no exception"); } catch (IOException e) { p("missing: IOException"); }
        try { Image.createImage(new byte[] {1, 2, 3}, 0, 3); p("garbage: no exception"); } catch (IllegalArgumentException e) { p("garbage: IllegalArgumentException"); }

        // argument checks
        Graphics ge = fresh().getGraphics();
        Image any = fresh();
        p("iae anchor " + thrown(ge, 0)); p("iae baseline-image " + thrown(ge, 1)); p("npe image " + thrown(ge, 2)); p("iae transform " + thrown(ge, 3));
        p("iae color " + thrown(ge, 4)); p("iae stroke " + thrown(ge, 5)); p("iae createImage " + thrown(ge, 6)); p("ise immutable.getGraphics " + thrown(ge, 7));
        p("iae drawRegion bounds " + thrown(ge, 8)); p("npe drawString " + thrown(ge, 9)); p("iae drawRegion self " + thrown(ge, 10));
        notifyDestroyed();
    }

    static int rgbOf(Image im, int x, int y) {
        int[] one = new int[1];
        im.getRGB(one, 0, 1, x, y, 1, 1);
        return one[0];
    }

    static String thrown(Graphics g, int k) {
        Image img = Image.createImage(8, 8);
        try {
            switch (k) {
                case 0: g.drawString("x", 0, 0, 0); break;
                case 1: g.drawImage(img, 0, 0, Graphics.BASELINE | Graphics.LEFT); break;
                case 2: g.drawImage(null, 0, 0, 0); break;
                case 3: g.drawRegion(img, 0, 0, 4, 4, 9, 0, 0, Graphics.TOP | Graphics.LEFT); break;
                case 4: g.setColor(256, 0, 0); break;
                case 5: g.setStrokeStyle(7); break;
                case 6: Image.createImage(0, 5); break;
                case 7: pattern(true).getGraphics(); break;
                case 8: g.drawRegion(img, 6, 6, 4, 4, 0, 0, 0, Graphics.TOP | Graphics.LEFT); break;
                case 9: g.drawString(null, 0, 0, Graphics.TOP | Graphics.LEFT); break;
                case 10: { Graphics gi = img.getGraphics(); gi.drawRegion(img, 0, 0, 2, 2, 0, 0, 0, Graphics.TOP | Graphics.LEFT); break; }
            }
        } catch (RuntimeException e) {
            return e.getClass().getName();
        }
        return "none";
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

package vmspec;

import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

/**
 * DirectGraphics.drawImage / drawPixels manipulations (Nokia UI API 1.1): FLIP_HORIZONTAL, FLIP_VERTICAL and ROTATE_90/180/270 (counter-clockwise);
 * with a combination first the rotation is done, then the vertical flip, then the horizontal one; the anchor is applied after the manipulation.
 * The expected grids are computed here by an independent rotate / flip routine written from that sentence, and three are written out by hand.
 */
public class Nk02 extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    // source 3 x 2:  1 2 3 / 4 5 6
    static final int[] SRC = {0xFF000001, 0xFF000002, 0xFF000003, 0xFF000004, 0xFF000005, 0xFF000006};

    static int[][] grid(int w, int h, int[] d) {
        int[][] g = new int[h][w];
        for (int j = 0; j < h; j++) for (int i = 0; i < w; i++) g[j][i] = d[j * w + i];
        return g;
    }

    static int[][] rotateCcw(int[][] g) {
        int h = g.length, w = g[0].length;
        int[][] r = new int[w][h];
        for (int j = 0; j < h; j++) for (int i = 0; i < w; i++) r[w - 1 - i][j] = g[j][i];
        return r;
    }

    static int[][] flipV(int[][] g) {
        int h = g.length, w = g[0].length;
        int[][] r = new int[h][w];
        for (int j = 0; j < h; j++) for (int i = 0; i < w; i++) r[h - 1 - j][i] = g[j][i];
        return r;
    }

    static int[][] flipH(int[][] g) {
        int h = g.length, w = g[0].length;
        int[][] r = new int[h][w];
        for (int j = 0; j < h; j++) for (int i = 0; i < w; i++) r[j][w - 1 - i] = g[j][i];
        return r;
    }

    static int[][] expected(int rot, boolean fv, boolean fh) {
        int[][] g = grid(3, 2, SRC);
        for (int k = 0; k < rot / 90; k++) g = rotateCcw(g);
        if (fv) g = flipV(g);
        if (fh) g = flipH(g);
        return g;
    }

    static String text(int[][] g) {
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < g.length; j++) {
            if (j > 0) sb.append('/');
            for (int i = 0; i < g[j].length; i++) sb.append((char) ('0' + (g[j][i] & 0xFF)));
        }
        return sb.toString();
    }

    /** Reads the transformed picture back from a 6 x 6 canvas: the picture lies at (1, 1), 0 = untouched background. */
    static String read(Image im, int w, int h) {
        int[] a = new int[36];
        im.getRGB(a, 0, 6, 0, 0, 6, 6);
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < h; j++) {
            if (j > 0) sb.append('/');
            for (int i = 0; i < w; i++) sb.append((char) ('0' + (a[(1 + j) * 6 + 1 + i] & 0xFF)));
        }
        // nothing may be drawn outside the picture
        for (int j = 0; j < 6; j++) for (int i = 0; i < 6; i++) if ((i < 1 || j < 1 || i >= 1 + w || j >= 1 + h) && a[j * 6 + i] != 0xFFFFFFFF) return "LEAK";
        return sb.toString();
    }

    static Image canvas() {
        Image im = Image.createImage(6, 6);
        Graphics g = im.getGraphics();
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, 6, 6);
        return im;
    }

    public void startApp() {
        Image src = Image.createRGBImage(SRC, 3, 2, false);
        int[] rots = {0, DirectGraphics.ROTATE_90, DirectGraphics.ROTATE_180, DirectGraphics.ROTATE_270};
        int ok = 0, total = 0;
        for (int r = 0; r < 4; r++) {
            for (int f = 0; f < 4; f++) {
                boolean fh = (f & 1) != 0, fv = (f & 2) != 0;
                int m = rots[r] | (fh ? DirectGraphics.FLIP_HORIZONTAL : 0) | (fv ? DirectGraphics.FLIP_VERTICAL : 0);
                int[][] e = expected(rots[r], fv, fh);
                int h = e.length, w = e[0].length;
                Image a = canvas();
                DirectUtils.getDirectGraphics(a.getGraphics()).drawImage(src, 1, 1, Graphics.TOP | Graphics.LEFT, m);
                Image b = canvas();
                DirectUtils.getDirectGraphics(b.getGraphics()).drawPixels(SRC, false, 0, 3, 1, 1, 3, 2, m, DirectGraphics.TYPE_INT_8888_ARGB);
                String want = text(e);
                String ra = read(a, w, h), rb = read(b, w, h);
                total++;
                if (want.equals(ra) && want.equals(rb)) ok++;
                else p("MISMATCH rot " + rots[r] + " fh " + fh + " fv " + fv + " want " + want + " drawImage " + ra + " drawPixels " + rb);
            }
        }
        p("all 16 manipulations match: " + ok + "/" + total);
        // by hand (counter-clockwise: the right-hand column becomes the top row)
        Image a = canvas();
        DirectUtils.getDirectGraphics(a.getGraphics()).drawImage(src, 1, 1, Graphics.TOP | Graphics.LEFT, DirectGraphics.ROTATE_90);
        p("ROTATE_90 " + read(a, 2, 3));
        a = canvas();
        DirectUtils.getDirectGraphics(a.getGraphics()).drawImage(src, 1, 1, Graphics.TOP | Graphics.LEFT, DirectGraphics.FLIP_HORIZONTAL);
        p("FLIP_HORIZONTAL " + read(a, 3, 2));
        a = canvas();
        DirectUtils.getDirectGraphics(a.getGraphics()).drawImage(src, 1, 1, Graphics.TOP | Graphics.LEFT, DirectGraphics.ROTATE_90 | DirectGraphics.FLIP_VERTICAL);
        p("ROTATE_90|FLIP_VERTICAL " + read(a, 2, 3));
        // the anchor is applied after the manipulation: the rotated picture is 2 x 3, its bottom-right corner lies on (5, 5)
        a = canvas();
        DirectUtils.getDirectGraphics(a.getGraphics()).drawImage(src, 5, 5, Graphics.BOTTOM | Graphics.RIGHT, DirectGraphics.ROTATE_90);
        int[] px = new int[36];
        a.getRGB(px, 0, 6, 0, 0, 6, 6);
        p("anchor after rotate: (4,2)=" + Integer.toHexString(px[2 * 6 + 4] & 0xFF) + " (3,2)=" + Integer.toHexString(px[2 * 6 + 3] & 0xFF) + " (4,5)=" + Integer.toHexString(px[5 * 6 + 4] & 0xFF) + " (3,1)=" + Integer.toHexString(px[1 * 6 + 3] & 0xFF));
        // errors
        a = canvas();
        DirectGraphics dg = DirectUtils.getDirectGraphics(a.getGraphics());
        try { dg.drawImage(null, 0, 0, 0, 0); p("null image no exception"); } catch (NullPointerException e) { p("null image NullPointerException"); }
        try { dg.drawImage(src, 0, 0, Graphics.TOP | Graphics.LEFT, 45); p("bad manipulation no exception"); } catch (IllegalArgumentException e) { p("bad manipulation IllegalArgumentException"); }
        try { dg.drawImage(src, 0, 0, 1000, 0); p("bad anchor no exception"); } catch (IllegalArgumentException e) { p("bad anchor IllegalArgumentException"); }
        notifyDestroyed();
    }

    public void pauseApp() {}
    public void destroyApp(boolean u) {}
}

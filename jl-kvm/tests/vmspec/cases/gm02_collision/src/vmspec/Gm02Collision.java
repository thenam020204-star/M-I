package vmspec;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.MIDlet;

/** T25: pixel-level collision (Sprite/Sprite with every transform, Sprite/TiledLayer incl. animated tiles, Sprite/Image, partial alpha) printed as hit maps. */
public class Gm02Collision extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    /** Image from rows of characters: 'X' opaque red, 'a' alpha 0x01, 'b' alpha 0x80, anything else transparent. */
    static Image img(String[] rows) {
        int h = rows.length, w = rows[0].length();
        int[] px = new int[w * h];
        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) {
            char c = rows[y].charAt(x);
            px[y * w + x] = c == 'X' ? 0xFFFF0000 : c == 'a' ? 0x01FF0000 : c == 'b' ? 0x80FF0000 : 0x00FF0000;
        }
        return Image.createRGBImage(px, w, h, true);
    }

    static final String[] A = {"X...", "XX..", "XXX.", "X..X"};
    static final String[] B = {"..", ".X"};

    interface Probe { boolean hit(int x, int y); }
    static String map(int x0, int x1, int y0, int y1, Probe pr) {
        StringBuffer r = new StringBuffer();
        for (int y = y0; y <= y1; y++) {
            for (int x = x0; x <= x1; x++) r.append(pr.hit(x, y) ? '#' : '.');
            r.append('/');
        }
        return r.toString();
    }

    protected void startApp() {
        final Image ia = img(A), ib = img(B);
        for (int tr = 0; tr < 8; tr++) {
            final Sprite a = new Sprite(ia), b = new Sprite(ib);
            a.setTransform(tr);
            p("sprite/sprite pixel, A transform " + tr + ": " + map(-3, 5, -3, 5, new Probe() { public boolean hit(int x, int y) { b.setPosition(x, y); return a.collidesWith(b, true); } }));
        }
        final Sprite a2 = new Sprite(ia), b2 = new Sprite(ib);
        b2.setTransform(Sprite.TRANS_MIRROR_ROT90);
        a2.setTransform(Sprite.TRANS_ROT270);
        p("both transformed: " + map(-3, 5, -3, 5, new Probe() { public boolean hit(int x, int y) { b2.setPosition(x, y); return a2.collidesWith(b2, true); } }));
        final Sprite a3 = new Sprite(ia), b3 = new Sprite(ib);
        a3.defineCollisionRectangle(1, 1, 3, 2);
        p("pixel inside a custom collision rectangle (1,1,3,2): " + map(-3, 5, -3, 5, new Probe() { public boolean hit(int x, int y) { b3.setPosition(x, y); return a3.collidesWith(b3, true); } }));
        p("rect only, same sprites: " + map(-3, 5, -3, 5, new Probe() { public boolean hit(int x, int y) { b3.setPosition(x, y); return a3.collidesWith(b3, false); } }));

        // frames: the pixels come from the current frame
        final Sprite f = new Sprite(img(new String[] {"X...", "...X"}), 2, 1);
        final Sprite pt = new Sprite(img(new String[] {"X"}));
        p("frames 0..3 (X. / .. / .. / .X): " + frameProbe(f, pt));
        f.setFrameSequence(new int[] {2, 1});
        p("frame 2 / 3 -> sequence {2,1}: " + frameProbe(f, pt));

        // partial alpha: only a fully opaque pixel (alpha 255) counts (MicroEmulator, FreeJ2ME)
        final Sprite al = new Sprite(img(new String[] {"ab."}));
        final Sprite q = new Sprite(img(new String[] {"X"}));
        p("alpha 0x01 / 0x80 / 0: " + map(0, 2, 0, 0, new Probe() { public boolean hit(int x, int y) { q.setPosition(x, 0); return al.collidesWith(q, true); } }));

        // self check against the rasteriser: the pixels drawRegion paints for every transform == the pixels the pixel-level test calls opaque
        final Image big = img(new String[] {"X.X.X", ".XX..", "XXX.X", "X...X", ".X.XX", "XX..."});
        for (int tr = 0; tr < 8; tr++) {
            final Sprite sp = new Sprite(big);
            sp.setPosition(3, 2);
            sp.setTransform(tr);
            sp.setPosition(3, 2);
            Image canvas = Image.createImage(14, 14);
            sp.paint(canvas.getGraphics());
            int[] px = new int[14 * 14];
            canvas.getRGB(px, 0, 14, 0, 0, 14, 14);
            final Sprite dot = new Sprite(img(new String[] {"X"}));
            int bad = 0, lit = 0;
            for (int y = 0; y < 14; y++) for (int x = 0; x < 14; x++) {
                boolean painted = (px[y * 14 + x] & 0xFFFFFF) == 0xFF0000;
                dot.setPosition(x, y);
                if (painted) lit++;
                if (painted != sp.collidesWith(dot, true)) bad++;
            }
            p("self check transform " + tr + ": painted pixels " + lit + ", disagreements " + bad);
        }

        // invisible
        final Sprite iv = new Sprite(ia);
        iv.setVisible(false);
        p("invisible: " + iv.collidesWith(new Sprite(ia), true) + " " + new Sprite(ia).collidesWith(iv, false));

        // Image
        final Sprite ai = new Sprite(ia);
        final Image im = img(new String[] {"X.", ".X"});
        p("sprite/image pixel: " + map(-3, 5, -3, 5, new Probe() { public boolean hit(int x, int y) { return ai.collidesWith(im, x, y, true); } }));
        p("sprite/image rect: " + map(-3, 5, -3, 5, new Probe() { public boolean hit(int x, int y) { return ai.collidesWith(im, x, y, false); } }));

        // TiledLayer: tile 1 = full, tile 2 = L shape, animated tile -1 -> tile 2
        final TiledLayer tl = new TiledLayer(3, 2, img(new String[] {"XXXXX...", "XXXXXX..", "XXXXXXX.", "XXXXX..X"}), 4, 4);
        final int an = tl.createAnimatedTile(2);
        tl.setCell(0, 0, 1);
        tl.setCell(1, 0, an);
        tl.setCell(2, 1, 2);
        tl.setPosition(2, 1);
        final Sprite s1 = new Sprite(ib);
        p("tiled layer (cells: full, animated->L, L at row 1) rect: " + map(0, 14, -2, 10, new Probe() { public boolean hit(int x, int y) { s1.setPosition(x, y); return s1.collidesWith(tl, false); } }));
        p("tiled layer pixel: " + map(0, 14, -2, 10, new Probe() { public boolean hit(int x, int y) { s1.setPosition(x, y); return s1.collidesWith(tl, true); } }));
        tl.setAnimatedTile(an, 1);
        p("animated tile -> full tile: " + map(4, 8, -1, 4, new Probe() { public boolean hit(int x, int y) { s1.setPosition(x, y); return s1.collidesWith(tl, true); } }));
        tl.setVisible(false);
        p("invisible layer: " + s1.collidesWith(tl, false));
        p("empty cells never collide: " + new TiledLayer(2, 2, img(new String[] {"XX"}), 1, 1).getCellWidth() + " " + new Sprite(ib).collidesWith(new TiledLayer(2, 2, img(new String[] {"XX"}), 1, 1), false));
        p("done");
        notifyDestroyed();
    }

    static String frameProbe(Sprite f, Sprite pt) {
        StringBuffer r = new StringBuffer();
        for (int i = 0; i < f.getFrameSequenceLength(); i++) {
            f.setFrame(i);
            for (int x = 0; x < 2; x++) {
                pt.setPosition(x, 0);
                r.append(f.collidesWith(pt, true) ? '#' : '.');
            }
            r.append('|');
        }
        return r.toString();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

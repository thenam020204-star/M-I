package vmspec;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.MIDlet;

/** T25: TiledLayer rules (cells, animated tiles, tile sets, exceptions) and its painting (checksum of the target image, clip, position, translate). */
public class Gm03TiledLayer extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }
    abstract static class T { abstract Object go() throws Exception; }
    static void t(String what, T c) {
        String r;
        try { Object o = c.go(); r = o == null ? "ok" : "ok " + o; } catch (Throwable e) { r = cn(e); }
        p(what + " -> " + r);
    }

    /** 4 tiles of 4x4 in a 2x2 sheet: 1 red, 2 green with a white dot, 3 blue, 4 yellow. */
    static Image tiles() {
        Image im = Image.createImage(8, 8);
        Graphics g = im.getGraphics();
        g.setColor(0xFF0000); g.fillRect(0, 0, 4, 4);
        g.setColor(0x00FF00); g.fillRect(4, 0, 4, 4);
        g.setColor(0xFFFFFF); g.fillRect(5, 1, 1, 1);
        g.setColor(0x0000FF); g.fillRect(0, 4, 4, 4);
        g.setColor(0xFFFF00); g.fillRect(4, 4, 4, 4);
        return Image.createImage(im);
    }

    static String sum(Image im) {
        int w = im.getWidth(), h = im.getHeight();
        int[] px = new int[w * h];
        im.getRGB(px, 0, w, 0, 0, w, h);
        int a = 1, b = 0;
        for (int i = 0; i < px.length; i++) {
            a = (a + (px[i] & 0xFFFFFF) + i) % 65521;
            b = (b + a) % 65521;
        }
        return Integer.toHexString((b << 16) | a);
    }

    static String render(TiledLayer l, int w, int h, int tx, int ty, int[] clip) {
        Image t = Image.createImage(w, h);
        Graphics g = t.getGraphics();
        g.setColor(0x808080);
        g.fillRect(0, 0, w, h);
        g.translate(tx, ty);
        if (clip != null) g.setClip(clip[0], clip[1], clip[2], clip[3]);
        l.paint(g);
        return sum(t);
    }

    protected void startApp() {
        final Image ts = tiles();
        final TiledLayer l = new TiledLayer(4, 3, ts, 4, 4);
        t("size", new T() { Object go() { return l.getWidth() + "x" + l.getHeight() + " cells " + l.getColumns() + "x" + l.getRows() + " tile " + l.getCellWidth() + "x" + l.getCellHeight(); } });
        t("ctor columns 0", new T() { Object go() { new TiledLayer(0, 3, ts, 4, 4); return null; } });
        t("ctor rows 0", new T() { Object go() { new TiledLayer(3, 0, ts, 4, 4); return null; } });
        t("ctor tile width 0", new T() { Object go() { new TiledLayer(3, 3, ts, 0, 4); return null; } });
        t("ctor tile size not a divisor", new T() { Object go() { new TiledLayer(3, 3, ts, 3, 4); return null; } });
        t("ctor null image", new T() { Object go() { new TiledLayer(3, 3, null, 4, 4); return null; } });
        t("cells start empty", new T() { Object go() { return new Integer(l.getCell(3, 2)); } });
        t("setCell / getCell", new T() { Object go() { l.setCell(1, 2, 3); return new Integer(l.getCell(1, 2)); } });
        t("setCell column out of range", new T() { Object go() { l.setCell(4, 0, 1); return null; } });
        t("setCell row out of range", new T() { Object go() { l.setCell(0, -1, 1); return null; } });
        t("getCell out of range", new T() { Object go() { return new Integer(l.getCell(0, 3)); } });
        t("setCell tile 4 (last)", new T() { Object go() { l.setCell(0, 0, 4); return new Integer(l.getCell(0, 0)); } });
        t("setCell tile 5 (too big)", new T() { Object go() { l.setCell(0, 0, 5); return null; } });
        t("setCell negative without animated tile", new T() { Object go() { l.setCell(0, 0, -1); return null; } });
        t("fillCells", new T() { Object go() { l.fillCells(1, 0, 2, 2, 2); return l.getCell(0, 0) + "" + l.getCell(1, 0) + l.getCell(2, 0) + l.getCell(3, 0) + l.getCell(1, 1) + l.getCell(2, 1) + l.getCell(3, 1); } });
        t("fillCells outside", new T() { Object go() { l.fillCells(3, 0, 2, 1, 1); return null; } });
        t("fillCells negative size", new T() { Object go() { l.fillCells(0, 0, -1, 1, 1); return null; } });
        t("fillCells empty region", new T() { Object go() { l.fillCells(0, 0, 0, 2, 1); return "ok"; } });
        t("fillCells bad tile", new T() { Object go() { l.fillCells(0, 0, 1, 1, 9); return null; } });
        t("createAnimatedTile", new T() { Object go() { return new Integer(l.createAnimatedTile(1)) + " " + l.createAnimatedTile(0) + " " + l.createAnimatedTile(4); } });
        t("createAnimatedTile bad static index", new T() { Object go() { l.createAnimatedTile(5); return null; } });
        t("createAnimatedTile negative", new T() { Object go() { l.createAnimatedTile(-1); return null; } });
        t("getAnimatedTile", new T() { Object go() { return l.getAnimatedTile(-1) + " " + l.getAnimatedTile(-2) + " " + l.getAnimatedTile(-3); } });
        t("getAnimatedTile 0", new T() { Object go() { return new Integer(l.getAnimatedTile(0)); } });
        t("getAnimatedTile not created", new T() { Object go() { return new Integer(l.getAnimatedTile(-4)); } });
        t("getAnimatedTile positive", new T() { Object go() { return new Integer(l.getAnimatedTile(1)); } });
        t("setAnimatedTile", new T() { Object go() { l.setAnimatedTile(-1, 3); return new Integer(l.getAnimatedTile(-1)); } });
        t("setAnimatedTile bad animated index", new T() { Object go() { l.setAnimatedTile(-9, 1); return null; } });
        t("setAnimatedTile bad static index", new T() { Object go() { l.setAnimatedTile(-1, 5); return null; } });
        t("setCell animated", new T() { Object go() { l.setCell(3, 2, -2); return new Integer(l.getCell(3, 2)); } });
        t("setCell animated not created", new T() { Object go() { l.setCell(3, 2, -4); return null; } });

        // painting
        final TiledLayer m = new TiledLayer(4, 3, ts, 4, 4);
        m.fillCells(0, 0, 4, 3, 1);
        m.setCell(1, 1, 2);
        m.setCell(2, 1, 3);
        final int an = m.createAnimatedTile(4);
        m.setCell(3, 2, an);
        p("paint all: " + render(m, 20, 16, 0, 0, null));
        m.setAnimatedTile(an, 2);
        p("animated tile -> 2: " + render(m, 20, 16, 0, 0, null));
        m.setPosition(2, 3);
        p("positioned (2,3): " + render(m, 20, 16, 0, 0, null));
        m.setPosition(-5, -2);
        p("positioned (-5,-2): " + render(m, 20, 16, 0, 0, null));
        m.setPosition(0, 0);
        p("translated (3,1): " + render(m, 20, 16, 3, 1, null));
        p("clipped (5,2,7,6): " + render(m, 20, 16, 0, 0, new int[] {5, 2, 7, 6}));
        p("clipped to one pixel (4,4,1,1): " + render(m, 20, 16, 0, 0, new int[] {4, 4, 1, 1}));
        p("clip outside the layer: " + render(m, 20, 16, 0, 0, new int[] {17, 13, 3, 3}));
        m.setVisible(false);
        p("invisible: " + render(m, 20, 16, 0, 0, null));
        m.setVisible(true);
        t("paint(null)", new T() { Object go() { m.paint(null); return null; } });
        t("empty cell leaves the target alone", new T() { Object go() { return render(new TiledLayer(2, 2, ts, 4, 4), 8, 8, 0, 0, null); } });

        // tile set changes
        final TiledLayer n = new TiledLayer(2, 2, ts, 4, 4);
        n.fillCells(0, 0, 2, 2, 3);
        final int an2 = n.createAnimatedTile(2);
        n.setCell(0, 0, an2);
        t("setStaticTileSet with the same number of tiles keeps cells", new T() { Object go() { n.setStaticTileSet(tiles(), 4, 4); return n.getCell(0, 0) + " " + n.getCell(1, 1) + " " + n.getAnimatedTile(an2); } });
        t("setStaticTileSet with more tiles keeps cells and changes the size", new T() { Object go() { n.setStaticTileSet(Image.createImage(16, 8), 2, 4); return n.getCell(1, 1) + " " + n.getWidth() + "x" + n.getHeight() + " " + n.getCellWidth(); } });
        t("setStaticTileSet with fewer tiles clears everything", new T() { Object go() { n.setStaticTileSet(Image.createImage(4, 4), 4, 4); return n.getCell(1, 1) + " " + n.getCell(0, 0) + " " + n.getWidth() + "x" + n.getHeight(); } });
        t("animated tiles deleted by the smaller set", new T() { Object go() { return new Integer(n.getAnimatedTile(an2)); } });
        t("setStaticTileSet bad size", new T() { Object go() { n.setStaticTileSet(ts, 3, 4); return null; } });
        t("setStaticTileSet null", new T() { Object go() { n.setStaticTileSet(null, 4, 4); return null; } });
        t("Layer position", new T() { Object go() { l.setPosition(3, 4); l.move(-1, 1); return l.getX() + "," + l.getY() + " " + l.isVisible(); } });
        p("done");
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

package vmspec;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.MIDlet;

/** T25: LayerManager: z order (index 0 on top), append / insert / remove rules, view window, Graphics state restored. */
public class Gm04LayerManager extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }
    abstract static class T { abstract Object go() throws Exception; }
    static void t(String what, T c) {
        String r;
        try { Object o = c.go(); r = o == null ? "ok" : "ok " + o; } catch (Throwable e) { r = cn(e); }
        p(what + " -> " + r);
    }

    static Sprite solid(int rgb, int w, int h) {
        Image im = Image.createImage(w, h);
        Graphics g = im.getGraphics();
        g.setColor(rgb);
        g.fillRect(0, 0, w, h);
        return new Sprite(Image.createImage(im));
    }

    /** 12x8 target, row dump of the colour letters. */
    static String dump(Image im) {
        int w = im.getWidth(), h = im.getHeight();
        int[] px = new int[w * h];
        im.getRGB(px, 0, w, 0, 0, w, h);
        StringBuffer r = new StringBuffer();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int c = px[y * w + x] & 0xFFFFFF;
                r.append(c == 0xFF0000 ? 'R' : c == 0x00FF00 ? 'G' : c == 0x0000FF ? 'B' : c == 0xFFFFFF ? '.' : '?');
            }
            r.append('/');
        }
        return r.toString();
    }

    static Image target() {
        Image t = Image.createImage(12, 8);
        return t;
    }

    protected void startApp() {
        final LayerManager lm = new LayerManager();
        final Sprite r = solid(0xFF0000, 5, 5), g = solid(0x00FF00, 5, 5), b = solid(0x0000FF, 5, 5);
        r.setPosition(0, 0);
        g.setPosition(2, 2);
        b.setPosition(4, 4);
        lm.append(r);
        lm.append(g);
        lm.append(b);
        t("size / order", new T() { Object go() { return lm.getSize() + " " + (lm.getLayerAt(0) == r) + (lm.getLayerAt(2) == b); } });
        Image t1 = target();
        lm.paint(t1.getGraphics(), 0, 0);
        p("index 0 is on top: " + dump(t1));
        t("insert b at 0", new T() { Object go() { lm.insert(b, 0); return lm.getSize() + " " + (lm.getLayerAt(0) == b) + (lm.getLayerAt(1) == r) + (lm.getLayerAt(2) == g); } });
        Image t2 = target();
        lm.paint(t2.getGraphics(), 0, 0);
        p("after insert: " + dump(t2));
        t("append moves an existing layer to the end", new T() { Object go() { lm.append(b); return lm.getSize() + " " + (lm.getLayerAt(2) == b); } });
        t("insert at size", new T() { Object go() { lm.insert(g, lm.getSize()); return lm.getSize() + " " + (lm.getLayerAt(2) == g) + " (g was in the manager: removed first)"; } });
        t("insert index > size after the removal", new T() { Object go() { lm.insert(r, 9); return null; } });
        t("insert negative index", new T() { Object go() { lm.insert(r, -1); return null; } });
        t("insert null", new T() { Object go() { lm.insert(null, 0); return null; } });
        t("append null", new T() { Object go() { lm.append(null); return null; } });
        t("remove null", new T() { Object go() { lm.remove(null); return null; } });
        t("remove a layer that is not in the manager", new T() { Object go() { lm.remove(solid(0, 1, 1)); return new Integer(lm.getSize()); } });
        t("remove", new T() { Object go() { lm.remove(r); return lm.getSize() + " " + (lm.getLayerAt(0) == b); } });
        t("getLayerAt out of range", new T() { Object go() { return lm.getLayerAt(2); } });
        t("getLayerAt negative", new T() { Object go() { return lm.getLayerAt(-1); } });
        t("setViewWindow negative", new T() { Object go() { lm.setViewWindow(0, 0, -1, 4); return null; } });
        t("paint(null)", new T() { Object go() { lm.paint(null, 0, 0); return null; } });

        // view window and position, on a fresh manager: R at (0,0), G at (2,2), B at (4,4), index 0 = R on top
        final LayerManager vm = new LayerManager();
        final Sprite r2 = solid(0xFF0000, 5, 5), g2 = solid(0x00FF00, 5, 5), b2 = solid(0x0000FF, 5, 5);
        g2.setPosition(2, 2);
        b2.setPosition(4, 4);
        vm.append(r2);
        vm.append(g2);
        vm.append(b2);
        vm.setViewWindow(1, 1, 6, 4);
        Image t3 = target();
        vm.paint(t3.getGraphics(), 3, 2);
        p("view window (1,1,6,4) at (3,2): " + dump(t3));
        vm.setViewWindow(0, 0, 100000, 100000);
        Image t4 = target();
        vm.paint(t4.getGraphics(), 2, 1);
        p("huge view window at (2,1): " + dump(t4));
        // invisible layer is skipped; Graphics state (translate, clip) restored
        r2.setVisible(false);
        Image t5 = target();
        Graphics g5 = t5.getGraphics();
        g5.translate(1, 1);
        g5.setClip(1, 1, 8, 5);
        vm.paint(g5, 0, 0);
        p("invisible layer skipped, translate / clip: " + dump(t5));
        p("state restored: translate " + g5.getTranslateX() + "," + g5.getTranslateY() + " clip " + g5.getClipX() + "," + g5.getClipY() + "," + g5.getClipWidth() + "x" + g5.getClipHeight());
        p("done");
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

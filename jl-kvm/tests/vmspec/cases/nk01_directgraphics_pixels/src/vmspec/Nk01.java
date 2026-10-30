package vmspec;

import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

/**
 * DirectGraphics.drawPixels / getPixels for every pixel format of the Nokia UI API 1.1 javadoc, and the other DirectGraphics state rules
 * (alpha, polygons, exceptions). Expected values come from the javadoc (formats, the 1-bit examples with their byte arrays, the alpha rules).
 */
public class Nk01 extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static String hex(int v) { return Integer.toHexString(v); }

    static Image white(int w, int h) {
        Image im = Image.createImage(w, h);
        Graphics g = im.getGraphics();
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, w, h);
        return im;
    }

    static int px(Image im, int x, int y) {
        int[] a = new int[1];
        im.getRGB(a, 0, 1, x, y, 1, 1);
        return a[0];
    }

    static String bytes(byte[] a) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < a.length; i++) sb.append(i == 0 ? "" : " ").append(hex(a[i] & 0xFF));
        return sb.toString();
    }

    static String ex(Runnable r) {
        try {
            r.run();
            return "none";
        } catch (RuntimeException e) {
            String n = e.getClass().getName();
            return n.substring(n.lastIndexOf('.') + 1);
        }
    }

    public void startApp() {
        // ---- int formats
        Image im = white(4, 2);
        DirectGraphics dg = DirectUtils.getDirectGraphics(im.getGraphics());
        int[] src = {0xFF102030, 0x00ABCDEF, 0xFFFF0000, 0x80000000};
        dg.drawPixels(src, false, 0, 4, 0, 0, 4, 1, 0, DirectGraphics.TYPE_INT_8888_ARGB);
        p("int8888 opaque " + hex(px(im, 0, 0)) + " " + hex(px(im, 1, 0)) + " " + hex(px(im, 2, 0)) + " " + hex(px(im, 3, 0)));
        im = white(4, 2);
        dg = DirectUtils.getDirectGraphics(im.getGraphics());
        dg.drawPixels(src, true, 0, 4, 0, 0, 4, 1, 0, DirectGraphics.TYPE_INT_8888_ARGB);
        p("int8888 transparency " + hex(px(im, 0, 0)) + " " + hex(px(im, 1, 0)) + " " + hex(px(im, 2, 0)));
        im = white(2, 1);
        dg = DirectUtils.getDirectGraphics(im.getGraphics());
        dg.drawPixels(new int[] {0x00123456, 0x01654321}, true, 0, 2, 0, 0, 2, 1, 0, DirectGraphics.TYPE_INT_888_RGB);
        p("int888 ignores alpha " + hex(px(im, 0, 0)) + " " + hex(px(im, 1, 0)));
        int[] back = new int[6];
        dg.getPixels(back, 2, 2, 0, 0, 2, 1, DirectGraphics.TYPE_INT_888_RGB);
        p("int888 get " + hex(back[2]) + " " + hex(back[3]) + " " + back[0] + " " + back[4]);
        dg.getPixels(back, 0, 2, 0, 0, 2, 1, DirectGraphics.TYPE_INT_8888_ARGB);
        p("int8888 get " + hex(back[0]) + " " + hex(back[1]));
        // scanlength and offset are in pixels
        im = white(3, 2);
        dg = DirectUtils.getDirectGraphics(im.getGraphics());
        dg.drawPixels(new int[] {0, 0, 0xFF0000FF, 0xFF00FF00, 0, 0, 0xFFFF0000, 0xFFFFFF00}, false, 2, 4, 0, 0, 2, 2, 0, 8888);
        p("scanlength " + hex(px(im, 0, 0)) + " " + hex(px(im, 1, 0)) + " " + hex(px(im, 0, 1)) + " " + hex(px(im, 1, 1)));

        // ---- short formats: [format, value, expected ARGB]
        int[][] sh = {
            {565, 0xF800, 0xFFFF0000}, {565, 0x07E0, 0xFF00FF00}, {565, 0x001F, 0xFF0000FF}, {565, 0xFFFF, 0xFFFFFFFF},
            {444, 0x0F00, 0xFFFF0000}, {444, 0x00F0, 0xFF00FF00}, {444, 0x000F, 0xFF0000FF}, {444, 0x0123, 0xFF112233},
            {4444, 0xFF00, 0xFFFF0000}, {4444, 0xF0F0, 0xFF00FF00}, {4444, 0xF00F, 0xFF0000FF}, {4444, 0xF123, 0xFF112233},
            {555, 0x7C00, 0xFFFF0000}, {555, 0x03E0, 0xFF00FF00}, {555, 0x001F, 0xFF0000FF},
            {1555, 0xFC00, 0xFFFF0000}, {1555, 0x83E0, 0xFF00FF00}, {1555, 0x801F, 0xFF0000FF}};
        for (int i = 0; i < sh.length; i++) {
            im = white(1, 1);
            dg = DirectUtils.getDirectGraphics(im.getGraphics());
            dg.drawPixels(new short[] {(short) sh[i][1]}, true, 0, 1, 0, 0, 1, 1, 0, sh[i][0]);
            short[] o = new short[1];
            dg.getPixels(o, 0, 1, 0, 0, 1, 1, sh[i][0]);
            p("short " + sh[i][0] + " " + hex(sh[i][1]) + " -> " + hex(px(im, 0, 0)) + " " + (px(im, 0, 0) == sh[i][2]) + " back " + hex(o[0] & 0xFFFF));
        }
        im = white(2, 1);
        dg = DirectUtils.getDirectGraphics(im.getGraphics());
        dg.drawPixels(new short[] {(short) 0x0F00, (short) 0x7C00}, true, 0, 2, 0, 0, 2, 1, 0, DirectGraphics.TYPE_USHORT_4444_ARGB);
        p("4444 alpha 0 is transparent " + hex(px(im, 0, 0)));
        im = white(2, 1);
        dg = DirectUtils.getDirectGraphics(im.getGraphics());
        dg.drawPixels(new short[] {(short) 0x7C00, (short) 0x03E0}, true, 0, 2, 0, 0, 2, 1, 0, DirectGraphics.TYPE_USHORT_1555_ARGB);
        p("1555 alpha bit 0 is transparent " + hex(px(im, 0, 0)) + " " + hex(px(im, 1, 0)));

        // ---- 1 bit, horizontal: the 9x9 example of the javadoc (11 bytes)
        byte[] ex1 = {(byte) 0xC1, (byte) 0xF0, (byte) 0xFC, 0x7F, 0x3D, (byte) 0xDE, 0x7F, 0x1F, (byte) 0x87, (byte) 0xC1, (byte) 0x80};
        im = white(9, 9);
        dg = DirectUtils.getDirectGraphics(im.getGraphics());
        dg.drawPixels(ex1, null, 0, 9, 0, 0, 9, 9, 0, DirectGraphics.TYPE_BYTE_1_GRAY);
        int on = 0;
        for (int y = 0; y < 9; y++) for (int x = 0; x < 9; x++) if (px(im, x, y) == 0xFF000000) on++;
        p("1gray drawn black pixels " + on + " first row " + hex(px(im, 0, 0)) + " " + hex(px(im, 2, 0)) + " " + hex(px(im, 8, 0)));
        byte[] got = new byte[11];
        dg.getPixels(got, null, 0, 9, 0, 0, 9, 9, DirectGraphics.TYPE_BYTE_1_GRAY);
        p("1gray get " + bytes(got) + " same " + bytes(got).equals(bytes(ex1)));
        got = new byte[11];
        got[10] = 0x7F;
        dg.getPixels(got, null, 0, 9, 0, 0, 9, 9, DirectGraphics.TYPE_BYTE_1_GRAY);
        p("1gray unused bits stay " + hex(got[10] & 0xFF));

        // ---- 1 bit vertical: example 1 of the javadoc (18 bytes)
        byte[] exv = {(byte) 0xFF, (byte) 0xFF, 0x0E, 0x1C, 0x38, 0x70, (byte) 0xE0, (byte) 0xFF, (byte) 0xFF, 1, 1, 0, 0, 0, 0, 0, 1, 1};
        im = white(9, 9);
        dg = DirectUtils.getDirectGraphics(im.getGraphics());
        dg.drawPixels(exv, null, 0, 9, 0, 0, 9, 9, 0, DirectGraphics.TYPE_BYTE_1_GRAY_VERTICAL);
        got = new byte[18];
        dg.getPixels(got, null, 0, 9, 0, 0, 9, 9, DirectGraphics.TYPE_BYTE_1_GRAY_VERTICAL);
        p("1gray vertical ex1 " + bytes(got) + " same " + bytes(got).equals(bytes(exv)));
        // example 2: 10x13 context, block x 4..8, y 4..12 set; offset 16, scanlength 15
        im = white(10, 13);
        Graphics g = im.getGraphics();
        g.setColor(0);
        g.fillRect(4, 4, 5, 9);
        dg = DirectUtils.getDirectGraphics(g);
        byte[] ba = new byte[30];
        dg.getPixels(ba, null, 16, 15, 4, 4, 5, 9, DirectGraphics.TYPE_BYTE_1_GRAY_VERTICAL);
        p("1gray vertical ex2 " + bytes(ba));

        // ---- other byte formats: round trips, value 0 = white
        int[] fm = {2, 4, 8, 332};
        for (int k = 0; k < fm.length; k++) {
            int f = fm[k];
            int n = f == 2 ? 4 : (f == 4 ? 16 : 256);
            byte[] data = f == 2 ? new byte[] {0x1B} : (f == 4 ? new byte[] {0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF} : null);
            if (f == 8 || f == 332) {
                data = new byte[8];
                for (int i = 0; i < 8; i++) data[i] = (byte) (f == 8 ? i * 36 : (i * 37 + 3));
            }
            int cnt = f == 2 ? 4 : (f == 4 ? 16 : 8);
            im = white(cnt, 1);
            dg = DirectUtils.getDirectGraphics(im.getGraphics());
            dg.drawPixels(data, null, 0, cnt, 0, 0, cnt, 1, 0, f);
            byte[] o = new byte[data.length];
            dg.getPixels(o, null, 0, cnt, 0, 0, cnt, 1, f);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < cnt; i += (cnt > 8 ? 5 : 1)) sb.append(' ').append(hex(px(im, i, 0)));
            p("byte format " + f + " first pixels" + sb + " round trip " + bytes(o).equals(bytes(data)));
        }
        im = white(2, 1);
        dg = DirectUtils.getDirectGraphics(im.getGraphics());
        dg.drawPixels(new byte[] {(byte) 0xE0, 0x1F}, null, 0, 2, 0, 0, 2, 1, 0, DirectGraphics.TYPE_BYTE_332_RGB);
        p("332 " + hex(px(im, 0, 0)) + " " + hex(px(im, 1, 0)));
        // transparency mask: bit 0 transparent, 1 opaque
        im = white(4, 1);
        dg = DirectUtils.getDirectGraphics(im.getGraphics());
        dg.drawPixels(new byte[] {(byte) 0xF0}, new byte[] {(byte) 0xA0}, 0, 4, 0, 0, 4, 1, 0, DirectGraphics.TYPE_BYTE_1_GRAY);
        p("1gray mask " + hex(px(im, 0, 0)) + " " + hex(px(im, 1, 0)) + " " + hex(px(im, 2, 0)) + " " + hex(px(im, 3, 0)));

        // ---- exceptions
        final DirectGraphics d2 = DirectUtils.getDirectGraphics(white(4, 4).getGraphics());
        p("null pixels " + ex(new Runnable() { public void run() { d2.drawPixels((int[]) null, false, 0, 1, 0, 0, 1, 1, 0, 8888); } }));
        p("negative width " + ex(new Runnable() { public void run() { d2.drawPixels(new int[4], false, 0, 1, 0, 0, -1, 1, 0, 8888); } }));
        p("bad manipulation " + ex(new Runnable() { public void run() { d2.drawPixels(new int[4], false, 0, 2, 0, 0, 2, 2, 45, 8888); } }));
        p("int array short format " + ex(new Runnable() { public void run() { d2.drawPixels(new int[4], false, 0, 2, 0, 0, 2, 2, 0, 565); } }));
        p("short array int format " + ex(new Runnable() { public void run() { d2.drawPixels(new short[4], false, 0, 2, 0, 0, 2, 2, 0, 8888); } }));
        p("byte array bad format " + ex(new Runnable() { public void run() { d2.drawPixels(new byte[4], null, 0, 2, 0, 0, 2, 2, 0, 565); } }));
        p("array too small " + ex(new Runnable() { public void run() { d2.drawPixels(new int[3], false, 0, 2, 0, 0, 2, 2, 0, 8888); } }));
        p("negative offset " + ex(new Runnable() { public void run() { d2.drawPixels(new int[4], false, -1, 2, 0, 0, 2, 2, 0, 8888); } }));
        p("get negative x " + ex(new Runnable() { public void run() { d2.getPixels(new int[4], 0, 2, -1, 0, 2, 2, 8888); } }));
        p("get small array " + ex(new Runnable() { public void run() { d2.getPixels(new int[3], 0, 2, 0, 0, 2, 2, 8888); } }));
        p("get bad format " + ex(new Runnable() { public void run() { d2.getPixels(new short[4], 0, 2, 0, 0, 2, 2, 8888); } }));
        p("zero size ok " + ex(new Runnable() { public void run() { d2.drawPixels(new int[0], false, 0, 0, 0, 0, 0, 0, 0, 8888); } }));
        p("native format " + d2.getNativePixelFormat());

        // ---- clip and translation apply to drawPixels, not to getPixels
        im = white(6, 6);
        g = im.getGraphics();
        dg = DirectUtils.getDirectGraphics(g);
        g.translate(1, 1);
        g.setClip(0, 0, 2, 2);
        int[] red = new int[16];
        for (int i = 0; i < 16; i++) red[i] = 0xFFFF0000;
        dg.drawPixels(red, false, 0, 4, 0, 0, 4, 4, 0, 8888);
        p("clip " + hex(px(im, 1, 1)) + " " + hex(px(im, 2, 2)) + " " + hex(px(im, 3, 3)) + " " + hex(px(im, 0, 0)));
        int[] q = new int[16];
        dg.getPixels(q, 0, 4, 0, 0, 4, 4, 8888);
        p("get ignores clip " + hex(q[0]) + " " + hex(q[5]) + " " + hex(q[10]) + " " + hex(q[15]));

        // ---- alpha state shared with Graphics
        im = white(4, 1);
        g = im.getGraphics();
        dg = DirectUtils.getDirectGraphics(g);
        p("same wrapper " + (dg == DirectUtils.getDirectGraphics(g)) + " initial alpha " + dg.getAlphaComponent());
        dg.setARGBColor(0x80FF0000);
        p("alpha " + hex(dg.getAlphaComponent()) + " color " + hex(g.getColor()));
        g.fillRect(0, 0, 1, 1);
        int c = px(im, 0, 0);
        p("translucent fillRect red high " + ((c >> 16 & 255) == 255) + " green mid " + ((c >> 8 & 255) > 100 && (c >> 8 & 255) < 140));
        dg.setARGBColor(0x00FF0000);
        g.fillRect(1, 0, 1, 1);
        p("alpha 0 draws nothing " + hex(px(im, 1, 0)));
        dg.setARGBColor(0xFF00FF00);
        g.fillRect(2, 0, 1, 1);
        p("opaque " + hex(px(im, 2, 0)) + " alpha " + dg.getAlphaComponent());
        dg.setARGBColor(0x40FF0000);
        g.setColor(0x0000FF);
        p("setColor makes it opaque " + dg.getAlphaComponent());
        g.fillRect(3, 0, 1, 1);
        p("after setColor " + hex(px(im, 3, 0)));
        dg.setARGBColor(0x20123456);
        g.setGrayScale(10);
        p("setGrayScale " + dg.getAlphaComponent());
        dg.setARGBColor(0x20123456);
        g.setColor(1, 2, 3);
        p("setColor rgb " + dg.getAlphaComponent());

        // ---- polygons use their own colour and keep the current one
        im = white(12, 12);
        g = im.getGraphics();
        dg = DirectUtils.getDirectGraphics(g);
        g.setColor(0x00FF00);
        dg.fillPolygon(new int[] {1, 9, 9, 1}, 0, new int[] {1, 1, 9, 9}, 0, 4, 0xFFFF0000);
        p("fillPolygon inside " + hex(px(im, 4, 4)) + " " + hex(px(im, 1, 1)) + " outside " + hex(px(im, 10, 10)) + " " + hex(px(im, 0, 4)) + " color kept " + hex(g.getColor()));
        im = white(12, 12);
        g = im.getGraphics();
        dg = DirectUtils.getDirectGraphics(g);
        dg.fillTriangle(0, 0, 10, 0, 0, 10, 0xFF0000FF);
        p("fillTriangle " + hex(px(im, 2, 2)) + " " + hex(px(im, 9, 9)));
        dg.drawTriangle(1, 11, 10, 11, 5, 3, 0xFF000000);
        p("drawTriangle edge " + hex(px(im, 5, 11)) + " closed side " + hex(px(im, 3, 7)));
        im = white(12, 12);
        g = im.getGraphics();
        dg = DirectUtils.getDirectGraphics(g);
        dg.drawPolygon(new int[] {2, 8, 8, 2}, 0, new int[] {2, 2, 8, 8}, 0, 4, 0xFF123456);
        p("drawPolygon closed " + hex(px(im, 2, 5)) + " " + hex(px(im, 5, 2)) + " " + hex(px(im, 8, 5)) + " " + hex(px(im, 5, 8)) + " inside " + hex(px(im, 5, 5)));
        im = white(12, 12);
        g = im.getGraphics();
        dg = DirectUtils.getDirectGraphics(g);
        dg.fillPolygon(new int[] {1, 9, 9, 1}, 0, new int[] {1, 1, 9, 9}, 0, 4, 0x00FF0000);
        p("fill alpha 0 " + hex(px(im, 4, 4)));
        dg.fillPolygon(new int[] {0, 5, 11, 4, 8}, 0, new int[] {5, 0, 5, 10, 10}, 0, 3, 0xFF00FF00);
        p("fillPolygon offsets/nPoints inside " + hex(px(im, 5, 3)));
        p("polygon null " + ex(new Runnable() { public void run() { d2.fillPolygon(null, 0, new int[3], 0, 3, 0); } }));
        p("polygon range " + ex(new Runnable() { public void run() { d2.fillPolygon(new int[3], 0, new int[3], 1, 3, 0); } }));
        notifyDestroyed();
    }

    public void pauseApp() {}
    public void destroyApp(boolean u) {}
}

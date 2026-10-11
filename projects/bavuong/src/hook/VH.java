import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Lop hook Viet hoa (NAM.P) cho 霸王怒与美人泪.
 *  - ds/sw/ssw/fh : thay Graphics.drawString, Font.stringWidth/substringWidth/getHeight -> BitmapFont 15c.
 *  - wA/wD/wC     : thay 3 ham ngat dong theo KY TU cua o -> ngat theo TU, tinh bang px.
 *  - dsn          : thay o.a(String, soKyTu, x, y, mau, anchor) (chia dong theo so ky tu co dinh).
 *  - box/dlg      : khung thoai moi (o.aI nhanh eS==1) - nen toi ban trong suot, vien vang, tab ten, 3 dong.
 * Chi dung CLDC 1.0 / MIDP 2.0.
 */
public final class VH {
    private static BitmapFont F;

    /** be rong 1 chu Han cua font goc (o.o = stringWidth("宽")) - giu cho cac bo cuc tinh theo o.o */
    static final int CJKW = 14;

    public static BitmapFont f() {
        if (F == null) {
            BitmapFont.setDefault(BitmapFont.SIZE_15, BitmapFont.SELECTIVE);
            F = BitmapFont.current();
        }
        return F;
    }

    // ------------------------------------------------------------ font hook
    public static void ds(Graphics g, String s, int x, int y, int a) {
        if (s == null) return;
        if (a == 0) a = Graphics.TOP | Graphics.LEFT;
        f().drawString(g, s, x, y, a);
    }

    public static int sw(Font fo, String s) {
        if (s != null && s.length() == 1 && s.charAt(0) >= 0x2E80) return CJKW;
        return f().stringWidth(s);
    }

    public static int ssw(Font fo, String s, int off, int len) { return f().substringWidth(s, off, len); }

    public static int fh(Font fo) { return f().getHeight(); }

    // ------------------------------------------------------------ ngat dong
    /** ngat theo tu; '|' va '\n' la xuong dong cung */
    public static String[] lines(String s, int maxW) {
        if (s == null) s = "";
        if (s.indexOf('|') >= 0) s = s.replace('|', '\n');
        if (maxW < 8) maxW = 8;
        String[] r = f().wrap(s, maxW);
        if (r == null || r.length == 0) r = new String[] { "" };
        return r;
    }

    /** o.a(String, int start, int maxW) -> eO[start + i].a */
    public static int wA(o G, String s, int start, int maxW) {
        String[] r = lines(s, maxW);
        int n = 0;
        for (int i = 0; i < r.length && start + i < G.eO.length; i++) { G.eO[start + i].a = r[i]; n++; }
        return n;
    }

    /** o.c(String, int maxW) -> bP[i] */
    public static int wC(o G, String s, int maxW) {
        String[] r = lines(s, maxW);
        int n = 0;
        for (int i = 0; i < r.length && i < G.bP.length; i++) { G.bP[i] = r[i]; n++; }
        return n;
    }

    /** ten nguoi noi cua tung cau thoai (theo chi so eP) */
    static final String[] NM = new String[100];

    /** o.a(String[] {anh, sprite, cam xuc, "[Ten]:cau"}, int idx, int maxW) -> eP[idx] */
    public static int wD(o G, String[] sa, int idx, int maxW) {
        try {
            t T = G.eP[idx];
            T.a = (byte) Integer.parseInt(sa[0]);
            T.b = (byte) Integer.parseInt(sa[1]);
            T.c = (byte) Integer.parseInt(sa[2]);
            String s = sa[3], name = null;
            if (s.length() > 0 && s.charAt(0) == '[') {
                int c = s.indexOf("]:");
                if (c < 0) c = s.indexOf("]：");
                if (c > 0) {
                    name = s.substring(1, c);
                    s = s.substring(c + 2);
                    int p;
                    while ((p = name.indexOf("][")) >= 0) name = name.substring(0, p) + ", " + name.substring(p + 2);
                }
            }
            if (idx < NM.length) NM[idx] = name;
            T.d = lines(s, TW);
            T.e = T.d.length;
            return T.e;
        } catch (Exception e) {
            return 0;
        }
    }

    /** o.a(String s, int soKyTu, x, y, mau, anchor): ngat theo px = soKyTu * CJKW */
    public static void dsn(o G, String s, int n, int x, int y, int color, int anchor) {
        if (s == null) return;
        G.e.setColor(color);
        String[] r = lines(s, (n < 1 ? 1 : n) * CJKW);
        int lh = G.p + 1;
        for (int i = 0; i < r.length; i++) f().drawString(G.e, r[i], x, y + i * lh, anchor);
    }

    // ------------------------------------------------------------ khung thoai
    static final int BX = 2, BW = 236, BH = 60, LH = 16, TX = 9, TW = 220, PAGE = 3;
    private static Image bg;

    private static int boxY(o G) { return G.bx[1] - BH - 1; }

    private static void frame(Graphics g, int x, int y, int w, int h) {
        // vien ngoai toi + vien vang trong, bo goc 1px
        g.setColor(0x24170A);
        g.drawLine(x + 1, y, x + w - 2, y);
        g.drawLine(x + 1, y + h - 1, x + w - 2, y + h - 1);
        g.drawLine(x, y + 1, x, y + h - 2);
        g.drawLine(x + w - 1, y + 1, x + w - 1, y + h - 2);
        g.setColor(0xD9B46A);
        g.drawLine(x + 2, y + 1, x + w - 3, y + 1);
        g.drawLine(x + 2, y + h - 2, x + w - 3, y + h - 2);
        g.drawLine(x + 1, y + 2, x + 1, y + h - 3);
        g.drawLine(x + w - 2, y + 2, x + w - 2, y + h - 3);
        g.setColor(0x6E5326);
        g.drawLine(x + 3, y + 2, x + w - 4, y + 2);
    }

    /** vung 1 cua o.aI (eS==1): ve nen + vien khung */
    public static void box(o G) {
        Graphics g = G.e;
        int y = boxY(G);
        if (bg == null) {
            int iw = BW - 4, ih = BH - 4;
            int[] px = new int[iw * ih];
            for (int yy = 0; yy < ih; yy++) {
                int a = 0xE0 + (yy * 0x16) / ih;            // tren trong hon, duoi dac hon (HUD duoi khung khong lo)
                int c = (a << 24) | 0x0E1222;
                for (int xx = 0; xx < iw; xx++) px[yy * iw + xx] = c;
            }
            bg = Image.createRGBImage(px, iw, ih, true);
        }
        g.drawImage(bg, BX + 2, y + 2, Graphics.TOP | Graphics.LEFT);
        frame(g, BX, y, BW, BH);
    }

    private static void outlined(Graphics g, String s, int x, int y, int c) {
        BitmapFont fo = f();
        g.setColor(0x000000);
        fo.drawString(g, s, x + 1, y + 1, Graphics.TOP | Graphics.LEFT);
        g.setColor(c);
        fo.drawString(g, s, x, y, Graphics.TOP | Graphics.LEFT);
    }

    /** vung 2 cua o.aI: chan dung, tab ten, chu (trang G.F, 3 dong), nhan "#Bo qua", mui ten trang sau */
    public static void dlg(o G, int color) {
        Graphics g = G.e;
        BitmapFont fo = f();
        int W = G.bx[0], by = boxY(G);
        t T = G.eP[G.eQ];
        // chan dung nho co san cua game: dung tren khung (a = 1..2 ben phai, con lai ben trai)
        int side = 0;                                   // 0 = khong co, 1 = trai, 2 = phai
        int iw = 0;
        if (G.ac != null && T.a > 0) {
            int ih = G.ac.getHeight();
            iw = G.ac.getWidth();
            side = T.a < 3 ? 2 : 1;
            g.drawImage(G.ac, side == 2 ? W - iw : 0, by - ih + 1, Graphics.TOP | Graphics.LEFT);
        }
        // tab ten: ben trai, sang phai neu chan dung o trai
        String name = G.eQ < NM.length ? NM[G.eQ] : null;
        boolean tabRight = false;
        int tabL = BX + 6, tabR = BX + 6;               // pham vi ngang cua tab ten
        if (name != null && name.length() > 0) {
            int tw = fo.stringWidth(name) + 14, th = 19;
            tabRight = side == 1;
            int tx = tabRight ? BX + BW - tw - 6 : BX + 6, ty = by - th + 2;
            tabL = tx; tabR = tx + tw;
            g.setColor(0x0E1222);
            g.fillRect(tx + 2, ty + 2, tw - 4, th - 4);
            frame(g, tx, ty, tw, th);                        // day tab trung vien tren cua khung
            outlined(g, name, tx + 7, ty + 2, 0xFFE08A);
        }
        // chu
        int n = T.e, first = 0;
        if (n <= PAGE) G.F = 0; else first = G.F * PAGE;
        for (int i = 0; i < PAGE && first + i < n; i++) {
            outlined(g, T.d[first + i], BX + TX, by + 6 + i * LH, color);
        }
        boolean blink = (System.currentTimeMillis() / 350 & 1) == 0;
        if (first + PAGE < n && blink) {                  // con trang sau
            int ax = BX + BW - 13, ay = by + BH - 9;
            g.setColor(0xFFE08A);
            g.fillTriangle(ax, ay, ax + 7, ay, ax + 3, ay + 4);
        }
        if (G.fj == 0) {
            // nhan "#Bo qua": dat vao khoang trong giua tab ten va chan dung tren mep khung; khong du cho thi an
            String sk = "#Bỏ qua";
            int w = fo.stringWidth(sk), x;
            if (side == 1) x = tabL - w - 6;                       // chan dung trai, tab phai
            else if (side == 2) x = tabR + 6;                      // chan dung phai, tab trai
            else x = BX + BW - w - 8;                              // khong chan dung: goc phai
            int lo = side == 1 ? iw + 2 : BX + 2, hi = side == 2 ? W - iw - 2 : BX + BW - 2;
            if (x >= lo && x + w <= hi && (x >= tabR + 4 || x + w <= tabL - 4))
                outlined(g, sk, x, by - 15, 0xFFD24A);
        }
    }
}

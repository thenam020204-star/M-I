import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Lop hook Viet hoa (NAM.P) cho 魔界战纪-魔女奇缘.
 *  - ds/sw/cw/fh : thay Graphics.drawString, Font.stringWidth/charWidth/getHeight -> BitmapFont 13c.
 *  - wrap        : thay d.a(String,Font,int) - ngat dong theo TU (giu ma | [C:xxxxxx] [/C] [N/]).
 *  - open/tick/paint : khung thoai moi 3 dong (k.a(BBB) / k.w() / k.d(Graphics) goi vao day).
 * Chi dung CLDC 1.0 / MIDP 2.0.
 */
public final class VH {
    private static BitmapFont F;

    public static BitmapFont f() {
        if (F == null) {
            BitmapFont.setDefault(BitmapFont.SIZE_13, BitmapFont.SELECTIVE);
            F = BitmapFont.current();
        }
        return F;
    }

    // ------------------------------------------------------------ font hook
    public static void ds(Graphics g, String s, int x, int y, int a) {
        if (s == null) return;
        if (a == 0) a = Graphics.TOP | Graphics.LEFT;
        // nhan neo trai o nua phai man hinh (vd phim mem phai, game dat theo be rong 1 chu Han)
        // ma chu Viet dai hon -> lui vao de khong tran mep
        if ((a & (Graphics.HCENTER | Graphics.RIGHT)) == 0 && x > (f.ad >> 1) && x < f.ad) {
            int w = f().stringWidth(s);
            if (x + w > f.ad - 1) x = f.ad - 1 - w;
        }
        f().drawString(g, s, x, y, a);
    }

    public static int sw(Font fo, String s) { return f().stringWidth(s); }

    public static int cw(Font fo, char c) { return f().charWidth(c); }

    public static int fh(Font fo) { return f().getHeight(); }

    // ------------------------------------------------------------ word wrap
    private static Vector wOut;
    private static StringBuffer wLine;
    private static int wW;
    private static String wOpen, wCur;

    private static void emit() {
        String s = wLine.toString();
        if (wOpen != null) s = wOpen + s;
        if (wCur != null) s = s + "[/C]";
        wOut.addElement(s);
        wOpen = wCur;
        wLine.setLength(0);
        wW = 0;
    }

    /** do rong phan chu hien thi cua tu (bo qua tag mau) */
    private static int wordW(String s, int b, int e) {
        BitmapFont fo = f();
        int w = 0;
        for (int i = b; i < e; i++) {
            char c = s.charAt(i);
            if (c == '[' && i + 3 < e) {
                if (s.charAt(i + 1) == 'C' && s.charAt(i + 2) == ':') { i += 9; continue; }
                if (s.charAt(i + 1) == '/' && s.charAt(i + 2) == 'C') { i += 3; continue; }
            }
            w += fo.charWidth(c);
        }
        return w;
    }

    /** chep tu vao dong; breakable = cat theo ky tu khi qua maxW */
    private static void put(String s, int b, int e, int maxW, boolean breakable) {
        BitmapFont fo = f();
        for (int i = b; i < e; i++) {
            char c = s.charAt(i);
            if (c == '[' && i + 3 < e) {
                if (s.charAt(i + 1) == 'C' && s.charAt(i + 2) == ':' && i + 9 < e + 1) {
                    String t = s.substring(i, Math.min(i + 10, s.length()));
                    wLine.append(t); wCur = t; i += 9; continue;
                }
                if (s.charAt(i + 1) == '/' && s.charAt(i + 2) == 'C') {
                    wLine.append("[/C]"); wCur = null; i += 3; continue;
                }
            }
            int w = fo.charWidth(c);
            if (breakable && wW > 0 && wW + w > maxW) emit();
            wLine.append(c);
            wW += w;
        }
    }

    public static String[] wrap(String s, Font font, int maxW) {
        if (s == null) s = "";
        int p;
        while ((p = s.indexOf("[N/]")) >= 0) s = s.substring(0, p) + j.aS + s.substring(p + 4);
        BitmapFont fo = f();
        int spW = fo.charWidth(' ');
        wOut = new Vector();
        wLine = new StringBuffer();
        wW = 0; wOpen = null; wCur = null;
        int n = s.length(), i = 0, spaces = 0;
        while (i < n) {
            char c = s.charAt(i);
            if (c == '|') { emit(); spaces = 0; i++; continue; }
            if (c == ' ') { spaces++; i++; continue; }
            int e = i;
            while (e < n && s.charAt(e) != ' ' && s.charAt(e) != '|') e++;
            int w = wordW(s, i, e);
            int lead = wW > 0 ? spaces * spW : 0;
            if (wW + lead + w <= maxW) {
                if (wW > 0) for (int k = 0; k < spaces; k++) { wLine.append(' '); wW += spW; }
                put(s, i, e, maxW, false);
            } else if (w <= maxW) {
                if (wW > 0) emit();
                put(s, i, e, maxW, false);
            } else {
                if (wW > 0) {
                    for (int k = 0; k < spaces; k++) { wLine.append(' '); wW += spW; }
                }
                put(s, i, e, maxW, true);
            }
            spaces = 0;
            i = e;
        }
        emit();
        String[] r = new String[wOut.size()];
        wOut.copyInto(r);
        wOut = null; wLine = null;
        d.e = r;
        return r;
    }

    // ------------------------------------------------------------ khung thoai
    static final int BX = 2, BW = 236, BH = 58, LH = 16, TX = 10, TW = 218;
    private static String[] dl;        // cac dong da ngat
    private static int dPage, dState, dTick, dWho, dSide, dReveal, dTotal;
    private static int[] dLineW = new int[3];
    private static String dName;
    private static Image bg, head;
    private static int headId = -2;

    /** k.a(byte nv, byte cau, byte phia) */
    public static void open(String s, int who, int side) {
        dl = wrap(s, null, TW);
        dPage = 0; dTick = 0; dState = 1; dWho = who; dSide = side;
        dName = null;
        if (who >= 0 && who < l.H.length) dName = who == 0 ? j.aS : l.H[who];
        setPage();
    }

    private static void setPage() {
        dTotal = 0;
        for (int i = 0; i < 3; i++) {
            int k = dPage + i;
            dLineW[i] = k < dl.length ? wordW(dl[k], 0, dl[k].length()) : 0;
            dTotal += dLineW[i];
        }
        dReveal = 0;
    }

    /** k.w(): tra ve trang thai moi (0 = da dong) */
    public static int tick() {
        dTick++;
        switch (dState) {
            case 1:
                if (dTick > 3) { dState = 2; dTick = 0; }
                break;
            case 2:
                dReveal += 14;
                if (j.m() || dReveal >= dTotal) { dReveal = dTotal; dState = 3; dTick = 0; }
                break;
            case 3:
                if (j.m()) {
                    dPage += 3;
                    if (dPage < dl.length) { dState = 2; setPage(); } else { dState = 4; }
                    dTick = 0;
                }
                break;
            case 4:
                if (dTick > 3) { dState = 0; dl = null; }
                break;
        }
        return dState;
    }

    private static void box(Graphics g, int x, int y, int w, int h) {
        if (bg == null) {
            int iw = BW - 4, ih = BH - 4;
            int[] px = new int[iw * ih];
            for (int yy = 0; yy < ih; yy++) {
                int a = 0x98 + (yy * 0x58) / ih;       // tren trong hon, duoi dac hon
                int c = (a << 24) | 0x0D1326;
                for (int xx = 0; xx < iw; xx++) px[yy * iw + xx] = c;
            }
            bg = Image.createRGBImage(px, iw, ih, true);
        }
        g.setClip(x + 2, y + 2, w - 4, h - 4);
        g.drawImage(bg, x + 2, y + 2 + h - BH, Graphics.TOP | Graphics.LEFT);
        g.setClip(0, 0, f.ad, f.ae);
        g.setColor(0x2A1A04);
        g.drawRect(x, y, w - 1, h - 1);
        g.setColor(0xE0C080);
        g.drawRect(x + 1, y + 1, w - 3, h - 3);
    }

    private static Image loadHead(int id) {
        try { return g.a("head_" + id + ".png", false, 0, 0); } catch (Throwable t) { return null; }
    }

    /** k.d(Graphics) */
    public static void paint(Graphics g, Object yj) {
        if (dState == 0 || dl == null) return;
        int W = f.ad, H = f.ae;
        int by = H - BH - 2;
        g.setClip(0, 0, W, H);
        if (dState == 1 || dState == 4) {
            int st = dState == 1 ? dTick : 3 - dTick;
            if (st < 0) st = 0;
            if (st > 3) st = 3;
            int h = 6 + (BH - 6) * st / 3;
            box(g, BX, by + BH - h, BW, h);
            return;
        }
        // chan dung: ve TRUOC khung (chim sau khung ban trong suot), khong khung vuong.
        // Anh lon (vd head_0 moi, cao 150): day anh trung canh day khung thoai.
        // Anh nho cu (54x54): nho len tren khung, chim 12px sau vien tren.
        boolean hasHead = false;
        if (dWho >= 0 && dWho < l.I.length && l.I[dWho] != -1) {
            if (headId != l.I[dWho]) {
                head = loadHead(l.I[dWho]);
                headId = l.I[dWho];
            }
            if (head != null) {
                int hw = head.getWidth(), hh = head.getHeight();
                int hy = hh > BH + 40 ? by + BH - hh : by - hh + 12;
                int hx = dSide == 0 ? 0 : W - hw;
                g.setClip(0, 0, W, by + BH);
                g.drawImage(head, hx, hy, Graphics.TOP | Graphics.LEFT);
                g.setClip(0, 0, W, H);
                hasHead = true;
            }
        }
        box(g, BX, by, BW, BH);
        int tagX = BX + 4;
        if (dName != null && dName.length() > 0) {
            BitmapFont fo = f();
            int tw = fo.stringWidth(dName) + 12, th = 17;
            int ty = by - th + 1;
            if (hasHead && dSide == 0) tagX = BX + BW - tw - 4;   // bang ten o phia doi dien chan dung
            g.setColor(0x171A2F);
            g.fillRect(tagX + 2, ty + 2, tw - 4, th - 3);
            g.setColor(0x2A1A04);
            g.drawRect(tagX, ty, tw - 1, th - 1);
            g.setColor(0xE0C080);
            g.drawRect(tagX + 1, ty + 1, tw - 3, th - 3);
            g.setColor(0x000000);
            fo.drawString(g, dName, tagX + 7, ty + 3, Graphics.TOP | Graphics.LEFT);
            g.setColor(0xFFD66E);
            fo.drawString(g, dName, tagX + 6, ty + 2, Graphics.TOP | Graphics.LEFT);
        }
        // chu: hieu ung go theo px qua tung dong
        int left = dReveal;
        for (int i = 0; i < 3; i++) {
            int k = dPage + i;
            if (k >= dl.length || left <= 0) break;
            int ly = by + 6 + i * LH;
            int vis = left < dLineW[i] ? left : dLineW[i] + 2;
            g.setClip(BX + TX - 1, ly, vis + 2, LH);
            g.setColor(0x000000);
            j.a(g, dl[k], BX + TX + 1, ly + 1, Graphics.TOP | Graphics.LEFT, false);
            g.setColor(0xFFFFFF);
            j.a(g, dl[k], BX + TX, ly, Graphics.TOP | Graphics.LEFT, true);
            left -= dLineW[i];
        }
        g.setClip(0, 0, W, H);
        if (dState == 3 && (dTick & 4) == 0) {
            int ax = BX + BW - 12, ay = by + BH - 5;
            g.setColor(0xFFD66E);
            g.fillTriangle(ax, ay, ax + 7, ay, ax + 3, ay + 4);
        }
    }
}

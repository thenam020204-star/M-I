import javax.microedition.lcdui.Graphics;

/**
 * Ve chu cua game (thay renderer dotFontSmall cu, class "ac").
 * Chu trong tai nguyen la byte[] UTF-8 (tieng Viet 2-3 byte/ky tu), ve bang BitmapFont f13c.
 * Ma dieu khien 1 byte: & | $ { } ^ \  = doi mau (bang COLOR_CODES/COLORS), ';' = xuong dong.
 * Moi ham do/ngat dong dung chung nextLine() nen ve, do, phan trang va go chu luon khop nhau.
 * Khong tao object trong luc ve.
 */
public final class TextRenderer {
    /** false = bo qua ma mau (ve mot mau). */
    public static boolean colorCodesOn = true;
    /** Chieu cao chu (px); khoang cach dong = lineHeight + 2. */
    public static int lineHeight = 13;

    private static final byte[] COLOR_CODES = { 38, 124, 36, 123, 125, 94, 92 };
    private static final int[] COLORS = { 0xFFFFFF, 0xFFFF00, 0x18DFFF, 0xFF9F3F, 0xDEBE7B, 0x00FF00, 0x7F7F7F };
    private static final byte NEWLINE = 59;
    /** Le phai khi ngat tai dau cach (giong ban goc). */
    private static final int WRAP_MARGIN = 7;

    private static BitmapFont font;
    private static boolean ready;

    // ket qua tra ve phu cua decode()/nextLine()
    private static int charLen;
    private static int lineEnd;

    public static final void init() {
        if (ready) return;
        BitmapFont.setDefault(BitmapFont.SIZE_13, BitmapFont.SELECTIVE);
        font = BitmapFont.current();
        if (font != null) {
            lineHeight = font.getHeight();
            ready = true;
        }
    }

    /** Thay String.getBytes() cua chuoi hien thi trong class: luon ma hoa UTF-8 (getBytes() mac dinh tuy may). */
    public static byte[] utf8(String s) {
        int n = 0, len = s.length();
        for (int i = 0; i < len; i++) { char c = s.charAt(i); n += c < 0x80 ? 1 : (c < 0x800 ? 2 : 3); }
        byte[] b = new byte[n];
        int p = 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c < 0x80) b[p++] = (byte) c;
            else if (c < 0x800) { b[p++] = (byte) (0xC0 | (c >> 6)); b[p++] = (byte) (0x80 | (c & 0x3F)); }
            else { b[p++] = (byte) (0xE0 | (c >> 12)); b[p++] = (byte) (0x80 | ((c >> 6) & 0x3F)); b[p++] = (byte) (0x80 | (c & 0x3F)); }
        }
        return b;
    }

    // ------------------------------------------------------------ giai ma

    /** Ky tu UTF-8 tai p; do dai byte ghi vao charLen. */
    private static char decode(byte[] t, int p) {
        int b = t[p] & 0xFF;
        if (b < 0x80) { charLen = 1; return (char) b; }
        if (b < 0xE0 && p + 1 < t.length) {
            charLen = 2;
            return (char) (((b & 0x1F) << 6) | (t[p + 1] & 0x3F));
        }
        if (p + 2 < t.length) {
            charLen = 3;
            return (char) (((b & 0x0F) << 12) | ((t[p + 1] & 0x3F) << 6) | (t[p + 2] & 0x3F));
        }
        charLen = 1;
        return '?';
    }

    private static int colorIndex(byte b) {
        for (int i = 0; i < COLOR_CODES.length; i++) if (COLOR_CODES[i] == b) return i;
        return -1;
    }

    private static final boolean isControl(byte b) {
        return b == NEWLINE || colorIndex(b) >= 0;
    }

    private static int charW(char c) {
        return font == null ? 6 : font.charWidth(c);
    }

    // ------------------------------------------------------------ ngat dong

    /** Bo dau cach dau dong. */
    private static int skipSpaces(byte[] t, int p, int end) {
        while (p < end && t[p] == 32) p++;
        return p;
    }

    /**
     * Mot dong bat dau tai p (da bo dau cach), rong toi da w.
     * Tra ve vi tri bat dau dong sau; vi tri het dong (khong gom) ghi vao lineEnd.
     */
    private static int nextLine(byte[] t, int p, int end, int w) {
        int start = p;
        int x = 0;
        int lastSpace = -1;
        while (p < end) {
            byte b = t[p];
            if (b == NEWLINE) { lineEnd = p; return p + 1; }
            if (colorIndex(b) >= 0) { p++; continue; }
            char c = decode(t, p);
            int cw = charW(c);
            if (c == ' ') {
                // dau cach: tu tiep theo co vua dong khong
                int q = p + 1, wx = x + cw;
                while (q < end && t[q] != 32 && t[q] != NEWLINE) {
                    if (colorIndex(t[q]) >= 0) { q++; continue; }
                    wx += charW(decode(t, q));
                    q += charLen;
                }
                if (wx > w - WRAP_MARGIN && x > 0) { lineEnd = p; return p + 1; }
                lastSpace = p;
            } else if (x + cw > w && p > start) {
                if (lastSpace > start) { lineEnd = lastSpace; return lastSpace + 1; }
                lineEnd = p;
                return p;
            }
            x += cw;
            p += charLen;
        }
        lineEnd = end;
        return end;
    }

    // ------------------------------------------------------------ ve

    public static void drawText(GameGraphics g, int x, int y, byte[] text, int unused) {
        drawTextRange(g, x, y, 1000, unused, text, 0, 0, text.length);
    }

    public static void drawTextBox(GameGraphics g, int x, int y, int w, int unused, byte[] text) {
        drawTextRange(g, x, y, w, unused, text, 0, 0, text.length);
    }

    /** align: 4 = trai, 1 = giua, khac = phai (theo dong dau). */
    public static void drawTextAligned(GameGraphics g, int x, int y, byte[] text, int unused, int align) {
        int w = textWidth(text);
        if (align == 4) w = 0;
        else if (align == 1) w >>= 1;
        drawTextRange(g, x - w, y, 1000, unused, text, 0, 0, text.length);
    }

    public static void drawTextRange(GameGraphics g, int x, int y, int w, int unused, byte[] text, int start, int unused2, int len) {
        drawTextCore(g, x, y, w, text, start, len, 4);
    }

    /**
     * Ve len byte tu start, ngat dong theo rong w (tinh tren ca chuoi de chu go dan khong nhay dong).
     * align 4 = can trai tai x, 1 = can giua quanh x.
     */
    public static void drawTextCore(GameGraphics gg, int x, int y, int w, byte[] text, int start, int len, int align) {
        if (!ready || text == null || gg.alphaMask != -1) return;
        Graphics g = gg.g;
        int end = text.length;
        int limit = start + len;
        if (limit > end) limit = end;
        int p = start;
        while (p < limit) {
            p = skipSpaces(text, p, end);
            if (p >= limit) break;
            int next = nextLine(text, p, end, w);
            int le = lineEnd;
            int lx = x;
            if (align == 1) lx = x - (rangeWidth(text, p, le) >> 1);
            while (p < le && p < limit) {
                byte b = text[p];
                int ci = colorIndex(b);
                if (ci >= 0) {
                    if (colorCodesOn) gg.setColor(COLORS[ci]);
                    p++;
                    continue;
                }
                char c = decode(text, p);
                lx += font.drawChar(g, c, lx, y);
                p += charLen;
            }
            // ma mau o cuoi dong van ap dung cho dong sau
            while (p < next && p < limit) {
                int ci = colorIndex(text[p]);
                if (ci >= 0 && colorCodesOn) gg.setColor(COLORS[ci]);
                p++;
            }
            p = next;
            y += lineHeight + 2;
        }
    }

    // ------------------------------------------------------------ do

    private static int rangeWidth(byte[] t, int p, int end) {
        int w = 0;
        while (p < end) {
            if (isControl(t[p])) { p++; continue; }
            w += charW(decode(t, p));
            p += charLen;
        }
        return w;
    }

    /** So byte tu start den dau dong thu n (dung de chia trang hoi thoai). */
    public static final int bytesForLines(byte[] text, int start, int w, int n) {
        int end = text.length;
        int p = start;
        for (int i = 0; i < n && p < end; i++) {
            p = skipSpaces(text, p, end);
            if (p >= end) break;
            p = nextLine(text, p, end, w);
        }
        return p - start;
    }

    /** Hieu ung go chu: so byte da hien sau khi go them 2 ky tu (tinh tu start). */
    public static final int typeNextChar(byte[] text, int start, int typed) {
        int p = start + typed;
        int end = text.length;
        int shown = 0;
        while (p < end) {
            byte b = text[p];
            if (b == 32 || isControl(b)) { p++; continue; }
            decode(text, p);
            p += charLen;
            if (++shown == 2) return p - start;
        }
        return end - start;
    }

    public static final int lineCount(byte[] text, int w) {
        int end = text.length;
        int p = 0, n = 0;
        while (true) {
            p = skipSpaces(text, p, end);
            if (p >= end) break;
            p = nextLine(text, p, end, w);
            n++;
        }
        return n;
    }

    public static final int textWidth(byte[] text) {
        return lineWidthFrom(text, 0, 0);
    }

    /** Rong dong dau tinh tu start (dung o ';'); +1 nhu ban goc, 0 neu rong. */
    public static final int lineWidthFrom(byte[] text, int start, int unused) {
        int p = start, end = text.length;
        while (p < end && text[p] != NEWLINE) p++;
        int w = rangeWidth(text, start, p);
        return w == 0 ? 0 : w + 1;
    }
}

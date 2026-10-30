/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import jlkvm.internal.Access;
import jlkvm.internal.LcduiAccess;

/**
 * The guest keeps the cheap state (colour, translation, clip, stroke, font); every drawing native gets the state it needs as
 * arguments (target raster handle, colour, absolute clip box, absolute coordinates), so the host keeps no per-Graphics state.
 */
public class Graphics {
    public static final int HCENTER = 1, VCENTER = 2, LEFT = 4, RIGHT = 8, TOP = 16, BOTTOM = 32, BASELINE = 64;
    public static final int SOLID = 0, DOTTED = 1;

    static {
        Access.lcdui = new LcduiAccess() {
            public int argb(Object g) {
                Graphics gr = (Graphics) g;
                return ((255 - (gr.rgba >>> 24)) << 24) | gr.color;
            }

            public void setArgb(Object g, int argb) {
                ((Graphics) g).setArgb(argb);
            }

            public Object ext(Object g) {
                return ((Graphics) g).ext;
            }

            public void setExt(Object g, Object e) {
                ((Graphics) g).ext = e;
            }

            public int targetWidth(Object g) {
                return ((Graphics) g).tw;
            }

            public int targetHeight(Object g) {
                return ((Graphics) g).th;
            }

            public void getRgb(Object g, int[] out, int off, int scan, int x, int y, int w, int h) {
                Graphics gr = (Graphics) g;
                Image.getRGB0(gr.target, out, off, scan, x + gr.tx, y + gr.ty, w, h);
            }

            public Object newArgbImage(int w, int h, int argb) {
                return Image.newArgb(w, h, argb);
            }
        };
    }

    private final Image image;
    /** Free slot of the Nokia UI layer (DirectGraphics wrapper). */
    Object ext;
    /** Colour handed to the natives: 0x00RRGGBB with the HIGH BYTE = 255 - alpha (0 = opaque; Surface contract). */
    private int rgba;
    private final int target;
    private final int tw, th;
    private int color;
    private int tx, ty;
    /** Absolute clip box [cx0, cx1) x [cy0, cy1). */
    private int cx0, cy0, cx1, cy1;
    private int stroke = SOLID;
    private Font font = Font.getDefaultFont();

    Graphics(Image img, boolean screen) {
        image = img;
        target = img.peer;
        tw = img.getWidth();
        th = img.getHeight();
        cx1 = tw;
        cy1 = th;
        isScreen = screen;
    }

    private final boolean isScreen;

    /** Back to the state of a new Graphics (Canvas reuses its screen Graphics for every paint: no allocation per frame). */
    void reset() {
        color = 0;
        rgba = 0;
        tx = ty = 0;
        cx0 = cy0 = 0;
        cx1 = tw;
        cy1 = th;
        stroke = SOLID;
        font = Font.getDefaultFont();
    }

    public void translate(int x, int y) {
        tx += x;
        ty += y;
    }

    public int getTranslateX() {
        return tx;
    }

    public int getTranslateY() {
        return ty;
    }

    public int getColor() {
        return color;
    }

    public int getRedComponent() {
        return (color >> 16) & 0xFF;
    }

    public int getGreenComponent() {
        return (color >> 8) & 0xFF;
    }

    public int getBlueComponent() {
        return color & 0xFF;
    }

    public int getGrayScale() {
        return (306 * getRedComponent() + 601 * getGreenComponent() + 117 * getBlueComponent()) >> 10;
    }

    public void setColor(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) throw new IllegalArgumentException("component");
        color = (red << 16) | (green << 8) | blue;
        rgba = color;
    }

    public void setColor(int RGB) {
        color = RGB & 0xFFFFFF;
        rgba = color;
    }

    /** Nokia setARGBColor: the RGB part is the colour, the alpha applies to every primitive until the next setColor (UNVERIFIED, docs/nokia.md). */
    void setArgb(int argb) {
        color = argb & 0xFFFFFF;
        rgba = color | ((255 - (argb >>> 24)) << 24);
    }

    public void setGrayScale(int value) {
        if (value < 0 || value > 255) throw new IllegalArgumentException("value");
        color = (value << 16) | (value << 8) | value;
        rgba = color;
    }

    /** UNVERIFIED: full-colour displays return the colour unchanged (profile data arrives with T33). */
    public int getDisplayColor(int color) {
        return color & 0xFFFFFF;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font == null ? Font.getDefaultFont() : font;
    }

    public void setStrokeStyle(int style) {
        if (style != SOLID && style != DOTTED) throw new IllegalArgumentException("style");
        stroke = style;
    }

    public int getStrokeStyle() {
        return stroke;
    }

    public int getClipX() {
        return cx0 - tx;
    }

    public int getClipY() {
        return cy0 - ty;
    }

    public int getClipWidth() {
        return cx1 > cx0 ? cx1 - cx0 : 0;
    }

    public int getClipHeight() {
        return cy1 > cy0 ? cy1 - cy0 : 0;
    }

    public void setClip(int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            cx0 = cy0 = cx1 = cy1 = 0;
            return;
        }
        cx0 = x + tx;
        cy0 = y + ty;
        cx1 = cx0 + width;
        cy1 = cy0 + height;
    }

    public void clipRect(int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            cx0 = cy0 = cx1 = cy1 = 0;
            return;
        }
        int nx0 = x + tx, ny0 = y + ty, nx1 = nx0 + width, ny1 = ny0 + height;
        if (nx0 > cx0) cx0 = nx0;
        if (ny0 > cy0) cy0 = ny0;
        if (nx1 < cx1) cx1 = nx1;
        if (ny1 < cy1) cy1 = ny1;
        if (cx1 <= cx0 || cy1 <= cy0) cx0 = cy0 = cx1 = cy1 = 0;
    }

    private boolean empty() {
        return cx1 <= cx0 || cy1 <= cy0;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        if (empty()) return;
        drawLine0(target, rgba, cx0, cy0, cx1, cy1, x1 + tx, y1 + ty, x2 + tx, y2 + ty, stroke);
    }

    public void fillRect(int x, int y, int width, int height) {
        if (empty() || width <= 0 || height <= 0) return;
        fillRect0(target, rgba, cx0, cy0, cx1, cy1, x + tx, y + ty, width, height);
    }

    public void drawRect(int x, int y, int width, int height) {
        if (empty() || width < 0 || height < 0) return;
        drawRect0(target, rgba, cx0, cy0, cx1, cy1, x + tx, y + ty, width, height, stroke);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        if (empty() || width < 0 || height < 0) return;
        drawRoundRect0(target, rgba, cx0, cy0, cx1, cy1, x + tx, y + ty, width, height, arcWidth, arcHeight, stroke);
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        if (empty() || width <= 0 || height <= 0) return;
        fillRoundRect0(target, rgba, cx0, cy0, cx1, cy1, x + tx, y + ty, width, height, arcWidth, arcHeight);
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        if (empty() || width < 0 || height < 0) return;
        drawArc0(target, rgba, cx0, cy0, cx1, cy1, x + tx, y + ty, width, height, startAngle, arcAngle, stroke);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        if (empty() || width <= 0 || height <= 0) return;
        fillArc0(target, rgba, cx0, cy0, cx1, cy1, x + tx, y + ty, width, height, startAngle, arcAngle);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        if (empty()) return;
        fillTriangle0(target, rgba, cx0, cy0, cx1, cy1, x1 + tx, y1 + ty, x2 + tx, y2 + ty, x3 + tx, y3 + ty);
    }

    public void drawString(String str, int x, int y, int anchor) {
        textStr(str, 0, str.length(), x, y, anchor);
    }

    public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
        if (offset < 0 || len < 0 || offset > str.length() - len) throw new StringIndexOutOfBoundsException();
        textStr(str, offset, len, x, y, anchor);
    }

    private char[] one;

    public void drawChar(char character, int x, int y, int anchor) {
        char[] c = one;
        if (c == null) one = c = new char[1];
        c[0] = character;
        text(c, 0, 1, x, y, anchor);
    }

    public void drawChars(char[] data, int offset, int length, int x, int y, int anchor) {
        if (data == null) throw new NullPointerException();
        if (offset < 0 || length < 0 || offset > data.length - length) throw new ArrayIndexOutOfBoundsException();
        text(data, offset, length, x, y, anchor);
    }

    private void text(char[] c, int off, int len, int x, int y, int anchor) {
        int h = anchor & (LEFT | HCENTER | RIGHT), v = anchor & (TOP | BASELINE | BOTTOM | VCENTER);
        if ((h != LEFT && h != HCENTER && h != RIGHT) || (v != TOP && v != BASELINE && v != BOTTOM) || (anchor & ~(h | v)) != 0)
            throw new IllegalArgumentException("anchor");
        if (empty() || len == 0) return;
        int w = font.advanceOf(c, off, len);
        if (h == HCENTER) x -= w >> 1;
        else if (h == RIGHT) x -= w;
        if (v == BASELINE) y -= font.getBaselinePosition();
        else if (v == BOTTOM) y -= font.getHeight();
        drawChars0(target, rgba, cx0, cy0, cx1, cy1, font.key(), c, off, len, x + tx, y + ty);
    }

    /** Same as text() for a String: the host reads the characters itself, so nothing is allocated per call. */
    private void textStr(String str, int off, int len, int x, int y, int anchor) {
        int h = anchor & (LEFT | HCENTER | RIGHT), v = anchor & (TOP | BASELINE | BOTTOM | VCENTER);
        if ((h != LEFT && h != HCENTER && h != RIGHT) || (v != TOP && v != BASELINE && v != BOTTOM) || (anchor & ~(h | v)) != 0)
            throw new IllegalArgumentException("anchor");
        if (empty() || len == 0) return;
        int w = font.advanceOf(str, off, len);
        if (h == HCENTER) x -= w >> 1;
        else if (h == RIGHT) x -= w;
        if (v == BASELINE) y -= font.getBaselinePosition();
        else if (v == BOTTOM) y -= font.getHeight();
        drawStr0(target, rgba, cx0, cy0, cx1, cy1, font.key(), str, off, len, x + tx, y + ty);
    }

    public void drawImage(Image img, int x, int y, int anchor) {
        if (img == null) throw new NullPointerException();
        int w = img.getWidth(), h = img.getHeight();
        int px = placeX(x, w, anchor), py = placeY(y, h, anchor);
        if (empty()) return;
        drawRegion0(target, cx0, cy0, cx1, cy1, img.peer, 0, 0, w, h, 0, px + tx, py + ty);
    }

    public void drawRegion(Image src, int x_src, int y_src, int width, int height, int transform, int x_dest, int y_dest, int anchor) {
        if (src == null) throw new NullPointerException();
        if (transform < 0 || transform > 7) throw new IllegalArgumentException("transform");
        if (width < 0 || height < 0 || x_src < 0 || y_src < 0 || x_src + width > src.getWidth() || y_src + height > src.getHeight()
                || x_src + width < 0 || y_src + height < 0) throw new IllegalArgumentException("region");
        if (src == image) throw new IllegalArgumentException("source is the destination");
        boolean swap = transform >= 4;
        int px = placeX(x_dest, swap ? height : width, anchor), py = placeY(y_dest, swap ? width : height, anchor);
        if (empty() || width == 0 || height == 0) return;
        drawRegion0(target, cx0, cy0, cx1, cy1, src.peer, x_src, y_src, width, height, transform, px + tx, py + ty);
    }

    public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha) {
        if (rgbData == null) throw new NullPointerException();
        if (width == 0 || height == 0) return;
        if (width < 0 || height < 0) throw new IllegalArgumentException("size");
        int abs = scanlength < 0 ? -scanlength : scanlength;
        if (abs < width) throw new IllegalArgumentException("scanlength");
        long lo = offset + (scanlength < 0 ? (long) (height - 1) * scanlength : 0L);
        long hi = offset + (scanlength > 0 ? (long) (height - 1) * scanlength : 0L) + width - 1;
        if (lo < 0 || hi >= rgbData.length) throw new ArrayIndexOutOfBoundsException();
        if (empty()) return;
        drawRGB0(target, cx0, cy0, cx1, cy1, rgbData, offset, scanlength, x + tx, y + ty, width, height, processAlpha);
    }

    public void copyArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor) {
        if (isScreen) throw new IllegalStateException("copyArea needs an Image destination");
        if (width < 0 || height < 0 || x_src + tx < 0 || y_src + ty < 0 || x_src + tx + width > tw || y_src + ty + height > th)
            throw new IllegalArgumentException("region");
        int px = placeX(x_dest, width, anchor), py = placeY(y_dest, height, anchor);
        if (empty() || width == 0 || height == 0) return;
        copyArea0(target, cx0, cy0, cx1, cy1, x_src + tx, y_src + ty, width, height, px + tx, py + ty);
    }

    /** Anchor handling of images / copyArea (BASELINE is invalid there): x / y of the top-left corner. The checks are in placeX (called first). */
    private static int placeX(int x, int w, int anchor) {
        int hh = anchor & (LEFT | HCENTER | RIGHT), vv = anchor & (TOP | VCENTER | BOTTOM);
        if ((hh != LEFT && hh != HCENTER && hh != RIGHT) || (vv != TOP && vv != VCENTER && vv != BOTTOM) || (anchor & ~(hh | vv)) != 0)
            throw new IllegalArgumentException("anchor");
        return hh == HCENTER ? x - (w >> 1) : hh == RIGHT ? x - w : x;
    }

    private static int placeY(int y, int h, int anchor) {
        int vv = anchor & (TOP | VCENTER | BOTTOM);
        return vv == VCENTER ? y - (h >> 1) : vv == BOTTOM ? y - h : y;
    }

    private static native void drawLine0(int t, int rgb, int cx0, int cy0, int cx1, int cy1, int x1, int y1, int x2, int y2, int stroke);

    private static native void fillRect0(int t, int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h);

    private static native void drawRect0(int t, int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h, int stroke);

    private static native void drawRoundRect0(int t, int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h, int aw, int ah, int stroke);

    private static native void fillRoundRect0(int t, int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h, int aw, int ah);

    private static native void drawArc0(int t, int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h, int start, int arc, int stroke);

    private static native void fillArc0(int t, int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h, int start, int arc);

    private static native void fillTriangle0(int t, int rgb, int cx0, int cy0, int cx1, int cy1, int x1, int y1, int x2, int y2, int x3, int y3);

    private static native void drawStr0(int t, int rgb, int cx0, int cy0, int cx1, int cy1, int fontKey, String s, int off, int len, int x, int y);

    private static native void drawChars0(int t, int rgb, int cx0, int cy0, int cx1, int cy1, int fontKey, char[] c, int off, int len, int x, int y);

    private static native void drawRegion0(int t, int cx0, int cy0, int cx1, int cy1, int src, int sx, int sy, int sw, int sh, int transform, int dx, int dy);

    private static native void drawRGB0(int t, int cx0, int cy0, int cx1, int cy1, int[] rgb, int off, int scan, int x, int y, int w, int h, boolean alpha);

    private static native void copyArea0(int t, int cx0, int cy0, int cx1, int cy1, int sx, int sy, int w, int h, int dx, int dy);
}

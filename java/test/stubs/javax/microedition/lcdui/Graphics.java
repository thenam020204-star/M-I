package javax.microedition.lcdui;
/** Stub toi thieu: ve vao mang ARGB. */
public class Graphics {
    public static final int HCENTER = 1, VCENTER = 2, LEFT = 4, RIGHT = 8, TOP = 16, BOTTOM = 32, BASELINE = 64;
    public final int w, h; public final int[] buf; private int color;
    public Graphics(int w, int h) { this.w = w; this.h = h; buf = new int[w * h]; }
    public void setColor(int c) { color = c & 0xFFFFFF; }
    public int getColor() { return color; }
    public void fillRect(int x, int y, int ww, int hh) {
        for (int j = y; j < y + hh; j++) for (int i = x; i < x + ww; i++) put(i, j, 0xFF000000 | color);
    }
    private void put(int x, int y, int argb) {
        if (x >= 0 && y >= 0 && x < w && y < h && (argb >>> 24) != 0) buf[y * w + x] = argb;
    }
    public void drawRegion(Image src, int sx, int sy, int ww, int hh, int tr, int x, int y, int anchor) {
        for (int j = 0; j < hh; j++) for (int i = 0; i < ww; i++) put(x + i, y + j, src.px[(sy + j) * src.w + sx + i]);
    }
    public void drawRGB(int[] rgb, int off, int scan, int x, int y, int ww, int hh, boolean a) {
        for (int j = 0; j < hh; j++) for (int i = 0; i < ww; i++) put(x + i, y + j, rgb[off + j * scan + i]);
    }
}

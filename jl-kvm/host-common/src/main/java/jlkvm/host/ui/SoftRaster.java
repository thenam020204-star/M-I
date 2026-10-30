package jlkvm.host.ui;

import java.io.IOException;

/**
 * Pure-Java software {@link Surface} and {@link SurfaceFactory}: the lcdui reference raster (PLAN T23). Deterministic on every
 * JVM / ART (integer maths; trigonometry through StrictMath only), so desktop and Android produce the same pixels when both use it.
 * Pixels are 0xAARRGGBB in an int[]; a mutable image is opaque white. Written for jl-kvm (no code taken from other projects).
 * Rasterisation rules (see docs/lcdui-natives.md): lines Bresenham with both end points; fillRect covers [x, x+w) x [y, y+h);
 * drawRect covers x..x+w and y..y+h; ellipses sample pixel centres; DOTTED = every second pixel along the primitive.
 */
public final class SoftRaster implements Surface, SurfaceFactory {
    public final int w, h;
    public final int[] px;
    private final boolean alpha;
    private int bx0, by0, bx1, by1;   // current clip box in surface coordinates

    /** The factory instance. */
    public SoftRaster() { this(0, 0, null, false); }

    private SoftRaster(int w, int h, int[] px, boolean alpha) {
        this.w = w; this.h = h; this.px = px; this.alpha = alpha;
    }

    // ------------------------------------------------------------------ factory
    public Surface create(int w, int h) {
        int[] p = new int[w * h];
        java.util.Arrays.fill(p, 0xFFFFFFFF);
        return new SoftRaster(w, h, p, false);
    }

    public Surface createArgb(int w, int h, int argb) {
        int[] p = new int[w * h];
        java.util.Arrays.fill(p, argb);
        return new SoftRaster(w, h, p, true);
    }

    public Surface decode(byte[] data, int off, int len) throws IOException {
        PngDecoder d = PngDecoder.decode(data, off, len);
        return fromRgb(d.argb, d.width, d.height, d.hasAlpha);
    }

    public Surface fromRgb(int[] argb, int w, int h, boolean alpha) {
        int[] p = new int[w * h];
        for (int i = 0; i < p.length; i++) p[i] = alpha ? argb[i] : (argb[i] | 0xFF000000);
        return new SoftRaster(w, h, p, alpha);
    }

    public int fontMetrics(int key) { return BitmapFonts.metrics(key); }
    public int charWidth(int key, char c) { return BitmapFonts.advance(key, c); }
    public int charsWidth(int key, char[] c, int off, int len) { return BitmapFonts.advance(key, c, off, len); }

    // ------------------------------------------------------------------ Surface
    public int width() { return w; }
    public int height() { return h; }
    public boolean hasAlpha() { return alpha; }

    private void clip(int cx0, int cy0, int cx1, int cy1) {
        bx0 = Math.max(cx0, 0); by0 = Math.max(cy0, 0); bx1 = Math.min(cx1, w); by1 = Math.min(cy1, h);
    }

    /** Colour argument -> 0xAARRGGBB: the high byte of a colour argument is 255 - alpha (0 = opaque, so plain 0xRRGGBB stays opaque; Nokia setARGBColor, T28). */
    private static int argb(int rgb) { return ((255 - (rgb >>> 24)) << 24) | (rgb & 0xFFFFFF); }

    /** Stores a colour: opaque colours replace, translucent ones are blended with the pixel there. */
    private void put(int i, int c) { px[i] = (c >>> 24) == 255 ? c : blend(px[i], c); }

    private void plot(int x, int y, int c) {
        if (x >= bx0 && x < bx1 && y >= by0 && y < by1) put(y * w + x, c);
    }

    public void drawLine(int rgb, int cx0, int cy0, int cx1, int cy1, int x1, int y1, int x2, int y2, int stroke) {
        clip(cx0, cy0, cx1, cy1);
        if (bx1 <= bx0 || by1 <= by0) return;
        line(argb(rgb), x1, y1, x2, y2, stroke);
    }

    private void line(int c, int x1, int y1, int x2, int y2, int stroke) {
        // trivial reject: both end points beyond the same clip edge
        if ((x1 < bx0 && x2 < bx0) || (x1 >= bx1 && x2 >= bx1) || (y1 < by0 && y2 < by0) || (y1 >= by1 && y2 >= by1)) return;
        long dx = Math.abs((long) x2 - x1), dy = Math.abs((long) y2 - y1);
        int sx = x1 < x2 ? 1 : -1, sy = y1 < y2 ? 1 : -1;
        long err = dx - dy;
        for (int i = 0; ; i++) {
            if (stroke == 0 || (i & 1) == 0) plot(x1, y1, c);
            if (x1 == x2 && y1 == y2) break;
            long e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x1 += sx; }
            if (e2 < dx) { err += dx; y1 += sy; }
        }
    }

    public void fillRect(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int rw, int rh) {
        clip(cx0, cy0, cx1, cy1);
        long ex = (long) x + rw, ey = (long) y + rh;
        int x0 = Math.max(x, bx0), y0 = Math.max(y, by0), x1 = (int) Math.min(ex, bx1), y1 = (int) Math.min(ey, by1);
        int c = argb(rgb);
        if ((c >>> 24) == 255) for (int j = y0; j < y1; j++) java.util.Arrays.fill(px, j * w + x0, j * w + Math.max(x0, x1), c);
        else for (int j = y0; j < y1; j++) for (int i = x0; i < x1; i++) put(j * w + i, c);
    }

    public void drawRect(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int rw, int rh, int stroke) {
        clip(cx0, cy0, cx1, cy1);
        int c = argb(rgb);
        line(c, x, y, x + rw, y, stroke);
        if (rh > 0) line(c, x, y + rh, x + rw, y + rh, stroke);
        if (rh > 1) {
            line(c, x, y + 1, x, y + rh - 1, stroke);
            if (rw > 0) line(c, x + rw, y + 1, x + rw, y + rh - 1, stroke);
        }
    }

    private static int clampArc(int a, int max) { return a < 0 ? 0 : a > max ? max : a; }

    public void drawRoundRect(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int rw, int rh, int aw, int ah, int stroke) {
        aw = clampArc(aw, rw); ah = clampArc(ah, rh);
        if (aw == 0 || ah == 0) { drawRect(rgb, cx0, cy0, cx1, cy1, x, y, rw, rh, stroke); return; }
        clip(cx0, cy0, cx1, cy1);
        int c = argb(rgb), hw = aw / 2, hh = ah / 2;
        line(c, x + hw, y, x + rw - hw, y, stroke);
        line(c, x + hw, y + rh, x + rw - hw, y + rh, stroke);
        line(c, x, y + hh, x, y + rh - hh, stroke);
        line(c, x + rw, y + hh, x + rw, y + rh - hh, stroke);
        arcOutline(c, x, y, aw, ah, 90, 90);
        arcOutline(c, x + rw - aw, y, aw, ah, 0, 90);
        arcOutline(c, x, y + rh - ah, aw, ah, 180, 90);
        arcOutline(c, x + rw - aw, y + rh - ah, aw, ah, 270, 90);
    }

    public void fillRoundRect(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int rw, int rh, int aw, int ah) {
        aw = clampArc(aw, rw); ah = clampArc(ah, rh);
        if (aw == 0 || ah == 0) { fillRect(rgb, cx0, cy0, cx1, cy1, x, y, rw, rh); return; }
        clip(cx0, cy0, cx1, cy1);
        int c = argb(rgb);
        double rx = aw / 2.0, ry = ah / 2.0;
        for (int j = 0; j < rh; j++) {
            double yc = j + 0.5, inset = 0;
            if (yc < ry) { double t = (ry - yc) / ry; inset = rx - rx * StrictMath.sqrt(1 - t * t); }
            else if (yc > rh - ry) { double t = (yc - (rh - ry)) / ry; inset = rx - rx * StrictMath.sqrt(1 - t * t); }
            int left = (int) StrictMath.ceil(inset - 0.5);
            if (left < 0) left = 0;
            for (int i = left; i < rw - left; i++) plot(x + i, y + j, c);
        }
    }

    private void arcOutline(int c, int x, int y, int rw, int rh, int start, int arc) {
        double rx = rw / 2.0, ry = rh / 2.0, cx = x + rx, cy = y + ry;
        double a0, a1;
        if (arc >= 360 || arc <= -360) { a0 = 0; a1 = 2 * StrictMath.PI; }
        else { a0 = start * StrictMath.PI / 180.0; a1 = (start + arc) * StrictMath.PI / 180.0; }
        int n = (int) StrictMath.ceil(StrictMath.abs(a1 - a0) * (Math.max(rx, ry) + 1) * 2) + 1;
        for (int k = 0; k <= n; k++) {
            double t = a0 + (a1 - a0) * k / n;
            plot((int) StrictMath.floor(cx + rx * StrictMath.cos(t) + 0.5), (int) StrictMath.floor(cy - ry * StrictMath.sin(t) + 0.5), c);
        }
    }

    public void drawArc(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int rw, int rh, int start, int arc, int stroke) {
        clip(cx0, cy0, cx1, cy1);
        arcOutline(argb(rgb), x, y, rw, rh, start, arc);
    }

    public void fillArc(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int rw, int rh, int start, int arc) {
        clip(cx0, cy0, cx1, cy1);
        if (arc == 0) return;
        int c = argb(rgb);
        double rx = rw / 2.0, ry = rh / 2.0, cx = x + rx, cy = y + ry;
        boolean full = arc >= 360 || arc <= -360;
        double lo = arc > 0 ? start : start + arc, span = StrictMath.abs(arc);
        int py0 = Math.max(y, by0), py1 = Math.min(y + rh, by1), px0 = Math.max(x, bx0), px1 = Math.min(x + rw, bx1);
        for (int j = py0; j < py1; j++) {
            double v = (cy - (j + 0.5)) / ry;
            for (int i = px0; i < px1; i++) {
                double u = (i + 0.5 - cx) / rx;
                if (u * u + v * v > 1.0) continue;
                if (!full) {
                    double ang = StrictMath.atan2(v, u) * 180.0 / StrictMath.PI;
                    double d = (ang - lo) % 360.0;
                    if (d < 0) d += 360.0;
                    if (d > span) continue;
                }
                put(j * w + i, c);
            }
        }
    }

    public void fillTriangle(int rgb, int cx0, int cy0, int cx1, int cy1, int x1, int y1, int x2, int y2, int x3, int y3) {
        clip(cx0, cy0, cx1, cy1);
        int c = argb(rgb);
        long ax = 2L * x1, ay = 2L * y1, bx = 2L * x2, by = 2L * y2, cxx = 2L * x3, cyy = 2L * y3;
        long area = (bx - ax) * (cyy - ay) - (by - ay) * (cxx - ax);
        int minx = Math.max(Math.min(x1, Math.min(x2, x3)), bx0), maxx = Math.min(Math.max(x1, Math.max(x2, x3)), bx1 - 1);
        int miny = Math.max(Math.min(y1, Math.min(y2, y3)), by0), maxy = Math.min(Math.max(y1, Math.max(y2, y3)), by1 - 1);
        for (int j = miny; j <= maxy; j++) {
            long py = 2L * j + 1;
            for (int i = minx; i <= maxx; i++) {
                long pxx = 2L * i + 1;
                long e0 = (bx - ax) * (py - ay) - (by - ay) * (pxx - ax);
                long e1 = (cxx - bx) * (py - by) - (cyy - by) * (pxx - bx);
                long e2 = (ax - cxx) * (py - cyy) - (ay - cyy) * (pxx - cxx);
                boolean in = area >= 0 ? (e0 >= 0 && e1 >= 0 && e2 >= 0) : (e0 <= 0 && e1 <= 0 && e2 <= 0);
                if (in) put(j * w + i, c);
            }
        }
        // the three edges themselves (thin triangles would otherwise lose pixels)
        line(c, x1, y1, x2, y2, 0);
        line(c, x2, y2, x3, y3, 0);
        line(c, x3, y3, x1, y1, 0);
    }

    public void drawChars(int rgb, int cx0, int cy0, int cx1, int cy1, int key, char[] chars, int off, int len, int x, int y) {
        clip(cx0, cy0, cx1, cy1);
        BitmapFonts.Face f = BitmapFonts.face(key);
        boolean bold = BitmapFonts.bold(key), italic = BitmapFonts.italic(key), under = BitmapFonts.underlined(key);
        int c = argb(rgb), pen = x;
        for (int k = 0; k < len; k++) {
            char ch = chars[off + k];
            int adv = f.advance(ch);
            for (int j = 0; j < f.height; j++) {
                int shear = italic ? (f.baseline - j) >> 2 : 0;
                for (int i = 0; i < adv; i++) {
                    if (f.bit(ch, i, j)) {
                        plot(pen + i + shear, y + j, c);
                        if (bold) plot(pen + i + shear + 1, y + j, c);
                    }
                }
            }
            pen += adv + (bold ? 1 : 0);
        }
        if (under) for (int i = x; i < pen; i++) plot(i, y + f.baseline + 1, c);
    }

    /** Source-over; the destination may be translucent too (a mutable image created with an alpha channel, T28). */
    private static int blend(int dst, int src) {
        int a = src >>> 24;
        if (a == 255) return src;
        if (a == 0) return dst;
        int da = dst >>> 24;
        if (da == 255) {
            int ia = 255 - a;
            int r = (((src >> 16) & 0xFF) * a + ((dst >> 16) & 0xFF) * ia + 127) / 255;
            int g = (((src >> 8) & 0xFF) * a + ((dst >> 8) & 0xFF) * ia + 127) / 255;
            int b = ((src & 0xFF) * a + (dst & 0xFF) * ia + 127) / 255;
            return 0xFF000000 | (r << 16) | (g << 8) | b;
        }
        if (da == 0) return src;
        int wd = da * (255 - a);                  // weight of the destination, scaled by 255
        int ws = a * 255;                         // weight of the source, scaled by 255
        int tot = ws + wd;
        int oa = (tot + 127) / 255;
        int r = (((src >> 16) & 0xFF) * ws + ((dst >> 16) & 0xFF) * wd + tot / 2) / tot;
        int g = (((src >> 8) & 0xFF) * ws + ((dst >> 8) & 0xFF) * wd + tot / 2) / tot;
        int b = ((src & 0xFF) * ws + (dst & 0xFF) * wd + tot / 2) / tot;
        return (oa << 24) | (r << 16) | (g << 8) | b;
    }

    public void drawSurface(int cx0, int cy0, int cx1, int cy1, Surface srcS, int sx, int sy, int sw, int sh, int tr, int dx, int dy) {
        clip(cx0, cy0, cx1, cy1);
        SoftRaster src = (SoftRaster) srcS;
        if (src == this) src = new SoftRaster(w, h, px.clone(), alpha);   // drawing an image onto itself: read from a snapshot
        int dw = tr >= 4 ? sh : sw, dh = tr >= 4 ? sw : sh;
        int j0 = Math.max(0, by0 - dy), j1 = Math.min(dh, by1 - dy), i0 = Math.max(0, bx0 - dx), i1 = Math.min(dw, bx1 - dx);
        for (int j = j0; j < j1; j++) {
            for (int i = i0; i < i1; i++) {
                int u, v;
                switch (tr) {
                    case 0: u = i; v = j; break;
                    case 1: u = i; v = sh - 1 - j; break;
                    case 2: u = sw - 1 - i; v = j; break;
                    case 3: u = sw - 1 - i; v = sh - 1 - j; break;
                    case 4: u = j; v = i; break;
                    case 5: u = j; v = sh - 1 - i; break;
                    case 6: u = sw - 1 - j; v = i; break;
                    default: u = sw - 1 - j; v = sh - 1 - i; break;
                }
                int s = src.px[(sy + v) * src.w + sx + u];
                int di = (dy + j) * w + dx + i;
                px[di] = src.alpha ? blend(px[di], s) : s;
            }
        }
    }

    public void drawRgb(int cx0, int cy0, int cx1, int cy1, int[] rgb, int off, int scan, int x, int y, int rw, int rh, boolean alphaOn) {
        clip(cx0, cy0, cx1, cy1);
        int j0 = Math.max(0, by0 - y), j1 = (int) Math.min(rh, (long) by1 - y), i0 = Math.max(0, bx0 - x), i1 = (int) Math.min(rw, (long) bx1 - x);
        for (int j = j0; j < j1; j++) {
            for (int i = i0; i < i1; i++) {
                int s = rgb[off + j * scan + i];
                int di = (y + j) * w + x + i;
                px[di] = alphaOn ? blend(px[di], s) : (s | 0xFF000000);
            }
        }
    }

    public void copyArea(int cx0, int cy0, int cx1, int cy1, int sx, int sy, int rw, int rh, int dx, int dy) {
        clip(cx0, cy0, cx1, cy1);
        int[] tmp = new int[rw * rh];
        for (int j = 0; j < rh; j++) System.arraycopy(px, (sy + j) * w + sx, tmp, j * rw, rw);
        for (int j = 0; j < rh; j++) {
            int yy = dy + j;
            if (yy < by0 || yy >= by1) continue;
            for (int i = 0; i < rw; i++) {
                int xx = dx + i;
                if (xx >= bx0 && xx < bx1) px[yy * w + xx] = tmp[j * rw + i];
            }
        }
    }

    public void getRgb(int[] out, int off, int scan, int x, int y, int rw, int rh) {
        for (int j = 0; j < rh; j++) System.arraycopy(px, (y + j) * w + x, out, off + j * scan, rw);
    }

    public Surface copyRegion(int x, int y, int rw, int rh, int tr) {
        int dw = tr >= 4 ? rh : rw, dh = tr >= 4 ? rw : rh;
        int[] p = new int[dw * dh];
        for (int j = 0; j < dh; j++) {
            for (int i = 0; i < dw; i++) {
                int u, v;
                switch (tr) {
                    case 0: u = i; v = j; break;
                    case 1: u = i; v = rh - 1 - j; break;
                    case 2: u = rw - 1 - i; v = j; break;
                    case 3: u = rw - 1 - i; v = rh - 1 - j; break;
                    case 4: u = j; v = i; break;
                    case 5: u = j; v = rh - 1 - i; break;
                    case 6: u = rw - 1 - j; v = i; break;
                    default: u = rw - 1 - j; v = rh - 1 - i; break;
                }
                p[j * dw + i] = px[(y + v) * w + x + u];
            }
        }
        return new SoftRaster(dw, dh, p, alpha);
    }

    /** CRC-style hash of the pixels (tests, golden files): FNV-1a over the ARGB words, size included. */
    public long hash() {
        long hh = 0xcbf29ce484222325L ^ ((long) w << 32 | h);
        for (int v : px) { hh ^= v & 0xFFFFFFFFL; hh *= 0x100000001b3L; }
        return hh;
    }
}

/*
 * jl-kvm guest class library (Nokia UI layer). Origin: original code written for jl-kvm (PLAN D12, T28); behaviour per the Nokia UI API 1.1
 * javadoc of DirectGraphics (S40 DP2.0 SDK 1.1), open points UNVERIFIED (docs/nokia.md). License: Apache-2.0.
 */
package com.nokia.mid.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import jlkvm.internal.LcduiAccess;
import jlkvm.internal.nokia.Host;

/**
 * The DirectGraphics of one Graphics (same drawing context: colour, alpha, clip and translation are the Graphics'). Everything is done
 * with the public Graphics primitives, so the clip, the translation and alpha blending are the ones of Graphics; the scratch arrays are
 * kept between calls (no allocation in a steady paint loop).
 */
final class DirectGraphicsImp implements DirectGraphics {
    private static final int MIDP_NONE = 0, MIDP_MIRROR_ROT180 = 1, MIDP_MIRROR = 2, MIDP_ROT180 = 3, MIDP_MIRROR_ROT270 = 4, MIDP_ROT90 = 5,
            MIDP_ROT270 = 6, MIDP_MIRROR_ROT90 = 7;
    /** [flip: none, horizontal, vertical, both][rotation counter-clockwise 0, 90, 180, 270]: first the rotation, then the vertical flip, then the horizontal one (javadoc). */
    private static final int[][] TRANSFORM = {
        {MIDP_NONE, MIDP_ROT270, MIDP_ROT180, MIDP_ROT90},
        {MIDP_MIRROR, MIDP_MIRROR_ROT90, MIDP_MIRROR_ROT180, MIDP_MIRROR_ROT270},
        {MIDP_MIRROR_ROT180, MIDP_MIRROR_ROT270, MIDP_MIRROR, MIDP_MIRROR_ROT90},
        {MIDP_ROT180, MIDP_ROT90, MIDP_NONE, MIDP_ROT270}};

    private final Graphics g;
    private final LcduiAccess acc;
    private int[] buf = new int[0];
    private int[] buf2 = new int[0];
    private int[] cross = new int[16];

    DirectGraphicsImp(Graphics g, LcduiAccess acc) {
        this.g = g;
        this.acc = acc;
    }

    // ------------------------------------------------------------------ colour
    public void setARGBColor(int argbColor) {
        acc.setArgb(g, argbColor);
    }

    public int getAlphaComponent() {
        return acc.argb(g) >>> 24;
    }

    public int getNativePixelFormat() {
        return Host.config0(0);
    }

    // ------------------------------------------------------------------ manipulation
    /** Javadoc: FLIP_* ORed with ROTATE_90/180/270 (or 0); anything else is unknown. @return the MIDP transform (0..7) */
    private static int transform(int manipulation) {
        int flip = manipulation & (FLIP_HORIZONTAL | FLIP_VERTICAL);
        int rot = manipulation & ~(FLIP_HORIZONTAL | FLIP_VERTICAL);
        if (rot != 0 && rot != ROTATE_90 && rot != ROTATE_180 && rot != ROTATE_270) throw new IllegalArgumentException("manipulation " + manipulation);
        int f = (flip & FLIP_HORIZONTAL) != 0 ? ((flip & FLIP_VERTICAL) != 0 ? 3 : 1) : ((flip & FLIP_VERTICAL) != 0 ? 2 : 0);
        return TRANSFORM[f][rot / 90];
    }

    public void drawImage(Image img, int x, int y, int anchor, int manipulation) {
        if (img == null) throw new NullPointerException();
        int t = transform(manipulation);
        g.drawRegion(img, 0, 0, img.getWidth(), img.getHeight(), t, x, y, anchor);
    }

    // ------------------------------------------------------------------ pixels in
    private int[] scratch(int n) {
        if (buf.length < n) buf = new int[n];
        return buf;
    }

    /** Draws the w x h block in {@code buf} (0xAARRGGBB) with the manipulation applied, top-left at (x, y); subject to clip and translation. */
    private void blit(int w, int h, int t, int x, int y) {
        int[] src = buf;
        if (t == MIDP_NONE) {
            g.drawRGB(src, 0, w, x, y, w, h, true);
            return;
        }
        int dw = t >= 4 ? h : w, dh = t >= 4 ? w : h;
        if (buf2.length < dw * dh) buf2 = new int[dw * dh];
        int[] dst = buf2;
        for (int j = 0; j < dh; j++) {
            for (int i = 0; i < dw; i++) {
                int u, v;
                switch (t) {   // same mapping as lcdui Sprite / Graphics.drawRegion
                    case 1: u = i; v = h - 1 - j; break;
                    case 2: u = w - 1 - i; v = j; break;
                    case 3: u = w - 1 - i; v = h - 1 - j; break;
                    case 4: u = j; v = i; break;
                    case 5: u = j; v = h - 1 - i; break;
                    case 6: u = w - 1 - j; v = i; break;
                    default: u = w - 1 - j; v = h - 1 - i; break;
                }
                dst[j * dw + i] = src[v * w + u];
            }
        }
        g.drawRGB(dst, 0, dw, x, y, dw, dh, true);
    }

    public void drawPixels(int[] pixels, boolean transparency, int offset, int scanlength, int x, int y, int width, int height, int manipulation, int format) {
        if (pixels == null) throw new NullPointerException();
        if (width < 0 || height < 0) throw new IllegalArgumentException("size");
        if (format != TYPE_INT_888_RGB && format != TYPE_INT_8888_ARGB) throw new IllegalArgumentException("format " + format);
        int t = transform(manipulation);
        if (width == 0 || height == 0) return;
        range(offset, scanlength, width, height, pixels.length);
        int[] b = scratch(width * height);
        boolean alpha = transparency && format == TYPE_INT_8888_ARGB;
        for (int j = 0; j < height; j++) {
            int p = offset + j * scanlength;
            for (int i = 0; i < width; i++) {
                int v = pixels[p + i];
                b[j * width + i] = alpha ? v : (v | 0xFF000000);
            }
        }
        blit(width, height, t, x, y);
    }

    /** ArrayIndexOutOfBoundsException unless all indexes offset + j * scanlength + i lie in 0..length-1. */
    private static void range(int offset, int scanlength, int width, int height, int length) {
        long span = (long) (height - 1) * scanlength;
        long lo = offset + (span < 0 ? span : 0), hi = offset + (span > 0 ? span : 0) + width - 1;
        if (lo < 0 || hi >= length) throw new ArrayIndexOutOfBoundsException();
    }

    public void drawPixels(short[] pixels, boolean transparency, int offset, int scanlength, int x, int y, int width, int height, int manipulation, int format) {
        if (pixels == null) throw new NullPointerException();
        if (width < 0 || height < 0) throw new IllegalArgumentException("size");
        if (format != TYPE_USHORT_4444_ARGB && format != TYPE_USHORT_444_RGB && format != TYPE_USHORT_565_RGB && format != TYPE_USHORT_555_RGB
                && format != TYPE_USHORT_1555_ARGB) throw new IllegalArgumentException("format " + format);
        int t = transform(manipulation);
        if (width == 0 || height == 0) return;
        range(offset, scanlength, width, height, pixels.length);
        int[] b = scratch(width * height);
        for (int j = 0; j < height; j++) {
            int p = offset + j * scanlength;
            for (int i = 0; i < width; i++) {
                int c = fromShort(pixels[p + i] & 0xFFFF, format);
                b[j * width + i] = transparency ? c : (c | 0xFF000000);
            }
        }
        blit(width, height, t, x, y);
    }

    /** 0xAARRGGBB of a 16 bit pixel; the missing low bits repeat the high ones (255 stays 255). */
    private static int fromShort(int s, int format) {
        int a = 255, r, gg, b;
        switch (format) {
            case TYPE_USHORT_4444_ARGB:
                a = ((s >> 12) & 15) * 17; r = ((s >> 8) & 15) * 17; gg = ((s >> 4) & 15) * 17; b = (s & 15) * 17; break;
            case TYPE_USHORT_444_RGB:
                r = ((s >> 8) & 15) * 17; gg = ((s >> 4) & 15) * 17; b = (s & 15) * 17; break;
            case TYPE_USHORT_565_RGB:
                r = (s >> 11) & 31; gg = (s >> 5) & 63; b = s & 31;
                r = (r << 3) | (r >> 2); gg = (gg << 2) | (gg >> 4); b = (b << 3) | (b >> 2); break;
            case TYPE_USHORT_555_RGB:
                r = (s >> 10) & 31; gg = (s >> 5) & 31; b = s & 31;
                r = (r << 3) | (r >> 2); gg = (gg << 3) | (gg >> 2); b = (b << 3) | (b >> 2); break;
            default:   // 1555
                a = (s & 0x8000) != 0 ? 255 : 0;
                r = (s >> 10) & 31; gg = (s >> 5) & 31; b = s & 31;
                r = (r << 3) | (r >> 2); gg = (gg << 3) | (gg >> 2); b = (b << 3) | (b >> 2); break;
        }
        return (a << 24) | (r << 16) | (gg << 8) | b;
    }

    private static int toShort(int argb, int format) {
        int a = argb >>> 24, r = (argb >> 16) & 255, gg = (argb >> 8) & 255, b = argb & 255;
        switch (format) {
            case TYPE_USHORT_4444_ARGB: return ((a >> 4) << 12) | ((r >> 4) << 8) | ((gg >> 4) << 4) | (b >> 4);
            case TYPE_USHORT_444_RGB: return ((r >> 4) << 8) | ((gg >> 4) << 4) | (b >> 4);
            case TYPE_USHORT_565_RGB: return ((r >> 3) << 11) | ((gg >> 2) << 5) | (b >> 3);
            case TYPE_USHORT_555_RGB: return ((r >> 3) << 10) | ((gg >> 3) << 5) | (b >> 3);
            default: return (a != 0 ? 0x8000 : 0) | ((r >> 3) << 10) | ((gg >> 3) << 5) | (b >> 3);
        }
    }

    // ------------------------------------------------------------------ byte formats
    private static int perByte(int format) {
        switch (format) {
            case TYPE_BYTE_1_GRAY: case TYPE_BYTE_1_GRAY_VERTICAL: return 8;
            case TYPE_BYTE_2_GRAY: return 4;
            case TYPE_BYTE_4_GRAY: return 2;
            case TYPE_BYTE_8_GRAY: case TYPE_BYTE_332_RGB: return 1;
            default: throw new IllegalArgumentException("format " + format);
        }
    }

    private static int bitsOf(int format) {
        return format == TYPE_BYTE_1_GRAY || format == TYPE_BYTE_1_GRAY_VERTICAL ? 1 : format == TYPE_BYTE_332_RGB ? 8 : format;
    }

    /** Value of pixel number {@code p} (horizontal formats: pixel 0 in the most significant bits of byte 0). */
    private static int get(byte[] a, int format, int p) {
        int bits = bitsOf(format), per = 8 / bits;
        int v = a[p / per] & 0xFF;
        if (bits == 8) return v;
        int shift = 8 - bits * (p % per + 1);
        return (v >> shift) & ((1 << bits) - 1);
    }

    private static void put(byte[] a, int format, int p, int val) {
        int bits = bitsOf(format), per = 8 / bits;
        if (bits == 8) {
            a[p] = (byte) val;
            return;
        }
        int shift = 8 - bits * (p % per + 1), mask = ((1 << bits) - 1) << shift;
        a[p / per] = (byte) ((a[p / per] & ~mask) | ((val << shift) & mask));
    }

    /** Gray formats: 0 is white (the "ink" amount, 1 = a set pixel is black); UNVERIFIED, same as both reference emulators. */
    private static int colorOf(int format, int v) {
        int gray;
        switch (format) {
            case TYPE_BYTE_1_GRAY: case TYPE_BYTE_1_GRAY_VERTICAL: gray = v != 0 ? 0 : 255; break;
            case TYPE_BYTE_2_GRAY: gray = 255 - v * 85; break;
            case TYPE_BYTE_4_GRAY: gray = 255 - v * 17; break;
            case TYPE_BYTE_8_GRAY: gray = 255 - v; break;
            default: return (((v >> 5) * 255 / 7) << 16) | ((((v >> 2) & 7) * 255 / 7) << 8) | ((v & 3) * 85);
        }
        return (gray << 16) | (gray << 8) | gray;
    }

    private static int maskAlpha(int format, int m) {
        switch (format) {
            case TYPE_BYTE_1_GRAY: case TYPE_BYTE_1_GRAY_VERTICAL: return m != 0 ? 255 : 0;
            case TYPE_BYTE_2_GRAY: return m * 85;
            case TYPE_BYTE_4_GRAY: return m * 17;
            default: return m;
        }
    }

    private static int valueOf(int format, int argb) {
        int r = (argb >> 16) & 255, gg = (argb >> 8) & 255, b = argb & 255;
        if (format == TYPE_BYTE_332_RGB) return ((r >> 5) << 5) | ((gg >> 5) << 2) | (b >> 6);
        int ink = 255 - ((r * 77 + gg * 151 + b * 28) >> 8);
        switch (format) {
            case TYPE_BYTE_1_GRAY: case TYPE_BYTE_1_GRAY_VERTICAL: return ink >= 128 ? 1 : 0;
            case TYPE_BYTE_2_GRAY: return ink >> 6;
            case TYPE_BYTE_4_GRAY: return ink >> 4;
            default: return ink;
        }
    }

    private static int maskValue(int format, int a) {
        switch (format) {
            case TYPE_BYTE_1_GRAY: case TYPE_BYTE_1_GRAY_VERTICAL: return a != 0 ? 1 : 0;
            case TYPE_BYTE_2_GRAY: return (a * 3 + 127) / 255;
            case TYPE_BYTE_4_GRAY: return (a * 15 + 127) / 255;
            default: return a;
        }
    }

    /** Validates the array bounds of a byte-format transfer. */
    private static void byteRange(byte[] a, int format, int offset, int scanlength, int width, int height) {
        if (offset < 0) throw new ArrayIndexOutOfBoundsException();
        if (format == TYPE_BYTE_1_GRAY_VERTICAL) {
            if (scanlength <= 0) throw new IllegalArgumentException("scanlength");
            long row = offset / scanlength, col = offset % scanlength;
            long last = ((row + height - 1) / 8) * scanlength + col + width - 1;
            if (last >= a.length) throw new ArrayIndexOutOfBoundsException();
        } else {
            long hi = (long) offset + (long) (height - 1) * scanlength;
            long lo = scanlength < 0 ? hi : offset;
            long end = (scanlength < 0 ? offset : hi) + width - 1;
            if (lo < 0 || end / perByte(format) >= a.length) throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void drawPixels(byte[] pixels, byte[] transparencyMask, int offset, int scanlength, int x, int y, int width, int height, int manipulation, int format) {
        if (pixels == null) throw new NullPointerException();
        if (width < 0 || height < 0) throw new IllegalArgumentException("size");
        perByte(format);
        int t = transform(manipulation);
        if (width == 0 || height == 0) return;
        byteRange(pixels, format, offset, scanlength, width, height);
        if (transparencyMask != null) byteRange(transparencyMask, format, offset, scanlength, width, height);
        int[] b = scratch(width * height);
        boolean vertical = format == TYPE_BYTE_1_GRAY_VERTICAL;
        int oy = vertical ? offset / scanlength : 0, ox = vertical ? offset % scanlength : 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int v, m;
                if (vertical) {
                    int idx = ((oy + j) >> 3) * scanlength + ox + i, bit = (oy + j) & 7;
                    v = (pixels[idx] >> bit) & 1;
                    m = transparencyMask == null ? 1 : (transparencyMask[idx] >> bit) & 1;
                } else {
                    int p = offset + j * scanlength + i;
                    v = get(pixels, format, p);
                    m = transparencyMask == null ? -1 : get(transparencyMask, format, p);
                }
                int a = m < 0 ? 255 : maskAlpha(format, m);
                b[j * width + i] = (a << 24) | colorOf(format, v);
            }
        }
        blit(width, height, t, x, y);
    }

    // ------------------------------------------------------------------ pixels out
    /** Reads the region into buf; the part outside the surface is not read. @return {x0, y0, x1, y1} visible part in region coordinates, or null when empty */
    private int[] read(int x, int y, int w, int h) {
        int tw = acc.targetWidth(g), th = acc.targetHeight(g);
        int tx = g.getTranslateX(), ty = g.getTranslateY();
        int i0 = x + tx < 0 ? -(x + tx) : 0, j0 = y + ty < 0 ? -(y + ty) : 0;
        int i1 = (long) x + tx + w > tw ? tw - x - tx : w, j1 = (long) y + ty + h > th ? th - y - ty : h;
        if (i1 <= i0 || j1 <= j0) return null;
        int[] b = scratch(w * h);
        acc.getRgb(g, b, 0, w, x + i0, y + j0, i1 - i0, j1 - j0);   // rows of (i1 - i0) pixels at stride w, from the visible corner
        return new int[] {i0, j0, i1, j1};
    }

    private static void checkGet(Object pixels, int x, int y, int width, int height) {
        if (pixels == null) throw new NullPointerException();
        if (x < 0 || y < 0 || width < 0 || height < 0) throw new IllegalArgumentException("region");
    }

    public void getPixels(int[] pixels, int offset, int scanlength, int x, int y, int width, int height, int format) {
        checkGet(pixels, x, y, width, height);
        if (format != TYPE_INT_888_RGB && format != TYPE_INT_8888_ARGB) throw new IllegalArgumentException("format " + format);
        if (width == 0 || height == 0) return;
        range(offset, scanlength, width, height, pixels.length);
        int[] v = read(x, y, width, height);
        if (v == null) return;
        int[] b = buf;
        for (int j = v[1]; j < v[3]; j++) {
            for (int i = v[0]; i < v[2]; i++) {
                int c = b[(j - v[1]) * width + (i - v[0])];
                pixels[offset + j * scanlength + i] = format == TYPE_INT_888_RGB ? c & 0xFFFFFF : c;
            }
        }
    }

    public void getPixels(short[] pixels, int offset, int scanlength, int x, int y, int width, int height, int format) {
        checkGet(pixels, x, y, width, height);
        if (format != TYPE_USHORT_4444_ARGB && format != TYPE_USHORT_444_RGB && format != TYPE_USHORT_565_RGB && format != TYPE_USHORT_555_RGB
                && format != TYPE_USHORT_1555_ARGB) throw new IllegalArgumentException("format " + format);
        if (width == 0 || height == 0) return;
        range(offset, scanlength, width, height, pixels.length);
        int[] v = read(x, y, width, height);
        if (v == null) return;
        int[] b = buf;
        for (int j = v[1]; j < v[3]; j++) {
            for (int i = v[0]; i < v[2]; i++) {
                pixels[offset + j * scanlength + i] = (short) toShort(b[(j - v[1]) * width + (i - v[0])], format);
            }
        }
    }

    public void getPixels(byte[] pixels, byte[] transparencyMask, int offset, int scanlength, int x, int y, int width, int height, int format) {
        checkGet(pixels, x, y, width, height);
        perByte(format);
        if (width == 0 || height == 0) return;
        byteRange(pixels, format, offset, scanlength, width, height);
        if (transparencyMask != null) byteRange(transparencyMask, format, offset, scanlength, width, height);
        int[] v = read(x, y, width, height);
        if (v == null) return;
        int[] b = buf;
        boolean vertical = format == TYPE_BYTE_1_GRAY_VERTICAL;
        int oy = vertical ? offset / scanlength : 0, ox = vertical ? offset % scanlength : 0;
        for (int j = v[1]; j < v[3]; j++) {
            for (int i = v[0]; i < v[2]; i++) {
                int c = b[(j - v[1]) * width + (i - v[0])];
                int val = valueOf(format, c), m = maskValue(format, c >>> 24);
                if (vertical) {
                    int idx = ((oy + j) >> 3) * scanlength + ox + i, bit = (oy + j) & 7, bm = 1 << bit;
                    pixels[idx] = (byte) (val != 0 ? pixels[idx] | bm : pixels[idx] & ~bm);
                    if (transparencyMask != null) transparencyMask[idx] = (byte) (m != 0 ? transparencyMask[idx] | bm : transparencyMask[idx] & ~bm);
                } else {
                    int p = offset + j * scanlength + i;
                    put(pixels, format, p, val);
                    if (transparencyMask != null) put(transparencyMask, format, p, m);
                }
            }
        }
    }

    // ------------------------------------------------------------------ polygons (colour = parameter, the current colour is kept)
    private static void checkPoly(int[] xs, int xo, int[] ys, int yo, int n) {
        if (xs == null || ys == null) throw new NullPointerException();
        if (n < 0 || xo < 0 || yo < 0 || (long) xo + n > xs.length || (long) yo + n > ys.length) throw new ArrayIndexOutOfBoundsException();
    }

    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argbColor) {
        int save = acc.argb(g);
        acc.setArgb(g, argbColor);
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2, y2, x3, y3);
        g.drawLine(x3, y3, x1, y1);
        acc.setArgb(g, save);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int argbColor) {
        int save = acc.argb(g);
        acc.setArgb(g, argbColor);
        g.fillTriangle(x1, y1, x2, y2, x3, y3);
        acc.setArgb(g, save);
    }

    public void drawPolygon(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints, int argbColor) {
        checkPoly(xPoints, xOffset, yPoints, yOffset, nPoints);
        if (nPoints < 2) return;
        int save = acc.argb(g);
        acc.setArgb(g, argbColor);
        for (int k = 0; k < nPoints; k++) {
            int n = k + 1 == nPoints ? 0 : k + 1;
            g.drawLine(xPoints[xOffset + k], yPoints[yOffset + k], xPoints[xOffset + n], yPoints[yOffset + n]);
        }
        acc.setArgb(g, save);
    }

    /** Even-odd scan conversion: the pixel row y is covered from the left crossing (inclusive) to the right one (exclusive). */
    public void fillPolygon(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints, int argbColor) {
        checkPoly(xPoints, xOffset, yPoints, yOffset, nPoints);
        if (nPoints < 3) return;
        int minY = yPoints[yOffset], maxY = minY;
        for (int k = 1; k < nPoints; k++) {
            int yy = yPoints[yOffset + k];
            if (yy < minY) minY = yy;
            if (yy > maxY) maxY = yy;
        }
        int cy = g.getClipY(), ch = g.getClipHeight();
        if (minY < cy) minY = cy;
        if ((long) maxY > (long) cy + ch) maxY = cy + ch;
        if (minY >= maxY) return;
        if (cross.length < nPoints) cross = new int[nPoints];
        int save = acc.argb(g);
        acc.setArgb(g, argbColor);
        for (int y = minY; y < maxY; y++) {
            int n = 0;
            for (int k = 0; k < nPoints; k++) {
                int e = k + 1 == nPoints ? 0 : k + 1;
                int ya = yPoints[yOffset + k], yb = yPoints[yOffset + e];
                if ((ya <= y && yb > y) || (yb <= y && ya > y)) {
                    int xa = xPoints[xOffset + k], xb = xPoints[xOffset + e];
                    int xc = xa + (int) ((long) (y - ya) * (xb - xa) / (yb - ya));
                    int p = n++;
                    while (p > 0 && cross[p - 1] > xc) {
                        cross[p] = cross[p - 1];
                        p--;
                    }
                    cross[p] = xc;
                }
            }
            for (int k = 0; k + 1 < n; k += 2) {
                if (cross[k + 1] > cross[k]) g.fillRect(cross[k], y, cross[k + 1] - cross[k], 1);
            }
        }
        acc.setArgb(g, save);
    }
}

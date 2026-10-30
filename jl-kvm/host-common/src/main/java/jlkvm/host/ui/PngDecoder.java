package jlkvm.host.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * PNG decoder (the one image format MIDP 2.0 requires): all colour types and bit depths, palette + tRNS, Adam7 interlace.
 * Written for jl-kvm. Decoding happens in the host (PLAN T23). CRCs are not checked (devices are lenient too); ancillary chunks
 * are skipped. Result is non-premultiplied 0xAARRGGBB.
 */
public final class PngDecoder {
    public final int width, height;
    public final int[] argb;
    public final boolean hasAlpha;

    private PngDecoder(int w, int h, int[] px, boolean alpha) { width = w; height = h; argb = px; hasAlpha = alpha; }

    public static boolean isPng(byte[] d, int off, int len) {
        return len >= 8 && (d[off] & 0xFF) == 0x89 && d[off + 1] == 'P' && d[off + 2] == 'N' && d[off + 3] == 'G' && d[off + 4] == 0x0D && d[off + 5] == 0x0A && d[off + 6] == 0x1A && d[off + 7] == 0x0A;
    }

    public static PngDecoder decode(byte[] d, int off, int len) throws IOException {
        if (!isPng(d, off, len)) throw new IOException("not a PNG");
        int end = off + len, p = off + 8;
        int w = 0, h = 0, depth = 0, ctype = 0, interlace = 0;
        int[] palette = null;
        int[] trnsPal = null;
        int trnsKey = -1;   // tRNS of grey / truecolour: the transparent sample value(s)
        int trnsR = -1, trnsG = -1, trnsB = -1;
        ByteArrayOutputStream idat = new ByteArrayOutputStream();
        boolean sawIhdr = false, sawEnd = false;
        while (p + 8 <= end && !sawEnd) {
            int clen = be32(d, p);
            String type = new String(d, p + 4, 4, "ISO-8859-1");
            int body = p + 8;
            if (clen < 0 || body + clen > end) throw new IOException("truncated chunk " + type);
            if (type.equals("IHDR")) {
                if (clen < 13) throw new IOException("bad IHDR");
                w = be32(d, body); h = be32(d, body + 4);
                depth = d[body + 8] & 0xFF; ctype = d[body + 9] & 0xFF; interlace = d[body + 12] & 0xFF;
                sawIhdr = true;
                if (w <= 0 || h <= 0 || (long) w * h > (1 << 26)) throw new IOException("bad size " + w + "x" + h);
                if (!validDepth(ctype, depth)) throw new IOException("bad colour type / depth");
            } else if (type.equals("PLTE")) {
                int n = clen / 3;
                palette = new int[n];
                for (int i = 0; i < n; i++) palette[i] = 0xFF000000 | ((d[body + 3 * i] & 0xFF) << 16) | ((d[body + 3 * i + 1] & 0xFF) << 8) | (d[body + 3 * i + 2] & 0xFF);
            } else if (type.equals("tRNS")) {
                if (ctype == 3) {
                    trnsPal = new int[clen];
                    for (int i = 0; i < clen; i++) trnsPal[i] = d[body + i] & 0xFF;
                } else if (ctype == 0 && clen >= 2) {
                    trnsKey = ((d[body] & 0xFF) << 8) | (d[body + 1] & 0xFF);
                } else if (ctype == 2 && clen >= 6) {
                    trnsR = ((d[body] & 0xFF) << 8) | (d[body + 1] & 0xFF);
                    trnsG = ((d[body + 2] & 0xFF) << 8) | (d[body + 3] & 0xFF);
                    trnsB = ((d[body + 4] & 0xFF) << 8) | (d[body + 5] & 0xFF);
                }
            } else if (type.equals("IDAT")) {
                idat.write(d, body, clen);
            } else if (type.equals("IEND")) sawEnd = true;
            p = body + clen + 4;
        }
        if (!sawIhdr) throw new IOException("no IHDR");
        if (ctype == 3 && palette == null) throw new IOException("no PLTE");
        int bpp = bitsPerPixel(ctype, depth);
        int rawLen = 0;
        int[][] passes = interlace == 1 ? ADAM7 : new int[][] {{0, 0, 1, 1}};
        for (int[] ps : passes) {
            int pw = (w - ps[0] + ps[2] - 1) / ps[2], ph = (h - ps[1] + ps[3] - 1) / ps[3];
            if (pw > 0 && ph > 0) rawLen += ph * (1 + (pw * bpp + 7) / 8);
        }
        byte[] raw = new byte[rawLen];
        Inflater inf = new Inflater();
        try {
            inf.setInput(idat.toByteArray());
            int n = 0;
            while (n < rawLen) {
                int r = inf.inflate(raw, n, rawLen - n);
                if (r == 0 && (inf.finished() || inf.needsInput() || inf.needsDictionary())) break;
                n += r;
            }
            if (n < rawLen) throw new IOException("image data truncated");
        } catch (DataFormatException e) {
            throw new IOException("bad zlib data: " + e.getMessage());
        } finally {
            inf.end();
        }
        int[] out = new int[w * h];
        boolean alpha = ctype == 4 || ctype == 6 || trnsPal != null || trnsKey >= 0 || trnsR >= 0;
        int pos = 0;
        int bytesPP = Math.max(1, bpp / 8);
        for (int[] ps : passes) {
            int pw = (w - ps[0] + ps[2] - 1) / ps[2], ph = (h - ps[1] + ps[3] - 1) / ps[3];
            if (pw <= 0 || ph <= 0) continue;
            int stride = (pw * bpp + 7) / 8;
            byte[] prev = new byte[stride], cur = new byte[stride];
            for (int y = 0; y < ph; y++) {
                int f = raw[pos++] & 0xFF;
                System.arraycopy(raw, pos, cur, 0, stride);
                pos += stride;
                unfilter(f, cur, prev, bytesPP);
                for (int x = 0; x < pw; x++) {
                    int px = pixel(cur, x, ctype, depth, palette, trnsPal, trnsKey, trnsR, trnsG, trnsB);
                    out[(ps[1] + y * ps[3]) * w + ps[0] + x * ps[2]] = px;
                }
                byte[] t = prev; prev = cur; cur = t;
            }
        }
        return new PngDecoder(w, h, out, alpha);
    }

    private static final int[][] ADAM7 = {{0, 0, 8, 8}, {4, 0, 8, 8}, {0, 4, 4, 8}, {2, 0, 4, 4}, {0, 2, 2, 4}, {1, 0, 2, 2}, {0, 1, 1, 2}};

    private static boolean validDepth(int ctype, int depth) {
        switch (ctype) {
            case 0: return depth == 1 || depth == 2 || depth == 4 || depth == 8 || depth == 16;
            case 2: case 4: case 6: return depth == 8 || depth == 16;
            case 3: return depth == 1 || depth == 2 || depth == 4 || depth == 8;
            default: return false;
        }
    }

    private static int bitsPerPixel(int ctype, int depth) {
        switch (ctype) {
            case 2: return 3 * depth;
            case 4: return 2 * depth;
            case 6: return 4 * depth;
            default: return depth;
        }
    }

    private static void unfilter(int f, byte[] cur, byte[] prev, int bpp) {
        int n = cur.length;
        switch (f) {
            case 0: break;
            case 1: for (int i = bpp; i < n; i++) cur[i] = (byte) (cur[i] + cur[i - bpp]); break;
            case 2: for (int i = 0; i < n; i++) cur[i] = (byte) (cur[i] + prev[i]); break;
            case 3:
                for (int i = 0; i < n; i++) {
                    int a = i >= bpp ? cur[i - bpp] & 0xFF : 0, b = prev[i] & 0xFF;
                    cur[i] = (byte) (cur[i] + ((a + b) >> 1));
                }
                break;
            case 4:
                for (int i = 0; i < n; i++) {
                    int a = i >= bpp ? cur[i - bpp] & 0xFF : 0, b = prev[i] & 0xFF, c = i >= bpp ? prev[i - bpp] & 0xFF : 0;
                    int pa = Math.abs(b - c), pb = Math.abs(a - c), pc = Math.abs(a + b - 2 * c);
                    int pr = pa <= pb && pa <= pc ? a : pb <= pc ? b : c;
                    cur[i] = (byte) (cur[i] + pr);
                }
                break;
            default: throw new IllegalArgumentException("filter " + f);
        }
    }

    private static int sample(byte[] row, int idx, int depth) {
        switch (depth) {
            case 8: return row[idx] & 0xFF;
            case 16: return ((row[2 * idx] & 0xFF) << 8) | (row[2 * idx + 1] & 0xFF);
            default:
                int bit = idx * depth;
                int v = row[bit >> 3] & 0xFF;
                return (v >> (8 - depth - (bit & 7))) & ((1 << depth) - 1);
        }
    }

    private static int pixel(byte[] row, int x, int ctype, int depth, int[] pal, int[] trnsPal, int trnsKey, int trR, int trG, int trB) {
        switch (ctype) {
            case 0: {
                int s = sample(row, x, depth);
                int g = depth == 16 ? s >> 8 : depth == 8 ? s : s * 255 / ((1 << depth) - 1);
                int a = s == trnsKey ? 0 : 255;
                return (a << 24) | (g << 16) | (g << 8) | g;
            }
            case 2: {
                int r = sample(row, 3 * x, depth), g = sample(row, 3 * x + 1, depth), b = sample(row, 3 * x + 2, depth);
                int a = (r == trR && g == trG && b == trB) ? 0 : 255;
                if (depth == 16) { r >>= 8; g >>= 8; b >>= 8; }
                return (a << 24) | (r << 16) | (g << 8) | b;
            }
            case 3: {
                int i = sample(row, x, depth);
                if (i >= pal.length) return 0xFF000000;
                int c = pal[i];
                if (trnsPal != null && i < trnsPal.length) c = (c & 0xFFFFFF) | (trnsPal[i] << 24);
                return c;
            }
            case 4: {
                int g = sample(row, 2 * x, depth), a = sample(row, 2 * x + 1, depth);
                if (depth == 16) { g >>= 8; a >>= 8; }
                return (a << 24) | (g << 16) | (g << 8) | g;
            }
            default: {
                int r = sample(row, 4 * x, depth), g = sample(row, 4 * x + 1, depth), b = sample(row, 4 * x + 2, depth), a = sample(row, 4 * x + 3, depth);
                if (depth == 16) { r >>= 8; g >>= 8; b >>= 8; a >>= 8; }
                return (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
    }

    private static int be32(byte[] d, int p) {
        return ((d[p] & 0xFF) << 24) | ((d[p + 1] & 0xFF) << 16) | ((d[p + 2] & 0xFF) << 8) | (d[p + 3] & 0xFF);
    }
}

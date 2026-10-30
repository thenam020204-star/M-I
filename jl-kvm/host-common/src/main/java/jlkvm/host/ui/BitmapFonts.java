package jlkvm.host.ui;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The baked 1-bit fonts (resource jlkvm/fonts.bin, see tools/fontgen/BakeFont.java): pixel-identical on every host because no
 * platform font rasteriser is involved. Faces: FACE_SYSTEM and FACE_PROPORTIONAL use DejaVu Sans, FACE_MONOSPACE DejaVu Sans Mono;
 * sizes small / medium / large are 10 / 13 / 17 px em. Bold = the glyph drawn twice one pixel apart (+1 advance), italic = shear,
 * underline = a line under the baseline. UNVERIFIED against Nokia fonts (profile data in T33).
 */
public final class BitmapFonts {
    public static final class Face {
        public final int height, baseline;
        final int[] codes;
        final int[] offsets;   // into data: [advance][rows...]
        final byte[] data;
        Face(int h, int b, int[] codes, int[] offsets, byte[] data) { height = h; baseline = b; this.codes = codes; this.offsets = offsets; this.data = data; }

        int index(char c) {
            int lo = 0, hi = codes.length - 1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (codes[mid] < c) lo = mid + 1; else if (codes[mid] > c) hi = mid - 1; else return mid;
            }
            return -1;
        }
        int glyph(char c) {
            int i = index(c);
            if (i < 0) i = index('?');
            return i;
        }
        public int advance(char c) { int i = glyph(c); return i < 0 ? 0 : data[offsets[i]] & 0xFF; }
        /** true when pixel (x, y) of the glyph cell is set. */
        public boolean bit(char c, int x, int y) {
            int i = glyph(c);
            if (i < 0) return false;
            int adv = data[offsets[i]] & 0xFF, rowBytes = (adv + 7) / 8;
            if (x < 0 || x >= adv || y < 0 || y >= height) return false;
            return (data[offsets[i] + 1 + y * rowBytes + (x >> 3)] & (0x80 >> (x & 7))) != 0;
        }
    }

    private static Face[] faces;   // [face * 3 + size]

    private static synchronized Face[] load() {
        if (faces != null) return faces;
        Face[] f = new Face[6];
        try (InputStream in = BitmapFonts.class.getResourceAsStream("/jlkvm/fonts.bin")) {
            if (in == null) throw new IOException("resource /jlkvm/fonts.bin missing");
            DataInputStream d = new DataInputStream(new java.io.BufferedInputStream(in));
            byte[] magic = new byte[4];
            d.readFully(magic);
            if (magic[0] != 'J' || magic[1] != 'L' || magic[2] != 'F' || magic[3] != '1') throw new IOException("bad font file");
            int n = d.readUnsignedByte();
            for (int k = 0; k < n; k++) {
                int face = d.readUnsignedByte(), size = d.readUnsignedByte(), h = d.readUnsignedByte(), b = d.readUnsignedByte(), ng = d.readUnsignedShort();
                int[] codes = new int[ng], offs = new int[ng];
                java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
                for (int g = 0; g < ng; g++) {
                    codes[g] = d.readUnsignedShort();
                    int adv = d.readUnsignedByte();
                    offs[g] = bo.size();
                    bo.write(adv);
                    byte[] rows = new byte[h * ((adv + 7) / 8)];
                    d.readFully(rows);
                    bo.write(rows, 0, rows.length);
                }
                f[face * 3 + size] = new Face(h, b, codes, offs, bo.toByteArray());
            }
        } catch (IOException e) {
            throw new IllegalStateException("fonts: " + e.getMessage());
        }
        faces = f;
        return f;
    }

    /** @param key lcdui font key = face | style | size */
    public static Face face(int key) {
        int face = (key & 32) != 0 ? 1 : 0;
        int sz = (key & 8) != 0 ? 0 : (key & 16) != 0 ? 2 : 1;
        return load()[face * 3 + sz];
    }

    public static boolean bold(int key) { return (key & 1) != 0; }
    public static boolean italic(int key) { return (key & 2) != 0; }
    public static boolean underlined(int key) { return (key & 4) != 0; }

    public static int metrics(int key) {
        Face f = face(key);
        int h = f.height + (underlined(key) ? 1 : 0);
        return (h << 16) | f.baseline;
    }

    public static int advance(int key, char c) {
        return face(key).advance(c) + (bold(key) ? 1 : 0);
    }

    public static int advance(int key, char[] c, int off, int len) {
        int w = 0;
        for (int i = 0; i < len; i++) w += advance(key, c[off + i]);
        return w;
    }
}

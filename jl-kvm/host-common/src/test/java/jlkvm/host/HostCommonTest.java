package jlkvm.host;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import jlkvm.host.ams.Descriptor;
import jlkvm.host.ams.DescriptorException;
import jlkvm.host.ams.MidletSuite;
import jlkvm.host.ui.PngDecoder;
import jlkvm.host.ui.SoftRaster;
import jlkvm.host.ui.Surface;
import org.junit.Test;

public class HostCommonTest {
    // ------------------------------------------------------------------ JAD / manifest
    @Test public void descriptorSyntax() throws Exception {
        String mf = "﻿MIDlet-Name: A B\r\nMIDlet-Vendor:  v \r\nMIDlet-Version: 1.2.3\r\nLong: part1\r\n :part2 with colon\r\n and3\r\nMIDlet-1: A, /i.png ,pkg.A\r\n\r\n";
        Descriptor d = new Descriptor(mf, false);
        assertEquals("A B", d.getName());
        assertEquals("v", d.getVendor());
        assertEquals("part1:part2 with colonand3", d.get("Long"));
        assertEquals(1, d.compareVersion("1.2.2"));
        assertEquals(-1, d.compareVersion("1.10"));
        assertEquals(0, d.compareVersion("1.2.3"));
        try { new Descriptor("MIDlet-Name: x\n", false); fail(); } catch (DescriptorException e) { assertTrue(e.getMessage().contains("MIDlet-Vendor")); }
        try { new Descriptor("MIDlet-Name: x\nMIDlet-Vendor: v\nMIDlet-Version: 1\n", true); fail(); } catch (DescriptorException e) { assertTrue(e.getMessage().contains("MIDlet-Jar-Size")); }
    }

    @Test public void suiteMergesJadOverManifestAndRejectsDisagreement() throws Exception {
        String base = "MIDlet-Name: N\nMIDlet-Vendor: V\nMIDlet-Version: 1.0\n";
        byte[] mf = (base + "MIDlet-1: One,,a.One\nMIDlet-2: Two,/t.png,a.Two\nShared: m\nOnlyM: m\n").getBytes("UTF-8");
        byte[] jad = (base + "MIDlet-Jar-URL: x.jar\nMIDlet-Jar-Size: 10\nShared: j\n").getBytes("UTF-8");
        MidletSuite s = MidletSuite.load(mf, jad);
        assertEquals("j", s.attributes.get("Shared"));
        assertEquals("m", s.attributes.get("OnlyM"));
        assertEquals("a.Two", s.midlet(2).className);
        assertEquals("/t.png", s.midlet(2).icon);
        byte[] bad = (base.replace("1.0", "2.0") + "MIDlet-Jar-URL: x.jar\nMIDlet-Jar-Size: 10\n").getBytes("UTF-8");
        try { MidletSuite.load(mf, bad); fail(); } catch (DescriptorException e) { assertTrue(e.getMessage().contains("MIDlet-Version")); }
        try { MidletSuite.load((base).getBytes("UTF-8"), null); fail(); } catch (DescriptorException e) { assertTrue(e.getMessage().contains("MIDlet-1")); }
    }

    // ------------------------------------------------------------------ PNG
    private static byte[] chunk(String type, byte[] data) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        DataOutputStream d = new DataOutputStream(bo);
        d.writeInt(data.length);
        byte[] t = type.getBytes("ISO-8859-1");
        d.write(t);
        d.write(data);
        CRC32 c = new CRC32();
        c.update(t);
        c.update(data);
        d.writeInt((int) c.getValue());
        return bo.toByteArray();
    }

    private static byte[] png(int w, int h, int depth, int ctype, int interlace, byte[] raw, byte[] plte, byte[] trns) throws IOException {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        o.write(new byte[] {(byte) 0x89, 'P', 'N', 'G', 13, 10, 26, 10});
        ByteArrayOutputStream ih = new ByteArrayOutputStream();
        DataOutputStream d = new DataOutputStream(ih);
        d.writeInt(w); d.writeInt(h); d.writeByte(depth); d.writeByte(ctype); d.writeByte(0); d.writeByte(0); d.writeByte(interlace);
        o.write(chunk("IHDR", ih.toByteArray()));
        if (plte != null) o.write(chunk("PLTE", plte));
        if (trns != null) o.write(chunk("tRNS", trns));
        Deflater df = new Deflater();
        df.setInput(raw);
        df.finish();
        byte[] buf = new byte[raw.length + 100];
        int n = df.deflate(buf);
        o.write(chunk("IDAT", java.util.Arrays.copyOf(buf, n)));
        o.write(chunk("IEND", new byte[0]));
        return o.toByteArray();
    }

    @Test public void pngColourTypesAndFilters() throws Exception {
        // 3x2 RGB, rows use filters Sub (1) and Up (2)
        byte[] raw = {1, 10, 20, 30, 5, 5, 5, 5, 5, 5,    2, 1, 1, 1, 2, 2, 2, 3, 3, 3};
        PngDecoder p = PngDecoder.decode(png(3, 2, 8, 2, 0, raw, null, null), 0, png(3, 2, 8, 2, 0, raw, null, null).length);
        assertEquals(0xFF0A141E, p.argb[0]);
        assertEquals(0xFF0F1923, p.argb[1]);   // (10+5, 20+5, 30+5)
        assertEquals(0xFF141E28, p.argb[2]);
        assertEquals(0xFF0B151F, p.argb[3]);   // row 2 (filter Up) = row 1 + (1,1,1)
        assertEquals(0xFF111B25, p.argb[4]);
        assertFalse(p.hasAlpha);
        // 1-bit grey with tRNS key, palette 2-bit + tRNS, 16-bit RGBA, grey+alpha
        byte[] g1 = {0, (byte) 0b10100000};
        PngDecoder g = PngDecoder.decode(png(3, 1, 1, 0, 0, g1, null, new byte[] {0, 1}), 0, png(3, 1, 1, 0, 0, g1, null, new byte[] {0, 1}).length);
        assertEquals(0x00FFFFFF, g.argb[0]);   // sample 1 == tRNS key: transparent
        assertEquals(0xFF000000, g.argb[1]);
        assertTrue(g.hasAlpha);
        byte[] pal = {0, (byte) 0b00011011};
        byte[] png2 = png(4, 1, 2, 3, 0, pal, new byte[] {(byte) 255, 0, 0, 0, (byte) 255, 0, 0, 0, (byte) 255, 9, 9, 9}, new byte[] {(byte) 255, (byte) 128});
        PngDecoder q = PngDecoder.decode(png2, 0, png2.length);
        assertEquals(0xFFFF0000, q.argb[0]);
        assertEquals(0x8000FF00, q.argb[1]);
        assertEquals(0xFF0000FF, q.argb[2]);
        byte[] raw16 = {0, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0};
        byte[] png3 = png(1, 1, 16, 6, 0, raw16, null, null);
        PngDecoder r16 = PngDecoder.decode(png3, 0, png3.length);
        assertEquals(0xDE << 24 | 0x12 << 16 | 0x56 << 8 | 0x9A, r16.argb[0]);
        byte[] ga = {0, 100, (byte) 200, (byte) 150, 50};
        byte[] png4 = png(2, 1, 8, 4, 0, ga, null, null);
        PngDecoder g2 = PngDecoder.decode(png4, 0, png4.length);
        assertEquals(0xC8646464, g2.argb[0]);
        assertEquals(0x32969696, g2.argb[1]);
    }

    /** Adam7 interlaced 5x5 greyscale where pixel (x, y) = 10 * y + x. */
    @Test public void pngAdam7() throws Exception {
        int[][] passes = {{0, 0, 8, 8}, {4, 0, 8, 8}, {0, 4, 4, 8}, {2, 0, 4, 4}, {0, 2, 2, 4}, {1, 0, 2, 2}, {0, 1, 1, 2}};
        ByteArrayOutputStream raw = new ByteArrayOutputStream();
        for (int[] ps : passes) {
            int pw = (5 - ps[0] + ps[2] - 1) / ps[2], ph = (5 - ps[1] + ps[3] - 1) / ps[3];
            if (pw <= 0 || ph <= 0) continue;
            for (int y = 0; y < ph; y++) {
                raw.write(0);
                for (int x = 0; x < pw; x++) raw.write(10 * (ps[1] + y * ps[3]) + ps[0] + x * ps[2]);
            }
        }
        byte[] b = png(5, 5, 8, 0, 1, raw.toByteArray(), null, null);
        PngDecoder d = PngDecoder.decode(b, 0, b.length);
        for (int y = 0; y < 5; y++) for (int x = 0; x < 5; x++) assertEquals("(" + x + "," + y + ")", 0xFF000000 | (10 * y + x) * 0x010101, d.argb[y * 5 + x]);
    }

    @Test public void pngRejectsGarbage() throws Exception {
        byte[] good = png(1, 1, 8, 0, 0, new byte[] {0, 7}, null, null);
        for (int cut = 0; cut < good.length; cut++) {
            try { PngDecoder.decode(java.util.Arrays.copyOf(good, cut), 0, cut); if (cut < 45) fail("accepted a PNG cut at " + cut); } catch (IOException expected) { }
        }
        try { PngDecoder.decode(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, 0, 9); fail(); } catch (IOException e) { }
    }

    // ------------------------------------------------------------------ software raster
    private static int[] px(Surface s) { int[] a = new int[s.width() * s.height()]; s.getRgb(a, 0, s.width(), 0, 0, s.width(), s.height()); return a; }

    @Test public void rasterBasics() {
        SoftRaster f = new SoftRaster();
        Surface s = f.create(8, 6);
        assertEquals(0xFFFFFFFF, px(s)[0]);
        s.fillRect(0xFF0000, 0, 0, 8, 6, 1, 1, 3, 2);
        int[] a = px(s);
        assertEquals(0xFFFF0000, a[1 * 8 + 1]);
        assertEquals(0xFFFF0000, a[2 * 8 + 3]);
        assertEquals(0xFFFFFFFF, a[3 * 8 + 1]);
        assertEquals(0xFFFFFFFF, a[1 * 8 + 4]);
        // clip box restricts, a rect partly outside the surface is cut
        s.fillRect(0x0000FF, 2, 2, 4, 4, -5, -5, 100, 100);
        a = px(s);
        assertEquals(0xFF0000FF, a[3 * 8 + 3]);
        assertEquals(0xFFFFFFFF, a[1 * 8 + 1] == 0xFFFF0000 ? 0xFFFFFFFF : 0);
        assertEquals(0xFFFF0000, a[2 * 8 + 3] == 0xFF0000FF ? 0xFFFF0000 : a[2 * 8 + 3] == 0xFFFF0000 ? 0xFFFF0000 : 0);
        // line includes both end points; drawRect covers x..x+w
        Surface t = f.create(6, 6);
        t.drawLine(0, 0, 0, 6, 6, 0, 0, 5, 5, 0);
        int[] b = px(t);
        assertEquals(0xFF000000, b[0]);
        assertEquals(0xFF000000, b[5 * 6 + 5]);
        t.drawRect(0xFF00, 0, 0, 6, 6, 1, 1, 3, 2, 0);
        b = px(t);
        assertEquals(0xFF00FF00, b[1 * 6 + 4]);   // x + w
        assertEquals(0xFF00FF00, b[3 * 6 + 4]);   // y + h
    }

    @Test public void rasterTransformsAndAlpha() {
        SoftRaster f = new SoftRaster();
        int[] src = {1, 2, 3, 4, 5, 6};   // 3x2, opaque marker values 0xFF0000nn
        for (int i = 0; i < 6; i++) src[i] |= 0xFF000000;
        Surface im = f.fromRgb(src, 3, 2, false);
        int[][] expect = {   // dest rows for transforms 0..7, values as the last byte
            {1, 2, 3, 4, 5, 6}, {4, 5, 6, 1, 2, 3}, {3, 2, 1, 6, 5, 4}, {6, 5, 4, 3, 2, 1},
            {1, 4, 2, 5, 3, 6}, {4, 1, 5, 2, 6, 3}, {3, 6, 2, 5, 1, 4}, {6, 3, 5, 2, 4, 1}};
        for (int t = 0; t < 8; t++) {
            Surface c = im.copyRegion(0, 0, 3, 2, t);
            assertEquals(t >= 4 ? 2 : 3, c.width());
            int[] a = px(c);
            for (int i = 0; i < 6; i++) assertEquals("transform " + t + " px " + i, expect[t][i], a[i] & 0xFF);
            Surface d = f.create(10, 10);
            d.drawSurface(0, 0, 10, 10, im, 0, 0, 3, 2, t, 2, 3);
            int[] da = px(d);
            for (int i = 0; i < 6; i++) assertEquals("drawSurface " + t, expect[t][i], da[(3 + i / c.width()) * 10 + 2 + i % c.width()] & 0xFF);
        }
        Surface al = f.fromRgb(new int[] {0x80FF0000, 0x00FF0000, 0xFFFF0000}, 3, 1, true);
        Surface bg = f.create(3, 1);
        bg.drawSurface(0, 0, 3, 1, al, 0, 0, 3, 1, 0, 0, 0);
        int[] r = px(bg);
        assertEquals(0xFFFF7F7F, r[0]);   // 50 % red over white, rounded
        assertEquals(0xFFFFFFFF, r[1]);
        assertEquals(0xFFFF0000, r[2]);
    }

    @Test public void rasterIsDeterministicAndFontsHaveMetrics() {
        SoftRaster f = new SoftRaster();
        long[] h = new long[2];
        for (int run = 0; run < 2; run++) {
            SoftRaster s = (SoftRaster) f.create(64, 48);
            s.drawArc(0x112233, 0, 0, 64, 48, 4, 4, 40, 30, 20, 250, 0);
            s.fillArc(0x445566, 0, 0, 64, 48, 10, 10, 30, 30, 45, 200);
            s.fillRoundRect(0x778899, 0, 0, 64, 48, 5, 5, 40, 30, 12, 8);
            s.fillTriangle(0xAABBCC, 0, 0, 64, 48, 3, 40, 30, 10, 60, 45);
            s.drawChars(0, 0, 0, 64, 48, 0, "Hello".toCharArray(), 0, 5, 2, 2);
            h[run] = s.hash();
        }
        assertEquals(h[0], h[1]);
        int m = f.fontMetrics(0);
        assertTrue((m >>> 16) > (m & 0xFFFF) && (m & 0xFFFF) > 5);
        assertTrue(f.charsWidth(0, "Hello".toCharArray(), 0, 5) > f.charWidth(0, 'H'));
        assertEquals(f.charWidth(32, 'i'), f.charWidth(32, 'W'));   // monospace
        assertTrue(f.charWidth(64, 'i') < f.charWidth(64, 'W'));      // proportional
        assertEquals(f.charWidth(0, 'a') + 1, f.charWidth(1, 'a'));   // bold: one pixel wider
        assertTrue(f.charWidth(0, 'ạ') > 0);                      // Vietnamese letters are baked
    }
}

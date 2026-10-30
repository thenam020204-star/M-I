package vmspec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.microedition.midlet.MIDlet;

/** T19 encodings: only what CLDC 1.1 prescribes (ISO-8859-1, UTF-8) plus the default-encoding-independent behaviour. */
public class Cl06Encoding extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            sb.append("0123456789abcdef".charAt(v >> 4)).append("0123456789abcdef".charAt(v & 15));
        }
        return sb.toString();
    }

    static String codes(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) { sb.append(Integer.toHexString(s.charAt(i))).append(' '); }
        return sb.toString();
    }

    static String enc(String s, String cs) {
        try { return hex(s.getBytes(cs)); } catch (UnsupportedEncodingException e) { return "UEE"; }
    }

    static String dec(byte[] b, String cs) {
        try { return codes(new String(b, cs)); } catch (UnsupportedEncodingException e) { return "UEE"; }
    }

    protected void startApp() {
        String all = "a\u0000\u007f\u0080ÿĀ߿ࠀ￿";
        p("utf8 encode " + enc(all, "UTF-8"));
        p("latin1 encode " + enc(all, "ISO-8859-1"));
        p("utf8 decode " + dec(new byte[] {0x61, 0x7f, (byte) 0xC2, (byte) 0x80, (byte) 0xDF, (byte) 0xBF, (byte) 0xE0, (byte) 0xA0, (byte) 0x80, (byte) 0xEF, (byte) 0xBF, (byte) 0xBF}, "UTF-8"));
        p("latin1 decode " + dec(new byte[] {0x61, (byte) 0x80, (byte) 0xff, 0}, "ISO-8859-1"));
        p("empty " + enc("", "UTF-8") + "|" + dec(new byte[0], "UTF-8") + "|" + enc("", "ISO-8859-1") + "|" + dec(new byte[0], "ISO-8859-1"));
        p("names " + enc("é", "utf-8") + " " + enc("é", "UTF8") + " " + enc("é", "ISO8859_1") + " " + enc("é", "iso-8859-1") + " " + enc("x", "bogus") + " " + enc("x", ""));
        byte[] b = {0x41, 0x42, 0x43, 0x44, 0x45};
        try {
            p("range " + codes(new String(b, 1, 3, "UTF-8")) + "| " + codes(new String(b, 1, 3, "ISO-8859-1")) + "| " + codes(new String(b, 0, 0, "UTF-8")) + "|");
        } catch (Exception e) { p("unexpected " + e); }
        try { new String(b, 3, 3, "UTF-8"); p("range ex none"); } catch (Exception e) { p("range ex " + e.getClass().getName()); }
        try { new String(b, -1, 1, "UTF-8"); p("range ex none"); } catch (Exception e) { p("range ex " + e.getClass().getName()); }
        try { new String((byte[]) null, "UTF-8"); p("null ex none"); } catch (Exception e) { p("null ex " + e.getClass().getName()); }
        try { "x".getBytes(null); p("null enc none"); } catch (Exception e) { p("null enc " + e.getClass().getName()); }
        try { new String(b, "nope"); p("unsupported none"); } catch (Exception e) { p("unsupported " + e.getClass().getName()); }
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            OutputStreamWriter w = new OutputStreamWriter(bo, "UTF-8");
            w.write(all);
            w.flush();
            p("writer utf8 " + hex(bo.toByteArray()));
            bo = new ByteArrayOutputStream();
            w = new OutputStreamWriter(bo, "ISO-8859-1");
            w.write("Aé€");
            w.close();
            p("writer latin1 " + hex(bo.toByteArray()));
            InputStreamReader r = new InputStreamReader(new ByteArrayInputStream(new byte[] {0x41, (byte) 0xC3, (byte) 0xA9, (byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xDF, (byte) 0xBF}), "UTF-8");
            StringBuffer sb = new StringBuffer();
            char[] cb = new char[3];
            int n;
            while ((n = r.read(cb, 0, 3)) != -1) { sb.append(cb, 0, n); }
            p("reader utf8 chunks " + codes(sb.toString()));
            r = new InputStreamReader(new ByteArrayInputStream(new byte[] {(byte) 0xE9, (byte) 0xFF, 0x41}), "ISO-8859-1");
            sb = new StringBuffer();
            int ch;
            while ((ch = r.read()) != -1) { sb.append((char) ch); }
            p("reader latin1 " + codes(sb.toString()));
        } catch (Exception e) { p("unexpected " + e); }
        // round trip of every BMP character that is not a surrogate
        StringBuffer big = new StringBuffer();
        for (int c = 0; c < 0xD800; c += 7) { big.append((char) c); }
        for (int c = 0xE000; c < 0x10000; c += 5) { big.append((char) c); }
        String bs = big.toString();
        try {
            byte[] u = bs.getBytes("UTF-8");
            p("roundtrip utf8 " + bs.equals(new String(u, "UTF-8")) + " " + u.length + " " + bs.length());
            byte[] l = bs.getBytes("ISO-8859-1");
            p("latin1 len " + l.length + " " + (l[0] & 0xff) + " " + (l[5] & 0xff) + " " + (l[40] & 0xff));
        } catch (Exception e) { p("unexpected " + e); }
        // default encoding: pure ASCII must be identical in every encoding
        p("ascii default " + hex("Hello 123".getBytes()) + " " + new String("Hello 123".getBytes()) + " " + new String("Hello 123".getBytes(), 6, 3));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

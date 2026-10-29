package vmspec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.microedition.midlet.MIDlet;

/**
 * Character encodings. Explicit charsets are specified by CLDC; the default (microedition.encoding) is
 * device/profile dependent and only printed on informational '# ' lines. No bytecode rewriting (PLAN D12/D16).
 */
public class Enc01Encoding extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            sb.append("0123456789abcdef".charAt(v >> 4)).append("0123456789abcdef".charAt(v & 15));
        }
        return sb.toString();
    }

    protected void startApp() {
        String s = "A\u00e9\u20ac";       // A, e-acute, euro sign
        System.out.println("# microedition.encoding=" + System.getProperty("microedition.encoding"));
        System.out.println("# default getBytes=" + hex(s.getBytes()));
        try {
            p("utf8 " + hex(s.getBytes("UTF-8")));
            p("latin1 " + hex(s.getBytes("ISO-8859-1")));
            byte[] u = new byte[] { 0x41, (byte) 0xC3, (byte) 0xA9, (byte) 0xE2, (byte) 0x82, (byte) 0xAC };
            String d = new String(u, "UTF-8");
            p("decode utf8 len=" + d.length() + " c1=" + (int) d.charAt(1) + " c2=" + (int) d.charAt(2));
            String l = new String(u, 0, 3, "ISO-8859-1");
            p("decode latin1 len=" + l.length() + " c1=" + (int) l.charAt(1) + " c2=" + (int) l.charAt(2));
            // malformed UTF-8: lone continuation byte, truncated sequence
            String m = new String(new byte[] { 0x41, (byte) 0x80, 0x42 }, "UTF-8");
            p("malformed len=" + m.length() + " first=" + (int) m.charAt(0) + " last=" + (int) m.charAt(m.length() - 1));
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            OutputStreamWriter w = new OutputStreamWriter(bo, "UTF-8");
            w.write(s);
            w.close();
            p("writer utf8 " + hex(bo.toByteArray()));
            InputStreamReader r = new InputStreamReader(new ByteArrayInputStream(bo.toByteArray()), "UTF-8");
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = r.read()) != -1) sb.append((char) ch);
            p("reader utf8 " + (sb.toString().equals(s)));
        } catch (Exception e) {
            p("unexpected " + e.getClass().getName());
        }
        try {
            "x".getBytes("no-such-charset");
            p("unknown charset ok (wrong)");
        } catch (UnsupportedEncodingException e) {
            p("unknown charset UnsupportedEncodingException");
        }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

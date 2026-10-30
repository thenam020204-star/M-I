package vmspec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.microedition.midlet.MIDlet;

/** T19: the default encoding (microedition.encoding of the profile) drives new String(byte[]), getBytes(), Reader/Writer, PrintStream. Driven by ClasslibSystemTest. */
public class Cl11DefaultEncoding extends MIDlet {
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

    protected void startApp() {
        p("enc " + System.getProperty("microedition.encoding"));
        String s = "Aé€ÿ";
        p("getBytes " + hex(s.getBytes()));
        byte[] in = {0x41, (byte) 0xC3, (byte) 0xA9, (byte) 0xE9, (byte) 0xE2, (byte) 0x82, (byte) 0xAC};
        p("decode " + codes(new String(in)));
        p("decode range " + codes(new String(in, 1, 2)));
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            OutputStreamWriter w = new OutputStreamWriter(bo);
            w.write(s);
            w.close();
            p("writer " + hex(bo.toByteArray()));
            InputStreamReader r = new InputStreamReader(new ByteArrayInputStream(in));
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = r.read()) != -1) { sb.append((char) ch); }
            p("reader " + codes(sb.toString()));
        } catch (Exception e) { p("unexpected " + e); }
        System.out.println("> console é€");
        System.err.println("> console err é");
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

package vmspec;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.midlet.MIDlet;

/** Damaged JAR (dup): the outcome is not covered by CLDC/MIDP; this case records what the oracles do. */
public class J04DuplicateEntry extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String read(String path) {
        InputStream in = J04DuplicateEntry.class.getResourceAsStream(path);
        if (in == null) return "null";
        try {
            StringBuffer sb = new StringBuffer();
            int c;
            while ((c = in.read()) != -1) sb.append((char) c);
            in.close();
            return sb.toString().trim();
        } catch (IOException e) {
            return "IOException";
        } catch (RuntimeException e) {
            return "RuntimeException";
        }
    }

    protected void startApp() {
        p("data -> " + read("/data.txt"));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

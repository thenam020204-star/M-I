package vmspec;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.midlet.MIDlet;

/** Class.getResourceAsStream: absolute when starting with '/', else relative to the class's package; case-sensitive. */
public class J01ResourcePaths extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String first(String path) {
        InputStream in = J01ResourcePaths.class.getResourceAsStream(path);
        if (in == null) return "null";
        try {
            StringBuffer sb = new StringBuffer();
            int c;
            while ((c = in.read()) != -1 && c != '\n') sb.append((char) c);
            in.close();
            return sb.toString();
        } catch (IOException e) {
            return "IOException";
        }
    }

    protected void startApp() {
        p("/data.txt -> " + first("/data.txt"));
        p("data.txt (relative to vmspec/) -> " + first("data.txt"));
        p("/vmspec/data.txt -> " + first("/vmspec/data.txt"));
        p("/Data.TXT -> " + first("/Data.TXT"));
        p("/missing.txt -> " + first("/missing.txt"));
        // unspecified by CLDC/MIDP (oracles differ): informational only
        System.out.println("# ../data.txt -> " + first("../data.txt"));
        System.out.println("# //data.txt -> " + first("//data.txt"));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

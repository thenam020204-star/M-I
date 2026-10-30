package vmspec;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import javax.microedition.midlet.MIDlet;

/** T20: driven by ClasslibSystemTest (needs a device profile JSON); prints the profile-dependent facts. */
public class Cl07System extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static final String[] KEYS = {"microedition.platform", "microedition.encoding", "microedition.configuration", "microedition.profiles", "microedition.locale",
        "microedition.hostname", "microedition.Platform", "MICROEDITION.PLATFORM", "microedition.", "java.version", "os.name", "user.dir", "file.separator", "line.separator", "com.nokia.mid.ui.version", "x", "microedition.platform "};

    static String open(String url) {
        try {
            Connection c = Connector.open(url, Connector.READ);
            String r = "opened " + c.getClass().getName();
            if (c instanceof InputConnection) {
                InputStream in = ((InputConnection) c).openInputStream();
                StringBuffer sb = new StringBuffer();
                int ch;
                while ((ch = in.read()) != -1) { sb.append((char) ch); }
                r += " data=" + sb;
            }
            c.close();
            return r;
        } catch (ConnectionNotFoundException e) { return "ConnectionNotFoundException";
        } catch (IOException e) { return "IOException";
        } catch (SecurityException e) { return "SecurityException";
        } catch (IllegalArgumentException e) { return "IllegalArgumentException";
        } catch (Throwable e) { return "other " + e.getClass().getName(); }
    }

    protected void startApp() {
        for (int i = 0; i < KEYS.length; i++) { p("prop " + KEYS[i] + " = " + System.getProperty(KEYS[i])); }
        try { System.getProperty(null); p("getProperty(null) no exception"); } catch (NullPointerException e) { p("getProperty(null) NullPointerException"); }
        try { System.getProperty(""); p("getProperty(\"\") no exception"); } catch (IllegalArgumentException e) { p("getProperty(\"\") IllegalArgumentException"); }
        Runtime rt = Runtime.getRuntime();
        p("runtime same " + (rt == Runtime.getRuntime()));
        long total = rt.totalMemory();
        p("total " + total);
        rt.gc();
        long f0 = rt.freeMemory();
        Object[] keep = new Object[100];
        for (int i = 0; i < keep.length; i++) { keep[i] = new byte[1000]; }
        long f1 = rt.freeMemory();
        p("free decreased " + (f1 < f0) + " by-at-least " + (f0 - f1 >= 100 * 1000));
        keep = null;
        rt.gc();
        long f2 = rt.freeMemory();
        p("free recovered " + (f2 > f1) + " " + (f2 >= f0 - 2000) + " total-constant " + (rt.totalMemory() == total) + " free<=total " + (f2 <= total));
        System.gc();
        p("gc ok " + (rt.freeMemory() <= rt.totalMemory()));
        try {
            int[][] hog = new int[100000][];
            for (int i = 0; i < hog.length; i++) { hog[i] = new int[10000]; }
            p("oom none");
        } catch (OutOfMemoryError e) { p("OutOfMemoryError caught, free now >= 0 " + (rt.freeMemory() >= 0)); }
        long t0 = System.currentTimeMillis();
        try { Thread.sleep(100); } catch (InterruptedException e) { }
        long t1 = System.currentTimeMillis();
        p("clock advanced " + (t1 - t0 >= 100) + " " + (t1 - t0 < 5000));
        p("clock value " + (t0 / 1000));
        TimeZone tz = TimeZone.getDefault();
        p("default tz " + tz.getID() + " " + tz.getRawOffset());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(0L));
        p("default calendar epoch hour " + cal.get(Calendar.HOUR_OF_DAY));
        p("id " + (System.identityHashCode(null)) + " " + (System.identityHashCode(rt) == System.identityHashCode(rt)) + " " + (System.identityHashCode(rt) == rt.hashCode()));
        p("open http " + open("http://example.invalid/a"));
        p("open https " + open("https://example.invalid/a"));
        p("open socket " + open("socket://example.invalid:80"));
        p("open HTTP upper " + open("HTTP://example.invalid/a"));
        p("open testnet " + open("testnet://x/y"));
        p("open nohandler " + open("nohandler://x"));
        p("open file " + open("file:///a/b"));
        p("open comm " + open("comm:0;baudrate=9600"));
        p("open null " + open(null));
        p("open noscheme " + open("example.invalid/x"));
        p("open emptyscheme " + open(":x"));
        p("open badscheme " + open("ht tp://x"));
        p("open badscheme2 " + open("1http://x"));
        try { Connector.open("testnet://x", 7); p("bad mode no exception"); } catch (Exception e) { p("bad mode " + e.getClass().getName()); }
        try { Connector.openInputStream("testnet://x").close(); p("openInputStream ok"); } catch (Exception e) { p("openInputStream " + e.getClass().getName()); }
        try { Connector.openDataInputStream("http://x").close(); p("openDataInputStream ok"); } catch (Exception e) { p("openDataInputStream " + e.getClass().getName()); }
        try { Connector.openOutputStream("socket://x:1"); p("openOutputStream ok"); } catch (Exception e) { p("openOutputStream " + e.getClass().getName()); }
        p("constants " + Connector.READ + Connector.WRITE + Connector.READ_WRITE);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

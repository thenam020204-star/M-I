package vmspec;

import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.midlet.MIDlet;

/** T32 (JSR-82): a device WITHOUT Bluetooth hardware (default profile, bluetooth.mode none) but with the JSR-82 API: nothing can be initialised. */
public class Bt01Nohw extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    protected void startApp() {
        try { LocalDevice.getLocalDevice(); p("getLocalDevice: ok"); } catch (Throwable t) { p("getLocalDevice: " + cn(t)); }
        try { LocalDevice.getLocalDevice(); p("getLocalDevice again: ok"); } catch (Throwable t) { p("getLocalDevice again: " + cn(t)); }
        p("api.version: " + LocalDevice.getProperty("bluetooth.api.version"));
        p("unknown property: " + LocalDevice.getProperty("bluetooth.nothing"));
        String[] urls = {"btspp://001122334455:1", "btspp://localhost:00001101000010008000006057029D35;name=x", "btl2cap://001122334455:1003"};
        for (int i = 0; i < urls.length; i++) {
            try { Connector.open(urls[i]); p("open " + i + ": opened"); } catch (Throwable t) { p("open " + i + ": " + cn(t)); }
        }
        try { Connector.open("btgoep://001122334455:9"); p("btgoep: opened"); } catch (Throwable t) { p("btgoep: " + cn(t)); }
        try { p("UUID class: " + Class.forName("javax.bluetooth.UUID").getName()); } catch (Throwable t) { p("UUID class: " + cn(t)); }
        notifyDestroyed();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}

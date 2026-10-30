package vmspec;

import javax.microedition.io.Connector;
import javax.microedition.midlet.MIDlet;

/** T32a: sms / cbs under the profile sms.mode offline: every open is an IOException, whatever the URL; the WMA types exist (layer present in the test ROM). */
public class Wma01bOffline extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    protected void startApp() {
        String[] urls = {"sms://+84901234567", "sms://+84901234567:5000", "sms://:5000", "cbs://:2000", "sms:garbage", "SMS://+1"};
        for (int i = 0; i < urls.length; i++) {
            try { Connector.open(urls[i]); p(urls[i] + ": opened"); } catch (Throwable t) { p(urls[i] + ": " + cn(t)); }
        }
        try { Connector.openOutputStream("sms://+84901234567"); p("stream: opened"); } catch (Throwable t) { p("stream: " + cn(t)); }
        try { p("MessageConnection " + Class.forName("javax.wireless.messaging.MessageConnection").isInterface()); } catch (Throwable t) { p("MessageConnection: " + cn(t)); }
        try { Connector.open("mms://+1"); p("mms: opened"); } catch (Throwable t) { p("mms: " + cn(t)); }
        destroyApp(true);
        notifyDestroyed();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}

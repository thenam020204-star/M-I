package vmspec;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.PushRegistry;
import javax.microedition.io.SocketConnection;
import javax.microedition.midlet.MIDlet;
import javax.microedition.pki.CertificateException;

/** T26b: the MIDP part of the GCF (constants, interface hierarchy), PushRegistry without push, CertificateException, Connector under the default profile (http deny, rest offline). */
public class Io01Gcf extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    protected void startApp() {
        p("http codes: " + HttpConnection.HTTP_OK + " " + HttpConnection.HTTP_NOT_FOUND + " " + HttpConnection.HTTP_MOVED_TEMP + " " + HttpConnection.HTTP_INTERNAL_ERROR
            + " " + HttpConnection.HTTP_TEMP_REDIRECT + " " + HttpConnection.HTTP_VERSION + " methods " + HttpConnection.GET + "," + HttpConnection.HEAD + "," + HttpConnection.POST);
        p("socket options: " + SocketConnection.DELAY + SocketConnection.LINGER + SocketConnection.KEEPALIVE + SocketConnection.RCVBUF + SocketConnection.SNDBUF);
        p("hierarchy: " + (HttpConnection.class.getName()) + " is ContentConnection: " + isAssignable("javax.microedition.io.ContentConnection", "javax.microedition.io.HttpConnection")
            + ", Https is Http: " + isAssignable("javax.microedition.io.HttpConnection", "javax.microedition.io.HttpsConnection")
            + ", Secure is Socket: " + isAssignable("javax.microedition.io.SocketConnection", "javax.microedition.io.SecureConnection")
            + ", ServerSocket is Notifier: " + isAssignable("javax.microedition.io.StreamConnectionNotifier", "javax.microedition.io.ServerSocketConnection")
            + ", UDP is Datagram: " + isAssignable("javax.microedition.io.DatagramConnection", "javax.microedition.io.UDPDatagramConnection")
            + ", Comm is Stream: " + isAssignable("javax.microedition.io.StreamConnection", "javax.microedition.io.CommConnection"));
        CertificateException ce = new CertificateException(null, CertificateException.EXPIRED);
        CertificateException ce2 = new CertificateException("bad site", null, CertificateException.SITENAME_MISMATCH);
        p("CertificateException: " + ce.getReason() + " " + ce.getCertificate() + " " + ce.getMessage() + " | " + ce2.getReason() + " " + ce2.getMessage() + " io=" + (ce instanceof IOException)
            + " reasons " + CertificateException.BAD_EXTENSIONS + CertificateException.BROKEN_CHAIN + CertificateException.VERIFICATION_FAILED + CertificateException.UNSUPPORTED_PUBLIC_KEY_TYPE);
        p("push: list=" + PushRegistry.listConnections(true).length + "/" + PushRegistry.listConnections(false).length + " midlet=" + PushRegistry.getMIDlet("socket://:79")
            + " filter=" + PushRegistry.getFilter("socket://:79") + " unregister=" + PushRegistry.unregisterConnection("socket://:79"));
        try { PushRegistry.registerConnection("socket://:79", "no.such.Midlet", "*"); p("register unknown class: ok"); } catch (Throwable t) { p("register unknown class: " + cn(t)); }
        try { PushRegistry.registerConnection("socket://:79", "vmspec.Io01Gcf", "*"); p("register: ok"); } catch (Throwable t) { p("register: " + cn(t)); }
        try { PushRegistry.registerConnection(null, "vmspec.Io01Gcf", "*"); p("register null: ok"); } catch (Throwable t) { p("register null: " + cn(t)); }
        try { PushRegistry.registerAlarm("vmspec.Io01Gcf", System.currentTimeMillis() + 1000); p("alarm: ok"); } catch (Throwable t) { p("alarm: " + cn(t)); }
        try { PushRegistry.registerAlarm("no.such.Midlet", 0); p("alarm unknown class: ok"); } catch (Throwable t) { p("alarm unknown class: " + cn(t)); }
        String[] urls = {"http://127.0.0.1:1/", "https://127.0.0.1:1/", "socket://127.0.0.1:1", "serversocket://:1", "datagram://127.0.0.1:1", "ssl://127.0.0.1:1", "comm:COM0", "nohandler:x"};
        for (int i = 0; i < urls.length; i++) {
            try { Connector.open(urls[i]); p(urls[i] + ": opened"); } catch (Throwable t) { p(urls[i] + ": " + cn(t)); }
        }
        notifyDestroyed();
    }

    static boolean isAssignable(String sup, String sub) {
        try { return Class.forName(sup).isAssignableFrom(Class.forName(sub)); } catch (Throwable t) { return false; }
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

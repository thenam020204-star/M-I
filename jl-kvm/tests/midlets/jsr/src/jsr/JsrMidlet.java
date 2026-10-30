package jsr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.midlet.MIDlet;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.TextMessage;

/**
 * T32a host test (JsrHostTest): the layers exist only when the profile declares them (D10); sms in the mode of the profile (client send, server
 * receive / listener with messages pushed by the test host through the script); FileConnection against the host file system (sandbox).
 * Each line starts with "> ". The compile-time stubs are the open jars (J2MEC_EXTRA by build.sh).
 */
public class JsrMidlet extends MIDlet implements MessageListener {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    static boolean has(String cls) {
        try { Class.forName(cls); return true; } catch (ClassNotFoundException e) { return false; }
    }

    private Thread main;
    private MessageConnection server;
    private int notified;

    public void notifyIncomingMessage(MessageConnection conn) {
        notified++;
        p("listener: notified " + notified + " on a guest thread other than main: " + (Thread.currentThread() != main) + " same connection: " + (conn == server));
        try {
            Message m = conn.receive();
            if (m instanceof TextMessage) p("listener: text '" + ((TextMessage) m).getPayloadText() + "' from " + m.getAddress() + " timestamp set " + (m.getTimestamp() != null));
            else p("listener: binary " + ((BinaryMessage) m).getPayloadData().length + " bytes from " + m.getAddress());
        } catch (Throwable t) {
            p("listener: receive " + cn(t));
        }
    }

    public void startApp() {
        main = Thread.currentThread();
        p("layers wma " + has("javax.wireless.messaging.MessageConnection") + " fc " + has("javax.microedition.io.file.FileConnection") + " pim " + has("javax.microedition.pim.PIM"));
        if (has("javax.wireless.messaging.MessageConnection")) sms();
        if (has("javax.microedition.io.file.FileConnection")) files();
        p("done");
        notifyDestroyed();
    }

    void sms() {
        MessageConnection c = null;
        try {
            c = (MessageConnection) Connector.open("sms://+84901234567:5000");
            TextMessage t = (TextMessage) c.newMessage(MessageConnection.TEXT_MESSAGE);
            t.setPayloadText("hello world");
            c.send(t);
            p("client send ok");
            BinaryMessage b = (BinaryMessage) c.newMessage(MessageConnection.BINARY_MESSAGE);
            b.setPayloadData(new byte[] {(byte) 0xCA, (byte) 0xFE});
            c.send(b);
            p("client binary send ok");
        } catch (Throwable t) {
            p("client: " + cn(t));
        }
        try {
            server = (MessageConnection) Connector.open("sms://:6000");
            p("server open");
            server.setMessageListener(this);
            Thread.sleep(200);      // script: 100 ms first message (the listener receives it)
            server.setMessageListener(null);
            p("listener removed, notified " + notified);
            Message m = server.receive();    // script: 300 ms second message
            p("receive: " + ((TextMessage) m).getPayloadText() + " from " + m.getAddress());
            Thread.sleep(300);      // script: 400 ms binary message arrives while no listener is set
            server.setMessageListener(this);    // the message that is waiting is notified at once
            Thread.sleep(200);
            server.setMessageListener(null);
            p("listener removed again, notified " + notified);
            server.close();
            p("server closed");
        } catch (Throwable t) {
            p("server: " + cn(t));
        }
        try {
            MessageConnection cb = (MessageConnection) Connector.open("cbs://:2000");
            p("cbs open");
            Message m = cb.receive();
            p("cbs receive: " + ((TextMessage) m).getPayloadText());
            cb.close();
        } catch (Throwable t) {
            p("cbs: " + cn(t));
        }
    }

    void files() {
        try {
            FileConnection d = (FileConnection) Connector.open("file:///root1/saves/");
            if (!d.exists()) d.mkdir();
            FileConnection f = (FileConnection) Connector.open("file:///root1/saves/slot1.dat");
            if (!f.exists()) f.create();
            OutputStream o = f.openOutputStream();
            o.write("saved".getBytes());
            o.close();
            InputStream in = f.openInputStream();
            byte[] buf = new byte[16];
            int n = in.read(buf);
            in.close();
            p("file: " + new String(buf, 0, n) + " size " + f.fileSize());
            f.close();
            d.close();
        } catch (Throwable t) {
            p("file: " + cn(t));
        }
        String[] urls = {"file:///root1/../escape.txt", "file:///root1/%2e%2e/escape.txt", "file:///root1/link/secret.txt", "file:///root1/link/new.txt"};
        for (int i = 0; i < urls.length; i++) {
            try {
                FileConnection f = (FileConnection) Connector.open(urls[i]);
                p(urls[i] + ": opened, exists " + f.exists());
                if (!f.exists()) { f.create(); p(urls[i] + ": created"); }
                else { InputStream in = f.openInputStream(); p(urls[i] + ": read " + in.read()); in.close(); }
                f.close();
            } catch (Throwable t) {
                p(urls[i] + ": " + cn(t));
            }
        }
        try {
            FileConnection l = (FileConnection) Connector.open("file:///root1/");
            StringBuffer sb = new StringBuffer();
            for (Enumeration e = l.list(); e.hasMoreElements();) sb.append(e.nextElement()).append(' ');
            p("root1 list: " + sb);
            l.close();
        } catch (Throwable t) {
            p("list: " + cn(t));
        }
    }

    public void pauseApp() {}
    public void destroyApp(boolean u) {}
}

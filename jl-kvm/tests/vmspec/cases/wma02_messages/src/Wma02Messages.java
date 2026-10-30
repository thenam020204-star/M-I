package vmspec;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.midlet.MIDlet;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.TextMessage;

/** T32a: WMA 1.1 messages and connections under sms.mode simulate (nothing leaves the host). */
public class Wma02Messages extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    static String rep(char c, int n) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) sb.append(c);
        return sb.toString();
    }

    static int segs(MessageConnection c, String text) {
        TextMessage m = (TextMessage) c.newMessage(MessageConnection.TEXT_MESSAGE);
        m.setPayloadText(text);
        return c.numberOfSegments(m);
    }

    static int bsegs(MessageConnection c, int len, String addr) {
        BinaryMessage m = (BinaryMessage) c.newMessage(MessageConnection.BINARY_MESSAGE, addr);
        m.setPayloadData(new byte[len]);
        return c.numberOfSegments(m);
    }

    protected void startApp() {
        try {
            run();
        } catch (Throwable t) {
            p("UNEXPECTED " + t);
        }
        notifyDestroyed();
    }

    void run() throws Exception {
        p("constants " + MessageConnection.TEXT_MESSAGE + " " + MessageConnection.BINARY_MESSAGE);
        MessageConnection c = (MessageConnection) Connector.open("sms://+84901234567:5000");
        Message t = c.newMessage(MessageConnection.TEXT_MESSAGE);
        Message b = c.newMessage(MessageConnection.BINARY_MESSAGE);
        p("text is TextMessage " + (t instanceof TextMessage) + " BinaryMessage " + (t instanceof BinaryMessage)
            + "; binary is TextMessage " + (b instanceof TextMessage) + " BinaryMessage " + (b instanceof BinaryMessage));
        p("address " + t.getAddress() + " timestamp " + t.getTimestamp() + " payload " + ((TextMessage) t).getPayloadText() + " " + ((BinaryMessage) b).getPayloadData());
        Message t2 = c.newMessage(MessageConnection.TEXT_MESSAGE, "sms://+1");
        p("explicit address " + t2.getAddress());
        try { c.newMessage("other"); p("other type: ok"); } catch (Throwable x) { p("other type: " + cn(x)); }
        try { c.newMessage(null); p("null type: ok"); } catch (Throwable x) { p("null type: " + cn(x)); }
        byte[] data = {1, 2, 3};
        ((BinaryMessage) b).setPayloadData(data);
        p("same array " + (((BinaryMessage) b).getPayloadData() == data));
        // segments of text: GSM 7-bit alphabet 160 / 153 (UNVERIFIED), a character outside it makes the message UCS-2: 70 / 67
        p("text segments: empty " + segs(c, "") + " hello " + segs(c, "hello") + " 160a " + segs(c, rep('a', 160)) + " 161a " + segs(c, rep('a', 161))
            + " 306a " + segs(c, rep('a', 306)) + " 307a " + segs(c, rep('a', 307)));
        p("extension chars count 2: 80x{ " + segs(c, rep('{', 80)) + " 81x{ " + segs(c, rep('{', 81)));
        p("UCS-2: 70 " + segs(c, rep('中', 70)) + " 71 " + segs(c, rep('中', 71)) + " 134 " + segs(c, rep('中', 134)) + " 135 " + segs(c, rep('中', 135))
            + " one such char among 160a " + segs(c, rep('a', 159) + "中"));
        p("binary segments without port: 140 " + bsegs(c, 140, "sms://+1") + " 141 " + bsegs(c, 141, "sms://+1") + " 268 " + bsegs(c, 268, "sms://+1") + " 269 " + bsegs(c, 269, "sms://+1")
            + "; with port: 133 " + bsegs(c, 133, "sms://+1:5000") + " 134 " + bsegs(c, 134, "sms://+1:5000") + " 256 " + bsegs(c, 256, "sms://+1:5000") + " 257 " + bsegs(c, 257, "sms://+1:5000"));
        p("too long: " + segs(c, rep('a', 255 * 153 + 1)) + " ok: " + segs(c, rep('a', 255 * 153)));
        // send
        ((TextMessage) t).setPayloadText("hello");
        c.send(t);
        p("sent text");
        c.send(b);
        p("sent binary");
        try { c.send(null); p("send null: ok"); } catch (Throwable x) { p("send null: " + cn(x)); }
        t.setAddress(null);
        try { c.send(t); p("send no address: ok"); } catch (Throwable x) { p("send no address: " + cn(x)); }
        t.setAddress("tel:123");
        try { c.send(t); p("send bad address: ok"); } catch (Throwable x) { p("send bad address: " + cn(x)); }
        t.setAddress("sms://+12ab");
        try { c.send(t); p("send bad number: ok"); } catch (Throwable x) { p("send bad number: " + cn(x)); }
        ((TextMessage) t).setPayloadText(rep('a', 255 * 153 + 1));
        t.setAddress("sms://+1");
        try { c.send(t); p("send too long: ok"); } catch (Throwable x) { p("send too long: " + cn(x)); }
        // a client mode connection cannot receive
        try { c.receive(); p("client receive: ok"); } catch (Throwable x) { p("client receive: " + cn(x)); }
        try { c.setMessageListener(null); p("client listener: ok"); } catch (Throwable x) { p("client listener: " + cn(x)); }
        c.close();
        c.close();
        try { c.send(t); p("send closed: ok"); } catch (Throwable x) { p("send closed: " + cn(x)); }
        try { c.receive(); p("receive closed: ok"); } catch (Throwable x) { p("receive closed: " + cn(x)); }
        // bad URLs
        String[] bad = {"sms://", "sms:+123", "sms://+12ab", "sms://+123:99999", "sms://+123:x", "sms://+", "cbs://+123", "sms://:", "sms://abc"};
        for (int i = 0; i < bad.length; i++) {
            try { Connector.open(bad[i]).close(); p(bad[i] + ": opened"); } catch (Throwable x) { p(bad[i] + ": " + cn(x)); }
        }
        // server mode: the port is reserved, the new message has no address
        MessageConnection s = (MessageConnection) Connector.open("sms://:6000");
        p("server new message address " + s.newMessage(MessageConnection.TEXT_MESSAGE).getAddress());
        try { Connector.open("sms://:6000"); p("same port: opened"); } catch (Throwable x) { p("same port: " + cn(x)); }
        MessageListener l = new MessageListener() { public void notifyIncomingMessage(MessageConnection mc) { p("UNEXPECTED notification"); } };
        s.setMessageListener(l);
        s.setMessageListener(null);
        s.setMessageListener(l);
        TextMessage out = (TextMessage) s.newMessage(MessageConnection.TEXT_MESSAGE, "sms://+84900000000:6001");
        out.setPayloadText("from the server connection");
        s.send(out);
        p("server sent");
        s.close();
        try { s.setMessageListener(null); p("listener closed: ok"); } catch (Throwable x) { p("listener closed: " + cn(x)); }
        MessageConnection s2 = (MessageConnection) Connector.open("sms://:6000");
        p("port reusable after close");
        s2.close();
        // cbs: server mode only, receive only
        MessageConnection cb = (MessageConnection) Connector.open("cbs://:2000");
        TextMessage cm = (TextMessage) cb.newMessage(MessageConnection.TEXT_MESSAGE, "sms://+1");
        try { cb.send(cm); p("cbs send: ok"); } catch (Throwable x) { p("cbs send: " + cn(x)); }
        cb.close();
        p("done");
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}

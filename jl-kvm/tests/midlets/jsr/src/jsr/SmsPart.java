package jsr;

import javax.microedition.io.Connector;
import javax.wireless.messaging.BinaryMessage;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.TextMessage;

/** The WMA part of JsrMidlet (loaded only when the profile declares wma-1.1). */
class SmsPart implements MessageListener {
    static void p(String s) { JsrMidlet.p(s); }

    static String cn(Throwable t) { return JsrMidlet.cn(t); }

    private final Thread main = Thread.currentThread();
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

    void run() {
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

}

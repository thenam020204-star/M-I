/*
 * jl-kvm guest class library (WMA 1.1 layer, API per tools/api-sigs/db/s40dp2-wma-1.1.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour per the WMA javadoc of the S40 DP2.0 SDK 1.1, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.wma;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Date;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;

/**
 * A MessageConnection of the SMS or CBS adapter. Client mode ({@code sms://+358401234567[:port]}): messages can only be sent, new
 * messages carry the destination of the URL. Server mode ({@code sms://:port}, {@code cbs://:channel}): the port is reserved on the host;
 * messages are received (queued by the host, which only a test host fills) and can be sent. The listener runs on its own guest thread.
 * Segment counts are the usual GSM 03.40 figures (UNVERIFIED against the adapter specification, docs/jsr.md).
 */
public final class Conn implements MessageConnection, Runnable {
    /** Most segments of one message (the 8-bit concatenation reference); a longer payload is "too long" (IllegalArgumentException on send). */
    static final int MAX_SEGMENTS = 255;

    private static final String GSM7 = "@\u00a3$\u00a5\u00e8\u00e9\u00f9\u00ec\u00f2\u00c7\n\u00d8\u00f8\r\u00c5\u00e5\u0394_\u03a6\u0393\u039b\u03a9\u03a0\u03a8\u03a3\u0398\u039e"
            + "\033\u00c6\u00e6\u00df\u00c9 !\"#\u00a4%&'()*+,-./0123456789:;<=>?\u00a1ABCDEFGHIJKLMNOPQRSTUVWXYZ\u00c4\u00d6\u00d1\u00dc\u00a7\u00bf"
            + "abcdefghijklmnopqrstuvwxyz\u00e4\u00f6\u00f1\u00fc\u00e0";
    private static final String GSM7_EXT = "\f^{}\\[~]|\u20ac";

    private final boolean cbs;
    private final boolean server;
    private final String scheme;
    /** client mode: "sms://+number[:port]" (the address of new messages), server mode: null. */
    private final String clientUrl;
    private final boolean hasPort;
    private final int handle;
    private boolean closed;
    private MessageListener listener;
    private Thread listenerThread;

    Conn(boolean cbs, String clientUrl, boolean hasPort, int handle) {
        this.cbs = cbs;
        this.scheme = cbs ? "cbs" : "sms";
        this.server = clientUrl == null;
        this.clientUrl = clientUrl;
        this.hasPort = hasPort;
        this.handle = handle;
    }

    /** Parses the part of the URL after the scheme (Connector.open). */
    public static Conn open(boolean cbs, String rest) throws IOException {
        String scheme = cbs ? "cbs" : "sms";
        if (!rest.startsWith("//")) {
            throw new IllegalArgumentException("Missing '//' in " + scheme + " URL");
        }
        String t = rest.substring(2);
        int semi = t.indexOf(';');
        if (semi >= 0) {
            t = t.substring(0, semi);
        }
        int colon = t.indexOf(':');
        String number = colon < 0 ? t : t.substring(0, colon);
        int port = -1;
        if (colon >= 0) {
            port = parsePort(t.substring(colon + 1));
        }
        if (number.length() == 0) {
            if (port < 0) {
                throw new IllegalArgumentException("No address and no port in " + scheme + " URL");
            }
            int h = Sms.bind0(cbs ? 1 : 0, port);
            if (h == -1) {
                throw new SecurityException("Receiving " + scheme + " is not permitted by the device profile");
            }
            if (h == -2) {
                throw new IOException("Messaging is offline: " + scheme);
            }
            if (h < 0) {
                throw new IOException("Port " + port + " is already in use");
            }
            return new Conn(cbs, null, true, h);
        }
        if (cbs) {
            throw new IllegalArgumentException("cbs has only the server mode (cbs://:channel)");
        }
        checkNumber(number);
        return new Conn(false, "sms://" + t, port >= 0, 0);
    }

    private static int parsePort(String s) {
        if (s.length() == 0 || s.length() > 5) {
            throw new IllegalArgumentException("Bad port: " + s);
        }
        int v = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                throw new IllegalArgumentException("Bad port: " + s);
            }
            v = v * 10 + (c - '0');
        }
        if (v > 65535) {
            throw new IllegalArgumentException("Bad port: " + s);
        }
        return v;
    }

    private static void checkNumber(String n) {
        for (int i = 0; i < n.length(); i++) {
            char c = n.charAt(i);
            if (!((c >= '0' && c <= '9') || (i == 0 && c == '+'))) {
                throw new IllegalArgumentException("Bad address: " + n);
            }
        }
        if (n.length() == 1 && n.charAt(0) == '+') {
            throw new IllegalArgumentException("Bad address: " + n);
        }
    }

    /** Validates a message address "sms://+number[:port]"; returns true when it has a port. */
    private boolean checkAddress(String a) {
        if (a == null || !a.startsWith("sms://")) {
            throw new IllegalArgumentException("Message address must be an sms:// URL: " + a);
        }
        String t = a.substring(6);
        int colon = t.indexOf(':');
        String number = colon < 0 ? t : t.substring(0, colon);
        if (number.length() == 0) {
            throw new IllegalArgumentException("Message address has no number: " + a);
        }
        checkNumber(number);
        if (colon >= 0) {
            parsePort(t.substring(colon + 1));
        }
        return colon >= 0;
    }

    private void checkOpen() throws IOException {
        if (closed) {
            throw new IOException("Connection closed");
        }
    }

    public Message newMessage(String type) {
        return newMessage(type, clientUrl);
    }

    public Message newMessage(String type, String address) {
        if (TEXT_MESSAGE.equals(type)) {
            return new SmsText(address, null);
        }
        if (BINARY_MESSAGE.equals(type)) {
            return new SmsBinary(address, null);
        }
        throw new IllegalArgumentException("Unknown message type: " + type);
    }

    /** Number of 7-bit units of the text, or -1 when a character is not in the GSM 7-bit alphabet (basic table + extension table, 2 units). */
    private static int gsmUnits(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (GSM7.indexOf(c) >= 0 && c != '\033') {
                n++;
            } else if (GSM7_EXT.indexOf(c) >= 0) {
                n += 2;
            } else {
                return -1;
            }
        }
        return n;
    }

    private static int segs(int units, int single, int multi) {
        if (units <= single) {
            return 1;
        }
        return (units + multi - 1) / multi;
    }

    public int numberOfSegments(Message msg) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        int n;
        if (msg instanceof SmsText) {
            String t = ((SmsText) msg).getPayloadText();
            if (t == null) {
                t = "";
            }
            int u = gsmUnits(t);
            n = u >= 0 ? segs(u, 160, 153) : segs(t.length(), 70, 67);
        } else if (msg instanceof SmsBinary) {
            byte[] d = ((SmsBinary) msg).getPayloadData();
            int len = d == null ? 0 : d.length;
            boolean port = msg.getAddress() != null && msg.getAddress().indexOf(':', 6) >= 0;
            n = port ? segs(len, 133, 128) : segs(len, 140, 134);
        } else {
            return 0;
        }
        return n > MAX_SEGMENTS ? 0 : n;
    }

    public void send(Message msg) throws IOException, InterruptedIOException {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        checkOpen();
        if (cbs) {
            throw new IOException("A cbs connection cannot send");
        }
        if (!(msg instanceof SmsMessage)) {
            throw new IllegalArgumentException("Not a message of this connection");
        }
        checkAddress(msg.getAddress());
        int segments = numberOfSegments(msg);
        if (segments == 0) {
            throw new IllegalArgumentException("Message payload too long");
        }
        int r;
        if (msg instanceof SmsText) {
            String t = ((SmsText) msg).getPayloadText();
            r = Sms.sendText0(msg.getAddress(), t == null ? "" : t, segments);
        } else {
            byte[] d = ((SmsBinary) msg).getPayloadData();
            r = Sms.sendBinary0(msg.getAddress(), d == null ? new byte[0] : d, segments);
        }
        if (r == -1) {
            throw new SecurityException("Sending messages is not permitted by the device profile");
        }
        if (r < 0) {
            throw new IOException("Messaging is offline");
        }
    }

    public Message receive() throws IOException, InterruptedIOException {
        checkOpen();
        if (!server) {
            throw new IOException("A client mode connection cannot receive");
        }
        for (;;) {
            if (Sms.await0(handle) == 0) {
                if (closed) {
                    throw new InterruptedIOException("Connection closed");
                }
                throw new IOException("Messaging is offline");
            }
            int type = Sms.take0(handle);
            if (type == 0) {
                continue;
            }
            Date when = new Date(Sms.curTime0());
            String from = Sms.curAddress0();
            if (type == 1) {
                SmsText m = new SmsText(from, when);
                m.setPayloadText(Sms.curText0());
                return m;
            }
            SmsBinary m = new SmsBinary(from, when);
            m.setPayloadData(Sms.curData0());
            return m;
        }
    }

    public void setMessageListener(MessageListener l) throws IOException {
        checkOpen();
        if (!server) {
            throw new IOException("A client mode connection cannot receive");
        }
        synchronized (this) {
            listener = l;
            if (l != null && listenerThread == null) {
                listenerThread = new Thread(this);
                Sms.listening0(handle, true);
                listenerThread.start();
            } else if (l == null && listenerThread != null) {
                listenerThread = null;
                Sms.listening0(handle, false);
            }
        }
    }

    /** The listener thread: notifies once for every message that arrived (also those queued before the listener was set). */
    public void run() {
        for (;;) {
            if (Sms.awaitNew0(handle) == 0) {
                return;
            }
            MessageListener l;
            synchronized (this) {
                if (listenerThread != Thread.currentThread()) {
                    return;
                }
                l = listener;
            }
            if (l != null) {
                try {
                    l.notifyIncomingMessage(this);
                } catch (Throwable t) {
                    System.err.println("sms listener: " + t);
                }
            }
        }
    }

    public void close() throws IOException {
        synchronized (this) {
            if (closed) {
                return;
            }
            closed = true;
            listener = null;
            listenerThread = null;
        }
        if (server) {
            Sms.unbind0(handle);
        }
    }
}

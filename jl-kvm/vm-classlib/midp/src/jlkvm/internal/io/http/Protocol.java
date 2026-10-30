/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package jlkvm.internal.io.http;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connection;
import javax.microedition.io.HttpConnection;
import jlkvm.internal.Net;
import jlkvm.internal.io.ConnectionBase;

/**
 * Handler of the scheme "http" (MIDP 2.0 HttpConnection). Reached only when the profile's network policy for "http" is "allow"
 * (Connector). States as in the javadoc: Setup (method, request properties and the output stream may change), Connected (after the first
 * call that needs the response: the request is sent with everything collected), Closed. The request body is the content of the
 * output stream; the response is held by the host and read through {@link Net}. Redirects are NOT followed (the application sees 3xx).
 * Only GET, HEAD and POST exist.
 */
public class Protocol implements ConnectionBase, HttpConnection {
    private static final int SETUP = 0, CONNECTED = 1, CLOSED = 2;
    private static final int TIMEOUT_MS = 30000;

    private String url;
    private String host;
    private int port;
    private String file;
    private String query;
    private String ref;
    private int mode;
    private int state;
    private String method = GET;
    private final Vector reqKeys = new Vector();
    private final Vector reqValues = new Vector();
    private ByteArrayOutputStream body;
    private boolean inOpened;
    private int handle;
    /** open parts of the response: the connection itself and an input stream. */
    private int users;

    public Connection openPrim(String name, int mode, boolean timeouts) throws IOException {
        Protocol p = new Protocol();
        p.parse(name);
        p.mode = mode;
        return p;
    }

    private void parse(String name) {
        url = "http:" + name;
        if (!name.startsWith("//")) {
            throw new IllegalArgumentException("Missing host in URL: " + url);
        }
        int hash = name.indexOf('#');
        if (hash >= 0) {
            ref = name.substring(hash + 1);
            name = name.substring(0, hash);
        }
        int q = name.indexOf('?');
        if (q >= 0) {
            query = name.substring(q + 1);
            name = name.substring(0, q);
        }
        int slash = name.indexOf('/', 2);
        String auth = slash < 0 ? name.substring(2) : name.substring(2, slash);
        if (slash >= 0) {
            file = name.substring(slash);
        }
        int at = auth.lastIndexOf('@');
        if (at >= 0) {
            auth = auth.substring(at + 1);
        }
        port = 80;
        int colon = auth.lastIndexOf(':');
        if (colon >= 0 && auth.indexOf(']', colon) < 0) {
            String ps = auth.substring(colon + 1);
            auth = auth.substring(0, colon);
            if (ps.length() > 0) {
                try {
                    port = Integer.parseInt(ps);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Bad port in URL: " + url);
                }
                if (port < 1 || port > 65535) {
                    throw new IllegalArgumentException("Bad port in URL: " + url);
                }
            }
        }
        if (auth.length() == 0) {
            throw new IllegalArgumentException("Missing host in URL: " + url);
        }
        host = auth;
    }

    // ---------------------------------------------------------------- URL parts

    public String getURL() {
        return url;
    }

    public String getProtocol() {
        return "http";
    }

    public String getHost() {
        return host;
    }

    public String getFile() {
        return file;
    }

    public String getRef() {
        return ref;
    }

    public String getQuery() {
        return query;
    }

    public int getPort() {
        return port;
    }

    // ---------------------------------------------------------------- request

    private void checkNotClosed() throws IOException {
        if (state == CLOSED) {
            throw new IOException("connection is closed");
        }
    }

    private void checkSetup() throws IOException {
        checkNotClosed();
        if (state != SETUP) {
            throw new IOException("connection already established");
        }
    }

    public String getRequestMethod() {
        return method;
    }

    public void setRequestMethod(String m) throws IOException {
        checkSetup();
        if (m == null || !(m.equals(GET) || m.equals(POST) || m.equals(HEAD))) {
            throw new IOException("unsupported request method: " + m);
        }
        method = m;
    }

    private int find(String key) {
        for (int i = 0; i < reqKeys.size(); i++) {
            if (((String) reqKeys.elementAt(i)).equalsIgnoreCase(key)) {
                return i;
            }
        }
        return -1;
    }

    public String getRequestProperty(String key) {
        int i = key == null ? -1 : find(key);
        return i < 0 ? null : (String) reqValues.elementAt(i);
    }

    public void setRequestProperty(String key, String value) throws IOException {
        checkSetup();
        if (key == null) {
            throw new NullPointerException("key");
        }
        int i = find(key);
        if (value == null) {
            if (i >= 0) {
                reqKeys.removeElementAt(i);
                reqValues.removeElementAt(i);
            }
        } else if (i >= 0) {
            reqValues.setElementAt(value, i);
        } else {
            reqKeys.addElement(key);
            reqValues.addElement(value);
        }
    }

    public OutputStream openOutputStream() throws IOException {
        checkNotClosed();
        if (mode == javax.microedition.io.Connector.READ) {
            throw new IOException("connection is read-only");
        }
        if (state != SETUP) {
            throw new IOException("connection already established");
        }
        if (body != null) {
            throw new IOException("output stream already open");
        }
        body = new ByteArrayOutputStream();
        return new Out();
    }

    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(openOutputStream());
    }

    /** The collected request body; writing after the request was sent is an error. */
    private final class Out extends OutputStream {
        private boolean closed;

        public void write(int b) throws IOException {
            if (closed || state != SETUP) {
                throw new IOException("stream closed");
            }
            body.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            if (closed || state != SETUP) {
                throw new IOException("stream closed");
            }
            body.write(b, off, len);
        }

        public void close() {
            closed = true;
        }
    }

    /** Sends the request (once) and keeps the response in the host. */
    private void connect() throws IOException {
        checkNotClosed();
        if (state == CONNECTED) {
            return;
        }
        String m = method;
        byte[] data = body == null ? new byte[0] : body.toByteArray();
        if (data.length > 0 && m.equals(GET)) {
            m = POST;   // like HttpURLConnection: a GET with a body is a POST
        }
        StringBuffer target = new StringBuffer("http://").append(host);
        if (port != 80) {
            target.append(':').append(port);
        }
        target.append(file == null ? "/" : file);
        if (query != null) {
            target.append('?').append(query);
        }
        boolean ua = find("user-agent") >= 0;
        String[] hs = new String[(reqKeys.size() + (ua ? 0 : 1)) * 2];
        int k = 0;
        for (int i = 0; i < reqKeys.size(); i++) {
            hs[k++] = (String) reqKeys.elementAt(i);
            hs[k++] = (String) reqValues.elementAt(i);
        }
        if (!ua) {
            hs[k++] = "User-Agent";
            hs[k++] = "Profile/" + System.getProperty("microedition.profiles") + " Configuration/" + System.getProperty("microedition.configuration");
        }
        int h = Net.http0(m, target.toString(), hs, data, data.length, TIMEOUT_MS);
        if (h < 0) {
            String why = Net.error0();
            if (h == -2) {
                throw new SecurityException("network access denied: http");
            }
            throw new IOException(why == null ? "http request failed" : why);
        }
        handle = h;
        users = 1;
        state = CONNECTED;
    }

    // ---------------------------------------------------------------- response

    public int getResponseCode() throws IOException {
        connect();
        return Net.status0(handle);
    }

    public String getResponseMessage() throws IOException {
        connect();
        String s = Net.message0(handle);
        return s;
    }

    public String getHeaderField(String name) throws IOException {
        connect();
        if (name == null) {
            return null;
        }
        int n = Net.headerCount0(handle);
        for (int i = 0; i < n; i++) {
            if (name.equalsIgnoreCase(Net.headerKey0(handle, i))) {
                return Net.headerValue0(handle, i);
            }
        }
        return null;
    }

    public String getHeaderField(int index) throws IOException {
        connect();
        if (index < 0 || index >= Net.headerCount0(handle)) {
            return null;
        }
        return Net.headerValue0(handle, index);
    }

    public String getHeaderFieldKey(int index) throws IOException {
        connect();
        if (index < 0 || index >= Net.headerCount0(handle)) {
            return null;
        }
        return Net.headerKey0(handle, index);
    }

    public int getHeaderFieldInt(String name, int def) throws IOException {
        String v = getHeaderField(name);
        if (v == null) {
            return def;
        }
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public long getHeaderFieldDate(String name, long def) throws IOException {
        String v = getHeaderField(name);
        long t = HttpDate.parse(v);
        return t < 0 ? def : t;
    }

    public long getDate() throws IOException {
        return getHeaderFieldDate("date", 0);
    }

    public long getExpiration() throws IOException {
        return getHeaderFieldDate("expires", 0);
    }

    public long getLastModified() throws IOException {
        return getHeaderFieldDate("last-modified", 0);
    }

    public String getType() {
        try {
            return getHeaderField("content-type");
        } catch (IOException e) {
            return null;
        }
    }

    public String getEncoding() {
        try {
            return getHeaderField("content-encoding");
        } catch (IOException e) {
            return null;
        }
    }

    public long getLength() {
        try {
            connect();
            String v = getHeaderField("content-length");
            if (v != null) {
                try {
                    return Long.parseLong(v.trim());
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
            return -1;
        } catch (IOException e) {
            return -1;
        }
    }

    public InputStream openInputStream() throws IOException {
        checkNotClosed();
        if (mode == javax.microedition.io.Connector.WRITE) {
            throw new IOException("connection is write-only");
        }
        if (inOpened) {
            throw new IOException("input stream already open");
        }
        connect();
        inOpened = true;
        users++;
        return new In();
    }

    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(openInputStream());
    }

    /** Reads the body kept by the host. */
    private final class In extends InputStream {
        private int pos;
        private boolean closed;
        private final byte[] one = new byte[1];

        public int read() throws IOException {
            int n = read(one, 0, 1);
            return n < 0 ? -1 : one[0] & 0xFF;
        }

        public int read(byte[] b, int off, int len) throws IOException {
            if (closed) {
                throw new IOException("stream closed");
            }
            if (b == null) {
                throw new NullPointerException("b");
            }
            if (off < 0 || len < 0 || off > b.length - len) {
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return 0;
            }
            int n = Net.read0(handle, pos, b, off, len);
            if (n <= 0) {
                return -1;
            }
            pos += n;
            return n;
        }

        public int available() throws IOException {
            if (closed) {
                throw new IOException("stream closed");
            }
            return Net.length0(handle) - pos;
        }

        public void close() {
            if (!closed) {
                closed = true;
                release();
            }
        }
    }

    private void release() {
        if (--users == 0 && handle != 0) {
            Net.free0(handle);
            handle = 0;
        }
    }

    public void close() throws IOException {
        if (state == CLOSED) {
            return;
        }
        boolean wasConnected = state == CONNECTED;
        state = CLOSED;
        if (wasConnected) {
            release();
        }
    }
}

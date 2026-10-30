package net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.MIDlet;

/**
 * T26b host test: Connector.open of the GCF schemes under the network policy of the profile (offline -> IOException, deny ->
 * SecurityException, allow -> the HTTP handler talks to the server whose URL is the JAD attribute Test-Url; schemes without a handler ->
 * ConnectionNotFoundException).
 */
public class NetMIDlet extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    static String read(InputStream in) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int c; (c = in.read()) >= 0;) sb.append((char) c);
        return sb.toString();
    }

    protected void startApp() {
        try {
            String base = getAppProperty("Test-Url");
            String[] others = {"https://127.0.0.1:9/", "socket://127.0.0.1:9", "serversocket://:9", "datagram://127.0.0.1:9", "ssl://127.0.0.1:9", "comm:COM0", "mms://+1234567"};
            try {
                Connection c = Connector.open(base + "/hello?x=1#frag");
                p("http open: ok " + (c instanceof HttpConnection));
                http(base, (HttpConnection) c);
            } catch (Throwable t) {
                p("http open: " + cn(t));
            }
            for (int i = 0; i < others.length; i++) {
                try {
                    Connector.open(others[i]).close();
                    p(others[i] + ": opened");
                } catch (Throwable t) {
                    p(others[i] + ": " + cn(t));
                }
            }
            try { Connector.open("http:nohost"); p("bad url: opened"); } catch (Throwable t) { p("bad url: " + cn(t)); }
            try { Connector.open("http://:80/x"); p("empty host: opened"); } catch (Throwable t) { p("empty host: " + cn(t)); }
            try { Connector.open("http://h:99999/x"); p("bad port: opened"); } catch (Throwable t) { p("bad port: " + cn(t)); }
        } catch (Throwable t) {
            p("FATAL " + t);
        }
        notifyDestroyed();
    }

    static void http(String base, HttpConnection c) throws Exception {
        p("parts: protocol=" + c.getProtocol() + " host=" + c.getHost() + " port>0=" + (c.getPort() > 0) + " file=" + c.getFile() + " query=" + c.getQuery() + " ref=" + c.getRef());
        p("url ends: " + c.getURL().endsWith("/hello?x=1#frag") + " method=" + c.getRequestMethod());
        c.setRequestProperty("X-Mine", "value1");
        c.setRequestProperty("x-mine", "value2");
        p("request property: " + c.getRequestProperty("X-MINE") + " absent=" + c.getRequestProperty("nothing"));
        try { c.setRequestMethod("PUT"); p("PUT: accepted"); } catch (Throwable t) { p("PUT: " + cn(t)); }
        p("code=" + c.getResponseCode() + " message=" + c.getResponseMessage());
        p("type=" + c.getType() + " length=" + c.getLength() + " encoding=" + c.getEncoding());
        p("X-Test=" + c.getHeaderField("x-test") + " X-Num=" + c.getHeaderFieldInt("X-Num", -1) + " bad int=" + c.getHeaderFieldInt("X-Test", -7) + " absent=" + c.getHeaderField("absent"));
        p("lastModified=" + c.getLastModified() + " date>0=" + (c.getDate() > 0) + " expires=" + c.getExpiration() + " dateField=" + c.getHeaderFieldDate("Last-Modified", 5) + " default=" + c.getHeaderFieldDate("nothing", 5));
        boolean found = false;
        for (int i = 0; c.getHeaderFieldKey(i) != null; i++) if (c.getHeaderFieldKey(i).equalsIgnoreCase("X-Test") && c.getHeaderField(i).equals("abc")) found = true;
        p("headers by index: " + found + " beyond end=" + c.getHeaderField(500) + "/" + c.getHeaderFieldKey(500));
        try { c.setRequestProperty("a", "b"); p("set after connect: allowed"); } catch (Throwable t) { p("set after connect: " + cn(t)); }
        try { c.setRequestMethod("GET"); p("method after connect: allowed"); } catch (Throwable t) { p("method after connect: " + cn(t)); }
        try { c.openOutputStream(); p("output after connect: allowed"); } catch (Throwable t) { p("output after connect: " + cn(t)); }
        DataInputStream in = c.openDataInputStream();
        try { c.openInputStream(); p("second input: allowed"); } catch (Throwable t) { p("second input: " + cn(t)); }
        c.close();
        p("body after connection close: " + read(in) + " available=" + in.available());
        in.close();
        try { c.getResponseCode(); p("closed connection: usable"); } catch (Throwable t) { p("closed connection: " + cn(t)); }

        HttpConnection nf = (HttpConnection) Connector.open(base + "/missing");
        p("404: code=" + nf.getResponseCode() + " message=" + nf.getResponseMessage() + " body=" + read(nf.openInputStream()) + " (no exception)");
        nf.close();
        HttpConnection rd = (HttpConnection) Connector.open(base + "/redir");
        p("redirect not followed: code=" + rd.getResponseCode() + " location=" + rd.getHeaderField("Location"));
        rd.close();
        HttpConnection hd = (HttpConnection) Connector.open(base + "/hello");
        hd.setRequestMethod(HttpConnection.HEAD);
        p("HEAD: code=" + hd.getResponseCode() + " length=" + hd.getLength() + " body=" + read(hd.openInputStream()).length());
        hd.close();
        HttpConnection po = (HttpConnection) Connector.open(base + "/echo", Connector.READ_WRITE);
        po.setRequestMethod(HttpConnection.POST);
        po.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStream out = po.openOutputStream();
        out.write("a=1&b=2".getBytes());
        out.close();
        p("POST: code=" + po.getResponseCode() + " body=" + read(po.openInputStream()));
        po.close();
        HttpConnection gb = (HttpConnection) Connector.open(base + "/echo");
        gb.openOutputStream().write("zz".getBytes());
        p("GET with a body becomes POST: " + read(gb.openInputStream()));
        gb.close();
        HttpConnection ro = (HttpConnection) Connector.open(base + "/hello", Connector.READ);
        try { ro.openOutputStream(); p("READ-only output: allowed"); } catch (Throwable t) { p("READ-only output: " + cn(t)); }
        ro.close();
        try { Connector.openInputStream(base + "/hello"); p("openInputStream: ok"); } catch (Throwable t) { p("openInputStream: " + cn(t)); }
        HttpConnection dead = (HttpConnection) Connector.open("http://127.0.0.1:1/");
        try { dead.getResponseCode(); p("connection refused: code"); } catch (Throwable t) { p("connection refused: " + cn(t)); }
        dead.close();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

package jlkvm.host.desktop;

import static org.junit.Assert.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * T26b: Connector + HttpConnection under the three network policies of the profile. offline -> IOException, deny -> SecurityException
 * (before any handler or socket is touched), allow -> the HTTP handler of the guest talks to a real HTTP server on the loopback interface
 * (status line, header fields, bodies, POST, HEAD, no redirect following); a scheme without handler -> ConnectionNotFoundException.
 */
public class NetHostTest {
    @Rule public TemporaryFolder tmp = new TemporaryFolder();

    private File profile(String network) throws Exception {
        String base = new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "profiles/jlkvm-min.json").toPath()), "UTF-8");
        String json = base.replaceAll("(?s)\"network\"\\s*:\\s*\\{.*?\\}", "\"network\": " + network);
        assertNotEquals(base, json);
        File f = tmp.newFile();
        Files.write(f.toPath(), json.getBytes("UTF-8"));
        return f;
    }

    private HeadlessRunner.Result run(String network, String url) throws Exception {
        File jar = DesktopTestSupport.midlet("net");
        File jad = File.createTempFile("net", ".jad", tmp.getRoot());
        String text = "MIDlet-Name: Net\r\nMIDlet-Version: 1.0.0\r\nMIDlet-Vendor: jl-net\r\nMIDlet-Jar-URL: net.jar\r\nMIDlet-Jar-Size: " + jar.length()
            + "\r\nTest-Url: " + url + "\r\n";
        Files.write(jad.toPath(), text.getBytes("UTF-8"));
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = jar;
        o.jad = jad;
        o.profile = profile(network);
        o.realtime = true;   // the request runs on a worker thread: wall clock
        o.maxWallMs = 60000;
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        assertEquals(r.stdout, HeadlessRunner.Result.EXITED, r.exitKind);
        assertEquals(r.host.guestErr.toString(), 0, r.host.guestErr.length());
        return r;
    }

    private static String others(String cls, String comm) {
        return "> https://127.0.0.1:9/: " + cls + "\n> socket://127.0.0.1:9: " + cls + "\n> serversocket://:9: " + cls + "\n> datagram://127.0.0.1:9: " + cls
            + "\n> ssl://127.0.0.1:9: " + cls + "\n> comm:COM0: " + comm + "\n> mms://+1234567: " + cls + "\n";
    }

    @Test public void offlineGivesIOException() throws Exception {
        String out = DesktopTestSupport.onlyGuestLines(run("{\"default\": \"offline\"}", "http://127.0.0.1:1").stdout);
        assertEquals("> http open: IOException\n" + others("IOException", "ConnectionNotFoundException")
            + "> bad url: IOException\n> empty host: IOException\n> bad port: IOException\n", out);
    }

    @Test public void denyGivesSecurityException() throws Exception {
        String out = DesktopTestSupport.onlyGuestLines(run("{\"default\": \"deny\"}", "http://127.0.0.1:1").stdout);
        assertEquals("> http open: SecurityException\n" + others("SecurityException", "ConnectionNotFoundException")
            + "> bad url: SecurityException\n> empty host: SecurityException\n> bad port: SecurityException\n", out);
    }

    @Test public void deniedHostNeverOpensASocket() throws Exception {
        // a server that records connections: with deny / offline nobody connects
        final int[] hits = {0};
        HttpServer s = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        s.createContext("/", new com.sun.net.httpserver.HttpHandler() { public void handle(HttpExchange e) throws java.io.IOException { hits[0]++; e.sendResponseHeaders(200, -1); e.close(); } });
        s.start();
        try {
            String url = "http://127.0.0.1:" + s.getAddress().getPort();
            run("{\"default\": \"deny\"}", url);
            run("{\"default\": \"offline\", \"http\": \"offline\"}", url);
            assertEquals(0, hits[0]);
        } finally {
            s.stop(0);
        }
    }

    @Test public void allowTalksHttpToALocalServer() throws Exception {
        HttpServer s = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        s.createContext("/hello", new com.sun.net.httpserver.HttpHandler() { public void handle(HttpExchange e) throws java.io.IOException {
            byte[] b = "hello world".getBytes("UTF-8");
            e.getResponseHeaders().add("Content-Type", "text/plain");
            e.getResponseHeaders().add("X-Test", "abc");
            e.getResponseHeaders().add("X-Num", "42");
            e.getResponseHeaders().add("Last-Modified", "Sun, 06 Nov 1994 08:49:37 GMT");
            if (e.getRequestMethod().equals("HEAD")) { e.getResponseHeaders().add("Content-Length", "11"); e.sendResponseHeaders(200, -1); }
            else { e.sendResponseHeaders(200, b.length); e.getResponseBody().write(b); }
            e.close(); } });
        s.createContext("/missing", new com.sun.net.httpserver.HttpHandler() { public void handle(HttpExchange e) throws java.io.IOException {
            byte[] b = "nope".getBytes("UTF-8");
            e.sendResponseHeaders(404, b.length); e.getResponseBody().write(b); e.close(); } });
        s.createContext("/redir", new com.sun.net.httpserver.HttpHandler() { public void handle(HttpExchange e) throws java.io.IOException {
            e.getResponseHeaders().add("Location", "/hello"); e.sendResponseHeaders(302, -1); e.close(); } });
        s.createContext("/echo", new com.sun.net.httpserver.HttpHandler() { public void handle(HttpExchange e) throws java.io.IOException {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            InputStream in = e.getRequestBody();
            for (int n; (n = in.read()) >= 0;) bo.write(n);
            String ua = e.getRequestHeaders().getFirst("User-Agent");
            String ct = e.getRequestHeaders().getFirst("Content-Type");
            byte[] b = (e.getRequestMethod() + ":" + bo.toString("UTF-8") + ":" + ct + ":" + (ua != null && ua.startsWith("Profile/MIDP-2.0 Configuration/CLDC-1.1"))).getBytes("UTF-8");
            e.sendResponseHeaders(200, b.length); e.getResponseBody().write(b); e.close(); } });
        s.start();
        try {
            String url = "http://127.0.0.1:" + s.getAddress().getPort();
            HeadlessRunner.Result r = run("{\"default\": \"offline\", \"http\": \"allow\", \"https\": \"allow\", \"socket\": \"allow\", \"serversocket\": \"allow\", \"datagram\": \"allow\", \"ssl\": \"allow\", \"mms\": \"allow\"}", url);
            String out = DesktopTestSupport.onlyGuestLines(r.stdout);
            assertEquals("> http open: ok true\n"
                + "> parts: protocol=http host=127.0.0.1 port>0=true file=/hello query=x=1 ref=frag\n"
                + "> url ends: true method=GET\n"
                + "> request property: value2 absent=null\n"
                + "> PUT: IOException\n"
                + "> code=200 message=OK\n"
                + "> type=text/plain length=11 encoding=null\n"
                + "> X-Test=abc X-Num=42 bad int=-7 absent=null\n"
                + "> lastModified=784111777000 date>0=true expires=0 dateField=784111777000 default=5\n"
                + "> headers by index: true beyond end=null/null\n"
                + "> set after connect: IOException\n"
                + "> method after connect: IOException\n"
                + "> output after connect: IOException\n"
                + "> second input: IOException\n"
                + "> body after connection close: hello world available=0\n"
                + "> closed connection: IOException\n"
                + "> 404: code=404 message=Not Found body=nope (no exception)\n"
                + "> redirect not followed: code=302 location=/hello\n"
                + "> HEAD: code=200 length=11 body=0\n"
                + "> POST: code=200 body=POST:a=1&b=2:application/x-www-form-urlencoded:true\n"
                + "> GET with a body becomes POST: POST:zz:application/x-www-form-urlencoded:true\n"
                + "> READ-only output: IOException\n"
                + "> openInputStream: ok\n"
                + "> connection refused: IOException\n"
                + others("ConnectionNotFoundException", "ConnectionNotFoundException")
                + "> bad url: IllegalArgumentException\n> empty host: IllegalArgumentException\n> bad port: IllegalArgumentException\n", out);
        } finally {
            s.stop(0);
        }
    }
}

package jlkvm.host.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** {@link HttpTransport} over {@code java.net.HttpURLConnection} (desktop and Android). Responses are limited to {@link #MAX_BODY} bytes. */
public final class JavaHttpTransport implements HttpTransport {
    public static final int MAX_BODY = 16 * 1024 * 1024;

    public Response execute(String method, String url, List<String[]> headers, byte[] body, int timeoutMs) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        try {
            c.setInstanceFollowRedirects(false);
            c.setConnectTimeout(timeoutMs);
            c.setReadTimeout(timeoutMs);
            c.setRequestMethod(method);
            c.setUseCaches(false);
            for (String[] h : headers) c.setRequestProperty(h[0], h[1]);
            if (body.length > 0) {
                c.setDoOutput(true);
                c.setFixedLengthStreamingMode(body.length);
                OutputStream o = c.getOutputStream();
                o.write(body);
                o.close();
            }
            int status = c.getResponseCode();
            List<String[]> hs = new ArrayList<String[]>();
            for (int i = 1; ; i++) {
                String k = c.getHeaderFieldKey(i), v = c.getHeaderField(i);
                if (k == null && v == null) break;
                if (k != null) hs.add(new String[] {k, v == null ? "" : v});
            }
            InputStream in = status >= 400 ? c.getErrorStream() : c.getInputStream();
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            if (in != null && !method.equals("HEAD")) {
                byte[] b = new byte[8192];
                for (int n; (n = in.read(b)) > 0;) {
                    bo.write(b, 0, n);
                    if (bo.size() > MAX_BODY) throw new IOException("response larger than " + MAX_BODY + " bytes");
                }
                in.close();
            }
            return new Response(status, c.getResponseMessage(), hs, bo.toByteArray());
        } finally {
            c.disconnect();
        }
    }
}

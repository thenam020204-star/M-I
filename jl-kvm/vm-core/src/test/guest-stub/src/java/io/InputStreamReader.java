package java.io;

public class InputStreamReader extends Reader {
    private final InputStream in;
    private final boolean utf8;
    public InputStreamReader(InputStream in) { this.in = in; utf8 = "UTF-8".equals(System.getProperty("microedition.encoding")); }
    public InputStreamReader(InputStream in, String enc) throws UnsupportedEncodingException {
        if (enc == null) throw new NullPointerException();
        boolean u = enc.equals("UTF-8") || enc.equals("UTF8");
        if (!u && !enc.equals("ISO-8859-1") && !enc.equals("ISO8859_1") && !enc.equals("ISO8859-1")) throw new UnsupportedEncodingException(enc);
        this.in = in;
        utf8 = u;
    }
    public int read() throws IOException {
        int c = in.read();
        if (c < 0x80 || !utf8) return c;
        int need = c >= 0xE0 ? 2 : 1;
        int v = need == 2 ? c & 0x0F : c & 0x1F;
        for (int i = 0; i < need; i++) {
            int d = in.read();
            if (d == -1) return 0xFFFD;
            v = (v << 6) | (d & 0x3F);
        }
        return v;
    }
    public int read(char[] c, int off, int len) throws IOException {
        int i = 0;
        while (i < len) {
            int ch = read();
            if (ch == -1) break;
            c[off + i++] = (char) ch;
        }
        return i == 0 && len > 0 ? -1 : i;
    }
    public void close() throws IOException { in.close(); }
}

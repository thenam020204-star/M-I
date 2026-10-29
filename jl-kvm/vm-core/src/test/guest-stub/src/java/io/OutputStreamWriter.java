package java.io;

public class OutputStreamWriter extends Writer {
    private final OutputStream out;
    private final String enc;
    public OutputStreamWriter(OutputStream out) { this.out = out; enc = System.getProperty("microedition.encoding"); }
    public OutputStreamWriter(OutputStream out, String enc) throws UnsupportedEncodingException {
        if (enc == null) throw new NullPointerException();
        this.out = out;
        this.enc = enc;
        "".getBytes(enc);
    }
    public void write(char[] c, int off, int len) throws IOException {
        byte[] b = new String(c, off, len).getBytes();
        try { b = new String(c, off, len).getBytes(enc); } catch (UnsupportedEncodingException e) { }
        out.write(b, 0, b.length);
    }
    public void flush() throws IOException { out.flush(); }
    public void close() throws IOException { out.close(); }
}

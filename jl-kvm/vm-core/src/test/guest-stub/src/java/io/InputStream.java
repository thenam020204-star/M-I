package java.io;

public abstract class InputStream {
    public InputStream() { }
    public abstract int read() throws IOException;
    public int read(byte[] b) throws IOException { return read(b, 0, b.length); }
    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0) return 0;
        int c = read();
        if (c == -1) return -1;
        b[off] = (byte) c;
        int i = 1;
        while (i < len) {
            c = read();
            if (c == -1) break;
            b[off + i] = (byte) c;
            i++;
        }
        return i;
    }
    public long skip(long n) throws IOException { long k = 0; while (k < n && read() != -1) k++; return k; }
    public int available() throws IOException { return 0; }
    public void close() throws IOException { }
}

package java.io;

public class ByteArrayOutputStream extends OutputStream {
    protected byte[] buf = new byte[32];
    protected int count;
    public ByteArrayOutputStream() { }
    public synchronized void write(int b) {
        if (count == buf.length) { byte[] n = new byte[count * 2]; System.arraycopy(buf, 0, n, 0, count); buf = n; }
        buf[count++] = (byte) b;
    }
    public synchronized void write(byte[] b, int off, int len) { for (int i = 0; i < len; i++) write(b[off + i]); }
    public synchronized void reset() { count = 0; }
    public synchronized byte[] toByteArray() { byte[] r = new byte[count]; System.arraycopy(buf, 0, r, 0, count); return r; }
    public int size() { return count; }
    public String toString() { return new String(buf, 0, count); }
}

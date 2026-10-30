/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.io;

public class ByteArrayOutputStream extends OutputStream {
    protected byte[] buf;
    protected int count;

    public ByteArrayOutputStream() {
        this(32);
    }

    public ByteArrayOutputStream(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        buf = new byte[size];
    }

    public synchronized void write(int b) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            grow(newcount);
        }
        buf[count] = (byte) b;
        count = newcount;
    }

    public synchronized void write(byte[] b, int off, int len) {
        if (off < 0 || len < 0 || off > b.length - len) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return;
        }
        int newcount = count + len;
        if (newcount > buf.length) {
            grow(newcount);
        }
        System.arraycopy(b, off, buf, count, len);
        count = newcount;
    }

    private void grow(int min) {
        byte[] nb = new byte[Math.max(buf.length * 2, min)];
        System.arraycopy(buf, 0, nb, 0, count);
        buf = nb;
    }

    public synchronized void reset() {
        count = 0;
    }

    public synchronized byte[] toByteArray() {
        byte[] r = new byte[count];
        System.arraycopy(buf, 0, r, 0, count);
        return r;
    }

    public int size() {
        return count;
    }

    /** Decodes the bytes with the default encoding. */
    public String toString() {
        return new String(buf, 0, count);
    }

    public void close() throws IOException {
    }
}

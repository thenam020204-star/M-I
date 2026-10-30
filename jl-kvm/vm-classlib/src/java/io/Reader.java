/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.io;

public abstract class Reader {
    protected Object lock;

    protected Reader() {
        this.lock = this;
    }

    protected Reader(Object lock) {
        if (lock == null) {
            throw new NullPointerException();
        }
        this.lock = lock;
    }

    public int read() throws IOException {
        char[] cb = new char[1];
        if (read(cb, 0, 1) == -1) {
            return -1;
        }
        return cb[0];
    }

    public int read(char[] cbuf) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }

    public abstract int read(char[] cbuf, int off, int len) throws IOException;

    public long skip(long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("skip value is negative");
        }
        int nn = (int) Math.min(n, 8192);
        synchronized (lock) {
            char[] skipBuffer = new char[nn];
            long r = n;
            while (r > 0) {
                int nc = read(skipBuffer, 0, (int) Math.min(r, nn));
                if (nc == -1) {
                    break;
                }
                r -= nc;
            }
            return n - r;
        }
    }

    public boolean ready() throws IOException {
        return false;
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int readAheadLimit) throws IOException {
        throw new IOException("mark() not supported");
    }

    public void reset() throws IOException {
        throw new IOException("reset() not supported");
    }

    public abstract void close() throws IOException;
}

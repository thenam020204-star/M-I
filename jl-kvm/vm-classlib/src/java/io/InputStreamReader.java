/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.io;

import jlkvm.internal.Encoding;

/** Decodes one character at a time, reading from the stream only the bytes that character needs. */
public class InputStreamReader extends Reader {
    private InputStream in;
    private final Encoding.Decoder decoder;

    public InputStreamReader(InputStream is) {
        super(is);
        in = is;
        decoder = new Encoding.Decoder(Encoding.defaultId());
    }

    public InputStreamReader(InputStream is, String enc) throws UnsupportedEncodingException {
        super(is);
        int id = Encoding.require(enc);
        in = is;
        decoder = new Encoding.Decoder(id);
    }

    private void ensureOpen() throws IOException {
        if (in == null) {
            throw new IOException("Stream closed");
        }
    }

    public int read() throws IOException {
        synchronized (lock) {
            ensureOpen();
            return decoder.read(in);
        }
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (off < 0 || len < 0 || off > cbuf.length - len) {
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return 0;
            }
            int c = decoder.read(in);
            if (c < 0) {
                return -1;
            }
            cbuf[off] = (char) c;
            int n = 1;
            while (n < len && (decoder.hasPending() || in.available() > 0)) {
                c = decoder.read(in);
                if (c < 0) {
                    break;
                }
                cbuf[off + n++] = (char) c;
            }
            return n;
        }
    }

    public long skip(long n) throws IOException {
        return super.skip(n);
    }

    public boolean ready() throws IOException {
        synchronized (lock) {
            ensureOpen();
            return decoder.hasPending() || in.available() > 0;
        }
    }

    /** As the reference implementation (phoneME, T05c): the reader supports mark when the underlying byte stream does. */
    public boolean markSupported() {
        synchronized (lock) {
            return in != null && in.markSupported();
        }
    }

    public void mark(int readAheadLimit) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (readAheadLimit < 0) {
                throw new IllegalArgumentException("Read-ahead limit < 0");
            }
            if (!in.markSupported()) {
                throw new IOException("mark() not supported");
            }
            in.mark(readAheadLimit > 0x1FFFFFFF ? Integer.MAX_VALUE : readAheadLimit * 4);   // up to 4 bytes per char
            decoder.mark();
        }
    }

    public void reset() throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (!in.markSupported()) {
                throw new IOException("reset() not supported");
            }
            in.reset();
            decoder.reset();
        }
    }

    public void close() throws IOException {
        synchronized (lock) {
            if (in != null) {
                in.close();
                in = null;
            }
        }
    }
}

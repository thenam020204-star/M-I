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
            while (n < len && in.available() > 0) {
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
            return in.available() > 0;
        }
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

    public void close() throws IOException {
        synchronized (lock) {
            if (in != null) {
                in.close();
                in = null;
            }
        }
    }
}

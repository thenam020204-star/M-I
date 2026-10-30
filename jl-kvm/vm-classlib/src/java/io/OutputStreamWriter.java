/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.io;

import jlkvm.internal.Encoding;

/** Encodes every write at once and passes the bytes to the stream (no hidden buffer); flush() flushes the stream. */
public class OutputStreamWriter extends Writer {
    private OutputStream out;
    private final int enc;
    private boolean bomDone;

    public OutputStreamWriter(OutputStream os) {
        super(os);
        out = os;
        enc = Encoding.defaultId();
    }

    public OutputStreamWriter(OutputStream os, String encName) throws UnsupportedEncodingException {
        super(os);
        enc = Encoding.require(encName);
        out = os;
    }

    private void ensureOpen() throws IOException {
        if (out == null) {
            throw new IOException("Stream closed");
        }
    }

    public void write(int c) throws IOException {
        write(new char[] {(char) c}, 0, 1);
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (off < 0 || len < 0 || off > cbuf.length - len) {
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return;
            }
            byte[] b = Encoding.encode(enc == Encoding.UTF16 && bomDone ? Encoding.UTF16BE : enc, cbuf, off, len);
            bomDone = true;
            out.write(b, 0, b.length);
        }
    }

    public void write(String str, int off, int len) throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        char[] cbuf = new char[len];
        str.getChars(off, off + len, cbuf, 0);
        write(cbuf, 0, len);
    }

    public void flush() throws IOException {
        synchronized (lock) {
            ensureOpen();
            out.flush();
        }
    }

    public void close() throws IOException {
        synchronized (lock) {
            if (out != null) {
                out.flush();
                out.close();
                out = null;
            }
        }
    }
}

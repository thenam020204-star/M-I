/*
 * jl-kvm guest class library (JSR-75 FileConnection layer, API per tools/api-sigs/db/jsr75-fc.json). Origin: original code written for jl-kvm
 * (PLAN D12, T32a); behaviour from the JSR-75 FileConnection API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.io.file;

import java.io.IOException;
import java.io.OutputStream;

/** Output stream of a FileConnection: writes at its own position (overwriting, never shortening the file), 1 KiB write buffer. */
final class FcOut extends OutputStream {
    private int handle;
    private long pos;           // file position of buf[0]
    private final byte[] buf = new byte[1024];
    private int n;

    FcOut(int handle, long start) {
        this.handle = handle;
        this.pos = start;
    }

    private void check() throws IOException {
        if (handle == 0) {
            throw new IOException("Stream closed");
        }
    }

    public void write(int b) throws IOException {
        check();
        if (n == buf.length) {
            flush();
        }
        buf[n++] = (byte) b;
    }

    public void write(byte[] b, int o, int l) throws IOException {
        check();
        if (b == null) {
            throw new NullPointerException();
        }
        if (o < 0 || l < 0 || o + l > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (l >= buf.length) {
            flush();
            Fs.write0(handle, pos, b, o, l);
            pos += l;
            return;
        }
        if (n + l > buf.length) {
            flush();
        }
        System.arraycopy(b, o, buf, n, l);
        n += l;
    }

    public void flush() throws IOException {
        check();
        if (n > 0) {
            int c = n;
            n = 0;
            Fs.write0(handle, pos, buf, 0, c);
            pos += c;
        }
    }

    public void close() throws IOException {
        if (handle != 0) {
            try {
                flush();
            } finally {
                int h = handle;
                handle = 0;
                Fs.close0(h);
            }
        }
    }
}

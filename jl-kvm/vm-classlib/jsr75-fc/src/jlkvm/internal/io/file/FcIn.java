/*
 * jl-kvm guest class library (JSR-75 FileConnection layer, API per tools/api-sigs/db/jsr75-fc.json). Origin: original code written for jl-kvm
 * (PLAN D12, T32a); behaviour from the JSR-75 FileConnection API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.io.file;

import java.io.IOException;
import java.io.InputStream;

/** Input stream of a FileConnection: its own host handle (it outlives the connection), a 1 KiB read buffer. */
final class FcIn extends InputStream {
    private int handle;
    private final String path;
    private long pos;           // file position of buf[0]
    private final byte[] buf = new byte[1024];
    private int len, off;       // valid bytes in buf, next byte

    FcIn(int handle, String path) {
        this.handle = handle;
        this.path = path;
    }

    private boolean fill() throws IOException {
        if (handle == 0) {
            throw new IOException("Stream closed");
        }
        pos += len;
        len = 0;
        off = 0;
        int n = Fs.read0(handle, pos, buf, 0, buf.length);
        if (n <= 0) {
            return false;
        }
        len = n;
        return true;
    }

    public int read() throws IOException {
        if (handle == 0) {
            throw new IOException("Stream closed");
        }
        if (off >= len && !fill()) {
            return -1;
        }
        return buf[off++] & 0xff;
    }

    public int read(byte[] b, int o, int l) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if (o < 0 || l < 0 || o + l > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (handle == 0) {
            throw new IOException("Stream closed");
        }
        if (l == 0) {
            return 0;
        }
        if (off >= len && !fill()) {
            return -1;
        }
        int n = len - off;
        if (n > l) {
            n = l;
        }
        System.arraycopy(buf, off, b, o, n);
        off += n;
        return n;
    }

    public int available() throws IOException {
        if (handle == 0) {
            throw new IOException("Stream closed");
        }
        long left = Fs.length0(path) - (pos + off);
        return left <= 0 ? 0 : left > 0x7fffffffL ? 0x7fffffff : (int) left;
    }

    public void close() throws IOException {
        if (handle != 0) {
            int h = handle;
            handle = 0;
            Fs.close0(h);
        }
    }
}

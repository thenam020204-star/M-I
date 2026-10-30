/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

import java.io.OutputStream;

/** Package-private: stdout (fd 1) / stderr (fd 2) of the host. Bytes are in the default encoding; the host decodes them. */
class ConsoleOutputStream extends OutputStream {
    private final int fd;

    ConsoleOutputStream(int fd) {
        this.fd = fd;
    }

    private static native void write0(int fd, byte[] b, int off, int len);

    public void write(int b) {
        write0(fd, new byte[] {(byte) b}, 0, 1);
    }

    public void write(byte[] b, int off, int len) {
        if (off < 0 || len < 0 || off > b.length - len) {
            throw new IndexOutOfBoundsException();
        }
        write0(fd, b, off, len);
    }
}

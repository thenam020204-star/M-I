/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.io;

import java.io.IOException;

/** MIDP 2.0 / GCF interface; the handler (if any) is jlkvm.internal.io.&lt;scheme&gt;.Protocol. */
public interface SocketConnection extends StreamConnection {
    byte DELAY = (byte) 0;
    byte KEEPALIVE = (byte) 2;
    byte LINGER = (byte) 1;
    byte RCVBUF = (byte) 3;
    byte SNDBUF = (byte) 4;

    String getAddress() throws IOException;

    String getLocalAddress() throws IOException;

    int getLocalPort() throws IOException;

    int getPort() throws IOException;

    int getSocketOption(byte a) throws IOException, IllegalArgumentException;

    void setSocketOption(byte a, int b) throws IOException, IllegalArgumentException;
}

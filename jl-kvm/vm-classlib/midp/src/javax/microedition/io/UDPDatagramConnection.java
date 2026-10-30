/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.io;

import java.io.IOException;

/** MIDP 2.0 / GCF interface; the handler (if any) is jlkvm.internal.io.&lt;scheme&gt;.Protocol. */
public interface UDPDatagramConnection extends DatagramConnection {
    String getLocalAddress() throws IOException;

    int getLocalPort() throws IOException;
}

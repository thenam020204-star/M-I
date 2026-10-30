/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package javax.microedition.io;

import java.io.IOException;

public interface DatagramConnection extends Connection {
    int getMaximumLength() throws IOException;

    int getNominalLength() throws IOException;

    void send(Datagram dgram) throws IOException;

    void receive(Datagram dgram) throws IOException;

    Datagram newDatagram(int size) throws IOException;

    Datagram newDatagram(int size, String addr) throws IOException;

    Datagram newDatagram(byte[] buf, int size) throws IOException;

    Datagram newDatagram(byte[] buf, int size, String addr) throws IOException;
}

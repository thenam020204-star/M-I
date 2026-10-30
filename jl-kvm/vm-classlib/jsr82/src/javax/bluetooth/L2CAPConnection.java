/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.bluetooth;

public interface L2CAPConnection extends javax.microedition.io.Connection {
    int DEFAULT_MTU = 672;
    int MINIMUM_MTU = 48;
    int getReceiveMTU() throws java.io.IOException;
    int getTransmitMTU() throws java.io.IOException;
    boolean ready() throws java.io.IOException;
    int receive(byte[] a0) throws java.io.IOException;
    void send(byte[] a0) throws java.io.IOException;
}

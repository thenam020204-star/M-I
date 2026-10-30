/*
 * jl-kvm TEST overlay (not part of the CLDC ROM): an in-memory protocol handler for the scheme "testnet", used to test the
 * Connector network policy "allow" (PLAN T20). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal.io.testnet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connection;
import javax.microedition.io.StreamConnection;
import jlkvm.internal.io.ConnectionBase;

public class Protocol implements ConnectionBase, StreamConnection {
    private String name;

    public Connection openPrim(String name, int mode, boolean timeouts) throws IOException {
        Protocol p = new Protocol();
        p.name = name;
        return p;
    }

    public InputStream openInputStream() throws IOException {
        return new ByteArrayInputStream(("testnet-ok:" + name).getBytes());
    }

    public java.io.DataInputStream openDataInputStream() throws IOException {
        return new java.io.DataInputStream(openInputStream());
    }

    public OutputStream openOutputStream() throws IOException {
        return new ByteArrayOutputStream();
    }

    public java.io.DataOutputStream openDataOutputStream() throws IOException {
        return new java.io.DataOutputStream(openOutputStream());
    }

    public void close() throws IOException {
    }
}

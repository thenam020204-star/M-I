/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.bluetooth;

import java.io.IOException;
import javax.microedition.io.Connection;
import jlkvm.internal.bt.Hw;

/**
 * A remote device of the simulated hardware is only an address: it cannot be reached (there are no peers), so asking it anything that needs the
 * radio is an IOException, and none of the security states is ever set.
 */
public class RemoteDevice {
    private final String address;

    protected RemoteDevice(String address) throws IllegalArgumentException, NullPointerException {
        if (address == null) {
            throw new NullPointerException("address");
        }
        if (address.length() != 12) {
            throw new IllegalArgumentException("address must be 12 characters");
        }
        for (int i = 0; i < 12; i++) {
            if (Character.digit(address.charAt(i), 16) < 0) {
                throw new IllegalArgumentException("address has a non-hexadecimal character");
            }
        }
        String a = address.toUpperCase();
        if (Hw.present() && a.equals(Hw.address0())) {
            throw new IllegalArgumentException("address is the address of the local device");
        }
        this.address = a;
    }

    /** "always == false" answers from the cache, which is empty (no inquiry ever finds a device): null. */
    public String getFriendlyName(boolean alwaysAsk) throws IOException {
        if (!alwaysAsk) {
            return null;
        }
        throw new IOException("remote device " + address + " cannot be contacted");
    }

    public final String getBluetoothAddress() {
        return address;
    }

    public boolean equals(Object obj) {
        return obj instanceof RemoteDevice && ((RemoteDevice) obj).address.equals(address);
    }

    public int hashCode() {
        return address.hashCode();
    }

    /** No Bluetooth connection can exist, so none is a Bluetooth connection. */
    public static RemoteDevice getRemoteDevice(Connection conn) throws IOException, IllegalArgumentException, NullPointerException {
        if (conn == null) {
            throw new NullPointerException("conn");
        }
        throw new IllegalArgumentException("not a Bluetooth connection");
    }

    public boolean isTrustedDevice() {
        return false;
    }

    public boolean authenticate() throws IOException {
        throw new IOException("no connection to " + address);
    }

    public boolean authorize(Connection conn) throws IOException, IllegalArgumentException {
        throw new IllegalArgumentException("not a connection to this device");
    }

    public boolean encrypt(Connection conn, boolean on) throws IOException, IllegalArgumentException {
        throw new IllegalArgumentException("not a connection to this device");
    }

    public boolean isAuthenticated() {
        return false;
    }

    public boolean isAuthorized(Connection conn) throws IOException, IllegalArgumentException {
        throw new IllegalArgumentException("not a connection to this device");
    }

    public boolean isEncrypted() {
        return false;
    }
}

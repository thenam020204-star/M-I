/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour: the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.bt;

import java.io.IOException;
import javax.bluetooth.BluetoothConnectionException;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;

/** Connector.open of btspp:// and btl2cap:// (the profile policy treats the schemes as local, they never reach the network code). */
public final class Open {
    private Open() {
    }

    /**
     * No hardware: BluetoothStateException (an IOException). Simulated adapter: a client URL (address after //) cannot reach any peer
     * (BluetoothConnectionException TIMEOUT); a server URL (//localhost:UUID) is ConnectionNotFoundException (accepting connections is not simulated).
     */
    public static Connection open(String name) throws IOException {
        Hw.need();
        int i = name.indexOf("//");
        if (i < 0) {
            throw new IllegalArgumentException("bad Bluetooth URL");
        }
        String rest = name.substring(i + 2);
        if (rest.startsWith("localhost")) {
            throw new ConnectionNotFoundException("Bluetooth server connections are not simulated");
        }
        throw new BluetoothConnectionException(BluetoothConnectionException.TIMEOUT, "no device answers at " + name);
    }
}

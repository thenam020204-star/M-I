/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * the Bluetooth hardware of the profile (host: BluetoothNatives). License: Apache-2.0.
 */
package jlkvm.internal.bt;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;

/** ROM-private: what Bluetooth hardware the device has. The plug point for a real adapter (Android host, M6): not done. */
public final class Hw {
    private Hw() {
    }

    /** 0 = no Bluetooth hardware, 1 = one lone simulated adapter without peers (profile bluetooth.mode). */
    public static native int mode0();

    public static native String address0();

    public static native String name0();

    public static boolean present() {
        return mode0() != 0;
    }

    /** The error of every operation that needs the radio on a device that has none. */
    public static BluetoothStateException noRadio() {
        return new BluetoothStateException("no Bluetooth hardware on this device");
    }

    public static void need() throws IOException {
        if (!present()) {
            throw noRadio();
        }
    }
}

/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.bluetooth;

public class BluetoothConnectionException extends java.io.IOException {
    public static final int FAILED_NOINFO = 4;
    public static final int NO_RESOURCES = 3;
    public static final int SECURITY_BLOCK = 2;
    public static final int TIMEOUT = 5;
    public static final int UNACCEPTABLE_PARAMS = 6;
    public static final int UNKNOWN_PSM = 1;
    private final int status;

    public BluetoothConnectionException(int error) {
        super();
        status = check(error);
    }

    public BluetoothConnectionException(int error, String msg) {
        super(msg);
        status = check(error);
    }

    private static int check(int error) {
        if (error < UNKNOWN_PSM || error > UNACCEPTABLE_PARAMS) {
            throw new IllegalArgumentException("error " + error);
        }
        return error;
    }

    public int getStatus() {
        return status;
    }
}

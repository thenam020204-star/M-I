/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.rms;

/** MIDP 2.0 RMS exception. */
public class RecordStoreNotFoundException extends RecordStoreException {
    public RecordStoreNotFoundException() {
        super();
    }

    public RecordStoreNotFoundException(String message) {
        super(message);
    }
}

/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.midlet;

public class MIDletStateChangeException extends Exception {
    public MIDletStateChangeException() {
        super();
    }

    public MIDletStateChangeException(String s) {
        super(s);
    }
}

/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

public class InterruptedException extends Exception {
    public InterruptedException() {
        super();
    }

    public InterruptedException(String s) {
        super(s);
    }
}

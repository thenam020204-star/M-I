/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.io;

public class InterruptedIOException extends IOException {
    public int bytesTransferred = 0;

    public InterruptedIOException() {
        super();
    }

    public InterruptedIOException(String s) {
        super(s);
    }
}

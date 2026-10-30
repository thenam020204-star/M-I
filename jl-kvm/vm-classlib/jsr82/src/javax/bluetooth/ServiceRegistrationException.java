/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.bluetooth;

public class ServiceRegistrationException extends java.io.IOException {
    public ServiceRegistrationException() {
        super();
    }

    public ServiceRegistrationException(String msg) {
        super(msg);
    }
}

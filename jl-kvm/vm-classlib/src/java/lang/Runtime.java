/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

/** Memory figures come from the guest heap arena (PLAN D3); gc() runs a full collection (D4). */
public class Runtime {
    private static final Runtime current = new Runtime();

    private Runtime() {
    }

    public static Runtime getRuntime() {
        return current;
    }

    public native long freeMemory();

    public native long totalMemory();

    public native void gc();

    /** Terminates the VM (all guest threads). Never returns. */
    public native void exit(int status);
}

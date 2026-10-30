/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal;

/** ROM-private holder of {@link CanvasAccess}. */
public final class Access {
    private Access() {
    }

    public static CanvasAccess canvas;
}

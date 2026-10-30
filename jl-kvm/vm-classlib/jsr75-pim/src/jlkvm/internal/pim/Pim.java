/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

/** ROM-private: the profile permission of the PIM (host: PimNatives). */
public final class Pim {
    private Pim() {
    }

    /** 1 allowed, 0 denied (SecurityException). */
    public static native int perm0();
}

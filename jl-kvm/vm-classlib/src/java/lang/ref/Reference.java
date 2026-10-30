/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang.ref;

/**
 * Reference object. The collector (PLAN D4) clears {@code referent} of a WeakReference when the target is only weakly reachable;
 * it finds the field by this name, so it must not be renamed.
 */
public abstract class Reference {
    Object referent;

    Reference(Object ref) {
        referent = ref;
    }

    public Object get() {
        return referent;
    }

    public void clear() {
        referent = null;
    }
}

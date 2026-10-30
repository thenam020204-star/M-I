/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal;

/** ROM-private: sees the key events of a Canvas on the event thread before the application does (GameCanvas key states). */
public interface KeyTap {
    /** @param type 1 pressed, 2 released, 3 repeated. @return true when the event is consumed (not delivered to keyPressed...). */
    boolean key(int type, int code);
}

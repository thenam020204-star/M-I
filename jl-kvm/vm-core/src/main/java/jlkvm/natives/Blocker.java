package jlkvm.natives;

/**
 * Suspends the calling guest thread while the vm-thread keeps scheduling the others (T15 skeleton). The scheduler polls
 * {@link #poll} on every pass; when it returns true the thread becomes runnable and the native call completes with the
 * values the blocker stored through the context it was given (retInt, retRef, throwNew...).
 */
public interface Blocker {
    /** @return true when finished. Called on the vm-thread only. */
    boolean poll(NativeContext c);
}

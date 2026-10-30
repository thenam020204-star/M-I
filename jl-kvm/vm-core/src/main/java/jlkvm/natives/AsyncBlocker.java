package jlkvm.natives;

/**
 * A {@link Blocker} completed from another host thread (file I/O, network, UI dialog on the Android UI thread, R7): the native
 * starts the host work, calls {@code c.block(blocker)} and returns; the work calls {@link #finish} when done. Only the guest
 * thread that made the call is parked; the vm-thread polls {@link #poll} on each scheduler pass and then runs
 * {@link #complete} on the vm-thread, where the result can be returned (c.retInt(...), c.throwNew(...)) and the arguments of
 * the call are still readable (c.argRef(i) ...: the collector keeps them valid and moves them while the thread is parked).
 * Never keep a raw guest reference in a field of the blocker: the heap is compacted; use c.argRef or {@link Handles}.
 */
public abstract class AsyncBlocker implements Blocker {
    private volatile boolean done;

    /** Any thread: the host work is finished (safe to call more than once). */
    public final void finish() { done = true; }

    public final boolean poll(NativeContext c) {
        if (!done) return false;
        complete(c);
        return true;
    }

    /** vm-thread: produce the result of the native call. */
    protected abstract void complete(NativeContext c);
}

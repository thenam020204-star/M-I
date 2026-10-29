package jlkvm.natives;

/**
 * A host object kept in {@link Handles} whose guest owner can die (D8, T13): when the collector finds the owner dead it removes
 * the entry and calls {@link #dispose()} (free the bitmap, close the stream...). Runs on the vm-thread inside the collection, so
 * it must not touch the guest heap.
 */
public interface Disposable {
    void dispose();
}

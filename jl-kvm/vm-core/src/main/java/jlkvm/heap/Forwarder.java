package jlkvm.heap;

/** Address translation of a sliding compaction (T13): where a live object went. */
public interface Forwarder {
    /** @return the new reference of the live object {@code ref} (identity when nothing moved). */
    int forward(int ref);
}

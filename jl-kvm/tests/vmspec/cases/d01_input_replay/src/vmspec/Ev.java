package vmspec;

/** Natives implemented by the test host (key "vmspec/Ev.name(descriptor)"). */
class Ev {
    /** Blocks until the host has an input event; fills e[0..3] = type, a, b, c. */
    static native void nextEvent(int[] e);
}

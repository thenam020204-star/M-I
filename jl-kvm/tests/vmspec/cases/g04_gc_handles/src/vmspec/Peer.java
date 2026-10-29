package vmspec;

/** Guest object with a host peer (PLAN D8): "peer" indexes the host handle table, the handle's owner is this object. */
public class Peer {
    int peer;

    Peer() { peer = attach0(); }

    void close() {
        detach0(peer);
        peer = 0;
    }

    native int attach0();
    native void detach0(int id);
    static native int live0();
    static native int disposed0();
}

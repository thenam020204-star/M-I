package jlkvm.link;

/** Internal carrier of a guest exception raised by VM code (no host stack trace). {@code ref != 0}: already a guest object. */
public final class VmThrow extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public final String guestClass;
    public final String msg;
    public final int ref;

    public VmThrow(String guestClass, String msg) {
        super(guestClass + ": " + msg, null, false, false);
        this.guestClass = guestClass;
        this.msg = msg;
        this.ref = 0;
    }

    public VmThrow(int ref) {
        super("guest exception @" + ref, null, false, false);
        this.guestClass = null;
        this.msg = null;
        this.ref = ref;
    }
}

/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package jlkvm.internal;

/**
 * ROM-private: byte storage of the record stores, answered by the host (host-common RmsNatives over a RecordStorage; docs/midp.md section 8).
 * A store id is valid between open0 and close0. Negative results: -1 not found / invalid record id, -2 full, -3 not allowed.
 */
public final class Rms {
    private Rms() {
    }

    public static final int INFO_AUTHMODE = 0, INFO_WRITABLE = 1, INFO_RECORDS = 2, INFO_NEXT_ID = 3, INFO_VERSION = 4, INFO_SIZE = 5,
            INFO_AVAILABLE = 6, INFO_OWN = 7;

    /** Takes a snapshot of the store names of this suite and returns their number; {@link #listName0} reads it. */
    public static native int list0();

    public static native String listName0(int index);

    /** @param vendor suite of another MIDlet suite (both null = this suite) */
    public static native int open0(String name, boolean create, int authmode, boolean writable, String vendor, String suite, long now);

    public static native void close0(int id);

    public static native int delete0(String name);

    public static native int info0(int id, int what);

    public static native long modified0(int id);

    public static native int add0(int id, byte[] data, int off, int len, long now);

    public static native int set0(int id, int recordId, byte[] data, int off, int len, long now);

    public static native int remove0(int id, int recordId, long now);

    /** @return the length of the record, -1 when it does not exist */
    public static native int size0(int id, int recordId);

    /** Copies the record to {@code buf} at {@code off}; the caller has checked the room. @return the length, -1 when it does not exist */
    public static native int read0(int id, int recordId, byte[] buf, int off);

    /** Fills {@code ids} (length >= INFO_RECORDS) with the record ids in ascending order. */
    public static native void ids0(int id, int[] ids);

    public static native int setMode0(int id, int authmode, boolean writable, long now);
}

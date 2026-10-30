/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package jlkvm.internal;

/**
 * ROM-private: the real HTTP transport of the host (host-common NetNatives). Only reachable when the profile's network policy for
 * "http" is "allow" (the host checks it again). http0 blocks the calling guest thread, not the VM. Negative results: -1 I/O error
 * (message in error0), -2 policy, -3 request too large. A response handle stays valid until free0.
 */
public final class Net {
    private Net() {
    }

    /** @param headers key, value, key, value... @return handle > 0, or a negative code */
    public static native int http0(String method, String url, String[] headers, byte[] body, int bodyLen, int timeoutMs);

    /** The message of the last failed http0 of this VM. */
    public static native String error0();

    public static native int status0(int handle);

    public static native String message0(int handle);

    public static native int headerCount0(int handle);

    public static native String headerKey0(int handle, int index);

    public static native String headerValue0(int handle, int index);

    public static native int length0(int handle);

    /** Copies up to len bytes of the body from position pos; returns the number copied (0 at the end). */
    public static native int read0(int handle, int pos, byte[] buf, int off, int len);

    public static native void free0(int handle);
}

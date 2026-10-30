/*
 * jl-kvm guest class library (WMA 1.1 layer, API per tools/api-sigs/db/s40dp2-wma-1.1.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour per the WMA javadoc of the S40 DP2.0 SDK 1.1, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.wma;

/** ROM-private natives of the SMS / CBS adapter (host: SmsNatives). The host re-checks the profile policy (sms.mode) in bind0 and send. */
public final class Sms {
    private Sms() {
    }

    /** Reserves a local port for a server connection: handle > 0, -1 policy deny, -2 offline, -3 port already in use. {@code cbs} 1 for cbs. */
    public static native int bind0(int cbs, int port);

    /** Releases the port; wakes the threads parked in await0 / awaitNew0 (they answer 0). */
    public static native void unbind0(int handle);

    /** Sends a text message: 0 sent (or simulated), -1 policy deny, -2 offline. */
    public static native int sendText0(String address, String text, int segments);

    public static native int sendBinary0(String address, byte[] data, int segments);

    /** Blocks (parks only the calling guest thread) until a message is queued: 1, or 0 when the port was released. Does not remove the message. */
    public static native int await0(int handle);

    /** Removes the oldest queued message into the "current" slot: 0 none, 1 text, 2 binary. */
    public static native int take0(int handle);

    public static native String curAddress0();

    public static native String curText0();

    public static native byte[] curData0();

    public static native long curTime0();

    /** Switches the notification of the listener thread on / off (on: every message still queued is notified). */
    public static native void listening0(int handle, boolean on);

    /** Listener thread: blocks until a message arrived that was not notified yet (1) or the listening ends (0). */
    public static native int awaitNew0(int handle);
}

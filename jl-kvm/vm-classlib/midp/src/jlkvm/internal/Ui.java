/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal;

/** ROM-private: the high-level screen protocol with the host (docs/midp.md section 6). Natives are answered by host-common MidpNatives. */
public final class Ui {
    private Ui() {
    }

    /** Sends the description of the current displayable: ints[0..len) and the string table (see lcdui.ScreenCodec). */
    public static native void sync0(int[] data, int len, String[] strings, int nstrings);

    /** The text the user last typed into the TextField / TextBox with the given item / screen id (host mailbox). */
    public static native String text0(int id);

    /** Answer of CustomItem.traverse: whether the item keeps the traversal inside (1) and the visible rectangle it asks for. */
    public static native void traverse0(int itemId, int dir, int inside, int x, int y, int w, int h);
}

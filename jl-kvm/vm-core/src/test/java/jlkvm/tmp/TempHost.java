package jlkvm.tmp;

import java.util.Arrays;
import jlkvm.ClassSource;
import jlkvm.HostPlatform;
import jlkvm.VmConfig;
import jlkvm.heap.Heap;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/**
 * TEMPORARY test host (PLAN T11): the few natives the stub ROM needs that are not VM-core natives (console, float formatting,
 * MIDlet properties, software Image). T15/T23 and host-desktop replace this.
 */
public final class TempHost implements HostPlatform {
    private final ClassSource rom;
    private final VmConfig cfg;
    /** Guest System.out goes here (stdout by default). */
    public java.io.PrintStream stdout = System.out;

    /** Host objects disposed by the collector because their guest owner died (Peer test). */
    public int gcDisposed;

    private final jlkvm.DeviceProfile profile;
    /** Platform services of the MIDP layer (frames, platformRequest, vibration) - tests inspect it. */
    public final jlkvm.host.RecordingServices services = new jlkvm.host.RecordingServices();

    public TempHost(ClassSource rom, VmConfig cfg, jlkvm.DeviceProfile profile) { this.rom = rom; this.cfg = cfg; this.profile = profile; }

    public long hostNanoTime() { return System.nanoTime(); }
    /** Everything the VM logged (tests read it). */
    public final java.util.List<String> logs = java.util.Collections.synchronizedList(new java.util.ArrayList<String>());
    public void log(int level, String tag, String msg) { logs.add("[" + tag + "] " + msg); System.err.println("[" + tag + "] " + msg); }
    public ClassSource rom() { return rom; }

    public void registerNatives(final NativeRegistry r) {
        r.register("java/lang/ConsoleOutputStream.write0(I[BII)V", new NativeMethod() { public void invoke(NativeContext c) {
            int arr = c.argRef(1), off = c.argInt(2), len = c.argInt(3);
            byte[] b = new byte[len];
            for (int i = 0; i < len; i++) b[i] = c.heap().getByte(arr, off + i);
            java.io.PrintStream o = c.argInt(0) == 2 ? System.err : stdout;
            o.write(b, 0, b.length); o.flush();
        } });
        // ---- T13 peer cleanup test (tests/vmspec/cases/g04_gc_handles): a host object owned by a guest Peer object
        r.register("vmspec/Peer.attach0()I", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(c.handles().alloc(new jlkvm.natives.Disposable() { public void dispose() { gcDisposed++; } }, c.argRef(0))); } });
        r.register("vmspec/Peer.detach0(I)V", new NativeMethod() { public void invoke(NativeContext c) { c.handles().release(c.argInt(1)); } });
        r.register("vmspec/Peer.live0()I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(c.handles().liveCount()); } });
        r.register("vmspec/Peer.disposed0()I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(gcDisposed); } });
        // ---- T16 input events: blocks until the host posted one, fills int[4] {type, a, b, c}
        r.register("vmspec/Ev.nextEvent([I)V", new NativeMethod() { public void invoke(NativeContext c) {
            c.block(new jlkvm.natives.Blocker() { public boolean poll(NativeContext cc) {
                int[] e = r.services().pollEvent();
                if (e == null) return false;
                for (int i = 0; i < 4; i++) cc.heap().putIntElem(cc.argRef(0), i, e[i]);
                return true; } }); } });
        // ---- T15 sample natives (tests/vmspec/cases/q01_natives)
        r.register("vmspec/Nat.add(II)I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(c.argInt(0) + c.argInt(1)); } });
        r.register("vmspec/Nat.mix(IJFD)D", new NativeMethod() { public void invoke(NativeContext c) {
            c.retDouble(c.argInt(0) + c.argLong(1) + c.argFloat(2) + c.argDouble(3)); } });
        r.register("vmspec/Nat.mixl(IJ)J", new NativeMethod() { public void invoke(NativeContext c) { c.retLong(c.argLong(1) * c.argInt(0)); } });
        r.register("vmspec/Nat.fill([BI)I", new NativeMethod() { public void invoke(NativeContext c) {
            int a = c.argRef(0), v = c.argInt(1);
            if (a == 0) { c.throwNew("java/lang/NullPointerException", null); return; }
            for (int i = 0; i < c.heap().arrayLength(a); i++) c.heap().putByte(a, i, (byte) (v + i));
            c.retInt(c.heap().arrayLength(a)); } });
        r.register("vmspec/Nat.fail(Ljava/lang/String;)V", new NativeMethod() { public void invoke(NativeContext c) {
            c.throwNew("java/lang/IllegalArgumentException", r.services().string(c.argRef(0))); } });
        r.register("vmspec/Nat.newString(I)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(r.services().newString("native-" + c.argInt(0))); } });
        // blocks the calling guest thread for ms (a host thread finishes it); the byte[] argument is filled when it wakes up
        r.register("vmspec/Nat.blockFill(I[B)I", new NativeMethod() { public void invoke(final NativeContext c) {
            final int ms = c.argInt(0);
            final jlkvm.natives.AsyncBlocker b = new jlkvm.natives.AsyncBlocker() {
                protected void complete(NativeContext cc) {
                    int a = cc.argRef(1);   // read again after the wake-up: the array may have been moved by a collection
                    for (int i = 0; i < cc.heap().arrayLength(a); i++) cc.heap().putByte(a, i, (byte) (ms + i));
                    cc.retInt(ms);
                } };
            c.block(b);
            new Thread() { public void run() { try { Thread.sleep(ms); } catch (InterruptedException e) { } b.finish(); } }.start(); } });
        // a polling blocker that fails: the exception is raised in the guest thread
        r.register("vmspec/Nat.blockThrow(I)V", new NativeMethod() { public void invoke(final NativeContext c) {
            final int[] n = {c.argInt(0)};
            c.block(new jlkvm.natives.Blocker() { public boolean poll(NativeContext cc) {
                if (--n[0] > 0) return false;
                cc.throwNew("java/lang/IllegalArgumentException", "blocker failed"); return true; } }); } });
        r.register("vmspec/Nat.blockLong(J)J", new NativeMethod() { public void invoke(final NativeContext c) {
            final long end = System.nanoTime() + 50000000L;
            c.block(new jlkvm.natives.Blocker() { public boolean poll(NativeContext cc) {
                if (System.nanoTime() < end) return false;
                cc.retLong(cc.argLong(0) * 2); return true; } }); } });
        // ---- MIDP layer (T21-T23): the natives of host-common over the software raster; frames are recorded in {@link #services}
        new jlkvm.host.MidpNatives(profile, cfg.appProperties, new jlkvm.host.ui.SoftRaster(), services).register(r);
    }
}

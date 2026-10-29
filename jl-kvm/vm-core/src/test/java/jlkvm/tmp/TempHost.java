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

    private static final class Img { final int w, h; final int[] px; Img(int w, int h) { this.w = w; this.h = h; px = new int[w * h]; Arrays.fill(px, 0xFFFFFFFF); } }

    public TempHost(ClassSource rom, VmConfig cfg) { this.rom = rom; this.cfg = cfg; }

    public long hostNanoTime() { return System.nanoTime(); }
    public void log(int level, String tag, String msg) { System.err.println("[" + tag + "] " + msg); }
    public ClassSource rom() { return rom; }

    public void registerNatives(final NativeRegistry r) {
        r.register("java/io/PrintStream.write0(ILjava/lang/String;)V", new NativeMethod() { public void invoke(NativeContext c) {
            String s = r.services().string(c.argRef(1));
            if (c.argInt(0) == 2) System.err.print(s);
            else { stdout.print(s); stdout.flush(); }
        } });
        r.register("java/lang/Float.toString(F)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(r.services().newString(Float.toString(c.argFloat(0)))); } });
        r.register("java/lang/Double.toString(D)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(r.services().newString(Double.toString(c.argDouble(0)))); } });
        r.register("javax/microedition/midlet/MIDlet.getAppProperty0(Ljava/lang/String;)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            String k = r.services().string(c.argRef(0));
            String v = null;
            for (java.util.Map.Entry<String, String> e : cfg.appProperties.entrySet()) if (e.getKey().equalsIgnoreCase(k)) v = e.getValue();
            c.retRef(v == null ? 0 : r.services().newString(v)); } });
        // ---- T13 peer cleanup test (tests/vmspec/cases/g04_gc_handles): a host object owned by a guest Peer object
        r.register("vmspec/Peer.attach0()I", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(c.handles().alloc(new jlkvm.natives.Disposable() { public void dispose() { gcDisposed++; } }, c.argRef(0))); } });
        r.register("vmspec/Peer.detach0(I)V", new NativeMethod() { public void invoke(NativeContext c) { c.handles().release(c.argInt(1)); } });
        r.register("vmspec/Peer.live0()I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(c.handles().liveCount()); } });
        r.register("vmspec/Peer.disposed0()I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(gcDisposed); } });
        r.register("javax/microedition/lcdui/Image.create0(II)I", new NativeMethod() { public void invoke(final NativeContext c) {
            int w = c.argInt(0), h = c.argInt(1);
            r.services().charge(w * h / 64);
            c.retInt(c.handles().alloc(new Img(w, h), 0)); } });
        r.register("javax/microedition/lcdui/Graphics.fillRect0(IIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            Img im = (Img) c.handles().get(c.argInt(0));
            int argb = 0xFF000000 | c.argInt(1), x = c.argInt(2), y = c.argInt(3), w = c.argInt(4), h = c.argInt(5);
            r.services().charge(Math.max(0, w) * Math.max(0, h) / 64);
            for (int j = Math.max(0, y); j < Math.min(im.h, y + h); j++)
                for (int i = Math.max(0, x); i < Math.min(im.w, x + w); i++) im.px[j * im.w + i] = argb;
        } });
        r.register("javax/microedition/lcdui/Image.getRGB0(I[IIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            Img im = (Img) c.handles().get(c.argInt(0));
            Heap hp = c.heap();
            int arr = c.argRef(1), off = c.argInt(2), scan = c.argInt(3), x = c.argInt(4), y = c.argInt(5), w = c.argInt(6), h = c.argInt(7);
            r.services().charge(w * h / 64);
            for (int j = 0; j < h; j++)
                for (int i = 0; i < w; i++) hp.putIntElem(arr, off + j * scan + i, im.px[(y + j) * im.w + x + i]);
        } });
    }
}

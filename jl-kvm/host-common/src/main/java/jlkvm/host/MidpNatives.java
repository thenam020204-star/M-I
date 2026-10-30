package jlkvm.host;

import java.io.IOException;
import java.util.Map;
import jlkvm.DeviceProfile;
import jlkvm.heap.Heap;
import jlkvm.host.ui.ScreenHost;
import jlkvm.host.ui.ScreenModel;
import jlkvm.host.ui.Surface;
import jlkvm.host.ui.SurfaceFactory;
import jlkvm.natives.Blocker;
import jlkvm.natives.GuestServices;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/**
 * The natives of the MIDP layer (T21-T23): AMS (properties, platformRequest, permissions), device parameters, the guest event loop,
 * lcdui Display / Font / Image / Graphics. Keys follow lcdui natives documented in docs/lcdui-natives.md. Shared by host-desktop
 * and host-android; drawing goes through {@link SurfaceFactory}/{@link Surface}.
 */
public final class MidpNatives {
    private final DeviceProfile profile;
    private final Map<String, String> appProps;
    private final SurfaceFactory factory;
    private final PlatformServices svc;
    private final ScreenHost ui;
    private final jlkvm.host.rms.RecordStorage records;
    private jlkvm.host.net.HttpTransport http = new jlkvm.host.net.JavaHttpTransport();
    private jlkvm.host.audio.AudioDevice audio = new jlkvm.host.audio.RecordingAudioDevice();
    /** Set by EventLoop.wake0: a guest thread posted a job while the event thread may be blocked. */
    private boolean wake;

    public MidpNatives(DeviceProfile profile, Map<String, String> appProperties, SurfaceFactory factory, PlatformServices svc) {
        this(profile, appProperties, factory, svc, ScreenHost.NONE);
    }

    public MidpNatives(DeviceProfile profile, Map<String, String> appProperties, SurfaceFactory factory, PlatformServices svc, ScreenHost ui) {
        this(profile, appProperties, factory, svc, ui, new jlkvm.host.rms.MemoryRecordStorage(profile));
    }

    /** {@code records}: where the RMS (T26) keeps the record stores (host-desktop: a directory; default: memory only). */
    public MidpNatives(DeviceProfile profile, Map<String, String> appProperties, SurfaceFactory factory, PlatformServices svc, ScreenHost ui,
                       jlkvm.host.rms.RecordStorage records) {
        this.records = records;
        this.ui = ui;
        this.profile = profile;
        this.appProps = appProperties;
        this.factory = factory;
        this.svc = svc;
    }

    /** The HTTP of the host for the "http" connections (T26b); default {@code JavaHttpTransport} (used only when the profile allows "http"). */
    public MidpNatives httpTransport(jlkvm.host.net.HttpTransport t) { this.http = t; return this; }

    /** The audio output (T27); default: a {@code RecordingAudioDevice} (silent, logs). */
    public MidpNatives audioDevice(jlkvm.host.audio.AudioDevice d) { this.audio = d; return this; }

    private jlkvm.host.nokia.NokiaDevice nokia = new jlkvm.host.nokia.RecordingNokiaDevice();

    /** The Nokia DeviceControl features (T28); default: a {@code RecordingNokiaDevice} (logs). */
    public MidpNatives nokiaDevice(jlkvm.host.nokia.NokiaDevice d) { this.nokia = d; return this; }

    private SmsNatives sms;

    /** The SMS / CBS adapter of WMA (T32a): created on first use from the profile ({@code sms.mode}); a test host pushes incoming messages through it. */
    public synchronized SmsNatives sms() { if (sms == null) sms = new SmsNatives(profile); return sms; }

    public MidpNatives sms(SmsNatives s) { this.sms = s; return this; }

    private jlkvm.host.fs.HostFs fs;

    /** The file system behind JSR-75 FileConnection (T32a); default: in memory ({@code MemoryFs}), lost at exit. */
    public MidpNatives fileSystem(jlkvm.host.fs.HostFs f) { this.fs = f; return this; }

    private static final String LCDUI = "javax/microedition/lcdui/";

    private Surface surf(NativeContext c, int id) {
        Object o = c.handles().get(id);
        if (!(o instanceof Surface)) throw new jlkvm.link.VmThrow("java/lang/IllegalStateException", "bad image handle " + id);
        return (Surface) o;
    }

    private static int[] ints(Heap h, int arr, int off, int n) {
        int[] r = new int[n];
        for (int i = 0; i < n; i++) r[i] = h.getIntElem(arr, off + i);
        return r;
    }

    public void register(final NativeRegistry r) {
        final GuestServices gs = r.services();
        new RmsNatives(records, profile, appProps).register(r);
        new NetNatives(profile, http).register(r);
        new AudioNatives(profile, audio).register(r);
        new NokiaNatives(profile, nokia).register(r);
        sms().register(r);
        new FileNatives(profile, fs != null ? fs : new jlkvm.host.fs.MemoryFs(profile.fileRoots, profile.fileQuotaBytes)).register(r);
        new PimNatives(profile).register(r);
        new BluetoothNatives(profile).register(r);
        // ------------------------------------------------------------ AMS
        r.register("jlkvm/internal/Ams.property0(Ljava/lang/String;)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            String k = gs.string(c.argRef(0)), v = null;
            for (Map.Entry<String, String> e : appProps.entrySet()) if (e.getKey().equalsIgnoreCase(k)) { v = e.getValue(); break; }
            c.retRef(v == null ? 0 : gs.newString(v)); } });
        r.register("jlkvm/internal/Ams.platformRequest0(Ljava/lang/String;)I", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(svc.platformRequest(gs.string(c.argRef(0)))); } });
        r.register("jlkvm/internal/Ams.checkPermission0(Ljava/lang/String;)I", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(svc.checkPermission(gs.string(c.argRef(0)))); } });
        // ------------------------------------------------------------ device
        r.register("jlkvm/internal/Device.info0(I)I", new NativeMethod() { public void invoke(NativeContext c) {
            DeviceProfile p = profile;
            int v;
            switch (c.argInt(0)) {
                case 0: v = p.screenWidth; break;
                case 1: v = p.screenHeight; break;
                case 2: v = p.canvasWidth; break;
                case 3: v = p.canvasHeight; break;
                case 4: v = p.isColor ? 1 : 0; break;
                case 5: v = p.numColors; break;
                case 6: v = p.numAlphaLevels; break;
                case 7: v = p.pointerEvents ? 1 : 0; break;
                case 8: v = p.pointerMotionEvents ? 1 : 0; break;
                case 9: v = p.repeatEvents ? 1 : 0; break;
                case 10: v = p.doubleBuffered ? 1 : 0; break;
                case 11: v = p.imagesCountInHeap ? 1 : 0; break;
                default: v = 0;
            }
            c.retInt(v); } });
        final String[] keyNames = {"softLeft", "softRight", "select", "up", "down", "left", "right", "clear", "send", "end"};
        r.register("jlkvm/internal/Device.key0(I)I", new NativeMethod() { public void invoke(NativeContext c) {
            int i = c.argInt(0);
            c.retInt(i >= 0 && i < keyNames.length ? profile.key(keyNames[i]) : 0); } });
        r.register("jlkvm/internal/Device.vibrate0(I)Z", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(svc.vibrate(c.argInt(0)) ? 1 : 0); } });
        r.register("jlkvm/internal/Device.flash0(I)Z", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(svc.flashBacklight(c.argInt(0)) ? 1 : 0); } });
        // ------------------------------------------------------------ guest event loop (D13)
        r.register("jlkvm/internal/EventLoop.wake0()V", new NativeMethod() { public void invoke(NativeContext c) { wake = true; } });
        r.register("jlkvm/internal/EventLoop.next0([IZ)I", new NativeMethod() { public void invoke(NativeContext c) {
            int[] e = gs.pollEvent();
            if (e != null) { fill(c, e); c.retInt(1); return; }
            if (c.argInt(1) == 0) { c.retInt(0); return; }
            if (wake) { wake = false; c.retInt(0); return; }
            c.block(new Blocker() { public boolean poll(NativeContext cc) {
                int[] ev = gs.pollEvent();
                if (ev != null) { fill(cc, ev); cc.retInt(1); return true; }
                if (wake) { wake = false; cc.retInt(0); return true; }
                return false; } }); } });
        // ------------------------------------------------------------ display
        r.register(LCDUI + "Display.present0(I)V", new NativeMethod() { public void invoke(NativeContext c) {
            svc.present(surf(c, c.argInt(0)));
            gs.charge(profile.screenWidth * profile.screenHeight / 256); } });
        // ------------------------------------------------------------ high-level screens (T24)
        r.register("jlkvm/internal/Ui.sync0([II[Ljava/lang/String;I)V", new NativeMethod() { public void invoke(NativeContext c) {
            Heap h = c.heap();
            int n = c.argInt(1), ns = c.argInt(3);
            int[] d = ints(h, c.argRef(0), 0, n);
            String[] s = new String[ns];
            for (int i = 0; i < ns; i++) s[i] = gs.string(h.getRefElem(c.argRef(2), i));
            ScreenModel m;
            try {
                m = ScreenModel.decode(d, s);
            } catch (IllegalArgumentException e) {
                throw new jlkvm.link.VmThrow("java/lang/IllegalStateException", "bad screen description: " + e.getMessage());
            }
            ui.screen(m);
            gs.charge(n / 4 + 8); } });
        r.register("jlkvm/internal/Ui.text0(I)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            String t = ui.text(c.argInt(0));
            c.retRef(t == null ? 0 : gs.newString(t)); } });
        r.register("jlkvm/internal/Ui.traverse0(IIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            ui.traverseResult(c.argInt(0), c.argInt(1), c.argInt(2) != 0, c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6)); } });
        // ------------------------------------------------------------ font
        r.register(LCDUI + "Font.metrics0(I)I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(factory.fontMetrics(c.argInt(0))); } });
        r.register(LCDUI + "Font.charWidth0(IC)I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(factory.charWidth(c.argInt(0), (char) c.argInt(1))); } });
        r.register(LCDUI + "Font.charsWidth0(I[CII)I", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(factory.charsWidth(c.argInt(0), chars(c.heap(), c.argRef(1), c.argInt(2), c.argInt(3)), 0, c.argInt(3))); } });
        r.register(LCDUI + "Font.strWidth0(ILjava/lang/String;II)I", new NativeMethod() { public void invoke(NativeContext c) {
            String str = gs.string(c.argRef(1));
            int off = c.argInt(2), len = c.argInt(3);
            c.retInt(factory.charsWidth(c.argInt(0), str.substring(off, off + len).toCharArray(), 0, len)); } });
        // ------------------------------------------------------------ image
        r.register(LCDUI + "Image.create0(II)I", new NativeMethod() { public void invoke(NativeContext c) {
            int w = c.argInt(1), h = c.argInt(2);
            gs.charge(w * h / 64);
            c.retInt(c.handles().alloc(factory.create(w, h), c.argRef(0))); } });
        r.register(LCDUI + "Image.createArgb0(III)I", new NativeMethod() { public void invoke(NativeContext c) {
            int w = c.argInt(1), h = c.argInt(2);
            gs.charge(w * h / 64);
            c.retInt(c.handles().alloc(factory.createArgb(w, h, c.argInt(3)), c.argRef(0))); } });
        r.register(LCDUI + "Image.decode0([BII)I", new NativeMethod() { public void invoke(NativeContext c) {
            int arr = c.argRef(0), off = c.argInt(1), len = c.argInt(2);
            byte[] b = new byte[len];
            for (int i = 0; i < len; i++) b[i] = c.heap().getByte(arr, off + i);
            gs.charge(len / 16);
            try {
                c.retInt(c.handles().alloc(factory.decode(b, 0, len), 0));
            } catch (IOException e) {
                c.retInt(0);
                gs.log(2, "media", "image cannot be decoded: " + mediaDesc("image/" + sniff(b), len) + " (" + e + ")");
            } catch (RuntimeException e) {
                c.retInt(0);   // a decoder bug on hostile data must not kill the VM
                gs.log(2, "media", "image cannot be decoded: " + mediaDesc("image/" + sniff(b), len) + " (" + e + ")");
            } } });
        r.register(LCDUI + "Image.fromRgb0([IIIZ)I", new NativeMethod() { public void invoke(NativeContext c) {
            int w = c.argInt(2), h = c.argInt(3);
            gs.charge(w * h / 64);
            c.retInt(c.handles().alloc(factory.fromRgb(ints(c.heap(), c.argRef(1), 0, w * h), w, h, c.argInt(4) != 0), c.argRef(0))); } });
        r.register(LCDUI + "Image.copy0(IIIIII)I", new NativeMethod() { public void invoke(NativeContext c) {
            Surface s = surf(c, c.argInt(1));
            gs.charge(c.argInt(4) * c.argInt(5) / 64);
            c.retInt(c.handles().alloc(s.copyRegion(c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6)), c.argRef(0))); } });
        r.register(LCDUI + "Image.size0(I)I", new NativeMethod() { public void invoke(NativeContext c) {
            Surface s = surf(c, c.argInt(0));
            c.retInt((s.width() << 16) | s.height()); } });
        r.register(LCDUI + "Image.own0(ILjavax/microedition/lcdui/Image;)V", new NativeMethod() { public void invoke(NativeContext c) {
            c.handles().setOwner(c.argInt(0), c.argRef(1)); } });
        r.register(LCDUI + "Image.getRGB0(I[IIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            Surface s = surf(c, c.argInt(0));
            int arr = c.argRef(1), off = c.argInt(2), scan = c.argInt(3), x = c.argInt(4), y = c.argInt(5), w = c.argInt(6), h = c.argInt(7);
            int[] tmp = new int[w * h];
            s.getRgb(tmp, 0, w, x, y, w, h);
            Heap hp = c.heap();
            for (int j = 0; j < h; j++) for (int i = 0; i < w; i++) hp.putIntElem(arr, off + j * scan + i, tmp[j * w + i]);
            gs.charge(w * h / 64); } });
        // ------------------------------------------------------------ graphics (all coordinates absolute, clip box [cx0,cx1) x [cy0,cy1))
        r.register(LCDUI + "Graphics.drawLine0(IIIIIIIIII" + "I)V", new NativeMethod() { public void invoke(NativeContext c) {
            surf(c, c.argInt(0)).drawLine(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), c.argInt(7), c.argInt(8), c.argInt(9), c.argInt(10));
            gs.charge((Math.abs(c.argInt(8) - c.argInt(6)) + Math.abs(c.argInt(9) - c.argInt(7))) / 16); } });
        r.register(LCDUI + "Graphics.fillRect0(IIIIIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            surf(c, c.argInt(0)).fillRect(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), c.argInt(7), c.argInt(8), c.argInt(9));
            gs.charge(Math.max(0, c.argInt(8)) * Math.max(0, c.argInt(9)) / 128); } });
        r.register(LCDUI + "Graphics.drawRect0(IIIIIIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            surf(c, c.argInt(0)).drawRect(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), c.argInt(7), c.argInt(8), c.argInt(9), c.argInt(10)); } });
        r.register(LCDUI + "Graphics.drawRoundRect0(IIIIIIIIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            surf(c, c.argInt(0)).drawRoundRect(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), c.argInt(7), c.argInt(8), c.argInt(9), c.argInt(10), c.argInt(11), c.argInt(12)); } });
        r.register(LCDUI + "Graphics.fillRoundRect0(IIIIIIIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            surf(c, c.argInt(0)).fillRoundRect(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), c.argInt(7), c.argInt(8), c.argInt(9), c.argInt(10), c.argInt(11));
            gs.charge(Math.max(0, c.argInt(8)) * Math.max(0, c.argInt(9)) / 128); } });
        r.register(LCDUI + "Graphics.drawArc0(IIIIIIIIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            surf(c, c.argInt(0)).drawArc(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), c.argInt(7), c.argInt(8), c.argInt(9), c.argInt(10), c.argInt(11), c.argInt(12)); } });
        r.register(LCDUI + "Graphics.fillArc0(IIIIIIIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            surf(c, c.argInt(0)).fillArc(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), c.argInt(7), c.argInt(8), c.argInt(9), c.argInt(10), c.argInt(11));
            gs.charge(Math.max(0, c.argInt(8)) * Math.max(0, c.argInt(9)) / 32); } });
        r.register(LCDUI + "Graphics.fillTriangle0(IIIIIIIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            surf(c, c.argInt(0)).fillTriangle(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), c.argInt(7), c.argInt(8), c.argInt(9), c.argInt(10), c.argInt(11));
            gs.charge(64); } });
        r.register(LCDUI + "Graphics.drawChars0(IIIIIII[CIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            int len = c.argInt(9);
            char[] ch = chars(c.heap(), c.argRef(7), c.argInt(8), len);
            surf(c, c.argInt(0)).drawChars(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), ch, 0, len, c.argInt(10), c.argInt(11));
            gs.charge(len * 2); } });
        r.register(LCDUI + "Graphics.drawStr0(IIIIIIILjava/lang/String;IIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            int off = c.argInt(8), len = c.argInt(9);
            char[] ch = gs.string(c.argRef(7)).substring(off, off + len).toCharArray();
            surf(c, c.argInt(0)).drawChars(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), ch, 0, len, c.argInt(10), c.argInt(11));
            gs.charge(len * 2); } });
        r.register(LCDUI + "Graphics.drawRegion0(IIIIIIIIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            Surface src = surf(c, c.argInt(5));
            surf(c, c.argInt(0)).drawSurface(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), src, c.argInt(6), c.argInt(7), c.argInt(8), c.argInt(9), c.argInt(10), c.argInt(11), c.argInt(12));
            gs.charge(c.argInt(8) * c.argInt(9) / 64); } });
        r.register(LCDUI + "Graphics.drawRGB0(IIIII[IIIIIIIZ)V", new NativeMethod() { public void invoke(NativeContext c) {
            int w = c.argInt(10), h = c.argInt(11), off = c.argInt(6), scan = c.argInt(7);
            int[] tmp = new int[w * h];
            Heap hp = c.heap();
            int arr = c.argRef(5);
            for (int j = 0; j < h; j++) for (int i = 0; i < w; i++) tmp[j * w + i] = hp.getIntElem(arr, off + j * scan + i);
            surf(c, c.argInt(0)).drawRgb(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), tmp, 0, w, c.argInt(8), c.argInt(9), w, h, c.argInt(12) != 0);
            gs.charge(w * h / 32); } });
        r.register(LCDUI + "Graphics.copyArea0(IIIIIIIIIII)V", new NativeMethod() { public void invoke(NativeContext c) {
            surf(c, c.argInt(0)).copyArea(c.argInt(1), c.argInt(2), c.argInt(3), c.argInt(4), c.argInt(5), c.argInt(6), c.argInt(7), c.argInt(8), c.argInt(9), c.argInt(10));
            gs.charge(c.argInt(7) * c.argInt(8) / 64); } });
    }

    private static void fill(NativeContext c, int[] e) {
        int arr = c.argRef(0);
        for (int i = 0; i < 4; i++) c.heap().putIntElem(arr, i, e[i]);
    }

    private static char[] chars(Heap h, int arr, int off, int n) {
        char[] r = new char[n];
        for (int i = 0; i < n; i++) r[i] = h.getChar(arr, off + i);
        return r;
    }

    private static String sniff(byte[] b) {
        if (b.length >= 4 && (b[0] & 0xFF) == 0x89 && b[1] == 'P') return "png";
        if (b.length >= 3 && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8) return "jpeg";
        if (b.length >= 3 && b[0] == 'G' && b[1] == 'I' && b[2] == 'F') return "gif";
        if (b.length >= 2 && b[0] == 'B' && b[1] == 'M') return "bmp";
        return "unknown";
    }

    private static String mediaDesc(String mime, int len) { return mime + ", " + len + " bytes"; }
}

package jlkvm.host.desktop;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import jlkvm.ClassSource;
import jlkvm.DeviceProfile;
import jlkvm.HostPlatform;
import jlkvm.host.MidpNatives;
import jlkvm.host.PlatformServices;
import jlkvm.host.ui.ScreenHost;
import jlkvm.host.ui.SurfaceFactory;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/** Headless {@link HostPlatform}: guest console to a PrintStream, MIDP natives over the software raster, no audio, no window. */
public final class DesktopHost implements HostPlatform {
    private final ClassSource rom;
    private final Clock clock;
    private final PrintStream out;
    private final DeviceProfile profile;
    private final Map<String, String> appProps;
    private final PlatformServices services;
    private final SurfaceFactory surfaces;
    private ScreenHost ui = ScreenHost.NONE;
    private jlkvm.host.rms.RecordStorage records;
    private jlkvm.host.audio.AudioDevice audio;
    /** Everything the VM logged ("[tag] message"), for tests and the CLI report. */
    public final List<String> logs = Collections.synchronizedList(new ArrayList<String>());
    public PrintStream logStream;
    /** Everything the guest wrote to System.err (stack traces of uncaught exceptions...). */
    public final StringBuffer guestErr = new StringBuffer();

    public DesktopHost(ClassSource rom, Clock clock, DeviceProfile profile, Map<String, String> appProps, PlatformServices services,
                       SurfaceFactory surfaces, PrintStream guestOut) {
        this.rom = rom; this.clock = clock; this.profile = profile; this.appProps = appProps; this.services = services; this.surfaces = surfaces; this.out = guestOut;
    }

    /** Where the RMS keeps the record stores (T26); default: memory only. Set before the VM runs. */
    public void setRecordStorage(jlkvm.host.rms.RecordStorage r) { this.records = r; }

    /** The audio output (T27); default: a silent logging device on the host clock. */
    public void setAudioDevice(jlkvm.host.audio.AudioDevice d) { this.audio = d; }
    private jlkvm.host.nokia.NokiaDevice nokia;
    private jlkvm.host.SmsNatives sms;
    private jlkvm.host.fs.HostFs fileSystem;
    /** The SMS adapter of WMA (T32a); default: one made from the profile (sms.mode). Set before the VM runs. */
    public void setSms(jlkvm.host.SmsNatives s) { this.sms = s; }
    /** The file system behind JSR-75 FileConnection (T32a); default: in memory. */
    public void setFileSystem(jlkvm.host.fs.HostFs f) { this.fileSystem = f; }
    public void setNokiaDevice(jlkvm.host.nokia.NokiaDevice d) { this.nokia = d; }

    /** The high-level UI (Form / List / TextBox / Alert); set before the VM runs. */
    public void setScreenHost(ScreenHost ui) { this.ui = ui; }

    public long hostNanoTime() { return clock.nanos(); }

    public void log(int level, String tag, String msg) {
        String l = "[" + tag + "] " + msg;
        logs.add(l);
        if (logStream != null) logStream.println(l);
    }

    public ClassSource rom() { return rom; }

    public void registerNatives(NativeRegistry r) {
        r.register("java/lang/ConsoleOutputStream.write0(I[BII)V", new NativeMethod() { public void invoke(NativeContext c) {
            int arr = c.argRef(1), off = c.argInt(2), len = c.argInt(3);
            byte[] b = new byte[len];
            for (int i = 0; i < len; i++) b[i] = c.heap().getByte(arr, off + i);
            if (c.argInt(0) == 2) {
                guestErr.append(new String(b));
                PrintStream e = logStream != null ? logStream : System.err;
                e.write(b, 0, b.length);
                e.flush();
            } else {
                out.write(b, 0, b.length);
                out.flush();
            } } });
        MidpNatives mn = new MidpNatives(profile, appProps, surfaces, services, ui, records != null ? records : new jlkvm.host.rms.MemoryRecordStorage(profile));
        if (audio != null) mn.audioDevice(audio);
        if (nokia != null) mn.nokiaDevice(nokia);
        if (sms != null) mn.sms(sms);
        if (fileSystem != null) mn.fileSystem(fileSystem);
        mn.register(r);
    }
}

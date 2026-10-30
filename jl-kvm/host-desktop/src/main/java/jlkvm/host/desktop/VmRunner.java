package jlkvm.host.desktop;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.concurrent.locks.LockSupport;
import jlkvm.DeviceProfile;
import jlkvm.ExecutionEngine;
import jlkvm.Vm;
import jlkvm.VmConfig;
import jlkvm.host.GameErrorLog;
import jlkvm.host.PlatformServices;
import jlkvm.host.ams.AmsHost;
import jlkvm.host.ams.MidletSuite;
import jlkvm.host.audio.AudioDevice;
import jlkvm.host.fs.HostFs;
import jlkvm.host.nokia.NokiaDevice;
import jlkvm.host.rms.RecordStorage;
import jlkvm.host.ui.ScreenHost;
import jlkvm.host.ui.SoftRaster;
import jlkvm.host.ui.SurfaceFactory;
import jlkvm.profile.ProfileLoader;

/**
 * The real-time VM of a host that has a UI thread (PLAN T35): owns the {@code vm-thread}, the only thread that enters the guest (D13).
 * The Android Activity creates one per game run, supplies its {@link PlatformServices} (frames, vibration, platformRequest), audio,
 * screen host, ... and then talks to the guest only through {@link #ams()} / the input methods, which queue events and wake the thread.
 * Plain Java (no android.*): the same class runs in the JVM tests of the Android glue.
 * <pre>
 *   VmRunner r = new VmRunner(o); r.start();
 *   Activity.onPause  -&gt; r.pause();   onResume -&gt; r.resume();   key/touch -&gt; r.key..(), r.pointer(..)
 *   Activity.onDestroy-&gt; r.stop(true)
 * </pre>
 */
public final class VmRunner {
    /** Everything a run needs; the host supplies the pieces that differ between platforms. */
    public static final class Options {
        public File jar, jad;
        /** Replaces META-INF/MANIFEST.MF of the JAR (the Android installer keeps the JAD + manifest merged); null = the JAR's own. */
        public byte[] manifest;
        /** Device profile JSON text (profiles/*.json); null = the default profile. */
        public String profileJson;
        public File romDir;
        public int midlet = 1;
        public boolean debug;
        public PlatformServices services;
        public SurfaceFactory surfaces = new SoftRaster();
        public ScreenHost screenHost;
        public AudioDevice audio;
        public NokiaDevice nokia;
        public RecordStorage records;
        public HostFs fs;
        public jlkvm.host.SmsNatives sms;
        /** Guest System.out; null = discarded. */
        public PrintStream out;
        /** VM diagnostics for logcat / console ("[tag] msg"); null = none (release). */
        public PrintStream log;
        public GameErrorLog errorLog;
    }

    /** Called on the vm-thread when the run ends. */
    public interface Listener {
        /** @param error null for a normal exit (the MIDlet called notifyDestroyed / the AMS ended) */
        void finished(Throwable error);
    }

    private final Options o;
    private final Object wake = new Object();
    private volatile boolean stopRequested;
    private volatile boolean finished;
    private Vm vm;
    private AmsHost ams;
    private DesktopHost host;
    private MidletSuite suite;
    private Thread thread;
    private Listener listener;
    private volatile jlkvm.host.ams.NokiaJad.Plan nokiaPlan;
    private volatile Throwable failure;

    public VmRunner(Options o) { this.o = o; }

    public void setListener(Listener l) { this.listener = l; }

    /** Builds the VM on the calling thread (so that errors in the JAR / profile / ROM surface immediately) and starts the vm-thread. */
    public synchronized void start() throws IOException {
        if (thread != null) throw new IllegalStateException("started");
        DeviceProfile profile = o.profileJson == null ? new DeviceProfile() : ProfileLoader.fromJson(o.profileJson);
        JarClassSource app = new JarClassSource(o.jar);
        byte[] mf = o.manifest != null ? o.manifest : app.readResource("/META-INF/MANIFEST.MF");
        byte[] jad = o.jad == null ? null : Files.readAllBytes(o.jad.toPath());
        suite = MidletSuite.load(mf, jad);
        MidletSuite.Entry entry = suite.midlet(o.midlet);
        // T30: the device limits and the Nokia JAD attributes the profile honours (the host shows the frame the plan asks for)
        String refused = jlkvm.host.ams.NokiaJad.checkInstall(profile, suite.attributes);
        if (refused != null) throw new IOException("install refused: " + refused);
        nokiaPlan = jlkvm.host.ams.NokiaJad.plan(profile, suite.attributes);
        jlkvm.host.ams.NokiaJad.apply(profile, nokiaPlan);
        VmConfig cfg = new VmConfig();
        cfg.deterministic = false;
        cfg.debug = o.debug;
        cfg.appProperties.putAll(suite.attributes);
        host = new DesktopHost(new RomLayers(o.romDir, profile), new Clock.Real(), profile, cfg.appProperties,
                o.services, o.surfaces, o.out != null ? o.out : new PrintStream(new java.io.OutputStream() { public void write(int b) { } }));
        host.logStream = o.log;
        if (o.records != null) host.setRecordStorage(o.records);
        if (o.audio != null) host.setAudioDevice(o.audio);
        if (o.nokia != null) host.setNokiaDevice(o.nokia);
        if (o.sms != null) host.setSms(o.sms);
        if (o.fs != null) host.setFileSystem(o.fs);
        if (o.screenHost != null) host.setScreenHost(o.screenHost);
        if (o.errorLog != null) {
            host.setErrorLog(o.errorLog);
            o.errorLog.header("device=" + profile.name + " heap=" + profile.heapBytes + " game=" + suite.attributes.get("MIDlet-Name")
                    + " midlet=" + entry.className);
        }
        vm = Vm.create(cfg, host, app, profile);
        ams = new AmsHost(vm);
        vm.launch("jlkvm/internal/Ams", "run", "(Ljava/lang/String;)V", entry.className);
        final JarClassSource fapp = app;
        thread = new Thread(new Runnable() { public void run() { loop(fapp); } }, "vm-thread");
        thread.start();
    }

    private void loop(JarClassSource app) {
        Throwable err = null;
        try {
            while (!stopRequested) {
                int r = vm.runSlice(20000);
                if (r == ExecutionEngine.EXITED) break;
                if (r == ExecutionEngine.IDLE_UNTIL) {
                    long wait = Math.min(vm.idleUntilNanos() - vm.nanoTime(), 20000000L);
                    if (wait > 0) {
                        synchronized (wake) {
                            // a wake-up posted just before is not lost: input methods notify under this lock after queueing
                            if (!pendingWake) { try { wake.wait(wait / 1000000L, (int) (wait % 1000000L)); } catch (InterruptedException e) { break; } }
                            pendingWake = false;
                        }
                    }
                }
            }
        } catch (Throwable t) {
            err = t;
            failure = t;
            host.log(3, "vm", "VM stopped by a host error: " + t + "\n" + stack(t));
        } finally {
            finished = true;
            try { app.close(); } catch (IOException ignored) { }
            if (o.errorLog != null) o.errorLog.close();
            Listener l = listener;
            if (l != null) l.finished(err);
            synchronized (this) { notifyAll(); }
        }
    }

    private static String stack(Throwable t) {
        java.io.StringWriter w = new java.io.StringWriter();
        t.printStackTrace(new java.io.PrintWriter(w));
        return w.toString();
    }

    private boolean pendingWake;

    private void poke() {
        synchronized (wake) { pendingWake = true; wake.notifyAll(); }
    }

    // ------------------------------------------------------------------ host -> guest (any thread; only queue events, D13)

    /** The Nokia JAD plan applied at start (null before {@link #start}). */
    public jlkvm.host.ams.NokiaJad.Plan nokiaPlan() { return nokiaPlan; }
    public AmsHost ams() { return ams; }
    public Vm vm() { return vm; }
    public DesktopHost host() { return host; }
    public MidletSuite suite() { return suite; }
    public boolean isFinished() { return finished; }
    public Throwable failure() { return failure; }

    public void pause() { ams.pause(); poke(); }
    public void resume() { ams.resume(); poke(); }
    public void repaint() { ams.repaint(); poke(); }
    public void displaySize(int w, int h) { ams.displaySize(w, h); poke(); }
    public void keyPressed(int code) { ams.keyPressed(code); poke(); }
    public void keyReleased(int code) { ams.keyReleased(code); poke(); }
    public void keyRepeated(int code) { ams.keyRepeated(code); poke(); }
    public void pointer(int type, int x, int y) { ams.pointer(type, x, y); poke(); }
    /** Any other event of {@code HostEvents} (UI actions, ...). */
    public void post(int type, int a, int b, int c) { vm.postEvent(type, a, b, c); poke(); }

    /** MIDlet.destroyApp then exit (back key: {@code unconditional=false} lets the MIDlet refuse). */
    public void destroy(boolean unconditional) { ams.destroy(unconditional); poke(); }

    /**
     * Ends the run: asks the MIDlet to destroy itself, waits up to {@code graceMs} for the guest to end, then stops the thread.
     * @return true when the thread has ended
     */
    public boolean stop(boolean unconditional, long graceMs) {
        if (thread == null) return true;
        if (!finished) {
            ams.destroy(unconditional);
            poke();
            long end = System.nanoTime() + graceMs * 1000000L;
            synchronized (this) {
                while (!finished) {
                    long left = (end - System.nanoTime()) / 1000000L;
                    if (left <= 0) break;
                    try { wait(left); } catch (InterruptedException e) { break; }
                }
            }
        }
        if (!finished) {
            stopRequested = true;
            vm.requestExit();
            poke();
            LockSupport.unpark(thread);
            try { thread.join(2000); } catch (InterruptedException ignored) { }
        }
        return finished;
    }
}

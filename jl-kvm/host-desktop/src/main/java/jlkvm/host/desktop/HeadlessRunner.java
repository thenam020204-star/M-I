package jlkvm.host.desktop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import jlkvm.DeviceProfile;
import jlkvm.ExecutionEngine;
import jlkvm.Vm;
import jlkvm.VmConfig;
import jlkvm.host.RecordingServices;
import jlkvm.host.ams.MidletSuite;
import jlkvm.host.ui.SoftRaster;
import jlkvm.profile.ProfileLoader;

/**
 * Runs a game JAR on the desktop VM without a window (PLAN T21-T23): AMS (JAD + manifest, MIDlet selection), software raster,
 * scripted input (tests/games/README.md format) on a virtual clock (D14) or on the wall clock. This is the vm-thread: it calls
 * {@code runSlice} in a loop and is the only thread that enters the guest; input is handed over with {@code Vm.postEvent}.
 * <pre>HeadlessRunner [--jad F] [--profile F.json] [--rom-dir DIR] [--script F.inp] [--rms-dir DIR] [--import-jlmod-rms DIR] [--midlet N] [--realtime] [--max-ms N] [--debug] GAME.jar</pre>
 */
public final class HeadlessRunner {
    private HeadlessRunner() {}

    public static final class Options {
        public File jar, jad, profile, script, romDir;
        public int midlet = 1;
        public boolean realtime, debug, gcVerify;
        public int gcStress;
        public long maxVirtualMs = 60000, maxWallMs = 300000;
        /** Guest stdout; null = captured into {@link Result#stdout}. */
        public PrintStream out;
        /** VM diagnostics ("[tag] msg" lines); null = only collected. */
        public PrintStream log;
        public String scriptText;
        /** RMS (T26): the record stores of the run; null = a directory {@link #rmsDir} if set, else memory only. */
        public jlkvm.host.rms.RecordStorage records;
        public File rmsDir;
        /** Audio output (T27); null = a silent {@link jlkvm.host.audio.RecordingAudioDevice} on the virtual clock (returned in {@link Result#audio}). */
        public jlkvm.host.audio.AudioDevice audio;
        /** Nokia DeviceControl features (T28); null = a {@link jlkvm.host.nokia.RecordingNokiaDevice} (returned in {@link Result#nokia}). */
        public jlkvm.host.nokia.NokiaDevice nokia;
        /** A JL-Mod data directory whose record stores are imported before the run. */
        public File importJlMod;
    }

    public static final class Result {
        public int exitKind;   // EXITED / IDLE / SCRIPT_END / TIMEOUT
        public static final int EXITED = 0, IDLE = 1, SCRIPT_END = 2, TIMEOUT = 3;
        public String stdout;
        public final Map<String, String> shots = new LinkedHashMap<String, String>();
        public RecordingServices services;
        public HeadlessUi ui;
        public jlkvm.host.audio.AudioDevice audio;
        public jlkvm.host.nokia.NokiaDevice nokia;
        public Vm vm;
        public DesktopHost host;
        public long virtualMs;
        public MidletSuite suite;
        public jlkvm.host.rms.RecordStorage records;
        public jlkvm.host.rms.JlModRmsImport.Result rmsImport;
    }

    public static File findRomDir() {
        String p = System.getProperty("jlkvm.rom.dir");
        if (p != null) return new File(p);
        for (File d = new File("").getAbsoluteFile(); d != null; d = d.getParentFile()) {
            File r = new File(d, "vm-classlib/build/rom");
            if (new File(r, "rom.jar").isFile()) return r;
        }
        throw new IllegalStateException("ROM directory not found: build it with ':vm-classlib:buildMidpRom' or pass -Djlkvm.rom.dir / --rom-dir");
    }

    public static Result run(Options o) throws Exception {
        DeviceProfile profile = o.profile == null ? new DeviceProfile()
                : ProfileLoader.fromJson(new String(Files.readAllBytes(o.profile.toPath()), "UTF-8"));
        File romDir = o.romDir != null ? o.romDir : findRomDir();
        JarClassSource app = new JarClassSource(o.jar);
        byte[] mf = app.readResource("/META-INF/MANIFEST.MF");
        byte[] jad = o.jad == null ? null : Files.readAllBytes(o.jad.toPath());
        MidletSuite suite = MidletSuite.load(mf, jad);
        MidletSuite.Entry entry = suite.midlet(o.midlet);

        Result res = new Result();
        res.suite = suite;
        ByteArrayOutputStream cap = new ByteArrayOutputStream();
        PrintStream out = o.out != null ? o.out : new PrintStream(cap, true, "UTF-8");
        Clock clock = o.realtime ? new Clock.Real() : new Clock.Virtual();
        VmConfig cfg = new VmConfig();
        cfg.deterministic = !o.realtime;
        cfg.debug = o.debug;
        cfg.gcStressInterval = o.gcStress;
        cfg.gcVerify = o.gcVerify;
        cfg.appProperties.putAll(suite.attributes);
        RecordingServices svc = new RecordingServices();
        res.services = svc;
        DesktopHost host = new DesktopHost(new RomLayers(romDir, profile), clock, profile, cfg.appProperties, svc, new SoftRaster(), out);
        jlkvm.host.rms.RecordStorage rs = o.records;
        if (rs == null) rs = o.rmsDir != null ? new jlkvm.host.rms.FileRecordStorage(o.rmsDir, profile) : new jlkvm.host.rms.MemoryRecordStorage(profile);
        if (o.importJlMod != null) {
            jlkvm.host.rms.JlModRmsImport.Result ir = jlkvm.host.rms.JlModRmsImport.importDir(o.importJlMod, rs, jlkvm.host.RmsNatives.suiteKey(suite.attributes));
            res.rmsImport = ir;
        }
        host.setRecordStorage(rs);
        final Result fres = res;
        java.util.function.LongSupplier vclock = new java.util.function.LongSupplier() {
            public long getAsLong() { return fres.vm == null ? 0 : (fres.vm.nanoTime() - fres.vm.baseNanos()) / 1000000L; } };
        jlkvm.host.audio.AudioDevice ad = o.audio != null ? o.audio : new jlkvm.host.audio.RecordingAudioDevice(vclock);
        if (ad instanceof jlkvm.host.audio.RecordingAudioDevice) ((jlkvm.host.audio.RecordingAudioDevice) ad).setClock(vclock);
        host.setAudioDevice(ad);
        res.audio = ad;
        res.nokia = o.nokia != null ? o.nokia : new jlkvm.host.nokia.RecordingNokiaDevice();
        host.setNokiaDevice(res.nokia);
        res.records = rs;
        host.logStream = o.log;
        HeadlessUi hui = new HeadlessUi();
        host.setScreenHost(hui);
        res.ui = hui;
        res.host = host;
        Vm vm = Vm.create(cfg, host, app, profile);
        res.vm = vm;

        ScriptedInput script = null;
        if (o.script != null || o.scriptText != null) {
            String text = o.scriptText != null ? o.scriptText : new String(Files.readAllBytes(o.script.toPath()), "UTF-8");
            script = ScriptedInput.parse(text, profile.keyRepeatDelayMs, profile.keyRepeatIntervalMs);
        }
        vm.launch("jlkvm/internal/Ams", "run", "(Ljava/lang/String;)V", entry.className);

        final long base = vm.baseNanos();
        long wallEnd = System.nanoTime() + o.maxWallMs * 1000000L;
        long endVirtual = o.maxVirtualMs * 1000000L;
        long lastScript = script != null && !script.entries.isEmpty() ? script.entries.get(script.entries.size() - 1).ms * 1000000L : 0;
        boolean quit = false;
        for (;;) {
            ScriptedInput.Entry e = script == null ? null : script.peek();
            vm.hostEventAtNanos = e == null ? Long.MAX_VALUE : base + e.ms * 1000000L;
            int r = vm.runSlice(20000);
            if (r == ExecutionEngine.EXITED) { res.exitKind = Result.EXITED; break; }
            long now = vm.nanoTime() - base;
            res.virtualMs = now / 1000000L;
            boolean posted = false;
            while ((e = script == null ? null : script.peek()) != null && e.ms * 1000000L <= now) {
                script.take();
                if (e.type == ScriptedInput.QUIT) { quit = true; break; }
                if (e.type == ScriptedInput.SHOT) { settle(vm); res.shots.put(e.name, shotHash(svc)); }
                else if (e.type == ScriptedInput.AUDIO) {
                    settle(vm);
                    String[] w = e.name.split("\\s+");
                    if (w.length == 2 && w[0].equals("end") && ad instanceof jlkvm.host.audio.RecordingAudioDevice) {
                        ((jlkvm.host.audio.RecordingAudioDevice) ad).finishClip(Integer.parseInt(w[1]));
                        posted = true;
                    } else throw new IllegalArgumentException("script: audio " + e.name);
                }
                else if (e.type == ScriptedInput.UI) { settle(vm); if (hui.perform(e.name, vm)) posted = true; }
                else { vm.postEvent(e.type, e.a, e.b, 0); posted = true; }
            }
            if (posted) continue;   // let the guest take the input before anything else is decided
            if (quit) { vm.requestExit(); res.exitKind = Result.SCRIPT_END; break; }
            if (r == ExecutionEngine.IDLE_UNTIL) {
                if (!o.realtime) {
                    if (script == null || script.peek() == null) {
                        // deterministic: the scheduler already jumped to every timer; an idle VM with no input left will never wake up
                        res.exitKind = script == null ? Result.IDLE : Result.SCRIPT_END;
                        break;
                    }
                } else {
                    long wait = Math.min(vm.idleUntilNanos() - vm.nanoTime(), 20000000L);
                    ScriptedInput.Entry n = script == null ? null : script.peek();
                    if (n != null) wait = Math.min(wait, base + n.ms * 1000000L - vm.nanoTime());
                    if (wait > 0) Thread.sleep(wait / 1000000L, (int) (wait % 1000000L));
                }
            }
            if (!o.realtime) ((Clock.Virtual) clock).advance(0);
            if (now > endVirtual + lastScript || System.nanoTime() > wallEnd) { res.exitKind = Result.TIMEOUT; break; }
        }
        out.flush();
        res.stdout = o.out == null ? cap.toString("UTF-8") : null;
        app.close();
        return res;
    }

    /** Runs until no guest thread is runnable at the current (virtual) time. */
    private static void settle(Vm vm) {
        for (int i = 0; i < 5000; i++) if (vm.runSlice(20000) != ExecutionEngine.RUNNING) return;
    }

    private static String shotHash(RecordingServices svc) throws Exception {
        int[] f = svc.lastFrame;
        if (f == null) return "no-frame";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] b = new byte[4];
        for (int v : f) { b[0] = (byte) (v >>> 24); b[1] = (byte) (v >>> 16); b[2] = (byte) (v >>> 8); b[3] = (byte) v; md.update(b); }
        StringBuilder sb = new StringBuilder();
        for (byte x : md.digest()) sb.append(String.format("%02x", x & 0xFF));
        return sb.toString();
    }

    public static void main(String[] a) throws Exception {
        Options o = new Options();
        for (int i = 0; i < a.length; i++) {
            if (a[i].equals("--jad")) o.jad = new File(a[++i]);
            else if (a[i].equals("--profile")) o.profile = new File(a[++i]);
            else if (a[i].equals("--rom-dir")) o.romDir = new File(a[++i]);
            else if (a[i].equals("--script")) o.script = new File(a[++i]);
            else if (a[i].equals("--midlet")) o.midlet = Integer.parseInt(a[++i]);
            else if (a[i].equals("--rms-dir")) o.rmsDir = new File(a[++i]);
            else if (a[i].equals("--import-jlmod-rms")) o.importJlMod = new File(a[++i]);
            else if (a[i].equals("--realtime")) o.realtime = true;
            else if (a[i].equals("--max-ms")) o.maxVirtualMs = Long.parseLong(a[++i]);
            else if (a[i].equals("--debug")) o.debug = true;
            else o.jar = new File(a[i]);
        }
        if (o.jar == null) { System.err.println("usage: HeadlessRunner [--jad F] [--profile F.json] [--rom-dir DIR] [--script F.inp] [--rms-dir DIR] [--import-jlmod-rms DIR] [--midlet N] [--realtime] [--max-ms N] [--debug] GAME.jar"); System.exit(2); }
        o.out = System.out;
        o.log = System.err;
        Result r = run(o);
        for (String d : r.ui.dumps) for (String l : d.split("\n")) System.out.println("# ui " + l);
        for (String e : r.ui.errors) System.out.println("# ui error: " + e);
        for (Map.Entry<String, String> s : r.shots.entrySet()) System.out.println("# shot " + s.getKey() + " " + s.getValue());
        System.out.println("# ended: " + new String[] {"exited", "idle", "script-end", "timeout"}[r.exitKind] + " at " + r.virtualMs + " ms, frames=" + r.services.frames);
        System.exit(r.exitKind == Result.TIMEOUT ? 124 : 0);
    }
}

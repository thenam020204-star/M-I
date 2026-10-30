package jlkvm.tmp;

import java.util.jar.Attributes;
import java.util.jar.JarFile;
import jlkvm.DeviceProfile;
import jlkvm.ExecutionEngine;
import jlkvm.Vm;
import jlkvm.VmConfig;

/**
 * TEST-ONLY command line runner (PLAN T11): {@code RunJar ROM.jar APP.jar [--cldc10] [--lenient] [--debug] [--timeout SEC] [--noverify]}.
 * Runs the MIDlet named by MIDlet-1 on the project's VM; guest console output goes to stdout, VM diagnostics to stderr.
 */
public final class RunJar {
    public static void main(String[] a) throws Exception {
        String rom = a[0], app = a[1];
        boolean cldc10 = false, noverify = false, lenient = false, debug = false;
        long timeout = 60;
        int stress = 0; boolean gcVerify = false, stats = false; long heapKb = 32 << 10;
        final String[] recrep = new String[2]; String rec = null, rep = null; boolean logEx = false, prof = false; int ring = 0;
        for (int i = 2; i < a.length; i++) {
            if (a[i].equals("--cldc10")) cldc10 = true;
            else if (a[i].equals("--lenient")) lenient = true;
            else if (a[i].equals("--debug")) debug = true;
            else if (a[i].equals("--noverify")) noverify = true;
            else if (a[i].equals("--timeout")) timeout = Long.parseLong(a[++i]);
            else if (a[i].equals("--gc-stress")) stress = Integer.parseInt(a[++i]);
            else if (a[i].equals("--gc-verify")) gcVerify = true;
            else if (a[i].equals("--gc-stats")) stats = true;
            else if (a[i].equals("--heap-kb")) heapKb = Long.parseLong(a[++i]);
            else if (a[i].equals("--deterministic")) deterministic = true;
            else if (a[i].equals("--seed")) hashSeed = Integer.parseInt(a[++i]);
            else if (a[i].equals("--record")) rec = a[++i];
            else if (a[i].equals("--replay")) rep = a[++i];
            else if (a[i].equals("--log-exceptions")) logEx = true;
            else if (a[i].equals("--profile")) prof = true;
            else if (a[i].equals("--trace-ring")) ring = Integer.parseInt(a[++i]);
        }
        final String frec = rec, frep = rep; final boolean fle = logEx, fprof = prof; final int fring = ring;
        configHook = new ConfigHook() { public void apply(VmConfig c) {
            c.recordInput = frec; c.replayInput = frep; c.logExceptions = fle; c.countBytecodes = fprof; c.profileAlloc = fprof; c.traceRing = fring; } };
        final boolean fprof2 = prof, fstats = stats;
        boolean ok = run(rom, app, cldc10, noverify, lenient, debug, timeout, System.out, stress, gcVerify, heapKb << 10,
                (stats || prof) ? new Inspector() { public void inspect(Vm vm) {
                    if (fprof2 && vm.debug != null) System.err.println(vm.debug.report(15));
                    if (!fstats) return;
                    System.err.println("gc: collections=" + vm.gc.collections + " total=" + vm.gc.totalNanos / 1000000 + "ms max=" + vm.gc.maxNanos / 1000 + "us handles=" + vm.handles.liveCount()
                        + " allocatedWords=" + vm.heap.allocatedWordsTotal() + " objects=" + vm.heap.allocatedObjectsTotal());
                } } : null);
        System.out.flush();
        String sf = System.getProperty("jlkvm.opstats.file");
        if (sf != null) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < 256; i++) if (jlkvm.interp.Interpreter.OPSTATS[i] > 0) b.append(i).append('\n');
            java.nio.file.Files.write(java.nio.file.Paths.get(sf), b.toString().getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
        }
        System.exit(ok ? 0 : 124);
    }

    /** Runs the MIDlet of {@code app} to completion; guest console output goes to {@code out}. @return false on timeout. */
    public static boolean run(String rom, String app, boolean cldc10, boolean noverify, boolean lenient, boolean debug,
                              long timeoutSec, java.io.PrintStream out) throws Exception {
        return run(rom, app, cldc10, noverify, lenient, debug, timeoutSec, out, 0, false, 32L << 20, null);
    }

    /** Extra MIDlet properties for the next runs (benchmarks pass the workload name this way). */
    public static final java.util.Map<String, String> extraProps = new java.util.HashMap<String, String>();

    /** T14 (D14): deterministic mode (virtual clock) and identity-hash seed for the next runs; hashSeed < 0 = default. */
    public static boolean deterministic;
    public static int hashSeed = -1;

    /** Extra configuration of the next runs (T16 options: record/replay, profiler...) and a callback when the Vm exists. */
    public interface ConfigHook { void apply(VmConfig c); }
    public static ConfigHook configHook;
    public static Inspector onCreate;

    /** Receives the VM after the run (tests inspect handles, collector statistics...). */
    public interface Inspector { void inspect(Vm vm); }

    public static boolean run(String rom, String app, boolean cldc10, boolean noverify, boolean lenient, boolean debug,
                              long timeoutSec, java.io.PrintStream out, int gcStress, boolean gcVerify, long heapBytes,
                              Inspector inspector) throws Exception {
        VmConfig cfg = new VmConfig();
        cfg.gcStressInterval = gcStress;
        cfg.gcVerify = gcVerify;
        cfg.deterministic = deterministic;
        if (hashSeed >= 0) cfg.hashSeed = hashSeed;
        DeviceProfile p = new DeviceProfile();
        p.heapBytes = (int) heapBytes;
        if (cldc10) { p.cldc11 = false; cfg.verifyRom = false; /* the stub ROM is a CLDC 1.1 library */ }
        if (lenient) cfg.strictVerify = false;
        if (debug) cfg.debug = true;
        if (noverify) { cfg.verifyApp = false; cfg.verifyRom = false; }
        JarFile jf = new JarFile(app);
        Attributes at = jf.getManifest().getMainAttributes();
        for (Object k : at.keySet()) cfg.appProperties.put(k.toString(), at.getValue(k.toString()).trim());   // MIDP: leading/trailing blanks of attribute values are ignored
        jf.close();
        cfg.appProperties.putAll(extraProps);
        if (configHook != null) configHook.apply(cfg);
        String m1 = cfg.appProperties.get("MIDlet-1");
        String cls = m1.substring(m1.lastIndexOf(',') + 1).trim();
        TempHost host = new TempHost(new JarSource(rom), cfg);
        host.stdout = out;
        Vm vm = Vm.create(cfg, host, new JarSource(app), p);
        if (onCreate != null) onCreate.inspect(vm);
        vm.launch("javax/microedition/midlet/MIDletBoot", "run", "(Ljava/lang/String;)V", cls);
        long end = System.nanoTime() + timeoutSec * 1000000000L;
        for (;;) {
            int r = vm.runSlice(20000);
            if (r == ExecutionEngine.EXITED) { if (inspector != null) inspector.inspect(vm); return true; }
            if (System.nanoTime() > end) { System.err.println("TIMEOUT"); return false; }
            if (r == ExecutionEngine.IDLE_UNTIL) {
                long ns = vm.idleUntilNanos() - System.nanoTime();
                if (ns > 0) Thread.sleep(Math.min(ns / 1000000L + 1, 20));
            }
        }
    }
}

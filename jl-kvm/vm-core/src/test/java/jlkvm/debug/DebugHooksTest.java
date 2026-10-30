package jlkvm.debug;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import jlkvm.Corpus;
import jlkvm.Vm;
import jlkvm.VmConfig;
import jlkvm.tmp.RunJar;
import jlkvm.tmp.TempHost;
import org.junit.After;
import org.junit.Test;

/** T16: the debug build has the logs and counters (class load, exception with guest stack, GC, bytecodes per method, allocation sites, trace ring). */
public class DebugHooksTest {
    static final File ROM = new File(Corpus.ROOT, "vm-core/build/guest-stub/rom.jar");

    @After
    public void reset() { RunJar.configHook = null; }

    static File jar(String id) { return new File(Corpus.ROOT, "tests/vmspec/build/vmspec/" + id + "/" + id + ".jar"); }

    @Test
    public void exceptionLogProfilerCountersAndRing() throws Exception {
        File jar = jar("g01_gc_stress");
        File ex = jar("e01_uncaught_exception");
        assumeTrue("run tests/vmspec/run_vmspec.sh --engine jlkvm first", jar.isFile() && ex.isFile() && ROM.isFile());
        RunJar.configHook = new RunJar.ConfigHook() { public void apply(VmConfig c) {
            c.logExceptions = true; c.countBytecodes = true; c.profileAlloc = true; c.traceRing = 64; } };
        final String[] report = new String[1];
        final java.util.List<String> logs = new java.util.ArrayList<String>();
        final long[] gcs = new long[1];
        RunJar.run(ROM.getPath(), ex.getPath(), false, false, false, true, 60, new PrintStream(new ByteArrayOutputStream()), 0, false, 32L << 20,
                new RunJar.Inspector() { public void inspect(Vm vm) { report[0] = vm.debug.report(40); logs.addAll(((TempHost) vm.host).logs); } });
        StringBuilder all = new StringBuilder();
        for (String l : logs) all.append(l).append('\n');
        String log = all.toString();
        assertTrue(log, log.contains("[link] loaded vmspec/E01UncaughtException"));
        assertTrue(log, log.contains("[exception] thread"));
        assertTrue("exception log has the guest stack: " + log, log.contains("\tat vmspec.E01UncaughtException"));
        assertTrue("trace ring dump before an uncaught exception: " + log, log.contains("last instructions before the uncaught exception"));
        assertTrue(report[0], report[0].contains("bytecodes by method:") && report[0].contains("E01UncaughtException"));
        assertTrue(report[0], report[0].contains("allocation sites by bytes:"));
        // GC log + allocation profile with a small heap
        logs.clear();
        RunJar.run(ROM.getPath(), jar.getPath(), false, false, false, true, 120, new PrintStream(new ByteArrayOutputStream()), 0, false, 3L << 20,
                new RunJar.Inspector() { public void inspect(Vm vm) { report[0] = vm.debug.report(5); logs.addAll(((TempHost) vm.host).logs); gcs[0] = vm.gc.collections; } });
        int gcLines = 0;
        for (String l : logs) if (l.startsWith("[gc] #")) gcLines++;
        assertEquals(gcs[0], gcLines);
        assertTrue(gcLines > 5);
        assertTrue(report[0], report[0].contains(" pc=") && report[0].contains("line"));
    }
}

package jlkvm.thread;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import jlkvm.Corpus;
import jlkvm.Vm;
import jlkvm.tmp.RunJar;
import org.junit.After;
import org.junit.Test;

/**
 * T14 acceptance on the project VM: producer-consumer, deliberate deadlock (debug report), ten runs with the same seed give
 * byte-identical output (deterministic mode, D14), strict priorities. Needs tests/vmspec/run_vmspec.sh --engine jlkvm (builds the jars).
 */
public class ThreadTest {
    static final File BUILT = new File(Corpus.ROOT, "tests/vmspec/build/vmspec");
    static final File ROM = new File(Corpus.ROOT, "vm-core/build/guest-stub/rom.jar");

    @After
    public void reset() { RunJar.deterministic = false; RunJar.hashSeed = -1; }

    private static String run(String id, boolean debug, RunJar.Inspector in) throws Exception {
        File jar = new File(BUILT, id + "/" + id + ".jar");
        assumeTrue("run tests/vmspec/run_vmspec.sh --engine jlkvm first", jar.isFile() && ROM.isFile());
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        assertTrue("timeout", RunJar.run(ROM.getPath(), jar.getPath(), false, false, false, debug, 120, new PrintStream(bo, true, "UTF-8"), 0, false, 32L << 20, in));
        return new String(bo.toByteArray(), "UTF-8");
    }

    @Test
    public void producerConsumer() throws Exception {
        String out = run("p01_producer_consumer", false, null);
        assertTrue(out, out.contains("> sequence 1 2 3 4 5 6 7 8 9 10 11 12\n"));
        assertTrue(out, out.contains("> taken 60 sum 6630\n"));
        assertTrue(out, out.contains("> blocked consumer interrupted\n"));
    }

    @Test
    public void deadlockIsReportedInDebug() throws Exception {
        final String[] report = new String[1];
        run("p02_deadlock", true, new RunJar.Inspector() { public void inspect(Vm vm) { report[0] = vm.lastDeadlock; } });
        assertNotNull("debug VM must report the deadlock", report[0]);
        assertTrue(report[0], report[0].startsWith("DEADLOCK: 2 thread(s)"));
        assertTrue(report[0], report[0].contains("held by thread"));
        assertTrue("guest stack in the report: " + report[0], report[0].contains("P02Main"));
    }

    @Test
    public void deadlockNotReportedInRelease() throws Exception {
        final String[] report = new String[] {"x"};
        run("p02_deadlock", false, new RunJar.Inspector() { public void inspect(Vm vm) { report[0] = vm.lastDeadlock; } });
        assertNull(report[0]);
    }

    @Test
    public void sameSeedTenRunsIdentical() throws Exception {
        RunJar.deterministic = true;
        RunJar.hashSeed = 12345;
        String first = run("p03_threads_schedule", false, null);
        assertTrue(first, first.contains("# A done") && first.contains("> finished"));
        for (int i = 1; i < 10; i++) assertEquals("run " + i, first, run("p03_threads_schedule", false, null));
        RunJar.hashSeed = 54321;   // another seed: same schedule, other identity hashes
        String other = run("p03_threads_schedule", false, null);
        assertNotEquals(first, other);
        assertEquals(first.substring(0, first.indexOf("# hash")), other.substring(0, other.indexOf("# hash")));
    }

    @Test
    public void priorityOrder() throws Exception {
        // E (priority 6) runs before A/B/C (5) and D (4) finishes last of the busy threads
        RunJar.deterministic = true;
        String out = run("p03_threads_schedule", false, null);
        assertTrue(out, out.indexOf("# E step 0") < out.indexOf("# A step 0"));
        assertTrue(out, out.indexOf("# A done") < out.indexOf("# D done"));
    }
}

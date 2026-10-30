package jlkvm.thread;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import jlkvm.Corpus;
import jlkvm.tmp.RunJar;
import org.junit.After;
import org.junit.Test;

/**
 * T22a: java.util.Timer / TimerTask on the project VM in deterministic mode (virtual clock, D14): the case tm01_timer prints the same
 * lines (including the measured times of the "# " lines) ten times in a row, equal to expected.txt. Needs run_vmspec.sh --engine jlkvm.
 */
public class TimerTest {
    @After
    public void reset() { RunJar.deterministic = false; RunJar.hashSeed = -1; }

    private static String run() throws Exception {
        File jar = new File(ThreadTest.BUILT, "tm01_timer/tm01_timer.jar");
        assumeTrue("run tests/vmspec/run_vmspec.sh --engine jlkvm first", jar.isFile() && ThreadTest.ROM.isFile());
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        assertTrue("timeout", RunJar.run(ThreadTest.ROM.getPath(), jar.getPath(), false, false, false, false, 120, new PrintStream(bo, true, "UTF-8"), 0, false, 32L << 20, null));
        return new String(bo.toByteArray(), "UTF-8");
    }

    @Test
    public void deterministicTenRunsIdenticalAndEqualToExpected() throws Exception {
        RunJar.deterministic = true;
        RunJar.hashSeed = 777;
        String first = run();
        StringBuilder exp = new StringBuilder();
        for (String l : first.split("\n")) if (l.startsWith("> ")) exp.append(l).append('\n');
        String expected = new String(java.nio.file.Files.readAllBytes(new File(Corpus.ROOT, "tests/vmspec/cases/tm01_timer/expected.txt").toPath()), "UTF-8");
        assertEquals(expected, exp.toString());
        // the virtual clock makes the schedule exact: fixed rate runs on the 100 ms grid, the slow first run is caught up at once
        assertTrue(first, first.contains("# one-shot start-t0=200\n"));
        assertTrue(first, first.contains("# rate starts 100 350 350 400 500\n"));
        assertTrue(first, first.contains("# delay starts 100 350 450 550\n"));
        for (int i = 1; i < 10; i++) assertEquals("run " + i, first, run());
    }
}

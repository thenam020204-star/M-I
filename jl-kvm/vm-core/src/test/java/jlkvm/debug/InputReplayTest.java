package jlkvm.debug;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import jlkvm.Corpus;
import jlkvm.Vm;
import jlkvm.VmConfig;
import jlkvm.tmp.RunJar;
import jlkvm.tmp.TempHost;
import org.junit.After;
import org.junit.Test;

/**
 * T16 / D14 acceptance: a run with input events posted by the host at real times is recorded (deterministic mode, virtual clock);
 * replaying the log reproduces the whole run byte for byte, also a second time; the host's live events are ignored while replaying.
 */
public class InputReplayTest {
    static final File JAR = new File(Corpus.ROOT, "tests/vmspec/build/vmspec/d01_input_replay/d01_input_replay.jar");
    static final File ROM = new File(Corpus.ROOT, "vm-core/build/guest-stub/rom.jar");

    @After
    public void reset() { RunJar.deterministic = false; RunJar.hashSeed = -1; RunJar.configHook = null; RunJar.onCreate = null; }

    private static String run(final String rec, final String rep, final boolean post) throws Exception {
        assumeTrue("run tests/vmspec/run_vmspec.sh --engine jlkvm first", JAR.isFile() && ROM.isFile());
        RunJar.deterministic = true;
        RunJar.hashSeed = 7;
        RunJar.configHook = new RunJar.ConfigHook() { public void apply(VmConfig c) { c.recordInput = rec; c.replayInput = rep; } };
        RunJar.onCreate = new RunJar.Inspector() { public void inspect(final Vm vm) {
            if (!post) return;
            new Thread() { public void run() {
                try {
                    int[][] evs = {{1, 10, 20, 0}, {2, 3, 4, 5}, {1, 77, 1, 9}, {3, 0, 0, 0}, {0, 0, 0, 0}};
                    for (int[] e : evs) { Thread.sleep(40); vm.postEvent(e[0], e[1], e[2], e[3]); }
                } catch (InterruptedException x) { }
            } }.start();
        } };
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        assertTrue("timeout", RunJar.run(ROM.getPath(), JAR.getPath(), false, false, false, false, 60, new PrintStream(bo, true, "UTF-8"), 0, false, 32L << 20, null));
        return new String(bo.toByteArray(), "UTF-8");
    }

    @Test
    public void recordedRunIsReplayedExactly() throws Exception {
        File log = File.createTempFile("input", ".log");
        try {
            String recorded = run(log.getPath(), null, true);
            assertTrue(recorded, recorded.contains("# events 4 sum"));
            java.util.List<String> lines = Files.readAllLines(log.toPath());
            assertEquals(lines.toString(), 5, lines.size());
            String r1 = run(null, log.getPath(), false);
            assertEquals(recorded, r1);
            String r2 = run(null, log.getPath(), true);   // live events are dropped while replaying
            assertEquals(recorded, r2);
        } finally {
            log.delete();
        }
    }
}

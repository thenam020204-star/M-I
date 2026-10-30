package jlkvm.natives;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import jlkvm.Corpus;
import jlkvm.Vm;
import jlkvm.tmp.RunJar;
import jlkvm.tmp.TempHost;
import org.junit.Test;

/**
 * T15 acceptance on the project VM: sample natives of the test host (every argument/result kind), a missing native (reported at
 * class load in debug, guest Error on the call), and blocking natives that park only the calling guest thread, also while the
 * collector compacts the heap (the argument array of a parked call stays valid). Needs run_vmspec.sh --engine jlkvm first.
 */
public class NativesTest {
    static final File JAR = new File(Corpus.ROOT, "tests/vmspec/build/vmspec/q01_natives/q01_natives.jar");
    static final File ROM = new File(Corpus.ROOT, "vm-core/build/guest-stub/rom.jar");

    private static String run(boolean debug, int stress, boolean verify, long heapBytes, final java.util.List<String> missing, final java.util.List<String> logs) throws Exception {
        assumeTrue("run tests/vmspec/run_vmspec.sh --engine jlkvm first", JAR.isFile() && ROM.isFile());
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        assertTrue("timeout", RunJar.run(ROM.getPath(), JAR.getPath(), false, false, false, debug, 120, new PrintStream(bo, true, "UTF-8"), stress, verify, heapBytes,
                new RunJar.Inspector() { public void inspect(Vm vm) {
                    missing.addAll(vm.missingNatives);
                    logs.addAll(((TempHost) vm.host).logs);
                } }));
        return new String(bo.toByteArray(), "UTF-8");
    }

    private static String expected() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String s : new String(Files.readAllBytes(new File(Corpus.ROOT, "tests/vmspec/cases/q01_natives/expected.txt").toPath()), "UTF-8").split("\n")) sb.append(s).append('\n');
        return sb.toString();
    }

    private static String compared(String out) {
        StringBuilder sb = new StringBuilder();
        for (String s : out.split("\n")) if (s.startsWith("> ")) sb.append(s).append('\n');
        return sb.toString();
    }

    @Test
    public void sampleNativesAndBlockers() throws Exception {
        java.util.List<String> missing = new java.util.ArrayList<String>(), logs = new java.util.ArrayList<String>();
        assertEquals(expected(), compared(run(true, 0, false, 32L << 20, missing, logs)));
        assertTrue("missing natives: " + missing, missing.contains("vmspec/Nat.nothing()V"));
        boolean logged = false;
        for (String l : logs) if (l.contains("missing native vmspec/Nat.nothing()V")) logged = true;
        assertTrue("debug build must log the missing native at class load: " + logs, logged);
    }

    @Test
    public void releaseDoesNotLogMissingNative() throws Exception {
        java.util.List<String> missing = new java.util.ArrayList<String>(), logs = new java.util.ArrayList<String>();
        run(false, 0, false, 32L << 20, missing, logs);
        for (String l : logs) assertFalse(l, l.contains("missing native"));
    }

    @Test
    public void parkedCallSurvivesCollections() throws Exception {
        java.util.List<String> missing = new java.util.ArrayList<String>(), logs = new java.util.ArrayList<String>();
        // 1 MB heap: the allocating thread forces many collections (with full verification) while the others are parked in natives
        assertEquals(expected(), compared(run(false, 0, true, 1L << 20, missing, logs)));
    }

    @Test(expected = IllegalStateException.class)
    public void duplicateRegistrationIsRejected() {
        NativeRegistry r = new NativeRegistry();
        NativeMethod m = new NativeMethod() { public void invoke(NativeContext c) { } };
        r.register("a/B.m()V", m);
        r.register("a/B.m()V", m);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badKeyIsRejected() {
        new NativeRegistry().register("a/B.m", new NativeMethod() { public void invoke(NativeContext c) { } });
    }
}

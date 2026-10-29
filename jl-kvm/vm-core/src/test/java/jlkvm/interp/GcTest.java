package jlkvm.interp;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import jlkvm.Corpus;
import jlkvm.Vm;
import jlkvm.tmp.RunJar;
import org.junit.Test;

/**
 * T13 acceptance, on the project VM (in process): the 1,000,000-allocation random-graph stress with a small heap (checksums must
 * equal the oracles'), identity hashes across compaction, WeakReference, and the host handle table returning to 0.
 * Needs tests/vmspec/run_vmspec.sh (builds the jars) and vm-core/src/test/guest-stub/build.sh (temporary ROM).
 */
public class GcTest {
    static final File BUILT = new File(Corpus.ROOT, "tests/vmspec/build/vmspec"), CASES = new File(Corpus.ROOT, "tests/vmspec/cases");
    static final File ROM = new File(Corpus.ROOT, "vm-core/build/guest-stub/rom.jar");

    static List<String> expected(String id) throws Exception {
        List<String> l = new ArrayList<String>();
        for (String s : new String(Files.readAllBytes(new File(CASES, id + "/expected.txt").toPath()), "UTF-8").split("\n")) if (s.startsWith("> ")) l.add(s);
        return l;
    }

    static final class Result { List<String> lines = new ArrayList<String>(); long collections, maxPauseNanos; int handles; boolean done; }

    static Result run(String id, int stress, boolean verify, long heapBytes) throws Exception {
        File jar = new File(BUILT, id + "/" + id + ".jar");
        assumeTrue("run tests/vmspec/run_vmspec.sh --engine jlkvm first", jar.isFile() && ROM.isFile());
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        final Result r = new Result();
        r.done = RunJar.run(ROM.getPath(), jar.getPath(), false, false, false, false, 300, new PrintStream(bo, true, "UTF-8"), stress, verify, heapBytes,
                new RunJar.Inspector() { public void inspect(Vm vm) { r.collections = vm.gc.collections; r.maxPauseNanos = vm.gc.maxNanos; r.handles = vm.handles.liveCount(); } });
        for (String l : new String(bo.toByteArray(), "UTF-8").split("\n")) if (l.startsWith("> ")) r.lines.add(l);
        return r;
    }

    @Test
    public void millionAllocationsRandomGraphChecksum() throws Exception {
        Result r = run("g01_gc_stress", 0, true, 3L << 20);
        assertTrue("timeout", r.done);
        assertEquals(expected("g01_gc_stress"), r.lines);
        System.out.println("GcTest g01: collections=" + r.collections + " maxPause=" + r.maxPauseNanos / 1000 + "us (3 MB heap, gcVerify on)");
        assertTrue("the collector must have run many times: " + r.collections, r.collections >= 10);
    }

    @Test
    public void identityHashSurvivesCompaction() throws Exception {
        Result r = run("g02_gc_identity_hash", 0, true, 1L << 20);
        assertEquals(expected("g02_gc_identity_hash"), r.lines);
        assertTrue(r.collections >= 6);
    }

    @Test
    public void weakReferences() throws Exception {
        Result r = run("g03_gc_weak_reference", 0, true, 1L << 20);
        assertEquals(expected("g03_gc_weak_reference"), r.lines);
        assertTrue(r.collections >= 2);
    }

    @Test
    public void handleTableReturnsToZero() throws Exception {
        Result r = run("g04_gc_handles", 0, true, 32L << 20);
        assertEquals(expected("g04_gc_handles"), r.lines);
        assertEquals("host handles must be 0 after everything is dropped", 0, r.handles);
    }

    @Test
    public void threadsParkedInEveryState() throws Exception {
        for (int stress : new int[] {0, 1}) {
            Result r = run("g05_gc_threads", stress, true, 1L << 20);
            assertTrue("timeout (stress " + stress + ")", r.done);
            assertEquals(expected("g05_gc_threads"), r.lines);
            assertTrue(r.collections >= 20);
        }
    }

    @Test
    public void outOfMemoryAfterCollectionThenRecovers() throws Exception {
        Result r = run("g06_gc_oom", 0, false, 32L << 20);
        assertEquals(expected("g06_gc_oom"), r.lines);
        assertTrue(r.collections >= 1);
    }

    @Test
    public void everyAllocationCollects() throws Exception {
        // a collection before each allocation + full heap validation: finds a missing root or a wrong type map immediately
        Result r = run("g03_gc_weak_reference", 1, true, 4L << 20);
        assertEquals(expected("g03_gc_weak_reference"), r.lines);
        r = run("g02_gc_identity_hash", 1, true, 4L << 20);
        assertEquals(expected("g02_gc_identity_hash"), r.lines);
        assertTrue(r.collections > 1000);
    }
}

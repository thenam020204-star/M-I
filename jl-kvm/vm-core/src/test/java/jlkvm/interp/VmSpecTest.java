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
import jlkvm.tmp.RunJar;
import org.junit.Test;

/**
 * T11: every tests/vmspec case, run on this project's VM (in process), must print exactly the "> " lines of expected.txt.
 * Needs tests/vmspec/run_vmspec.sh (builds the jars) and vm-core/src/test/guest-stub/build.sh (temporary ROM) to have been run.
 */
public class VmSpecTest {
    @Test
    public void corpusMatchesExpected() throws Exception {
        File cases = new File(Corpus.ROOT, "tests/vmspec/cases"), built = new File(Corpus.ROOT, "tests/vmspec/build/vmspec");
        File rom = new File(Corpus.ROOT, "vm-core/build/guest-stub/rom.jar");
        assumeTrue("run tests/vmspec/run_vmspec.sh --engine jlkvm first", built.isDirectory() && rom.isFile());
        StringBuilder bad = new StringBuilder();
        int n = 0;
        for (File c : cases.listFiles()) {
            File jar = new File(built, c.getName() + "/" + c.getName() + ".jar"), exp = new File(c, "expected.txt");
            if (!jar.isFile() || !exp.isFile()) continue;
            String meta = new String(Files.readAllBytes(new File(c, "meta.txt").toPath()), "UTF-8");
            boolean cldc10 = meta.contains("cldc: cldc10"), noverify = meta.contains("--noverify");
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(bo, true, "UTF-8");
            boolean done = RunJar.run(rom.getPath(), jar.getPath(), cldc10, noverify, false, false, 60, ps);
            List<String> got = new ArrayList<String>();
            for (String l : new String(bo.toByteArray(), "UTF-8").split("\n")) if (l.startsWith("> ")) got.add(l);
            List<String> want = new ArrayList<String>();
            for (String l : new String(Files.readAllBytes(exp.toPath()), "UTF-8").split("\n")) if (l.startsWith("> ")) want.add(l);
            n++;
            if (!done) bad.append("\n  ").append(c.getName()).append(": timeout");
            else if (!got.equals(want)) bad.append("\n  ").append(c.getName()).append(": got ").append(got).append(" want ").append(want);
        }
        System.out.println("VmSpecTest: " + n + " cases on the project VM");
        assertTrue("cases: " + n, n >= 40);
        assertEquals("", bad.toString());
    }
}

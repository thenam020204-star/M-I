package jlkvm.verify;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import jlkvm.ClassSource;
import jlkvm.Corpus;
import jlkvm.classfile.ClassFile;
import jlkvm.classfile.ClassParser;
import org.junit.Test;

/** T08: ASM-generated accept/reject cases (tests/vmspec/gen_verify_cases.sh), strict and lenient verdicts, pc and reason. */
public class GeneratedVerifyTest {
    static final File DIR = new File(Corpus.ROOT, "tests/vmspec/build/verify");

    @Test
    public void generatedCases() throws Exception {
        File idx = new File(DIR, "cases.txt");
        assumeTrue("run tests/vmspec/gen_verify_cases.sh first", idx.isFile());
        List<String> lines = Files.readAllLines(idx.toPath(), java.nio.charset.StandardCharsets.UTF_8);
        CorpusVerifyTest.MapSource app = new CorpusVerifyTest.MapSource();
        for (String l : lines) {
            String id = l.split("\t")[0];
            app.m.put("vf/" + id, Files.readAllBytes(new File(DIR, id + ".class").toPath()));
        }
        ClassSource st11 = CorpusVerifyTest.stubs(true), st10 = CorpusVerifyTest.stubs(false);
        StringBuilder bad = new StringBuilder();
        int acc = 0, rej = 0, lenOk = 0;
        for (String l : lines) {
            String[] f = l.split("\t");
            String id = f[0];
            boolean expectAccept = f[1].equals("accept"), cldc11 = f[2].equals("cldc11");
            int pc = Integer.parseInt(f[3]);
            String reason = f[4];
            boolean lenAccept = f[5].equals("accept");
            ClassFile cf = ClassParser.parse(app.m.get("vf/" + id));
            ClassSource st = cldc11 ? st11 : st10;
            // strict
            try {
                new Verifier(new SourceHierarchy(app, st), true, cldc11).verify(cf);
                if (expectAccept) acc++;
                else bad.append("\n  ").append(id).append(": strict accepted a bad class");
            } catch (VerifyException e) {
                if (expectAccept) bad.append("\n  ").append(id).append(": strict rejected: ").append(e.getMessage());
                else {
                    rej++;
                    if (pc != -2 && e.pc != pc) bad.append("\n  ").append(id).append(": pc ").append(e.pc).append(" expected ").append(pc).append(" (").append(e.getMessage()).append(")");
                    if (!reason.equals("-") && !e.reason.contains(reason)) bad.append("\n  ").append(id).append(": reason '").append(e.reason).append("' lacks '").append(reason).append("'");
                }
            }
            // lenient
            Verifier lv = new Verifier(new SourceHierarchy(app, st), false, cldc11);
            try {
                lv.verify(cf);
                if (lenAccept) lenOk++;
                else bad.append("\n  ").append(id).append(": lenient accepted a bad class");
                if (!expectAccept && lenAccept && lv.warnings().isEmpty()) bad.append("\n  ").append(id).append(": lenient accepted without a warning");
            } catch (VerifyException e) {
                if (lenAccept) bad.append("\n  ").append(id).append(": lenient rejected: ").append(e.getMessage());
                else lenOk++;
            }
        }
        System.out.println("generated cases: " + lines.size() + " (strict accepted " + acc + ", rejected " + rej + "; lenient as expected " + lenOk + ")");
        assertEquals("verdict mismatches:" + bad, 0, bad.length());
    }
}

package jlkvm.verify;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import jlkvm.ClassSource;
import jlkvm.Corpus;
import jlkvm.classfile.ClassFile;
import jlkvm.classfile.ClassParser;
import org.junit.Test;

/** T08: every preverified class of the corpus verifies (strict); the negative vmspec cases are rejected. */
public class CorpusVerifyTest {
    static final class MapSource implements ClassSource {
        final Map<String, byte[]> m = new HashMap<String, byte[]>();
        public byte[] readClass(String n) { return m.get(n); }
        public byte[] readResource(String p) { return null; }
    }

    static ClassSource stubs(boolean cldc11) throws Exception {
        MapSource s = new MapSource();
        File[] fs = {Corpus.stub(cldc11 ? "cldcapi11" : "cldcapi10"), Corpus.stub("midpapi20"), Corpus.stub("microemu-nokiaui"),
            Corpus.stub("microemu-jsr-120"), Corpus.stub("microemu-jsr-75"), new File(Corpus.ROOT, "vm-classlib/build/rom/rom-jsr75-pim.jar")};   // T32a: WMA / FileConnection stubs, PIM = this project's own layer (no open stub)
        for (File f : fs) if (f != null && f.isFile()) s.m.putAll(Corpus.classes(f));
        return s;
    }

    /** @return null if accepted, else the VerifyException message. */
    static String verifyClass(byte[] b, ClassSource app, ClassSource stubs, boolean strict, boolean cldc11) {
        try {
            ClassFile cf = ClassParser.parse(b);
            new Verifier(new SourceHierarchy(app, stubs), strict, cldc11).verify(cf);
            return null;
        } catch (VerifyException e) {
            return e.getMessage();
        }
    }

    @Test
    public void vmspecCorpus() throws Exception {
        Map<String, File> jars = Corpus.vmspecJars();
        assumeTrue("run tests/vmspec/run_vmspec.sh first", !jars.isEmpty());
        ClassSource st11 = stubs(true), st10 = stubs(false);
        int accepted = 0, rejected = 0;
        StringBuilder unexpected = new StringBuilder();
        for (Map.Entry<String, File> e : jars.entrySet()) {
            boolean neg = e.getKey().startsWith("neg") || e.getKey().equals("o03_jsr_ret");   // o03: jsr/ret, run with verification off
            boolean cldc10 = e.getKey().equals("neg03_float_in_cldc10");
            MapSource app = new MapSource();
            app.m.putAll(Corpus.classes(e.getValue()));
            int rej = 0;
            for (Map.Entry<String, byte[]> c : app.m.entrySet()) {
                String r = verifyClass(c.getValue(), app, cldc10 ? st10 : st11, true, !cldc10);
                if (r == null) accepted++; else { rejected++; rej++; System.out.println("REJECT " + e.getKey() + " " + r); }
                if (r != null && !neg) unexpected.append("\n  ").append(e.getKey()).append(": ").append(r);
            }
            if (neg && rej == 0) unexpected.append("\n  ").append(e.getKey()).append(": negative case accepted");
        }
        System.out.println("vmspec corpus: accepted=" + accepted + " rejected=" + rejected);
        assertEquals("unexpected verdicts:" + unexpected, 0, unexpected.length());
    }

    /** tests/midlets, hello.jar, the jarscan clean jar and the CLDC/MIDP stub jars (strict, must all verify). */
    @Test
    public void otherCorpus() throws Exception {
        File[] mid = new File(Corpus.ROOT, "tests/midlets/build").listFiles();
        java.util.List<File> jars = new java.util.ArrayList<File>();
        if (mid != null) for (File f : mid) if (f.getName().endsWith(".jar")) jars.add(f);
        File hello = new File(Corpus.ROOT, "tools/env/cache/out/hello.jar");
        if (hello.isFile()) jars.add(hello);
        File clean = new File(Corpus.ROOT, "tools/jarscan/build/selftest/clean.jar");
        if (clean.isFile()) jars.add(clean);
        assumeTrue("build tests/midlets (tests/midlets/build.sh) first", !jars.isEmpty());
        ClassSource st11 = stubs(true);
        int ok = 0;
        StringBuilder unexpected = new StringBuilder();
        for (File j : jars) {
            MapSource app = new MapSource();
            app.m.putAll(Corpus.classes(j));
            for (Map.Entry<String, byte[]> c : app.m.entrySet()) {
                String r = verifyClass(c.getValue(), app, st11, true, true);
                if (r == null) ok++; else unexpected.append("\n  ").append(j.getName()).append(": ").append(r);
            }
        }
        System.out.println("midlets/hello/clean jars (" + jars.size() + "): accepted=" + ok);
        // stubs: verified against themselves
        int stubOk = 0, withMap = 0;
        for (String sn : new String[] {"cldcapi11", "midpapi20"}) {
            File f = Corpus.stub(sn);
            if (f == null) continue;
            MapSource app = new MapSource();
            app.m.putAll(Corpus.classes(f));
            for (Map.Entry<String, byte[]> c : app.m.entrySet()) {
                ClassFile cf = ClassParser.parse(c.getValue());
                for (ClassFile.MethodInfo mi : cf.methods) if (mi.code != null && mi.code.stackMap != null) { withMap++; break; }
                String r = verifyClass(c.getValue(), app, st11, true, true);
                if (r == null) stubOk++; else unexpected.append("\n  ").append(sn).append(" ").append(c.getKey()).append(": ").append(r);
            }
        }
        System.out.println("stub classes accepted=" + stubOk + " (with a StackMap attribute: " + withMap + ")");
        assertEquals("unexpected verdicts:" + unexpected, 0, unexpected.length());
    }
}

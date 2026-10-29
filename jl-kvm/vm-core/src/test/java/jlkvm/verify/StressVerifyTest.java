package jlkvm.verify;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.util.Map;
import jlkvm.Corpus;
import jlkvm.classfile.ClassFile;
import jlkvm.classfile.ClassParser;
import org.junit.Test;

/** T08 stress: large real-world classes preverified by ProGuard (tests/vmspec/gen_stress_corpus.sh); hierarchy from the host JVM. */
public class StressVerifyTest {
    /** App classes first, then the host JVM's classes (reflection) for everything else. */
    static final class HostHierarchy implements ClassHierarchy {
        final SourceHierarchy app;
        HostHierarchy(CorpusVerifyTest.MapSource s) { app = new SourceHierarchy(s); }
        private static Class<?> host(String n) {
            try { return Class.forName(n.replace('/', '.'), false, StressVerifyTest.class.getClassLoader()); }
            catch (Throwable t) { return null; }
        }
        public boolean exists(String n) { return app.exists(n) || host(n) != null; }
        public String superName(String n) {
            if (app.exists(n)) return app.superName(n);
            Class<?> s = host(n).getSuperclass();
            return s == null ? null : s.getName().replace('.', '/');
        }
        public boolean isInterface(String n) { return app.exists(n) ? app.isInterface(n) : host(n).isInterface(); }
    }

    @Test
    public void realWorldClasses() throws Exception {
        File[] jars = new File(Corpus.ROOT, "tests/vmspec/build/stress").listFiles();
        assumeTrue("run tests/vmspec/gen_stress_corpus.sh first", jars != null && jars.length > 0);
        java.util.Arrays.sort(jars);
        int ok = 0, bad = 0, methods = 0, withMap = 0, ldcClass = 0, missing = 0;
        StringBuilder msg = new StringBuilder();
        // all stress jars form one class path (they reference each other); ASM comes straight from the toolchain cache
        CorpusVerifyTest.MapSource app = new CorpusVerifyTest.MapSource();
        for (File j : jars) app.m.putAll(Corpus.classes(j));
        File asm = new File(Corpus.MAVEN, "asm-9.6.jar");
        java.util.Map<String, byte[]> extra = asm.isFile() ? Corpus.classes(asm) : new java.util.HashMap<String, byte[]>();
        for (Map.Entry<String, byte[]> x : extra.entrySet()) if (!app.m.containsKey(x.getKey())) app.m.put(x.getKey(), x.getValue());
        HostHierarchy hh = new HostHierarchy(app);
        for (File j : jars) {
            for (String cn : Corpus.classes(j).keySet()) {
                ClassFile cf = ClassParser.parse(app.m.get(cn));
                Verifier v = new Verifier(hh, true, true);
                for (ClassFile.MethodInfo mi : cf.methods) {
                    if (mi.code == null) continue;
                    methods++;
                    if (mi.code.stackMap != null) withMap++;
                    try {
                        v.verifyMethod(cf, mi, false);
                        ok++;
                    } catch (VerifyException e) {
                        // ProGuard -target 1.3 leaves "ldc <Class>" in version 47 code: illegal below 49, rejected on purpose.
                        // A few classes depend on libraries absent from the class path (Ant, ...).
                        if (e.reason.startsWith("ldc of a class constant requires")) { ldcClass++; continue; }
                        if (e.reason.startsWith("cannot load class")) { missing++; continue; }
                        bad++;
                        if (bad <= 40) msg.append("\n  ").append(j.getName()).append(": ").append(e.getMessage());
                    }
                }
            }
        }
        System.out.println("stress corpus: methods accepted=" + ok + " unexpectedly-rejected=" + bad + " rejected(ldc class in v47)=" + ldcClass + " not-loadable(deps absent)=" + missing + " methods=" + methods + " (with StackMap: " + withMap + ")");
        assertEquals("rejected classes:" + msg, 0, bad);
    }
}

package jlkvm.tools.romgen;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/** T17: the ROM build refuses guest code that names classes outside the ROM; accepts a self-contained library. */
public class RomGenTest {
    private static byte[] cls(String name, String sup, String refOwner) {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(Opcodes.V1_3, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, name, null, sup, null);
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, sup, "<init>", "()V");
        if (refOwner != null) {
            mv.visitTypeInsn(Opcodes.NEW, refOwner);
            mv.visitInsn(Opcodes.POP);
        }
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }

    private static Path jar(Map<String, byte[]> m) throws Exception {
        Path p = Files.createTempFile("rom", ".jar");
        RomGen.writeJar(new TreeMap<>(m), p);
        return p;
    }

    @Test
    public void selfContainedRomPasses() throws Exception {
        Map<String, byte[]> m = new TreeMap<>();
        m.put("java/lang/Object", cls("java/lang/Object", "java/lang/Object", null));   // content irrelevant for the closure check
        m.put("a/B", cls("a/B", "java/lang/Object", "java/lang/Object"));
        assertEquals(new ArrayList<String>(), RomGen.check(jar(m)));
    }

    @Test
    public void classOutsideTheRomIsRejected() throws Exception {
        Map<String, byte[]> m = new TreeMap<>();
        m.put("java/lang/Object", cls("java/lang/Object", "java/lang/Object", null));
        m.put("a/B", cls("a/B", "java/lang/Object", "java/util/ArrayList"));
        List<String> p = RomGen.check(jar(m));
        assertEquals(1, p.size());
        assertTrue(p.get(0), p.get(0).contains("a/B -> java/util/ArrayList"));
    }

    @Test
    public void descriptorsAndSuperclassesAreChecked() throws Exception {
        Map<String, byte[]> m = new TreeMap<>();
        m.put("java/lang/Object", cls("java/lang/Object", "java/lang/Object", null));
        m.put("a/C", cls("a/C", "java/lang/Object", null));
        ClassWriter cw = new ClassWriter(0);
        cw.visit(Opcodes.V1_3, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, "a/D", null, "java/lang/Object", null);
        cw.visitField(Opcodes.ACC_PUBLIC, "f", "[Ljava/util/Vector;", null, null);
        cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT, "m", "(Ljava/io/File;)V", null, new String[] {"java/lang/Foo"});
        cw.visitEnd();
        m.put("a/D", cw.toByteArray());
        List<String> p = RomGen.check(jar(m));
        assertEquals(p.toString(), 3, p.size());
    }

    // ------------------------------------------------------------------ end to end (needs /opt/jdk8: javac 1.3 target)

    private static final java.nio.file.Path ROOT = java.nio.file.Paths.get("..", "..").toAbsolutePath().normalize();
    private static final java.nio.file.Path JDK8 = java.nio.file.Paths.get(System.getenv("JDK8_HOME") != null ? System.getenv("JDK8_HOME") : "/opt/jdk8");

    private static void needJdk8() {
        org.junit.Assume.assumeTrue("JDK 8 not found", Files.isExecutable(JDK8.resolve("bin/javac")));
    }

    private static Path src(String objectBody, String... extra) throws Exception {
        Path d = Files.createTempDirectory("romsrc");
        Path obj = d.resolve("java/lang/Object.java");
        Files.createDirectories(obj.getParent());
        Files.write(obj, ("package java.lang; public class Object { public Object() { } " + objectBody + " }").getBytes("UTF-8"));
        for (int i = 0; i + 1 < extra.length; i += 2) {
            Path f = d.resolve(extra[i]);
            Files.createDirectories(f.getParent());
            Files.write(f, extra[i + 1].getBytes("UTF-8"));
        }
        return d;
    }

    @Test
    public void realLibraryBuildsVerifiesAndMatchesCldc11() throws Exception {
        needJdk8();
        Path out = Files.createTempFile("realrom", ".jar");
        int n = RomGen.build(java.util.Collections.singletonList(ROOT.resolve("vm-classlib/src")), out, null, null, JDK8,
                ROOT.resolve("tools/api-sigs/db/cldc-1.1.json"), java.util.Collections.singletonList("jlkvm/internal/"), true);
        assertTrue("classes: " + n, n >= 81);
        assertEquals(new ArrayList<String>(), RomGen.check(out));
    }

    @Test
    public void guestSourceUsingJavaSeIsRejected() throws Exception {
        needJdk8();
        Path s = src("", "a/Bad.java", "package a; public class Bad { Object m() { return new java.util.HashMap(); } }");
        try {
            RomGen.build(java.util.Collections.singletonList(s), Files.createTempFile("bad", ".jar"), null, null, JDK8, null, new ArrayList<String>(), true);
            fail("a guest class using java.util.HashMap must not build");
        } catch (RomGen.RomException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("javac failed"));
        }
    }

    @Test
    public void guestSourceUsingAnUnlistedLibraryClassIsRejected() throws Exception {
        needJdk8();
        // String is not part of this tiny library: javac sees only the library itself (D11), never rt.jar
        Path s = src("public String toString() { return null; }");
        try {
            RomGen.build(java.util.Collections.singletonList(s), Files.createTempFile("bad", ".jar"), null, null, JDK8, null, new ArrayList<String>(), true);
            fail("String is outside the library");
        } catch (RomGen.RomException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("javac failed"));
        }
    }

    @Test
    public void surfaceDifferenceFailsTheBuild() throws Exception {
        needJdk8();
        Path db = Files.createTempFile("db", ".json");
        // DB of a library with only java/lang/Object { Object() }, then a build with one more public method
        Path ok = src("");
        Path okJar = Files.createTempFile("ok", ".jar");
        RomGen.build(java.util.Collections.singletonList(ok), okJar, null, null, JDK8, null, new ArrayList<String>(), true);
        jlkvm.tools.apisigs.Extractor.extract("t", "t", null, okJar).write(db);
        RomGen.build(java.util.Collections.singletonList(ok), Files.createTempFile("ok2", ".jar"), null, null, JDK8, db, new ArrayList<String>(), true);   // same surface: passes
        Path extra = src("public void extra() { }");
        try {
            RomGen.build(java.util.Collections.singletonList(extra), Files.createTempFile("x", ".jar"), null, null, JDK8, db, new ArrayList<String>(), true);
            fail("an additional public method must fail the diff (D9)");
        } catch (RomGen.RomException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("API surface differs") && e.getMessage().contains("extra"));
        }
    }
}

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
}

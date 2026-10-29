package jlkvm.tools.apisigs;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** Builds an {@link ApiDb} from stub jars: public classes, public/protected members (D9). */
public final class Extractor {
    private Extractor() {
    }

    /** Count of public top-level-or-nested classes in the jars (ground truth for the T03 test). */
    public static int countPublicClasses(Path... jars) throws IOException {
        int n = 0;
        for (Path j : jars) {
            try (ZipFile z = new ZipFile(j.toFile())) {
                for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements(); ) {
                    ZipEntry en = e.nextElement();
                    if (!en.getName().endsWith(".class")) continue;
                    try (InputStream in = z.getInputStream(en)) {
                        if (isPublic(new ClassReader(in))) n++;
                    }
                }
            }
        }
        return n;
    }

    private static boolean isPublic(ClassReader cr) {
        return (cr.getAccess() & Opcodes.ACC_PUBLIC) != 0 && !"module-info".equals(cr.getClassName());
    }

    public static ApiDb extract(String name, String source, String note, Path... jars) throws IOException {
        ApiDb db = new ApiDb();
        db.name = name;
        db.source = source;
        db.note = note;
        for (Path j : jars) {
            try (ZipFile z = new ZipFile(j.toFile())) {
                for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements(); ) {
                    ZipEntry en = e.nextElement();
                    if (!en.getName().endsWith(".class")) continue;
                    try (InputStream in = z.getInputStream(en)) {
                        ClassReader cr = new ClassReader(in);
                        if (!isPublic(cr)) continue;
                        db.classes.add(read(cr));
                    }
                }
            }
        }
        db.classes.sort(Comparator.comparing(c -> c.name));
        return db;
    }

    private static ApiDb.ClassSig read(ClassReader cr) {
        final ApiDb.ClassSig cs = new ApiDb.ClassSig();
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public void visit(int version, int access, String name, String sig, String superName, String[] ifs) {
                cs.name = name;
                cs.superName = superName;
                cs.access = access & ~Opcodes.ACC_SUPER;
                if (ifs != null) {
                    String[] s = ifs.clone();
                    Arrays.sort(s);
                    cs.interfaces.addAll(Arrays.asList(s));
                }
            }

            @Override
            public void visitInnerClass(String name, String outer, String inner, int access) {
                if (name.equals(cs.name)) cs.access = (access & ~Opcodes.ACC_SUPER) | (cs.access & Opcodes.ACC_INTERFACE);
            }

            @Override
            public FieldVisitor visitField(int access, String name, String desc, String sig, Object value) {
                if ((access & (Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED)) == 0) return null;
                ApiDb.FieldSig f = new ApiDb.FieldSig();
                f.name = name;
                f.desc = desc;
                f.access = access;
                if (value != null) {
                    f.constant = String.valueOf(value);
                    f.constantType = value instanceof String ? "Ljava/lang/String;" : desc;
                }
                cs.fields.add(f);
                return null;
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
                if ((access & (Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED)) == 0) return null;
                ApiDb.MethodSig m = new ApiDb.MethodSig();
                m.name = name;
                m.desc = desc;
                m.access = access;
                if (ex != null) {
                    String[] s = ex.clone();
                    Arrays.sort(s);
                    m.exceptions.addAll(Arrays.asList(s));
                }
                cs.methods.add(m);
                return null;
            }
        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        cs.fields.sort(Comparator.comparing((ApiDb.FieldSig f) -> f.name).thenComparing(f -> f.desc));
        cs.methods.sort(Comparator.comparing((ApiDb.MethodSig m) -> m.name).thenComparing(m -> m.desc));
        return cs;
    }
}

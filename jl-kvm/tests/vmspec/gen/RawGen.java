import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Generates the raw-bytecode vmspec classes that javac cannot produce (old class file versions, missing ACC_SUPER,
 * jsr/ret, unbalanced monitors, ConstantValue access, missing StackMap...).
 * Usage: java -cp asm.jar RawGen.java OUT_CASES_DIR     (writes OUT/<case>/raw/vmspec/*.class)
 * Classes are written with NO frames (ClassWriter flags 0): StackMap/StackMapTable are never emitted here.
 */
public class RawGen implements Opcodes {
    static Path out;

    static void write(String caseId, String name, byte[] b) throws Exception {
        Path f = out.resolve(caseId).resolve("raw").resolve("vmspec").resolve(name + ".class");
        Files.createDirectories(f.getParent());
        Files.write(f, b);
    }

    static void println(MethodVisitor m, String s) {
        m.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        m.visitLdcInsn("> " + s);
        m.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    static void ctor(ClassWriter cw, String sup) {
        MethodVisitor m = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        m.visitCode();
        m.visitVarInsn(ALOAD, 0);
        m.visitMethodInsn(INVOKESPECIAL, sup, "<init>", "()V", false);
        m.visitInsn(RETURN);
        m.visitMaxs(1, 1);
        m.visitEnd();
    }

    /** Minimal CLDC StackMap attribute: frames given as {Label, Object[] locals, Object[] stack}, types "T" or "Lname". */
    static final class SM extends Attribute {
        final java.util.List<Object[]> entries = new java.util.ArrayList<Object[]>();
        SM() { super("StackMap"); }
        @Override public boolean isCodeAttribute() { return true; }
        @Override protected Label[] getLabels() {
            Label[] r = new Label[entries.size()];
            for (int i = 0; i < r.length; i++) r[i] = (Label) entries.get(i)[0];
            return r;
        }
        @Override protected ByteVector write(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals) {
            ByteVector v = new ByteVector();
            v.putShort(entries.size());
            for (Object[] e : entries) {
                v.putShort(((Label) e[0]).getOffset());
                for (int k = 1; k <= 2; k++) {
                    Object[] ts = (Object[]) e[k];
                    v.putShort(ts.length);
                    for (Object t : ts) {
                        String x = (String) t;
                        if (x.equals("T")) v.putByte(0); else v.putByte(7).putShort(cw.newClass(x.substring(1)));
                    }
                }
            }
            return v;
        }
    }

    static ClassWriter cls(int version, int access, String name, String sup, String... ifs) {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(version, access, name, null, sup, ifs);
        return cw;
    }

    static int ver(int major, int minor) { return (minor << 16) | major; }

    public static void main(String[] a) throws Exception {
        out = Paths.get(a[0]);
        k01();
        k02();
        m05();
        neg01();
        neg02();
        neg03();
    }

    /** k01: invokespecial of a non-direct superclass method from a class WITHOUT ACC_SUPER (class file 45.3). */
    static void k01() throws Exception {
        String id = "k01_no_acc_super";
        ClassWriter A = cls(ver(45, 3), ACC_PUBLIC, "vmspec/K01A", "java/lang/Object");
        ctor(A, "java/lang/Object");
        MethodVisitor m = A.visitMethod(ACC_PUBLIC, "m", "()V", null, null);
        m.visitCode(); println(m, "A.m"); m.visitInsn(RETURN); m.visitMaxs(2, 1); m.visitEnd();
        write(id, "K01A", A.toByteArray());

        ClassWriter B = cls(ver(45, 3), ACC_PUBLIC, "vmspec/K01B", "vmspec/K01A");
        ctor(B, "vmspec/K01A");
        m = B.visitMethod(ACC_PUBLIC, "m", "()V", null, null);
        m.visitCode(); println(m, "B.m"); m.visitInsn(RETURN); m.visitMaxs(2, 1); m.visitEnd();
        write(id, "K01B", B.toByteArray());

        // C extends B, flags WITHOUT ACC_SUPER (0x20)
        ClassWriter C = cls(ver(45, 3), ACC_PUBLIC, "vmspec/K01C", "vmspec/K01B", "java/lang/Runnable");
        ctor(C, "vmspec/K01B");
        m = C.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
        m.visitCode();
        m.visitVarInsn(ALOAD, 0);
        m.visitMethodInsn(INVOKESPECIAL, "vmspec/K01A", "m", "()V", false);   // names K01A, not the direct super K01B
        m.visitInsn(RETURN); m.visitMaxs(1, 1); m.visitEnd();
        write(id, "K01C", C.toByteArray());
    }

    /** k02: getstatic of a ConstantValue field (never inlined by javac here) must initialise the class first. */
    static void k02() throws Exception {
        String id = "k02_constantvalue_getstatic";
        ClassWriter K = cls(ver(46, 0), ACC_PUBLIC | ACC_SUPER, "vmspec/K02K", "java/lang/Object");
        FieldVisitor f = K.visitField(ACC_PUBLIC | ACC_STATIC | ACC_FINAL, "V", "I", null, Integer.valueOf(42));
        f.visitEnd();
        f = K.visitField(ACC_PUBLIC | ACC_STATIC | ACC_FINAL, "S", "Ljava/lang/String;", null, "const-string");
        f.visitEnd();
        f = K.visitField(ACC_PUBLIC | ACC_STATIC, "W", "I", null, null);   // not final: ConstantValue on non-final is ignored? (not used)
        f.visitEnd();
        MethodVisitor m = K.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
        m.visitCode(); println(m, "K.clinit"); m.visitInsn(RETURN); m.visitMaxs(2, 0); m.visitEnd();
        write(id, "K02K", K.toByteArray());

        ClassWriter D = cls(ver(46, 0), ACC_PUBLIC | ACC_SUPER, "vmspec/K02D", "java/lang/Object", "java/lang/Runnable");
        ctor(D, "java/lang/Object");
        m = D.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
        m.visitCode();
        println(m, "before getstatic");
        m.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        m.visitTypeInsn(NEW, "java/lang/StringBuffer");
        m.visitInsn(DUP);
        m.visitLdcInsn("> V=");
        m.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuffer", "<init>", "(Ljava/lang/String;)V", false);
        m.visitFieldInsn(GETSTATIC, "vmspec/K02K", "V", "I");
        m.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(I)Ljava/lang/StringBuffer;", false);
        m.visitFieldInsn(GETSTATIC, "vmspec/K02K", "S", "Ljava/lang/String;");
        m.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;", false);
        m.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "toString", "()Ljava/lang/String;", false);
        m.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        m.visitInsn(RETURN); m.visitMaxs(4, 1); m.visitEnd();
        write(id, "K02D", D.toByteArray());
    }

    /** m05: monitorexit without ownership, and monitorenter never exited, must raise IllegalMonitorStateException. */
    static void m05() throws Exception {
        String id = "m05_monitor_imbalance";
        ClassWriter D = cls(ver(46, 0), ACC_PUBLIC | ACC_SUPER, "vmspec/M05D", "java/lang/Object", "java/lang/Runnable");
        ctor(D, "java/lang/Object");
        // static void exitOnly(): new Object; monitorexit
        MethodVisitor m = D.visitMethod(ACC_STATIC, "exitOnly", "()V", null, null);
        m.visitCode();
        m.visitTypeInsn(NEW, "java/lang/Object"); m.visitInsn(DUP);
        m.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        m.visitInsn(MONITOREXIT);
        m.visitInsn(RETURN); m.visitMaxs(2, 0); m.visitEnd();
        // static void enterOnly(): new Object; monitorenter; return (lock left held)
        m = D.visitMethod(ACC_STATIC, "enterOnly", "()V", null, null);
        m.visitCode();
        m.visitTypeInsn(NEW, "java/lang/Object"); m.visitInsn(DUP);
        m.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        m.visitInsn(MONITORENTER);
        m.visitInsn(RETURN); m.visitMaxs(2, 0); m.visitEnd();
        // static void doubleExit(): enter once, exit twice
        m = D.visitMethod(ACC_STATIC, "doubleExit", "()V", null, null);
        m.visitCode();
        m.visitTypeInsn(NEW, "java/lang/Object"); m.visitInsn(DUP);
        m.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        m.visitVarInsn(ASTORE, 0);
        m.visitVarInsn(ALOAD, 0); m.visitInsn(MONITORENTER);
        m.visitVarInsn(ALOAD, 0); m.visitInsn(MONITOREXIT);
        m.visitVarInsn(ALOAD, 0); m.visitInsn(MONITOREXIT);
        m.visitInsn(RETURN); m.visitMaxs(2, 1); m.visitEnd();
        // run(): call each in try/catch Throwable printing the class name
        m = D.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
        m.visitCode();
        String[] names = { "exitOnly", "enterOnly", "doubleExit" };
        SM sm = new SM();
        for (String n : names) {
            Label s = new Label(), e = new Label(), h = new Label(), end = new Label();
            m.visitTryCatchBlock(s, e, h, "java/lang/Throwable");
            m.visitLabel(s);
            m.visitMethodInsn(INVOKESTATIC, "vmspec/M05D", n, "()V", false);
            println(m, n + " returned");
            m.visitLabel(e);
            m.visitJumpInsn(GOTO, end);
            m.visitLabel(h);
            sm.entries.add(new Object[] { h, new Object[] { "Lvmspec/M05D" }, new Object[] { "Ljava/lang/Throwable" } });
            m.visitVarInsn(ASTORE, 1);
            m.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            m.visitTypeInsn(NEW, "java/lang/StringBuffer"); m.visitInsn(DUP);
            m.visitLdcInsn("> " + n + " ");
            m.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuffer", "<init>", "(Ljava/lang/String;)V", false);
            m.visitVarInsn(ALOAD, 1);
            m.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
            m.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
            m.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;", false);
            m.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "toString", "()Ljava/lang/String;", false);
            m.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            m.visitLabel(end);
            sm.entries.add(new Object[] { end, new Object[] { "Lvmspec/M05D" }, new Object[0] });
        }
        m.visitInsn(RETURN); m.visitAttribute(sm); m.visitMaxs(4, 2); m.visitEnd();
        write(id, "M05D", D.toByteArray());
    }

    static void driverWithLoop(String id, String name, boolean withJsr, boolean withFloat) throws Exception {
        ClassWriter D = cls(ver(46, 0), ACC_PUBLIC | ACC_SUPER, "vmspec/" + name, "java/lang/Object", "java/lang/Runnable");
        ctor(D, "java/lang/Object");
        MethodVisitor m = D.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
        m.visitCode();
        if (withJsr) {
            Label sub = new Label(), after = new Label();
            m.visitJumpInsn(JSR, sub);
            m.visitJumpInsn(GOTO, after);
            m.visitLabel(sub);
            m.visitVarInsn(ASTORE, 1);
            println(m, "in subroutine");
            m.visitVarInsn(RET, 1);
            m.visitLabel(after);
            println(m, "after jsr");
        } else if (withFloat) {
            m.visitLdcInsn(Float.valueOf(1.5f));
            m.visitLdcInsn(Float.valueOf(2.5f));
            m.visitInsn(FADD);
            m.visitInsn(F2I);
            m.visitVarInsn(ISTORE, 1);
            println(m, "float executed");
        } else {
            // loop with a backward branch and no StackMap attribute at all
            Label top = new Label(), done = new Label();
            m.visitInsn(ICONST_0);
            m.visitVarInsn(ISTORE, 1);
            m.visitLabel(top);
            m.visitVarInsn(ILOAD, 1);
            m.visitInsn(ICONST_3);
            m.visitJumpInsn(IF_ICMPGE, done);
            m.visitIincInsn(1, 1);
            m.visitJumpInsn(GOTO, top);
            m.visitLabel(done);
            println(m, "loop executed without StackMap");
        }
        m.visitInsn(RETURN); m.visitMaxs(4, 3); m.visitEnd();
        write(id, name, D.toByteArray());
    }

    static void neg01() throws Exception { driverWithLoop("neg01_no_stackmap", "Neg01D", false, false); }

    static void neg02() throws Exception { driverWithLoop("neg02_jsr_ret", "Neg02D", true, false); }

    static void neg03() throws Exception { driverWithLoop("neg03_float_in_cldc10", "Neg03D", false, true); }
}

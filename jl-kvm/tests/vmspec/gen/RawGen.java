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
        o02();
        o03();
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

    // ---- opcode coverage helpers (T11): print the value on top of the stack with a "> " prefix
    static void pre(MethodVisitor m) {
        m.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        m.visitLdcInsn("> ");
        m.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
    }
    static void printInt(MethodVisitor m, String label) {
        pre(m); m.visitLdcInsn(label + " "); m.visitInsn(POP); // keep label out of the stack effect
        m.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        m.visitInsn(SWAP);
        m.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
    }
    static void printFloat(MethodVisitor m) {
        pre(m);
        m.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        m.visitInsn(SWAP);
        m.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false);
    }
    static void printWide(MethodVisitor m, String desc) {   // value occupies 2 words
        pre(m);
        m.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        m.visitInsn(DUP_X2); m.visitInsn(POP);
        m.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(" + desc + ")V", false);
    }

    /** o02: stack-shuffling opcodes, nop, wide forms, ixor, rarely used short load/store forms. Straight-line code (no StackMap needed). */
    static void o02() throws Exception {
        String id = "o02_raw_ops";
        ClassWriter D = cls(ver(46, 0), ACC_PUBLIC | ACC_SUPER, "vmspec/O02D", "java/lang/Object", "java/lang/Runnable");
        ctor(D, "java/lang/Object");
        MethodVisitor m = D.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
        m.visitCode();
        m.visitInsn(NOP);
        // dup_x2: 1 2 3 -> 3 1 2 3 ; print 3 2 1 3
        m.visitInsn(ICONST_1); m.visitInsn(ICONST_2); m.visitInsn(ICONST_3); m.visitInsn(DUP_X2);
        for (int i = 0; i < 4; i++) printInt(m, "");
        // dup2_x1: 1 2 3 -> 2 3 1 2 3 ; print 3 2 1 3 2
        m.visitInsn(ICONST_1); m.visitInsn(ICONST_2); m.visitInsn(ICONST_3); m.visitInsn(DUP2_X1);
        for (int i = 0; i < 5; i++) printInt(m, "");
        // dup2_x2: 1 2 3 4 -> 3 4 1 2 3 4 ; print 4 3 2 1 4 3
        m.visitInsn(ICONST_1); m.visitInsn(ICONST_2); m.visitInsn(ICONST_3); m.visitInsn(ICONST_4); m.visitInsn(DUP2_X2);
        for (int i = 0; i < 6; i++) printInt(m, "");
        // swap, pop2 (two ints, then one long)
        m.visitInsn(ICONST_5); m.visitIntInsn(BIPUSH, 6); m.visitInsn(SWAP); printInt(m, ""); printInt(m, "");
        m.visitInsn(ICONST_1); m.visitInsn(ICONST_2); m.visitInsn(POP2);
        m.visitInsn(LCONST_1); m.visitInsn(POP2);
        // ixor
        m.visitInsn(ICONST_5); m.visitInsn(ICONST_3); m.visitInsn(IXOR); printInt(m, "");
        // wide: locals above 255
        m.visitIntInsn(SIPUSH, 1000); m.visitVarInsn(ISTORE, 300);
        m.visitIincInsn(300, 1000); m.visitIincInsn(300, -3);
        m.visitVarInsn(ILOAD, 300); printInt(m, "");
        m.visitLdcInsn(Long.valueOf(123456789012L)); m.visitVarInsn(LSTORE, 301); m.visitVarInsn(LLOAD, 301); printWide(m, "J");
        m.visitLdcInsn(Double.valueOf(2.5d)); m.visitVarInsn(DSTORE, 303); m.visitVarInsn(DLOAD, 303); printWide(m, "D");
        m.visitLdcInsn(Float.valueOf(1.25f)); m.visitVarInsn(FSTORE, 305); m.visitVarInsn(FLOAD, 305); printFloat(m);
        m.visitTypeInsn(NEW, "java/lang/Object"); m.visitInsn(DUP);
        m.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        m.visitVarInsn(ASTORE, 306); m.visitVarInsn(ALOAD, 306); m.visitInsn(POP);
        // short forms: fstore_0/2/3-style slots 0..3 of a static helper
        m.visitMethodInsn(INVOKESTATIC, "vmspec/O02D", "slots", "()V", false);
        m.visitInsn(RETURN); m.visitMaxs(8, 307); m.visitEnd();
        m = D.visitMethod(ACC_STATIC, "slots", "()V", null, null);
        m.visitCode();
        m.visitInsn(FCONST_2); m.visitVarInsn(FSTORE, 0);
        m.visitInsn(FCONST_1); m.visitVarInsn(FSTORE, 2);
        m.visitInsn(FCONST_0); m.visitVarInsn(FSTORE, 3);
        m.visitVarInsn(FLOAD, 0); m.visitVarInsn(FLOAD, 2); m.visitInsn(FADD); m.visitVarInsn(FLOAD, 3); m.visitInsn(FADD); printFloat(m);
        m.visitInsn(DCONST_1); m.visitVarInsn(DSTORE, 0);
        m.visitLdcInsn(Double.valueOf(0.25d)); m.visitVarInsn(DSTORE, 3);
        m.visitVarInsn(DLOAD, 0); m.visitVarInsn(DLOAD, 3); m.visitInsn(DADD); printWide(m, "D");
        m.visitLdcInsn(Double.valueOf(0.5d)); m.visitVarInsn(DSTORE, 1);
        m.visitVarInsn(DLOAD, 1); printWide(m, "D");
        m.visitInsn(RETURN); m.visitMaxs(8, 5); m.visitEnd();
        write(id, "O02D", D.toByteArray());
    }

    /** o03: jsr/ret, jsr_w and goto_w (patched into placeholders), class version 46 (old verifier). The VM must run it with verification off. */
    static void o03() throws Exception {
        String id = "o03_jsr_ret";
        ClassWriter D = cls(ver(46, 0), ACC_PUBLIC | ACC_SUPER, "vmspec/O03D", "java/lang/Object", "java/lang/Runnable");
        ctor(D, "java/lang/Object");
        MethodVisitor m = D.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
        m.visitCode();
        Label sub = new Label(), pw = new Label(), gw = new Label(), end = new Label(), skipped = new Label();
        m.visitInsn(ICONST_0); m.visitVarInsn(ISTORE, 1);
        m.visitJumpInsn(JSR, sub);
        m.visitLabel(pw);
        for (int i = 0; i < 5; i++) m.visitInsn(ICONST_M1);       // -> jsr_w sub
        m.visitInsn(NOP);
        m.visitLabel(gw);
        for (int i = 0; i < 5; i++) m.visitInsn(ICONST_M1);       // -> goto_w end
        m.visitLabel(skipped);
        println(m, "skipped (wrong)");
        m.visitLabel(end);
        m.visitVarInsn(ILOAD, 1); printInt(m, "");
        m.visitInsn(RETURN);
        m.visitLabel(sub);
        m.visitVarInsn(ASTORE, 2);
        m.visitIincInsn(1, 1);
        println(m, "in subroutine");
        m.visitVarInsn(RET, 2);
        m.visitMaxs(8, 3); m.visitEnd();
        byte[] b = D.toByteArray();
        int pPw = pw.getOffset(), pGw = gw.getOffset(), pSub = sub.getOffset(), pEnd = end.getOffset();
        // find the code array start: the first occurrence of the 5 x ICONST_M1 pattern
        int first = indexOf(b, new byte[] { 2, 2, 2, 2, 2 }, 0);
        int second = indexOf(b, new byte[] { 2, 2, 2, 2, 2 }, first + 5);
        int codeStart = first - pPw;
        if (second - codeStart != pGw) throw new IllegalStateException("o03 placeholder layout");
        put(b, first, 0xC9, pSub - pPw);          // jsr_w
        put(b, second, 0xC8, pEnd - pGw);         // goto_w
        write(id, "O03D", b);
    }

    static int indexOf(byte[] b, byte[] pat, int from) {
        outer:
        for (int i = from; i + pat.length <= b.length; i++) {
            for (int k = 0; k < pat.length; k++) if (b[i + k] != pat[k]) continue outer;
            return i;
        }
        throw new IllegalStateException("pattern not found");
    }

    static void put(byte[] b, int at, int op, int off) {
        b[at] = (byte) op;
        b[at + 1] = (byte) (off >>> 24); b[at + 2] = (byte) (off >>> 16); b[at + 3] = (byte) (off >>> 8); b[at + 4] = (byte) off;
    }
}

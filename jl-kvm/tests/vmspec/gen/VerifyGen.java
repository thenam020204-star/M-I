import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates the verifier cases of PLAN T08 (class files WITH a CLDC "StackMap" attribute written by hand, so every
 * frame is under test control). Usage: java -cp asm.jar VerifyGen.java OUT_DIR
 * Writes OUT/<id>.class and OUT/cases.txt, one line per case, tab separated:
 *   id  accept|reject  cldc10|cldc11  expected-pc (-1 = method level, -2 = any)  reason-substring (or -)  lenient-verdict
 * The class under test is vf/<id>; its method under test is "t". All classes have major version 46.
 * Frame type notation: "T" top, "I" int, "F" float, "D" double, "J" long, "N" null, "U" uninitializedThis,
 * "Lname" object (name or array descriptor), a Label = uninitialized object created at that label's offset.
 * Long/double take ONE entry in a frame (CLDC StackMap format).
 */
public class VerifyGen implements Opcodes {
    static Path out;
    static final List<String> index = new ArrayList<>();
    static String nextLenient = null;       // lenient verdict when it differs from the strict one

    /** CLDC StackMap attribute (code attribute). */
    static final class SM extends Attribute {
        final List<Object[]> entries = new ArrayList<>();   // {Label, Object[] locals, Object[] stack}
        SM() { super("StackMap"); }
        SM f(Object l, Object[] locals, Object[] stack) { entries.add(new Object[] {l, locals, stack}); return this; }
        @Override public boolean isCodeAttribute() { return true; }
        @Override protected Label[] getLabels() {
            List<Label> r = new ArrayList<>();
            for (Object[] e : entries) {
                if (e[0] instanceof Label) r.add((Label) e[0]);
                for (int k = 1; k <= 2; k++) for (Object t : (Object[]) e[k]) if (t instanceof Label) r.add((Label) t);
            }
            return r.toArray(new Label[0]);
        }
        @Override protected ByteVector write(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals) {
            ByteVector v = new ByteVector();
            v.putShort(entries.size());
            for (Object[] e : entries) {
                v.putShort(e[0] instanceof Label ? ((Label) e[0]).getOffset() : (Integer) e[0]);
                for (int k = 1; k <= 2; k++) {
                    Object[] ts = (Object[]) e[k];
                    v.putShort(ts.length);
                    for (Object t : ts) {
                        if (t instanceof Label) { v.putByte(8).putShort(((Label) t).getOffset()); continue; }
                        String s = (String) t;
                        switch (s.charAt(0)) {
                            case 'T': v.putByte(0); break;
                            case 'I': v.putByte(1); break;
                            case 'F': v.putByte(2); break;
                            case 'D': v.putByte(3); break;
                            case 'J': v.putByte(4); break;
                            case 'N': v.putByte(5); break;
                            case 'U': v.putByte(6); break;
                            default: v.putByte(7).putShort(cw.newClass(s.substring(1)));
                        }
                    }
                }
            }
            return v;
        }
    }

    static Object[] a(Object... x) { return x; }

    interface Body { void emit(MethodVisitor m, Ctx c); }

    static final class Ctx {
        final Label at = new Label();            // the instruction the error is expected at (visit it with c.mark(m))
        boolean marked;
        void mark(MethodVisitor m) { m.visitLabel(at); marked = true; }
        SM sm = new SM();
        boolean useSm = true;
    }

    /** One case: class vf/<id> with a single method {@code name desc}. */
    static void kase(String id, String expect, String cldc, int pcMode, String reason, int access, String name, String desc,
                     int maxStack, int maxLocals, Body body) throws Exception {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(46, ACC_PUBLIC | ACC_SUPER, "vf/" + id, null, "java/lang/Object", null);
        if (!name.equals("<init>")) {
            MethodVisitor c0 = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            c0.visitCode();
            c0.visitVarInsn(ALOAD, 0);
            c0.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            c0.visitInsn(RETURN);
            c0.visitMaxs(1, 1);
            c0.visitEnd();
        }
        cw.visitField(ACC_PUBLIC, "i", "I", null, null).visitEnd();
        cw.visitField(ACC_PUBLIC, "s", "Ljava/lang/String;", null, null).visitEnd();
        cw.visitField(ACC_PUBLIC | ACC_STATIC, "arr", "[I", null, null).visitEnd();
        MethodVisitor m = cw.visitMethod(access, name, desc, null, null);
        m.visitCode();
        Ctx c = new Ctx();
        body.emit(m, c);
        if (!c.sm.entries.isEmpty() && c.useSm) m.visitAttribute(c.sm);
        m.visitMaxs(maxStack, maxLocals);
        m.visitEnd();
        cw.visitEnd();
        byte[] b = cw.toByteArray();
        Files.write(out.resolve(id + ".class"), b);
        int pc = pcMode;                      // -1 / -2 as is; otherwise use the marked label
        if (pcMode == 0) pc = c.marked ? c.at.getOffset() : -2;
        index.add(id + "\t" + expect + "\t" + cldc + "\t" + pc + "\t" + reason + "\t" + (nextLenient != null ? nextLenient : expect));
        nextLenient = null;
    }

    static final int ANY = -2, MARK = 0, METHOD = -1;

    static void ok(String id, int maxStack, int maxLocals, String desc, Body b) throws Exception {
        kase(id, "accept", "cldc11", ANY, "-", ACC_PUBLIC | ACC_STATIC, "t", desc, maxStack, maxLocals, b);
    }
    static void bad(String id, int pc, String reason, int maxStack, int maxLocals, String desc, Body b) throws Exception {
        kase(id, "reject", "cldc11", pc, reason, ACC_PUBLIC | ACC_STATIC, "t", desc, maxStack, maxLocals, b);
    }

    public static void main(String[] args) throws Exception {
        out = Paths.get(args[0]);
        Files.createDirectories(out);
        for (java.io.File f : out.toFile().listFiles()) f.delete();
        positives();
        negatives();
        patchIllegalOpcode(out.resolve("n26_illegal_opcode.class"));
        Files.write(out.resolve("cases.txt"), index);
        System.out.println("VerifyGen: " + index.size() + " cases -> " + out);
    }

    /** n26: the NOP of "iconst_0 nop pop return" becomes the undefined opcode 0xFE. */
    static void patchIllegalOpcode(Path f) throws Exception {
        byte[] b = Files.readAllBytes(f);
        int at = -1, n = 0;
        for (int i = 0; i + 3 < b.length; i++)
            if (b[i] == 0x03 && b[i + 1] == 0x00 && b[i + 2] == 0x57 && (b[i + 3] & 0xff) == 0xb1) { at = i + 1; n++; }
        if (n != 1) throw new IllegalStateException("cannot patch n26 (" + n + " matches)");
        b[at] = (byte) 0xfe;
        Files.write(f, b);
    }

    static void positives() throws Exception {
        ok("p01_straight_line", 2, 1, "(I)I", (m, c) -> {
            m.visitVarInsn(ILOAD, 0); m.visitInsn(ICONST_3); m.visitInsn(IMUL); m.visitInsn(IRETURN);
        });
        ok("p02_loop", 2, 2, "(I)I", (m, c) -> {
            Label top = new Label(), end = new Label();
            m.visitInsn(ICONST_0); m.visitVarInsn(ISTORE, 1);
            m.visitLabel(top);
            m.visitVarInsn(ILOAD, 0); m.visitJumpInsn(IFLE, end);
            m.visitVarInsn(ILOAD, 1); m.visitVarInsn(ILOAD, 0); m.visitInsn(IADD); m.visitVarInsn(ISTORE, 1);
            m.visitIincInsn(0, -1); m.visitJumpInsn(GOTO, top);
            m.visitLabel(end);
            m.visitVarInsn(ILOAD, 1); m.visitInsn(IRETURN);
            c.sm.f(top, a("I", "I"), a()).f(end, a("I", "I"), a());
        });
        ok("p03_long_double_slots", 6, 6, "(JD)J", (m, c) -> {      // locals: long(0,1) double(2,3)
            Label l = new Label();
            m.visitVarInsn(LLOAD, 0); m.visitVarInsn(DLOAD, 2); m.visitInsn(D2L); m.visitInsn(LADD);
            m.visitInsn(DUP2); m.visitVarInsn(LSTORE, 4);               // dup2 of a long, store at 4/5
            m.visitInsn(LCONST_0); m.visitInsn(LCMP); m.visitJumpInsn(IFEQ, l);
            m.visitVarInsn(LLOAD, 4); m.visitInsn(LRETURN);
            m.visitLabel(l);
            m.visitVarInsn(LLOAD, 0); m.visitInsn(LRETURN);
            c.sm.f(l, a("J", "D", "J"), a());
        });
        ok("p04_uninit_new_branch", 4, 2, "(I)Ljava/lang/Object;", (m, c) -> {
            // new Object; dup; (cond ? 1 : 2) pop; invokespecial; areturn  -- uninit on the stack across forward branches
            Label n = new Label(), a1 = new Label(), b1 = new Label();
            m.visitLabel(n);
            m.visitTypeInsn(NEW, "java/lang/Object"); m.visitInsn(DUP);
            m.visitVarInsn(ILOAD, 0); m.visitJumpInsn(IFEQ, a1);
            m.visitInsn(ICONST_1); m.visitJumpInsn(GOTO, b1);
            m.visitLabel(a1); m.visitInsn(ICONST_2);
            m.visitLabel(b1); m.visitInsn(POP);
            m.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            m.visitInsn(ARETURN);
            c.sm.f(a1, a("I"), a(n, n)).f(b1, a("I"), a(n, n, "I"));
        });
        kase("p05_ctor_uninit_this", "accept", "cldc11", ANY, "-", ACC_PUBLIC, "<init>", "(I)V", 2, 2, (m, c) -> {
            Label a1 = new Label();
            m.visitVarInsn(ALOAD, 0); m.visitVarInsn(ILOAD, 1); m.visitJumpInsn(IFEQ, a1);
            m.visitLabel(a1);
            m.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            m.visitInsn(RETURN);
            c.sm.f(a1, a("U", "I"), a("U"));
        });
        ok("p06_handler", 2, 2, "(I)I", (m, c) -> {
            Label s = new Label(), e = new Label(), h = new Label();
            m.visitTryCatchBlock(s, e, h, "java/lang/Exception");
            m.visitLabel(s); m.visitInsn(ICONST_1); m.visitVarInsn(ILOAD, 0); m.visitInsn(IDIV); m.visitVarInsn(ISTORE, 1);
            m.visitLabel(e); m.visitVarInsn(ILOAD, 1); m.visitInsn(IRETURN);
            m.visitLabel(h); m.visitInsn(POP); m.visitInsn(ICONST_M1); m.visitInsn(IRETURN);
            c.sm.f(h, a("I", "T"), a("Ljava/lang/Exception"));
        });
        ok("p07_catch_all_subclass_frame", 2, 2, "(I)I", (m, c) -> {   // handler frame stack type is a superclass of the catch type
            Label s = new Label(), e = new Label(), h = new Label();
            m.visitTryCatchBlock(s, e, h, "java/lang/RuntimeException");
            m.visitLabel(s); m.visitVarInsn(ILOAD, 0); m.visitInsn(IRETURN);
            m.visitLabel(e);
            m.visitLabel(h); m.visitInsn(POP); m.visitInsn(ICONST_0); m.visitInsn(IRETURN);
            c.sm.f(h, a("I"), a("Ljava/lang/Throwable"));
        });
        ok("p08_interface_as_object", 2, 1, "(Ljava/lang/Runnable;)Ljava/lang/Object;", (m, c) -> {
            m.visitVarInsn(ALOAD, 0);
            m.visitMethodInsn(INVOKESTATIC, "vf/p08_interface_as_object", "o", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            m.visitVarInsn(ALOAD, 0);
            m.visitMethodInsn(INVOKESTATIC, "vf/p08_interface_as_object", "o2", "(Ljavax/microedition/lcdui/CommandListener;)V", false);
            m.visitVarInsn(ALOAD, 0); m.visitMethodInsn(INVOKEINTERFACE, "java/lang/Runnable", "run", "()V", true);
            m.visitInsn(ARETURN);
        });
        ok("p09_arrays", 5, 2, "([[Ljava/lang/String;)Ljava/lang/Object;", (m, c) -> {
            m.visitVarInsn(ALOAD, 0); m.visitInsn(ICONST_0); m.visitInsn(AALOAD);       // String[]
            m.visitInsn(ICONST_1); m.visitInsn(AALOAD);                                    // String
            m.visitInsn(POP);
            m.visitIntInsn(BIPUSH, 3); m.visitIntInsn(BIPUSH, 4); m.visitMultiANewArrayInsn("[[I", 2);
            m.visitInsn(DUP); m.visitInsn(POP);
            m.visitVarInsn(ASTORE, 1);
            m.visitVarInsn(ALOAD, 0);                       // [[String -> Object[] -> Object / Cloneable
            m.visitInsn(ARETURN);
        });
        ok("p10_switches", 2, 1, "(I)I", (m, c) -> {
            Label a1 = new Label(), b1 = new Label(), d = new Label(), x = new Label(), y = new Label();
            m.visitVarInsn(ILOAD, 0);
            m.visitTableSwitchInsn(0, 1, d, a1, b1);
            m.visitLabel(a1); m.visitInsn(ICONST_1); m.visitInsn(IRETURN);
            m.visitLabel(b1); m.visitVarInsn(ILOAD, 0);
            m.visitLookupSwitchInsn(x, new int[] {5, 9}, new Label[] {x, y});
            m.visitLabel(x); m.visitInsn(ICONST_2); m.visitInsn(IRETURN);
            m.visitLabel(y); m.visitInsn(ICONST_3); m.visitInsn(IRETURN);
            m.visitLabel(d); m.visitInsn(ICONST_0); m.visitInsn(IRETURN);
            c.sm.f(a1, a("I"), a()).f(b1, a("I"), a()).f(x, a("I"), a()).f(y, a("I"), a()).f(d, a("I"), a());
        });
        ok("p11_stack_ops_wide", 8, 1, "()V", (m, c) -> {
            m.visitInsn(LCONST_1); m.visitInsn(ICONST_2); m.visitInsn(DUP_X2);   // int under long: i, long, i
            m.visitInsn(POP); m.visitInsn(POP2);
            m.visitInsn(LCONST_0); m.visitInsn(LCONST_1); m.visitInsn(DUP2_X2); m.visitInsn(POP2); m.visitInsn(POP2); m.visitInsn(POP2);
            m.visitInsn(ICONST_1); m.visitInsn(ICONST_2); m.visitInsn(SWAP); m.visitInsn(POP); m.visitInsn(POP);
            m.visitInsn(RETURN);
        });
        ok("p12_branch_to_pc0", 1, 1, "(I)V", (m, c) -> {
            Label top = new Label();
            m.visitLabel(top);
            m.visitIincInsn(0, -1); m.visitVarInsn(ILOAD, 0); m.visitJumpInsn(IFGT, top); m.visitInsn(RETURN);
            c.sm.f(top, a("I"), a());
        });
        ok("p13_null_flows", 2, 2, "(I)Ljava/lang/String;", (m, c) -> {
            Label a1 = new Label(), b1 = new Label();
            m.visitVarInsn(ILOAD, 0); m.visitJumpInsn(IFEQ, a1);
            m.visitInsn(ACONST_NULL); m.visitJumpInsn(GOTO, b1);
            m.visitLabel(a1); m.visitLdcInsn("x");
            m.visitLabel(b1); m.visitInsn(ARETURN);
            c.sm.f(a1, a("I"), a()).f(b1, a("I"), a("Ljava/lang/String"));
        });
        ok("p14_field_access", 3, 1, "(Lvf/p14_field_access;)I", (m, c) -> {
            m.visitVarInsn(ALOAD, 0); m.visitIntInsn(BIPUSH, 7); m.visitFieldInsn(PUTFIELD, "vf/p14_field_access", "i", "I");
            m.visitVarInsn(ALOAD, 0); m.visitFieldInsn(GETFIELD, "vf/p14_field_access", "i", "I");
            m.visitFieldInsn(GETSTATIC, "vf/p14_field_access", "arr", "[I"); m.visitInsn(ARRAYLENGTH); m.visitInsn(IADD);
            m.visitInsn(IRETURN);
        });
        ok("p15_wide_index", 2, 300, "()V", (m, c) -> {
            m.visitInsn(ICONST_1); m.visitVarInsn(ISTORE, 290); m.visitVarInsn(ILOAD, 290); m.visitIincInsn(290, 1000); m.visitInsn(POP); m.visitInsn(RETURN);
        });
        nextLenient = "accept";
        kase("p17_loop_without_stackmap_lenient_only", "reject", "cldc11", MARK, "no StackMap", ACC_PUBLIC | ACC_STATIC, "t", "(I)I", 2, 2, (m, c) -> {
            Label top = new Label(), end = new Label();
            m.visitInsn(ICONST_0); m.visitVarInsn(ISTORE, 1);
            m.visitLabel(top);
            m.visitVarInsn(ILOAD, 0); c.mark(m); m.visitJumpInsn(IFLE, end);
            m.visitVarInsn(ILOAD, 1); m.visitVarInsn(ILOAD, 0); m.visitInsn(IADD); m.visitVarInsn(ISTORE, 1);
            m.visitIincInsn(0, -1); m.visitJumpInsn(GOTO, top);
            m.visitLabel(end);
            m.visitVarInsn(ILOAD, 1); m.visitInsn(IRETURN);
        });
        // CLDC 1.0: a class without float must verify there
        kase("p16_cldc10_no_float", "accept", "cldc10", ANY, "-", ACC_PUBLIC | ACC_STATIC, "t", "(I)I", 2, 1,
                (m, c) -> { m.visitVarInsn(ILOAD, 0); m.visitInsn(ICONST_1); m.visitInsn(IADD); m.visitInsn(IRETURN); });
    }

    static void negatives() throws Exception {
        bad("n01_wrong_stack_type", MARK, "expected int", 2, 0, "()V", (m, c) -> {
            m.visitInsn(ACONST_NULL); m.visitInsn(ICONST_1); c.mark(m); m.visitInsn(IADD); m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n02_stack_underflow", MARK, "underflow", 2, 0, "()V", (m, c) -> {
            m.visitInsn(ICONST_0); m.visitInsn(POP); c.mark(m); m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n03_stack_overflow", MARK, "overflow", 1, 0, "()V", (m, c) -> {
            m.visitInsn(ICONST_0); c.mark(m); m.visitInsn(ICONST_1); m.visitInsn(POP2); m.visitInsn(RETURN);
        });
        nextLenient = "accept";
        bad("n04_frame_local_mismatch", MARK, "local 1", 2, 2, "(I)V", (m, c) -> {
            Label l = new Label();
            m.visitInsn(FCONST_0); m.visitVarInsn(FSTORE, 1);
            m.visitVarInsn(ILOAD, 0); c.mark(m); m.visitJumpInsn(IFEQ, l);   // the branch is where the state is compared
            m.visitLabel(l); m.visitInsn(RETURN);
            c.sm.f(l, a("I", "I"), a());                                     // frame says local 1 is int, it is float
        });
        nextLenient = "accept";
        bad("n05_branch_without_frame", MARK, "no StackMap", 1, 1, "(I)V", (m, c) -> {
            Label l = new Label();
            m.visitVarInsn(ILOAD, 0); c.mark(m); m.visitJumpInsn(IFEQ, l);
            m.visitLabel(l); m.visitInsn(RETURN);
        });
        bad("n06_falls_off_end", MARK, "falls off", 1, 0, "()V", (m, c) -> {
            m.visitInsn(ICONST_0); m.visitInsn(POP); c.mark(m);
        });
        bad("n07_invoke_on_uninit", MARK, "receiver", 2, 0, "()I", (m, c) -> {
            m.visitTypeInsn(NEW, "java/lang/Object");
            c.mark(m); m.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "hashCode", "()I", false);
            m.visitInsn(IRETURN);
        });
        bad("n08_return_uninit", MARK, "returning", 2, 0, "()Ljava/lang/Object;", (m, c) -> {
            m.visitTypeInsn(NEW, "java/lang/Object"); c.mark(m); m.visitInsn(ARETURN);
        });
        kase("n09_ctor_no_super_call", "reject", "cldc11", MARK, "before the superclass", ACC_PUBLIC, "<init>", "()V", 1, 1, (m, c) -> {
            c.mark(m); m.visitInsn(RETURN);
        });
        bad("n10_init_wrong_class", MARK, "created as a different class", 3, 0, "()V", (m, c) -> {
            m.visitTypeInsn(NEW, "java/lang/Object"); m.visitInsn(DUP);
            c.mark(m); m.visitMethodInsn(INVOKESPECIAL, "java/lang/String", "<init>", "()V", false);
            m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n11_split_long_pop", MARK, "pop", 2, 0, "()V", (m, c) -> {
            m.visitInsn(LCONST_0); c.mark(m); m.visitInsn(POP); m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n12_long_second_half_as_int", MARK, "local 2", 2, 3, "()V", (m, c) -> {
            m.visitInsn(LCONST_0); m.visitVarInsn(LSTORE, 1); c.mark(m); m.visitVarInsn(ILOAD, 2); m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n13_overwrite_half_then_lload", MARK, "not long", 2, 3, "()V", (m, c) -> {
            m.visitInsn(LCONST_0); m.visitVarInsn(LSTORE, 0); m.visitInsn(ICONST_0); m.visitVarInsn(ISTORE, 1);
            c.mark(m); m.visitVarInsn(LLOAD, 0); m.visitInsn(POP2); m.visitInsn(RETURN);
        });
        nextLenient = "accept";
        bad("n14_handler_frame_two_items", ANY, "exactly one", 2, 1, "()V", (m, c) -> {
            Label s = new Label(), e = new Label(), h = new Label();
            m.visitTryCatchBlock(s, e, h, "java/lang/Exception");
            m.visitLabel(s); m.visitInsn(NOP); m.visitLabel(e); m.visitInsn(RETURN);
            m.visitLabel(h); m.visitInsn(RETURN);
            c.sm.f(h, a(), a("Ljava/lang/Exception", "I"));
        });
        bad("n15_catch_type_not_throwable", METHOD, "not a Throwable", 1, 0, "()V", (m, c) -> {
            Label s = new Label(), e = new Label(), h = new Label();
            m.visitTryCatchBlock(s, e, h, "java/lang/String");
            m.visitLabel(s); m.visitInsn(NOP); m.visitLabel(e); m.visitInsn(RETURN);
            m.visitLabel(h); m.visitInsn(POP); m.visitInsn(RETURN);
            c.sm.f(h, a(), a("Ljava/lang/String"));
        });
        bad("n16_areturn_wrong_type", MARK, "returning", 1, 1, "(Ljava/lang/Object;)Ljava/lang/String;", (m, c) -> {
            m.visitVarInsn(ALOAD, 0); c.mark(m); m.visitInsn(ARETURN);
        });
        bad("n17_putfield_wrong_type", MARK, "expected", 2, 1, "(Lvf/n17_putfield_wrong_type;)V", (m, c) -> {
            m.visitVarInsn(ALOAD, 0); m.visitInsn(ACONST_NULL);
            c.mark(m); m.visitFieldInsn(PUTFIELD, "vf/n17_putfield_wrong_type", "i", "I"); m.visitInsn(RETURN);
        });
        bad("n18_invoke_arg_mismatch", MARK, "expected", 2, 0, "()V", (m, c) -> {
            m.visitInsn(ICONST_0);
            c.mark(m); m.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;", false);
            m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n19_iaload_on_object_array", MARK, "expected array", 2, 1, "([Ljava/lang/Object;)V", (m, c) -> {
            m.visitVarInsn(ALOAD, 0); m.visitInsn(ICONST_0); c.mark(m); m.visitInsn(IALOAD); m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n20_athrow_non_throwable", MARK, "non-Throwable", 1, 0, "()V", (m, c) -> {
            m.visitLdcInsn("s"); c.mark(m); m.visitInsn(ATHROW);
        });
        bad("n21_jsr_ret", MARK, "jsr/ret", 2, 2, "()V", (m, c) -> {
            Label sub = new Label();
            c.mark(m); m.visitJumpInsn(JSR, sub);
            m.visitInsn(RETURN);
            m.visitLabel(sub); m.visitVarInsn(ASTORE, 0); m.visitVarInsn(RET, 0);
        });
        nextLenient = "accept";
        bad("n22_stackmap_offset_inside_insn", ANY, "instruction boundary", 2, 1, "()V", (m, c) -> {
            Label l = new Label();
            m.visitIntInsn(SIPUSH, 1000); m.visitInsn(POP);
            m.visitLabel(l); m.visitInsn(RETURN);
            c.sm.f(1, a(), a());           // offset 1 is inside the operand of sipush
        });
        nextLenient = "accept";
        bad("n23_stackmap_local_beyond_max", ANY, "exceed max_locals", 1, 1, "(I)V", (m, c) -> {
            Label l = new Label();
            m.visitVarInsn(ILOAD, 0); m.visitJumpInsn(IFEQ, l); m.visitLabel(l); m.visitInsn(RETURN);
            c.sm.f(l, a("I", "I", "I"), a());
        });
        nextLenient = "accept";
        bad("n24_dead_code_without_frame", MARK, "no StackMap", 1, 0, "()V", (m, c) -> {
            m.visitInsn(RETURN); c.mark(m); m.visitInsn(RETURN);
        });
        kase("n25_float_in_cldc10", "reject", "cldc10", MARK, "floating point", ACC_PUBLIC | ACC_STATIC, "t", "()V", 2, 0, (m, c) -> {
            m.visitInsn(ICONST_1); c.mark(m); m.visitInsn(I2F); m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n26_illegal_opcode", MARK, "illegal opcode", 1, 0, "()V", (m, c) -> {
            m.visitInsn(ICONST_0); c.mark(m); m.visitInsn(NOP); m.visitInsn(POP); m.visitInsn(RETURN);   // NOP patched to 0xFE below
        });
        bad("n27_uninit_backward_branch", MARK, "backward", 2, 0, "()V", (m, c) -> {
            Label top = new Label();
            m.visitLabel(top);
            m.visitTypeInsn(NEW, "java/lang/Object");
            c.mark(m); m.visitJumpInsn(GOTO, top);
            c.sm.f(top, a(), a());
        });
        bad("n28_monitorenter_int", MARK, "reference", 1, 0, "()V", (m, c) -> {
            m.visitInsn(ICONST_0); c.mark(m); m.visitInsn(MONITORENTER); m.visitInsn(RETURN);
        });
        bad("n29_getfield_wrong_receiver", MARK, "expected", 1, 1, "(Ljava/lang/Object;)I", (m, c) -> {
            m.visitVarInsn(ALOAD, 0); c.mark(m); m.visitFieldInsn(GETFIELD, "vf/n29_getfield_wrong_receiver", "i", "I"); m.visitInsn(IRETURN);
        });
        bad("n30_init_twice", MARK, "already initialized", 3, 0, "()V", (m, c) -> {
            m.visitTypeInsn(NEW, "java/lang/Object"); m.visitInsn(DUP); m.visitInsn(DUP);
            m.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            c.mark(m); m.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n31_multianewarray_dims", MARK, "multianewarray", 3, 0, "()V", (m, c) -> {
            m.visitInsn(ICONST_1); m.visitInsn(ICONST_1); c.mark(m); m.visitMultiANewArrayInsn("[I", 2); m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n32_newarray_bad_type", MARK, "newarray", 1, 0, "()V", (m, c) -> {
            m.visitInsn(ICONST_1); c.mark(m); m.visitIntInsn(NEWARRAY, 3); m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n33_iinc_on_reference", MARK, "iinc", 1, 1, "(Ljava/lang/Object;)V", (m, c) -> {
            c.mark(m); m.visitIincInsn(0, 1); m.visitInsn(RETURN);
        });
        nextLenient = "accept";
        bad("n34_handler_local_mismatch", MARK, "handler", 2, 2, "()V", (m, c) -> {
            Label s = new Label(), e = new Label(), h = new Label();
            m.visitTryCatchBlock(s, e, h, "java/lang/Exception");
            m.visitLdcInsn("s"); m.visitVarInsn(ASTORE, 1);
            m.visitLabel(s); m.visitInsn(ICONST_0); m.visitVarInsn(ISTORE, 1);
            c.mark(m); m.visitInsn(NOP);                          // here local 1 is int, the handler frame wants a String
            m.visitLabel(e); m.visitInsn(RETURN);
            m.visitLabel(h); m.visitInsn(POP); m.visitInsn(RETURN);
            c.sm.f(h, a("T", "Ljava/lang/String"), a("Ljava/lang/Exception"));
        });
        bad("n35_local_beyond_max_locals", MARK, "out of range", 1, 1, "()V", (m, c) -> {
            m.visitInsn(ICONST_0); c.mark(m); m.visitVarInsn(ISTORE, 3); m.visitInsn(RETURN);
        });
        bad("n36_unknown_class_in_check", MARK, "cannot load", 1, 1, "(Lvf/DoesNotExist;)Ljava/lang/String;", (m, c) -> {
            m.visitVarInsn(ALOAD, 0); c.mark(m); m.visitInsn(ARETURN);
        });
        nextLenient = "accept";
        bad("n37_stack_depth_at_merge", MARK, "stack depth", 2, 1, "(I)V", (m, c) -> {
            Label l = new Label();
            m.visitInsn(ICONST_0); m.visitVarInsn(ILOAD, 0); c.mark(m); m.visitJumpInsn(IFEQ, l);
            m.visitLabel(l); m.visitInsn(POP); m.visitInsn(RETURN);
            c.sm.f(l, a("I"), a());                              // actual stack has one int, the frame says empty
        });
        kase("n38_float_ldc_cldc10", "reject", "cldc10", MARK, "floating point", ACC_PUBLIC | ACC_STATIC, "t", "()V", 1, 0, (m, c) -> {
            c.mark(m); m.visitLdcInsn(1.5f); m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n39_aastore_into_int_array", MARK, "reference array", 3, 1, "([I)V", (m, c) -> {
            m.visitVarInsn(ALOAD, 0); m.visitInsn(ICONST_0); m.visitInsn(ACONST_NULL); c.mark(m); m.visitInsn(AASTORE); m.visitInsn(RETURN);
        });
        bad("n40_long_compared_as_int", MARK, "expected int", 2, 0, "()V", (m, c) -> {
            Label l = new Label();
            m.visitInsn(LCONST_0); c.mark(m); m.visitJumpInsn(IFEQ, l); m.visitLabel(l); m.visitInsn(RETURN);
        });
        bad("n41_wrong_receiver_class", MARK, "receiver", 2, 0, "()V", (m, c) -> {
            m.visitLdcInsn("s"); c.mark(m); m.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "length", "()I", false);
            m.visitInsn(POP); m.visitInsn(RETURN);
        });
        bad("n42_intarray_as_cloneable", MARK, "-", 1, 1, "([I)V", (m, c) -> {
            m.visitVarInsn(ALOAD, 0); c.mark(m); m.visitMethodInsn(INVOKESTATIC, "vf/n42_intarray_as_cloneable", "o", "(Ljava/lang/Cloneable;)V", false); m.visitInsn(RETURN);
        });
        bad("n43_objarray_as_serializable", MARK, "-", 1, 1, "([Ljava/lang/Object;)V", (m, c) -> {
            m.visitVarInsn(ALOAD, 0); c.mark(m); m.visitMethodInsn(INVOKESTATIC, "vf/n43_objarray_as_serializable", "o", "(Ljava/io/Serializable;)V", false); m.visitInsn(RETURN);
        });
    }
}

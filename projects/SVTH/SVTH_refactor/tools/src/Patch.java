import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

/**
 * Patch <orig.jar> <outClassesDir> [--debug]
 * Đường B (hook bytecode): chỉ THÊM accessor sfx$* và lời gọi static SfxHooks.* vào class gốc; không đổi logic game.
 * Các lời gọi dùng dup2 để giữ nguyên ngăn xếp: [obj, giá trị] → dup2 → [obj, giá trị, obj, giá trị] → gọi hook → [obj, giá trị] → lệnh gốc.
 */
public class Patch {
    static final String HOOKS = "SfxHooks";
    static int sites = 0;
    static List<String> report = new ArrayList<>();

    public static void main(String[] a) throws Exception {
        String jar = a[0], out = a[1];
        boolean debug = a.length > 2 && a[2].equals("--debug");
        Map<String, ClassNode> cls = new LinkedHashMap<>();
        try (ZipFile z = new ZipFile(jar)) {
            for (Enumeration<? extends ZipEntry> en = z.entries(); en.hasMoreElements();) {
                ZipEntry ze = en.nextElement();
                String n = ze.getName();
                if (n.equals("e.class") || n.equals("bx.class") || n.equals("bn.class") || n.equals("da.class") || n.equals("av.class") || n.equals("as.class") || n.equals("ab.class") || (debug && DebugPatch.wants(n))) {
                    ClassNode cn = new ClassNode();
                    new ClassReader(z.getInputStream(ze).readAllBytes()).accept(cn, 0);
                    cls.put(cn.name, cn);
                }
            }
        }
        patchE(cls.get("e"));
        patchBx(cls.get("bx"));
        patchBn(cls.get("bn"));
        patchDa(cls.get("da"));
        patchUi(cls.get("av"), cls.get("as"), cls.get("ab"));
        if (debug) DebugPatch.apply(cls, report);
        Files.createDirectories(Paths.get(out));
        for (ClassNode cn : cls.values()) {
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cn.accept(cw);
            Files.write(Paths.get(out, cn.name + ".class"), cw.toByteArray());
        }
        for (String r : report) System.out.println(r);
        System.out.println("patch: classes=" + cls.size() + " hook sites=" + sites);
    }

    // ---------- helpers ----------
    static MethodNode method(ClassNode cn, String name, String desc) {
        for (MethodNode m : cn.methods) if (m.name.equals(name) && m.desc.equals(desc)) return m;
        throw new RuntimeException("method not found " + cn.name + "." + name + desc);
    }
    static boolean hasField(ClassNode cn, String name, String desc) {
        for (FieldNode f : cn.fields) if (f.name.equals(name) && f.desc.equals(desc)) return true;
        return false;
    }
    static void requireField(ClassNode cn, String name, String desc) {
        if (!hasField(cn, name, desc)) throw new RuntimeException("field not found " + cn.name + "." + name + ":" + desc);
    }
    /** Thêm accessor: getter đơn giản trả field (retDesc có thể rộng hơn field, ví dụ S→I). */
    static void getter(ClassNode cn, String acc, String retDesc, String field, String fdesc) {
        requireField(cn, field, fdesc);
        MethodNode m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, acc, "()" + retDesc, null, null);
        m.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        m.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, cn.name, field, fdesc));
        m.instructions.add(new InsnNode(retDesc.equals("I") || retDesc.equals("Z") ? Opcodes.IRETURN : Opcodes.ARETURN));
        cn.methods.add(m);
        report.add("  + " + cn.name + "." + acc + "()" + retDesc + "  <- " + field + ":" + fdesc);
    }
    static void add(ClassNode cn, MethodNode m, String note) { cn.methods.add(m); report.add("  + " + cn.name + "." + m.name + m.desc + "  " + note); }

    static InsnList seq(AbstractInsnNode... n) { InsnList l = new InsnList(); for (AbstractInsnNode x : n) l.add(x); return l; }
    static MethodInsnNode hook2(String owner, String name, String desc) { return new MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, desc, false); }
    static MethodInsnNode hook(String name, String desc) { return new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS, name, desc, false); }

    // ---------- class e ----------
    static void patchE(ClassNode e) {
        report.add("class e (actor chiến đấu):");
        getter(e, "sfx$slot", "I", "a", "I");
        getter(e, "sfx$role", "I", "d", "I");
        getter(e, "sfx$partyUid", "I", "a", "S");   // session 7: phân biệt nhân vật chính (0) với sủng vật (petUid ≥ 1) để chọn giọng SFX
        getter(e, "sfx$state", "I", "f", "I");
        getter(e, "sfx$target", "I", "b", "I");
        getter(e, "sfx$crit", "Z", "m", "Z");
        getter(e, "sfx$dodge", "Z", "o", "Z");
        getter(e, "sfx$targets", "[B", "b", "[B");
        getter(e, "sfx$bx", "Lbx;", "a", "Lbx;");
        getter(e, "sfx$hitFrames", "[B", "c", "[B");
        getter(e, "sfx$hp", "I", "m", "I");
        getter(e, "sfx$mp", "I", "n", "I");
        getter(e, "sfx$dead", "Z", "f", "Z");
        getter(e, "sfx$visible", "Z", "b", "Z");
        requireField(e, "a", "[I");
        for (int[] x : new int[][]{{3, 0}, {10, 1}}) {
            MethodNode m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, x[1] == 0 ? "sfx$hpReal" : "sfx$hpMax", "()I", null, null);
            m.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            m.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "e", "a", "[I"));
            m.instructions.add(new IntInsnNode(Opcodes.BIPUSH, x[0]));
            m.instructions.add(new InsnNode(Opcodes.IALOAD));
            m.instructions.add(new InsnNode(Opcodes.IRETURN));
            add(e, m, "<- a[I][" + x[0] + "]");
        }
    }

    // ---------- class bx ----------
    static void patchBx(ClassNode bx) {
        report.add("class bx (bộ phát animation):");
        bx.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "sfx$name", "Ljava/lang/String;", null, null));
        bx.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "sfx$last", "I", null, null));
        report.add("  + field bx.sfx$name:String, bx.sfx$last:I");
        getter(bx, "sfx$name", "Ljava/lang/String;", "sfx$name", "Ljava/lang/String;");
        getter(bx, "sfx$last", "I", "sfx$last", "I");
        getter(bx, "sfx$anim", "I", "a", "S");
        MethodNode m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "sfx$setLast", "(I)V", null, null);
        m.instructions.add(seq(new VarInsnNode(Opcodes.ALOAD, 0), new VarInsnNode(Opcodes.ILOAD, 1), new FieldInsnNode(Opcodes.PUTFIELD, "bx", "sfx$last", "I"), new InsnNode(Opcodes.RETURN)));
        add(bx, m, "");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "sfx$frames", "(I)I", null, null);
        m.instructions.add(seq(new VarInsnNode(Opcodes.ALOAD, 0), new VarInsnNode(Opcodes.ILOAD, 1), new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bx", "a", "(I)I", false), new InsnNode(Opcodes.IRETURN)));
        add(bx, m, "<- bx.a(I)I (số frame của anim)");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "sfx$frame", "()I", null, null);
        m.instructions.add(seq(new VarInsnNode(Opcodes.ALOAD, 0), new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bx", "c", "()I", false), new InsnNode(Opcodes.IRETURN)));
        add(bx, m, "<- bx.c()I (frame hiện tại)");

        // ghi tên file .av khi nạp
        String[][] loaders = {{"a", "(Ljava/lang/String;)Z"}, {"a", "(Ljava/lang/String;I)Z"}, {"a", "(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[I[II)Z"}};
        for (String[] l : loaders) {
            MethodNode mm = method(bx, l[0], l[1]);
            mm.instructions.insert(seq(new VarInsnNode(Opcodes.ALOAD, 0), new VarInsnNode(Opcodes.ALOAD, 1), new FieldInsnNode(Opcodes.PUTFIELD, "bx", "sfx$name", "Ljava/lang/String;")));
            sites++; report.add("  hook bx." + l[0] + l[1] + " entry: ghi tên .av vào sfx$name");
        }
        // đặt anim/frame → xoá dấu frame cuối đã thấy
        for (String d : new String[]{"a", "b"}) {
            MethodNode mm = method(bx, d, "(I)V");
            mm.instructions.insert(seq(new VarInsnNode(Opcodes.ALOAD, 0), new InsnNode(Opcodes.ICONST_M1), new FieldInsnNode(Opcodes.PUTFIELD, "bx", "sfx$last", "I")));
            sites++; report.add("  hook bx." + d + "(I)V entry: sfx$last = -1");
        }
        // điểm tăng frame duy nhất: bx.a(Z)V — gọi onFrame trước mỗi RETURN
        MethodNode adv = method(bx, "a", "(Z)V");
        int n = 0;
        for (AbstractInsnNode in : adv.instructions.toArray()) {
            if (in.getOpcode() == Opcodes.RETURN) {
                adv.instructions.insertBefore(in, seq(new VarInsnNode(Opcodes.ALOAD, 0), hook("onFrame", "(Lbx;)V")));
                n++; sites++;
            }
        }
        report.add("  hook bx.a(Z)V: onFrame(bx) trước " + n + " RETURN");
    }

    // ---------- class bn ----------
    static void patchBn(ClassNode bn) {
        report.add("class bn (trận chiến):");
        requireField(bn, "a", "[Le;");
        requireField(bn, "j", "I");
        MethodNode m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "sfx$actor", "(I)Le;", null, null);
        m.instructions.add(seq(new VarInsnNode(Opcodes.ALOAD, 0), new FieldInsnNode(Opcodes.GETFIELD, "bn", "a", "[Le;"), new VarInsnNode(Opcodes.ILOAD, 1), new InsnNode(Opcodes.AALOAD), new InsnNode(Opcodes.ARETURN)));
        add(bn, m, "<- bn.a[Le;[slot]");
        getter(bn, "sfx$cur", "I", "j", "I");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "sfx$count", "()I", null, null);
        m.instructions.add(seq(new VarInsnNode(Opcodes.ALOAD, 0), new FieldInsnNode(Opcodes.GETFIELD, "bn", "a", "[Le;"), new InsnNode(Opcodes.ARRAYLENGTH), new InsnNode(Opcodes.IRETURN)));
        add(bn, m, "<- bn.a.length");

        // e.g := true (nhịp đánh thường) trong f()
        MethodNode f = method(bn, "f", "()V");
        int g = 0;
        for (AbstractInsnNode in : f.instructions.toArray()) {
            if (in instanceof FieldInsnNode && in.getOpcode() == Opcodes.PUTFIELD) {
                FieldInsnNode fi = (FieldInsnNode) in;
                if (fi.owner.equals("e") && fi.name.equals("g") && fi.desc.equals("Z")) {
                    f.instructions.insertBefore(in, seq(new InsnNode(Opcodes.DUP2), new VarInsnNode(Opcodes.ALOAD, 0), hook("onBlow", "(Le;ZLbn;)V")));
                    g++; sites++;
                }
            }
        }
        report.add("  hook bn.f(): onBlow(e,Z,bn) trước " + g + " putfield e.g:Z");
        if (g != 4) throw new RuntimeException("expected 4 putfield e.g:Z in bn.f, got " + g);

        // e.f := state (mọi chỗ trong bn)
        int s = 0;
        for (MethodNode mm : bn.methods) {
            if (mm.name.startsWith("sfx$") || (mm.access & Opcodes.ACC_STATIC) != 0) continue;
            for (AbstractInsnNode in : mm.instructions.toArray()) {
                if (in instanceof FieldInsnNode && in.getOpcode() == Opcodes.PUTFIELD) {
                    FieldInsnNode fi = (FieldInsnNode) in;
                    if (fi.owner.equals("e") && fi.name.equals("f") && fi.desc.equals("I")) {
                        mm.instructions.insertBefore(in, seq(new InsnNode(Opcodes.DUP2), new VarInsnNode(Opcodes.ALOAD, 0), hook("onState", "(Le;ILbn;)V")));
                        s++; sites++;
                    }
                }
            }
        }
        report.add("  hook bn.*: onState(e,I,bn) trước " + s + " putfield e.f:I");
        if (s != 44) throw new RuntimeException("expected 44 putfield e.f:I in bn, got " + s);

        // áp kỹ năng
        MethodNode sk = method(bn, "a", "(Le;II)V");
        sk.instructions.insert(seq(new VarInsnNode(Opcodes.ALOAD, 0), new VarInsnNode(Opcodes.ALOAD, 1), new VarInsnNode(Opcodes.ILOAD, 2), new VarInsnNode(Opcodes.ILOAD, 3), hook("onSkillApply", "(Lbn;Le;II)V")));
        sites++; report.add("  hook bn.a(Le;II)V entry: onSkillApply(bn,e,skillId,level)");
        // vào trận / kết thúc
        MethodNode st = method(bn, "c", "()V");
        st.instructions.insert(seq(new VarInsnNode(Opcodes.ALOAD, 0), hook("battleStart", "(Lbn;)V")));
        sites++; report.add("  hook bn.c()V entry: battleStart(bn)");
        // 1 = thắng (h), 2 = thua (e), 3 = hết vòng (g), 4 = màn kết quả (b)
        String[][] ends = {{"h", "()V", "1"}, {"e", "()V", "2"}, {"g", "()V", "3"}, {"b", "()V", "4"}};
        for (String[] x : ends) {
            MethodNode en = method(bn, x[0], x[1]);
            en.instructions.insert(seq(new InsnNode(Opcodes.ICONST_0 + Integer.parseInt(x[2])), hook("battleEnd", "(I)V")));
            sites++; report.add("  hook bn." + x[0] + "()V entry: battleEnd(" + x[2] + ") (flush)");
        }
    }

    static void patchDa(ClassNode da) {
        report.add("class da (canvas chính):");
        MethodNode hn = method(da, "hideNotify", "()V");
        hn.instructions.insert(hook("paused", "()V"));
        sites++; report.add("  hook da.hideNotify()V entry: paused() (flush SFX)");
    }

    // ---------- giao diện menu: tách SFX khỏi nhạc, bỏ hộp thoại hỏi nhạc (SfxUi) ----------
    static final String UI = "SfxUi";
    static final String MUSIC_ROW = "C\u00e0i \u0111\u1eb7t nh\u1ea1c";

    /** Thay mọi lệnh đọc field tĩnh (owner.name:desc) trong method bằng INVOKESTATIC SfxUi.<fn><fdesc>; trả số chỗ đã thay. */
    static int replaceGetstatic(MethodNode m, String owner, String name, String desc, String fn, String fdesc) {
        int n = 0;
        for (AbstractInsnNode in : m.instructions.toArray()) {
            if (in.getOpcode() == Opcodes.GETSTATIC) {
                FieldInsnNode fi = (FieldInsnNode) in;
                if (fi.owner.equals(owner) && fi.name.equals(name) && fi.desc.equals(desc)) {
                    m.instructions.set(in, hook2(UI, fn, fdesc));
                    n++; sites++;
                }
            }
        }
        return n;
    }
    static void expect(String what, int got, int want) {
        if (got != want) throw new RuntimeException(what + ": expected " + want + " got " + got);
    }

    static void patchUi(ClassNode av, ClassNode as, ClassNode ab) {
        report.add("class av (màn logo):");
        int n = 0;
        for (MethodNode m : av.methods) n += replaceGetstatic(m, "at", "a", "Z", "skipPrompt", "()Z");
        expect("av at.a:Z", n, 2);
        report.add("  hook av: " + n + " x getstatic at.a:Z -> SfxUi.skipPrompt() (bỏ hộp thoại 'Bật âm nhạc?', tự bật nhạc)");

        report.add("class as (menu hệ thống):");
        requireField(as, "b", "Lbx;");
        requireField(as, "x", "I");
        requireField(as, "y", "I");
        MethodNode ic = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "sfx$icon", "(I)V", null, null);
        LabelNode end = new LabelNode();
        ic.instructions.add(seq(new VarInsnNode(Opcodes.ALOAD, 0), new FieldInsnNode(Opcodes.GETFIELD, "as", "b", "Lbx;"), new VarInsnNode(Opcodes.ASTORE, 2),
                new VarInsnNode(Opcodes.ALOAD, 2), new JumpInsnNode(Opcodes.IFNULL, end),
                new VarInsnNode(Opcodes.ALOAD, 2), new VarInsnNode(Opcodes.ILOAD, 1), new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bx", "a", "(I)V", false),
                end, new InsnNode(Opcodes.RETURN)));
        add(as, ic, "<- if (this.b:bx != null) this.b.a(anim)  (biểu tượng loa)");
        // danh sách dòng menu: chèn "Cài đặt SFX"
        MethodNode p = method(as, "p", "()V");
        n = 0;
        for (AbstractInsnNode in : p.instructions.toArray()) {
            if (in.getOpcode() == Opcodes.PUTSTATIC) {
                FieldInsnNode fi = (FieldInsnNode) in;
                if (fi.owner.equals("as") && fi.name.equals("a") && fi.desc.equals("[Ljava/lang/String;")) {
                    p.instructions.insertBefore(in, hook2(UI, "menuRows", "([Ljava/lang/String;)[Ljava/lang/String;"));
                    n++; sites++;
                }
            }
        }
        expect("as.p() putstatic as.a:[String", n, 2);
        report.add("  hook as.p()V: SfxUi.menuRows(String[]) trước " + n + " putstatic as.a:[String (thêm dòng 'Cài đặt SFX')");
        // trang âm lượng: khởi tạo biểu tượng + vẽ
        MethodNode init = method(as, "a", "(BB)V");
        init.instructions.insert(seq(new VarInsnNode(Opcodes.ALOAD, 0), new VarInsnNode(Opcodes.ILOAD, 1), new VarInsnNode(Opcodes.ILOAD, 2), hook2(UI, "page", "(Las;II)V")));
        sites++; report.add("  hook as.a(BB)V entry: SfxUi.page(as,group,page)");
        expect("as.a(BB) o.g", replaceGetstatic(init, "o", "g", "B", "vol", "()B"), 1);
        expect("as.a(BB) at.b", replaceGetstatic(init, "at", "b", "Z", "on", "()Z"), 1);
        MethodNode paint = method(as, "h", "(Ljavax/microedition/lcdui/Graphics;)V");
        expect("as.h(G) o.g", replaceGetstatic(paint, "o", "g", "B", "vol", "()B"), 2);
        expect("as.h(G) at.b", replaceGetstatic(paint, "at", "b", "Z", "on", "()Z"), 1);
        AbstractInsnNode onCall = null;
        for (AbstractInsnNode in : paint.instructions.toArray()) {
            if (in instanceof MethodInsnNode && ((MethodInsnNode) in).owner.equals(UI) && ((MethodInsnNode) in).name.equals("on")) onCall = in;
        }
        AbstractInsnNode ret = onCall;
        while (ret != null && ret.getOpcode() != Opcodes.RETURN) ret = ret.getNext();
        if (ret == null) throw new RuntimeException("as.h(G): no RETURN after volume block");
        paint.instructions.insertBefore(ret, seq(new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ALOAD, 0), new FieldInsnNode(Opcodes.GETFIELD, "as", "x", "I"),
                new VarInsnNode(Opcodes.ALOAD, 0), new FieldInsnNode(Opcodes.GETFIELD, "as", "y", "I"),
                hook2(UI, "paint", "(Ljavax/microedition/lcdui/Graphics;II)V")));
        sites++;
        report.add("  hook as.h(Graphics)V: 2 x o.g:B -> SfxUi.vol(), 1 x at.b:Z -> SfxUi.on(), SfxUi.paint(g,x,y) trước RETURN của khối âm lượng");

        report.add("class ab (xử lý phím menu):");
        MethodNode h = method(ab, "h", "(I)V");
        LabelNode cont = new LabelNode();
        h.instructions.insert(seq(new VarInsnNode(Opcodes.ILOAD, 0), hook2(UI, "key", "(I)Z"), new JumpInsnNode(Opcodes.IFEQ, cont), new InsnNode(Opcodes.RETURN), cont));
        sites++; report.add("  hook ab.h(I)V entry: if (SfxUi.key(k)) return;  (phím trái/phải của trang SFX)");
        n = 0;
        for (AbstractInsnNode in : h.instructions.toArray()) {
            if (in instanceof LdcInsnNode && MUSIC_ROW.equals(((LdcInsnNode) in).cst)) {
                AbstractInsnNode nx = in.getNext();
                if (nx instanceof MethodInsnNode && nx.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) nx).owner.equals("java/lang/String") && ((MethodInsnNode) nx).name.equals("equals")) {
                    h.instructions.set(nx, hook2(UI, "isSoundRow", "(Ljava/lang/String;Ljava/lang/Object;)Z"));
                    n++; sites++;
                }
            }
        }
        expect("ab.h String.equals(\"Cài đặt nhạc\")", n, 1);
        report.add("  hook ab.h(I)V: String.equals(\"Cài đặt nhạc\") -> SfxUi.isSoundRow(row, lit)  (dòng nhạc và dòng SFX cùng mở trang âm lượng)");
    }
}

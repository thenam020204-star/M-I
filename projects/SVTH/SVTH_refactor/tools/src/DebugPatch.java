import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.util.*;

/**
 * Patch RIÊNG cho bản debug (Patch --debug): accessor/setter dbg$*, và hook gọi DebugBattle.*.
 * Bản release KHÔNG chạy file này nên không chứa bất kỳ mã debug nào.
 */
public class DebugPatch {
    static boolean wants(String entry) { return entry.equals("cd.class") || entry.equals("ci.class"); }

    static void apply(Map<String, ClassNode> cls, List<String> report) {
        ClassNode e = cls.get("e"), bn = cls.get("bn"), da = cls.get("da"), cd = cls.get("cd");
        report.add("DEBUG patch:");
        // ---- e: setter ----
        setter(e, "dbg$setHp", "(I)V", "m", "I", report);
        setter(e, "dbg$setCrit", "(Z)V", "m", "Z", report);
        MethodNode m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dbg$setHpReal", "(I)V", null, null);
        m.instructions.add(Patch.seq(new VarInsnNode(Opcodes.ALOAD, 0), new FieldInsnNode(Opcodes.GETFIELD, "e", "a", "[I"), new IntInsnNode(Opcodes.BIPUSH, 3), new VarInsnNode(Opcodes.ILOAD, 1), new InsnNode(Opcodes.IASTORE), new InsnNode(Opcodes.RETURN)));
        Patch.add(e, m, "(a[3]=v)");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dbg$mpFull", "()V", null, null);
        m.instructions.add(Patch.seq(
            new VarInsnNode(Opcodes.ALOAD, 0), new VarInsnNode(Opcodes.ALOAD, 0), new FieldInsnNode(Opcodes.GETFIELD, "e", "a", "[I"), new IntInsnNode(Opcodes.BIPUSH, 11), new InsnNode(Opcodes.IALOAD), new FieldInsnNode(Opcodes.PUTFIELD, "e", "n", "I"),
            new VarInsnNode(Opcodes.ALOAD, 0), new FieldInsnNode(Opcodes.GETFIELD, "e", "a", "[I"), new IntInsnNode(Opcodes.BIPUSH, 5),
            new VarInsnNode(Opcodes.ALOAD, 0), new FieldInsnNode(Opcodes.GETFIELD, "e", "a", "[I"), new IntInsnNode(Opcodes.BIPUSH, 11), new InsnNode(Opcodes.IALOAD), new InsnNode(Opcodes.IASTORE), new InsnNode(Opcodes.RETURN)));
        Patch.add(e, m, "(MP đầy)");

        // ---- e: bảng kỹ năng (d[] a) — nhân vật chưa học kỹ năng nào có a == null và mã game (bn.f) sẽ NPE khi tăng EXP kỹ năng ----
        Patch.requireField(e, "a", "[Ld;");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dbg$noSkills", "()Z", null, null);
        LabelNode lnn = new LabelNode();
        m.instructions.add(Patch.seq(new VarInsnNode(Opcodes.ALOAD, 0), new FieldInsnNode(Opcodes.GETFIELD, "e", "a", "[Ld;"), new JumpInsnNode(Opcodes.IFNONNULL, lnn),
            new InsnNode(Opcodes.ICONST_1), new InsnNode(Opcodes.IRETURN), lnn, new InsnNode(Opcodes.ICONST_0), new InsnNode(Opcodes.IRETURN)));
        Patch.add(e, m, "(a == null)");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dbg$giveSkills", "()V", null, null);
        m.instructions.add(Patch.seq(new VarInsnNode(Opcodes.ALOAD, 0), new InsnNode(Opcodes.ICONST_0), new TypeInsnNode(Opcodes.ANEWARRAY, "d"), new FieldInsnNode(Opcodes.PUTFIELD, "e", "a", "[Ld;"), new InsnNode(Opcodes.RETURN)));
        Patch.add(e, m, "(a = new d[0])");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dbg$takeSkills", "()V", null, null);
        m.instructions.add(Patch.seq(new VarInsnNode(Opcodes.ALOAD, 0), new InsnNode(Opcodes.ACONST_NULL), new FieldInsnNode(Opcodes.PUTFIELD, "e", "a", "[Ld;"), new InsnNode(Opcodes.RETURN)));
        Patch.add(e, m, "(a = null)");

        // ---- bn: kế hoạch lượt / điều khiển ----
        Patch.requireField(bn, "e", "[[I");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dbg$plan", "(IIIII)V", null, null);
        InsnList l = new InsnList();
        for (int k = 1; k <= 4; k++) {
            l.add(new VarInsnNode(Opcodes.ALOAD, 0)); l.add(new FieldInsnNode(Opcodes.GETFIELD, "bn", "e", "[[I"));
            l.add(new VarInsnNode(Opcodes.ILOAD, 1)); l.add(new InsnNode(Opcodes.AALOAD));
            l.add(new InsnNode(Opcodes.ICONST_0 + k)); l.add(new VarInsnNode(Opcodes.ILOAD, 1 + k)); l.add(new InsnNode(Opcodes.IASTORE));
        }
        l.add(new InsnNode(Opcodes.RETURN));
        m.instructions.add(l);
        Patch.add(bn, m, "(e[slot][1..4] = cmd,target,skill,level)");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dbg$clearPlan", "(I)V", null, null);
        l = new InsnList();
        for (int k = 1; k <= 4; k++) {
            l.add(new VarInsnNode(Opcodes.ALOAD, 0)); l.add(new FieldInsnNode(Opcodes.GETFIELD, "bn", "e", "[[I"));
            l.add(new VarInsnNode(Opcodes.ILOAD, 1)); l.add(new InsnNode(Opcodes.AALOAD));
            l.add(new InsnNode(Opcodes.ICONST_0 + k)); l.add(new InsnNode(Opcodes.ICONST_M1)); l.add(new InsnNode(Opcodes.IASTORE));
        }
        l.add(new InsnNode(Opcodes.RETURN));
        m.instructions.add(l);
        Patch.add(bn, m, "(e[slot][1..4] = -1)");
        Patch.requireField(bn, "a", "I"); Patch.requireField(bn, "i", "I"); Patch.requireField(bn, "f", "I");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dbg$go", "()V", null, null);
        m.instructions.add(Patch.seq(
            new VarInsnNode(Opcodes.ALOAD, 0), new InsnNode(Opcodes.ICONST_2), new FieldInsnNode(Opcodes.PUTFIELD, "bn", "a", "I"),
            new VarInsnNode(Opcodes.ALOAD, 0), new InsnNode(Opcodes.ICONST_0), new FieldInsnNode(Opcodes.PUTFIELD, "bn", "i", "I"),
            new VarInsnNode(Opcodes.ALOAD, 0), new InsnNode(Opcodes.ICONST_0), new FieldInsnNode(Opcodes.PUTFIELD, "bn", "f", "I"),
            new InsnNode(Opcodes.RETURN)));
        Patch.add(bn, m, "(phase=2; i=0; f=0 — như bn.s() sau khi người chơi chọn lệnh)");
        Patch.getter(bn, "dbg$phase", "I", "a", "I");

        // ---- cd / da ----
        Patch.getter(cd, "dbg$bn", "Lbn;", "a", "Lbn;");
        Patch.requireField(cd, "q", "Z");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "dbg$battle", "([Ljava/lang/String;)V", null, null);
        m.instructions.add(Patch.seq(
            new VarInsnNode(Opcodes.ALOAD, 0), new InsnNode(Opcodes.ICONST_1), new FieldInsnNode(Opcodes.PUTFIELD, "cd", "q", "Z"),
            new VarInsnNode(Opcodes.ALOAD, 0), new VarInsnNode(Opcodes.ALOAD, 1), new InsnNode(Opcodes.ICONST_0),
            new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cd", "b", "([Ljava/lang/String;I)V", false), new InsnNode(Opcodes.RETURN)));
        Patch.add(cd, m, "(q=true; cd.b(String[],0) — đúng đường lệnh script \"战斗\" của ci)");
        // join: sao chép trình tự của ci khi script "加入队伍" (nhánh tạo mới)
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "dbg$join", "(I)V", null, null);
        m.instructions.add(Patch.seq(
            new VarInsnNode(Opcodes.ILOAD, 0), new MethodInsnNode(Opcodes.INVOKESTATIC, "o", "a", "(I)Lbl;", false),
            new InsnNode(Opcodes.DUP), new VarInsnNode(Opcodes.ASTORE, 1),
            new MethodInsnNode(Opcodes.INVOKESTATIC, "o", "c", "(Lbl;)V", false),
            new FieldInsnNode(Opcodes.GETSTATIC, "da", "a", "Lda;"), new FieldInsnNode(Opcodes.GETFIELD, "da", "a", "Lcd;"), new FieldInsnNode(Opcodes.GETFIELD, "cd", "a", "Lbj;"),
            new VarInsnNode(Opcodes.ALOAD, 1), new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bj", "a", "(Lcz;)V", false),
            new VarInsnNode(Opcodes.ALOAD, 1), new MethodInsnNode(Opcodes.INVOKESTATIC, "o", "d", "(Lbl;)V", false),
            new VarInsnNode(Opcodes.ALOAD, 1), new FieldInsnNode(Opcodes.GETFIELD, "bl", "a", "[I"), new InsnNode(Opcodes.ICONST_3),
            new VarInsnNode(Opcodes.ALOAD, 1), new FieldInsnNode(Opcodes.GETFIELD, "bl", "a", "[I"), new IntInsnNode(Opcodes.BIPUSH, 10), new InsnNode(Opcodes.IALOAD), new InsnNode(Opcodes.IASTORE),
            new VarInsnNode(Opcodes.ALOAD, 1), new FieldInsnNode(Opcodes.GETFIELD, "bl", "a", "[I"), new InsnNode(Opcodes.ICONST_5),
            new VarInsnNode(Opcodes.ALOAD, 1), new FieldInsnNode(Opcodes.GETFIELD, "bl", "a", "[I"), new IntInsnNode(Opcodes.BIPUSH, 11), new InsnNode(Opcodes.IALOAD), new InsnNode(Opcodes.IASTORE),
            new InsnNode(Opcodes.RETURN)));
        Patch.add(cd, m, "static (o.a(id); o.c; da.a.a.a.a(b); o.d; HP/MP đầy — như ci \"加入队伍\")");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "dbg$game", "()Lcd;", null, null);
        m.instructions.add(Patch.seq(new FieldInsnNode(Opcodes.GETSTATIC, "da", "a", "Lda;"), new FieldInsnNode(Opcodes.GETFIELD, "da", "a", "Lcd;"), new InsnNode(Opcodes.ARETURN)));
        Patch.add(cd, m, "static (da.a.a)");
        m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "dbg$state", "()I", null, null);
        m.instructions.add(Patch.seq(new FieldInsnNode(Opcodes.GETSTATIC, "da", "a", "B"), new InsnNode(Opcodes.IRETURN)));
        Patch.add(cd, m, "static (da.a:B — trạng thái chính: 2 logo, 3 cover, 4 trong game, 7 tạm dừng)");

        // chặn script cốt truyện khởi động trận khác đè lên trận debug (cd.a(short[],short[],short[]) là lối vào mọi trận)
        MethodNode st = Patch.method(cd, "a", "([S[S[S)V");
        LabelNode go = new LabelNode();
        st.instructions.insert(Patch.seq(Patch.hook2("DebugBattle", "blockStart", "()Z"), new JumpInsnNode(Opcodes.IFEQ, go), new InsnNode(Opcodes.RETURN), go));
        Patch.sites++; report.add("  hook cd.a([S[S[S)V entry: if (DebugBattle.blockStart()) return;  (trận do script cốt truyện bị chặn khi đang debug)");

        for (String nm : new String[]{"l", "e"}) {
            MethodNode tr = Patch.method(cd, nm, "()V");
            tr.instructions.insert(Patch.seq(new LdcInsnNode("cd." + nm + "()"), Patch.hook2("DebugBattle", "trap", "(Ljava/lang/String;)V")));
            Patch.sites++; report.add("  hook cd." + nm + "()V entry: DebugBattle.trap(...) (in vết gọi để chẩn đoán kết thúc trận)");
        }

        // đóng băng bộ chạy script cốt truyện (ci.a()) khi đang debug: script không được đổi cảnh/mở trận đè lên trận thử
        ClassNode ci = cls.get("ci");
        MethodNode sc = Patch.method(ci, "a", "()V");
        LabelNode run = new LabelNode();
        sc.instructions.insert(Patch.seq(Patch.hook2("DebugBattle", "blockScript", "()Z"), new JumpInsnNode(Opcodes.IFEQ, run), new InsnNode(Opcodes.RETURN), run));
        Patch.sites++; report.add("  hook ci.a()V entry: if (DebugBattle.blockScript()) return;  (đóng băng script cốt truyện khi debug)");

        // ---- hooks ----
        MethodNode kp = Patch.method(da, "keyPressed", "(I)V");
        LabelNode cont = new LabelNode();
        kp.instructions.insert(Patch.seq(new VarInsnNode(Opcodes.ILOAD, 1), Patch.hook2("DebugBattle", "onKey", "(I)Z"), new JumpInsnNode(Opcodes.IFEQ, cont), new InsnNode(Opcodes.RETURN), cont));
        Patch.sites++; report.add("  hook da.keyPressed(I) entry: DebugBattle.onKey(key) — true = đã xử lý, không chuyển cho game");
        MethodNode pt = Patch.method(da, "paint", "(Ljavax/microedition/lcdui/Graphics;)V");
        int n = 0;
        for (AbstractInsnNode in : pt.instructions.toArray()) if (in.getOpcode() == Opcodes.RETURN) {
            pt.instructions.insertBefore(in, Patch.seq(new VarInsnNode(Opcodes.ALOAD, 1), Patch.hook2("DebugBattle", "paint", "(Ljavax/microedition/lcdui/Graphics;)V")));
            n++; Patch.sites++;
        }
        report.add("  hook da.paint(Graphics): DebugBattle.paint(g) trước " + n + " RETURN");
        MethodNode f = Patch.method(bn, "f", "()V");
        f.instructions.insert(Patch.seq(new VarInsnNode(Opcodes.ALOAD, 0), Patch.hook2("DebugBattle", "tick", "(Lbn;)V")));
        n = 0;
        for (AbstractInsnNode in : f.instructions.toArray()) if (in.getOpcode() == Opcodes.RETURN) {
            f.instructions.insertBefore(in, Patch.seq(new VarInsnNode(Opcodes.ALOAD, 0), Patch.hook2("DebugBattle", "clamp", "(Lbn;)V")));
            n++; Patch.sites++;
        }
        Patch.sites++; report.add("  hook bn.f(): DebugBattle.tick(bn) ở đầu, DebugBattle.clamp(bn) trước " + n + " RETURN");
        MethodNode bp = Patch.method(bn, "a", "(Ljavax/microedition/lcdui/Graphics;)V");
        bp.instructions.insert(Patch.seq(new VarInsnNode(Opcodes.ALOAD, 0), Patch.hook2("DebugBattle", "clamp", "(Lbn;)V")));
        Patch.sites++; report.add("  hook bn.a(Graphics) entry: DebugBattle.clamp(bn) (bất tử)");
        MethodNode hr = Patch.method(bn, "a", "(Le;Le;I)Z");
        n = 0;
        for (AbstractInsnNode in : hr.instructions.toArray()) if (in.getOpcode() == Opcodes.IRETURN) {
            hr.instructions.insertBefore(in, Patch.hook2("DebugBattle", "hitRoll", "(Z)Z"));
            n++; Patch.sites++;
        }
        report.add("  hook bn.a(e,e,I)Z (xúc xắc trúng/hụt): DebugBattle.hitRoll(Z)Z trước " + n + " IRETURN");
        int ph = 0;
        for (AbstractInsnNode in : f.instructions.toArray()) {
            if (in instanceof FieldInsnNode && in.getOpcode() == Opcodes.PUTFIELD) {
                FieldInsnNode fi = (FieldInsnNode) in;
                if (fi.owner.equals("bn") && fi.name.equals("a") && fi.desc.equals("I")) {
                    f.instructions.insertBefore(in, Patch.seq(new InsnNode(Opcodes.DUP2), Patch.hook2("DebugBattle", "onPhase", "(Lbn;I)V")));
                    ph++; Patch.sites++;
                }
            }
        }
        report.add("  hook bn.f(): DebugBattle.onPhase(bn,I) trước " + ph + " putfield bn.a:I (pha trận)");
    }

    static void setter(ClassNode cn, String name, String desc, String field, String fdesc, List<String> report) {
        Patch.requireField(cn, field, fdesc);
        MethodNode m = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, name, desc, null, null);
        m.instructions.add(Patch.seq(new VarInsnNode(Opcodes.ALOAD, 0), new VarInsnNode(fdesc.equals("Z") || fdesc.equals("I") ? Opcodes.ILOAD : Opcodes.ALOAD, 1), new FieldInsnNode(Opcodes.PUTFIELD, cn.name, field, fdesc), new InsnNode(Opcodes.RETURN)));
        Patch.add(cn, m, "-> " + field + ":" + fdesc);
    }
}

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

/**
 * Patch8 <base.jar (renamed, 1.7.0)> <outClassesDir>
 * Session 8 (enemy AI + level tracking). Works on the RENAMED jar (Battle, BattleActor…), on top of the session-7 build.
 * Only adds static calls into EnemyAI and widens 3 private fields of Battle to package access (fields only: no
 * invokespecial semantics change). Every site is asserted; any mismatch stops the build.
 *   Battle.createEnemyActors()V : before the 1st GameUtil.a(...) of the loop body -> EnemyAI.adjustLevel(this, i)
 *                                 before each RETURN                              -> EnemyAI.onEnemiesCreated(this)
 *   Battle.tick()V              : at the exit of the phase-2 enemy planning loop   -> EnemyAI.planRound(this)
 *                                 after each putfield actingSlot                   -> EnemyAI.onTurn(this)
 *   Battle.tick()V / handleKey(I)Z entry                                          -> EnemyAI.petGate(this)  (pets are AI-controlled)
 *   + public final void ai$confirm() { confirmCommand(); }  (bridge to the private method, invokespecial inside Battle)
 * Old CLDC StackMap attributes are dropped from rewritten classes (ProGuard -microedition recomputes them).
 */
public class Patch8 {
    static final String AI = "EnemyAI", B = "Battle";
    static List<String> report = new ArrayList<>();

    public static void main(String[] a) throws Exception {
        String jar = a[0], out = a[1];
        ClassNode bn = null;
        try (ZipFile z = new ZipFile(jar)) {
            ZipEntry ze = z.getEntry("Battle.class");
            if (ze == null) throw new RuntimeException("Battle.class not found: need the renamed jar");
            bn = new ClassNode();
            new ClassReader(z.getInputStream(ze).readAllBytes()).accept(bn, 0);
        }
        widen(bn, "actionPlans", "[[I");
        widen(bn, "actingSlot", "I");
        widen(bn, "enemyCount", "B");
        widen(bn, "commandSlot", "I");
        widen(bn, "targetCursor", "I");
        patchCreateEnemies(bn);
        patchTick(bn);
        patchPets(bn);
        int dropped = 0;
        for (MethodNode m : bn.methods) {
            if (m.attrs == null) continue;
            for (Iterator<Attribute> it = m.attrs.iterator(); it.hasNext();) if (it.next().type.equals("StackMap")) { it.remove(); dropped++; }
        }
        report.add("  raw StackMap attributes dropped: " + dropped + " (ASM reads CLDC StackMap as frames; ProGuard -microedition recomputes them)");
        Files.createDirectories(Paths.get(out));
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        bn.accept(cw);
        Files.write(Paths.get(out, "Battle.class"), cw.toByteArray());
        for (String r : report) System.out.println(r);
        System.out.println("patch8: OK");
    }

    static void widen(ClassNode cn, String name, String desc) {
        for (FieldNode f : cn.fields) {
            if (f.name.equals(name) && f.desc.equals(desc)) {
                if ((f.access & Opcodes.ACC_PRIVATE) == 0) throw new RuntimeException("field already non-private: " + name);
                f.access &= ~Opcodes.ACC_PRIVATE;
                report.add("  field " + cn.name + "." + name + ":" + desc + " private -> package");
                return;
            }
        }
        throw new RuntimeException("field not found " + cn.name + "." + name + ":" + desc);
    }

    static MethodNode method(ClassNode cn, String name, String desc) {
        for (MethodNode m : cn.methods) if (m.name.equals(name) && m.desc.equals(desc)) return m;
        throw new RuntimeException("method not found " + cn.name + "." + name + desc);
    }

    static AbstractInsnNode prevReal(AbstractInsnNode n) {
        do { n = n.getPrevious(); } while (n != null && n.getOpcode() < 0);
        return n;
    }

    static AbstractInsnNode nextReal(AbstractInsnNode n) {
        do { n = n.getNext(); } while (n != null && n.getOpcode() < 0);
        return n;
    }

    static boolean isField(AbstractInsnNode n, int op, String owner, String name) {
        return n instanceof FieldInsnNode && n.getOpcode() == op && ((FieldInsnNode) n).owner.equals(owner) && ((FieldInsnNode) n).name.equals(name);
    }

    static void patchPets(ClassNode cn) {
        method(cn, "confirmCommand", "()V");
        for (String[] e : new String[][]{{"tick", "()V"}, {"handleKey", "(I)Z"}}) {
            MethodNode m = method(cn, e[0], e[1]);
            InsnList h = new InsnList();
            h.add(new VarInsnNode(Opcodes.ALOAD, 0));
            h.add(new MethodInsnNode(Opcodes.INVOKESTATIC, AI, "petGate", "(LBattle;)V", false));
            m.instructions.insert(h);
            report.add("  " + e[0] + e[1] + ": petGate(this) at entry");
        }
        MethodNode br = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "ai$confirm", "()V", null, null);
        br.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        br.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, B, "confirmCommand", "()V", false));
        br.instructions.add(new InsnNode(Opcodes.RETURN));
        cn.methods.add(br);
        report.add("  + Battle.ai$confirm()V -> confirmCommand()V");
    }

    static void patchCreateEnemies(ClassNode cn) {
        MethodNode m = method(cn, "createEnemyActors", "()V");
        List<MethodInsnNode> calls = new ArrayList<>();
        List<AbstractInsnNode> rets = new ArrayList<>();
        for (AbstractInsnNode n : m.instructions) {
            if (n instanceof MethodInsnNode && ((MethodInsnNode) n).owner.equals("GameUtil") && ((MethodInsnNode) n).name.equals("a")
                    && ((MethodInsnNode) n).desc.equals("([[Ljava/lang/String;[[Ljava/lang/String;III)[I")) calls.add((MethodInsnNode) n);
            if (n.getOpcode() == Opcodes.RETURN) rets.add(n);
        }
        if (calls.size() != 5) throw new RuntimeException("createEnemyActors: expected 5 GameUtil.a calls, found " + calls.size());
        // arguments of the first call: aload_0 getfield petTable aload_0 getfield starTable iload idx iconst_3
        //                               aload_0 getfield actors iload i bipush 10 iadd aaload getfield stats iconst_0 iaload
        AbstractInsnNode c = calls.get(0);
        AbstractInsnNode[] w = new AbstractInsnNode[15];
        AbstractInsnNode n = c;
        for (int k = 14; k >= 0; k--) { n = prevReal(n); w[k] = n; }
        if (!(w[0].getOpcode() == Opcodes.ALOAD && ((VarInsnNode) w[0]).var == 0 && isField(w[1], Opcodes.GETFIELD, B, "petTable")
                && isField(w[3], Opcodes.GETFIELD, B, "starTable") && w[5].getOpcode() == Opcodes.ICONST_3
                && isField(w[7], Opcodes.GETFIELD, B, "actors") && w[8].getOpcode() == Opcodes.ILOAD
                && w[9] instanceof IntInsnNode && ((IntInsnNode) w[9]).operand == 10 && w[10].getOpcode() == Opcodes.IADD
                && w[11].getOpcode() == Opcodes.AALOAD && isField(w[12], Opcodes.GETFIELD, "BattleActor", "stats")
                && w[13].getOpcode() == Opcodes.ICONST_0 && w[14].getOpcode() == Opcodes.IALOAD))
            throw new RuntimeException("createEnemyActors: unexpected argument pattern before the first GameUtil.a");
        int idx = ((VarInsnNode) w[8]).var;
        InsnList h = new InsnList();
        h.add(new VarInsnNode(Opcodes.ALOAD, 0));
        h.add(new VarInsnNode(Opcodes.ILOAD, idx));
        h.add(new MethodInsnNode(Opcodes.INVOKESTATIC, AI, "adjustLevel", "(LBattle;I)V", false));
        m.instructions.insertBefore(w[0], h);
        report.add("  createEnemyActors: adjustLevel(this, local " + idx + ") before the 1st GameUtil.a (5 calls)");
        if (rets.isEmpty()) throw new RuntimeException("createEnemyActors: no RETURN");
        for (AbstractInsnNode r : rets) {
            InsnList e = new InsnList();
            e.add(new VarInsnNode(Opcodes.ALOAD, 0));
            e.add(new MethodInsnNode(Opcodes.INVOKESTATIC, AI, "onEnemiesCreated", "(LBattle;)V", false));
            m.instructions.insertBefore(r, e);
        }
        report.add("  createEnemyActors: onEnemiesCreated(this) before " + rets.size() + " RETURN");
    }

    static void patchTick(ClassNode cn) {
        MethodNode m = method(cn, "tick", "()V");
        for (AbstractInsnNode n : m.instructions)
            if (n.getOpcode() == Opcodes.ASTORE && ((VarInsnNode) n).var == 0) throw new RuntimeException("tick: local 0 is reassigned");
        // phase-2 block: aload_0 getfield phase iconst_2 if_icmpne ... bipush 10 istore X ; L: iload X aload Y getfield actionPlans arraylength if_icmpge EXIT
        LabelNode exit = null;
        int found = 0;
        for (AbstractInsnNode n : m.instructions) {
            if (!isField(n, Opcodes.GETFIELD, B, "phase")) continue;
            AbstractInsnNode c2 = nextReal(n), j = nextReal(c2);
            if (c2 == null || c2.getOpcode() != Opcodes.ICONST_2 || j == null || j.getOpcode() != Opcodes.IF_ICMPNE) continue;
            // the loop must start within a few instructions
            AbstractInsnNode p = nextReal(j);
            for (int k = 0; k < 6 && p != null; k++, p = nextReal(p)) {
                if (p instanceof IntInsnNode && p.getOpcode() == Opcodes.BIPUSH && ((IntInsnNode) p).operand == 10) {
                    AbstractInsnNode st = nextReal(p), l1 = nextReal(st), l2 = nextReal(l1), gf = nextReal(l2), al = nextReal(gf), cmp = nextReal(al);
                    if (st.getOpcode() == Opcodes.ISTORE && l1.getOpcode() == Opcodes.ILOAD && ((VarInsnNode) l1).var == ((VarInsnNode) st).var
                            && l2.getOpcode() == Opcodes.ALOAD && isField(gf, Opcodes.GETFIELD, B, "actionPlans")
                            && al.getOpcode() == Opcodes.ARRAYLENGTH && cmp.getOpcode() == Opcodes.IF_ICMPGE) {
                        exit = ((JumpInsnNode) cmp).label;
                        found++;
                    }
                    break;
                }
            }
        }
        if (found != 1) throw new RuntimeException("tick: phase-2 enemy planning loop found " + found + " times (expected 1)");
        // sanity: the instruction before EXIT jumps back (end of the loop body)
        AbstractInsnNode back = prevReal(exit);
        if (back == null || back.getOpcode() != Opcodes.GOTO) throw new RuntimeException("tick: loop exit not preceded by GOTO");
        AbstractInsnNode at = exit;
        while (at.getNext() != null && at.getNext().getOpcode() < 0) at = at.getNext();   // after label/line/frame nodes
        InsnList h = new InsnList();
        h.add(new VarInsnNode(Opcodes.ALOAD, 0));
        h.add(new MethodInsnNode(Opcodes.INVOKESTATIC, AI, "planRound", "(LBattle;)V", false));
        m.instructions.insert(at, h);
        report.add("  tick: planRound(this) at the exit of the phase-2 enemy planning loop");
        int puts = 0;
        List<AbstractInsnNode> sites = new ArrayList<>();
        for (AbstractInsnNode n : m.instructions) if (isField(n, Opcodes.PUTFIELD, B, "actingSlot")) sites.add(n);
        for (AbstractInsnNode n : sites) {
            InsnList t = new InsnList();
            t.add(new VarInsnNode(Opcodes.ALOAD, 0));
            t.add(new MethodInsnNode(Opcodes.INVOKESTATIC, AI, "onTurn", "(LBattle;)V", false));
            m.instructions.insert(n, t);
            puts++;
        }
        if (puts != 3) throw new RuntimeException("tick: expected 3 putfield actingSlot, found " + puts);
        report.add("  tick: onTurn(this) after " + puts + " putfield actingSlot");
    }
}

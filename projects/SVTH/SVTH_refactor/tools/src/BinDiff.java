import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

/** BinDiff <old.jar> <new.jar> : so sánh nhị phân theo entry, và theo field/method (so lệnh bytecode, bỏ StackMap). */
public class BinDiff {
    static Map<String, byte[]> load(String jar) throws Exception {
        Map<String, byte[]> m = new TreeMap<>();
        try (ZipFile z = new ZipFile(jar)) {
            for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements();) {
                ZipEntry ze = e.nextElement();
                if (!ze.isDirectory()) m.put(ze.getName(), z.getInputStream(ze).readAllBytes());
            }
        }
        return m;
    }
    static String sig(MethodNode m) {
        StringBuilder sb = new StringBuilder();
        for (AbstractInsnNode in : m.instructions) {
            if (in instanceof FrameNode || in instanceof LineNumberNode || in instanceof LabelNode) continue;
            sb.append(in.getOpcode());
            if (in instanceof MethodInsnNode) { MethodInsnNode x = (MethodInsnNode) in; sb.append(x.owner).append('.').append(x.name).append(x.desc); }
            else if (in instanceof FieldInsnNode) { FieldInsnNode x = (FieldInsnNode) in; sb.append(x.owner).append('.').append(x.name).append(x.desc); }
            else if (in instanceof VarInsnNode) sb.append(((VarInsnNode) in).var);
            else if (in instanceof IntInsnNode) sb.append(((IntInsnNode) in).operand);
            else if (in instanceof LdcInsnNode) sb.append(((LdcInsnNode) in).cst);
            else if (in instanceof TypeInsnNode) sb.append(((TypeInsnNode) in).desc);
            else if (in instanceof IincInsnNode) sb.append(((IincInsnNode) in).var).append(',').append(((IincInsnNode) in).incr);
            else if (in instanceof JumpInsnNode) sb.append('J');
            sb.append(';');
        }
        return sb.toString();
    }
    static ClassNode cn(byte[] b) { ClassNode c = new ClassNode(); new ClassReader(b).accept(c, ClassReader.SKIP_FRAMES); return c; }
    public static void main(String[] a) throws Exception {
        Map<String, byte[]> o = load(a[0]), n = load(a[1]);
        List<String> added = new ArrayList<>(), removed = new ArrayList<>(), changed = new ArrayList<>(), same = new ArrayList<>();
        for (String k : n.keySet()) { if (!o.containsKey(k)) added.add(k); else if (Arrays.equals(o.get(k), n.get(k))) same.add(k); else changed.add(k); }
        for (String k : o.keySet()) if (!n.containsKey(k)) removed.add(k);
        System.out.println("entries: old=" + o.size() + " new=" + n.size() + " identical=" + same.size() + " changed=" + changed.size() + " added=" + added.size() + " removed=" + removed.size());
        System.out.println("\n## Entry thêm mới");
        for (String k : added) System.out.println("- " + k + " (" + n.get(k).length + " B)");
        System.out.println("\n## Entry bị xoá");
        for (String k : removed) System.out.println("- " + k);
        System.out.println("\n## Entry khác nội dung");
        int nonClass = 0;
        for (String k : changed) {
            if (!k.endsWith(".class")) { System.out.println("- " + k + " (không phải class)"); nonClass++; continue; }
            ClassNode co = cn(o.get(k)), cw = cn(n.get(k));
            System.out.println("- " + k + ":");
            Set<String> fo = new TreeSet<>(), fn = new TreeSet<>();
            for (FieldNode f : co.fields) fo.add(f.name + ":" + f.desc); for (FieldNode f : cw.fields) fn.add(f.name + ":" + f.desc);
            for (String f : fn) if (!fo.contains(f)) System.out.println("    + field " + f);
            for (String f : fo) if (!fn.contains(f)) System.out.println("    - field " + f);
            Map<String, MethodNode> mo = new TreeMap<>(), mn = new TreeMap<>();
            for (MethodNode m : co.methods) mo.put(m.name + m.desc, m); for (MethodNode m : cw.methods) mn.put(m.name + m.desc, m);
            for (String m : mn.keySet()) if (!mo.containsKey(m)) System.out.println("    + method " + m);
            for (String m : mo.keySet()) if (!mn.containsKey(m)) System.out.println("    - method " + m);
            for (String m : mn.keySet()) if (mo.containsKey(m) && !sig(mo.get(m)).equals(sig(mn.get(m)))) {
                int inO = mo.get(m).instructions.size(), inN = mn.get(m).instructions.size();
                System.out.println("    ~ method " + m + " (lệnh " + inO + " → " + inN + ")");
            }
        }
    }
}

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

/** Va bytecode cac class goc (sau khi doi ten) de goi sang class moi. java Patcher in.jar out.jar */
public class Patcher {
    static ClassNode read(byte[] b) { ClassNode c = new ClassNode(); new ClassReader(b).accept(c, 0); return c; }
    static MethodNode method(ClassNode c, String n, String d) {
        for (MethodNode m : c.methods) if (m.name.equals(n) && m.desc.equals(d)) return m;
        throw new RuntimeException("khong thay " + c.name + "." + n + d);
    }
    /** Thay toan bo than ham bang loi goi static owner.name(cung tham so; them this neu ham khong static). */
    static void redirect(ClassNode c, String n, String d, String owner, String name, String desc) {
        MethodNode m = method(c, n, d);
        m.instructions.clear(); m.tryCatchBlocks.clear();
        if (m.localVariables != null) m.localVariables.clear();
        int slot = 0;
        if ((m.access & Opcodes.ACC_STATIC) == 0) m.instructions.add(new VarInsnNode(Opcodes.ALOAD, slot++));
        for (Type t : Type.getArgumentTypes(d)) { m.instructions.add(new VarInsnNode(t.getOpcode(Opcodes.ILOAD), slot)); slot += t.getSize(); }
        m.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, owner, name, desc, false));
        m.instructions.add(new InsnNode(Type.getReturnType(d).getOpcode(Opcodes.IRETURN)));
    }
    static int replaceInt(MethodNode m, int from, int to) {
        int k = 0;
        for (AbstractInsnNode i : m.instructions.toArray())
            if (i instanceof IntInsnNode && ((IntInsnNode) i).operand == from && i.getOpcode() == Opcodes.BIPUSH) { ((IntInsnNode) i).operand = to; k++; }
        return k;
    }
    static void patchMapScript(ClassNode c) {
        redirect(c, "dialogNewPage", "()V", "DialogBox", "newPage", "()V");
        redirect(c, "dialogTypeStep", "()Z", "DialogBox", "typeStep", "()Z");
        MethodNode p = method(c, "paint", "(LGameGraphics;)V");
        AbstractInsnNode[] a = p.instructions.toArray();
        int s = -1, e = -1;
        for (int i = 0; i < a.length - 1; i++) {
            if (s < 0 && a[i] instanceof FieldInsnNode && ((FieldInsnNode) a[i]).name.equals("dlgText") && a[i + 1].getOpcode() == Opcodes.IFNULL) s = i + 2;
            if (s > 0 && i >= s && a[i] instanceof FieldInsnNode && ((FieldInsnNode) a[i]).owner.equals("di") && ((FieldInsnNode) a[i]).name.equals("d") && ((FieldInsnNode) a[i]).desc.equals("Z")) { e = i; break; }
        }
        if (s < 0 || e < 0) throw new RuntimeException("khong tim thay doan ve hop thoai");
        for (int i = s; i < e; i++) if (!(a[i] instanceof LabelNode)) p.instructions.remove(a[i]);
        InsnList ins = new InsnList();
        ins.add(new VarInsnNode(Opcodes.ALOAD, 0));
        ins.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "DialogBox", "paint", "(LGameGraphics;)V", false));
        p.instructions.insertBefore(a[e], ins);
        System.out.println("paint: bo " + (e - s) + " lenh, 37->64: " + replaceInt(p, 37, 64));
        System.out.println("clipAboveDialog 37->64: " + replaceInt(method(c, "clipAboveDialog", "(LGameGraphics;)V"), 37, 64));
    }
    /** Do dai ten/mo ta trong ban ghi itm doc la byte khong dau (chu Viet UTF-8 de vuot 127 byte). */
    static void patchItem(ClassNode c) {
        MethodNode m = method(c, "parseRecord", "([BI)I");
        int k = 0;
        for (AbstractInsnNode i : m.instructions.toArray())
            if (i.getOpcode() == Opcodes.BALOAD) {
                InsnList ins = new InsnList();
                ins.add(new IntInsnNode(Opcodes.SIPUSH, 255));
                ins.add(new InsnNode(Opcodes.IAND));
                m.instructions.insert(i, ins); k++;
            }
        if (k != 2) throw new RuntimeException("Item.parseRecord: " + k + " baload");
        System.out.println("Item.parseRecord: do dai khong dau x" + k);
    }
    /** Chuoi hien thi trong class: thay hang LDC theo mapping/class_strings.tsv (class<TAB>goc<TAB>dich, \\t \\n \\\\ thoat)
     *  va doi String.getBytes() -> TextRenderer.utf8() de tieng Viet thanh UTF-8 tren moi may. */
    static Map<String, Map<String,String>> classStrings = new HashMap<>();
    static String unesc(String s) { return s.replace("\\t", "\t").replace("\\n", "\n"); }
    static void loadClassStrings(String f) throws IOException {
        for (String l : java.nio.file.Files.readAllLines(java.nio.file.Paths.get(f), java.nio.charset.StandardCharsets.UTF_8)) {
            if (l.isEmpty() || l.startsWith("#") || l.startsWith("class\t")) continue;
            String[] c = l.split("\t", -1);
            if (c.length < 3 || c[2].isEmpty()) continue;
            // "lop#truong": chi thay hang LDC duoc gan (qua getBytes) vao truong static do
            String cls = c[0], key = unesc(c[1]);
            if (cls.indexOf('#') > 0) { key = cls.substring(cls.indexOf('#') + 1) + "#" + key; cls = cls.substring(0, cls.indexOf('#')); }
            classStrings.computeIfAbsent(cls, k -> new HashMap<>()).put(key, unesc(c[2]));
        }
    }
    static void patchStrings(ClassNode c) {
        Map<String,String> tr = classStrings.getOrDefault(c.name, Collections.emptyMap());
        int nl = 0, ng = 0; Set<String> used = new HashSet<>();
        for (MethodNode m : c.methods)
            for (AbstractInsnNode i : m.instructions.toArray()) {
                if (i instanceof LdcInsnNode && ((LdcInsnNode) i).cst instanceof String) {
                    String v = (String) ((LdcInsnNode) i).cst, key = v;
                    AbstractInsnNode put = i.getNext() == null ? null : i.getNext().getNext();
                    if (put instanceof FieldInsnNode && put.getOpcode() == Opcodes.PUTSTATIC && tr.containsKey(((FieldInsnNode) put).name + "#" + v)) key = ((FieldInsnNode) put).name + "#" + v;
                    if (tr.containsKey(key)) { used.add(key); ((LdcInsnNode) i).cst = tr.get(key); nl++; }
                } else if (i instanceof MethodInsnNode && ((MethodInsnNode) i).owner.equals("java/lang/String") && ((MethodInsnNode) i).name.equals("getBytes") && ((MethodInsnNode) i).desc.equals("()[B")) {
                    m.instructions.set(i, new MethodInsnNode(Opcodes.INVOKESTATIC, "TextRenderer", "utf8", "(Ljava/lang/String;)[B", false)); ng++;
                }
            }
        for (String k : tr.keySet()) if (!used.contains(k)) throw new RuntimeException(c.name + ": khong thay chuoi \"" + k + "\"");
        System.out.println(c.name + ": " + nl + " chuoi dich, " + ng + " getBytes->utf8");
    }
    public static void main(String[] args) throws Exception {
        Map<String,byte[]> files = new LinkedHashMap<>();
        ZipInputStream zi = new ZipInputStream(new FileInputStream(args[0]));
        for (ZipEntry e; (e = zi.getNextEntry()) != null; ) if (!e.isDirectory()) files.put(e.getName(), zi.readAllBytes());
        Map<String,java.util.function.Consumer<ClassNode>> todo = new HashMap<>();
        todo.put("MapScript.class", Patcher::patchMapScript);
        todo.put("Item.class", Patcher::patchItem);
        todo.put("ScriptRunner.class", c -> redirect(c, "loadScript", "([BI)I", "ScriptLoader", "load", "(LScriptRunner;[BI)I"));
        loadClassStrings(args[2]);
        for (String k : classStrings.keySet()) {
            String f = k + ".class";
            java.util.function.Consumer<ClassNode> prev = todo.get(f);
            todo.put(f, prev == null ? Patcher::patchStrings : prev.andThen(Patcher::patchStrings));
        }
        for (String k : todo.keySet()) {
            ClassNode c = read(files.get(k));
            todo.get(k).accept(c);
            ClassWriter w = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            c.accept(w);
            files.put(k, w.toByteArray());
        }
        ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(args[1]));
        for (Map.Entry<String,byte[]> e : files.entrySet()) { zo.putNextEntry(new ZipEntry(e.getKey())); zo.write(e.getValue()); zo.closeEntry(); }
        zo.close();
    }
}

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import java.util.*;
import java.util.zip.*;

public class Verify {
    static Map<String, ClassNode> app = new HashMap<>(), lib = new HashMap<>();
    static int errors = 0, warnings = 0;
    public static void main(String[] a) throws Exception {
        load(a[0], app, 0);
        for (String l : a[1].split(":")) load(l, lib, ClassReader.SKIP_CODE);
        boolean requireStackMap = a.length > 2 && a[2].equals("--require-stackmap");
        for (ClassNode cn : app.values()) {
            if ((cn.version & 0xFFFF) > 49) err(cn.name, "class version > 49");
            check(cn.superName, cn.name);
            for (String i : cn.interfaces) check(i, cn.name);
            for (MethodNode m : cn.methods) {
                if (m.instructions == null || m.instructions.size() == 0) continue;
                for (AbstractInsnNode in : m.instructions) {
                    if (in instanceof MethodInsnNode) {
                        MethodInsnNode mi = (MethodInsnNode) in;
                        if (!mi.owner.startsWith("[") && !hasMethod(mi.owner, mi.name, mi.desc)) err(cn.name + "." + m.name, "unresolved method " + mi.owner + "." + mi.name + mi.desc);
                    } else if (in instanceof FieldInsnNode) {
                        FieldInsnNode fi = (FieldInsnNode) in;
                        if (!hasField(fi.owner, fi.name, fi.desc)) err(cn.name + "." + m.name, "unresolved field " + fi.owner + "." + fi.name + ":" + fi.desc);
                    } else if (in instanceof TypeInsnNode) {
                        String d = ((TypeInsnNode) in).desc; boolean arr = d.startsWith("[");
                        while (d.startsWith("[")) d = d.substring(1);
                        // session 5: "L…;" is a descriptor only inside an array type; a plain internal name may itself start with 'L' (renamed classes)
                        if (arr && d.startsWith("L")) d = d.substring(1, d.length() - 1);
                        if (d.length() > 1 && node(d) == null) err(cn.name + "." + m.name, "unresolved class " + d);
                    } else if (in instanceof LdcInsnNode && ((LdcInsnNode) in).cst instanceof Type) {
                        err(cn.name + "." + m.name, "class literal ldc");
                    } else if (in instanceof InvokeDynamicInsnNode) err(cn.name + "." + m.name, "invokedynamic");
                }
                if (requireStackMap) {
                    boolean branches = !m.tryCatchBlocks.isEmpty(), has = false;
                    for (AbstractInsnNode in : m.instructions) {
                        if (in instanceof JumpInsnNode || in instanceof TableSwitchInsnNode || in instanceof LookupSwitchInsnNode) branches = true;
                        if (in instanceof FrameNode) has = true;
                        if (in.getOpcode() == Opcodes.JSR || in.getOpcode() == Opcodes.RET) err(cn.name + "." + m.name + m.desc, "jsr/ret left after preverify");
                    }
                    if (m.attrs != null) for (Attribute at : m.attrs) if (at.type.equals("StackMap")) has = true;
                    if (branches && !has) err(cn.name + "." + m.name + m.desc, "missing CLDC StackMap");
                }
                try {
                    MethodNode am = m;
                    boolean hasJsr = false;
                    for (AbstractInsnNode in : m.instructions) if (in.getOpcode() == Opcodes.JSR) hasJsr = true;
                    if (hasJsr) {
                        am = new MethodNode(Opcodes.ASM9, m.access, m.name, m.desc, m.signature, m.exceptions.toArray(new String[0]));
                        m.accept(new org.objectweb.asm.commons.JSRInlinerAdapter(am, m.access, m.name, m.desc, m.signature, m.exceptions.toArray(new String[0])));
                    }
                    new Analyzer<>(new Sv()).analyze(cn.name, am);
                } catch (AnalyzerException e) { err(cn.name + "." + m.name + m.desc, "dataflow: " + e.getMessage()); }
            }
        }
        System.out.println("verify: classes=" + app.size() + " errors=" + errors + " warnings=" + warnings);
        System.exit(errors > 0 ? 1 : 0);
    }
    static class Sv extends SimpleVerifier {
        Sv() { super(Opcodes.ASM9, null, null, null, false); }
        @Override protected boolean isInterface(Type t) { ClassNode c = node(t.getInternalName()); return c != null && (c.access & Opcodes.ACC_INTERFACE) != 0; }
        @Override protected Type getSuperClass(Type t) { ClassNode c = node(t.getInternalName()); return c == null ? Type.getObjectType("java/lang/Object") : (c.superName == null ? null : Type.getObjectType(c.superName)); }
        @Override protected boolean isAssignableFrom(Type t, Type u) {
            if (t == null || u == null) return false;
            if (t.equals(u)) return true;
            if (t.getSort() == Type.OBJECT && t.getInternalName().equals("java/lang/Object")) return true;
            if (t.getSort() == Type.ARRAY) {
                if (u.getSort() != Type.ARRAY) return false;
                Type te = Type.getType(t.getDescriptor().substring(1)), ue = Type.getType(u.getDescriptor().substring(1));
                if (te.getSort() <= Type.DOUBLE || ue.getSort() <= Type.DOUBLE) return te.equals(ue);
                return isAssignableFrom(te, ue);
            }
            if (u.getSort() == Type.ARRAY) return false;
            if (isInterface(t)) return true;
            for (String s = u.getInternalName(); s != null; ) { if (s.equals(t.getInternalName())) return true; ClassNode c = node(s); s = c == null ? null : c.superName; }
            return false;
        }
        @Override protected boolean isSubTypeOf(BasicValue value, BasicValue expected) {
            Type et = expected.getType(), t = value.getType();
            switch (et.getSort()) {
                case Type.INT: case Type.FLOAT: case Type.LONG: case Type.DOUBLE: return t.equals(et);
                case Type.ARRAY: case Type.OBJECT:
                    if (t.getDescriptor().equals("Lnull;")) return true;
                    if (t.getSort() == Type.OBJECT || t.getSort() == Type.ARRAY) return isAssignableFrom(et, t) || (et.getSort() == Type.OBJECT && isInterface(et));
                    return false;
                default: throw new AssertionError(et);
            }
        }
        @Override protected Class<?> getClass(Type t) { throw new RuntimeException("no reflection"); }
    }
    static ClassNode node(String n) { ClassNode c = app.get(n); return c != null ? c : lib.get(n); }
    static void check(String cls, String ctx) { if (cls != null && node(cls) == null) err(ctx, "unresolved class " + cls); }
    static boolean hasField(String owner, String name, String desc) {
        for (ClassNode c = node(owner); c != null; c = c.superName == null ? null : node(c.superName)) {
            for (FieldNode f : c.fields) if (f.name.equals(name) && f.desc.equals(desc)) return true;
            for (String i : c.interfaces) if (hasField(i, name, desc)) return true;
        }
        return false;
    }
    static boolean hasMethod(String owner, String name, String desc) {
        if (node(owner) == null) return false;
        Deque<String> q = new ArrayDeque<>(); q.add(owner); Set<String> seen = new HashSet<>();
        while (!q.isEmpty()) {
            String s = q.poll(); if (!seen.add(s)) continue; ClassNode n = node(s); if (n == null) continue;
            for (MethodNode m : n.methods) if (m.name.equals(name) && m.desc.equals(desc)) return true;
            if (n.superName != null) q.add(n.superName); q.addAll(n.interfaces);
        }
        return name.equals("clone") || owner.equals("java/lang/Object");
    }
    static class StackMapAttr extends Attribute {
        StackMapAttr() { super("StackMap"); }
        @Override public boolean isCodeAttribute() { return true; }
        @Override protected Attribute read(ClassReader cr, int off, int len, char[] buf, int codeOff, Label[] labels) { return new StackMapAttr(); }
    }
    static void load(String jar, Map<String, ClassNode> into, int flags) throws Exception {
        try (ZipFile z = new ZipFile(jar)) {
            for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements();) {
                ZipEntry ze = e.nextElement(); if (!ze.getName().endsWith(".class")) continue;
                ClassNode cn = new ClassNode();
                new ClassReader(z.getInputStream(ze).readAllBytes()).accept(cn, new Attribute[]{new StackMapAttr()}, flags);
                into.putIfAbsent(cn.name, cn);
            }
        }
    }
    static void err(String ctx, String msg) { errors++; if (errors < 200) System.out.println("ERROR " + ctx + ": " + msg); }
}

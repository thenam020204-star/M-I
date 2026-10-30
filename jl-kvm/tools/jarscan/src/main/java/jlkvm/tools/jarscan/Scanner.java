package jlkvm.tools.jarscan;

import jlkvm.tools.apisigs.ApiDb;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** Static compatibility scanner (PLAN T04). */
public final class Scanner {
    private final ScanProfile profile;
    private final Map<String, ApiDb.ClassSig> api = new HashMap<>();
    private final Map<String, ClassInfo> app = new LinkedHashMap<>();
    private final List<Finding> findings = new ArrayList<>();
    private final Map<String, Set<String>> callGraph = new LinkedHashMap<>();
    private final Set<String> dedupe = new HashSet<>();
    public final Map<String, Object> stats = new TreeMap<>();

    /** Facts about one application class. */
    static final class ClassInfo {
        String name, superName;
        String[] interfaces;
        int access, version;
        final Set<String> fields = new HashSet<>();    // name:desc
        final Set<String> methods = new HashSet<>();   // name+desc
        int[] stackMapInfo;
    }

    public Scanner(ScanProfile profile, List<ApiDb> dbs) {
        this.profile = profile;
        for (ApiDb d : dbs) for (ApiDb.ClassSig c : d.classes) api.put(c.name, c);
    }

    public List<Finding> findings() {
        return findings;
    }

    public boolean hasErrors() {
        for (Finding f : findings) if (f.level == Finding.Level.ERROR) return true;
        return false;
    }

    private void add(Finding.Level l, String code, String where, String msg) {
        if (dedupe.add(l + code + where + msg)) findings.add(new Finding(l, code, where, msg));
    }

    public void scan(Path jar) throws IOException {
        scan(jar.toFile());
    }

    /** Same as {@link #scan(Path)}; java.io only, so the Android installer (minSdk below 26, no java.nio.file) can run it. */
    public void scan(java.io.File jar) throws IOException {
        List<byte[]> blobs = new ArrayList<>();
        List<String> names = new ArrayList<>();
        try (ZipFile z = new ZipFile(jar)) {
            java.util.Enumeration<? extends ZipEntry> en = z.entries();
            Set<String> seen = new HashSet<>();
            while (en.hasMoreElements()) {
                ZipEntry e = en.nextElement();
                if (!e.getName().endsWith(".class")) continue;
                if (!seen.add(e.getName())) add(Finding.Level.WARN, "DUP_ENTRY", e.getName(), "duplicate jar entry");
                try (InputStream in = z.getInputStream(e)) {
                    ByteArrayOutputStream bo = new ByteArrayOutputStream();
                    byte[] buf = new byte[8192];
                    int n;
                    while ((n = in.read(buf)) > 0) bo.write(buf, 0, n);
                    blobs.add(bo.toByteArray());
                    names.add(e.getName());
                } catch (IOException ex) {
                    add(Finding.Level.ERROR, "BAD_ENTRY", e.getName(), "unreadable entry: " + ex.getMessage());
                }
            }
        }
        // pass 1: declarations
        List<ClassReader> readers = new ArrayList<>();
        for (int i = 0; i < blobs.size(); i++) {
            try {
                ClassReader cr = new ClassReader(blobs.get(i));
                readers.add(cr);
                ClassInfo ci = new ClassInfo();
                ci.name = cr.getClassName();
                ci.superName = cr.getSuperName();
                ci.interfaces = cr.getInterfaces();
                ci.access = cr.getAccess();
                ci.version = ((blobs.get(i)[6] & 0xff) << 8) | (blobs.get(i)[7] & 0xff);
                cr.accept(new ClassVisitor(Opcodes.ASM9) {
                    @Override
                    public org.objectweb.asm.FieldVisitor visitField(int a, String n, String d, String s, Object v) {
                        ci.fields.add(n + ":" + d);
                        return null;
                    }

                    @Override
                    public MethodVisitor visitMethod(int a, String n, String d, String s, String[] e) {
                        ci.methods.add(n + d);
                        return null;
                    }
                }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                app.put(ci.name, ci);
                int[] stackMap = StackMapProbe.scan(blobs.get(i));
                ci.stackMapInfo = stackMap;
            } catch (Exception ex) {
                add(Finding.Level.ERROR, "BAD_CLASS", names.get(i), "cannot parse class: " + ex);
            }
        }
        stats.put("classes", app.size());
        // pass 2: code
        for (ClassReader cr : readers) scanClass(cr);
        detectRecursion();
    }

    // ---- per class ----------------------------------------------------------------------------------------------
    private void scanClass(ClassReader cr) {
        final ClassInfo ci = app.get(cr.getClassName());
        if (ci == null) return;
        final String cn = ci.name;
        if (ci.version > profile.maxClassVersion) {
            add(Finding.Level.ERROR, "CLASS_VERSION", cn, "class file version " + ci.version + " > profile max " + profile.maxClassVersion + " (NOT J2ME-SAFE)");
        }
        if (ci.superName != null) needClass(cn, ci.superName, "superclass");
        for (String i : ci.interfaces) needClass(cn, i, "interface");
        if (profile.requireStackMap && ci.stackMapInfo != null) {
            int needing = ci.stackMapInfo[0], having = ci.stackMapInfo[1];
            if (needing > having) {
                add(Finding.Level.ERROR, "NO_STACKMAP", cn, (needing - having) + " method(s) with branches/handlers lack a StackMap attribute (not preverified; NOT J2ME-SAFE)");
            }
            if (ci.version >= 50 && ci.stackMapInfo[2] > 0) {
                add(Finding.Level.WARN, "STACKMAPTABLE", cn, "has StackMapTable (Java SE format), CLDC needs StackMap");
            }
        }
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
                final String self = cn + "." + name + desc;
                final Set<String> calls = new java.util.LinkedHashSet<>();
                callGraph.put(self, calls);
                checkDesc(self, desc);
                return new MethodVisitor(Opcodes.ASM9) {
                    int lastInt = Integer.MIN_VALUE;
                    int narrowing = 0;
                    boolean fp = false;

                    @Override
                    public void visitInsn(int op) {
                        switch (op) {
                            case Opcodes.FADD: case Opcodes.DADD: case Opcodes.FSUB: case Opcodes.DSUB: case Opcodes.FMUL:
                            case Opcodes.DMUL: case Opcodes.FDIV: case Opcodes.DDIV: case Opcodes.FREM: case Opcodes.DREM:
                            case Opcodes.FNEG: case Opcodes.DNEG: case Opcodes.I2F: case Opcodes.I2D: case Opcodes.F2I:
                            case Opcodes.D2I: case Opcodes.L2F: case Opcodes.L2D: case Opcodes.F2L: case Opcodes.D2L:
                            case Opcodes.F2D: case Opcodes.D2F: case Opcodes.FCMPL: case Opcodes.FCMPG: case Opcodes.DCMPL:
                            case Opcodes.DCMPG: case Opcodes.FCONST_0: case Opcodes.FCONST_1: case Opcodes.FCONST_2:
                            case Opcodes.DCONST_0: case Opcodes.DCONST_1: case Opcodes.FALOAD: case Opcodes.DALOAD:
                            case Opcodes.FASTORE: case Opcodes.DASTORE: case Opcodes.FRETURN: case Opcodes.DRETURN:
                                fp = true;
                                break;
                            default:
                        }
                        switch (op) {
                            case Opcodes.L2I: case Opcodes.D2I: case Opcodes.F2I: case Opcodes.I2B: case Opcodes.I2S:
                            case Opcodes.I2C: case Opcodes.D2F: case Opcodes.F2L: case Opcodes.D2L:
                                narrowing++;
                                break;
                            default:
                        }
                        lastInt = Integer.MIN_VALUE;
                    }

                    @Override
                    public void visitIntInsn(int op, int operand) {
                        if (op == Opcodes.NEWARRAY) {
                            if (lastInt >= profile.largeArrayThreshold) {
                                add(Finding.Level.WARN, "BIG_ARRAY", self, "allocates array of constant size " + lastInt + " (>= " + profile.largeArrayThreshold + ")");
                            }
                            if (operand == Opcodes.T_FLOAT || operand == Opcodes.T_DOUBLE) fp = true;
                        }
                        lastInt = (op == Opcodes.BIPUSH || op == Opcodes.SIPUSH) ? operand : Integer.MIN_VALUE;
                    }

                    @Override
                    public void visitLdcInsn(Object v) {
                        if (v instanceof Float || v instanceof Double) fp = true;
                        if (v instanceof Type && ((Type) v).getSort() == Type.OBJECT) needClass(self, ((Type) v).getInternalName(), "ldc class");
                        lastInt = v instanceof Integer ? (Integer) v : Integer.MIN_VALUE;
                    }

                    @Override
                    public void visitJumpInsn(int op, Label l) {
                        if (op == Opcodes.JSR) add(Finding.Level.ERROR, "JSR_RET", self, "uses jsr (not preverified; CLDC forbids jsr/ret) (NOT J2ME-SAFE)");
                        lastInt = Integer.MIN_VALUE;
                    }

                    @Override
                    public void visitVarInsn(int op, int var) {
                        if (op == Opcodes.RET) add(Finding.Level.ERROR, "JSR_RET", self, "uses ret (NOT J2ME-SAFE)");
                        if (op == Opcodes.FLOAD || op == Opcodes.DLOAD || op == Opcodes.FSTORE || op == Opcodes.DSTORE) fp = true;
                        lastInt = Integer.MIN_VALUE;
                    }

                    @Override
                    public void visitTypeInsn(int op, String type) {
                        String t = type.startsWith("[") ? arrayElem(type) : type;
                        if (t != null) needClass(self, t, op == Opcodes.NEW ? "new" : op == Opcodes.CHECKCAST ? "checkcast" : op == Opcodes.INSTANCEOF ? "instanceof" : "anewarray");
                        if (op == Opcodes.ANEWARRAY && lastInt >= profile.largeArrayThreshold) {
                            add(Finding.Level.WARN, "BIG_ARRAY", self, "allocates reference array of constant size " + lastInt);
                        }
                        lastInt = Integer.MIN_VALUE;
                    }

                    @Override
                    public void visitMultiANewArrayInsn(String desc, int dims) {
                        String t = arrayElem(desc);
                        if (t != null) needClass(self, t, "multianewarray");
                        lastInt = Integer.MIN_VALUE;
                    }

                    @Override
                    public void visitFieldInsn(int op, String owner, String name, String desc) {
                        if (desc.equals("F") || desc.equals("D")) fp = true;
                        if (!owner.startsWith("[")) checkField(self, owner, name, desc);
                        lastInt = Integer.MIN_VALUE;
                    }

                    @Override
                    public void visitMethodInsn(int op, String owner, String name, String desc, boolean itf) {
                        if (desc.indexOf('F') >= 0 || desc.indexOf('D') >= 0) {
                            Type[] ts = Type.getArgumentTypes(desc);
                            for (Type t : ts) if (t.getSort() == Type.FLOAT || t.getSort() == Type.DOUBLE) fp = true;
                            if (Type.getReturnType(desc).getSort() == Type.FLOAT || Type.getReturnType(desc).getSort() == Type.DOUBLE) fp = true;
                        }
                        if (!owner.startsWith("[")) {
                            checkMethod(self, owner, name, desc, op == Opcodes.INVOKEINTERFACE);
                            if (app.containsKey(owner)) calls.add(owner + "." + name + desc);
                            if (owner.equals("java/lang/System") && name.equals("gc")) add(Finding.Level.WARN, "SYSTEM_GC", self, "calls System.gc()");
                            if (owner.equals("java/lang/Thread") || owner.equals("java/util/Timer") || owner.equals("java/util/TimerTask")) {
                                add(Finding.Level.INFO, "THREADING", self, "uses " + owner + "." + name);
                            }
                        }
                        lastInt = Integer.MIN_VALUE;
                    }

                    @Override
                    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... args) {
                        add(Finding.Level.ERROR, "INVOKEDYNAMIC", self, "invokedynamic does not exist in CLDC (NOT J2ME-SAFE)");
                    }

                    @Override
                    public void visitTryCatchBlock(Label s, Label e, Label h, String type) {
                        if (type != null) needClass(self, type, "catch");
                    }

                    @Override
                    public void visitEnd() {
                        if (fp) {
                            add(profile.floatingPoint ? Finding.Level.INFO : Finding.Level.ERROR, "FLOAT_DOUBLE", self,
                                    profile.floatingPoint ? "uses float/double" : "uses float/double but profile has no floating point (CLDC 1.0) (NOT J2ME-SAFE)");
                        }
                        if (narrowing > 0) add(Finding.Level.INFO, "NARROWING", self, narrowing + " narrowing conversion(s) (l2i/d2i/f2i/i2b/i2s/i2c/d2f)");
                    }
                };
            }
        }, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
    }

    private void checkDesc(String where, String desc) {
        for (Type t : Type.getArgumentTypes(desc)) checkType(where, t);
        checkType(where, Type.getReturnType(desc));
    }

    private void checkType(String where, Type t) {
        if (t.getSort() == Type.ARRAY) t = t.getElementType();
        if (t.getSort() == Type.OBJECT) needClass(where, t.getInternalName(), "signature");
    }

    private static String arrayElem(String d) {
        int i = 0;
        while (d.charAt(i) == '[') i++;
        return d.charAt(i) == 'L' ? d.substring(i + 1, d.length() - 1) : null;
    }

    // ---- resolution -----------------------------------------------------------------------------------------------
    private boolean classExists(String n) {
        return app.containsKey(n) || api.containsKey(n);
    }

    private void needClass(String where, String cls, String what) {
        if (!classExists(cls)) {
            add(Finding.Level.ERROR, "MISSING_CLASS", where, "class " + cls.replace('/', '.') + " (" + what + ") not in profile " + profile.name + " (NOT J2ME-SAFE)");
        }
    }

    private void checkField(String where, String owner, String name, String desc) {
        if (!classExists(owner)) {
            needClass(where, owner, "field owner");
            return;
        }
        if (!findField(owner, name + ":" + desc, new HashSet<>())) {
            add(Finding.Level.ERROR, "MISSING_FIELD", where, "field " + owner.replace('/', '.') + "." + name + " " + desc + " not in profile " + profile.name + " (NOT J2ME-SAFE)");
        }
    }

    private boolean findField(String cls, String key, Set<String> seen) {
        if (cls == null || !seen.add(cls)) return false;
        ClassInfo a = app.get(cls);
        if (a != null) {
            if (a.fields.contains(key)) return true;
            if (findField(a.superName, key, seen)) return true;
            for (String i : a.interfaces) if (findField(i, key, seen)) return true;
            return false;
        }
        ApiDb.ClassSig c = api.get(cls);
        if (c == null) return true;   // unknown class already reported
        for (ApiDb.FieldSig f : c.fields) if ((f.name + ":" + f.desc).equals(key)) return true;
        if (findField(c.superName, key, seen)) return true;
        for (String i : c.interfaces) if (findField(i, key, seen)) return true;
        return false;
    }

    private void checkMethod(String where, String owner, String name, String desc, boolean itf) {
        if (!classExists(owner)) {
            needClass(where, owner, "method owner");
            return;
        }
        String key = name + desc;
        boolean ok = findMethod(owner, key, new HashSet<>()) || (itf && findMethod("java/lang/Object", key, new HashSet<>()));
        if (!ok) {
            add(Finding.Level.ERROR, "MISSING_METHOD", where, "method " + owner.replace('/', '.') + "." + name + desc + " not in profile " + profile.name + " (NOT J2ME-SAFE)");
        }
    }

    private boolean findMethod(String cls, String key, Set<String> seen) {
        if (cls == null || !seen.add(cls)) return false;
        ClassInfo a = app.get(cls);
        if (a != null) {
            if (a.methods.contains(key)) return true;
            if (findMethod(a.superName, key, seen)) return true;
            for (String i : a.interfaces) if (findMethod(i, key, seen)) return true;
            return false;
        }
        ApiDb.ClassSig c = api.get(cls);
        if (c == null) return true;
        for (ApiDb.MethodSig m : c.methods) if ((m.name + m.desc).equals(key)) return true;
        if (findMethod(c.superName, key, seen)) return true;
        for (String i : c.interfaces) if (findMethod(i, key, seen)) return true;
        // an abstract app class / interface may inherit an API method via java/lang/Object
        return false;
    }

    // ---- recursion (Tarjan SCC over app call graph) -------------------------------------------------------------
    private int idx;
    private final Map<String, Integer> index = new HashMap<>(), low = new HashMap<>();
    private final Deque<String> stack = new ArrayDeque<>();
    private final Set<String> onStack = new HashSet<>();

    private void detectRecursion() {
        int rec = 0;
        List<String> nodes = new ArrayList<>(callGraph.keySet());
        for (String n : nodes) if (!index.containsKey(n)) rec += tarjan(n);
        stats.put("recursiveMethods", rec);
    }

    private int tarjan(String root) {
        // iterative Tarjan to survive deep call graphs
        int found = 0;
        Deque<Object[]> work = new ArrayDeque<>();
        work.push(new Object[]{root, null});
        while (!work.isEmpty()) {
            Object[] fr = work.peek();
            String v = (String) fr[0];
            @SuppressWarnings("unchecked")
            java.util.Iterator<String> it = (java.util.Iterator<String>) fr[1];
            if (it == null) {
                index.put(v, idx);
                low.put(v, idx);
                idx++;
                stack.push(v);
                onStack.add(v);
                Set<String> succ = callGraph.get(v);
                it = (succ == null ? new HashSet<String>() : succ).iterator();
                fr[1] = it;
            }
            boolean descended = false;
            while (it.hasNext()) {
                String w = it.next();
                if (!callGraph.containsKey(w)) continue;
                if (!index.containsKey(w)) {
                    work.push(new Object[]{w, null});
                    descended = true;
                    break;
                } else if (onStack.contains(w)) {
                    low.put(v, Math.min(low.get(v), index.get(w)));
                }
            }
            if (descended) continue;
            work.pop();
            if (!work.isEmpty()) {
                String parent = (String) work.peek()[0];
                low.put(parent, Math.min(low.get(parent), low.get(v)));
            }
            if (low.get(v).equals(index.get(v))) {
                List<String> scc = new ArrayList<>();
                String w;
                do {
                    w = stack.pop();
                    onStack.remove(w);
                    scc.add(w);
                } while (!w.equals(v));
                boolean self = scc.size() == 1 && callGraph.get(v).contains(v);
                if (scc.size() > 1 || self) {
                    found += scc.size();
                    for (String m : scc) {
                        add(Finding.Level.WARN, "RECURSION", m, scc.size() > 1 ? "in recursion cycle of " + scc.size() + " methods" : "directly recursive (stack depth is device-limited)");
                    }
                }
            }
        }
        return found;
    }

    // ---- StackMap probe: counts methods needing / having a CLDC StackMap attribute ---------------------------------
    static final class StackMapProbe {
        /** returns {methodsNeedingStackMap, methodsWithStackMap, methodsWithStackMapTable}. */
        static int[] scan(byte[] b) throws IOException {
            DataInputStream in = new DataInputStream(new java.io.ByteArrayInputStream(b));
            in.readInt();
            in.readUnsignedShort();
            in.readUnsignedShort();
            int n = in.readUnsignedShort();
            String[] utf = new String[n];
            for (int i = 1; i < n; i++) {
                int t = in.readUnsignedByte();
                switch (t) {
                    case 1: utf[i] = in.readUTF(); break;
                    case 3: case 4: case 9: case 10: case 11: case 12: case 18: in.readInt(); break;
                    case 5: case 6: in.readLong(); i++; break;
                    case 7: case 8: case 16: in.readUnsignedShort(); break;
                    case 15: in.readUnsignedByte(); in.readUnsignedShort(); break;
                    default: throw new IOException("bad cp tag " + t);
                }
            }
            in.skipBytes(6);
            int ni = in.readUnsignedShort();
            in.skipBytes(ni * 2);
            int nf = in.readUnsignedShort();
            for (int i = 0; i < nf; i++) {
                in.skipBytes(6);
                int na = in.readUnsignedShort();
                for (int j = 0; j < na; j++) {
                    in.readUnsignedShort();
                    in.skipBytes(in.readInt());
                }
            }
            int nm = in.readUnsignedShort();
            int need = 0, have = 0, table = 0;
            for (int i = 0; i < nm; i++) {
                in.skipBytes(6);
                int na = in.readUnsignedShort();
                for (int j = 0; j < na; j++) {
                    String an = utf[in.readUnsignedShort()];
                    int len = in.readInt();
                    if (!"Code".equals(an)) {
                        in.skipBytes(len);
                        continue;
                    }
                    in.readUnsignedShort();
                    in.readUnsignedShort();
                    int cl = in.readInt();
                    byte[] code = new byte[cl];
                    in.readFully(code);
                    int ne = in.readUnsignedShort();
                    in.skipBytes(ne * 8);
                    boolean branches = ne > 0 || hasBranch(code);
                    int nca = in.readUnsignedShort();
                    boolean sm = false;
                    for (int k = 0; k < nca; k++) {
                        String cn = utf[in.readUnsignedShort()];
                        in.skipBytes(in.readInt());
                        if ("StackMap".equals(cn)) sm = true;
                        if ("StackMapTable".equals(cn)) table++;
                    }
                    if (branches) need++;
                    if (sm) have++;
                }
            }
            return new int[]{need, have, table};
        }

        static boolean hasBranch(byte[] c) {
            int pc = 0;
            while (pc < c.length) {
                int op = c[pc] & 0xff;
                if ((op >= 0x99 && op <= 0xa8) || op == 0xaa || op == 0xab || op == 0xc6 || op == 0xc7 || op == 0xc8 || op == 0xc9) return true;
                pc += len(c, pc, op);
            }
            return false;
        }

        static int len(byte[] c, int pc, int op) {
            switch (op) {
                case 0x10: case 0x12: case 0x15: case 0x16: case 0x17: case 0x18: case 0x19: case 0x36: case 0x37: case 0x38:
                case 0x39: case 0x3a: case 0xa9: case 0xbc: return 2;
                case 0x11: case 0x13: case 0x14: case 0x84: case 0xb2: case 0xb3: case 0xb4: case 0xb5: case 0xb6: case 0xb7:
                case 0xb8: case 0xbb: case 0xbd: case 0xc0: case 0xc1: return 3;
                case 0xc5: return 4;
                case 0xb9: case 0xba: return 5;
                case 0xc4: return (c[pc + 1] & 0xff) == 0x84 ? 6 : 4;
                case 0xaa: {
                    int p = (pc + 4) & ~3;
                    int lo = i4(c, p + 4), hi = i4(c, p + 8);
                    return p + 12 + (hi - lo + 1) * 4 - pc;
                }
                case 0xab: {
                    int p = (pc + 4) & ~3;
                    int np = i4(c, p + 4);
                    return p + 8 + np * 8 - pc;
                }
                default: return 1;
            }
        }

        static int i4(byte[] c, int p) {
            return ((c[p] & 0xff) << 24) | ((c[p + 1] & 0xff) << 16) | ((c[p + 2] & 0xff) << 8) | (c[p + 3] & 0xff);
        }
    }
}

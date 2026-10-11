import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

/** Doi ten class/field/method theo file mapping (ten cu, kem descriptor cu).
 *  Dong mapping:  C old New | F owner name desc newName | M owner name desc newName  (# = chu thich) */
public class Renamer {
    static Map<String,String> cls = new HashMap<>(), mem = new HashMap<>();
    static Set<String> pub = new HashSet<>();
    static Map<String,String[]> supers = new HashMap<>();

    static String find(String owner, String key) {
        Deque<String> q = new ArrayDeque<>(); q.add(owner);
        while (!q.isEmpty()) {
            String o = q.poll();
            String r = mem.get(o + "." + key);
            if (r != null) return r;
            String[] s = supers.get(o);
            if (s != null) for (String x : s) if (x != null) q.add(x);
        }
        return null;
    }

    public static void main(String[] a) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(a[1]));
        for (String l; (l = br.readLine()) != null; ) {
            l = l.replaceAll("#.*", "").trim();
            if (l.isEmpty()) continue;
            String[] t = l.split("\\s+");
            if (t[0].equals("C")) cls.put(t[1], t[2]);
            else if (t[0].equals("P")) pub.add(t[1] + "." + t[2] + t[3]);
            else mem.put(t[1] + "." + t[2] + t[3], t[4]);
        }
        Map<String,byte[]> files = new LinkedHashMap<>();
        ZipInputStream zi = new ZipInputStream(new FileInputStream(a[0]));
        for (ZipEntry e; (e = zi.getNextEntry()) != null; ) {
            if (e.isDirectory()) continue;
            files.put(e.getName(), zi.readAllBytes());
        }
        for (Map.Entry<String,byte[]> e : files.entrySet()) if (e.getKey().endsWith(".class")) {
            ClassReader cr = new ClassReader(e.getValue());
            String[] it = cr.getInterfaces();
            String[] s = new String[it.length + 1]; s[0] = cr.getSuperName();
            System.arraycopy(it, 0, s, 1, it.length);
            supers.put(cr.getClassName(), s);
        }
        Remapper rm = new Remapper() {
            public String map(String n) { return cls.getOrDefault(n, n); }
            public String mapMethodName(String o, String n, String d) { String r = find(o, n + d); return r == null ? n : r; }
            public String mapFieldName(String o, String n, String d) { String r = find(o, n + d); return r == null ? n : r; }
        };
        ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(a[2]));
        Set<String> used = new HashSet<>();
        for (Map.Entry<String,byte[]> e : files.entrySet()) {
            String n = e.getKey(); byte[] b = e.getValue();
            if (n.endsWith(".class")) {
                ClassReader cr = new ClassReader(b);
                ClassWriter cw = new ClassWriter(0);
                final String cn = cr.getClassName();
                ClassVisitor acc = new ClassVisitor(Opcodes.ASM9, cw) {
                    int fix(int ac, String k) { return pub.contains(k) ? (ac & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC : ac; }
                    String oldName(String nm, String d) { return nm; }
                };
                cr.accept(new ClassVisitor(Opcodes.ASM9, new ClassRemapper(cw, rm)) {
                    public FieldVisitor visitField(int ac, String nm, String d, String sg, Object v) {
                        if (pub.contains(cn + "." + nm + d)) ac = (ac & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
                        return super.visitField(ac, nm, d, sg, v);
                    }
                    public MethodVisitor visitMethod(int ac, String nm, String d, String sg, String[] ex) {
                        if (pub.contains(cn + "." + nm + d)) ac = (ac & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
                        return super.visitMethod(ac, nm, d, sg, ex);
                    }
                }, 0);
                b = cw.toByteArray();
                n = rm.map(cr.getClassName()) + ".class";
            }
            if (!used.add(n)) throw new RuntimeException("trung ten: " + n);
            zo.putNextEntry(new ZipEntry(n)); zo.write(b); zo.closeEntry();
        }
        zo.close();
        // kiem tra: moi dong M/F phai khop 1 member co that
        Set<String> real = new HashSet<>();
        for (Map.Entry<String,byte[]> e : files.entrySet()) if (e.getKey().endsWith(".class")) {
            final String[] cn = {null};
            new ClassReader(e.getValue()).accept(new ClassVisitor(Opcodes.ASM9) {
                public void visit(int v,int ac,String nm,String sg,String sp,String[] in){cn[0]=nm;}
                public FieldVisitor visitField(int ac,String nm,String d,String sg,Object v){real.add(cn[0]+"."+nm+d);return null;}
                public MethodVisitor visitMethod(int ac,String nm,String d,String sg,String[] ex){real.add(cn[0]+"."+nm+d);return null;}
            }, ClassReader.SKIP_CODE);
        }
        for (String k : pub) if (!real.contains(k)) System.err.println("KHONG CO MEMBER (P): " + k);
        for (String k : mem.keySet()) if (!real.contains(k)) System.err.println("KHONG CO MEMBER: " + k);
        for (String k : cls.keySet()) if (!supers.containsKey(k)) System.err.println("KHONG CO CLASS: " + k);
    }
}

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;
import org.objectweb.asm.tree.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.*;

/**
 * Remap — đổi tên class / field / method trên bytecode theo bảng ánh xạ (ASM), có kiểm tra quy tắc Java:
 *  - khoá theo (owner, name, desc); tên cũ trùng khác kiểu trong cùng class là chuyện thường của bộ làm rối;
 *  - phân giải owner qua phân cấp class/interface trước khi ánh xạ (getfield Sub.x có thể trỏ field của lớp cha);
 *  - hàm ảo đổi tên theo NHÓM override (lớp cha, lớp con, interface cùng tên+descriptor đổi cùng nhau);
 *  - nhóm có hàm ghi đè API CLDC/MIDP (paint, run, keyPressed, startApp, toString, ...) bị ĐÓNG BĂNG, không đổi tên;
 *  - sau khi tính xong, mọi lệnh getfield/putfield/invoke phân giải lại trong không gian tên mới và phải trỏ đúng
 *    thành viên cũ (bắt va chạm tên, ghi đè ngẫu nhiên, che field), tên mới không được trùng nhau trong class.
 *
 * Dùng:
 *   Remap apply <in.jar> <out.jar> --lib a.jar:b.jar [--map f.map]... [--scope orig.jar] [--placeholders] [--lenient]
 *               [--resolved out.map] [--inverse out.map] [--reverse out.tsv] [--report out.txt]
 *   Remap compare <a.jar> <b.jar>      // so từng class sau khi đọc-ghi qua ASM + md5 tài nguyên (chứng minh khứ hồi)
 *
 * Định dạng .map (mỗi dòng; # là chú thích; tên cũ theo không gian tên GỐC):
 *   C <cũ> <mới> [HIGH|MED|LOW]
 *   F <owner>.<tên>:<desc> <mới> [conf]
 *   M <owner>.<tên><desc> <mới> [conf]          // một thành viên của nhóm override là đủ, cả nhóm cùng đổi
 * --placeholders: thành viên chưa có tên, tên cũ 1–2 chữ cái, thuộc class trong --scope (JAR gốc) →
 *   field: <cũ>_<kiểu> (a_Z, b_AB, c_Str, d_Image…); hàm: giữ tên, chỉ thêm _<kiểu trả về> khi hai hàm cùng tham số khác kiểu trả về.
 */
public class Remap {
    // ------------------------------------------------------------------ mô hình
    static Map<String, ClassNode> app = new LinkedHashMap<>(), lib = new HashMap<>();
    static Map<String, String> cmap = new HashMap<>();                 // class cũ → mới
    static Map<String, String> fnew = new HashMap<>();                 // "owner|name|desc" → tên mới
    static Map<String, String> mnew = new HashMap<>();                 // gốc nhóm (hoặc khoá tĩnh/private) → tên mới
    static Map<String, String> conf = new HashMap<>();                 // khoá (C:/F:/M:) → độ tin cậy
    static Map<String, String> parent = new HashMap<>();               // union-find nhóm override
    static Set<String> frozen = new HashSet<>();                       // gốc nhóm bị đóng băng
    static Set<String> scope = null;                                   // class thuộc JAR gốc (null = tất cả)
    static boolean lenient = false;                                   // khứ hồi về tên làm rối: cho phép field trùng tên khác kiểu
    static List<String> warnings = new ArrayList<>(), errors = new ArrayList<>();

    static String key(String o, String n, String d) { return o + "|" + n + "|" + d; }
    static ClassNode node(String n) { ClassNode c = app.get(n); return c != null ? c : lib.get(n); }

    static String find(String k) {
        String p = parent.get(k);
        if (p == null) return k;
        String r = find(p);
        parent.put(k, r);
        return r;
    }
    static void union(String a, String b) {
        String ra = find(a), rb = find(b);
        if (!ra.equals(rb)) parent.put(ra, rb);
    }
    static boolean isVirtual(MethodNode m) {
        return (m.access & (Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC)) == 0 && !m.name.startsWith("<");
    }
    static MethodNode declMethod(ClassNode c, String n, String d) {
        for (MethodNode m : c.methods) if (m.name.equals(n) && m.desc.equals(d)) return m;
        return null;
    }
    static FieldNode declField(ClassNode c, String n, String d) {
        for (FieldNode f : c.fields) if (f.name.equals(n) && f.desc.equals(d)) return f;
        return null;
    }
    /** mọi tổ tiên (lớp cha + interface, bắc cầu), app hoặc lib, không gồm chính nó */
    static List<ClassNode> ancestors(ClassNode c) {
        List<ClassNode> out = new ArrayList<>();
        Deque<String> q = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        if (c.superName != null) q.add(c.superName);
        q.addAll(c.interfaces);
        while (!q.isEmpty()) {
            String s = q.poll();
            if (!seen.add(s)) continue;
            ClassNode n = node(s);
            if (n == null) { warnings.add("class không thấy: " + s + " (tổ tiên của " + c.name + ")"); continue; }
            out.add(n);
            if (n.superName != null) q.add(n.superName);
            q.addAll(n.interfaces);
        }
        return out;
    }

    // ------------------------------------------------------------------ phân giải tham chiếu (theo JVMS 5.4.3)
    /** khoá "decl|name|desc" của hàm app được tham chiếu, null nếu thuộc thư viện / không thấy */
    static String resolveMethod(Map<String, ClassNode> pool, String owner, String name, String desc) {
        ClassNode c = pool.get(owner);
        if (c == null) return null;
        for (ClassNode k = c; k != null; ) {
            if (declMethod(k, name, desc) != null) return key(k.name, name, desc);
            if (k.superName == null) break;
            ClassNode s = pool.get(k.superName);
            if (s == null) {
                // lớp cha thuộc thư viện: nếu thư viện khai báo thì đây là hàm API
                ClassNode l = lib.get(k.superName);
                for (; l != null; l = l.superName == null ? null : lib.get(l.superName))
                    if (declMethod(l, name, desc) != null) return null;
                break;
            }
            k = s;
        }
        Deque<String> q = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        for (ClassNode k = c; k != null; k = k.superName == null ? null : pool.get(k.superName)) q.addAll(k.interfaces);
        while (!q.isEmpty()) {
            String s = q.poll();
            if (!seen.add(s)) continue;
            ClassNode n = pool.get(s);
            if (n == null) continue;
            if (declMethod(n, name, desc) != null) return key(n.name, name, desc);
            q.addAll(n.interfaces);
        }
        return null;
    }
    static String resolveField(Map<String, ClassNode> pool, String owner, String name, String desc) {
        ClassNode c = pool.get(owner);
        if (c == null) return null;
        if (declField(c, name, desc) != null) return key(c.name, name, desc);
        Deque<String> q = new ArrayDeque<>(c.interfaces);
        Set<String> seen = new HashSet<>();
        while (!q.isEmpty()) {
            String s = q.poll();
            if (!seen.add(s)) continue;
            ClassNode n = pool.get(s);
            if (n == null) continue;
            if (declField(n, name, desc) != null) return key(n.name, name, desc);
            q.addAll(n.interfaces);
        }
        if (c.superName != null) return resolveField(pool, c.superName, name, desc);
        return null;
    }

    // ------------------------------------------------------------------ nạp
    static void loadJar(String jar, Map<String, ClassNode> into, int flags) throws IOException {
        try (ZipFile z = new ZipFile(jar)) {
            for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements(); ) {
                ZipEntry ze = e.nextElement();
                if (!ze.getName().endsWith(".class")) continue;
                ClassNode cn = new ClassNode();
                try (InputStream in = z.getInputStream(ze)) { new ClassReader(in.readAllBytes()).accept(cn, flags); }
                into.putIfAbsent(cn.name, cn);
            }
        }
    }

    static void parseMap(String file) throws IOException {
        int ln = 0;
        for (String line : Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8)) {
            ln++;
            int h = line.indexOf('#');
            if (h >= 0) line = line.substring(0, h);
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] t = line.split("\\s+");
            String c = t.length > 3 ? t[3] : (t.length > 2 && !t[0].equals("C") ? t[2] : "");
            try {
                switch (t[0]) {
                    case "C":
                        if (cmap.containsKey(t[1]) && !cmap.get(t[1]).equals(t[2])) errors.add(file + ":" + ln + " class " + t[1] + " đặt hai tên");
                        cmap.put(t[1], t[2]);
                        conf.put("C:" + t[1], t.length > 3 ? t[3] : "");
                        break;
                    case "F":
                        rawSpecs.add(new String[]{"F", t[1], t[2], t.length > 3 ? t[3] : "", file + ":" + ln});
                        break;
                    case "M":
                        rawSpecs.add(new String[]{"M", t[1], t[2], t.length > 3 ? t[3] : "", file + ":" + ln});
                        break;
                    default: errors.add(file + ":" + ln + " dòng lạ: " + line);
                }
            } catch (RuntimeException ex) { errors.add(file + ":" + ln + " lỗi cú pháp: " + line); }
        }
    }
    static Map<String, String> pendingM = new LinkedHashMap<>();
    static List<String[]> rawSpecs = new ArrayList<>();

    /** "(ILjava/lang/String;[B)V" → "int,String,byte[]" (tên class gốc / tên đơn giản của class thư viện) */
    static String javaParams(String desc) {
        StringBuilder sb = new StringBuilder();
        for (Type t : Type.getArgumentTypes(desc)) {
            if (sb.length() > 0) sb.append(',');
            sb.append(javaType(t));
        }
        return sb.toString();
    }
    static String javaType(Type t) {
        switch (t.getSort()) {
            case Type.ARRAY: {
                StringBuilder sb = new StringBuilder(javaType(t.getElementType()));
                for (int i = 0; i < t.getDimensions(); i++) sb.append("[]");
                return sb.toString();
            }
            case Type.OBJECT: { String n = t.getInternalName(); return n.substring(n.lastIndexOf('/') + 1); }
            default: return t.getClassName();
        }
    }
    /** tên placeholder KHÔNG PHỤ THUỘC đổi tên class (dùng để tra khi viết map): <cũ>_<kiểu> với tên class gốc */
    static String tagOrig(String d) {
        switch (d.charAt(0)) {
            case '[': return "A" + tagOrig(d.substring(1));
            case 'L': { String n = d.substring(1, d.length() - 1); return n.substring(n.lastIndexOf('/') + 1); }
            case 'V': return "V";
            default: return d.substring(0, 1);
        }
    }
    /** đổi mọi dòng F/M viết tắt (owner.tên_kiểu / owner.tên(tham,số)) về khoá chuẩn owner|tên|desc */
    static void resolveSpecs() {
        for (String[] sp : rawSpecs) {
            String kind = sp[0], spec = sp[1], neu = sp[2], cf = sp[3], where = sp[4];
            int dot = spec.indexOf('.');
            if (dot < 0) { errors.add(where + " thiếu '.' trong " + spec); continue; }
            String owner = spec.substring(0, dot), rest = spec.substring(dot + 1);
            ClassNode c = app.get(owner);
            if (c == null) { errors.add(where + " class không có: " + owner); continue; }
            String k = null;
            if (kind.equals("F")) {
                if (rest.indexOf(':') >= 0) {
                    k = key(owner, rest.substring(0, rest.indexOf(':')), rest.substring(rest.indexOf(':') + 1));
                } else {
                    for (FieldNode f : c.fields) {
                        String ph = f.name + "_" + tagOrig(f.desc);
                        if (rest.equals(ph) || (rest.equals(f.name) && obf(f.name) == false)) {
                            if (k != null) errors.add(where + " mơ hồ: " + spec);
                            k = key(owner, f.name, f.desc);
                        }
                    }
                }
                if (k == null) { errors.add(where + " không tìm thấy field " + spec); continue; }
                if (fnew.containsKey(k) && !fnew.get(k).equals(neu)) errors.add(where + " field " + spec + " đặt hai tên");
                fnew.put(k, neu);
                conf.put("F:" + k, cf);
            } else {
                int par = rest.indexOf('(');
                if (par < 0) { errors.add(where + " thiếu '(' trong " + spec); continue; }
                String nm = rest.substring(0, par), tail = rest.substring(par);
                if (!tail.endsWith(")")) {
                    k = key(owner, nm, tail);
                } else {
                    String params = tail.substring(1, tail.length() - 1);
                    for (MethodNode m : c.methods) {
                        if (m.name.startsWith("<")) continue;
                        if (!javaParams(m.desc).equals(params)) continue;
                        String ret = tagOrig(m.desc.substring(m.desc.indexOf(')') + 1));
                        if (nm.equals(m.name) || nm.equals(m.name + "_" + ret)) {
                            if (k != null) { errors.add(where + " mơ hồ (cùng tham số khác kiểu trả về — thêm _<kiểu>): " + spec); k = null; break; }
                            k = key(owner, m.name, m.desc);
                        }
                    }
                }
                if (k == null) { errors.add(where + " không tìm thấy hàm " + spec); continue; }
                pendingM.put(k, neu);
                conf.put("M:" + k, cf);
            }
        }
        rawSpecs.clear();
    }

    // ------------------------------------------------------------------ nhóm override
    static void buildGroups() {
        for (ClassNode c : app.values()) {
            for (MethodNode m : c.methods) {
                if (!isVirtual(m)) continue;
                String k = key(c.name, m.name, m.desc);
                for (ClassNode a : ancestors(c)) {
                    MethodNode am = declMethod(a, m.name, m.desc);
                    if (am == null || !isVirtual(am)) continue;
                    if (app.containsKey(a.name)) union(k, key(a.name, m.name, m.desc));
                    else frozen.add(k);
                }
            }
        }
        Set<String> roots = new HashSet<>();
        for (String k : frozen) roots.add(find(k));
        frozen.clear();
        frozen.addAll(roots);
        // frozen lan theo nhóm: kiểm lại bằng gốc
    }
    static String methodRoot(String k) { return find(k); }

    static boolean obf(String n) { return n.length() <= 2 && n.matches("[A-Za-z]{1,2}[0-9]?") && n.indexOf('$') < 0; }
    static boolean inScope(String cls) { return scope == null || scope.contains(cls); }

    static String simple(String internal) {
        String m = cmap.getOrDefault(internal, internal);
        return m.substring(m.lastIndexOf('/') + 1);
    }
    static String tag(String d) {
        switch (d.charAt(0)) {
            case '[': return "A" + tag(d.substring(1));
            case 'L': return simple(d.substring(1, d.length() - 1));
            case 'V': return "V";
            default: return d.substring(0, 1);
        }
    }

    // ------------------------------------------------------------------ tính tên cuối
    static void computeNames(boolean placeholders) {
        // class
        for (String o : cmap.keySet()) if (!app.containsKey(o)) errors.add("C: class không có trong JAR: " + o);
        // field
        for (String k : fnew.keySet()) {
            String[] p = k.split("\\|");
            ClassNode c = app.get(p[0]);
            if (c == null || declField(c, p[1], p[2]) == null) errors.add("F: field không khai báo ở " + p[0] + ": " + p[1] + ":" + p[2]);
        }
        // method
        for (Map.Entry<String, String> e : pendingM.entrySet()) {
            String[] p = e.getKey().split("\\|");
            ClassNode c = app.get(p[0]);
            if (c == null || declMethod(c, p[1], p[2]) == null) { errors.add("M: hàm không khai báo ở " + p[0] + ": " + p[1] + p[2]); continue; }
            MethodNode m = declMethod(c, p[1], p[2]);
            String r = isVirtual(m) ? find(e.getKey()) : e.getKey();
            if (frozen.contains(r)) { errors.add("M: nhóm bị đóng băng (ghi đè API thư viện), không đổi tên: " + e.getKey() + " → " + e.getValue()); continue; }
            String prev = mnew.get(r);
            if (prev != null && !prev.equals(e.getValue())) errors.add("M: nhóm " + r + " có hai tên: " + prev + " / " + e.getValue());
            mnew.put(r, e.getValue());
        }
        if (!placeholders) return;
        // field placeholder
        for (ClassNode c : app.values()) {
            if (!inScope(c.name)) continue;
            for (FieldNode f : c.fields) {
                String k = key(c.name, f.name, f.desc);
                if (!fnew.containsKey(k) && obf(f.name)) {
                    fnew.put(k, f.name + "_" + tag(f.desc));
                    conf.put("F:" + k, "PLACEHOLDER");
                }
            }
        }
        // method: chỉ đổi khi hai hàm cùng tên + cùng tham số khác kiểu trả về (không biên dịch lại được nếu giữ)
        for (ClassNode c : app.values()) {
            if (!inScope(c.name)) continue;
            Map<String, List<MethodNode>> b = new LinkedHashMap<>();
            for (MethodNode m : c.methods) {
                if (m.name.startsWith("<")) continue;
                String nm = newMethodName(c.name, m);
                b.computeIfAbsent(nm + m.desc.substring(0, m.desc.indexOf(')') + 1), x -> new ArrayList<>()).add(m);
            }
            for (List<MethodNode> l : b.values()) {
                if (l.size() < 2) continue;
                for (MethodNode m : l) {
                    String k = key(c.name, m.name, m.desc);
                    String r = isVirtual(m) ? find(k) : k;
                    if (mnew.containsKey(r) || frozen.contains(r) || !obf(m.name)) continue;
                    mnew.put(r, m.name + "_" + tag(m.desc.substring(m.desc.indexOf(')') + 1)));
                    conf.put("M:" + k, "PLACEHOLDER");
                }
            }
        }
    }

    static String newMethodName(String owner, MethodNode m) {
        if (m.name.startsWith("<")) return m.name;
        String k = key(owner, m.name, m.desc);
        String r = isVirtual(m) ? find(k) : k;
        String n = mnew.get(r);
        return n != null ? n : m.name;
    }
    static String newFieldName(String decl, String name, String desc) {
        String n = fnew.get(key(decl, name, desc));
        return n != null ? n : name;
    }

    // ------------------------------------------------------------------ Remapper
    static class R extends Remapper {
        @Override public String map(String internalName) {
            String m = cmap.get(internalName);
            return m != null ? m : internalName;
        }
        @Override public String mapFieldName(String owner, String name, String desc) {
            String k = resolveField(app, owner, name, desc);
            return k == null ? name : newFieldName(k.split("\\|")[0], name, desc);
        }
        @Override public String mapMethodName(String owner, String name, String desc) {
            if (name.startsWith("<") || owner.startsWith("[")) return name;
            String k = resolveMethod(app, owner, name, desc);
            if (k == null) return name;
            String[] p = k.split("\\|");
            ClassNode c = app.get(p[0]);
            return newMethodName(p[0], declMethod(c, name, desc));
        }
    }

    // ------------------------------------------------------------------ kiểm tra sau khi tính tên
    static void verifyNames(R r) {
        // 1) mô hình không gian tên mới
        Map<String, ClassNode> nm = new HashMap<>();
        Map<String, String> back = new HashMap<>();                       // khoá mới → khoá cũ
        Set<String> lowers = new HashSet<>();
        for (ClassNode c : app.values()) {
            String n = r.map(c.name);
            if (!lowers.add(n.toLowerCase())) errors.add("tên class trùng (không phân biệt hoa/thường): " + n);
            if (lib.containsKey(n)) errors.add("tên class trùng class thư viện: " + n);
            if (nm.containsKey(n)) errors.add("tên class trùng: " + n);
            ClassNode x = new ClassNode();
            x.name = n;
            x.superName = c.superName == null ? null : r.map(c.superName);
            x.interfaces = new ArrayList<>();
            for (String i : c.interfaces) x.interfaces.add(r.map(i));
            x.access = c.access;
            Set<String> fseen = new HashSet<>(), mseen = new HashSet<>();
            for (FieldNode f : c.fields) {
                String fn = newFieldName(c.name, f.name, f.desc), fd = r.mapDesc(f.desc);
                if (!fseen.add(lenient ? fn + ":" + fd : fn)) errors.add("field trùng tên trong " + n + ": " + fn);
                if (fn.indexOf('$') < 0 && !fn.matches("[A-Za-z_][A-Za-z0-9_]*")) errors.add("tên field không hợp lệ: " + n + "." + fn);
                x.fields.add(new FieldNode(f.access, fn, fd, null, null));
                back.put(key(n, fn, fd), key(c.name, f.name, f.desc));
            }
            for (MethodNode m : c.methods) {
                String mn = newMethodName(c.name, m), md = r.mapMethodDesc(m.desc);
                if (!mseen.add(mn + md)) errors.add("hàm trùng tên+desc trong " + n + ": " + mn + md);
                if (!mn.startsWith("<") && mn.indexOf('$') < 0 && !mn.matches("[A-Za-z_][A-Za-z0-9_]*")) errors.add("tên hàm không hợp lệ: " + n + "." + mn);
                x.methods.add(new MethodNode(m.access, mn, md, null, null));
                back.put(key(n, mn, md), key(c.name, m.name, m.desc));
            }
            nm.put(n, x);
        }
        // 2) hàm ảo: không ghi đè ngẫu nhiên / không trùng hàm thư viện
        for (ClassNode c : nm.values()) {
            Deque<String> q = new ArrayDeque<>();
            Set<String> seen = new HashSet<>();
            if (c.superName != null) q.add(c.superName);
            q.addAll(c.interfaces);
            List<ClassNode> anc = new ArrayList<>();
            while (!q.isEmpty()) {
                String s = q.poll();
                if (!seen.add(s)) continue;
                ClassNode a = nm.get(s);
                if (a == null) a = lib.get(s);
                if (a == null) continue;
                anc.add(a);
                if (a.superName != null) q.add(a.superName);
                q.addAll(a.interfaces);
            }
            for (MethodNode m : c.methods) {
                if (!isVirtual(m)) continue;
                String oldK = back.get(key(c.name, m.name, m.desc));
                for (ClassNode a : anc) {
                    MethodNode am = declMethod(a, m.name, m.desc);
                    if (am == null || !isVirtual(am)) continue;
                    if (nm.containsKey(a.name)) {
                        String aOld = back.get(key(a.name, m.name, m.desc));
                        if (!find(oldK).equals(find(aOld)))
                            errors.add("ghi đè ngẫu nhiên: " + c.name + "." + m.name + m.desc + " trùng " + a.name + " (nhóm khác)");
                    } else if (!frozen.contains(find(oldK))) {
                        errors.add("tên mới trùng hàm thư viện " + a.name + "." + m.name + m.desc + " (từ " + oldK + ")");
                    }
                }
            }
        }
        // 3) mọi tham chiếu phải phân giải về đúng thành viên cũ
        int refs = 0;
        for (ClassNode c : app.values()) {
            for (MethodNode m : c.methods) {
                if (m.instructions == null) continue;
                for (AbstractInsnNode in : m.instructions) {
                    if (in instanceof FieldInsnNode) {
                        FieldInsnNode fi = (FieldInsnNode) in;
                        String o1 = resolveField(app, fi.owner, fi.name, fi.desc);
                        String n2 = resolveField(nm, r.map(fi.owner), r.mapFieldName(fi.owner, fi.name, fi.desc), r.mapDesc(fi.desc));
                        String o2 = n2 == null ? null : back.get(n2);
                        refs++;
                        if (!Objects.equals(o1, o2)) errors.add("tham chiếu field đổi nghĩa: " + c.name + "." + m.name + " " + fi.owner + "." + fi.name + " " + o1 + " → " + o2);
                    } else if (in instanceof MethodInsnNode) {
                        MethodInsnNode mi = (MethodInsnNode) in;
                        if (mi.owner.startsWith("[") || mi.name.startsWith("<")) continue;
                        String o1 = resolveMethod(app, mi.owner, mi.name, mi.desc);
                        String n2 = resolveMethod(nm, r.map(mi.owner), r.mapMethodName(mi.owner, mi.name, mi.desc), r.mapMethodDesc(mi.desc));
                        String o2 = n2 == null ? null : back.get(n2);
                        refs++;
                        if (!Objects.equals(o1, o2)) errors.add("tham chiếu hàm đổi nghĩa: " + c.name + "." + m.name + " " + mi.owner + "." + mi.name + mi.desc + " " + o1 + " → " + o2);
                    }
                }
            }
        }
        System.out.println("verify tham chiếu: " + refs + " lệnh field/hàm phân giải lại, lỗi=" + errors.size());
    }

    // ------------------------------------------------------------------ apply
    static void apply(String[] a) throws Exception {
        String in = a[1], out = a[2];
        List<String> maps = new ArrayList<>();
        String libs = null, scopeJar = null, resolved = null, inverse = null, reverse = null, report = null;
        boolean ph = false;
        for (int i = 3; i < a.length; i++) {
            switch (a[i]) {
                case "--map": maps.add(a[++i]); break;
                case "--lib": libs = a[++i]; break;
                case "--scope": scopeJar = a[++i]; break;
                case "--placeholders": ph = true; break;
                case "--lenient": lenient = true; break;
                case "--resolved": resolved = a[++i]; break;
                case "--inverse": inverse = a[++i]; break;
                case "--reverse": reverse = a[++i]; break;
                case "--report": report = a[++i]; break;
                default: throw new IllegalArgumentException(a[i]);
            }
        }
        loadJar(in, app, 0);
        for (String l : libs.split(":")) loadJar(l, lib, ClassReader.SKIP_CODE);
        if (scopeJar != null) {
            Map<String, ClassNode> s = new HashMap<>();
            loadJar(scopeJar, s, ClassReader.SKIP_CODE);
            scope = new HashSet<>(s.keySet());
        }
        for (String m : maps) parseMap(m);
        resolveSpecs();
        buildGroups();
        computeNames(ph);
        R r = new R();
        verifyNames(r);
        // ASCII + không trùng chữ hoa/thường đã kiểm trong verifyNames; Main phải giữ nguyên
        if (!r.map("Main").equals("Main")) errors.add("Main (MIDlet-1) không được đổi tên");
        for (String w : warnings) System.out.println("WARN " + w);
        if (!errors.isEmpty()) {
            for (String e : errors) System.out.println("ERROR " + e);
            System.out.println("Remap: " + errors.size() + " lỗi, không ghi JAR");
            System.exit(1);
        }
        // ghi JAR
        Map<String, byte[]> classBytes = new HashMap<>();
        for (ClassNode c : app.values()) {
            ClassWriter cw = new ClassWriter(0);
            c.accept(new ClassRemapper(cw, r));
            classBytes.put(c.name, cw.toByteArray());
        }
        int nres = 0, ncls = 0;
        try (ZipFile z = new ZipFile(in); ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(out))) {
            zo.setLevel(9);
            for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements(); ) {
                ZipEntry ze = e.nextElement();
                if (ze.isDirectory()) continue;
                String n = ze.getName();
                byte[] data;
                if (n.endsWith(".class")) {
                    String cn = n.substring(0, n.length() - 6);
                    data = classBytes.get(cn);
                    n = r.map(cn) + ".class";
                    ncls++;
                } else {
                    try (InputStream is = z.getInputStream(ze)) { data = is.readAllBytes(); }
                    nres++;
                }
                zo.putNextEntry(new ZipEntry(n));
                zo.write(data);
                zo.closeEntry();
            }
        }
        // thống kê + bảng đã giải
        int nc = 0, nf = 0, nmth = 0, nph = 0, nfrozen = 0;
        StringBuilder res = new StringBuilder("# resolved map — sinh bởi Remap, KHÔNG sửa tay (không gian tên gốc → mới)\n");
        StringBuilder rev = new StringBuilder("# kind\tmới\tcũ\tđộ tin cậy\n");
        StringBuilder inv = new StringBuilder("# inverse map — không gian tên mới → gốc (dùng cho kiểm khứ hồi)\n");
        for (ClassNode c : app.values()) {
            String n = r.map(c.name);
            if (!n.equals(c.name)) {
                nc++;
                res.append("C ").append(c.name).append(' ').append(n).append(' ').append(conf.getOrDefault("C:" + c.name, "")).append('\n');
                rev.append("C\t").append(n).append('\t').append(c.name).append('\t').append(conf.getOrDefault("C:" + c.name, "")).append('\n');
                inv.append("C ").append(n).append(' ').append(c.name).append('\n');
            }
        }
        for (ClassNode c : app.values()) {
            String n = r.map(c.name);
            for (FieldNode f : c.fields) {
                String fn = newFieldName(c.name, f.name, f.desc);
                if (fn.equals(f.name)) continue;
                String cf = conf.getOrDefault("F:" + key(c.name, f.name, f.desc), "");
                if (cf.equals("PLACEHOLDER")) nph++;
                nf++;
                res.append("F ").append(c.name).append('.').append(f.name).append(':').append(f.desc).append(' ').append(fn).append(' ').append(cf).append('\n');
                rev.append("F\t").append(n).append('.').append(fn).append(':').append(r.mapDesc(f.desc)).append('\t').append(c.name).append('.').append(f.name).append(':').append(f.desc).append('\t').append(cf).append('\n');
                inv.append("F ").append(n).append('.').append(fn).append(':').append(r.mapDesc(f.desc)).append(' ').append(f.name).append('\n');
            }
            for (MethodNode m : c.methods) {
                if (isVirtual(m) && frozen.contains(find(key(c.name, m.name, m.desc)))) nfrozen++;
                String mn = newMethodName(c.name, m);
                if (mn.equals(m.name)) continue;
                String cf = conf.getOrDefault("M:" + key(c.name, m.name, m.desc), "");
                if (cf.isEmpty()) { // thành viên khác của nhóm mang độ tin cậy
                    for (ClassNode o : app.values()) {
                        MethodNode om = declMethod(o, m.name, m.desc);
                        if (om != null && isVirtual(om) && find(key(o.name, m.name, m.desc)).equals(find(key(c.name, m.name, m.desc)))
                                && !conf.getOrDefault("M:" + key(o.name, m.name, m.desc), "").isEmpty()) { cf = conf.get("M:" + key(o.name, m.name, m.desc)); break; }
                    }
                }
                if (cf.equals("PLACEHOLDER")) nph++;
                nmth++;
                res.append("M ").append(c.name).append('.').append(m.name).append(m.desc).append(' ').append(mn).append(' ').append(cf).append('\n');
                rev.append("M\t").append(n).append('.').append(mn).append(r.mapMethodDesc(m.desc)).append('\t').append(c.name).append('.').append(m.name).append(m.desc).append('\t').append(cf).append('\n');
                inv.append("M ").append(n).append('.').append(mn).append(r.mapMethodDesc(m.desc)).append(' ').append(m.name).append('\n');
            }
        }
        if (resolved != null) Files.write(Paths.get(resolved), res.toString().getBytes(StandardCharsets.UTF_8));
        if (reverse != null) Files.write(Paths.get(reverse), rev.toString().getBytes(StandardCharsets.UTF_8));
        if (inverse != null) Files.write(Paths.get(inverse), inv.toString().getBytes(StandardCharsets.UTF_8));
        String sum = "Remap: " + ncls + " class, " + nres + " tài nguyên → " + out + "; đổi tên class=" + nc + " field=" + nf + " hàm=" + nmth
                + " (placeholder " + nph + "); nhóm đóng băng (ghi đè API thư viện)=" + frozen.size() + ", hàm trong đó=" + nfrozen + "; cảnh báo=" + warnings.size();
        System.out.println(sum);
        if (report != null) Files.write(Paths.get(report), (sum + "\n").getBytes(StandardCharsets.UTF_8));
    }

    // ------------------------------------------------------------------ compare (khứ hồi)
    static Map<String, byte[]> norm(String jar) throws Exception {
        Map<String, byte[]> m = new TreeMap<>();
        try (ZipFile z = new ZipFile(jar)) {
            for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements(); ) {
                ZipEntry ze = e.nextElement();
                if (ze.isDirectory()) continue;
                byte[] d;
                try (InputStream is = z.getInputStream(ze)) { d = is.readAllBytes(); }
                if (ze.getName().endsWith(".class")) {
                    ClassNode cn = new ClassNode();
                    new ClassReader(d).accept(cn, 0);
                    ClassWriter cw = new ClassWriter(0);
                    // sắp xếp để ghi ổn định: ASM giữ thứ tự field/method gốc, đủ để so
                    cn.accept(cw);
                    d = cw.toByteArray();
                }
                m.put(ze.getName(), d);
            }
        }
        return m;
    }
    static void compare(String[] a) throws Exception {
        Map<String, byte[]> x = norm(a[1]), y = norm(a[2]);
        int same = 0, diff = 0;
        for (String k : x.keySet()) {
            if (!y.containsKey(k)) { System.out.println("CHỈ Ở A: " + k); diff++; continue; }
            if (Arrays.equals(x.get(k), y.get(k))) same++;
            else { System.out.println("KHÁC: " + k); diff++; }
        }
        for (String k : y.keySet()) if (!x.containsKey(k)) { System.out.println("CHỈ Ở B: " + k); diff++; }
        System.out.println("compare: giống=" + same + " khác=" + diff);
        System.exit(diff == 0 ? 0 : 1);
    }

    public static void main(String[] a) throws Exception {
        if (a[0].equals("apply")) apply(a);
        else if (a[0].equals("compare")) compare(a);
        else { System.err.println("Remap apply|compare ..."); System.exit(2); }
    }
}

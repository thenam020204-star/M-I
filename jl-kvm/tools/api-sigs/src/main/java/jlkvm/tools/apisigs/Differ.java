package jlkvm.tools.apisigs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/** Lists differences between two API databases (A = expected, B = actual). */
public final class Differ {
    private Differ() {
    }

    public static List<String> diff(ApiDb a, ApiDb b) {
        List<String> out = new ArrayList<>();
        Map<String, ApiDb.ClassSig> ma = index(a), mb = index(b);
        TreeSet<String> all = new TreeSet<>(ma.keySet());
        all.addAll(mb.keySet());
        for (String n : all) {
            ApiDb.ClassSig ca = ma.get(n), cb = mb.get(n);
            if (cb == null) { out.add("- class " + n + " (only in A)"); continue; }
            if (ca == null) { out.add("+ class " + n + " (only in B)"); continue; }
            if (!eq(ca.superName, cb.superName)) out.add("~ class " + n + " super: " + ca.superName + " -> " + cb.superName);
            if (!ca.interfaces.equals(cb.interfaces)) out.add("~ class " + n + " interfaces: " + ca.interfaces + " -> " + cb.interfaces);
            if (ca.access != cb.access) out.add("~ class " + n + " access: 0x" + Integer.toHexString(ca.access) + " -> 0x" + Integer.toHexString(cb.access));
            Map<String, ApiDb.FieldSig> fa = new LinkedHashMap<>(), fb = new LinkedHashMap<>();
            for (ApiDb.FieldSig f : ca.fields) fa.put(f.name + ":" + f.desc, f);
            for (ApiDb.FieldSig f : cb.fields) fb.put(f.name + ":" + f.desc, f);
            TreeSet<String> fk = new TreeSet<>(fa.keySet());
            fk.addAll(fb.keySet());
            for (String k : fk) {
                ApiDb.FieldSig x = fa.get(k), y = fb.get(k);
                if (y == null) out.add("- field " + n + "." + k);
                else if (x == null) out.add("+ field " + n + "." + k);
                else {
                    if (x.access != y.access) out.add("~ field " + n + "." + k + " access: 0x" + Integer.toHexString(x.access) + " -> 0x" + Integer.toHexString(y.access));
                    if (!a.constKey(x).equals(b.constKey(y))) out.add("~ field " + n + "." + k + " constant: " + a.constKey(x) + " -> " + b.constKey(y));
                }
            }
            Map<String, ApiDb.MethodSig> xa = new LinkedHashMap<>(), xb = new LinkedHashMap<>();
            for (ApiDb.MethodSig m : ca.methods) xa.put(m.name + m.desc, m);
            for (ApiDb.MethodSig m : cb.methods) xb.put(m.name + m.desc, m);
            TreeSet<String> mk = new TreeSet<>(xa.keySet());
            mk.addAll(xb.keySet());
            for (String k : mk) {
                ApiDb.MethodSig x = xa.get(k), y = xb.get(k);
                if (y == null) out.add("- method " + n + "." + k);
                else if (x == null) out.add("+ method " + n + "." + k);
                else {
                    if (x.access != y.access) out.add("~ method " + n + "." + k + " access: 0x" + Integer.toHexString(x.access) + " -> 0x" + Integer.toHexString(y.access));
                    if (!x.exceptions.equals(y.exceptions)) out.add("~ method " + n + "." + k + " exceptions: " + x.exceptions + " -> " + y.exceptions);
                }
            }
        }
        return out;
    }

    private static Map<String, ApiDb.ClassSig> index(ApiDb d) {
        Map<String, ApiDb.ClassSig> m = new LinkedHashMap<>();
        for (ApiDb.ClassSig c : d.classes) m.put(c.name, c);
        return m;
    }

    private static boolean eq(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }
}

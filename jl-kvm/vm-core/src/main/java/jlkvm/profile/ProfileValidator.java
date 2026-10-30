package jlkvm.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jlkvm.DeviceProfile;

/**
 * Schema validator of device profiles (PLAN T33). Beyond what {@link ProfileLoader} rejects (unknown keys, wrong types, ranges) it enforces
 * the source rule: every value carries a source tag. A tag is a string that starts with
 * <ul>
 * <li>{@code sdk:} + what (file and section of the vendor SDK the number comes from),</li>
 * <li>{@code datasheet:} + what (a citable public document),</li>
 * <li>{@code measured:} + what (measured on a real device),</li>
 * <li>or {@code UNVERIFIED} (optionally followed by a reason).</li>
 * </ul>
 * Where tags live: top-level key {@code k} is covered by {@code _sources.k} of the top-level object; an object-valued key is also covered by its
 * own {@code _source} (whole section) or by {@code _sources.leaf} inside it (per value; every leaf must then be listed). Only {@code name}
 * needs no tag. Consistency checks: apis vs. layers, CLDC/MIDP properties vs. apis, canvas inside the screen, key table complete.
 */
public final class ProfileValidator {
    private ProfileValidator() {}

    public static final String[] REQUIRED_KEYS = {"softLeft", "softRight", "select", "up", "down", "left", "right", "send", "end"};

    /** @return the problems found (empty = valid). */
    @SuppressWarnings("unchecked")
    public static List<String> validate(String json) {
        List<String> errs = new ArrayList<String>();
        Object o;
        DeviceProfile p;
        try {
            o = MiniJson.parse(json);
            p = ProfileLoader.fromJson(json);
        } catch (RuntimeException e) {
            errs.add("schema: " + e.getMessage());
            return errs;
        }
        Map<String, Object> top = (Map<String, Object>) o;
        Object ts = top.get("_sources");
        Map<String, Object> tops = ts instanceof Map ? (Map<String, Object>) ts : null;
        if (ts != null && tops == null) errs.add("_sources must be an object");
        if (tops != null) for (Map.Entry<String, Object> e : tops.entrySet()) tag(errs, "_sources." + e.getKey(), e.getValue());
        for (Map.Entry<String, Object> e : top.entrySet()) {
            String k = e.getKey();
            if (k.startsWith("_") || k.equals("name")) continue;
            Object t = tops == null ? null : tops.get(k);
            if (t != null) continue;   // validated above
            if (!(e.getValue() instanceof Map)) { errs.add("no source tag for '" + k + "' (top-level _sources." + k + ")"); continue; }
            Map<String, Object> sec = (Map<String, Object>) e.getValue();
            if (sec.get("_source") != null) { tag(errs, k + "._source", sec.get("_source")); continue; }
            Object ls = sec.get("_sources");
            Map<String, Object> leaf = ls instanceof Map ? (Map<String, Object>) ls : null;
            if (leaf == null) { errs.add("no source tag for section '" + k + "' (_source or _sources)"); continue; }
            for (Map.Entry<String, Object> le : leaf.entrySet()) tag(errs, k + "._sources." + le.getKey(), le.getValue());
            for (String n : sec.keySet()) if (!n.startsWith("_") && !leaf.containsKey(n)) errs.add("no source tag for '" + k + "." + n + "'");
        }
        // consistency
        if (p.name == null || p.name.length() == 0 || p.name.equals("generic")) errs.add("name missing");
        if (!p.apis.contains("cldc-1.1")) errs.add("apis must contain cldc-1.1");
        String[][] needs = {{"nokia-ui", "midp-2.0"}, {"wma-1.1", "midp-2.0"}, {"jsr-135", "midp-2.0"}, {"jsr75-fc", "cldc-1.1"}, {"jsr75-pim", "cldc-1.1"}};
        for (int i = 0; i < needs.length; i++) if (p.apis.contains(needs[i][0]) && !p.apis.contains(needs[i][1])) errs.add("api " + needs[i][0] + " needs " + needs[i][1]);
        boolean midp = p.apis.contains("midp-2.0");
        String prof = p.properties.get("microedition.profiles");
        if (midp != (prof != null && prof.indexOf("MIDP-2.0") >= 0)) errs.add("microedition.profiles disagrees with apis (midp-2.0)");
        String conf = p.properties.get("microedition.configuration");
        if (conf == null || !conf.equals(p.cldc11 ? "CLDC-1.1" : "CLDC-1.0")) errs.add("microedition.configuration disagrees with cldc11");
        if (p.heapBytes <= 0) errs.add("heapBytes must be > 0");
        if (p.screenWidth <= 0 || p.screenHeight <= 0 || p.canvasWidth <= 0 || p.canvasHeight <= 0) errs.add("display sizes must be > 0");
        if (p.canvasWidth > p.screenWidth || p.canvasHeight > p.screenHeight) errs.add("canvas larger than the screen");
        for (int i = 0; i < REQUIRED_KEYS.length; i++) if (p.key(REQUIRED_KEYS[i]) == 0) errs.add("keys." + REQUIRED_KEYS[i] + " missing");
        for (String h : p.jadHonored) if (!jlkvm.profile.ProfileValidator.knownJad(h)) errs.add("jad.honored: unknown attribute " + h);
        return errs;
    }

    /** Names the host knows how to act on (see {@code jlkvm.host.ams.NokiaJad}; kept here as plain data so vm-core needs no host class). */
    public static final String[] KNOWN_JAD = {"Nokia-MIDlet-Category", "Nokia-MIDlet-Original-Display-Size", "Nokia-MIDlet-Target-Display-Size",
        "Nokia-MIDlet-On-Screen-Keypad", "Nokia-MIDlet-App-Orientation"};

    public static boolean knownJad(String n) {
        for (int i = 0; i < KNOWN_JAD.length; i++) if (KNOWN_JAD[i].equals(n)) return true;
        return false;
    }

    private static void tag(List<String> errs, String where, Object v) {
        if (!(v instanceof String)) { errs.add(where + ": source tag must be a string"); return; }
        String s = ((String) v).trim();
        if (s.startsWith("UNVERIFIED")) return;
        String[] kinds = {"sdk:", "datasheet:", "measured:"};
        for (int i = 0; i < kinds.length; i++) {
            if (s.startsWith(kinds[i])) {
                if (s.length() - kinds[i].length() < 8) errs.add(where + ": '" + kinds[i] + "' needs a citation (file/section)");
                return;
            }
        }
        errs.add(where + ": source tag must start with sdk: / datasheet: / measured: / UNVERIFIED, got '" + s + "'");
    }
}

package jlkvm.profile;

import java.util.Map;
import jlkvm.DeviceProfile;

/**
 * Loads a device profile from JSON text (PLAN T20 minimal schema; T33 extends it and adds a validator). Keys:
 * name, heapBytes, timesliceBytecodes, priorityScheduling, threadStackWords, cldc11, maxTraceFrames,
 * properties{key:string} (System.getProperty), network{scheme|"default": "allow"|"deny"|"offline"},
 * timeZone{id, offsetMillis}. Keys starting with "_" (comments, source tags) are ignored. Unknown keys are an error.
 */
public final class ProfileLoader {
    private ProfileLoader() {}

    @SuppressWarnings("unchecked")
    public static DeviceProfile fromJson(String text) {
        Object o = MiniJson.parse(text);
        if (!(o instanceof Map)) throw new IllegalArgumentException("profile: object expected");
        DeviceProfile p = new DeviceProfile();
        for (Map.Entry<String, Object> e : ((Map<String, Object>) o).entrySet()) {
            String k = e.getKey();
            Object v = e.getValue();
            if (k.startsWith("_")) continue;
            if (k.equals("name")) p.name = str(k, v);
            else if (k.equals("heapBytes")) p.heapBytes = (int) num(k, v);
            else if (k.equals("timesliceBytecodes")) p.timesliceBytecodes = (int) num(k, v);
            else if (k.equals("priorityScheduling")) p.priorityScheduling = bool(k, v);
            else if (k.equals("threadStackWords")) p.threadStackWords = (int) num(k, v);
            else if (k.equals("cldc11")) p.cldc11 = bool(k, v);
            else if (k.equals("maxTraceFrames")) p.maxTraceFrames = (int) num(k, v);
            else if (k.equals("properties")) {
                p.properties.clear();
                for (Map.Entry<String, Object> pe : obj(k, v).entrySet()) if (!pe.getKey().startsWith("_")) p.properties.put(pe.getKey(), str(pe.getKey(), pe.getValue()));
            } else if (k.equals("network")) {
                for (Map.Entry<String, Object> ne : obj(k, v).entrySet()) {
                    if (ne.getKey().startsWith("_")) continue;
                    String pol = str(ne.getKey(), ne.getValue());
                    if (!pol.equals("allow") && !pol.equals("deny") && !pol.equals("offline")) throw new IllegalArgumentException("profile: network policy '" + pol + "'");
                    if (ne.getKey().equals("default")) p.networkDefault = pol; else p.network.put(ne.getKey().toLowerCase(), pol);
                }
            } else if (k.equals("timeZone")) {
                for (Map.Entry<String, Object> te : obj(k, v).entrySet()) {
                    if (te.getKey().startsWith("_")) continue;
                    if (te.getKey().equals("id")) p.timeZoneId = str("timeZone.id", te.getValue());
                    else if (te.getKey().equals("offsetMillis")) p.timeZoneOffsetMillis = (int) num("timeZone.offsetMillis", te.getValue());
                    else throw new IllegalArgumentException("profile: unknown key timeZone." + te.getKey());
                }
            } else throw new IllegalArgumentException("profile: unknown key '" + k + "'");
        }
        return p;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> obj(String k, Object v) {
        if (!(v instanceof Map)) throw new IllegalArgumentException("profile: '" + k + "' must be an object");
        return (Map<String, Object>) v;
    }

    private static String str(String k, Object v) {
        if (!(v instanceof String)) throw new IllegalArgumentException("profile: '" + k + "' must be a string");
        return (String) v;
    }

    private static long num(String k, Object v) {
        if (!(v instanceof Long)) throw new IllegalArgumentException("profile: '" + k + "' must be an integer");
        return ((Long) v).longValue();
    }

    private static boolean bool(String k, Object v) {
        if (!(v instanceof Boolean)) throw new IllegalArgumentException("profile: '" + k + "' must be a boolean");
        return ((Boolean) v).booleanValue();
    }
}

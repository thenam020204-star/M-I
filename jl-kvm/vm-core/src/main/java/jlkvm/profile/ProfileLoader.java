package jlkvm.profile;

import java.util.Map;
import jlkvm.DeviceProfile;

/**
 * Loads a device profile from JSON text (PLAN T20 minimal schema; T33 extends it and adds a validator). Keys:
 * name, heapBytes, timesliceBytecodes, priorityScheduling, threadStackWords, cldc11, maxTraceFrames,
 * properties{key:string} (System.getProperty), network{scheme|"default": "allow"|"deny"|"offline"},
 * timeZone{id, offsetMillis}, apis[string] (ROM layers), imagesCountInHeap, rms{quotaBytes,storeOverheadBytes,recordOverheadBytes}, media{contentTypes[],protocols[],tone}, nokia{pixelFormat,soundFormats[],concurrentTone,concurrentWav}, keyRepeat{delayMs,intervalMs},
 * display{width,height,canvasWidth,canvasHeight,color,numColors,numAlphaLevels,pointerEvents,pointerMotionEvents,repeatEvents,doubleBuffered},
 * keys{name:int} (names of DeviceProfile.keys). Keys starting with "_" (comments, source tags) are ignored. Unknown keys are an error.
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
            } else if (k.equals("apis")) {
                if (!(v instanceof java.util.List)) throw new IllegalArgumentException("profile: 'apis' must be an array");
                p.apis.clear();
                for (Object a : (java.util.List<Object>) v) p.apis.add(str("apis[]", a));
            } else if (k.equals("imagesCountInHeap")) p.imagesCountInHeap = bool(k, v);
            else if (k.equals("keyRepeat")) {
                for (Map.Entry<String, Object> te : obj(k, v).entrySet()) {
                    if (te.getKey().startsWith("_")) continue;
                    if (te.getKey().equals("delayMs")) p.keyRepeatDelayMs = (int) num("keyRepeat.delayMs", te.getValue());
                    else if (te.getKey().equals("intervalMs")) p.keyRepeatIntervalMs = (int) num("keyRepeat.intervalMs", te.getValue());
                    else throw new IllegalArgumentException("profile: unknown key keyRepeat." + te.getKey());
                }
            } else if (k.equals("keys")) {
                for (Map.Entry<String, Object> te : obj(k, v).entrySet()) if (!te.getKey().startsWith("_")) p.keys.put(te.getKey(), Integer.valueOf((int) num("keys." + te.getKey(), te.getValue())));
            } else if (k.equals("display")) {
                for (Map.Entry<String, Object> te : obj(k, v).entrySet()) {
                    String n = te.getKey(), w = "display." + n;
                    Object x = te.getValue();
                    if (n.startsWith("_")) continue;
                    if (n.equals("width")) p.screenWidth = (int) num(w, x);
                    else if (n.equals("height")) p.screenHeight = (int) num(w, x);
                    else if (n.equals("canvasWidth")) p.canvasWidth = (int) num(w, x);
                    else if (n.equals("canvasHeight")) p.canvasHeight = (int) num(w, x);
                    else if (n.equals("color")) p.isColor = bool(w, x);
                    else if (n.equals("numColors")) p.numColors = (int) num(w, x);
                    else if (n.equals("numAlphaLevels")) p.numAlphaLevels = (int) num(w, x);
                    else if (n.equals("pointerEvents")) p.pointerEvents = bool(w, x);
                    else if (n.equals("pointerMotionEvents")) p.pointerMotionEvents = bool(w, x);
                    else if (n.equals("repeatEvents")) p.repeatEvents = bool(w, x);
                    else if (n.equals("doubleBuffered")) p.doubleBuffered = bool(w, x);
                    else throw new IllegalArgumentException("profile: unknown key " + w);
                }
            } else if (k.equals("rms")) {
                for (Map.Entry<String, Object> te : obj(k, v).entrySet()) {
                    if (te.getKey().startsWith("_")) continue;
                    long n = num("rms." + te.getKey(), te.getValue());
                    if (n < 0 || n > Integer.MAX_VALUE) throw new IllegalArgumentException("profile: rms." + te.getKey() + " out of range");
                    if (te.getKey().equals("quotaBytes")) p.rmsQuotaBytes = (int) n;
                    else if (te.getKey().equals("storeOverheadBytes")) p.rmsStoreOverheadBytes = (int) n;
                    else if (te.getKey().equals("recordOverheadBytes")) p.rmsRecordOverheadBytes = (int) n;
                    else throw new IllegalArgumentException("profile: unknown key rms." + te.getKey());
                }
            } else if (k.equals("media")) {
                for (Map.Entry<String, Object> te : obj(k, v).entrySet()) {
                    String n = te.getKey();
                    if (n.startsWith("_")) continue;
                    if (n.equals("contentTypes") || n.equals("protocols")) {
                        if (!(te.getValue() instanceof java.util.List)) throw new IllegalArgumentException("profile: media." + n + " must be an array");
                        java.util.List<String> dst = n.equals("contentTypes") ? p.mediaContentTypes : p.mediaProtocols;
                        dst.clear();
                        for (Object mo : (java.util.List<Object>) te.getValue()) dst.add(str("media." + n + "[]", mo).trim().toLowerCase());
                    } else if (n.equals("tone")) p.mediaTone = bool("media.tone", te.getValue());
                    else throw new IllegalArgumentException("profile: unknown key media." + n);
                }
            } else if (k.equals("nokia")) {
                for (Map.Entry<String, Object> te : obj(k, v).entrySet()) {
                    String n = te.getKey();
                    if (n.startsWith("_")) continue;
                    if (n.equals("soundFormats")) {
                        if (!(te.getValue() instanceof java.util.List)) throw new IllegalArgumentException("profile: nokia.soundFormats must be an array");
                        p.nokiaSoundFormats.clear();
                        for (Object so : (java.util.List<Object>) te.getValue()) {
                            long f = num("nokia.soundFormats[]", so);
                            if (f != 1 && f != 5) throw new IllegalArgumentException("profile: nokia.soundFormats holds 1 (tone) or 5 (WAV)");
                            p.nokiaSoundFormats.add(Integer.valueOf((int) f));
                        }
                    } else {
                        long x = num("nokia." + n, te.getValue());
                        if (n.equals("pixelFormat")) {
                            if (x != 4444 && x != 444 && x != 565 && x != 555 && x != 1555 && x != 888 && x != 8888) throw new IllegalArgumentException("profile: nokia.pixelFormat must be a TYPE_INT/USHORT format");
                            p.nokiaPixelFormat = (int) x;
                        } else if (n.equals("concurrentTone") || n.equals("concurrentWav")) {
                            if (x < 1 || x > 64) throw new IllegalArgumentException("profile: nokia." + n + " out of range");
                            if (n.equals("concurrentTone")) p.nokiaConcurrentTone = (int) x; else p.nokiaConcurrentWav = (int) x;
                        } else throw new IllegalArgumentException("profile: unknown key nokia." + n);
                    }
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

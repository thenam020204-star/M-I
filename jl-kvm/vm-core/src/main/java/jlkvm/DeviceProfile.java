package jlkvm;

import java.util.HashMap;
import java.util.Map;

/** The device parameters the core needs (PLAN T33 will load these from profiles/*.json). Plain mutable holder. */
public final class DeviceProfile {
    public String name = "generic";
    /** Guest heap size in bytes (D3). */
    public int heapBytes = 1 << 20;
    /** Bytecodes per green-thread time slice (D5). */
    public int timesliceBytecodes = 2000;
    /**
     * true = strict priorities (the highest-priority runnable thread runs; round robin inside a priority). false = every thread
     * is scheduled round robin. UNVERIFIED which one the Nokia KVM does (V2/V4).
     */
    public boolean priorityScheduling = true;
    /** Stack size of each guest thread, in 32-bit words. */
    public int threadStackWords = 8192;
    /** false = CLDC 1.0 (float/double rejected by the verifier). */
    public boolean cldc11 = true;
    /** Maximum number of frames recorded in a Throwable. */
    public int maxTraceFrames = 64;
    /** System properties ({@code System.getProperty}); unknown keys give null. */
    public final Map<String, String> properties = new HashMap<String, String>();

    /**
     * Network policy of Connector.open (T20): per scheme "allow" / "deny" / "offline"; a scheme without entry gets {@link #networkDefault}.
     * Schemes that never use the network ({@link #LOCAL_SCHEMES}) are not subject to it. Real network traffic is out of scope (PLAN).
     */
    public String networkDefault = "offline";
    public final Map<String, String> network = new HashMap<String, String>();
    public static final String[] LOCAL_SCHEMES = {"file", "comm", "resource", "storage"};
    /** Default TimeZone (TimeZone.getDefault): id and raw offset in milliseconds. CLDC itself only requires GMT. */
    public String timeZoneId = "GMT";
    public int timeZoneOffsetMillis = 0;

    // ---- T21-T23 (MIDP layer). Every value is UNVERIFIED until T33 (source tags).
    /** API layers present in the ROM (D10): a class of MIDP exists only when "midp-2.0" is listed. */
    public final java.util.List<String> apis = new java.util.ArrayList<String>(java.util.Arrays.asList("cldc-1.1", "midp-2.0"));
    /** Whether the pixel memory of an Image is charged to the guest heap (PLAN "Ràng buộc"; a device property). */
    public boolean imagesCountInHeap = false;
    /** RMS (T26): bytes a MIDlet suite may keep in all its record stores (getSizeAvailable = quota - used); UNVERIFIED defaults. */
    public int rmsQuotaBytes = 262144;
    /** RMS: bytes a record store occupies besides its records (counted in getSize and against the quota). */
    public int rmsStoreOverheadBytes = 16;
    /** RMS: bytes each record adds to the store size besides its data. */
    public int rmsRecordOverheadBytes = 8;
    /** Full-screen size and the size of a normal (non full-screen) Canvas, in pixels. */
    public int screenWidth = 240, screenHeight = 320, canvasWidth = 240, canvasHeight = 294;
    public boolean isColor = true, pointerEvents = false, pointerMotionEvents = false, repeatEvents = true, doubleBuffered = true;
    public int numColors = 65536, numAlphaLevels = 256;
    /** Key codes by name: softLeft, softRight, select, up, down, left, right, clear, send, end, num0..num9, star, pound. */
    public final Map<String, Integer> keys = new HashMap<String, Integer>();
    /** Key repeat: first repeat after {@code keyRepeatDelayMs}, then every {@code keyRepeatIntervalMs} (host drivers use them). */
    public int keyRepeatDelayMs = 500, keyRepeatIntervalMs = 100;

    public static final int NET_DENY = 0, NET_OFFLINE = 1, NET_ALLOW = 2, NET_LOCAL = 3;

    /** @return NET_DENY / NET_OFFLINE / NET_ALLOW, or NET_LOCAL for a scheme that does not use the network. */
    public int networkPolicy(String scheme) {
        String s = scheme.toLowerCase();
        for (int i = 0; i < LOCAL_SCHEMES.length; i++) if (LOCAL_SCHEMES[i].equals(s)) return NET_LOCAL;
        String v = network.get(s);
        if (v == null) v = networkDefault;
        return v.equals("allow") ? NET_ALLOW : v.equals("deny") ? NET_DENY : NET_OFFLINE;
    }

    public int key(String name) { Integer k = keys.get(name); return k == null ? 0 : k.intValue(); }

    public DeviceProfile() {
        String[][] k = {{"softLeft", "-6"}, {"softRight", "-7"}, {"select", "-5"}, {"up", "-1"}, {"down", "-2"}, {"left", "-3"}, {"right", "-4"},
                {"clear", "-8"}, {"send", "-10"}, {"end", "-11"}, {"star", "42"}, {"pound", "35"}};
        for (int i = 0; i < k.length; i++) keys.put(k[i][0], Integer.valueOf(Integer.parseInt(k[i][1])));
        for (int i = 0; i < 10; i++) keys.put("num" + i, Integer.valueOf(48 + i));
        properties.put("microedition.configuration", "CLDC-1.1");
        properties.put("microedition.profiles", "MIDP-2.0");
        properties.put("microedition.platform", "jl-kvm");
        properties.put("microedition.encoding", "ISO-8859-1");
        properties.put("microedition.locale", "en-US");
    }
}

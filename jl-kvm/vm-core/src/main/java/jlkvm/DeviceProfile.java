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
    /**
     * MMAPI (T27), profile {@code media{}}: content types and protocols {@code Manager} reports and accepts, and whether the tone generator
     * ({@code Manager.playTone}, device://tone) exists. UNVERIFIED defaults (a typical Series 40 list is not known).
     */
    public final java.util.List<String> mediaContentTypes = new java.util.ArrayList<String>(java.util.Arrays.asList(
            "audio/x-tone-seq", "audio/midi", "audio/sp-midi", "audio/x-wav", "audio/mpeg", "audio/amr", "audio/x-rtttl", "text/x-rtttl"));
    public final java.util.List<String> mediaProtocols = new java.util.ArrayList<String>(java.util.Arrays.asList("device", "http", "https", "file", "resource"));
    public boolean mediaTone = true;
    /**
     * Nokia UI API (T28), profile {@code nokia{}} (layer "nokia-ui"): native pixel format of DirectGraphics, sound formats of com.nokia.mid.sound.Sound
     * (1 = tone, 5 = WAV) and the number of concurrent sounds per format. UNVERIFIED defaults.
     */
    public int nokiaPixelFormat = 565;
    public final java.util.List<Integer> nokiaSoundFormats = new java.util.ArrayList<Integer>(java.util.Arrays.asList(1, 5));
    public int nokiaConcurrentTone = 1, nokiaConcurrentWav = 1;
    /** Full-screen size and the size of a normal (non full-screen) Canvas, in pixels. */
    public int screenWidth = 240, screenHeight = 320, canvasWidth = 240, canvasHeight = 294;
    public boolean isColor = true, pointerEvents = false, pointerMotionEvents = false, repeatEvents = true, doubleBuffered = true;
    public int numColors = 65536, numAlphaLevels = 256;
    /** Key codes by name: softLeft, softRight, select, up, down, left, right, clear, send, end, num0..num9, star, pound. */
    public final Map<String, Integer> keys = new HashMap<String, Integer>();
    /** Key repeat: first repeat after {@code keyRepeatDelayMs}, then every {@code keyRepeatIntervalMs} (host drivers use them). */
    public int keyRepeatDelayMs = 500, keyRepeatIntervalMs = 100;

    /**
     * WMA (T32a, layer "wma-1.1"), profile {@code sms{mode}}: "deny" (default; SecurityException at open), "simulate" (nothing leaves the host: the host logs
     * the message and send succeeds) or "offline" (IOException). Governs the schemes sms and cbs. UNVERIFIED defaults (docs/jsr.md).
     */
    public String smsMode = "deny";
    /**
     * JSR-75 FileConnection (layer "jsr75-fc"), profile {@code file{roots[], permission, quotaBytes}}: the virtual roots ("name/"), "readwrite" (default) | "read" | "deny",
     * and the size of each root. UNVERIFIED defaults (docs/jsr.md).
     */
    public final java.util.List<String> fileRoots = new java.util.ArrayList<String>(java.util.Arrays.asList("c:/", "e:/", "root1/"));
    public String filePermission = "readwrite";
    public long fileQuotaBytes = 8L << 20;
    /** JSR-75 PIM (layer "jsr75-pim"), profile {@code pim{permission}}: "allow" (default) | "deny" (SecurityException at openPIMList / listPIMLists). */
    public String pimPermission = "allow";

    // ---- T33 / T30 (device identity and limits). 0 = unlimited / not enforced.
    /** Informational identity: platform family ("s40"), edition ("dp2.0") and the model the numbers come from. */
    public String deviceFamily = "", deviceEdition = "", deviceModel = "";
    /** Largest MIDlet-Jar-Size the device installs (bytes); 0 = no limit. */
    public long jarMaxBytes = 0;
    /** Highest class-file major version accepted for APPLICATION classes (ROM classes are not checked); 0 = the parser limit (49). */
    public int maxClassMajor = 0;
    /** Emulated CPU speed in bytecodes per second for the throttle of T38; 0 = unthrottled. */
    public long cpuBytecodesPerSecond = 0;
    /** Pixel heights of Font.SIZE_SMALL / MEDIUM / LARGE of the device; 0 = the host font set decides (not consumed by the host yet). */
    public int fontSmallHeight = 0, fontMediumHeight = 0, fontLargeHeight = 0;
    /** Nokia JAD attributes (T30) this device acts on, see {@code jlkvm.host.ams.NokiaJad}. */
    public final java.util.List<String> jadHonored = new java.util.ArrayList<String>();

    public static final int NET_DENY = 0, NET_OFFLINE = 1, NET_ALLOW = 2, NET_LOCAL = 3;

    /** @return NET_DENY / NET_OFFLINE / NET_ALLOW, or NET_LOCAL for a scheme that does not use the network. */
    public int networkPolicy(String scheme) {
        String s = scheme.toLowerCase();
        if (s.equals("sms") || s.equals("cbs")) return smsMode.equals("simulate") ? NET_ALLOW : smsMode.equals("offline") ? NET_OFFLINE : NET_DENY;
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

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

    public static final int NET_DENY = 0, NET_OFFLINE = 1, NET_ALLOW = 2, NET_LOCAL = 3;

    /** @return NET_DENY / NET_OFFLINE / NET_ALLOW, or NET_LOCAL for a scheme that does not use the network. */
    public int networkPolicy(String scheme) {
        String s = scheme.toLowerCase();
        for (int i = 0; i < LOCAL_SCHEMES.length; i++) if (LOCAL_SCHEMES[i].equals(s)) return NET_LOCAL;
        String v = network.get(s);
        if (v == null) v = networkDefault;
        return v.equals("allow") ? NET_ALLOW : v.equals("deny") ? NET_DENY : NET_OFFLINE;
    }

    public DeviceProfile() {
        properties.put("microedition.configuration", "CLDC-1.1");
        properties.put("microedition.profiles", "MIDP-2.0");
        properties.put("microedition.platform", "jl-kvm");
        properties.put("microedition.encoding", "ISO-8859-1");
        properties.put("microedition.locale", "en-US");
    }
}

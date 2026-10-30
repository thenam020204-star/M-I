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

    public DeviceProfile() {
        properties.put("microedition.configuration", "CLDC-1.1");
        properties.put("microedition.profiles", "MIDP-2.0");
        properties.put("microedition.platform", "jl-kvm");
        properties.put("microedition.encoding", "ISO-8859-1");
        properties.put("microedition.locale", "en-US");
    }
}

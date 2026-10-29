package jlkvm;

import java.util.HashMap;
import java.util.Map;

/** Run-time switches of the VM (not device properties). */
public final class VmConfig {
    /** D6 strict: only the StackMap attribute is trusted. false = lenient (debug only). */
    public boolean strictVerify = true;
    /** Verify app classes. */
    public boolean verifyApp = true;
    /** Verify ROM classes (PLAN T17 requires true for the real ROM). */
    public boolean verifyRom = true;
    /** Seed for identity hashes (D14). */
    public int hashSeed = 0x1234567;
    /** Debug hooks (D15): class load and exception logging. */
    public boolean debug = false;
    /** JAD/manifest attributes visible through MIDlet.getAppProperty (keys compared case-insensitively). */
    public final Map<String, String> appProperties = new HashMap<String, String>();
}

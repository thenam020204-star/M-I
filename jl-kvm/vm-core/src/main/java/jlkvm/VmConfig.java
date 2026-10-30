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
    /** T13 debug: collect before every N-th allocation (1 = every allocation, 0 = off) to flush out missing roots. */
    public int gcStressInterval = 0;
    /** T13 debug: validate every root and reference during a collection and the whole heap afterwards (slow). */
    public boolean gcVerify = false;
    /**
     * D14 deterministic mode: the clock is virtual (advances {@link #nanosPerBytecode} per executed bytecode and jumps to the next
     * timer when every thread sleeps), so sleep/wait(timeout)/currentTimeMillis give the same schedule on every run.
     */
    public boolean deterministic = false;
    /** Virtual time per bytecode in deterministic mode (ns). */
    public int nanosPerBytecode = 100;
    /** Debug hooks (D15, only effective when jlkvm.debug.DebugFlags.ENABLED): class load / native / GC / deadlock logging. */
    public boolean debug = false;
    /** T16 debug features (each needs DebugFlags.ENABLED): exceptions with guest stack, bytecodes per method, allocation sites by pc, ring of the last N instructions. */
    public boolean logExceptions = false, countBytecodes = false, profileAlloc = false;
    public int traceRing = 0;
    /** D14 input log files (deterministic mode): record to / replay from. */
    public String recordInput, replayInput;
    /** JAD/manifest attributes visible through MIDlet.getAppProperty (keys compared case-insensitively). */
    public final Map<String, String> appProperties = new HashMap<String, String>();
}

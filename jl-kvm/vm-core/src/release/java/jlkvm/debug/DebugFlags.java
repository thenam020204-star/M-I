package jlkvm.debug;

/**
 * D15: compile-time switch of every debug hook (class-load/exception/GC logs, bytecode counters, allocation profiler, trace ring,
 * deadlock report, input record/replay). A hook is written {@code if (DebugFlags.ENABLED && ...)}; with the constant false javac
 * drops the whole statement, so a release build contains no hook code at all in the hot paths.
 * This is the RELEASE version of the file. The release build compiles {@code src/release/java/jlkvm/debug/DebugFlags.java}
 * (ENABLED = false) instead of it: Gradle {@code -Pjlkvm.release=true}, or JLKVM_RELEASE=1 for tools/difftest/oracles/run_jlkvm.sh.
 */
public final class DebugFlags {
    private DebugFlags() {}
    public static final boolean ENABLED = false;
}

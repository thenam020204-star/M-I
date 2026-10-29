package jlkvm;

/** Allows replacing the Java core by a C++ core later (PLAN D2). */
public interface ExecutionEngine {
    int RUNNING = 0, IDLE_UNTIL = 1, EXITED = 2;

    /** Runs at most {@code maxBytecodes} bytecodes (natives may be charged extra). @return RUNNING / IDLE_UNTIL / EXITED. */
    int runSlice(int maxBytecodes);

    /** Valid after {@link #runSlice} returned IDLE_UNTIL: host nano time at which something becomes runnable (Long.MAX_VALUE: only an event can). */
    long idleUntilNanos();
}

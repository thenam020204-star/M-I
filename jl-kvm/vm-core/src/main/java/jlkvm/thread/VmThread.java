package jlkvm.thread;

import jlkvm.link.RMethod;
import jlkvm.natives.Blocker;

/** One green thread (D5). The guest stack is an int[]; see {@code jlkvm.interp.Frames} for the frame layout. */
public final class VmThread {
    public static final int RUNNABLE = 0, RUNNING = 1, SLEEPING = 2, WAITING = 3, BLOCKED_ENTER = 4, BLOCKED_NATIVE = 5, DEAD = 6;

    public final int id;
    public final int[] stack;
    /** Registers of the top frame while the thread is not running. */
    public int fp = -1, sp, pc;
    public RMethod method;
    public int state = RUNNABLE;
    public long wakeAt;                // host nano time, 0 = none
    public Monitors.Monitor waitMon;   // monitor being waited for / entered
    public int waitRef;
    public int pendingCount;           // monitor recursion count to restore on (re)acquisition
    public boolean interrupted;
    public String pendingThrow;        // guest class thrown when the thread resumes
    public String pendingMsg;          // its message
    public int excPc;                  // pc used for that throw
    /** Class to initialise before the first instruction runs (main thread of {@link jlkvm.Vm#launch}). */
    public jlkvm.link.ClassInfo initFirst;
    /** Native method being executed (its arguments are still on the operand stack; GC needs their types). */
    public RMethod inNative;
    public int threadObj;              // guest java/lang/Thread object (GC root)
    public Blocker blocker;
    public char blockerRet;
    public int priority = 5;
    /** References locked by monitorenter (structured locking, see Frames.LOCKS). GC roots. */
    public int[] locks = new int[8];
    public int lockTop;
    /** Thread being joined (state WAITING without a monitor) and the threads joining this one. */
    public VmThread joinTarget;
    public final java.util.ArrayList<VmThread> joiners = new java.util.ArrayList<VmThread>();

    public VmThread(int id, int stackWords) {
        this.id = id;
        this.stack = new int[stackWords];
    }
}

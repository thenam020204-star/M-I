package jlkvm.interp;

/**
 * Frame layout inside {@code VmThread.stack} (int[]), used by the interpreter, the stack-trace code and the collector (T13):
 * <pre>
 *   fp                 locals[0 .. maxLocals)           (the callee's arguments are the top words of the caller's operand stack)
 *   fp + maxLocals     header, HDR words (below)
 *   fp + maxLocals + HDR   operand stack (grows up) up to maxStack words
 * </pre>
 * The header of a frame describes how to return to its caller: the caller's frame pointer, method id and the pc of the
 * invoke instruction (or of the instruction to re-execute for class-init frames). The pc of the top frame is
 * {@code VmThread.pc}; the pc of a frame that is not on top is {@code PREV_PC} of the frame above it. GC reference maps:
 * {@code TypeMaps.at} for the top frame, {@code TypeMaps.atCall} for the others. {@code SYNC} is a reference (monitor
 * object of a synchronized method, 0 if none); {@code EXTRA} is a reference when {@code FLAGS & F_EXTRA_REF}.
 * {@code LOCKS} = {@code VmThread.lockTop} at frame entry: the monitorenter locks of the frame are the entries of
 * {@code VmThread.locks} above it (structured locking: a frame that returns holding one gets IllegalMonitorStateException).
 */
public final class Frames {
    private Frames() {}
    public static final int PREV_FP = 0, PREV_MID = 1, PREV_PC = 2, SYNC = 3, FLAGS = 4, EXTRA = 5, LOCKS = 6, HDR = 7;
    /** Frame runs a {@code <clinit>}: EXTRA = class id; on return the class becomes INITIALIZED and the caller re-executes. */
    public static final int F_INIT = 1;
    /** On return push EXTRA (a reference) as the result of the native call that started this frame (Class.newInstance). */
    public static final int F_PUSH_REF = 2;
    public static final int F_EXTRA_REF = 4;
}

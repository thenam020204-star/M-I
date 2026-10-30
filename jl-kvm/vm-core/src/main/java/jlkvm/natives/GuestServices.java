package jlkvm.natives;

/** Helpers for natives that need to look at guest objects (strings, class names). Implemented by the VM. */
public interface GuestServices {
    /** @return the Java string of a guest java/lang/String, null for null. */
    String string(int ref);
    /** Allocates a guest String (null for null); may return 0 when the heap is exhausted. */
    int newString(String s);
    /** Allocates a guest byte[]. */
    int newByteArray(byte[] b, int off, int len);
    String className(int objRef);
    /** Takes the next input event posted with Vm.postEvent: {type, a, b, c}, null when none (vm-thread only). */
    int[] pollEvent();
    /** Charges {@code bytecodes} to the running time slice (long natives must not starve other threads). */
    void charge(int bytecodes);
    /** Host diagnostics sink (HostPlatform.log): level 1 info, 2 warning, 3 error. game-errors.log keeps tags oom / link / resource / media / vm (PLAN T35b). */
    void log(int level, String tag, String msg);
}

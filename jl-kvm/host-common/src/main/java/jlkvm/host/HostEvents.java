package jlkvm.host;

/**
 * Event types of {@code Vm.postEvent(type, a, b, c)} understood by the guest EventLoop (jlkvm.internal.EventLoop; same numbers).
 * postEvent may be called from any thread - it is the only entry from the host into the VM (PLAN D13).
 */
public final class HostEvents {
    private HostEvents() {}
    public static final int KEY_PRESSED = 1, KEY_RELEASED = 2, KEY_REPEATED = 3;        // a = key code
    public static final int POINTER_PRESSED = 4, POINTER_DRAGGED = 5, POINTER_RELEASED = 6;   // a = x, b = y
    public static final int DISPLAY_SIZE = 7;                                              // a = width, b = height
    public static final int REPAINT_ALL = 8;
    /** Life cycle (Android onPause / onResume / back): the AMS calls pauseApp / startApp / destroyApp. DESTROY: a = 1 unconditional. */
    public static final int PAUSE = 20, RESUME = 21, DESTROY = 22;
}

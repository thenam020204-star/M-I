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
    /**
     * High-level UI actions (docs/midp.md section 6); ids are those of the description the guest sent last:
     * UI_COMMAND a = screen id, b = command id; UI_ITEM_COMMAND a = item id, b = command id; UI_CHOICE a = item id (or list screen id),
     * b = element, c = 1 selected / 0 cleared; UI_TEXT a = item id (or TextBox screen id), the text is read with ScreenHost.text;
     * UI_GAUGE a = item id, b = value; UI_DATE a = item id, b/c = high/low int of the millis; UI_DATE_CLEAR a = item id;
     * UI_CUSTOM a = item id, b = CustomItem event code (KEY_PRESS 4, KEY_RELEASE 8, KEY_REPEAT 16, POINTER_PRESS 32, POINTER_RELEASE 64,
     * POINTER_DRAG 128, TRAVERSE_HORIZONTAL 1, TRAVERSE_VERTICAL 2), c = key code / (x << 16 | y & 0xFFFF) / direction.
     */
    public static final int UI_COMMAND = 30, UI_ITEM_COMMAND = 31, UI_CHOICE = 32, UI_TEXT = 33, UI_GAUGE = 34, UI_DATE = 35, UI_DATE_CLEAR = 36,
            UI_CUSTOM = 37;
    public static final int PAUSE = 20, RESUME = 21, DESTROY = 22;
}

package jlkvm.host.ui;

/**
 * The high-level lcdui presentation of a host (PLAN T24): Form / List / TextBox / Alert as widgets (host-android: real Android views, M6;
 * host-desktop: the headless scripted UI). Called on the vm-thread only; a host reports user actions with {@code Vm.postEvent}
 * ({@link jlkvm.host.HostEvents} UI_*), never by calling the guest (D13). The guest owns the model: every change is re-sent as a whole.
 */
public interface ScreenHost {
    /** The guest made {@code screen} current (or changed it): show it. A CANVAS screen means "show the canvas surface" (pixels come with PlatformServices.present). */
    void screen(ScreenModel screen);

    /** The text the user last typed into the TextField (item id) / TextBox (screen id); read by the guest after a UI_TEXT event. Any thread. */
    String text(int id);

    /** Answer of CustomItem.traverse. */
    void traverseResult(int itemId, int dir, boolean inside, int x, int y, int w, int h);

    /** A host without high-level UI support (everything is dropped). */
    ScreenHost NONE = new ScreenHost() {
        public void screen(ScreenModel s) { }
        public String text(int id) { return null; }
        public void traverseResult(int i, int d, boolean in, int x, int y, int w, int h) { }
    };
}

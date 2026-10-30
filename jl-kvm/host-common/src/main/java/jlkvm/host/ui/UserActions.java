package jlkvm.host.ui;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jlkvm.host.HostEvents;

/**
 * What a widget host does when the user acts (PLAN T35c, docs/midp.md section 6): every action becomes one {@code HostEvents.UI_*}
 * event for the guest event loop (D13 - no guest code runs on the UI thread), and typed text goes to a mailbox the guest reads with
 * {@link ScreenHost#text} after the UI_TEXT event (latest wins). Thread-safe; the UI thread calls it, {@code sink} must only queue.
 */
public final class UserActions {
    /** Where events go: {@code VmRunner.post} on Android; it only queues and wakes the vm-thread. */
    public interface Sink {
        void post(int type, int a, int b, int c);
    }

    private final Sink sink;
    private final Map<Integer, String> texts = new ConcurrentHashMap<Integer, String>();

    public UserActions(Sink sink) { this.sink = sink; }

    public void command(int screenId, int commandId) { sink.post(HostEvents.UI_COMMAND, screenId, commandId, 0); }

    public void itemCommand(int itemId, int commandId) { sink.post(HostEvents.UI_ITEM_COMMAND, itemId, commandId, 0); }

    /** @param id the item (Form) or the List screen; @param index element; @param selected the new state of that element */
    public void choice(int id, int index, boolean selected) { sink.post(HostEvents.UI_CHOICE, id, index, selected ? 1 : 0); }

    /** The user typed {@code s} into the TextField (item id) / TextBox (screen id). */
    public void text(int id, String s) {
        texts.put(id, s == null ? "" : s);
        sink.post(HostEvents.UI_TEXT, id, 0, 0);
    }

    /** ScreenHost.text: the last text the user typed into {@code id}, null when never. */
    public String text(int id) { return texts.get(id); }

    public void gauge(int itemId, int value) { sink.post(HostEvents.UI_GAUGE, itemId, value, 0); }

    public void date(int itemId, long millis) { sink.post(HostEvents.UI_DATE, itemId, (int) (millis >>> 32), (int) millis); }

    public void dateCleared(int itemId) { sink.post(HostEvents.UI_DATE_CLEAR, itemId, 0, 0); }

    /** CustomItem pointer / key event: {@code code} is POINTER_PRESS 32 / RELEASE 64 / DRAG 128 with {@link #xy}, or a key event with the key code. */
    public void custom(int itemId, int code, int param) { sink.post(HostEvents.UI_CUSTOM, itemId, code, param); }

    public static int xy(int x, int y) { return (x << 16) | (y & 0xFFFF); }
}

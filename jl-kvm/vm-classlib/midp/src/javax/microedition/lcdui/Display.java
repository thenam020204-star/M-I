/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import javax.microedition.midlet.MIDlet;
import jlkvm.internal.Ams;
import jlkvm.internal.Device;
import jlkvm.internal.EventLoop;
import jlkvm.internal.Ui;

public class Display {
    public static final int LIST_ELEMENT = 1, CHOICE_GROUP_ELEMENT = 2, ALERT = 3;
    public static final int COLOR_BACKGROUND = 0, COLOR_FOREGROUND = 1, COLOR_HIGHLIGHTED_BACKGROUND = 2, COLOR_HIGHLIGHTED_FOREGROUND = 3,
            COLOR_BORDER = 4, COLOR_HIGHLIGHTED_BORDER = 5;

    static Display instance;
    /** Visible area in pixels: the whole screen, and the part available to a normal (not full-screen) Canvas / Screen. */
    static int screenW, screenH, canvasW, canvasH;
    volatile Displayable current;
    /** false while the application is in the background (Android onPause). */
    volatile boolean foreground = true;
    private Displayable afterAlert;
    private int alertGen;

    static {
        screenW = Device.info0(Device.SCREEN_W);
        screenH = Device.info0(Device.SCREEN_H);
        canvasW = Device.info0(Device.CANVAS_W);
        canvasH = Device.info0(Device.CANVAS_H);
    }

    private Display() {
        Dispatcher d = new Dispatcher(this);
        EventLoop.setHandler(d);
        Ams.setUi(d);
    }

    public static synchronized Display getDisplay(MIDlet m) {
        if (m == null) throw new NullPointerException();
        if (instance == null) instance = new Display();
        return instance;
    }

    public int numColors() {
        return Device.info0(Device.NUM_COLORS);
    }

    public int numAlphaLevels() {
        return Device.info0(Device.NUM_ALPHA);
    }

    public boolean isColor() {
        return Device.info0(Device.IS_COLOR) != 0;
    }

    /** UNVERIFIED values (device look and feel; profile data arrives with T33). */
    public int getColor(int colorSpecifier) {
        switch (colorSpecifier) {
            case COLOR_BACKGROUND: return 0xFFFFFF;
            case COLOR_FOREGROUND: return 0x000000;
            case COLOR_HIGHLIGHTED_BACKGROUND: return 0x000080;
            case COLOR_HIGHLIGHTED_FOREGROUND: return 0xFFFFFF;
            case COLOR_BORDER: return 0x000000;
            case COLOR_HIGHLIGHTED_BORDER: return 0x000080;
            default: throw new IllegalArgumentException("colorSpecifier");
        }
    }

    public int getBorderStyle(boolean highlighted) {
        return highlighted ? Graphics.SOLID : Graphics.DOTTED;
    }

    public int getBestImageWidth(int imageType) {
        switch (imageType) {
            case LIST_ELEMENT: case CHOICE_GROUP_ELEMENT: return 16;
            case ALERT: return canvasW;
            default: throw new IllegalArgumentException("imageType");
        }
    }

    public int getBestImageHeight(int imageType) {
        switch (imageType) {
            case LIST_ELEMENT: case CHOICE_GROUP_ELEMENT: return 16;
            case ALERT: return canvasH / 2;
            default: throw new IllegalArgumentException("imageType");
        }
    }

    public Displayable getCurrent() {
        return current;
    }

    /** The value of getCurrent() changes at once; the display callbacks (hideNotify / showNotify / first paint) follow on the event thread. */
    public void setCurrent(Displayable nextDisplayable) {
        if (nextDisplayable instanceof Alert) {
            // an Alert shown with setCurrent(Displayable) returns to the previous displayable (MIDP 2.0)
            Displayable prev = current;
            setCurrent((Alert) nextDisplayable, prev instanceof Alert ? null : prev);
            return;
        }
        switchTo(nextDisplayable);
    }

    public void setCurrent(final Alert alert, Displayable nextDisplayable) {
        if (alert == null) throw new NullPointerException();
        if (nextDisplayable instanceof Alert) throw new IllegalArgumentException("nextDisplayable is an Alert");
        if (nextDisplayable == null && current == null) throw new NullPointerException();
        final int gen;
        synchronized (this) {
            afterAlert = nextDisplayable != null ? nextDisplayable : current instanceof Alert ? afterAlert : current;
            gen = ++alertGen;
        }
        switchTo(alert);
        final int ms = alert.getTimeout();
        if (!alert.modal()) {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(ms);
                    } catch (InterruptedException e) {
                    }
                    if (alertGen == gen && !alert.modal()) dismissAlert(alert);
                }
            }.start();
        }
    }

    void dismissAlert(Alert a) {
        Displayable next;
        synchronized (this) {
            if (current != a) return;
            next = afterAlert;
            alertGen++;
        }
        if (next != null) switchTo(next);
    }

    private void switchTo(final Displayable next) {
        final Displayable old;
        synchronized (this) {
            old = current;
            if (old == next) return;
            current = next;
        }
        EventLoop.post(new Runnable() {
            public void run() {
                if (old != null && old.visible) old.onHide();
                if (next != null && foreground && current == next && !next.visible) next.onShow();
            }
        });
    }

    public void setCurrentItem(Item item) {
        if (item == null) throw new NullPointerException();
        if (!(item.owner instanceof Form)) throw new IllegalStateException("item is not owned by a Form");
        Form f = (Form) item.owner;
        f.focus = item;
        f.touch();
        setCurrent(f);
    }

    /** A Ticker (shared by displayables) changed: re-send the current displayable. */
    static void touchCurrent() {
        Display d = instance;
        Displayable c = d == null ? null : d.current;
        if (c != null) c.touch();
    }

    /** Event thread: an event of the host screen protocol (a = screen / item id). Stale events (other screen) are ignored. */
    void uiEvent(int type, int a, int b, int c) {
        Displayable d = current;
        if (d == null || !d.visible || !foreground) return;
        Item it;
        switch (type) {
            case EventLoop.UI_COMMAND:
                if (d.sid == a) d.onUiCommand(b);
                break;
            case EventLoop.UI_ITEM_COMMAND:
                it = d.findItem(a);
                if (it != null) {
                    Command cmd = it.findCommand(b);
                    if (cmd != null) it.fire(cmd);
                }
                break;
            case EventLoop.UI_CHOICE:
                if (d.sid == a && d instanceof List) {
                    ((List) d).onUiChoice(b, c != 0);
                } else if ((it = d.findItem(a)) instanceof ChoiceGroup) {
                    ((ChoiceGroup) it).hostSelect(b, c != 0);
                    d.itemChanged(it);
                }
                break;
            case EventLoop.UI_TEXT: {
                String t = Ui.text0(a);
                if (d.sid == a && d instanceof TextBox) {
                    ((TextBox) d).onUiText(t);
                } else if ((it = d.findItem(a)) instanceof TextField && ((TextField) it).m.hostSet(t)) {
                    d.itemChanged(it);
                }
                break;
            }
            case EventLoop.UI_GAUGE:
                if ((it = d.findItem(a)) instanceof Gauge && ((Gauge) it).isInteractive()) {
                    ((Gauge) it).hostValue(b);
                    d.itemChanged(it);
                }
                break;
            case EventLoop.UI_DATE:
            case EventLoop.UI_DATE_CLEAR:
                if ((it = d.findItem(a)) instanceof DateField) {
                    ((DateField) it).hostSet(type == EventLoop.UI_DATE, ((long) b << 32) | (c & 0xFFFFFFFFL));
                    d.itemChanged(it);
                }
                break;
            case EventLoop.UI_CUSTOM:
                if ((it = d.findItem(a)) instanceof CustomItem) ((CustomItem) it).onUiEvent(b, c);
                break;
            default:
                break;
        }
    }

    public void callSerially(Runnable r) {
        if (r == null) throw new NullPointerException();
        EventLoop.post(r);
    }

    public boolean flashBacklight(int duration) {
        if (duration < 0) throw new IllegalArgumentException("duration");
        return Device.flash0(duration);
    }

    public boolean vibrate(int duration) {
        if (duration < 0) throw new IllegalArgumentException("duration");
        return Device.vibrate0(duration);
    }

    /** Event thread: shows the frame of {@code img} (the canvas back buffer) on the screen. */
    static void present(Image img) {
        present0(img.peer);
    }

    private static native void present0(int imagePeer);
}

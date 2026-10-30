/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import javax.microedition.midlet.MIDlet;
import jlkvm.internal.Ams;
import jlkvm.internal.Device;
import jlkvm.internal.EventLoop;

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
        synchronized (this) {
            afterAlert = nextDisplayable != null ? nextDisplayable : current;
        }
        switchTo(alert);
        int t = alert.getTimeout();
        if (t != Alert.FOREVER && alert.implicitDismiss()) {
            final int ms = t;
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(ms);
                    } catch (InterruptedException e) {
                    }
                    dismissAlert(alert);
                }
            }.start();
        }
    }

    void dismissAlert(Alert a) {
        Displayable next;
        synchronized (this) {
            if (current != a) return;
            next = afterAlert;
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
        if (item.owner == null) throw new IllegalStateException("item is not owned by a Form");
        setCurrent(item.owner);
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

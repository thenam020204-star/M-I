/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import jlkvm.internal.Device;
import jlkvm.internal.EventHandler;
import jlkvm.internal.EventLoop;

/** Package-private (not API): the event thread side of the display (D13) - routes host events to the current Displayable. */
final class Dispatcher implements EventHandler {
    private final Display display;
    /** Key whose press was consumed as a command, so that its release / repeat are swallowed too. */
    private int consumedKey = Integer.MIN_VALUE;

    Dispatcher(Display d) {
        display = d;
    }

    public void handle(int type, int a, int b, int c) {
        Displayable d = display.current;
        switch (type) {
            case EventLoop.KEY_PRESSED:
            case EventLoop.KEY_RELEASED:
            case EventLoop.KEY_REPEATED:
                key(d, type, a);
                break;
            case EventLoop.POINTER_PRESSED:
            case EventLoop.POINTER_DRAGGED:
            case EventLoop.POINTER_RELEASED:
                if (d instanceof Canvas && d.visible && Device.info0(Device.POINTER) != 0) ((Canvas) d).onPointer(type, a, b);
                break;
            case EventLoop.DISPLAY_SIZE:
                Display.screenW = a;
                Display.screenH = b;
                Display.canvasW = a;
                Display.canvasH = b - (Device.info0(Device.SCREEN_H) - Device.info0(Device.CANVAS_H));
                if (d instanceof Canvas) ((Canvas) d).resized();
                break;
            case EventLoop.REPAINT_ALL:
                if (d instanceof Canvas) ((Canvas) d).repaint();
                break;
            case EventLoop.UI_HIDE:
                display.foreground = false;
                if (d != null && d.visible) d.onHide();
                break;
            case EventLoop.UI_SHOW:
                display.foreground = true;
                if (d != null && !d.visible) d.onShow();
                break;
            default:
                break;
        }
    }

    private void key(Displayable d, int type, int code) {
        if (d == null || !display.foreground || !d.visible) return;
        boolean left = code == Device.key0(Device.KEY_SOFT_LEFT), right = code == Device.key0(Device.KEY_SOFT_RIGHT);
        if (d instanceof Alert) {
            Alert al = (Alert) d;
            if (type == EventLoop.KEY_PRESSED && (left || right || code == Device.key0(Device.KEY_SELECT))) {
                Command cmd = al.commandForSoftKey(left || code == Device.key0(Device.KEY_SELECT));
                if (cmd != null && !al.implicitDismiss()) al.fire(cmd);
                else display.dismissAlert(al);
            }
            return;
        }
        if (d instanceof Canvas && (left || right) && !((Canvas) d).fullScreen && d.commandCount() > 0) {
            if (type == EventLoop.KEY_PRESSED) {
                Command cmd = d.commandForSoftKey(left);
                if (cmd != null) {
                    consumedKey = code;
                    d.fire(cmd);
                    return;
                }
            } else if (consumedKey == code) {
                if (type == EventLoop.KEY_RELEASED) consumedKey = Integer.MIN_VALUE;
                return;
            }
        }
        d.onKey(type, code);
    }
}

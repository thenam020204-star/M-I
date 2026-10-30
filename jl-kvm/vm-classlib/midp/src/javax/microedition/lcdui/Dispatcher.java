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
                if (type >= EventLoop.UI_COMMAND && type <= EventLoop.UI_CUSTOM) display.uiEvent(type, a, b, c);
                break;
        }
    }

    private void key(Displayable d, int type, int code) {
        if (d == null || !display.foreground || !d.visible) return;
        boolean left = code == Device.key0(Device.KEY_SOFT_LEFT), right = code == Device.key0(Device.KEY_SOFT_RIGHT);
        if (d instanceof Canvas && !((Canvas) d).fullScreen && d.commandCount() > 0) {
            Canvas cv = (Canvas) d;
            int n = d.commandCount();
            boolean pressed = type == EventLoop.KEY_PRESSED;
            if (cv.menuOpen) {
                // the menu (more than two commands) owns the keys until it is closed
                if (pressed) consumedKey = code;
                if (pressed || (type == EventLoop.KEY_REPEATED && (code == Device.key0(Device.KEY_UP) || code == Device.key0(Device.KEY_DOWN)))) {
                    Command chosen = cv.menuKey(code);
                    if (chosen != null) d.fire(chosen);
                }
                else if (consumedKey == code && type == EventLoop.KEY_RELEASED) consumedKey = Integer.MIN_VALUE;
                return;
            }
            if (left || right) {
                if (pressed) {
                    Command cmd;
                    if (n > 2) {
                        cmd = left ? null : d.backCommand();
                        if (left) cv.openMenu();
                    } else {
                        cmd = d.commandForSoftKey(left);
                    }
                    consumedKey = code;
                    if (cmd != null) d.fire(cmd);
                    return;
                } else if (consumedKey == code) {
                    if (type == EventLoop.KEY_RELEASED) consumedKey = Integer.MIN_VALUE;
                    return;
                }
            }
        }
        d.onKey(type, code);
    }
}

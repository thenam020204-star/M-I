/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package jlkvm.internal;

/** ROM-private: device parameters of the running profile (all natives are answered by the host from DeviceProfile). */
public final class Device {
    private Device() {
    }

    public static final int SCREEN_W = 0, SCREEN_H = 1, CANVAS_W = 2, CANVAS_H = 3, IS_COLOR = 4, NUM_COLORS = 5, NUM_ALPHA = 6,
            POINTER = 7, POINTER_MOTION = 8, REPEAT = 9, DOUBLE_BUFFERED = 10, IMAGES_IN_HEAP = 11;
    public static final int KEY_SOFT_LEFT = 0, KEY_SOFT_RIGHT = 1, KEY_SELECT = 2, KEY_UP = 3, KEY_DOWN = 4, KEY_LEFT = 5, KEY_RIGHT = 6,
            KEY_CLEAR = 7, KEY_SEND = 8, KEY_END = 9;

    public static native int info0(int what);

    public static native int key0(int which);

    /** @return true when the device has a vibrator / backlight (the host records the request). */
    public static native boolean vibrate0(int millis);

    public static native boolean flash0(int millis);
}

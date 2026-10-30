package jlkvm.host;

import jlkvm.host.ui.Surface;

/** What the MIDP layer asks of the platform besides drawing primitives (implemented by host-desktop and host-android). Called on the vm-thread only. */
public interface PlatformServices {
    /** MIDlet.platformRequest: 0 handled, 1 the MIDlet must exit before the request is honoured, -1 not supported (ConnectionNotFoundException). */
    int platformRequest(String url);
    /** MIDlet.checkPermission: 1 allowed, 0 denied, -1 unknown. */
    int checkPermission(String permission);
    boolean vibrate(int millis);
    boolean flashBacklight(int millis);
    /** A Canvas finished a paint cycle: {@code frame} holds the complete screen image (the canvas back buffer). */
    void present(Surface frame);
}

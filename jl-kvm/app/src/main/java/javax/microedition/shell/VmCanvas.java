/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package javax.microedition.shell;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import jlkvm.host.HostEvents;
import jlkvm.host.android.AndroidServices;
import jlkvm.host.desktop.VmRunner;

/**
 * The window of the VM (PLAN T35): JL-Mod's own {@link Canvas} is kept as the presentation layer - its SurfaceView / GLES view, shader
 * and scaling settings, skin, virtual keyboard overlay, soft keys, FPS limit and screenshot all work unchanged - but it no longer
 * belongs to a MIDlet running in the app process. The game is a guest of the VM: its frames arrive through {@link #frame}, and what JL-Mod
 * dispatches to a Canvas (keys, pointer, size) is forwarded to the guest as host events. The JL-Mod event queue thread calls
 * {@code keyPressed...}; they only queue an event ({@code VmRunner}, D13).
 */
final class VmCanvas extends Canvas implements AndroidServices.FrameSink {
    private final VmSession session;
    private volatile VmRunner runner;
    private final Object frameLock = new Object();
    private int[] pixels;
    private int fw, fh;
    private int lastW = -1, lastH = -1;

    VmCanvas(VmSession session) {
        super();
        this.session = session;
    }

    void attach(VmRunner r) {
        runner = r;
    }

    // ---------------------------------------------------------------- guest -> screen

    /** vm-thread: a complete frame of the guest Canvas. */
    @Override
    public void frame(int[] argb, int w, int h) {
        synchronized (frameLock) {
            if (pixels == null || pixels.length != argb.length) pixels = new int[argb.length];
            System.arraycopy(argb, 0, pixels, 0, argb.length);
            fw = w;
            fh = h;
        }
        repaint();
    }

    @Override
    protected void paint(Graphics g) {
        synchronized (frameLock) {
            if (pixels == null) return;
            g.drawRGB(pixels, 0, fw, 0, 0, fw, fh, false);
        }
    }

    // ---------------------------------------------------------------- screen -> guest

    /** The size of the virtual screen changed (rotation, padding, soft keys): the guest Canvas gets the new size. */
    @Override
    public void updateSize() {
        super.updateSize();
        VmRunner r = runner;
        if (r != null && (width != lastW || height != lastH)) {
            lastW = width;
            lastH = height;
            r.displaySize(width, height);
        }
    }

    @Override
    protected void keyPressed(int keyCode) {
        VmRunner r = runner;
        if (r != null) r.keyPressed(keyCode);
    }

    @Override
    protected void keyRepeated(int keyCode) {
        VmRunner r = runner;
        if (r != null) r.keyRepeated(keyCode);
    }

    @Override
    protected void keyReleased(int keyCode) {
        VmRunner r = runner;
        if (r != null) r.keyReleased(keyCode);
    }

    @Override
    public void pointerPressed(int pointer, int x, int y) {
        VmRunner r = runner;
        if (r != null && pointer == 0) r.pointer(HostEvents.POINTER_PRESSED, x, y);
    }

    @Override
    public void pointerDragged(int pointer, int x, int y) {
        VmRunner r = runner;
        if (r != null && pointer == 0) r.pointer(HostEvents.POINTER_DRAGGED, x, y);
    }

    @Override
    public void pointerReleased(int pointer, int x, int y) {
        VmRunner r = runner;
        if (r != null && pointer == 0) r.pointer(HostEvents.POINTER_RELEASED, x, y);
    }
}

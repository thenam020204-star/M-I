/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package jlkvm.internal;

import java.util.Vector;

/**
 * The guest event thread (PLAN D13): the only thread that runs paint / key / pointer / commandAction / callSerially / showNotify /
 * hideNotify / sizeChanged and the MIDlet pause / resume / destroy callbacks. Two sources feed it, in a fair order (one guest
 * job, then one host event, per round): jobs posted by guest threads ({@link #post}) and input events the host put into
 * Vm.postEvent (taken by the native {@link #next0}, which blocks only this guest thread). The host never calls guest code.
 */
public final class EventLoop implements Runnable {
    public static final int KEY_PRESSED = 1, KEY_RELEASED = 2, KEY_REPEATED = 3, POINTER_PRESSED = 4, POINTER_DRAGGED = 5,
            POINTER_RELEASED = 6, DISPLAY_SIZE = 7, REPAINT_ALL = 8;
    /** Life cycle requests of the host (Android onPause / onResume / back key): a = unconditional for DESTROY. */
    public static final int PAUSE = 20, RESUME = 21, DESTROY = 22;
    /** Screen protocol events of the host (docs/midp.md section 6): a = screen / item id, b, c = parameters. */
    public static final int UI_COMMAND = 30, UI_ITEM_COMMAND = 31, UI_CHOICE = 32, UI_TEXT = 33, UI_GAUGE = 34, UI_DATE = 35, UI_DATE_CLEAR = 36,
            UI_CUSTOM = 37;
    /** Posted internally to the UI handler (the display lost / regained the foreground). */
    public static final int UI_HIDE = 100, UI_SHOW = 101;

    private static EventLoop instance;
    private static EventHandler handler;
    private final Vector jobs = new Vector();
    private Thread thread;

    private EventLoop() {
    }

    /** Starts the event thread (once). */
    public static synchronized void start() {
        if (instance != null) return;
        instance = new EventLoop();
        instance.thread = new Thread(instance, "event");
        instance.thread.start();
    }

    public static synchronized void setHandler(EventHandler h) {
        handler = h;
    }

    public static boolean isEventThread() {
        EventLoop e = instance;
        return e != null && Thread.currentThread() == e.thread;
    }

    /** Any guest thread: run {@code r} on the event thread, in FIFO order with the other jobs. */
    public static void post(Runnable r) {
        EventLoop e;
        synchronized (EventLoop.class) {
            if (instance == null) start();
            e = instance;
        }
        synchronized (e.jobs) {
            e.jobs.addElement(r);
        }
        wake0();
    }

    private Runnable take() {
        synchronized (jobs) {
            if (jobs.isEmpty()) return null;
            Runnable r = (Runnable) jobs.elementAt(0);
            jobs.removeElementAt(0);
            return r;
        }
    }

    private boolean hasJobs() {
        synchronized (jobs) {
            return !jobs.isEmpty();
        }
    }

    public void run() {
        int[] ev = new int[4];
        for (;;) {
            Runnable r = take();
            if (r != null) {
                try {
                    r.run();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            boolean block = r == null && !hasJobs();
            if (next0(ev, block) == 1) {
                EventHandler h = handler;
                try {
                    if (ev[0] >= PAUSE && ev[0] <= DESTROY) Ams.lifecycle(ev[0], ev[1]);
                    else if (h != null) h.handle(ev[0], ev[1], ev[2], ev[3]);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    /** Marks guest work pending so that a blocked {@link #next0} returns 0. */
    private static native void wake0();

    /**
     * Takes the next host event into ev = {type, a, b, c}. @return 1 when ev was filled, 0 when no host event is queued (with
     * block = true: only after a guest job was posted).
     */
    private static native int next0(int[] ev, boolean block);
}

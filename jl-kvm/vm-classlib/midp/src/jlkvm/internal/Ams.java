/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package jlkvm.internal;

import javax.microedition.io.ConnectionNotFoundException;

/**
 * Application management (T21): drives the life cycle of the one MIDlet of the run. States as in MIDP 2.0 (Paused after
 * construction, Active, Destroyed). startApp of the first start runs on the thread that called {@link #run}; the later
 * callbacks (pause, resume, destroy) run on the event thread (D13), after the display callbacks hideNotify / showNotify of the
 * same transition: pause = hideNotify then pauseApp; resume = startApp then showNotify.
 */
public final class Ams {
    public static final int PAUSED = 0, ACTIVE = 1, DESTROYED = 2;
    private static final Object lock = new Object();
    private static MidletHooks hooks;
    private static int state = PAUSED;
    private static boolean starting, pausedByApp, pausedBySystem;
    private static EventHandler ui;

    private Ams() {
    }

    /** Called by the MIDlet constructor: the first MIDlet created is the one of the run. */
    public static void attach(Object midlet, MidletHooks h) {
        synchronized (lock) {
            if (hooks == null) hooks = h;
        }
    }

    public static void setUi(EventHandler h) {
        ui = h;
    }

    public static int state() {
        return state;
    }

    /** Main thread of the VM: creates the MIDlet of {@code className} and starts it. */
    public static void run(String className) {
        // test hook: a suite that reads host events itself (vmspec d01_input_replay) asks for no event thread; real MIDlets always get one
        if (property0("JLKVM-No-Event-Loop") == null) EventLoop.start();
        try {
            Class.forName(className).newInstance();
        } catch (Throwable t) {
            t.printStackTrace();
            Runtime.getRuntime().exit(1);
            return;
        }
        if (hooks == null) {
            System.err.println("AMS: " + className + " is not a MIDlet");
            Runtime.getRuntime().exit(1);
            return;
        }
        start(false);
    }

    /** startApp: the first start (on the main thread) and every resume (on the event thread). */
    private static void start(boolean resume) {
        synchronized (lock) {
            if (state == DESTROYED) return;
            starting = true;
        }
        boolean destroy = false;
        try {
            hooks.startApp();
            synchronized (lock) {
                if (state == PAUSED && !pausedByApp) state = ACTIVE;   // notifyPaused() inside startApp keeps it Paused
            }
        } catch (Exception e) {
            if (e.getClass().getName().equals("javax.microedition.midlet.MIDletStateChangeException")) {
                synchronized (lock) {
                    state = PAUSED;   // MIDP 2.0: the MIDlet stays Paused
                }
            } else {
                e.printStackTrace();
                destroy = true;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            destroy = true;
        }
        synchronized (lock) {
            starting = false;
            lock.notifyAll();
        }
        if (destroy) {
            try {
                hooks.destroyApp(true);
            } catch (Throwable t) {
                // MIDP: the MIDlet is destroyed anyway
            }
            finish();
        } else if (resume && state == ACTIVE && ui != null) {
            ui.handle(EventLoop.UI_SHOW, 0, 0, 0);
        }
    }

    private static void finish() {
        synchronized (lock) {
            state = DESTROYED;
        }
        Runtime.getRuntime().exit(0);
    }

    /** Event thread: PAUSE / RESUME / DESTROY requested by the host. */
    static void lifecycle(int type, int a) {
        synchronized (lock) {
            while (starting) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        if (type == EventLoop.PAUSE) {
            if (state != ACTIVE) return;
            if (ui != null) ui.handle(EventLoop.UI_HIDE, 0, 0, 0);
            try {
                hooks.pauseApp();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            synchronized (lock) {
                state = PAUSED;
                pausedBySystem = true;
            }
        } else if (type == EventLoop.RESUME) {
            synchronized (lock) {
                if (state != PAUSED || !pausedBySystem) return;
                pausedBySystem = false;
                pausedByApp = false;
            }
            start(true);
        } else if (type == EventLoop.DESTROY) {
            if (state == DESTROYED) return;
            try {
                hooks.destroyApp(a != 0);
            } catch (Exception e) {
                if (a == 0 && e.getClass().getName().equals("javax.microedition.midlet.MIDletStateChangeException")) return;   // the MIDlet refused
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            finish();
        }
    }

    public static void notifyDestroyed(Object midlet) {
        finish();
    }

    public static void notifyPaused(Object midlet) {
        synchronized (lock) {
            if (state == ACTIVE || starting) {
                state = PAUSED;
                pausedByApp = true;
            }
        }
    }

    public static void resumeRequest(Object midlet) {
        boolean ask;
        synchronized (lock) {
            ask = state == PAUSED && pausedByApp;
        }
        if (ask) {
            EventLoop.post(new Runnable() {
                public void run() {
                    synchronized (lock) {
                        if (state != PAUSED) return;
                        pausedByApp = false;
                        pausedBySystem = false;
                    }
                    start(true);
                }
            });
        }
    }

    public static String property(String key) {
        return property0(key);
    }

    public static boolean platformRequest(String url) throws ConnectionNotFoundException {
        int r = platformRequest0(url);
        if (r < 0) throw new ConnectionNotFoundException(url);
        return r == 1;
    }

    public static int checkPermission(String permission) {
        return permission == null ? 0 : checkPermission0(permission);
    }

    private static native String property0(String key);

    /** @return 0 handled, 1 the MIDlet must exit before the request is honoured, -1 not supported. */
    private static native int platformRequest0(String url);

    private static native int checkPermission0(String permission);
}

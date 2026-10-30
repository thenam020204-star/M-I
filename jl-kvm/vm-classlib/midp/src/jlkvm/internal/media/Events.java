/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal.media;

import java.util.Vector;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

/**
 * Delivers PlayerListener events in order on ONE guest thread ("media-events"), never inside the call that caused them and never on a
 * host thread (D13). A listener that throws is reported and does not stop the delivery.
 */
final class Events implements Runnable {
    private static final Events INSTANCE = new Events();
    private final Vector queue = new Vector();   // Object[]{Player, PlayerListener[], String, Object}
    private boolean started;

    static void post(Player p, PlayerListener[] ls, String event, Object data) {
        if (ls.length == 0) return;
        INSTANCE.put(new Object[] {p, ls, event, data});
    }

    private void put(Object[] ev) {
        synchronized (queue) {
            queue.addElement(ev);
            if (!started) {
                started = true;
                Thread t = new Thread(this);
                t.start();
            }
            queue.notifyAll();
        }
    }

    public void run() {
        while (true) {
            Object[] ev;
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        // keep waiting
                    }
                }
                ev = (Object[]) queue.elementAt(0);
                queue.removeElementAt(0);
            }
            PlayerListener[] ls = (PlayerListener[]) ev[1];
            for (int i = 0; i < ls.length; i++) {
                try {
                    ls[i].playerUpdate((Player) ev[0], (String) ev[2], ev[3]);
                } catch (Throwable t) {
                    System.err.println("media listener: " + t);
                }
            }
        }
    }
}

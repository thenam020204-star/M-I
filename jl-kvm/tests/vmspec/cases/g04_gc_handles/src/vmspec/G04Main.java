package vmspec;

import javax.microedition.midlet.MIDlet;

/** T13: host handles whose guest owner died are released and disposed by the collector; the handle table returns to 0 once everything is dropped. */
public class G04Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static void make(int n, Peer[] keep) {
        for (int i = 0; i < n; i++) {
            Peer x = new Peer();
            if (i < keep.length) keep[i] = x;       // the first ones survive
        }
    }

    protected void startApp() {
        Peer[] keep = new Peer[10];
        make(1000, keep);
        p("before gc: live=" + Peer.live0());
        Runtime.getRuntime().gc();
        p("after gc, 10 kept: live=" + Peer.live0() + " disposed by gc=" + Peer.disposed0());
        for (int i = 0; i < 5; i++) { keep[i].close(); keep[i] = null; }
        p("5 closed explicitly: live=" + Peer.live0());
        Runtime.getRuntime().gc();
        p("after gc, 5 kept: live=" + Peer.live0() + " disposed by gc=" + Peer.disposed0() + " last kept=" + (keep[9] != null));   // keep is used after the gc: a local that is dead after a point is not a root (StackMap)
        keep = null;
        Runtime.getRuntime().gc();
        p("all dropped: live=" + Peer.live0() + " disposed by gc=" + Peer.disposed0());
        make(200, new Peer[0]);
        Runtime.getRuntime().gc();
        p("again: live=" + Peer.live0() + " disposed by gc=" + Peer.disposed0());
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

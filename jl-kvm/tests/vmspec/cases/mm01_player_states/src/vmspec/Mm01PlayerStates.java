package vmspec;

import java.util.Vector;
import javax.microedition.media.Control;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.ToneControl;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.midlet.MIDlet;

/**
 * T27: Player life cycle of MIDP 2.0 (javax.microedition.media.Player javadoc) on the tone player (device://tone): states, implicit
 * transitions, exceptions per state, loop count, media time, PlayerListener events (order, data, the thread they run on), VolumeControl.
 * "> " lines are compared; "# " lines are informational.
 */
public class Mm01PlayerStates extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) {
        String n = t.getClass().getName();
        return n.substring(n.lastIndexOf('.') + 1);
    }

    abstract static class Call { abstract void go() throws Exception; }

    static String ex(Call c) {
        try { c.go(); return "ok"; } catch (Throwable t) { return cn(t); }
    }

    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }

    static String st(Player pl) {
        switch (pl.getState()) {
            case Player.UNREALIZED: return "UNREALIZED";
            case Player.REALIZED: return "REALIZED";
            case Player.PREFETCHED: return "PREFETCHED";
            case Player.STARTED: return "STARTED";
            case Player.CLOSED: return "CLOSED";
            default: return "?" + pl.getState();
        }
    }

    static Thread mainThread;
    static final Vector events = new Vector();
    static boolean offThread = true;

    static final PlayerListener L = new PlayerListener() {
        public void playerUpdate(Player pl, String event, Object data) {
            if (Thread.currentThread() == mainThread) offThread = false;
            String d = data == null ? "null" : data instanceof Long ? "Long" : data instanceof VolumeControl ? "VolumeControl" : data.getClass().getName();
            events.addElement(event + "(" + d + ")");
        }
    };

    static void flush(String label) { flush(label, "> "); }

    static void flush(String label, String prefix) {
        sleep(100);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < events.size(); i++) sb.append(i == 0 ? "" : " ").append(events.elementAt(i));
        System.out.println(prefix + label + " events: " + (sb.length() == 0 ? "-" : sb.toString()));
        events.removeAllElements();
    }

    // 250 ms: TEMPO 60 (240 bpm), RESOLUTION 64 -> one unit = 15.625 ms, 8 units per note
    static final byte[] SEQ = {ToneControl.VERSION, 1, ToneControl.TEMPO, 60, 60, 8, 62, 8};

    public void startApp() {
        mainThread = Thread.currentThread();
        try {
            run();
        } catch (Throwable t) {
            p("UNEXPECTED " + t);
            t.printStackTrace();
        }
        notifyDestroyed();
    }

    public void pauseApp() { }

    public void destroyApp(boolean u) { }

    void run() throws Exception {
        final Player pl = Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR);
        p("new: " + st(pl));
        p("unrealized getContentType: " + ex(new Call() { void go() { pl.getContentType(); } }));
        p("unrealized getControl: " + ex(new Call() { void go() { pl.getControl("VolumeControl"); } }));
        p("unrealized getControls: " + ex(new Call() { void go() { pl.getControls(); } }));
        p("unrealized setMediaTime: " + ex(new Call() { void go() throws Exception { pl.setMediaTime(0); } }));
        p("unrealized duration: " + pl.getDuration());
        p("loop 0: " + ex(new Call() { void go() { pl.setLoopCount(0); } }));
        p("loop -1 / 1 / 3: " + ex(new Call() { void go() { pl.setLoopCount(-1); pl.setLoopCount(1); pl.setLoopCount(3); pl.setLoopCount(1); } }));
        pl.addPlayerListener(null);
        pl.addPlayerListener(L);

        pl.realize();
        p("realize: " + st(pl));
        pl.realize();
        p("realize again: " + st(pl));
        p("content type: " + pl.getContentType());
        final ToneControl tc = (ToneControl) pl.getControl("ToneControl");
        p("ToneControl: " + (tc != null) + " same by full name: " + (pl.getControl("javax.microedition.media.control.ToneControl") == tc));
        p("VolumeControl: " + (pl.getControl("VolumeControl") != null));
        p("unknown control: " + pl.getControl("NoSuchControl") + " / midi: " + pl.getControl("javax.microedition.media.control.MIDIControl"));
        p("getControl(null): " + ex(new Call() { void go() { pl.getControl(null); } }));
        Control[] cs = pl.getControls();
        p("controls: " + cs.length + " " + (cs.length >= 2));
        p("setSequence(null): " + ex(new Call() { void go() { tc.setSequence(null); } }));
        p("setSequence(bad): " + ex(new Call() { void go() { tc.setSequence(new byte[] {60, 4}); } }));
        tc.setSequence(SEQ);
        p("duration: " + pl.getDuration() + " TIME_UNKNOWN=" + Player.TIME_UNKNOWN);
        pl.prefetch();
        p("prefetch: " + st(pl));
        p("setSequence prefetched: " + ex(new Call() { void go() { tc.setSequence(SEQ); } }));

        // implicit transitions, STARTED, no-ops
        pl.start();
        p("start: " + st(pl));
        pl.start();
        p("start again: " + st(pl));
        p("loop started: " + ex(new Call() { void go() { pl.setLoopCount(2); } }));
        p("setSequence started: " + ex(new Call() { void go() { tc.setSequence(SEQ); } }));
        pl.stop();
        p("stop: " + st(pl));
        pl.stop();
        p("stop again: " + st(pl));
        long mt = pl.getMediaTime();
        p("media time after stop in (0, duration): " + (mt > 0 && mt < pl.getDuration()));
        flush("first run");

        // setMediaTime: clamp, return value
        p("setMediaTime(-5): " + pl.setMediaTime(-5) + " now " + pl.getMediaTime());
        p("setMediaTime(100000): " + pl.setMediaTime(100000) + " now " + pl.getMediaTime());
        p("setMediaTime(1e9): " + pl.setMediaTime(1000000000L) + " duration " + pl.getDuration());
        pl.setMediaTime(0);

        pl.deallocate();
        p("deallocate: " + st(pl));
        pl.deallocate();
        p("deallocate again: " + st(pl));
        pl.prefetch();
        p("prefetch again: " + st(pl));
        pl.deallocate();

        // plays to the end: START -> END_OF_MEDIA, state PREFETCHED, media time = duration
        events.removeAllElements();
        pl.start();
        p("start from realized: " + st(pl));
        sleep(400);
        p("after the end: " + st(pl) + " media time = duration: " + (pl.getMediaTime() == pl.getDuration()));
        flush("play to end");

        // loop count 2: one END_OF_MEDIA after both rounds
        pl.setLoopCount(2);
        pl.start();
        sleep(350);
        p("loop 2 after 350 ms: " + st(pl));
        sleep(350);
        p("loop 2 after 700 ms: " + st(pl));
        flush("loop 2");

        // stop while playing, deallocate while started, restart after the end plays again
        pl.setLoopCount(1);
        pl.start();
        sleep(60);
        pl.deallocate();
        p("deallocate started: " + st(pl));
        flush("deallocate started");
        pl.start();
        pl.stop();
        flush("start stop");

        // VolumeControl
        final VolumeControl vc = (VolumeControl) pl.getControl("VolumeControl");
        p("volume default muted=" + vc.isMuted());
        p("setLevel 50 -> " + vc.setLevel(50) + " getLevel " + vc.getLevel());
        p("setLevel 150 -> " + vc.setLevel(150));
        p("setLevel -3 -> " + vc.setLevel(-3));
        vc.setLevel(70);
        vc.setMute(true);
        p("mute: " + vc.isMuted() + " level " + vc.getLevel());
        vc.setMute(false);
        p("unmute: " + vc.isMuted() + " level " + vc.getLevel());
        flush("volume", "# ");   // whether every change (also an unchanged level) sends VOLUME_CHANGED is left open

        // remove the listener: no more events
        pl.removePlayerListener(L);
        pl.removePlayerListener(L);
        pl.removePlayerListener(null);
        pl.start();
        sleep(400);
        flush("without listener");
        pl.addPlayerListener(L);

        // close
        pl.start();
        pl.close();
        p("close: " + st(pl));
        flush("close while started", "# ");   // STOPPED before CLOSED: UNVERIFIED
        pl.close();
        p("close again: " + st(pl));
        p("closed realize: " + ex(new Call() { void go() throws Exception { pl.realize(); } }));
        p("closed prefetch: " + ex(new Call() { void go() throws Exception { pl.prefetch(); } }));
        p("closed start: " + ex(new Call() { void go() throws Exception { pl.start(); } }));
        p("closed stop: " + ex(new Call() { void go() throws Exception { pl.stop(); } }));
        p("closed deallocate: " + ex(new Call() { void go() { pl.deallocate(); } }));
        p("closed getDuration: " + ex(new Call() { void go() { pl.getDuration(); } }));
        p("closed getMediaTime: " + ex(new Call() { void go() { pl.getMediaTime(); } }));
        p("closed setMediaTime: " + ex(new Call() { void go() throws Exception { pl.setMediaTime(0); } }));
        p("closed getContentType: " + ex(new Call() { void go() { pl.getContentType(); } }));
        p("closed setLoopCount: " + ex(new Call() { void go() { pl.setLoopCount(1); } }));
        p("closed addPlayerListener: " + ex(new Call() { void go() { pl.addPlayerListener(L); } }));
        p("closed removePlayerListener: " + ex(new Call() { void go() { pl.removePlayerListener(L); } }));
        p("closed getControl: " + ex(new Call() { void go() { pl.getControl("VolumeControl"); } }));
        p("closed getControls: " + ex(new Call() { void go() { pl.getControls(); } }));
        p("closed getState: " + pl.getState());
        p("listeners ran off the main thread only: " + offThread);
    }
}

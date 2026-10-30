package mm;

import java.io.ByteArrayInputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.MIDIControl;
import javax.microedition.media.control.RateControl;
import javax.microedition.media.control.TempoControl;
import javax.microedition.midlet.MIDlet;

/**
 * T27, JSR-135 controls (profile api "jsr-135", needed by the game that uses MIDIControl / TempoControl): MIDIControl on the host
 * synthesiser, TempoControl / RateControl on a MIDI clip (host) and on a tone sequence (guest timing).
 */
public class MmApi135 extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) {
        String n = t.getClass().getName();
        return n.substring(n.lastIndexOf('.') + 1);
    }

    abstract static class Call { abstract Object go() throws Exception; }

    static String ex(Call c) {
        try { Object o = c.go(); return o == null ? "ok" : "ok " + o; } catch (Throwable t) { return cn(t); }
    }

    static String ints(int[] a) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; a != null && i < a.length; i++) sb.append(i == 0 ? "" : ",").append(a[i]);
        return "[" + sb + "]";
    }

    static boolean ended;
    static final Object lock = new Object();

    public void startApp() {
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
        Player m = Manager.createPlayer(new ByteArrayInputStream(new byte[] {'M', 'T', 'h', 'd', 0, 0, 0, 6, 0, 0, 0, 1, 0, 96}), "audio/midi");
        m.realize();
        final MIDIControl mc = (MIDIControl) m.getControl("javax.microedition.media.control.MIDIControl");
        p("MIDIControl: " + (mc != null) + " bank query " + mc.isBankQuerySupported());
        mc.shortMidiEvent(MIDIControl.NOTE_ON | 1, 60, 100);
        mc.shortMidiEvent(0xC0 | 2, 7, 0);
        p("channel volume unknown: " + mc.getChannelVolume(2));
        mc.setChannelVolume(2, 90);
        p("channel volume: " + mc.getChannelVolume(2));
        mc.shortMidiEvent(MIDIControl.CONTROL_CHANGE | 3, 7, 33);
        p("channel volume by event: " + mc.getChannelVolume(3));
        p("program unknown: " + ex(new Call() { Object go() throws Exception { return ints(mc.getProgram(4)); } }));
        mc.setProgram(4, 129, 5);
        p("program: " + ints(mc.getProgram(4)));
        p("long event: " + mc.longMidiEvent(new byte[] {(byte) 0xF0, 0x7E, 0x7F, 9, 1, (byte) 0xF7}, 0, 6) + " / part " + mc.longMidiEvent(new byte[] {1, 2, 3, 4}, 1, 2));
        p("channel 16: " + ex(new Call() { Object go() { mc.setChannelVolume(16, 1); return null; } }));
        p("channel -1: " + ex(new Call() { Object go() { return "" + mc.getChannelVolume(-1); } }));
        p("volume 128: " + ex(new Call() { Object go() { mc.setChannelVolume(0, 128); return null; } }));
        p("program 128: " + ex(new Call() { Object go() { mc.setProgram(0, 0, 128); return null; } }));
        p("bank 16384: " + ex(new Call() { Object go() { mc.setProgram(0, 16384, 0); return null; } }));
        p("status 0x70: " + ex(new Call() { Object go() { mc.shortMidiEvent(0x70, 0, 0); return null; } }));
        p("status 0xF0: " + ex(new Call() { Object go() { mc.shortMidiEvent(0xF0, 0, 0); return null; } }));
        p("data 128: " + ex(new Call() { Object go() { mc.shortMidiEvent(0x90, 128, 0); return null; } }));
        p("long null: " + ex(new Call() { Object go() { return "" + mc.longMidiEvent(null, 0, 0); } }));
        p("long range: " + ex(new Call() { Object go() { return "" + mc.longMidiEvent(new byte[2], 1, 2); } }));
        p("bank list: " + ex(new Call() { Object go() throws Exception { return ints(mc.getBankList(false)); } }));
        p("program list: " + ex(new Call() { Object go() throws Exception { return ints(mc.getProgramList(0)); } }));
        p("program name: " + ex(new Call() { Object go() throws Exception { return mc.getProgramName(0, 0); } }));
        p("key name: " + ex(new Call() { Object go() throws Exception { return mc.getKeyName(0, 0, 60); } }));

        // TempoControl on the MIDI clip (the device decides)
        TempoControl tc = (TempoControl) m.getControl("TempoControl");
        RateControl rc = (RateControl) m.getControl("javax.microedition.media.control.RateControl");
        p("tempo/rate are one object: " + (tc == (Object) rc));
        p("tempo " + tc.getTempo() + " rate " + tc.getRate() + " range " + tc.getMinRate() + ".." + tc.getMaxRate());
        p("setTempo(90000) -> " + tc.setTempo(90000) + " now " + tc.getTempo());
        p("setRate(200000) -> " + tc.setRate(200000));
        p("setRate(1) -> " + tc.setRate(1) + " (clamped to the minimum), setRate(99999999) -> " + tc.setRate(99999999));
        m.close();

        // tone sequence: TEMPO 60 -> 240 bpm; 250 ms at rate 100000
        byte[] seq = {-2, 1, -3, 60, 60, 8, 62, 8};
        Player t = Manager.createPlayer(new ByteArrayInputStream(seq), "audio/x-tone-seq");
        t.realize();
        TempoControl tt = (TempoControl) t.getControl("TempoControl");
        p("tone tempo " + tt.getTempo() + " rate " + tt.getRate());
        p("tone has MIDIControl: " + (t.getControl("MIDIControl") != null));
        t.addPlayerListener(new PlayerListener() {
            public void playerUpdate(Player pl, String event, Object data) {
                if (PlayerListener.END_OF_MEDIA.equals(event)) synchronized (lock) { ended = true; lock.notifyAll(); }
            } });
        long t0 = System.currentTimeMillis();
        t.start();
        waitEnd();
        long normal = System.currentTimeMillis() - t0;
        p("normal speed took about 250 ms: " + (normal >= 240 && normal < 400));
        p("setTempo(120000) -> " + tt.setTempo(120000) + " rate " + tt.getRate());   // half the tempo = half the rate
        ended = false;
        t.setMediaTime(0);
        t0 = System.currentTimeMillis();
        t.start();
        waitEnd();
        long slow = System.currentTimeMillis() - t0;
        p("half tempo took about 500 ms: " + (slow >= 490 && slow < 650));
        p("setRate(400000) -> " + tt.setRate(400000) + " tempo " + tt.getTempo());
        t.close();
    }

    static void waitEnd() {
        long end = System.currentTimeMillis() + 20000;
        synchronized (lock) {
            while (!ended) {
                long w = end - System.currentTimeMillis();
                if (w <= 0) return;
                try { lock.wait(w); } catch (InterruptedException e) { }
            }
        }
    }
}

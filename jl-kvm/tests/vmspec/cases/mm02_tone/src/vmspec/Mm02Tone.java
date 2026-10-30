package vmspec;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.ToneControl;
import javax.microedition.midlet.MIDlet;

/**
 * T27: Manager (createPlayer argument rules, supported content types / protocols, playTone), the MIDP tone sequence format
 * (ToneControl) and RTTTL / RTX text played through the tone generator. "> " lines are compared.
 */
public class Mm02Tone extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) {
        String n = t.getClass().getName();
        return n.substring(n.lastIndexOf('.') + 1);
    }

    abstract static class Call { abstract void go() throws Exception; }

    static String ex(Call c) {
        try { c.go(); return "ok"; } catch (Throwable t) { return cn(t); }
    }

    static String join(String[] a) {
        if (a == null) return "null";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < a.length; i++) sb.append(i == 0 ? "" : ",").append(a[i]);
        return "[" + sb + "]";
    }

    static boolean has(String[] a, String s) {
        for (int i = 0; i < a.length; i++) if (a[i].equals(s)) return true;
        return false;
    }

    static InputStream in(String s) throws Exception { return new ByteArrayInputStream(s.getBytes("ISO-8859-1")); }

    static long duration(final byte[] seq) throws Exception {
        Player pl = Manager.createPlayer(new ByteArrayInputStream(seq), "audio/x-tone-seq");
        pl.realize();
        long d = pl.getDuration();
        pl.close();
        return d;
    }

    static String rtttl(String text, String type) {
        try {
            Player pl = Manager.createPlayer(in(text), type);
            try {
                pl.realize();
                long d = pl.getDuration();
                String ct = pl.getContentType();
                pl.close();
                return "duration " + d + " type " + ct;
            } catch (MediaException e) {
                return "MediaException at realize";
            }
        } catch (Throwable t) {
            return cn(t) + " at create";
        }
    }

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
        p("TONE_DEVICE_LOCATOR " + Manager.TONE_DEVICE_LOCATOR);
        // supported types / protocols (the lists themselves come from the profile: only the guaranteed members are compared)
        String[] all = Manager.getSupportedContentTypes(null);
        p("all types non-null, has tone-seq: " + (all != null && has(all, "audio/x-tone-seq")));
        p("types of device: " + join(Manager.getSupportedContentTypes("device")));
        p("types of unknown protocol: " + join(Manager.getSupportedContentTypes("nosuchprotocol")));
        String[] pr = Manager.getSupportedProtocols(null);
        p("all protocols has device: " + has(pr, "device"));
        p("protocols of tone-seq has device: " + has(Manager.getSupportedProtocols("audio/x-tone-seq"), "device"));
        p("protocols of unknown type: " + join(Manager.getSupportedProtocols("video/x-nothing")));
        p("rtttl listed: " + (has(all, "audio/x-rtttl") || has(all, "text/x-rtttl")));

        // createPlayer argument rules
        p("createPlayer(null): " + ex(new Call() { void go() throws Exception { Manager.createPlayer((String) null); } }));
        p("createPlayer(stream null): " + ex(new Call() { void go() throws Exception { Manager.createPlayer((InputStream) null, "audio/x-tone-seq"); } }));
        p("createPlayer(malformed): " + ex(new Call() { void go() throws Exception { Manager.createPlayer("nonsense"); } }));
        p("createPlayer(capture): " + ex(new Call() { void go() throws Exception { Manager.createPlayer("capture://audio"); } }));
        p("createPlayer(device://nothing): " + ex(new Call() { void go() throws Exception { Manager.createPlayer("device://nothing"); } }));
        p("createPlayer(unsupported type): " + ex(new Call() { void go() throws Exception { Manager.createPlayer(in("abc"), "video/mpeg"); } }));
        p("createPlayer(unknown content): " + ex(new Call() { void go() throws Exception { Manager.createPlayer(in("\u0001\u0002 not media"), null); } }));
        p("createPlayer(tone device): " + ex(new Call() { void go() throws Exception { Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR).close(); } }));

        // playTone
        p("playTone(60,100,100): " + ex(new Call() { void go() throws Exception { Manager.playTone(60, 100, 100); } }));
        p("playTone(0..127): " + ex(new Call() { void go() throws Exception { Manager.playTone(0, 1, 0); Manager.playTone(127, 1, 100); } }));
        p("playTone volume clamped: " + ex(new Call() { void go() throws Exception { Manager.playTone(69, 10, 250); Manager.playTone(69, 10, -4); } }));
        p("playTone(-1): " + ex(new Call() { void go() throws Exception { Manager.playTone(-1, 100, 50); } }));
        p("playTone(128): " + ex(new Call() { void go() throws Exception { Manager.playTone(128, 100, 50); } }));
        p("playTone(duration 0): " + ex(new Call() { void go() throws Exception { Manager.playTone(60, 0, 50); } }));
        p("playTone(duration -5): " + ex(new Call() { void go() throws Exception { Manager.playTone(60, -5, 50); } }));

        // tone sequences: durations in microseconds (unit = 60 000 000 / (tempo * resolution) us)
        final byte V = ToneControl.VERSION, T = ToneControl.TEMPO, R = ToneControl.RESOLUTION, BS = ToneControl.BLOCK_START, BE = ToneControl.BLOCK_END,
                PB = ToneControl.PLAY_BLOCK, REP = ToneControl.REPEAT, VOL = ToneControl.SET_VOLUME, SIL = ToneControl.SILENCE;
        p("default tempo 120 bpm: quarter = " + duration(new byte[] {V, 1, ToneControl.C4, 16}));
        p("silence counts: " + duration(new byte[] {V, 1, SIL, 16, 60, 16}));
        p("tempo 60 res 16: " + duration(new byte[] {V, 1, T, 60, R, 16, 69, 4}));
        p("empty sequence: " + duration(new byte[] {V, 1}));
        p("blocks + repeat: " + duration(new byte[] {V, 1, BS, 0, 60, 8, 62, 8, BE, 0, PB, 0, REP, 3, 64, 4, PB, 0, VOL, 50}));
        p("tempo change in the middle: " + duration(new byte[] {V, 1, 60, 16, T, 60, 60, 16}));
        final byte[][] bad = {
            {60, 4}, {V, 2, 60, 4}, {V, 1, T, 4}, {V, 1, R, 0}, {V, 1, 60, 0}, {V, 1, 60}, {V, 1, VOL, 101}, {V, 1, REP, 1, 60, 4}, {V, 1, PB, 0},
            {V, 1, BS, 0, 60, 4}, {V, 1, BS, 0, 60, 4, BE, 1}, {V, 1, BE, 0}, {V, 1, BS, 0, BS, 1, BE, 1, BE, 0}, {V, 1, BS, 0, PB, 0, BE, 0}, {V, 1, -20, 1}};
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bad.length; i++) {
            Player pl = Manager.createPlayer(Manager.TONE_DEVICE_LOCATOR);
            pl.realize();
            final ToneControl tc = (ToneControl) pl.getControl("ToneControl");
            final byte[] b = bad[i];
            sb.append(ex(new Call() { void go() { tc.setSequence(b); } }).equals("IllegalArgumentException") ? "I" : "?");
            pl.close();
        }
        p("invalid sequences rejected: " + sb);
        p("invalid sequence as a stream: " + ex(new Call() { void go() throws Exception {
            Player pl = Manager.createPlayer(new ByteArrayInputStream(new byte[] {V, 1, 60, 0}), "audio/x-tone-seq");
            pl.realize(); } }));

        // RTTTL / RTX: b=120 -> 8th = 250 ms; no defaults -> d=4 o=6 b=63
        p("rtttl audio/x-rtttl: " + rtttl("t:d=8,o=5,b=120:c,d,e,p", "audio/x-rtttl"));
        p("rtttl text/x-rtttl: " + rtttl("t:d=8,o=5,b=120:c,d,e,p", "text/x-rtttl"));
        p("rtttl sniffed: " + rtttl("Tune:d=4,o=5,b=60:4c,4d", null));
        p("rtttl upper case and blanks: " + rtttl("TUNE : D=8 , O=5 , B=120 :\r\n C , D , E , P\r\n", "audio/x-rtttl"));
        p("rtttl without defaults: " + rtttl("t::c,d", "audio/x-rtttl"));
        p("rtttl dotted: " + rtttl("t:d=4,o=5,b=60:4c.,8p.", "audio/x-rtttl"));
        p("rtttl bad note: " + rtttl("t:d=4,o=5,b=60:4z", "audio/x-rtttl"));
        p("rtttl bad duration: " + rtttl("t:d=4,o=5,b=60:3c", "audio/x-rtttl"));
        p("rtttl no colon: " + rtttl("just text", "audio/x-rtttl"));
        p("rtttl has no ToneControl: " + ex(new Call() { void go() throws Exception {
            Player pl = Manager.createPlayer(in("t:d=8:c"), "audio/x-rtttl");
            pl.realize();
            if (pl.getControl("ToneControl") != null) throw new IllegalStateException("ToneControl on RTTTL");
            pl.close(); } }));
    }
}

package mm;

import java.io.ByteArrayInputStream;
import java.util.Vector;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.midlet.MIDlet;

/**
 * T27 on the host: clips played by the audio device (MIDI, WAV...), the end of a clip arriving from the host, PlayerListener only on
 * guest threads, the content types of the profile, a device that refuses a type. Does not name any JSR-135 class (see MmApi135).
 * The script (tests/midlets/scripts/mmapi.inp) makes the headless device finish clip 1 and 2.
 */
public class MmApi extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) {
        String n = t.getClass().getName();
        return n.substring(n.lastIndexOf('.') + 1);
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
    static boolean offThread = true;
    static final Vector events = new Vector();
    static final Object lock = new Object();

    static final PlayerListener L = new PlayerListener() {
        public void playerUpdate(Player pl, String event, Object data) {
            if (Thread.currentThread() == mainThread) offThread = false;
            synchronized (lock) {
                events.addElement(event);
                lock.notifyAll();
            }
        }
    };

    /** Waits (virtual time) until {@code event} was delivered. */
    static boolean await(String event) {
        long end = System.currentTimeMillis() + 20000;
        synchronized (lock) {
            while (!events.contains(event)) {
                long w = end - System.currentTimeMillis();
                if (w <= 0) return false;
                try { lock.wait(w); } catch (InterruptedException e) { }
            }
        }
        return true;
    }

    static String drain() {
        try { Thread.sleep(100); } catch (InterruptedException e) { }   // let the event thread deliver what is queued
        StringBuffer sb = new StringBuffer();
        synchronized (lock) {
            for (int i = 0; i < events.size(); i++) sb.append(i == 0 ? "" : " ").append(events.elementAt(i));
            events.removeAllElements();
        }
        return sb.length() == 0 ? "-" : sb.toString();
    }

    static byte[] midi() {
        return new byte[] {'M', 'T', 'h', 'd', 0, 0, 0, 6, 0, 0, 0, 1, 0, 96};
    }

    /** 8000 Hz, 8 bit mono WAV with 4000 data bytes = 500 ms. */
    static byte[] wav() {
        byte[] b = new byte[44 + 4000];
        String h = "RIFF";
        for (int i = 0; i < 4; i++) b[i] = (byte) h.charAt(i);
        int riff = b.length - 8;
        b[4] = (byte) riff; b[5] = (byte) (riff >> 8); b[6] = (byte) (riff >> 16); b[7] = 0;
        String w = "WAVEfmt ";
        for (int i = 0; i < 8; i++) b[8 + i] = (byte) w.charAt(i);
        b[16] = 16; b[20] = 1; b[22] = 1;
        b[24] = (byte) (8000 & 255); b[25] = (byte) (8000 >> 8);
        b[28] = (byte) (8000 & 255); b[29] = (byte) (8000 >> 8);   // byte rate
        b[32] = 1; b[34] = 8;
        String d = "data";
        for (int i = 0; i < 4; i++) b[36 + i] = (byte) d.charAt(i);
        b[40] = (byte) (4000 & 255); b[41] = (byte) (4000 >> 8);
        return b;
    }

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
        p("types: " + join(Manager.getSupportedContentTypes(null)));
        p("protocols: " + join(Manager.getSupportedProtocols(null)));
        p("midi protocols: " + join(Manager.getSupportedProtocols("audio/midi")));
        p("amr: " + create(new byte[] {'#', '!', 'A', 'M', 'R', '\n'}, "audio/amr"));
        p("mp3 refused by the device: " + create(new byte[] {'I', 'D', '3', 4, 0}, "audio/mpeg"));

        // RTTTL: c5 (250 ms), a pause (250 ms), e5 (250 ms) on the tone generator, no decoder involved
        Player r = Manager.createPlayer(new ByteArrayInputStream("ring:d=8,o=5,b=120:c,p,e".getBytes("ISO-8859-1")), "audio/x-rtttl");
        r.addPlayerListener(L);
        r.start();
        p("rtttl waits for its end: " + await("endOfMedia") + " state " + st(r) + " duration " + r.getDuration());
        p("rtttl events " + drain());
        r.close();
        await("closed");
        drain();
        Manager.playTone(69, 100, 80);

        // clip 1: a MIDI file, 2 loops, ends when the host says so
        Player m = Manager.createPlayer(new ByteArrayInputStream(midi()), "audio/midi");
        p("midi: " + st(m));
        m.realize();
        p("midi realized: " + st(m) + " type " + m.getContentType() + " duration " + m.getDuration());
        p("midi has VolumeControl: " + (m.getControl("VolumeControl") != null));
        p("midi has MIDIControl: " + (m.getControl("javax.microedition.media.control.MIDIControl") != null));
        p("midi has TempoControl: " + (m.getControl("TempoControl") != null) + " RateControl: " + (m.getControl("RateControl") != null));
        m.setLoopCount(2);
        m.addPlayerListener(L);
        m.start();
        p("midi started: " + st(m));
        ((VolumeControl) m.getControl("VolumeControl")).setLevel(40);
        p("waiting for the host: " + await("endOfMedia"));
        p("midi ended: " + st(m) + " events " + drain());
        m.start();
        p("midi restarted: " + st(m));
        m.stop();
        p("midi stopped: " + st(m) + " events " + drain());
        m.deallocate();
        p("midi deallocated: " + st(m));
        m.close();
        p("midi closed: " + st(m) + " events " + drain());

        // clip 2: WAV of 500 ms: the duration comes from the device
        Player w;
        try {
            w = Manager.createPlayer(new ByteArrayInputStream(wav()), null);
        } catch (MediaException e) {
            p("wav: MediaException (" + e.getMessage() + ")");
            return;
        }
        w.realize();
        p("wav type " + w.getContentType() + " duration " + w.getDuration());
        w.addPlayerListener(L);
        w.start();
        p("wav started: " + st(w));
        p("wav waits for the host: " + await("endOfMedia"));
        p("wav ended: " + st(w) + " media time = duration " + (w.getMediaTime() == w.getDuration()) + " events " + drain());
        w.close();
        p("listener only on guest threads other than main: " + offThread);
    }

    static String join(String[] a) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < a.length; i++) sb.append(i == 0 ? "" : ",").append(a[i]);
        return "[" + sb + "]";
    }

    static String create(byte[] data, String type) {
        try {
            Player pl = Manager.createPlayer(new ByteArrayInputStream(data), type);
            try {
                pl.realize();
                String r = "ok " + pl.getContentType();
                pl.close();
                return r;
            } catch (MediaException e) {
                return "MediaException at realize";
            }
        } catch (Throwable t) {
            return cn(t) + " at create";
        }
    }
}

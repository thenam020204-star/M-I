/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal.media;

import java.util.Vector;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.ToneControl;
import jlkvm.internal.media.tone.Rtttl;
import jlkvm.internal.media.tone.Seq;
import jlkvm.internal.media.tone.ToneSeq;

/**
 * Player for tone sequences (audio/x-tone-seq and device://tone) and RTTTL / RTX text: the notes are timed by a guest thread against
 * System.currentTimeMillis() and played one by one on the host tone generator ({@link Audio#tone0}); nothing goes through a media
 * decoder. Parsing happens in the guest (the same code serves the unit tests and T32's ringing tone) - docs/midp.md section 10.
 */
public final class TonePlayer extends BasePlayer {
    public static final int DEVICE = 0, TONE_SEQ = 1, RTTTL = 2;
    private static final int MIN_RATE = 10000, MAX_RATE = 1000000;

    private final int kind;
    private byte[] data;
    private Seq seq;
    private final Object lock = new Object();
    private long baseUs;
    private long startMs;
    private int rate = 100000;

    public TonePlayer(int kind, String contentType, byte[] data) {
        super(contentType);
        this.kind = kind;
        this.data = data;
    }

    protected void doRealize() throws MediaException {
        if (seq != null) return;
        try {
            if (kind == RTTTL) seq = Rtttl.parse(data);
            else if (kind == TONE_SEQ) seq = ToneSeq.compile(data);
            else seq = new Seq();   // device://tone before ToneControl.setSequence: silence (UNVERIFIED, lenient)
        } catch (IllegalArgumentException e) {
            throw new MediaException(e.getMessage());
        }
        data = null;
    }

    protected boolean midiCapable() {
        return true;
    }

    protected void addOwnControls(Vector cs, Vector ns) {
        if (kind != RTTTL) {
            cs.addElement(new Tone());
            ns.addElement(CONTROL_PKG + "ToneControl");
        }
    }

    protected long durationUs() {
        return seq == null ? 0 : seq.totalUs;
    }

    protected long currentUs() {
        long d = durationUs();
        long v = baseUs + (System.currentTimeMillis() - startMs) * 1000L * rate / 100000L;
        return v > d ? d : (v < 0 ? 0 : v);
    }

    protected void doStart() {
        begin(mediaUs);
    }

    private void begin(long from) {
        baseUs = from;
        startMs = System.currentTimeMillis();
        final int id = runId;
        final Seq s = seq;
        final int loopCount = loops;
        new Thread() {
            public void run() {
                play(id, s, loopCount);
            }
        }.start();
    }

    protected void doStop() {
        mediaUs = currentUs();
        cancel();
    }

    protected void doSeekStarted(long us) {
        cancel();
        begin(us);
    }

    protected void doClose() {
        cancel();
    }

    private void cancel() {
        Audio.silence0();
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    /** Sleeps until the wall clock reaches {@code targetMs}; false when this run was stopped meanwhile. */
    private boolean sleepUntil(long targetMs, int id) {
        synchronized (lock) {
            while (true) {
                if (id != runId) return false;
                long w = targetMs - System.currentTimeMillis();
                if (w <= 0) return true;
                try {
                    lock.wait(w);
                } catch (InterruptedException e) {
                    // re-check
                }
            }
        }
    }

    private void play(int id, Seq s, int loopCount) {
        long from = baseUs;
        int left = loopCount;
        while (true) {
            int i = 0;
            long acc = 0;
            while (i < s.n && acc + s.us[i] <= from) {
                acc += s.us[i];
                i++;
            }
            long skip = from - acc;
            long anchorUs = System.currentTimeMillis() * 1000L;
            for (; i < s.n; i++) {
                if (id != runId) return;
                long media = s.us[i] - skip;
                skip = 0;
                long wall = media * 100000L / rate;
                int nt = s.note[i];
                if (nt >= 0 && media > 0) {
                    int lvl = effectiveLevel();
                    Audio.tone0(nt > 127 ? 127 : nt, (int) ((wall + 500) / 1000), s.vol[i] * lvl / 100);
                }
                anchorUs += wall;
                if (!sleepUntil(anchorUs / 1000, id)) return;
            }
            if (s.totalUs <= 0 || (loopCount >= 0 && --left <= 0)) break;
            from = 0;
            synchronized (this) {
                if (id != runId) return;
                baseUs = 0;
                startMs = System.currentTimeMillis();
            }
        }
        endOfMedia(id);
    }

    // ---------------------------------------------------------------- JSR-135 rate / tempo
    public synchronized int getRateMilli() {
        return rate;
    }

    public int minRateMilli() {
        return MIN_RATE;
    }

    public int maxRateMilli() {
        return MAX_RATE;
    }

    public synchronized int setRateMilli(int milli) {
        if (milli < MIN_RATE) milli = MIN_RATE;
        if (milli > MAX_RATE) milli = MAX_RATE;
        if (state == STARTED) {
            baseUs = currentUs();
            startMs = System.currentTimeMillis();
        }
        rate = milli;
        return rate;
    }

    private int baseTempo() {
        return seq == null ? 120000 : seq.baseTempoMilli;
    }

    public synchronized int getTempoMilli() {
        return (int) ((long) baseTempo() * rate / 100000L);
    }

    public synchronized int setTempoMilli(int milli) {
        if (milli <= 0) return getTempoMilli();
        setRateMilli((int) ((long) milli * 100000L / baseTempo()));
        return getTempoMilli();
    }

    /** ToneControl (MIDP 2.0). */
    private final class Tone implements ToneControl {
        public void setSequence(byte[] sequence) {
            synchronized (TonePlayer.this) {
                if (state == CLOSED || state == PREFETCHED || state == STARTED) throw new IllegalStateException("setSequence in state " + state);
                seq = ToneSeq.compile(sequence);   // IllegalArgumentException for an invalid sequence
                mediaUs = 0;
            }
        }
    }
}

package jlkvm.host.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The portable part of the Android {@link AudioDevice} (PLAN T35c): decides which engine plays what and keeps the clip state machine,
 * on top of three small engine interfaces so that it runs on the JVM with fakes ({@code AudioRouterTest}).
 * <ul>
 * <li>MIDI files (content type or the {@code MThd} header) go to the synthesiser ({@link SynthLibrary}: TinySoundFont with a sound
 * bank, else Sonivox); everything else (WAV, MP3, AMR...) goes to the {@link ClipBackend}. Without a synthesiser MIDI files try the
 * clip backend (the platform player).</li>
 * <li>Raw MIDI messages ({@code midiShort / midiLong}) go to a second synthesiser player on the raw port ({@code device://midi}),
 * created on the first message; only Sonivox accepts messages, a library that does not just consumes 0 bytes.</li>
 * <li>Tones are rendered by {@link ToneSynth} (the T27 generator) and handed to {@link ToneOut}.</li>
 * <li>Looping of backend clips, "ended" reporting (once per play, after the last loop), pause / resume with the Activity.</li>
 * </ul>
 * All public methods are synchronized; the engines may call back from any thread, the listener is called outside the lock and only
 * wakes the guest thread (D13, R7).
 */
public class AudioRouter implements AudioDevice {
    private static final class Clip {
        final int id;
        final SynthLibrary synth;     // non null = synth clip
        final long sh;
        final int bh;                 // backend handle
        int loops;                    // as given to play: -1 forever, n >= 1
        int loopsLeft;
        boolean playing, suspended, ended, prefetched;
        Clip(int id, SynthLibrary synth, long sh, int bh) { this.id = id; this.synth = synth; this.sh = sh; this.bh = bh; }
    }

    private final ClipBackend clips;
    private final SynthLibrary clipSynth;
    private final SynthLibrary rawSynth;
    private final ToneOut toneOut;
    private final Map<Integer, Clip> byId = new HashMap<Integer, Clip>();
    private final Map<Long, Clip> bySynth = new HashMap<Long, Clip>();
    private final Map<Integer, Clip> byBackend = new HashMap<Integer, Clip>();
    private int next = 1;
    private Listener listener;
    private long rawHandle;
    private boolean rawTried, rawSuspended;
    private static final float MASTER = 1f;

    public AudioRouter(ClipBackend clips, SynthLibrary clipSynth, SynthLibrary rawSynth, ToneOut toneOut) {
        this.clips = clips;
        this.clipSynth = clipSynth;
        this.rawSynth = rawSynth;
        this.toneOut = toneOut;
        if (clips != null) {
            clips.setEndListener(new ClipBackend.EndListener() {
                public void ended(int handle) { backendEnded(handle); }
            });
        }
    }

    /** MIDI by content type or by header ("MThd"; RIFF RMID files are plain MIDI inside). */
    public static boolean isMidi(String mime, byte[] d) {
        if (mime != null) {
            String m = mime.toLowerCase();
            int semi = m.indexOf(';');
            if (semi >= 0) m = m.substring(0, semi).trim();
            if (m.equals("audio/midi") || m.equals("audio/mid") || m.equals("audio/x-midi") || m.equals("audio/sp-midi") || m.equals("audio/x-mid")
                    || m.equals("audio/rmf")) return true;
        }
        return d != null && d.length >= 4 && d[0] == 'M' && d[1] == 'T' && d[2] == 'h' && d[3] == 'd';
    }

    @Override public synchronized void setListener(Listener l) { listener = l; }

    // ------------------------------------------------------------------ tone

    @Override
    public synchronized void tone(int note, int durationMs, int volume) {
        silence();
        if (toneOut == null || durationMs <= 0 || volume <= 0) return;
        toneOut.play(ToneSynth.render(note, Math.min(durationMs, 5000), volume), ToneSynth.RATE);
    }

    @Override
    public synchronized void silence() {
        if (toneOut != null) toneOut.stop();
    }

    // ------------------------------------------------------------------ raw MIDI

    private boolean rawReady() {
        if (rawHandle != 0) return true;
        if (rawTried || rawSynth == null) return false;
        rawTried = true;
        long h = 0;
        try {
            h = rawSynth.createPlayer("device://midi");
            rawSynth.realize(h);
            rawSynth.prefetch(h);
            rawSynth.start(h);
            rawHandle = h;
            return true;
        } catch (Exception e) {
            if (h != 0) { try { rawSynth.close(h); rawSynth.release(h); } catch (RuntimeException ignored) { } }
            return false;
        } catch (LinkageError e) {   // native library missing
            return false;
        }
    }

    @Override
    public synchronized void midiShort(int status, int data1, int data2) {
        if (status < 0x80 || status > 0xEF) return;   // system messages are not for the synthesiser
        int top = status & 0xF0;
        byte[] m = top == 0xC0 || top == 0xD0 ? new byte[] {(byte) status, (byte) (data1 & 0x7F)}
                : new byte[] {(byte) status, (byte) (data1 & 0x7F), (byte) (data2 & 0x7F)};
        write(m, 0, m.length);
    }

    @Override
    public synchronized void midiLong(byte[] data, int off, int len) {
        if (data == null || off < 0 || len <= 0 || off + len > data.length) return;
        write(data, off, len);
    }

    private void write(byte[] d, int off, int len) {
        if (!rawReady()) return;
        try { rawSynth.writeMIDI(rawHandle, d, off, len); } catch (RuntimeException ignored) { }
    }

    private void allNotesOff() {
        if (rawHandle == 0) return;
        byte[] m = new byte[6 * 16];
        int p = 0;
        for (int ch = 0; ch < 16; ch++) {
            m[p++] = (byte) (0xB0 | ch); m[p++] = 123; m[p++] = 0;      // all notes off
            m[p++] = (byte) (0xB0 | ch); m[p++] = 120; m[p++] = 0;      // all sound off
        }
        try { rawSynth.writeMIDI(rawHandle, m, 0, p); } catch (RuntimeException ignored) { }
    }

    // ------------------------------------------------------------------ clips

    @Override
    public synchronized int open(byte[] data, String mime) {
        if (data == null) return -1;
        if (isMidi(mime, data) && clipSynth != null) {
            int id = openSynth(data);
            if (id > 0) return id;
        }
        if (clips == null) return -1;
        int bh = clips.open(data, mime);
        if (bh <= 0) return -1;
        Clip c = new Clip(next++, null, 0, bh);
        byId.put(c.id, c);
        byBackend.put(bh, c);
        return c.id;
    }

    private int openSynth(byte[] data) {
        long h = 0;
        try {
            h = clipSynth.createPlayer("device://tone");
            clipSynth.setDataSource(h, data);
            clipSynth.realize(h);
            final Clip c = new Clip(next++, clipSynth, h, 0);
            clipSynth.setEventListener(h, new SynthLibrary.EventListener() {
                public void event(long handle, int type, long timeUs) { synthEvent(handle, type); }
            });
            byId.put(c.id, c);
            bySynth.put(h, c);
            return c.id;
        } catch (Exception e) {
            if (h != 0) { try { clipSynth.close(h); clipSynth.release(h); } catch (RuntimeException ignored) { } }
            return -1;
        } catch (LinkageError e) {
            return -1;
        }
    }

    private void synthEvent(long handle, int type) {
        if (type == SynthLibrary.EVENT_RESTART) return;
        Listener l;
        int id;
        synchronized (this) {
            Clip c = bySynth.get(handle);
            if (c == null || !c.playing) return;
            c.playing = false;
            c.ended = true;
            id = c.id;
            l = listener;
        }
        if (l != null) l.ended(id);
    }

    private void backendEnded(int bh) {
        Listener l;
        int id;
        synchronized (this) {
            Clip c = byBackend.get(bh);
            if (c == null || !c.playing) return;
            if (c.loops < 0 || c.loopsLeft > 1) {
                if (c.loops > 0) c.loopsLeft--;
                clips.seek(bh, 0);
                clips.start(bh);
                return;
            }
            c.playing = false;
            c.ended = true;
            id = c.id;
            l = listener;
        }
        if (l != null) l.ended(id);
    }

    private Clip clip(int id) { return byId.get(id); }

    @Override
    public synchronized long durationMs(int clip) {
        Clip c = clip(clip);
        if (c == null) return -1;
        if (c.synth != null) {
            long us = c.synth.getDuration(c.sh);
            return us < 0 ? -1 : us / 1000;
        }
        return clips.durationMs(c.bh);
    }

    @Override
    public synchronized void play(int clip, int loops) {
        Clip c = clip(clip);
        if (c == null || loops == 0) return;
        c.loops = loops;
        c.loopsLeft = loops;
        c.suspended = false;
        try {
            if (c.synth != null) {
                c.synth.setRepeat(c.sh, loops);
                c.synth.setVolume(c.sh, level(c), level(c));
                if (!c.prefetched) { c.synth.prefetch(c.sh); c.prefetched = true; }
                if (c.ended) { c.synth.setMediaTime(c.sh, 0); }
                c.synth.start(c.sh);
            } else {
                if (c.ended) clips.seek(c.bh, 0);
                clips.start(c.bh);
            }
            c.playing = true;
            c.ended = false;
        } catch (Exception e) {
            c.playing = false;
        } catch (LinkageError e) {
            c.playing = false;
        }
    }

    private final Map<Integer, Integer> levels = new HashMap<Integer, Integer>();

    private float level(Clip c) {
        Integer l = levels.get(c.id);
        return (l == null ? 100 : l) / 100f * MASTER;
    }

    @Override
    public synchronized void stop(int clip) {
        Clip c = clip(clip);
        if (c == null) return;
        pauseClip(c);
        c.playing = false;
        c.suspended = false;
    }

    private void pauseClip(Clip c) {
        try {
            if (c.synth != null) c.synth.pause(c.sh); else clips.pause(c.bh);
        } catch (Exception ignored) {
        } catch (LinkageError ignored) {
        }
    }

    @Override
    public synchronized long positionMs(int clip) {
        Clip c = clip(clip);
        if (c == null) return 0;
        if (c.synth != null) { long us = c.synth.getMediaTime(c.sh); return us < 0 ? 0 : us / 1000; }
        return Math.max(0, clips.positionMs(c.bh));
    }

    @Override
    public synchronized void seek(int clip, long ms) {
        Clip c = clip(clip);
        if (c == null) return;
        long t = Math.max(0, ms);
        if (c.synth != null) c.synth.setMediaTime(c.sh, t * 1000); else clips.seek(c.bh, t);
    }

    @Override
    public synchronized void volume(int clip, int lvl) {
        Clip c = clip(clip);
        if (c == null) return;
        levels.put(c.id, Math.max(0, Math.min(100, lvl)));
        float v = level(c);
        if (c.synth != null) c.synth.setVolume(c.sh, v, v); else clips.setVolume(c.bh, v);
    }

    /** Neither Sonivox nor TinySoundFont of JL-Mod exposes the tempo of a file: unknown. */
    @Override public int tempo(int clip, int milli) { return -1; }

    @Override public int rate(int clip, int milli) { return -1; }

    @Override
    public synchronized void close(int clip) {
        Clip c = byId.remove(clip);
        if (c == null) return;
        levels.remove(clip);
        if (c.synth != null) {
            bySynth.remove(c.sh);
            try { c.synth.close(c.sh); c.synth.release(c.sh); } catch (RuntimeException ignored) { }
        } else {
            byBackend.remove(c.bh);
            clips.close(c.bh);
        }
    }

    // ------------------------------------------------------------------ Activity life cycle

    /** The Activity goes to the background: tone and every sounding clip stop (remembered for {@link #resumeAll}). */
    public synchronized void pauseAll() {
        silence();
        for (Clip c : new ArrayList<Clip>(byId.values())) {
            if (c.playing) {
                pauseClip(c);
                c.suspended = true;
            }
        }
        if (rawHandle != 0) {
            allNotesOff();
            try { rawSynth.pause(rawHandle); rawSuspended = true; } catch (Exception ignored) { }
        }
    }

    /** The Activity is back: the clips {@link #pauseAll} stopped go on where they were. */
    public synchronized void resumeAll() {
        for (Clip c : new ArrayList<Clip>(byId.values())) {
            if (c.suspended) {
                c.suspended = false;
                try {
                    if (c.synth != null) c.synth.start(c.sh); else clips.start(c.bh);
                } catch (Exception e) {
                    c.playing = false;
                }
            }
        }
        if (rawHandle != 0 && rawSuspended) {
            rawSuspended = false;
            try { rawSynth.start(rawHandle); } catch (Exception ignored) { }
        }
    }

    /** Frees everything (Activity destroyed). */
    public synchronized void releaseAll() {
        silence();
        for (Integer id : new ArrayList<Integer>(byId.keySet())) close(id);
        if (rawHandle != 0) {
            try { rawSynth.close(rawHandle); rawSynth.release(rawHandle); } catch (RuntimeException ignored) { }
            rawHandle = 0;
        }
    }

    /** Number of open clips (tests). */
    public synchronized int openClips() { return byId.size(); }
}

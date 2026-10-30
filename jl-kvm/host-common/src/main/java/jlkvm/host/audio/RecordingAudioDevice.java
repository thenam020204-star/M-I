package jlkvm.host.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongSupplier;

/**
 * Headless audio device: makes no sound, logs every call ("t=12 tone 69 500 100") with the time from {@code clock} (the virtual clock of
 * the desktop host), renders the tones into PCM (checksum + sample count, for tests) and lets a test finish clips by hand
 * ({@link #finishClip}). Clip durations: WAV from the header, anything else {@code unknownMs}.
 */
public class RecordingAudioDevice implements AudioDevice {
    private static final class Clip {
        final String mime; final byte[] data; long durationMs; long positionMs; long playStartedMs = -1; int loops; int level = 100;
        int tempo = 120000, rate = 100000; boolean playing;
        Clip(String m, byte[] d) { mime = m; data = d; }
    }

    private volatile LongSupplier clock;
    private final List<String> log = Collections.synchronizedList(new ArrayList<String>());
    private final Map<Integer, Clip> clips = new HashMap<Integer, Clip>();
    private Listener listener;
    private int next = 1;
    private long pcmSamples;
    private long pcmCrc = 17;
    private long lastToneEndMs = -1;
    /** Content types this device accepts; null = every type. */
    public java.util.Set<String> accepts;
    /** Duration given to clips whose duration cannot be read (-1 = unknown). */
    public long unknownMs = -1;

    public RecordingAudioDevice(LongSupplier clock) { this.clock = clock; }

    public RecordingAudioDevice() { this(new LongSupplier() { public long getAsLong() { return System.currentTimeMillis(); } }); }

    /** The time source of the log (milliseconds); host-desktop's runner sets the virtual clock of the run. */
    public void setClock(LongSupplier c) { clock = c; }

    private void log(String s) { log.add("t=" + clock.getAsLong() + " " + s); }

    /** The log so far. */
    public List<String> log() { synchronized (log) { return new ArrayList<String>(log); } }

    public long pcmSamples() { return pcmSamples; }

    public long pcmChecksum() { return pcmCrc; }

    public synchronized void setListener(Listener l) { listener = l; }

    public synchronized void tone(int note, int durationMs, int volume) {
        log("tone " + note + " " + durationMs + " " + volume);
        long now = clock.getAsLong();
        if (lastToneEndMs >= 0 && now > lastToneEndMs) {   // silence between two tones
            int gap = (int) Math.min(now - lastToneEndMs, 60000);
            pcmSamples += (long) gap * ToneSynth.RATE / 1000;
            pcmCrc = pcmCrc * 31 + gap;
        }
        short[] pcm = ToneSynth.render(note, durationMs, volume);
        pcmSamples += pcm.length;
        for (int i = 0; i < pcm.length; i++) pcmCrc = pcmCrc * 31 + pcm[i];
        lastToneEndMs = now + durationMs;
    }

    public synchronized void silence() { log("silence"); lastToneEndMs = -1; }

    public synchronized void midiShort(int status, int d1, int d2) { log("midi " + status + " " + d1 + " " + d2); }

    public synchronized void midiLong(byte[] data, int off, int len) {
        StringBuilder sb = new StringBuilder("midiLong");
        for (int i = 0; i < len; i++) sb.append(' ').append(data[off + i] & 0xFF);
        log(sb.toString());
    }

    public synchronized int open(byte[] data, String mime) {
        if (accepts != null && !accepts.contains(mime)) { log("open " + mime + " refused"); return -1; }
        Clip c = new Clip(mime, data);
        c.durationMs = wavDurationMs(data);
        if (c.durationMs < 0) c.durationMs = unknownMs;
        int h = next++;
        clips.put(Integer.valueOf(h), c);
        log("open " + h + " " + mime + " " + data.length + " bytes duration=" + c.durationMs);
        return h;
    }

    private Clip clip(int h) {
        Clip c = clips.get(Integer.valueOf(h));
        if (c == null) throw new IllegalStateException("bad clip " + h);
        return c;
    }

    public synchronized long durationMs(int h) { return clip(h).durationMs; }

    public synchronized void play(int h, int loops) {
        Clip c = clip(h);
        c.playing = true; c.loops = loops; c.playStartedMs = clock.getAsLong();
        log("play " + h + " loops=" + loops + " at=" + c.positionMs);
    }

    private void settle(Clip c) {
        if (c.playing) { c.positionMs += (clock.getAsLong() - c.playStartedMs) * c.rate / 100000; c.playStartedMs = clock.getAsLong();
            if (c.durationMs >= 0 && c.positionMs > c.durationMs) c.positionMs = c.durationMs; }
    }

    public synchronized void stop(int h) { Clip c = clip(h); settle(c); c.playing = false; log("stop " + h + " at=" + c.positionMs); }

    public synchronized long positionMs(int h) { Clip c = clip(h); settle(c); return c.positionMs; }

    public synchronized void seek(int h, long ms) { Clip c = clip(h); settle(c); c.positionMs = ms; log("seek " + h + " " + ms); }

    public synchronized void volume(int h, int level) { clip(h).level = level; log("volume " + h + " " + level); }

    public synchronized int tempo(int h, int milli) {
        Clip c = clip(h);
        if (milli > 0) { c.tempo = milli; log("tempo " + h + " " + milli); }
        return c.tempo;
    }

    public synchronized int rate(int h, int milli) {
        Clip c = clip(h);
        if (milli > 0) { settle(c); c.rate = Math.max(10000, Math.min(1000000, milli)); log("rate " + h + " " + c.rate); }
        return c.rate;
    }

    public synchronized void close(int h) { if (clips.remove(Integer.valueOf(h)) != null) log("close " + h); }

    /** Test hook: the clip played to its end (position = duration); tells the listener like a real device. */
    public void finishClip(int h) {
        Listener l;
        synchronized (this) {
            Clip c = clips.get(Integer.valueOf(h));
            if (c == null || !c.playing) return;
            c.playing = false;
            if (c.durationMs >= 0) c.positionMs = c.durationMs;
            log("ended " + h);
            l = listener;
        }
        if (l != null) l.ended(h);
    }

    /** Handles of the open clips, ascending. */
    public synchronized List<Integer> openClips() { List<Integer> r = new ArrayList<Integer>(clips.keySet()); Collections.sort(r); return r; }

    /** Duration in ms of a canonical RIFF/WAVE PCM file, else -1. */
    public static long wavDurationMs(byte[] d) {
        if (d.length < 44 || d[0] != 'R' || d[1] != 'I' || d[2] != 'F' || d[3] != 'F' || d[8] != 'W' || d[9] != 'A' || d[10] != 'V' || d[11] != 'E') return -1;
        long byteRate = 0, dataLen = -1;
        int p = 12;
        while (p + 8 <= d.length) {
            long len = (d[p + 4] & 0xFFL) | (d[p + 5] & 0xFFL) << 8 | (d[p + 6] & 0xFFL) << 16 | (d[p + 7] & 0xFFL) << 24;
            String id = new String(d, p, 4, java.nio.charset.StandardCharsets.ISO_8859_1);
            if (id.equals("fmt ") && p + 20 <= d.length) byteRate = (d[p + 16] & 0xFFL) | (d[p + 17] & 0xFFL) << 8 | (d[p + 18] & 0xFFL) << 16 | (d[p + 19] & 0xFFL) << 24;
            else if (id.equals("data")) { dataLen = Math.min(len, d.length - p - 8); break; }
            p += 8 + (int) len + (int) (len & 1);
        }
        return byteRate > 0 && dataLen >= 0 ? dataLen * 1000 / byteRate : -1;
    }
}

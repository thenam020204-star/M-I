package jlkvm.host.android;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jlkvm.host.audio.AudioDevice;

/**
 * {@link AudioDevice} on the Android media stack (PLAN T27/T35): clips (MIDI files, WAV, MP3, AMR...) through {@link MediaPlayer} on a
 * cache file, the tone generator through a short {@link AudioTrack} sine. The raw MIDI synthesiser (MIDIControl short / long
 * messages) is NOT connected to JL-Mod's Sonivox / TinySoundFont yet: those calls are dropped. Tempo / rate are unsupported (-1).
 * Not tested on a device (UNVERIFIED).
 */
public final class AndroidAudioDevice implements AudioDevice {
    private static final class Clip {
        MediaPlayer player;
        File file;
        int loopsLeft;
        boolean forever;
        boolean prepared;
    }

    private final Context context;
    private final Map<Integer, Clip> clips = new HashMap<>();
    private int next = 1;
    private Listener listener;
    private AudioTrack tone;

    public AndroidAudioDevice(Context context) { this.context = context.getApplicationContext(); }

    @Override public synchronized void setListener(Listener l) { listener = l; }

    @Override
    public synchronized void tone(int note, int durationMs, int volume) {
        silence();
        if (durationMs <= 0 || volume <= 0) return;
        final int rate = 8000;
        int n = Math.min(rate * durationMs / 1000, rate * 5);
        double hz = 440.0 * Math.pow(2.0, (note - 69) / 12.0);
        short[] pcm = new short[n];
        double amp = 32767.0 * Math.min(100, volume) / 100.0 * 0.5;
        for (int i = 0; i < n; i++) pcm[i] = (short) (amp * Math.sin(2 * Math.PI * hz * i / rate));
        try {
            AudioTrack t = new AudioTrack(AudioManager.STREAM_MUSIC, rate, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, Math.max(n * 2, 1024), AudioTrack.MODE_STATIC);
            t.write(pcm, 0, n);
            t.play();
            tone = t;
        } catch (RuntimeException e) {
            tone = null;
        }
    }

    @Override
    public synchronized void silence() {
        AudioTrack t = tone;
        tone = null;
        if (t != null) {
            try { t.stop(); } catch (RuntimeException ignored) { }
            t.release();
        }
    }

    @Override public void midiShort(int status, int data1, int data2) { }

    @Override public void midiLong(byte[] data, int off, int len) { }

    @Override
    public synchronized int open(byte[] data, String mime) {
        File f = null;
        try {
            f = File.createTempFile("clip", ".snd", context.getCacheDir());
            try (FileOutputStream o = new FileOutputStream(f)) { o.write(data); }
            final Clip c = new Clip();
            c.file = f;
            c.player = new MediaPlayer();
            c.player.setDataSource(f.getPath());
            c.player.prepare();
            c.prepared = true;
            final int id = next++;
            c.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override public void onCompletion(MediaPlayer mp) { completed(id, c); }
            });
            clips.put(id, c);
            return id;
        } catch (IOException | RuntimeException e) {
            if (f != null) f.delete();
            return -1;
        }
    }

    private void completed(int id, Clip c) {
        Listener l;
        synchronized (this) {
            if (c.forever || c.loopsLeft > 1) {
                if (!c.forever) c.loopsLeft--;
                try { c.player.seekTo(0); c.player.start(); return; } catch (RuntimeException ignored) { }
            }
            l = listener;
        }
        if (l != null) l.ended(id);   // never runs guest code: it only wakes the guest thread parked in Audio.await0 (D13)
    }

    private Clip get(int id) { return clips.get(id); }

    @Override public synchronized long durationMs(int clip) {
        Clip c = get(clip);
        if (c == null) return -1;
        int d = c.player.getDuration();
        return d <= 0 ? -1 : d;
    }

    @Override public synchronized void play(int clip, int loops) {
        Clip c = get(clip);
        if (c == null) return;
        c.forever = loops < 0;
        c.loopsLeft = loops;
        try { c.player.start(); } catch (RuntimeException ignored) { }
    }

    @Override public synchronized void stop(int clip) {
        Clip c = get(clip);
        if (c == null) return;
        try { if (c.player.isPlaying()) c.player.pause(); } catch (RuntimeException ignored) { }
    }

    @Override public synchronized long positionMs(int clip) {
        Clip c = get(clip);
        try { return c == null ? 0 : c.player.getCurrentPosition(); } catch (RuntimeException e) { return 0; }
    }

    @Override public synchronized void seek(int clip, long ms) {
        Clip c = get(clip);
        try { if (c != null) c.player.seekTo((int) ms); } catch (RuntimeException ignored) { }
    }

    @Override public synchronized void volume(int clip, int level) {
        Clip c = get(clip);
        float v = Math.max(0, Math.min(100, level)) / 100f;
        try { if (c != null) c.player.setVolume(v, v); } catch (RuntimeException ignored) { }
    }

    @Override public int tempo(int clip, int milli) { return -1; }

    @Override public int rate(int clip, int milli) { return -1; }

    @Override public synchronized void close(int clip) {
        Clip c = clips.remove(clip);
        if (c == null) return;
        try { c.player.release(); } catch (RuntimeException ignored) { }
        c.file.delete();
    }

    /** Pauses / resumes every clip with the Activity (a game must not keep sounding behind the home screen). */
    public synchronized void pauseAll() {
        silence();
        for (Clip c : clips.values()) {
            try { if (c.player.isPlaying()) c.player.pause(); } catch (RuntimeException ignored) { }
        }
    }

    public synchronized void releaseAll() {
        silence();
        for (Integer id : new java.util.ArrayList<Integer>(clips.keySet())) close(id);
    }
}

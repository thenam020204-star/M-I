package jlkvm.host.android;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jlkvm.host.audio.ClipBackend;

/**
 * {@link ClipBackend} on {@link MediaPlayer} (WAV, MP3, AMR, AAC... whatever the platform decodes): the data goes through a cache file
 * (MediaPlayer needs a path), one player per clip. Looping and end reports belong to {@code AudioRouter}; this class only reports
 * "played to its end once". Not tested on a device (UNVERIFIED, V16).
 */
final class AndroidClipBackend implements ClipBackend {
    private static final class Clip {
        MediaPlayer player;
        File file;
    }

    private final Context context;
    private final Map<Integer, Clip> clips = new HashMap<>();
    private int next = 1;
    private EndListener listener;

    AndroidClipBackend(Context context) { this.context = context.getApplicationContext(); }

    @Override public synchronized void setEndListener(EndListener l) { listener = l; }

    @Override
    public synchronized int open(byte[] data, String mime) {
        File f = null;
        MediaPlayer p = null;
        try {
            f = File.createTempFile("clip", ".snd", context.getCacheDir());
            try (FileOutputStream o = new FileOutputStream(f)) { o.write(data); }
            p = new MediaPlayer();
            p.setDataSource(f.getPath());
            p.prepare();
            final int id = next++;
            p.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override public void onCompletion(MediaPlayer mp) {
                    EndListener l;
                    synchronized (AndroidClipBackend.this) { l = listener; }
                    if (l != null) l.ended(id);
                }
            });
            Clip c = new Clip();
            c.player = p;
            c.file = f;
            clips.put(id, c);
            return id;
        } catch (IOException | RuntimeException e) {
            if (p != null) { try { p.release(); } catch (RuntimeException ignored) { } }
            if (f != null) //noinspection ResultOfMethodCallIgnored
                f.delete();
            return -1;
        }
    }

    @Override public synchronized long durationMs(int h) {
        Clip c = clips.get(h);
        if (c == null) return -1;
        try { int d = c.player.getDuration(); return d <= 0 ? -1 : d; } catch (RuntimeException e) { return -1; }
    }

    @Override public synchronized void start(int h) {
        Clip c = clips.get(h);
        try { if (c != null) c.player.start(); } catch (RuntimeException ignored) { }
    }

    @Override public synchronized void pause(int h) {
        Clip c = clips.get(h);
        try { if (c != null && c.player.isPlaying()) c.player.pause(); } catch (RuntimeException ignored) { }
    }

    @Override public synchronized long positionMs(int h) {
        Clip c = clips.get(h);
        try { return c == null ? 0 : c.player.getCurrentPosition(); } catch (RuntimeException e) { return 0; }
    }

    @Override public synchronized void seek(int h, long ms) {
        Clip c = clips.get(h);
        try { if (c != null) c.player.seekTo((int) ms); } catch (RuntimeException ignored) { }
    }

    @Override public synchronized void setVolume(int h, float v) {
        Clip c = clips.get(h);
        try { if (c != null) c.player.setVolume(v, v); } catch (RuntimeException ignored) { }
    }

    @Override public synchronized void close(int h) {
        Clip c = clips.remove(h);
        if (c == null) return;
        try { c.player.release(); } catch (RuntimeException ignored) { }
        //noinspection ResultOfMethodCallIgnored
        c.file.delete();
    }
}

package jlkvm.host.android;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import jlkvm.host.audio.ToneOut;

/** {@link ToneOut} on a static {@link AudioTrack}: the PCM block rendered by {@code ToneSynth} (T27 tone generator). */
final class AndroidToneOut implements ToneOut {
    private AudioTrack track;

    @Override
    public synchronized void play(short[] pcm, int rate) {
        stop();
        if (pcm.length == 0) return;
        try {
            AudioTrack t = new AudioTrack(AudioManager.STREAM_MUSIC, rate, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, Math.max(pcm.length * 2, 1024), AudioTrack.MODE_STATIC);
            t.write(pcm, 0, pcm.length);
            t.play();
            track = t;
        } catch (RuntimeException e) {
            track = null;
        }
    }

    @Override
    public synchronized void stop() {
        AudioTrack t = track;
        track = null;
        if (t != null) {
            try { t.stop(); } catch (RuntimeException ignored) { }
            t.release();
        }
    }
}

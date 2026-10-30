/*
 * jl-kvm guest class library (Nokia UI layer, API per tools/api-sigs/db/s40dp2-nokia-ui.json). Origin: original code written for jl-kvm
 * (PLAN D12, T28); behaviour per the Nokia UI API 1.1 javadoc of com.nokia.mid.sound.Sound, open points UNVERIFIED (docs/nokia.md).
 * License: Apache-2.0.
 */
package com.nokia.mid.sound;

import java.io.ByteArrayInputStream;
import java.util.Vector;
import javax.microedition.media.Control;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;
import jlkvm.internal.media.TonePlayer;
import jlkvm.internal.media.tone.Seq;
import jlkvm.internal.nokia.Host;
import jlkvm.internal.nokia.Ott;

/**
 * Built on the MMAPI player of the library: tones are timed by {@link TonePlayer} (the tone generator of T27, never a media decoder), WAV by
 * the host audio device. The listener is called on the "media-events" guest thread, never inside the call that caused the event and never
 * from a host thread.
 */
public class Sound {
    public static final int FORMAT_TONE = 1;
    public static final int FORMAT_WAV = 5;
    public static final int SOUND_PLAYING = 0;
    public static final int SOUND_STOPPED = 1;
    public static final int SOUND_UNINITIALIZED = 3;

    /** Frequencies (Hz) of the MIDI notes 0..127 (A4 = 69 = 440 Hz), rounded. */
    private static final int[] HZ = new int[128];
    /** Highest frequency of the javadoc table (A6b). */
    private static final int MAX_FREQ = 13288;
    /** The sounds playing now, oldest first (concurrency limit of the device). */
    private static final Vector playing = new Vector();

    static {
        // 8.1757989 Hz * 2^(n / 12): built by repeated multiplication with 2^(1/12) in float (CLDC 1.1 has no Math.pow); rounding to whole Hz
        double f = 8.175798915643707;
        for (int n = 0; n < 128; n++) {
            HZ[n] = (int) (f + 0.5);
            f *= 1.0594630943592953;
        }
    }

    private Player player;
    private int state = SOUND_UNINITIALIZED;
    private int type;
    private int gain = 255;
    private int generation;
    private SoundListener listener;

    public Sound(int freq, long duration) {
        init(freq, duration);
    }

    public Sound(byte[] data, int type) {
        init(data, type);
    }

    public static int[] getSupportedFormats() {
        int mask = Host.config0(1);
        int n = ((mask & 2) != 0 ? 1 : 0) + ((mask & 32) != 0 ? 1 : 0);
        int[] r = new int[n];
        int i = 0;
        if ((mask & 2) != 0) r[i++] = FORMAT_TONE;
        if ((mask & 32) != 0) r[i++] = FORMAT_WAV;
        return r;
    }

    public static int getConcurrentSoundCount(int type) {
        int mask = Host.config0(1);
        if ((type != FORMAT_TONE && type != FORMAT_WAV) || (mask & (1 << type)) == 0) throw new IllegalArgumentException("type " + type);
        return Host.config0(type == FORMAT_TONE ? 2 : 3);
    }

    private static int noteOf(int freq) {
        int best = 0;
        long bestDiff = Long.MAX_VALUE;
        for (int n = 0; n < 128; n++) {
            long d = (long) (HZ[n] > freq ? HZ[n] - freq : freq - HZ[n]);
            if (d < bestDiff) {
                bestDiff = d;
                best = n;
            }
        }
        return best;
    }

    public synchronized void init(int freq, long duration) {
        release0(false);
        state = SOUND_UNINITIALIZED;
        if (duration <= 0) throw new IllegalArgumentException("duration " + duration);
        if (freq < 0 || freq > MAX_FREQ) throw new IllegalArgumentException("freq " + freq);
        Seq s = new Seq();
        long us = duration > 3600000L ? 3600000000L : duration * 1000L;
        s.add(freq == 0 ? -1 : noteOf(freq), us, 100);
        attach(new TonePlayer(s), FORMAT_TONE);
    }

    public synchronized void init(byte[] data, int type) {
        release0(false);
        state = SOUND_UNINITIALIZED;
        if (data == null) throw new NullPointerException();
        int mask = Host.config0(1);
        if ((type != FORMAT_TONE && type != FORMAT_WAV) || (mask & (1 << type)) == 0) throw new IllegalArgumentException("type " + type);
        Player p;
        try {
            if (type == FORMAT_TONE) {
                p = new TonePlayer(Ott.parse(data));
            } else {
                p = Manager.createPlayer(new ByteArrayInputStream(data), null);
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("sound data: " + e);
        }
        attach(p, type);
    }

    private void attach(Player p, int fmt) {
        try {
            p.realize();
        } catch (Exception e) {
            p.close();
            throw new IllegalArgumentException("sound data: " + e);
        }
        generation++;
        final int gen = generation;
        p.addPlayerListener(new PlayerListener() {
            public void playerUpdate(Player pl, String event, Object data) {
                event(gen, pl, event);
            }
        });
        player = p;
        type = fmt;
        applyGain();
        state = SOUND_STOPPED;
    }

    private void applyGain() {
        if (player == null) return;
        Control c = player.getControl("VolumeControl");
        if (c instanceof VolumeControl) ((VolumeControl) c).setLevel(gain == 0 ? 0 : (gain * 100 + 254) / 255);
    }

    /** Player event (guest thread "media-events"). */
    private void event(int gen, Player pl, String event) {
        int ev;
        SoundListener l;
        synchronized (this) {
            if (gen != generation) return;
            if (event.equals(PlayerListener.STARTED)) {
                ev = SOUND_PLAYING;
            } else if (event.equals(PlayerListener.STOPPED) || event.equals(PlayerListener.END_OF_MEDIA)) {
                ev = SOUND_STOPPED;
            } else if (event.equals(PlayerListener.CLOSED)) {
                ev = SOUND_UNINITIALIZED;
            } else {
                return;
            }
            // the state is set by the calls themselves; an event of an older run must not undo a restart that is already playing
            if (ev == SOUND_UNINITIALIZED) state = SOUND_UNINITIALIZED;
            else if (state == SOUND_UNINITIALIZED) state = SOUND_UNINITIALIZED;   // late event of a sound released since: reported, state stays
            else if (ev == SOUND_PLAYING) state = SOUND_PLAYING;
            else if (pl.getState() != Player.STARTED) state = SOUND_STOPPED;
            if (ev == SOUND_STOPPED) forget();
            l = listener;
        }
        if (l != null) {
            try {
                l.soundStateChanged(this, ev);
            } catch (Throwable t) {
                System.err.println("sound listener: " + t);
            }
        }
    }

    private void forget() {
        if (state != SOUND_PLAYING) {
            synchronized (playing) {
                playing.removeElement(this);
            }
        }
    }

    public synchronized int getGain() {
        return gain;
    }

    public synchronized void setGain(int gain) {
        this.gain = gain < 0 ? 0 : (gain > 255 ? 255 : gain);
        applyGain();
    }

    public synchronized int getState() {
        return state;
    }

    public void setSoundListener(SoundListener listener) {
        synchronized (this) {
            this.listener = listener;
        }
    }

    public void play(int loop) {
        Sound victim = null;
        synchronized (this) {
            if (loop < 0) throw new IllegalArgumentException("loop " + loop);
            if (state == SOUND_UNINITIALIZED || player == null) return;
            if (loop >= 255) return;   // the javadoc lets the implementation ignore such values
            synchronized (playing) {
                playing.removeElement(this);
                int limit = Host.config0(type == FORMAT_TONE ? 2 : 3), same = 0;
                for (int i = 0; i < playing.size(); i++) if (((Sound) playing.elementAt(i)).type == type) same++;
                if (same >= limit) {
                    for (int i = 0; i < playing.size(); i++) {
                        if (((Sound) playing.elementAt(i)).type == type) {
                            victim = (Sound) playing.elementAt(i);
                            playing.removeElementAt(i);
                            break;
                        }
                    }
                }
                playing.addElement(this);
            }
            try {
                if (player.getState() == Player.STARTED) player.stop();
                player.setLoopCount(loop == 0 ? -1 : loop);
                player.setMediaTime(0);
                player.start();
                state = SOUND_PLAYING;
            } catch (Exception e) {
                state = SOUND_STOPPED;
            }
        }
        if (victim != null) victim.stop();
    }

    public synchronized void stop() {
        if (player == null || state != SOUND_PLAYING) return;
        try {
            player.stop();
        } catch (Exception e) {
            // already stopped
        }
        state = SOUND_STOPPED;
        forget();
    }

    public synchronized void resume() {
        if (player == null || state != SOUND_STOPPED) return;
        try {
            player.setMediaTime(0);   // tones cannot resume in the middle (javadoc); the other formats restart too (UNVERIFIED)
            player.start();
            state = SOUND_PLAYING;
        } catch (Exception e) {
            state = SOUND_STOPPED;
        }
    }

    public synchronized void release() {
        release0(true);
    }

    private void release0(boolean notify) {
        if (player == null) return;
        Player p = player;
        player = null;
        synchronized (playing) {
            playing.removeElement(this);
        }
        if (!notify) generation++;   // init() replaces the player: the events of the old one are not reported
        state = SOUND_UNINITIALIZED;
        try {
            p.close();
        } catch (Exception e) {
            // closed anyway
        }
    }
}

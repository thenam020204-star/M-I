package jlkvm.host.android;

import android.content.Context;

import jlkvm.host.audio.AudioRouter;
import jlkvm.host.audio.SynthLibrary;

/**
 * {@link jlkvm.host.audio.AudioDevice} on Android (PLAN T27 / T35 / T35c): {@link AudioRouter} with the platform {@link android.media.MediaPlayer}
 * for WAV / MP3 / AMR clips ({@link AndroidClipBackend}), a short {@link android.media.AudioTrack} for the tone generator
 * ({@link AndroidToneOut}) and JL-Mod's native synthesisers for MIDI: {@code clipSynth} plays MIDI files (TinySoundFont when the game
 * has a sound bank, else Sonivox), {@code rawSynth} (Sonivox) takes the MIDIControl messages. Either may be null (MIDI files then try
 * the platform player, raw messages are dropped). Not tested on a device (UNVERIFIED, V16).
 */
public final class AndroidAudioDevice extends AudioRouter {
    public AndroidAudioDevice(Context context, SynthLibrary clipSynth, SynthLibrary rawSynth) {
        super(new AndroidClipBackend(context), clipSynth, rawSynth, new AndroidToneOut());
    }
}

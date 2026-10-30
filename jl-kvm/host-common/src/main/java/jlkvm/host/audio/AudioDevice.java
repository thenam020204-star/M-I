package jlkvm.host.audio;

/**
 * The audio output of a host (PLAN T27): a tone generator, a MIDI synthesiser and a player of clips. host-desktop has the headless
 * {@link RecordingAudioDevice}; host-android will plug the Sonivox / TinySoundFont synthesiser of JL-Mod in here (M6, NOT done).
 *
 * <p>Threading: the natives call every method from the vm-thread; the device may play on its own threads. Only
 * {@link Listener#ended} may be called from any thread - it never runs guest code, it only wakes the guest thread parked in
 * {@code Audio.await0} (D13, R7).
 */
public interface AudioDevice {
    /** A clip played to its end (not when it was stopped or closed). */
    interface Listener {
        void ended(int clip);
    }

    void setListener(Listener l);

    // ---- tone generator (Manager.playTone, tone sequences, RTTTL): the guest does the timing
    /** Sounds MIDI note {@code note} (0..127, 69 = A4 = 440 Hz) for at most {@code durationMs} at {@code volume} 0..100; a later call replaces it. */
    void tone(int note, int durationMs, int volume);

    /** Silences the tone generator. */
    void silence();

    // ---- MIDI synthesiser (MIDIControl)
    void midiShort(int status, int data1, int data2);

    void midiLong(byte[] data, int off, int len);

    // ---- clips (MIDI files, WAV, MP3, AMR...)
    /** @return a clip handle &gt; 0, or -1 when the device cannot play this data / content type. */
    int open(byte[] data, String mime);

    /** @return the duration in milliseconds, -1 unknown. */
    long durationMs(int clip);

    /** Starts at the current position; {@code loops} -1 = forever, n &gt;= 1 = n times. {@link Listener#ended} follows the last one. */
    void play(int clip, int loops);

    /** Pauses: keeps the position. */
    void stop(int clip);

    long positionMs(int clip);

    void seek(int clip, long ms);

    /** Output level 0..100 (0 when muted). */
    void volume(int clip, int level);

    /** @param milli milli-beats per minute to set (&gt; 0) or only query (&lt;= 0). @return the actual tempo, -1 unknown. */
    int tempo(int clip, int milli);

    /** @param milli milli-percent (100000 = normal) to set (&gt; 0) or query (&lt;= 0). @return the actual rate. */
    int rate(int clip, int milli);

    void close(int clip);
}

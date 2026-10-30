package jlkvm.host.audio;

/**
 * A native MIDI synthesiser with players (JL-Mod's Sonivox "EAS" and TinySoundFont libraries, PLAN T35c): host-common only names the
 * calls so that {@link AudioRouter} is tested with a fake; the app module adapts {@code ru.woesss.j2me.mmapi.synth.Library} to it.
 * Handles are the native player pointers; times are microseconds (MMAPI media time). Methods may throw on a native failure.
 */
public interface SynthLibrary {
    /** Native playback events of one player: 1 = loop restarted, 2 = stopped at the end, 3 = error. May arrive on any thread. */
    interface EventListener {
        void event(long handle, int type, long timeUs);
    }

    int EVENT_RESTART = 1, EVENT_END = 2, EVENT_ERROR = 3;

    /** @param locator {@code "device://tone"} (an empty player that takes {@link #setDataSource}) or {@code "device://midi"} (the raw MIDI port). */
    long createPlayer(String locator) throws Exception;

    void setEventListener(long handle, EventListener l);

    void setDataSource(long handle, byte[] data) throws Exception;

    void realize(long handle) throws Exception;

    void prefetch(long handle) throws Exception;

    void start(long handle) throws Exception;

    void pause(long handle) throws Exception;

    void deallocate(long handle);

    void close(long handle);

    /** Frees the native player after {@link #close}. */
    void release(long handle);

    long setMediaTime(long handle, long us);

    long getMediaTime(long handle);

    /** -1 = forever, n &gt;= 1 = n times in total. */
    void setRepeat(long handle, int count);

    void setVolume(long handle, float left, float right);

    /** @return microseconds, &lt; 0 unknown */
    long getDuration(long handle);

    /** @return the number of bytes consumed */
    int writeMIDI(long handle, byte[] data, int off, int len);
}

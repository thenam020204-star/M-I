package jlkvm.host.audio;

/** Decodes and plays encoded clips (WAV, MP3, AMR...): MediaPlayer on Android. Looping and end reporting are done by {@link AudioRouter}. */
public interface ClipBackend {
    interface EndListener {
        /** The clip played to its end once (any thread). */
        void ended(int handle);
    }

    void setEndListener(EndListener l);

    /** @return a handle &gt; 0 or -1 when the data cannot be played */
    int open(byte[] data, String mime);

    /** @return milliseconds, -1 unknown */
    long durationMs(int handle);

    void start(int handle);

    void pause(int handle);

    long positionMs(int handle);

    void seek(int handle, long ms);

    /** 0..1 */
    void setVolume(int handle, float v);

    void close(int handle);
}

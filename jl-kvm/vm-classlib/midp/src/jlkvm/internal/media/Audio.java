/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal.media;

/**
 * ROM-private: the audio device of the host (host-common {@code AudioNatives} over {@code jlkvm.host.audio.AudioDevice}, docs/midp.md
 * section 10). The host never calls guest code: end of a clip is learned by a guest thread parked in {@link #await0}.
 */
public final class Audio {
    private Audio() {
    }

    /** config0 selectors */
    public static final int CFG_TYPES = 0, CFG_PROTOCOLS = 1, CFG_TONE = 2, CFG_JSR135 = 3;

    /** Profile data as a comma separated string (CFG_TONE / CFG_JSR135: "1" or "0"). */
    public static native String config0(int what);

    /** One tone on the host synthesiser for {@code ms} milliseconds (replaces the tone still sounding); returns at once. */
    public static native void tone0(int note, int ms, int volume);

    public static native void silence0();

    public static native void midiShort0(int status, int data1, int data2);

    public static native void midiLong0(byte[] data, int off, int len);

    /** Appends bytes to the staging buffer of the clip being created ({@code len} &lt; 0 empties the buffer). */
    public static native void stage0(byte[] buf, int off, int len);

    /** Turns the staging buffer into a clip source (host memory) and returns its id; -1 when the host cannot take it. */
    public static native int source0(String mime);

    public static native void freeSource0(int source);

    /** Opens a clip handle on the device: handle &gt; 0, or -1 when the device cannot decode the data. */
    public static native int open0(int source);

    /** Duration in milliseconds, -1 unknown. */
    public static native long duration0(int clip);

    /** Starts the clip (loops: -1 forever, n &gt;= 1 times) as run number {@code run}; {@link #await0} of that run reports its end. */
    public static native void play0(int clip, int loops, int run);

    public static native void stop0(int clip);

    public static native long position0(int clip);

    public static native void seek0(int clip, long ms);

    /** Effective level 0..100 (0 when muted). */
    public static native void volume0(int clip, int level);

    /** milli-beats per minute: sets (&gt; 0) or only queries (&lt;= 0); returns the actual tempo, -1 unknown. */
    public static native int tempo0(int clip, int milli);

    /** milli-percent (100000 = normal): sets (&gt; 0) or queries (&lt;= 0); returns the actual rate. */
    public static native int rate0(int clip, int milli);

    public static native void close0(int clip);

    /** Parks the calling guest thread until run {@code run} of the clip ended (1) or was stopped / closed / replaced (0). */
    public static native int await0(int clip, int run);
}

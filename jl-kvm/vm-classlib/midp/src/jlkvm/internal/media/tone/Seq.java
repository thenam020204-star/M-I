/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal.media.tone;

/**
 * A compiled tone sequence: a list of (note, duration in microseconds, volume 0..100) events; note -1 = silence. Shared by the
 * ToneControl sequence compiler ({@link ToneSeq}) and the RTTTL parser ({@link Rtttl}); depends on java.lang only (so that the host
 * unit tests can compile it, docs/midp.md section 10).
 */
public final class Seq {
    public static final int MAX_EVENTS = 65536;
    public int n;
    public int[] note = new int[16];
    public long[] us = new long[16];
    public int[] vol = new int[16];
    /** Sum of the durations. */
    public long totalUs;
    /** Tempo in effect at the start, milli-beats per minute (tone sequence: tempo * 4 * 1000; default 120000). */
    public int baseTempoMilli = 120000;

    public void add(int nt, long micros, int volume) {
        if (n >= MAX_EVENTS) throw new IllegalArgumentException("sequence too long");
        if (n == note.length) {
            int c = n * 2;
            int[] a = new int[c];
            long[] b = new long[c];
            int[] d = new int[c];
            System.arraycopy(note, 0, a, 0, n);
            System.arraycopy(us, 0, b, 0, n);
            System.arraycopy(vol, 0, d, 0, n);
            note = a;
            us = b;
            vol = d;
        }
        note[n] = nt;
        us[n] = micros;
        vol[n] = volume;
        n++;
        totalUs += micros;
    }
}

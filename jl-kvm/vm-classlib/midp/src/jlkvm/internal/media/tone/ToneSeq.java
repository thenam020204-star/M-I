/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 * Behaviour per the MIDP 2.0 javadoc of javax.microedition.media.control.ToneControl; points the javadoc leaves open are UNVERIFIED
 * (docs/midp.md section 10).
 */
package jlkvm.internal.media.tone;

/**
 * Compiler of the MIDP tone sequence format: VERSION 1; TEMPO t (5..127, beats per minute = 4 * t, default 30); RESOLUTION r (1..127,
 * a duration unit is 1/r of a whole note, default 64); BLOCK_START n ... BLOCK_END n (0..127, defined before use, no nesting);
 * PLAY_BLOCK n; SET_VOLUME v (0..100, default 100); REPEAT m (2..127) followed by one tone or PLAY_BLOCK; a tone is a note 0..127
 * (60 = C4) or SILENCE followed by a duration 1..127 units. One unit lasts 60 000 000 / (tempo * resolution) microseconds.
 * Strict: every violation is an {@link IllegalArgumentException}. TEMPO / RESOLUTION / SET_VOLUME are accepted anywhere between
 * events and apply from there on (UNVERIFIED: the javadoc grammar puts them in a header).
 */
public final class ToneSeq {
    public static final byte C4 = 60, SILENCE = -1, VERSION = -2, TEMPO = -3, RESOLUTION = -4, BLOCK_START = -5, BLOCK_END = -6,
            PLAY_BLOCK = -7, SET_VOLUME = -8, REPEAT = -9;

    private final byte[] s;
    private int tempo = 30, res = 64, vol = 100;
    private final int[] blockFrom = new int[128], blockTo = new int[128];
    private final boolean[] defined = new boolean[128];
    private boolean firstTempoSeen;
    private int baseTempo = 30;
    private boolean anyEvent;

    private ToneSeq(byte[] s) {
        this.s = s;
    }

    /** @throws IllegalArgumentException when the sequence is null or violates the format */
    public static Seq compile(byte[] seq) {
        if (seq == null) throw new IllegalArgumentException("null sequence");
        if (seq.length < 2 || seq[0] != VERSION || seq[1] != 1) throw new IllegalArgumentException("sequence must start with VERSION, 1");
        ToneSeq c = new ToneSeq(seq);
        Seq out = new Seq();
        int end = c.run(2, seq.length, out, false);
        if (end != seq.length) throw new IllegalArgumentException("unexpected BLOCK_END");
        out.baseTempoMilli = c.baseTempo * 4000;
        return out;
    }

    private static int b(byte[] s, int i, int lo, int hi, String what) {
        if (i >= s.length) throw new IllegalArgumentException("sequence ends inside " + what);
        int v = s[i];
        if (v < lo || v > hi) throw new IllegalArgumentException(what + " " + v + " out of range " + lo + ".." + hi + " at " + i);
        return v;
    }

    private void tone(int note, int units, Seq out) {
        anyEvent = true;
        if (out != null) out.add(note, units * 60000000L / ((long) tempo * res), vol);
    }

    /**
     * Interprets s[from, to). With {@code inDef}: validation of a block body, ends at the BLOCK_END (its index is returned, out is
     * null); otherwise returns {@code to}.
     */
    private int run(int from, int to, Seq out, boolean inDef) {
        int i = from;
        while (i < to) {
            int e = s[i];
            if (e >= 0 || e == SILENCE) {
                if (e >= 0 && e > 127) throw new IllegalArgumentException("note");
                int d = b(s, i + 1, 1, 127, "duration");
                tone(e, d, out);
                i += 2;
            } else if (e == TEMPO) {
                tempo = b(s, i + 1, 5, 127, "TEMPO");
                if (!firstTempoSeen && !anyEvent) baseTempo = tempo;
                firstTempoSeen = true;
                i += 2;
            } else if (e == RESOLUTION) {
                res = b(s, i + 1, 1, 127, "RESOLUTION");
                i += 2;
            } else if (e == SET_VOLUME) {
                vol = b(s, i + 1, 0, 100, "SET_VOLUME");
                i += 2;
            } else if (e == BLOCK_START) {
                if (inDef) throw new IllegalArgumentException("nested BLOCK_START at " + i);
                if (out == null && !inDef) throw new IllegalArgumentException("BLOCK_START in a replayed block");
                int n = b(s, i + 1, 0, 127, "block number");
                if (defined[n]) throw new IllegalArgumentException("block " + n + " defined twice");
                int saveT = tempo, saveR = res, saveV = vol;
                boolean saveA = anyEvent;
                int endAt = run(i + 2, s.length, null, true);
                tempo = saveT;
                res = saveR;
                vol = saveV;
                anyEvent = saveA;
                if (endAt >= s.length) throw new IllegalArgumentException("BLOCK_START " + n + " without BLOCK_END");
                if (b(s, endAt + 1, 0, 127, "block number") != n) throw new IllegalArgumentException("BLOCK_END does not match BLOCK_START " + n);
                blockFrom[n] = i + 2;
                blockTo[n] = endAt;
                defined[n] = true;
                i = endAt + 2;
            } else if (e == BLOCK_END) {
                if (!inDef) throw new IllegalArgumentException("BLOCK_END without BLOCK_START at " + i);
                return i;
            } else if (e == PLAY_BLOCK) {
                i = play(i, out);
            } else if (e == REPEAT) {
                int m = b(s, i + 1, 2, 127, "REPEAT multiplier");
                if (i + 2 >= s.length) throw new IllegalArgumentException("REPEAT without event");
                int next = s[i + 2];
                if (next == PLAY_BLOCK) {
                    int save = i;
                    int after = i + 2;
                    for (int k = 0; k < m; k++) after = play(save + 2, out == null ? null : out);
                    i = after;
                } else if (next >= 0 || next == SILENCE) {
                    int d = b(s, i + 3, 1, 127, "duration");
                    if (next > 127) throw new IllegalArgumentException("note");
                    for (int k = 0; k < m; k++) tone(next, d, out);
                    i += 4;
                } else throw new IllegalArgumentException("REPEAT must be followed by a tone or PLAY_BLOCK at " + i);
            } else {
                throw new IllegalArgumentException("unknown event " + e + " at " + i);
            }
        }
        if (inDef) throw new IllegalArgumentException("BLOCK_START without BLOCK_END");
        return to;
    }

    /** PLAY_BLOCK at index i: validates the number, plays the block when output is wanted; returns the index after the event. */
    private int play(int i, Seq out) {
        int n = b(s, i + 1, 0, 127, "block number");
        if (!defined[n]) throw new IllegalArgumentException("block " + n + " is not defined");
        if (out != null) run(blockFrom[n], blockTo[n], out, false);
        return i + 2;
    }
}

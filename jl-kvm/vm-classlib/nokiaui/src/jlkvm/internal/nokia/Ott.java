/*
 * jl-kvm guest class library (Nokia UI layer). Origin: original code written for jl-kvm (PLAN D12, T28). License: Apache-2.0.
 * Parser of the Nokia Smart Messaging ringing tone (OTA / OTT bit stream) used by com.nokia.mid.sound.Sound (FORMAT_TONE). The layout
 * (command part, sound, song type, patterns, instruction ids and sizes) is the one the Nokia UI API 1.1 javadoc names (basic and
 * temporary song); the exact bit tables are not in the javadoc and follow what the reference emulators decode: UNVERIFIED, docs/nokia.md.
 */
package jlkvm.internal.nokia;

import jlkvm.internal.media.tone.Seq;

/**
 * Bit stream, most significant bit first:
 * <pre>
 *   command length (8)  | command 0x4A = ringing tone programming (8) | 0x1D "sound" (7)  or  0x22 "unicode" (7) + 1 filler + 0x1D (7)
 *   song type (3): 1 basic (title: length 4, then characters of 8 - 16 with unicode - bits), 2 temporary (no title); 3..5 unsupported
 *   pattern count (8), then per pattern: header id 000 (3), pattern id (2), loop (4), instruction count (8), instructions
 *   instruction id (3): 001 note (value 4, duration 3, specifier 2) | 010 scale (2) | 011 style (2) | 100 tempo (5) | 101 volume (4)
 * </pre>
 * A pattern whose instruction count is 0 plays the last defined pattern again. Loop value n plays a pattern n + 1 times, 15 (endless) once.
 */
public final class Ott {
    private Ott() {
    }

    public static final int DEFAULT_BPM = 63;
    private static final int[] TEMPO = {25, 28, 31, 35, 40, 45, 50, 56, 63, 70, 80, 90, 100, 112, 125, 140, 160, 180, 200, 225, 250, 285, 320, 355,
        400, 450, 500, 565, 635, 715, 800, 900};
    private static final int[] VOLUME = {0, 48, 56, 64, 72, 80, 88, 92, 100, 104, 108, 112, 116, 120, 124, 127};

    private static final class Bits {
        final byte[] d;
        int pos;

        Bits(byte[] d, int pos) {
            this.d = d;
            this.pos = pos;
        }

        int read(int n) {
            if (pos + n > d.length * 8) throw new IllegalArgumentException("OTT: data ends inside the ringing tone");
            int v = 0;
            for (int i = 0; i < n; i++) {
                v = (v << 1) | ((d[pos >> 3] >> (7 - (pos & 7))) & 1);
                pos++;
            }
            return v;
        }
    }

    /** Running state of the instructions: tempo and volume go on from pattern to pattern, scale and style restart with every defined pattern. */
    private static final class State {
        int scale = 1, style = 0, bpm = DEFAULT_BPM, volume = 100;
        boolean tempoSet;
    }

    public static Seq parse(byte[] data) {
        if (data == null) throw new IllegalArgumentException("null data");
        Seq out = new Seq();
        out.baseTempoMilli = DEFAULT_BPM * 1000;
        Bits b = new Bits(data, 0);
        try {
            read(b, out);
        } catch (IllegalArgumentException e) {
            if (out.n == 0) throw e;   // a tone cut short or with an unknown instruction after notes plays what was understood
        }
        if (out.n == 0) throw new IllegalArgumentException("OTT: no notes");
        return out;
    }

    private static void read(Bits b, Seq out) {
        if (b.read(8) == 0) throw new IllegalArgumentException("OTT: no command");
        int cmd = b.read(8);
        if (cmd != 0x4A) throw new IllegalArgumentException("OTT: not a ringing tone programming command: " + cmd);
        int sub = b.read(7);
        int charBits = 8;
        if (sub == 0x22) {
            charBits = 16;
            b.read(1);
            sub = b.read(7);
        }
        if (sub != 0x1D) throw new IllegalArgumentException("OTT: no sound command");
        int type = b.read(3);
        if (type == 1) {
            int len = b.read(4);
            for (int i = 0; i < len; i++) b.read(charBits);
        } else if (type != 2) {
            throw new IllegalArgumentException("OTT: unsupported song type " + type);
        }
        int patterns = b.read(8);
        State st = new State();
        int lastPos = -1, lastCount = 0;
        for (int p = 0; p < patterns; p++) {
            if (b.read(3) != 0) throw new IllegalArgumentException("OTT: pattern header expected");
            b.read(2);
            int loop = b.read(4);
            int count = b.read(8);
            int times = loop == 15 ? 1 : loop + 1;
            if (count == 0) {
                if (lastPos < 0) throw new IllegalArgumentException("OTT: pattern 0 instructions without an earlier pattern");
                for (int t = 0; t < times; t++) run(new Bits(b.d, lastPos), lastCount, st, out);
                continue;
            }
            st.scale = 1;
            st.style = 0;
            int start = b.pos;
            run(b, count, st, out);
            lastPos = start;
            lastCount = count;
            for (int t = 1; t < times; t++) run(new Bits(b.d, start), count, st, out);
        }
    }

    private static void run(Bits b, int count, State st, Seq out) {
        for (int i = 0; i < count; i++) {
            int id = b.read(3);
            switch (id) {
                case 1: {
                    int value = b.read(4), dur = b.read(3), spec = b.read(2);
                    note(out, st, value, dur, spec);
                    break;
                }
                case 2:
                    st.scale = b.read(2);
                    break;
                case 3: {
                    int s = b.read(2);
                    if (s <= 2) st.style = s;   // 3 is reserved
                    break;
                }
                case 4:
                    st.bpm = TEMPO[b.read(5)];
                    if (out.n == 0 && !st.tempoSet) out.baseTempoMilli = st.bpm * 1000;
                    st.tempoSet = true;
                    break;
                case 5:
                    st.volume = VOLUME[b.read(4)] * 100 / 127;
                    break;
                default:
                    throw new IllegalArgumentException("OTT: unknown instruction " + id);
            }
        }
    }

    private static void note(Seq out, State st, int value, int dur, int spec) {
        long whole = 240000000L / st.bpm;   // microseconds
        int d = dur >= 1 && dur <= 6 ? dur : 3;   // 0 and 7 are not defined: a quarter note (UNVERIFIED)
        long us = whole >> (d - 1);
        if (spec == 1) us = us * 3 / 2;
        else if (spec == 2) us = us * 7 / 4;
        else if (spec == 3) us = us * 2 / 3;
        if (value == 0 || value > 12) {   // pause; 13..15 are reserved: pause (UNVERIFIED)
            out.add(-1, us, st.volume);
            return;
        }
        int midi = 60 + 12 * st.scale + (value - 1);
        long sound = st.style == 1 ? us : (st.style == 2 ? us / 2 : us * 20 / 21);
        out.add(midi, sound, st.volume);
        if (us - sound > 0) out.add(-1, us - sound, st.volume);
    }
}

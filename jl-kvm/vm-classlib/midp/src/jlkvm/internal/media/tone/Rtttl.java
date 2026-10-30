/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 * RTTTL / RTX (Nokia ringing tone text transfer language) parser; shared by the MMAPI player (T27) and, later, the Smart Messaging
 * ringing tone (OTT) of T32.
 */
package jlkvm.internal.media.tone;

/**
 * {@code name:d=4,o=5,b=63:8a,8p,16c#6,4e.,...}
 * <ul>
 * <li>Case-insensitive, blanks and line breaks are ignored; a UTF BOM at the start is skipped. The name may be empty or longer than the
 * 10 characters of the original specification.</li>
 * <li>The defaults section may be empty or lack any of d, o, b (defaults d=4, o=6, b=63: the usual values; UNVERIFIED against a
 * Nokia). Order is free. d must be 1, 2, 4, 8, 16 or 32, o 0..8 (the original specification names 4..7), b 1..1000.</li>
 * <li>A note: {@code [duration] letter [#] [.] [octave] [.]} with letter a..g (h = b) or p (pause); the dot lengthens by one half
 * and is accepted before or after the octave. Octave 4 contains A4 = 440 Hz: MIDI number {@code 12 * (octave + 1) + semitone}.</li>
 * <li>Empty list items (",,") are ignored; anything else that is not a note is an {@link IllegalArgumentException}. A tone that
 * would be above MIDI note 127 is rejected.</li>
 * </ul>
 * Durations: a whole note lasts {@code 4 * 60 000 000 / bpm} microseconds, a note {@code whole / d} (integer division), dotted * 3 / 2.
 */
public final class Rtttl {
    private Rtttl() {
    }

    public static final int DEF_DURATION = 4, DEF_OCTAVE = 6, DEF_BPM = 63;
    private static final int[] SEMITONE = {9, 11, 0, 2, 4, 5, 7};   // a b c d e f g

    /** The name of the tone (text before the first colon, trimmed); parse errors give null here too. */
    public static String name(String rtttl) {
        if (rtttl == null) return null;
        int c = rtttl.indexOf(':');
        return c < 0 ? null : rtttl.substring(0, c).trim();
    }

    public static Seq parse(byte[] data) {
        if (data == null) throw new IllegalArgumentException("null data");
        try {
            return parse(new String(data, "ISO-8859-1"));
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalArgumentException("encoding");
        }
    }

    public static Seq parse(String text) {
        if (text == null) throw new IllegalArgumentException("null text");
        int start = 0;
        if (text.length() > 0 && text.charAt(0) == '﻿') start = 1;
        else if (text.startsWith("ï»¿")) start = 3;   // UTF-8 BOM read as ISO-8859-1
        int c1 = text.indexOf(':', start);
        if (c1 < 0) throw new IllegalArgumentException("RTTTL: no ':' after the name");
        int c2 = text.indexOf(':', c1 + 1);
        if (c2 < 0) throw new IllegalArgumentException("RTTTL: no ':' after the defaults");
        int d = DEF_DURATION, o = DEF_OCTAVE, bpm = DEF_BPM;
        String defs = text.substring(c1 + 1, c2);
        int p = 0;
        int n = defs.length();
        while (p < n) {
            char ch = lower(defs.charAt(p));
            if (ch == ' ' || ch == ',' || ch == '\t' || ch == '\r' || ch == '\n') {
                p++;
                continue;
            }
            if (ch != 'd' && ch != 'o' && ch != 'b') throw new IllegalArgumentException("RTTTL: unknown default '" + defs.charAt(p) + "'");
            p++;
            while (p < n && isBlank(defs.charAt(p))) p++;
            if (p >= n || defs.charAt(p) != '=') throw new IllegalArgumentException("RTTTL: '=' expected after '" + ch + "'");
            p++;
            while (p < n && isBlank(defs.charAt(p))) p++;
            int v = 0, digits = 0;
            while (p < n && defs.charAt(p) >= '0' && defs.charAt(p) <= '9') {
                if (digits++ > 5) throw new IllegalArgumentException("RTTTL: number too long");
                v = v * 10 + (defs.charAt(p) - '0');
                p++;
            }
            if (digits == 0) throw new IllegalArgumentException("RTTTL: number expected after '" + ch + "='");
            if (ch == 'd') {
                if (!validDuration(v)) throw new IllegalArgumentException("RTTTL: default duration " + v);
                d = v;
            } else if (ch == 'o') {
                if (v > 8) throw new IllegalArgumentException("RTTTL: default octave " + v);
                o = v;
            } else {
                if (v < 1 || v > 1000) throw new IllegalArgumentException("RTTTL: bpm " + v);
                bpm = v;
            }
        }
        Seq seq = new Seq();
        seq.baseTempoMilli = bpm * 1000;
        long whole = 4L * 60000000L / bpm;
        String notes = text.substring(c2 + 1);
        n = notes.length();
        p = 0;
        while (p < n) {
            char ch = notes.charAt(p);
            if (isBlank(ch) || ch == ',') {
                p++;
                continue;
            }
            int dur = d, oct = o;
            boolean dot = false;
            if (ch >= '0' && ch <= '9') {
                int v = 0, digits = 0;
                while (p < n && notes.charAt(p) >= '0' && notes.charAt(p) <= '9') {
                    if (digits++ > 3) throw new IllegalArgumentException("RTTTL: duration too long at " + p);
                    v = v * 10 + (notes.charAt(p) - '0');
                    p++;
                }
                if (!validDuration(v)) throw new IllegalArgumentException("RTTTL: duration " + v + " at " + (p - 1));
                dur = v;
                while (p < n && isBlank(notes.charAt(p))) p++;
            }
            if (p >= n) throw new IllegalArgumentException("RTTTL: note letter expected at the end");
            char letter = lower(notes.charAt(p));
            int semi;
            boolean pause = false;
            if (letter == 'p') {
                pause = true;
                semi = 0;
            } else if (letter == 'h') {
                semi = 11;
            } else if (letter >= 'a' && letter <= 'g') {
                semi = SEMITONE[letter - 'a'];
            } else {
                throw new IllegalArgumentException("RTTTL: bad note '" + notes.charAt(p) + "' at " + p);
            }
            p++;
            if (!pause && p < n && notes.charAt(p) == '#') {
                semi++;
                p++;
            }
            while (p < n && isBlank(notes.charAt(p))) p++;
            if (p < n && notes.charAt(p) == '.') {
                dot = true;
                p++;
                while (p < n && isBlank(notes.charAt(p))) p++;
            }
            if (p < n && notes.charAt(p) >= '0' && notes.charAt(p) <= '9') {
                if (pause) throw new IllegalArgumentException("RTTTL: octave on a pause at " + p);
                oct = notes.charAt(p) - '0';
                p++;
                if (oct > 8) throw new IllegalArgumentException("RTTTL: octave " + oct);
                if (p < n && notes.charAt(p) == '.') {
                    dot = true;
                    p++;
                }
            }
            while (p < n && isBlank(notes.charAt(p))) p++;
            if (p < n && notes.charAt(p) != ',') throw new IllegalArgumentException("RTTTL: ',' expected at " + p + " instead of '" + notes.charAt(p) + "'");
            long us = whole / dur;
            if (dot) us = us * 3 / 2;
            if (pause) {
                seq.add(-1, us, 100);
            } else {
                int midi = 12 * (oct + 1) + semi;
                if (midi > 127) throw new IllegalArgumentException("RTTTL: note above 127");
                seq.add(midi, us, 100);
            }
        }
        return seq;
    }

    private static boolean validDuration(int v) {
        return v == 1 || v == 2 || v == 4 || v == 8 || v == 16 || v == 32;
    }

    private static boolean isBlank(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    private static char lower(char c) {
        return c >= 'A' && c <= 'Z' ? (char) (c + 32) : c;
    }
}

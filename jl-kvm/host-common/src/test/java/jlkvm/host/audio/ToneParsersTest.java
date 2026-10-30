package jlkvm.host.audio;

import static org.junit.Assert.*;

import jlkvm.internal.media.tone.Rtttl;
import jlkvm.internal.media.tone.Seq;
import jlkvm.internal.media.tone.ToneSeq;
import org.junit.Test;

/** T27: the RTTTL / RTX parser and the MIDP tone sequence compiler (guest code, compiled here as plain Java). */
public class ToneParsersTest {
    private static String dump(Seq s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.n; i++) sb.append(i == 0 ? "" : " ").append(s.note[i]).append('/').append(s.us[i]).append('/').append(s.vol[i]);
        return sb.toString();
    }

    private static void bad(String text) {
        try {
            Rtttl.parse(text);
            fail("accepted: " + text);
        } catch (IllegalArgumentException expected) {
            assertNotNull(expected.getMessage());
        }
    }

    // ------------------------------------------------------------------ RTTTL
    @Test public void rtttlBasic() {
        // b=120: whole note 2 s; 8th 250 ms; dotted quarter 750 ms; pause 500 ms; half 1 s. c5 = 72, d#5 = 75, e5 = 76, a6 = 93
        Seq s = Rtttl.parse("Test:d=4,o=5,b=120:8c,8d#,e.,p,2a6");
        assertEquals("72/250000/100 75/250000/100 76/750000/100 -1/500000/100 93/1000000/100", dump(s));
        assertEquals(120000, s.baseTempoMilli);
        assertEquals(2750000L, s.totalUs);
        assertEquals("Test", Rtttl.name("Test:d=4,o=5,b=120:8c"));
    }

    @Test public void rtttlMissingDefaultsUseTheUsualValues() {
        // d=4, o=6, b=63: a quarter = 240 000 000 / 63 / 4 = 952380 us; c6 = 84
        assertEquals("84/952380/100 86/476190/100", dump(Rtttl.parse("x::c,8d")));
        assertEquals(63000, Rtttl.parse("x::c").baseTempoMilli);
        // only some defaults, any order
        assertEquals("69/500000/100", dump(Rtttl.parse("x:b=120,o=4:a")));
        assertEquals("69/250000/100", dump(Rtttl.parse("x:b=120,d=8,o=4:a")));
        assertEquals("57/500000/100", dump(Rtttl.parse("x:o=3,b=120:a")));
        assertEquals("", dump(Rtttl.parse("x:d=4:")));   // no notes at all: empty tone
    }

    @Test public void rtttlOctaveDurationAndDots() {
        // b=60: whole = 4 s
        assertEquals("60/4000000/100", dump(Rtttl.parse("a:b=60:1c4")));
        assertEquals("60/2000000/100", dump(Rtttl.parse("a:b=60:2c4")));
        assertEquals("60/1000000/100", dump(Rtttl.parse("a:b=60:4c4")));
        assertEquals("60/500000/100", dump(Rtttl.parse("a:b=60:8c4")));
        assertEquals("60/250000/100", dump(Rtttl.parse("a:b=60:16c4")));
        assertEquals("60/125000/100", dump(Rtttl.parse("a:b=60:32c4")));
        // the dot lengthens by one half, before or after the octave, also on a pause
        assertEquals("60/1500000/100", dump(Rtttl.parse("a:b=60:4c.4")));
        assertEquals("60/1500000/100", dump(Rtttl.parse("a:b=60:4c4.")));
        assertEquals("-1/750000/100", dump(Rtttl.parse("a:b=60:8p.")));
        assertEquals("-1/187500/100", dump(Rtttl.parse("a:b=60:32p.")));
        // sharps, h = b, the whole scale of octave 4
        assertEquals("60/1000000/100 61/1000000/100 62/1000000/100 63/1000000/100 64/1000000/100 65/1000000/100 66/1000000/100 67/1000000/100"
                + " 68/1000000/100 69/1000000/100 70/1000000/100 71/1000000/100 71/1000000/100", dump(Rtttl.parse("a:b=60,o=4:c,c#,d,d#,e,f,f#,g,g#,a,a#,b,h")));
        // lowest and highest tones
        assertEquals("12/1000000/100", dump(Rtttl.parse("a:b=60,o=0:c")));
        assertEquals("119/1000000/100", dump(Rtttl.parse("a:b=60,o=8:b")));
    }

    @Test public void rtttlCaseBlanksBomAndLongNames() {
        String lower = dump(Rtttl.parse("Name:d=8,o=5,b=100:c#,e,p,4g.6"));
        assertEquals(lower, dump(Rtttl.parse("NAME:D=8, O=5, B=100:C#,E,P,4G.6")));
        assertEquals(lower, dump(Rtttl.parse("﻿Name :\r\n d = 8 , o = 5 , b = 100 :\r\n c# , e ,\r\n p , 4 g . 6\r\n")));
        assertEquals(lower, dump(Rtttl.parse("ï»¿Name:d=8,o=5,b=100:c#,,e,p,4g.6,")));   // BOM read as ISO-8859-1, empty items
        String longName = "A ringtone name far longer than the ten characters of the specification";
        assertEquals(lower, dump(Rtttl.parse(longName + ":d=8,o=5,b=100:c#,e,p,4g.6")));
        assertEquals(longName, Rtttl.name(longName + ":d=8:c"));
        assertEquals(lower, dump(Rtttl.parse(":d=8,o=5,b=100:c#,e,p,4g.6".getBytes(java.nio.charset.StandardCharsets.ISO_8859_1))));   // empty name, byte[] input
    }

    @Test public void rtttlBpmDecidesTheLengths() {
        assertEquals("69/1000000/100", dump(Rtttl.parse("x:b=60:a4")));
        assertEquals("69/500000/100", dump(Rtttl.parse("x:b=120:a4")));
        assertEquals("69/240000/100", dump(Rtttl.parse("x:b=250:a4")));
        assertEquals("69/967741/100", dump(Rtttl.parse("x:b=62:a4")));   // integer microseconds: 240000000 / 62 = 3870967, / 4
        assertEquals(900000, Rtttl.parse("x:b=900:a").baseTempoMilli);
        assertEquals(1000, Rtttl.parse("x:b=1:a").baseTempoMilli);
    }

    @Test public void rtttlErrors() {
        bad("no colons at all");
        bad("name:d=4,o=5,b=63");                 // second colon missing
        bad("name:d=3:c");                         // duration not a power of two
        bad("name:d=64:c");
        bad("name:d=0:c");
        bad("name:o=9:c");
        bad("name:b=0:c");
        bad("name:b=1001:c");
        bad("name:x=4:c");                         // unknown default
        bad("name:d:c");                           // no '='
        bad("name:d=:c");                          // no number
        bad("name:d=4:3c");                        // note duration 3
        bad("name:d=4:c9");                        // octave 9
        bad("name:d=4:x");                         // not a note
        bad("name:d=4:cc");                        // two letters without comma
        bad("name:d=4:p#");                        // no sharp pause
        bad("name:d=4:p5");                        // no octave on a pause
        bad("name:d=4:8");                         // duration without a note
        bad("name:d=4:c c");                       // missing comma
        try { Rtttl.parse((String) null); fail(); } catch (IllegalArgumentException expected) { }
        try { Rtttl.parse((byte[]) null); fail(); } catch (IllegalArgumentException expected) { }
    }

    // ------------------------------------------------------------------ MIDP tone sequence
    private static final byte VER = ToneSeq.VERSION, TEMPO = ToneSeq.TEMPO, RES = ToneSeq.RESOLUTION, BS = ToneSeq.BLOCK_START, BE = ToneSeq.BLOCK_END,
            PB = ToneSeq.PLAY_BLOCK, VOL = ToneSeq.SET_VOLUME, REP = ToneSeq.REPEAT, SIL = ToneSeq.SILENCE;

    private static void badSeq(int... v) {
        byte[] b = new byte[v.length];
        for (int i = 0; i < b.length; i++) b[i] = (byte) v[i];
        badSeq(b);
    }

    private static void badSeq(byte[] b) {
        try {
            ToneSeq.compile(b);
            fail("accepted");
        } catch (IllegalArgumentException expected) {
            assertNotNull(expected.getMessage());
        }
    }

    @Test public void toneSequenceTimingAndHeader() {
        // tempo 30 (= 120 bpm), resolution 64: one unit = 31 250 us, 16 units = a quarter note = 500 ms
        Seq s = ToneSeq.compile(new byte[] {VER, 1, 60, 16, SIL, 8, 62, 16});
        assertEquals("60/500000/100 -1/250000/100 62/500000/100", dump(s));
        assertEquals(120000, s.baseTempoMilli);
        s = ToneSeq.compile(new byte[] {VER, 1, TEMPO, 60, RES, 16, 69, 4});   // 240 bpm, 1/16 units: 4 units = a quarter = 250 ms
        assertEquals("69/250000/100", dump(s));
        assertEquals(240000, s.baseTempoMilli);
        s = ToneSeq.compile(new byte[] {VER, 1});
        assertEquals(0, s.n);
        s = ToneSeq.compile(new byte[] {VER, 1, VOL, 40, 60, 1, VOL, 0, 61, 1, VOL, 100, 127, 127});
        assertEquals("60/31250/40 61/31250/0 127/3968750/100", dump(s));
    }

    @Test public void toneSequenceBlocksAndRepeat() {
        byte[] q = {VER, 1, BS, 0, 60, 8, 62, 8, BE, 0, PB, 0, REP, 3, 64, 4, PB, 0, BS, 5, VOL, 50, 65, 1, BE, 5, PB, 5, 66, 1};
        Seq s = ToneSeq.compile(q);
        assertEquals("60/250000/100 62/250000/100 64/125000/100 64/125000/100 64/125000/100 60/250000/100 62/250000/100 65/31250/50 66/31250/50", dump(s));
        // REPEAT of a block, a block played inside another one, tempo changes apply from there on
        s = ToneSeq.compile(new byte[] {VER, 1, BS, 1, 60, 16, BE, 1, BS, 2, PB, 1, PB, 1, BE, 2, REP, 2, PB, 2, TEMPO, 60, 61, 16});
        assertEquals("60/500000/100 60/500000/100 60/500000/100 60/500000/100 61/250000/100", dump(s));
        assertEquals(2250000L, s.totalUs);
    }

    @Test public void toneSequenceErrors() {
        try { ToneSeq.compile(null); fail(); } catch (IllegalArgumentException expected) { }
        badSeq();
        badSeq(VER);
        badSeq((byte) 60, (byte) 4);                                 // no VERSION
        badSeq(VER, 2);                                              // version 2
        badSeq(VER, 1, TEMPO, 4);                                    // tempo < 5
        badSeq(VER, 1, TEMPO, (byte) 128);                           // negative byte
        badSeq(VER, 1, RES, 0);
        badSeq(VER, 1, 60, 0);                                       // duration 0
        badSeq(VER, 1, 60);                                          // note without a duration
        badSeq(VER, 1, TEMPO);                                       // value missing
        badSeq(VER, 1, VOL, 101);
        badSeq(VER, 1, REP, 1, 60, 4);                               // multiplier 1
        badSeq(VER, 1, REP, 2);                                      // nothing to repeat
        badSeq(VER, 1, REP, 2, TEMPO, 30);                           // not a tone
        badSeq(VER, 1, PB, 0);                                       // undefined block
        badSeq(VER, 1, BS, 0, 60, 4);                                // no BLOCK_END
        badSeq(VER, 1, BS, 0, 60, 4, BE, 1);                         // wrong number
        badSeq(VER, 1, BE, 0);                                       // BLOCK_END alone
        badSeq(VER, 1, BS, 0, BS, 1, BE, 1, BE, 0);                  // nested
        badSeq(VER, 1, BS, 0, 60, 4, BE, 0, BS, 0, 60, 4, BE, 0);    // defined twice
        badSeq(VER, 1, BS, 0, PB, 0, BE, 0);                         // plays itself while defined
        badSeq(VER, 1, BS, (byte) 128, BE, 0);                       // block number out of range
        badSeq(VER, 1, (byte) -20, 1);                               // unknown event
    }

    @Test public void toneSequenceTooLongIsRefused() {
        // 127 x 127 x 4 repeats of a tone: more than Seq.MAX_EVENTS events
        byte[] b = {VER, 1, BS, 0, REP, 127, 60, 1, BE, 0,
            BS, 1, PB, 0, PB, 0, PB, 0, PB, 0, PB, 0, PB, 0, PB, 0, PB, 0, BE, 1,
            BS, 2, PB, 1, PB, 1, PB, 1, PB, 1, PB, 1, PB, 1, PB, 1, PB, 1, BE, 2,
            BS, 3, PB, 2, PB, 2, PB, 2, PB, 2, PB, 2, PB, 2, PB, 2, PB, 2, BE, 3, PB, 3, PB, 3};
        badSeq(b);
    }
}

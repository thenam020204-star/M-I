package jlkvm.host.audio;

import static org.junit.Assert.*;

import jlkvm.internal.media.tone.Seq;
import jlkvm.internal.nokia.Ott;
import org.junit.Test;

/** T28: the Smart Messaging ringing tone (OTA / OTT) parser of com.nokia.mid.sound.Sound, guest code compiled here as plain Java. */
public class OttParserTest {
    /** MSB-first bit writer. */
    private static final class W {
        final byte[] b = new byte[4096];
        int n;

        W put(int bits, int v) {
            for (int i = bits - 1; i >= 0; i--) {
                if (((v >> i) & 1) != 0) b[n >> 3] |= (byte) (0x80 >> (n & 7));
                n++;
            }
            return this;
        }

        byte[] done() {
            byte[] r = new byte[(n + 7) >> 3];
            System.arraycopy(b, 0, r, 0, r.length);
            return r;
        }
    }

    private static W basic(String title, int patterns) {
        W w = new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 1).put(4, title.length());
        for (int i = 0; i < title.length(); i++) w.put(8, title.charAt(i));
        return w.put(8, patterns);
    }

    private static W temporary(int patterns) { return new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 2).put(8, patterns); }

    private static W pattern(W w, int loop, int count) { return w.put(3, 0).put(2, 0).put(4, loop).put(8, count); }

    private static W note(W w, int value, int dur, int spec) { return w.put(3, 1).put(4, value).put(3, dur).put(2, spec); }

    private static String dump(Seq s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.n; i++) sb.append(i == 0 ? "" : " ").append(s.note[i]).append('/').append(s.us[i]).append('/').append(s.vol[i]);
        return sb.toString();
    }

    private static void bad(byte[] d) {
        try {
            Ott.parse(d);
            fail("accepted");
        } catch (IllegalArgumentException expected) {
            assertNotNull(expected.getMessage());
        }
    }

    @Test public void basicSongDefaults() {
        // default tempo 63 bpm: whole = 240 000 000 / 63 = 3 809 523 us; quarter 952 380; default scale 1: C = MIDI 72; natural style sounds 20/21
        Seq s = Ott.parse(note(note(pattern(basic("Test", 1), 0, 2), 1, 3, 0), 3, 2, 1).done());
        assertEquals("72/907028/100 -1/45352/100 74/2721086/100 -1/136055/100", dump(s));
        assertEquals(63000, s.baseTempoMilli);
    }

    @Test public void temporarySongTempoVolumeScaleStyle() {
        W w = pattern(temporary(1), 0, 6);
        w.put(3, 4).put(5, 12);    // tempo 100 bpm: whole 2 400 000 us
        w.put(3, 5).put(4, 1);     // volume 48/127 -> 37
        w.put(3, 3).put(2, 1);     // continuous style: the note sounds its whole length
        w.put(3, 2).put(2, 2);     // scale value 2: +12
        note(w, 8, 3, 0);          // G: 60 + 24 + 7 = 91, quarter 600 000
        note(w, 0, 4, 3);          // pause, eighth, 2/3: 300 000 * 2 / 3
        Seq s = Ott.parse(w.done());
        assertEquals("91/600000/37 -1/200000/37", dump(s));
        assertEquals(100000, s.baseTempoMilli);
    }

    @Test public void durationsAndSpecifiers() {
        W w = pattern(temporary(1), 0, 10);
        w.put(3, 4).put(5, 12).put(3, 3).put(2, 1);   // 100 bpm, continuous
        for (int d = 1; d <= 6; d++) note(w, 1, d, 0);
        note(w, 1, 3, 2);
        note(w, 1, 3, 1);
        Seq s = Ott.parse(w.done());
        assertEquals("72/2400000/100 72/1200000/100 72/600000/100 72/300000/100 72/150000/100 72/75000/100 72/1050000/100 72/900000/100", dump(s));
    }

    @Test public void patternLoopsAndRepeat() {
        W w = temporary(2);
        w.put(3, 0).put(2, 0).put(4, 1).put(8, 3).put(3, 3).put(2, 1);
        note(w, 1, 3, 0);
        note(w, 2, 3, 0);
        pattern(w, 0, 0);   // instruction count 0: the last pattern again
        Seq s = Ott.parse(w.done());
        // loop value 1 plays the pattern twice, then once more by the 0 count pattern: 3 x 2 notes
        assertEquals(6, s.n);
        assertEquals("72/952380/100 73/952380/100 72/952380/100 73/952380/100 72/952380/100 73/952380/100", dump(s));
        // loop value 15 (endless) plays once
        W e = temporary(1);
        e.put(3, 0).put(2, 0).put(4, 15).put(8, 2).put(3, 3).put(2, 1);
        note(e, 1, 3, 0);
        assertEquals(1, Ott.parse(e.done()).n);
    }

    @Test public void unicodeTitleAndReservedValues() {
        W u = new W().put(8, 2).put(8, 0x4A).put(7, 0x22).put(1, 0).put(7, 0x1D).put(3, 1).put(4, 2).put(16, 'H').put(16, 'i').put(8, 1);
        pattern(u, 0, 3);
        u.put(3, 3).put(2, 1);
        note(u, 13, 3, 0);    // reserved note value: a pause
        note(u, 12, 7, 0);    // duration 7 is not defined: a quarter note
        Seq s = Ott.parse(u.done());
        assertEquals("-1/952380/100 83/952380/100", dump(s));
    }

    @Test public void notesBeforeADamagedPartStillPlay() {
        byte[] cut = note(note(pattern(temporary(1), 0, 5), 1, 3, 0), 3, 3, 0).put(3, 7).done();
        assertEquals(4, Ott.parse(cut).n);
        // the data ending inside an instruction, after a note
        W w = note(pattern(temporary(1), 0, 4), 1, 3, 0).put(3, 1).put(4, 1);
        byte[] d = w.done();
        byte[] shorter = new byte[d.length - 1];
        System.arraycopy(d, 0, shorter, 0, shorter.length);
        assertTrue(Ott.parse(shorter).n >= 2);
    }

    @Test public void errors() {
        try {
            Ott.parse(null);
            fail();
        } catch (IllegalArgumentException expected) {
            // null data
        }
        bad(new byte[0]);
        bad(new byte[] {2});
        bad(new byte[] {0, 0x4A, 0x3A, 0x51});                                   // command length 0
        bad(new byte[] {2, 0x55, 0x3A, 0x51});                                   // not ringing tone programming
        bad(new W().put(8, 2).put(8, 0x4A).put(7, 0x11).put(3, 1).done());       // not "sound"
        bad(new W().put(8, 2).put(8, 0x4A).put(7, 0x22).put(1, 0).put(7, 0x11).put(3, 1).done());   // unicode, then not "sound"
        bad(new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 3).put(8, 1).done());    // MIDI song
        bad(new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 4).put(8, 1).done());    // digitized song
        bad(new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 5).put(8, 1).done());    // polyphonic song
        bad(new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 0).put(8, 1).done());    // song type 0
        bad(temporary(0).done());                                                  // no pattern
        bad(pattern(temporary(1), 0, 1).put(3, 2).put(2, 1).done());               // a pattern without a note
        bad(pattern(temporary(1), 0, 0).done());                                   // repeat with no earlier pattern
        bad(pattern(temporary(1), 0, 2).put(3, 1).done());                         // cut inside the first note
        bad(pattern(temporary(1), 0, 1).put(3, 7).put(8, 0).done());              // unknown instruction before any note
        bad(new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 2).put(8, 1).put(3, 3).put(14, 0).done());   // no pattern header id
        bad(basic("abcdefghijklmno", 1).done());                                   // title cut off / no patterns follow
    }

    @Test public void manyNotesAndLoops() {
        W w = temporary(1);
        w.put(3, 0).put(2, 0).put(4, 14).put(8, 254).put(3, 3).put(2, 1);
        for (int i = 0; i < 253; i++) note(w, 1, 6, 0);
        // 253 notes x 15 plays = 3795 notes: fine
        assertEquals(3795, Ott.parse(w.done()).n);
    }
}

package jlkvm.host.audio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/** The Android audio state machine on fake engines (PLAN T35c): routing, loops, end reports, pause / resume, raw MIDI, tones. */
public class AudioRouterTest {
    static final class FakeBackend implements ClipBackend {
        final List<String> log = new ArrayList<String>();
        EndListener l;
        int next = 1;
        boolean refuse;
        public void setEndListener(EndListener l) { this.l = l; }
        public int open(byte[] d, String mime) { log.add("open " + mime); return refuse ? -1 : next++; }
        public long durationMs(int h) { return 1500; }
        public void start(int h) { log.add("start " + h); }
        public void pause(int h) { log.add("pause " + h); }
        public long positionMs(int h) { return 42; }
        public void seek(int h, long ms) { log.add("seek " + h + " " + ms); }
        public void setVolume(int h, float v) { log.add("vol " + h + " " + v); }
        public void close(int h) { log.add("close " + h); }
    }

    static final class FakeSynth implements SynthLibrary {
        final List<String> log = new ArrayList<String>();
        final Map<Long, EventListener> listeners = new HashMap<Long, EventListener>();
        final List<byte[]> midi = new ArrayList<byte[]>();
        long next = 100;
        boolean failData;
        public long createPlayer(String locator) { log.add("create " + locator); return next++; }
        public void setEventListener(long h, EventListener l) { listeners.put(h, l); }
        public void setDataSource(long h, byte[] d) throws Exception { if (failData) throw new Exception("bad"); log.add("data " + h + " " + d.length); }
        public void realize(long h) { log.add("realize " + h); }
        public void prefetch(long h) { log.add("prefetch " + h); }
        public void start(long h) { log.add("start " + h); }
        public void pause(long h) { log.add("pause " + h); }
        public void deallocate(long h) { }
        public void close(long h) { log.add("close " + h); }
        public void release(long h) { log.add("release " + h); }
        public long setMediaTime(long h, long us) { log.add("time " + h + " " + us); return us; }
        public long getMediaTime(long h) { return 2500000; }
        public void setRepeat(long h, int n) { log.add("repeat " + h + " " + n); }
        public void setVolume(long h, float l, float r) { log.add("vol " + h + " " + l); }
        public long getDuration(long h) { return 3000000; }
        public int writeMIDI(long h, byte[] d, int off, int len) { byte[] c = new byte[len]; System.arraycopy(d, off, c, 0, len); midi.add(c); return len; }
    }

    static final class FakeTone implements ToneOut {
        final List<String> log = new ArrayList<String>();
        public void play(short[] pcm, int rate) { log.add("play " + pcm.length + " " + rate); }
        public void stop() { log.add("stop"); }
    }

    static final byte[] MIDI = {'M', 'T', 'h', 'd', 0, 0, 0, 6};
    static final byte[] WAV = {'R', 'I', 'F', 'F', 0, 0, 0, 0};

    final FakeBackend be = new FakeBackend();
    final FakeSynth clipSynth = new FakeSynth(), raw = new FakeSynth();
    final FakeTone tone = new FakeTone();
    final AudioRouter r = new AudioRouter(be, clipSynth, raw, tone);
    final List<Integer> ended = new ArrayList<Integer>();

    { r.setListener(new AudioDevice.Listener() { public void ended(int clip) { ended.add(clip); } }); }

    @Test public void midiDetection() {
        assertTrue(AudioRouter.isMidi("audio/midi", WAV));
        assertTrue(AudioRouter.isMidi("Audio/X-MIDI; foo=bar", null));
        assertTrue(AudioRouter.isMidi(null, MIDI));
        assertTrue(AudioRouter.isMidi("application/octet-stream", MIDI));
        assertFalse(AudioRouter.isMidi("audio/x-wav", WAV));
        assertFalse(AudioRouter.isMidi(null, null));
    }

    @Test public void routesMidiToTheSynthAndOthersToTheBackend() {
        int m = r.open(MIDI, "audio/midi");
        int w = r.open(WAV, "audio/x-wav");
        assertTrue(m > 0 && w > 0 && m != w);
        assertEquals("[create device://tone, data 100 8, realize 100]", clipSynth.log.toString());
        assertEquals("[open audio/x-wav]", be.log.toString());
        assertEquals(3000, r.durationMs(m));
        assertEquals(1500, r.durationMs(w));
        assertEquals(2500, r.positionMs(m));
        assertEquals(42, r.positionMs(w));
    }

    @Test public void midiWithoutSynthFallsBackToThePlatformPlayer() {
        AudioRouter n = new AudioRouter(be, null, null, tone);
        assertTrue(n.open(MIDI, "audio/midi") > 0);
        assertEquals("[open audio/midi]", be.log.toString());
    }

    @Test public void synthRefusalFallsBackToTheBackendAndUnplayableIsMinusOne() {
        clipSynth.failData = true;
        assertTrue(r.open(MIDI, "audio/midi") > 0);
        assertTrue(clipSynth.log.contains("close 100") && clipSynth.log.contains("release 100"));
        be.refuse = true;
        assertEquals(-1, r.open(WAV, "audio/x-wav"));
        assertEquals(-1, r.open(null, "audio/x-wav"));
        assertEquals(-1, new AudioRouter(null, null, null, null).open(WAV, "audio/x-wav"));
        assertEquals(1, r.openClips());
    }

    @Test public void backendClipLoopsThenReportsEndOnce() {
        int c = r.open(WAV, "audio/x-wav");
        r.play(c, 3);
        assertEquals("[open audio/x-wav, start 1]", be.log.toString());
        be.l.ended(1);
        be.l.ended(1);
        assertTrue(ended.isEmpty());
        be.l.ended(1);
        assertEquals("[" + c + "]", ended.toString());
        be.l.ended(1);                       // stray callback after the end: nothing
        assertEquals(1, ended.size());
        r.play(c, 1);                        // replaying a finished clip starts from 0
        assertTrue(be.log.toString().endsWith("seek 1 0, start 1]"));
        be.l.ended(1);
        assertEquals(2, ended.size());
    }

    @Test public void foreverNeverEnds() {
        int c = r.open(WAV, "audio/x-wav");
        r.play(c, -1);
        for (int i = 0; i < 20; i++) be.l.ended(1);
        assertTrue(ended.isEmpty());
        r.stop(c);
        be.l.ended(1);                       // completion racing a stop is ignored
        assertTrue(ended.isEmpty());
    }

    @Test public void synthClipLifecycleAndEvents() {
        int c = r.open(MIDI, "audio/midi");
        r.volume(c, 50);
        r.play(c, 2);
        assertEquals("[create device://tone, data 100 8, realize 100, vol 100 0.5, repeat 100 2, vol 100 0.5, prefetch 100, start 100]", clipSynth.log.toString());
        clipSynth.listeners.get(100L).event(100, SynthLibrary.EVENT_RESTART, 0);
        assertTrue(ended.isEmpty());
        clipSynth.listeners.get(100L).event(100, SynthLibrary.EVENT_END, 0);
        assertEquals("[" + c + "]", ended.toString());
        r.play(c, 1);                        // second play: no second prefetch, restarts at 0
        assertTrue(clipSynth.log.toString().endsWith("repeat 100 1, vol 100 0.5, time 100 0, start 100]"));
        assertEquals(1, count(clipSynth.log, "prefetch 100"));
        r.seek(c, 700);
        assertTrue(clipSynth.log.contains("time 100 700000"));
        r.close(c);
        assertTrue(clipSynth.log.toString().endsWith("close 100, release 100]"));
        clipSynth.listeners.get(100L).event(100, SynthLibrary.EVENT_END, 0);   // closed: ignored
        assertEquals(1, ended.size());
        assertEquals(0, r.openClips());
    }

    @Test public void pauseAllAndResumeAllKeepWhatWasPlaying() {
        int a = r.open(WAV, "audio/x-wav");
        int b = r.open(MIDI, "audio/midi");
        int idle = r.open(WAV, "audio/x-wav");
        r.play(a, -1);
        r.play(b, 1);
        r.tone(69, 200, 80);
        r.midiShort(0x90, 60, 100);
        r.pauseAll();
        assertTrue(be.log.contains("pause 1") && !be.log.contains("pause " + 2));
        assertTrue(clipSynth.log.contains("pause 100"));
        assertEquals("stop", tone.log.get(tone.log.size() - 1));
        r.resumeAll();
        assertEquals(2, count(be.log, "start 1"));
        assertEquals(2, count(clipSynth.log, "start 100"));
        assertEquals(0, count(be.log, "start 2"));
        r.resumeAll();                       // second resume: nothing more
        assertEquals(2, count(be.log, "start 1"));
        assertEquals(idle, 3);
    }

    @Test public void stoppedClipIsNotResumed() {
        int a = r.open(WAV, "audio/x-wav");
        r.play(a, 1);
        r.stop(a);
        r.pauseAll();
        r.resumeAll();
        assertEquals(1, count(be.log, "start 1"));
    }

    @Test public void rawMidiMessages() {
        r.midiShort(0x90, 60, 127);          // note on
        r.midiShort(0xC1, 5, 99);            // program change: 2 bytes
        r.midiShort(0xF8, 0, 0);             // system real time: dropped
        r.midiShort(0x40, 0, 0);             // not a status: dropped
        r.midiShort(0xB0, 200, 255);         // data bytes masked to 7 bits
        r.midiLong(new byte[] {(byte) 0xF0, 1, 2, (byte) 0xF7}, 1, 3);
        r.midiLong(null, 0, 1);
        r.midiLong(new byte[2], 1, 5);
        assertEquals("[create device://midi, realize 100, prefetch 100, start 100]", raw.log.toString());
        assertEquals(4, raw.midi.size());
        assertEquals("90 3c 7f", hex(raw.midi.get(0)));
        assertEquals("c1 05", hex(raw.midi.get(1)));
        assertEquals("b0 48 7f", hex(raw.midi.get(2)));
        assertEquals("01 02 f7", hex(raw.midi.get(3)));
        r.pauseAll();
        assertEquals(96, raw.midi.get(raw.midi.size() - 1).length);   // all notes / sound off on 16 channels
        r.releaseAll();
        assertTrue(raw.log.toString().endsWith("close 100, release 100]"));
    }

    @Test public void rawMidiWithoutSynthIsHarmless() {
        AudioRouter n = new AudioRouter(be, null, null, tone);
        n.midiShort(0x90, 1, 1);
        n.midiLong(new byte[] {1}, 0, 1);
        n.pauseAll();
        n.resumeAll();
        n.releaseAll();
    }

    @Test public void tonesRenderAndReplace() {
        r.tone(69, 500, 100);
        r.tone(60, 100000, 50);              // capped at 5 s
        r.tone(60, 0, 50);                   // nothing but silence
        r.tone(60, 50, 0);
        r.silence();
        assertEquals("[stop, play 4000 8000, stop, play 40000 8000, stop, stop, stop]", tone.log.toString());
    }

    @Test public void tempoAndRateAreUnknown() {
        int c = r.open(MIDI, "audio/midi");
        assertEquals(-1, r.tempo(c, 0));
        assertEquals(-1, r.rate(c, 0));
    }

    @Test public void releaseAllClosesEverything() {
        r.open(WAV, "audio/x-wav");
        r.open(MIDI, "audio/midi");
        r.releaseAll();
        assertEquals(0, r.openClips());
        assertTrue(be.log.contains("close 1"));
        assertTrue(clipSynth.log.contains("release 100"));
    }

    private static int count(List<String> l, String s) {
        int n = 0;
        for (String x : l) if (x.equals(s)) n++;
        return n;
    }

    private static String hex(byte[] b) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < b.length; i++) s.append(i > 0 ? " " : "").append(String.format("%02x", b[i] & 255));
        return s.toString();
    }
}

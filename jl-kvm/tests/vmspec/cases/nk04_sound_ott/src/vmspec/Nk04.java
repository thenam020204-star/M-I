package vmspec;

import com.nokia.mid.sound.Sound;
import javax.microedition.midlet.MIDlet;

/**
 * com.nokia.mid.sound.Sound (Nokia UI API 1.1): state machine, argument rules, gain clamp, supported formats and the acceptance / rejection
 * of Smart Messaging ringing tones (basic and temporary song). The ringing tones are built bit by bit with the layout documented in
 * jlkvm.internal.nokia.Ott; events and timing are covered by the host test (NokiaHostTest).
 */
public class Nk04 extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    /** MSB-first bit writer. */
    static final class W {
        byte[] b = new byte[64];
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

    /** Header of a basic song with a title (command part 02 4A, sound 1D, song type 1). */
    static W basic(String title, int patterns) {
        W w = new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 1).put(4, title.length());
        for (int i = 0; i < title.length(); i++) w.put(8, title.charAt(i));
        return w.put(8, patterns);
    }

    static W temporary(int patterns) {
        return new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 2).put(8, patterns);
    }

    static W pattern(W w, int id, int loop, int count) {
        return w.put(3, 0).put(2, id).put(4, loop).put(8, count);
    }

    static W note(W w, int value, int dur, int spec) {
        return w.put(3, 1).put(4, value).put(3, dur).put(2, spec);
    }

    static String state(Sound s) { return String.valueOf(s.getState()); }

    static String tryTone(byte[] d, int type) {
        try {
            Sound s = new Sound(d, type);
            return "ok state " + s.getState();
        } catch (IllegalArgumentException e) {
            return "IllegalArgumentException";
        } catch (NullPointerException e) {
            return "NullPointerException";
        }
    }

    public void startApp() {
        p("constants " + Sound.FORMAT_TONE + " " + Sound.FORMAT_WAV + " " + Sound.SOUND_PLAYING + " " + Sound.SOUND_STOPPED + " " + Sound.SOUND_UNINITIALIZED);
        int[] f = Sound.getSupportedFormats();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < f.length; i++) sb.append(' ').append(f[i]);
        p("supported formats" + sb + " concurrent tone " + Sound.getConcurrentSoundCount(Sound.FORMAT_TONE));
        try { Sound.getConcurrentSoundCount(7); p("concurrent 7 no exception"); } catch (IllegalArgumentException e) { p("concurrent 7 IllegalArgumentException"); }

        // single tones
        Sound s = new Sound(440, 200);
        p("new tone state " + state(s) + " gain " + s.getGain());
        s.play(1);
        p("after play " + state(s));
        s.stop();
        p("after stop " + state(s));
        s.stop();
        p("stop twice " + state(s));
        s.resume();
        p("resume " + state(s));
        s.play(0);
        p("play(0) loops " + state(s));
        s.play(3);
        p("restart " + state(s));
        s.stop();
        s.release();
        p("after release " + state(s));
        s.play(1);
        p("play released " + state(s));
        s.resume();
        s.stop();
        p("resume/stop released " + state(s));
        s.release();
        s.init(880, 100);
        p("init again " + state(s));
        try { s.play(-1); p("play(-1) no exception"); } catch (IllegalArgumentException e) { p("play(-1) IllegalArgumentException"); }
        try { new Sound(440, 0); p("duration 0 no exception"); } catch (IllegalArgumentException e) { p("duration 0 IllegalArgumentException"); }
        try { new Sound(440, -5); p("duration -5 no exception"); } catch (IllegalArgumentException e) { p("duration -5 IllegalArgumentException"); }
        try { new Sound(-1, 100); p("freq -1 no exception"); } catch (IllegalArgumentException e) { p("freq -1 IllegalArgumentException"); }
        try { new Sound(100000, 100); p("freq 100000 no exception"); } catch (IllegalArgumentException e) { p("freq 100000 IllegalArgumentException"); }
        p("freq 0 (off) " + state(new Sound(0, 100)) + ", 3951 " + state(new Sound(3951, 100)) + ", 220 " + state(new Sound(220, 100)));
        try { s.init(-1, 100); } catch (IllegalArgumentException e) { }
        p("failed init leaves " + state(s));

        // gain
        s = new Sound(440, 100);
        s.setGain(100);
        p("gain " + s.getGain());
        s.setGain(-5);
        p("gain -5 -> " + s.getGain());
        s.setGain(1000);
        p("gain 1000 -> " + s.getGain());
        s.setGain(1);
        p("gain 1 -> " + s.getGain());

        // type / null
        p("null data " + tryTone(null, Sound.FORMAT_TONE));
        p("unknown type " + tryTone(new byte[] {1, 2, 3}, 2));
        p("wav garbage " + tryTone(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}, Sound.FORMAT_WAV));

        // ringing tones
        byte[] t1 = note(note(note(pattern(basic("Test", 1), 0, 0, 3), 1, 3, 0), 3, 3, 0), 5, 2, 1).done();
        p("basic song " + tryTone(t1, Sound.FORMAT_TONE));
        byte[] t2 = note(pattern(temporary(1), 0, 0, 1), 10, 3, 0).done();
        p("temporary song " + tryTone(t2, Sound.FORMAT_TONE));
        W w = pattern(basic("", 2), 0, 1, 4);
        w.put(3, 4).put(5, 12);      // tempo 100
        w.put(3, 5).put(4, 15);      // volume
        w.put(3, 2).put(2, 2);       // scale
        note(w, 8, 4, 0);
        pattern(w, 0, 0, 0);          // instruction count 0: the previous pattern again
        p("tempo/volume/scale + repeated pattern " + tryTone(w.done(), Sound.FORMAT_TONE));
        W u = new W().put(8, 2).put(8, 0x4A).put(7, 0x22).put(1, 0).put(7, 0x1D).put(3, 1).put(4, 2).put(16, 'H').put(16, 'i').put(8, 1);
        note(pattern(u, 0, 0, 1), 4, 3, 0);
        p("unicode title " + tryTone(u.done(), Sound.FORMAT_TONE));
        // errors
        p("empty " + tryTone(new byte[0], Sound.FORMAT_TONE));
        p("one byte " + tryTone(new byte[] {2}, Sound.FORMAT_TONE));
        p("wrong command " + tryTone(new byte[] {2, 0x55, 0x3A, 0x51}, Sound.FORMAT_TONE));
        W bad = new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 3).put(8, 1);
        p("song type 3 (midi) " + tryTone(bad.done(), Sound.FORMAT_TONE));
        p("digitized song type " + tryTone(new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 4).put(8, 1).done(), Sound.FORMAT_TONE));
        p("no patterns " + tryTone(temporary(0).done(), Sound.FORMAT_TONE));
        p("pattern without notes " + tryTone(pattern(temporary(1), 0, 0, 1).put(3, 2).put(2, 1).done(), Sound.FORMAT_TONE));
        p("cut inside the first note " + tryTone(pattern(temporary(1), 0, 0, 2).put(3, 1).done(), Sound.FORMAT_TONE));
        p("repeat without a pattern " + tryTone(pattern(temporary(1), 0, 0, 0).done(), Sound.FORMAT_TONE));
        p("unknown instruction first " + tryTone(pattern(temporary(1), 0, 0, 1).put(3, 7).put(8, 0).done(), Sound.FORMAT_TONE));
        p("no header id " + tryTone(new W().put(8, 2).put(8, 0x4A).put(7, 0x1D).put(3, 2).put(8, 1).put(3, 3).put(14, 0).done(), Sound.FORMAT_TONE));
        // notes understood before a damaged part still play
        byte[] cut = note(note(pattern(temporary(1), 0, 0, 5), 1, 3, 0), 3, 3, 0).put(3, 7).done();
        p("notes then an unknown instruction " + tryTone(cut, Sound.FORMAT_TONE));
        // a failed init moves a sound to uninitialized
        s = new Sound(t1, Sound.FORMAT_TONE);
        try { s.init(new byte[0], Sound.FORMAT_TONE); } catch (IllegalArgumentException e) { }
        p("failed init(byte[]) leaves " + state(s));
        s.init(t2, Sound.FORMAT_TONE);
        p("re-init with a tone " + state(s));
        s.setSoundListener(null);
        p("listener removed");
        notifyDestroyed();
    }

    public void pauseApp() {}
    public void destroyApp(boolean u) {}
}

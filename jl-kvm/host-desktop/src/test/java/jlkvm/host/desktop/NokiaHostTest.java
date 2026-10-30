package jlkvm.host.desktop;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import jlkvm.DeviceProfile;
import jlkvm.host.audio.RecordingAudioDevice;
import jlkvm.host.nokia.RecordingNokiaDevice;
import jlkvm.profile.ProfileLoader;
import org.junit.Test;

/**
 * T28 on the desktop host: the Nokia UI layer exists only for a profile that declares "nokia-ui" (D10); FullCanvas gets the soft keys as plain
 * key events, is full screen from the constructor on and refuses commands; DeviceControl reaches the host device; Sound events run on a guest
 * thread (never in the call that caused them), and a second play on a one-slot device stops the first.
 */
public class NokiaHostTest {
    private static File profile(String name) { return new File(DesktopTestSupport.root(), "profiles/" + name); }

    private static DeviceProfile load(String name) throws Exception {
        return ProfileLoader.fromJson(new String(Files.readAllBytes(profile(name).toPath()), "UTF-8"));
    }

    private static String script() throws Exception {
        return new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "tests/midlets/scripts/nokia.inp").toPath()), "UTF-8");
    }

    private HeadlessRunner.Result run(File profile, RecordingNokiaDevice dev, RecordingAudioDevice audio) throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("nokia");
        o.profile = profile;
        o.nokia = dev;
        o.audio = audio;
        o.scriptText = script();
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        assertEquals(r.stdout, HeadlessRunner.Result.EXITED, r.exitKind);
        assertEquals(r.host.guestErr.toString(), 0, r.host.guestErr.length());
        assertEquals(0, r.vm.threadViolations);
        return r;
    }

    @Test public void layerOnlyWithTheProfileApi() throws Exception {
        HeadlessRunner.Result r = run(profile("jlkvm-min.json"), new RecordingNokiaDevice(), null);
        assertEquals("> nokia-ui layer absent\n", DesktopTestSupport.onlyGuestLines(r.stdout));
        DeviceProfile df = load("jlkvm-nokia.json");
        File romDir = HeadlessRunner.findRomDir();
        assertNotNull(new RomLayers(romDir, df).readClass("com/nokia/mid/ui/FullCanvas"));
        assertNull(new RomLayers(romDir, load("jlkvm-min.json")).readClass("com/nokia/mid/ui/FullCanvas"));
        assertNull(new RomLayers(romDir, load("jlkvm-min.json")).readClass("com/nokia/mid/sound/Sound"));
    }

    private static int count(String s, String t) {
        int n = 0;
        for (int i = s.indexOf(t); i >= 0; i = s.indexOf(t, i + 1)) n++;
        return n;
    }

    @Test public void fullCanvasKeysDeviceControlAndSoundEvents() throws Exception {
        RecordingNokiaDevice dev = new RecordingNokiaDevice();
        RecordingAudioDevice audio = new RecordingAudioDevice();
        HeadlessRunner.Result r = run(profile("jlkvm-nokia.json"), dev, audio);
        String g = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertTrue(g, g.startsWith("> nokia-ui layer present\n> size before show 240x320\n"));
        assertTrue(g, g.contains("> addCommand IllegalStateException\n> setCommandListener IllegalStateException\n> setCommandListener IllegalStateException\n"));
        assertTrue(g, g.contains("> constants -6 -7 -5 -10 -11 -1 -2 -3 -4\n"));
        assertTrue(g, g.contains("> transparent image mutable true pixel 0\n"));
        // full screen, with no sizeChanged before the first paint
        assertFalse(g, g.contains("sizeChanged"));
        assertTrue(g, g.contains("> paint 240x320\n"));
        // the soft keys and the other command keys come as key events (no commands exist)
        assertTrue(g, g.contains("> keyPressed -6 name SOFT1 action 0 SOFTKEY1\n> keyPressed -7 name SOFT2 action 0 SOFTKEY2\n> keyPressed -5 name SELECT action 8 SOFTKEY3\n"));
        assertTrue(g, g.contains("> keyPressed -10 name SEND action 0 SEND\n> keyPressed -11 name END action 0 END\n"));
        assertTrue(g, g.contains("> keyPressed -1 name UP action 1 UP\n> keyPressed -2 name DOWN action 6 DOWN\n> keyPressed -3 name LEFT action 2 LEFT\n> keyPressed -4 name RIGHT action 5 RIGHT\n"));
        // DeviceControl reaches the host device
        assertEquals("[vibra start 50 300, vibra stop, lights 0 100, flash 200]", dev.log.toString());
        assertTrue(g, g.contains("> vibra 101 IllegalArgumentException\n> lights 1 IllegalArgumentException\n> level 101 IllegalArgumentException\n> flash -1 IllegalArgumentException\n> device control done\n"));
        // Sound: PLAYING at once, the listener runs on a guest thread that is not the event thread, STOPPED after the stop / the end of the tone
        assertTrue(g, g.contains("> playing state 0\n"));
        assertEquals(g, 4, count(g, "> sound event ") );
        assertTrue(g, g.contains("> sound event 0 state 0 onEventThread false\n"));
        assertTrue(g, g.contains("> sound event 1 state 1 onEventThread false\n"));
        assertFalse(g, g.contains("onEventThread true"));
        assertTrue(g, g.contains("> stopped state 1\n"));
        // one concurrent tone: the second play stops the first (as if stop() was called)
        assertTrue(g, g.contains("> states 1 0\n"));
        assertTrue(g, g.contains("> slot first event 0\n> slot second event 0\n> states 1 0\n> slot first event 1\n"));
        assertTrue(g, g.contains("> slot first event 3\n> slot second event 1\n> slot second event 3\n"));
        // the tone generator got the notes: 440 Hz = MIDI 69
        assertTrue(audio.log().toString(), audio.log().toString().contains("tone 69 100"));
    }
}

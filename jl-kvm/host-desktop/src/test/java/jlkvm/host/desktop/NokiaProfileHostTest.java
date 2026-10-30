package jlkvm.host.desktop;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import org.junit.Test;

/** T33 / T30 on the desktop host: the Nokia S40 profiles boot a MIDlet, JAD attributes change what the game sees, device limits refuse / reject. */
public class NokiaProfileHostTest {
    private static String text(String name) throws Exception {
        return new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "profiles/" + name).toPath()), "UTF-8");
    }

    private static File temp(String suffix, String content) throws Exception {
        File f = File.createTempFile("t33", suffix);
        f.deleteOnExit();
        Files.write(f.toPath(), content.getBytes("UTF-8"));
        return f;
    }

    private static final String EVENTS_JAD = "MIDlet-Name: EventsTest\nMIDlet-Vendor: jl-kvm\nMIDlet-Version: 1.0.0\nMIDlet-1: Events,,ev.EventsMIDlet\n"
        + "MIDlet-Jar-URL: events.jar\nMIDlet-Jar-Size: 1000\n";

    @Test public void everyNokiaProfileBootsAMidlet() throws Exception {
        String[] names = {"nokia-s40-dp2.0.json", "nokia-s40-2nd.json", "nokia-s40-3rd.json", "nokia-s40-5th.json", "nokia-s40-6th.json"};
        for (int i = 0; i < names.length; i++) {
            HeadlessRunner.Options o = new HeadlessRunner.Options();
            o.jar = DesktopTestSupport.midlet("hello");
            o.profile = new File(DesktopTestSupport.root(), "profiles/" + names[i]);
            HeadlessRunner.Result r = HeadlessRunner.run(o);
            assertEquals(names[i] + ": " + r.stdout, HeadlessRunner.Result.EXITED, r.exitKind);
            assertEquals(names[i], 0, r.host.guestErr.length());
        }
    }

    @Test public void originalDisplaySizeChangesWhatTheGameSees() throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("events");
        o.script = new File(DesktopTestSupport.root(), "tests/midlets/scripts/events.inp");
        o.profile = temp(".json", text("nokia-s40-6th.json").replace("\"canvasHeight\": 320", "\"canvasHeight\": 294"));
        o.jad = temp(".jad", EVENTS_JAD + "Nokia-MIDlet-Original-Display-Size: 176,208\n");
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        String out = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertTrue(out, out.contains("> showNotify\n> paint 176x182\n"));      // normal Canvas: 26 px of chrome stay
        assertTrue(out, out.contains("> sizeChanged 176x208\n> paint 176x208\n"));   // full screen mode
        assertEquals(176, r.nokiaPlan.screenW);
        assertEquals(240, r.nokiaPlan.presentW);
        // the same JAD on the DP2.0 profile (SDK has no such attribute): ignored, the game sees the 128x128 screen
        o.profile = new File(DesktopTestSupport.root(), "profiles/nokia-s40-dp2.0.json");
        r = HeadlessRunner.run(o);
        out = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertTrue(out, out.contains("> showNotify\n> paint 128x128\n"));
        assertEquals(1, r.nokiaPlan.ignored.size());
    }

    @Test public void jarLargerThanTheDeviceLimitIsRefused() throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("events");
        o.profile = new File(DesktopTestSupport.root(), "profiles/nokia-s40-dp2.0.json");
        o.jad = temp(".jad", EVENTS_JAD.replace("Jar-Size: 1000", "Jar-Size: 357001"));
        try { HeadlessRunner.run(o); fail(); } catch (IllegalStateException e) { assertTrue(e.getMessage(), e.getMessage().contains("357000")); }
        o.jad = temp(".jad", EVENTS_JAD.replace("Jar-Size: 1000", "Jar-Size: 357000"));
        o.script = new File(DesktopTestSupport.root(), "tests/midlets/scripts/events.inp");
        assertEquals(HeadlessRunner.Result.EXITED, HeadlessRunner.run(o).exitKind);
    }

    @Test public void classVersionAboveTheDeviceMaximumFailsToLink() throws Exception {
        String t = text("nokia-s40-dp2.0.json").replace("\"maxClassMajor\": 49", "\"maxClassMajor\": 45");
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("hello");
        o.profile = temp(".json", t);
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        String all = r.stdout + r.host.guestErr;
        assertTrue(all, all.contains("unsupported class file version") || all.contains("Error") || all.contains("Exception"));
        assertNotEquals(r.stdout, 0, all.length());
    }
}

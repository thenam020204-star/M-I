package jlkvm.host.desktop;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import org.junit.Test;

/** T21 (life cycle, JAD + manifest, AMS) and T22 (event loop, soft keys, key codes from the profile) on the headless desktop host. */
public class DesktopHostTest {
    private static HeadlessRunner.Options opts(File jar, String scriptName) throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = jar;
        o.script = new File(DesktopTestSupport.root(), "tests/midlets/scripts/" + scriptName);
        return o;
    }

    @Test public void lifecycleOrder() throws Exception {
        HeadlessRunner.Options o = opts(DesktopTestSupport.midlet("lifecycle"), "lifecycle.inp");
        o.jad = new File(DesktopTestSupport.root(), "tests/midlets/lifecycle/test.jad");
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        String expected =
            "> constructor\n> startApp #1\n"
            + "> name=LifecycleTest vendor=jl-kvm shared=from-jad jadOnly=from-jad manifestOnly=from-manifest missing=null\n"
            + "> name lower-case lookup=LifecycleTest\n> getAppProperty(null) NullPointerException\n"
            + "> platformRequest http=false\n> platformRequest foo ConnectionNotFoundException\n> permission=1\n"
            + "> showNotify\n"
            // 200 pause (Android onPause): hideNotify, then pauseApp
            + "> hideNotify\n> pauseApp\n"
            // 400 resume (onResume): startApp, then showNotify
            + "> startApp #2\n> showNotify\n"
            // 600 key 1: the MIDlet pauses itself, 100 ms later resumeRequest -> startApp (no display change: it never lost the screen)
            + "> key 49\n> notifyPaused\n> resumeRequest\n> startApp #3\n"
            // 1200 pause / 1400 resume
            + "> hideNotify\n> pauseApp\n> startApp #4\n> showNotify\n"
            // 1600 key 2 (refuse the next conditional destroy); 1800 destroy(false) refused; 2000 destroy(true)
            + "> key 50\n> destroyApp unconditional=false\n> refuses\n> destroyApp unconditional=true\n";
        assertEquals(expected, DesktopTestSupport.onlyGuestLines(r.stdout));
        assertEquals(HeadlessRunner.Result.EXITED, r.exitKind);
        assertEquals(0, r.vm.threadViolations);
        assertEquals("http://example.org/", r.services.platformRequests.get(0));
    }

    @Test public void secondMidletOfTheSuiteIsSelected() throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("lifecycle");
        o.midlet = 2;
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        assertEquals("> second startApp\n", DesktopTestSupport.onlyGuestLines(r.stdout));
        assertEquals(2, r.suite.midlets.size());
        assertEquals("Second", r.suite.midlet(2).name);
    }

    private static final String EVENTS_EXPECTED =
        "> keycodes UP=-1 DOWN=-2 LEFT=-3 RIGHT=-4 FIRE=-5 A=49 B=51 C=55 D=57\n"
        + "> flags repeat=true pointer=false motion=false doubleBuffered=true\n"
        + "> showNotify\n> paint 240x294\n"
        + "> keyPressed 49 action=9 name=1\n> keyReleased 49\n"
        + "> keyPressed -1 action=1 name=UP\n> keyReleased -1\n> keyPressed -2 action=6 name=DOWN\n> keyReleased -2\n"
        + "> keyPressed -3 action=2 name=LEFT\n> keyReleased -3\n> keyPressed -4 action=5 name=RIGHT\n> keyReleased -4\n"
        + "> keyPressed -5 action=8 name=SELECT\n> keyReleased -5\n> keyPressed 50 action=1 name=2\n> keyReleased 50\n"
        // key 5: repaint + serviceRepaints on the event thread paints at once; callSerially runs after the event has been delivered
        + "> keyPressed 53 action=8 name=5\n> paint 240x294\n> serviceRepaints returned\n> callSerially ran\n> keyReleased 53\n"
        // hold 1200 ms: press, repeats at 500 ms + n * 100 ms (profile key repeat), release
        + "> keyPressed 52 action=2 name=4\n> keyRepeated 52\n> keyRepeated 52\n> keyRepeated 52\n> keyRepeated 52\n> keyRepeated 52\n> keyRepeated 52\n> keyRepeated 52\n> keyReleased 52\n"
        // soft keys with commands (normal mode): left = OK, right = Back; consumed, no key events
        + "> commandAction OK\n> commandAction Back\n"
        // full-screen mode: sizeChanged + repaint; soft keys now reach keyPressed
        + "> keyPressed 48 action=0 name=0\n> sizeChanged 240x320\n> paint 240x320\n> keyReleased 48\n"
        + "> keyPressed -6 action=0 name=SOFT1\n> keyReleased -6\n"
        + "> keyPressed 48 action=0 name=0\n> sizeChanged 240x294\n> paint 240x294\n> keyReleased 48\n"
        // display size event (rotation): sizeChanged; the pointer press is dropped (profile without pointer events)
        + "> sizeChanged 320x214\n> paint 320x214\n"
        // 3600 pause / 3700 resume: hideNotify; the test MIDlet builds a new canvas in startApp; 3800 destroy
        + "> hideNotify\n> keycodes UP=-1 DOWN=-2 LEFT=-3 RIGHT=-4 FIRE=-5 A=49 B=51 C=55 D=57\n> flags repeat=true pointer=false motion=false doubleBuffered=true\n> showNotify\n"
        + "> paint 320x214\n> consistent=true\n";

    @Test public void eventOrderSoftKeysAndSingleEventThread() throws Exception {
        HeadlessRunner.Result r = HeadlessRunner.run(opts(DesktopTestSupport.midlet("events"), "events.inp"));
        assertEquals(EVENTS_EXPECTED, DesktopTestSupport.onlyGuestLines(r.stdout));
        assertEquals(0, r.vm.threadViolations);
        assertTrue(r.stdout, r.stdout.indexOf("THREAD MISMATCH") < 0);
    }

    @Test public void shippedProfileGivesTheDefaultBehaviour() throws Exception {
        HeadlessRunner.Options o = opts(DesktopTestSupport.midlet("events"), "events.inp");
        o.profile = new File(DesktopTestSupport.root(), "profiles/jlkvm-min.json");
        assertEquals(EVENTS_EXPECTED, DesktopTestSupport.onlyGuestLines(HeadlessRunner.run(o).stdout));
    }

    @Test public void deterministicReplay() throws Exception {
        String a = HeadlessRunner.run(opts(DesktopTestSupport.midlet("events"), "events.inp")).stdout;
        String b = HeadlessRunner.run(opts(DesktopTestSupport.midlet("events"), "events.inp")).stdout;
        assertEquals(a, b);
    }

    @Test public void keyCodesComeFromTheProfile() throws Exception {
        File p = File.createTempFile("profile", ".json");
        Files.write(p.toPath(), ("{\"name\":\"alt\",\"keys\":{\"softLeft\":-21,\"softRight\":-22,\"select\":-20,\"up\":-59,\"down\":-60,\"left\":-61,\"right\":-62},"
            + "\"display\":{\"width\":128,\"height\":160,\"canvasWidth\":128,\"canvasHeight\":140,\"pointerEvents\":true}}").getBytes("UTF-8"));
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("events");
        o.profile = p;
        o.scriptText = "100 key tap -21\n200 key tap -22\n300 key tap -59\n400 key tap -20\n500 ptr press 5 6\n600 destroy 1\n";
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        String out = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertTrue(out, out.contains("> keycodes UP=-59 DOWN=-60 LEFT=-61 RIGHT=-62 FIRE=-20 A=49 B=51 C=55 D=57\n"));
        assertTrue(out, out.contains("> paint 128x140\n"));
        assertTrue(out, out.contains("> commandAction OK\n> commandAction Back\n"));
        assertTrue(out, out.contains("> keyPressed -59 action=1 name=UP\n"));
        assertTrue(out, out.contains("> keyPressed -20 action=8 name=SELECT\n"));
        assertTrue(out, out.contains("> pointerPressed 5,6\n"));
    }

    /** D13/D15: the guest may only be entered from the vm-thread; the debug build asserts it. */
    @Test public void guestEntryFromAnotherThreadIsRejected() throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("lifecycle");
        o.midlet = 2;
        final HeadlessRunner.Result r = HeadlessRunner.run(o);
        // the run above made the calling thread the vm-thread; a second thread entering the same VM is a violation
        final Throwable[] err = new Throwable[1];
        Thread t = new Thread("android-ui-simulated") { public void run() { try { r.vm.runSlice(10); } catch (Throwable x) { err[0] = x; } } };
        t.start();
        t.join();
        assertTrue(String.valueOf(err[0]), err[0] instanceof IllegalStateException);
        assertEquals(1, r.vm.threadViolations);
        // postEvent / requestExit stay legal from any thread
        Thread u = new Thread() { public void run() { r.vm.postEvent(1, 2, 3, 4); r.vm.requestExit(); } };
        u.start();
        u.join();
        assertEquals(1, r.vm.threadViolations);
    }

    @Test public void scriptParsing() throws Exception {
        ScriptedInput s = ScriptedInput.parse("# c\n100 key tap -5 50\n200 key hold 49 700\n300 shot a\n\n400 quit\n", 500, 100);
        // tap = 2 entries, hold 700 = press + repeats at 500, 600 + release, shot, quit
        assertEquals(2 + (1 + 2 + 1) + 1 + 1, s.entries.size());
        assertEquals(150, s.entries.get(1).ms);
        try { ScriptedInput.parse("100 bogus\n", 500, 100); fail(); } catch (java.io.IOException e) { assertTrue(e.getMessage().contains("line 1")); }
    }
}

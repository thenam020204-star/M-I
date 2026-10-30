package jlkvm.host.desktop;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.file.Files;
import jlkvm.host.GameErrorLog;
import jlkvm.host.RecordingServices;
import org.junit.Test;

/** T35: the real-time vm-thread that the Android Activity drives (pause / resume / destroy from another thread, errors file). */
public class VmRunnerTest {
    private static String out(ByteArrayOutputStream b) throws Exception { return DesktopTestSupport.onlyGuestLines(b.toString("UTF-8")); }

    private static void waitFor(ByteArrayOutputStream b, String what) throws Exception {
        for (int i = 0; i < 400; i++) {
            if (b.toString("UTF-8").contains(what)) return;
            Thread.sleep(25);
        }
        fail("timeout waiting for '" + what + "' in:\n" + b.toString("UTF-8"));
    }

    @Test public void lifecycleFromAnotherThread() throws Exception {
        ByteArrayOutputStream cap = new ByteArrayOutputStream();
        VmRunner.Options o = new VmRunner.Options();
        o.jar = DesktopTestSupport.midlet("lifecycle");
        o.jad = new File(DesktopTestSupport.root(), "tests/midlets/lifecycle/test.jad");
        o.romDir = HeadlessRunner.findRomDir();
        o.services = new RecordingServices();
        o.out = new PrintStream(cap, true, "UTF-8");
        VmRunner r = new VmRunner(o);
        final Throwable[] done = new Throwable[1];
        final boolean[] fin = new boolean[1];
        r.setListener(new VmRunner.Listener() { public void finished(Throwable e) { done[0] = e; fin[0] = true; } });
        r.start();
        waitFor(cap, "> showNotify");
        r.pause();
        waitFor(cap, "> pauseApp");
        r.resume();
        waitFor(cap, "> startApp #2");
        r.keyPressed(50);   // key "2": the MIDlet refuses the next conditional destroy
        waitFor(cap, "> key 50");
        r.destroy(false);
        waitFor(cap, "> refuses");
        assertTrue(r.stop(true, 5000));
        String s = out(cap);
        assertTrue(s, s.indexOf("> hideNotify\n> pauseApp\n> startApp #2\n> showNotify") > 0);
        assertTrue(s, s.endsWith("> destroyApp unconditional=true\n"));
        assertTrue(fin[0]);
        assertNull(done[0]);
        assertEquals(0, r.vm().threadViolations);
    }

    @Test public void errorsGoToTheGameLogOnly() throws Exception {
        File log = File.createTempFile("game-errors", ".log");
        VmRunner.Options o = new VmRunner.Options();
        o.jar = DesktopTestSupport.midlet("errors");
        o.romDir = HeadlessRunner.findRomDir();
        o.services = new RecordingServices();
        o.errorLog = new GameErrorLog(new FileWriter(log));
        ByteArrayOutputStream cap = new ByteArrayOutputStream();
        o.out = new PrintStream(cap, true, "UTF-8");
        VmRunner r = new VmRunner(o);
        r.start();
        waitFor(cap, "> done");
        assertTrue(r.stop(true, 5000));
        String s = new String(Files.readAllBytes(log.toPath()), "UTF-8");
        assertTrue(s, s.contains("[resource] /missing/sprite.png"));
        assertTrue(s, s.contains("IllegalStateException: deliberate failure"));
        int first = s.indexOf("deliberate failure");
        assertEquals(s, -1, s.indexOf("deliberate failure", first + 1));
        log.delete();
    }

    private static File tempJad(String extra, String jarSize) throws Exception {
        File f = File.createTempFile("vmrunner", ".jad");
        f.deleteOnExit();
        Files.write(f.toPath(), ("MIDlet-Name: EventsTest\nMIDlet-Version: 1.0.0\nMIDlet-Vendor: jl-kvm\nMIDlet-1: Events,,ev.EventsMIDlet\n"
                + "MIDlet-Jar-URL: events.jar\nMIDlet-Jar-Size: " + jarSize + "\n" + extra).getBytes("UTF-8"));
        return f;
    }

    /** T35c / T30 on the Android path: VmRunner refuses a JAR over the device limit and applies the JAD frame plan before the guest boots. */
    @Test public void nokiaJadPlanAndInstallLimit() throws Exception {
        String dp2 = new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "profiles/nokia-s40-dp2.0.json").toPath()), "UTF-8");
        VmRunner.Options o = new VmRunner.Options();
        o.jar = DesktopTestSupport.midlet("events");
        o.romDir = HeadlessRunner.findRomDir();
        o.services = new RecordingServices();
        o.profileJson = dp2;
        o.jad = tempJad("", "357001");
        VmRunner big = new VmRunner(o);
        try { big.start(); fail("refused"); } catch (java.io.IOException e) { assertTrue(e.getMessage(), e.getMessage().contains("357000")); }
        // a profile that honours Original-Display-Size: the plan is applied to the guest-visible profile
        String s6 = new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "profiles/nokia-s40-6th.json").toPath()), "UTF-8");
        o.profileJson = s6;
        o.jad = tempJad("Nokia-MIDlet-Original-Display-Size: 176,208\n", "1000");
        ByteArrayOutputStream cap = new ByteArrayOutputStream();
        o.out = new PrintStream(cap, true, "UTF-8");
        VmRunner r = new VmRunner(o);
        r.start();
        assertEquals(176, r.nokiaPlan().screenW);
        assertEquals(208, r.nokiaPlan().screenH);
        assertTrue(r.stop(true, 5000));
    }
}

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
}

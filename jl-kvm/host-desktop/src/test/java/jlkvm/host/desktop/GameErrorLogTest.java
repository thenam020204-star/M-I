package jlkvm.host.desktop;

import static org.junit.Assert.*;

import java.io.StringWriter;
import jlkvm.host.GameErrorLog;
import org.junit.Test;

/** T35b: game-errors.log keeps the errors of the game and nothing else. */
public class GameErrorLogTest {
    private static int count(String s, String sub) {
        int n = 0;
        for (int i = s.indexOf(sub); i >= 0; i = s.indexOf(sub, i + 1)) n++;
        return n;
    }

    @Test public void filterDropsSystemAndInputNoise() {
        StringWriter w = new StringWriter();
        GameErrorLog g = new GameErrorLog(w);
        g.log(1, "link", "loaded Foo");   // the debug trace of the linker (D15)
        g.log(1, "gc", "#1 live=100B");
        g.log(1, "input", "key 53 pressed");
        g.log(1, "MIUIInput", "touch");
        g.log(1, "vm", "info line");
        g.log(3, "vm", "Uncaught exception in guest thread 2: java.lang.Foo\n    at A.m (pc 3)");
        g.log(3, "vm", "Uncaught exception in guest thread 2: java.lang.Foo\n    at A.m (pc 3)");
        String s = w.toString();
        assertFalse(s, s.contains("loaded Foo"));
        assertFalse(s, s.contains("gc"));
        assertFalse(s, s.contains("MIUIInput"));
        assertFalse(s, s.contains("pressed"));
        assertFalse(s, s.contains("info line"));
        assertEquals(1, count(s, "Uncaught exception"));
    }

    @Test public void deliberateErrorsAreLoggedOnce() throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("errors");
        StringWriter w = new StringWriter();
        o.errorLog = new GameErrorLog(w);
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        String log = w.toString();
        String out = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertTrue(out, out.contains("> resources true true"));
        assertTrue(out, out.contains("> done"));
        // the missing resource: one line although it was asked twice
        assertEquals(log, 1, count(log, "[resource] /missing/sprite.png"));
        assertEquals(log, 1, count(log, "[media] image cannot be decoded"));
        // Class.forName of a missing class is the application's own question (ClassNotFoundException), not a linking error of the VM
        assertTrue(out, out.contains("> forName java.lang.ClassNotFoundException"));
        assertEquals(log, 0, count(log, "[link]"));
        assertTrue(log, count(log, "[oom] OutOfMemoryError") >= 1);
        assertTrue(log, log.contains("heap free="));
        // the uncaught exception: once, with the guest stack trace
        assertEquals(log, 1, count(log, "IllegalStateException: deliberate failure"));
        assertTrue(log, log.contains("errors.ErrorsMIDlet$Boom.explode"));
        assertTrue(log, log.contains("[profile]"));
        // nothing but the allowed tags
        for (String line : log.split("\n")) {
            if (line.startsWith("+")) {
                assertTrue(line, line.matches("\\+\\d+ms \\[(profile|exception|oom|resource|media|log)\\].*"));
            }
        }
        assertTrue(log, log.length() < 6000);
    }
}

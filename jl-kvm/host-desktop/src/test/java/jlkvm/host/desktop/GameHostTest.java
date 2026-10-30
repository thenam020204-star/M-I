package jlkvm.host.desktop;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import org.junit.Test;

/**
 * T25 on the desktop host: the sample game (tests/midlets/game) driven by tests/midlets/scripts/game.inp. getKeyStates latches as MIDP
 * says (press + release between two polls is seen once, a held key by every poll, game keys are not delivered to keyPressed when
 * suppressKeyEvents is true), flushGraphics shows the buffer, and 200 frames of flush / layer paint / collision allocate nothing in the guest heap.
 */
public class GameHostTest {
    private static HeadlessRunner.Result run() throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("game");
        o.script = new File(DesktopTestSupport.root(), "tests/midlets/scripts/game.inp");
        return HeadlessRunner.run(o);
    }

    @Test public void keyStatesFlushAndAllocation() throws Exception {
        HeadlessRunner.Result r = run();
        assertEquals(r.stdout, HeadlessRunner.Result.EXITED, r.exitKind);
        String guest = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertEquals(new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "tests/midlets/expected/game.txt").toPath()), "UTF-8"), guest);
        assertFalse(guest, guest.contains("NOT-EVENT-THREAD"));
        assertTrue(guest, guest.contains("heap bytes allocated by 200 frames of flush / paint / collision: 0\n"));
        // latching: UP pressed and released between two polls is seen once; LEFT held, RIGHT added, LEFT released, RIGHT released
        assertTrue(guest, guest.contains("> A keys 2\n> A keys 0\n> A keys 4\n> A keys 24\n> A keys 20\n> A keys 0\n"));
        // suppressKeyEvents: no keyPressed for the game keys on A (only for '*' = 42), keyPressed on B for them
        assertFalse(guest, guest.contains("A keyPressed 50") || guest.contains("A keyPressed 53"));
        assertTrue(guest, guest.contains("B keyPressed 49"));
        assertTrue("flushGraphics presented frames", r.services.frames > 200);
        assertEquals(r.host.guestErr.toString(), 0, r.host.guestErr.length());
        assertEquals(0, r.vm.threadViolations);
        String again = DesktopTestSupport.onlyGuestLines(run().stdout);
        assertEquals(guest, again);
    }
}

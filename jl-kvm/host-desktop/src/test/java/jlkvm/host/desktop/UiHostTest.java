package jlkvm.host.desktop;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import org.junit.Test;

/**
 * T24 on the desktop host: the sample MIDlet tests/midlets/ui is driven through the headless UI (HeadlessUi verbs of
 * tests/midlets/scripts/ui.inp): List select, Form items (button command, TextField, ChoiceGroup, Gauge, DateField, CustomItem), TextBox
 * constraints, Alert dismissal, Canvas command menu (more than two commands). Every callback must run on the event thread (D13).
 */
public class UiHostTest {
    private static String read(String rel) throws Exception {
        return new String(Files.readAllBytes(new File(DesktopTestSupport.root(), rel).toPath()), "UTF-8");
    }

    private static HeadlessRunner.Result run() throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("ui");
        o.script = new File(DesktopTestSupport.root(), "tests/midlets/scripts/ui.inp");
        return HeadlessRunner.run(o);
    }

    private static String dumps(HeadlessRunner.Result r) {
        StringBuilder b = new StringBuilder();
        for (String d : r.ui.dumps) b.append(d);
        return b.toString();
    }

    @Test public void scriptedSession() throws Exception {
        HeadlessRunner.Result r = run();
        assertEquals(r.stdout, HeadlessRunner.Result.EXITED, r.exitKind);
        String guest = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertEquals(read("tests/midlets/expected/ui.txt"), guest);
        assertFalse(guest, guest.contains("NOT-EVENT-THREAD"));
        assertEquals(read("tests/midlets/expected/ui-dumps.txt"), dumps(r));
        assertTrue(r.ui.errors.toString(), r.ui.errors.isEmpty());
        assertEquals(0, r.vm.threadViolations);
        assertTrue(r.host.guestErr.toString(), r.host.guestErr.length() == 0);
        // twice: identical
        HeadlessRunner.Result r2 = run();
        assertEquals(guest, DesktopTestSupport.onlyGuestLines(r2.stdout));
        assertEquals(dumps(r), dumps(r2));
        assertEquals(r.shots, r2.shots);
    }

    @Test public void actionsOnAStaleScreenAreIgnored() throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("ui");
        // select, then (still addressed to the old List) another select and a command that no longer exists
        o.scriptText = "100 ui select 1\n110 ui cmd Exit\n200 ui dump x\n300 quit\n";
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        assertTrue(r.ui.errors.toString(), r.ui.errors.size() == 1 && r.ui.errors.get(0).contains("no command 'Exit'"));
        assertTrue(r.ui.dumps.get(0), r.ui.dumps.get(0).contains("screen FORM"));
    }
}

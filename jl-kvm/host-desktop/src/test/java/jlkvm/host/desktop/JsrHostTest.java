package jlkvm.host.desktop;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import jlkvm.DeviceProfile;
import jlkvm.host.SmsNatives;
import jlkvm.host.fs.DirFs;
import jlkvm.host.fs.HostFs;
import jlkvm.host.fs.MemoryFs;
import jlkvm.profile.ProfileLoader;
import org.junit.Test;

/**
 * T32a on the desktop host: the WMA / JSR-75 layers exist only for a profile that declares them (D10); the sms policy in its three modes
 * (deny and offline: nothing is sent and nothing is accepted; simulate: the message is only logged on the host); incoming messages pushed by
 * the test host reach the receive() and the listener, the listener on a guest thread; FileConnection cannot leave its sandbox (".." in the URL,
 * a symlink inside the suite directory, direct HostFs calls).
 */
public class JsrHostTest {
    private static File profile(String name) { return new File(DesktopTestSupport.root(), "profiles/" + name); }

    /** The sample profile with the sms mode replaced. */
    private static File withSms(String mode) throws Exception {
        String text = new String(Files.readAllBytes(profile("jlkvm-nokia.json").toPath()), "UTF-8");
        assertTrue(text.contains("\"mode\": \"deny\""));
        File f = File.createTempFile("jsr-profile", ".json");
        f.deleteOnExit();
        Files.write(f.toPath(), text.replace("\"mode\": \"deny\"", "\"mode\": \"" + mode + "\"").getBytes("UTF-8"));
        return f;
    }

    private static String script() throws Exception {
        return new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "tests/midlets/scripts/jsr.inp").toPath()), "UTF-8");
    }

    private HeadlessRunner.Result run(File profile, SmsNatives sms, File fsDir) throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("jsr");
        o.profile = profile;
        o.sms = sms;
        o.fsDir = fsDir;
        o.scriptText = script();
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        assertEquals(r.stdout, HeadlessRunner.Result.EXITED, r.exitKind);
        assertEquals(r.host.guestErr.toString(), 0, r.host.guestErr.length());
        assertEquals(0, r.vm.threadViolations);
        return r;
    }

    @Test public void layersOnlyWithTheProfileApis() throws Exception {
        HeadlessRunner.Result r = run(profile("jlkvm-min.json"), null, null);
        assertEquals("> layers wma false fc false pim false\n> done\n", DesktopTestSupport.onlyGuestLines(r.stdout));
        File romDir = HeadlessRunner.findRomDir();
        DeviceProfile full = ProfileLoader.fromJson(new String(Files.readAllBytes(profile("jlkvm-nokia.json").toPath()), "UTF-8"));
        DeviceProfile min = ProfileLoader.fromJson(new String(Files.readAllBytes(profile("jlkvm-min.json").toPath()), "UTF-8"));
        String[] cls = {"javax/wireless/messaging/MessageConnection", "javax/microedition/io/file/FileConnection", "javax/microedition/pim/PIM",
            "jlkvm/internal/io/sms/Protocol", "jlkvm/internal/io/file/Protocol"};
        for (String c : cls) {
            assertNotNull(c, new RomLayers(romDir, full).readClass(c));
            assertNull(c, new RomLayers(romDir, min).readClass(c));
        }
        // one layer without the others
        DeviceProfile onlyWma = ProfileLoader.fromJson("{\"apis\": [\"cldc-1.1\", \"midp-2.0\", \"wma-1.1\"]}");
        assertNotNull(new RomLayers(romDir, onlyWma).readClass("javax/wireless/messaging/Message"));
        assertNull(new RomLayers(romDir, onlyWma).readClass("javax/microedition/io/file/FileConnection"));
    }

    /** T32: JSR-82 / 179 / 256 / 234 are separate layers, each present only when the profile declares it. */
    @Test public void bluetoothLocationSensorAmmsLayersOnlyWithTheirApi() throws Exception {
        File romDir = HeadlessRunner.findRomDir();
        String[][] layers = {{"jsr82", "javax/bluetooth/LocalDevice"}, {"jsr82", "jlkvm/internal/io/btspp/Protocol"},
            {"jsr179", "javax/microedition/location/LocationProvider"}, {"jsr256", "javax/microedition/sensor/SensorManager"},
            {"jsr234", "javax/microedition/amms/GlobalManager"}};
        String[] all = {"jsr82", "jsr179", "jsr256", "jsr234"};
        for (String[] l : layers) {
            DeviceProfile only = ProfileLoader.fromJson("{\"apis\": [\"cldc-1.1\", \"midp-2.0\", \"" + l[0] + "\"]}");
            assertNotNull(l[1], new RomLayers(romDir, only).readClass(l[1]));
            for (String other : all) {
                if (other.equals(l[0])) continue;
                DeviceProfile o = ProfileLoader.fromJson("{\"apis\": [\"cldc-1.1\", \"midp-2.0\", \"" + other + "\"]}");
                assertNull(l[1] + " must not exist with only " + other, new RomLayers(romDir, o).readClass(l[1]));
            }
            assertNull(l[1], new RomLayers(romDir, ProfileLoader.fromJson("{\"apis\": [\"cldc-1.1\", \"midp-2.0\"]}")).readClass(l[1]));
        }
    }

    private static String guest(HeadlessRunner.Result r) { return DesktopTestSupport.onlyGuestLines(r.stdout); }

    @Test public void smsDenyAndOfflineSendNothingAndAcceptNothing() throws Exception {
        for (String mode : new String[] {"deny", "offline"}) {
            DeviceProfile p = ProfileLoader.fromJson(new String(Files.readAllBytes(withSms(mode).toPath()), "UTF-8"));
            SmsNatives sms = new SmsNatives(p);
            HeadlessRunner.Result r = run(withSms(mode), sms, null);
            String g = guest(r);
            String ex = mode.equals("deny") ? "SecurityException" : "IOException";
            assertTrue(g, g.contains("> client: " + ex + "\n> server: " + ex + "\n> cbs: " + ex + "\n"));
            assertFalse(g, g.contains("client send ok"));
            assertEquals(Arrays.asList(), sms.sent());
            // the host log has no "sent" line, and the pushed messages were dropped
            for (String l : r.host.logs) assertFalse(l, l.startsWith("[sms] sent"));
            assertFalse(sms.deliverText(false, 6000, "sms://+1", "x", 0));
            assertFalse(sms.sendAllowed());
        }
    }

    @Test public void smsSimulateLogsOnTheHostAndDeliversPushedMessages() throws Exception {
        DeviceProfile p = ProfileLoader.fromJson(new String(Files.readAllBytes(withSms("simulate").toPath()), "UTF-8"));
        SmsNatives sms = new SmsNatives(p);
        HeadlessRunner.Result r = run(withSms("simulate"), sms, null);
        String g = guest(r);
        assertEquals(Arrays.asList("to=sms://+84901234567:5000;text=hello world;segments=1", "to=sms://+84901234567:5000;data=cafe;segments=1"), sms.sent());
        boolean logged = false;
        for (String l : r.host.logs) if (l.equals("[sms] sent to=sms://+84901234567:5000;text=hello world;segments=1")) logged = true;
        assertTrue(r.host.logs.toString(), logged);
        assertTrue(g, g.contains("> client send ok\n> client binary send ok\n> server open\n"));
        assertTrue(g, g.contains("> listener: notified 1 on a guest thread other than main: true same connection: true\n> listener: text 'first message' from sms://+84900000001:5001 timestamp set true\n"));
        assertTrue(g, g.contains("> listener removed, notified 1\n> receive: second message from sms://+84900000002:5002\n"));
        // the binary message arrived while no listener was set: it is notified as soon as the listener is registered
        assertTrue(g, g.contains("> listener: notified 2 on a guest thread other than main: true same connection: true\n> listener: binary 3 bytes from sms://+84900000003:5003\n"));
        assertTrue(g, g.contains("> listener removed again, notified 2\n> server closed\n> cbs open\n> cbs receive: CBS text\n"));
        // the message for a port nobody holds was dropped
        boolean dropped = false;
        for (String l : r.host.logs) if (l.contains("7777") && l.endsWith("dropped")) dropped = true;
        assertTrue(r.host.logs.toString(), dropped);
    }

    @Test public void fileConnectionStaysInsideItsSandbox() throws Exception {
        Path tmp = Files.createTempDirectory("jsr-fs");
        Path outside = Files.createTempDirectory("jsr-outside");
        Path secret = outside.resolve("secret.txt");
        Files.write(secret, "top secret".getBytes("UTF-8"));
        try {
            // the suite directory is base/<vendor_name>; plant a symlink to the outside directory inside root1
            Path suite = tmp.resolve("jl-kvm_Jsr").resolve("root1");
            Files.createDirectories(suite);
            try {
                Files.createSymbolicLink(suite.resolve("link"), outside);
            } catch (UnsupportedOperationException | IOException e) {
                assumeTrue("symbolic links not available: " + e, false);
            }
            DeviceProfile p = ProfileLoader.fromJson(new String(Files.readAllBytes(withSms("deny").toPath()), "UTF-8"));
            HeadlessRunner.Result r = run(withSms("deny"), new SmsNatives(p), tmp.toFile());
            String g = guest(r);
            // normal use works and lands in the suite directory
            assertTrue(g, g.contains("> file: saved size 5\n"));
            assertEquals("saved", new String(Files.readAllBytes(suite.resolve("saves").resolve("slot1.dat")), "UTF-8"));
            // ".." is refused at the URL, the symlink at the first access
            assertTrue(g, g.contains("> file:///root1/../escape.txt: IllegalArgumentException\n> file:///root1/%2e%2e/escape.txt: IllegalArgumentException\n"));
            assertTrue(g, g.contains("> file:///root1/link/secret.txt: opened, exists false\n> file:///root1/link/secret.txt: IOException\n"));
            assertTrue(g, g.contains("> file:///root1/link/new.txt: opened, exists false\n> file:///root1/link/new.txt: IOException\n"));
            // nothing was read from or written to the outside; the listing does not show the symlink
            assertEquals("top secret", new String(Files.readAllBytes(secret), "UTF-8"));
            assertEquals(Arrays.asList("secret.txt"), Arrays.asList(outside.toFile().list()));
            assertFalse(Files.exists(tmp.resolve("escape.txt")));
            assertFalse(Files.exists(tmp.resolve("jl-kvm_Jsr").resolve("escape.txt")));
            assertTrue(g, g.contains("> root1 list: saves/ \n"));
        } finally {
            deleteTree(tmp.toFile());
            deleteTree(outside.toFile());
        }
    }

    private static void deleteTree(File f) {
        File[] c = f.listFiles();
        if (c != null && !Files.isSymbolicLink(f.toPath())) for (File x : c) deleteTree(x);
        f.delete();
    }

    @Test public void hostFsRefusesEverythingOutsideItsRoots() throws Exception {
        Path tmp = Files.createTempDirectory("jsr-fs2");
        try {
            List<String> roots = Arrays.asList("c:/", "root1/");
            HostFs[] all = {new DirFs(tmp.toFile(), roots, 1000), new MemoryFs(roots, 1000)};
            for (HostFs fs : all) {
                String n = fs.getClass().getSimpleName();
                String[] bad = {"c:/../x", "c:/a/../../x", "c:/./x", "c:/a//b", "c:/a\\b", "c:/a:b", "d:/x", "/etc/passwd", "etc/passwd", "../x", "c:", "root1", "c:/x\0y"};
                for (String b : bad) {
                    assertFalse(n + " exists " + b, fs.exists(b));
                    try { fs.create(b); fail(n + " create " + b); } catch (IOException e) { /* refused */ }
                    try { fs.mkdir(b); fail(n + " mkdir " + b); } catch (IOException e) { /* refused */ }
                    try { fs.open(b, true); fail(n + " open " + b); } catch (IOException e) { /* refused */ }
                    try { fs.list(b); fail(n + " list " + b); } catch (IOException e) { /* refused */ }
                    try { fs.delete(b); fail(n + " delete " + b); } catch (IOException e) { /* refused */ }
                }
                fs.create("c:/a.txt");
                try { fs.rename("c:/a.txt", "../b.txt"); fail(n + " rename out"); } catch (IOException e) { /* refused */ }
                try { fs.rename("c:/a.txt", "x/y"); fail(n + " rename with path"); } catch (IOException e) { /* refused */ }
                // quota
                HostFs.Handle h = fs.open("c:/a.txt", true);
                h.write(0, new byte[1000], 0, 1000);
                try { h.write(1000, new byte[1], 0, 1); fail(n + " quota"); } catch (IOException e) { /* full */ }
                h.close();
                assertEquals(1000, fs.length("c:/a.txt"));
                assertEquals(1000, fs.used("c:/"));
                assertEquals(0, fs.used("root1/"));
                fs.delete("c:/a.txt");
                assertEquals(0, fs.used("c:/"));
            }
            // the disk holds only the root directories ("c:/" -> c_), nothing beside them
            assertTrue(new File(tmp.toFile(), "c_").isDirectory());
            for (String n : tmp.toFile().list()) assertTrue(n, n.equals("c_") || n.equals("root1"));
        } finally {
            deleteTree(tmp.toFile());
        }
    }
}

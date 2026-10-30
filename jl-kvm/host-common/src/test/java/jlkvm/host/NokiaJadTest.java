package jlkvm.host;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import jlkvm.DeviceProfile;
import jlkvm.host.ams.NokiaJad;
import jlkvm.profile.ProfileLoader;
import org.junit.Test;

/** PLAN T30: behaviour of the Nokia JAD attributes (scale, game-visible size, virtual keys, orientation, category, JAR limit). */
public class NokiaJadTest {
    static DeviceProfile profile(String name) throws Exception {
        return ProfileLoader.fromJson(new String(Files.readAllBytes(new File(new File("..").getAbsoluteFile(), "profiles/" + name).toPath()), "UTF-8"));
    }

    static Map<String, String> jad(String... kv) {
        Map<String, String> m = new HashMap<String, String>();
        for (int i = 0; i < kv.length; i += 2) m.put(kv[i], kv[i + 1]);
        return m;
    }

    @Test public void categoryIsTheOnlyAttributeOfDp20() throws Exception {
        DeviceProfile p = profile("nokia-s40-dp2.0.json");
        NokiaJad.Plan r = NokiaJad.plan(p, jad(NokiaJad.CATEGORY, " Games ", NokiaJad.ORIGINAL_SIZE, "176,208", NokiaJad.KEYPAD, "yes"));
        assertEquals("Games", r.category);
        assertTrue(r.isGame);
        assertFalse(NokiaJad.plan(p, jad(NokiaJad.CATEGORY, "Game")).isGame);   // User Guide: only the string Games selects the Games folder
        // the SDK has no display-size / keypad attributes: present but not honored -> no effect, reported
        assertEquals(128, r.screenW); assertEquals(1.0, r.scale, 0); assertEquals(2, r.ignored.size());
        assertNull(r.keypad); assertTrue(r.virtualKeys.isEmpty());
        assertEquals(0, NokiaJad.plan(p, jad()).ignored.size());
        assertNull(NokiaJad.plan(p, jad()).category);
    }

    @Test public void scaleToTargetKeepsAspectAndCenters() throws Exception {
        DeviceProfile p = profile("nokia-s40-6th.json");   // 240x320
        NokiaJad.Plan r = NokiaJad.plan(p, jad(NokiaJad.ORIGINAL_SIZE, "176, 208"));
        assertEquals(176, r.screenW); assertEquals(208, r.screenH);   // the game sees the size it was written for
        assertEquals(240.0 / 176, r.scale, 1e-9);                      // width limited, fits the 240x320 screen
        assertEquals(240, r.presentW); assertEquals(284, r.presentH);
        assertEquals(0, r.presentX); assertEquals(18, r.presentY);
        r = NokiaJad.plan(p, jad(NokiaJad.ORIGINAL_SIZE, "320,240", NokiaJad.TARGET_SIZE, "160,120"));
        assertEquals(0.5, r.scale, 0); assertEquals(160, r.presentW); assertEquals(120, r.presentH);
        // target alone is meaningless
        r = NokiaJad.plan(p, jad(NokiaJad.TARGET_SIZE, "100,100"));
        assertEquals(1.0, r.scale, 0); assertEquals(240, r.presentW); assertEquals(1, r.ignored.size());
        // no attribute: unscaled
        r = NokiaJad.plan(p, jad());
        assertEquals(1.0, r.scale, 0); assertEquals(0, r.presentX); assertEquals(320, r.presentH);
    }

    @Test public void badValuesAreIgnoredNotFatal() throws Exception {
        DeviceProfile p = profile("nokia-s40-6th.json");
        String[] bad = {"", "176", "176,", "a,b", "0,10", "10,0", "5000,10", "1,2,3", "-4,4"};
        for (int i = 0; i < bad.length; i++) {
            NokiaJad.Plan r = NokiaJad.plan(p, jad(NokiaJad.ORIGINAL_SIZE, bad[i]));
            assertEquals(bad[i], 240, r.screenW);
            assertEquals(bad[i], 1, r.ignored.size());
        }
        assertEquals(1, NokiaJad.plan(p, jad(NokiaJad.KEYPAD, "bogus")).ignored.size());
        assertEquals(1, NokiaJad.plan(p, jad(NokiaJad.ORIENTATION, "diagonal")).ignored.size());
    }

    @Test public void virtualKeypadFollowsTheAttribute() throws Exception {
        DeviceProfile p = profile("nokia-s40-6th.json");
        NokiaJad.Plan r = NokiaJad.plan(p, jad(NokiaJad.KEYPAD, "gameactions"));
        assertEquals("[up, down, left, right, select]", r.virtualKeys.toString());
        r = NokiaJad.plan(p, jad(NokiaJad.KEYPAD, "NavigationKeys"));
        assertEquals("[up, down, left, right, select, softLeft, softRight]", r.virtualKeys.toString());
        r = NokiaJad.plan(p, jad(NokiaJad.KEYPAD, "no"));
        assertEquals("no", r.keypad); assertTrue(r.virtualKeys.isEmpty());
        assertNull(NokiaJad.plan(p, jad()).keypad);
        // a profile that does not honor the attribute (3rd edition list has no keypad) ignores it
        assertTrue(NokiaJad.plan(profile("nokia-s40-3rd.json"), jad(NokiaJad.KEYPAD, "gameactions")).virtualKeys.isEmpty());
    }

    @Test public void orientationLockTurnsTheFrame() throws Exception {
        DeviceProfile p = profile("nokia-s40-6th.json");
        NokiaJad.Plan r = NokiaJad.plan(p, jad(NokiaJad.ORIENTATION, "landscape"));
        assertEquals("landscape", r.orientation);
        assertEquals(320, r.screenW); assertEquals(240, r.screenH); assertEquals(1.0, r.scale, 0); assertEquals(320, r.presentW);
        r = NokiaJad.plan(p, jad(NokiaJad.ORIENTATION, "portrait"));
        assertEquals(240, r.screenW); assertEquals(1.0, r.scale, 0);
        // landscape game with a size of its own, scaled into the turned box
        r = NokiaJad.plan(p, jad(NokiaJad.ORIENTATION, "landscape", NokiaJad.ORIGINAL_SIZE, "160,120"));
        assertEquals(2.0, r.scale, 0); assertEquals(320, r.presentW); assertEquals(240, r.presentH);
    }

    @Test public void applyChangesWhatTheGuestSees() throws Exception {
        DeviceProfile p = profile("nokia-s40-6th.json");
        p.canvasHeight = 294;   // 26 px of title / soft key area
        NokiaJad.apply(p, NokiaJad.plan(p, jad(NokiaJad.ORIGINAL_SIZE, "176,208")));
        assertEquals(176, p.screenWidth); assertEquals(208, p.screenHeight);
        assertEquals(176, p.canvasWidth); assertEquals(182, p.canvasHeight);
    }

    @Test public void jarSizeLimit() throws Exception {
        DeviceProfile p = profile("nokia-s40-dp2.0.json");   // 357000, User Guide
        assertNull(NokiaJad.checkInstall(p, jad("MIDlet-Jar-Size", "357000")));
        assertTrue(NokiaJad.checkInstall(p, jad("MIDlet-Jar-Size", "357001")).contains("357000"));
        assertTrue(NokiaJad.checkInstall(p, jad("MIDlet-Jar-Size", "x")).contains("not a number"));
        assertNull(NokiaJad.checkInstall(p, jad()));
        assertNull(NokiaJad.checkInstall(new DeviceProfile(), jad("MIDlet-Jar-Size", "99999999")));   // 0 = no limit
    }
}

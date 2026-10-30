package jlkvm.profile;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import jlkvm.DeviceProfile;
import org.junit.Test;

/** PLAN T33: every shipped profile passes the schema + source-tag validator; the SDK-sourced numbers of nokia-s40-dp2.0 are pinned. */
public class ProfileValidatorTest {
    static final File DIR = new File("../profiles").getAbsoluteFile();

    static String read(String name) throws Exception { return new String(Files.readAllBytes(new File(DIR, name).toPath()), "UTF-8"); }

    @Test public void everyShippedProfileIsValid() throws Exception {
        File[] fs = DIR.listFiles();
        assertNotNull(fs);
        int n = 0;
        for (File f : fs) {
            if (!f.getName().endsWith(".json")) continue;
            List<String> e = ProfileValidator.validate(read(f.getName()));
            assertTrue(f.getName() + ": " + e, e.isEmpty());
            n++;
        }
        assertTrue("expected the T20 profiles + 5 Nokia S40 profiles, found " + n, n >= 7);
    }

    @Test public void dp20NumbersAreTheSdkOnes() throws Exception {
        DeviceProfile p = ProfileLoader.fromJson(read("nokia-s40-dp2.0.json"));
        assertEquals(1024000, p.heapBytes);                  // User Guide: maximum heapsize for the Nokia 6230
        assertEquals(357000L, p.jarMaxBytes);                // User Guide: maximum JAR size that the Nokia 6230 can use
        assertEquals(128, p.screenWidth); assertEquals(128, p.screenHeight);
        assertEquals(-6, p.key("softLeft")); assertEquals(-7, p.key("softRight")); assertEquals(-5, p.key("select"));
        assertEquals(-10, p.key("send")); assertEquals(-11, p.key("end")); assertEquals(35, p.key("pound")); assertEquals(53, p.key("num5"));
        assertEquals("CLDC-1.1", p.properties.get("microedition.configuration"));
        assertEquals("MIDP-2.0", p.properties.get("microedition.profiles"));
        assertEquals("ISO-8859-1", p.properties.get("microedition.encoding"));
        assertEquals("1.1", p.properties.get("microedition.media.version"));
        assertEquals("false", p.properties.get("supports.mixing"));
        assertTrue(p.apis.contains("nokia-ui") && p.apis.contains("wma-1.1") && p.apis.contains("jsr-135"));
        assertFalse("JSR-75 is not in the DP2.0 SDK JSR list", p.apis.contains("jsr75-fc"));
        assertEquals("s40", p.deviceFamily); assertEquals("dp2.0", p.deviceEdition);
    }

    @Test public void sdkTaggedValuesNameTheirFile() throws Exception {
        // every tag of the DP2.0 profile that claims "sdk:" must name an SDK file / section (a bare 'sdk:' is rejected by the validator too)
        String t = read("nokia-s40-dp2.0.json");
        int i = 0, n = 0;
        while ((i = t.indexOf("\"sdk:", i)) >= 0) { int e = t.indexOf('"', i + 1); String tag = t.substring(i + 1, e); assertTrue(tag, tag.length() > 25); i = e; n++; }
        assertTrue(n > 20);
    }

    private static String bad(String json) {
        List<String> e = ProfileValidator.validate(json);
        return e.isEmpty() ? "" : e.toString();
    }

    @Test public void rejectsMissingOrBadSources() throws Exception {
        String good = read("nokia-s40-dp2.0.json");
        assertEquals("", bad(good));
        // value without a tag
        assertTrue(bad(good.replace("\"heapBytes\": \"sdk:", "\"heapBytesX\": \"sdk:")), bad(good.replace("\"heapBytes\": \"sdk:", "\"heapBytesX\": \"sdk:")).length() > 0);
        // tag of an unknown kind
        String t = good.replace("\"cldc11\": \"sdk:", "\"cldc11\": \"guess:");
        assertTrue(bad(t), bad(t).contains("must start with"));
        // 'sdk:' with no citation
        t = good.replaceFirst("\"jarMaxBytes\": \"sdk:[^\"]*\"", "\"jarMaxBytes\": \"sdk:\"");
        assertTrue(bad(t), bad(t).contains("needs a citation"));
        // a leaf of a per-leaf section without its own tag
        t = good.replace("\"supports.mixing\": \"sdk:", "\"supports.mixing.x\": \"sdk:");
        assertTrue(bad(t), bad(t).contains("no source tag for 'properties.supports.mixing'"));
        // section without any tag
        t = good.replace("\"_source\": \"UNVERIFIED\",\n    \"id\"", "\"id\"");
        assertTrue(bad(t), bad(t).contains("no source tag for section 'timeZone'"));
    }

    @Test public void rejectsSchemaAndConsistencyErrors() throws Exception {
        String good = read("nokia-s40-dp2.0.json");
        assertTrue(bad(good.replace("\"heapBytes\": 1024000", "\"heapBytes\": \"x\"")).contains("schema"));
        assertTrue(bad(good.replace("\"timeZone\": {", "\"bogus\": {}, \"timeZone\": {")).contains("unknown key"));
        assertTrue(bad(good.replace("\"canvasWidth\": 128", "\"canvasWidth\": 129")).contains("canvas larger"));
        assertTrue(bad(good.replace("\"MIDP-2.0\",\n", "\"MIDP-1.0\",\n")).contains("microedition.profiles"));
        assertTrue(bad(good.replace("\"cldc11\": true", "\"cldc11\": false")).contains("microedition.configuration"));
        assertTrue(bad(good.replace("\"midp-2.0\",\n    \"nokia-ui\"", "\"nokia-ui\"")).contains("needs midp-2.0"));
        assertTrue(bad(good.replace("\"Nokia-MIDlet-Category\"\n", "\"Nokia-Bogus\"\n")).contains("unknown attribute"));
        assertTrue(bad(good.replace("\"maxClassMajor\": 49", "\"maxClassMajor\": 60")).contains("maxClassMajor"));
        assertTrue(bad("[]").contains("schema"));
    }
}

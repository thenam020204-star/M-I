package jlkvm.natives;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jlkvm.Corpus;
import jlkvm.DeviceProfile;
import jlkvm.profile.ProfileLoader;
import jlkvm.tmp.RunJar;
import org.junit.After;
import org.junit.Test;

/** T20: System properties / Runtime / currentTimeMillis / Connector policy follow the device profile JSON (tests/vmspec/cases/cl07_system). */
public class ClasslibSystemTest {
    static final File ROM = new File(Corpus.ROOT, "vm-core/build/guest-stub/rom.jar");
    static final File JAR = new File(Corpus.ROOT, "tests/vmspec/build/vmspec/cl07_system/cl07_system.jar");
    static final File PROFILE = new File(Corpus.ROOT, "profiles/jlkvm-min.json");

    @After
    public void reset() {
        RunJar.profileJson = null;
        RunJar.deterministic = false;
    }

    private static List<String> run(String profileJson, boolean deterministic) throws Exception {
        assumeTrue("run tests/vmspec/run_vmspec.sh --engine jlkvm first", ROM.isFile() && JAR.isFile());
        RunJar.profileJson = profileJson;
        RunJar.deterministic = deterministic;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bo, true, "UTF-8");
        assertTrue("timeout", RunJar.run(ROM.getPath(), JAR.getPath(), false, false, false, false, 60, ps, 0, false, 4L << 20, null));
        List<String> out = new ArrayList<String>();
        for (String l : new String(bo.toByteArray(), "UTF-8").split("\n")) if (l.startsWith("> ")) out.add(l.substring(2));
        return out;
    }

    private static String get(List<String> out, String prefix) {
        for (String l : out) if (l.startsWith(prefix)) return l.substring(prefix.length());
        fail("no line '" + prefix + "' in " + out);
        return null;
    }

    private static void checkProperties(List<String> out, DeviceProfile p) {
        int n = 0;
        for (String l : out) {
            if (!l.startsWith("prop ")) continue;
            int i = l.lastIndexOf(" = ");
            String key = l.substring(5, i), val = l.substring(i + 3);
            String want = p.properties.get(key);
            assertEquals("property '" + key + "'", want == null ? "null" : want, val);
            n++;
        }
        assertTrue("properties printed: " + n, n >= 15);
    }

    @Test
    public void propertiesFollowTheProfileFile() throws Exception {
        DeviceProfile p = ProfileLoader.fromJson(new String(Files.readAllBytes(PROFILE.toPath()), "UTF-8"));
        List<String> out = run(PROFILE.getPath(), false);
        checkProperties(out, p);
        assertEquals("CLDC-1.1", get(out, "prop microedition.configuration = "));   // spelling fixed (JL-Mod had CDLC-1.1)
        assertEquals("null", get(out, "prop x = "));
        assertEquals("null", get(out, "prop microedition.Platform = "));            // keys are case sensitive
        assertEquals("NullPointerException", get(out, "getProperty(null) "));
        assertEquals("IllegalArgumentException", get(out, "getProperty(\"\") "));
    }

    @Test
    public void propertiesChangeWithTheProfile() throws Exception {
        String json = "{\"name\":\"alt\",\"heapBytes\":1048576,\"properties\":{\"microedition.platform\":\"Nokia6233\",\"microedition.encoding\":\"UTF-8\","
            + "\"microedition.configuration\":\"CLDC-1.0\",\"microedition.profiles\":\"MIDP-1.0 MIDP-2.0\",\"x\":\"y\"},\"timeZone\":{\"id\":\"GMT+02:00\",\"offsetMillis\":7200000}}";
        File f = File.createTempFile("profile", ".json");
        Files.write(f.toPath(), json.getBytes("UTF-8"));
        List<String> out = run(f.getPath(), false);
        checkProperties(out, ProfileLoader.fromJson(json));
        assertEquals("Nokia6233", get(out, "prop microedition.platform = "));
        assertEquals("y", get(out, "prop x = "));
        assertEquals("null", get(out, "prop microedition.locale = "));              // absent from this profile
        assertEquals("GMT+02:00 7200000", get(out, "default tz "));
        assertEquals("2", get(out, "default calendar epoch hour "));
        // heap = 1 MB comes from the profile through RunJar's heap argument (4 MB here): memory figures are not profile-file tests
    }

    @Test
    public void runtimeMemoryComesFromTheArena() throws Exception {
        List<String> out = run(PROFILE.getPath(), false);
        long total = Long.parseLong(get(out, "total "));
        assertTrue("total " + total, total <= (4L << 20) && total >= (4L << 20) - 64);   // RunJar heap argument (4 MB): Runtime.totalMemory() is the arena size (minus the reserved null header)
        assertEquals("true by-at-least true", get(out, "free decreased "));
        assertTrue(get(out, "free recovered "), get(out, "free recovered ").startsWith("true true total-constant true free<=total true"));
        assertEquals("true", get(out, "gc ok "));
        assertTrue(get(out, "OutOfMemoryError caught"), get(out, "OutOfMemoryError caught, free now >= 0 ").equals("true"));
        assertEquals("true true", get(out, "clock advanced "));
    }

    @Test
    public void connectorPolicy() throws Exception {
        List<String> out = run(PROFILE.getPath(), false);
        assertEquals("SecurityException", get(out, "open http "));
        assertEquals("IOException", get(out, "open https "));
        assertEquals("IOException", get(out, "open socket "));
        assertEquals("SecurityException", get(out, "open HTTP upper "));
        assertEquals("opened jlkvm.internal.io.testnet.Protocol data=testnet-ok://x/y", get(out, "open testnet "));
        assertEquals("ConnectionNotFoundException", get(out, "open nohandler "));
        assertEquals("ConnectionNotFoundException", get(out, "open file "));         // local scheme: not subject to the network policy, no handler in CLDC
        assertEquals("ConnectionNotFoundException", get(out, "open comm "));
        assertEquals("IllegalArgumentException", get(out, "open null "));
        assertEquals("IllegalArgumentException", get(out, "open noscheme "));
        assertEquals("IllegalArgumentException", get(out, "open emptyscheme "));
        assertEquals("IllegalArgumentException", get(out, "open badscheme "));
        assertEquals("IllegalArgumentException", get(out, "open badscheme2 "));
        assertEquals("java.lang.IllegalArgumentException", get(out, "bad mode "));
        assertEquals("ok", get(out, "openInputStream ").trim());
        assertEquals("java.lang.SecurityException", get(out, "openDataInputStream "));
        assertEquals("java.io.IOException", get(out, "openOutputStream "));
        assertEquals("123", get(out, "constants "));
    }

    @Test
    public void connectorPolicyFollowsTheProfile() throws Exception {
        String json = "{\"network\":{\"default\":\"allow\",\"http\":\"offline\",\"https\":\"deny\",\"socket\":\"offline\"}}";
        File f = File.createTempFile("profile", ".json");
        Files.write(f.toPath(), json.getBytes("UTF-8"));
        List<String> out = run(f.getPath(), false);
        assertEquals("IOException", get(out, "open http "));
        assertEquals("SecurityException", get(out, "open https "));
        assertEquals("IOException", get(out, "open socket "));
        assertEquals("opened jlkvm.internal.io.testnet.Protocol data=testnet-ok://x/y", get(out, "open testnet "));
        assertEquals("ConnectionNotFoundException", get(out, "open nohandler "));
    }

    @Test
    public void deterministicClockGivesIdenticalRuns() throws Exception {
        List<String> a = run(PROFILE.getPath(), true), b = run(PROFILE.getPath(), true);
        assertEquals(a, b);
        assertEquals("true true", get(a, "clock advanced "));   // the virtual clock advanced by the 100 ms sleep
    }

    static final File ENC_JAR = new File(Corpus.ROOT, "tests/vmspec/build/vmspec/cl11_default_encoding/cl11_default_encoding.jar");

    /** Raw console bytes of a run (the host does not decode them). */
    private static byte[] runRaw(String profileJson) throws Exception {
        assumeTrue("run tests/vmspec/run_vmspec.sh --engine jlkvm first", ROM.isFile() && ENC_JAR.isFile());
        RunJar.profileJson = profileJson;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bo, true, "UTF-8");
        assertTrue("timeout", RunJar.run(ROM.getPath(), ENC_JAR.getPath(), false, false, false, false, 60, ps));
        return bo.toByteArray();
    }

    private static String latin1(byte[] b) throws Exception { return new String(b, "ISO-8859-1"); }

    private static File profileWithEncoding(String enc) throws Exception {
        File f = File.createTempFile("profile", ".json");
        Files.write(f.toPath(), ("{\"properties\":{\"microedition.encoding\":\"" + enc + "\"}}").getBytes("UTF-8"));
        return f;
    }

    @Test
    public void defaultEncodingLatin1() throws Exception {
        String out = latin1(runRaw(PROFILE.getPath()));   // jlkvm-min.json: ISO-8859-1
        assertTrue(out, out.contains("> enc ISO-8859-1\n"));
        assertTrue(out, out.contains("> getBytes 41e93fff\n"));                   // U+20AC is not representable: '?'
        assertTrue(out, out.contains("> decode 41 c3 a9 e9 e2 82 ac \n"));        // every byte is a char
        assertTrue(out, out.contains("> decode range c3 a9 \n"));
        assertTrue(out, out.contains("> writer 41e93fff\n"));
        assertTrue(out, out.contains("> reader 41 c3 a9 e9 e2 82 ac \n"));
        assertTrue(out, out.contains("> console \u00e9?\n"));                       // console bytes in the default encoding
    }

    @Test
    public void defaultEncodingUtf8() throws Exception {
        String out = latin1(runRaw(profileWithEncoding("UTF-8").getPath()));
        assertTrue(out, out.contains("> enc UTF-8\n"));
        assertTrue(out, out.contains("> getBytes 41c3a9e282acc3bf\n"));
        assertTrue(out, out.contains("> decode 41 e9 fffd 20ac \n"));   // lone 0xE9 is malformed UTF-8: one replacement char (handling unspecified by CLDC)
        assertTrue(out, out.contains("> decode range e9 \n"));
        assertTrue(out, out.contains("> writer 41c3a9e282acc3bf\n"));
        assertTrue(out, out.contains("> console \u00c3\u00a9\u00e2\u0082\u00ac\n"));   // bytes C3 A9 E2 82 AC of U+00E9 U+20AC
    }

    @Test
    public void unsupportedDefaultEncodingFallsBackToLatin1() throws Exception {
        String out = latin1(runRaw(profileWithEncoding("EBCDIC").getPath()));
        assertTrue(out, out.contains("> getBytes 41e93fff\n"));
    }

    @Test
    public void profileLoaderRejectsMistakes() {
        Map<String, String> bad = new HashMap<String, String>();
        bad.put("{\"heapBytez\":1}", "unknown key");
        bad.put("{\"network\":{\"http\":\"maybe\"}}", "network policy");
        bad.put("{\"properties\":{\"a\":1}}", "must be a string");
        bad.put("[1]", "object expected");
        bad.put("{\"name\":\"x\"", "JSON");
        for (Map.Entry<String, String> e : bad.entrySet()) {
            try {
                ProfileLoader.fromJson(e.getKey());
                fail("accepted " + e.getKey());
            } catch (IllegalArgumentException x) {
                assertTrue(x.getMessage(), x.getMessage().contains(e.getValue()));
            }
        }
    }
}

package jlkvm.tools.apisigs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

public class ApiSigsTest {
    private static Path cache(String f) {
        return Paths.get(System.getProperty("jlkvm.cache", "../env/cache"), "maven", f);
    }

    @Test
    public void cldc11ClassCountEqualsPublicClassesInStub() throws Exception {
        Path jar = cache("cldcapi11-2.0.4.jar");
        assumeTrue("run tools/env/setup_toolchain.sh first", Files.exists(jar));
        ApiDb db = Extractor.extract("cldc-1.1", "test", null, jar);
        assertEquals(Extractor.countPublicClasses(jar), db.classes.size());
        assertTrue(db.classes.size() > 40);
    }

    @Test
    public void diffDetectsAndRoundTrips() throws Exception {
        Path jar10 = cache("cldcapi10-2.0.4.jar"), jar11 = cache("cldcapi11-2.0.4.jar");
        assumeTrue(Files.exists(jar10) && Files.exists(jar11));
        ApiDb a = Extractor.extract("cldc-1.0", "t", null, jar10), b = Extractor.extract("cldc-1.1", "t", null, jar11);
        assertTrue(Differ.diff(a, a).isEmpty());
        assertFalse("CLDC 1.1 adds float/WeakReference over 1.0", Differ.diff(a, b).isEmpty());
        Path tmp = Files.createTempFile("db", ".json");
        b.write(tmp);
        assertTrue(Differ.diff(b, ApiDb.read(tmp)).isEmpty());
    }
}

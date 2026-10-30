package ru.woesss.j2me.installer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assume;
import org.junit.Test;

/** T34: the install-time jarscan on the bundled databases (the same files the build copies into the assets). */
public class JarScanTest {
    private static File root() {
        return new File(System.getProperty("jlkvm.root", "..")).getAbsoluteFile();
    }

    /** Reads profile and databases from the repository, exactly the files the app build copies into assets/jarscan. */
    static JarScan.Source repo() {
        return path -> {
            File f = path.equals("jarscan/profile.json")
                    ? new File(root(), "tools/jarscan/profiles/cldc11-midp20-nokiaui-jsr.json")
                    : new File(root(), "tools/api-sigs/db/" + path.substring("jarscan/db/".length()));
            return new FileInputStream(f);
        };
    }

    private static File jar(String name) {
        File j = new File(root(), "tests/midlets/build/" + name + ".jar");
        Assume.assumeTrue("build it with ./gradlew :host-desktop:test first: " + j, j.isFile());
        return j;
    }

    @Test
    public void cleanGameIsSafeAndReportIsMarkdown() throws IOException {
        JarScan.Result r = JarScan.scan(jar("hello"), repo());
        assertTrue(r.summary, r.safe);
        assertEquals(0, r.errors);
        assertTrue(r.markdown, r.markdown.startsWith("# jarscan: hello.jar"));
        assertTrue(r.markdown, r.markdown.contains("**Result: OK**"));
        assertTrue(r.summary, r.summary.startsWith("jarscan: OK"));
    }

    @Test
    public void everyBundledDatabaseExists() throws IOException {
        // the profile names the databases the build must copy; a missing one would make every install skip the scan
        try (InputStream in = repo().open("jarscan/profile.json")) {
            java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) > 0; ) bo.write(b, 0, n);
            String s = bo.toString("UTF-8");
            for (String api : new String[]{"cldc-1.1", "midp-2.0", "s40dp2-nokia-ui", "s40dp2-wma-1.1", "jsr75-fc", "jsr75-pim"}) {
                assertTrue(api, s.contains("\"" + api + "\""));
                repo().open("jarscan/db/" + api + ".json").close();
            }
        }
    }
}

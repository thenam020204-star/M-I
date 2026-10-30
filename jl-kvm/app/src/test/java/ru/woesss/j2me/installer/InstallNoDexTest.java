package ru.woesss.j2me.installer;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;

import org.junit.Assume;
import org.junit.Test;

import ru.woesss.j2me.jar.Descriptor;

/** PLAN T34: installing a game writes no converted.zip / *.dex, keeps the JAR as res.jar, and writes the jarscan report. */
public class InstallNoDexTest {
    private static List<String> names(File dir, String prefix) {
        List<String> l = new ArrayList<>();
        File[] fs = dir.listFiles();
        if (fs != null) {
            for (File f : fs) {
                l.add(prefix + f.getName());
                if (f.isDirectory()) l.addAll(names(f, prefix + f.getName() + "/"));
            }
        }
        return l;
    }

    @Test
    public void installedGameIsJarDescriptorAndReportOnly() throws Exception {
        File jar = new File(System.getProperty("jlkvm.root", ".."), "tests/midlets/build/hello.jar").getAbsoluteFile();
        Assume.assumeTrue("build tests/midlets/build/hello.jar first (tests/midlets/build.sh)", jar.isFile());
        JarScan.testSource = JarScanTest.repo();
        Descriptor desc;
        try (ZipFile z = new ZipFile(jar)) {
            byte[] b = new byte[(int) z.getEntry("META-INF/MANIFEST.MF").getSize()];
            try (java.io.InputStream in = z.getInputStream(z.getEntry("META-INF/MANIFEST.MF"))) {
                int n = 0;
                while (n < b.length) n += in.read(b, n, b.length - n);
            }
            desc = new Descriptor(new String(b, "UTF-8"), false);
        }
        File dir = Files.createTempDirectory("jlkvm-install").toFile();
        String summary = AppInstaller.writeGameFiles(dir, jar, desc);
        List<String> files = names(dir, "");
        for (String f : files) {
            assertFalse("no DEX artefact expected: " + f, f.contains("converted") || f.endsWith(".dex") || f.endsWith(".dex.conf") || f.endsWith(".zip"));
        }
        assertTrue(files.toString(), files.containsAll(Arrays.asList("res.jar", "midlet.conf", "jarscan-report.md")));
        assertEquals(jar.length(), new File(dir, "res.jar").length());
        String report = new String(Files.readAllBytes(new File(dir, "jarscan-report.md").toPath()), "UTF-8");
        assertTrue(report, report.contains("**Result: OK**"));
        assertNotNull(summary);
        assertTrue(summary, summary.startsWith("jarscan: OK"));
    }
}

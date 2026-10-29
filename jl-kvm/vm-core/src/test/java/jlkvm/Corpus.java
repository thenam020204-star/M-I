package jlkvm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** Test corpus: the jars built by tests/vmspec/run_vmspec.sh and the CLDC/MIDP stub jars (tools/env/cache). */
public final class Corpus {
    private Corpus() {}

    public static final File ROOT = new File("..").getAbsoluteFile();
    public static final File VMSPEC_BUILD = new File(ROOT, "tests/vmspec/build/vmspec");
    public static final File MAVEN = new File(ROOT, "tools/env/cache/maven");

    public static byte[] read(InputStream in) throws Exception {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        byte[] b = new byte[8192];
        int n;
        while ((n = in.read(b)) > 0) o.write(b, 0, n);
        in.close();
        return o.toByteArray();
    }

    /** vmspec case jars: case id -> jar file (empty if run_vmspec.sh has not been run). */
    public static Map<String, File> vmspecJars() {
        Map<String, File> m = new LinkedHashMap<String, File>();
        File[] dirs = VMSPEC_BUILD.listFiles();
        if (dirs == null) return m;
        java.util.Arrays.sort(dirs);
        for (File d : dirs) {
            File j = new File(d, d.getName() + ".jar");
            if (j.isFile()) m.put(d.getName(), j);
        }
        return m;
    }

    public static File stub(String prefix) {
        File[] fs = MAVEN.listFiles();
        if (fs != null) for (File f : fs) if (f.getName().startsWith(prefix + "-") && f.getName().endsWith(".jar")) return f;
        return null;
    }

    /** internal class name -> bytes for every .class of the jar. */
    public static Map<String, byte[]> classes(File jar) throws Exception {
        Map<String, byte[]> m = new LinkedHashMap<String, byte[]>();
        ZipFile z = new ZipFile(jar);
        try {
            for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements();) {
                ZipEntry ze = e.nextElement();
                if (ze.getName().endsWith(".class")) {
                    String n = ze.getName();
                    m.put(n.substring(0, n.length() - 6), read(z.getInputStream(ze)));
                }
            }
        } finally {
            z.close();
        }
        return m;
    }

    /** All class files of all vmspec jars (key "case/ClassName") and the stubs. */
    public static Map<String, byte[]> allClasses() throws Exception {
        Map<String, byte[]> all = new LinkedHashMap<String, byte[]>();
        for (Map.Entry<String, File> e : vmspecJars().entrySet())
            for (Map.Entry<String, byte[]> c : classes(e.getValue()).entrySet()) all.put(e.getKey() + "/" + c.getKey(), c.getValue());
        for (String s : new String[] {"cldcapi11", "midpapi20"}) {
            File f = stub(s);
            if (f != null) for (Map.Entry<String, byte[]> c : classes(f).entrySet()) all.put(s + "/" + c.getKey(), c.getValue());
        }
        return all;
    }

    public static List<String> names(Map<String, ?> m) { return new ArrayList<String>(m.keySet()); }
}

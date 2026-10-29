package jlkvm.tmp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import jlkvm.ClassSource;

/** TEST-ONLY ClassSource over a jar read with java.util.zip (the real hosts use ru.woesss.util.zip.ZipFile, PLAN D16). Last duplicate entry wins. */
public final class JarSource implements ClassSource {
    private final ZipFile zip;
    private final Map<String, ZipEntry> entries = new HashMap<String, ZipEntry>();

    public JarSource(String path) throws java.io.IOException {
        zip = new ZipFile(path);
        for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements(); ) {
            ZipEntry z = e.nextElement();
            entries.put(z.getName(), z);
        }
    }

    private byte[] read(String name) {
        ZipEntry z = entries.get(name);
        if (z == null || z.isDirectory()) return null;
        try {
            InputStream in = zip.getInputStream(z);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = in.read(buf)) > 0) bo.write(buf, 0, n);
            in.close();
            return bo.toByteArray();
        } catch (java.io.IOException ex) {
            return null;
        }
    }

    public byte[] readClass(String internalName) { return read(internalName + ".class"); }
    public byte[] readResource(String absPath) { return read(absPath.startsWith("/") ? absPath.substring(1) : absPath); }
}

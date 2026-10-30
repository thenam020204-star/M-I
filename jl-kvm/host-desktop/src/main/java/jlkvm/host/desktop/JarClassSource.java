package jlkvm.host.desktop;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import jlkvm.ClassSource;

/**
 * A game JAR (or ROM jar) read in place, never unpacked (PLAN D16): entries are kept by their exact name, so names that differ only
 * by case stay distinct and resource lookups are case sensitive. Uses java.util.zip; the Android host uses the JL-Mod tolerant ZipFile
 * (ru.woesss.util.zip.ZipFile) for damaged JARs - not available to this module.
 */
public final class JarClassSource implements ClassSource, Closeable {
    private final ZipFile zip;
    private final Map<String, ZipEntry> entries = new HashMap<String, ZipEntry>();

    public JarClassSource(File jar) throws IOException {
        zip = new ZipFile(jar);
        for (Enumeration<? extends ZipEntry> en = zip.entries(); en.hasMoreElements();) {
            ZipEntry e = en.nextElement();
            if (!entries.containsKey(e.getName())) entries.put(e.getName(), e);   // duplicate names: the first one wins
        }
    }

    private byte[] read(String name) {
        ZipEntry e = entries.get(name);
        if (e == null || e.isDirectory()) return null;
        try (InputStream in = zip.getInputStream(e)) {
            java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream(Math.max(32, (int) Math.max(0, e.getSize())));
            byte[] b = new byte[8192];
            for (int n; (n = in.read(b)) > 0;) bo.write(b, 0, n);
            return bo.toByteArray();
        } catch (IOException x) {
            return null;
        }
    }

    public byte[] readClass(String internalName) { return read(internalName + ".class"); }

    public byte[] readResource(String absPath) { return read(absPath.startsWith("/") ? absPath.substring(1) : absPath); }

    public void close() throws IOException { zip.close(); }
}

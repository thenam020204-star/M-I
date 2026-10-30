package jlkvm.host.fs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link HostFs} over a host directory: root {@code c:/} is {@code base/c}, {@code root1/} is {@code base/root1} (one directory per root,
 * created on demand). Nothing outside {@code base} is ever touched: every call checks the segments (no ".." or absolute path can be spelled)
 * and then the CANONICAL path of the target, so a symlink inside a root that points elsewhere is refused with an IOException (also for
 * the parent of a file to be created). Attributes (read-only / hidden) are kept in memory only. Quota per root; usage is counted once and then
 * followed (files changed outside the run are not noticed). Pure java.io (Android as well).
 */
public final class DirFs implements HostFs {
    private final File base;
    private final String[] roots;
    private final long quota;
    private final Map<String, Integer> attrs = new HashMap<String, Integer>();
    private final Map<String, Long> used = new HashMap<String, Long>();

    public DirFs(File base, List<String> roots, long quotaBytes) {
        this.base = base;
        this.roots = roots.toArray(new String[0]);
        this.quota = quotaBytes;
    }

    public String[] roots() { return roots.clone(); }

    private File rootDir(String root) throws IOException {
        File d = new File(base, root.substring(0, root.length() - 1).replace(':', '_'));
        if (!d.isDirectory() && !d.mkdirs()) throw new IOException("cannot create the root directory");
        return d;
    }

    /** The host file of a path; IOException when the path is invalid or its canonical form is outside the root. */
    private File resolve(String path) throws IOException {
        String root = HostFs.validate(roots, path);
        File rd = rootDir(root);
        File f = path.length() == root.length() ? rd : new File(rd, path.substring(root.length()).replace('/', File.separatorChar));
        String rc = rd.getCanonicalPath(), fc = f.getCanonicalPath();
        if (!fc.equals(rc) && !fc.startsWith(rc + File.separator)) throw new IOException("path leaves the sandbox: " + path);
        return f;
    }

    private File tryResolve(String path) { try { return resolve(path); } catch (IOException e) { return null; } }

    private static String rootOf(String path, String[] roots) {
        for (String r : roots) if (path.startsWith(r)) return r;
        return null;
    }

    public synchronized boolean exists(String p) { File f = tryResolve(p); return f != null && f.exists(); }

    public synchronized boolean isDirectory(String p) { File f = tryResolve(p); return f != null && f.isDirectory(); }

    public synchronized long length(String p) { File f = tryResolve(p); return f != null && f.isFile() ? f.length() : -1; }

    public synchronized long lastModified(String p) { File f = tryResolve(p); return f != null && f.exists() ? f.lastModified() : 0; }

    public synchronized String[] list(String dir) throws IOException {
        File d = resolve(dir);
        if (!d.isDirectory()) throw new IOException("not a directory");
        String[] names = d.list();
        List<String> out = new ArrayList<String>();
        String prefix = dir.endsWith("/") ? dir : dir + "/";
        if (names != null) for (String n : names) {
            File c = tryResolve(prefix + n);
            if (c == null) continue;   // an entry that leads out of the root (symlink) is not shown
            out.add(c.isDirectory() ? n + "/" : n);
        }
        String[] a = out.toArray(new String[0]);
        Arrays.sort(a);
        return a;
    }

    public synchronized void create(String path) throws IOException {
        File f = resolve(path);
        if (f.exists()) throw new IOException("already exists");
        File par = f.getParentFile();
        if (par == null || !par.isDirectory()) throw new IOException("parent directory does not exist");
        if (!f.createNewFile()) throw new IOException("cannot create");
    }

    public synchronized void mkdir(String path) throws IOException {
        File f = resolve(path);
        if (f.exists()) throw new IOException("already exists");
        File par = f.getParentFile();
        if (par == null || !par.isDirectory()) throw new IOException("parent directory does not exist");
        if (!f.mkdir()) throw new IOException("cannot create");
    }

    public synchronized void delete(String path) throws IOException {
        File f = resolve(path);
        if (path.equals(HostFs.validate(roots, path))) throw new IOException("a root cannot be deleted");
        if (!f.exists()) throw new IOException("does not exist");
        long len = f.isFile() ? f.length() : 0;
        if (f.isDirectory()) { String[] l = f.list(); if (l != null && l.length > 0) throw new IOException("directory not empty"); }
        if (!f.delete()) throw new IOException("cannot delete");
        addUsed(rootOf(path, roots), -len);
        attrs.remove(path);
    }

    public synchronized void rename(String path, String newName) throws IOException {
        File f = resolve(path);
        if (path.equals(HostFs.validate(roots, path))) throw new IOException("a root cannot be renamed");
        if (!f.exists()) throw new IOException("does not exist");
        if (newName.length() == 0 || newName.indexOf('/') >= 0) throw new IOException("a bare name is needed to rename");
        String target = path.substring(0, path.lastIndexOf('/') + 1) + newName;
        File t = resolve(target);
        if (t.exists()) throw new IOException("target exists");
        if (!f.renameTo(t)) throw new IOException("cannot rename");
        Integer a = attrs.remove(path);
        if (a != null) attrs.put(target, a);
    }

    public synchronized void truncate(String path, long size) throws IOException {
        File f = resolve(path);
        if (!f.isFile()) throw new IOException("not a file");
        long old = f.length();
        if (size >= old) return;
        RandomAccessFile r = new RandomAccessFile(f, "rw");
        try { r.setLength(size); } finally { r.close(); }
        addUsed(rootOf(path, roots), size - old);
    }

    public long total(String root) { return quota; }

    private long scan(File d) {
        long u = 0;
        File[] l = d.listFiles();
        if (l != null) for (File c : l) u += c.isDirectory() ? scan(c) : c.length();
        return u;
    }

    public synchronized long used(String root) {
        Long u = used.get(root);
        if (u == null) {
            try { u = Long.valueOf(scan(rootDir(root))); } catch (IOException e) { u = Long.valueOf(0); }
            used.put(root, u);
        }
        return u.longValue();
    }

    private void addUsed(String root, long delta) { if (root != null) used.put(root, Long.valueOf(Math.max(0, used(root) + delta))); }

    public synchronized int attrs(String p) { Integer a = attrs.get(p); return a == null || !exists(p) ? 0 : a.intValue(); }

    public synchronized void setAttr(String p, int bit, boolean on) {
        if (!exists(p)) return;
        int a = attrs(p);
        attrs.put(p, Integer.valueOf(on ? a | bit : a & ~bit));
    }

    public synchronized Handle open(final String path, boolean write) throws IOException {
        File f = resolve(path);
        if (!f.exists()) throw new IOException("does not exist");
        if (f.isDirectory()) throw new IOException("is a directory");
        final RandomAccessFile raf = new RandomAccessFile(f, write ? "rw" : "r");
        final String root = rootOf(path, roots);
        return new Handle() {
            public int read(long pos, byte[] b, int off, int len) throws IOException {
                raf.seek(pos);
                return raf.read(b, off, len);
            }
            public void write(long pos, byte[] b, int off, int len) throws IOException {
                synchronized (DirFs.this) {
                    long old = raf.length(), end = pos + len, growth = Math.max(0, end - old);
                    if (growth > 0 && used(root) + growth > quota) throw new IOException("not enough space");
                    raf.seek(pos > old ? old : pos);
                    if (pos > old) raf.write(new byte[(int) (pos - old)]);   // gap of zeros (rare)
                    raf.write(b, off, len);
                    addUsed(root, growth);
                }
            }
            public void close() { try { raf.close(); } catch (IOException e) { /* nothing to do */ } }
        };
    }
}

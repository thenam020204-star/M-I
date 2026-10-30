package jlkvm.host.fs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A {@link HostFs} that lives in memory (default of the runners; nothing touches the host disk). Case sensitive. */
public final class MemoryFs implements HostFs {
    private static final class Node {
        boolean dir;
        byte[] data = new byte[0];
        int len;
        long mtime;
        int attrs;
    }

    private final String[] roots;
    private final long quota;
    private final Map<String, Node> nodes = new HashMap<String, Node>();   // key: "c:/dir/file" ("c:/" for the root)

    public MemoryFs(List<String> roots, long quotaBytes) {
        this.roots = roots.toArray(new String[0]);
        this.quota = quotaBytes;
        for (String r : this.roots) { Node n = new Node(); n.dir = true; nodes.put(r, n); }
    }

    private String check(String path) throws IOException { return HostFs.validate(roots, path); }

    private static String parent(String path) {
        int i = path.lastIndexOf('/');
        return path.substring(0, i + 1);   // keeps the trailing '/'
    }

    /** Key of the parent directory: a root keeps its trailing '/', other directories have none. */
    private String parentKey(String path) {
        String p = parent(path);
        return nodes.containsKey(p) ? p : p.substring(0, p.length() - 1);
    }

    public String[] roots() { return roots.clone(); }

    public synchronized boolean exists(String p) { try { check(p); } catch (IOException e) { return false; } return nodes.containsKey(p); }

    public synchronized boolean isDirectory(String p) { return exists(p) && nodes.get(p).dir; }

    public synchronized long length(String p) { return exists(p) && !nodes.get(p).dir ? nodes.get(p).len : -1; }

    public synchronized long lastModified(String p) { return exists(p) ? nodes.get(p).mtime : 0; }

    public synchronized String[] list(String dir) throws IOException {
        check(dir);
        Node d = nodes.get(dir);
        if (d == null || !d.dir) throw new IOException("not a directory");
        String prefix = dir.endsWith("/") ? dir : dir + "/";
        List<String> out = new ArrayList<String>();
        for (Map.Entry<String, Node> e : nodes.entrySet()) {
            String k = e.getKey();
            if (k.length() > prefix.length() && k.startsWith(prefix) && k.indexOf('/', prefix.length()) < 0) out.add(k.substring(prefix.length()) + (e.getValue().dir ? "/" : ""));
        }
        String[] a = out.toArray(new String[0]);
        Arrays.sort(a);
        return a;
    }

    private void add(String path, boolean dir) throws IOException {
        check(path);
        if (nodes.containsKey(path)) throw new IOException("already exists");
        Node pn = nodes.get(parentKey(path));
        if (pn == null || !pn.dir) throw new IOException("parent directory does not exist");
        Node n = new Node();
        n.dir = dir;
        n.mtime = System.currentTimeMillis();
        nodes.put(path, n);
    }

    public synchronized void create(String path) throws IOException { add(path, false); }

    public synchronized void mkdir(String path) throws IOException { add(path, true); }

    public synchronized void delete(String path) throws IOException {
        check(path);
        Node n = nodes.get(path);
        if (n == null) throw new IOException("does not exist");
        if (path.endsWith("/")) throw new IOException("a root cannot be deleted");
        if (n.dir && list(path).length > 0) throw new IOException("directory not empty");
        nodes.remove(path);
    }

    public synchronized void rename(String path, String newName) throws IOException {
        check(path);
        Node n = nodes.get(path);
        if (n == null) throw new IOException("does not exist");
        if (path.endsWith("/")) throw new IOException("a root cannot be renamed");
        if (newName.length() == 0 || newName.indexOf('/') >= 0) throw new IOException("a bare name is needed to rename");
        String target = parent(path) + newName;
        check(target);
        if (nodes.containsKey(target)) throw new IOException("target exists");
        if (n.dir) {
            String old = path + "/";
            Map<String, Node> moved = new HashMap<String, Node>();
            for (Map.Entry<String, Node> e : nodes.entrySet()) if (e.getKey().startsWith(old)) moved.put(target + "/" + e.getKey().substring(old.length()), e.getValue());
            for (String k : new ArrayList<String>(nodes.keySet())) if (k.startsWith(old)) nodes.remove(k);
            nodes.putAll(moved);
        }
        nodes.remove(path);
        nodes.put(target, n);
    }

    public synchronized void truncate(String path, long size) throws IOException {
        check(path);
        Node n = nodes.get(path);
        if (n == null || n.dir) throw new IOException("not a file");
        if (size < n.len) { n.len = (int) size; n.mtime = System.currentTimeMillis(); }
    }

    public long total(String root) { return quota; }

    public synchronized long used(String root) {
        long u = 0;
        for (Map.Entry<String, Node> e : nodes.entrySet()) if (e.getKey().startsWith(root)) u += e.getValue().len;
        return u;
    }

    public synchronized int attrs(String p) { return exists(p) ? nodes.get(p).attrs : 0; }

    public synchronized void setAttr(String p, int bit, boolean on) { if (exists(p)) { Node n = nodes.get(p); n.attrs = on ? n.attrs | bit : n.attrs & ~bit; } }

    public synchronized Handle open(final String path, boolean write) throws IOException {
        check(path);
        final Node n = nodes.get(path);
        if (n == null) throw new IOException("does not exist");
        if (n.dir) throw new IOException("is a directory");
        final String root = HostFs.validate(roots, path);
        return new Handle() {
            public int read(long pos, byte[] b, int off, int len) {
                synchronized (MemoryFs.this) {
                    if (pos >= n.len) return -1;
                    int c = (int) Math.min(len, n.len - pos);
                    System.arraycopy(n.data, (int) pos, b, off, c);
                    return c;
                }
            }
            public void write(long pos, byte[] b, int off, int len) throws IOException {
                synchronized (MemoryFs.this) {
                    long end = pos + len;
                    if (end > n.len && used(root) + (end - n.len) > quota) throw new IOException("not enough space");
                    if (end > n.data.length) n.data = Arrays.copyOf(n.data, (int) Math.max(end, Math.min(Integer.MAX_VALUE - 8, n.data.length * 2L)));
                    if (pos > n.len) Arrays.fill(n.data, n.len, (int) pos, (byte) 0);
                    System.arraycopy(b, off, n.data, (int) pos, len);
                    if (end > n.len) n.len = (int) end;
                    n.mtime = System.currentTimeMillis();
                }
            }
            public void close() { }
        };
    }
}

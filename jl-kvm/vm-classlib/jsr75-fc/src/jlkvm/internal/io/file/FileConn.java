/*
 * jl-kvm guest class library (JSR-75 FileConnection layer, API per tools/api-sigs/db/jsr75-fc.json). Origin: original code written for jl-kvm
 * (PLAN D12, T32a); behaviour from the JSR-75 FileConnection API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.io.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.file.ConnectionClosedException;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.IllegalModeException;

/**
 * A FileConnection on the virtual file system of the profile. The target is a root (profile file.roots, e.g. "c:/") and a list of
 * name segments below it; "file:///" alone is the (virtual) list of roots. Nothing here touches a host path: the segments are validated
 * (no "", ".", "..", backslash, NUL, ':'), and the host keeps every path inside its root again (HostFs). Names given back to the application
 * are decoded / encoded as %XX UTF-8 in URLs.
 */
final class FileConn implements FileConnection {
    private static final int ATTR_NOREAD = 1, ATTR_NOWRITE = 2, ATTR_HIDDEN = 4;

    private String root;              // canonical root ("c:/") or null for the root list
    private final Vector segs = new Vector();
    private boolean dirHint;          // the URL ended with '/'
    private final int mode;
    private boolean closed;

    private FileConn(int mode) {
        this.mode = mode;
    }

    static FileConn open(String name, int mode) throws IOException {
        if (Fs.perm0() == 0) {
            throw new SecurityException("File system access denied by the device profile");
        }
        if (!name.startsWith("//")) {
            throw new IllegalArgumentException("Missing '//' in file URL");
        }
        int slash = name.indexOf('/', 2);
        if (slash < 0) {
            throw new IllegalArgumentException("Missing path in file URL");
        }
        String host = name.substring(2, slash);
        if (host.length() > 0 && !host.equalsIgnoreCase("localhost")) {
            throw new IllegalArgumentException("Unsupported host in file URL: " + host);
        }
        String path = name.substring(slash);
        int semi = path.indexOf(';');
        if (semi >= 0) {
            path = path.substring(0, semi);
        }
        FileConn c = new FileConn(mode);
        if ((mode & Connector.WRITE) != 0 && Fs.perm0() < 2) {
            throw new SecurityException("Write access to files denied by the device profile");
        }
        c.parse(path);
        return c;
    }

    /** Sets root / segs / dirHint from an absolute path "/c:/dir/file" (percent-decoded here). */
    private void parse(String path) throws IOException {
        if (path.equals("/")) {
            root = null;
            return;
        }
        dirHint = path.endsWith("/");
        String p = path.substring(1);
        if (dirHint) {
            p = p.substring(0, p.length() - 1);
        }
        Vector parts = new Vector();
        int from = 0;
        for (;;) {
            int s = p.indexOf('/', from);
            String seg = decode(s < 0 ? p.substring(from) : p.substring(from, s));
            parts.addElement(seg);
            if (s < 0) {
                break;
            }
            from = s + 1;
        }
        String r = (String) parts.elementAt(0);
        if (r.length() == 0 || r.equals(".") || r.equals("..") || r.indexOf('\\') >= 0) {
            throw new IllegalArgumentException("Invalid root in file URL: " + r);
        }
        String canon = null;
        Enumeration roots = Fs.rootList().elements();
        while (roots.hasMoreElements()) {
            String x = (String) roots.nextElement();
            if (x.substring(0, x.length() - 1).equalsIgnoreCase(r)) {
                canon = x;
            }
        }
        if (canon == null) {
            throw new ConnectionNotFoundException("No such root: " + r);
        }
        root = canon;
        for (int i = 1; i < parts.size(); i++) {
            checkName((String) parts.elementAt(i));
            segs.addElement(parts.elementAt(i));
        }
    }

    private static void checkName(String s) {
        if (s.length() == 0 || s.equals(".") || s.equals("..") || s.indexOf('/') >= 0 || s.indexOf('\\') >= 0 || s.indexOf('\0') >= 0 || s.indexOf(':') >= 0) {
            throw new IllegalArgumentException("Invalid file name: " + s);
        }
    }

    private static int hexv(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        return -1;
    }

    private static String decode(String s) {
        if (s.indexOf('%') < 0) {
            return s;
        }
        byte[] out = new byte[s.length() * 3];
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '%') {
                int h = i + 2 < s.length() ? hexv(s.charAt(i + 1)) : -1;
                int l = i + 2 < s.length() ? hexv(s.charAt(i + 2)) : -1;
                if (h < 0 || l < 0) {
                    throw new IllegalArgumentException("Bad escape in file URL");
                }
                out[n++] = (byte) (h * 16 + l);
                i += 2;
            } else if (c < 128) {
                out[n++] = (byte) c;
            } else {
                byte[] b;
                try {
                    b = s.substring(i, i + 1).getBytes("UTF-8");
                } catch (java.io.UnsupportedEncodingException e) {
                    throw new IllegalArgumentException("UTF-8");
                }
                for (int k = 0; k < b.length; k++) {
                    out[n++] = b[k];
                }
            }
        }
        try {
            return new String(out, 0, n, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8");
        }
    }

    private static String encode(String s) {
        StringBuffer sb = new StringBuffer();
        byte[] b;
        try {
            b = s.getBytes("UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return s;
        }
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if ((v >= 'a' && v <= 'z') || (v >= 'A' && v <= 'Z') || (v >= '0' && v <= '9') || v == '-' || v == '_' || v == '.' || v == '~' || v == '(' || v == ')' || v == '!' || v == '+' || v == ',') {
                sb.append((char) v);
            } else {
                sb.append('%').append("0123456789ABCDEF".charAt(v >> 4)).append("0123456789ABCDEF".charAt(v & 15));
            }
        }
        return sb.toString();
    }

    // ---- helpers

    private void checkOpen() {
        if (closed) {
            throw new ConnectionClosedException("Connection closed");
        }
    }

    private void needRead() {
        if ((mode & Connector.READ) == 0) {
            throw new IllegalModeException("Connection not opened for reading");
        }
    }

    private void needWrite() {
        if ((mode & Connector.WRITE) == 0) {
            throw new IllegalModeException("Connection not opened for writing");
        }
        if (Fs.perm0() < 2) {
            throw new SecurityException("Write access to files denied by the device profile");
        }
    }

    private String host() {
        StringBuffer sb = new StringBuffer(root);
        for (int i = 0; i < segs.size(); i++) {
            if (i > 0) {
                sb.append('/');
            }
            sb.append((String) segs.elementAt(i));
        }
        return sb.toString();
    }

    private String hostChild(String name) {
        return join(host(), name);
    }

    private boolean isRootList() {
        return root == null;
    }

    private void needTarget() throws IOException {
        if (isRootList()) {
            throw new IOException("Not a file or directory: the list of roots");
        }
    }

    // ---- FileConnection

    public boolean isOpen() {
        return !closed;
    }

    public InputStream openInputStream() throws IOException {
        checkOpen();
        if ((mode & Connector.READ) == 0) {
            throw new IllegalModeException("Connection not opened for reading");
        }
        needTarget();
        String p = host();
        if ((Fs.attr0(p) & ATTR_NOREAD) != 0) {
            throw new SecurityException("File is not readable");
        }
        return new FcIn(Fs.open0(p, false), p);
    }

    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(openInputStream());
    }

    public OutputStream openOutputStream() throws IOException {
        return openOutputStream(0);
    }

    public OutputStream openOutputStream(long byteOffset) throws IOException {
        checkOpen();
        needWrite();
        if (byteOffset < 0) {
            throw new IllegalArgumentException("Negative offset");
        }
        needTarget();
        String p = host();
        if ((Fs.attr0(p) & ATTR_NOWRITE) != 0) {
            throw new SecurityException("File is not writable");
        }
        long size = Fs.length0(p);
        if (byteOffset > size && size >= 0) {
            throw new IOException("Offset beyond the end of the file");
        }
        return new FcOut(Fs.open0(p, true), byteOffset);
    }

    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(openOutputStream());
    }

    public long totalSize() {
        checkOpen();
        return isRootList() ? -1 : Fs.total0(root);
    }

    public long availableSize() {
        checkOpen();
        return isRootList() ? -1 : Fs.total0(root) - Fs.used0(root);
    }

    public long usedSize() {
        checkOpen();
        return isRootList() ? -1 : Fs.used0(root);
    }

    public long directorySize(boolean includeSubDirs) throws IOException {
        checkOpen();
        needTarget();
        needRead();
        String p = host();
        if (!Fs.exists0(p) || !Fs.isDir0(p)) {
            throw new IOException("Not a directory");
        }
        return dirSize(p, includeSubDirs);
    }

    private static long dirSize(String dir, boolean sub) {
        long total = 0;
        Enumeration e = split(Fs.list0(dir)).elements();
        while (e.hasMoreElements()) {
            String n = (String) e.nextElement();
            if (n.endsWith("/")) {
                if (sub) {
                    total += dirSize(join(dir, n.substring(0, n.length() - 1)), true);
                }
            } else {
                total += Fs.length0(join(dir, n));
            }
        }
        return total;
    }

    /** A host path of an entry of a directory (a root already ends with '/'). */
    private static String join(String dir, String name) {
        return dir.endsWith("/") ? dir + name : dir + "/" + name;
    }

    private static Vector split(String s) {
        Vector v = new Vector();
        int from = 0;
        while (from < s.length()) {
            int nul = s.indexOf('\0', from);
            if (nul < 0) {
                nul = s.length();
            }
            v.addElement(s.substring(from, nul));
            from = nul + 1;
        }
        return v;
    }

    public long fileSize() throws IOException {
        checkOpen();
        needTarget();
        needRead();
        return Fs.length0(host());
    }

    public boolean canRead() {
        checkOpen();
        return !isRootList() && Fs.exists0(host()) && (Fs.attr0(host()) & ATTR_NOREAD) == 0;
    }

    public boolean canWrite() {
        checkOpen();
        return !isRootList() && Fs.perm0() >= 2 && Fs.exists0(host()) && (Fs.attr0(host()) & ATTR_NOWRITE) == 0;
    }

    public boolean isHidden() {
        checkOpen();
        return !isRootList() && Fs.exists0(host()) && (Fs.attr0(host()) & ATTR_HIDDEN) != 0;
    }

    private void setAttr(int bit, boolean on) throws IOException {
        checkOpen();
        needWrite();
        needTarget();
        if (!Fs.exists0(host())) {
            throw new IOException("File does not exist");
        }
        Fs.setAttr0(host(), bit, on);
    }

    public void setReadable(boolean readable) throws IOException {
        setAttr(ATTR_NOREAD, !readable);
    }

    public void setWritable(boolean writable) throws IOException {
        setAttr(ATTR_NOWRITE, !writable);
    }

    public void setHidden(boolean hidden) throws IOException {
        setAttr(ATTR_HIDDEN, hidden);
    }

    public Enumeration list() throws IOException {
        return list(null, false);
    }

    public Enumeration list(String filter, boolean includeHidden) throws IOException {
        checkOpen();
        needRead();
        if (isRootList()) {
            return Fs.rootList().elements();
        }
        String p = host();
        if (!Fs.exists0(p) || !Fs.isDir0(p)) {
            throw new IOException("Not a directory");
        }
        Vector all = split(Fs.list0(p));
        Vector out = new Vector();
        for (int i = 0; i < all.size(); i++) {
            String n = (String) all.elementAt(i);
            String bare = n.endsWith("/") ? n.substring(0, n.length() - 1) : n;
            if (!includeHidden && (Fs.attr0(join(p, bare)) & ATTR_HIDDEN) != 0) {
                continue;
            }
            if (filter != null && !matches(filter, 0, bare, 0)) {
                continue;
            }
            out.addElement(n);
        }
        return out.elements();
    }

    /** Filter with the wildcard '*' only (any run of characters). */
    private static boolean matches(String f, int fi, String s, int si) {
        while (fi < f.length()) {
            char c = f.charAt(fi);
            if (c == '*') {
                for (int k = si; k <= s.length(); k++) {
                    if (matches(f, fi + 1, s, k)) {
                        return true;
                    }
                }
                return false;
            }
            if (si >= s.length() || s.charAt(si) != c) {
                return false;
            }
            fi++;
            si++;
        }
        return si == s.length();
    }

    public void create() throws IOException {
        checkOpen();
        needWrite();
        needTarget();
        if (segs.isEmpty() || dirHint) {
            throw new IOException("Not a file name");
        }
        Fs.create0(host());
    }

    public void mkdir() throws IOException {
        checkOpen();
        needWrite();
        needTarget();
        if (segs.isEmpty()) {
            throw new IOException("A root already exists");
        }
        Fs.mkdir0(host());
    }

    public boolean exists() {
        checkOpen();
        return isRootList() || Fs.exists0(host());
    }

    public boolean isDirectory() {
        checkOpen();
        return isRootList() || Fs.isDir0(host());
    }

    public void delete() throws IOException {
        checkOpen();
        needWrite();
        needTarget();
        if (segs.isEmpty()) {
            throw new IOException("A root cannot be deleted");
        }
        Fs.delete0(host());
    }

    public void rename(String newName) throws IOException {
        checkOpen();
        needWrite();
        needTarget();
        if (newName == null) {
            throw new NullPointerException("newName");
        }
        String bare = newName.endsWith("/") ? newName.substring(0, newName.length() - 1) : newName;
        checkName(bare);
        if (segs.isEmpty()) {
            throw new IOException("A root cannot be renamed");
        }
        Fs.rename0(host(), bare);
        segs.setElementAt(bare, segs.size() - 1);
    }

    public void truncate(long byteOffset) throws IOException {
        checkOpen();
        needWrite();
        needTarget();
        if (byteOffset < 0) {
            throw new IllegalArgumentException("Negative offset");
        }
        Fs.truncate0(host(), byteOffset);
    }

    public void setFileConnection(String fileName) throws IOException {
        checkOpen();
        if (fileName == null) {
            throw new NullPointerException("fileName");
        }
        if (isRootList()) {
            throw new IOException("Cannot navigate from the list of roots");
        }
        if (fileName.equals("..")) {
            if (segs.isEmpty()) {
                throw new IOException("Already at the root");
            }
            segs.removeElementAt(segs.size() - 1);
            dirHint = true;
            return;
        }
        String bare = fileName.endsWith("/") ? fileName.substring(0, fileName.length() - 1) : fileName;
        checkName(bare);
        if (!(segs.isEmpty() || (Fs.exists0(host()) && Fs.isDir0(host())))) {
            throw new IOException("The connection is not a directory");
        }
        if (!Fs.exists0(hostChild(bare))) {
            throw new IOException("No such file or directory: " + bare);
        }
        segs.addElement(bare);
        dirHint = fileName.endsWith("/");
    }

    private boolean dirNow() {
        return isRootList() || segs.isEmpty() || (Fs.exists0(host()) ? Fs.isDir0(host()) : dirHint);
    }

    public String getName() {
        if (isRootList() || segs.isEmpty()) {
            return "";
        }
        String n = (String) segs.elementAt(segs.size() - 1);
        return dirNow() ? n + "/" : n;
    }

    public String getPath() {
        if (isRootList()) {
            return "/";
        }
        StringBuffer sb = new StringBuffer("/").append(root);
        int n = dirNow() ? segs.size() : segs.size() - 1;
        for (int i = 0; i < n; i++) {
            sb.append((String) segs.elementAt(i)).append('/');
        }
        return sb.toString();
    }

    public String getURL() {
        if (isRootList()) {
            return "file:///";
        }
        StringBuffer sb = new StringBuffer("file:///").append(root);
        for (int i = 0; i < segs.size(); i++) {
            if (i > 0) {
                sb.append('/');
            }
            sb.append(encode((String) segs.elementAt(i)));
        }
        if (!segs.isEmpty() && dirNow()) {
            sb.append('/');
        }
        return sb.toString();
    }

    public long lastModified() {
        checkOpen();
        return isRootList() ? 0 : Fs.mtime0(host());
    }

    public void close() throws IOException {
        closed = true;
    }
}

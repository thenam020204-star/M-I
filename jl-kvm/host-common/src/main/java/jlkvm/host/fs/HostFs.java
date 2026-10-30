package jlkvm.host.fs;

import java.io.IOException;

/**
 * The file system behind JSR-75 FileConnection (PLAN T32a). Paths are {@code root + "dir/sub/file"}, e.g. {@code c:/dir/file}, with a root
 * of {@link #roots()} spelled exactly like that and no empty, "." or ".." segment ({@link #validate} - the guest checks too, an implementation
 * must not trust it). Every implementation keeps every path inside its root: no ".." and no symlink leads out of it (IOException).
 * Called on the vm-thread only.
 */
public interface HostFs {
    /** The roots, each one name followed by '/'. */
    String[] roots();

    boolean exists(String path);

    boolean isDirectory(String path);

    /** Size of a file; -1 when the path is not a file. */
    long length(String path);

    /** 0 when the path does not exist. */
    long lastModified(String path);

    /** Names inside the directory, sorted; a directory name ends with '/'. IOException: not a directory. */
    String[] list(String dir) throws IOException;

    /** IOException: exists already, parent missing, quota. */
    void create(String path) throws IOException;

    void mkdir(String path) throws IOException;

    /** IOException: does not exist, directory not empty. */
    void delete(String path) throws IOException;

    /** {@code newName} is a bare name. IOException: does not exist, target exists. */
    void rename(String path, String newName) throws IOException;

    void truncate(String path, long size) throws IOException;

    long total(String root);

    long used(String root);

    /** Attribute bits: 1 not readable, 2 not writable, 4 hidden. */
    int attrs(String path);

    void setAttr(String path, int bit, boolean on);

    /** An open file for streaming; IOException: does not exist, is a directory. */
    Handle open(String path, boolean write) throws IOException;

    interface Handle {
        /** @return bytes read, -1 at the end of the file. */
        int read(long pos, byte[] b, int off, int len) throws IOException;

        /** Writes at pos (overwriting, extending the file; a gap is filled with zeros). IOException: quota exceeded. */
        void write(long pos, byte[] b, int off, int len) throws IOException;

        void close();
    }

    /** The common checks of a path: a known root, then segments that are not empty, ".", "..", and hold no backslash, NUL or ':'. @return the root. */
    static String validate(String[] roots, String path) throws IOException {
        for (int i = 0; i < roots.length; i++) {
            String r = roots[i];
            if (path.startsWith(r)) {
                String rest = path.substring(r.length());
                if (rest.length() == 0) return r;
                int from = 0;
                while (true) {
                    int s = rest.indexOf('/', from);
                    String seg = s < 0 ? rest.substring(from) : rest.substring(from, s);
                    if (seg.length() == 0 || seg.equals(".") || seg.equals("..") || seg.indexOf('\\') >= 0 || seg.indexOf('\0') >= 0 || seg.indexOf(':') >= 0)
                        throw new IOException("invalid path segment '" + seg + "'");
                    if (s < 0) break;
                    from = s + 1;
                }
                return r;
            }
        }
        throw new IOException("unknown root in " + path);
    }
}

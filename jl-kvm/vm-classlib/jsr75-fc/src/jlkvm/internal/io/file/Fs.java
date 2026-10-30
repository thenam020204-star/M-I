/*
 * jl-kvm guest class library (JSR-75 FileConnection layer, API per tools/api-sigs/db/jsr75-fc.json). Origin: original code written for jl-kvm
 * (PLAN D12, T32a); behaviour from the JSR-75 FileConnection API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.io.file;

import java.util.Vector;

/**
 * ROM-private natives over the host file system (host: FileNatives / HostFs). Paths are "root/dir/file" with the canonical root name
 * ("c:/"), no empty, "." or ".." segment: the host validates them again and keeps every path inside its root (symlinks included).
 * The profile permission (file.permission) is checked by the host on every call: SecurityException.
 */
public final class Fs {
    private Fs() {
    }

    /** 0 deny, 1 read, 2 readwrite. */
    public static native int perm0();

    /** The roots of the profile, separated by NUL. */
    public static native String roots0();

    public static native boolean exists0(String path);

    public static native boolean isDir0(String path);

    /** Size of a file; -1 when it is not a file. */
    public static native long length0(String path);

    public static native long mtime0(String path);

    /** Names in the directory, separated by NUL, sorted; a directory name ends with '/'. IOException when path is not a directory. */
    public static native String list0(String dir);

    public static native void create0(String path);

    public static native void mkdir0(String path);

    public static native void delete0(String path);

    public static native void rename0(String path, String newName);

    public static native void truncate0(String path, long size);

    public static native long total0(String root);

    public static native long used0(String root);

    /** Attribute bits: 1 not readable, 2 not writable, 4 hidden. */
    public static native int attr0(String path);

    public static native void setAttr0(String path, int bit, boolean on);

    /** Opens a host file for streaming, handle > 0; IOException when it does not exist, is a directory or too many are open. */
    public static native int open0(String path, boolean write);

    /** Reads at most len bytes at pos; -1 at the end. */
    public static native int read0(int handle, long pos, byte[] b, int off, int len);

    public static native void write0(int handle, long pos, byte[] b, int off, int len);

    public static native void close0(int handle);

    public static Vector rootList() {
        String s = roots0();
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
}

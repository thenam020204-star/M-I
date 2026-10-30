/*
 * jl-kvm guest class library (JSR-75 FileConnection layer, API per tools/api-sigs/db/jsr75-fc.json). Origin: original code written for jl-kvm
 * (PLAN D12, T32a); behaviour from the JSR-75 FileConnection API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.io.file;

import java.util.Enumeration;
import java.util.Vector;
import jlkvm.internal.io.file.Fs;

public class FileSystemRegistry {
    private static final Vector listeners = new Vector();

    private FileSystemRegistry() {
    }

    /** The roots never change at run time, so a registered listener is never called. */
    public static boolean addFileSystemListener(FileSystemListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        if (Fs.perm0() == 0) {
            throw new SecurityException("File system access denied by the device profile");
        }
        synchronized (listeners) {
            if (listeners.contains(listener)) {
                return false;
            }
            listeners.addElement(listener);
            return true;
        }
    }

    public static boolean removeFileSystemListener(FileSystemListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        synchronized (listeners) {
            return listeners.removeElement(listener);
        }
    }

    public static Enumeration listRoots() {
        return Fs.rootList().elements();
    }
}

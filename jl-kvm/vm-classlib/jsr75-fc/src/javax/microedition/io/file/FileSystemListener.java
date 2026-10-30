/*
 * jl-kvm guest class library (JSR-75 FileConnection layer, API per tools/api-sigs/db/jsr75-fc.json). Origin: original code written for jl-kvm
 * (PLAN D12, T32a); behaviour from the JSR-75 FileConnection API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.io.file;

public interface FileSystemListener {
    int ROOT_ADDED = 0;
    int ROOT_REMOVED = 1;

    void rootChanged(int state, String rootName);
}

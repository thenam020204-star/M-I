/*
 * jl-kvm guest class library (JSR-75 FileConnection layer, API per tools/api-sigs/db/jsr75-fc.json). Origin: original code written for jl-kvm
 * (PLAN D12, T32a); behaviour from the JSR-75 FileConnection API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.io.file;

import java.io.IOException;
import javax.microedition.io.Connection;
import jlkvm.internal.io.ConnectionBase;

/** Handler of the scheme "file": {@code file://[localhost]/root/dir/file}. */
public class Protocol implements ConnectionBase {
    public Connection openPrim(String name, int mode, boolean timeouts) throws IOException {
        return FileConn.open(name, mode);
    }
}

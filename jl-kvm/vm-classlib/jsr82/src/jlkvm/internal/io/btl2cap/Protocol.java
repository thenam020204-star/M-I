/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour: the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.io.btl2cap;

import java.io.IOException;
import javax.microedition.io.Connection;
import jlkvm.internal.bt.Open;
import jlkvm.internal.io.ConnectionBase;

/** Handler of the scheme "btl2cap". */
public class Protocol implements ConnectionBase {
    public Connection openPrim(String name, int mode, boolean timeouts) throws IOException {
        return Open.open(name);
    }
}

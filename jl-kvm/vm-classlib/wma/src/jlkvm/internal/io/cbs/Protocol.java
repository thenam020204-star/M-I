/*
 * jl-kvm guest class library (WMA 1.1 layer, API per tools/api-sigs/db/s40dp2-wma-1.1.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour per the WMA javadoc of the S40 DP2.0 SDK 1.1, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.io.cbs;

import java.io.IOException;
import javax.microedition.io.Connection;
import jlkvm.internal.io.ConnectionBase;
import jlkvm.internal.wma.Conn;

/** Handler of the scheme "cbs" (Connector has applied the profile policy sms.mode already). */
public class Protocol implements ConnectionBase {
    public Connection openPrim(String name, int mode, boolean timeouts) throws IOException {
        return Conn.open(true, name);
    }
}

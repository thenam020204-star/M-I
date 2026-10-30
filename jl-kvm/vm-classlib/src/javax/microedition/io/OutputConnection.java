/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package javax.microedition.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface OutputConnection extends Connection {
    OutputStream openOutputStream() throws IOException;

    DataOutputStream openDataOutputStream() throws IOException;
}

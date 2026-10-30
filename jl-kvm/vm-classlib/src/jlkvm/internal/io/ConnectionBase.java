/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package jlkvm.internal.io;

import java.io.IOException;
import javax.microedition.io.Connection;

/**
 * Protocol handler contract of the Generic Connection Framework (CLDC 6.3.2: "the binding of protocols is done at runtime").
 * Connector.open loads {@code jlkvm.internal.io.<scheme>.Protocol} and calls openPrim. ROM-private.
 */
public interface ConnectionBase {
    Connection openPrim(String name, int mode, boolean timeouts) throws IOException;
}

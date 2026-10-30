/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package javax.microedition.io;

import java.io.IOException;

public class ConnectionNotFoundException extends IOException {
    public ConnectionNotFoundException() {
        super();
    }

    public ConnectionNotFoundException(String s) {
        super(s);
    }
}

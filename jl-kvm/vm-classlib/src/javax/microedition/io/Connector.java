/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package javax.microedition.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jlkvm.internal.io.ConnectionBase;

/**
 * Generic Connection Framework factory. CLDC defines no protocols (CLDC 6.3.3); a profile adds them as classes
 * {@code jlkvm.internal.io.<scheme>.Protocol}. The device profile's network policy (PLAN T20) decides first, per scheme:
 * <ul>
 * <li>deny: SecurityException (access to the protocol handler is prohibited);</li>
 * <li>offline: IOException (no network);</li>
 * <li>allow: the handler is used, ConnectionNotFoundException when the build has no handler for the scheme.</li>
 * </ul>
 * Schemes that do not use the network (file, comm...) skip the policy.
 */
public class Connector {
    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int READ_WRITE = 3;

    private static final int POLICY_DENY = 0, POLICY_OFFLINE = 1, POLICY_ALLOW = 2, POLICY_LOCAL = 3;

    private Connector() {
    }

    /** Profile policy for a scheme: 0 deny, 1 offline, 2 allow, 3 not a network scheme. */
    private static native int policy0(String scheme);

    public static Connection open(String name) throws IOException {
        return open(name, READ_WRITE);
    }

    public static Connection open(String name, int mode) throws IOException {
        return open(name, mode, false);
    }

    public static Connection open(String name, int mode, boolean timeouts) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("Null URL");
        }
        if (mode != READ && mode != WRITE && mode != READ_WRITE) {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
        int colon = name.indexOf(':');
        if (colon < 1) {
            throw new IllegalArgumentException("no ':' in URL");
        }
        String scheme = name.substring(0, colon);
        for (int i = 0; i < scheme.length(); i++) {
            char c = scheme.charAt(i);
            boolean ok = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (i > 0 && ((c >= '0' && c <= '9') || c == '+' || c == '-' || c == '.'));
            if (!ok) {
                throw new IllegalArgumentException("Invalid scheme: " + scheme);
            }
        }
        scheme = scheme.toLowerCase();
        int policy = policy0(scheme);
        if (policy == POLICY_DENY) {
            throw new SecurityException("Network access denied by the device profile: " + scheme);
        }
        if (policy == POLICY_OFFLINE) {
            throw new IOException("Network is offline: " + scheme);
        }
        Object handler;
        try {
            handler = Class.forName("jlkvm.internal.io." + scheme + ".Protocol").newInstance();
        } catch (ClassNotFoundException e) {
            throw new ConnectionNotFoundException("The requested protocol does not exist " + name);
        } catch (InstantiationException e) {
            throw new ConnectionNotFoundException("Cannot create protocol handler " + scheme);
        } catch (IllegalAccessException e) {
            throw new ConnectionNotFoundException("Cannot create protocol handler " + scheme);
        }
        return ((ConnectionBase) handler).openPrim(name.substring(colon + 1), mode, timeouts);
    }

    public static DataInputStream openDataInputStream(String name) throws IOException {
        return new DataInputStream(openInputStream(name));
    }

    public static DataOutputStream openDataOutputStream(String name) throws IOException {
        return new DataOutputStream(openOutputStream(name));
    }

    public static InputStream openInputStream(String name) throws IOException {
        Connection c = open(name, READ);
        if (!(c instanceof InputConnection)) {
            c.close();
            throw new IllegalArgumentException("Not an input connection: " + name);
        }
        try {
            return ((InputConnection) c).openInputStream();
        } finally {
            c.close();
        }
    }

    public static OutputStream openOutputStream(String name) throws IOException {
        Connection c = open(name, WRITE);
        if (!(c instanceof OutputConnection)) {
            c.close();
            throw new IllegalArgumentException("Not an output connection: " + name);
        }
        try {
            return ((OutputConnection) c).openOutputStream();
        } finally {
            c.close();
        }
    }
}

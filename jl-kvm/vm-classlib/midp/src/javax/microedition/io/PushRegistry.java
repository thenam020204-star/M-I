/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.io;

import java.io.IOException;

/**
 * MIDP 2.0 push registry. This runtime delivers no push (no inbound connections, no timed launch of a MIDlet): the arguments are checked as
 * the javadoc says, then registering a connection or an alarm fails with ConnectionNotFoundException ("push delivery is not supported");
 * nothing is ever registered, so the lists are empty.
 */
public class PushRegistry {
    private PushRegistry() {
    }

    private static void checkMidlet(String midlet) throws ClassNotFoundException {
        if (midlet == null) {
            throw new NullPointerException("midlet");
        }
        Class.forName(midlet);
    }

    public static void registerConnection(String connection, String midlet, String filter) throws ClassNotFoundException, IOException {
        if (connection == null || midlet == null || filter == null) {
            throw new NullPointerException("null argument");
        }
        checkMidlet(midlet);
        throw new ConnectionNotFoundException("push delivery is not supported: " + connection);
    }

    public static boolean unregisterConnection(String connection) {
        if (connection == null) {
            throw new NullPointerException("connection");
        }
        return false;
    }

    public static String[] listConnections(boolean available) {
        return new String[0];
    }

    public static String getMIDlet(String connection) {
        if (connection == null) {
            throw new NullPointerException("connection");
        }
        return null;
    }

    public static String getFilter(String connection) {
        if (connection == null) {
            throw new NullPointerException("connection");
        }
        return null;
    }

    public static long registerAlarm(String midlet, long time) throws ClassNotFoundException, ConnectionNotFoundException {
        checkMidlet(midlet);
        throw new ConnectionNotFoundException("timed launch is not supported");
    }
}

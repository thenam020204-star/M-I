/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

import java.io.PrintStream;

/** Standard streams go to the host console (native write); properties come from the device profile (PLAN T20). */
public final class System {
    public static final PrintStream out = new PrintStream(new ConsoleOutputStream(1));
    public static final PrintStream err = new PrintStream(new ConsoleOutputStream(2));

    private System() {
    }

    public static native void arraycopy(Object src, int srcPos, Object dest, int destPos, int length);

    /** Wall clock; the virtual clock in deterministic mode (PLAN D14). */
    public static native long currentTimeMillis();

    public static native int identityHashCode(Object x);

    /** Value of the profile property, null when the profile does not define it. */
    private static native String getProperty0(String key);

    public static String getProperty(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException();
        }
        return getProperty0(key);
    }

    public static void exit(int status) {
        Runtime.getRuntime().exit(status);
    }

    public static void gc() {
        Runtime.getRuntime().gc();
    }
}

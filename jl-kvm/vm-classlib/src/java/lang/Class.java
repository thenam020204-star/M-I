/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/** Class mirror. The VM stores the class identity in vmId; reflection beyond this surface does not exist in CLDC. */
public final class Class {
    private int vmId;

    private Class() {
    }

    public static native Class forName(String className) throws ClassNotFoundException;

    public native Object newInstance() throws InstantiationException, IllegalAccessException;

    public native boolean isInstance(Object obj);

    public native boolean isAssignableFrom(Class cls);

    public native boolean isInterface();

    public native boolean isArray();

    public native String getName();

    public String toString() {
        return (isInterface() ? "interface " : "class ") + getName();
    }

    /** Bytes of the resource with this absolute path in the app JAR (then the ROM), or null. */
    private static native byte[] resourceBytes(String absPath);

    /**
     * Resource of the application JAR: a name starting with '/' is absolute, any other name is relative to the package of this class.
     */
    public InputStream getResourceAsStream(String name) {
        String abs;
        if (name.startsWith("/")) {
            abs = name;
        } else {
            String n = getName();
            int i = n.lastIndexOf('.');
            abs = i < 0 ? "/" + name : "/" + n.substring(0, i).replace('.', '/') + "/" + name;
        }
        byte[] b = resourceBytes(abs);
        return b == null ? null : new ByteArrayInputStream(b);
    }
}

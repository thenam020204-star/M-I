package java.lang;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class Class {
    private int vmId;
    private Class() { }
    public static native Class forName(String className) throws ClassNotFoundException;
    public native Object newInstance() throws InstantiationException, IllegalAccessException;
    public native boolean isInstance(Object obj);
    public native boolean isAssignableFrom(Class cls);
    public native boolean isInterface();
    public native boolean isArray();
    public native String getName();
    public String toString() { return (isInterface() ? "interface " : "class ") + getName(); }
    private static native byte[] resourceBytes(String absPath);
    public InputStream getResourceAsStream(String name) {
        String abs;
        if (name.startsWith("/")) abs = name;
        else {
            String n = getName();
            int i = n.lastIndexOf('.');
            abs = i < 0 ? "/" + name : "/" + n.substring(0, i).replace('.', '/') + "/" + name;
        }
        byte[] b = resourceBytes(abs);
        return b == null ? null : new ByteArrayInputStream(b);
    }
}

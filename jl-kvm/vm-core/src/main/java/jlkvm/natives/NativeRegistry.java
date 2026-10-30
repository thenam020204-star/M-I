package jlkvm.natives;

import java.util.HashMap;
import java.util.Map;

/** Native methods by key {@code "javax/microedition/lcdui/Graphics.drawLine(IIII)V"} (PLAN "Quy ước khoá native"). */
public final class NativeRegistry {
    private final Map<String, NativeMethod> map = new HashMap<String, NativeMethod>();
    private GuestServices services;

    /** Registers a native under {@code "pkg/Class.name(descriptor)"}; a key can be registered once (a clash is a bug of a host). */
    public void register(String key, NativeMethod m) {
        if (key == null || m == null || key.indexOf('.') < 0 || key.indexOf('(') < key.indexOf('.')) throw new IllegalArgumentException("bad native key: " + key);
        if (map.put(key, m) != null) throw new IllegalStateException("native registered twice: " + key);
    }
    /** All registered keys (tests, debug dumps). */
    public java.util.Set<String> keys() { return map.keySet(); }
    public NativeMethod lookup(String key) { return map.get(key); }
    public boolean has(String key) { return map.containsKey(key); }
    public int size() { return map.size(); }
    public GuestServices services() { return services; }
    public void setServices(GuestServices s) { services = s; }
}

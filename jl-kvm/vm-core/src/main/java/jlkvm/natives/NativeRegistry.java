package jlkvm.natives;

import java.util.HashMap;
import java.util.Map;

/** Native methods by key {@code "javax/microedition/lcdui/Graphics.drawLine(IIII)V"} (PLAN "Quy ước khoá native"). */
public final class NativeRegistry {
    private final Map<String, NativeMethod> map = new HashMap<String, NativeMethod>();
    private GuestServices services;

    public void register(String key, NativeMethod m) { map.put(key, m); }
    public NativeMethod lookup(String key) { return map.get(key); }
    public boolean has(String key) { return map.containsKey(key); }
    public int size() { return map.size(); }
    public GuestServices services() { return services; }
    public void setServices(GuestServices s) { services = s; }
}

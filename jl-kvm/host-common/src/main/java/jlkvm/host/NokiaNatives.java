package jlkvm.host;

import jlkvm.DeviceProfile;
import jlkvm.host.nokia.NokiaDevice;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/** Natives of {@code jlkvm.internal.nokia.Host} (PLAN T28): profile data of the Nokia UI layer and the {@link NokiaDevice}. */
public final class NokiaNatives {
    private static final String HOST = "jlkvm/internal/nokia/Host.";
    private final DeviceProfile profile;
    private final NokiaDevice device;

    public NokiaNatives(DeviceProfile profile, NokiaDevice device) {
        this.profile = profile;
        this.device = device;
    }

    public void register(NativeRegistry r) {
        r.register(HOST + "config0(I)I", new NativeMethod() { public void invoke(NativeContext c) {
            int v;
            switch (c.argInt(0)) {
                case 0: v = profile.nokiaPixelFormat; break;
                case 1: v = 0; for (Integer f : profile.nokiaSoundFormats) v |= 1 << f.intValue(); break;
                case 2: v = profile.nokiaConcurrentTone; break;
                default: v = profile.nokiaConcurrentWav;
            }
            c.retInt(v); } });
        // op0(op, a, b): 0 hasVibra -> 1/0, 1 startVibra(freq a, ms b), 2 stopVibra, 3 setLights(num a, level b), 4 flashLights(ms b)
        r.register(HOST + "op0(IIJ)I", new NativeMethod() { public void invoke(NativeContext c) {
            int a = c.argInt(1);
            long b = c.argLong(2);
            int op = c.argInt(0);
            int res = 0;
            switch (op) {
                case 0: res = device.hasVibra() ? 1 : 0; break;
                case 1: device.startVibra(a, b); break;
                case 2: device.stopVibra(); break;
                case 3: device.setLights(a, (int) b); break;
                default: device.flashLights(b);
            }
            c.retInt(res); } });
    }
}

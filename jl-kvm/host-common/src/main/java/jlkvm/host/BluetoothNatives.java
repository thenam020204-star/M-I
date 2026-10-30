package jlkvm.host;

import jlkvm.DeviceProfile;
import jlkvm.natives.GuestServices;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/**
 * Natives of {@code jlkvm.internal.bt.Hw} (PLAN T32, JSR-82): what the "Bluetooth hardware" of this device is. Only the profile decides today
 * ({@code bluetooth{mode,address,name}}: none | simulate = one lone adapter without peers). This is the plug point for the Android host (M6): a mode "host" that
 * talks to the real adapter would answer here (and get natives for inquiry / service search / connections); NOT done. The natives touch no radio.
 */
public final class BluetoothNatives {
    private final DeviceProfile profile;

    public BluetoothNatives(DeviceProfile profile) { this.profile = profile; }

    public void register(NativeRegistry r) {
        final GuestServices gs = r.services();
        r.register("jlkvm/internal/bt/Hw.mode0()I", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(profile.bluetoothMode.equals("simulate") ? 1 : 0); } });
        r.register("jlkvm/internal/bt/Hw.address0()Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(gs.newString(profile.bluetoothAddress)); } });
        r.register("jlkvm/internal/bt/Hw.name0()Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(gs.newString(profile.bluetoothName)); } });
    }
}

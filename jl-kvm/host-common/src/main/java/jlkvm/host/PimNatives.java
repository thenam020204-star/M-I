package jlkvm.host;

import jlkvm.DeviceProfile;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/** Natives of {@code jlkvm.internal.pim.Pim} (PLAN T32a): the profile permission of the PIM ({@code pim.permission}), asked at every list open. */
public final class PimNatives {
    private final DeviceProfile profile;

    public PimNatives(DeviceProfile profile) { this.profile = profile; }

    public void register(NativeRegistry r) {
        r.register("jlkvm/internal/pim/Pim.perm0()I", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(profile.pimPermission.equals("allow") ? 1 : 0); } });
    }
}

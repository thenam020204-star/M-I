package jlkvm;

import jlkvm.natives.NativeRegistry;

/** What the VM needs from the platform (host-desktop / host-android). See PLAN "Interface giữa module". */
public interface HostPlatform {
    long hostNanoTime();
    void log(int level, String tag, String msg);
    /** host-android / host-desktop register their native methods here. */
    void registerNatives(NativeRegistry r);
    /** The guest library ROM. */
    ClassSource rom();
}

package jlkvm.host.rms;

import jlkvm.DeviceProfile;

/** Record stores that live only as long as the object (tests, the vmspec runner). */
public final class MemoryRecordStorage extends RecordStorage {
    public MemoryRecordStorage(DeviceProfile p) { super(p); }
}

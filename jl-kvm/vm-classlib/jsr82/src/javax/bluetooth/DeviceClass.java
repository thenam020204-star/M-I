/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.bluetooth;

public class DeviceClass {
    private final int record;

    public DeviceClass(int record) {
        if ((record & 0xFF000000) != 0) {
            throw new IllegalArgumentException("record has bits 24..31 set");
        }
        this.record = record;
    }

    /** Bluetooth Assigned Numbers "class of device": service classes are bits 13..23 (bit mask 0xFFE000), kept in place. */
    public int getServiceClasses() {
        return record & 0xFFE000;
    }

    /** Major device class: bits 8..12 (mask 0x1F00), kept in place. */
    public int getMajorDeviceClass() {
        return record & 0x1F00;
    }

    /** Minor device class: bits 2..7 (mask 0xFC), kept in place. */
    public int getMinorDeviceClass() {
        return record & 0xFC;
    }
}

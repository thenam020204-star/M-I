/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.util;

/** Package-private zone with a constant offset and no daylight saving time. */
class FixedTimeZone extends TimeZone {
    private final int rawOffset;

    FixedTimeZone(int rawOffset) {
        this.rawOffset = rawOffset;
    }

    public int getOffset(int era, int year, int month, int day, int dayOfWeek, int millis) {
        return rawOffset;
    }

    public int getRawOffset() {
        return rawOffset;
    }

    public boolean useDaylightTime() {
        return false;
    }
}

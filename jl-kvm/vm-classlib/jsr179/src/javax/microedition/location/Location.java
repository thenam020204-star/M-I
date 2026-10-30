/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.location;

/**
 * Without a position source the only location there is is the invalid one: isValid() false, no coordinates, speed and course NaN. A device with a
 * receiver (Android host, M6, NOT done) would hand out subclasses that override these methods.
 */
public class Location {
    public static final int MTE_SATELLITE = 1;
    public static final int MTE_TIMEDIFFERENCE = 2;
    public static final int MTE_TIMEOFARRIVAL = 4;
    public static final int MTE_CELLID = 8;
    public static final int MTE_SHORTRANGE = 16;
    public static final int MTE_ANGLEOFARRIVAL = 32;
    public static final int MTY_TERMINALBASED = 65536;
    public static final int MTY_NETWORKBASED = 131072;
    public static final int MTA_ASSISTED = 262144;
    public static final int MTA_UNASSISTED = 524288;

    protected Location() {
    }

    public boolean isValid() {
        return false;
    }

    public long getTimestamp() {
        return 0L;
    }

    public QualifiedCoordinates getQualifiedCoordinates() {
        return null;
    }

    public float getSpeed() {
        return Float.NaN;
    }

    public float getCourse() {
        return Float.NaN;
    }

    public int getLocationMethod() {
        return 0;
    }

    public AddressInfo getAddressInfo() {
        return null;
    }

    public String getExtraInfo(String mimetype) {
        return null;
    }
}

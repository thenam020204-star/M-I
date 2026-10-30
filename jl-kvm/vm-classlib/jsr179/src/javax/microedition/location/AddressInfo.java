/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.location;

public class AddressInfo {
    public static final int EXTENSION = 1;
    public static final int STREET = 2;
    public static final int POSTAL_CODE = 3;
    public static final int CITY = 4;
    public static final int COUNTY = 5;
    public static final int STATE = 6;
    public static final int COUNTRY = 7;
    public static final int COUNTRY_CODE = 8;
    public static final int DISTRICT = 9;
    public static final int BUILDING_NAME = 10;
    public static final int BUILDING_FLOOR = 11;
    public static final int BUILDING_ROOM = 12;
    public static final int BUILDING_ZONE = 13;
    public static final int CROSSING1 = 14;
    public static final int CROSSING2 = 15;
    public static final int URL = 16;
    public static final int PHONE_NUMBER = 17;

    private final String[] fields = new String[18];

    public AddressInfo() {
    }

    private static void check(int field) {
        if (field < EXTENSION || field > PHONE_NUMBER) {
            throw new IllegalArgumentException("field " + field);
        }
    }

    /** Null for a field that is not set. */
    public String getField(int field) {
        check(field);
        return fields[field];
    }

    public void setField(int field, String value) {
        check(field);
        fields[field] = value;
    }
}

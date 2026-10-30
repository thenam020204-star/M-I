/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.util;

/**
 * Time zones. CLDC requires only GMT; the default zone comes from the device profile. getTimeZone accepts "GMT", "UTC" and
 * "GMT+hh[:mm]" / "GMT-hh[:mm]" and the profile zone; any other id gives GMT (as J2SE does for unknown ids).
 */
public abstract class TimeZone {
    private String ID;
    private static TimeZone defaultZone;

    public TimeZone() {
    }

    /** Profile default zone: id (null = "GMT"). */
    private static native String defaultId0();

    /** Profile default zone: raw offset in milliseconds. */
    private static native int defaultOffset0();

    public abstract int getOffset(int era, int year, int month, int day, int dayOfWeek, int millis);

    public abstract int getRawOffset();

    public abstract boolean useDaylightTime();

    public String getID() {
        return ID;
    }

    void setID(String id) {
        ID = id;
    }

    public static synchronized TimeZone getTimeZone(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        String d = defaultId0();
        if (d != null && d.equals(id)) {
            return make(id, defaultOffset0());
        }
        if (id.equals("GMT") || id.equals("UTC")) {
            return make(id, 0);
        }
        if (id.startsWith("GMT") && id.length() > 4) {
            int sign = id.charAt(3) == '+' ? 1 : id.charAt(3) == '-' ? -1 : 0;
            if (sign != 0) {
                String r = id.substring(4);
                int colon = r.indexOf(':');
                try {
                    int h = Integer.parseInt(colon < 0 ? r : r.substring(0, colon), 10);
                    int m = colon < 0 ? 0 : Integer.parseInt(r.substring(colon + 1), 10);
                    if (h >= 0 && h <= 23 && m >= 0 && m <= 59) {
                        return make(id, sign * (h * 60 + m) * 60000);
                    }
                } catch (NumberFormatException e) {
                    // falls through to GMT
                }
            }
        }
        return make("GMT", 0);
    }

    private static TimeZone make(String id, int offset) {
        TimeZone z = new FixedTimeZone(offset);
        z.ID = id;
        return z;
    }

    public static synchronized TimeZone getDefault() {
        if (defaultZone == null) {
            String d = defaultId0();
            defaultZone = make(d == null ? "GMT" : d, d == null ? 0 : defaultOffset0());
        }
        return defaultZone;
    }

    public static String[] getAvailableIDs() {
        String d = defaultId0();
        if (d == null || d.equals("GMT")) {
            return new String[] {"GMT"};
        }
        return new String[] {"GMT", d};
    }
}

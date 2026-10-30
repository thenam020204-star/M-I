/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.bluetooth;

public class UUID {
    public static final String baseUUID = "00001000800000805F9B34FB";

    /** 32 upper-case hex digits (the 128-bit value). */
    private final String value;

    public UUID(long uuidValue) {
        if (uuidValue < 0L || uuidValue > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("uuidValue not in [0, 2^32-1]");
        }
        value = pad(Long.toString(uuidValue, 16), 8).toUpperCase() + baseUUID;
    }

    public UUID(String uuidValue, boolean shortUUID) {
        if (uuidValue == null) {
            throw new NullPointerException("uuidValue");
        }
        int n = uuidValue.length();
        if (n == 0 || n > (shortUUID ? 8 : 32)) {
            throw new IllegalArgumentException("uuidValue length " + n);
        }
        for (int i = 0; i < n; i++) {
            if (Character.digit(uuidValue.charAt(i), 16) < 0) {
                throw new NumberFormatException("uuidValue: not a hexadecimal digit at " + i);
            }
        }
        value = shortUUID ? pad(uuidValue, 8).toUpperCase() + baseUUID : pad(uuidValue, 32).toUpperCase();
    }

    private static String pad(String s, int width) {
        StringBuffer sb = new StringBuffer(width);
        for (int i = s.length(); i < width; i++) {
            sb.append('0');
        }
        return sb.append(s).toString();
    }

    public String toString() {
        return value;
    }

    public boolean equals(Object o) {
        return o instanceof UUID && ((UUID) o).value.equals(value);
    }

    public int hashCode() {
        return value.hashCode();
    }
}

/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

/** Text conversion (toString/parseDouble) and bit conversion are VM natives: deterministic on every host (PLAN T19). */
public final class Double {
    public static final double POSITIVE_INFINITY = 1.0 / 0.0;
    public static final double NEGATIVE_INFINITY = -1.0 / 0.0;
    public static final double NaN = 0.0d / 0.0;
    public static final double MAX_VALUE = 1.79769313486231570e+308;
    public static final double MIN_VALUE = 4.94065645841246544e-324;

    private final double value;

    public Double(double value) {
        this.value = value;
    }

    /** Shortest decimal text that identifies the double (Java format: 1.0E10, 1.0E-5, 123.456). */
    public static native String toString(double d);

    private static native double parseDouble0(String s);

    public static Double valueOf(String s) throws NumberFormatException {
        return new Double(parseDouble(s));
    }

    public static double parseDouble(String s) throws NumberFormatException {
        if (s == null) {
            throw new NullPointerException();
        }
        return parseDouble0(s);
    }

    public static boolean isNaN(double v) {
        return v != v;
    }

    public static boolean isInfinite(double v) {
        return v == POSITIVE_INFINITY || v == NEGATIVE_INFINITY;
    }

    public boolean isNaN() {
        return isNaN(value);
    }

    public boolean isInfinite() {
        return isInfinite(value);
    }

    public String toString() {
        return toString(value);
    }

    public byte byteValue() {
        return (byte) value;
    }

    public short shortValue() {
        return (short) value;
    }

    public int intValue() {
        return (int) value;
    }

    public long longValue() {
        return (long) value;
    }

    public float floatValue() {
        return (float) value;
    }

    public double doubleValue() {
        return value;
    }

    public int hashCode() {
        long bits = doubleToLongBits(value);
        return (int) (bits ^ (bits >>> 32));
    }

    public boolean equals(Object obj) {
        return obj instanceof Double && doubleToLongBits(((Double) obj).value) == doubleToLongBits(value);
    }

    /** Bit pattern; every NaN gives 0x7ff8000000000000L. */
    public static native long doubleToLongBits(double value);

    public static native double longBitsToDouble(long bits);
}

package java.lang;

public final class Double {
    public static final double POSITIVE_INFINITY = 1.0 / 0.0;
    public static final double NEGATIVE_INFINITY = -1.0 / 0.0;
    public static final double NaN = 0.0d / 0.0;
    public static final double MAX_VALUE = 1.7976931348623157e+308;
    public static final double MIN_VALUE = 4.9e-324;
    private final double value;
    public Double(double v) { value = v; }
    public double doubleValue() { return value; }
    public boolean isNaN() { return isNaN(value); }
    public static boolean isNaN(double d) { return d != d; }
    public static boolean isInfinite(double d) { return d == POSITIVE_INFINITY || d == NEGATIVE_INFINITY; }
    public static native long doubleToLongBits(double d);
    public static native double longBitsToDouble(long l);
    /** TEMPORARY: native in the test host. */
    public static native String toString(double d);
    public String toString() { return toString(value); }
    public boolean equals(Object o) { return o instanceof Double && doubleToLongBits(((Double) o).value) == doubleToLongBits(value); }
    public int hashCode() { long b = doubleToLongBits(value); return (int) (b ^ (b >>> 32)); }
}

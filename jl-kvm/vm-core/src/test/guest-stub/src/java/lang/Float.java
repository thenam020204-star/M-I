package java.lang;

public final class Float {
    public static final float POSITIVE_INFINITY = 1.0f / 0.0f;
    public static final float NEGATIVE_INFINITY = -1.0f / 0.0f;
    public static final float NaN = 0.0f / 0.0f;
    public static final float MAX_VALUE = 3.4028235e+38f;
    public static final float MIN_VALUE = 1.4e-45f;
    private final float value;
    public Float(float v) { value = v; }
    public float floatValue() { return value; }
    public boolean isNaN() { return isNaN(value); }
    public static boolean isNaN(float f) { return f != f; }
    public static boolean isInfinite(float f) { return f == POSITIVE_INFINITY || f == NEGATIVE_INFINITY; }
    public static native int floatToIntBits(float f);
    public static native float intBitsToFloat(int i);
    /** TEMPORARY: native in the test host (T19 writes the real algorithm). */
    public static native String toString(float f);
    public String toString() { return toString(value); }
    public boolean equals(Object o) { return o instanceof Float && floatToIntBits(((Float) o).value) == floatToIntBits(value); }
    public int hashCode() { return floatToIntBits(value); }
}

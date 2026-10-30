/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

/** Text conversion (toString/parseFloat) and bit conversion are VM natives: deterministic on every host (PLAN T19). */
public final class Float {
    public static final float POSITIVE_INFINITY = 1.0f / 0.0f;
    public static final float NEGATIVE_INFINITY = -1.0f / 0.0f;
    public static final float NaN = 0.0f / 0.0f;
    public static final float MAX_VALUE = 3.40282346638528860e+38f;
    public static final float MIN_VALUE = 1.40129846432481707e-45f;

    private final float value;

    public Float(float value) {
        this.value = value;
    }

    public Float(double value) {
        this.value = (float) value;
    }

    /** Shortest decimal text that identifies the float (Java format: 1.0E10, 1.0E-5, 123.456). */
    public static native String toString(float f);

    private static native float parseFloat0(String s);

    public static Float valueOf(String s) throws NumberFormatException {
        return new Float(parseFloat(s));
    }

    public static float parseFloat(String s) throws NumberFormatException {
        if (s == null) {
            throw new NullPointerException();
        }
        return parseFloat0(s);
    }

    public static boolean isNaN(float v) {
        return v != v;
    }

    public static boolean isInfinite(float v) {
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
        return value;
    }

    public double doubleValue() {
        return (double) value;
    }

    public int hashCode() {
        return floatToIntBits(value);
    }

    public boolean equals(Object obj) {
        return obj instanceof Float && floatToIntBits(((Float) obj).value) == floatToIntBits(value);
    }

    /** Bit pattern; every NaN gives 0x7fc00000. */
    public static native int floatToIntBits(float value);

    public static native float intBitsToFloat(int bits);
}

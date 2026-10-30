/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

public final class Short {
    public static final short MIN_VALUE = -32768;
    public static final short MAX_VALUE = 32767;

    private final short value;

    public Short(short value) {
        this.value = value;
    }

    public static short parseShort(String s) throws NumberFormatException {
        return parseShort(s, 10);
    }

    public static short parseShort(String s, int radix) throws NumberFormatException {
        int i = Integer.parseInt(s, radix);
        if (i < MIN_VALUE || i > MAX_VALUE) {
            throw new NumberFormatException("Value out of range. Value:\"" + s + "\" Radix:" + radix);
        }
        return (short) i;
    }

    public short shortValue() {
        return value;
    }

    public String toString() {
        return Integer.toString((int) value, 10);
    }

    public int hashCode() {
        return (int) value;
    }

    public boolean equals(Object obj) {
        return obj instanceof Short && ((Short) obj).value == value;
    }
}

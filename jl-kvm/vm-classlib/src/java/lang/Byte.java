/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

public final class Byte {
    public static final byte MIN_VALUE = -128;
    public static final byte MAX_VALUE = 127;

    private final byte value;

    public Byte(byte value) {
        this.value = value;
    }

    public static byte parseByte(String s) throws NumberFormatException {
        return parseByte(s, 10);
    }

    public static byte parseByte(String s, int radix) throws NumberFormatException {
        int i = Integer.parseInt(s, radix);
        if (i < MIN_VALUE || i > MAX_VALUE) {
            throw new NumberFormatException("Value out of range. Value:\"" + s + "\" Radix:" + radix);
        }
        return (byte) i;
    }

    public byte byteValue() {
        return value;
    }

    public String toString() {
        return Integer.toString((int) value, 10);
    }

    public int hashCode() {
        return (int) value;
    }

    public boolean equals(Object obj) {
        return obj instanceof Byte && ((Byte) obj).value == value;
    }
}

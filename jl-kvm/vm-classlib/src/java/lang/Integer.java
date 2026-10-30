/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

public final class Integer {
    public static final int MIN_VALUE = 0x80000000;
    public static final int MAX_VALUE = 0x7fffffff;

    private final int value;

    public Integer(int value) {
        this.value = value;
    }

    public static String toString(int i, int radix) {
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
            radix = 10;
        }
        if (i == 0) {
            return "0";
        }
        if (radix == 10 && i == MIN_VALUE) {
            return "-2147483648";
        }
        char[] buf = new char[33];
        int pos = 33;
        boolean neg = i < 0;
        if (!neg) {
            i = -i;
        }
        // work with negative numbers: -MIN_VALUE does not fit
        while (i != 0) {
            int d = -(i % radix);
            buf[--pos] = digitChar(d);
            i /= radix;
        }
        if (neg) {
            buf[--pos] = '-';
        }
        return new String(buf, pos, 33 - pos);
    }

    static char digitChar(int d) {
        return (char) (d < 10 ? '0' + d : 'a' + d - 10);
    }

    public static String toHexString(int i) {
        return toUnsigned(i, 4);
    }

    public static String toOctalString(int i) {
        return toUnsigned(i, 3);
    }

    public static String toBinaryString(int i) {
        return toUnsigned(i, 1);
    }

    private static String toUnsigned(int i, int shift) {
        char[] buf = new char[32];
        int pos = 32;
        int mask = (1 << shift) - 1;
        do {
            buf[--pos] = digitChar(i & mask);
            i >>>= shift;
        } while (i != 0);
        return new String(buf, pos, 32 - pos);
    }

    public static String toString(int i) {
        return toString(i, 10);
    }

    public static int parseInt(String s, int radix) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix " + radix + " less than Character.MIN_RADIX");
        }
        if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix " + radix + " greater than Character.MAX_RADIX");
        }
        int len = s.length();
        if (len == 0) {
            throw new NumberFormatException("For input string: \"" + s + "\"");
        }
        int i = 0;
        boolean neg = false;
        int limit = -MAX_VALUE;
        char first = s.charAt(0);
        if (first < '0') {
            if (first == '-') {
                neg = true;
                limit = MIN_VALUE;
            } else {
                // CLDC 1.1 javadoc: only an ASCII minus sign may precede the digits ('+7' is a NumberFormatException; T05c)
                throw new NumberFormatException("For input string: \"" + s + "\"");
            }
            if (len == 1) {
                throw new NumberFormatException("For input string: \"" + s + "\"");
            }
            i++;
        }
        int multmin = limit / radix;
        int result = 0;
        while (i < len) {
            int digit = Character.digit(s.charAt(i++), radix);
            if (digit < 0 || result < multmin) {
                throw new NumberFormatException("For input string: \"" + s + "\"");
            }
            result *= radix;
            if (result < limit + digit) {
                throw new NumberFormatException("For input string: \"" + s + "\"");
            }
            result -= digit;
        }
        return neg ? result : -result;
    }

    public static int parseInt(String s) throws NumberFormatException {
        return parseInt(s, 10);
    }

    public static Integer valueOf(String s, int radix) throws NumberFormatException {
        return new Integer(parseInt(s, radix));
    }

    public static Integer valueOf(String s) throws NumberFormatException {
        return new Integer(parseInt(s, 10));
    }

    public byte byteValue() {
        return (byte) value;
    }

    public short shortValue() {
        return (short) value;
    }

    public int intValue() {
        return value;
    }

    public long longValue() {
        return (long) value;
    }

    public float floatValue() {
        return (float) value;
    }

    public double doubleValue() {
        return (double) value;
    }

    public String toString() {
        return toString(value, 10);
    }

    public int hashCode() {
        return value;
    }

    public boolean equals(Object obj) {
        return obj instanceof Integer && ((Integer) obj).value == value;
    }
}

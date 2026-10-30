/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

public final class Long {
    public static final long MIN_VALUE = 0x8000000000000000L;
    public static final long MAX_VALUE = 0x7fffffffffffffffL;

    private final long value;

    public Long(long value) {
        this.value = value;
    }

    public static String toString(long i, int radix) {
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
            radix = 10;
        }
        if (i == 0) {
            return "0";
        }
        char[] buf = new char[65];
        int pos = 65;
        boolean neg = i < 0;
        if (!neg) {
            i = -i;
        }
        while (i != 0) {
            int d = (int) -(i % radix);
            buf[--pos] = Integer.digitChar(d);
            i /= radix;
        }
        if (neg) {
            buf[--pos] = '-';
        }
        return new String(buf, pos, 65 - pos);
    }

    public static String toString(long i) {
        return toString(i, 10);
    }

    public static long parseLong(String s, int radix) throws NumberFormatException {
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
        long limit = -MAX_VALUE;
        char first = s.charAt(0);
        if (first < '0') {
            if (first == '-') {
                neg = true;
                limit = MIN_VALUE;
            } else if (first != '+') {
                throw new NumberFormatException("For input string: \"" + s + "\"");
            }
            if (len == 1) {
                throw new NumberFormatException("For input string: \"" + s + "\"");
            }
            i++;
        }
        long multmin = limit / radix;
        long result = 0;
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

    public static long parseLong(String s) throws NumberFormatException {
        return parseLong(s, 10);
    }

    public long longValue() {
        return value;
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
        return (int) (value ^ (value >>> 32));
    }

    public boolean equals(Object obj) {
        return obj instanceof Long && ((Long) obj).value == value;
    }
}

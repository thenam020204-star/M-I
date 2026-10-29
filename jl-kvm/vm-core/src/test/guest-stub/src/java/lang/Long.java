package java.lang;

public final class Long {
    public static final long MIN_VALUE = 0x8000000000000000L;
    public static final long MAX_VALUE = 0x7fffffffffffffffL;
    private final long value;
    public Long(long v) { value = v; }
    public long longValue() { return value; }
    public int intValue() { return (int) value; }
    public boolean equals(Object o) { return o instanceof Long && ((Long) o).value == value; }
    public int hashCode() { return (int) (value ^ (value >>> 32)); }
    public String toString() { return toString(value, 10); }
    public static String toString(long i) { return toString(i, 10); }
    public static String toString(long i, int radix) {
        if (radix < 2 || radix > 36) radix = 10;
        if (i == 0) return "0";
        char[] buf = new char[65];
        int pos = 65;
        boolean neg = i < 0;
        if (!neg) i = -i;
        while (i != 0) {
            int d = (int) -(i % radix);
            buf[--pos] = (char) (d < 10 ? '0' + d : 'a' + d - 10);
            i /= radix;
        }
        if (neg) buf[--pos] = '-';
        return new String(buf, pos, 65 - pos);
    }
    public static long parseLong(String s) throws NumberFormatException {
        if (s == null) throw new NumberFormatException("null");
        int n = s.length(), i = 0;
        if (n == 0) throw new NumberFormatException(s);
        boolean neg = false;
        if (s.charAt(0) == '-') { neg = true; i = 1; } else if (s.charAt(0) == '+') i = 1;
        if (i == n) throw new NumberFormatException(s);
        long limit = neg ? MIN_VALUE : -MAX_VALUE, result = 0, multmin = limit / 10;
        while (i < n) {
            int d = Character.digit(s.charAt(i++), 10);
            if (d < 0 || result < multmin) throw new NumberFormatException(s);
            result *= 10;
            if (result < limit + d) throw new NumberFormatException(s);
            result -= d;
        }
        return neg ? result : -result;
    }
}

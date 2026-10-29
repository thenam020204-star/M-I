package java.lang;

public final class Integer {
    public static final int MIN_VALUE = 0x80000000;
    public static final int MAX_VALUE = 0x7fffffff;
    private final int value;
    public Integer(int v) { value = v; }
    public int intValue() { return value; }
    public long longValue() { return value; }
    public boolean equals(Object o) { return o instanceof Integer && ((Integer) o).value == value; }
    public int hashCode() { return value; }
    public String toString() { return toString(value, 10); }
    public static String toString(int i) { return toString(i, 10); }
    public static String toString(int i, int radix) {
        if (radix < 2 || radix > 36) radix = 10;
        if (i == 0) return "0";
        char[] buf = new char[33];
        int pos = 33;
        boolean neg = i < 0;
        if (!neg) i = -i;
        while (i != 0) {
            int d = -(i % radix);
            buf[--pos] = (char) (d < 10 ? '0' + d : 'a' + d - 10);
            i /= radix;
        }
        if (neg) buf[--pos] = '-';
        return new String(buf, pos, 33 - pos);
    }
    public static String toHexString(int i) { return unsigned(i, 4); }
    public static String toBinaryString(int i) { return unsigned(i, 1); }
    public static String toOctalString(int i) { return unsigned(i, 3); }
    private static String unsigned(int i, int shift) {
        if (i == 0) return "0";
        char[] buf = new char[32];
        int pos = 32, mask = (1 << shift) - 1;
        while (i != 0) {
            int d = i & mask;
            buf[--pos] = (char) (d < 10 ? '0' + d : 'a' + d - 10);
            i >>>= shift;
        }
        return new String(buf, pos, 32 - pos);
    }
    public static int parseInt(String s) throws NumberFormatException { return parseInt(s, 10); }
    public static int parseInt(String s, int radix) throws NumberFormatException {
        if (s == null) throw new NumberFormatException("null");
        int n = s.length();
        if (n == 0) throw new NumberFormatException(s);
        int i = 0;
        boolean neg = false;
        if (s.charAt(0) == '-') { neg = true; i = 1; } else if (s.charAt(0) == '+') i = 1;
        if (i == n) throw new NumberFormatException(s);
        int limit = neg ? MIN_VALUE : -MAX_VALUE;
        int result = 0;
        int multmin = limit / radix;
        while (i < n) {
            int d = Character.digit(s.charAt(i++), radix);
            if (d < 0 || result < multmin) throw new NumberFormatException(s);
            result *= radix;
            if (result < limit + d) throw new NumberFormatException(s);
            result -= d;
        }
        return neg ? result : -result;
    }
    public static Integer valueOf(String s) throws NumberFormatException { return new Integer(parseInt(s)); }
}

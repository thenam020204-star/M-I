package java.lang;

import java.io.UnsupportedEncodingException;

/** TEMPORARY guest stub (PLAN T11; T19 writes the real one). */
public final class String {
    private char[] value;
    private int offset;
    private int count;

    public String() { value = new char[0]; }
    public String(String s) { value = s.value; offset = s.offset; count = s.count; }
    public String(char[] c) { this(c, 0, c.length); }
    public String(char[] c, int off, int len) {
        if (off < 0 || len < 0 || off > c.length - len) throw new StringIndexOutOfBoundsException();
        value = new char[len];
        System.arraycopy(c, off, value, 0, len);
        count = len;
    }
    public String(StringBuffer sb) { String s = sb.toString(); value = s.value; offset = s.offset; count = s.count; }
    public String(byte[] b) { this(b, 0, b.length); }
    public String(byte[] b, int off, int len) {
        String s = decode(b, off, len, System.getProperty("microedition.encoding"));
        value = s.value; offset = s.offset; count = s.count;
    }
    public String(byte[] b, String enc) throws UnsupportedEncodingException { this(b, 0, b.length, enc); }
    public String(byte[] b, int off, int len, String enc) throws UnsupportedEncodingException {
        if (enc == null) throw new NullPointerException();
        if (!supported(enc)) throw new UnsupportedEncodingException(enc);
        String s = decode(b, off, len, enc);
        value = s.value; offset = s.offset; count = s.count;
    }

    private static boolean isUtf8(String e) { return e.equals("UTF-8") || e.equals("UTF8"); }
    private static boolean isLatin1(String e) { return e.equals("ISO-8859-1") || e.equals("ISO8859_1") || e.equals("ISO8859-1"); }
    private static boolean supported(String e) { return isUtf8(e) || isLatin1(e); }

    private static String decode(byte[] b, int off, int len, String enc) {
        if (off < 0 || len < 0 || off > b.length - len) throw new IndexOutOfBoundsException();
        char[] out = new char[len];
        int n = 0;
        if (enc != null && isUtf8(enc)) {
            int i = off, end = off + len;
            while (i < end) {
                int c = b[i++] & 0xFF;
                if (c < 0x80) out[n++] = (char) c;
                else if (c >= 0xC2 && c < 0xE0 && i < end && (b[i] & 0xC0) == 0x80) out[n++] = (char) (((c & 0x1F) << 6) | (b[i++] & 0x3F));
                else if (c >= 0xE0 && c < 0xF0 && i + 1 < end && (b[i] & 0xC0) == 0x80 && (b[i + 1] & 0xC0) == 0x80) {
                    out[n++] = (char) (((c & 0x0F) << 12) | ((b[i] & 0x3F) << 6) | (b[i + 1] & 0x3F));
                    i += 2;
                } else out[n++] = '�';
            }
        } else {
            for (int i = 0; i < len; i++) out[n++] = (char) (b[off + i] & 0xFF);
        }
        return new String(out, 0, n);
    }

    public byte[] getBytes() {
        try { return getBytes(System.getProperty("microedition.encoding")); }
        catch (UnsupportedEncodingException e) { return getBytes0(false); }
    }
    public byte[] getBytes(String enc) throws UnsupportedEncodingException {
        if (enc == null) throw new NullPointerException();
        if (!supported(enc)) throw new UnsupportedEncodingException(enc);
        return getBytes0(isUtf8(enc));
    }
    private byte[] getBytes0(boolean utf8) {
        byte[] tmp = new byte[utf8 ? count * 3 : count];
        int n = 0;
        for (int i = 0; i < count; i++) {
            char c = value[offset + i];
            if (!utf8) tmp[n++] = (byte) (c < 256 ? c : '?');
            else if (c < 0x80) tmp[n++] = (byte) c;
            else if (c < 0x800) { tmp[n++] = (byte) (0xC0 | (c >> 6)); tmp[n++] = (byte) (0x80 | (c & 0x3F)); }
            else { tmp[n++] = (byte) (0xE0 | (c >> 12)); tmp[n++] = (byte) (0x80 | ((c >> 6) & 0x3F)); tmp[n++] = (byte) (0x80 | (c & 0x3F)); }
        }
        byte[] r = new byte[n];
        System.arraycopy(tmp, 0, r, 0, n);
        return r;
    }

    public int length() { return count; }
    public char charAt(int i) {
        if (i < 0 || i >= count) throw new StringIndexOutOfBoundsException("index " + i + ", length " + count);
        return value[offset + i];
    }
    public void getChars(int sb, int se, char[] dst, int db) {
        if (sb < 0 || se > count || sb > se) throw new StringIndexOutOfBoundsException();
        System.arraycopy(value, offset + sb, dst, db, se - sb);
    }
    public char[] toCharArray() { char[] r = new char[count]; System.arraycopy(value, offset, r, 0, count); return r; }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof String)) return false;
        String s = (String) o;
        if (s.count != count) return false;
        for (int i = 0; i < count; i++) if (value[offset + i] != s.value[s.offset + i]) return false;
        return true;
    }
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < count; i++) h = 31 * h + value[offset + i];
        return h;
    }
    public int compareTo(String s) {
        int n = count < s.count ? count : s.count;
        for (int i = 0; i < n; i++) {
            int d = value[offset + i] - s.value[s.offset + i];
            if (d != 0) return d;
        }
        return count - s.count;
    }
    public boolean startsWith(String p) { return startsWith(p, 0); }
    public boolean startsWith(String p, int from) {
        if (from < 0 || from > count - p.count) return false;
        for (int i = 0; i < p.count; i++) if (value[offset + from + i] != p.value[p.offset + i]) return false;
        return true;
    }
    public boolean endsWith(String p) { return startsWith(p, count - p.count); }
    public int indexOf(int ch) { return indexOf(ch, 0); }
    public int indexOf(int ch, int from) {
        if (from < 0) from = 0;
        for (int i = from; i < count; i++) if (value[offset + i] == ch) return i;
        return -1;
    }
    public int indexOf(String s) { return indexOf(s, 0); }
    public int indexOf(String s, int from) {
        if (from < 0) from = 0;
        for (int i = from; i <= count - s.count; i++) if (startsWith(s, i)) return i;
        return -1;
    }
    public int lastIndexOf(int ch) {
        for (int i = count - 1; i >= 0; i--) if (value[offset + i] == ch) return i;
        return -1;
    }
    public String substring(int b) { return substring(b, count); }
    public String substring(int b, int e) {
        if (b < 0 || e > count || b > e) throw new StringIndexOutOfBoundsException("begin " + b + ", end " + e + ", length " + count);
        if (b == 0 && e == count) return this;
        return new String(value, offset + b, e - b);
    }
    public String concat(String s) {
        if (s.count == 0) return this;
        char[] r = new char[count + s.count];
        System.arraycopy(value, offset, r, 0, count);
        System.arraycopy(s.value, s.offset, r, count, s.count);
        return new String(r, 0, r.length);
    }
    public String replace(char a, char b) {
        char[] r = toCharArray();
        for (int i = 0; i < r.length; i++) if (r[i] == a) r[i] = b;
        return new String(r, 0, r.length);
    }
    public String trim() {
        int b = 0, e = count;
        while (b < e && value[offset + b] <= ' ') b++;
        while (e > b && value[offset + e - 1] <= ' ') e--;
        return substring(b, e);
    }
    public String toLowerCase() {
        char[] r = toCharArray();
        for (int i = 0; i < r.length; i++) r[i] = Character.toLowerCase(r[i]);
        return new String(r, 0, r.length);
    }
    public String toUpperCase() {
        char[] r = toCharArray();
        for (int i = 0; i < r.length; i++) r[i] = Character.toUpperCase(r[i]);
        return new String(r, 0, r.length);
    }
    public boolean equalsIgnoreCase(String s) { return s != null && toLowerCase().equals(s.toLowerCase()); }
    public String toString() { return this; }
    public native String intern();
    public static String valueOf(Object o) { return o == null ? "null" : o.toString(); }
    public static String valueOf(char[] c) { return new String(c); }
    public static String valueOf(boolean b) { return b ? "true" : "false"; }
    public static String valueOf(char c) { return new String(new char[] { c }, 0, 1); }
    public static String valueOf(int i) { return Integer.toString(i); }
    public static String valueOf(long l) { return Long.toString(l); }
    public static String valueOf(float f) { return Float.toString(f); }
    public static String valueOf(double d) { return Double.toString(d); }
}

package java.lang;

public final class StringBuffer {
    private char[] value;
    private int count;
    public StringBuffer() { value = new char[16]; }
    public StringBuffer(int cap) { value = new char[cap]; }
    public StringBuffer(String s) { value = new char[s.length() + 16]; append(s); }
    private void ensure(int n) {
        if (n > value.length) {
            char[] nv = new char[n * 2 + 2];
            System.arraycopy(value, 0, nv, 0, count);
            value = nv;
        }
    }
    public int length() { return count; }
    public int capacity() { return value.length; }
    public char charAt(int i) { if (i < 0 || i >= count) throw new StringIndexOutOfBoundsException(); return value[i]; }
    public void setCharAt(int i, char c) { if (i < 0 || i >= count) throw new StringIndexOutOfBoundsException(); value[i] = c; }
    public void setLength(int n) { ensure(n); for (int i = count; i < n; i++) value[i] = 0; count = n; }
    public StringBuffer append(String s) {
        if (s == null) s = "null";
        int n = s.length();
        ensure(count + n);
        s.getChars(0, n, value, count);
        count += n;
        return this;
    }
    public StringBuffer append(Object o) { return append(String.valueOf(o)); }
    public StringBuffer append(char[] c) { return append(new String(c)); }
    public StringBuffer append(boolean b) { return append(b ? "true" : "false"); }
    public StringBuffer append(char c) { ensure(count + 1); value[count++] = c; return this; }
    public StringBuffer append(int i) { return append(Integer.toString(i)); }
    public StringBuffer append(long l) { return append(Long.toString(l)); }
    public StringBuffer append(float f) { return append(Float.toString(f)); }
    public StringBuffer append(double d) { return append(Double.toString(d)); }
    public StringBuffer insert(int at, String s) {
        if (at < 0 || at > count) throw new StringIndexOutOfBoundsException();
        int n = s.length();
        ensure(count + n);
        System.arraycopy(value, at, value, at + n, count - at);
        s.getChars(0, n, value, at);
        count += n;
        return this;
    }
    public StringBuffer reverse() {
        for (int i = 0, j = count - 1; i < j; i++, j--) { char t = value[i]; value[i] = value[j]; value[j] = t; }
        return this;
    }
    public String toString() { return new String(value, 0, count); }
}

/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

/** Package-private: contents, size limit and constraints shared by TextBox and TextField (MIDP 2.0 javadoc). */
final class TextModel {
    static final int MAX = 5000;   // UNVERIFIED implementation limit of maxSize (T33 profile data)
    static final int FLAGS = 0x10000 | 0x20000 | 0x40000 | 0x80000 | 0x100000 | 0x200000;

    char[] buf;
    int len, maxSize, constraints, caret;
    String inputMode;

    TextModel(String text, int maxSize, int constraints) {
        if (maxSize <= 0) throw new IllegalArgumentException("maxSize");
        checkConstraints(constraints);
        this.maxSize = maxSize > MAX ? MAX : maxSize;
        this.constraints = constraints;
        buf = new char[16];
        if (text != null) {
            if (text.length() > this.maxSize) throw new IllegalArgumentException("text longer than maxSize");
            char[] c = text.toCharArray();
            if (!valid(c, 0, c.length, constraints)) throw new IllegalArgumentException("text does not match the constraints");
            put(c, 0, c.length);
        }
    }

    static void checkConstraints(int c) {
        if ((c & ~(0xFFFF | FLAGS)) != 0 || (c & 0xFFFF) > 5) throw new IllegalArgumentException("constraints");
    }

    /** true when {@code a[off..off+n)} is a legal content for the constraint class (flags are ignored). */
    static boolean valid(char[] a, int off, int n, int constraints) {
        int k = constraints & 0xFFFF;
        if (k == TextField.NUMERIC || k == TextField.DECIMAL) {
            boolean dot = false;
            for (int i = 0; i < n; i++) {
                char ch = a[off + i];
                if (ch == '-' && i == 0) continue;
                if (ch >= '0' && ch <= '9') continue;
                if (ch == '.' && k == TextField.DECIMAL && !dot) {
                    dot = true;
                    continue;
                }
                return false;
            }
        } else if (k == TextField.PHONENUMBER) {
            for (int i = 0; i < n; i++) {
                char ch = a[off + i];
                if ((ch >= '0' && ch <= '9') || ch == '#' || ch == '*' || (ch == '+' && i == 0)) continue;
                return false;
            }
        }
        return true;
    }

    private void put(char[] c, int off, int n) {
        if (buf.length < n) buf = new char[n + 16];
        System.arraycopy(c, off, buf, 0, n);
        len = n;
        caret = n;
    }

    synchronized String getString() {
        return new String(buf, 0, len);
    }

    synchronized void setString(String s) {
        if (s == null) {
            len = 0;
            caret = 0;
            return;
        }
        char[] c = s.toCharArray();
        set(c, 0, c.length);
    }

    synchronized void set(char[] c, int off, int n) {
        if (n > maxSize) throw new IllegalArgumentException("text longer than maxSize");
        if (!valid(c, off, n, constraints)) throw new IllegalArgumentException("text does not match the constraints");
        put(c, off, n);
    }

    synchronized void setChars(char[] data, int offset, int length) {
        if (data == null) {
            len = 0;
            caret = 0;
            return;
        }
        if (offset < 0 || length < 0 || offset + length > data.length) throw new ArrayIndexOutOfBoundsException("range");
        set(data, offset, length);
    }

    synchronized void insert(char[] c, int off, int n, int pos) {
        if (pos < 0) pos = 0;
        if (pos > len) pos = len;
        if (len + n > maxSize) throw new IllegalArgumentException("contents would exceed maxSize");
        char[] r = new char[len + n];
        System.arraycopy(buf, 0, r, 0, pos);
        System.arraycopy(c, off, r, pos, n);
        System.arraycopy(buf, pos, r, pos + n, len - pos);
        if (!valid(r, 0, r.length, constraints)) throw new IllegalArgumentException("text does not match the constraints");
        buf = r;
        len = r.length;
        caret = pos + n;
    }

    synchronized void insert(String s, int pos) {
        if (s == null) throw new NullPointerException();
        char[] c = s.toCharArray();
        insert(c, 0, c.length, pos);
    }

    synchronized void insertChars(char[] data, int offset, int length, int pos) {
        if (data == null) throw new NullPointerException();
        if (offset < 0 || length < 0 || offset + length > data.length) throw new ArrayIndexOutOfBoundsException("range");
        insert(data, offset, length, pos);
    }

    synchronized void delete(int offset, int length) {
        if (offset < 0 || length < 0 || offset + length > len) throw new StringIndexOutOfBoundsException("range");
        System.arraycopy(buf, offset + length, buf, offset, len - offset - length);
        len -= length;
        caret = offset;
    }

    synchronized int getChars(char[] data) {
        if (data == null) throw new NullPointerException();
        if (data.length < len) throw new ArrayIndexOutOfBoundsException("array too short");
        System.arraycopy(buf, 0, data, 0, len);
        return len;
    }

    synchronized int setMaxSize(int m) {
        if (m <= 0) throw new IllegalArgumentException("maxSize");
        maxSize = m > MAX ? MAX : m;
        if (len > maxSize) {
            len = maxSize;
            if (caret > len) caret = len;
        }
        return maxSize;
    }

    synchronized void setConstraints(int c) {
        checkConstraints(c);
        constraints = c;
        if (!valid(buf, 0, len, c)) {
            len = 0;
            caret = 0;
        }
    }

    /** Event thread: the user edited the text on the host; a text that violates the limits or constraints is ignored. @return true when applied. */
    synchronized boolean hostSet(String s) {
        if (s == null) return false;
        char[] c = s.toCharArray();
        if (c.length > maxSize || !valid(c, 0, c.length, constraints)) return false;
        put(c, 0, c.length);
        return true;
    }
}

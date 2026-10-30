/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

import java.io.UnsupportedEncodingException;
import jlkvm.internal.Encoding;

/**
 * Immutable character sequence. The VM builds strings (literals, natives) by filling the private fields value/offset/count
 * directly, so their names are fixed (PLAN D8). Literals are interned by the VM; {@link #intern()} is a VM native.
 */
public final class String {
    private char[] value;
    private int offset;
    private int count;
    private int hash;

    public String() {
        value = new char[0];
    }

    public String(String original) {
        value = original.value;
        offset = original.offset;
        count = original.count;
        hash = original.hash;
    }

    public String(char[] chars) {
        this(chars, 0, chars.length);
    }

    public String(char[] chars, int off, int len) {
        if (off < 0 || len < 0 || off > chars.length - len) {
            throw new StringIndexOutOfBoundsException("offset " + off + ", count " + len + ", length " + chars.length);
        }
        value = new char[len];
        System.arraycopy(chars, off, value, 0, len);
        count = len;
    }

    public String(StringBuffer buffer) {
        String s = buffer.toString();
        value = s.value;
        offset = s.offset;
        count = s.count;
    }

    public String(byte[] bytes) {
        this(bytes, 0, bytes.length);
    }

    public String(byte[] bytes, int off, int len) {
        char[] c = Encoding.decode(Encoding.defaultId(), bytes, off, len);
        value = c;
        count = c.length;
    }

    public String(byte[] bytes, String enc) throws UnsupportedEncodingException {
        this(bytes, 0, bytes.length, enc);
    }

    public String(byte[] bytes, int off, int len, String enc) throws UnsupportedEncodingException {
        char[] c = Encoding.decode(Encoding.require(enc), bytes, off, len);
        value = c;
        count = c.length;
    }

    /** Shares the array (substring): package-private, the argument order differs from every public constructor. */
    String(int off, int len, char[] shared) {
        value = shared;
        offset = off;
        count = len;
    }

    public int length() {
        return count;
    }

    public char charAt(int index) {
        if (index < 0 || index >= count) {
            throw new StringIndexOutOfBoundsException("index " + index + ", length " + count);
        }
        return value[offset + index];
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > count) {
            throw new StringIndexOutOfBoundsException("srcBegin " + srcBegin + ", srcEnd " + srcEnd + ", length " + count);
        }
        if (dstBegin < 0 || dstBegin > dst.length - (srcEnd - srcBegin)) {
            throw new ArrayIndexOutOfBoundsException("dstBegin " + dstBegin + ", dst.length " + dst.length);
        }
        System.arraycopy(value, offset + srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }

    public byte[] getBytes() {
        return Encoding.encode(Encoding.defaultId(), value, offset, count);
    }

    public byte[] getBytes(String enc) throws UnsupportedEncodingException {
        return Encoding.encode(Encoding.require(enc), value, offset, count);
    }

    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String s = (String) anObject;
            int n = count;
            if (n != s.count) {
                return false;
            }
            char[] a = value, b = s.value;
            int i = offset, j = s.offset;
            while (n-- > 0) {
                if (a[i++] != b[j++]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean equalsIgnoreCase(String anotherString) {
        return anotherString != null && anotherString.count == count && regionMatches(true, 0, anotherString, 0, count);
    }

    public int compareTo(String anotherString) {
        int n = count < anotherString.count ? count : anotherString.count;
        char[] a = value, b = anotherString.value;
        int i = offset, j = anotherString.offset;
        while (n-- > 0) {
            char c1 = a[i++], c2 = b[j++];
            if (c1 != c2) {
                return c1 - c2;
            }
        }
        return count - anotherString.count;
    }

    public boolean regionMatches(boolean ignoreCase, int toffset, String other, int ooffset, int len) {
        if (ooffset < 0 || toffset < 0 || toffset > count - len || ooffset > other.count - len) {
            return false;
        }
        int i = offset + toffset, j = other.offset + ooffset;
        while (len-- > 0) {
            char c1 = value[i++], c2 = other.value[j++];
            if (c1 == c2) {
                continue;
            }
            if (ignoreCase && (Character.toUpperCase(c1) == Character.toUpperCase(c2) || Character.toLowerCase(c1) == Character.toLowerCase(c2))) {
                continue;
            }
            return false;
        }
        return true;
    }

    public boolean startsWith(String prefix, int toffset) {
        int pc = prefix.count;
        if (toffset < 0 || toffset > count - pc) {
            return false;
        }
        int i = offset + toffset, j = prefix.offset;
        while (pc-- > 0) {
            if (value[i++] != prefix.value[j++]) {
                return false;
            }
        }
        return true;
    }

    public boolean startsWith(String prefix) {
        return startsWith(prefix, 0);
    }

    public boolean endsWith(String suffix) {
        return startsWith(suffix, count - suffix.count);
    }

    public int hashCode() {
        int h = hash;
        if (h == 0 && count > 0) {
            int off = offset;
            char[] v = value;
            for (int i = 0; i < count; i++) {
                h = 31 * h + v[off++];
            }
            hash = h;
        }
        return h;
    }

    public int indexOf(int ch) {
        return indexOf(ch, 0);
    }

    public int indexOf(int ch, int fromIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        for (int i = fromIndex; i < count; i++) {
            if (value[offset + i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(int ch) {
        return lastIndexOf(ch, count - 1);
    }

    public int lastIndexOf(int ch, int fromIndex) {
        int i = fromIndex >= count ? count - 1 : fromIndex;
        for (; i >= 0; i--) {
            if (value[offset + i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    public int indexOf(String str, int fromIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        int sc = str.count;
        if (fromIndex >= count) {
            return sc == 0 && count == 0 ? 0 : (sc == 0 ? count : -1);
        }
        if (sc == 0) {
            return fromIndex;
        }
        char first = str.value[str.offset];
        int max = count - sc;
        for (int i = fromIndex; i <= max; i++) {
            if (value[offset + i] != first) {
                continue;
            }
            int j = 1;
            while (j < sc && value[offset + i + j] == str.value[str.offset + j]) {
                j++;
            }
            if (j == sc) {
                return i;
            }
        }
        return -1;
    }

    public String substring(int beginIndex) {
        return substring(beginIndex, count);
    }

    public String substring(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > count || beginIndex > endIndex) {
            throw new StringIndexOutOfBoundsException("begin " + beginIndex + ", end " + endIndex + ", length " + count);
        }
        if (beginIndex == 0 && endIndex == count) {
            return this;
        }
        return new String(offset + beginIndex, endIndex - beginIndex, value);
    }

    public String concat(String str) {
        int sc = str.count;
        if (sc == 0) {
            return this;
        }
        char[] r = new char[count + sc];
        System.arraycopy(value, offset, r, 0, count);
        System.arraycopy(str.value, str.offset, r, count, sc);
        return new String(0, r.length, r);
    }

    public String replace(char oldChar, char newChar) {
        if (oldChar != newChar) {
            for (int i = 0; i < count; i++) {
                if (value[offset + i] == oldChar) {
                    char[] r = new char[count];
                    System.arraycopy(value, offset, r, 0, count);
                    for (int k = i; k < count; k++) {
                        if (r[k] == oldChar) {
                            r[k] = newChar;
                        }
                    }
                    return new String(0, count, r);
                }
            }
        }
        return this;
    }

    public String toLowerCase() {
        for (int i = 0; i < count; i++) {
            char c = value[offset + i];
            if (Character.toLowerCase(c) != c) {
                char[] r = new char[count];
                for (int k = 0; k < count; k++) {
                    r[k] = Character.toLowerCase(value[offset + k]);
                }
                return new String(0, count, r);
            }
        }
        return this;
    }

    public String toUpperCase() {
        for (int i = 0; i < count; i++) {
            char c = value[offset + i];
            if (Character.toUpperCase(c) != c) {
                char[] r = new char[count];
                for (int k = 0; k < count; k++) {
                    r[k] = Character.toUpperCase(value[offset + k]);
                }
                return new String(0, count, r);
            }
        }
        return this;
    }

    public String trim() {
        int b = 0, e = count;
        while (b < e && value[offset + b] <= ' ') {
            b++;
        }
        while (e > b && value[offset + e - 1] <= ' ') {
            e--;
        }
        return b == 0 && e == count ? this : substring(b, e);
    }

    public String toString() {
        return this;
    }

    public char[] toCharArray() {
        char[] r = new char[count];
        System.arraycopy(value, offset, r, 0, count);
        return r;
    }

    /** Canonical instance with this content (the VM's intern table, shared with string literals). */
    public native String intern();

    public static String valueOf(Object obj) {
        return obj == null ? "null" : obj.toString();
    }

    public static String valueOf(char[] data) {
        return new String(data, 0, data.length);
    }

    public static String valueOf(char[] data, int offset, int count) {
        return new String(data, offset, count);
    }

    public static String valueOf(boolean b) {
        return b ? "true" : "false";
    }

    public static String valueOf(char c) {
        return new String(0, 1, new char[] {c});
    }

    public static String valueOf(int i) {
        return Integer.toString(i, 10);
    }

    public static String valueOf(long l) {
        return Long.toString(l, 10);
    }

    public static String valueOf(float f) {
        return Float.toString(f);
    }

    public static String valueOf(double d) {
        return Double.toString(d);
    }
}

/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

/** Growable character buffer. javac -target 1.3 compiles string concatenation to this class. */
public final class StringBuffer {
    private char[] value;
    private int count;

    public StringBuffer() {
        this(16);
    }

    public StringBuffer(int length) {
        if (length < 0) {
            throw new NegativeArraySizeException(String.valueOf(length));
        }
        value = new char[length];
    }

    public StringBuffer(String str) {
        this(str.length() + 16);
        append(str);
    }

    public synchronized int length() {
        return count;
    }

    public synchronized int capacity() {
        return value.length;
    }

    public synchronized void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > value.length) {
            expand(minimumCapacity);
        }
    }

    private void expand(int min) {
        int n = (value.length + 1) * 2;
        if (n < min) {
            n = min;
        }
        char[] nv = new char[n];
        System.arraycopy(value, 0, nv, 0, count);
        value = nv;
    }

    public synchronized void setLength(int newLength) {
        if (newLength < 0) {
            throw new StringIndexOutOfBoundsException(newLength);
        }
        if (newLength > value.length) {
            expand(newLength);
        }
        if (count < newLength) {
            for (; count < newLength; count++) {
                value[count] = '\0';
            }
        } else {
            count = newLength;
        }
    }

    public synchronized char charAt(int index) {
        if (index < 0 || index >= count) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return value[index];
    }

    public synchronized void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        if (srcBegin < 0) {
            throw new StringIndexOutOfBoundsException(srcBegin);
        }
        if (srcEnd < 0 || srcEnd > count) {
            throw new StringIndexOutOfBoundsException(srcEnd);
        }
        if (srcBegin > srcEnd) {
            throw new StringIndexOutOfBoundsException("srcBegin > srcEnd");
        }
        System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }

    public synchronized void setCharAt(int index, char ch) {
        if (index < 0 || index >= count) {
            throw new StringIndexOutOfBoundsException(index);
        }
        value[index] = ch;
    }

    public synchronized StringBuffer append(Object obj) {
        return append(String.valueOf(obj));
    }

    public synchronized StringBuffer append(String str) {
        if (str == null) {
            str = "null";
        }
        int len = str.length();
        int newCount = count + len;
        if (newCount > value.length) {
            expand(newCount);
        }
        str.getChars(0, len, value, count);
        count = newCount;
        return this;
    }

    public synchronized StringBuffer append(char[] str) {
        return append(str, 0, str.length);
    }

    public synchronized StringBuffer append(char[] str, int offset, int len) {
        if (offset < 0 || len < 0 || offset > str.length - len) {
            throw new IndexOutOfBoundsException();
        }
        int newCount = count + len;
        if (newCount > value.length) {
            expand(newCount);
        }
        System.arraycopy(str, offset, value, count, len);
        count = newCount;
        return this;
    }

    public synchronized StringBuffer append(boolean b) {
        return append(b ? "true" : "false");
    }

    public synchronized StringBuffer append(char c) {
        int newCount = count + 1;
        if (newCount > value.length) {
            expand(newCount);
        }
        value[count++] = c;
        return this;
    }

    public synchronized StringBuffer append(int i) {
        return append(Integer.toString(i, 10));
    }

    public synchronized StringBuffer append(long l) {
        return append(Long.toString(l, 10));
    }

    public synchronized StringBuffer append(float f) {
        return append(Float.toString(f));
    }

    public synchronized StringBuffer append(double d) {
        return append(Double.toString(d));
    }

    public synchronized StringBuffer delete(int start, int end) {
        if (end > count) {
            end = count;
        }
        if (start < 0 || start > end) {
            throw new StringIndexOutOfBoundsException();
        }
        int len = end - start;
        if (len > 0) {
            System.arraycopy(value, start + len, value, start, count - end);
            count -= len;
        }
        return this;
    }

    public synchronized StringBuffer deleteCharAt(int index) {
        if (index < 0 || index >= count) {
            throw new StringIndexOutOfBoundsException(index);
        }
        System.arraycopy(value, index + 1, value, index, count - index - 1);
        count--;
        return this;
    }

    public synchronized StringBuffer insert(int offset, Object obj) {
        return insert(offset, String.valueOf(obj));
    }

    public synchronized StringBuffer insert(int offset, String str) {
        if (offset < 0 || offset > count) {
            throw new StringIndexOutOfBoundsException();
        }
        if (str == null) {
            str = "null";
        }
        int len = str.length();
        int newCount = count + len;
        if (newCount > value.length) {
            expand(newCount);
        }
        System.arraycopy(value, offset, value, offset + len, count - offset);
        str.getChars(0, len, value, offset);
        count = newCount;
        return this;
    }

    public synchronized StringBuffer insert(int offset, char[] str) {
        if (offset < 0 || offset > count) {
            throw new StringIndexOutOfBoundsException();
        }
        int len = str.length;
        int newCount = count + len;
        if (newCount > value.length) {
            expand(newCount);
        }
        System.arraycopy(value, offset, value, offset + len, count - offset);
        System.arraycopy(str, 0, value, offset, len);
        count = newCount;
        return this;
    }

    public synchronized StringBuffer insert(int offset, boolean b) {
        return insert(offset, b ? "true" : "false");
    }

    public synchronized StringBuffer insert(int offset, char c) {
        if (offset < 0 || offset > count) {
            throw new StringIndexOutOfBoundsException();
        }
        int newCount = count + 1;
        if (newCount > value.length) {
            expand(newCount);
        }
        System.arraycopy(value, offset, value, offset + 1, count - offset);
        value[offset] = c;
        count = newCount;
        return this;
    }

    public synchronized StringBuffer insert(int offset, int i) {
        return insert(offset, Integer.toString(i, 10));
    }

    public synchronized StringBuffer insert(int offset, long l) {
        return insert(offset, Long.toString(l, 10));
    }

    public synchronized StringBuffer insert(int offset, float f) {
        return insert(offset, Float.toString(f));
    }

    public synchronized StringBuffer insert(int offset, double d) {
        return insert(offset, Double.toString(d));
    }

    public synchronized StringBuffer reverse() {
        for (int i = 0, j = count - 1; i < j; i++, j--) {
            char t = value[i];
            value[i] = value[j];
            value[j] = t;
        }
        return this;
    }

    public synchronized String toString() {
        return new String(value, 0, count);
    }
}

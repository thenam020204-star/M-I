package jlkvm.classfile;

import java.util.ArrayList;
import java.util.List;

/** Field/method descriptor helpers. */
public final class Descriptors {
    private Descriptors() {}

    /** @return index just after the field type starting at {@code i}, or -1 if malformed. */
    public static int skipFieldType(String d, int i) {
        int n = d.length();
        int dims = 0;
        while (i < n && d.charAt(i) == '[') {
            i++;
            if (++dims > 255) return -1;
        }
        if (i >= n) return -1;
        char c = d.charAt(i);
        switch (c) {
            case 'B': case 'C': case 'D': case 'F': case 'I': case 'J': case 'S': case 'Z':
                return i + 1;
            case 'L': {
                int semi = d.indexOf(';', i);
                if (semi < i + 2) return -1;
                for (int k = i + 1; k < semi; k++) {
                    char ch = d.charAt(k);
                    if (ch == '.' || ch == '[' || ch == '<' || ch == '>') return -1;
                }
                return semi + 1;
            }
            default:
                return -1;
        }
    }

    public static boolean isValidField(String d) {
        return d != null && skipFieldType(d, 0) == d.length();
    }

    public static boolean isValidMethod(String d) {
        if (d == null || d.length() < 3 || d.charAt(0) != '(') return false;
        int i = 1;
        while (i < d.length() && d.charAt(i) != ')') {
            i = skipFieldType(d, i);
            if (i < 0) return false;
        }
        if (i >= d.length()) return false;
        i++;
        if (i < d.length() && d.charAt(i) == 'V') return i + 1 == d.length();
        return skipFieldType(d, i) == d.length();
    }

    /** Parameter descriptors of a (valid) method descriptor. */
    public static List<String> params(String d) {
        List<String> r = new ArrayList<String>();
        int i = 1;
        while (d.charAt(i) != ')') {
            int e = skipFieldType(d, i);
            r.add(d.substring(i, e));
            i = e;
        }
        return r;
    }

    /** Return descriptor of a (valid) method descriptor ("V" for void). */
    public static String returnType(String d) {
        return d.substring(d.indexOf(')') + 1);
    }

    /** Number of 32-bit words taken by the parameters (long/double count 2). */
    public static int paramWords(String d) {
        int w = 0;
        for (String p : params(d)) w += (p.equals("J") || p.equals("D")) ? 2 : 1;
        return w;
    }
}

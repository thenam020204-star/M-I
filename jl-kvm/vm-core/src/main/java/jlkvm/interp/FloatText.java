package jlkvm.interp;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Text conversion of float/double for Float/Double.toString and parseFloat/parseDouble (PLAN T19).
 * Independent of the host JDK's own Double.toString (which changed in JDK 19): the output is the SHORTEST decimal digit string
 * that parses back to the same value, laid out in the Java format (plain decimal for 1e-3 <= |v| < 1e7, else d.dddE[-]n).
 * Grammar of the parser = JDK 1.3 FloatingDecimal.readJavaFormatString (no hex floats, optional f/F/d/D suffix, surrounding blanks trimmed).
 */
public final class FloatText {
    private FloatText() {}

    public static String toString(double d) {
        if (d != d) return "NaN";
        if (d == Double.POSITIVE_INFINITY) return "Infinity";
        if (d == Double.NEGATIVE_INFINITY) return "-Infinity";
        if (d == 0) return 1 / d < 0 ? "-0.0" : "0.0";
        double a = Math.abs(d);
        return layout(d < 0, a, shortest(new BigDecimal(a), a, false));
    }

    public static String toString(float f) {
        if (f != f) return "NaN";
        if (f == Float.POSITIVE_INFINITY) return "Infinity";
        if (f == Float.NEGATIVE_INFINITY) return "-Infinity";
        if (f == 0) return 1 / f < 0 ? "-0.0" : "0.0";
        float a = Math.abs(f);
        return layout(f < 0, a, shortest(new BigDecimal((double) a), a, true));
    }

    /** @return {digits (no trailing zeros, no leading zeros), decimal exponent of the first digit} */
    private static Object[] shortest(BigDecimal exact, double target, boolean isFloat) {
        BigDecimal r = exact;
        int maxP = isFloat ? 9 : 17;
        for (int p = 1; p <= maxP; p++) {
            r = exact.round(new MathContext(p, RoundingMode.HALF_EVEN));
            String s = r.toString();
            if (isFloat ? Float.parseFloat(s) == (float) target : Double.parseDouble(s) == target) break;
        }
        String digits = r.unscaledValue().toString();
        if (digits.length() == 1) {
            // Java prints at least two digits (d.d): when one digit suffices, the two-digit decimal nearest to the exact value is used
            // when it still identifies the number (Double.MIN_VALUE = 4.9E-324, Float.MIN_VALUE = 1.4E-45), as the JDK 19+ algorithm does.
            BigDecimal r2 = exact.round(new MathContext(2, RoundingMode.HALF_EVEN));
            String s2 = r2.toString();
            if (isFloat ? Float.parseFloat(s2) == (float) target : Double.parseDouble(s2) == target) {
                r = r2;
                digits = r.unscaledValue().toString();
            }
        }
        int e = digits.length() - 1 - r.scale();
        int end = digits.length();
        while (end > 1 && digits.charAt(end - 1) == '0') end--;
        return new Object[] {digits.substring(0, end), Integer.valueOf(e)};
    }

    private static String layout(boolean neg, double a, Object[] de) {
        String dg = (String) de[0];
        int e = ((Integer) de[1]).intValue();
        StringBuilder b = new StringBuilder();
        if (neg) b.append('-');
        if (a >= 1e-3 && a < 1e7) {
            if (e >= 0) {
                for (int i = 0; i <= e; i++) b.append(i < dg.length() ? dg.charAt(i) : '0');
                b.append('.');
                if (dg.length() > e + 1) b.append(dg, e + 1, dg.length()); else b.append('0');
            } else {
                b.append("0.");
                for (int i = -1; i > e; i--) b.append('0');
                b.append(dg);
            }
        } else {
            b.append(dg.charAt(0)).append('.');
            if (dg.length() > 1) b.append(dg, 1, dg.length()); else b.append('0');
            b.append('E').append(e);
        }
        return b.toString();
    }

    /** @return the value; throws NumberFormatException (message = the offending text) for text outside CLDC 1.1 Double.valueOf: Sign? FloatingPointLiteral of JLS 3.10.2 (so no "NaN"/"Infinity", T05c). */
    public static double parseDouble(String s) {
        return Double.parseDouble(checked(s));
    }

    public static float parseFloat(String s) {
        return Float.parseFloat(checked(s));
    }

    private static String checked(String s) {
        String t = s.trim();   // String.trim: everything <= ' '
        if (t.length() == 0) throw new NumberFormatException(s.length() == 0 ? "empty String" : s);
        int i = 0;
        if (t.charAt(0) == '+' || t.charAt(0) == '-') i++;
        int n = t.length(), digits = 0;
        for (; i < n && Character.isDigit0(t.charAt(i)); i++) digits++;
        if (i < n && t.charAt(i) == '.') {
            i++;
            for (; i < n && Character.isDigit0(t.charAt(i)); i++) digits++;
        }
        if (digits == 0) throw new NumberFormatException(s);
        if (i < n && (t.charAt(i) == 'e' || t.charAt(i) == 'E')) {
            i++;
            if (i < n && (t.charAt(i) == '+' || t.charAt(i) == '-')) i++;
            int ed = 0;
            for (; i < n && Character.isDigit0(t.charAt(i)); i++) ed++;
            if (ed == 0) throw new NumberFormatException(s);
        }
        if (i < n && "fFdD".indexOf(t.charAt(i)) >= 0) i++;
        if (i != n) throw new NumberFormatException(s);
        return t;
    }

    private static final class Character {
        static boolean isDigit0(char c) { return c >= '0' && c <= '9'; }
    }
}

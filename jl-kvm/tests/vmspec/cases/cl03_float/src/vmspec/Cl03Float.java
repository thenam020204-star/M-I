package vmspec;

import javax.microedition.midlet.MIDlet;

public class Cl03Float extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String pd(String s) {
        try { return Double.toString(Double.parseDouble(s)); } catch (NumberFormatException e) { return "NFE"; }
    }

    static String pf(String s) {
        try { return Float.toString(Float.parseFloat(s)); } catch (NumberFormatException e) { return "NFE"; }
    }

    static long bits(double d) { return Double.doubleToLongBits(d); }

    protected void startApp() {
        double[] ds = {0.0, -0.0, 1.0, -1.0, 0.5, 0.1, 0.2, 0.3, 1.5, 2.0, 3.14159, 100.0, 123.456, 1234567.0, 9999999.0, 10000000.0, 12345678.9, 0.001, 0.0009999, 0.01, 1.0E-5, 1.0E10, 1.0E21, 1.0E22, 1.0E23, 1.7976931348623157E308, 4.9E-324, 2.2250738585072014E-308, 123456789012345678.0, 0.30000000000000004, 1.0 / 3, 2.0 / 3, 100.0 / 7, 1.0E7, 9.999999999999999E22, 5.0E-324, 8.41E21, 2.0E-3, 4.35, 0.07, 1.0E-3, 999999.9999999999};
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < ds.length; i++) { b.append(ds[i]).append(' '); }
        p("double " + b);
        float[] fs = {0.0f, -0.0f, 1.0f, 0.1f, 0.2f, 0.3f, 1.5f, 3.14159f, 100.0f, 123.456f, 1234567.0f, 9999999.0f, 1.0E7f, 16777216.0f, 0.001f, 1.0E-5f, 1.0E10f, 3.4028235E38f, 1.4E-45f, 1.17549435E-38f, 1.0f / 3, 2.0f / 3, 100.0f / 7, 33554432.0f, 0.7f, 4.35f, 8.5E-3f};
        b = new StringBuffer();
        for (int i = 0; i < fs.length; i++) { b.append(fs[i]).append(' '); }
        p("float " + b);
        p("special " + Double.toString(Double.NaN) + " " + Double.toString(Double.POSITIVE_INFINITY) + " " + Double.toString(Double.NEGATIVE_INFINITY) + " " + Float.toString(Float.NaN) + " " + Float.toString(Float.POSITIVE_INFINITY) + " " + Float.toString(Float.NEGATIVE_INFINITY));
        p("consts " + Double.MAX_VALUE + " " + Double.MIN_VALUE + " " + Float.MAX_VALUE + " " + Float.MIN_VALUE);
        p("parseDouble " + pd("1") + " " + pd("-1.5") + " " + pd("+2.5e3") + " " + pd(".5") + " " + pd("5.") + " " + pd("1e-3") + " " + pd("1E5") + " " + pd(" 7.5 ") + " " + pd("1.5d") + " " + pd("1.5f") + " " + pd("NaN") + " " + pd("-Infinity") + " " + pd("Infinity") + " " + pd("+Infinity"));
        p("parseDouble bad " + pd("") + " " + pd("abc") + " " + pd("1.2.3") + " " + pd("e5") + " " + pd("1e") + " " + pd(".") + " " + pd("-") + " " + pd("1 2") + " " + pd("0x10") + " " + pd("1,5") + " " + pd("nan") + " " + pd("inf"));
        p("parseDouble round " + pd("0.1") + " " + pd("4.9e-324") + " " + pd("1e-400") + " " + pd("1e400") + " " + pd("-1e400") + " " + pd("2.2250738585072012e-308") + " " + pd("9007199254740993") + " " + pd("0.30000000000000004") + " " + pd("123456789012345678901234567890"));
        p("parseFloat " + pf("1") + " " + pf("0.1") + " " + pf("3.4028235e38") + " " + pf("3.4028236e38") + " " + pf("1e-46") + " " + pf("1.4e-45") + " " + pf("16777217") + " " + pf("abc") + " " + pf("") + " " + pf("1.5F"));
        p("bits " + bits(1.0) + " " + bits(-0.0) + " " + bits(Double.NaN) + " " + bits(Double.POSITIVE_INFINITY) + " " + Float.floatToIntBits(1.0f) + " " + Float.floatToIntBits(Float.NaN) + " " + Float.floatToIntBits(-0.0f) + " " + Double.longBitsToDouble(0x4000000000000000L) + " " + Float.intBitsToFloat(0x40490fdb));
        p("props " + Double.isNaN(Double.NaN) + Double.isNaN(1.0) + Double.isInfinite(1.0 / 0) + Double.isInfinite(-1.0 / 0) + Double.isInfinite(1.0) + " " + new Double(Double.NaN).isNaN() + new Double(1.0 / 0).isInfinite() + Float.isNaN(Float.NaN) + Float.isInfinite(1.0f / 0) + new Float(Float.NaN).isNaN() + new Float(1.0f / 0).isInfinite());
        p("equals " + new Double(Double.NaN).equals(new Double(Double.NaN)) + new Double(0.0).equals(new Double(-0.0)) + new Double(1.5).equals(new Double(1.5)) + new Float(Float.NaN).equals(new Float(Float.NaN)) + new Float(0.0f).equals(new Float(-0.0f)) + new Double(1.5).equals(new Float(1.5f)));
        p("hash " + new Double(1.5).hashCode() + " " + new Double(-0.0).hashCode() + " " + new Float(1.5f).hashCode() + " " + new Float(-0.0f).hashCode() + " " + new Double(Double.NaN).hashCode() + " " + new Float(Float.NaN).hashCode());
        p("values " + new Double(3.99).intValue() + " " + new Double(-3.99).longValue() + " " + new Double(300.5).byteValue() + " " + new Double(70000.5).shortValue() + " " + new Double(1.5).floatValue() + " " + new Float(2.5f).doubleValue() + " " + new Float(1.0E10f).intValue() + " " + new Float(-1.0E10f).longValue() + " " + new Float(Float.NaN).intValue() + " " + new Float(0.1f).doubleValue());
        p("valueOf " + Double.valueOf("2.5") + " " + Float.valueOf("2.5") + " " + new Float(0.1).toString() + " " + new Double(0.1f).toString());
        p("concat " + (1.0f + 2) + " " + (0.1f + 0.2f) + " " + (0.1 + 0.2) + " " + (float) 0.1 + " " + (double) 0.1f + " " + 1e-4 + " " + (float) 1e-4 + " " + 3.0e6 + " " + 3.0e7 + " " + (long) 1e19 + " " + (int) -1e10 + " " + (char) 65.7);
        p("strvalueOf " + String.valueOf(2.50) + " " + String.valueOf(2.5f) + " " + String.valueOf(1e100));
        double acc = 0;
        for (int i = 1; i <= 50; i++) { acc += 1.0 / i; }
        p("sum " + acc + " " + (float) acc + " " + acc * 1e10 + " " + acc / 1e10);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

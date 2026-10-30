package jlkvm.interp;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.util.Random;
import org.junit.Test;

/** T19: Float/Double text conversion of the VM equals the JDK 19+ shortest-digits algorithm (the CLDC 1.1 javadoc requirement). */
public class FloatTextTest {
    private static boolean jdk19() {
        String v = System.getProperty("java.specification.version");
        try { return Integer.parseInt(v) >= 19; } catch (NumberFormatException e) { return false; }
    }

    @Test
    public void doublesMatchShortestRepresentation() {
        assumeTrue("needs the JDK 19+ Double.toString as reference", jdk19());
        Random r = new Random(7);
        int n = 0;
        for (int i = 0; i < 60000; i++) {
            double d;
            switch (i % 4) {
                case 0: d = Double.longBitsToDouble(r.nextLong()); break;
                case 1: d = r.nextDouble() * Math.pow(10, r.nextInt(40) - 20); break;
                case 2: d = r.nextInt(2000000) / 1000.0; break;
                default: d = (double) r.nextFloat(); break;
            }
            if (Double.isNaN(d)) continue;
            assertEquals(Double.toString(d), FloatText.toString(d));
            n++;
        }
        double[] special = {0.0, -0.0, Double.MIN_VALUE, Double.MAX_VALUE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN, 1e23, 8.41e21, 1e7, 9999999.999999998, 1e-3, 9.999999999999999e-4, 2e-3, 5e-324, 1.0E22, 123456789012345678.0};
        for (double d : special) assertEquals(Double.toString(d), FloatText.toString(d));
        assertTrue(n > 50000);
    }

    @Test
    public void floatsMatchShortestRepresentation() {
        assumeTrue("needs the JDK 19+ Float.toString as reference", jdk19());
        Random r = new Random(11);
        for (int i = 0; i < 60000; i++) {
            float f;
            switch (i % 3) {
                case 0: f = Float.intBitsToFloat(r.nextInt()); break;
                case 1: f = r.nextFloat() * (float) Math.pow(10, r.nextInt(20) - 10); break;
                default: f = r.nextInt(2000000) / 100.0f; break;
            }
            if (Float.isNaN(f)) continue;
            assertEquals(Float.toString(f), FloatText.toString(f));
        }
        float[] special = {0.0f, -0.0f, Float.MIN_VALUE, Float.MAX_VALUE, Float.MIN_NORMAL, 1e7f, 9999999f, 1e-3f, 16777216f, 1.0E10f};
        for (float f : special) assertEquals(Float.toString(f), FloatText.toString(f));
    }

    @Test
    public void parserAcceptsTheJdk13Grammar() {
        assertEquals(2500.0, FloatText.parseDouble("+2.5e3"), 0);
        assertEquals(0.5, FloatText.parseDouble(".5"), 0);
        assertEquals(5.0, FloatText.parseDouble("5."), 0);
        assertEquals(1.5, FloatText.parseDouble(" 1.5d "), 0);
        assertEquals(1.5f, FloatText.parseFloat("1.5F"), 0);
        assertTrue(Double.isNaN(FloatText.parseDouble("NaN")));
        assertEquals(Double.NEGATIVE_INFINITY, FloatText.parseDouble("-Infinity"), 0);
        for (String bad : new String[] {"", " ", "abc", "1.2.3", "e5", "1e", ".", "-", "+", "0x10", "0x1p3", "1,5", "nan", "inf", "1 2", "--1", "1e+", "Infinit", "NaN1"}) {
            try { FloatText.parseDouble(bad); fail("accepted: '" + bad + "'"); } catch (NumberFormatException expected) { }
        }
    }
}

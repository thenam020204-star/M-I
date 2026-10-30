package vmspec;

import javax.microedition.midlet.MIDlet;

public class Cl02Numbers extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String pi(String s, int radix) {
        try { return String.valueOf(Integer.parseInt(s, radix)); } catch (NumberFormatException e) { return "NFE"; }
    }

    static String pl(String s, int radix) {
        try { return String.valueOf(Long.parseLong(s, radix)); } catch (NumberFormatException e) { return "NFE"; }
    }

    static String ex(Runnable r) {
        try { r.run(); return "no-exception"; } catch (Throwable t) { return t.getClass().getName(); }
    }

    protected void startApp() {
        p("int toString " + Integer.toString(0) + " " + Integer.toString(-1) + " " + Integer.toString(Integer.MAX_VALUE) + " " + Integer.toString(Integer.MIN_VALUE) + " " + Integer.toString(255, 16) + " " + Integer.toString(-255, 16) + " " + Integer.toString(Integer.MIN_VALUE, 2) + " " + Integer.toString(35, 36) + " " + Integer.toString(10, 99) + " " + Integer.toString(10, 1));
        p("int hex/oct/bin " + Integer.toHexString(-1) + " " + Integer.toHexString(255) + " " + Integer.toHexString(0) + " " + Integer.toOctalString(-1) + " " + Integer.toOctalString(8) + " " + Integer.toBinaryString(-1) + " " + Integer.toBinaryString(5) + " " + Integer.toBinaryString(0));
        p("parseInt " + pi("0", 10) + " " + pi("-0", 10) + " " + pi("+7", 10) + " " + pi("2147483647", 10) + " " + pi("-2147483648", 10) + " " + pi("2147483648", 10) + " " + pi("-2147483649", 10) + " " + pi("", 10) + " " + pi("-", 10) + " " + pi("+", 10) + " " + pi("1 ", 10) + " " + pi(" 1", 10) + " " + pi("12a", 10) + " " + pi("ff", 16) + " " + pi("FF", 16) + " " + pi("-80000000", 16) + " " + pi("80000000", 16) + " " + pi("zz", 36) + " " + pi("102", 2) + " " + pi("99999999999", 10) + " " + pi("0x10", 16) + " " + pi("1_0", 10));
        p("parseInt radix " + pi("1", 1) + " " + pi("1", 37) + " " + ex(new Runnable() { public void run() { Integer.parseInt(null); } }));
        p("valueOf " + Integer.valueOf("42") + " " + Integer.valueOf("-1f", 16) + " " + new Integer(7).hashCode() + " " + new Integer(7).equals(new Integer(7)) + " " + new Integer(7).equals(new Long(7)) + " " + new Integer(300).byteValue() + " " + new Integer(70000).shortValue() + " " + new Integer(5).doubleValue() + " " + new Integer(5).floatValue() + " " + new Integer(-5).longValue());
        p("long toString " + Long.toString(0L) + " " + Long.toString(-1L) + " " + Long.toString(Long.MAX_VALUE) + " " + Long.toString(Long.MIN_VALUE) + " " + Long.toString(255L, 16) + " " + Long.toString(-255L, 2) + " " + Long.toString(Long.MIN_VALUE, 16) + " " + Long.toString(Long.MAX_VALUE, 36));
        p("parseLong " + pl("0", 10) + " " + pl("9223372036854775807", 10) + " " + pl("-9223372036854775808", 10) + " " + pl("9223372036854775808", 10) + " " + pl("-9223372036854775809", 10) + " " + pl("7fffffffffffffff", 16) + " " + pl("", 10) + " " + pl("12L", 10) + " " + pl("zzzzzzzzzzzzz", 36) + " " + pl("-", 10));
        p("long misc " + new Long(1L << 40).hashCode() + " " + new Long(-1L).hashCode() + " " + new Long(3).equals(new Long(3)) + " " + new Long(3).equals(new Integer(3)) + " " + new Long(5).floatValue());
        p("short/byte " + Short.parseShort("-32768") + " " + Short.parseShort("32767") + " " + Byte.parseByte("-128") + " " + Byte.parseByte("127") + " " + new Short((short) 3).hashCode() + " " + new Byte((byte) -3).hashCode() + " " + Short.MIN_VALUE + " " + Byte.MAX_VALUE);
        p("short/byte ex " + ex(new Runnable() { public void run() { Short.parseShort("32768"); } }) + " " + ex(new Runnable() { public void run() { Byte.parseByte("128"); } }) + " " + ex(new Runnable() { public void run() { Byte.parseByte("x"); } }));
        p("boolean " + Boolean.TRUE + " " + Boolean.FALSE + " " + new Boolean(true).booleanValue() + " " + new Boolean(true).hashCode() + " " + new Boolean(false).hashCode() + " " + new Boolean(true).equals(new Boolean(true)) + " " + new Boolean(true).equals(Boolean.FALSE) + " " + new Boolean(false).toString());
        p("char props " + Character.isDigit('5') + Character.isDigit('a') + Character.isLowerCase('a') + Character.isLowerCase('A') + Character.isUpperCase('A') + Character.isUpperCase('a') + " " + (int) Character.toLowerCase('A') + " " + (int) Character.toUpperCase('z') + " " + (int) Character.toUpperCase('5') + " " + (int) Character.toUpperCase('é') + " " + (int) Character.toLowerCase('É') + " " + (int) Character.toUpperCase('ÿ'));
        p("char digit " + Character.digit('7', 10) + " " + Character.digit('a', 16) + " " + Character.digit('F', 16) + " " + Character.digit('g', 16) + " " + Character.digit('7', 7) + " " + Character.digit('z', 36) + " " + Character.digit('1', 37) + " " + Character.digit('1', 1));
        p("char misc " + new Character('x').charValue() + " " + new Character('x').hashCode() + " " + new Character('x') + " " + new Character('x').equals(new Character('x')) + " " + (int) Character.MIN_VALUE + " " + (int) Character.MAX_VALUE + " " + Character.MIN_RADIX + " " + Character.MAX_RADIX);
        p("math abs " + Math.abs(-5) + " " + Math.abs(Integer.MIN_VALUE) + " " + Math.abs(-5L) + " " + Math.abs(Long.MIN_VALUE) + " " + Math.abs(-1.5f) + " " + Math.abs(-0.0) + " " + Math.abs(-2.5));
        p("math minmax " + Math.max(3, 4) + " " + Math.min(3, 4) + " " + Math.max(3L, -4L) + " " + Math.min(3L, -4L) + " " + Math.max(1.5f, 2.5f) + " " + Math.min(1.5f, 2.5f) + " " + Math.max(1.5, 2.5) + " " + Math.min(1.5, 2.5) + " " + Math.max(Double.NaN, 1.0) + " " + Math.min(1.0f, Float.NaN) + " " + Math.max(-0.0, 0.0) + " " + Math.min(-0.0, 0.0) + " " + Math.max(-0.0f, 0.0f) + " " + Math.min(0.0f, -0.0f));
        p("math round " + Math.floor(2.7) + " " + Math.floor(-2.7) + " " + Math.ceil(2.1) + " " + Math.ceil(-2.1) + " " + Math.ceil(-0.5) + " " + Math.floor(5) + " " + Math.sqrt(16.0) + " " + Math.sqrt(2.0) + " " + Math.sqrt(-1.0));
        p("math trig " + Math.sin(0.0) + " " + Math.cos(0.0) + " " + Math.tan(0.0) + " " + Math.sin(Math.PI / 2) + " " + Math.toDegrees(Math.PI) + " " + Math.toRadians(180.0) + " " + Math.E);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

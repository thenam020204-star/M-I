package vmspec;

import javax.microedition.midlet.MIDlet;

public class Cl01String extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String ex(Runnable r) {
        try { r.run(); return "no-exception"; } catch (Throwable t) { return t.getClass().getName(); }
    }

    static String exIdx(Runnable r) {
        try { r.run(); return "no-exception"; } catch (IndexOutOfBoundsException t) { return "IndexOutOfBoundsException-family"; } catch (Throwable t) { return t.getClass().getName(); }
    }

    protected void startApp() {
        String s = "Hello, World";
        p("length " + s.length() + " charAt " + s.charAt(4));
        p("substring " + s.substring(7) + "|" + s.substring(0, 5) + "|" + s.substring(5, 5) + "|");
        p("indexOf " + s.indexOf('o') + " " + s.indexOf('o', 5) + " " + s.indexOf('z') + " " + s.indexOf("World") + " " + s.indexOf("o", 9) + " " + s.indexOf("") + " " + s.indexOf("", 50) + " " + s.indexOf('l', -4));
        p("indexOf empty " + s.indexOf("", 11) + " " + s.indexOf("", 12) + " " + s.indexOf("", -1) + " " + "".indexOf("", 0) + " " + "".indexOf("", 1));
        p("lastIndexOf " + s.lastIndexOf('o') + " " + s.lastIndexOf('o', 7) + " " + s.lastIndexOf('H') + " " + s.lastIndexOf('o', -1) + " " + s.lastIndexOf('l', 100));
        p("starts/ends " + s.startsWith("Hello") + s.startsWith("World", 7) + s.startsWith("") + s.startsWith("Hello", 1) + s.startsWith("x", -1) + s.endsWith("World") + s.endsWith("") + s.endsWith("Worlds"));
        p("equals " + s.equals("Hello, World") + s.equals(null) + s.equals("hello, world") + s.equalsIgnoreCase("hELLO, wORLD") + s.equalsIgnoreCase(null) + "".equals(""));
        p("compareTo " + "a".compareTo("b") + " " + "b".compareTo("a") + " " + "abc".compareTo("abcd") + " " + "abcd".compareTo("abc") + " " + "a".compareTo("a") + " " + "Z".compareTo("a"));
        p("trim [" + "  a b \t\n".trim() + "] [" + "   ".trim() + "] [" + "x".trim() + "]");
        p("replace " + "banana".replace('a', 'o') + " " + "banana".replace('z', 'y'));
        p("case " + "MiXed 123".toUpperCase() + " " + "MiXed 123".toLowerCase());
        p("concat " + "ab".concat("cd") + " " + "ab".concat(""));
        p("hashCode " + "".hashCode() + " " + "a".hashCode() + " " + s.hashCode() + " " + "The quick brown fox jumps over the lazy dog".hashCode());
        p("valueOf " + String.valueOf(12) + String.valueOf(-34L) + String.valueOf(true) + String.valueOf('c') + String.valueOf((Object) null) + String.valueOf(new char[] {'x', 'y', 'z'}) + String.valueOf(new char[] {'x', 'y', 'z'}, 1, 2));
        char[] ca = s.toCharArray();
        p("toCharArray " + ca.length + ca[0] + ca[11]);
        char[] dst = new char[6];
        s.getChars(7, 12, dst, 1);
        p("getChars " + (int) dst[0] + new String(dst, 1, 5));
        p("ctor " + new String() .length() + new String("abc") + new String(new char[] {'a', 'b', 'c'}) + new String(new char[] {'a', 'b', 'c'}, 1, 1) + new String(new StringBuffer("sb")));
        String shared = new String("shared");
        p("substring-same " + (shared.substring(0, 6) == shared) + (shared.substring(0) == shared) + shared.substring(2, 4).equals("ar"));
        p("literal intern " + ("Hello, World" == s) + (new String("Hello, World") == s) + (new String("Hello, World").intern() == s));
        p("regionMatches " + s.regionMatches(false, 7, "World", 0, 5) + s.regionMatches(true, 7, "WORLD", 0, 5) + s.regionMatches(false, 7, "WORLD", 0, 5) + s.regionMatches(false, 8, "World", 0, 5) + s.regionMatches(false, -1, "H", 0, 1) + s.regionMatches(false, 0, "H", 0, 0));
        p("toString " + (s.toString() == s));
        p("ex charAt " + ex(new Runnable() { public void run() { "abc".charAt(3); } }));
        p("ex charAt-1 " + ex(new Runnable() { public void run() { "abc".charAt(-1); } }));
        p("ex substring " + ex(new Runnable() { public void run() { "abc".substring(2, 1); } }));
        p("ex substring2 " + ex(new Runnable() { public void run() { "abc".substring(4); } }));
        p("ex substring3 " + ex(new Runnable() { public void run() { "abc".substring(0, 4); } }));
        p("ex null-concat " + ex(new Runnable() { public void run() { "abc".concat(null); } }));
        p("ex ctor-null " + ex(new Runnable() { public void run() { new String((char[]) null); } }));
        p("ex ctor-range " + ex(new Runnable() { public void run() { new String(new char[2], 1, 2); } }));
        p("ex ctor-neg " + ex(new Runnable() { public void run() { new String(new char[2], -1, 1); } }));
        p("ex compareTo-null " + ex(new Runnable() { public void run() { "a".compareTo(null); } }));
        p("ex indexOf-null " + ex(new Runnable() { public void run() { "a".indexOf((String) null); } }));
        p("ex startsWith-null " + ex(new Runnable() { public void run() { "a".startsWith(null); } }));
        p("ex getChars " + ex(new Runnable() { public void run() { "abc".getChars(0, 4, new char[10], 0); } }));
        p("ex getChars2 " + exIdx(new Runnable() { public void run() { "abc".getChars(0, 3, new char[2], 0); } }));
        StringBuffer sb = new StringBuffer();
        sb.append("a").append(1).append('c').append(2L).append(true).append(new char[] {'x', 'y'}).append((Object) null).append((String) null);
        p("sb1 " + sb + " " + sb.length());
        sb.setLength(3);
        p("sb2 " + sb + " " + sb.length());
        sb.insert(1, "ZZ").insert(0, 7).insert(0, 'q').insert(0, false);
        p("sb3 " + sb);
        sb.reverse();
        p("sb4 " + sb);
        sb.setCharAt(0, 'T');
        sb.deleteCharAt(1);
        sb.delete(2, 4);
        p("sb5 " + sb + " " + sb.charAt(1));
        p("sb6 " + sb);
        StringBuffer c = new StringBuffer(2);
        p("sb cap " + (c.capacity() >= 2) + " " + (new StringBuffer(50).capacity() >= 50) + " " + (new StringBuffer("abc").capacity() >= 3));
        c.append("abcdef");
        c.ensureCapacity(100);
        p("sb7 " + c + " " + (c.capacity() >= 100));
        char[] gd = new char[4];
        c.getChars(1, 4, gd, 0);
        p("sb getChars " + new String(gd, 0, 3));
        p("ex sb insert " + ex(new Runnable() { public void run() { new StringBuffer("ab").insert(3, "x"); } }));
        p("ex sb charAt " + ex(new Runnable() { public void run() { new StringBuffer("ab").charAt(2); } }));
        p("ex sb setLength " + ex(new Runnable() { public void run() { new StringBuffer("ab").setLength(-1); } }));
        p("ex sb delete " + ex(new Runnable() { public void run() { new StringBuffer("ab").delete(1, 5); } }));
        p("ex sb delete2 " + ex(new Runnable() { public void run() { new StringBuffer("ab").delete(2, 1); } }));
        p("ex sb deleteCharAt " + ex(new Runnable() { public void run() { new StringBuffer("ab").deleteCharAt(2); } }));
        p("ex sb ctor " + ex(new Runnable() { public void run() { new StringBuffer(-1); } }));
        p("ex sb ctor null " + ex(new Runnable() { public void run() { new StringBuffer((String) null); } }));
        p("ex sb setCharAt " + ex(new Runnable() { public void run() { new StringBuffer("ab").setCharAt(2, 'x'); } }));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

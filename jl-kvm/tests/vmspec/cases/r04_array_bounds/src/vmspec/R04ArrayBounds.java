package vmspec;

import javax.microedition.midlet.MIDlet;

public class R04ArrayBounds extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static int m1 = -1, n = 3;

    protected void startApp() {
        int[] a = new int[3];
        byte[] b = new byte[3];
        Object[] o = new Object[3];
        try { a[m1] = 1; p("istore -1 ok"); } catch (ArrayIndexOutOfBoundsException e) { p("istore -1 ArrayIndexOutOfBoundsException"); }
        try { p("iload " + a[n]); } catch (ArrayIndexOutOfBoundsException e) { p("iload n ArrayIndexOutOfBoundsException"); }
        try { b[n] = 1; p("bastore ok"); } catch (ArrayIndexOutOfBoundsException e) { p("bastore n ArrayIndexOutOfBoundsException"); }
        try { p("aaload " + o[m1]); } catch (ArrayIndexOutOfBoundsException e) { p("aaload -1 ArrayIndexOutOfBoundsException"); }
        try { o[n] = null; p("aastore ok"); } catch (ArrayIndexOutOfBoundsException e) { p("aastore n ArrayIndexOutOfBoundsException"); }
        p("last-ok " + a[n - 1] + " " + b[n - 1]);
        // IndexOutOfBounds vs ArrayIndexOutOfBounds relationship (String/StringBuffer use the superclass family)
        try { "abc".charAt(n); } catch (StringIndexOutOfBoundsException e) { p("charAt StringIndexOutOfBoundsException"); }
        try { System.arraycopy(a, 0, a, 1, n); p("arraycopy ok"); } catch (ArrayIndexOutOfBoundsException e) { p("arraycopy ArrayIndexOutOfBoundsException"); }
        try { System.arraycopy(a, 0, b, 0, 1); p("arraycopy-type ok"); } catch (ArrayStoreException e) { p("arraycopy-type ArrayStoreException"); }
        try { System.arraycopy(null, 0, a, 0, 1); p("arraycopy-null ok"); } catch (NullPointerException e) { p("arraycopy-null NullPointerException"); }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

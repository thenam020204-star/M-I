package vmspec;

import javax.microedition.midlet.MIDlet;

public class R01NegativeArraySize extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static int neg = -1, zero = 0;

    protected void startApp() {
        try { int[] a = new int[neg]; p("newarray " + a.length); } catch (NegativeArraySizeException e) { p("newarray NegativeArraySizeException"); }
        try { Object[] a = new Object[neg]; p("anewarray " + a.length); } catch (NegativeArraySizeException e) { p("anewarray NegativeArraySizeException"); }
        try { int[][] a = new int[2][neg]; p("multianewarray-inner " + a.length); } catch (NegativeArraySizeException e) { p("multianewarray-inner NegativeArraySizeException"); }
        try { int[][] a = new int[neg][2]; p("multianewarray-outer " + a.length); } catch (NegativeArraySizeException e) { p("multianewarray-outer NegativeArraySizeException"); }
        p("zero-length " + new int[zero].length + " " + new Object[zero].length);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

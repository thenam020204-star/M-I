package vmspec;

import javax.microedition.midlet.MIDlet;

public class R02ArrayStore extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static class A { }
    static class B extends A { }

    protected void startApp() {
        Object[] o = new String[1];
        try { o[0] = new Integer(1); p("store-Integer-in-String[] ok"); } catch (ArrayStoreException e) { p("store-Integer-in-String[] ArrayStoreException"); }
        try { o[0] = null; p("store-null ok"); } catch (ArrayStoreException e) { p("store-null ArrayStoreException"); }
        try { o[0] = "s"; p("store-String ok"); } catch (ArrayStoreException e) { p("store-String ArrayStoreException"); }
        A[] arr = new B[1];
        try { arr[0] = new A(); p("store-A-in-B[] ok"); } catch (ArrayStoreException e) { p("store-A-in-B[] ArrayStoreException"); }
        try { arr[0] = new B(); p("store-B-in-B[] ok"); } catch (ArrayStoreException e) { p("store-B-in-B[] ArrayStoreException"); }
        Object[] oa = new int[1][];
        try { oa[0] = new int[0]; p("store-int[]-in-int[][] ok"); } catch (ArrayStoreException e) { p("store-int[]-in-int[][] ArrayStoreException"); }
        try { oa[0] = new long[0]; p("store-long[]-in-int[][] ok"); } catch (ArrayStoreException e) { p("store-long[]-in-int[][] ArrayStoreException"); }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

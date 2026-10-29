package vmspec;

import javax.microedition.midlet.MIDlet;

public class C01CheckcastInstanceof extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    interface I { }
    interface J extends I { }
    static class R implements Runnable { public void run() { } }
    static class K implements J { }
    static String b(boolean v) { return v ? "T" : "F"; }

    protected void startApp() {
        Object s = new String[1];
        p("String[] instanceof Object[] " + b(s instanceof Object[]));
        p("String[] instanceof String[] " + b(s instanceof String[]));
        p("String[] instanceof Integer[] " + b(s instanceof Integer[]));
        p("String[] instanceof Object " + b(s instanceof Object));
        Object ia = new int[1];
        p("int[] instanceof Object[] " + b(ia instanceof Object[]));
        p("int[] instanceof int[] " + b(ia instanceof int[]));
        p("int[] instanceof long[] " + b(ia instanceof long[]));
        Object ii = new int[1][1];
        p("int[][] instanceof Object[] " + b(ii instanceof Object[]));
        p("int[][] instanceof int[][] " + b(ii instanceof int[][]));
        p("int[][] instanceof int[] " + b(ii instanceof int[]));
        Object ka = new K[1];
        p("K[] instanceof I[] " + b(ka instanceof I[]));
        p("K[] instanceof J[] " + b(ka instanceof J[]));
        p("K[] instanceof Runnable[] " + b(ka instanceof Runnable[]));
        Object k = new K();
        p("K instanceof I " + b(k instanceof I));
        p("K instanceof Runnable " + b(k instanceof Runnable));
        p("null instanceof Object " + b(null instanceof Object));
        try { Runnable r = (Runnable) k; p("cast K->Runnable ok " + r); } catch (ClassCastException e) { p("cast K->Runnable ClassCastException"); }
        try { I i = (I) k; p("cast K->I ok"); } catch (ClassCastException e) { p("cast K->I ClassCastException"); }
        try { Object[] o = (Object[]) ia; p("cast int[]->Object[] ok"); } catch (ClassCastException e) { p("cast int[]->Object[] ClassCastException"); }
        try { Integer[] o = (Integer[]) s; p("cast String[]->Integer[] ok"); } catch (ClassCastException e) { p("cast String[]->Integer[] ClassCastException"); }
        try { Object n = (String) null; p("cast null ok"); } catch (ClassCastException e) { p("cast null ClassCastException"); }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

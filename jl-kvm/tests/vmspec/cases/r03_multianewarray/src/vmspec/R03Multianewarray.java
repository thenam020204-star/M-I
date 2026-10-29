package vmspec;

import javax.microedition.midlet.MIDlet;

public class R03Multianewarray extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }



    protected void startApp() {
        int[][][] a = new int[2][3][];
        p("lens " + a.length + " " + a[0].length + " " + a[1].length);
        p("third-null " + (a[0][0] == null) + " " + (a[1][2] == null));
        int[][][] b = new int[2][0][3];
        p("lens " + b.length + " " + b[0].length + " " + b[1].length);
        long[][] c = new long[2][2];
        p("deflt " + c[1][1]);
        p("name " + a.getClass().getName() + " " + c.getClass().getName());
        Object[][] d = new String[2][2];
        p("name " + d.getClass().getName() + " " + d[0].getClass().getName());
        boolean[][] e = new boolean[1][1];
        char[][] f = new char[1][1];
        p("name " + e.getClass().getName() + " " + f.getClass().getName() + " " + e[0][0] + " " + (int) f[0][0]);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

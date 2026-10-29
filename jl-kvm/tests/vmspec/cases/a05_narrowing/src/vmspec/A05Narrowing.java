package vmspec;

import javax.microedition.midlet.MIDlet;

public class A05Narrowing extends MIDlet {
    static int i200 = 200, i70000 = 70000, im1 = -1, im129 = -129, i65537 = 65537, i32768 = 32768;
    static long big = 0x100000001L, ffff = 0xFFFFFFFFL, lmin = Long.MIN_VALUE;

    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        p("i2b200 " + (byte) i200);
        p("i2b-129 " + (byte) im129);
        p("i2s70000 " + (short) i70000);
        p("i2s32768 " + (short) i32768);
        p("i2c-1 " + (int) (char) im1);
        p("i2c65537 " + (int) (char) i65537);
        p("l2i0x100000001 " + (int) big);
        p("l2i0xFFFFFFFF " + (int) ffff);
        p("l2iMIN " + (int) lmin);
        p("b2c " + (int) (char) (byte) i200);
        p("s2c " + (int) (char) (short) i32768);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

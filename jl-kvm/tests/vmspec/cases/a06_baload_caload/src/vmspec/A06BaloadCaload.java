package vmspec;

import javax.microedition.midlet.MIDlet;

public class A06BaloadCaload extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        byte[] b = new byte[] { (byte) 0xFF, (byte) 0x80, 0x7F };
        char[] c = new char[] { (char) 0xFFFF, (char) 0x8000, 0x7FFF };
        short[] s = new short[] { (short) 0xFFFF, (short) 0x8000, 0x7FFF };
        for (int i = 0; i < 3; i++) {
            p("baload[" + i + "] " + b[i] + " &ff=" + (b[i] & 0xFF));
            p("caload[" + i + "] " + (int) c[i]);
            p("saload[" + i + "] " + s[i]);
        }
        // bastore truncates, castore truncates, sastore truncates
        int big = 0x1FF;
        b[0] = (byte) big;
        c[0] = (char) 0x1FFFF;
        s[0] = (short) 0x1FFFF;
        p("bastore " + b[0]);
        p("castore " + (int) c[0]);
        p("sastore " + s[0]);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

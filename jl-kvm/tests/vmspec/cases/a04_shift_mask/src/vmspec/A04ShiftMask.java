package vmspec;

import javax.microedition.midlet.MIDlet;

public class A04ShiftMask extends MIDlet {
    static int one = 1, neg8 = -8, neg1 = -1, s33 = 33, sm1 = -1, s32 = 32;
    static long lone = 1L, lneg1 = -1L, lneg8 = -8L;
    static int s65 = 65, s64 = 64, s63 = 63;

    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        p("ishl33 " + (one << s33));
        p("ishl-1 " + (one << sm1));
        p("ishl32 " + (one << s32));
        p("ishr33 " + (neg8 >> s33));
        p("iushr33 " + (neg1 >>> s33));
        p("iushr-1 " + (neg1 >>> sm1));
        p("lshl65 " + (lone << s65));
        p("lshl64 " + (lone << s64));
        p("lshl63 " + (lone << s63));
        p("lshr65 " + (lneg8 >> s65));
        p("lushr65 " + (lneg1 >>> s65));
        p("lushr-1 " + (lneg1 >>> sm1));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}

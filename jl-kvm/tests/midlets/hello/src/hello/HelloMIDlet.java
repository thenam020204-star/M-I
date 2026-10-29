package hello;

import javax.microedition.midlet.MIDlet;

/** Minimal MIDlet used as toolchain proof and oracle smoke test (PLAN T02/T05). */
public class HelloMIDlet extends MIDlet {
    protected void startApp() {
        System.out.println("HELLO-JLKVM");
        int s = 0;
        for (int i = 1; i <= 10; i++) {
            if ((i & 1) == 0) s += i; else s -= 1;
        }
        System.out.println("sum=" + s);
        notifyDestroyed();
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean unconditional) {
    }
}

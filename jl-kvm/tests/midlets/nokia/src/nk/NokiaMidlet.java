package nk;

import javax.microedition.midlet.MIDlet;

/** T28 host test: the Nokia UI layer exists only when the profile declares "nokia-ui" (D10). */
public class NokiaMidlet extends MIDlet {
    public void startApp() {
        try {
            Class.forName("com.nokia.mid.ui.FullCanvas");
            Class.forName("com.nokia.mid.sound.Sound");
        } catch (ClassNotFoundException e) {
            System.out.println("> nokia-ui layer absent");
            notifyDestroyed();
            return;
        }
        System.out.println("> nokia-ui layer present");
        NokiaTest.run(this);
    }

    public void pauseApp() {}
    public void destroyApp(boolean u) {}
}

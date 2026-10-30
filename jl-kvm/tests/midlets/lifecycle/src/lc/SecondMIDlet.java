package lc;

import javax.microedition.midlet.MIDlet;

/** MIDlet-2 of the suite: selected with --midlet 2. */
public class SecondMIDlet extends MIDlet {
    protected void startApp() {
        System.out.println("> second startApp");
        notifyDestroyed();
    }
    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

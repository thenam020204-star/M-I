package javax.microedition.midlet;

/** TEMPORARY launcher run by the VM as the main thread: creates the MIDlet and calls startApp. */
public class MIDletBoot {
    public static void run(String className) {
        try {
            MIDlet m = (MIDlet) Class.forName(className).newInstance();
            m.startApp();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

package javax.microedition.midlet;

/** TEMPORARY launcher run by the VM as the main thread: creates the MIDlet and calls startApp. */
public class MIDletBoot {
    /** Set by <clinit>: Vm.launch must initialise the class of the static method it starts (JVMS 5.5), T12. */
    static int booted = 1;

    public static void run(String className) {
        if (booted != 1) System.out.println("> BOOT CLINIT NOT RUN");
        try {
            MIDlet m = (MIDlet) Class.forName(className).newInstance();
            m.startApp();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

package javax.microedition.midlet;

/** Launcher for run_phoneme.sh: cldc_vm -classpath shim.jar:case.jar javax.microedition.midlet.ShimRun <MIDlet class>. Same package as MIDlet to reach startApp(). */
public class ShimRun {
    public static void main(String[] args) throws Exception {
        MIDlet m = (MIDlet) Class.forName(args[0]).newInstance();
        try { m.startApp(); }
        catch (Throwable t) { System.out.println("# shim: startApp threw " + t); t.printStackTrace(); }
        // like a device: the application lives until notifyDestroyed(); other threads keep the VM alive; with none left the VM exits.
    }
}

package javax.microedition.midlet;

/**
 * Minimal MIDP 2.0 MIDlet for running CLDC-only vmspec cases on a bare phoneME CLDC VM (no MIDP). Own code (T05b), written from the
 * MIDP 2.0 javadoc: the lifecycle methods, notifyDestroyed(), getAppProperty() (reads nothing: always null), platformRequest (false).
 */
public abstract class MIDlet {
    protected MIDlet() { }
    protected abstract void startApp() throws MIDletStateChangeException;
    protected abstract void pauseApp();
    protected abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;
    public final void notifyDestroyed() { System.out.println("# shim: notifyDestroyed"); System.out.flush(); Runtime.getRuntime().exit(0); }
    public final void notifyPaused() { }
    public final void resumeRequest() { }
    public final String getAppProperty(String key) { return null; }
    public final boolean platformRequest(String url) { return false; }
    public final int checkPermission(String permission) { return -1; }
}

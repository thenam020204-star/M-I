package javax.microedition.midlet;

/** TEMPORARY MIDlet (T21 writes the real AMS). */
public abstract class MIDlet {
    protected MIDlet() { }
    protected abstract void startApp() throws MIDletStateChangeException;
    protected abstract void pauseApp();
    protected abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;
    public final void notifyDestroyed() { Runtime.getRuntime().exit(0); }
    public final void notifyPaused() { }
    public final void resumeRequest() { }
    private static native String getAppProperty0(String key);
    public final String getAppProperty(String key) { return getAppProperty0(key); }
}

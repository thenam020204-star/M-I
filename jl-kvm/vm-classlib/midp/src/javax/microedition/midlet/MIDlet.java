/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.midlet;

import javax.microedition.io.ConnectionNotFoundException;
import jlkvm.internal.Ams;

/** The MIDlet life cycle is driven by jlkvm.internal.Ams (T21); the AMS reaches the protected callbacks through {@link Bridge}. */
public abstract class MIDlet {
    protected MIDlet() {
        Ams.attach(this, new Bridge(this));
    }

    protected abstract void startApp() throws MIDletStateChangeException;

    protected abstract void pauseApp();

    protected abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;

    public final void notifyDestroyed() {
        Ams.notifyDestroyed(this);
    }

    public final void notifyPaused() {
        Ams.notifyPaused(this);
    }

    public final void resumeRequest() {
        Ams.resumeRequest(this);
    }

    public final String getAppProperty(String key) {
        if (key == null) throw new NullPointerException();
        return Ams.property(key);
    }

    public final boolean platformRequest(String url) throws ConnectionNotFoundException {
        if (url == null) throw new NullPointerException();
        return Ams.platformRequest(url);
    }

    public final int checkPermission(String permission) {
        return Ams.checkPermission(permission);
    }
}

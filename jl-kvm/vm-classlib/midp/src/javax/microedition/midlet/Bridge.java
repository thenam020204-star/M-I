/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.midlet;

import jlkvm.internal.MidletHooks;

/** Package-private (not API): lets the AMS (another package) call the protected life-cycle methods of a MIDlet. */
final class Bridge implements MidletHooks {
    private final MIDlet midlet;

    Bridge(MIDlet midlet) {
        this.midlet = midlet;
    }

    public void startApp() throws Exception {
        midlet.startApp();
    }

    public void pauseApp() {
        midlet.pauseApp();
    }

    public void destroyApp(boolean unconditional) throws Exception {
        midlet.destroyApp(unconditional);
    }
}

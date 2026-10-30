/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package jlkvm.internal;

/** ROM-private: the life-cycle callbacks of the running MIDlet, implemented by javax.microedition.midlet.Bridge. */
public interface MidletHooks {
    void startApp() throws Exception;

    void pauseApp();

    void destroyApp(boolean unconditional) throws Exception;
}

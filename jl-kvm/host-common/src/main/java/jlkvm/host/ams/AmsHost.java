package jlkvm.host.ams;

import jlkvm.Vm;
import jlkvm.host.HostEvents;

/**
 * The host side of the application manager (PLAN T21): what an Android Activity (or any other host) calls to drive the MIDlet life
 * cycle. Every method is thread-safe and only queues an event ({@code Vm.postEvent}); the guest AMS (jlkvm.internal.Ams) runs
 * hideNotify + pauseApp / startApp + showNotify / destroyApp on the guest event thread (D13) - the host never calls guest code.
 * <pre>
 *   Activity.onPause()          -> ams.pause()            (also when the surface is lost / a dialog covers the game)
 *   Activity.onResume()         -> ams.resume()           (+ ams.repaint() when the surface was recreated)
 *   back key / "exit" menu      -> ams.destroy(false)     (the MIDlet may refuse with MIDletStateChangeException)
 *   Activity.onDestroy()        -> ams.destroy(true)
 *   surface size / rotation     -> ams.displaySize(w, h)
 * </pre>
 * host-desktop drives the same calls from scripts ({@code pause}, {@code resume}, {@code destroy} lines); the Android Activity wiring
 * is PLAN M6 (T35) and is not tested yet.
 */
public final class AmsHost {
    private final Vm vm;

    public AmsHost(Vm vm) { this.vm = vm; }

    public void pause() { vm.postEvent(HostEvents.PAUSE, 0, 0, 0); }
    public void resume() { vm.postEvent(HostEvents.RESUME, 0, 0, 0); }
    public void destroy(boolean unconditional) { vm.postEvent(HostEvents.DESTROY, unconditional ? 1 : 0, 0, 0); }
    public void repaint() { vm.postEvent(HostEvents.REPAINT_ALL, 0, 0, 0); }
    public void displaySize(int w, int h) { vm.postEvent(HostEvents.DISPLAY_SIZE, w, h, 0); }
    public void keyPressed(int code) { vm.postEvent(HostEvents.KEY_PRESSED, code, 0, 0); }
    public void keyReleased(int code) { vm.postEvent(HostEvents.KEY_RELEASED, code, 0, 0); }
    public void keyRepeated(int code) { vm.postEvent(HostEvents.KEY_REPEATED, code, 0, 0); }
    public void pointer(int type, int x, int y) { vm.postEvent(type, x, y, 0); }
}

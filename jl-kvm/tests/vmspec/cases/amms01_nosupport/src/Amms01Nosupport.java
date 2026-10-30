package vmspec;

import javax.microedition.amms.GlobalManager;
import javax.microedition.midlet.MIDlet;

/** T32 (JSR-234): a device with the AMMS API but none of its services: the factories refuse with MediaException, the lookups are null / empty. */
public class Amms01Nosupport extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    protected void startApp() {
        try { p("getSpectator: " + GlobalManager.getSpectator()); } catch (Throwable t) { p("getSpectator: " + cn(t)); }
        try { p("createSoundSource3D: " + GlobalManager.createSoundSource3D()); } catch (Throwable t) { p("createSoundSource3D: " + cn(t)); }
        try { p("createEffectModule: " + GlobalManager.createEffectModule()); } catch (Throwable t) { p("createEffectModule: " + cn(t)); }
        try { p("createMediaProcessor: " + GlobalManager.createMediaProcessor("audio/mpeg")); } catch (Throwable t) { p("createMediaProcessor: " + cn(t)); }
        try { p("createMediaProcessor(null): " + GlobalManager.createMediaProcessor(null)); } catch (Throwable t) { p("createMediaProcessor(null): " + cn(t)); }
        p("3D player types: " + GlobalManager.getSupportedSoundSource3DPlayerTypes().length);
        p("processor input types: " + GlobalManager.getSupportedMediaProcessorInputTypes().length);
        p("getControls: " + GlobalManager.getControls().length);
        p("getControl: " + GlobalManager.getControl("javax.microedition.amms.control.camera.ZoomControl"));
        try { GlobalManager.getControl(null); p("getControl(null): ok"); } catch (Throwable t) { p("getControl(null): " + cn(t)); }
        String[] names = {"javax.microedition.amms.Spectator", "javax.microedition.amms.control.camera.ZoomControl", "javax.microedition.amms.control.audio3d.LocationControl",
            "javax.microedition.amms.control.tuner.TunerControl", "javax.microedition.amms.MediaProcessor"};
        for (int i = 0; i < names.length; i++) {
            try { p(names[i].substring(names[i].lastIndexOf('.') + 1) + " interface: " + Class.forName(names[i]).isInterface()); } catch (Throwable t) { p(names[i] + ": " + cn(t)); }
        }
        p("MediaProcessor states: " + javax.microedition.amms.MediaProcessor.UNREALIZED + " " + javax.microedition.amms.MediaProcessor.REALIZED + " "
                + javax.microedition.amms.MediaProcessor.STOPPED + " " + javax.microedition.amms.MediaProcessor.STARTED + " " + javax.microedition.amms.MediaProcessor.UNKNOWN);
        notifyDestroyed();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}

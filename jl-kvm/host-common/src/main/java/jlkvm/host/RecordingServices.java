package jlkvm.host;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jlkvm.host.ui.Surface;
import jlkvm.host.ui.SoftRaster;

/** Headless {@link PlatformServices}: records everything, keeps the last frame (tests, host-desktop). */
public class RecordingServices implements PlatformServices {
    public final List<String> platformRequests = Collections.synchronizedList(new ArrayList<String>());
    public final List<String> vibrations = Collections.synchronizedList(new ArrayList<String>());
    /** Hash (SoftRaster.hash) of every presented frame, in order. */
    public final List<Long> frameHashes = Collections.synchronizedList(new ArrayList<Long>());
    public volatile int frames;
    public volatile int[] lastFrame;
    public volatile int lastW, lastH;
    /** URL schemes platformRequest accepts (others: unsupported); a "tel:" request normally needs no exit. */
    public String[] schemes = {"http", "https", "tel", "mailto", "sms"};
    public boolean exitBeforeRequest = false;

    public int platformRequest(String url) {
        platformRequests.add(url);
        if (url.length() == 0) return 0;   // MIDP 2.0: an empty string cancels a pending request
        int c = url.indexOf(':');
        if (c <= 0) return -1;
        String s = url.substring(0, c).toLowerCase();
        for (String k : schemes) if (k.equals(s)) return exitBeforeRequest ? 1 : 0;
        return -1;
    }

    public int checkPermission(String p) {
        return p.startsWith("javax.microedition.") || p.startsWith("javax.wireless.") ? 1 : -1;
    }

    public boolean vibrate(int ms) { vibrations.add("vibrate " + ms); return true; }
    public boolean flashBacklight(int ms) { vibrations.add("backlight " + ms); return true; }

    public void present(Surface frame) {
        if (frame instanceof SoftRaster) {
            SoftRaster r = (SoftRaster) frame;
            lastFrame = r.px.clone();
            lastW = r.w;
            lastH = r.h;
            frameHashes.add(r.hash());
        }
        frames++;
    }
}

package jlkvm.host.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;

import jlkvm.host.PlatformServices;
import jlkvm.host.ui.SoftRaster;
import jlkvm.host.ui.Surface;

/**
 * {@link PlatformServices} of the Android host (PLAN T35, V12): frames go to the view through {@link FrameSink} (the JL-Mod canvas /
 * GLES / skin / virtual keyboard stack draws them), platformRequest starts an Intent, vibration uses the system Vibrator.
 * Called on the vm-thread; everything it does with the UI is posted by the sink.
 */
public final class AndroidServices implements PlatformServices {
    /** Receives the complete screen image after every paint cycle (ARGB, row-major); may be called from the vm-thread. */
    public interface FrameSink {
        void frame(int[] argb, int w, int h);
    }

    /** Lets the host app decide for MIDlet.checkPermission: 1 allowed, 0 denied, -1 unknown. */
    public interface Permissions {
        int check(String permission);
    }

    private final Context context;
    private final FrameSink sink;
    private volatile boolean vibration = true;
    private volatile Permissions permissions;

    public AndroidServices(Context context, FrameSink sink) {
        this.context = context.getApplicationContext();
        this.sink = sink;
    }

    public void setVibration(boolean on) { vibration = on; }

    public void setPermissions(Permissions p) { permissions = p; }

    @Override
    public int platformRequest(String url) {
        if (url == null || url.length() == 0) return 0;   // MIDP 2.0: an empty string cancels a pending request
        try {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            if (scheme == null) return -1;
            Intent i = new Intent(scheme.equals("tel") ? Intent.ACTION_DIAL : Intent.ACTION_VIEW, uri);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (i.resolveActivity(context.getPackageManager()) == null) return -1;
            context.startActivity(i);
            return 0;
        } catch (RuntimeException e) {
            return -1;
        }
    }

    @Override
    public int checkPermission(String permission) {
        Permissions p = permissions;
        if (p != null) return p.check(permission);
        return permission != null && (permission.startsWith("javax.microedition.") || permission.startsWith("javax.wireless.")) ? 1 : -1;
    }

    @Override
    public boolean vibrate(int millis) {
        if (!vibration) return false;
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (v == null || !v.hasVibrator()) return false;
        if (millis <= 0) v.cancel();
        else v.vibrate(millis);
        return true;
    }

    @Override
    public boolean flashBacklight(int millis) {
        return false;   // the screen backlight is the user's; the Activity keeps it on by the existing preference
    }

    private int[] copy;

    @Override
    public void present(Surface frame) {
        if (!(frame instanceof SoftRaster)) return;
        SoftRaster r = (SoftRaster) frame;
        int n = r.w * r.h;
        int[] c = copy;
        if (c == null || c.length != n) copy = c = new int[n];
        System.arraycopy(r.px, 0, c, 0, n);
        sink.frame(c, r.w, r.h);
    }
}

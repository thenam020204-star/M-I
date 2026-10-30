package jlkvm.host.android;

import android.content.Context;
import android.os.Vibrator;

import jlkvm.host.nokia.NokiaDevice;

/** DeviceControl of Nokia UI (PLAN T28) on Android: the vibrator; lights are the user's screen (no-op, UNVERIFIED on a device). */
public final class AndroidNokiaDevice implements NokiaDevice {
    private final Vibrator vibrator;
    private volatile boolean enabled = true;

    public AndroidNokiaDevice(Context context) {
        vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void setVibration(boolean on) { enabled = on; }

    @Override public boolean hasVibra() { return enabled && vibrator != null && vibrator.hasVibrator(); }

    @Override public void startVibra(int freq, long millis) {
        if (hasVibra()) vibrator.vibrate(Math.max(1, millis));
    }

    @Override public void stopVibra() {
        if (vibrator != null) vibrator.cancel();
    }

    @Override public void setLights(int num, int level) { }

    @Override public void flashLights(long millis) { }
}

package jlkvm.host.nokia;

/** The device features behind com.nokia.mid.ui.DeviceControl (PLAN T28). Called on the vm-thread only. */
public interface NokiaDevice {
    /** Whether the device has a vibrator (DeviceControl.startVibra throws IllegalStateException when not). */
    boolean hasVibra();

    /** Vibrates; {@code freq} 1..100, {@code millis} >= 0. A running vibration is replaced. */
    void startVibra(int freq, long millis);

    void stopVibra();

    /** Light {@code num} (0 = screen backlight) to {@code level} 0..100. */
    void setLights(int num, int level);

    /** Flashes the lights for {@code millis}; 0 stops the effect. */
    void flashLights(long millis);
}

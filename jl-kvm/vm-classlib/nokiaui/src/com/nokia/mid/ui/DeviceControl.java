/*
 * jl-kvm guest class library (Nokia UI layer, API per tools/api-sigs/db/s40dp2-nokia-ui.json). Origin: original code written for jl-kvm
 * (PLAN D12, T28); behaviour per the Nokia UI API 1.1 javadoc (S40 DP2.0 SDK 1.1), open points UNVERIFIED (docs/nokia.md). License: Apache-2.0.
 */
package com.nokia.mid.ui;

import jlkvm.internal.nokia.Host;

public class DeviceControl {
    private DeviceControl() {
    }

    /** Light 0 = screen backlight; level 0..100. */
    public static void setLights(int num, int level) {
        if (num != 0) throw new IllegalArgumentException("light " + num);
        if (level < 0 || level > 100) throw new IllegalArgumentException("level " + level);
        Host.op0(3, num, level);
    }

    public static void flashLights(long duration) {
        if (duration < 0) throw new IllegalArgumentException("duration " + duration);
        Host.op0(4, 0, duration);
    }

    /** freq 0 only asks whether the device vibrates (IllegalStateException when it does not). */
    public static void startVibra(int freq, long duration) {
        if (freq < 0 || freq > 100) throw new IllegalArgumentException("freq " + freq);
        if (duration < 0) throw new IllegalArgumentException("duration " + duration);
        if (Host.op0(0, 0, 0L) == 0) throw new IllegalStateException("no vibration support");
        if (freq == 0) return;
        Host.op0(1, freq, duration);
    }

    public static void stopVibra() {
        Host.op0(2, 0, 0L);
    }
}

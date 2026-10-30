/*
 * jl-kvm guest class library (Nokia UI layer, API per tools/api-sigs/db/s40dp2-nokia-ui.json). Origin: original code written for jl-kvm
 * (PLAN D12, T28); behaviour per the Nokia UI API 1.1 javadoc (S40 DP2.0 SDK 1.1), open points UNVERIFIED (docs/nokia.md). License: Apache-2.0.
 */
package jlkvm.internal.nokia;

/** ROM-private: profile data and device features of the Nokia UI layer (host: NokiaNatives). */
public final class Host {
    private Host() {
    }

    /** 0 native pixel format, 1 bit mask of the sound formats (bit n = format n), 2 concurrent tones, 3 concurrent WAV. */
    public static native int config0(int what);

    /** 0 hasVibra (1/0), 1 startVibra(freq a, ms b), 2 stopVibra, 3 setLights(num a, level b), 4 flashLights(ms b). */
    public static native int op0(int op, int a, long b);
}

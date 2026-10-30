/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.media.control;

import javax.microedition.media.Control;

public interface VolumeControl extends Control {
    void setMute(boolean mute);

    boolean isMuted();

    int setLevel(int level);

    int getLevel();
}

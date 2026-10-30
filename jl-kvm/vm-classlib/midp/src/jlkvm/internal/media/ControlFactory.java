/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal.media;

import java.util.Vector;

/** Implemented by the JSR-135 layer (rom-jsr135.jar, profile api "jsr-135"): adds MIDIControl / TempoControl / RateControl to a player. */
public interface ControlFactory {
    /** Appends the extra controls to {@code controls}; their interface names ("javax.microedition.media.control.X") to {@code names}. */
    void addControls(BasePlayer player, Vector controls, Vector names);
}

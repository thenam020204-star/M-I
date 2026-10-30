/*
 * jl-kvm guest class library (JSR-135 layer, profile api "jsr-135"). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 * API signatures: tools/api-sigs/db/jsr-135-extra.json (extracted from the JL-Mod classes, Apache-2.0; docs/midp.md section 10).
 */
package jlkvm.internal.media.ext;

import java.util.Vector;
import jlkvm.internal.media.BasePlayer;
import jlkvm.internal.media.ControlFactory;

/** Found by name ({@code Class.forName}) by BasePlayer when the profile declares "jsr-135": gives tone / RTTTL / MIDI players their MIDIControl and TempoControl. */
public final class Jsr135Controls implements ControlFactory {
    public Jsr135Controls() {
    }

    public void addControls(BasePlayer p, Vector controls, Vector names) {
        Tempo t = new Tempo(p);
        controls.addElement(t);
        names.addElement(BasePlayer.CONTROL_PKG + "TempoControl");
        controls.addElement(t);
        names.addElement(BasePlayer.CONTROL_PKG + "RateControl");
        controls.addElement(new Midi());
        names.addElement(BasePlayer.CONTROL_PKG + "MIDIControl");
    }
}

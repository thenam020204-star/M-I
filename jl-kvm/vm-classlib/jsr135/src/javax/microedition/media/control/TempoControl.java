/*
 * jl-kvm guest class library (JSR-135 layer, profile api "jsr-135"). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 * API signatures: tools/api-sigs/db/jsr-135-extra.json (extracted from the JL-Mod classes, Apache-2.0; docs/midp.md section 10).
 */
package javax.microedition.media.control;

public interface TempoControl extends RateControl {
    int setTempo(int millitempo);

    int getTempo();
}

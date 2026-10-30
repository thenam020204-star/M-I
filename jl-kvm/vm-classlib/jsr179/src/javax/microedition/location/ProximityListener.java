/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.location;

public interface ProximityListener {
    void monitoringStateChanged(boolean a0);
    void proximityEvent(javax.microedition.location.Coordinates a0, javax.microedition.location.Location a1);
}

/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.audio3d;

public interface DistanceAttenuationControl extends javax.microedition.media.Control {
    int getMaxDistance();
    int getMinDistance();
    boolean getMuteAfterMax();
    int getRolloffFactor();
    void setParameters(int a0, int a1, boolean a2, int a3);
}

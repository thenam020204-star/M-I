/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.audio3d;

public interface LocationControl extends javax.microedition.media.Control {
    int[] getCartesian();
    void setCartesian(int a0, int a1, int a2);
    void setSpherical(int a0, int a1, int a2);
}

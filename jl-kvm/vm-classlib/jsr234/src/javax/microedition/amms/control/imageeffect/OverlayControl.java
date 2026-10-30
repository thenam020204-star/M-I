/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.imageeffect;

public interface OverlayControl extends javax.microedition.amms.control.EffectControl {
    void clear();
    Object getImage(int a0) throws IllegalArgumentException;
    int insertImage(Object a0, int a1, int a2, int a3) throws IllegalArgumentException;
    int insertImage(Object a0, int a1, int a2, int a3, int a4) throws IllegalArgumentException;
    int numberOfImages();
    void removeImage(Object a0) throws IllegalArgumentException;
}

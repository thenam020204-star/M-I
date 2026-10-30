/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.audioeffect;

public interface ReverbControl extends javax.microedition.amms.control.EffectControl {
    int getReverbLevel();
    int getReverbTime() throws javax.microedition.media.MediaException;
    int setReverbLevel(int a0) throws IllegalArgumentException;
    void setReverbTime(int a0) throws IllegalArgumentException, javax.microedition.media.MediaException;
}

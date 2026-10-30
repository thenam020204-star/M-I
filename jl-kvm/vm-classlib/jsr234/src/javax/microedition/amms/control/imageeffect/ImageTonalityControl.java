/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.imageeffect;

public interface ImageTonalityControl extends javax.microedition.amms.control.EffectControl {
    int AUTO = -1000;
    int NEXT = -1001;
    int PREVIOUS = -1002;
    int getBrightness();
    int getBrightnessLevels();
    int getContrast();
    int getContrastLevels();
    int getGamma();
    int getGammaLevels();
    int setBrightness(int a0) throws IllegalArgumentException;
    int setContrast(int a0) throws IllegalArgumentException;
    int setGamma(int a0) throws IllegalArgumentException;
}

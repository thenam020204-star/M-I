/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.audioeffect;

public interface EqualizerControl extends javax.microedition.amms.control.EffectControl {
    int UNDEFINED = -1004;
    int getBand(int a0);
    int getBandLevel(int a0) throws IllegalArgumentException;
    int getBass();
    int getCenterFreq(int a0) throws IllegalArgumentException;
    int getMaxBandLevel();
    int getMinBandLevel();
    int getNumberOfBands();
    int getTreble();
    void setBandLevel(int a0, int a1) throws IllegalArgumentException;
    int setBass(int a0) throws IllegalArgumentException;
    int setTreble(int a0) throws IllegalArgumentException;
}

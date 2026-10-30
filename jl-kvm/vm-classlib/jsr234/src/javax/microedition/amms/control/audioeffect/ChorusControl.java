/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.audioeffect;

public interface ChorusControl extends javax.microedition.amms.control.EffectControl {
    int getAverageDelay();
    int getMaxAverageDelay();
    int getMaxModulationDepth();
    int getMaxModulationRate();
    int getMinModulationRate();
    int getModulationDepth();
    int getModulationRate();
    int getWetLevel();
    void setAverageDelay(int a0) throws IllegalArgumentException;
    void setModulationDepth(int a0) throws IllegalArgumentException;
    void setModulationRate(int a0) throws IllegalArgumentException;
    int setWetLevel(int a0) throws IllegalArgumentException;
}

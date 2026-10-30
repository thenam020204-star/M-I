/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.tuner;

public interface TunerControl extends javax.microedition.media.Control {
    int AUTO = 3;
    String MODULATION_AM = "am";
    String MODULATION_FM = "fm";
    int MONO = 1;
    int STEREO = 2;
    int getFrequency();
    int getMaxFreq(String a0) throws IllegalArgumentException;
    int getMinFreq(String a0) throws IllegalArgumentException;
    String getModulation();
    int getNumberOfPresets();
    int getPresetFrequency(int a0) throws IllegalArgumentException;
    String getPresetModulation(int a0) throws IllegalArgumentException;
    String getPresetName(int a0) throws IllegalArgumentException;
    int getPresetStereoMode(int a0) throws javax.microedition.media.MediaException;
    int getSignalStrength() throws javax.microedition.media.MediaException;
    boolean getSquelch();
    int getStereoMode();
    int seek(int a0, String a1, boolean a2) throws javax.microedition.media.MediaException;
    int setFrequency(int a0, String a1) throws IllegalArgumentException;
    void setPreset(int a0) throws IllegalArgumentException;
    void setPreset(int a0, int a1, String a2, int a3) throws IllegalArgumentException;
    void setPresetName(int a0, String a1) throws IllegalArgumentException;
    void setSquelch(boolean a0) throws javax.microedition.media.MediaException;
    void setStereoMode(int a0) throws IllegalArgumentException;
    void usePreset(int a0) throws IllegalArgumentException;
}

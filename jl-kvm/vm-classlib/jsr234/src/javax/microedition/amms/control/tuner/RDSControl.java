/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.tuner;

public interface RDSControl extends javax.microedition.media.Control {
    String RADIO_CHANGED = "radio_changed";
    String RDS_NEW_ALARM = "RDS_ALARM";
    String RDS_NEW_DATA = "RDS_NEW_DATA";
    boolean getAutomaticSwitching();
    boolean getAutomaticTA();
    java.util.Date getCT();
    int[] getFreqsByPTY(short a0);
    int[][] getFreqsByTA(boolean a0);
    short getPI();
    String getPS();
    String[] getPSByPTY(short a0);
    String[] getPSByTA(boolean a0);
    short getPTY();
    String getPTYString(boolean a0);
    String getRT();
    boolean getTA();
    boolean getTP();
    boolean isRDSSignal();
    void setAutomaticSwitching(boolean a0) throws javax.microedition.media.MediaException;
    void setAutomaticTA(boolean a0) throws javax.microedition.media.MediaException;
}

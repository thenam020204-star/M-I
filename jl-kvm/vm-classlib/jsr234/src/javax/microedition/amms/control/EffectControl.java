/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control;

public interface EffectControl extends javax.microedition.media.Control {
    int SCOPE_LIVE_AND_RECORD = 3;
    int SCOPE_LIVE_ONLY = 1;
    int SCOPE_RECORD_ONLY = 2;
    String getPreset();
    String[] getPresetNames();
    int getScope();
    boolean isEnabled();
    boolean isEnforced();
    void setEnabled(boolean a0);
    void setEnforced(boolean a0);
    void setPreset(String a0);
    void setScope(int a0) throws javax.microedition.media.MediaException;
}

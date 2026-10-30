/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.camera;

public interface FocusControl extends javax.microedition.media.Control {
    int AUTO = -1000;
    int AUTO_LOCK = -1005;
    int NEXT = -1001;
    int PREVIOUS = -1002;
    int UNKNOWN = -1004;
    int getFocus();
    int getFocusSteps();
    boolean getMacro();
    int getMinFocus();
    boolean isAutoFocusSupported();
    boolean isMacroSupported();
    boolean isManualFocusSupported();
    int setFocus(int a0) throws javax.microedition.media.MediaException;
    void setMacro(boolean a0) throws javax.microedition.media.MediaException;
}

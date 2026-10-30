/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.camera;

public interface FlashControl extends javax.microedition.media.Control {
    int AUTO = 2;
    int AUTO_WITH_REDEYEREDUCE = 3;
    int FILLIN = 6;
    int FORCE = 4;
    int FORCE_WITH_REDEYEREDUCE = 5;
    int OFF = 1;
    int getMode();
    int[] getSupportedModes();
    boolean isFlashReady();
    void setMode(int a0) throws IllegalArgumentException;
}

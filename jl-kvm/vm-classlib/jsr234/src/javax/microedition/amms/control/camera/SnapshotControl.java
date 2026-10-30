/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.camera;

public interface SnapshotControl extends javax.microedition.media.Control {
    int FREEZE = -2;
    int FREEZE_AND_CONFIRM = -1;
    String SHOOTING_STOPPED = "SHOOTING_STOPPED";
    String STORAGE_ERROR = "STORAGE_ERROR";
    String WAITING_UNFREEZE = "WAITING_UNFREEZE";
    String getDirectory();
    String getFilePrefix();
    String getFileSuffix();
    void setDirectory(String a0) throws IllegalArgumentException, SecurityException;
    void setFilePrefix(String a0) throws IllegalArgumentException;
    void setFileSuffix(String a0) throws IllegalArgumentException;
    void start(int a0) throws SecurityException;
    void stop();
    void unfreeze(boolean a0);
}

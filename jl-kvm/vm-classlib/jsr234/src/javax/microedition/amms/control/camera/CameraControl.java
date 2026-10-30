/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.camera;

public interface CameraControl extends javax.microedition.media.Control {
    int ROTATE_LEFT = 2;
    int ROTATE_NONE = 1;
    int ROTATE_RIGHT = 3;
    int UNKNOWN = -1004;
    void enableShutterFeedback(boolean a0) throws javax.microedition.media.MediaException;
    int getCameraRotation();
    String getExposureMode();
    int getStillResolution();
    String[] getSupportedExposureModes();
    int[] getSupportedStillResolutions();
    int[] getSupportedVideoResolutions();
    int getVideoResolution();
    boolean isShutterFeedbackEnabled();
    void setExposureMode(String a0) throws IllegalArgumentException;
    void setStillResolution(int a0) throws IllegalArgumentException;
    void setVideoResolution(int a0) throws IllegalArgumentException, IllegalStateException;
}

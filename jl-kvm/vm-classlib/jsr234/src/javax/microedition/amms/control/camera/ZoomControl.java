/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.camera;

public interface ZoomControl extends javax.microedition.media.Control {
    int NEXT = -1001;
    int PREVIOUS = -1002;
    int UNKNOWN = -1004;
    int getDigitalZoom();
    int getDigitalZoomLevels();
    int getMaxDigitalZoom();
    int getMaxOpticalZoom();
    int getMinFocalLength();
    int getOpticalZoom();
    int getOpticalZoomLevels();
    int setDigitalZoom(int a0) throws IllegalArgumentException;
    int setOpticalZoom(int a0) throws IllegalArgumentException;
}

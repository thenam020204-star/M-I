/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control.camera;

public interface ExposureControl extends javax.microedition.media.Control {
    int getExposureCompensation();
    int getExposureTime();
    int getExposureValue();
    int getFStop();
    int getISO();
    String getLightMetering();
    int getMaxExposureTime();
    int getMinExposureTime();
    int[] getSupportedExposureCompensations();
    int[] getSupportedFStops();
    int[] getSupportedISOs();
    String[] getSupportedLightMeterings();
    void setExposureCompensation(int a0) throws javax.microedition.media.MediaException;
    int setExposureTime(int a0) throws javax.microedition.media.MediaException;
    void setFStop(int a0) throws javax.microedition.media.MediaException;
    void setISO(int a0) throws javax.microedition.media.MediaException;
    void setLightMetering(String a0) throws IllegalArgumentException;
}

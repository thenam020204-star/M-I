/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms.control;

public interface FormatControl extends javax.microedition.media.Control {
    int METADATA_NOT_SUPPORTED = 0;
    int METADATA_SUPPORTED_FIXED_KEYS = 1;
    int METADATA_SUPPORTED_FREE_KEYS = 2;
    String PARAM_BITRATE = "bitrate";
    String PARAM_BITRATE_TYPE = "bitrate ype";
    String PARAM_FRAMERATE = "frame rate";
    String PARAM_QUALITY = "quality";
    String PARAM_SAMPLERATE = "sample rate";
    String PARAM_VERSION_TYPE = "version type";
    int getEstimatedBitRate() throws javax.microedition.media.MediaException;
    String getFormat();
    int getIntParameterValue(String a0);
    boolean getMetadataOverride();
    int getMetadataSupportMode();
    String getStrParameterValue(String a0);
    String[] getSupportedFormats();
    int[] getSupportedIntParameterRange(String a0);
    String[] getSupportedIntParameters();
    String[] getSupportedMetadataKeys();
    String[] getSupportedStrParameterValues(String a0);
    String[] getSupportedStrParameters();
    void setFormat(String a0);
    void setMetadata(String a0, String a1) throws javax.microedition.media.MediaException;
    void setMetadataOverride(boolean a0);
    int setParameter(String a0, int a1);
    void setParameter(String a0, String a1);
}

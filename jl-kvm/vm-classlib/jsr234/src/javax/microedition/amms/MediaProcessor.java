/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms;

public interface MediaProcessor extends javax.microedition.media.Controllable {
    int REALIZED = 200;
    int STARTED = 400;
    int STOPPED = 300;
    int UNKNOWN = -1;
    int UNREALIZED = 100;
    void abort();
    void addMediaProcessorListener(javax.microedition.amms.MediaProcessorListener a0);
    void complete() throws javax.microedition.media.MediaException;
    int getProgress();
    int getState();
    void removeMediaProcessorListener(javax.microedition.amms.MediaProcessorListener a0);
    void setInput(java.io.InputStream a0, int a1) throws javax.microedition.media.MediaException;
    void setInput(Object a0) throws javax.microedition.media.MediaException;
    void setOutput(java.io.OutputStream a0);
    void start() throws javax.microedition.media.MediaException;
    void stop() throws javax.microedition.media.MediaException;
}

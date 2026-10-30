/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms;

public interface MediaProcessorListener {
    String PROCESSING_ABORTED = "processingAborted";
    String PROCESSING_COMPLETED = "processingCompleted";
    String PROCESSING_ERROR = "processingError";
    String PROCESSING_STARTED = "processingStarted";
    String PROCESSING_STOPPED = "processingStopped";
    String PROCESSOR_REALIZED = "processRealized";
    void mediaProcessorUpdate(javax.microedition.amms.MediaProcessor a0, String a1, Object a2);
}

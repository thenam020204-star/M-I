/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.rms;

/** MIDP 2.0: bidirectional record iterator. */
public interface RecordEnumeration {
    int numRecords();

    byte[] nextRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException;

    int nextRecordId() throws InvalidRecordIDException;

    byte[] previousRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException;

    int previousRecordId() throws InvalidRecordIDException;

    boolean hasNextElement();

    boolean hasPreviousElement();

    void reset();

    void rebuild();

    void keepUpdated(boolean keepUpdated);

    boolean isKeptUpdated();

    void destroy();
}

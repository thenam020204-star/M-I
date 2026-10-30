/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.rms;

/** MIDP 2.0: orders the records of an enumeration. */
public interface RecordComparator {
    int EQUIVALENT = 0;
    int FOLLOWS = 1;
    int PRECEDES = -1;

    int compare(byte[] rec1, byte[] rec2);
}

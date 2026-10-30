/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.pim;

public interface ToDo extends PIMItem {
    int CLASS = 100;
    int CLASS_CONFIDENTIAL = 200;
    int CLASS_PRIVATE = 201;
    int CLASS_PUBLIC = 202;
    int COMPLETED = 101;
    int COMPLETION_DATE = 102;
    int DUE = 103;
    int NOTE = 104;
    int PRIORITY = 105;
    int REVISION = 106;
    int SUMMARY = 107;
    int UID = 108;
}

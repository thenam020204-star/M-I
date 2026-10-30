/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.pim;

public interface Event extends PIMItem {
    int ALARM = 100;
    int CLASS = 101;
    int CLASS_CONFIDENTIAL = 200;
    int CLASS_PRIVATE = 201;
    int CLASS_PUBLIC = 202;
    int END = 102;
    int LOCATION = 103;
    int NOTE = 104;
    int REVISION = 105;
    int START = 106;
    int SUMMARY = 107;
    int UID = 108;

    RepeatRule getRepeat();

    void setRepeat(RepeatRule a0);
}

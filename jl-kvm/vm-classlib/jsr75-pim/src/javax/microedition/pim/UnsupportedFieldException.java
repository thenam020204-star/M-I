/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.pim;

public class UnsupportedFieldException extends RuntimeException {
    private final int field;

    public UnsupportedFieldException() {
        super();
        field = -1;
    }

    public UnsupportedFieldException(String s) {
        super(s);
        field = -1;
    }

    public UnsupportedFieldException(String s, int field) {
        super(s);
        this.field = field;
    }

    public int getField() {
        return field;
    }
}

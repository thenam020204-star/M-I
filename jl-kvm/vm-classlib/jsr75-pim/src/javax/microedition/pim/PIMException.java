/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.pim;

public class PIMException extends Exception {
    public static final int FEATURE_NOT_SUPPORTED = 0;
    public static final int GENERAL_ERROR = 1;
    public static final int LIST_CLOSED = 2;
    public static final int LIST_NOT_ACCESSIBLE = 3;
    public static final int MAX_CATEGORIES_EXCEEDED = 4;
    public static final int UNSUPPORTED_VERSION = 5;
    public static final int UPDATE_ERROR = 6;

    private final int reason;

    public PIMException() {
        super();
        reason = GENERAL_ERROR;
    }

    public PIMException(String s) {
        super(s);
        reason = GENERAL_ERROR;
    }

    public PIMException(String s, int reason) {
        super(s);
        this.reason = reason;
    }

    public int getReason() {
        return reason;
    }
}

/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

/** The VM fills in {@code trace} (method id / pc pairs) in {@code fillInStackTrace} and formats it in {@code traceText}. */
public class Throwable {
    private String message;
    private int[] trace;

    public Throwable() {
        fillInStackTrace();
    }

    public Throwable(String message) {
        this.message = message;
        fillInStackTrace();
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        String n = getClass().getName();
        return message != null ? n + ": " + message : n;
    }

    private native void fillInStackTrace();

    private native String traceText();

    /** Prints this throwable and its backtrace to System.err. */
    public void printStackTrace() {
        System.err.println(toString());
        System.err.print(traceText());
    }
}

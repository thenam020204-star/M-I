/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

public final class Boolean {
    public static final Boolean TRUE = new Boolean(true);
    public static final Boolean FALSE = new Boolean(false);

    private final boolean value;

    public Boolean(boolean value) {
        this.value = value;
    }

    public boolean booleanValue() {
        return value;
    }

    public String toString() {
        return value ? "true" : "false";
    }

    public int hashCode() {
        return value ? 1231 : 1237;
    }

    public boolean equals(Object obj) {
        return obj instanceof Boolean && ((Boolean) obj).value == value;
    }
}

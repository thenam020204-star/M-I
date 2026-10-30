/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

public class ArrayIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public ArrayIndexOutOfBoundsException() {
        super();
    }

    public ArrayIndexOutOfBoundsException(String s) {
        super(s);
    }

    public ArrayIndexOutOfBoundsException(int index) {
        super("Array index out of range: " + index);
    }
}

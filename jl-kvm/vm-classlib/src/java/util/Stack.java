/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.util;

public class Stack extends Vector {
    public Stack() {
    }

    public Object push(Object item) {
        addElement(item);
        return item;
    }

    public synchronized Object pop() {
        int len = size();
        Object obj = peek();
        removeElementAt(len - 1);
        return obj;
    }

    public synchronized Object peek() {
        int len = size();
        if (len == 0) {
            throw new EmptyStackException();
        }
        return elementAt(len - 1);
    }

    public boolean empty() {
        return size() == 0;
    }

    public synchronized int search(Object o) {
        int i = lastIndexOf(o);
        if (i >= 0) {
            return size() - i;
        }
        return -1;
    }
}

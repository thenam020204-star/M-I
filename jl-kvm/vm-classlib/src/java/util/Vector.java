/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.util;

public class Vector {
    protected Object[] elementData;
    protected int elementCount;
    protected int capacityIncrement;

    public Vector(int initialCapacity, int capacityIncrement) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        this.elementData = new Object[initialCapacity];
        this.capacityIncrement = capacityIncrement;
    }

    public Vector(int initialCapacity) {
        this(initialCapacity, 0);
    }

    public Vector() {
        this(10);
    }

    public synchronized void copyInto(Object[] anArray) {
        System.arraycopy(elementData, 0, anArray, 0, elementCount);
    }

    public synchronized void trimToSize() {
        if (elementCount < elementData.length) {
            Object[] old = elementData;
            elementData = new Object[elementCount];
            System.arraycopy(old, 0, elementData, 0, elementCount);
        }
    }

    public synchronized void ensureCapacity(int minCapacity) {
        if (minCapacity > elementData.length) {
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        Object[] old = elementData;
        int newCapacity = capacityIncrement > 0 ? old.length + capacityIncrement : old.length * 2;
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }
        elementData = new Object[newCapacity];
        System.arraycopy(old, 0, elementData, 0, elementCount);
    }

    public synchronized void setSize(int newSize) {
        if (newSize > elementData.length) {
            grow(newSize);
        } else {
            for (int i = newSize; i < elementCount; i++) {
                elementData[i] = null;
            }
        }
        elementCount = newSize;
    }

    public int capacity() {
        return elementData.length;
    }

    public int size() {
        return elementCount;
    }

    public boolean isEmpty() {
        return elementCount == 0;
    }

    public Enumeration elements() {
        return new Enumeration() {
            int count = 0;

            public boolean hasMoreElements() {
                return count < elementCount;
            }

            public Object nextElement() {
                synchronized (Vector.this) {
                    if (count < elementCount) {
                        return elementData[count++];
                    }
                }
                throw new NoSuchElementException("Vector Enumeration");
            }
        };
    }

    public boolean contains(Object elem) {
        return indexOf(elem, 0) >= 0;
    }

    public int indexOf(Object elem) {
        return indexOf(elem, 0);
    }

    public synchronized int indexOf(Object elem, int index) {
        if (elem == null) {
            for (int i = index; i < elementCount; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = index; i < elementCount; i++) {
                if (elem.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(Object elem) {
        return lastIndexOf(elem, elementCount - 1);
    }

    public synchronized int lastIndexOf(Object elem, int index) {
        if (index >= elementCount) {
            throw new IndexOutOfBoundsException(index + " >= " + elementCount);
        }
        if (elem == null) {
            for (int i = index; i >= 0; i--) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = index; i >= 0; i--) {
                if (elem.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public synchronized Object elementAt(int index) {
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
        }
        return elementData[index];
    }

    public synchronized Object firstElement() {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        return elementData[0];
    }

    public synchronized Object lastElement() {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        return elementData[elementCount - 1];
    }

    public synchronized void setElementAt(Object obj, int index) {
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
        }
        elementData[index] = obj;
    }

    public synchronized void removeElementAt(int index) {
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
        } else if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        int j = elementCount - index - 1;
        if (j > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, j);
        }
        elementCount--;
        elementData[elementCount] = null;
    }

    public synchronized void insertElementAt(Object obj, int index) {
        if (index > elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " > " + elementCount);
        }
        if (elementCount + 1 > elementData.length) {
            grow(elementCount + 1);
        }
        System.arraycopy(elementData, index, elementData, index + 1, elementCount - index);
        elementData[index] = obj;
        elementCount++;
    }

    public synchronized void addElement(Object obj) {
        if (elementCount + 1 > elementData.length) {
            grow(elementCount + 1);
        }
        elementData[elementCount++] = obj;
    }

    public synchronized boolean removeElement(Object obj) {
        int i = indexOf(obj);
        if (i >= 0) {
            removeElementAt(i);
            return true;
        }
        return false;
    }

    public synchronized void removeAllElements() {
        for (int i = 0; i < elementCount; i++) {
            elementData[i] = null;
        }
        elementCount = 0;
    }

    public synchronized String toString() {
        int max = size() - 1;
        StringBuffer buf = new StringBuffer();
        Enumeration e = elements();
        buf.append("[");
        for (int i = 0; i <= max; i++) {
            String s = String.valueOf(e.nextElement());
            buf.append(s);
            if (i < max) {
                buf.append(", ");
            }
        }
        buf.append("]");
        return buf.toString();
    }
}

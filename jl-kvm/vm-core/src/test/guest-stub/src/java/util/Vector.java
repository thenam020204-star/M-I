package java.util;

public class Vector {
    protected Object[] elementData = new Object[10];
    protected int elementCount;
    public Vector() { }
    public synchronized void addElement(Object o) {
        if (elementCount == elementData.length) { Object[] n = new Object[elementCount * 2]; System.arraycopy(elementData, 0, n, 0, elementCount); elementData = n; }
        elementData[elementCount++] = o;
    }
    public synchronized Object elementAt(int i) { if (i >= elementCount) throw new ArrayIndexOutOfBoundsException(); return elementData[i]; }
    public synchronized Object firstElement() { if (elementCount == 0) throw new NoSuchElementException(); return elementData[0]; }
    public synchronized void removeElementAt(int i) {
        if (i < 0 || i >= elementCount) throw new ArrayIndexOutOfBoundsException();
        System.arraycopy(elementData, i + 1, elementData, i, elementCount - i - 1);
        elementData[--elementCount] = null;
    }
    public synchronized boolean removeElement(Object o) {
        for (int i = 0; i < elementCount; i++) if (elementData[i].equals(o)) { removeElementAt(i); return true; }
        return false;
    }
    public int size() { return elementCount; }
    public boolean isEmpty() { return elementCount == 0; }
}

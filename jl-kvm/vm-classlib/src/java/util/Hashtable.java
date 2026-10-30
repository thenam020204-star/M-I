/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.util;

/**
 * Chained hash table with the exact placement and enumeration order of the J2SE Hashtable (initial capacity 11, load factor 0.75,
 * new entries at the head of a bucket, enumeration from the last bucket down), so games that print or index by enumeration order
 * see what they see on devices and on J2SE oracles.
 */
public class Hashtable {
    private transient Entry[] table;
    private transient int count;
    private int threshold;

    private static class Entry {
        int hash;
        Object key;
        Object value;
        Entry next;
    }

    public Hashtable(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        if (initialCapacity == 0) {
            initialCapacity = 1;
        }
        table = new Entry[initialCapacity];
        threshold = (int) (initialCapacity * 3 / 4);
    }

    public Hashtable() {
        this(11);
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public synchronized Enumeration keys() {
        return new Enumerator(true);
    }

    public synchronized Enumeration elements() {
        return new Enumerator(false);
    }

    public synchronized boolean contains(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Entry[] tab = table;
        for (int i = tab.length; i-- > 0;) {
            for (Entry e = tab[i]; e != null; e = e.next) {
                if (e.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized boolean containsKey(Object key) {
        Entry[] tab = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public synchronized Object get(Object key) {
        Entry[] tab = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    protected void rehash() {
        int oldCapacity = table.length;
        Entry[] oldMap = table;
        int newCapacity = oldCapacity * 2 + 1;
        Entry[] newMap = new Entry[newCapacity];
        threshold = (int) (newCapacity * 3 / 4);
        table = newMap;
        for (int i = oldCapacity; i-- > 0;) {
            for (Entry old = oldMap[i]; old != null;) {
                Entry e = old;
                old = old.next;
                int index = (e.hash & 0x7FFFFFFF) % newCapacity;
                e.next = newMap[index];
                newMap[index] = e;
            }
        }
    }

    public synchronized Object put(Object key, Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Entry[] tab = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
                Object old = e.value;
                e.value = value;
                return old;
            }
        }
        if (count >= threshold) {
            rehash();
            tab = table;
            index = (hash & 0x7FFFFFFF) % tab.length;
        }
        Entry e = new Entry();
        e.hash = hash;
        e.key = key;
        e.value = value;
        e.next = tab[index];
        tab[index] = e;
        count++;
        return null;
    }

    public synchronized Object remove(Object key) {
        Entry[] tab = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }
                count--;
                Object oldValue = e.value;
                e.value = null;
                return oldValue;
            }
        }
        return null;
    }

    public synchronized void clear() {
        Entry[] tab = table;
        for (int index = tab.length; --index >= 0;) {
            tab[index] = null;
        }
        count = 0;
    }

    public synchronized String toString() {
        int max = size() - 1;
        StringBuffer buf = new StringBuffer();
        Enumeration k = keys();
        Enumeration e = elements();
        buf.append("{");
        for (int i = 0; i <= max; i++) {
            String s1 = k.nextElement().toString();
            String s2 = e.nextElement().toString();
            buf.append(s1 + "=" + s2);
            if (i < max) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }

    private class Enumerator implements Enumeration {
        private final boolean keys;
        private int index;
        private Entry[] tab;
        private Entry entry;

        Enumerator(boolean keys) {
            this.keys = keys;
            this.tab = table;
            this.index = tab.length;
        }

        public boolean hasMoreElements() {
            Entry e = entry;
            int i = index;
            Entry[] t = tab;
            while (e == null && i > 0) {
                e = t[--i];
            }
            entry = e;
            index = i;
            return e != null;
        }

        public Object nextElement() {
            Entry et = entry;
            int i = index;
            Entry[] t = tab;
            while (et == null && i > 0) {
                et = t[--i];
            }
            entry = et;
            index = i;
            if (et != null) {
                Entry e = entry;
                entry = e.next;
                return keys ? e.key : e.value;
            }
            throw new NoSuchElementException("Hashtable Enumerator");
        }
    }
}

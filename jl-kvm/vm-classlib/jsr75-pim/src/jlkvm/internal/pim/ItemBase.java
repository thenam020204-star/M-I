/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

import java.util.Vector;
import javax.microedition.pim.FieldFullException;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;
import javax.microedition.pim.UnsupportedFieldException;

/** An item of an in-memory list: per field a list of values, each with its attribute bits. Values: String, Integer, Long (date), Boolean, byte[], String[]. */
abstract class ItemBase implements PIMItem {
    final ListBase list;
    /** The entry of the list store this working copy stands for (null: not stored). */
    Object[] entry;
    /** field ids, parallel Vectors of Object[] {Integer attrs, Object value}. */
    private final Vector ids = new Vector();
    private final Vector vals = new Vector();
    private boolean modified;

    ItemBase(ListBase list) {
        this.list = list;
    }

    /** A copy for storing in the list (import / commit). */
    final void copyTo(ItemBase o) {
        for (int i = 0; i < ids.size(); i++) {
            Vector src = (Vector) vals.elementAt(i);
            Vector dst = new Vector();
            for (int k = 0; k < src.size(); k++) {
                Object[] v = (Object[]) src.elementAt(k);
                Object x = v[1];
                if (x instanceof byte[]) {
                    x = copy((byte[]) x);
                } else if (x instanceof String[]) {
                    x = copy((String[]) x);
                }
                dst.addElement(new Object[] {v[0], x});
            }
            o.ids.addElement(ids.elementAt(i));
            o.vals.addElement(dst);
        }
    }

    private static byte[] copy(byte[] a) {
        byte[] r = new byte[a.length];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    private static String[] copy(String[] a) {
        String[] r = new String[a.length];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }

    private static boolean same(Object x, Object w) {
        if (x instanceof String[] && w instanceof String[]) {
            String[] a = (String[]) x, b = (String[]) w;
            if (a.length != b.length) {
                return false;
            }
            for (int i = 0; i < a.length; i++) {
                if (a[i] == null ? b[i] != null : !a[i].equals(b[i])) {
                    return false;
                }
            }
            return true;
        }
        if (x instanceof byte[] && w instanceof byte[]) {
            byte[] a = (byte[]) x, b = (byte[]) w;
            if (a.length != b.length) {
                return false;
            }
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) {
                    return false;
                }
            }
            return true;
        }
        return x.equals(w);
    }

    private int row(int field, int type) {
        int r = Fields.row(list.type, field);
        if (r < 0) {
            throw new UnsupportedFieldException("Field not supported: " + field, field);
        }
        if (Fields.dataType(list.type, r) != type) {
            throw new IllegalArgumentException("Field " + field + " has another data type");
        }
        return r;
    }

    private Vector values(int field) {
        for (int i = 0; i < ids.size(); i++) {
            if (((Integer) ids.elementAt(i)).intValue() == field) {
                return (Vector) vals.elementAt(i);
            }
        }
        return null;
    }

    private Object[] get(int field, int index, int type) {
        row(field, type);
        Vector v = values(field);
        if (v == null || index < 0 || index >= v.size()) {
            throw new IndexOutOfBoundsException("No value " + index + " in field " + field);
        }
        return (Object[]) v.elementAt(index);
    }

    private void add(int field, int attrs, int type, Object value) {
        int r = row(field, type);
        Vector v = values(field);
        int max = Fields.maxValues(list.type, r);
        if (max >= 0 && v != null && v.size() >= max) {
            throw new FieldFullException("Field is full: " + field, field);
        }
        if (v == null) {
            v = new Vector();
            ids.addElement(new Integer(field));
            vals.addElement(v);
        }
        v.addElement(new Object[] {new Integer(attrs), value});
        modified = true;
    }

    private void set(int field, int index, int attrs, int type, Object value) {
        Object[] v = get(field, index, type);
        v[0] = new Integer(attrs);
        v[1] = value;
        modified = true;
    }

    public PIMList getPIMList() {
        return list;
    }

    public void commit() throws PIMException {
        list.commit(this);
        modified = false;
    }

    public boolean isModified() {
        return modified;
    }

    final void clean() {
        modified = false;
    }

    public int[] getFields() {
        int n = 0;
        for (int i = 0; i < ids.size(); i++) {
            if (((Vector) vals.elementAt(i)).size() > 0) {
                n++;
            }
        }
        int[] r = new int[n];
        n = 0;
        for (int i = 0; i < ids.size(); i++) {
            if (((Vector) vals.elementAt(i)).size() > 0) {
                r[n++] = ((Integer) ids.elementAt(i)).intValue();
            }
        }
        return r;
    }

    /** All string values (search by text). */
    final boolean anyStringContains(String text) {
        for (int i = 0; i < vals.size(); i++) {
            Vector v = (Vector) vals.elementAt(i);
            for (int k = 0; k < v.size(); k++) {
                Object x = ((Object[]) v.elementAt(k))[1];
                if (x instanceof String && ((String) x).toLowerCase().indexOf(text.toLowerCase()) >= 0) {
                    return true;
                }
                if (x instanceof String[]) {
                    String[] a = (String[]) x;
                    for (int j = 0; j < a.length; j++) {
                        if (a[j] != null && a[j].toLowerCase().indexOf(text.toLowerCase()) >= 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /** First value of a date or int field as a long, or Long.MIN_VALUE when the field is empty. */
    final long number(int field) {
        Vector v = values(field);
        if (v == null || v.size() == 0) {
            return Long.MIN_VALUE;
        }
        Object x = ((Object[]) v.elementAt(0))[1];
        return x instanceof Long ? ((Long) x).longValue() : x instanceof Integer ? ((Integer) x).intValue() : Long.MIN_VALUE;
    }

    /** True when every value of {@code m} (a template item) is also a value of this item. */
    final boolean matches(ItemBase m) {
        for (int i = 0; i < m.ids.size(); i++) {
            Vector mine = values(((Integer) m.ids.elementAt(i)).intValue());
            Vector want = (Vector) m.vals.elementAt(i);
            for (int k = 0; k < want.size(); k++) {
                Object w = ((Object[]) want.elementAt(k))[1];
                boolean found = false;
                for (int j = 0; mine != null && j < mine.size() && !found; j++) {
                    Object x = ((Object[]) mine.elementAt(j))[1];
                    found = same(x, w);
                }
                if (!found) {
                    return false;
                }
            }
        }
        return true;
    }

    public byte[] getBinary(int field, int index) {
        return copy((byte[]) get(field, index, BINARY)[1]);
    }

    private static byte[] slice(byte[] v, int off, int len) {
        if (v == null) {
            throw new NullPointerException("value");
        }
        if (off < 0 || len < 0 || off + len > v.length) {
            throw new IllegalArgumentException("offset / length outside the array");
        }
        byte[] r = new byte[len];
        System.arraycopy(v, off, r, 0, len);
        return r;
    }

    public void addBinary(int field, int attributes, byte[] value, int offset, int length) {
        add(field, attributes, BINARY, slice(value, offset, length));
    }

    public void setBinary(int field, int index, int attributes, byte[] value, int offset, int length) {
        set(field, index, attributes, BINARY, slice(value, offset, length));
    }

    public long getDate(int field, int index) {
        return ((Long) get(field, index, DATE)[1]).longValue();
    }

    public void addDate(int field, int attributes, long value) {
        add(field, attributes, DATE, new Long(value));
    }

    public void setDate(int field, int index, int attributes, long value) {
        set(field, index, attributes, DATE, new Long(value));
    }

    public int getInt(int field, int index) {
        return ((Integer) get(field, index, INT)[1]).intValue();
    }

    public void addInt(int field, int attributes, int value) {
        add(field, attributes, INT, new Integer(value));
    }

    public void setInt(int field, int index, int attributes, int value) {
        set(field, index, attributes, INT, new Integer(value));
    }

    public String getString(int field, int index) {
        return (String) get(field, index, STRING)[1];
    }

    public void addString(int field, int attributes, String value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        add(field, attributes, STRING, value);
    }

    public void setString(int field, int index, int attributes, String value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        set(field, index, attributes, STRING, value);
    }

    public boolean getBoolean(int field, int index) {
        return ((Boolean) get(field, index, BOOLEAN)[1]).booleanValue();
    }

    public void addBoolean(int field, int attributes, boolean value) {
        add(field, attributes, BOOLEAN, value ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setBoolean(int field, int index, int attributes, boolean value) {
        set(field, index, attributes, BOOLEAN, value ? Boolean.TRUE : Boolean.FALSE);
    }

    public String[] getStringArray(int field, int index) {
        return copy((String[]) get(field, index, STRING_ARRAY)[1]);
    }

    private String[] checkArray(int field, String[] value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        int r = row(field, STRING_ARRAY);
        if (value.length != Fields.arraySize(list.type, r)) {
            throw new IllegalArgumentException("String array must have " + Fields.arraySize(list.type, r) + " elements");
        }
        return copy(value);
    }

    public void addStringArray(int field, int attributes, String[] value) {
        add(field, attributes, STRING_ARRAY, checkArray(field, value));
    }

    public void setStringArray(int field, int index, int attributes, String[] value) {
        set(field, index, attributes, STRING_ARRAY, checkArray(field, value));
    }

    public int countValues(int field) {
        int r = Fields.row(list.type, field);
        if (r < 0) {
            throw new UnsupportedFieldException("Field not supported: " + field, field);
        }
        Vector v = values(field);
        return v == null ? 0 : v.size();
    }

    public void removeValue(int field, int index) {
        int r = Fields.row(list.type, field);
        if (r < 0) {
            throw new UnsupportedFieldException("Field not supported: " + field, field);
        }
        Vector v = values(field);
        if (v == null || index < 0 || index >= v.size()) {
            throw new IndexOutOfBoundsException("No value " + index + " in field " + field);
        }
        v.removeElementAt(index);
        modified = true;
    }

    public int getAttributes(int field, int index) {
        int r = Fields.row(list.type, field);
        if (r < 0) {
            throw new UnsupportedFieldException("Field not supported: " + field, field);
        }
        Vector v = values(field);
        if (v == null || index < 0 || index >= v.size()) {
            throw new IndexOutOfBoundsException("No value " + index + " in field " + field);
        }
        return ((Integer) ((Object[]) v.elementAt(index))[0]).intValue();
    }

    /** Categories are not supported (maxCategories() == 0). */
    public void addToCategory(String category) throws PIMException {
        if (category == null) {
            throw new NullPointerException("category");
        }
        throw new PIMException("Categories are not supported", PIMException.MAX_CATEGORIES_EXCEEDED);
    }

    public void removeFromCategory(String category) {
        if (category == null) {
            throw new NullPointerException("category");
        }
    }

    public String[] getCategories() {
        return new String[0];
    }

    public int maxCategories() {
        return 0;
    }
}

/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.bluetooth;

import java.util.Vector;

public class DataElement {
    public static final int NULL = 0;
    public static final int U_INT_1 = 8;
    public static final int U_INT_2 = 9;
    public static final int U_INT_4 = 10;
    public static final int U_INT_8 = 11;
    public static final int U_INT_16 = 12;
    public static final int INT_1 = 16;
    public static final int INT_2 = 17;
    public static final int INT_4 = 18;
    public static final int INT_8 = 19;
    public static final int INT_16 = 20;
    public static final int UUID = 24;
    public static final int STRING = 32;
    public static final int BOOL = 40;
    public static final int DATSEQ = 48;
    public static final int DATALT = 56;
    public static final int URL = 64;

    private final int type;
    private long num;
    private Object obj;

    public DataElement(int valueType) {
        if (valueType != NULL && valueType != DATALT && valueType != DATSEQ) {
            throw new IllegalArgumentException("valueType " + valueType);
        }
        type = valueType;
        if (valueType != NULL) {
            obj = new Vector();
        }
    }

    public DataElement(boolean bool) {
        type = BOOL;
        num = bool ? 1L : 0L;
    }

    public DataElement(int valueType, long value) {
        long lo, hi;
        switch (valueType) {
            case U_INT_1: lo = 0L; hi = 0xFFL; break;
            case U_INT_2: lo = 0L; hi = 0xFFFFL; break;
            case U_INT_4: lo = 0L; hi = 0xFFFFFFFFL; break;
            case INT_1: lo = -128L; hi = 127L; break;
            case INT_2: lo = -32768L; hi = 32767L; break;
            case INT_4: lo = -2147483648L; hi = 2147483647L; break;
            case INT_8: lo = Long.MIN_VALUE; hi = Long.MAX_VALUE; break;
            default: throw new IllegalArgumentException("valueType " + valueType);
        }
        if (value < lo || value > hi) {
            throw new IllegalArgumentException("value out of range for type " + valueType);
        }
        type = valueType;
        num = value;
    }

    public DataElement(int valueType, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
        switch (valueType) {
            case URL:
            case STRING:
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException("value must be a String");
                }
                obj = value;
                break;
            case UUID:
                if (!(value instanceof javax.bluetooth.UUID)) {
                    throw new IllegalArgumentException("value must be a UUID");
                }
                obj = value;
                break;
            case INT_16:
            case U_INT_16:
            case U_INT_8:
                int len = valueType == U_INT_8 ? 8 : 16;
                if (!(value instanceof byte[]) || ((byte[]) value).length != len) {
                    throw new IllegalArgumentException("value must be a byte[" + len + "]");
                }
                obj = copy((byte[]) value);
                break;
            default:
                throw new IllegalArgumentException("valueType " + valueType);
        }
        type = valueType;
    }

    private Vector seq() {
        if (type != DATSEQ && type != DATALT) {
            throw new ClassCastException("not a DATSEQ / DATALT");
        }
        return (Vector) obj;
    }

    public void addElement(DataElement elem) {
        Vector v = seq();
        if (elem == null) {
            throw new NullPointerException("elem");
        }
        v.addElement(elem);
    }

    public void insertElementAt(DataElement elem, int index) throws IndexOutOfBoundsException {
        Vector v = seq();
        if (elem == null) {
            throw new NullPointerException("elem");
        }
        if (index < 0 || index > v.size()) {
            throw new IndexOutOfBoundsException("index " + index);
        }
        v.insertElementAt(elem, index);
    }

    public int getSize() {
        return seq().size();
    }

    /** Compares references only (the class does not override equals), as the javadoc says. */
    public boolean removeElement(DataElement elem) {
        Vector v = seq();
        if (elem == null) {
            throw new NullPointerException("elem");
        }
        for (int i = 0; i < v.size(); i++) {
            if (v.elementAt(i) == elem) {
                v.removeElementAt(i);
                return true;
            }
        }
        return false;
    }

    public int getDataType() {
        return type;
    }

    public long getLong() {
        switch (type) {
            case U_INT_1: case U_INT_2: case U_INT_4: case INT_1: case INT_2: case INT_4: case INT_8:
                return num;
            default:
                throw new ClassCastException("not an integer of at most 8 bytes");
        }
    }

    public boolean getBoolean() {
        if (type != BOOL) {
            throw new ClassCastException("not a BOOL");
        }
        return num != 0L;
    }

    /** URL / STRING: String, UUID: UUID, DATSEQ / DATALT: Enumeration (of a copy), U_INT_8 / U_INT_16 / INT_16: byte[] (a copy). */
    public Object getValue() {
        switch (type) {
            case URL: case STRING: case UUID:
                return obj;
            case DATSEQ: case DATALT: {
                Vector copy = new Vector();
                Vector v = (Vector) obj;
                for (int i = 0; i < v.size(); i++) {
                    copy.addElement(v.elementAt(i));
                }
                return copy.elements();
            }
            case U_INT_8: case U_INT_16: case INT_16:
                return copy((byte[]) obj);
            default:
                throw new ClassCastException("type " + type + " has no object value");
        }
    }

    /** Deep copy (the SDK class has this public method; it is in the signature DB). */
    public DataElement clone() {
        DataElement c;
        switch (type) {
            case NULL: c = new DataElement(NULL); break;
            case BOOL: c = new DataElement(num != 0L); break;
            case DATSEQ: case DATALT: {
                c = new DataElement(type);
                Vector v = (Vector) obj;
                for (int i = 0; i < v.size(); i++) {
                    ((Vector) c.obj).addElement(((DataElement) v.elementAt(i)).clone());
                }
                break;
            }
            case URL: case STRING: case UUID: case U_INT_8: case U_INT_16: case INT_16:
                c = new DataElement(type, obj);
                break;
            default:
                c = new DataElement(type, num);
                break;
        }
        return c;
    }

    private static byte[] copy(byte[] a) {
        byte[] c = new byte[a.length];
        System.arraycopy(a, 0, c, 0, a.length);
        return c;
    }
}

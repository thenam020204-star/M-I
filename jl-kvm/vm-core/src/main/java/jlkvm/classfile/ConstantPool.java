package jlkvm.classfile;

/** Constant pool of a class file with version <= 49 (tags 1,3-12 only). Index 0 is unused. */
public final class ConstantPool {
    public static final int UTF8 = 1, INTEGER = 3, FLOAT = 4, LONG = 5, DOUBLE = 6, CLASS = 7, STRING = 8,
            FIELDREF = 9, METHODREF = 10, IMETHODREF = 11, NAME_AND_TYPE = 12;

    final byte[] tags;
    final Object[] values; // String (utf8), Integer, Float, Long, Double
    final int[] a;         // class/string: utf8 index; refs: class index; nat: name index
    final int[] b;         // refs: nat index; nat: descriptor index

    ConstantPool(int count) {
        tags = new byte[count];
        values = new Object[count];
        a = new int[count];
        b = new int[count];
    }

    public int size() { return tags.length; }

    public int tag(int i) {
        if (i <= 0 || i >= tags.length) throw new ClassFormatException("constant pool index out of range: " + i);
        return tags[i];
    }

    private void expect(int i, int tag) {
        if (tag(i) != tag) throw new ClassFormatException("constant pool entry " + i + " has tag " + tags[i] + ", expected " + tag);
    }

    public String utf8(int i) { expect(i, UTF8); return (String) values[i]; }
    public int intValue(int i) { expect(i, INTEGER); return (Integer) values[i]; }
    public float floatValue(int i) { expect(i, FLOAT); return (Float) values[i]; }
    public long longValue(int i) { expect(i, LONG); return (Long) values[i]; }
    public double doubleValue(int i) { expect(i, DOUBLE); return (Double) values[i]; }
    /** Internal name (or array descriptor) of a CONSTANT_Class. */
    public String className(int i) { expect(i, CLASS); return utf8(a[i]); }
    public String stringValue(int i) { expect(i, STRING); return utf8(a[i]); }

    private void expectRef(int i) {
        int t = tag(i);
        if (t != FIELDREF && t != METHODREF && t != IMETHODREF)
            throw new ClassFormatException("constant pool entry " + i + " is not a member reference");
    }

    public String refClass(int i) { expectRef(i); return className(a[i]); }
    public String refName(int i) { expectRef(i); expect(b[i], NAME_AND_TYPE); return utf8(a[b[i]]); }
    public String refDescriptor(int i) { expectRef(i); expect(b[i], NAME_AND_TYPE); return utf8(b[b[i]]); }
}

package jlkvm.verify;

/** Verification type of one 32-bit word of a local variable or operand stack slot. Long/double use two words. */
public final class VType {
    public static final int TOP = 0, INT = 1, FLOAT = 2, LONG = 3, LONG2 = 4, DOUBLE = 5, DOUBLE2 = 6, NULL = 7,
            UNINIT_THIS = 8, OBJECT = 9, UNINIT = 10;

    public static final VType T_TOP = new VType(TOP, null, 0), T_INT = new VType(INT, null, 0),
            T_FLOAT = new VType(FLOAT, null, 0), T_LONG = new VType(LONG, null, 0), T_LONG2 = new VType(LONG2, null, 0),
            T_DOUBLE = new VType(DOUBLE, null, 0), T_DOUBLE2 = new VType(DOUBLE2, null, 0),
            T_NULL = new VType(NULL, null, 0), T_UNINIT_THIS = new VType(UNINIT_THIS, null, 0);

    public final int kind;
    /** Internal class name, or array descriptor starting with '[' (kind OBJECT). */
    public final String name;
    /** Offset of the {@code new} instruction (kind UNINIT). */
    public final int off;

    private VType(int kind, String name, int off) { this.kind = kind; this.name = name; this.off = off; }

    public static VType object(String name) { return new VType(OBJECT, name, 0); }
    public static VType uninit(int off) { return new VType(UNINIT, null, off); }

    /** True for kinds that hold a heap reference (relevant for GC maps). Uninitialized objects are allocated. */
    public boolean isRef() { return kind == NULL || kind >= UNINIT_THIS; }
    public boolean isArray() { return kind == OBJECT && name.charAt(0) == '['; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VType)) return false;
        VType t = (VType) o;
        return kind == t.kind && off == t.off && (name == null ? t.name == null : name.equals(t.name));
    }
    @Override public int hashCode() { return kind * 31 + off + (name == null ? 0 : name.hashCode()); }

    @Override public String toString() {
        switch (kind) {
            case TOP: return "top"; case INT: return "int"; case FLOAT: return "float"; case LONG: return "long";
            case LONG2: return "long2"; case DOUBLE: return "double"; case DOUBLE2: return "double2";
            case NULL: return "null"; case UNINIT_THIS: return "uninitThis"; case UNINIT: return "uninit@" + off;
            default: return name;
        }
    }
}

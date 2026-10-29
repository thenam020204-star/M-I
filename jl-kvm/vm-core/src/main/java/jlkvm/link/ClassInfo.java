package jlkvm.link;

import jlkvm.classfile.ClassFile;
import java.util.HashMap;
import java.util.Map;

/** Runtime class (also used for array classes). */
public final class ClassInfo {
    public static final int LINKED = 1, INITIALIZING = 2, INITIALIZED = 3, ERROR = 4;

    public final int id;
    public final String name;               // internal name or array descriptor
    public final ClassFile cf;               // null for arrays
    public ClassInfo superClass;
    public ClassInfo[] interfaces = new ClassInfo[0];
    public int access;
    public boolean isInterface;
    public boolean fromRom;
    public int state = LINKED;
    /** Id of the guest thread running <clinit> while INITIALIZING. */
    public int initThread;

    public RField[] fields = new RField[0];       // declared
    public RMethod[] methods = new RMethod[0];    // declared
    public RMethod clinit;
    /** Words of the instance field area including superclasses. */
    public int instanceWords;
    /** Word offsets of reference fields (superclass first). GC input (T13). */
    public int[] refSlots = new int[0];
    public int[] statics = new int[0];
    public boolean[] staticIsRef = new boolean[0];

    /** Per constant-pool index: ClassInfo, RField or RMethod once resolved (D7). */
    public Object[] resolved;
    /** Per constant-pool index: guest String reference of a resolved string literal (0 = not yet). GC root. */
    public int[] strRefs;

    // arrays
    public boolean isArray;
    public int elemKind;                      // Heap.K_*
    public ClassInfo component;               // null for primitive arrays
    public int dims;
    private ClassInfo arrayOf;

    // WeakReference support (T13)
    public boolean isWeakRef;
    public int weakSlot = -1;

    /** Guest java/lang/Class object (0 = not created). GC root. */
    public int mirror;

    private final Map<Integer, RMethod> vcache = new HashMap<Integer, RMethod>();

    ClassInfo(int id, String name, ClassFile cf) {
        this.id = id;
        this.name = name;
        this.cf = cf;
    }

    public ClassInfo arrayClassCache() { return arrayOf; }
    void setArrayClass(ClassInfo a) { arrayOf = a; }

    public RMethod findDeclared(String n, String d) {
        for (RMethod m : methods) if (m.name.equals(n) && m.desc.equals(d)) return m;
        return null;
    }

    public RField findDeclaredField(String n, String d) {
        for (RField f : fields) if (f.name.equals(n) && (d == null || f.desc.equals(d))) return f;
        return null;
    }

    public boolean isSubclassOf(ClassInfo o) {
        for (ClassInfo c = this; c != null; c = c.superClass) if (c == o) return true;
        return false;
    }

    public boolean implementsInterface(ClassInfo i) {
        for (ClassInfo c = this; c != null; c = c.superClass) {
            for (ClassInfo x : c.interfaces) if (x == i || x.implementsInterface(i)) return true;
        }
        return false;
    }

    /** Virtual selection of the method that overrides {@code ref} in this (receiver) class; null if none (AbstractMethodError). */
    public RMethod selectVirtual(RMethod ref) {
        RMethod r = vcache.get(ref.ndId);
        if (r != null) return r;
        for (ClassInfo c = this; c != null && r == null; c = c.superClass) {
            RMethod m = c.findDeclared(ref.name, ref.desc);
            if (m != null && !m.isStatic && (m.access & ClassFile.ACC_PRIVATE) == 0 && !m.isAbstract) r = m;
        }
        if (r != null) vcache.put(ref.ndId, r);
        return r;
    }

    @Override public String toString() { return name; }
}

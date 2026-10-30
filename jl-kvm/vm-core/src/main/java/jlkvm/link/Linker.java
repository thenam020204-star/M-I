package jlkvm.link;

import jlkvm.ClassSource;
import jlkvm.Vm;
import jlkvm.classfile.ClassFile;
import jlkvm.debug.DebugFlags;
import jlkvm.classfile.ClassFormatException;
import jlkvm.classfile.ClassParser;
import jlkvm.classfile.ConstantPool;
import jlkvm.heap.Heap;
import jlkvm.natives.NativeMethod;
import jlkvm.verify.ClassHierarchy;
import jlkvm.verify.TypeMaps;
import jlkvm.verify.VerifyException;
import jlkvm.verify.Verifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class loading, linking and lazy constant-pool resolution (PLAN D7, T12): loading+verification at first use of a class,
 * resolution (with access control, JVMS 5.4.3/5.4.4) on first execution of an instruction, invokespecial/ACC_SUPER and
 * invokeinterface selection. Error mapping: {@link Errors}; rules and sources: docs/error-mapping.md.
 */
public final class Linker implements ClassHierarchy {
    private static final Object MISSING = new Object();

    private final Vm vm;
    private final ClassSource rom, app;
    private final Map<String, ClassInfo> classes = new HashMap<String, ClassInfo>();
    private final List<ClassInfo> byId = new ArrayList<ClassInfo>();
    private final Map<String, Object> parsed = new HashMap<String, Object>();
    private final Set<String> romNames = new HashSet<String>();
    private final Set<String> loading = new HashSet<String>();
    private final Map<String, Integer> ndIds = new HashMap<String, Integer>();
    public final List<RMethod> methodTable = new ArrayList<RMethod>();
    public final Verifier verifier;
    public final TypeMaps typeMaps;
    public ClassInfo objectClass, classClass;
    private int classMirrorIdSlot = -1;

    public Linker(Vm vm, ClassSource rom, ClassSource app) {
        this.vm = vm;
        this.rom = rom;
        this.app = app;
        byId.add(null);
        methodTable.add(null);
        verifier = new Verifier(this, vm.config.strictVerify, vm.profile.cldc11);
        typeMaps = new TypeMaps(verifier);
    }

    // ------------------------------------------------------------------ ClassHierarchy for the verifier

    public boolean exists(String n) { return n.charAt(0) == '[' || cf(n) != null; }
    public String superName(String n) { return cf(n).superName; }
    public boolean isInterface(String n) { return cf(n).isInterface(); }

    /** @return the parsed class or null when missing/malformed. */
    private ClassFile cf(String n) {
        Object o = parse(n);
        return o instanceof ClassFile ? (ClassFile) o : null;
    }

    private Object parse(String n) {
        Object o = parsed.get(n);
        if (o != null) return o;
        byte[] b = rom.readClass(n);
        boolean fromRom = b != null;
        if (b == null && app != null) {
            if (isProtectedPackage(n)) {
                // CLDC 1.1 spec 3.4.2.2: an application cannot add classes to the system packages
                if (app.readClass(n) != null && DebugFlags.ENABLED && vm.config.debug) vm.host.log(2, "link", "refused app class in protected package: " + n);
            } else b = app.readClass(n);
        }
        if (b == null) o = MISSING;
        else {
            try {
                ClassFile c = ClassParser.parse(b);
                if (!fromRom && vm.profile.maxClassMajor > 0 && c.major > vm.profile.maxClassMajor)
                    throw new ClassFormatException("unsupported class file version " + c.major + "." + c.minor + " (device maximum " + vm.profile.maxClassMajor + ")");
                o = c.name.equals(n) ? c : new ClassFormatException("class name " + c.name + " does not match " + n);
                if (fromRom) romNames.add(n);
            } catch (ClassFormatException e) {
                o = e;
            }
        }
        parsed.put(n, o);
        return o;
    }

    /** CLDC 1.1 spec 3.4.2.2: java.*, javax.microedition.* and the manufacturer packages of the profile belong to the system. */
    public static boolean isProtectedPackage(String internalName) {
        return internalName.startsWith("java/") || internalName.startsWith("javax/microedition/") || internalName.startsWith("com/nokia/mid/");
    }

    /**
     * ROM-private helper classes (package jlkvm.internal, PLAN T18): public in the ROM because the library's own packages use them,
     * but not part of any device API, so application classes must not see them (NoClassDefFoundError, as on a device that lacks the class).
     */
    public static boolean isRomPrivate(String internalName) {
        return internalName.startsWith("jlkvm/internal/");
    }

    // ------------------------------------------------------------------ access control (JVMS 2nd ed. 5.4.4)

    private static boolean samePackage(ClassInfo a, ClassInfo b) { return a.pkg.equals(b.pkg); }

    private static ClassInfo elementClass(ClassInfo c) {
        while (c.isArray) { if (c.component == null) return null; c = c.component; }
        return c;
    }

    /** A class is accessible to {@code from} when it is public or in the same runtime package (one bootstrap namespace, CLDC 5.1.1). */
    static boolean classAccessible(ClassInfo from, ClassInfo target) {
        ClassInfo e = elementClass(target);
        return e == null || (e.access & ClassFile.ACC_PUBLIC) != 0 || samePackage(from, e);
    }

    static boolean memberAccessible(ClassInfo from, ClassInfo declaring, int access) {
        if ((access & ClassFile.ACC_PUBLIC) != 0) return true;
        if ((access & ClassFile.ACC_PRIVATE) != 0) return from == declaring;
        if (samePackage(from, declaring)) return true;
        return (access & ClassFile.ACC_PROTECTED) != 0 && from.isSubclassOf(declaring);
    }

    private static void checkClass(ClassInfo from, ClassInfo target) {
        if (!classAccessible(from, target)) throw Errors.error("java/lang/IllegalAccessError", from.name + " cannot access class " + target.name);
    }

    // ------------------------------------------------------------------ lookup

    public ClassInfo byId(int id) { return byId.get(id); }
    public int classCount() { return byId.size(); }
    public ClassInfo loaded(String name) { return classes.get(name); }
    public Iterable<ClassInfo> allClasses() { return byId.subList(1, byId.size()); }

    private int ndId(String name, String desc) {
        String k = name + desc;
        Integer i = ndIds.get(k);
        if (i == null) { i = ndIds.size() + 1; ndIds.put(k, i); }
        return i;
    }

    public RMethod method(int id) { return methodTable.get(id); }

    /** Loads and links a class (not initialised). Throws {@link VmThrow} (NoClassDefFoundError, mapped errors). */
    public ClassInfo find(String name) {
        ClassInfo c = classes.get(name);
        if (c != null) return c;
        if (name.charAt(0) == '[') return arrayClass(name);
        Object p = parse(name);
        if (p == MISSING) throw Errors.noClassDef(name);
        if (p instanceof ClassFormatException) throw Errors.error("java/lang/ClassFormatError", name + ": " + ((ClassFormatException) p).getMessage());
        ClassFile cf = (ClassFile) p;
        if (!loading.add(name)) throw Errors.error("java/lang/ClassCircularityError", name);
        try {
            ClassInfo sup = null;
            boolean appClass = !romNames.contains(name);
            if (cf.superName != null) {
                if (appClass && isRomPrivate(cf.superName)) throw Errors.noClassDef(cf.superName);
                sup = find(cf.superName);
                if ((sup.access & ClassFile.ACC_PUBLIC) == 0 && !sup.pkg.equals(pkgOf(name)))
                    throw Errors.error("java/lang/IllegalAccessError", name + " cannot access super class " + sup.name);
                if (sup.isInterface && !cf.isInterface()) throw Errors.error("java/lang/IncompatibleClassChangeError", name + " has interface " + sup.name + " as super class");
                if ((sup.access & ClassFile.ACC_FINAL) != 0) throw Errors.error("java/lang/VerifyError", name + " cannot inherit from final " + sup.name);
            } else if (!name.equals("java/lang/Object")) throw Errors.error("java/lang/ClassFormatError", name + " has no super class");
            ClassInfo[] ifs = new ClassInfo[cf.interfaces.length];
            for (int i = 0; i < ifs.length; i++) {
                if (appClass && isRomPrivate(cf.interfaces[i])) throw Errors.noClassDef(cf.interfaces[i]);
                ifs[i] = find(cf.interfaces[i]);
                if (!ifs[i].isInterface) throw Errors.error("java/lang/IncompatibleClassChangeError", name + ": " + ifs[i].name + " is not an interface");
                if ((ifs[i].access & ClassFile.ACC_PUBLIC) == 0 && !ifs[i].pkg.equals(pkgOf(name)))
                    throw Errors.error("java/lang/IllegalAccessError", name + " cannot access interface " + ifs[i].name);
            }
            boolean fromRom = romNames.contains(name);
            if (fromRom ? vm.config.verifyRom : vm.config.verifyApp) {
                try {
                    verifier.verify(cf);
                } catch (VerifyException e) {
                    throw Errors.error(VerifyException.GUEST_CLASS, e.getMessage());
                }
            }
            c = new ClassInfo(byId.size(), name, cf);
            byId.add(c);
            c.fromRom = fromRom;
            c.superClass = sup;
            c.interfaces = ifs;
            c.access = cf.access;
            c.isInterface = cf.isInterface();
            layout(c);
            classes.put(name, c);
            if (name.equals("java/lang/Object")) objectClass = c;
            if (name.equals("java/lang/Class")) classClass = c;
            if (DebugFlags.ENABLED && vm.config.debug) vm.host.log(1, "link", "loaded " + name + (fromRom ? " (rom)" : ""));
            return c;
        } finally {
            loading.remove(name);
        }
    }

    private static String pkgOf(String name) { int s = name.lastIndexOf('/'); return s < 0 ? "" : name.substring(0, s); }

    private void layout(ClassInfo c) {
        ClassFile cf = c.cf;
        int words = c.superClass == null ? 0 : c.superClass.instanceWords;
        List<Integer> refs = new ArrayList<Integer>();
        if (c.superClass != null) for (int s : c.superClass.refSlots) refs.add(s);
        int ns = 0;
        RField[] fs = new RField[cf.fields.length];
        for (int i = 0; i < fs.length; i++) {
            RField f = new RField(c, cf.fields[i]);
            fs[i] = f;
            if (f.isStatic) { f.slot = ns; ns += f.words; }
            else { f.slot = words; if (f.isRef) refs.add(words); words += f.words; }
        }
        c.fields = fs;
        c.statics = new int[ns];
        c.staticIsRef = new boolean[ns];
        for (RField f : fs) if (f.isStatic && f.isRef) c.staticIsRef[f.slot] = true;
        c.instanceWords = words;
        c.refSlots = new int[refs.size()];
        for (int i = 0; i < c.refSlots.length; i++) c.refSlots[i] = refs.get(i);
        vm.heap.setInstanceWords(c.id, words);
        RMethod[] ms = new RMethod[cf.methods.length];
        for (int i = 0; i < ms.length; i++) {
            ClassFile.MethodInfo mi = cf.methods[i];
            RMethod m = new RMethod(methodTable.size(), c, mi, ndId(mi.name, mi.descriptor));
            methodTable.add(m);
            ms[i] = m;
            if (m.isNative) {
                m.nativeKey = c.name + "." + m.name + m.desc;
                NativeMethod nm = vm.registry.lookup(m.nativeKey);
                m.nat = nm;
                if (nm == null) {
                    vm.missingNatives.add(m.nativeKey);   // T15: a call throws a guest Error; the debug build says so already at class load
                    if (DebugFlags.ENABLED && vm.config.debug) vm.host.log(2, "link", "missing native " + m.nativeKey);
                }
            } else if (m.name.equals("<clinit>") && m.desc.equals("()V") && m.code != null) c.clinit = m;
        }
        c.methods = ms;
        c.resolved = new Object[cf.pool.size()];
        c.strRefs = new int[cf.pool.size()];
        c.isWeakRef = (c.superClass != null && c.superClass.isWeakRef) || c.name.equals("java/lang/ref/WeakReference");
        if (c.isWeakRef) c.weakSlot = c.superClass != null && c.superClass.isWeakRef ? c.superClass.weakSlot : -1;
        if (c.name.equals("java/lang/ref/WeakReference")) {
            RField r = null;
            for (ClassInfo k = c; k != null && r == null; k = k.superClass) r = k.findDeclaredField("referent", null);
            c.weakSlot = r == null ? -1 : r.slot;
        }
    }

    // ------------------------------------------------------------------ arrays

    public ClassInfo arrayClass(String desc) {
        ClassInfo c = classes.get(desc);
        if (c != null) return c;
        char e = desc.charAt(1);
        ClassInfo comp = null;
        int kind;
        switch (e) {
            case 'Z': kind = Heap.K_BOOLEAN; break;
            case 'B': kind = Heap.K_BYTE; break;
            case 'C': kind = Heap.K_CHAR; break;
            case 'S': kind = Heap.K_SHORT; break;
            case 'I': kind = Heap.K_INT; break;
            case 'F': kind = Heap.K_FLOAT; break;
            case 'J': kind = Heap.K_LONG; break;
            case 'D': kind = Heap.K_DOUBLE; break;
            case '[': comp = arrayClass(desc.substring(1)); kind = Heap.K_REF; break;
            case 'L': comp = find(desc.substring(2, desc.length() - 1)); kind = Heap.K_REF; break;
            default: throw Errors.noClassDef(desc);
        }
        c = new ClassInfo(byId.size(), desc, null);
        byId.add(c);
        c.superClass = objectClass != null ? objectClass : find("java/lang/Object");
        c.access = ClassFile.ACC_PUBLIC | ClassFile.ACC_FINAL | ClassFile.ACC_ABSTRACT;
        c.isArray = true;
        c.elemKind = kind;
        c.component = comp;
        c.dims = 1 + (comp != null && comp.isArray ? comp.dims : 0);
        c.state = ClassInfo.INITIALIZED;
        c.resolved = new Object[0];
        c.strRefs = new int[0];
        vm.heap.setInstanceWords(c.id, 0);
        classes.put(desc, c);
        return c;
    }

    public ClassInfo arrayOf(ClassInfo comp) {
        ClassInfo a = comp.arrayClassCache();
        if (a == null) {
            a = arrayClass(comp.isArray ? "[" + comp.name : "[L" + comp.name + ";");
            comp.setArrayClass(a);
        }
        return a;
    }

    public ClassInfo primitiveArray(int kind) {
        switch (kind) {
            case Heap.K_BOOLEAN: return arrayClass("[Z");
            case Heap.K_BYTE: return arrayClass("[B");
            case Heap.K_CHAR: return arrayClass("[C");
            case Heap.K_SHORT: return arrayClass("[S");
            case Heap.K_INT: return arrayClass("[I");
            case Heap.K_FLOAT: return arrayClass("[F");
            case Heap.K_LONG: return arrayClass("[J");
            default: return arrayClass("[D");
        }
    }

    /** JVMS assignment compatibility of an object of class {@code from} to {@code to} (checkcast / instanceof / aastore). */
    public boolean isAssignable(ClassInfo from, ClassInfo to) {
        if (from == to || to == objectClass) return true;
        if (from.isArray) {
            if (!to.isArray || from.elemKind != to.elemKind) return false;
            if (to.component == null || from.component == null) return from.component == to.component;
            return isAssignable(from.component, to.component);
        }
        if (to.isArray) return false;
        if (to.isInterface) return from.implementsInterface(to);
        return from.isSubclassOf(to);
    }

    // ------------------------------------------------------------------ resolution

    /** Resolves a CONSTANT_Class (JVMS 5.4.3.1): loads it and checks that {@code from} may access it. Not cached on failure. */
    public ClassInfo resolveClass(ClassInfo from, int idx) {
        Object o = from.resolved[idx];
        if (o instanceof ClassInfo) return (ClassInfo) o;
        String cn = from.cf.pool.className(idx);
        if (!from.fromRom && isRomPrivate(cn)) throw Errors.noClassDef(cn);
        ClassInfo c = find(cn);
        checkClass(from, c);
        from.resolved[idx] = c;
        return c;
    }

    public RField findField(ClassInfo c, String name, String desc) {
        for (ClassInfo k = c; k != null; k = k.superClass) {
            RField f = k.findDeclaredField(name, desc);
            if (f != null && f.desc.equals(desc)) return f;
            for (ClassInfo i : k.interfaces) {
                RField g = findField(i, name, desc);
                if (g != null) return g;
            }
        }
        return null;
    }

    /** Field resolution (JVMS 5.4.3.2): class resolution, lookup, NoSuchFieldError, access check (IllegalAccessError). */
    public RField resolveField(ClassInfo from, int idx) {
        Object o = from.resolved[idx];
        if (o instanceof RField) return (RField) o;
        ConstantPool cp = from.cf.pool;
        ClassInfo c = resolveClass(from, cp.refClassIndex(idx));
        RField f = findField(c, cp.refName(idx), cp.refDescriptor(idx));
        if (f == null) throw Errors.error("java/lang/NoSuchFieldError", c.name + "." + cp.refName(idx));
        if (!memberAccessible(from, f.owner, f.access))
            throw Errors.error("java/lang/IllegalAccessError", from.name + " cannot access field " + f.owner.name + "." + f.name);
        from.resolved[idx] = f;
        return f;
    }

    public RMethod findMethod(ClassInfo c, String name, String desc) {
        if (name.equals("<init>") || name.equals("<clinit>")) return c.findDeclared(name, desc);   // not inherited
        for (ClassInfo k = c; k != null; k = k.superClass) {
            RMethod m = k.findDeclared(name, desc);
            if (m != null) return m;
        }
        for (ClassInfo k = c; k != null; k = k.superClass) {
            for (ClassInfo i : k.interfaces) {
                RMethod m = findMethod(i, name, desc);
                if (m != null) return m;
            }
        }
        return null;
    }

    /**
     * Method resolution (JVMS 5.4.3.3 / 5.4.3.4). A Methodref must name a class and an InterfaceMethodref an interface
     * (IncompatibleClassChangeError otherwise); NoSuchMethodError; access check (IllegalAccessError).
     */
    public RMethod resolveMethod(ClassInfo from, int idx) {
        Object o = from.resolved[idx];
        if (o instanceof RMethod) return (RMethod) o;
        ConstantPool cp = from.cf.pool;
        ClassInfo c = resolveClass(from, cp.refClassIndex(idx));
        boolean viaInterfaceRef = cp.tag(idx) == ConstantPool.IMETHODREF;
        if (c.isInterface != viaInterfaceRef)
            throw Errors.error("java/lang/IncompatibleClassChangeError", c.name + (viaInterfaceRef ? " is not an interface" : " is an interface"));
        RMethod m = findMethod(c, cp.refName(idx), cp.refDescriptor(idx));
        if (m == null) throw Errors.error("java/lang/NoSuchMethodError", c.name + "." + cp.refName(idx) + cp.refDescriptor(idx));
        if (!memberAccessible(from, m.owner, m.access))
            throw Errors.error("java/lang/IllegalAccessError", from.name + " cannot access method " + m);
        from.resolved[idx] = m;
        return m;
    }

    /**
     * invokespecial target (JVMS 2nd ed. 6.4). With ACC_SUPER set on the calling class, a method that is not <init> and whose
     * reference names a proper superclass is looked up again from the direct superclass; without ACC_SUPER (class files from
     * before JDK 1.0.2) the resolved method is called as is. Cached per (class, pool index).
     */
    public RMethod specialTarget(ClassInfo cls, int idx, RMethod ref) {
        if (ref.isInit || (cls.access & ClassFile.ACC_SUPER) == 0) return ref;
        RMethod[] cache = cls.superTargets;
        if (cache == null) cache = cls.superTargets = new RMethod[cls.cf.pool.size()];
        RMethod t = cache[idx];
        if (t != null) return t;
        ClassInfo named = resolveClass(cls, cls.cf.pool.refClassIndex(idx));
        t = ref;
        if (!named.isInterface && named != cls && cls.isSubclassOf(named)) {
            t = cls.superClass.selectVirtual(ref);
            if (t == null) throw Errors.error("java/lang/AbstractMethodError", ref.toString());
        }
        cache[idx] = t;
        return t;
    }

    /** invokeinterface selection: the receiver class must implement the interface (ICCE); the method must be public (IllegalAccessError). */
    public RMethod selectInterface(ClassInfo recv, RMethod ref) {
        RMethod t = recv.cachedInterfaceTarget(ref);
        if (t != null) return t;
        if (ref.owner.isInterface && !recv.implementsInterface(ref.owner))
            throw Errors.error("java/lang/IncompatibleClassChangeError", recv.name + " does not implement " + ref.owner.name);
        t = recv.selectVirtual(ref);
        if (t == null) throw Errors.error("java/lang/AbstractMethodError", ref.toString());
        if ((t.access & ClassFile.ACC_PUBLIC) == 0) throw Errors.error("java/lang/IllegalAccessError", t.toString());
        recv.cacheInterfaceTarget(ref, t);
        return t;
    }

    /** Guest String of a CONSTANT_String (interned). */
    public int resolveString(ClassInfo from, int idx) {
        int r = from.strRefs[idx];
        if (r != 0) return r;
        r = vm.intern(from.cf.pool.stringValue(idx));
        if (r == 0) throw new VmThrow("java/lang/OutOfMemoryError", "string literal");
        from.strRefs[idx] = r;
        return r;
    }

    // ------------------------------------------------------------------ initialisation / mirrors

    /** Applies ConstantValue attributes to the statics of {@code c} (done right before <clinit> runs). */
    public void prepareStatics(ClassInfo c) {
        ConstantPool cp = c.cf.pool;
        for (RField f : c.fields) {
            int cv = f.fi.constantValue;
            if (!f.isStatic || cv == 0) continue;
            switch (cp.tag(cv)) {
                case ConstantPool.INTEGER: c.statics[f.slot] = cp.intValue(cv); break;
                case ConstantPool.FLOAT: c.statics[f.slot] = Float.floatToRawIntBits(cp.floatValue(cv)); break;
                case ConstantPool.LONG: { long v = cp.longValue(cv); c.statics[f.slot] = (int) (v >>> 32); c.statics[f.slot + 1] = (int) v; break; }
                case ConstantPool.DOUBLE: { long v = Double.doubleToRawLongBits(cp.doubleValue(cv)); c.statics[f.slot] = (int) (v >>> 32); c.statics[f.slot + 1] = (int) v; break; }
                case ConstantPool.STRING: c.statics[f.slot] = resolveString(c, cv); break;
                default: break;
            }
        }
    }

    public int mirror(ClassInfo c) {
        if (c.mirror != 0) return c.mirror;
        if (classClass == null) find("java/lang/Class");
        if (classMirrorIdSlot < 0) classMirrorIdSlot = classClass.findDeclaredField("vmId", null).slot;
        int r = vm.heap.allocObject(classClass.id);
        if (r == 0) throw new VmThrow("java/lang/OutOfMemoryError", "class mirror");
        vm.heap.putInt(r, classMirrorIdSlot, c.id);
        c.mirror = r;
        return r;
    }

    public ClassInfo fromMirror(int ref) {
        if (classMirrorIdSlot < 0) classMirrorIdSlot = classClass.findDeclaredField("vmId", null).slot;
        return byId.get(vm.heap.getInt(ref, classMirrorIdSlot));
    }
}

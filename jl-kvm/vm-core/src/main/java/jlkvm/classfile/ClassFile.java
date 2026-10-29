package jlkvm.classfile;

/** Parsed class file (version <= 49). All fields are immutable after parsing. */
public final class ClassFile {
    public static final int ACC_PUBLIC = 0x0001, ACC_PRIVATE = 0x0002, ACC_PROTECTED = 0x0004, ACC_STATIC = 0x0008,
            ACC_FINAL = 0x0010, ACC_SUPER = 0x0020, ACC_SYNCHRONIZED = 0x0020, ACC_VOLATILE = 0x0040,
            ACC_NATIVE = 0x0100, ACC_INTERFACE = 0x0200, ACC_ABSTRACT = 0x0400;

    /** verification_type_info tags (StackMap attribute), packed as {@code tag | arg << 8}. */
    public static final int VT_TOP = 0, VT_INTEGER = 1, VT_FLOAT = 2, VT_DOUBLE = 3, VT_LONG = 4, VT_NULL = 5,
            VT_UNINIT_THIS = 6, VT_OBJECT = 7, VT_UNINIT = 8;

    public int minor, major;
    public ConstantPool pool;
    public int access;
    public String name;         // internal name
    public String superName;    // null only for java/lang/Object
    public String[] interfaces;
    public FieldInfo[] fields;
    public MethodInfo[] methods;
    public String sourceFile;   // may be null

    public boolean isInterface() { return (access & ACC_INTERFACE) != 0; }

    public MethodInfo findMethod(String n, String desc) {
        for (MethodInfo m : methods) if (m.name.equals(n) && m.descriptor.equals(desc)) return m;
        return null;
    }

    public static final class FieldInfo {
        public int access;
        public String name, descriptor;
        /** Constant pool index of the ConstantValue attribute, or 0. */
        public int constantValue;
    }

    public static final class MethodInfo {
        public int access;
        public String name, descriptor;
        public CodeAttr code;          // null for abstract/native
        public String[] exceptions;    // Exceptions attribute, empty if none

        public boolean isStatic() { return (access & ACC_STATIC) != 0; }
    }

    public static final class ExceptionEntry {
        public int startPc, endPc, handlerPc;
        /** 0 = catch all. */
        public int catchType;
    }

    /** One entry of the CLDC {@code StackMap} attribute. {@code offset} is the absolute bytecode offset. */
    public static final class StackMapEntry {
        public int offset;
        public int[] locals;  // packed verification types; long/double occupy ONE element
        public int[] stack;
    }

    public static final class CodeAttr {
        public int maxStack, maxLocals;
        public byte[] code;
        public ExceptionEntry[] exceptionTable;
        /** CLDC StackMap attribute; null if absent. */
        public StackMapEntry[] stackMap;
        /** True if a Java 6 StackMapTable attribute was seen (ignored by the CLDC verifier). */
        public boolean hasStackMapTable;
        /** pairs (startPc, line); may be empty. */
        public int[] lineNumbers = new int[0];
    }
}

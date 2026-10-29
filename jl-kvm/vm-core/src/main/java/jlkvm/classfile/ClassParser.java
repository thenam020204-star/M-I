package jlkvm.classfile;

import jlkvm.classfile.ClassFile.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Class file parser (PLAN T07). Accepts versions 45.0 - 49.x. Malformed input of any kind (truncation, bad indices,
 * bad lengths, trailing bytes...) results in {@link ClassFormatException}; no other exception escapes.
 */
public final class ClassParser {
    public static final int MAX_MAJOR = 49;

    private final byte[] d;
    private int p;
    private ConstantPool cp;

    private ClassParser(byte[] data) { d = data; }

    public static ClassFile parse(byte[] data) {
        if (data == null) throw new ClassFormatException("null class data");
        try {
            return new ClassParser(data).parseClass();
        } catch (ClassFormatException e) {
            throw e;
        } catch (RuntimeException e) { // defensive: never let a host exception escape (T07)
            throw new ClassFormatException("internal parser error: " + e);
        }
    }

    // ---- primitives ----
    private int u1() {
        if (p >= d.length) throw new ClassFormatException("truncated class file");
        return d[p++] & 0xff;
    }
    private int u2() { int h = u1(); return (h << 8) | u1(); }
    private int s4() { int h = u2(); return (h << 16) | u2(); }
    private void need(int n) {
        if (n < 0 || n > d.length - p) throw new ClassFormatException("truncated class file");
    }

    private ClassFile parseClass() {
        if (s4() != 0xCAFEBABE) throw new ClassFormatException("bad magic");
        ClassFile c = new ClassFile();
        c.minor = u2();
        c.major = u2();
        if (c.major < 45 || c.major > MAX_MAJOR) throw new ClassFormatException("unsupported class file version " + c.major + "." + c.minor);
        parseConstantPool();
        c.pool = cp;
        c.access = u2();
        c.name = cpClass(u2());
        int sup = u2();
        c.superName = sup == 0 ? null : cpClass(sup);
        if (c.superName == null && !c.name.equals("java/lang/Object")) throw new ClassFormatException("missing superclass");
        if (c.name.charAt(0) == '[') throw new ClassFormatException("array class name");
        int ni = u2();
        need(ni * 2);
        c.interfaces = new String[ni];
        for (int i = 0; i < ni; i++) c.interfaces[i] = cpClass(u2());
        int nf = u2();
        c.fields = new FieldInfo[Math.min(nf, d.length)];
        if (nf > d.length) throw new ClassFormatException("truncated class file");
        Set<String> seen = new HashSet<String>();
        for (int i = 0; i < nf; i++) {
            c.fields[i] = parseField();
            if (!seen.add(c.fields[i].name + ":" + c.fields[i].descriptor)) throw new ClassFormatException("duplicate field " + c.fields[i].name);
        }
        int nm = u2();
        if (nm > d.length) throw new ClassFormatException("truncated class file");
        c.methods = new MethodInfo[nm];
        seen.clear();
        for (int i = 0; i < nm; i++) {
            c.methods[i] = parseMethod();
            if (!seen.add(c.methods[i].name + c.methods[i].descriptor)) throw new ClassFormatException("duplicate method " + c.methods[i].name);
        }
        int na = u2();
        for (int i = 0; i < na; i++) {
            String an = cp.utf8(u2());
            int len = s4();
            need(len);
            int end = p + len;
            if (an.equals("SourceFile")) {
                if (len != 2) throw new ClassFormatException("bad SourceFile");
                c.sourceFile = cp.utf8(u2());
            }
            p = end;
        }
        if (p != d.length) throw new ClassFormatException("extra bytes at end of class file");
        return c;
    }

    private String cpClass(int i) {
        String n = cp.className(i);
        if (n.length() == 0) throw new ClassFormatException("empty class name");
        if (n.charAt(0) == '[') {
            if (!Descriptors.isValidField(n)) throw new ClassFormatException("bad array class name " + n);
        } else if (n.indexOf(';') >= 0 || n.indexOf('[') >= 0 || n.indexOf('.') >= 0) {
            throw new ClassFormatException("bad class name " + n);
        }
        return n;
    }

    private void parseConstantPool() {
        int n = u2();
        if (n < 1) throw new ClassFormatException("bad constant pool count");
        cp = new ConstantPool(n);
        for (int i = 1; i < n; i++) {
            int tag = u1();
            cp.tags[i] = (byte) tag;
            switch (tag) {
                case ConstantPool.UTF8: {
                    int len = u2();
                    need(len);
                    cp.values[i] = decodeUtf8(d, p, len);
                    p += len;
                    break;
                }
                case ConstantPool.INTEGER: cp.values[i] = Integer.valueOf(s4()); break;
                case ConstantPool.FLOAT: cp.values[i] = Float.valueOf(Float.intBitsToFloat(s4())); break;
                case ConstantPool.LONG: case ConstantPool.DOUBLE: {
                    long hi = s4() & 0xffffffffL, lo = s4() & 0xffffffffL;
                    long v = (hi << 32) | lo;
                    cp.values[i] = tag == ConstantPool.LONG ? (Object) Long.valueOf(v) : (Object) Double.valueOf(Double.longBitsToDouble(v));
                    if (++i >= n) throw new ClassFormatException("long/double at end of constant pool");
                    cp.tags[i] = 0; // unusable slot
                    break;
                }
                case ConstantPool.CLASS: case ConstantPool.STRING: cp.a[i] = u2(); break;
                case ConstantPool.FIELDREF: case ConstantPool.METHODREF: case ConstantPool.IMETHODREF:
                case ConstantPool.NAME_AND_TYPE:
                    cp.a[i] = u2();
                    cp.b[i] = u2();
                    break;
                default:
                    throw new ClassFormatException("bad constant pool tag " + tag + " at " + i);
            }
        }
        // validate cross references
        for (int i = 1; i < n; i++) {
            switch (cp.tags[i]) {
                case ConstantPool.CLASS: case ConstantPool.STRING: cp.utf8(cp.a[i]); break;
                case ConstantPool.NAME_AND_TYPE: {
                    String nm = cp.utf8(cp.a[i]), ds = cp.utf8(cp.b[i]);
                    if (nm.length() == 0) throw new ClassFormatException("empty member name");
                    if (!(ds.charAt(0) == '(' ? Descriptors.isValidMethod(ds) : Descriptors.isValidField(ds)))
                        throw new ClassFormatException("bad descriptor " + ds);
                    break;
                }
                case ConstantPool.FIELDREF: case ConstantPool.METHODREF: case ConstantPool.IMETHODREF: {
                    cp.className(cp.a[i]);
                    if (cp.tag(cp.b[i]) != ConstantPool.NAME_AND_TYPE) throw new ClassFormatException("bad name-and-type ref");
                    String ds = cp.refDescriptor(i);
                    boolean isM = ds.charAt(0) == '(';
                    if (isM != (cp.tags[i] != ConstantPool.FIELDREF)) throw new ClassFormatException("descriptor kind mismatch");
                    String nm = cp.refName(i);
                    if (nm.charAt(0) == '<' && !(isM && nm.equals("<init>") && ds.endsWith(")V")))
                        throw new ClassFormatException("bad special method name " + nm);
                    break;
                }
                default: break;
            }
        }
    }

    /** Modified UTF-8 decoder; malformed input is a ClassFormatException. */
    static String decodeUtf8(byte[] b, int off, int len) {
        char[] out = new char[len];
        int o = 0, i = off, end = off + len;
        while (i < end) {
            int c = b[i++] & 0xff;
            if (c < 0x80) {
                if (c == 0) throw new ClassFormatException("bad UTF-8 (raw 0)");
                out[o++] = (char) c;
            } else if ((c & 0xe0) == 0xc0) {
                if (i >= end) throw new ClassFormatException("bad UTF-8");
                int c2 = b[i++] & 0xff;
                if ((c2 & 0xc0) != 0x80) throw new ClassFormatException("bad UTF-8");
                out[o++] = (char) (((c & 0x1f) << 6) | (c2 & 0x3f));
            } else if ((c & 0xf0) == 0xe0) {
                if (i + 1 >= end) throw new ClassFormatException("bad UTF-8");
                int c2 = b[i++] & 0xff, c3 = b[i++] & 0xff;
                if ((c2 & 0xc0) != 0x80 || (c3 & 0xc0) != 0x80) throw new ClassFormatException("bad UTF-8");
                out[o++] = (char) (((c & 0x0f) << 12) | ((c2 & 0x3f) << 6) | (c3 & 0x3f));
            } else {
                throw new ClassFormatException("bad UTF-8");
            }
        }
        return new String(out, 0, o);
    }

    private FieldInfo parseField() {
        FieldInfo f = new FieldInfo();
        f.access = u2();
        f.name = cp.utf8(u2());
        f.descriptor = cp.utf8(u2());
        if (f.name.length() == 0 || !Descriptors.isValidField(f.descriptor)) throw new ClassFormatException("bad field " + f.name);
        int na = u2();
        for (int i = 0; i < na; i++) {
            String an = cp.utf8(u2());
            int len = s4();
            need(len);
            int end = p + len;
            if (an.equals("ConstantValue")) {
                if (len != 2) throw new ClassFormatException("bad ConstantValue");
                int idx = u2();
                int t = cp.tag(idx);
                String ds = f.descriptor;
                boolean ok;
                switch (ds.charAt(0)) {
                    case 'J': ok = t == ConstantPool.LONG; break;
                    case 'F': ok = t == ConstantPool.FLOAT; break;
                    case 'D': ok = t == ConstantPool.DOUBLE; break;
                    case 'B': case 'C': case 'I': case 'S': case 'Z': ok = t == ConstantPool.INTEGER; break;
                    default: ok = ds.equals("Ljava/lang/String;") && t == ConstantPool.STRING;
                }
                if (!ok) throw new ClassFormatException("ConstantValue type mismatch for " + f.name);
                f.constantValue = idx;
            }
            p = end;
        }
        return f;
    }

    private MethodInfo parseMethod() {
        MethodInfo m = new MethodInfo();
        m.access = u2();
        m.name = cp.utf8(u2());
        m.descriptor = cp.utf8(u2());
        if (m.name.length() == 0 || !Descriptors.isValidMethod(m.descriptor)) throw new ClassFormatException("bad method " + m.name);
        m.exceptions = new String[0];
        int na = u2();
        for (int i = 0; i < na; i++) {
            String an = cp.utf8(u2());
            int len = s4();
            need(len);
            int end = p + len;
            if (an.equals("Code")) {
                if (m.code != null) throw new ClassFormatException("duplicate Code attribute");
                m.code = parseCode(end);
            } else if (an.equals("Exceptions")) {
                int n = u2();
                if (len != 2 + 2 * n) throw new ClassFormatException("bad Exceptions length");
                m.exceptions = new String[n];
                for (int k = 0; k < n; k++) m.exceptions[k] = cpClass(u2());
            }
            if (p > end) throw new ClassFormatException("attribute overrun");
            p = end;
        }
        boolean noCode = (m.access & (ClassFile.ACC_ABSTRACT | ClassFile.ACC_NATIVE)) != 0;
        if (noCode && m.code != null) throw new ClassFormatException("abstract/native method has Code: " + m.name);
        if (!noCode && m.code == null) throw new ClassFormatException("method without Code: " + m.name);
        return m;
    }

    private CodeAttr parseCode(int attrEnd) {
        CodeAttr c = new CodeAttr();
        c.maxStack = u2();
        c.maxLocals = u2();
        int len = s4();
        if (len <= 0 || len >= 65536) throw new ClassFormatException("bad code length " + len);
        need(len);
        c.code = new byte[len];
        System.arraycopy(d, p, c.code, 0, len);
        p += len;
        int ne = u2();
        c.exceptionTable = new ExceptionEntry[ne];
        need(ne * 8);
        for (int i = 0; i < ne; i++) {
            ExceptionEntry e = new ExceptionEntry();
            e.startPc = u2(); e.endPc = u2(); e.handlerPc = u2(); e.catchType = u2();
            if (e.startPc >= e.endPc || e.endPc > len || e.handlerPc >= len) throw new ClassFormatException("bad exception table entry");
            if (e.catchType != 0) cpClass(e.catchType);
            c.exceptionTable[i] = e;
        }
        int na = u2();
        for (int i = 0; i < na; i++) {
            String an = cp.utf8(u2());
            int alen = s4();
            need(alen);
            int end = p + alen;
            if (end > attrEnd) throw new ClassFormatException("code sub-attribute overruns Code");
            if (an.equals("StackMap")) {
                if (c.stackMap != null) throw new ClassFormatException("duplicate StackMap");
                c.stackMap = parseStackMap(len, end);
            } else if (an.equals("StackMapTable")) {
                c.hasStackMapTable = true;
            } else if (an.equals("LineNumberTable")) {
                int n = u2();
                if (alen != 2 + 4 * n) throw new ClassFormatException("bad LineNumberTable");
                int[] ln = new int[n * 2];
                for (int k = 0; k < n; k++) {
                    ln[2 * k] = u2();
                    ln[2 * k + 1] = u2();
                }
                int[] old = c.lineNumbers;
                int[] all = new int[old.length + ln.length];
                System.arraycopy(old, 0, all, 0, old.length);
                System.arraycopy(ln, 0, all, old.length, ln.length);
                c.lineNumbers = all;
            }
            p = end;
        }
        if (p != attrEnd) throw new ClassFormatException("Code attribute length mismatch");
        return c;
    }

    private StackMapEntry[] parseStackMap(int codeLen, int end) {
        int n = u2();
        if (n * 6 > end - p) throw new ClassFormatException("bad StackMap");
        StackMapEntry[] r = new StackMapEntry[n];
        for (int i = 0; i < n; i++) {
            StackMapEntry e = new StackMapEntry();
            e.offset = u2();
            if (e.offset >= codeLen) throw new ClassFormatException("StackMap offset out of code");
            if (i > 0 && e.offset <= r[i - 1].offset) throw new ClassFormatException("StackMap offsets not ascending");
            e.locals = vtypes(codeLen, end);
            e.stack = vtypes(codeLen, end);
            r[i] = e;
        }
        if (p != end) throw new ClassFormatException("StackMap length mismatch");
        return r;
    }

    private int[] vtypes(int codeLen, int end) {
        int n = u2();
        if (n > end - p) throw new ClassFormatException("bad StackMap type count");
        int[] r = new int[n];
        for (int i = 0; i < n; i++) {
            int tag = u1();
            int arg = 0;
            if (tag > ClassFile.VT_UNINIT) throw new ClassFormatException("bad verification type " + tag);
            if (tag == ClassFile.VT_OBJECT) {
                arg = u2();
                cpClass(arg);
            } else if (tag == ClassFile.VT_UNINIT) {
                arg = u2();
                if (arg >= codeLen) throw new ClassFormatException("bad uninitialized offset");
            }
            r[i] = tag | (arg << 8);
        }
        return r;
    }

}

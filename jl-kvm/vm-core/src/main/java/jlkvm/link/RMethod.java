package jlkvm.link;

import jlkvm.classfile.ClassFile;
import jlkvm.classfile.Descriptors;
import jlkvm.natives.NativeMethod;
import java.util.List;

/** Runtime method. */
public final class RMethod {
    public final int id;
    public final ClassInfo owner;
    public final ClassFile.MethodInfo mi;
    public final String name, desc;
    public final int access;
    public final boolean isStatic, isNative, isAbstract, isSync, isInit;
    /** Words of the arguments including the receiver. */
    public final int argWords;
    /** Word offset of each parameter (receiver is parameter 0 of an instance method). */
    public final int[] argOffset;
    /** Per argument word (receiver and long/double halves included): holds a reference. Used by the collector for natives in progress. */
    public final boolean[] argWordIsRef;
    /** Return descriptor first char: V Z B C S I F J D L [ . */
    public final char ret;
    public final int maxLocals, maxStack;
    public final byte[] code;
    /** Name+descriptor id shared by all methods with the same signature (virtual lookup cache key). */
    public final int ndId;
    public NativeMethod nat;
    public String nativeKey;

    RMethod(int id, ClassInfo owner, ClassFile.MethodInfo mi, int ndId) {
        this.id = id;
        this.owner = owner;
        this.mi = mi;
        this.name = mi.name;
        this.desc = mi.descriptor;
        this.access = mi.access;
        this.isStatic = (access & ClassFile.ACC_STATIC) != 0;
        this.isNative = (access & ClassFile.ACC_NATIVE) != 0;
        this.isAbstract = (access & ClassFile.ACC_ABSTRACT) != 0;
        this.isInit = mi.name.equals("<init>");
        this.isSync = (access & ClassFile.ACC_SYNCHRONIZED) != 0;
        List<String> ps = Descriptors.params(desc);
        int n = ps.size() + (isStatic ? 0 : 1);
        argOffset = new int[n];
        int w = 0, k = 0;
        if (!isStatic) { argOffset[k++] = 0; w = 1; }
        for (String p : ps) {
            argOffset[k++] = w;
            w += (p.equals("J") || p.equals("D")) ? 2 : 1;
        }
        argWords = w;
        argWordIsRef = new boolean[w];
        if (!isStatic) argWordIsRef[0] = true;
        { int ai = isStatic ? 0 : 1; for (String p : ps) { argWordIsRef[argOffset[ai++]] = p.charAt(0) == 'L' || p.charAt(0) == '['; } }
        ret = Descriptors.returnType(desc).charAt(0);
        if (mi.code != null) {
            code = mi.code.code;
            maxLocals = Math.max(mi.code.maxLocals, argWords);
            maxStack = mi.code.maxStack;
        } else {
            code = null;
            maxLocals = argWords;
            maxStack = 0;
        }
        this.ndId = ndId;
    }

    /** Line number for a pc, or -1. */
    public int lineAt(int pc) {
        if (mi.code == null) return -1;
        int[] ln = mi.code.lineNumbers;
        int best = -1, bestPc = -1;
        for (int i = 0; i + 1 < ln.length; i += 2) if (ln[i] <= pc && ln[i] > bestPc) { bestPc = ln[i]; best = ln[i + 1]; }
        return best;
    }

    @Override public String toString() { return owner.name + "." + name + desc; }
}

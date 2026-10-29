package jlkvm.link;

import jlkvm.classfile.ClassFile;

/** Runtime field. Instance: {@code slot} = word offset in the field area; static: index into {@code owner.statics}. */
public final class RField {
    public final ClassInfo owner;
    public final ClassFile.FieldInfo fi;
    public final String name, desc;
    public final boolean isStatic;
    /** Descriptor first char: Z B C S I F J D L [ . */
    public final char kind;
    public final int words;
    public final boolean isRef;
    public int slot;

    RField(ClassInfo owner, ClassFile.FieldInfo fi) {
        this.owner = owner;
        this.fi = fi;
        this.name = fi.name;
        this.desc = fi.descriptor;
        this.isStatic = (fi.access & ClassFile.ACC_STATIC) != 0;
        this.kind = desc.charAt(0);
        this.words = (kind == 'J' || kind == 'D') ? 2 : 1;
        this.isRef = kind == 'L' || kind == '[';
    }
}

package jlkvm.verify;

import jlkvm.classfile.ClassFile;
import jlkvm.classfile.ClassFile.MethodInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * CLDC class verifier (PLAN T08, D6). {@code strict}: only the StackMap attribute is trusted, a missing/wrong one is a
 * {@link VerifyException} (guest VerifyError). {@code lenient} (debug only): frames are inferred by data flow and a
 * warning is recorded when the StackMap did not verify.
 */
public final class Verifier {
    private final ClassHierarchy hierarchy;
    private final boolean strict;
    private final boolean cldc11;
    private final List<String> warnings = new ArrayList<String>();

    /** @param cldc11 false = CLDC 1.0: float/double opcodes, constants and descriptors are rejected. */
    public Verifier(ClassHierarchy hierarchy, boolean strict, boolean cldc11) {
        this.hierarchy = hierarchy;
        this.strict = strict;
        this.cldc11 = cldc11;
    }

    public List<String> warnings() { return warnings; }

    /** Verifies every method with code; throws {@link VerifyException} at the first failure. */
    public void verify(ClassFile cf) {
        for (MethodInfo m : cf.methods) if (m.code != null) verifyMethod(cf, m, false);
    }

    /**
     * Verifies one method. With {@code record} returns the type state before each instruction (indexed by pc; null at
     * non-instruction offsets or unreachable code), used by {@link TypeMaps}.
     */
    public Frame[] verifyMethod(ClassFile cf, MethodInfo m, boolean record) {
        if (m.code == null) throw new VerifyException(cf.name, m.name, m.descriptor, -1, "method has no code");
        return new MethodVerifier(cf, m, hierarchy, cldc11, warnings).verify(strict, record);
    }
}

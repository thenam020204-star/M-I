package jlkvm.verify;

/** Type state at one bytecode offset: locals (one entry per slot) and operand stack (one entry per 32-bit word). */
public final class Frame {
    public final VType[] locals;
    public final VType[] stack;
    public int sp;

    Frame(int maxLocals, int maxStack) {
        locals = new VType[maxLocals];
        stack = new VType[maxStack];
        java.util.Arrays.fill(locals, VType.T_TOP);
    }

    private Frame(Frame f) {
        locals = f.locals.clone();
        stack = f.stack.clone();
        sp = f.sp;
    }

    Frame copy() { return new Frame(this); }

    @Override public String toString() {
        StringBuilder b = new StringBuilder("locals=[");
        for (int i = 0; i < locals.length; i++) b.append(i > 0 ? "," : "").append(locals[i]);
        b.append("] stack=[");
        for (int i = 0; i < sp; i++) b.append(i > 0 ? "," : "").append(stack[i]);
        return b.append("]").toString();
    }
}

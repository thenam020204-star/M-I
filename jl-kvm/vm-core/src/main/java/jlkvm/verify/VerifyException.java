package jlkvm.verify;

/** Verification failure; the VM maps it to the guest {@code java.lang.VerifyError} (PLAN D6). */
public final class VerifyException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final String GUEST_CLASS = "java/lang/VerifyError";

    public final String className, methodName, methodDescriptor;
    /** Bytecode offset, or -1 for class/method level problems. */
    public final int pc;
    public final String reason;

    public VerifyException(String cls, String method, String desc, int pc, String reason) {
        super(cls + "." + method + desc + (pc >= 0 ? " @" + pc : "") + ": " + reason, null, false, false);
        this.className = cls;
        this.methodName = method;
        this.methodDescriptor = desc;
        this.pc = pc;
        this.reason = reason;
    }
}

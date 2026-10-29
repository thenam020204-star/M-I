package jlkvm.classfile;

/**
 * Malformed class file. The only exception the parser throws for bad input (PLAN T07); the VM maps it to the
 * guest {@code java.lang.ClassFormatError} (see {@link #GUEST_CLASS}).
 */
public final class ClassFormatException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final String GUEST_CLASS = "java/lang/ClassFormatError";

    public ClassFormatException(String message) {
        super(message, null, false, false);
    }
}

package jlkvm.verify;

/** What the verifier needs to know about other classes (loaded from the app or the ROM). */
public interface ClassHierarchy {
    /** @return true if the class can be found. */
    boolean exists(String internalName);

    /** @return super class name, or null for java/lang/Object. Only called when {@link #exists} is true. */
    String superName(String internalName);

    /** Only called when {@link #exists} is true. */
    boolean isInterface(String internalName);
}

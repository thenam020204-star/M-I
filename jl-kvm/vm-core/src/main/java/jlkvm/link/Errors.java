package jlkvm.link;

/**
 * Error class mapping (PLAN D7, T12 skeleton; docs/error-mapping.md). CLDC 1.1 only has Error, NoClassDefFoundError,
 * OutOfMemoryError and VirtualMachineError (CLDC spec 6.2.7.2); an Error of JVMS that is not in the list is replaced by its
 * nearest superclass that is (CLDC spec 4.2 / 5.1.4). Callers pass the JVMS name; {@link #guest} gives the class to throw.
 */
public final class Errors {
    private Errors() {}

    public static String guest(String jvmsName) {
        if (jvmsName.equals("java/lang/StackOverflowError") || jvmsName.equals("java/lang/InternalError")
                || jvmsName.equals("java/lang/UnknownError")) return "java/lang/VirtualMachineError";
        switch (jvmsName.charAt(jvmsName.length() - 1)) {
            case 'r':
                if (jvmsName.endsWith("Error")
                        && !jvmsName.equals("java/lang/Error")
                        && !jvmsName.equals("java/lang/NoClassDefFoundError")
                        && !jvmsName.equals("java/lang/OutOfMemoryError")
                        && !jvmsName.equals("java/lang/VirtualMachineError")) return "java/lang/Error";
                return jvmsName;
            default:
                return jvmsName;
        }
    }

    /**
     * The VM-raised error {@code jvmsName} as the guest class that CLDC has. When the class changed, the message starts with the
     * simple JVMS name ("NoSuchMethodError: A.m()V") so logs and tests can tell the cases apart (docs/error-mapping.md).
     */
    public static VmThrow error(String jvmsName, String msg) {
        String g = guest(jvmsName);
        if (!g.equals(jvmsName)) msg = jvmsName.substring(jvmsName.lastIndexOf('/') + 1) + (msg == null ? "" : ": " + msg);
        return new VmThrow(g, msg);
    }
    public static VmThrow noClassDef(String name) { return new VmThrow("java/lang/NoClassDefFoundError", name); }
}

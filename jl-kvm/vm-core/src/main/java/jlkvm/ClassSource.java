package jlkvm;

/** Source of class files and resources (a game JAR or the guest ROM). See PLAN "Interface giữa module". */
public interface ClassSource {
    /** @return class file bytes for an internal name such as {@code java/lang/String}, or null if absent. */
    byte[] readClass(String internalName);

    /** @return resource bytes for an absolute path such as {@code /data/a.txt}, or null if absent. */
    byte[] readResource(String absPath);
}

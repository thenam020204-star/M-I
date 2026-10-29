package jlkvm.verify;

import jlkvm.ClassSource;
import jlkvm.classfile.ClassFile;
import jlkvm.classfile.ClassFormatException;
import jlkvm.classfile.ClassParser;
import java.util.HashMap;
import java.util.Map;

/** {@link ClassHierarchy} backed by one or more {@link ClassSource}s (searched in order); results are cached. */
public final class SourceHierarchy implements ClassHierarchy {
    private final ClassSource[] sources;
    private final Map<String, ClassFile> cache = new HashMap<String, ClassFile>();

    public SourceHierarchy(ClassSource... sources) { this.sources = sources; }

    private ClassFile get(String n) {
        if (cache.containsKey(n)) return cache.get(n);
        ClassFile cf = null;
        for (ClassSource s : sources) {
            byte[] b = s.readClass(n);
            if (b != null) {
                try {
                    cf = ClassParser.parse(b);
                } catch (ClassFormatException e) {
                    cf = null;
                }
                break;
            }
        }
        cache.put(n, cf);
        return cf;
    }

    public boolean exists(String n) { return get(n) != null; }
    public String superName(String n) { return get(n).superName; }
    public boolean isInterface(String n) { return get(n).isInterface(); }
}

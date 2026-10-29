package java.lang.ref;

/** TEMPORARY stub ROM class (T13 tests): the VM finds the slot by the field name "referent" and clears it when the target is only weakly reachable. */
public class WeakReference extends Reference {
    private Object referent;
    public WeakReference(Object ref) { referent = ref; }
    public Object get() { return referent; }
    public void clear() { referent = null; }
}

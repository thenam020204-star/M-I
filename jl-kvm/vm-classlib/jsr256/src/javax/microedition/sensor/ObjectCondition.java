/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public final class ObjectCondition implements Condition {
    private final Object limit;

    public ObjectCondition(Object limit) {
        if (limit == null) {
            throw new NullPointerException("limit");
        }
        this.limit = limit;
    }

    public final Object getLimit() {
        return limit;
    }

    public boolean isMet(double value) {
        return false;
    }

    public boolean isMet(Object value) {
        return limit.equals(value);
    }
}

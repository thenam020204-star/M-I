/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public final class LimitCondition implements Condition {
    private final double limit;
    private final String operator;

    public LimitCondition(double limit, String operator) {
        if (operator == null) {
            throw new NullPointerException("operator");
        }
        if (!(operator.equals(OP_EQUALS) || operator.equals(OP_GREATER_THAN) || operator.equals(OP_GREATER_THAN_OR_EQUALS)
                || operator.equals(OP_LESS_THAN) || operator.equals(OP_LESS_THAN_OR_EQUALS))) {
            throw new IllegalArgumentException("operator " + operator);
        }
        this.limit = limit;
        this.operator = operator;
    }

    public final double getLimit() {
        return limit;
    }

    public final String getOperator() {
        return operator;
    }

    public boolean isMet(double value) {
        if (operator.equals(OP_EQUALS)) return value == limit;
        if (operator.equals(OP_GREATER_THAN)) return value > limit;
        if (operator.equals(OP_GREATER_THAN_OR_EQUALS)) return value >= limit;
        if (operator.equals(OP_LESS_THAN)) return value < limit;
        return value <= limit;
    }

    /** A limit condition is numeric; an object value never meets it. */
    public boolean isMet(Object value) {
        return false;
    }
}

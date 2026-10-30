/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public class RangeCondition implements Condition {
    private final double lower, upper;
    private final String lowerOp, upperOp;

    /** lowerOp is OP_GREATER_THAN or OP_GREATER_THAN_OR_EQUALS, upperOp OP_LESS_THAN or OP_LESS_THAN_OR_EQUALS, lowerLimit <= upperLimit, else IllegalArgumentException. */
    public RangeCondition(double lowerLimit, String lowerOp, double upperLimit, String upperOp) {
        if (lowerOp == null || upperOp == null) {
            throw new NullPointerException("operator");
        }
        if (!(lowerOp.equals(OP_GREATER_THAN) || lowerOp.equals(OP_GREATER_THAN_OR_EQUALS)) || !(upperOp.equals(OP_LESS_THAN) || upperOp.equals(OP_LESS_THAN_OR_EQUALS))
                || !(lowerLimit <= upperLimit)) {
            throw new IllegalArgumentException("bad operators or limits");
        }
        lower = lowerLimit;
        this.lowerOp = lowerOp;
        upper = upperLimit;
        this.upperOp = upperOp;
    }

    public double getLowerLimit() {
        return lower;
    }

    public String getLowerOp() {
        return lowerOp;
    }

    public double getUpperLimit() {
        return upper;
    }

    public String getUpperOp() {
        return upperOp;
    }

    public boolean isMet(double value) {
        boolean lo = lowerOp.equals(OP_GREATER_THAN) ? value > lower : value >= lower;
        boolean hi = upperOp.equals(OP_LESS_THAN) ? value < upper : value <= upper;
        return lo && hi;
    }

    public boolean isMet(Object value) {
        return false;
    }
}

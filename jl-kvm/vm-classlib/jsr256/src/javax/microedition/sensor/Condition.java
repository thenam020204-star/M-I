/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public interface Condition {
    String OP_EQUALS = "eq";
    String OP_GREATER_THAN = "gt";
    String OP_GREATER_THAN_OR_EQUALS = "ge";
    String OP_LESS_THAN = "lt";
    String OP_LESS_THAN_OR_EQUALS = "le";
    boolean isMet(double a0);
    boolean isMet(Object a0);
}

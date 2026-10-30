/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public interface ConditionListener {
    void conditionMet(javax.microedition.sensor.SensorConnection a0, javax.microedition.sensor.Data a1, javax.microedition.sensor.Condition a2);
}

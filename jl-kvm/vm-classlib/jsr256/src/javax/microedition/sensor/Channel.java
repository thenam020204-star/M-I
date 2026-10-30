/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public interface Channel {
    void addCondition(javax.microedition.sensor.ConditionListener a0, javax.microedition.sensor.Condition a1);
    javax.microedition.sensor.ChannelInfo getChannelInfo();
    String getChannelUrl();
    javax.microedition.sensor.Condition[] getConditions(javax.microedition.sensor.ConditionListener a0);
    void removeAllConditions();
    void removeCondition(javax.microedition.sensor.ConditionListener a0, javax.microedition.sensor.Condition a1);
    void removeConditionListener(javax.microedition.sensor.ConditionListener a0);
}

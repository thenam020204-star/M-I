/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public interface DataAndErrorListener extends javax.microedition.sensor.DataListener {
    void errorReceived(javax.microedition.sensor.SensorConnection a0, int a1, long a2);
}

/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public interface ChannelInfo {
    int TYPE_DOUBLE = 1;
    int TYPE_INT = 2;
    int TYPE_OBJECT = 4;
    float getAccuracy();
    int getDataType();
    javax.microedition.sensor.MeasurementRange[] getMeasurementRanges();
    String getName();
    int getScale();
    javax.microedition.sensor.Unit getUnit();
}

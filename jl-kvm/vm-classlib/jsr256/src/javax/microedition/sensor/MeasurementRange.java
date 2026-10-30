/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public class MeasurementRange {
    private final double smallest, largest, resolution;

    public MeasurementRange(double smallestValue, double largestValue, double resolution) {
        if (!(smallestValue <= largestValue) || !(resolution >= 0.0)) {
            throw new IllegalArgumentException("smallestValue > largestValue or resolution < 0");
        }
        this.smallest = smallestValue;
        this.largest = largestValue;
        this.resolution = resolution;
    }

    public double getSmallestValue() {
        return smallest;
    }

    public double getLargestValue() {
        return largest;
    }

    public double getResolution() {
        return resolution;
    }
}

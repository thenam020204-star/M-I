/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.location;

public class QualifiedCoordinates extends Coordinates {
    private float horizontalAccuracy, verticalAccuracy;

    public QualifiedCoordinates(double latitude, double longitude, float altitude, float horizontalAccuracy, float verticalAccuracy) {
        super(latitude, longitude, altitude);
        this.horizontalAccuracy = horizontalAccuracy;
        this.verticalAccuracy = verticalAccuracy;
    }

    public float getHorizontalAccuracy() {
        return horizontalAccuracy;
    }

    public float getVerticalAccuracy() {
        return verticalAccuracy;
    }

    /** IllegalArgumentException if negative (NaN = unknown is allowed). */
    public void setHorizontalAccuracy(float horizontalAccuracy) {
        if (horizontalAccuracy < 0.0f) {
            throw new IllegalArgumentException("horizontalAccuracy");
        }
        this.horizontalAccuracy = horizontalAccuracy;
    }

    public void setVerticalAccuracy(float verticalAccuracy) {
        if (verticalAccuracy < 0.0f) {
            throw new IllegalArgumentException("verticalAccuracy");
        }
        this.verticalAccuracy = verticalAccuracy;
    }

    public boolean equals(Object o) {
        if (!(o instanceof QualifiedCoordinates)) {
            return false;
        }
        QualifiedCoordinates q = (QualifiedCoordinates) o;
        return q.getLatitude() == getLatitude() && q.getLongitude() == getLongitude() && q.getAltitude() == getAltitude()
                && q.horizontalAccuracy == horizontalAccuracy && q.verticalAccuracy == verticalAccuracy;
    }

    public int hashCode() {
        long a = Double.doubleToLongBits(getLatitude()) ^ Double.doubleToLongBits(getLongitude());
        return (int) (a ^ (a >>> 32)) ^ Float.floatToIntBits(getAltitude());
    }
}

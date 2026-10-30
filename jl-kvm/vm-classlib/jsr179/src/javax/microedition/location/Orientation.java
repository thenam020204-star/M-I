/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.location;

public class Orientation {
    private final float azimuth, pitch, roll;
    private final boolean magnetic;

    public Orientation(float azimuth, boolean isMagnetic, float pitch, float roll) {
        if (!(azimuth != azimuth) && (azimuth < 0.0f || azimuth >= 360.0f)) {
            throw new IllegalArgumentException("azimuth");
        }
        if (!(pitch != pitch) && (pitch < -90.0f || pitch > 90.0f)) {
            throw new IllegalArgumentException("pitch");
        }
        if (!(roll != roll) && (roll < -180.0f || roll >= 180.0f)) {
            throw new IllegalArgumentException("roll");
        }
        this.azimuth = azimuth;
        this.magnetic = isMagnetic;
        this.pitch = pitch;
        this.roll = roll;
    }

    public float getCompassAzimuth() {
        return azimuth;
    }

    public boolean isOrientationMagnetic() {
        return magnetic;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    /** No compass / orientation sensor: null ("the orientation cannot be determined"). */
    public static Orientation getOrientation() throws LocationException {
        return null;
    }
}

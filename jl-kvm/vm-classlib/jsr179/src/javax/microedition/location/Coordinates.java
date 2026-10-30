/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.location;

import jlkvm.internal.loc.Geo;

public class Coordinates {
    public static final int DD_MM_SS = 1;
    public static final int DD_MM = 2;

    private double latitude, longitude;
    private float altitude;

    public Coordinates(double latitude, double longitude, float altitude) {
        setLatitude(latitude);
        setLongitude(longitude);
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    /** IllegalArgumentException if outside [-90, 90] or NaN. */
    public void setLatitude(double latitude) {
        if (!(latitude >= -90.0 && latitude <= 90.0)) {
            throw new IllegalArgumentException("latitude " + latitude);
        }
        this.latitude = latitude;
    }

    /** IllegalArgumentException if outside [-180, 180) or NaN (180 itself is refused, like the javadoc range [-180.0, 180.0)). */
    public void setLongitude(double longitude) {
        if (!(longitude >= -180.0 && longitude < 180.0)) {
            throw new IllegalArgumentException("longitude " + longitude);
        }
        this.longitude = longitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public static String convert(double coordinate, int outputType) {
        return Geo.format(coordinate, outputType);
    }

    public static double convert(String coordinate) {
        return Geo.parse(coordinate);
    }

    /** Initial great-circle bearing in degrees [0, 360). */
    public float azimuthTo(Coordinates to) {
        if (to == null) {
            throw new NullPointerException("to");
        }
        return (float) Geo.azimuth(latitude, longitude, to.latitude, to.longitude);
    }

    /** Great-circle (spherical, radius 6378137 m) distance in meters; altitude is not used. */
    public float distance(Coordinates to) {
        if (to == null) {
            throw new NullPointerException("to");
        }
        return (float) Geo.distance(latitude, longitude, to.latitude, to.longitude);
    }
}

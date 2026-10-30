/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.location;

public class Criteria {
    public static final int NO_REQUIREMENT = 0;
    public static final int POWER_USAGE_LOW = 1;
    public static final int POWER_USAGE_MEDIUM = 2;
    public static final int POWER_USAGE_HIGH = 3;

    private int horizontalAccuracy = NO_REQUIREMENT, verticalAccuracy = NO_REQUIREMENT, responseTime = NO_REQUIREMENT, power = NO_REQUIREMENT;
    private boolean costAllowed = true, speedAndCourse, altitude, address;

    public Criteria() {
    }

    public void setHorizontalAccuracy(int accuracy) {
        horizontalAccuracy = accuracy;
    }

    public int getHorizontalAccuracy() {
        return horizontalAccuracy;
    }

    public void setVerticalAccuracy(int accuracy) {
        verticalAccuracy = accuracy;
    }

    public int getVerticalAccuracy() {
        return verticalAccuracy;
    }

    public void setPreferredResponseTime(int time) {
        responseTime = time;
    }

    public int getPreferredResponseTime() {
        return responseTime;
    }

    /** IllegalArgumentException unless NO_REQUIREMENT or POWER_USAGE_LOW / MEDIUM / HIGH. */
    public void setPreferredPowerConsumption(int level) {
        if (level < NO_REQUIREMENT || level > POWER_USAGE_HIGH) {
            throw new IllegalArgumentException("level " + level);
        }
        power = level;
    }

    public int getPreferredPowerConsumption() {
        return power;
    }

    public void setCostAllowed(boolean costAllowed) {
        this.costAllowed = costAllowed;
    }

    public boolean isAllowedToCost() {
        return costAllowed;
    }

    public void setSpeedAndCourseRequired(boolean speedAndCourseRequired) {
        speedAndCourse = speedAndCourseRequired;
    }

    public boolean isSpeedAndCourseRequired() {
        return speedAndCourse;
    }

    public void setAltitudeRequired(boolean altitudeRequired) {
        altitude = altitudeRequired;
    }

    public boolean isAltitudeRequired() {
        return altitude;
    }

    public void setAddressInfoRequired(boolean addressInfoRequired) {
        address = addressInfoRequired;
    }

    public boolean isAddressInfoRequired() {
        return address;
    }
}

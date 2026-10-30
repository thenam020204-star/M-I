/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.location;

/**
 * No position source (nothing here talks to a receiver; the Android host plugs one in at M6, not done): getInstance finds no provider and answers
 * null, "null if no provider ... can be found", so the application falls back to its no-GPS path.
 */
public abstract class LocationProvider {
    public static final int AVAILABLE = 1;
    public static final int TEMPORARILY_UNAVAILABLE = 2;
    public static final int OUT_OF_SERVICE = 3;

    protected LocationProvider() {
    }

    public static LocationProvider getInstance(Criteria criteria) throws LocationException {
        return null;
    }

    public abstract int getState();

    public abstract Location getLocation(int timeout) throws LocationException, InterruptedException;

    public abstract void setLocationListener(LocationListener listener, int interval, int timeout, int maxAge);

    public abstract void reset();

    public static Location getLastKnownLocation() {
        return null;
    }

    /** "LocationException - if the platform does not have proximity monitoring support": none has. */
    public static void addProximityListener(ProximityListener listener, Coordinates coordinates, float proximityRadius) throws LocationException {
        if (listener == null || coordinates == null) {
            throw new NullPointerException("listener or coordinates");
        }
        if (!(proximityRadius > 0.0f)) {
            throw new IllegalArgumentException("proximityRadius");
        }
        throw new LocationException("no proximity monitoring on this device");
    }

    public static void removeProximityListener(ProximityListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
    }
}

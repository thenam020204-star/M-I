/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

import java.util.Vector;

/**
 * No sensor at all (the host would register some, Android M6, NOT done): findSensors answers an empty array, which is what the javadoc says for
 * "no sensor matches", and the listeners are kept but never called.
 */
public class SensorManager {
    private static final Vector listeners = new Vector();

    private SensorManager() {
    }

    public static SensorInfo[] findSensors(String quantity, String contextType) {
        if (quantity == null) {
            throw new NullPointerException("quantity");
        }
        return new SensorInfo[0];
    }

    /** url = "sensor:quantity[;contextType=...][;model=...][;location=...]"; IllegalArgumentException if it is not of that form. */
    public static SensorInfo[] findSensors(String url) {
        if (url == null) {
            throw new NullPointerException("url");
        }
        if (!url.startsWith("sensor:") || url.length() == 7 || url.charAt(7) == ';') {
            throw new IllegalArgumentException("not a sensor URL: " + url);
        }
        return new SensorInfo[0];
    }

    public static void addSensorListener(SensorListener listener, SensorInfo info) {
        if (listener == null || info == null) {
            throw new NullPointerException("listener or info");
        }
        add(listener);
    }

    public static void addSensorListener(SensorListener listener, String quantity) {
        if (listener == null || quantity == null) {
            throw new NullPointerException("listener or quantity");
        }
        add(listener);
    }

    private static void add(SensorListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.addElement(l);
            }
        }
    }

    public static void removeSensorListener(SensorListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        synchronized (listeners) {
            listeners.removeElement(listener);
        }
    }
}

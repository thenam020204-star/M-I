/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

import java.util.Hashtable;

public class Unit {
    private static final Hashtable units = new Hashtable();

    private final String name;

    private Unit(String name) {
        this.name = name;
    }

    /** One Unit object per name (so == works), as the javadoc of the factory says. */
    public static Unit getUnit(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        synchronized (units) {
            Unit u = (Unit) units.get(name);
            if (u == null) {
                u = new Unit(name);
                units.put(name, u);
            }
            return u;
        }
    }

    public String toString() {
        return name;
    }
}

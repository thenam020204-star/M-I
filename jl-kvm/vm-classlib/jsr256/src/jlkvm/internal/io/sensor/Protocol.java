/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package jlkvm.internal.io.sensor;

import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import jlkvm.internal.io.ConnectionBase;

/** Handler of the scheme "sensor": no sensor exists (SensorManager.findSensors is always empty), so no URL names one. */
public class Protocol implements ConnectionBase {
    public Connection openPrim(String name, int mode, boolean timeouts) throws IOException {
        throw new ConnectionNotFoundException("no such sensor: " + name);
    }
}

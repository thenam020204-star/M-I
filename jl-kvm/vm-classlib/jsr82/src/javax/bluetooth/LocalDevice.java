/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.bluetooth;

import javax.microedition.io.Connection;
import jlkvm.internal.bt.Hw;

public class LocalDevice {
    private static LocalDevice instance;

    private DiscoveryAgent agent;
    private int discoverable = DiscoveryAgent.NOT_DISCOVERABLE;

    LocalDevice() {
    }

    /**
     * "never returns null", "BluetoothStateException - if the Bluetooth system could not be initialized": a device without Bluetooth hardware cannot
     * initialise it, so every caller gets the exception (profile bluetooth.mode "none", the default).
     */
    public static LocalDevice getLocalDevice() throws BluetoothStateException {
        if (!Hw.present()) {
            throw Hw.noRadio();
        }
        synchronized (LocalDevice.class) {
            if (instance == null) {
                instance = new LocalDevice();
            }
            return instance;
        }
    }

    public DiscoveryAgent getDiscoveryAgent() {
        synchronized (this) {
            if (agent == null) {
                agent = new DiscoveryAgent();
            }
            return agent;
        }
    }

    public String getFriendlyName() {
        return Hw.name0();
    }

    /** Class of device of a phone (major 0x200, minor 0x0C = smart phone, no service classes): the project's choice, UNVERIFIED. */
    public DeviceClass getDeviceClass() {
        return new DeviceClass(0x20020C);
    }

    public boolean setDiscoverable(int mode) throws BluetoothStateException {
        if (mode != DiscoveryAgent.GIAC && mode != DiscoveryAgent.LIAC && mode != DiscoveryAgent.NOT_DISCOVERABLE
                && (mode < 0x9E8B00 || mode > 0x9E8B3F)) {
            throw new IllegalArgumentException("mode " + mode);
        }
        synchronized (this) {
            discoverable = mode;
        }
        return true;
    }

    public int getDiscoverable() {
        synchronized (this) {
            return discoverable;
        }
    }

    /** Null for a device without Bluetooth ("null if the property is not defined"); the values of a simulated adapter are the project's own, UNVERIFIED. */
    public static String getProperty(String property) {
        if (property == null || !Hw.present()) {
            return null;
        }
        if (property.equals("bluetooth.api.version")) return "1.0";
        if (property.equals("bluetooth.master.switch")) return "false";
        if (property.equals("bluetooth.sd.attr.retrievable.max")) return "8";
        if (property.equals("bluetooth.connected.devices.max")) return "7";
        if (property.equals("bluetooth.l2cap.receiveMTU.max")) return "672";
        if (property.equals("bluetooth.sd.trans.max")) return "1";
        if (property.equals("bluetooth.connected.inquiry.scan")) return "false";
        if (property.equals("bluetooth.connected.page.scan")) return "false";
        if (property.equals("bluetooth.connected.inquiry")) return "false";
        if (property.equals("bluetooth.connected.page")) return "false";
        return null;
    }

    public String getBluetoothAddress() {
        return Hw.address0();
    }

    /** No Bluetooth notifier can exist (Connector.open of a server URL is refused), so no connection is a Bluetooth notifier. */
    public ServiceRecord getRecord(Connection notifier) {
        if (notifier == null) {
            throw new NullPointerException("notifier");
        }
        throw new IllegalArgumentException("not a Bluetooth notifier");
    }

    public void updateRecord(ServiceRecord srvRecord) throws IllegalArgumentException, NullPointerException, ServiceRegistrationException {
        if (srvRecord == null) {
            throw new NullPointerException("srvRecord");
        }
        throw new IllegalArgumentException("srvRecord was not obtained from getRecord()");
    }
}

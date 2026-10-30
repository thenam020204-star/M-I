package vmspec;

import java.io.IOException;
import javax.bluetooth.BluetoothConnectionException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.midlet.MIDlet;

/** T32 (JSR-82): profile bluetooth.mode simulate = one lone adapter with no peers: discovery finds nothing, no device can be reached. */
public class Bt03Sim extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    static Thread mainThread;

    static class L implements DiscoveryListener {
        int found, inquiryCode = -1, searchCode = -1, searchTrans = -1, services;
        boolean otherThread = true;

        public synchronized void deviceDiscovered(RemoteDevice d, DeviceClass c) { found++; }

        public synchronized void inquiryCompleted(int type) {
            otherThread &= Thread.currentThread() != mainThread;
            inquiryCode = type;
            notifyAll();
        }

        public synchronized void serviceSearchCompleted(int trans, int type) {
            otherThread &= Thread.currentThread() != mainThread;
            searchTrans = trans;
            searchCode = type;
            notifyAll();
        }

        public synchronized void servicesDiscovered(int trans, ServiceRecord[] r) { services++; }

        synchronized int awaitInquiry() throws InterruptedException {
            long end = System.currentTimeMillis() + 10000;
            while (inquiryCode < 0 && System.currentTimeMillis() < end) wait(100);
            return inquiryCode;
        }

        synchronized int awaitSearch() throws InterruptedException {
            long end = System.currentTimeMillis() + 10000;
            while (searchCode < 0 && System.currentTimeMillis() < end) wait(100);
            return searchCode;
        }
    }

    static class Other implements javax.microedition.io.Connection {
        public void close() {}
    }

    static class Peer extends RemoteDevice {
        Peer(String a) { super(a); }
    }

    protected void startApp() {
        mainThread = Thread.currentThread();
        try {
            LocalDevice ld = LocalDevice.getLocalDevice();
            p("same instance: " + (ld == LocalDevice.getLocalDevice()));
            p("address: " + ld.getBluetoothAddress() + " name: " + ld.getFriendlyName());
            p("class: " + (ld.getDeviceClass() != null) + " agent same: " + (ld.getDiscoveryAgent() == ld.getDiscoveryAgent()) + " " + (ld.getDiscoveryAgent() != null));
            p("discoverable at start: " + ld.getDiscoverable());
            p("set GIAC: " + ld.setDiscoverable(DiscoveryAgent.GIAC) + " now " + Integer.toHexString(ld.getDiscoverable()));
            p("set 0x9E8B10: " + ld.setDiscoverable(0x9E8B10) + " now " + Integer.toHexString(ld.getDiscoverable()));
            p("set off: " + ld.setDiscoverable(DiscoveryAgent.NOT_DISCOVERABLE) + " now " + ld.getDiscoverable());
            try { ld.setDiscoverable(5); p("set 5: ok"); } catch (Throwable t) { p("set 5: " + cn(t)); }
            try { ld.setDiscoverable(0x9E8B40); p("set 0x9E8B40: ok"); } catch (Throwable t) { p("set 0x9E8B40: " + cn(t)); }
            p("api.version: " + LocalDevice.getProperty("bluetooth.api.version") + " master.switch: " + LocalDevice.getProperty("bluetooth.master.switch")
                    + " unknown: " + LocalDevice.getProperty("x.y") + " null: " + LocalDevice.getProperty(null));
            try { ld.getRecord(null); } catch (Throwable t) { p("getRecord(null): " + cn(t)); }
            try { ld.getRecord(new Other()); } catch (Throwable t) { p("getRecord(other): " + cn(t)); }
            try { ld.updateRecord(null); } catch (Throwable t) { p("updateRecord(null): " + cn(t)); }

            DiscoveryAgent da = ld.getDiscoveryAgent();
            p("retrieve CACHED: " + da.retrieveDevices(DiscoveryAgent.CACHED) + " PREKNOWN: " + da.retrieveDevices(DiscoveryAgent.PREKNOWN));
            try { da.retrieveDevices(2); p("retrieve 2: ok"); } catch (Throwable t) { p("retrieve 2: " + cn(t)); }
            try { da.startInquiry(DiscoveryAgent.GIAC, null); } catch (Throwable t) { p("inquiry null listener: " + cn(t)); }
            L l = new L();
            try { da.startInquiry(7, l); } catch (Throwable t) { p("inquiry bad code: " + cn(t)); }
            p("start inquiry: " + da.startInquiry(DiscoveryAgent.GIAC, l) + " completed yet: " + (l.inquiryCode >= 0));
            try { da.startInquiry(DiscoveryAgent.LIAC, new L()); p("second inquiry: ok"); } catch (Throwable t) { p("second inquiry: " + cn(t)); }
            p("cancel other listener: " + da.cancelInquiry(new L()));
            try { da.cancelInquiry(null); } catch (Throwable t) { p("cancel null: " + cn(t)); }
            p("inquiry result: " + l.awaitInquiry() + " devices " + l.found + " other thread " + l.otherThread);
            p("cancel finished inquiry: " + da.cancelInquiry(l));
            L l2 = new L();
            da.startInquiry(DiscoveryAgent.GIAC, l2);
            p("cancel running inquiry: " + da.cancelInquiry(l2) + " again " + da.cancelInquiry(l2));
            p("terminated result: " + l2.awaitInquiry() + " other thread " + l2.otherThread);

            UUID[] uu = {new UUID(0x1101L)};
            RemoteDevice peer = new Peer("aabbccddeeff");
            p("peer: " + peer.getBluetoothAddress() + " equals " + peer.equals(new Peer("AABBCCDDEEFF")) + " " + peer.equals(new Peer("AABBCCDDEE00")) + " trusted " + peer.isTrustedDevice()
                    + " auth " + peer.isAuthenticated() + " enc " + peer.isEncrypted());
            try { new Peer("short"); } catch (Throwable t) { p("peer short: " + cn(t)); }
            try { new Peer("00112233AAXX"); } catch (Throwable t) { p("peer nonhex: " + cn(t)); }
            try { new Peer(null); } catch (Throwable t) { p("peer null: " + cn(t)); }
            try { new Peer("00112233AABB"); } catch (Throwable t) { p("peer = local: " + cn(t)); }
            p("name cached: " + peer.getFriendlyName(false));
            try { peer.getFriendlyName(true); p("name asked: ok"); } catch (Throwable t) { p("name asked: " + cn(t)); }
            try { peer.authenticate(); p("authenticate: ok"); } catch (Throwable t) { p("authenticate: " + cn(t)); }
            try { RemoteDevice.getRemoteDevice(new Other()); } catch (Throwable t) { p("getRemoteDevice(other): " + cn(t)); }
            try { da.searchServices(null, new UUID[0], peer, new L()); } catch (Throwable t) { p("search empty uuids: " + cn(t)); }
            try { da.searchServices(null, uu, null, new L()); } catch (Throwable t) { p("search null device: " + cn(t)); }
            try { da.searchServices(new int[0], uu, peer, new L()); } catch (Throwable t) { p("search empty attrs: " + cn(t)); }
            try { da.searchServices(new int[]{1, 1}, uu, peer, new L()); } catch (Throwable t) { p("search duplicate attrs: " + cn(t)); }
            try { da.searchServices(new int[]{0x10000}, uu, peer, new L()); } catch (Throwable t) { p("search attr id too big: " + cn(t)); }
            try { da.searchServices(null, new UUID[]{uu[0], new UUID(0x1101L)}, peer, new L()); } catch (Throwable t) { p("search duplicate uuids: " + cn(t)); }
            try { da.searchServices(null, new UUID[]{null}, peer, new L()); } catch (Throwable t) { p("search null uuid: " + cn(t)); }
            L l3 = new L();
            int id = da.searchServices(new int[]{0x100}, uu, peer, l3);
            p("search id positive: " + (id > 0));
            try { da.searchServices(null, uu, peer, new L()); p("second search: ok"); } catch (Throwable t) { p("second search: " + cn(t)); }
            p("search result: " + l3.awaitSearch() + " id matches " + (l3.searchTrans == id) + " services " + l3.services + " other thread " + l3.otherThread);
            p("cancel finished search: " + da.cancelServiceSearch(id));
            L l4 = new L();
            int id2 = da.searchServices(null, uu, peer, l4);
            p("cancel search: " + da.cancelServiceSearch(id2) + " wrong id " + da.cancelServiceSearch(id2 + 100));
            p("terminated search: " + l4.awaitSearch());
            p("selectService: " + da.selectService(uu[0], ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false));
            try { da.selectService(uu[0], 9, false); } catch (Throwable t) { p("selectService bad security: " + cn(t)); }
            try { da.selectService(null, 0, false); } catch (Throwable t) { p("selectService null: " + cn(t)); }

            try { Connector.open("btspp://AABBCCDDEEFF:1"); p("client open: opened"); } catch (Throwable t) {
                p("client open: " + cn(t) + (t instanceof BluetoothConnectionException ? " status " + ((BluetoothConnectionException) t).getStatus() : ""));
            }
            try { Connector.open("btl2cap://AABBCCDDEEFF:1003"); p("l2cap client open: opened"); } catch (Throwable t) { p("l2cap client open: " + cn(t)); }
            try { Connector.open("btspp://localhost:00001101000010008000006057029D35;name=x"); p("server open: opened"); } catch (Throwable t) { p("server open: " + cn(t)); }
        } catch (Throwable t) {
            p("unexpected: " + t);
        }
        notifyDestroyed();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}

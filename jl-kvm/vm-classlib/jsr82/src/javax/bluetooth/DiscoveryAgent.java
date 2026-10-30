/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.bluetooth;

/**
 * Discovery on the one lone adapter of the simulated hardware (profile bluetooth.mode "simulate"): there are no peers, so an inquiry runs for a
 * fixed time and ends with INQUIRY_COMPLETED without a deviceDiscovered, and a service search ends with SERVICE_SEARCH_DEVICE_NOT_REACHABLE. The
 * events come from a guest thread (the two public inner thread classes of the SDK signature), never inside the call that started them.
 */
public class DiscoveryAgent {
    public static final int NOT_DISCOVERABLE = 0;
    public static final int GIAC = 10390323;
    public static final int LIAC = 10390272;
    public static final int PREKNOWN = 1;
    public static final int CACHED = 0;

    /** Length of a simulated inquiry in virtual milliseconds (the real length is implementation dependent, UNVERIFIED). */
    private static final int INQUIRY_MILLIS = 1000;
    private static final int SEARCH_MILLIS = 200;

    private final Object lock = new Object();
    private InquiryListenerThread inquiry;
    private ServiceSearchListenerThread search;
    private int nextTrans = 1;

    DiscoveryAgent() {
    }

    public RemoteDevice[] retrieveDevices(int option) {
        if (option != CACHED && option != PREKNOWN) {
            throw new IllegalArgumentException("option " + option);
        }
        return null;
    }

    public boolean startInquiry(int accessCode, DiscoveryListener listener) throws IllegalArgumentException, NullPointerException, BluetoothStateException {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        if (accessCode != GIAC && accessCode != LIAC && (accessCode < 0x9E8B00 || accessCode > 0x9E8B3F)) {
            throw new IllegalArgumentException("accessCode " + accessCode);
        }
        InquiryListenerThread t;
        synchronized (lock) {
            if (inquiry != null) {
                throw new BluetoothStateException("an inquiry is already running");
            }
            t = new InquiryListenerThread(accessCode, listener);
            inquiry = t;
        }
        t.start();
        return true;
    }

    public boolean cancelInquiry(DiscoveryListener listener) throws NullPointerException {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        synchronized (lock) {
            if (inquiry == null || inquiry.getListener() != listener || inquiry.cancelled) {
                return false;
            }
            inquiry.cancelled = true;
            lock.notifyAll();
            return true;
        }
    }

    public int searchServices(int[] attrSet, UUID[] uuidSet, RemoteDevice btDev, DiscoveryListener discListener)
            throws IllegalArgumentException, NullPointerException, BluetoothStateException {
        if (uuidSet == null || btDev == null || discListener == null) {
            throw new NullPointerException("uuidSet, btDev or discListener is null");
        }
        for (int i = 0; i < uuidSet.length; i++) {
            if (uuidSet[i] == null) {
                throw new NullPointerException("uuidSet[" + i + "]");
            }
        }
        if (uuidSet.length == 0 || (attrSet != null && attrSet.length == 0)) {
            throw new IllegalArgumentException("attrSet or uuidSet is empty");
        }
        for (int i = 0; i < uuidSet.length; i++) {
            for (int j = 0; j < i; j++) {
                if (uuidSet[i].equals(uuidSet[j])) {
                    throw new IllegalArgumentException("duplicate UUID");
                }
            }
        }
        if (attrSet != null) {
            if (attrSet.length > 8) {
                throw new IllegalArgumentException("more than bluetooth.sd.attr.retrievable.max (8) attributes");
            }
            for (int i = 0; i < attrSet.length; i++) {
                if (attrSet[i] < 0 || attrSet[i] > 0xFFFF) {
                    throw new IllegalArgumentException("attribute id " + attrSet[i]);
                }
                for (int j = 0; j < i; j++) {
                    if (attrSet[i] == attrSet[j]) {
                        throw new IllegalArgumentException("duplicate attribute id");
                    }
                }
            }
        }
        ServiceSearchListenerThread t;
        synchronized (lock) {
            if (search != null) {
                throw new BluetoothStateException("bluetooth.sd.trans.max (1) service searches are already running");
            }
            int id = nextTrans++;
            t = new ServiceSearchListenerThread(btDev, id, uuidSet.length, discListener);
            search = t;
        }
        t.start();
        return t.getTransID();
    }

    public boolean cancelServiceSearch(int transID) {
        synchronized (lock) {
            if (search == null || search.getTransID() != transID || search.cancelled) {
                return false;
            }
            search.cancelled = true;
            lock.notifyAll();
            return true;
        }
    }

    public String selectService(UUID uuid, int security, boolean master) throws IllegalArgumentException, NullPointerException, BluetoothStateException {
        if (uuid == null) {
            throw new NullPointerException("uuid");
        }
        if (security != ServiceRecord.NOAUTHENTICATE_NOENCRYPT && security != ServiceRecord.AUTHENTICATE_NOENCRYPT
                && security != ServiceRecord.AUTHENTICATE_ENCRYPT) {
            throw new IllegalArgumentException("security " + security);
        }
        return null;
    }

    /** Waits up to millis of virtual time or until the activity is cancelled; true when it was cancelled. */
    private boolean pause(int millis, boolean isInquiry) {
        long end = System.currentTimeMillis() + millis;
        synchronized (lock) {
            while (!(isInquiry ? inquiry.cancelled : search.cancelled)) {
                long left = end - System.currentTimeMillis();
                if (left <= 0) {
                    return false;
                }
                try {
                    lock.wait(left);
                } catch (InterruptedException e) {
                    return false;
                }
            }
            return true;
        }
    }

    public class InquiryListenerThread extends Thread {
        private final DiscoveryListener listener;
        boolean cancelled;

        public InquiryListenerThread(int accessCode, DiscoveryListener listener) {
            this.listener = listener;
        }

        public DiscoveryListener getListener() {
            return listener;
        }

        public void run() {
            boolean stopped = pause(INQUIRY_MILLIS, true);
            synchronized (lock) {
                inquiry = null;
            }
            try {
                listener.inquiryCompleted(stopped ? DiscoveryListener.INQUIRY_TERMINATED : DiscoveryListener.INQUIRY_COMPLETED);
            } catch (Throwable t) {
                // an exception of the application's listener must not kill the agent
            }
        }
    }

    public class ServiceSearchListenerThread extends Thread {
        private final RemoteDevice device;
        private final int transID;
        private final DiscoveryListener listener;
        boolean cancelled;

        public ServiceSearchListenerThread(RemoteDevice device, int transID, int uuidCount, DiscoveryListener listener) {
            this.device = device;
            this.transID = transID;
            this.listener = listener;
        }

        public int getTransID() {
            return transID;
        }

        public void run() {
            boolean stopped = pause(SEARCH_MILLIS, false);
            synchronized (lock) {
                search = null;
            }
            try {
                listener.serviceSearchCompleted(transID, stopped ? DiscoveryListener.SERVICE_SEARCH_TERMINATED
                        : DiscoveryListener.SERVICE_SEARCH_DEVICE_NOT_REACHABLE);
            } catch (Throwable t) {
                // see above
            }
        }
    }
}

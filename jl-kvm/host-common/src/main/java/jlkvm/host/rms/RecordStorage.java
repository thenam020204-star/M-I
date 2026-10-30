package jlkvm.host.rms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import jlkvm.DeviceProfile;

/**
 * Host side of the Record Management System (PLAN T26): the persistent record stores of every MIDlet suite, the per-suite quota
 * and the byte accounting of the profile (docs/midp.md section 8). The guest ({@code javax.microedition.rms}) keeps the MIDP rules
 * (arguments, exceptions, enumerations, listeners, reference counting); this class only stores bytes and decides "full".
 * A subclass supplies persistence ({@link #loadSuite}, {@link #saveStore}, {@link #deleteStore}); the base class is the in-memory
 * store. Thread-safe (methods are synchronized): the vm-thread uses it, an importer may use it from another thread.
 */
public abstract class RecordStorage {
    /** Thrown when the quota of the suite would be exceeded. */
    public static final class Full extends Exception {
        private static final long serialVersionUID = 1L;
        Full(String m) { super(m); }
    }

    /** One record store. Fields are read through the accessors; they change only through {@link RecordStorage}. */
    public static final class Store {
        final String name;
        int authmode;
        boolean writable;
        int version;
        long lastModified;
        int nextId = 1;
        final TreeMap<Integer, byte[]> records = new TreeMap<Integer, byte[]>();
        long dataBytes;

        public Store(String name, int authmode, boolean writable) { this.name = name; this.authmode = authmode; this.writable = writable; }
        public String name() { return name; }
        public int authmode() { return authmode; }
        public boolean writable() { return writable; }
        public int version() { return version; }
        public long lastModified() { return lastModified; }
        public int nextId() { return nextId; }
        public int numRecords() { return records.size(); }
        /** Record data (not copied), null when the record does not exist. */
        public byte[] get(int id) { return records.get(Integer.valueOf(id)); }
        /** Record ids in ascending order. */
        public int[] ids() {
            int[] r = new int[records.size()];
            int i = 0;
            for (Integer k : records.keySet()) r[i++] = k.intValue();
            return r;
        }
        /** Adds a record with a given id during an import (no quota check). */
        public void restore(int id, byte[] data) {
            byte[] old = records.put(Integer.valueOf(id), data);
            if (old != null) dataBytes -= old.length;
            dataBytes += data.length;
            if (id >= nextId) nextId = id + 1;
        }
        public void restoreHeader(int version, long lastModified, int nextId) { this.version = version; this.lastModified = lastModified; if (nextId > this.nextId) this.nextId = nextId; }
    }

    protected final int quota, storeOverhead, recordOverhead;
    private final Map<String, Map<String, Store>> suites = new HashMap<String, Map<String, Store>>();

    protected RecordStorage(DeviceProfile p) {
        this.quota = p.rmsQuotaBytes;
        this.storeOverhead = p.rmsStoreOverheadBytes;
        this.recordOverhead = p.rmsRecordOverheadBytes;
    }

    // ---- persistence hooks (called with the monitor held)
    /** Fills {@code into} with the stores kept for {@code suite} (once per suite, on first use). */
    protected void loadSuite(String suite, Map<String, Store> into) { }
    /** Writes the whole store durably (after every change). */
    protected void saveStore(String suite, Store s) { }
    protected void deleteStore(String suite, String name) { }

    private Map<String, Store> suite(String suite) {
        Map<String, Store> m = suites.get(suite);
        if (m == null) {
            m = new HashMap<String, Store>();
            suites.put(suite, m);
            loadSuite(suite, m);
        }
        return m;
    }

    public static int sizeOf(Store s, int storeOverhead, int recordOverhead) {
        long n = storeOverhead + s.dataBytes + (long) recordOverhead * s.records.size();
        return n > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) n;
    }

    public synchronized int size(Store s) { return sizeOf(s, storeOverhead, recordOverhead); }

    private long used(Map<String, Store> m) {
        long u = 0;
        for (Store s : m.values()) u += storeOverhead + s.dataBytes + (long) recordOverhead * s.records.size();
        return u;
    }

    /** Bytes the suite can still add (quota minus everything its stores occupy), never negative. */
    public synchronized int available(String suite) {
        long a = quota - used(suite(suite));
        return a < 0 ? 0 : a > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) a;
    }

    /** Names of the stores of the suite, sorted. */
    public synchronized List<String> list(String suite) {
        List<String> r = new ArrayList<String>(suite(suite).keySet());
        java.util.Collections.sort(r);
        return r;
    }

    public synchronized Store find(String suite, String name) { return suite(suite).get(name); }

    public synchronized Store create(String suite, String name, int authmode, boolean writable, long now) throws Full {
        Map<String, Store> m = suite(suite);
        Store old = m.get(name);
        if (old != null) return old;
        if (used(m) + storeOverhead > quota) throw new Full("no room for a new record store");
        Store s = new Store(name, authmode, writable);
        s.lastModified = now;
        m.put(name, s);
        saveStore(suite, s);
        return s;
    }

    /** Puts a store read from elsewhere (import); replaces a store of the same name; the quota is not checked. */
    public synchronized void restore(String suite, Store s) {
        suite(suite).put(s.name, s);
        saveStore(suite, s);
    }

    public synchronized boolean delete(String suite, String name) {
        Map<String, Store> m = suite(suite);
        if (m.remove(name) == null) return false;
        deleteStore(suite, name);
        return true;
    }

    public synchronized int add(String suite, Store s, byte[] data, int off, int len, long now) throws Full {
        if (used(suite(suite)) + len + recordOverhead > quota) throw new Full("record store full");
        byte[] copy = new byte[len];
        if (len > 0) System.arraycopy(data, off, copy, 0, len);
        int id = s.nextId++;
        s.records.put(Integer.valueOf(id), copy);
        s.dataBytes += len;
        touch(suite, s, now);
        return id;
    }

    /** @return false when the record does not exist. */
    public synchronized boolean set(String suite, Store s, int id, byte[] data, int off, int len, long now) throws Full {
        byte[] old = s.records.get(Integer.valueOf(id));
        if (old == null) return false;
        if (len > old.length && used(suite(suite)) + (len - old.length) > quota) throw new Full("record store full");
        byte[] copy = new byte[len];
        if (len > 0) System.arraycopy(data, off, copy, 0, len);
        s.records.put(Integer.valueOf(id), copy);
        s.dataBytes += len - old.length;
        touch(suite, s, now);
        return true;
    }

    public synchronized boolean remove(String suite, Store s, int id, long now) {
        byte[] old = s.records.remove(Integer.valueOf(id));
        if (old == null) return false;
        s.dataBytes -= old.length;
        touch(suite, s, now);
        return true;
    }

    public synchronized void setMode(String suite, Store s, int authmode, boolean writable, long now) {
        s.authmode = authmode;
        s.writable = writable;
        touch(suite, s, now);
    }

    private void touch(String suite, Store s, long now) {
        s.version++;
        s.lastModified = now;
        saveStore(suite, s);
    }
}

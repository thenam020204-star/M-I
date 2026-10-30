/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.rms;

import java.util.Hashtable;
import java.util.Vector;
import jlkvm.internal.Rms;

/**
 * MIDP 2.0 Record Management System. The MIDP rules (names, arguments, exceptions, reference counting, listeners, enumerations) live
 * here in the guest; the bytes, the quota and persistence belong to the host ({@link jlkvm.internal.Rms}, docs/midp.md section 8).
 * Listeners run synchronously on the thread that changed the store, after the change is complete.
 */
public class RecordStore {
    public static final int AUTHMODE_PRIVATE = 0;
    public static final int AUTHMODE_ANY = 1;

    /** Open stores of this VM by key ("o" + name for this suite, "f" + vendor + suite + name for another suite). */
    private static final Hashtable OPEN = new Hashtable();

    private final String name;
    private final String key;
    private final int hid;
    private final boolean own;
    private int refs;
    private boolean open = true;
    private final Vector listeners = new Vector();
    /** RecordEnumerationImpl objects that are kept updated. */
    final Vector kept = new Vector();

    private RecordStore(String name, String key, int hid, boolean own) {
        this.name = name;
        this.key = key;
        this.hid = hid;
        this.own = own;
        this.refs = 1;
    }

    private static void checkName(String n) {
        if (n == null) {
            throw new NullPointerException("record store name");   // the javadoc is silent; MicroEmulator and FreeJ2ME both throw it
        }
        if (n.length() < 1 || n.length() > 32) {
            throw new IllegalArgumentException("record store name must have 1..32 characters");
        }
    }

    public static void deleteRecordStore(String recordStoreName) throws RecordStoreException, RecordStoreNotFoundException {
        checkName(recordStoreName);
        synchronized (OPEN) {
            if (OPEN.containsKey("o" + recordStoreName)) {
                throw new RecordStoreException("record store is open: " + recordStoreName);
            }
            if (Rms.delete0(recordStoreName) < 0) {
                throw new RecordStoreNotFoundException(recordStoreName);
            }
        }
    }

    public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary) throws RecordStoreException,
            RecordStoreFullException, RecordStoreNotFoundException {
        return openRecordStore(recordStoreName, createIfNecessary, AUTHMODE_PRIVATE, true);
    }

    public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary, int authmode, boolean writable)
            throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {
        checkName(recordStoreName);
        if (authmode != AUTHMODE_PRIVATE && authmode != AUTHMODE_ANY) {
            throw new IllegalArgumentException("authmode");
        }
        synchronized (OPEN) {
            RecordStore rs = (RecordStore) OPEN.get("o" + recordStoreName);
            if (rs != null) {
                rs.refs++;
                return rs;
            }
            int id = Rms.open0(recordStoreName, createIfNecessary, authmode, writable, null, null, System.currentTimeMillis());
            if (id == -2) {
                throw new RecordStoreFullException("no room for a record store");
            }
            if (id < 0) {
                throw new RecordStoreNotFoundException(recordStoreName);
            }
            rs = new RecordStore(recordStoreName, "o" + recordStoreName, id, true);
            OPEN.put(rs.key, rs);
            return rs;
        }
    }

    public static RecordStore openRecordStore(String recordStoreName, String vendorName, String suiteName) throws RecordStoreException,
            RecordStoreNotFoundException {
        checkName(recordStoreName);
        if (vendorName == null || suiteName == null) {
            throw new IllegalArgumentException("vendor and suite name required");
        }
        synchronized (OPEN) {
            String fkey = "f" + vendorName + "\n" + suiteName + "\n" + recordStoreName;
            RecordStore rs = (RecordStore) OPEN.get(fkey);
            if (rs != null) {
                rs.refs++;
                return rs;
            }
            int id = Rms.open0(recordStoreName, false, AUTHMODE_PRIVATE, false, vendorName, suiteName, 0L);
            if (id == -3) {
                throw new SecurityException("record store is not shared: " + recordStoreName);
            }
            if (id < 0) {
                throw new RecordStoreNotFoundException(recordStoreName);
            }
            boolean mine = Rms.info0(id, Rms.INFO_OWN) != 0;   // the named suite is this suite: same as the one-suite open
            String k = mine ? "o" + recordStoreName : fkey;
            rs = (RecordStore) OPEN.get(k);
            if (rs != null) {
                Rms.close0(id);
                rs.refs++;
                return rs;
            }
            rs = new RecordStore(recordStoreName, k, id, mine);
            OPEN.put(k, rs);
            return rs;
        }
    }

    public void closeRecordStore() throws RecordStoreNotOpenException, RecordStoreException {
        synchronized (OPEN) {
            if (!open) {
                throw new RecordStoreNotOpenException("record store is closed");
            }
            if (--refs == 0) {
                open = false;
                OPEN.remove(key);
                Rms.close0(hid);
                listeners.removeAllElements();
                kept.removeAllElements();
            }
        }
    }

    public static String[] listRecordStores() {
        synchronized (OPEN) {
            int n = Rms.list0();
            if (n == 0) {
                return null;
            }
            String[] r = new String[n];
            for (int i = 0; i < n; i++) {
                r[i] = Rms.listName0(i);
            }
            return r;
        }
    }

    private void checkOpen() throws RecordStoreNotOpenException {
        if (!open) {
            throw new RecordStoreNotOpenException("record store is closed");
        }
    }

    private void checkWrite() {
        if (!own && Rms.info0(hid, Rms.INFO_WRITABLE) == 0) {
            throw new SecurityException("record store is read-only for this suite");
        }
    }

    private static void checkRange(byte[] data, int offset, int numBytes) {
        if (data == null) {
            if (numBytes != 0) {
                throw new NullPointerException("data");
            }
            return;
        }
        if (offset < 0 || numBytes < 0 || offset > data.length - numBytes) {
            throw new ArrayIndexOutOfBoundsException("offset " + offset + " length " + numBytes + " of " + data.length);
        }
    }

    public String getName() throws RecordStoreNotOpenException {
        checkOpen();
        return name;
    }

    public int getVersion() throws RecordStoreNotOpenException {
        checkOpen();
        return Rms.info0(hid, Rms.INFO_VERSION);
    }

    public int getNumRecords() throws RecordStoreNotOpenException {
        checkOpen();
        return Rms.info0(hid, Rms.INFO_RECORDS);
    }

    public int getSize() throws RecordStoreNotOpenException {
        checkOpen();
        return Rms.info0(hid, Rms.INFO_SIZE);
    }

    public int getSizeAvailable() throws RecordStoreNotOpenException {
        checkOpen();
        return Rms.info0(hid, Rms.INFO_AVAILABLE);
    }

    public long getLastModified() throws RecordStoreNotOpenException {
        checkOpen();
        return Rms.modified0(hid);
    }

    public int getNextRecordID() throws RecordStoreNotOpenException, RecordStoreException {
        checkOpen();
        return Rms.info0(hid, Rms.INFO_NEXT_ID);
    }

    public void addRecordListener(RecordListener listener) {
        if (listener != null && open && !listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }

    public void removeRecordListener(RecordListener listener) {
        listeners.removeElement(listener);
    }

    public int addRecord(byte[] data, int offset, int numBytes) throws RecordStoreNotOpenException, RecordStoreException,
            RecordStoreFullException {
        checkOpen();
        checkWrite();
        checkRange(data, offset, numBytes);
        int id;
        synchronized (this) {
            id = Rms.add0(hid, data, offset, data == null ? 0 : numBytes, System.currentTimeMillis());
        }
        if (id < 0) {
            throw new RecordStoreFullException("record store full");
        }
        changed(0, id);
        return id;
    }

    public void deleteRecord(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
        checkOpen();
        checkWrite();
        int r;
        synchronized (this) {
            r = Rms.remove0(hid, recordId, System.currentTimeMillis());
        }
        if (r < 0) {
            throw new InvalidRecordIDException("record " + recordId);
        }
        changed(2, recordId);
    }

    public int getRecordSize(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
        checkOpen();
        int n = Rms.size0(hid, recordId);
        if (n < 0) {
            throw new InvalidRecordIDException("record " + recordId);
        }
        return n;
    }

    /** MIDP: a record without data gives null. */
    public byte[] getRecord(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
        checkOpen();
        synchronized (this) {
            int n = Rms.size0(hid, recordId);
            if (n < 0) {
                throw new InvalidRecordIDException("record " + recordId);
            }
            if (n == 0) {
                return null;
            }
            byte[] b = new byte[n];
            Rms.read0(hid, recordId, b, 0);
            return b;
        }
    }

    public int getRecord(int recordId, byte[] buffer, int offset) throws RecordStoreNotOpenException, InvalidRecordIDException,
            RecordStoreException {
        checkOpen();
        synchronized (this) {
            int n = Rms.size0(hid, recordId);
            if (n < 0) {
                throw new InvalidRecordIDException("record " + recordId);
            }
            if (buffer == null) {
                throw new NullPointerException("buffer");
            }
            if (offset < 0 || offset > buffer.length - n) {
                throw new ArrayIndexOutOfBoundsException("buffer too small for " + n + " bytes at " + offset);
            }
            if (n > 0) {
                Rms.read0(hid, recordId, buffer, offset);
            }
            return n;
        }
    }

    public void setRecord(int recordId, byte[] newData, int offset, int numBytes) throws RecordStoreNotOpenException,
            InvalidRecordIDException, RecordStoreException, RecordStoreFullException {
        checkOpen();
        checkWrite();
        checkRange(newData, offset, numBytes);
        int r;
        synchronized (this) {
            r = Rms.set0(hid, recordId, newData, offset, newData == null ? 0 : numBytes, System.currentTimeMillis());
        }
        if (r == -2) {
            throw new RecordStoreFullException("record store full");
        }
        if (r < 0) {
            throw new InvalidRecordIDException("record " + recordId);
        }
        changed(1, recordId);
    }

    public RecordEnumeration enumerateRecords(RecordFilter filter, RecordComparator comparator, boolean keepUpdated)
            throws RecordStoreNotOpenException {
        checkOpen();
        return new RecordEnumerationImpl(this, filter, comparator, keepUpdated);
    }

    public void setMode(int authmode, boolean writable) throws RecordStoreException {
        checkOpen();
        if (authmode != AUTHMODE_PRIVATE && authmode != AUTHMODE_ANY) {
            throw new IllegalArgumentException("authmode");
        }
        if (!own) {
            throw new SecurityException("only the suite that owns the record store can change its mode");
        }
        Rms.setMode0(hid, authmode, writable, System.currentTimeMillis());
    }

    // ------------------------------------------------------------------ package-private help for RecordEnumerationImpl

    boolean isOpen() {
        return open;
    }

    int[] ids() {
        synchronized (this) {
            int n = Rms.info0(hid, Rms.INFO_RECORDS);
            int[] ids = new int[n];
            if (n > 0) {
                Rms.ids0(hid, ids);
            }
            return ids;
        }
    }

    /** Record data for the enumeration; null when the record does not exist. A record without data gives an empty array. */
    byte[] fetch(int recordId) {
        synchronized (this) {
            int n = Rms.size0(hid, recordId);
            if (n < 0) {
                return null;
            }
            byte[] b = new byte[n];
            if (n > 0) {
                Rms.read0(hid, recordId, b, 0);
            }
            return b;
        }
    }

    /** kind 0 added, 1 changed, 2 deleted: runs the listeners (user listeners first) on the calling thread. */
    private void changed(int kind, int recordId) {
        Object[] ls;
        Object[] ks;
        synchronized (this) {
            ls = new Object[listeners.size()];
            listeners.copyInto(ls);
            ks = new Object[kept.size()];
            kept.copyInto(ks);
        }
        for (int i = 0; i < ks.length; i++) {
            ((RecordEnumerationImpl) ks[i]).storeChanged();
        }
        for (int i = 0; i < ls.length; i++) {
            RecordListener l = (RecordListener) ls[i];
            if (kind == 0) {
                l.recordAdded(this, recordId);
            } else if (kind == 1) {
                l.recordChanged(this, recordId);
            } else {
                l.recordDeleted(this, recordId);
            }
        }
    }
}

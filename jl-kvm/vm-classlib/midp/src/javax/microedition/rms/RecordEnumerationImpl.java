/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package javax.microedition.rms;

/**
 * The enumeration of a RecordStore: a snapshot of record ids (filtered, ordered by the comparator or by id) with a cursor BETWEEN
 * elements (nextRecord then previousRecord gives the same record). The data of a record is read when it is returned. Filter and comparator
 * run on the calling guest thread while the snapshot is built.
 */
final class RecordEnumerationImpl implements RecordEnumeration {
    private final RecordStore store;
    private final RecordFilter filter;
    private final RecordComparator comparator;
    private boolean keepUpdated;
    private boolean destroyed;
    private int[] ids = new int[0];
    private int cur;

    RecordEnumerationImpl(RecordStore store, RecordFilter filter, RecordComparator comparator, boolean keepUpdated) {
        this.store = store;
        this.filter = filter;
        this.comparator = comparator;
        build();
        if (keepUpdated) {
            keepUpdated(true);
        }
    }

    private void check() {
        if (destroyed) {
            throw new IllegalStateException("enumeration destroyed");
        }
    }

    /** Builds the id list; the position is kept when it still fits (rebuild) and is 0 at the start. */
    private void build() {
        int[] all = store.ids();
        int n = 0;
        byte[][] data = new byte[all.length][];
        int[] sel = new int[all.length];
        for (int i = 0; i < all.length; i++) {
            byte[] d = store.fetch(all[i]);
            if (d == null) {
                continue;
            }
            if (filter != null && !filter.matches(d)) {
                continue;
            }
            sel[n] = all[i];
            data[n] = d;
            n++;
        }
        int[] order = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        if (comparator != null && n > 1) {
            sort(order, new int[n], 0, n, data);
        }
        int[] r = new int[n];
        for (int i = 0; i < n; i++) {
            r[i] = sel[order[i]];
        }
        ids = r;
        if (cur > n) {
            cur = n;
        }
    }

    /** Stable merge sort of order[lo, hi) by comparator over data[]. */
    private void sort(int[] order, int[] tmp, int lo, int hi, byte[][] data) {
        if (hi - lo < 2) {
            return;
        }
        int mid = (lo + hi) >>> 1;
        sort(order, tmp, lo, mid, data);
        sort(order, tmp, mid, hi, data);
        int i = lo, j = mid, k = lo;
        while (i < mid && j < hi) {
            if (comparator.compare(data[order[j]], data[order[i]]) < 0) {
                tmp[k++] = order[j++];
            } else {
                tmp[k++] = order[i++];
            }
        }
        while (i < mid) {
            tmp[k++] = order[i++];
        }
        while (j < hi) {
            tmp[k++] = order[j++];
        }
        for (k = lo; k < hi; k++) {
            order[k] = tmp[k];
        }
    }

    /** Called by the store after a change when the enumeration is kept updated. */
    void storeChanged() {
        if (keepUpdated && !destroyed) {
            build();
        }
    }

    public int numRecords() {
        check();
        return ids.length;
    }

    public byte[] nextRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException {
        check();
        if (!store.isOpen()) {
            throw new RecordStoreNotOpenException("record store is closed");
        }
        if (cur >= ids.length) {
            throw new InvalidRecordIDException("no next record");
        }
        return read(ids[cur++]);
    }

    public int nextRecordId() throws InvalidRecordIDException {
        check();
        if (cur >= ids.length) {
            throw new InvalidRecordIDException("no next record");
        }
        return ids[cur++];
    }

    public byte[] previousRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException {
        check();
        if (!store.isOpen()) {
            throw new RecordStoreNotOpenException("record store is closed");
        }
        if (cur <= 0) {
            throw new InvalidRecordIDException("no previous record");
        }
        return read(ids[--cur]);
    }

    public int previousRecordId() throws InvalidRecordIDException {
        check();
        if (cur <= 0) {
            throw new InvalidRecordIDException("no previous record");
        }
        return ids[--cur];
    }

    private byte[] read(int id) throws InvalidRecordIDException {
        byte[] d = store.fetch(id);
        if (d == null) {
            throw new InvalidRecordIDException("record " + id + " was deleted");
        }
        return d.length == 0 ? null : d;
    }

    public boolean hasNextElement() {
        check();
        return cur < ids.length;
    }

    public boolean hasPreviousElement() {
        check();
        return cur > 0;
    }

    public void reset() {
        check();
        cur = 0;
    }

    public void rebuild() {
        check();
        build();
    }

    public void keepUpdated(boolean keepUpdated) {
        check();
        if (keepUpdated) {
            if (!this.keepUpdated) {
                this.keepUpdated = true;
                if (store.isOpen()) {
                    store.kept.addElement(this);
                }
            }
            build();
        } else {
            this.keepUpdated = false;
            store.kept.removeElement(this);
        }
    }

    public boolean isKeptUpdated() {
        check();
        return keepUpdated;
    }

    public void destroy() {
        destroyed = true;
        keepUpdated = false;
        store.kept.removeElement(this);
        ids = new int[0];
    }
}

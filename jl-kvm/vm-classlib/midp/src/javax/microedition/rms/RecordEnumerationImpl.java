/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package javax.microedition.rms;

/**
 * The enumeration of a RecordStore: a snapshot of record ids (filtered, ordered by the comparator or by id) with a cursor
 * elements: it sits ON the element last returned (-1 before the first call), as in the MIDP 2.0 javadoc "to return the record data from the previous
 * consecutive from any given point in the enumeration, call previousRecord()" and as in phoneME (T05c): after nextRecord returned the element k, previousRecord returns
 * k-1; as first call previousRecord returns the last element. The data of a record is read when it is returned. Filter and comparator
 * run on the calling guest thread while the snapshot is built.
 */
final class RecordEnumerationImpl implements RecordEnumeration {
    private final RecordStore store;
    private final RecordFilter filter;
    private final RecordComparator comparator;
    private boolean keepUpdated;
    private boolean destroyed;
    private int[] ids = new int[0];
    private int cur = -1;

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
        if (cur > n - 1) {
            cur = n - 1;
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
        if (cur >= ids.length - 1) {
            throw new InvalidRecordIDException("no next record");
        }
        return read(ids[++cur]);
    }

    public int nextRecordId() throws InvalidRecordIDException {
        check();
        if (cur >= ids.length - 1) {
            throw new InvalidRecordIDException("no next record");
        }
        return ids[++cur];
    }

    public byte[] previousRecord() throws InvalidRecordIDException, RecordStoreNotOpenException, RecordStoreException {
        check();
        if (!store.isOpen()) {
            throw new RecordStoreNotOpenException("record store is closed");
        }
        step();
        return read(ids[cur]);
    }

    public int previousRecordId() throws InvalidRecordIDException {
        check();
        step();
        return ids[cur];
    }

    /** previous: the last element as first call (cursor -1), else one step back; InvalidRecordIDException at the first element or when empty. */
    private void step() throws InvalidRecordIDException {
        if (cur == 0 || ids.length == 0) {
            throw new InvalidRecordIDException("no previous record");
        }
        cur = cur < 0 ? ids.length - 1 : cur - 1;
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
        return ids.length > 0 && store.isOpen() && cur != ids.length - 1;
    }

    public boolean hasPreviousElement() {
        check();
        return ids.length > 0 && store.isOpen() && cur != 0;
    }

    public void reset() {
        check();
        cur = -1;
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

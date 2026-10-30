package jlkvm.host;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jlkvm.DeviceProfile;
import jlkvm.heap.Heap;
import jlkvm.host.rms.RecordStorage;
import jlkvm.natives.GuestServices;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;

/**
 * Natives of {@code jlkvm.internal.Rms} (PLAN T26): the guest keeps the MIDP rules, this class gives it byte storage in a
 * {@link RecordStorage}. A guest "store id" is an index in {@link #open}; the guest releases it with {@code close0}. Return codes
 * of the natives: negative = problem (-1 not found / invalid id, -2 full, -3 not allowed). docs/midp.md section 8.
 */
public final class RmsNatives {
    private static final String RMS = "jlkvm/internal/Rms.";
    private static final class Open { final String suite; final RecordStorage.Store store; Open(String s, RecordStorage.Store st) { suite = s; store = st; } }

    private final RecordStorage storage;
    private final DeviceProfile profile;
    private final String suite;
    private final Map<Integer, Open> open = new HashMap<Integer, Open>();
    private int nextOpen = 1;
    private List<String> listing;

    /** Suite identity (MIDP: vendor + name of the MIDlet suite) from the JAD / manifest attributes. */
    public static String suiteKey(String vendor, String name) { return (vendor == null ? "" : vendor) + "\n" + (name == null ? "" : name); }

    public RmsNatives(RecordStorage storage, DeviceProfile profile, Map<String, String> appProps) {
        this.storage = storage;
        this.profile = profile;
        this.suite = suiteKey(appProps);
    }

    /** Suite identity from the attributes of the manifest + JAD (names are case-insensitive). */
    public static String suiteKey(Map<String, String> attrs) {
        String v = null, n = null;
        for (Map.Entry<String, String> e : attrs.entrySet()) {
            if (e.getKey().equalsIgnoreCase("MIDlet-Vendor")) v = e.getValue();
            else if (e.getKey().equalsIgnoreCase("MIDlet-Name")) n = e.getValue();
        }
        return suiteKey(v, n);
    }

    private Open get(NativeContext c, int id) {
        Open o = open.get(Integer.valueOf(id));
        if (o == null) throw new jlkvm.link.VmThrow("java/lang/IllegalStateException", "bad record store id " + id);
        return o;
    }

    private static byte[] bytes(Heap h, int arr, int off, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) b[i] = h.getByte(arr, off + i);
        return b;
    }

    public void register(NativeRegistry r) {
        final GuestServices gs = r.services();
        r.register(RMS + "list0()I", new NativeMethod() { public void invoke(NativeContext c) {
            listing = storage.list(suite);
            c.retInt(listing.size()); } });
        r.register(RMS + "listName0(I)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(gs.newString(listing.get(c.argInt(0)))); } });
        // open0(name, create, authmode, writable, vendor, suite, now) -> id | -1 not found | -2 full | -3 not shared
        r.register(RMS + "open0(Ljava/lang/String;ZIZLjava/lang/String;Ljava/lang/String;J)I", new NativeMethod() { public void invoke(NativeContext c) {
            String name = gs.string(c.argRef(0));
            boolean create = c.argInt(1) != 0;
            String ste = suite;
            boolean foreign = c.argRef(4) != 0 || c.argRef(5) != 0;
            if (foreign) {
                String k = RmsNatives.suiteKey(gs.string(c.argRef(4)), gs.string(c.argRef(5)));
                if (!k.equals(suite)) ste = k; else foreign = false;
            }
            RecordStorage.Store s = storage.find(ste, name);
            if (s == null) {
                if (foreign || !create) { c.retInt(-1); return; }
                try {
                    s = storage.create(ste, name, c.argInt(2), c.argInt(3) != 0, c.argLong(6));
                } catch (RecordStorage.Full e) { c.retInt(-2); return; }
            } else if (foreign && s.authmode() != 1) { c.retInt(-3); return; }
            int id = nextOpen++;
            open.put(Integer.valueOf(id), new Open(ste, s));
            c.retInt(id); } });
        r.register(RMS + "close0(I)V", new NativeMethod() { public void invoke(NativeContext c) { open.remove(Integer.valueOf(c.argInt(0))); } });
        r.register(RMS + "delete0(Ljava/lang/String;)I", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(storage.delete(suite, gs.string(c.argRef(0))) ? 0 : -1); } });
        // info0(id, what): 0 authmode, 1 writable, 2 numRecords, 3 nextId, 4 version, 5 size, 6 sizeAvailable, 7 own suite
        r.register(RMS + "info0(II)I", new NativeMethod() { public void invoke(NativeContext c) {
            Open o = get(c, c.argInt(0));
            int v;
            switch (c.argInt(1)) {
                case 0: v = o.store.authmode(); break;
                case 1: v = o.store.writable() ? 1 : 0; break;
                case 2: v = o.store.numRecords(); break;
                case 3: v = o.store.nextId(); break;
                case 4: v = o.store.version(); break;
                case 5: v = storage.size(o.store); break;
                case 6: v = storage.available(o.suite); break;
                case 7: v = o.suite.equals(suite) ? 1 : 0; break;
                default: v = 0;
            }
            c.retInt(v); } });
        r.register(RMS + "modified0(I)J", new NativeMethod() { public void invoke(NativeContext c) { c.retLong(get(c, c.argInt(0)).store.lastModified()); } });
        r.register(RMS + "add0(I[BIIJ)I", new NativeMethod() { public void invoke(NativeContext c) {
            Open o = get(c, c.argInt(0));
            int len = c.argInt(3);
            byte[] d = len == 0 ? new byte[0] : bytes(c.heap(), c.argRef(1), c.argInt(2), len);
            gs.charge(len / 16 + 8);
            try {
                c.retInt(storage.add(o.suite, o.store, d, 0, len, c.argLong(4)));
            } catch (RecordStorage.Full e) { c.retInt(-2); } } });
        r.register(RMS + "set0(II[BIIJ)I", new NativeMethod() { public void invoke(NativeContext c) {
            Open o = get(c, c.argInt(0));
            int len = c.argInt(4);
            byte[] d = len == 0 ? new byte[0] : bytes(c.heap(), c.argRef(2), c.argInt(3), len);
            gs.charge(len / 16 + 8);
            try {
                c.retInt(storage.set(o.suite, o.store, c.argInt(1), d, 0, len, c.argLong(5)) ? 0 : -1);
            } catch (RecordStorage.Full e) { c.retInt(-2); } } });
        r.register(RMS + "remove0(IIJ)I", new NativeMethod() { public void invoke(NativeContext c) {
            Open o = get(c, c.argInt(0));
            c.retInt(storage.remove(o.suite, o.store, c.argInt(1), c.argLong(2)) ? 0 : -1); } });
        r.register(RMS + "size0(II)I", new NativeMethod() { public void invoke(NativeContext c) {
            byte[] d = get(c, c.argInt(0)).store.get(c.argInt(1));
            c.retInt(d == null ? -1 : d.length); } });
        r.register(RMS + "read0(II[BI)I", new NativeMethod() { public void invoke(NativeContext c) {
            byte[] d = get(c, c.argInt(0)).store.get(c.argInt(1));
            if (d == null) { c.retInt(-1); return; }
            Heap h = c.heap();
            int arr = c.argRef(2), off = c.argInt(3);
            for (int i = 0; i < d.length; i++) h.putByte(arr, off + i, d[i]);
            gs.charge(d.length / 16 + 8);
            c.retInt(d.length); } });
        r.register(RMS + "ids0(I[I)V", new NativeMethod() { public void invoke(NativeContext c) {
            int[] ids = get(c, c.argInt(0)).store.ids();
            Heap h = c.heap();
            int arr = c.argRef(1);
            for (int i = 0; i < ids.length; i++) h.putIntElem(arr, i, ids[i]);
            gs.charge(ids.length / 8 + 4); } });
        r.register(RMS + "setMode0(IIZJ)I", new NativeMethod() { public void invoke(NativeContext c) {
            Open o = get(c, c.argInt(0));
            if (!o.suite.equals(suite)) { c.retInt(-3); return; }
            storage.setMode(o.suite, o.store, c.argInt(1), c.argInt(2) != 0, c.argLong(3));
            c.retInt(0); } });
    }
}

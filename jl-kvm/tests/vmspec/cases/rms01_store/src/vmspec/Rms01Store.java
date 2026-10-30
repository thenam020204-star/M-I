package vmspec;

import java.util.Vector;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 * T26: MIDP 2.0 RMS - names, record ids (never reused), argument ranges, exceptions, listeners (thread of the caller), enumerations
 * (filter, comparator, cursor, keepUpdated, destroy), open reference counting, quota. "> " lines are compared; "# " lines carry
 * implementation-defined numbers (sizes, overheads).
 */
public class Rms01Store extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static void i(String s) { System.out.println("# " + s); }

    static String cn(Throwable t) {
        String n = t.getClass().getName();
        return n.substring(n.lastIndexOf('.') + 1);
    }

    static byte[] b(String s) { return s.getBytes(); }
    static String s(byte[] d) { return d == null ? "null" : new String(d); }

    abstract static class Call { abstract Object go() throws Exception; }
    static String ex(Call c) {
        try { Object o = c.go(); return o == null ? "ok" : o instanceof byte[] ? "ok bytes" : o instanceof RecordStore || o instanceof RecordEnumeration ? "ok" : "ok " + o; } catch (Throwable t) { return cn(t); }
    }

    static void cleanup() throws Exception {
        String[] n = RecordStore.listRecordStores();
        for (int k = 0; n != null && k < n.length; k++) RecordStore.deleteRecordStore(n[k]);
    }

    static String list() {
        String[] n = RecordStore.listRecordStores();
        if (n == null) return "null";
        for (int a = 0; a < n.length; a++) for (int c = a + 1; c < n.length; c++) if (n[c].compareTo(n[a]) < 0) { String t = n[a]; n[a] = n[c]; n[c] = t; }
        StringBuffer sb = new StringBuffer();
        for (int a = 0; a < n.length; a++) sb.append(a == 0 ? "" : ",").append(n[a]);
        return sb.toString();
    }

    static final class Rec implements RecordListener {
        final StringBuffer log = new StringBuffer();
        Thread thread;
        boolean sameStore = true;
        RecordStore expect;
        public void recordAdded(RecordStore rs, int id) { log.append("A").append(id).append(' '); thread = Thread.currentThread(); sameStore &= rs == expect; }
        public void recordChanged(RecordStore rs, int id) { log.append("C").append(id).append(' '); thread = Thread.currentThread(); sameStore &= rs == expect; }
        public void recordDeleted(RecordStore rs, int id) { log.append("D").append(id).append(' '); thread = Thread.currentThread(); sameStore &= rs == expect; }
    }

    static final class Len3 implements RecordFilter {
        public boolean matches(byte[] c) { return c.length > 3; }
    }

    static final class Alpha implements RecordComparator {
        public int compare(byte[] x, byte[] y) {
            int c = new String(x).compareTo(new String(y));
            return c < 0 ? PRECEDES : c > 0 ? FOLLOWS : EQUIVALENT;
        }
    }

    static String drain(RecordEnumeration e) throws Exception {
        StringBuffer sb = new StringBuffer();
        while (e.hasNextElement()) sb.append(s(e.nextRecord())).append(' ');
        return sb.toString().trim();
    }

    protected void startApp() {
        try {
            run();
        } catch (Throwable t) {
            p("FATAL " + t);
        }
        notifyDestroyed();
    }

    void run() throws Exception {
        cleanup();
        p("empty list: " + list());
        p("open missing, create=false: " + ex(new Call() { Object go() throws Exception { return RecordStore.openRecordStore("nope", false); } }));
        p("name empty: " + ex(new Call() { Object go() throws Exception { return RecordStore.openRecordStore("", true); } }));
        StringBuffer long33 = new StringBuffer();
        for (int k = 0; k < 33; k++) long33.append('x');
        final String l33 = long33.toString();
        p("name 33 chars: " + ex(new Call() { Object go() throws Exception { return RecordStore.openRecordStore(l33, true); } }));
        p("name 32 chars ok: " + ex(new Call() { Object go() throws Exception { RecordStore r = RecordStore.openRecordStore(l33.substring(1), true); r.closeRecordStore(); RecordStore.deleteRecordStore(l33.substring(1)); return null; } }));
        p("name null: " + ex(new Call() { Object go() throws Exception { return RecordStore.openRecordStore(null, true); } }));
        p("bad authmode: " + ex(new Call() { Object go() throws Exception { return RecordStore.openRecordStore("am", true, 2, true); } }));
        p("delete missing: " + ex(new Call() { Object go() throws Exception { RecordStore.deleteRecordStore("nope"); return null; } }));
        p("list after failures: " + list());

        // ---- basics
        final RecordStore rs = RecordStore.openRecordStore("Alpha", true);
        p("name=" + rs.getName() + " records=" + rs.getNumRecords() + " nextId=" + rs.getNextRecordID());
        p("list: " + list());
        int v0 = rs.getVersion();
        long m0 = rs.getLastModified();
        int sz0 = rs.getSize(), av0 = rs.getSizeAvailable();
        i("empty store: size=" + sz0 + " available=" + av0 + " version=" + v0 + " lastModified=" + m0);
        final int id1 = rs.addRecord(b("one"), 0, 3);
        final int id2 = rs.addRecord(b("twotwo"), 0, 6);
        int id3 = rs.addRecord(b("xxthree"), 2, 5);
        p("ids " + id1 + "," + id2 + "," + id3 + " records=" + rs.getNumRecords() + " nextId=" + rs.getNextRecordID());
        p("content: " + s(rs.getRecord(id1)) + "," + s(rs.getRecord(id2)) + "," + s(rs.getRecord(id3)) + " sizes " + rs.getRecordSize(id1) + "," + rs.getRecordSize(id2) + "," + rs.getRecordSize(id3));
        p("version increased: " + (rs.getVersion() > v0) + " size grew by >= 14: " + (rs.getSize() - sz0 >= 14) + " available shrank by >= 14: " + (av0 - rs.getSizeAvailable() >= 14) + " lastModified >=: " + (rs.getLastModified() >= m0));
        i("after 3 adds: size=" + rs.getSize() + " available=" + rs.getSizeAvailable() + " version=" + rs.getVersion());
        byte[] buf = new byte[10];
        int n = rs.getRecord(id2, buf, 2);
        p("getRecord(buf): n=" + n + " buf=" + new String(buf, 2, n) + " untouched=" + (buf[0] == 0 && buf[8] == 0));
        p("buffer too small: " + ex(new Call() { Object go() throws Exception { return new Integer(rs.getRecord(id2, new byte[3], 0)); } }));
        p("buffer offset too big: " + ex(new Call() { Object go() throws Exception { return new Integer(rs.getRecord(id2, new byte[10], 5)); } }));
        p("add offset/length: " + ex(new Call() { Object go() throws Exception { return new Integer(rs.addRecord(new byte[4], 3, 2)); } })
            + " " + ex(new Call() { Object go() throws Exception { return new Integer(rs.addRecord(new byte[4], -1, 2)); } })
            + " " + ex(new Call() { Object go() throws Exception { return new Integer(rs.addRecord(new byte[4], 0, -1)); } }));
        p("records still " + rs.getNumRecords());
        p("null data, 0 bytes: " + ex(new Call() { Object go() throws Exception { return new Integer(rs.addRecord(null, 0, 0)); } }));
        int idNull = rs.getNextRecordID() - 1;
        p("empty record: size=" + rs.getRecordSize(idNull) + " data=" + s(rs.getRecord(idNull)) + " buf n=" + rs.getRecord(idNull, buf, 0));
        p("null data, 3 bytes: " + ex(new Call() { Object go() throws Exception { return new Integer(rs.addRecord(null, 0, 3)); } }));
        p("records now " + rs.getNumRecords() + " nextId=" + rs.getNextRecordID());

        // ---- delete / set / ids are never reused
        rs.deleteRecord(id2);
        p("deleted " + id2 + ": getRecord=" + ex(new Call() { Object go() throws Exception { return rs.getRecord(id2); } })
            + " size=" + ex(new Call() { Object go() throws Exception { return new Integer(rs.getRecordSize(id2)); } })
            + " delete again=" + ex(new Call() { Object go() throws Exception { rs.deleteRecord(id2); return null; } })
            + " set=" + ex(new Call() { Object go() throws Exception { rs.setRecord(id2, b("q"), 0, 1); return null; } }));
        int idNew = rs.addRecord(b("again"), 0, 5);
        p("new id after delete: " + idNew + " (not " + id2 + ") records=" + rs.getNumRecords());
        p("bad ids: " + ex(new Call() { Object go() throws Exception { return rs.getRecord(0); } }) + " " + ex(new Call() { Object go() throws Exception { return rs.getRecord(-1); } })
            + " " + ex(new Call() { Object go() throws Exception { return rs.getRecord(9999); } }));
        int before = rs.getSize();
        rs.setRecord(id1, b("longer-one"), 0, 10);
        p("setRecord grow: " + s(rs.getRecord(id1)) + " size grew by 7: " + (rs.getSize() - before == 7) + " nextId unchanged=" + rs.getNextRecordID());
        rs.setRecord(id1, b("1"), 0, 1);
        p("setRecord shrink: " + s(rs.getRecord(id1)) + " size=" + rs.getSize() + " == before-2: " + (rs.getSize() == before - 2));
        rs.setRecord(id1, null, 0, 0);
        p("setRecord null: size=" + rs.getRecordSize(id1) + " data=" + s(rs.getRecord(id1)));
        rs.setRecord(id1, b("one"), 0, 3);

        // ---- listener
        Rec L = new Rec();
        L.expect = rs;
        rs.addRecordListener(L);
        rs.addRecordListener(L);
        int la = rs.addRecord(b("lis"), 0, 3);
        rs.setRecord(la, b("lis2"), 0, 4);
        rs.deleteRecord(la);
        p("listener log: " + L.log.toString().trim() + " (ids " + la + ") same thread=" + (L.thread == Thread.currentThread()) + " same store=" + L.sameStore);
        final Vector other = new Vector();
        final Rec L2 = new Rec();
        L2.expect = rs;
        rs.addRecordListener(L2);
        Thread t = new Thread() { public void run() { try { rs.addRecord(b("th"), 0, 2); } catch (Exception e) { other.addElement(e); } L2.log.append("."); other.addElement(Thread.currentThread()); } };
        t.start();
        t.join();
        p("listener thread = the changing thread: " + (L2.thread == other.elementAt(other.size() - 1) && L2.thread != Thread.currentThread()) + " " + L2.log.toString().trim());
        rs.removeRecordListener(L);
        rs.removeRecordListener(L2);
        int c0 = L.log.length();
        rs.addRecord(b("zz"), 0, 2);
        p("removed listener silent: " + (L.log.length() == c0));

        // ---- enumerations
        rs.closeRecordStore();
        RecordStore.deleteRecordStore("Alpha");
        final RecordStore en = RecordStore.openRecordStore("Enum", true);
        String[] words = {"pear", "apple", "fig", "banana", "kiwi"};
        for (int k = 0; k < words.length; k++) en.addRecord(b(words[k]), 0, words[k].length());
        RecordEnumeration all = en.enumerateRecords(null, null, false);
        p("all: n=" + all.numRecords());
        String got = drain(all);
        p("all contents (sorted here): " + sortWords(got));
        RecordEnumeration e1 = en.enumerateRecords(new Len3(), new Alpha(), false);
        p("filter+comparator: n=" + e1.numRecords() + " order=" + drain(e1));
        p("at end: hasNext=" + e1.hasNextElement() + " hasPrevious=" + e1.hasPreviousElement());
        final RecordEnumeration e1f = e1;
        p("next at end: " + ex(new Call() { Object go() throws Exception { return e1f.nextRecord(); } }) + " / " + ex(new Call() { Object go() throws Exception { return new Integer(e1f.nextRecordId()); } }));
        p("previous: " + s(e1.previousRecord()) + " " + s(e1.previousRecord()) + " then next: " + s(e1.nextRecord()));
        e1.reset();
        p("reset: hasPrevious=" + e1.hasPreviousElement() + " previous as first call=" + s(e1.previousRecord()) + " " + s(e1.previousRecord()));
        e1.reset();
        p("reset: first=" + s(e1.nextRecord()) + " previous at the first element: hasPrevious=" + e1.hasPreviousElement() + " " + ex(new Call() { Object go() throws Exception { return e1f.previousRecord(); } })
            + " then next=" + s(e1.nextRecord()));
        RecordEnumeration e2 = en.enumerateRecords(null, new Alpha(), false);
        p("comparator only: " + drain(e2));
        e2.reset();
        p("ids ascending by comparator: " + e2.nextRecordId() + " " + e2.nextRecordId());
        RecordEnumeration e3 = en.enumerateRecords(new Len3(), null, false);
        p("filter only: n=" + e3.numRecords() + " as set=" + sortWords(drain(e3)));
        // zero matches
        RecordEnumeration e0 = en.enumerateRecords(new RecordFilter() { public boolean matches(byte[] c) { return false; } }, null, false);
        p("nothing matches: n=" + e0.numRecords() + " hasNext=" + e0.hasNextElement() + " hasPrevious=" + e0.hasPreviousElement());
        // keepUpdated
        final RecordEnumeration ku = en.enumerateRecords(null, new Alpha(), true);
        final RecordEnumeration nk = en.enumerateRecords(null, new Alpha(), false);
        p("isKeptUpdated: " + ku.isKeptUpdated() + " " + nk.isKeptUpdated());
        int idz = en.addRecord(b("zebra"), 0, 5);
        p("after add 'zebra': kept n=" + ku.numRecords() + " not kept n=" + nk.numRecords());
        nk.rebuild();
        p("after rebuild: not kept n=" + nk.numRecords());
        en.deleteRecord(idz);
        p("after delete: kept n=" + ku.numRecords() + " not kept n=" + nk.numRecords() + " stale walk=" + staleWalk(nk));
        nk.keepUpdated(true);
        p("keepUpdated(true) rebuilds: " + nk.numRecords());
        ku.keepUpdated(false);
        en.addRecord(b("zulu"), 0, 4);
        p("keepUpdated(false): kept n=" + ku.numRecords() + " nk n=" + nk.numRecords());
        ku.destroy();
        p("after destroy: " + ex(new Call() { Object go() throws Exception { return new Integer(ku.numRecords()); } }));
        // exceptions from filter propagate
        p("filter throws: " + ex(new Call() { Object go() throws Exception {
            return en.enumerateRecords(new RecordFilter() { public boolean matches(byte[] c) { throw new IllegalStateException(); } }, null, false); } }));

        // ---- reference counting and closing
        final RecordStore a = RecordStore.openRecordStore("Enum", false);
        p("second open is the same object: " + (a == en) + " name=" + a.getName());
        a.closeRecordStore();
        p("after 1st close still open: records=" + en.getNumRecords());
        p("delete while open: " + ex(new Call() { Object go() throws Exception { RecordStore.deleteRecordStore("Enum"); return null; } }));
        en.closeRecordStore();
        p("closed: getNumRecords=" + ex(new Call() { Object go() throws Exception { return new Integer(en.getNumRecords()); } })
            + " getName=" + ex(new Call() { Object go() throws Exception { return en.getName(); } })
            + " add=" + ex(new Call() { Object go() throws Exception { return new Integer(en.addRecord(b("x"), 0, 1)); } })
            + " getRecord=" + ex(new Call() { Object go() throws Exception { return en.getRecord(1); } })
            + " enumerate=" + ex(new Call() { Object go() throws Exception { return en.enumerateRecords(null, null, false); } }));
        p("close again: " + ex(new Call() { Object go() throws Exception { en.closeRecordStore(); return null; } }));
        p("old enumeration after close: nextRecord=" + ex(new Call() { Object go() throws Exception { return nk.nextRecord(); } }));
        RecordStore.deleteRecordStore("Enum");
        p("deleted; list=" + list() + " delete again=" + ex(new Call() { Object go() throws Exception { RecordStore.deleteRecordStore("Enum"); return null; } }));

        // ---- persistence inside the run, case-sensitive names, reopen keeps data and ids
        RecordStore p1 = RecordStore.openRecordStore("Keep", true);
        p1.addRecord(b("a"), 0, 1);
        p1.addRecord(b("b"), 0, 1);
        p1.deleteRecord(2);
        p1.closeRecordStore();
        RecordStore p2 = RecordStore.openRecordStore("Keep", false);
        p("reopen: records=" + p2.getNumRecords() + " nextId=" + p2.getNextRecordID() + " data=" + s(p2.getRecord(1)));
        RecordStore p3 = RecordStore.openRecordStore("keep", true);
        p("names are case sensitive: list=" + list() + " other records=" + p3.getNumRecords());
        p2.closeRecordStore();
        p3.closeRecordStore();
        RecordStore.deleteRecordStore("keep");
        RecordStore p4 = RecordStore.openRecordStore("Keep", true);
        p("create=true on existing keeps data: " + p4.getNumRecords());
        p4.closeRecordStore();

        // ---- setMode on own store
        final RecordStore sm = RecordStore.openRecordStore("Keep", true);
        sm.setMode(RecordStore.AUTHMODE_ANY, false);
        sm.setMode(RecordStore.AUTHMODE_PRIVATE, true);
        p("setMode bad: " + ex(new Call() { Object go() throws Exception { sm.setMode(7, true); return null; } }));
        sm.closeRecordStore();
        RecordStore.deleteRecordStore("Keep");

        // ---- quota: fill until RecordStoreFullException
        final RecordStore fl = RecordStore.openRecordStore("Full", true);
        int count = 0;
        boolean full = false;
        byte[] kb = new byte[1000];
        while (count < 100000 && !full) {
            try { fl.addRecord(kb, 0, kb.length); count++; } catch (RecordStoreFullException e) { full = true; }
        }
        i("records until full: " + count + " available=" + fl.getSizeAvailable() + " size=" + fl.getSize());
        p("filling ends with RecordStoreFullException: " + full + " available < 1100: " + (fl.getSizeAvailable() < 1100));
        p("store still consistent: records=" + fl.getNumRecords() + " == count " + (fl.getNumRecords() == count) + " last readable=" + (fl.getRecordSize(count) == 1000));
        p("small record may still fit or not, no other exception: " + ex(new Call() { Object go() throws Exception { try { fl.addRecord(new byte[1], 0, 1); return "fits"; } catch (RecordStoreFullException e) { return "full"; } } }).startsWith("ok"));
        fl.deleteRecord(1);
        fl.addRecord(kb, 0, 1000);
        p("space freed by delete can be reused: true");
        p("setRecord beyond quota: " + ex(new Call() { Object go() throws Exception { fl.setRecord(2, new byte[100000], 0, 100000); return null; } }) + " record 2 intact: " + (fl.getRecordSize(2) == 1000));
        fl.closeRecordStore();
        RecordStore.deleteRecordStore("Full");
        RecordStore ok = RecordStore.openRecordStore("After", true);
        p("quota freed by delete: " + (ok.getSizeAvailable() > 100000));
        ok.closeRecordStore();
        RecordStore.deleteRecordStore("After");
        p("end: " + list());
    }

    /** Walks a (stale, not kept updated) enumeration: ids of deleted records still come back, their data does not. */
    static String staleWalk(RecordEnumeration e) {
        StringBuffer sb = new StringBuffer();
        e.reset();
        while (e.hasNextElement()) {
            try { sb.append(s(e.nextRecord())); } catch (Exception x) { sb.append(cn(x)); }
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    static String sortWords(String joined) {
        Vector v = new Vector();
        int st = 0;
        for (int k = 0; k <= joined.length(); k++) {
            if (k == joined.length() || joined.charAt(k) == ' ') { if (k > st) v.addElement(joined.substring(st, k)); st = k + 1; }
        }
        for (int a = 0; a < v.size(); a++) for (int c = a + 1; c < v.size(); c++) if (((String) v.elementAt(c)).compareTo((String) v.elementAt(a)) < 0) { Object t = v.elementAt(a); v.setElementAt(v.elementAt(c), a); v.setElementAt(t, c); }
        StringBuffer sb = new StringBuffer();
        for (int a = 0; a < v.size(); a++) sb.append(a == 0 ? "" : " ").append(v.elementAt(a));
        return sb.toString();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

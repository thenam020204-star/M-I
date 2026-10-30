package rms;

import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreFullException;

/** T26 host test: the quota of the profile (run with a profile of 1000 bytes, store overhead 16, record overhead 8). */
public class RmsQ extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        try {
            RecordStore rs = RecordStore.openRecordStore("q", true);
            p("empty: size=" + rs.getSize() + " available=" + rs.getSizeAvailable());
            byte[] d = new byte[100];
            int n = 0;
            try { for (;;) { rs.addRecord(d, 0, 100); n++; } } catch (RecordStoreFullException e) { p("full after " + n + " records"); }
            p("size=" + rs.getSize() + " available=" + rs.getSizeAvailable() + " records=" + rs.getNumRecords() + " nextId=" + rs.getNextRecordID());
            rs.setRecord(1, d, 0, 100);
            p("same size set ok");
            rs.deleteRecord(2);
            p("after delete: available=" + rs.getSizeAvailable());
            rs.setRecord(1, new byte[220], 0, 220);
            p("grown by 120 (exactly the room): available=" + rs.getSizeAvailable());
            try { rs.setRecord(1, new byte[221], 0, 221); p("grow by 1: allowed"); } catch (RecordStoreFullException e) { p("grow by 1: full, record 1 still " + rs.getRecordSize(1) + " bytes"); }
            try { rs.addRecord(new byte[0], 0, 0); p("empty record: added"); } catch (RecordStoreFullException e) { p("empty record: full (needs " + 8 + " bytes overhead)"); }
            try { RecordStore.openRecordStore("second", true); p("second store: created"); } catch (RecordStoreFullException e) { p("second store: full"); }
            rs.deleteRecord(1);
            p("after deleting the big record: available=" + rs.getSizeAvailable());
            RecordStore other = RecordStore.openRecordStore("second", true);
            p("second store created, size=" + other.getSize() + " available=" + other.getSizeAvailable() + " (quota is shared by the stores of the suite)");
            other.closeRecordStore();
            rs.closeRecordStore();
            RecordStore.deleteRecordStore("q");
            RecordStore.deleteRecordStore("second");
            p("after delete: " + RecordStore.listRecordStores());
        } catch (Throwable t) {
            p("FATAL " + t);
        }
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

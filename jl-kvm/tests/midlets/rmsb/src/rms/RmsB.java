package rms;

import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

/** T26 host test: suite RmsB (another vendor) reaches the shared stores of suite RmsA by vendor + suite name. */
public class RmsB extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    protected void startApp() {
        try {
            p("own stores: " + RecordStore.listRecordStores());
            final RecordStore pub = RecordStore.openRecordStore("pub", "jl-kvm", "RmsA");
            p("pub: records=" + pub.getNumRecords() + " first=" + new String(pub.getRecord(1)) + " name=" + pub.getName());
            try { pub.addRecord(new byte[1], 0, 1); p("pub add: allowed"); } catch (Throwable t) { p("pub add: " + cn(t)); }
            try { pub.setRecord(1, new byte[1], 0, 1); p("pub set: allowed"); } catch (Throwable t) { p("pub set: " + cn(t)); }
            try { pub.deleteRecord(1); p("pub delete: allowed"); } catch (Throwable t) { p("pub delete: " + cn(t)); }
            try { pub.setMode(RecordStore.AUTHMODE_PRIVATE, true); p("pub setMode: allowed"); } catch (Throwable t) { p("pub setMode: " + cn(t)); }
            p("pub still " + pub.getNumRecords() + " records, same object again: " + (RecordStore.openRecordStore("pub", "jl-kvm", "RmsA") == pub));
            pub.closeRecordStore();
            pub.closeRecordStore();
            p("pub closed: " + RecordStoreState.state(pub));
            RecordStore rw = RecordStore.openRecordStore("rw", "jl-kvm", "RmsA");
            rw.addRecord("fromB".getBytes(), 0, 5);
            p("rw: records=" + rw.getNumRecords());
            rw.closeRecordStore();
            try { RecordStore.openRecordStore("priv", "jl-kvm", "RmsA"); p("priv: opened"); } catch (Throwable t) { p("priv: " + cn(t)); }
            try { RecordStore.openRecordStore("nothing", "jl-kvm", "RmsA"); p("missing: opened"); } catch (Throwable t) { p("missing: " + cn(t)); }
            try { RecordStore.openRecordStore("pub", "jl-kvm", "NoSuite"); p("no suite: opened"); } catch (Throwable t) { p("no suite: " + cn(t)); }
            try { RecordStore.openRecordStore("pub", "someone", "RmsA"); p("other vendor: opened"); } catch (Throwable t) { p("other vendor: " + cn(t)); }
            RecordStore mine = RecordStore.openRecordStore("mine", true);
            RecordStore again = RecordStore.openRecordStore("mine", "jl-b", "RmsB");
            p("own suite by name: same object=" + (again == mine));
            mine.closeRecordStore();
            again.closeRecordStore();
            p("own list: " + RecordStore.listRecordStores().length);
        } catch (Throwable t) {
            p("FATAL " + t);
        }
        notifyDestroyed();
    }

    static final class RecordStoreState {
        static String state(RecordStore r) { try { r.getName(); return "open"; } catch (Exception e) { return cn(e); } }
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

package rms;

import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

/** T26 host test: suite RmsA creates stores on the first run and reads / extends them later (the record directory outlives a run). */
public class RmsA extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static void put(RecordStore rs, String s) throws Exception { rs.addRecord(s.getBytes(), 0, s.length()); }

    protected void startApp() {
        try {
            String[] names = RecordStore.listRecordStores();
            if (names == null) {
                p("run 1: creating");
                RecordStore pub = RecordStore.openRecordStore("pub", true, RecordStore.AUTHMODE_ANY, false);
                put(pub, "p1");
                put(pub, "p2");
                pub.closeRecordStore();
                RecordStore rw = RecordStore.openRecordStore("rw", true, RecordStore.AUTHMODE_ANY, true);
                put(rw, "w1");
                rw.closeRecordStore();
                RecordStore priv = RecordStore.openRecordStore("priv", true);
                put(priv, "x1");
                priv.closeRecordStore();
                RecordStore game = RecordStore.openRecordStore("game", true);
                put(game, "score=100");
                p("game version=" + game.getVersion() + " records=" + game.getNumRecords());
                game.closeRecordStore();
            } else {
                RecordStore game = RecordStore.openRecordStore("game", false);
                int n = game.getNumRecords();
                p("later run: stores=" + names.length + " game records=" + n + " nextId=" + game.getNextRecordID() + " version=" + game.getVersion());
                for (int i = 1; i < game.getNextRecordID(); i++) {
                    try { p("  record " + i + " = " + new String(game.getRecord(i)).trim()); } catch (Exception e) { p("  record " + i + " " + e.getClass().getName()); }
                }
                put(game, "score=" + (100 + 10 * n));
                if (n >= 2) game.deleteRecord(1);
                game.closeRecordStore();
                RecordStore rw = RecordStore.openRecordStore("rw", false);
                StringBuffer sb = new StringBuffer();
                for (int i = 1; i < rw.getNextRecordID(); i++) { try { sb.append(new String(rw.getRecord(i))).append(' '); } catch (Exception e) { } }
                p("rw holds: " + sb.toString().trim());
                rw.closeRecordStore();
            }
        } catch (Throwable t) {
            p("FATAL " + t);
        }
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}

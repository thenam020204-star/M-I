package jlkvm.host.rms;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Imports the record stores that JL-Mod / J2ME-Loader wrote (app/.../rms/impl/AndroidRecordStoreManager): in the data directory of a
 * game, {@code <name>.rsh} is the header ("MIDRMS", version 3.0, encrypted flag, UTF name, long lastModified, int version, int authmode,
 * byte writable, int record count, optional int lastRecordId) and {@code <name>.<id>.rsr} holds one record (int id, int tag, int length,
 * data). The file name is the store name with the characters {@code FileUtils.ILLEGAL_FILENAME_CHARS} removed, so the name inside the
 * header is used as the store name. Broken record files are skipped and listed in the result. The quota is NOT applied to an import
 * (a save that fitted on the old emulator must not be cut).
 */
public final class JlModRmsImport {
    private JlModRmsImport() { }

    /** Result of an import: stores read and problems found. */
    public static final class Result {
        public int stores, records;
        public final List<String> problems = new ArrayList<String>();
    }

    private static final byte[] ID = {'M', 'I', 'D', 'R', 'M', 'S'};

    /** Reads every store in {@code dir} into {@code suite} of {@code target}. */
    public static Result importDir(File dir, RecordStorage target, String suite) {
        Result r = new Result();
        File[] fs = dir.listFiles();
        if (fs == null) { r.problems.add("not a directory: " + dir); return r; }
        java.util.Arrays.sort(fs);
        for (File f : fs) {
            String fn = f.getName();
            if (!fn.endsWith(".rsh")) continue;
            String base = fn.substring(0, fn.length() - 4);
            try {
                RecordStorage.Store s = readHeader(f);
                File[] rs = dir.listFiles();
                java.util.Arrays.sort(rs);
                for (File rf : rs) {
                    String rn = rf.getName();
                    if (!rn.startsWith(base + ".") || !rn.endsWith(".rsr")) continue;
                    String mid = rn.substring(base.length() + 1, rn.length() - 4);
                    if (mid.length() == 0 || !isDigits(mid)) continue;   // another store whose name starts with "<base>."
                    try {
                        readRecord(rf, s);
                        r.records++;
                    } catch (IOException e) {
                        r.problems.add("record " + rn + ": " + e.getMessage());
                    }
                }
                target.restore(suite, s);
                r.stores++;
            } catch (IOException e) {
                r.problems.add("header " + fn + ": " + e.getMessage());
            }
        }
        return r;
    }

    private static boolean isDigits(String s) {
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) < '0' || s.charAt(i) > '9') return false;
        return true;
    }

    private static RecordStorage.Store readHeader(File f) throws IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
        try {
            for (int i = 0; i < ID.length; i++) if (in.read() != ID[i]) throw new IOException("not a MIDRMS header");
            in.readByte();
            in.readByte();
            in.readByte();                       // encrypted flag
            String name = in.readUTF();
            long lastModified = in.readLong();
            int version = in.readInt();
            int authmode = in.readInt();
            boolean writable = in.readByte() != 0;
            in.readInt();                        // record count
            int last = in.available() >= 4 ? in.readInt() : 0;
            RecordStorage.Store s = new RecordStorage.Store(name, authmode == 1 ? 1 : 0, writable);
            s.restoreHeader(version, lastModified, last + 1);
            return s;
        } finally {
            in.close();
        }
    }

    private static void readRecord(File f, RecordStorage.Store s) throws IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
        try {
            int id = in.readInt();
            in.readInt();                        // tag
            int len = in.readInt();
            if (len < 0 || len > (1 << 28)) throw new IOException("bad length " + len);
            byte[] b = new byte[len];
            in.readFully(b);
            s.restore(id, b);
        } finally {
            in.close();
        }
    }
}

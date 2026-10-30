package jlkvm.host.rms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import jlkvm.DeviceProfile;

/**
 * Record stores in a directory: {@code <root>/<suite>/<store>.rms}, one file per store, rewritten through a temporary file after
 * every change (a crash leaves the previous complete file). Names are escaped ({@code %XXXX} per UTF-16 unit outside [A-Za-z0-9_-])
 * so that every MIDP name (case sensitive, any character) is a valid file name on every platform. Pure java.io: usable on Android.
 * A broken file is skipped (reported to {@link #problems}).
 */
public final class FileRecordStorage extends RecordStorage {
    static final String MAGIC = "JLRMS001";
    private final File root;
    /** Files that could not be read or written (message per entry), for logs and tests. */
    public final java.util.List<String> problems = new java.util.ArrayList<String>();

    public FileRecordStorage(File root, DeviceProfile p) {
        super(p);
        this.root = root;
    }

    static String esc(String s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '-') b.append(c);
            else b.append('%').append(String.format("%04X", Integer.valueOf(c)));
        }
        return b.length() == 0 ? "%" : b.toString();
    }

    static String unesc(String s) {
        if (s.equals("%")) return "";
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '%' && i + 5 <= s.length()) { b.append((char) Integer.parseInt(s.substring(i + 1, i + 5), 16)); i += 4; }
            else b.append(c);
        }
        return b.toString();
    }

    private File dir(String suite) { return new File(root, esc(suite)); }
    private File file(String suite, String name) { return new File(dir(suite), esc(name) + ".rms"); }

    @Override protected void loadSuite(String suite, Map<String, Store> into) {
        File[] fs = dir(suite).listFiles();
        if (fs == null) return;
        for (File f : fs) {
            if (!f.getName().endsWith(".rms")) continue;
            try {
                Store s = read(f);
                if (s != null) into.put(s.name, s);
            } catch (IOException e) {
                problems.add("read " + f + ": " + e);
            } catch (RuntimeException e) {
                problems.add("read " + f + ": " + e);
            }
        }
    }

    private static Store read(File f) throws IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
        try {
            byte[] m = new byte[MAGIC.length()];
            in.readFully(m);
            if (!new String(m, "ISO-8859-1").equals(MAGIC)) throw new IOException("bad magic");
            String name = in.readUTF();
            Store s = new Store(name, in.readInt(), in.readBoolean());
            int version = in.readInt();
            long lm = in.readLong();
            int next = in.readInt();
            int n = in.readInt();
            if (n < 0) throw new IOException("bad record count");
            for (int i = 0; i < n; i++) {
                int id = in.readInt(), len = in.readInt();
                if (len < 0 || len > (1 << 28)) throw new IOException("bad record length");
                byte[] b = new byte[len];
                in.readFully(b);
                s.restore(id, b);
            }
            s.restoreHeader(version, lm, next);
            return s;
        } finally {
            in.close();
        }
    }

    @Override protected void saveStore(String suite, Store s) {
        File f = file(suite, s.name), tmp = new File(f.getPath() + ".tmp");
        try {
            f.getParentFile().mkdirs();
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(tmp)));
            try {
                out.write(MAGIC.getBytes("ISO-8859-1"));
                out.writeUTF(s.name);
                out.writeInt(s.authmode);
                out.writeBoolean(s.writable);
                out.writeInt(s.version);
                out.writeLong(s.lastModified);
                out.writeInt(s.nextId);
                out.writeInt(s.records.size());
                for (Map.Entry<Integer, byte[]> e : s.records.entrySet()) {
                    out.writeInt(e.getKey().intValue());
                    out.writeInt(e.getValue().length);
                    out.write(e.getValue());
                }
            } finally {
                out.close();
            }
            if (!tmp.renameTo(f)) {
                f.delete();
                if (!tmp.renameTo(f)) throw new IOException("rename failed");
            }
        } catch (IOException e) {
            problems.add("write " + f + ": " + e);
        }
    }

    @Override protected void deleteStore(String suite, String name) { file(suite, name).delete(); }
}

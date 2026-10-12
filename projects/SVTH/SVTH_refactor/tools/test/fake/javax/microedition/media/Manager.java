package javax.microedition.media;
import java.io.*;
/**
 * Manager giả: createPlayer đọc hết luồng, độ dài lấy từ header WAV (byteRate, chunk "data"). Nhật ký sự kiện có thời điểm (ns).
 * session 6: latencyMs = độ trễ thiết bị (Player còn STARTED tới start + latency + độ dài, như J2ME Loader/Android);
 * createMs = thời gian tạo + realize + prefetch một Player (máy thật hàng chục ms).
 */
public class Manager {
    public static final java.util.Vector LOG = new java.util.Vector();   // long[]{timeNs, kindCode, playerId, arg}
    public static int created = 0, failAfter = Integer.MAX_VALUE;
    public static volatile int latencyMs = 0, createMs = 0;
    /** session 7: mô phỏng J2ME Loader (MicroPlayer/AndroidPlayer): realize/prefetch không nạp; start() nạp (prepare, prepareMs) nếu chưa nạp;
     *  phát hết → reset (chưa nạp lại); stop() = pause (vẫn nạp, giữ vị trí); start() sau pause chạy tiếp từ vị trí cũ. */
    public static volatile boolean j2mel = false;
    public static volatile int prepareMs = 0;
    static int nextId = 1;
    public static final String[] KIND = {"start", "stop", "rewind", "vol", "tone", "create", "prepare", "astart", "pstart", "pstop"};
    static void log(String kind, int id, int arg) {
        int k = 0; for (int i = 0; i < KIND.length; i++) if (KIND[i].equals(kind)) k = i;
        synchronized (LOG) { LOG.addElement(new long[]{System.nanoTime(), k, id, arg}); }
    }
    static int le32(byte[] w, int o) { return (w[o] & 0xFF) | ((w[o + 1] & 0xFF) << 8) | ((w[o + 2] & 0xFF) << 16) | ((w[o + 3] & 0xFF) << 24); }
    static int durMs(byte[] w) {
        int p = 12, br = 0;
        while (p + 8 <= w.length) {
            int len = le32(w, p + 4);
            if (w[p] == 'f' && w[p + 1] == 'm') br = le32(w, p + 16);
            if (w[p] == 'd' && w[p + 1] == 'a') return br > 0 ? (int) ((long) Math.min(len, w.length - p - 8) * 1000 / br) : 1;
            p += 8 + len + (len & 1);
        }
        return Math.max(1, (w.length - 44) / 8);
    }
    public static synchronized Player createPlayer(InputStream in, String type) throws IOException, MediaException {
        if (created >= failAfter) throw new MediaException("fake: out of players");
        ByteArrayOutputStream bo = new ByteArrayOutputStream(); byte[] b = new byte[1024]; int n;
        while ((n = in.read(b)) > 0) bo.write(b, 0, n);
        if (createMs > 0) try { Thread.sleep(createMs); } catch (InterruptedException e) { }
        created++;
        Player p = new Player(nextId++, Math.max(1, durMs(bo.toByteArray())));
        log("create", p.id, p.durMs);
        return p;
    }
    public static void playTone(int note, int dur, int vol) throws MediaException { log("tone", 0, note); }
}

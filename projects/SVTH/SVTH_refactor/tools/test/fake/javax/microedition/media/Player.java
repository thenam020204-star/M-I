package javax.microedition.media;
/**
 * Player giả (kiểm thử trên JVM thường): ghi lại start/stop/rewind kèm thời điểm; STARTED trong suốt độ trễ + độ dài tiếng rồi về PREFETCHED.
 * rewind/start ghi arg = 1 nếu Player còn STARTED (tức là tua/đè lên tiếng đang kêu).
 * session 7: chế độ Manager.j2mel mô phỏng J2ME Loader (xem Manager). Mỗi start() khi âm lượng > 0 ghi thêm "astart" với arg bitmask:
 * 1 = Player đang kêu, 2 = phải prepare ngay trong start() (chậm), 4 = không bắt đầu từ vị trí 0.
 */
public class Player {
    public static final int CLOSED = 0, UNREALIZED = 100, REALIZED = 200, PREFETCHED = 300, STARTED = 400;
    public final int id; public final int durMs;
    int state = UNREALIZED; long startedAtNs = 0; int lat = 0;
    boolean loaded = false; int posMs = 0; int level = 100;
    public Player(int id, int durMs) { this.id = id; this.durMs = durMs; }
    public void realize() { if (state == UNREALIZED) state = REALIZED; }
    public void prefetch() { if (state < PREFETCHED) state = PREFETCHED; if (!Manager.j2mel) loaded = true; }
    private long elapsedMs() { return (System.nanoTime() - startedAtNs) / 1000000L; }
    public synchronized int getState() {
        if (state == STARTED && elapsedMs() >= durMs - posMs + lat) {
            state = PREFETCHED; posMs = Manager.j2mel ? 0 : durMs;
            if (Manager.j2mel) loaded = false;                       // complete() → doReset()
        }
        return state;
    }
    public synchronized void start() throws MediaException {
        boolean playing = getState() == STARTED;
        Manager.log(level > 0 ? "start" : "pstart", id, playing ? 1 : 0);    // âm lượng 0 = làm ấm (không nghe thấy): ghi riêng
        if (playing) { if (level > 0) Manager.log("astart", id, 1); return; }
        if (state < PREFETCHED) prefetch();
        boolean prep = false;
        if (Manager.j2mel && !loaded) {
            prep = true; Manager.log("prepare", id, 0);
            if (Manager.prepareMs > 0) try { Thread.sleep(Manager.prepareMs); } catch (InterruptedException e) { }
            loaded = true;
        }
        if (!Manager.j2mel && posMs >= durMs) posMs = 0;                 // hết tiếng rồi start lại: phát từ đầu (đặc tả MMAPI)
        if (level > 0) Manager.log("astart", id, (prep ? 2 : 0) | (posMs != 0 ? 4 : 0));
        state = STARTED; startedAtNs = System.nanoTime(); lat = Manager.latencyMs;
    }
    public synchronized void stop() throws MediaException {
        boolean playing = getState() == STARTED;
        Manager.log(level > 0 ? "stop" : "pstop", id, playing ? 1 : 0);
        if (playing) { long e = elapsedMs() - lat; posMs = (int) Math.max(0, Math.min(durMs, posMs + e)); }
        state = PREFETCHED;
    }
    public synchronized long setMediaTime(long t) throws MediaException {
        boolean playing = getState() == STARTED;
        Manager.log("rewind", id, playing ? 1 : 0);
        if (playing) startedAtNs = System.nanoTime() - lat * 1000000L;   // tua giữa chừng: tiếng chạy lại từ đầu (ghost replay)
        posMs = (int) (t / 1000);
        return t;
    }
    public synchronized void deallocate() { if (getState() == STARTED) state = PREFETCHED; if (state > REALIZED) state = REALIZED; loaded = false; posMs = 0; }
    public void close() { state = CLOSED; }
    public Control getControl(String n) { return n.equals("VolumeControl") ? new javax.microedition.media.control.VolumeControl() {
        public int setLevel(int l) { Manager.log("vol", id, l); level = l; return l; }
        public int getLevel() { return level; } } : null; }
}

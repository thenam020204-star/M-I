/**
 * Bộ điều phối SFX độ trễ thấp: hàng đợi cố định (không cấp phát khi post), một thread riêng ưu tiên cao, tiếng phát chồng lên nhau,
 * không bao giờ cắt tiếng đang phát để nhường chỗ (BeepBackend dùng bể Player), tự hạ nhạc nền khi SFX phát (BgmDuck).
 * Xoá sự kiện CHƯA phát khi hủy/hết animation (flushAll); chỉ dừng tiếng đang phát khi tạm dừng/tắt SFX (stopAll).
 * Mọi lỗi bị bắt (Throwable): SFX không bao giờ phá lượt đánh.
 * Logic chiến đấu chỉ gọi SfxHooks → SFXManager.post(); đổi sang âm thật chỉ cần thay file wav / BeepBackend.
 */
public final class SFXManager implements Runnable {
    private static final int Q = 32;
    private static final int[] qType = new int[Q], qVar = new int[Q], qPrio = new int[Q], qActor = new int[Q], qLog = new int[Q];
    private static final long[] qDue = new long[Q];
    private static final int[] qRetry = new int[Q];
    /** Tiếng bị hoãn vì bể Player đầy được hẹn phát lại tối đa RETRY_MAX lần, mỗi lần chỉ nếu Player rảnh trong vòng RETRY_MS (phát hơi trễ còn hơn bỏ, và không cắt tiếng cũ). */
    // session 6: a sound played 150-300 ms late lands on top of the next one ("chồng loạn"); retry once, and only if a Player frees within 60 ms.
    private static final int RETRY_MAX = 1, RETRY_MS = 60;
    private static final boolean[] qUsed = new boolean[Q];
    private static final int MAXV = 16;

    private static final Object lock = new Object();
    private static Thread thread;
    private static BeepBackend backend;
    private static boolean wantPreload = false;
    private static boolean configLoaded = false;

    public static volatile boolean enabled = true;
    /** Âm lượng SFX 0..100 (0 = tắt). Độc lập với nhạc nền: chỉnh trong menu game "Cài đặt SFX" (SfxUi), mặc định 70, luôn bật khi vào game. */
    public static volatile int volume = 70;
    /** Số tiếng tối đa đang phát cùng lúc (1..MAXV); quá mức thì tiếng mới bị bỏ (DROPPED), tiếng đang phát không bị cắt. CRIT được thêm 2. */
    public static volatile int maxVoices = 12;
    /** Trễ lớp chồng (ms) so với tiếng chính: 0 = phát đồng thời (mặc định, như game hiện đại). Máy không trộn được nhiều tiếng có thể đặt 35/60 trong config. */
    public static volatile int critDelay = 0, healDelay = 0;
    /**
     * session 6: bù độ trễ thiết bị (ms) cho tiếng va chạm của kỹ năng: SfxHooks đặt tiếng sớm hơn frame va chạm (đã biết trước trong map.txt,
     * kết quả trúng/né đã tính lúc tung) đúng `lead` ms. 0 = như cũ (phát đúng lúc frame va chạm hiện). Cấu hình `lead=`.
     */
    public static volatile int lead = 60;
    /** Dùng cho test: ép SFX ném lỗi trong thread để chứng minh trận không bị ảnh hưởng. */
    public static volatile boolean injectFailure = false;
    public static volatile int posted = 0, played = 0, apiCalls = 0;

    public static void init() {
        try {
            if (!configLoaded) { configLoaded = true; loadConfig(); }   // chỉ nạp một lần: không đè âm lượng người chơi chỉnh trong menu
            synchronized (lock) {
                if (backend == null) { backend = new BeepBackend(); backend.startWorker(); }   // session 7: thread nạp riêng (tạo / làm ấm Player)
                wantPreload = enabled && volume > 0;   // SFX tắt (âm lượng 0) → không nạp Player; bật lại giữa trận thì nạp ở thread SFX lúc rảnh
                backend.setLoadAllowed(wantPreload);
                if (thread == null) {
                    thread = new Thread(new SFXManager());
                    thread.start();
                }
                lock.notifyAll();
            }
        } catch (Throwable t) { /* bỏ qua */ }
    }

    /** /sfx/config.txt (tuỳ chọn): enabled=1, volume=70, maxvoices=12, pool=3 (Player/tiếng), players=40 (tổng Player SFX), duck=45 (% nhạc khi hạ; 100 = tắt), duckhold=200, duckramp=150, critdelay=0, healdelay=0, lead=60, solo=1, gap=40, trim=150. */
    private static void loadConfig() {
        try {
            java.io.InputStream in = new SFXManager().getClass().getResourceAsStream("/sfx/config.txt");
            if (in == null) return;
            StringBuffer sb = new StringBuffer();
            int c;
            while ((c = in.read()) >= 0) sb.append((char) c);
            in.close();
            String s = sb.toString();
            int p = 0;
            while (p < s.length()) {
                int e = s.indexOf('\n', p);
                if (e < 0) e = s.length();
                String line = s.substring(p, e).trim();
                p = e + 1;
                int eq = line.indexOf('=');
                if (eq <= 0 || line.charAt(0) == '#') continue;
                String k = line.substring(0, eq).trim(), v = line.substring(eq + 1).trim();
                int n = 0;
                try { n = Integer.parseInt(v); } catch (Throwable t) { continue; }
                if (k.equals("enabled")) enabled = n != 0;
                else if (k.equals("volume")) setVolume(n);
                else if (k.equals("maxvoices")) setMaxVoices(n);
                else if (k.equals("pool")) BeepBackend.poolMax = n;
                else if (k.equals("players")) BeepBackend.totalMax = n < 1 ? 1 : n;
                else if (k.equals("duck")) BgmDuck.pct = n < 0 ? 0 : (n > 100 ? 100 : n);
                else if (k.equals("duckhold")) BgmDuck.holdMs = n < 0 ? 0 : n;
                else if (k.equals("duckramp")) BgmDuck.rampMs = n < 0 ? 0 : n;
                else if (k.equals("critdelay")) critDelay = n < 0 ? 0 : n;
                else if (k.equals("healdelay")) healDelay = n < 0 ? 0 : n;
                else if (k.equals("lead")) lead = n < 0 ? 0 : (n > 300 ? 300 : n);
                else if (k.equals("solo")) BeepBackend.solo = n != 0;
                else if (k.equals("gap")) BeepBackend.gapMs = n < 0 ? 0 : n;
                else if (k.equals("trim")) BeepBackend.trimMs = n < 0 ? 0 : n;
                else if (k.equals("warm")) BeepBackend.warmMax = n < 0 ? 0 : n;
                else if (k.equals("prime")) BeepBackend.prime = n != 0;
            }
        } catch (Throwable t) { }
    }

    /** Tiếng thử khi người chơi chỉnh âm lượng SFX trong menu (HIT, giọng quái). Không qua log/hook trận. */
    public static void preview() { if (thread == null) init(); post(SFXType.HIT, 0, -1, 0, 0); }

    public static void setEnabled(boolean on) { enabled = on; if (!on) stopAll(); }
    public static void setVolume(int v) {
        int old = volume;
        volume = v < 0 ? 0 : (v > 100 ? 100 : v);
        if (old <= 0 && volume > 0) wake(true);        // bật lại SFX: nạp sẵn Player ở thread SFX để tiếng đầu không phải chờ tạo
    }
    private static void wake(boolean preload) {
        try { synchronized (lock) { if (preload && enabled) { wantPreload = true; if (backend != null) backend.setLoadAllowed(true); } lock.notifyAll(); } } catch (Throwable t) { }
    }
    public static void setMaxVoices(int n) { maxVoices = n < 1 ? 1 : (n > MAXV ? MAXV : n); }

    /**
     * Xếp một sự kiện. delayMs > 0 dùng cho lớp phát sau (ví dụ CRIT chồng lên HIT).
     * @return false nếu bị loại ngay (tắt/đầy hàng đợi)
     */
    public static boolean post(int type, int variant, int actor, int delayMs, int logSeq) {
        try {
            posted++;
            if (!enabled || volume <= 0) { if (logSeq != 0) SfxLog.setStatus(logSeq, SfxLog.ST_OFF); return false; }
            int prio = SFXType.priority(type);
            BeepBackend be = backend;
            if (be != null) be.touch(variant);                    // session 7: giọng vừa xuất hiện → ưu tiên giữ ấm
            synchronized (lock) {
                int free = -1, lowest = -1;
                for (int i = 0; i < Q; i++) {
                    if (!qUsed[i]) { if (free < 0) free = i; }
                    else if (lowest < 0 || qPrio[i] < qPrio[lowest]) lowest = i;
                }
                if (free < 0) {
                    if (lowest >= 0 && qPrio[lowest] < prio) {
                        SfxLog.setStatus(qLog[lowest], SfxLog.ST_DROPPED);
                        free = lowest;
                    } else {
                        if (logSeq != 0) SfxLog.setStatus(logSeq, SfxLog.ST_DROPPED);
                        return false;
                    }
                }
                qUsed[free] = true; qType[free] = type; qVar[free] = variant; qPrio[free] = prio;
                qActor[free] = actor; qLog[free] = logSeq; qDue[free] = System.currentTimeMillis() + delayMs; qRetry[free] = 0;
                lock.notifyAll();
            }
            return true;
        } catch (Throwable t) { return false; }
    }

    /** Xoá mọi sự kiện đang chờ của một actor (hủy/chết/hết animation). */
    public static void flushActor(int actor) {
        try {
            synchronized (lock) {
                for (int i = 0; i < Q; i++) if (qUsed[i] && qActor[i] == actor) { qUsed[i] = false; SfxLog.setStatus(qLog[i], SfxLog.ST_FLUSHED); }
            }
        } catch (Throwable t) { }
    }

    private static boolean requeue(int type, int variant, int prio, int actor, int logSeq, long due, int retried) {
        synchronized (lock) {
            for (int i = 0; i < Q; i++) if (!qUsed[i]) {
                qUsed[i] = true; qType[i] = type; qVar[i] = variant; qPrio[i] = prio; qActor[i] = actor; qLog[i] = logSeq; qDue[i] = due; qRetry[i] = retried;
                return true;
            }
        }
        return false;
    }

    /** Xoá mọi sự kiện CHƯA phát (kết thúc trận, hủy animation). Tiếng đang phát được để chạy hết đuôi — không cắt. */
    public static void flushAll() {
        try {
            synchronized (lock) {
                for (int i = 0; i < Q; i++) if (qUsed[i]) { qUsed[i] = false; SfxLog.setStatus(qLog[i], SfxLog.ST_FLUSHED); }
            }
        } catch (Throwable t) { }
    }

    /** Xoá hàng đợi VÀ dừng tiếng đang phát, trả nhạc nền về mức cũ (tạm dừng game, tắt SFX). */
    public static void stopAll() {
        try {
            flushAll();
            BeepBackend b = backend;
            if (b != null) b.stopAll();
            BgmDuck.restoreNow();
            synchronized (lock) { if (enabled && volume > 0) wantPreload = true; lock.notifyAll(); }   // session 7: Player vừa dừng → làm ấm lại lúc rảnh
        } catch (Throwable t) { }
    }

    public static int pending() {
        int n = 0;
        synchronized (lock) { for (int i = 0; i < Q; i++) if (qUsed[i]) n++; }
        return n;
    }

    public static BeepBackend backend() { return backend; }
    /** session 7: báo giọng có mặt trong trận (thread game; chỉ ghi số nguyên). */
    public static void touchVoice(int v) { BeepBackend b = backend; if (b != null) b.touch(v); }

    /** Thời điểm (ms epoch) thread SFX cần làm việc nền (dọn Player hết tiếng, trả nhạc nền); 0 = không. Chỉ thread SFX đụng tới. */
    private long bgNext = 0;
    /** session 7: còn việc nền nặng (tạo / làm ấm Player) — làm từng việc một giữa các tiếng tới hạn. Chỉ thread SFX đụng tới. */
    private boolean bgWork = true;

    /** Việc nền của thread phát: chỉ còn trả nhạc nền (BgmDuck). Tạo / làm ấm Player nằm ở thread nạp của BeepBackend. */
    private void background(BeepBackend b, long now) {
        long n2 = 0;
        try { n2 = BgmDuck.tick(now); } catch (Throwable t) { }
        bgWork = false;
        bgNext = n2;
    }

    public void run() {
        try { Thread.currentThread().setPriority(Thread.NORM_PRIORITY + 2); } catch (Throwable t) { }   // ưu tiên hơn thread game một chút để start() không phải chờ
        while (true) {
            try {
                int idx = -1;
                int type = 0, var = 0, prio = 0, lg = 0, actor = -1, retried = 0;
                boolean work = false;
                synchronized (lock) {
                    if (wantPreload) { wantPreload = false; if (backend != null) backend.kick(volume); }
                    long now = System.currentTimeMillis(), best = Long.MAX_VALUE;
                    for (int i = 0; i < Q; i++) if (qUsed[i] && qDue[i] < best) { best = qDue[i]; idx = i; }
                    if (idx >= 0 && best <= now) {                       // tiếng tới hạn luôn đi trước việc nền
                        type = qType[idx]; var = qVar[idx]; prio = qPrio[idx]; lg = qLog[idx]; actor = qActor[idx]; retried = qRetry[idx];
                        qUsed[idx] = false;
                    } else if (bgWork || (bgNext != 0 && bgNext <= now)) {
                        idx = -1; work = true;
                    } else {
                        long target = bgNext;
                        if (idx >= 0 && (target == 0 || best < target)) target = best;
                        if (target == 0) lock.wait();
                        else if (target > now) lock.wait(target - now);
                        continue;
                    }
                }
                BeepBackend b = backend;
                if (work) { background(b, System.currentTimeMillis()); continue; }
                if (b == null) continue;
                if (injectFailure) throw new RuntimeException("SFX injected failure");
                long now = System.currentTimeMillis();
                if (b.busy(now) >= maxVoices + (prio >= 5 ? 2 : 0)) { SfxLog.setStatus(lg, SfxLog.ST_DROPPED); continue; }   // quá nhiều tiếng cùng lúc: bỏ tiếng MỚI, không cắt tiếng đang phát
                SfxLog.setStatus(lg, SfxLog.ST_REQUESTED);
                int r = b.play(type, var, actor, volume);
                if (r >= 1) {
                    BgmDuck.onSfx(now, b.durationMs(type, var));          // hạ nhạc nền ngay sau khi tiếng đã bắt đầu
                    bgWork = true;                                         // tính lại lịch trả nhạc nền
                }
                if (r == 2) { SfxLog.setStatus(lg, SfxLog.ST_PLAYED); played++; }
                else if (r == 1) { apiCalls++; /* giữ REQUESTED: API đã gọi nhưng chưa xác nhận đang phát */ }
                else if (r == -2) SfxLog.setStatus(lg, SfxLog.ST_DUP);          // tiếng solo của chính actor còn kêu / HIT trùng trong gap ms: bỏ qua, không chồng, không cắt
                else if (r < 0) {                                              // mọi Player của tiếng này đang kêu: hẹn lại khi rảnh (≤ 60 ms), không cắt tiếng cũ
                    long at = b.retryAt;
                    if (retried < RETRY_MAX && at - now <= RETRY_MS && requeue(type, var, prio, actor, lg, at + 1, retried + 1)) { /* chờ lượt sau */ }
                    else SfxLog.setStatus(lg, SfxLog.ST_DROPPED);
                }
                else SfxLog.setStatus(lg, SfxLog.ST_FAILED);
            } catch (Throwable t) {
                try { SfxLog.add(SfxLog.REC_ACTION, -1, 0, 0, 0, 0, 0, 0, 0, SfxLog.ST_FAILED); Thread.sleep(5); } catch (Throwable t2) { }
            }
        }
    }
}

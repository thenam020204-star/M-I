import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

/**
 * session 7: bộ phát SFX viết lại theo mô hình engine âm thanh game hiện đại (ngân hàng asset + pool voice + chính sách theo sự kiện
 * + làm ấm trước), giữ nguyên giao diện công khai cho SFXManager / test.
 *
 * <pre>
 *  Asset   (loại, giọng) → byte[] WAV (đọc một lần, bỏ im lặng đầu file), độ dài từ header.
 *  Voice   một Player: COLD (đã tạo, chưa nạp) → READY (đã làm ấm: nạp + dừng ở 0, start() tức thì)
 *          → PLAYING (đã start, tới khi getState() != STARTED) → DONE (hết tiếng) → làm ấm lại lúc rảnh → READY.
 *  Policy  SKILL/HEAL/nguyên tố: tối đa 1 tiếng/chủ thể (actor) — gọi lại khi tiếng của chính actor đó còn kêu thì bỏ qua;
 *          actor khác vẫn phát được (pool riêng). HIT/MISS/CRIT: chồng tự do, hai lần start cùng (loại, giọng) cách nhau < gap ms thì gộp.
 *          Không bao giờ stop() tiếng đang kêu để nhường chỗ (chỉ stopAll khi tạm dừng / tắt SFX).
 *  Warm    tối đa `warmMax` Player được giữ ấm (mỗi Player ấm trên Android giữ một AudioTrack), ưu tiên tiếng hay dùng của các giọng
 *          vừa xuất hiện trong trận. Player còn lại để COLD (vẫn phát được, chậm hơn).
 * </pre>
 *
 * Vì sao phải làm ấm (đọc mã J2ME Loader: MicroPlayer/AndroidPlayer/BasePlayer): realize()/prefetch() KHÔNG chuẩn bị MediaPlayer;
 * start() mới setDataSource + prepare() (đồng bộ, chậm); phát hết → complete() → MediaPlayer.reset() nên lần start sau lại prepare;
 * stop() = pause() (vẫn nạp); createPlayer(InputStream) ghi file tạm + FFprobe. Bản ≤ 1.6.0 "nạp sẵn" bằng realize/prefetch nên
 * trên J2ME Loader mọi tiếng đều prepare ngay lúc phát. Làm ấm = âm lượng 0 → start() → stop() → setMediaTime(0) → trả âm lượng:
 * MediaPlayer đã prepare, dừng ở 0, lần start() thật chỉ là resume. Làm lại sau mỗi lần phát hết, ở thread SFX lúc rảnh.
 * Máy không có VolumeControl (hoặc prime=0 trong config) thì không làm ấm (tránh tiếng "tách").
 * CLDC 1.0: không float/double; không cấp phát trong play().
 */
public final class BeepBackend implements Runnable {
    private static final int RATE = 8000;
    /** tần số Hz theo [loại] cho biến thể 0; biến thể 1 (thương) trầm hơn, 2 (súng) cao hơn. */
    private static final int[] FREQ = {330, 180, 520, 700, 880, 440, 150, 1040, 1320};
    private static final int[] DUR = {70, 90, 60, 120, 110, 110, 130, 100, 150};
    private static final int[] SLIDE = {-60, -20, 40, 80, 60, -40, -30, 90, 120}; // Hz thay đổi mỗi 100 ms (đơn giản hoá)
    private static final int N = SFXType.COUNT * SFXType.VARIANTS;
    private static final int MAXPOOL = 4;
    /** dư thời gian sau độ dài lý thuyết trước khi hỏi getState() (ms). */
    private static final int TAIL_MS = 25;
    /** Player vẫn STARTED quá busyUntil + STUCK_MS: coi là kẹt (máy không báo hết tiếng) → stop() rồi dùng lại (tiếng đã hết từ lâu). */
    private static final int STUCK_MS = 1500;
    /** Hỏi lại trạng thái Player đang kêu quá thời lượng lý thuyết sau mỗi POLL_MS. */
    private static final int POLL_MS = 30;
    /** Thứ tự ưu tiên làm ấm theo loại: HIT MISS CRIT SKILL HEAL FIRE WATER EARTH WIND. */
    private static final int[] TYPE_RANK = {0, 1, 2, 3, 5, 6, 7, 8, 4};

    static final int COLD = 0, READY = 1, PLAYING = 2, DONE = 3, WORK = 4;   // WORK: thread nạp đang làm ấm / hạ ấm Player này

    /** Khoá ngắn bảo vệ pool/trạng thái voice. Thread phát giữ nó trong cả play() (nhanh); thread nạp chỉ giữ khi chọn việc / chốt
     *  trạng thái, KHÔNG giữ trong lúc tạo/làm ấm Player (chậm) — nên tiếng tới hạn không bao giờ phải chờ việc nặng. */
    private final Object lk = new Object();
    private Thread worker;
    private boolean kicked = false;
    private volatile int workVolume = 70;

    /** Một voice (Player). */
    private static final class Inst {
        Player p;
        VolumeControl vc;
        int st = COLD, owner = -1, vol = -1;
        boolean noWarm = false;                         // làm ấm từng lỗi: không thử lại (Player vẫn phát được)
        long startedAt = 0, busyUntil = 0;
    }

    // ---- cấu hình (SFXManager.loadConfig) ----
    /** Số Player tối đa mỗi (loại, giọng) (`pool=`, 1..MAXPOOL). */
    public static volatile int poolMax = 3;
    /** Tổng số Player SFX tối đa (`players=`). */
    public static volatile int totalMax = 40;
    /** Số Player được giữ ấm tối đa (`warm=`, mặc định 26 = 2 giọng × 13): mỗi Player ấm trên Android giữ một AudioTrack (máy cũ giới hạn ~32 cho cả nhạc nền). */
    public static volatile int warmMax = 26;
    /** Làm ấm Player (`prime=`, 1 = bật). Chỉ có tác dụng khi Player có VolumeControl. */
    public static volatile boolean prime = true;
    /** SKILL/HEAL/nguyên tố tối đa 1 tiếng mỗi actor (`solo=`). */
    public static volatile boolean solo = true;
    /** HIT/MISS/CRIT cùng (loại, giọng) start cách nhau < gapMs thì gộp (`gap=`). */
    public static volatile int gapMs = 40;
    /** Bỏ tối đa trimMs ms im lặng đầu WAV, chỉ trong bộ nhớ (`trim=`). */
    public static volatile int trimMs = 150;

    // ---- asset ----
    private final byte[][] data = new byte[N][];
    private final int[] durMs = new int[N];
    private final boolean[] fromFile = new boolean[N];
    private final int[] trimmed = new int[N];
    // ---- voice ----
    private final Inst[][] pool = new Inst[N][];
    private final int[] size = new int[N];
    private final int[] want = new int[N];             // số Player mong muốn cho (loại, giọng): 1, 2 cho HIT/MISS/CRIT, tăng khi thấy chồng
    private final boolean[] failed = new boolean[N];
    private final long[] lastStart = new long[N];
    private boolean toneFallback = false, mmapiBroken = false;
    private int hardCap = Integer.MAX_VALUE;
    // ---- giọng xuất hiện gần đây (ưu tiên làm ấm) ----
    private final int[] voiceSeen = new int[SFXType.VARIANTS];
    private int seenSeq = 0;
    private boolean loadAllowed = true, assetsLoaded = false;

    /** Chẩn đoán: lỗi cuối cùng khi tạo/phát Player. */
    public String lastError = null;
    public int created = 0, createFailed = 0, grown = 0, stopped = 0, skipped = 0, unstuck = 0, primed = 0, cooled = 0, coldStarts = 0;
    /** Khi play() trả -1: thời điểm (ms epoch) Player của tiếng đó sớm nhất rảnh lại. */
    public long retryAt = 0;

    public static int key(int type, int variant) { return type * SFXType.VARIANTS + variant; }
    static boolean isSolo(int type) { return type >= SFXType.SKILL; }
    /** Số Player nạp sẵn: 2 cho HIT/MISS/CRIT (đòn nhiều nhịp chồng nhau) và cho SKILL giọng 0 (sủng vật và quái dùng chung giọng 0,
     *  hay tung kỹ năng sát nhau — mỗi actor một tiếng), 1 cho tiếng còn lại; tự tăng tới poolMax khi thấy chồng. */
    private static int baseWant(int k) { int t = k / SFXType.VARIANTS; return !isSolo(t) || (t == SFXType.SKILL && k % SFXType.VARIANTS == 0) ? 2 : 1; }
    private static int limit() { int m = poolMax; return m < 1 ? 1 : (m > MAXPOOL ? MAXPOOL : m); }

    public BeepBackend() { for (int k = 0; k < N; k++) want[k] = baseWant(k); }

    public int durationMs(int type) { return DUR[type]; }
    public int durationMs(int type, int variant) { int d = durMs[key(type, variant)]; return d > 0 ? d : DUR[type]; }

    private static int freqFor(int type, int variant) {
        int f = FREQ[type];
        if (variant == 1) f = f * 3 / 4;
        else if (variant == 2) f = f * 5 / 4;
        return f;
    }

    /** Sinh WAV beep; mảng mới chỉ tạo khi nạp (không trong vòng animation). */
    static byte[] makeWav(int type, int variant) {
        int ms = DUR[type];
        int n = RATE * ms / 1000;
        byte[] w = new byte[44 + n];
        putStr(w, 0, "RIFF"); put32(w, 4, 36 + n); putStr(w, 8, "WAVE"); putStr(w, 12, "fmt ");
        put32(w, 16, 16); put16(w, 20, 1); put16(w, 22, 1); put32(w, 24, RATE); put32(w, 28, RATE); put16(w, 32, 1); put16(w, 34, 8);
        putStr(w, 36, "data"); put32(w, 40, n);
        int f0 = freqFor(type, variant);
        int slide = SLIDE[type];
        int phase = 0; // Q16
        int seed = 12345 + type * 977;
        for (int i = 0; i < n; i++) {
            int tms = i * 1000 / RATE;                   // ms từ đầu
            int f = f0 + slide * tms / 100;
            if (f < 60) f = 60;
            phase += f * 65536 / RATE;
            int env = 256 - (i * 256 / n);               // giảm dần tuyến tính
            int s;
            if (type == HIT_NOISE || type == SFXType.MISS) {
                seed = seed * 1103515245 + 12345;         // nhiễu cho đòn trúng/hụt
                s = ((seed >> 16) & 0x3F) - 32 + (((phase & 0xFFFF) < 32768) ? 24 : -24);
            } else {
                s = ((phase & 0xFFFF) < 32768) ? 48 : -48; // sóng vuông
            }
            int v = 128 + s * env / 256;
            w[44 + i] = (byte) v;
        }
        return w;
    }
    private static final int HIT_NOISE = SFXType.HIT;

    private static void putStr(byte[] w, int o, String s) { for (int i = 0; i < 4; i++) w[o + i] = (byte) s.charAt(i); }
    private static void put32(byte[] w, int o, int v) { w[o] = (byte) v; w[o + 1] = (byte) (v >> 8); w[o + 2] = (byte) (v >> 16); w[o + 3] = (byte) (v >> 24); }
    private static void put16(byte[] w, int o, int v) { w[o] = (byte) v; w[o + 1] = (byte) (v >> 8); }
    private static int get32(byte[] w, int o) { return (w[o] & 0xFF) | ((w[o + 1] & 0xFF) << 8) | ((w[o + 2] & 0xFF) << 16) | ((w[o + 3] & 0xFF) << 24); }

    /** Độ dài ms theo header WAV (byteRate, kích thước chunk "data"); 0 nếu không đọc được. Số nguyên, không float. */
    static int wavDurationMs(byte[] w) {
        try {
            if (w.length < 44 || w[0] != 'R' || w[1] != 'I' || w[2] != 'F' || w[3] != 'F') return 0;
            int p = 12, byteRate = 0, dataLen = 0;
            while (p + 8 <= w.length) {
                int len = get32(w, p + 4);
                boolean isFmt = w[p] == 'f' && w[p + 1] == 'm' && w[p + 2] == 't';
                boolean isData = w[p] == 'd' && w[p + 1] == 'a' && w[p + 2] == 't' && w[p + 3] == 'a';
                if (isFmt && p + 20 <= w.length) byteRate = get32(w, p + 16);
                if (isData) { dataLen = len > 0 && len <= w.length - p - 8 ? len : w.length - p - 8; break; }
                if (len < 0 || p + 8 + len < p) break;
                p += 8 + len + (len & 1);
            }
            if (byteRate <= 0 || dataLen <= 0) return 0;
            long ms = (long) dataLen * 1000L / byteRate;
            return ms > 30000 ? 30000 : (int) ms;
        } catch (Throwable t) { return 0; }
    }

    /** Đọc /sfx/<TÊN>_<v>.wav rồi /sfx/<TÊN>.wav; không có thì beep. Chỉ gọi ở thread SFX. */
    private void load(int type, int variant) {
        int k = key(type, variant);
        if (data[k] != null) return;
        byte[] b = null;
        try {
            String nm = SFXType.name(type);
            String[] cands = {"/sfx/" + nm + "_" + variant + ".wav", "/sfx/" + nm + ".wav"};
            for (int i = 0; i < cands.length && b == null; i++) {
                InputStream in = getClass().getResourceAsStream(cands[i]);
                if (in == null) continue;
                try {
                    java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
                    byte[] buf = new byte[2048];
                    int n;
                    while ((n = in.read(buf)) > 0) bo.write(buf, 0, n);
                    b = bo.toByteArray();
                    if (b.length > 0) fromFile[k] = true; else b = null;
                } finally { try { in.close(); } catch (Throwable t) { } }
            }
        } catch (Throwable t) { lastError = String.valueOf(t); b = null; }
        if (b == null) { b = makeWav(type, variant); fromFile[k] = false; }
        else { byte[] t = trimLead(b, trimMs); if (t != b) { trimmed[k] = wavDurationMs(b) - wavDurationMs(t); b = t; } }
        data[k] = b;
        int d = wavDurationMs(b);
        durMs[k] = d > 0 ? d : DUR[type];
    }

    /**
     * session 6: bỏ đoạn im lặng thật ở đầu WAV PCM (|mẫu| < ~1% toàn thang, tức -40 dBFS), tối đa maxMs, để tiếng không "trễ" vì chính file.
     * Trả mảng mới (header 44 byte chuẩn + dữ liệu từ mẫu đầu có tiếng) hoặc chính w nếu không cắt / không đọc được. Chỉ gọi lúc nạp.
     */
    static byte[] trimLead(byte[] w, int maxMs) {
        try {
            if (maxMs <= 0 || w.length < 44 || w[0] != 'R' || w[8] != 'W') return w;
            int p = 12, fmt = 0, ch = 0, rate = 0, byteRate = 0, align = 0, bits = 0, dOff = -1, dLen = 0;
            while (p + 8 <= w.length) {
                int len = get32(w, p + 4);
                if (w[p] == 'f' && w[p + 1] == 'm' && w[p + 2] == 't' && p + 24 <= w.length) {
                    fmt = (w[p + 8] & 0xFF) | ((w[p + 9] & 0xFF) << 8); ch = (w[p + 10] & 0xFF) | ((w[p + 11] & 0xFF) << 8);
                    rate = get32(w, p + 12); byteRate = get32(w, p + 16); align = (w[p + 20] & 0xFF) | ((w[p + 21] & 0xFF) << 8); bits = (w[p + 22] & 0xFF) | ((w[p + 23] & 0xFF) << 8);
                }
                if (w[p] == 'd' && w[p + 1] == 'a' && w[p + 2] == 't' && w[p + 3] == 'a') { dOff = p + 8; dLen = len > 0 && len <= w.length - dOff ? len : w.length - dOff; break; }
                if (len < 0 || p + 8 + len < p) return w;
                p += 8 + len + (len & 1);
            }
            if (fmt != 1 || dOff < 0 || align <= 0 || byteRate <= 0 || (bits != 8 && bits != 16)) return w;
            int frames = dLen / align, maxFrames = (int) ((long) rate * maxMs / 1000), f = 0;
            if (maxFrames > frames) maxFrames = frames;
            for (; f < maxFrames; f++) {
                int o = dOff + f * align, a;
                if (bits == 16) { a = (short) ((w[o] & 0xFF) | (w[o + 1] << 8)); if (a < 0) a = -a; if (a >= 328) break; }
                else { a = (w[o] & 0xFF) - 128; if (a < 0) a = -a; if (a >= 2) break; }
            }
            if (f >= maxFrames || f * 1000L / rate < 5) return w;          // im lặng < 5 ms hoặc cả đoạn đầu im lặng: giữ nguyên
            int cut = f * align, nLen = dLen - cut;
            byte[] r = new byte[44 + nLen];
            putStr(r, 0, "RIFF"); put32(r, 4, 36 + nLen); putStr(r, 8, "WAVE"); putStr(r, 12, "fmt ");
            put32(r, 16, 16); put16(r, 20, 1); put16(r, 22, ch); put32(r, 24, rate); put32(r, 28, byteRate); put16(r, 32, align); put16(r, 34, bits);
            putStr(r, 36, "data"); put32(r, 40, nLen);
            System.arraycopy(w, dOff + cut, r, 44, nLen);
            return r;
        } catch (Throwable t) { return w; }
    }


    // =====================================================================================================================  voice

    /** Tạo một Player (COLD). Nặng (J2ME Loader: file tạm + FFprobe) — chỉ ở thread SFX lúc rảnh. null nếu máy không tạo được. */
    private Inst make(int k) {
        if (mmapiBroken || failed[k]) return null;
        int type = k / SFXType.VARIANTS, variant = k % SFXType.VARIANTS;
        try {
            load(type, variant);
            Player p = Manager.createPlayer(new ByteArrayInputStream(data[k]), "audio/x-wav");
            p.realize();
            p.prefetch();
            Inst in = new Inst();
            in.p = p;
            try { in.vc = (VolumeControl) p.getControl("VolumeControl"); } catch (Throwable t) { in.vc = null; }
            created++;
            return in;
        } catch (Throwable t) {
            createFailed++; lastError = String.valueOf(t);
            if (size[k] == 0) { failed[k] = true; toneFallback = true; }
            else hardCap = totalPlayers();
            if (t instanceof Error) mmapiBroken = true;
            return null;
        }
    }

    private boolean add(int k, Inst in) {
        Inst[] a = pool[k];
        if (a == null) { a = new Inst[MAXPOOL]; pool[k] = a; }
        if (size[k] >= MAXPOOL) return false;
        a[size[k]++] = in;
        return true;
    }

    private int totalPlayers() { int n = 0; for (int i = 0; i < N; i++) n += size[i]; return n; }
    private boolean canGrow() { int cap = hardCap < totalMax ? hardCap : totalMax; return totalPlayers() < cap; }

    /** Tiếng còn kêu? busyUntil chưa tới → còn; quá đó hỏi getState() (máy có độ trễ vẫn STARTED), trừ khi đã quá STUCK_MS. */
    private static boolean sounding(Inst in, long now) {
        if (in.st != PLAYING) return false;
        if (now < in.busyUntil) return true;
        try { if (in.p.getState() != Player.STARTED) return false; } catch (Throwable t) { return false; }
        return now < in.busyUntil + STUCK_MS;
    }

    /** Đưa Player đã dừng về vị trí 0 (rẻ: không nạp gì). Player kẹt STARTED quá lâu thì stop() trước. */
    private void rewind(Inst in) {
        try { if (in.p.getState() == Player.STARTED) { in.p.stop(); unstuck++; } } catch (Throwable t) { }
        try { in.p.setMediaTime(0); } catch (Throwable t) { lastError = String.valueOf(t); }
    }

    /** Làm ấm: âm lượng 0 → start() (máy nạp/prepare) → stop() → tua 0 → trả âm lượng. Nặng — chỉ lúc rảnh. */
    private void warm(Inst in, int volume) {
        rewind(in);
        if (prime && in.vc != null) {
            try {
                in.vc.setLevel(0);
                in.p.start();
                in.p.stop();
                in.p.setMediaTime(0);
                primed++;
            } catch (Throwable t) { lastError = String.valueOf(t); in.noWarm = true; rewind(in); }
            try { in.vc.setLevel(volume); in.vol = volume; } catch (Throwable t) { in.vol = -1; }
            in.st = in.noWarm ? COLD : READY;
        } else in.st = (prime && in.vc == null) ? COLD : READY;               // prime=0: đã tua về 0 là sẵn sàng; không có VolumeControl: để COLD
    }
    private static boolean warmable(Inst in) { return prime && in.vc != null && !in.noWarm; }

    /** Hạ một Player ấm về COLD (giải phóng tài nguyên máy: deallocate). */
    private void cool(Inst in) {
        try { in.p.deallocate(); } catch (Throwable t) { lastError = String.valueOf(t); }
        try { in.p.setMediaTime(0); } catch (Throwable t) { }
        in.st = COLD; cooled++;
    }

    /** Hạng ưu tiên giữ ấm của Player thứ idx của (loại, giọng) (nhỏ = ưu tiên). Giọng vừa xuất hiện trong trận đứng trước. */
    private int rank(int k, int idx) {
        int v = k % SFXType.VARIANTS, t = k / SFXType.VARIANTS;
        int newer = 0;
        for (int w = 0; w < SFXType.VARIANTS; w++) if (voiceSeen[w] > voiceSeen[v]) newer++;
        int vr = voiceSeen[v] > 0 ? newer : 3 + v;
        // giọng (gần đây trước) > loại (HIT MISS CRIT SKILL HEAL …) ; Player thứ 2 của HIT/MISS/CRIT xếp ngay sau HEAL. Duy nhất nhờ k, idx.
        return (((vr * 100 + TYPE_RANK[t] + idx * 5) * 32 + k) * 4) + idx;
    }

    /** Số hạng (k, idx) được giữ ấm: những cặp có hạng thuộc warmMax hạng nhỏ nhất. */
    private boolean inWarmSet(int k, int idx) {
        if (idx >= want[k]) return false;
        int r = rank(k, idx), better = 0, cap = warmMax;
        for (int k2 = 0; k2 < N && better < cap; k2++) {
            if (failed[k2]) continue;
            for (int i = 0; i < want[k2]; i++) if (rank(k2, i) < r) better++;
        }
        return better < cap;
    }

    /** Ghi nhận giọng vừa được dùng (thread game gọi qua SFXManager.post — chỉ ghi số nguyên). */
    public void touch(int variant) {
        if (variant < 0 || variant >= SFXType.VARIANTS) return;
        boolean first = voiceSeen[variant] == 0;
        voiceSeen[variant] = ++seenSeq;
        if (first) kick(workVolume);                                     // giọng mới có mặt: thread nạp làm ấm tiếng của giọng này
    }

    /** SFX tắt (âm lượng 0): không tạo/làm ấm Player mới. */
    public void setLoadAllowed(boolean on) { loadAllowed = on; if (on) kick(workVolume); }

    // =====================================================================================================================  phát

    /**
     * @return 2 = Player đang STARTED; 1 = API đã gọi (tone dự phòng / chưa xác nhận); 0 = lỗi;
     * -1 = mọi Player của tiếng này đang kêu (không cắt; retryAt = lúc rảnh sớm nhất); -2 = bỏ qua có chủ đích (solo/gap).
     */
    public int play(int type, int variant, int owner, int volume) {
        synchronized (lk) { return playLocked(type, variant, owner, volume); }
    }

    private int playLocked(int type, int variant, int owner, int volume) {
        int k = key(type, variant);
        try {
            long now = System.currentTimeMillis();
            Inst[] a = pool[k];
            int n = size[k];
            boolean one = solo && isSolo(type);
            if (!one && gapMs > 0 && lastStart[k] != 0 && now - lastStart[k] < gapMs) { skipped++; return -2; }
            Inst use = null;
            int ready = 0;
            for (int i = 0; i < n; i++) {
                Inst in = a[i];
                if (in.st == WORK) continue;                                 // thread nạp đang làm ấm Player này
                if (sounding(in, now)) { if (one && owner >= 0 && in.owner == owner) { skipped++; return -2; } continue; }
                if (in.st == PLAYING) in.st = DONE;                          // vừa hết tiếng (chưa kịp dọn ở nền)
                if (in.st == READY) ready++;
                if (use == null || (in.st == READY && use.st != READY)) use = in;
            }
            if (use == null && n == 0 && !failed[k] && !mmapiBroken) {      // chưa có Player nào (trước khi nạp xong): tạo ngay, hiếm
                Inst in = make(k);
                if (in != null && add(k, in)) { use = in; grown++; }
            }
            if (use == null && n > 0) {                                      // tất cả đang kêu: không cắt, không tạo Player lúc phát
                if (want[k] < limit()) { want[k]++; kickLocked(); }
                long soon = Long.MAX_VALUE;
                for (int i = 0; i < n; i++) if (a[i].st == PLAYING && a[i].busyUntil < soon) soon = a[i].busyUntil;
                retryAt = soon;
                return -1;
            }
            if (use != null) {
                Player p = use.p;
                if (use.st != READY) { coldStarts++; if (use.st == DONE) rewind(use); }
                if (use.vol != volume && use.vc != null) { try { use.vc.setLevel(volume); } catch (Throwable ignore) { } use.vol = volume; }
                p.start();
                use.st = PLAYING; use.owner = owner;
                use.startedAt = now; use.busyUntil = now + durMs[k] + TAIL_MS;
                lastStart[k] = now;
                if (ready <= 1 && want[k] < limit() && !one) want[k]++;               // vừa dùng Player ấm cuối → lúc rảnh làm ấm thêm một
                kickLocked();                                                    // thread nạp: theo dõi Player này, làm ấm lại khi hết tiếng
                return p.getState() == Player.STARTED ? 2 : 1;
            }
            if (!mmapiBroken) {                                              // dự phòng: tone
                int f = freqFor(type, variant);
                int note = 45 + f / 20;
                if (note > 120) note = 120;
                Manager.playTone(note, DUR[type], volume);
                return 1;
            }
        } catch (Throwable t) { lastError = String.valueOf(t); }
        return 0;
    }

    /** Bản cũ (không có chủ thể): dùng cho preview và test. */
    public int play(int type, int variant, int volume) { return play(type, variant, -1, volume); }

    // =====================================================================================================================  thread nạp

    /** Khởi động thread nạp (một lần). */
    public void startWorker() {
        synchronized (lk) {
            if (worker != null) return;
            worker = new Thread(this);
            try { worker.setPriority(Thread.NORM_PRIORITY); } catch (Throwable t) { }
            worker.start();
        }
    }
    /** Báo thread nạp có việc (sau mỗi lần phát, khi bật SFX, khi đổi âm lượng). */
    public void kick(int volume) { synchronized (lk) { workVolume = volume; kickLocked(); } }
    private void kickLocked() { kicked = true; lk.notifyAll(); }

    public void run() {
        while (true) {
            try {
                long next = step(System.currentTimeMillis(), workVolume);
                if (next == 0) continue;                                     // còn việc: làm tiếp (thread phát chen vào giữa các bước)
                synchronized (lk) {
                    if (!kicked) {
                        long now = System.currentTimeMillis();
                        if (next < 0) lk.wait();
                        else if (next > now) lk.wait(next - now);
                    }
                    kicked = false;
                }
            } catch (Throwable t) {
                lastError = String.valueOf(t);
                try { Thread.sleep(20); } catch (Throwable t2) { }
            }
        }
    }

    private static final int OP_NONE = 0, OP_ASSETS = 1, OP_CREATE_WARM = 2, OP_WARM = 3, OP_COOL = 4, OP_CREATE_COLD = 5;
    private boolean reportedReady = false;

    /**
     * Một bước của thread nạp: chọn việc (giữ khoá, nhanh) rồi làm việc nặng NGOÀI khoá — tạo / làm ấm / hạ ấm đúng một Player.
     * @return 0 = còn việc, gọi lại ngay; > 0 = thời điểm (ms epoch) cần gọi lại (theo dõi Player đang kêu); -1 = không còn việc.
     */
    public long step(long now, int volume) {
        long next = -1;
        int op = OP_NONE, tk = -1;
        Inst target = null;
        synchronized (lk) {
            // 1. Player đang kêu: hết tiếng → DONE (rẻ)
            for (int k = 0; k < N; k++) {
                Inst[] a = pool[k];
                for (int i = 0; i < size[k]; i++) {
                    Inst in = a[i];
                    if (in.st != PLAYING) continue;
                    if (!sounding(in, now)) in.st = DONE;
                    else { long t = in.busyUntil > now ? in.busyUntil : now + POLL_MS; if (next < 0 || t < next) next = t; }
                }
            }
            if (!loadAllowed || mmapiBroken) return next;
            if (!assetsLoaded) op = OP_ASSETS;
            else {
                // 2. việc tốt nhất theo hạng trong tập ấm: làm ấm Player DONE/COLD, hoặc tạo Player còn thiếu
                int bestK = -1, bestI = -1, bestR = Integer.MAX_VALUE;
                for (int k = 0; k < N; k++) {
                    if (failed[k]) continue;
                    for (int i = 0; i < want[k]; i++) {
                        boolean need = i >= size[k] || pool[k][i].st == DONE || (pool[k][i].st == COLD && warmable(pool[k][i]));
                        if (!need) continue;
                        if (i >= size[k] && (i > size[k] || !canGrow())) continue;
                        int r = rank(k, i);
                        if (r < bestR && inWarmSet(k, i)) { bestR = r; bestK = k; bestI = i; }
                    }
                }
                if (bestK >= 0) {
                    tk = bestK;
                    if (bestI >= size[bestK]) op = OP_CREATE_WARM;
                    else { target = pool[bestK][bestI]; target.st = WORK; op = OP_WARM; }
                } else {
                    // 3. ngoài tập ấm: DONE → tua về 0 (rẻ, trong khoá); READY → hạ ấm (giải phóng tài nguyên máy)
                    for (int k = 0; k < N && op == OP_NONE; k++) for (int i = 0; i < size[k]; i++) {
                        Inst in = pool[k][i];
                        if (in.st == DONE) { rewind(in); in.st = COLD; continue; }
                        if (in.st == READY && !inWarmSet(k, i)) { target = in; in.st = WORK; op = OP_COOL; break; }
                    }
                    // 4. mỗi tiếng ít nhất một Player; Player thêm (want) ngoài tập ấm để COLD — vẫn phát được khi chồng tiếng
                    for (int k = 0; k < N && op == OP_NONE; k++) {
                        if (failed[k] || !canGrow()) continue;
                        if (size[k] < want[k]) { tk = k; op = OP_CREATE_COLD; }
                    }
                }
            }
        }
        // ---- việc nặng, ngoài khoá ----
        switch (op) {
            case OP_ASSETS:
                for (int k = 0; k < N; k++) load(k / SFXType.VARIANTS, k % SFXType.VARIANTS);
                synchronized (lk) { assetsLoaded = true; }
                return 0;
            case OP_CREATE_WARM: case OP_CREATE_COLD: {
                Inst in = make(tk);
                if (in != null) {
                    if (op == OP_CREATE_WARM) warm(in, volume); else { rewind(in); in.st = warmable(in) ? COLD : READY; }
                    synchronized (lk) { if (add(tk, in) && size[tk] > 1) grown++; }
                }
                return 0;
            }
            case OP_WARM: { warm(target, volume); return 0; }
            case OP_COOL: { cool(target); return 0; }
        }
        if (!reportedReady && preloadDone()) {
            reportedReady = true;
            if (SfxLog.print) System.out.println("SFXLOG BACKEND players=" + created + " warm=" + warmCount() + " primed=" + primed + " createFailed=" + createFailed + " toneFallback=" + toneFallback + " lastError=" + lastError);
        }
        return next;
    }

    /** Đã nạp xong (mọi tiếng có Player; tập ấm đã ấm). */
    public boolean preloadDone() { synchronized (lk) { return preloadDoneLocked(); } }
    private boolean preloadDoneLocked() {
        if (!assetsLoaded) return false;
        for (int k = 0; k < N; k++) {
            if (failed[k]) continue;
            if (size[k] == 0) return false;
            for (int i = 0; i < want[k]; i++) {
                if (!inWarmSet(k, i)) continue;
                if (i >= size[k]) { if (canGrow()) return false; continue; }
                int st = pool[k][i].st;
                if (st == DONE || st == WORK || (st == COLD && warmable(pool[k][i]))) return false;
            }
        }
        return true;
    }

    /** Nạp đồng bộ đến khi xong (test, không có thread nạp). */
    public void preload() { for (int i = 0; i < 1000 && step(System.currentTimeMillis(), 70) == 0; i++) { } }

    /** Số Player đang kêu — giới hạn số tiếng đồng thời. */
    public int busy(long now) {
        synchronized (lk) { return busyLocked(now); }
    }
    private int busyLocked(long now) {
        int n = 0;
        for (int k = 0; k < N; k++) for (int i = 0; i < size[k]; i++) if (sounding(pool[k][i], now)) n++;
        return n;
    }

    public void close() { synchronized (lk) { closeLocked(); } }
    private void closeLocked() {
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < size[k]; i++) { try { pool[k][i].p.close(); } catch (Throwable t) { } pool[k][i] = null; }
            size[k] = 0; failed[k] = false; want[k] = baseWant(k);
        }
        mmapiBroken = false; toneFallback = false; hardCap = Integer.MAX_VALUE;
    }

    /** Dừng mọi tiếng đang kêu (chỉ khi tạm dừng game / tắt SFX). Player về DONE → làm ấm lại lúc rảnh. */
    public void stopAll() { synchronized (lk) { stopAllLocked(); } }
    private void stopAllLocked() {
        for (int k = 0; k < N; k++) for (int i = 0; i < size[k]; i++) {
            Inst in = pool[k][i];
            try { if (in.p.getState() == Player.STARTED) { in.p.stop(); stopped++; } } catch (Throwable t) { }
            if (in.st == PLAYING) in.st = DONE;
            in.busyUntil = 0;
        }
        kickLocked();
    }

    public boolean usedFile(int type, int variant) { return fromFile[key(type, variant)]; }
    public boolean usedToneFallback() { return toneFallback; }
    public int poolSize(int type, int variant) { return size[key(type, variant)]; }
    public int players() { return totalPlayers(); }
    public int trimmedMs(int type, int variant) { return trimmed[key(type, variant)]; }
    /** Số Player đang ấm (READY hoặc đang kêu). */
    public int warmCount() { synchronized (lk) { return warmCountLocked(); } }
    private int warmCountLocked() { int n = 0; for (int k = 0; k < N; k++) for (int i = 0; i < size[k]; i++) { int s = pool[k][i].st; if (s == READY || s == PLAYING) n++; } return n; }
}

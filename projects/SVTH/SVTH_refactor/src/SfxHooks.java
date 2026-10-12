/**
 * Keo dán giữa game và SFX. Patch.java chèn lời gọi tĩnh tới các hàm này vào class gốc (bn, bx);
 * mọi giá trị game được truyền qua tham số / accessor sfx$* do Patch sinh (Java không đọc field rối tên).
 * Mỗi hook bọc try/catch(Throwable): không bao giờ làm hỏng lượt đánh. Không cấp phát trong đường nóng.
 *
 * Nguyên tắc đồng bộ: game chạy logic và vẽ trên cùng thread (cd/bn.f() trong paint, mỗi lần paint = 1 tick),
 * bx.a(boolean) tăng frame mỗi 3 tick (da.a % 3). Sự kiện nổ trên CHUYỂN frame / chuyển trạng thái, được khử trùng theo
 * (actor, frame, lượt đánh) nên cùng một frame được cập nhật nhiều tick vẫn chỉ phát một lần.
 */
public final class SfxHooks {
    static final int N = 20;
    static final int[] atkSeq = new int[N], lastBlowFrame = new int[N], blowCount = new int[N], blowExpected = new int[N];
    static final int[] skillId = new int[N], skillLevel = new int[N], beatNo = new int[N], beatExpected = new int[N], markCount = new int[N], expectedRole = new int[N];
    static final int[] dupSuppressed = new int[N];
    static int fxSeq = 0, lastMarkKey = -1;
    // session 6: pre-trigger of skill impact sounds (SFXManager.lead). preKey = marker already scheduled early for this effect;
    // frameMs = measured duration of one effect frame (3 game ticks; 48 ms at the game's 16 ms tick), EMA over consecutive frames.
    static int preKey = -1, frameMs = 48, trFr = -1;
    static long trMsAt = 0;
    static bx trBx = null;
    public static int preFired = 0;
    static e fxActor = null;
    static bn curBn = null;
    static boolean started = false;

    // ---- thống kê cho test / debug ----
    public static int blowEvents = 0, markEvents = 0, summaryOk = 0, summaryBad = 0, dupEvents = 0, hookErrors = 0;
    /** Tick đếm số lần onFrame được gọi (đo chi phí). */
    public static int frameCalls = 0;
    /** Số sự kiện đã xếp theo loại (SFXType), test dùng hiệu trước/sau từng ca. */
    public static final int[] typeCount = new int[SFXType.COUNT];
    /** Hook cho debug: nhận mỗi bản tổng kết (actor, role, skill, loại, expected, actual). null ở bản release. */
    public static SummarySink sink = null;

    public interface EndSink { void onEnd(int which); }
    /** Debug: được báo khi trận kết thúc/hết vòng. null ở bản release. */
    public static EndSink endSink = null;

    public interface SummarySink { void onSummary(int actor, int role, int skill, int kind, int expected, int actual); }

    // ---- vết trạng thái (chỉ bật trong debug) ----
    public static boolean trace = false;
    public static final int TR = 128;
    public static final int[] trSlot = new int[TR], trOld = new int[TR], trNew = new int[TR], trMs = new int[TR];
    public static int trN = 0;
    static void traceAdd(int slot, int old, int ns) {
        int i = trN % TR; trN++;
        trSlot[i] = slot; trOld[i] = old; trNew[i] = ns; trMs[i] = (int) (System.currentTimeMillis() - SfxLog.t0);
    }

    static void ensure() {
        if (!started) { started = true; AnimSfxMap.load(); }
    }

    // ---- vòng đời trận ----
    public static void battleStart(bn b) {
        try {
            ensure();
            curBn = b; fxActor = null; fxSeq = 0; lastMarkKey = -1; preKey = -1; trBx = null;
            for (int i = 0; i < N; i++) { atkSeq[i] = 0; lastBlowFrame[i] = -1; blowCount[i] = 0; blowExpected[i] = -1; skillId[i] = 0; beatNo[i] = 0; markCount[i] = 0; }
            SFXManager.init();
        } catch (Throwable t) { hookErrors++; }
    }

    /** which: 1 = thắng (bn.h), 2 = thua (bn.e), 3 = hết vòng (bn.g). */
    public static void battleEnd(int which) {
        try {
            if (endSink != null) endSink.onEnd(which);
            SFXManager.flushAll();
            fxActor = null; trBx = null;                  // session 6: không giữ sprite hiệu ứng của trận cũ
            for (int i = 0; i < N; i++) { blowExpected[i] = -1; skillId[i] = 0; }
        } catch (Throwable t) { hookErrors++; }
    }

    /** Tạm dừng/ẩn màn hình (da.hideNotify). */
    public static void paused() {
        try { SFXManager.stopAll(); } catch (Throwable t) { hookErrors++; }
    }

    // ---- chuyển trạng thái actor (e.f) ----
    public static void onState(e a, int ns, bn b) {
        try {
            curBn = b;
            int slot = a.sfx$slot();
            if (slot < 0 || slot >= N) return;
            int old = a.sfx$state();
            if (old == ns) return;
            SFXManager.touchVoice(voiceOf(a));            // session 7: giọng có mặt trong trận → bộ phát ưu tiên giữ ấm
            if (trace) traceAdd(slot, old, ns);
            if (ns == 1 || ns == 13) {
                atkSeq[slot]++;
                lastBlowFrame[slot] = -1; blowCount[slot] = 0; dupSuppressed[slot] = 0;
                blowExpected[slot] = expectedBlows(a, ns == 13);
                expectedRole[slot] = a.sfx$role();
            } else if ((old == 1 || old == 13) && blowExpected[slot] >= 0) {
                summary(slot, a.sfx$role(), 0, SFXType.HIT, blowExpected[slot], blowCount[slot]);
                blowExpected[slot] = -1;
            }
            if ((ns == 10 || ns == 17) && skillId[slot] != 0) {   // 10 = hiệu ứng lên mục tiêu địch, 17 = hiệu ứng lên bản thân/đồng minh
                beatNo[slot]++;
                fxSeq++; lastMarkKey = -1; preKey = -1; trBx = null; fxActor = a;
                char cls = AnimSfxMap.skillClass(skillId[slot]);
                int variant = voiceOf(a);
                if (cls == 'E') {
                    int t = AnimSfxMap.elemType(AnimSfxMap.skillElem(skillId[slot]));
                    if (t >= 0) fire(t, variant, slot, a.sfx$role(), skillId[slot], 0, beatNo[slot], 0);
                }
            }
            if (ns == 0 || ns == 128 || ns == 6 || ns == 2) {
                if (skillId[slot] != 0 && (old == 10 || old == 5 || old == 17) && (ns == 0 || ns == 128)) {
                    summary(slot, a.sfx$role(), skillId[slot], SFXType.SKILL, beatExpected[slot], markCount[slot]);
                    skillId[slot] = 0;
                    if (fxActor == a) fxActor = null;
                }
                if (ns == 0 || ns == 128) SFXManager.flushActor(slot);
            }
        } catch (Throwable t) { hookErrors++; }
    }

    /** Số nhịp ENGINE nổ cho một đòn thường: các frame trong e.c[] thỏa c.length < f <= (số frame - 2), cộng 1 nhịp cuối đòn nếu là trạng thái 1 (xem bên dưới). */
    static int expectedBlows(e a, boolean st13) {
        bx x = a.sfx$bx();
        byte[] c = a.sfx$hitFrames();
        if (x == null || c == null) return 1;
        int n = x.sfx$frames(3);
        if (n - 1 <= c.length) return 1;     // anim quá ngắn: chỉ nhịp ở frame cuối
        int cnt = 0;
        for (int k = 0; k < c.length; k++) {
            int f = c[k] & 0xFF;
            if (f <= c.length || f > n - 2) continue;
            boolean dup = false;
            for (int j = 0; j < k; j++) if ((c[j] & 0xFF) == f) { dup = true; break; }
            if (!dup) cnt++;
        }
        // Trạng thái 1 (nhân vật): bn.f() đặt lại anim về frame 0 RỒI mới so c() <= c.length → thêm 1 nhịp cuối đòn.
        // Trạng thái 13 (quái/sủng vật): so c() <= c.length TRƯỚC khi đặt lại (c() = frame cuối > c.length) → KHÔNG có nhịp cuối đòn.
        return st13 ? cnt : cnt + 1;
    }

    // ---- nhịp đánh thường: e.g := true ----
    public static void onBlow(e a, boolean v, bn b) {
        if (!v) return;
        try {
            curBn = b;
            int slot = a.sfx$slot();
            if (slot < 0 || slot >= N) return;
            bx x = a.sfx$bx();
            int frame = x != null ? x.sfx$frame() : -1;
            if (frame == lastBlowFrame[slot]) {          // cùng frame, tick tiếp theo → không phát lại
                dupSuppressed[slot]++; dupEvents++; SfxLog.count[SfxLog.ST_DUP]++;
                return;
            }
            lastBlowFrame[slot] = frame;
            blowCount[slot]++; blowEvents++;
            e tg = b != null ? b.sfx$actor(a.sfx$target()) : null;
            boolean miss = tg != null && tg.sfx$dodge();
            fireImpact(slot, voiceOf(a), a.sfx$role(), 0, frame, blowCount[slot], miss, a.sfx$crit(), 0);
        } catch (Throwable t) { hookErrors++; }
    }

    // ---- áp kỹ năng bn.a(e,int,int) ----
    public static void onSkillApply(bn b, e a, int id, int level) {
        try {
            ensure();
            curBn = b;
            int slot = a.sfx$slot();
            if (slot < 0 || slot >= N) return;
            char cls = AnimSfxMap.skillClass(id);
            skillId[slot] = (cls == 'X' || cls == 0) ? 0 : id;
            skillLevel[slot] = level; beatNo[slot] = 0; markCount[slot] = 0;
            beatExpected[slot] = AnimSfxMap.beats(id, level);
            if (cls == 'X' || cls == 0) return;
            fire(SFXType.SKILL, voiceOf(a), slot, a.sfx$role(), id, 0, 0, 0);
        } catch (Throwable t) { hookErrors++; }
    }

    // ---- frame hiệu ứng đã tiến (bx.a(boolean) trả về) ----
    public static void onFrame(bx x) {
        try {
            frameCalls++;
            e act = fxActor;
            if (act == null) return;
            int fr = x.sfx$frame();
            if (fr == x.sfx$last()) return;
            x.sfx$setLast(fr);
            String nm = x.sfx$name();
            if (nm == null) return;
            int ast = act.sfx$state();
            if (ast != 10 && ast != 17) return;
            int slot = act.sfx$slot();
            if (slot < 0 || slot >= N || skillId[slot] == 0) return;
            if (!AnimSfxMap.isMarker(nm, fr)) {
                if (SFXManager.lead > 0) preFire(x, act, slot, nm, fr);
                return;
            }
            int key = fxSeq * 64 + fr;
            if (key == lastMarkKey) return;               // AoE: nhiều bx cùng marker → một sự kiện/nhịp
            lastMarkKey = key;
            markCount[slot]++; markEvents++;
            if (key != preKey) fireMark(act, slot, fr, 0); // session 6: key == preKey → tiếng của nhịp này đã đặt sớm (bù độ trễ)
            if (SFXManager.lead > 0) preFire(x, act, slot, nm, fr);   // nhịp kế tiếp có thể chỉ cách 1 frame
        } catch (Throwable t) { hookErrors++; }
    }

    /**
     * session 6: đặt tiếng va chạm sớm `lead` ms. Frame va chạm kế tiếp m đã biết (map.txt), kết quả trúng/né/chí mạng đã tính lúc tung kỹ năng
     * (bn.a(e,int,int) đặt e.o; e.m đặt đầu lượt), nên phát trước vài chục ms không đổi tiếng nào được chọn. Chỉ đặt khi thời điểm phát rơi trước
     * lần đổi frame kế tiếp; khi frame m tới thì không phát lại (preKey). Hủy animation trước m → flushActor xoá tiếng chưa tới hạn.
     */
    static void preFire(bx x, e act, int slot, String nm, int fr) {
        long now = System.currentTimeMillis();
        if (x == trBx && fr == trFr + 1) {                // đo độ dài một frame hiệu ứng trên chính bx này
            int d = (int) (now - trMsAt);
            if (d >= 15 && d <= 400) frameMs = (frameMs * 3 + d) / 4;
        }
        if (trBx == null || x == trBx) { trBx = x; trFr = fr; trMsAt = now; }
        int m = AnimSfxMap.nextMarker(nm, fr);
        if (m < 0) return;
        int key = fxSeq * 64 + m;
        if (key == preKey || key == lastMarkKey) return;
        int fm = frameMs, until = (m - fr) * fm;     // ms tới lúc frame m hiện
        if (until - SFXManager.lead >= fm) return;   // còn xa: để lần đổi frame sau đặt cho chính xác
        int delay = until - SFXManager.lead;
        preKey = key; preFired++;
        fireMark(act, slot, m, delay < 0 ? 0 : delay);
    }

    /** Tiếng của một nhịp va chạm kỹ năng (trước đây nằm trong onFrame). delay > 0: phát sau delay ms. */
    static void fireMark(e act, int slot, int fr, int delay) {
        int id = skillId[slot];
        char cls = AnimSfxMap.skillClass(id);
        int variant = voiceOf(act);
        int role = act.sfx$role();
        if (cls == 'H') { fire(SFXType.HEAL, variant, slot, role, id, fr, beatNo[slot], delay); return; }
        if (cls == 'B') {
            // session 7: the cast already played SKILL for this action; replaying the same file at the landing frame was heard as
            // "SKILL kêu lại" (a 1 s sound twice). Landing is silent unless every target resisted (MISS).
            if (!anyTargetOk(act)) fire(SFXType.MISS, variant, slot, role, id, fr, beatNo[slot], delay);
            return;
        }
        boolean miss = !anyTargetOk(act);
        fireImpact(slot, variant, role, id, fr, beatNo[slot], miss, act.sfx$crit(), delay);
        if (cls == 'D' && !miss) fire(SFXType.HEAL, variant, slot, role, id, fr, beatNo[slot], SFXManager.healDelay + delay);
    }

    /**
     * session 7: giọng SFX của một actor. Chỉ NHÂN VẬT CHÍNH (slot 0–9 và partyUid == 0) mới có giọng riêng theo roleId (Quinn 1, Julie 2…).
     * Sủng vật có roleId = petId (GameData.createPetSprite) và quái tra cùng bảng pet, nên roleId 1/2/3/39/40 của chúng trùng số với nhân vật:
     * trước đây pet có petId 1 kêu đúng SKILL_1/HIT_1 của Quinn ("pet hành động → skill 1 kêu lại"). Pet luôn có petUid ≥ 1.
     */
    public static int voiceOf(e a) {
        int slot = a.sfx$slot();
        if (slot < 0 || slot >= 10 || a.sfx$partyUid() != 0) return 0;
        return AnimSfxMap.variantOf(a.sfx$role());
    }

    static boolean anyTargetOk(e act) {
        byte[] tg = act.sfx$targets();
        if (tg == null || curBn == null) return true;
        boolean any = false, checked = false;
        for (int i = 0; i < tg.length; i++) {
            e t = curBn.sfx$actor(tg[i]);
            if (t == null) continue;
            checked = true;
            if (!t.sfx$dodge()) any = true;
        }
        return !checked || any;
    }

    // ---- phát ----
    static void fireImpact(int slot, int variant, int role, int skill, int frame, int beat, boolean miss, boolean crit, int delay) {
        if (miss) { fire(SFXType.MISS, variant, slot, role, skill, frame, beat, delay); return; }
        fire(SFXType.HIT, variant, slot, role, skill, frame, beat, delay);
        if (crit) fire(SFXType.CRIT, variant, slot, role, skill, frame, beat, SFXManager.critDelay + delay);   // lớp chồng: mặc định phát đồng thời với HIT
    }

    static void fire(int type, int variant, int slot, int role, int skill, int frame, int beat, int delay) {
        typeCount[type]++;
        int lg = SfxLog.add(SfxLog.REC_ACTION, slot, role, skill, type, frame, beat, variant, 0, SfxLog.ST_QUEUED);   // session 7: cột expect của bản ghi ACTION = giọng
        SFXManager.post(type, variant, slot, delay, lg);
    }

    static void summary(int slot, int role, int skill, int kind, int expected, int actual) {
        if (expected == actual) summaryOk++; else summaryBad++;
        SfxLog.add(SfxLog.REC_SUMMARY, slot, role, skill, kind, 0, 0, expected, actual, 0);
        SummarySink s = sink;
        if (s != null) s.onSummary(slot, role, skill, kind, expected, actual);
    }

    private SfxHooks() {}
}

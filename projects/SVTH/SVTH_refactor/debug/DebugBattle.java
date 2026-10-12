import java.io.InputStream;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * SFX Test Arena (CHỈ có trong bản debug). Dựng trận bằng chính hệ thống chiến đấu thật:
 * Main.newGame() → cd.dbg$join(Julie) → cd.b(String[]) (đường lệnh script "战斗"), 3 mục tiêu giả, hai bên bất tử (kẹp HP ≥ 1),
 * lệnh chiêu được đưa vào ma trận kế hoạch lượt bn.e[][] như người chơi chọn rồi chạy pha lượt thật.
 * Mọi hook gọi từ patch (DebugPatch) cùng thread game; không dùng lock.
 */
public final class DebugBattle {
    // ---- chế độ ----
    static final int OFF = 0, STARTING = 1, WAITBATTLE = 2, MENU = 3, RUNNING = 4, LOGVIEW = 5;
    static volatile int mode = OFF;
    static volatile boolean injecting = false;   // đang tự bơm phím vào game → onKey không chặn
    static boolean autoRun = false, autoDone = false;
    // ---- cờ debug (reset() trả về mặc định) ----
    static boolean invincible = true, passive = true;
    static int forceHit = -1;                    // hiệu lực trong ca đang chạy: -1 ngẫu nhiên, 0 luôn trượt, 1 luôn trúng
    static int forceHitMenu = -1;                // lựa chọn trong menu cho ca chạy tay
    // ---- bảng kỹ năng thật (đọc /bin/skill.txt) ----
    static final int NS = 40;
    static final String[] skName = new String[NS];
    static final int[] skSide = new int[NS], skCount = new int[NS];
    static boolean skillsLoaded = false;
    // ---- ca thử ----
    static final int MAXC = 256;
    static final int K_NORMAL = 0, K_MISS = 1, K_CRIT = 2, K_SKILL = 3, K_SKILL_MISS = 4, K_INJECT = 5, K_OFF = 6, K_ENEMY = 7;   // INJECT: SFX ném lỗi trong thread; OFF: SFX tắt — trận vẫn phải chạy y hệt
    static final int[] cHero = new int[MAXC], cKind = new int[MAXC], cSkill = new int[MAXC], cLevel = new int[MAXC];
    static int nCases = 0, curCase = -1;
    static int passCount = 0, failCount = 0, skipCount = 0;
    // ---- trạng thái chạy ca ----
    static final int RS_IDLE = 0, RS_START = 1, RS_DONE = 2;
    static int runState = RS_IDLE;
    static long caseStart = 0, quietSince = 0, readySince = 0;
    static int menuPhase = -1, lastPhase = -2, lastReadyPhase = -3;
    static final int[] snap = new int[SFXType.COUNT];
    static int snapErr, snapDup;
    static final int MAXS = 16;
    static final int[] sActor = new int[MAXS], sKind = new int[MAXS], sExp = new int[MAXS], sAct = new int[MAXS];
    static int nSum = 0;
    static bn curBn = null;
    // ---- menu ----
    static int row = 0, hero = 0, caseSel = 0, level = 1;
    static final int ROWS = 12;
    static String[] lines = new String[ROWS + 8];
    static int nLines = 0;
    static boolean dirty = true;
    static String status = "";
    static Font font = null;
    static final String[] HERO = {"Quinn (thuong)", "Julie (sung)"};

    // ====================================================================================== vào / ra
    public static void enter() {
        if (mode != OFF) return;
        resetFlags();
        SfxHooks.trace = true;
        mode = STARTING;
        status = "dang dung tran that...";
        dirty = true;
        new Thread(new Starter()).start();
    }
    /** Cho harness / chạy tự động. */
    public static void startAuto() { autoRun = true; autoDone = false; enter(); }
    public static boolean autoDone() { return autoDone; }
    public static int mode() { return mode; }

    static final class Starter implements Runnable {
        public void run() {
            try {
                loadSkills();
                SfxLog.reset();
                SfxHooks.endSink = new EndWatch();
                Main.self.newGame();
                long end = System.currentTimeMillis() + 40000;
                cd g = null;
                while (System.currentTimeMillis() < end) {
                    g = cd.dbg$game();
                    if (g != null && cd.dbg$state() == 4) break;
                    Thread.sleep(200);
                }
                Thread.sleep(2500);
                injecting = true;
                try { ((da) Main.disp.getCurrent()).keyPressed(-7); } finally { injecting = false; }   // bỏ qua màn prologue
                Thread.sleep(1500);
                g = cd.dbg$game();
                cd.dbg$join(2);                                   // Julie vào đội
                allowStart = true;
                g.dbg$battle(new String[]{"133|5|1", "133|5|1", "133|5|1"});
                mode = WAITBATTLE;
                new Thread(new Watchdog(g)).start();
                status = "cho tran bat dau...";
                dirty = true;
            } catch (Throwable t) {
                System.out.println("SFXTEST ERROR start " + t);
                mode = OFF;
            }
        }
    }

    /** Trả MỌI cờ debug về mặc định và rời trận: không để rò trạng thái sang game thường. */
    static final boolean[] skillsGiven = new boolean[2];
    static void restoreSkills() {
        try {
            bn b = curBn;
            for (int i = 0; i < 2; i++) { if (skillsGiven[i] && b != null) { e a = b.sfx$actor(i); if (a != null) a.dbg$takeSkills(); } skillsGiven[i] = false; }
        } catch (Throwable t) { }
    }
    public static void exit() {
        try {
            restoreSkills();
            SFXManager.stopAll();
            resetFlags();
            mode = OFF; runState = RS_IDLE; autoRun = false;
            System.out.println("SFXTEST EXIT flagsClean=" + !dirtyFlags());
            // Ván debug đã ghi đè trạng thái tĩnh của game (đội hình, sủng vật…): chỉ có thoát hẳn ứng dụng (đường destroyApp của chính game) mới bảo đảm ván mới sau đó sạch. Mở lại game để chơi.
            if (Main.self != null) Main.self.destroyApp(true);
        } catch (Throwable t) { System.out.println("SFXTEST ERROR exit " + t); mode = OFF; }
    }
    static void resetFlags() {
        SfxHooks.endSink = null; SfxHooks.trace = false;
        invincible = true; passive = true; forceHit = -1; forceHitMenu = -1; injecting = false;
        SfxHooks.sink = null;
        SFXManager.injectFailure = false;
        SFXManager.setEnabled(true);
    }
    /** true nếu còn cờ debug khác mặc định sau khi thoát (test dùng). */
    public static boolean dirtyFlags() {
        return SfxHooks.endSink != null || mode != OFF || forceHit != -1 || forceHitMenu != -1 || !invincible || SfxHooks.sink != null || SFXManager.injectFailure || !SFXManager.enabled;
    }

    /** Theo dõi: nếu trận biến mất (không còn tick) trong lúc chạy ca → in vết trạng thái để chẩn đoán, rồi dựng lại trận. */
    static final class Watchdog implements Runnable {
        final cd game;
        Watchdog(cd g) { game = g; }
        public void run() {
            long lastSeen = System.currentTimeMillis();
            while (mode != OFF) {
                try { Thread.sleep(1000); } catch (Throwable t) { }
                if (mode == RUNNING || mode == WAITBATTLE) {
                    if (System.currentTimeMillis() - lastTick > 6000 && lastTick != 0) {
                        System.out.println("SFXTEST WATCHDOG: khong con tick " + (System.currentTimeMillis() - lastTick) + "ms; bn=" + (game.dbg$bn() != null) + " curCase=" + (curCase + 1) + " " + enemyDump());
                        dumpTrace();
                        mode = OFF;
                        System.out.println("SFXTEST RESULT pass=" + passCount + " fail=" + (failCount + 1) + " ABORTED (tran bi ket thuc ngoai y muon)");
                        autoDone = true;
                    }
                }
            }
        }
    }
    static void dumpTrace() {
        int n = SfxHooks.trN < SfxHooks.TR ? SfxHooks.trN : SfxHooks.TR;
        for (int k = n; k >= 1; k--) {
            if (k > 40) continue;
            int i = (SfxHooks.trN - k) % SfxHooks.TR;
            System.out.println("SFXTEST TRACE t=" + SfxHooks.trMs[i] + " slot=" + SfxHooks.trSlot[i] + " " + SfxHooks.trOld[i] + "->" + SfxHooks.trNew[i]);
        }
    }
    static long lastTick = 0, lastPress = 0;
    static int caseActor = 0, snapSeq = 0;
    static int snapFail, snapOff, snapApi;

    static final class EndWatch implements SfxHooks.EndSink {
        public void onEnd(int which) {
            if (which == 3) return;           // hết vòng: bình thường
            battleOver = which;
            System.out.println("SFXTEST BATTLE-END which=" + which + " (1=thang 2=thua) curCase=" + (curCase + 1) + " runState=" + runState + " " + enemyDump());
        }
    }
    static volatile int battleOver = 0;
    static String enemyDump() {
        bn b = curBn;
        if (b == null) return "";
        StringBuffer s = new StringBuffer("HP[");
        for (int i = 0; i < 20; i++) { e a = b.sfx$actor(i); if (a != null) s.append(i).append(':').append(a.sfx$hp()).append('/').append(a.sfx$hpReal()).append(a.sfx$visible() ? "" : "v0").append(a.sfx$state() == 128 ? "dead" : "").append(' '); }
        return s.append(']').toString();
    }

    static void loadSkills() {
        if (skillsLoaded) return;
        skillsLoaded = true;
        try {
            InputStream in = new DebugBattle().getClass().getResourceAsStream("/bin/skill.txt");
            StringBuffer sb = new StringBuffer();
            int c;
            byte[] buf = new byte[4096];
            java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
            int n;
            while ((n = in.read(buf)) > 0) bo.write(buf, 0, n);
            in.close();
            String s = new String(bo.toByteArray(), "UTF-8");
            int p = 0;
            while (p < s.length()) {
                int e = s.indexOf('\n', p);
                if (e < 0) e = s.length();
                String line = s.substring(p, e);
                p = e + 1;
                String[] t = split(line, '\t');
                if (t.length > 5 && t[0].length() > 0 && Character.isDigit(t[0].charAt(0))) {
                    int id = Integer.parseInt(t[0].trim());
                    if (id > 0 && id < NS) { skName[id] = t[1].trim(); skSide[id] = Integer.parseInt(t[4].trim()); skCount[id] = Integer.parseInt(t[5].trim()); }
                }
            }
        } catch (Throwable t) { System.out.println("SFXTEST ERROR loadSkills " + t); }
    }
    static String[] split(String s, char sep) {
        int n = 1;
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == sep) n++;
        String[] r = new String[n];
        int k = 0, st = 0;
        for (int i = 0; i <= s.length(); i++) if (i == s.length() || s.charAt(i) == sep) { r[k++] = s.substring(st, i); st = i + 1; }
        return r;
    }

    // ====================================================================================== hook từ game
    /** da.keyPressed: true = đã xử lý. */
    public static boolean onKey(int key) {
        try {
            if (injecting) return false;
            if (mode == OFF) {
                if (cd.dbg$state() == 3 && key == 35) { enter(); return true; }   // '#': trên màn cover/menu chính
                return false;
            }
            if (mode == STARTING || mode == WAITBATTLE) return true;
            if (mode == RUNNING) {
                if (key == 48 || key == -7) { autoRun = false; status = "da huy (phim 0/SR)"; dirty = true; }   // 0 hoặc SR: dừng auto
                return true;
            }
            if (mode == LOGVIEW) { mode = MENU; dirty = true; return true; }
            handleMenuKey(key);
            return true;
        } catch (Throwable t) { return false; }
    }

    static void handleMenuKey(int key) {
        dirty = true;
        if (key == -1) { row = (row + ROWS - 1) % ROWS; return; }
        if (key == -2) { row = (row + 1) % ROWS; return; }
        int d = key == -4 ? 1 : (key == -3 ? -1 : 0);
        if (d != 0) {
            switch (row) {
                case 0: hero = (hero + 1) & 1; break;
                case 1: caseSel = (caseSel + d + totalChoices()) % totalChoices(); break;
                case 2: level = level + d; if (level < 1) level = 10; if (level > 10) level = 1; break;
                case 7: invincible = !invincible; break;
                case 8: forceHitMenu = forceHitMenu + d; if (forceHitMenu < -1) forceHitMenu = 1; if (forceHitMenu > 1) forceHitMenu = -1; break;
                case 9: SFXManager.setEnabled(!SFXManager.enabled); break;
                case 10: SFXManager.setVolume(SFXManager.volume + 10 * d); break;
            }
            return;
        }
        if (key == 35) { invincible = !invincible; return; }
        if (key == -7 || key == 48) { exit(); return; }
        if (key == -5 || key == -6) {
            switch (row) {
                case 3: runSelected(); break;                                       // chạy / chạy lại
                case 4: caseSel = (caseSel + 1) % totalChoices(); runSelected(); break;   // chiêu kế
                case 5: autoRun = true; autoDone = false; break;                    // auto-test
                case 6: mode = LOGVIEW; break;
                case 9: SFXManager.setEnabled(!SFXManager.enabled); break;
                case 11: exit(); break;
                default: break;
            }
        }
    }

    /** Lựa chọn trong menu: 0 đánh thường, 1 trượt, 2 chí mạng, rồi mọi kỹ năng 1..39. */
    static int totalChoices() { return 3 + (NS - 1); }
    static String choiceName(int c) {
        if (c == 0) return "Danh thuong";
        if (c == 1) return "Danh thuong (ep TRUOT)";
        if (c == 2) return "Danh thuong (ep CHI MANG)";
        int id = c - 2;
        return id + " " + (skName[id] != null ? ascii(skName[id]) : "?") + (castable(id) ? "" : " [bi dong]");
    }
    static String ascii(String s) {   // font mặc định máy không có dấu tiếng Việt → bỏ dấu cho overlay
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < s.length(); i++) { char c = s.charAt(i); b.append(c < 128 ? c : '?'); }
        return b.toString();
    }
    static boolean castable(int id) { char c = AnimSfxMap.skillClass(id); return c != 0 && c != 'X'; }

    static void runSelected() {
        nCases = 0; curCase = -1;
        int c = caseSel;
        if (c == 0) add(hero, K_NORMAL, 0, 1); else if (c == 1) add(hero, K_MISS, 0, 1); else if (c == 2) add(hero, K_CRIT, 0, 1);
        else {
            int id = c - 2;
            if (!castable(id)) { status = "skill " + id + " la bi dong/khong co hieu ung: khong tung duoc"; return; }
            add(hero, K_SKILL, id, level);
        }
        mode = RUNNING; autoRun = false;
        startCase(curBn, 0);
    }
    static void add(int h, int kind, int skill, int lv) {
        if (nCases >= MAXC) return;
        cHero[nCases] = h; cKind[nCases] = kind; cSkill[nCases] = skill; cLevel[nCases] = lv; nCases++;
    }

    static volatile boolean allowStart = false;
    /** Trận do script cốt truyện bị chặn khi đang debug; chỉ cho phép trận do chính DebugBattle bắt đầu. */
    public static boolean blockStart() {
        if (mode == OFF) return false;
        if (allowStart) { allowStart = false; return false; }
        System.out.println("SFXTEST BLOCKED trận do script bắt đầu");
        return true;
    }

    /** Đóng băng script cốt truyện trong suốt phiên debug. */
    public static boolean blockScript() { return mode != OFF; }

    /** Chẩn đoán: in vết gọi khi game dọn trận. */
    public static void trap(String what) {
        if (mode == OFF) return;
        System.out.println("SFXTEST TRAP " + what + " curCase=" + (curCase + 1) + " runState=" + runState + " phase=" + lastPhase);
        new Throwable("trap " + what).printStackTrace();
    }

    // ---- hitRoll / clamp / phase ----
    public static boolean hitRoll(boolean v) {
        if (mode == OFF) return v;
        if (forceHit == 1) return true;
        if (forceHit == 0) return false;
        return v;
    }
    public static void clamp(bn b) {
        if (mode == OFF || !invincible) return;
        int n = b.sfx$count();
        for (int i = 0; i < n; i++) {
            e a = b.sfx$actor(i);
            if (a == null) continue;
            if (a.sfx$hp() < 1) a.dbg$setHp(1);
            if (a.sfx$hpReal() < 1) a.dbg$setHpReal(1);
        }
    }
    public static void onPhase(bn b, int ns) {
        try {
            if (mode == OFF) return;
            int old = b.dbg$phase();
            if (passive && old == 2 && (ns == 3 || ns == 6)) {            // pha kế hoạch vừa xong: mục tiêu giả đứng yên
                for (int i = 10; i < 20; i++) if (b.sfx$actor(i) != null) b.dbg$clearPlan(i);
            }
        } catch (Throwable t) { }
    }

    // ====================================================================================== tick
    public static void tick(bn b) {
        try {
            if (mode == OFF) return;
            curBn = b;
            long now = System.currentTimeMillis();
            lastTick = now;
            int ph = b.dbg$phase();
            if (ph != lastPhase) { SfxHooks.traceAdd(-1, lastPhase, ph); System.out.println("SFXTEST PHASE " + lastPhase + "->" + ph + " mode=" + mode + " run=" + runState); lastPhase = ph; }
            if (mode == WAITBATTLE) {
                if (isIdle(b) && b.sfx$actor(10) != null) {
                    if (ph != lastReadyPhase) { lastReadyPhase = ph; readySince = now; }
                    else if (now - readySince > 1500) { menuPhase = ph; mode = MENU; status = "san sang. OK=chay"; dirty = true; System.out.println("SFXTEST READY menuPhase=" + menuPhase); if (autoRun) buildAuto(); }
                } else lastReadyPhase = -3;
                return;
            }
            if (mode == MENU && autoRun && runState == RS_IDLE) { if (nCases == 0) buildAuto(); mode = RUNNING; nextAuto(b); return; }
            if (runState == RS_START) {
                if (ph != menuPhase || !isIdle(b)) { runState = RS_DONE; quietSince = 0; }
                else if (now - caseStart > 4000) { finish(b, "KHONG BAT DAU (4s)", false); }
            } else if (runState == RS_DONE) {
                if (ph == menuPhase && isIdle(b) && SFXManager.pending() == 0) {
                    if (quietSince == 0) quietSince = now; else if (now - quietSince > 700) evaluate(b);
                } else quietSince = 0;
                // màn thông tin của game (vd. kỹ năng 39 Trinh sát mở bảng chỉ số, pha 5) chờ phím OK: bấm giúp để ca tiếp tục
                if (ph != menuPhase && ph == 5 && isIdle(b) && now - caseStart > 3000 && now - lastPress > 1500) {
                    lastPress = now; injecting = true;
                    try { ((da) Main.disp.getCurrent()).keyPressed(-5); } finally { injecting = false; }
                    System.out.println("SFXTEST NOTE phim OK bam giup (man thong tin cua game, pha " + ph + ")");
                }
                if (now - caseStart > 60000) finish(b, "TIMEOUT 60s", false);
            }
        } catch (Throwable t) { System.out.println("SFXTEST ERROR tick " + t); }
    }

    static boolean isIdle(bn b) {
        int n = b.sfx$count();
        for (int i = 0; i < n; i++) {
            e a = b.sfx$actor(i);
            if (a == null) continue;
            int st = a.sfx$state();
            if (st != 0 && st != 128 && st != 3) return false;
        }
        return true;
    }

    // ====================================================================================== ca thử
    static void startCase(bn b, int idx) {
        curCase = idx;
        int h = cHero[idx], kind = cKind[idx], id = cSkill[idx], lv = cLevel[idx];
        e he = b.sfx$actor(h);
        if (he == null) { finish(b, "khong co nhan vat " + h, false); return; }
        he.dbg$mpFull();
        if ((kind == K_SKILL || kind == K_SKILL_MISS) && he.dbg$noSkills()) { he.dbg$giveSkills(); skillsGiven[h] = true; }   // nhân vật chưa học kỹ năng nào: bảng rỗng để mã EXP kỹ năng của game không NPE
        int other = 1 - h;
        if (b.sfx$actor(other) != null) b.dbg$clearPlan(other);
        int enemy = 10;
        for (int i = 10; i < 20; i++) { e a = b.sfx$actor(i); if (a != null && !a.sfx$dead()) { enemy = i; break; } }
        System.arraycopy(SfxHooks.typeCount, 0, snap, 0, SFXType.COUNT);
        snapErr = SfxHooks.hookErrors; snapDup = SfxHooks.dupEvents; nSum = 0;
        SfxHooks.sink = new Sink();
        he.dbg$setCrit(kind == K_CRIT);
        SFXManager.injectFailure = kind == K_INJECT;
        SFXManager.setEnabled(kind != K_OFF);
        snapFail = SfxLog.count[SfxLog.ST_FAILED]; snapOff = SfxLog.count[SfxLog.ST_OFF]; snapApi = SFXManager.apiCalls + SFXManager.played;
        if (kind == K_MISS || kind == K_SKILL_MISS) forceHit = 0; else if (autoRun) forceHit = 1; else forceHit = forceHitMenu;
        if (kind == K_NORMAL || kind == K_MISS || kind == K_CRIT || kind == K_INJECT || kind == K_OFF) {
            b.dbg$plan(h, 1, enemy, -1, -1);
        } else if (kind == K_ENEMY) {
            passive = false;                              // ca này cần quái ra đòn thật (trạng thái 13): giữ kế hoạch của quái
            b.dbg$clearPlan(h);
            for (int i = 10; i < 20; i++) if (i != enemy && b.sfx$actor(i) != null) b.dbg$clearPlan(i);   // chỉ MỘT quái ra đòn → đếm sự kiện đúng
            b.dbg$plan(enemy, 1, h, -1, -1);
        } else {
            int side = id < NS ? skSide[id] : 2;
            int target = (side == 2) ? enemy : h;
            b.dbg$plan(h, 4, target, id, lv);
        }
        caseActor = kind == K_ENEMY ? enemy : h;
        snapSeq = SfxLog.seqNo;
        { StringBuffer ci = new StringBuffer("SFXTEST CASEINFO h=" + h + " enemy=" + enemy + " actors:");
          for (int i = 0; i < 20; i++) { e a = b.sfx$actor(i); if (a != null) ci.append(' ').append(i).append(":r").append(a.sfx$role()).append(a.sfx$dead() ? "D" : "L").append(":s").append(a.sfx$state()).append(":u").append(a.sfx$partyUid()).append(":v").append(SfxHooks.voiceOf(a)); }
          System.out.println(ci.toString()); }
        caseStart = System.currentTimeMillis(); quietSince = 0;
        runState = RS_START;
        status = "chay ca " + (idx + 1) + "/" + nCases;
        dirty = true;
        b.dbg$go();
    }

    static final class Sink implements SfxHooks.SummarySink {
        public void onSummary(int actor, int role, int skill, int kind, int expected, int actual) {
            if (nSum < MAXS) { sActor[nSum] = actor; sKind[nSum] = kind; sExp[nSum] = expected; sAct[nSum] = actual; nSum++; }
        }
    }

    static void evaluate(bn b) {
        int idx = curCase, h = cHero[idx], kind = cKind[idx], id = cSkill[idx], lv = cLevel[idx];
        int[] d = new int[SFXType.COUNT];
        for (int i = 0; i < d.length; i++) d[i] = SfxHooks.typeCount[i] - snap[i];
        if (kind == K_ENEMY) {   // quái khác có thể ra đòn cùng lượt: chỉ đếm sự kiện của đúng một quái (theo nhật ký)
            for (int i = 0; i < d.length; i++) d[i] = 0;
            for (int k = 0; k < SfxLog.N; k++) if (SfxLog.kind[k] == SfxLog.REC_ACTION && SfxLog.seq[k] > snapSeq && SfxLog.actor[k] == caseActor) d[SfxLog.type[k]]++;
        }
        boolean ok = true;
        StringBuffer why = new StringBuffer();
        int exp = -1, act = -1;
        for (int i = 0; i < nSum; i++) if (sActor[i] == caseActor) { exp = sExp[i]; act = sAct[i]; }
        if (exp < 0) { ok = false; why.append(" khong co SUMMARY"); }
        else if (exp != act) { ok = false; why.append(" nhip ky vong ").append(exp).append(" thuc te ").append(act); }
        if (SfxHooks.hookErrors != snapErr) { ok = false; why.append(" hookErrors+").append(SfxHooks.hookErrors - snapErr); }
        int n = exp < 0 ? 0 : exp;
        if (kind == K_ENEMY) {
            // AI của quái tự chọn lệnh ở lượt của nó: có thể ra đòn thường (trạng thái 13) hoặc kỹ năng. Đòn thường: HIT+MISS == nhịp, không SKILL; kỹ năng: chỉ cần kỳ vọng == thực tế
            if (d[SFXType.SKILL] == 0) ok &= chk(why, "HIT+MISS", d[SFXType.HIT] + d[SFXType.MISS], n);
            if (n < 1) { ok = false; why.append(" quai khong ra don"); }
        } else
        if (kind == K_NORMAL || kind == K_MISS || kind == K_CRIT || kind == K_INJECT || kind == K_OFF) {
            int hit = kind == K_MISS ? 0 : n, miss = kind == K_MISS ? n : 0, crit = kind == K_CRIT ? n : 0;
            ok &= chk(why, "HIT", d[SFXType.HIT], hit); ok &= chk(why, "MISS", d[SFXType.MISS], miss);
            if (kind == K_INJECT) { int f = SfxLog.count[SfxLog.ST_FAILED] - snapFail; if (f < 1) { ok = false; why.append(" khong co FAILED (loi chua duoc tiem?)"); } }
            if (kind == K_OFF) { int o = SfxLog.count[SfxLog.ST_OFF] - snapOff; if (o < n) { ok = false; why.append(" OFF ").append(o).append("<").append(n); } if (SFXManager.apiCalls + SFXManager.played != snapApi) { ok = false; why.append(" co phat khi dang TAT"); } }
            if (kind == K_NORMAL || kind == K_INJECT || kind == K_OFF) { if (d[SFXType.CRIT] > n) { ok = false; why.append(" CRIT ").append(d[SFXType.CRIT]).append(">").append(n); } }   // chí mạng tự nhiên được phép
            else ok &= chk(why, "CRIT", d[SFXType.CRIT], crit);
            ok &= chk(why, "SKILL", d[SFXType.SKILL], 0);
        } else if (kind == K_SKILL || kind == K_SKILL_MISS) {
            boolean miss = kind == K_SKILL_MISS;
            char cls = AnimSfxMap.skillClass(id);
            int el = AnimSfxMap.elemType(AnimSfxMap.skillElem(id));
            int beats = AnimSfxMap.beats(id, lv);
            if (n != beats) { ok = false; why.append(" beats ").append(n).append("!=").append(beats); }
            if (cls == 'P' || cls == 'E' || cls == 'D') {
                ok &= chk(why, "SKILL(cast)", d[SFXType.SKILL], 1);
                ok &= chk(why, miss ? "MISS" : "HIT", d[miss ? SFXType.MISS : SFXType.HIT], beats);
                ok &= chk(why, miss ? "HIT" : "MISS", d[miss ? SFXType.HIT : SFXType.MISS], 0);
                if (cls == 'E' && el >= 0) ok &= chk(why, "ELEM", d[el], beats);
                if (cls == 'D') ok &= chk(why, "HEAL", d[SFXType.HEAL], miss ? 0 : beats);
            } else if (cls == 'H') {
                ok &= chk(why, "SKILL(cast)", d[SFXType.SKILL], 1);
                ok &= chk(why, "HEAL", d[SFXType.HEAL], beats);
            } else if (cls == 'B') {
                // trạng thái (session 7): SKILL một lần lúc tung; nhịp dấu ấn im lặng nếu trúng, MISS nếu mọi mục tiêu kháng/miễn
                ok &= chk(why, "SKILL(cast)", d[SFXType.SKILL], 1);
                if (miss) ok &= chk(why, "MISS", d[SFXType.MISS], beats);
                else if (d[SFXType.MISS] > beats) { ok = false; why.append(" MISS ").append(d[SFXType.MISS]).append(">").append(beats); }   // ép trúng vẫn có thể bị miễn hiệu ứng (vd. kỹ năng 10) → MISS
            }
            if (cls == 'E' && el < 0) { /* ánh sáng: không lớp nguyên tố */ }
        }
        // sau ca: không còn sự kiện treo, không ai chết
        if (SFXManager.pending() != 0) { ok = false; why.append(" con su kien cho"); }
        if (invincible) { for (int i = 0; i < b.sfx$count(); i++) { e a = b.sfx$actor(i); if (a != null && a.sfx$hp() < 1) { ok = false; why.append(" actor ").append(i).append(" HP<1"); } } }
        System.out.println("SFXTEST AFTER " + enemyDump());
        report(idx, ok, why.toString(), d, exp, act);
        finishCase(b, ok);
    }
    static boolean chk(StringBuffer why, String what, int got, int want) {
        if (got == want) return true;
        why.append(' ').append(what).append(' ').append(got).append("!=").append(want);
        return false;
    }
    static void report(int idx, boolean ok, String why, int[] d, int exp, int act) {
        StringBuffer b = new StringBuffer();
        b.append("SFXTEST ").append(ok ? "PASS" : "FAIL").append(" case=").append(idx + 1).append('/').append(nCases)
         .append(" hero=").append(cHero[idx] == 0 ? "Quinn" : "Julie").append(" kind=").append(cKind[idx]).append(" skill=").append(cSkill[idx])
         .append(" level=").append(cLevel[idx]).append(" beats(exp/act)=").append(exp).append('/').append(act)
         .append(" HIT=").append(d[SFXType.HIT]).append(" MISS=").append(d[SFXType.MISS]).append(" CRIT=").append(d[SFXType.CRIT])
         .append(" SKILL=").append(d[SFXType.SKILL]).append(" FIRE=").append(d[SFXType.FIRE]).append(" WATER=").append(d[SFXType.WATER])
         .append(" EARTH=").append(d[SFXType.EARTH]).append(" WIND=").append(d[SFXType.WIND]).append(" HEAL=").append(d[SFXType.HEAL])
         .append(" dupSuppressed=").append(SfxHooks.dupEvents - snapDup).append(" ms=").append(System.currentTimeMillis() - caseStart);
        if (!ok) b.append(" WHY:").append(why);
        System.out.println(b.toString());
        if (!ok) dumpTrace();
    }

    static void finishCase(bn b, boolean ok) {
        SFXManager.injectFailure = false; SFXManager.setEnabled(true); passive = true;
        if (ok) passCount++; else failCount++;
        runState = RS_IDLE;
        SfxHooks.sink = null;
        forceHit = -1;
        if (autoRun) { nextAuto(b); return; }
        mode = MENU; status = (ok ? "PASS" : "FAIL") + " - xem log"; dirty = true;
    }
    static void finish(bn b, String msg, boolean ok) {
        System.out.println("SFXTEST FAIL case=" + (curCase + 1) + " " + msg);
        finishCase(b, ok);
    }

    // ====================================================================================== auto-test
    static void buildAuto() {
        nCases = 0; curCase = -1; passCount = failCount = skipCount = 0;
        for (int h = 0; h < 2; h++) {
            add(h, K_NORMAL, 0, 1); add(h, K_MISS, 0, 1); add(h, K_CRIT, 0, 1); add(h, K_INJECT, 0, 1); add(h, K_OFF, 0, 1);
            if (h == 0) { add(0, K_ENEMY, 0, 1); add(0, K_ENEMY, 0, 1); }   // quái đánh thường (trạng thái 13)
            for (int id = 1; id < NS; id++) {
                if (!castable(id)) continue;
                if (!only(id)) continue;
                add(h, K_SKILL, id, 1);
                if (id == 4) { add(h, K_SKILL, id, 5); add(h, K_SKILL, id, 9); }
                char c = AnimSfxMap.skillClass(id);
                if ((c == 'P' || c == 'E' || c == 'D') && id != 6) add(h, K_SKILL_MISS, id, 1);   // kỹ năng 6 dùng force-hit: không bao giờ trượt
            }
        }
        loadSkills();
        StringBuffer un = new StringBuffer();
        for (int id = 1; id < NS; id++) if (!castable(id)) { skipCount++; un.append(id).append(' '); }
        System.out.println("SFXTEST PLAN cases=" + nCases + " unreachable(passive/no-effect)=" + skipCount + " ids: " + un);
    }
    static boolean only(int id) {
        try {
            String o = System.getProperty("sfxtest.only");
            if (o == null || o.length() == 0) return true;
            return (" " + o.replace(',', ' ') + " ").indexOf(" " + id + " ") >= 0;
        } catch (Throwable t) { return true; }
    }
    static void nextAuto(bn b) {
        curCase++;
        if (curCase >= nCases) {
            robustness(b);
            System.out.println("SFXTEST RESULT pass=" + passCount + " fail=" + failCount + " unreachable=" + skipCount + " total=" + nCases);
            autoRun = false; autoDone = true; mode = MENU; runState = RS_IDLE; status = "auto xong: pass " + passCount + " fail " + failCount; dirty = true;
            return;
        }
        startCase(b, curCase);
    }

    /** Kiểm thử độ bền không cần chạy animation: flush, tắt SFX, SFX lỗi. */
    static void robustness(bn b) {
        // (1) hủy: sự kiện trễ phải bị xoá khi flush
        int lg = SfxLog.add(SfxLog.REC_ACTION, 5, 1, 0, SFXType.CRIT, 0, 0, 0, 0, SfxLog.ST_QUEUED);
        SFXManager.post(SFXType.CRIT, 1, 5, 5000, lg);
        boolean queued = SFXManager.pending() == 1;
        SfxHooks.paused();
        boolean flushed = SFXManager.pending() == 0;
        System.out.println("SFXTEST " + (queued && flushed ? "PASS" : "FAIL") + " robustness flush queued=" + queued + " flushedAfterPause=" + flushed);
        if (queued && flushed) passCount++; else failCount++;
        // (2) flush theo actor
        lg = SfxLog.add(SfxLog.REC_ACTION, 7, 1, 0, SFXType.HIT, 0, 0, 0, 0, SfxLog.ST_QUEUED);
        SFXManager.post(SFXType.HIT, 1, 7, 5000, lg);
        SFXManager.flushActor(8);
        boolean keep = SFXManager.pending() == 1;
        SFXManager.flushActor(7);
        boolean gone = SFXManager.pending() == 0;
        System.out.println("SFXTEST " + (keep && gone ? "PASS" : "FAIL") + " robustness flushActor keepOther=" + keep + " removed=" + gone);
        if (keep && gone) passCount++; else failCount++;
    }

    // ====================================================================================== vẽ
    public static void paint(Graphics g) {
        if (mode == OFF) return;
        try {
            if (font == null) font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            if (dirty || mode == RUNNING || mode == LOGVIEW) buildLines();
            int lh = font.getHeight() + 1;
            g.setClip(0, 0, 240, 320);
            g.setFont(font);
            int hgt = nLines * lh + 4;
            g.setColor(0x000000);
            g.fillRect(0, 0, 240, hgt);
            for (int i = 0; i < nLines; i++) {
                boolean sel = mode == MENU && i >= 0 && i < ROWS && i == row;
                g.setColor(sel ? 0xFFFF00 : 0xA0FFA0);
                g.drawString(lines[i], 2, 2 + i * lh, Graphics.TOP | Graphics.LEFT);
            }
        } catch (Throwable t) { }
    }

    static void buildLines() {
        dirty = false;
        nLines = 0;
        if (mode == LOGVIEW) {
            addLine("-- SFX LOG (phim bat ky: quay lai) --");
            int total = SfxLog.total();
            int shown = 0;
            for (int k = 1; k <= SfxLog.N && shown < 14 && k <= total; k++) {
                int i = (SfxLog.head - k + SfxLog.N) % SfxLog.N;
                addLine(shortLog(i));
                shown++;
            }
            return;
        }
        if (mode == MENU) {
            addLine("Nhan vat : < " + HERO[hero] + " >");
            addLine("Chieu    : < " + choiceName(caseSel) + " >");
            addLine("Cap      : < " + level + " >");
            addLine("[OK] Chay / chay lai");
            addLine("[OK] Chieu ke tiep");
            addLine("[OK] Auto-test");
            addLine("[OK] Xem log SFX");
            addLine("Bat tu   : " + (invincible ? "BAT" : "TAT") + "  (#)");
            addLine("Trung    : < " + (forceHitMenu < 0 ? "ngau nhien" : forceHitMenu == 1 ? "luon trung" : "luon truot") + " >");
            addLine("SFX      : " + (SFXManager.enabled ? "BAT" : "TAT") + "  (OK/trai/phai)");
            addLine("Am luong : < " + SFXManager.volume + " >");
            addLine("[OK] Thoat debug (0/SR)");
        } else {
            addLine("DEBUG: " + status);
        }
        addLine(status);
        int total = SfxLog.total();
        for (int k = 1; k <= 5 && k <= total; k++) addLine(shortLog((SfxLog.head - k + SfxLog.N) % SfxLog.N));
    }
    static void addLine(String s) { if (nLines < lines.length) lines[nLines++] = s; }
    static String shortLog(int i) {
        if (SfxLog.seq[i] == 0) return "-";
        String s = SfxLog.format(i);
        int p = s.indexOf(' ', 7);
        return p > 0 ? s.substring(p + 1) : s;
    }

    private DebugBattle() {}
}

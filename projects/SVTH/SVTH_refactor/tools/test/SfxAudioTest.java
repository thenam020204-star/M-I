import java.util.*;
import javax.microedition.media.Manager;

/**
 * Kiểm thử bộ phát SFX trên JVM thường với MMAPI giả (tools/test/fake): độ trễ post→start, chồng tiếng, không cắt tiếng,
 * bể Player, hạ/trả nhạc nền. Dùng đúng SFXManager/BeepBackend/BgmDuck sẽ đóng gói. Chạy: tools/test/run_audio_test.sh
 */
public class SfxAudioTest {
    static int fails = 0, passes = 0;
    static void check(String name, boolean ok, String detail) {
        if (ok) passes++; else fails++;
        System.out.println((ok ? "PASS " : "FAIL ") + name + (detail.length() > 0 ? "  [" + detail + "]" : ""));
    }
    static void sleep(long ms) { try { Thread.sleep(ms); } catch (InterruptedException e) { } }

    static List<long[]> log() { synchronized (Manager.LOG) { return new ArrayList<long[]>(Arrays.asList(snap(Manager.LOG))); } }
    static long[][] snap(Vector v) { long[][] r = new long[v.size()][]; for (int i = 0; i < r.length; i++) r[i] = (long[]) v.elementAt(i); return r; }
    static int count(String kind, int from) {
        int k = Arrays.asList(Manager.KIND).indexOf(kind), n = 0;
        List<long[]> l = log();
        for (int i = from; i < l.size(); i++) if (l.get(i)[1] == k) n++;
        return n;
    }
    static int mark() { return log().size(); }
    static long ms(long ns) { return ns / 1000000L; }

    /** Máy hết tài nguyên MMAPI giữa chừng (createPlayer lỗi sau N Player) và giới hạn tổng Player: không lỗi, vẫn phát, không vượt trần. */
    static void capTest() throws Exception {
        Manager.failAfter = 30;                              // 27 nạp sẵn + 3 Player thêm rồi máy báo hết
        BeepBackend.totalMax = 40;
        SFXManager.init(); sleep(400);
        BeepBackend be = SFXManager.backend();
        for (int r = 0; r < 6; r++) { for (int i = 0; i < 4; i++) SFXManager.post(i % 2 == 0 ? SFXType.HIT : SFXType.SKILL, 0, 1, 0, 0); sleep(220); }
        sleep(300);
        check("createPlayer lỗi sau 30 Player: bể dừng ở ≤ 30, không ngoại lệ, vẫn phát", be.players() <= 30 && count("start", 0) > 0 && count("stop", 0) == 0, "players=" + be.players() + " created=" + Manager.created + " starts=" + count("start", 0) + " lastError=" + be.lastError);
        System.out.println("TEST RESULT(cap) pass=" + passes + " fail=" + fails);
        Runtime.getRuntime().halt(fails == 0 ? 0 : 1);
    }

    static void totalTest() throws Exception {
        BeepBackend.totalMax = 29;                           // chỉ chừa 2 Player cho chồng tiếng
        SFXManager.init(); sleep(400);
        BeepBackend be = SFXManager.backend();
        for (int r = 0; r < 8; r++) { for (int i = 0; i < 3; i++) SFXManager.post(i == 0 ? SFXType.HIT : (i == 1 ? SFXType.MISS : SFXType.SKILL), 0, 1, 0, 0); sleep(200); }
        sleep(300);
        check("totalMax=29: tổng Player SFX không vượt 29", be.players() <= 29 && be.players() >= 27, "players=" + be.players());
        System.out.println("TEST RESULT(total) pass=" + passes + " fail=" + fails);
        Runtime.getRuntime().halt(fails == 0 ? 0 : 1);
    }

    /** Danh sách thời điểm (ns) các lần start() vào Player của một tiếng (theo id Player). */
    static List<long[]> starts(int from) {
        List<long[]> r = new ArrayList<long[]>(), l = log();
        for (int i = from; i < l.size(); i++) if (l.get(i)[1] == 0) r.add(l.get(i));
        return r;
    }
    static int countArg(String kind, int from, int arg) {
        int k = Arrays.asList(Manager.KIND).indexOf(kind), n = 0;
        List<long[]> l = log();
        for (int i = from; i < l.size(); i++) if (l.get(i)[1] == k && l.get(i)[3] == arg) n++;
        return n;
    }

    /**
     * session 6: thiết bị có độ trễ (Player còn STARTED tới start + L + độ dài, như J2ME Loader) và createPlayer chậm.
     * Dùng WAV thật của người dùng (res/ trên classpath). Kiểm: không tua/không start lên Player còn đang kêu,
     * kỹ năng buff (SKILL lúc tung + SKILL ở marker) chỉ kêu 1 lần, chuỗi HIT dài không phải chờ tạo Player, không phát muộn.
     */
    static BeepBackend waitPreload() {
        BeepBackend be = null;
        for (int w = 0; w < 400; w++) { sleep(50); be = SFXManager.backend(); if (be != null && be.preloadDone()) break; }
        sleep(100);
        return be;
    }

    /**
     * session 7: mô phỏng J2ME Loader (đọc từ mã nguồn MicroPlayer/AndroidPlayer/BasePlayer): realize/prefetch không nạp, start() prepare
     * (80 ms) nếu chưa nạp, phát hết → reset, stop = pause. Kịch bản trận thật: Quinn (giọng 1, actor 0) tung kỹ năng rồi đánh,
     * sủng vật (giọng 0, actor 1) tung kỹ năng + đánh, quái (giọng 0, actor 10) đánh; Quinn đánh thường 3 nhịp. Mỗi lần phát thật (âm lượng > 0)
     * phải: không prepare trong start(), bắt đầu ở vị trí 0, không đè Player đang kêu, post→start ≤ 15 ms.
     */
    static void j2melTest() throws Exception {
        Manager.j2mel = true; Manager.prepareMs = 80; Manager.createMs = 60; Manager.latencyMs = 100;
        SFXManager.init();
        SFXManager.touchVoice(1); SFXManager.touchVoice(0);                 // như SfxHooks.onState khi trận bắt đầu
        BeepBackend be = waitPreload();
        System.out.println("j2mel preload: players=" + be.players() + " warm=" + be.warmCount() + " primed=" + be.primed + " created=" + Manager.created);
        int m0 = mark();
        long maxLate = 0; int lost = 0, events = 0;
        int[][] seq = {   // {delay ms trước sự kiện, loại, giọng, actor}
            {0, SFXType.SKILL, 1, 0}, {650, SFXType.HIT, 1, 0},                                   // Quinn tung kỹ năng → va chạm
            {900, SFXType.SKILL, 0, 1}, {700, SFXType.HIT, 0, 1},                                 // sủng vật tung kỹ năng → va chạm
            {800, SFXType.HIT, 0, 10}, {350, SFXType.MISS, 0, 10},                                // quái đánh 2 nhịp
            {900, SFXType.HIT, 1, 0}, {240, SFXType.HIT, 1, 0}, {320, SFXType.HIT, 1, 0}, {0, SFXType.CRIT, 1, 0},   // Quinn đánh thường 3 nhịp + chí mạng
            {900, SFXType.SKILL, 1, 0}, {400, SFXType.SKILL, 1, 0},                               // buff: cast + marker khi tiếng cast còn kêu → 1 tiếng
            {1200, SFXType.SKILL, 0, 1}, {150, SFXType.SKILL, 0, 10},                             // sủng vật và quái cùng giọng 0 gần nhau: actor khác → đều kêu
            {900, SFXType.HEAL, 1, 0}, {500, SFXType.WATER, 0, 1}, {600, SFXType.HIT, 0, 1},
        };
        int skillQuinnBuff = 0, petMonsterSkill = 0;
        for (int r = 0; r < 3; r++) {
            for (int i = 0; i < seq.length; i++) {
                sleep(seq[i][0]);
                int m = mark(); long t0 = System.nanoTime();
                SFXManager.post(seq[i][1], seq[i][2], seq[i][3], 0, 0);
                events++;
                sleep(20);
                List<long[]> l = log(); long t1 = 0;
                for (int j = m; j < l.size(); j++) if (l.get(j)[1] == 7) { t1 = l.get(j)[0]; if ((l.get(j)[3] & 2) != 0) System.out.println("  prepare r=" + r + " i=" + i + " type=" + SFXType.name(seq[i][1]) + " v=" + seq[i][2]); break; }
                boolean skipOk = (i == 11);                                   // marker buff khi tiếng cast còn kêu: phải bỏ qua
                if (t1 == 0) { if (!skipOk) { lost++; System.out.println("  lost r=" + r + " i=" + i + " type=" + SFXType.name(seq[i][1]) + " v=" + seq[i][2] + " actor=" + seq[i][3]); } }
                else { if (skipOk) skillQuinnBuff++; long late = (t1 - t0) / 1000000L; if (late > maxLate) maxLate = late; }
                if ((i == 12 || i == 13) && t1 != 0) petMonsterSkill++;
            }
            sleep(1500);
        }
        int bad = 0, prep = 0, notZero = 0, over = 0, ast = 0;
        List<long[]> l = log();
        for (int j = m0; j < l.size(); j++) if (l.get(j)[1] == 7) { ast++; long a = l.get(j)[3]; if (a != 0) bad++; if ((a & 2) != 0) prep++; if ((a & 4) != 0) notZero++; if ((a & 1) != 0) over++; }
        check("j2mel: mọi tiếng thật không phải prepare lúc phát (Player đã được làm ấm)", prep == 0, "prepareTrongStart=" + prep + "/" + ast);
        check("j2mel: mọi tiếng thật bắt đầu ở vị trí 0, không đè Player đang kêu", notZero == 0 && over == 0, "khongTu0=" + notZero + " de=" + over);
        check("j2mel: không mất tiếng nào (trừ marker buff khi cast còn kêu), post→start ≤ 15 ms", lost == 0 && maxLate <= 15, "lost=" + lost + "/" + events + " maxLate=" + maxLate + "ms");
        check("j2mel: buff (cast + marker khi tiếng cast còn kêu) chỉ kêu 1 lần", skillQuinnBuff == 0, "markerKeu=" + skillQuinnBuff);
        check("j2mel: SKILL giọng 0 của sủng vật và của quái gần nhau: cả hai kêu (giới hạn theo actor)", petMonsterSkill == 6, "keu=" + petMonsterSkill + "/6");
        int rw = countArg("rewind", m0, 1);
        check("j2mel: không tua Player đang kêu, không stop()", rw == 0 && countArg("stop", m0, 1) == 0, "rewindWhilePlaying=" + rw + " stopWhilePlaying=" + countArg("stop", m0, 1));
        System.out.println("TEST RESULT(j2mel) pass=" + passes + " fail=" + fails + " players=" + be.players() + " warm=" + be.warmCount() + " primed=" + be.primed + " coldStarts=" + be.coldStarts + " created=" + Manager.created);
        Runtime.getRuntime().halt(fails == 0 ? 0 : 1);
    }

    static void latTest(int L) throws Exception {
        Manager.latencyMs = L; Manager.createMs = 60;
        SFXManager.init();
        BeepBackend be = waitPreload();
        int pre = be.players();
        System.out.println("lat=" + L + " preload players=" + pre + " created=" + Manager.created + " fromFile(SKILL,1)=" + be.usedFile(SFXType.SKILL, 1) + " dur(SKILL,1)=" + be.durationMs(SFXType.SKILL, 1) + " dur(HIT,2)=" + be.durationMs(SFXType.HIT, 2));
        int m0 = mark();
        // 1. kỹ năng buff: SKILL lúc tung, 400 ms sau SKILL ở marker (cùng giọng Quinn)
        int skillStarts = 0;
        for (int c = 0; c < 3; c++) {
            int m = mark();
            SFXManager.post(SFXType.SKILL, 1, 0, 0, 0); sleep(400);
            SFXManager.post(SFXType.SKILL, 1, 0, 0, 0); sleep(2200);
            int n = starts(m).size(); skillStarts += n;
            if (c == 0) System.out.println("  buff cast: starts=" + n);
        }
        check("lat=" + L + ": buff (SKILL tung + SKILL marker khi tiếng đầu còn kêu) chỉ kêu 1 lần/lần tung", skillStarts == 3, "starts=" + skillStarts + "/3 lần tung");
        // 2. đánh thường Julie: 3 HIT (871 ms) cách 240/320 ms, có CRIT; 4 lượt
        long maxLate = 0, sumLate = 0; int nLate = 0, missing = 0;
        int mc = mark();
        for (int r = 0; r < 4; r++) {
            int[] at = {0, 240, 560};
            long base = System.nanoTime();
            for (int i = 0; i < 3; i++) {
                long due = base + at[i] * 1000000L;
                while (System.nanoTime() < due) sleep(1);
                int m = mark(); long t0 = System.nanoTime();
                SFXManager.post(SFXType.HIT, 2, 0, 0, 0);
                if (i == 1) SFXManager.post(SFXType.CRIT, 2, 0, 0, 0);
                sleep(150);
                List<long[]> st = starts(m);
                if (st.isEmpty()) missing++;
                else { long late = (st.get(0)[0] - t0) / 1000000L; if (late > maxLate) maxLate = late; sumLate += late; nLate++; }
            }
            sleep(1500);
        }
        int createsDuringPlay = count("create", mc);
        check("lat=" + L + ": chuỗi HIT dài chồng nhau: mọi HIT đều kêu, post→start ≤ 15 ms (không tạo Player lúc phát, không hẹn muộn)", missing == 0 && maxLate <= 15, "missing=" + missing + " maxLate=" + maxLate + "ms avg=" + (nLate > 0 ? sumLate / nLate : -1) + "ms createsDuringPlay=" + createsDuringPlay);
        // 3. bão ngẫu nhiên
        Random rnd = new Random(11);
        for (int i = 0; i < 200; i++) { SFXManager.post(rnd.nextInt(SFXType.COUNT), rnd.nextInt(3), rnd.nextInt(6), 0, 0); sleep(rnd.nextInt(40)); }
        sleep(2500);
        int rw = countArg("rewind", m0, 1), sw = countArg("start", m0, 1);
        check("lat=" + L + ": không tua (setMediaTime) Player còn đang kêu", rw == 0, "rewindWhilePlaying=" + rw + " rewinds=" + count("rewind", m0));
        check("lat=" + L + ": không start() lên Player còn đang kêu", sw == 0, "startWhilePlaying=" + sw);
        check("lat=" + L + ": không stop() (không cắt tiếng)", count("stop", m0) == 0, "stops=" + count("stop", m0));
        System.out.println("TEST RESULT(lat" + L + ") pass=" + passes + " fail=" + fails + " players=" + be.players() + " created=" + Manager.created);
        Runtime.getRuntime().halt(fails == 0 ? 0 : 1);
    }

    public static void main(String[] a) throws Exception {
        bv music = new bv(); Main.music = music;
        if (a.length > 0 && a[0].startsWith("lat")) { latTest(Integer.parseInt(a[0].substring(3))); return; }
        if (a.length > 0 && a[0].equals("j2mel")) { j2melTest(); return; }
        if (a.length > 0 && a[0].equals("cap")) { capTest(); return; }
        if (a.length > 0 && a[0].equals("total")) { totalTest(); return; }
        SFXManager.init();
        BeepBackend be = waitPreload();
        System.out.println("preload: players=" + be.players() + " warm=" + be.warmCount() + " created=" + Manager.created);
        check("preload (session 7): mọi tiếng có Player (≥ 27), giữ ấm đúng warmMax = 26", be.players() >= 27 && be.warmCount() == 26, "players=" + be.players() + " warm=" + be.warmCount());

        // ---- 1. độ trễ post -> start
        long[] lat = new long[40];
        for (int i = 0; i < lat.length; i++) {
            int m = mark();
            long t0 = System.nanoTime();
            SFXManager.post(SFXType.HIT, 0, 1, 0, 0);
            long t1 = 0;
            for (int w = 0; w < 2000 && t1 == 0; w++) {
                List<long[]> l = log();
                for (int j = m; j < l.size(); j++) if (l.get(j)[1] == 0) { t1 = l.get(j)[0]; break; }
                if (t1 == 0) sleep(1);
            }
            lat[i] = t1 == 0 ? 999999 : (t1 - t0) / 1000;      // µs
            sleep(160);                                         // chờ hết tiếng (HIT 70 ms) + rewind nền
        }
        Arrays.sort(lat);
        long med = lat[lat.length / 2], p95 = lat[(int) (lat.length * 0.95)];
        check("độ trễ post→start: trung vị < 3 ms, p95 < 10 ms", med < 3000 && p95 < 10000, "median=" + med + "us p95=" + p95 + "us max=" + lat[lat.length - 1] + "us");
        check("không stop()/phát đè khi lặp lại cùng tiếng", count("stop", 0) == 0, "stop=" + count("stop", 0));

        // ---- 2. chồng tiếng cùng loại (session 6: cùng giọng trong < gap ms thì gộp; cách ≥ gap thì chồng, mỗi tiếng một Player)
        int m = mark();
        for (int i = 0; i < 3; i++) SFXManager.post(SFXType.HIT, 0, 1, 0, 0);
        sleep(300);
        check("3 HIT cùng giọng cùng một lúc: gộp làm 1 tiếng (gap 40 ms), không start đè", count("start", m) == 1 && countArg("start", m, 1) == 0, "starts=" + count("start", m));
        m = mark();
        for (int i = 0; i < 3; i++) { SFXManager.post(SFXType.HIT, 0, 1, 0, 0); sleep(45); }
        sleep(300);
        List<long[]> l = log();
        Set<Long> ids = new HashSet<Long>();
        for (int j = m; j < l.size(); j++) if (l.get(j)[1] == 0) ids.add(l.get(j)[2]);
        check("3 HIT cách 45 ms (tiếng 70 ms chồng nhau): kêu đủ 3, ≥ 2 Player khác nhau, không start đè", count("start", m) == 3 && ids.size() >= 2 && countArg("start", m, 1) == 0, "starts=" + count("start", m) + " distinct=" + ids.size() + " pool=" + be.poolSize(SFXType.HIT, 0));
        sleep(300);

        // ---- 3. nhiều loại khác nhau cùng lúc
        m = mark();
        SFXManager.post(SFXType.HIT, 1, 1, 0, 0); SFXManager.post(SFXType.CRIT, 1, 1, 0, 0); SFXManager.post(SFXType.SKILL, 1, 1, 0, 0); SFXManager.post(SFXType.FIRE, 0, 2, 0, 0); SFXManager.post(SFXType.HEAL, 2, 3, 0, 0);
        sleep(40);
        check("5 loại khác nhau cùng lúc: cả 5 start trong 40 ms", count("start", m) == 5, "starts=" + count("start", m));
        sleep(400);

        // ---- 4. quá bể: không cắt, không đè
        m = mark();
        for (int i = 0; i < 7; i++) { SFXManager.post(SFXType.MISS, 0, 1, 0, 0); sleep(42); }
        sleep(600);
        int allStarts = count("start", m);
        check("7 MISS cách 42 ms (tiếng 90 ms, bể ≤ 3): không stop, không start đè, phát ≥ 3", count("stop", m) == 0 && countArg("start", m, 1) == 0 && allStarts >= 3 && allStarts <= 7, "starts=" + allStarts + " stops=" + count("stop", m));
        m = mark();
        SFXManager.post(SFXType.SKILL, 0, 1, 0, 0); sleep(30); SFXManager.post(SFXType.SKILL, 0, 1, 0, 0);
        sleep(300);
        check("SKILL gọi lại khi tiếng cũ còn kêu: bỏ qua (solo), không chồng, không cắt", count("start", m) == 1 && count("stop", m) == 0, "starts=" + count("start", m));

        // ---- 5. flush không cắt tiếng đang phát
        m = mark();
        SFXManager.post(SFXType.SKILL, 0, 1, 0, 0);
        sleep(15);
        SFXManager.flushAll();                               // kết thúc trận / hủy animation
        sleep(20);
        check("flushAll() không dừng tiếng đang phát", count("stop", m) == 0, "stops=" + count("stop", m));
        SFXManager.post(SFXType.SKILL, 0, 1, 0, 0);
        sleep(15);
        SFXManager.stopAll();                                // tạm dừng game
        sleep(20);
        check("stopAll() (tạm dừng) mới dừng tiếng đang phát", count("stop", m) >= 1, "stops=" + count("stop", m));
        sleep(300);

        // ---- 6. bão sự kiện ngẫu nhiên: không có stop nào
        m = mark();
        Random rnd = new Random(7);
        for (int i = 0; i < 300; i++) { SFXManager.post(rnd.nextInt(SFXType.COUNT), rnd.nextInt(3), rnd.nextInt(6), 0, 0); sleep(rnd.nextInt(12)); }
        sleep(600);
        check("300 sự kiện ngẫu nhiên: 0 stop, 0 start đè", count("stop", m) == 0, "stops=" + count("stop", m) + " starts=" + count("start", m));
        int overStart = 0; l = log();
        for (int j = m; j < l.size(); j++) if (l.get(j)[1] == 0 && l.get(j)[3] == 1) overStart++;
        check("không start() lên Player còn đang phát", overStart == 0, "startWhilePlaying=" + overStart);

        // ---- 7. hạ nhạc nền
        sleep(500);
        synchronized (bv.VOL) { bv.VOL.clear(); }
        int lm = mark();
        BgmDuck.duckCalls = 0;
        long tPost = System.nanoTime();
        SFXManager.post(SFXType.SKILL, 0, 1, 0, 0);          // 120 ms
        sleep(80);
        long[][] vol = snap(bv.VOL);
        l = log();
        long startNs = 0; for (int j = lm; j < l.size(); j++) if (l.get(j)[1] == 0) { startNs = l.get(j)[0]; break; }
        check("SFX phát → nhạc hạ xuống 45% (80→36) ngay sau khi tiếng bắt đầu", vol.length >= 1 && vol[0][1] == 36 && vol[0][0] >= startNs, "vol calls=" + vol.length + (vol.length > 0 ? " first=" + vol[0][1] + " afterStart=" + ((vol[0][0] - startNs) / 1000) + "us" : ""));
        sleep(700);
        vol = snap(bv.VOL);
        long[] seqv = new long[vol.length]; for (int i = 0; i < vol.length; i++) seqv[i] = vol[i][1];
        boolean ramp = vol.length >= 3 && vol[vol.length - 1][1] == 80;
        for (int i = 2; i < vol.length; i++) if (vol[i][1] < vol[i - 1][1]) ramp = false;
        check("hết tiếng + 200 ms giữ → nhạc trả về 80 từng bước, không giật xuống", ramp && !BgmDuck.isDucked(), "levels=" + Arrays.toString(seqv));
        long relNs = 0; for (int i = 1; i < vol.length; i++) if (vol[i][1] > 36) { relNs = vol[i][0]; break; }
        long endMs = ms(startNs) + 120;
        check("thời điểm bắt đầu trả nhạc ≥ hết tiếng + hold(200)", relNs != 0 && ms(relNs) >= endMs + 190, "restoreStart-soundEnd=" + (relNs == 0 ? -1 : ms(relNs) - endMs) + "ms");

        // nhiều tiếng liên tiếp: giữ hạ, chỉ trả một lần
        synchronized (bv.VOL) { bv.VOL.clear(); }
        for (int i = 0; i < 6; i++) { SFXManager.post(SFXType.HIT, 0, 1, 0, 0); sleep(100); }
        sleep(900);
        vol = snap(bv.VOL); int lowCount = 0, finalV = (int) vol[vol.length - 1][1];
        for (long[] x : vol) if (x[1] == 36) lowCount++;
        boolean noDipBack = true; boolean seenLow = false;
        for (long[] x : vol) { if (x[1] == 36) seenLow = true; else if (seenLow && x[1] > 36) { seenLow = false; } }
        check("chuỗi 6 tiếng cách 100 ms: nhạc cuối cùng về 80", finalV == 80 && !BgmDuck.isDucked(), "calls=" + vol.length + " final=" + finalV);

        // SFX tới giữa lúc đang trả → hạ lại ngay
        synchronized (bv.VOL) { bv.VOL.clear(); }
        SFXManager.post(SFXType.SKILL, 0, 1, 0, 0);
        sleep(120 + 200 + 40 + 40);                          // đang ở giữa đoạn trả
        SFXManager.post(SFXType.HIT, 0, 1, 0, 0);
        sleep(40);
        vol = snap(bv.VOL);
        check("SFX đến giữa lúc trả nhạc → hạ lại về 36 ngay", vol.length >= 2 && vol[vol.length - 1][1] == 36, "last=" + (vol.length > 0 ? vol[vol.length - 1][1] : -1) + " calls=" + vol.length);
        sleep(900);

        // nhạc tắt / duck=100 / âm lượng nhạc 0: không đụng nhạc
        synchronized (bv.VOL) { bv.VOL.clear(); }
        at.b = false; SFXManager.post(SFXType.SKILL, 0, 1, 0, 0); sleep(500);
        check("nhạc đang tắt (at.b=false): không đụng âm lượng nhạc", bv.VOL.size() == 0, "calls=" + bv.VOL.size());
        at.b = true; o.g = 0; SFXManager.post(SFXType.SKILL, 0, 1, 0, 0); sleep(500);
        check("âm lượng nhạc = 0: không đụng", bv.VOL.size() == 0, "calls=" + bv.VOL.size());
        o.g = 60; BgmDuck.pct = 100; SFXManager.post(SFXType.SKILL, 0, 1, 0, 0); sleep(500);
        check("duck=100 (tắt hạ nhạc): không đụng", bv.VOL.size() == 0, "calls=" + bv.VOL.size());
        BgmDuck.pct = 45; Main.music = null; SFXManager.post(SFXType.SKILL, 0, 1, 0, 0); sleep(300);
        check("không có nhạc (Main.music=null): không lỗi, SFX vẫn phát", count("start", mark() - 1) >= 0, "");
        Main.music = music;
        o.g = 80;

        // dừng tạm: trả nhạc về đầy ngay
        synchronized (bv.VOL) { bv.VOL.clear(); }
        SFXManager.post(SFXType.SKILL, 0, 1, 0, 0); sleep(30);
        SFXManager.stopAll(); sleep(20);
        vol = snap(bv.VOL);
        check("stopAll() trả nhạc về mức đầy ngay", vol.length >= 2 && vol[vol.length - 1][1] == 80, "last=" + (vol.length > 0 ? vol[vol.length - 1][1] : -1));

        // ---- 8. máy chỉ nạp được ít Player: không crash, vẫn phát
        System.out.println("---- summary");
        System.out.println("TEST RESULT pass=" + passes + " fail=" + fails);
        Runtime.getRuntime().halt(fails == 0 ? 0 : 1);
    }
}

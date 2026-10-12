/**
 * Tự hạ âm lượng nhạc nền (BGM) khi SFX đang phát rồi trả lại từ từ (ducking như game hiện đại).
 * Chỉ dùng API sẵn có của game: Main.music (bv).b(volume) — cùng lời gọi mà game dùng ở "Cài đặt nhạc" — và âm lượng nhạc người chơi
 * chọn o.g, nên tôn trọng cài đặt: nhạc tắt (o.g = 0 hoặc at.b = false) thì không làm gì; đổi âm lượng nhạc giữa chừng được tính lại
 * ở lần hạ/trả kế tiếp. Chạy ở thread SFX; mọi lỗi bị bắt, không bao giờ ảnh hưởng game.
 */
public final class BgmDuck {
    /** % âm lượng nhạc giữ lại khi đang hạ (cấu hình `duck=`; 100 = không hạ). */
    public static volatile int pct = 45;
    /** Giữ mức hạ thêm bao nhiêu ms sau khi tiếng SFX cuối kết thúc (cấu hình `duckhold=`). */
    public static volatile int holdMs = 200;
    /** Thời gian trả nhạc về mức cũ (cấu hình `duckramp=`). */
    public static volatile int rampMs = 150;
    /** Số bước trả âm lượng. */
    private static final int STEPS = 4;

    private static boolean ducked = false, ramping = false;
    private static long releaseAt = 0, nextAt = 0;
    private static int step = 0;
    public static volatile int duckCalls = 0, restoreCalls = 0;

    /** Gọi ngay sau khi một SFX bắt đầu phát (durMs = độ dài tiếng). Hạ nhạc nếu chưa hạ; làm mới hạn giữ. */
    public static synchronized void onSfx(long now, int durMs) {
        try {
            int p = pct;
            if (p >= 100) return;
            long end = now + durMs + holdMs;
            if (end > releaseAt) releaseAt = end;
            bv m = Main.music;
            int base = o.g;
            if (m == null || !at.b || base <= 0) return;
            ducked = true; ramping = false;
            m.b(base * (p < 0 ? 0 : p) / 100);     // luôn đặt lại: game có thể vừa đặt nhạc về mức đầy (vào trận, đổi bài)
            duckCalls++;
        } catch (Throwable t) { }
    }

    /** Thread SFX gọi định kỳ: trả nhạc về mức cũ theo từng bước khi hết tiếng + hạn giữ. @return thời điểm cần gọi lại (ms epoch), 0 = không cần. */
    public static synchronized long tick(long now) {
        try {
            if (!ducked) return 0;
            if (!ramping) {
                if (now < releaseAt) return releaseAt;
                ramping = true; step = 0; nextAt = now;
            }
            if (now < nextAt) return nextAt;
            step++;
            bv m = Main.music;
            int base = o.g;
            int p = pct < 0 ? 0 : (pct > 100 ? 100 : pct);
            if (m != null && at.b && base > 0) {
                m.b(base * (p + (100 - p) * step / STEPS) / 100);
                restoreCalls++;
            }
            if (step >= STEPS) { ducked = false; ramping = false; return 0; }
            int gap = rampMs / STEPS;
            nextAt = now + (gap < 1 ? 1 : gap);
            return nextAt;
        } catch (Throwable t) { ducked = false; ramping = false; return 0; }
    }

    /** Trả nhạc về mức đầy ngay (dừng SFX, tạm dừng, tắt SFX). */
    public static synchronized void restoreNow() {
        try {
            if (!ducked) return;
            ducked = false; ramping = false; releaseAt = 0;
            bv m = Main.music;
            int base = o.g;
            if (m != null && at.b && base > 0) { m.b(base); restoreCalls++; }
        } catch (Throwable t) { }
    }

    public static synchronized boolean isDucked() { return ducked; }

    private BgmDuck() {}
}

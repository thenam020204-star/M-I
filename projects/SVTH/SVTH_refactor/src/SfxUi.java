import javax.microedition.lcdui.Graphics;

/**
 * Giao diện cài đặt SFX nằm trong menu hệ thống của game, tách khỏi nhạc nền:
 *  - thêm dòng "Cài đặt SFX" ngay dưới "Cài đặt nhạc" (menuRows);
 *  - mở lại chính trang âm lượng của game (as.p == 3: thanh 10 nấc, trái/phải ±10%) nhưng chỉnh SFXManager.volume
 *    thay vì o.g/at.b khi mode == 1 (vol/on thay lệnh đọc o.g/at.b của hàm vẽ; key nuốt phím trái/phải);
 *  - bỏ hộp thoại "Bật âm nhạc?" lúc khởi động: nhạc tự bật như khi chọn "Có" (skipPrompt).
 * Âm lượng 0 = SFX tắt. Mọi hàm bắt Throwable: lỗi giao diện không được làm hỏng menu game.
 * Các hook được Patch.patchUi() chèn vào av/as/ab; mode 0 (nhạc) giữ nguyên hành vi gốc.
 */
public final class SfxUi {
    /** "Cài đặt SFX" */
    public static final String ROW = "Cài đặt SFX";
    private static final String MUSIC_ROW = "Cài đặt nhạc";
    private static final String EXIT_ROW = "Thoát game";
    private static final int PAGE_VOLUME = 3;

    /** 0: trang âm lượng đang chỉnh NHẠC (game gốc); 1: đang chỉnh SFX. Đặt lúc chọn dòng menu, xoá khi rời trang. */
    public static volatile int mode = 0;
    private static as host;

    // ---------------------------------------------------------------- khởi động: bỏ hộp thoại "Bật âm nhạc?"
    /**
     * Thay `getstatic at.a:Z` (allowMusic) trước nhánh "hỏi nhạc" của màn logo (av). Luôn trả false để rơi vào showCover(),
     * sau khi đặt đúng trạng thái mà câu trả lời "Có" của game gốc đặt: at.b = true, o.g = at.d (âm lượng mặc định).
     * Máy không cho phép nhạc (allowMusic = 0) giữ nguyên hành vi gốc.
     */
    public static boolean skipPrompt() {
        try {
            if (at.a) { at.b = true; o.g = at.d; }
        } catch (Throwable t) { }
        try { SFXManager.init(); } catch (Throwable t) { }   // session 6: nạp sẵn Player SFX từ màn logo (thread SFX, từng cái một), không đợi trận đầu
        return false;
    }

    // ---------------------------------------------------------------- dòng menu
    /** Chèn "Cài đặt SFX" sau "Cài đặt nhạc" (hoặc trước "Thoát game" nếu máy không có mục nhạc). Hook trước `putstatic as.a:[String`. */
    public static String[] menuRows(String[] rows) {
        try {
            if (rows == null) return rows;
            int pos = -1, exit = -1;
            for (int i = 0; i < rows.length; i++) {
                if (ROW.equals(rows[i])) return rows;
                if (MUSIC_ROW.equals(rows[i])) pos = i + 1;
                else if (EXIT_ROW.equals(rows[i])) exit = i;
            }
            if (pos < 0) pos = exit >= 0 ? exit : rows.length;
            String[] o = new String[rows.length + 1];
            for (int i = 0, j = 0; i < o.length; i++) o[i] = (i == pos) ? ROW : rows[j++];
            return o;
        } catch (Throwable t) { return rows; }
    }

    /**
     * Thay `String.equals` đứng sau `ldc "Cài đặt nhạc"` trong ab.h: dòng nhạc và dòng SFX đều dẫn tới trang âm lượng,
     * chỉ khác mode. Ngăn xếp giống hệt equals (receiver, đối số → boolean).
     */
    public static boolean isSoundRow(String row, Object music) {
        try {
            if (row != null && row.equals(ROW)) { mode = 1; SFXManager.init(); return true; }   // mở trang SFX: khởi động thread + nạp sẵn Player để tiếng thử không phải chờ
            boolean m = row != null && row.equals(music);
            if (m) mode = 0;
            return m;
        } catch (Throwable t) { return row != null && row.equals(music); }
    }

    // ---------------------------------------------------------------- trang âm lượng dùng chung
    /** Hook đầu as.a(byte,byte): ghi nhớ trang; rời trang âm lượng thì về mode 0. */
    public static void page(as self, int group, int p) {
        try {
            host = self;
            if (!(group == 9 && p == PAGE_VOLUME)) mode = 0;
        } catch (Throwable t) { }
    }

    /** Thay `getstatic o.g:B` trong vẽ/khởi tạo trang âm lượng. */
    public static byte vol() {
        try { if (mode == 1) return (byte) SFXManager.volume; } catch (Throwable t) { }
        return o.g;
    }

    /** Thay `getstatic at.b:Z` trong vẽ/khởi tạo trang âm lượng. */
    public static boolean on() {
        try { if (mode == 1) return SFXManager.volume > 0; } catch (Throwable t) { }
        return at.b;
    }

    /** Hook đầu ab.h(int): true = đã xử lý (nuốt phím). Chỉ động tới phím trái(2)/phải(5) của trang SFX; mọi phím khác (OK/Về…) chạy mã gốc. */
    public static boolean key(int k) {
        try {
            if (mode != 1 || as.p != PAGE_VOLUME) return false;
            if (k == 5) { setVolume(SFXManager.volume + 10, true); return true; }
            if (k == 2) { setVolume(SFXManager.volume - 10, false); return true; }
        } catch (Throwable t) { }
        return false;
    }

    private static void setVolume(int v, boolean preview) {
        SFXManager.setVolume(v);
        if (SFXManager.volume <= 0) SFXManager.stopAll();
        icon();
        if (preview && SFXManager.volume > 0) SFXManager.preview();
    }

    /** Biểu tượng loa của trang: hoạt hoạ 1 khi có tiếng, 0 khi tắt (giống mã nhạc của game). */
    private static void icon() {
        try { if (host != null) host.sfx$icon(SFXManager.volume > 0 ? 1 : 0); } catch (Throwable t) { }
    }

    /** Hook ngay trước RETURN của khối vẽ trang âm lượng: nhãn phân biệt trang SFX với trang nhạc (cùng ảnh tiêu đề "Âm lượng"). */
    public static void paint(Graphics g, int w, int h) {
        try {
            if (mode != 1) return;
            g.setColor(0xFFF0A0);
            vh.drawString(g, "Hiệu ứng (SFX)", w / 2, h / 2 - 60, 17);
        } catch (Throwable t) { }
    }
}

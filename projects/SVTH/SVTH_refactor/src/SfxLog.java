/**
 * Nhật ký SFX dạng vòng, không cấp phát khi ghi (mảng int tĩnh).
 * Các giai đoạn: QUEUED (hook báo sự kiện) → API_CALLED (đã gọi API phát, chưa xác nhận) → PLAYED (Player.getState()==STARTED)
 * hoặc DROPPED / FLUSHED / FAILED / OFF.
 */
public final class SfxLog {
    public static final int N = 512;
    public static final int ST_QUEUED = 0, ST_REQUESTED = 1, ST_PLAYED = 2, ST_DROPPED = 3, ST_FLUSHED = 4, ST_FAILED = 5, ST_DUP = 6, ST_OFF = 7;
    public static final int REC_ACTION = 1, REC_SUMMARY = 2;

    static final int[] kind = new int[N], seq = new int[N], ms = new int[N], actor = new int[N], role = new int[N], skill = new int[N];
    static final int[] type = new int[N], frame = new int[N], beat = new int[N], expect = new int[N], actual = new int[N], status = new int[N];
    static int head = 0, total = 0, seqNo = 0;
    static long t0 = System.currentTimeMillis();
    /** In ra System.out mỗi dòng (chỉ bản debug bật). */
    public static boolean print = BuildFlags.DEBUG || propOn();
    /** Cờ -Dsfx.log=1 chỉ có tác dụng trong emulator (trên máy thật getProperty trả null) — dùng cho harness. */
    private static boolean propOn() { try { return "1".equals(System.getProperty("sfx.log")); } catch (Throwable t) { return false; } }
    /** Bộ đếm theo trạng thái, đọc bởi test. */
    public static final int[] count = new int[8];

    private static final String[] ST = {"QUEUED", "API_CALLED", "PLAYED", "DROPPED", "FLUSHED", "FAILED", "DUP", "OFF"};

    public static synchronized int add(int kind_, int actor_, int role_, int skill_, int type_, int frame_, int beat_, int expect_, int actual_, int status_) {
        int i = head;
        head = (head + 1) % N;
        total++;
        kind[i] = kind_; seq[i] = ++seqNo; ms[i] = (int) (System.currentTimeMillis() - t0);
        actor[i] = actor_; role[i] = role_; skill[i] = skill_; type[i] = type_; frame[i] = frame_; beat[i] = beat_;
        expect[i] = expect_; actual[i] = actual_; status[i] = status_;
        if (kind_ == REC_ACTION && status_ >= 0 && status_ < 8) count[status_]++;
        if (print) System.out.println(format(i));
        return seq[i];
    }

    /** Cập nhật trạng thái bản ghi gần nhất theo seq (nếu còn trong vòng). */
    public static synchronized void setStatus(int seqId, int st) {
        for (int k = 0; k < N; k++) {
            if (seq[k] == seqId) {
                if (kind[k] == REC_ACTION) { if (status[k] >= 0 && status[k] < 8 && count[status[k]] > 0) count[status[k]]--; count[st]++; }
                status[k] = st;
                if (print) System.out.println(format(k));
                return;
            }
        }
    }

    public static synchronized void reset() {
        head = 0; total = 0;
        for (int i = 0; i < count.length; i++) count[i] = 0;
        for (int i = 0; i < N; i++) seq[i] = 0;
    }

    public static String format(int i) {
        StringBuffer b = new StringBuffer();
        b.append("SFXLOG ").append(seq[i]).append(' ').append(ms[i]).append("ms ");
        if (kind[i] == REC_SUMMARY) {
            b.append("SUMMARY actor=").append(actor[i]).append(" role=").append(role[i]).append(" skill=").append(skill[i])
             .append(" kind=").append(SFXType.name(type[i])).append(" expected=").append(expect[i]).append(" actual=").append(actual[i])
             .append(expect[i] == actual[i] ? " OK" : " MISMATCH");
        } else {
            b.append(SFXType.name(type[i])).append(" actor=").append(actor[i]).append(" role=").append(role[i]).append(" skill=").append(skill[i])
             .append(" voice=").append(expect[i]).append(" frame=").append(frame[i]).append(" beat=").append(beat[i]).append(' ').append(ST[status[i] & 7]);
        }
        return b.toString();
    }

    public static synchronized int total() { return total; }
    private SfxLog() {}
}

import java.io.InputStream;

/**
 * Bảng ánh xạ dữ liệu → SFX, nạp từ /sfx/map.txt (sinh bởi tools/gen_sfx_map.py, sửa tay được).
 *  role <roleId> <biến thể>
 *  skill <id> <lớp> <nguyên tố> <số nhịp>
 *  effect <tệp.av> <frame,...>        (frame va chạm của hiệu ứng; không cấp phát khi tra)
 */
public final class AnimSfxMap {
    public static final int MAXID = 64;
    static final int[] roleIds = new int[16], roleVar = new int[16];
    static int nRoles = 0;
    static final char[] sClass = new char[MAXID], sElem = new char[MAXID];
    static final boolean[] sMulti = new boolean[MAXID];
    static final String[] eName = new String[32];
    static final int[][] eFrames = new int[32][];
    static int nEff = 0;
    static boolean loaded = false;

    public static synchronized void load() {
        if (loaded) return;
        loaded = true;
        try {
            InputStream in = new AnimSfxMap().getClass().getResourceAsStream("/sfx/map.txt");
            if (in == null) return;
            StringBuffer sb = new StringBuffer();
            int c;
            while ((c = in.read()) >= 0) sb.append((char) c);
            in.close();
            byte[] raw = new byte[sb.length()];
            for (int i = 0; i < raw.length; i++) raw[i] = (byte) sb.charAt(i);
            String s = new String(raw, "UTF-8");
            int p = 0;
            while (p < s.length()) {
                int e = s.indexOf('\n', p);
                if (e < 0) e = s.length();
                String line = s.substring(p, e);
                p = e + 1;
                int h = line.indexOf('#');
                if (h >= 0) line = line.substring(0, h);
                line = line.trim();
                if (line.length() == 0) continue;
                String[] t = split(line);
                try {
                    if (t[0].equals("role") && nRoles < 16) { roleIds[nRoles] = Integer.parseInt(t[1]); roleVar[nRoles++] = Integer.parseInt(t[2]); }
                    else if (t[0].equals("skill")) {
                        int id = Integer.parseInt(t[1]);
                        if (id >= 0 && id < MAXID) { sClass[id] = t[2].charAt(0); sElem[id] = t[3].charAt(0); sMulti[id] = t[4].charAt(0) == 'M'; }
                    } else if (t[0].equals("effect") && nEff < 32) {
                        eName[nEff] = "/ft/" + t[1];
                        String[] fr = splitBy(t[2], ',');
                        int[] v = new int[fr.length];
                        for (int i = 0; i < v.length; i++) v[i] = Integer.parseInt(fr[i]);
                        eFrames[nEff++] = v;
                    }
                } catch (Throwable bad) { /* dòng hỏng: bỏ qua */ }
            }
        } catch (Throwable t) { }
    }

    private static String[] split(String s) { return splitBy(s, ' '); }
    private static String[] splitBy(String s, char sep) {
        int n = 1;
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == sep) n++;
        String[] r = new String[n];
        int k = 0, st = 0;
        for (int i = 0; i <= s.length(); i++) {
            if (i == s.length() || s.charAt(i) == sep) { r[k++] = s.substring(st, i); st = i + 1; }
        }
        // gộp các token rỗng do nhiều khoảng trắng
        int m = 0;
        for (int i = 0; i < n; i++) if (r[i].length() > 0) m++;
        if (m == n) return r;
        String[] q = new String[m];
        m = 0;
        for (int i = 0; i < n; i++) if (r[i].length() > 0) q[m++] = r[i];
        return q;
    }

    public static int variantOf(int role) {
        for (int i = 0; i < nRoles; i++) if (roleIds[i] == role) return roleVar[i];
        return 0;
    }
    /** 'P','E','H','B','D','X' hoặc 0 nếu skill không có trong bảng. */
    public static char skillClass(int id) { return id >= 0 && id < MAXID ? sClass[id] : 0; }
    public static char skillElem(int id) { return id >= 0 && id < MAXID ? sElem[id] : '-'; }
    /** Số nhịp kỳ vọng của một lần tung kỹ năng (skill nhiều nhịp: 2 + (cấp-1)/4 — theo bn.a(e,int,int)). */
    public static int beats(int id, int level) {
        if (id >= 0 && id < MAXID && sMulti[id]) { if (level < 1) level = 1; return 2 + (level - 1) / 4; }
        return 1;
    }
    public static boolean isMarker(String name, int frame) {
        for (int i = 0; i < nEff; i++) {
            if (eName[i].equals(name)) {
                int[] f = eFrames[i];
                for (int k = 0; k < f.length; k++) if (f[k] == frame) return true;
                return false;
            }
        }
        return false;
    }
    /** session 6: frame va chạm kế tiếp (> frame) của hiệu ứng, -1 nếu không còn (dùng để đặt tiếng sớm bù độ trễ thiết bị). */
    public static int nextMarker(String name, int frame) {
        for (int i = 0; i < nEff; i++) {
            if (eName[i].equals(name)) {
                int[] f = eFrames[i];
                int best = -1;
                for (int k = 0; k < f.length; k++) if (f[k] > frame && (best < 0 || f[k] < best)) best = f[k];
                return best;
            }
        }
        return -1;
    }
    public static boolean hasEffect(String name) {
        for (int i = 0; i < nEff; i++) if (eName[i].equals(name)) return true;
        return false;
    }
    public static int elemType(char el) {
        switch (el) { case 'F': return SFXType.FIRE; case 'W': return SFXType.WATER; case 'E': return SFXType.EARTH; case 'A': return SFXType.WIND; default: return -1; }
    }
    private AnimSfxMap() {}
}

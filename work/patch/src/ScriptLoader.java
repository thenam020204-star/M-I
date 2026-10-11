/**
 * Doc phan kich ban + bang chuoi cua file map (thay ScriptRunner.loadScript).
 * Ban goc: so chuoi u1 + do dai u1 -> chuoi dai > 255 byte bi doc lech (m/m/22.m da loi san).
 * Ban viet hoa: so chuoi u2 BE + do dai u2 BE; moi chuoi UTF-8 theo sau 1 byte 0.
 */
final class ScriptLoader {
    static int load(ScriptRunner self, byte[] data, int p) {
        ScriptRunner.reset();
        ScriptRunner.cursor = null;
        ScriptRunner.callStack = new ScriptCallStack(self);
        ScriptRunner.eventTable = new ScriptCodeTable(self);
        ScriptRunner.codeTable = new ScriptCodeTable(self);
        p = ScriptRunner.eventTable.a(data, p, 1);
        p = ScriptRunner.codeTable.a(data, p, 2);
        int n = ((data[p] & 0xFF) << 8) | (data[p + 1] & 0xFF);
        p += 2;
        ScriptRunner.strings = null;
        if (n > 0) {
            byte[][] s = new byte[n][];
            for (int i = 0; i < n; i++) {
                s[i] = new byte[((data[p] & 0xFF) << 8) | (data[p + 1] & 0xFF)];
                p += 2;
            }
            for (int i = 0; i < n; i++) {
                System.arraycopy(data, p, s[i], 0, s[i].length);
                p += s[i].length + 1;
            }
            ScriptRunner.strings = s;
        }
        ScriptRunner.callStack.depth = 0;
        return p;
    }
}

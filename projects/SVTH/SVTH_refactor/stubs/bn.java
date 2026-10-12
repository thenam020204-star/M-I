/** Stub biên dịch cho class game `bn` (trận chiến). KHÔNG đóng gói. */
public final class bn {
    public native e sfx$actor(int slot);
    public native int sfx$cur();
    public native int sfx$count();
    // --- chỉ có trong bản debug ---
    public native void dbg$plan(int slot, int cmd, int target, int skill, int level);
    public native void dbg$clearPlan(int slot);
    public native void dbg$go();
    public native int dbg$phase();
}

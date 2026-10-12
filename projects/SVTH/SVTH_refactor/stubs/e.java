/** Stub biên dịch cho class game `e` (nhân vật chiến đấu). Chỉ khai báo accessor do Patch.java chèn vào. KHÔNG đóng gói. */
public final class e {
    public native int sfx$slot();
    public native int sfx$role();
    public native int sfx$partyUid();   // session 7: e.a:S (petUid của party; 0 = nhân vật chính, ≥ 1 = sủng vật)
    public native int sfx$state();
    public native int sfx$target();
    public native boolean sfx$crit();
    public native boolean sfx$dodge();
    public native byte[] sfx$targets();
    public native bx sfx$bx();
    public native byte[] sfx$hitFrames();
    public native int sfx$hp();          // HP chờ áp (e.m:I)
    public native int sfx$hpReal();      // HP thật (a[3])
    public native int sfx$hpMax();       // HP tối đa (a[10])
    public native int sfx$mp();          // MP hiện tại (e.n:I)
    public native boolean sfx$dead();
    public native boolean sfx$visible();
    // --- chỉ có trong bản debug (DebugPatch) ---
    public native void dbg$setHp(int v);
    public native void dbg$setHpReal(int v);
    public native void dbg$setCrit(boolean v);
    public native void dbg$mpFull();
    public native boolean dbg$noSkills();
    public native void dbg$giveSkills();
    public native void dbg$takeSkills();
}

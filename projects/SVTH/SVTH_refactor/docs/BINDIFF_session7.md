# BINDIFF phiên 7 (1.7.0)

So `dist/*_renamed.jar` 1.6.0 → 1.7.0 (`tools/src/BinDiff.java`).

Kết luận: class game khác **duy nhất `BattleActor` (tên rối `e`) thêm một accessor `sfx$partyUid()I`** (đọc `partyUid`, không đổi logic game) — các class game khác giống hệt; khác còn lại: class SFX (`BeepBackend` viết lại, `BeepBackend$Inst`, `SFXManager`, `SfxHooks`, `SfxLog`), `DebugBattle` (bản debug: chấm ca buff, in giọng), `sfx/config.txt` (thêm `warm`, `prime`), MANIFEST. Wav không đổi.

## Release

```
entries: old=969 new=969 identical=961 changed=8 added=0 removed=0

## Entry thêm mới

## Entry bị xoá

## Entry khác nội dung
- BattleActor.class:
    + method sfx$partyUid()I
- BeepBackend$Inst.class:
    + field noWarm:Z
    + field owner:I
    + field st:I
    - field dirty:Z
    ~ method <init>()V (lệnh 27 → 31)
- BeepBackend.class:
    + field COLD:I
    + field DONE:I
    + field OP_ASSETS:I
    + field OP_COOL:I
    + field OP_CREATE_COLD:I
    + field OP_CREATE_WARM:I
    + field OP_NONE:I
    + field OP_WARM:I
    + field PLAYING:I
    + field POLL_MS:I
    + field READY:I
    + field TYPE_RANK:[I
    + field WORK:I
    + field assetsLoaded:Z
    + field coldStarts:I
    + field cooled:I
    + field kicked:Z
    + field lk:Ljava/lang/Object;
    + field loadAllowed:Z
    + field prime:Z
    + field primed:I
    + field reportedReady:Z
    + field seenSeq:I
    + field voiceSeen:[I
    + field want:[I
    + field warmMax:I
    + field workVolume:I
    + field worker:Ljava/lang/Thread;
    - field preloadDone:Z
    - field preloadKey:I
    - field preloadPhase:I
    - field wantGrow:[Z
    + method baseWant(I)I
    + method busyLocked(J)I
    + method closeLocked()V
    + method cool(LBeepBackend$Inst;)V
    + method inWarmSet(II)Z
    + method kick(I)V
    + method kickLocked()V
    + method make(I)LBeepBackend$Inst;
    + method play(IIII)I
    + method playLocked(IIII)I
    + method preloadDoneLocked()Z
    + method rank(II)I
    + method rewind(LBeepBackend$Inst;)V
    + method run()V
    + method setLoadAllowed(Z)V
    + method startWorker()V
    + method step(JI)J
    + method stopAllLocked()V
    + method touch(I)V
    + method warm(LBeepBackend$Inst;I)V
    + method warmCount()I
    + method warmCountLocked()I
    + method warmable(LBeepBackend$Inst;)Z
    - method hasPending()Z
    - method maintain(J)J
    - method make(II)LBeepBackend$Inst;
    - method preloadStep()Z
    - method target(I)I
    - method unstick(LBeepBackend$Inst;)V
    ~ method <clinit>()V (lệnh 146 → 191)
    ~ method <init>()V (lệnh 121 → 169)
    ~ method add(ILBeepBackend$Inst;)Z (lệnh 47 → 47)
    ~ method busy(J)I (lệnh 43 → 20)
    ~ method close()V (lệnh 92 → 21)
    ~ method limit()I (lệnh 19 → 19)
    ~ method play(III)I (lệnh 408 → 9)
    ~ method preload()V (lệnh 6 → 20)
    ~ method preloadDone()Z (lệnh 5 → 19)
    ~ method sounding(LBeepBackend$Inst;J)Z (lệnh 46 → 45)
    ~ method stopAll()V (lệnh 70 → 21)
- META-INF/MANIFEST.MF (không phải class)
- SFXManager.class:
    + field bgWork:Z
    - field preloading:Z
    + method background(LBeepBackend;J)V
    + method touchVoice(I)V
    - method bg(J)J
    ~ method <clinit>()V (lệnh 83 → 81)
    ~ method <init>()V (lệnh 12 → 17)
    ~ method init()V (lệnh 71 → 78)
    ~ method loadConfig()V (lệnh 343 → 373)
    ~ method post(IIIII)Z (lệnh 179 → 190)
    ~ method run()V (lệnh 445 → 397)
    ~ method stopAll()V (lệnh 24 → 47)
    ~ method wake(Z)V (lệnh 30 → 35)
- SfxHooks.class:
    + method fireImpact(IIIIIIZZI)V
    + method voiceOf(LBattleActor;)I
    - method fireImpact(IIIIIZZI)V
    ~ method fire(IIIIIIII)V (lệnh 35 → 35)
    ~ method fireMark(LBattleActor;III)V (lệnh 119 → 111)
    ~ method onBlow(LBattleActor;ZLBattle;)V (lệnh 141 → 143)
    ~ method onSkillApply(LBattle;LBattleActor;II)V (lệnh 99 → 98)
    ~ method onState(LBattleActor;ILBattle;)V (lệnh 291 → 295)
- SfxLog.class:
    ~ method format(I)Ljava/lang/String; (lệnh 148 → 154)
- sfx/config.txt (không phải class)
```

## Debug

```
entries: old=974 new=974 identical=965 changed=9 added=0 removed=0

## Entry thêm mới

## Entry bị xoá

## Entry khác nội dung
- BattleActor.class:
    + method sfx$partyUid()I
- BeepBackend$Inst.class:
    + field noWarm:Z
    + field owner:I
    + field st:I
    - field dirty:Z
    ~ method <init>()V (lệnh 27 → 31)
- BeepBackend.class:
    + field COLD:I
    + field DONE:I
    + field OP_ASSETS:I
    + field OP_COOL:I
    + field OP_CREATE_COLD:I
    + field OP_CREATE_WARM:I
    + field OP_NONE:I
    + field OP_WARM:I
    + field PLAYING:I
    + field POLL_MS:I
    + field READY:I
    + field TYPE_RANK:[I
    + field WORK:I
    + field assetsLoaded:Z
    + field coldStarts:I
    + field cooled:I
    + field kicked:Z
    + field lk:Ljava/lang/Object;
    + field loadAllowed:Z
    + field prime:Z
    + field primed:I
    + field reportedReady:Z
    + field seenSeq:I
    + field voiceSeen:[I
    + field want:[I
    + field warmMax:I
    + field workVolume:I
    + field worker:Ljava/lang/Thread;
    - field preloadDone:Z
    - field preloadKey:I
    - field preloadPhase:I
    - field wantGrow:[Z
    + method baseWant(I)I
    + method busyLocked(J)I
    + method closeLocked()V
    + method cool(LBeepBackend$Inst;)V
    + method inWarmSet(II)Z
    + method kick(I)V
    + method kickLocked()V
    + method make(I)LBeepBackend$Inst;
    + method play(IIII)I
    + method playLocked(IIII)I
    + method preloadDoneLocked()Z
    + method rank(II)I
    + method rewind(LBeepBackend$Inst;)V
    + method run()V
    + method setLoadAllowed(Z)V
    + method startWorker()V
    + method step(JI)J
    + method stopAllLocked()V
    + method touch(I)V
    + method warm(LBeepBackend$Inst;I)V
    + method warmCount()I
    + method warmCountLocked()I
    + method warmable(LBeepBackend$Inst;)Z
    - method hasPending()Z
    - method maintain(J)J
    - method make(II)LBeepBackend$Inst;
    - method preloadStep()Z
    - method target(I)I
    - method unstick(LBeepBackend$Inst;)V
    ~ method <clinit>()V (lệnh 146 → 191)
    ~ method <init>()V (lệnh 121 → 169)
    ~ method add(ILBeepBackend$Inst;)Z (lệnh 47 → 47)
    ~ method busy(J)I (lệnh 43 → 20)
    ~ method close()V (lệnh 92 → 21)
    ~ method limit()I (lệnh 19 → 19)
    ~ method play(III)I (lệnh 408 → 9)
    ~ method preload()V (lệnh 6 → 20)
    ~ method preloadDone()Z (lệnh 5 → 19)
    ~ method sounding(LBeepBackend$Inst;J)Z (lệnh 46 → 45)
    ~ method stopAll()V (lệnh 70 → 21)
- DebugBattle.class:
    ~ method evaluate(LBattle;)V (lệnh 716 → 746)
    ~ method startCase(LBattle;I)V (lệnh 427 → 437)
- META-INF/MANIFEST.MF (không phải class)
- SFXManager.class:
    + field bgWork:Z
    - field preloading:Z
    + method background(LBeepBackend;J)V
    + method touchVoice(I)V
    - method bg(J)J
    ~ method <clinit>()V (lệnh 83 → 81)
    ~ method <init>()V (lệnh 12 → 17)
    ~ method init()V (lệnh 71 → 78)
    ~ method loadConfig()V (lệnh 343 → 373)
    ~ method post(IIIII)Z (lệnh 179 → 190)
    ~ method run()V (lệnh 445 → 397)
    ~ method stopAll()V (lệnh 24 → 47)
    ~ method wake(Z)V (lệnh 30 → 35)
- SfxHooks.class:
    + method fireImpact(IIIIIIZZI)V
    + method voiceOf(LBattleActor;)I
    - method fireImpact(IIIIIZZI)V
    ~ method fire(IIIIIIII)V (lệnh 35 → 35)
    ~ method fireMark(LBattleActor;III)V (lệnh 119 → 111)
    ~ method onBlow(LBattleActor;ZLBattle;)V (lệnh 141 → 143)
    ~ method onSkillApply(LBattle;LBattleActor;II)V (lệnh 99 → 98)
    ~ method onState(LBattleActor;ILBattle;)V (lệnh 291 → 295)
- SfxLog.class:
    ~ method format(I)Ljava/lang/String; (lệnh 148 → 154)
- sfx/config.txt (không phải class)
```

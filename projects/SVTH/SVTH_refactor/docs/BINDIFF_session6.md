# BINDIFF phiên 6 (1.6.0)

Đường: bản người dùng gửi `SVTH_SFX_renamed.jar` (1.5.0 đã đặt tên + 27 wav của họ) → `dist/SVTH_SFX_renamed.jar` 1.6.0. Sinh bằng `tools/src/BinDiff.java`.

Kết luận: **mọi class của game giống hệt**; khác chỉ ở 6 class SFX (`AnimSfxMap`, `BeepBackend`, `BeepBackend$Inst`, `SFXManager`, `SfxHooks`, `SfxUi`), `META-INF/MANIFEST.MF` (phiên bản) và tệp mới `sfx/config.txt`. 27 wav giống hệt bản người dùng.

## Release: SVTH_SFX_renamed.jar (người dùng, 1.5.0) → 1.6.0

```
entries: old=968 new=969 identical=961 changed=7 added=1 removed=0

## Entry thêm mới
- sfx/config.txt (1141 B)

## Entry bị xoá

## Entry khác nội dung
- AnimSfxMap.class:
    + method nextMarker(Ljava/lang/String;I)I
- BeepBackend$Inst.class:
    + field startedAt:J
    ~ method <init>()V (lệnh 22 → 27)
- BeepBackend.class:
    + field STUCK_MS:I
    + field gapMs:I
    + field lastStart:[J
    + field preloadDone:Z
    + field preloadKey:I
    + field preloadPhase:I
    + field skipped:I
    + field solo:Z
    + field trimMs:I
    + field trimmed:[I
    + field unstuck:I
    + method isSolo(I)Z
    + method preloadDone()Z
    + method preloadStep()Z
    + method sounding(LBeepBackend$Inst;J)Z
    + method target(I)I
    + method trimLead([BI)[B
    + method trimmedMs(II)I
    + method unstick(LBeepBackend$Inst;)V
    ~ method <clinit>()V (lệnh 134 → 146)
    ~ method <init>()V (lệnh 88 → 121)
    ~ method close()V (lệnh 81 → 92)
    ~ method load(II)V (lệnh 207 → 228)
    ~ method maintain(J)J (lệnh 162 → 157)
    ~ method play(III)I (lệnh 338 → 408)
    ~ method preload()V (lệnh 59 → 6)
    ~ method stopAll()V (lệnh 67 → 70)
- META-INF/MANIFEST.MF (không phải class)
- SFXManager.class:
    + field lead:I
    + field preloading:Z
    ~ method <clinit>()V (lệnh 77 → 83)
    ~ method loadConfig()V (lệnh 277 → 343)
    ~ method run()V (lệnh 412 → 445)
- SfxHooks.class:
    + field frameMs:I
    + field preFired:I
    + field preKey:I
    + field trBx:LAnimSprite;
    + field trFr:I
    + field trMsAt:J
    + method fireImpact(IIIIIZZI)V
    + method fireMark(LBattleActor;III)V
    + method preFire(LAnimSprite;LBattleActor;ILjava/lang/String;I)V
    - method fireImpact(IIIIIZZ)V
    ~ method <clinit>()V (lệnh 111 → 131)
    ~ method battleEnd(I)V (lệnh 45 → 47)
    ~ method battleStart(LBattle;)V (lệnh 67 → 71)
    ~ method onBlow(LBattleActor;ZLBattle;)V (lệnh 140 → 141)
    ~ method onFrame(LAnimSprite;)V (lệnh 240 → 161)
    ~ method onState(LBattleActor;ILBattle;)V (lệnh 287 → 291)
- SfxUi.class:
    ~ method skipPrompt()Z (lệnh 17 → 24)
```

## Debug: SVTH_SFX_debug_renamed.jar 1.5.0 (wav im lặng) → 1.6.0

27 wav đổi (nhận từ bản người dùng) + cùng các class SFX như release + MANIFEST + `sfx/config.txt`; class debug (`DebugBattle`) và class game giống hệt.

```
entries: old=973 new=974 identical=939 changed=34 added=1 removed=0

## Entry thêm mới
- sfx/config.txt (1141 B)

## Entry bị xoá

## Entry khác nội dung
- AnimSfxMap.class:
    + method nextMarker(Ljava/lang/String;I)I
- BeepBackend$Inst.class:
    + field startedAt:J
    ~ method <init>()V (lệnh 22 → 27)
- BeepBackend.class:
    + field STUCK_MS:I
    + field gapMs:I
    + field lastStart:[J
    + field preloadDone:Z
    + field preloadKey:I
    + field preloadPhase:I
    + field skipped:I
    + field solo:Z
    + field trimMs:I
    + field trimmed:[I
    + field unstuck:I
    + method isSolo(I)Z
    + method preloadDone()Z
    + method preloadStep()Z
    + method sounding(LBeepBackend$Inst;J)Z
    + method target(I)I
    + method trimLead([BI)[B
    + method trimmedMs(II)I
    + method unstick(LBeepBackend$Inst;)V
    ~ method <clinit>()V (lệnh 134 → 146)
    ~ method <init>()V (lệnh 88 → 121)
    ~ method close()V (lệnh 81 → 92)
    ~ method load(II)V (lệnh 207 → 228)
    ~ method maintain(J)J (lệnh 162 → 157)
    ~ method play(III)I (lệnh 338 → 408)
    ~ method preload()V (lệnh 59 → 6)
    ~ method stopAll()V (lệnh 67 → 70)
- META-INF/MANIFEST.MF (không phải class)
- SFXManager.class:
    + field lead:I
    + field preloading:Z
    ~ method <clinit>()V (lệnh 77 → 83)
    ~ method loadConfig()V (lệnh 277 → 343)
    ~ method run()V (lệnh 412 → 445)
- SfxHooks.class:
    + field frameMs:I
    + field preFired:I
    + field preKey:I
    + field trBx:LAnimSprite;
    + field trFr:I
    + field trMsAt:J
    + method fireImpact(IIIIIZZI)V
    + method fireMark(LBattleActor;III)V
    + method preFire(LAnimSprite;LBattleActor;ILjava/lang/String;I)V
    - method fireImpact(IIIIIZZ)V
    ~ method <clinit>()V (lệnh 111 → 131)
    ~ method battleEnd(I)V (lệnh 45 → 47)
    ~ method battleStart(LBattle;)V (lệnh 67 → 71)
    ~ method onBlow(LBattleActor;ZLBattle;)V (lệnh 140 → 141)
    ~ method onFrame(LAnimSprite;)V (lệnh 240 → 161)
    ~ method onState(LBattleActor;ILBattle;)V (lệnh 287 → 291)
- SfxUi.class:
    ~ method skipPrompt()Z (lệnh 17 → 24)
```

## Bản tên rối 1.6.0 so với JAR gốc của game

```
entries: old=928 new=969 identical=919 changed=9 added=41 removed=0

## Entry thêm mới
- AnimSfxMap.class (4495 B)
- BeepBackend$Inst.class (620 B)
- BeepBackend.class (13536 B)
- BgmDuck.class (1804 B)
- BuildFlags.class (243 B)
- SFXManager.class (9753 B)
- SFXType.class (986 B)
- SfxHooks$EndSink.class (183 B)
- SfxHooks$SummarySink.class (200 B)
- SfxHooks.class (8148 B)
- SfxLog.class (3523 B)
- SfxUi.class (2955 B)
- sfx/CRIT_0.wav (11120 B)
- sfx/CRIT_1.wav (11120 B)
- sfx/CRIT_2.wav (11120 B)
- sfx/EARTH_0.wav (23672 B)
- sfx/EARTH_1.wav (23672 B)
- sfx/EARTH_2.wav (23672 B)
- sfx/FIRE_0.wav (9644 B)
- sfx/FIRE_1.wav (9644 B)
- sfx/FIRE_2.wav (9644 B)
- sfx/HEAL_0.wav (34648 B)
- sfx/HEAL_1.wav (34648 B)
- sfx/HEAL_2.wav (34648 B)
- sfx/HIT_0.wav (3360 B)
- sfx/HIT_1.wav (8860 B)
- sfx/HIT_2.wav (13980 B)
- sfx/MISS_0.wav (7104 B)
- sfx/MISS_1.wav (7104 B)
- sfx/MISS_2.wav (7104 B)
- sfx/SKILL_0.wav (12768 B)
- sfx/SKILL_1.wav (16800 B)
- sfx/SKILL_2.wav (12400 B)
- sfx/WATER_0.wav (8908 B)
- sfx/WATER_1.wav (8908 B)
- sfx/WATER_2.wav (8908 B)
- sfx/WIND_0.wav (5216 B)
…
```

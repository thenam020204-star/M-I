# BINDIFF phiên 8 — 1.7.0 → 1.8.0

Công cụ: `tools/src/BinDiff.java` (so entry theo md5; class so theo field/method và chuỗi lệnh bytecode, bỏ StackMap).
Quyền truy cập field không nằm trong BinDiff — kiểm bằng `javap -p`: `Battle.actionPlans [[I`, `actingSlot I`, `enemyCount B` đổi `private` → package (chủ đích, Patch8).

## Bản phát hành: `base/SVTH_SFX_renamed_1.7.0_user.jar` (res.jar người dùng gửi) → `dist/SVTH_SFX_renamed.jar`

```
entries: old=970 new=972 identical=968 changed=2 added=2 removed=0

## Entry thêm mới
- EnemyAI.class (25605 B)
- ai/config.txt (2205 B)

## Entry bị xoá

## Entry khác nội dung
- Battle.class:
    ~ method createEnemyActors()V (lệnh 1204 → 1209)
    ~ method tick()V (lệnh 4325 → 4333)
- META-INF/MANIFEST.MF (không phải class)
```

Lệnh thêm: `createEnemyActors` +5 (aload_0, iload_2, invokestatic adjustLevel; aload_0, invokestatic onEnemiesCreated); `tick` +8 (planRound 2 + onTurn 3×2). Không lệnh gốc nào bị đổi/xoá.

## Bản debug: `base/SVTH_SFX_debug_renamed_1.7.0.jar` (dist 1.7.0) → `dist/SVTH_SFX_debug_renamed.jar`

```
entries: old=974 new=977 identical=971 changed=3 added=3 removed=0

## Entry thêm mới
- EnemyAI.class (25605 B)
- ai/config.txt (2205 B)
- f18.bin (3018 B)

## Entry bị xoá

## Entry khác nội dung
- Battle.class:
    ~ method createEnemyActors()V (lệnh 1204 → 1209)
    ~ method tick()V (lệnh 4369 → 4377)
- META-INF/MANIFEST.MF (không phải class)
- font.bin (không phải class)
```

`font.bin` (+ `f18.bin` thêm) khác vì bản debug 1.7.0 chưa có chỉnh sửa font của người dùng — phiên 8 đưa vào `res/` nên cả hai bản giờ cùng font.

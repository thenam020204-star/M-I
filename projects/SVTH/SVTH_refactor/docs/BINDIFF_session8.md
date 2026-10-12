# BINDIFF phiên 8 — 1.7.0 → 1.8.0

Công cụ: `tools/src/BinDiff.java` (so entry theo md5; class so theo field/method và chuỗi lệnh bytecode, bỏ StackMap).
Quyền truy cập field không nằm trong BinDiff — kiểm bằng `javap -p`: `Battle.actionPlans [[I`, `actingSlot I`, `enemyCount B`, `commandSlot I`, `targetCursor I` đổi `private` → package (chủ đích, Patch8).

## Bản phát hành: `base/SVTH_SFX_renamed_1.7.0_user.jar` (res.jar người dùng gửi) → `dist/SVTH_SFX_renamed.jar`

```
entries: old=970 new=972 identical=968 changed=2 added=2 removed=0

## Entry thêm mới
- EnemyAI.class (32414 B)
- ai/config.txt (2760 B)

## Entry bị xoá

## Entry khác nội dung
- Battle.class:
    + method ai$confirm()V
    ~ method createEnemyActors()V (lệnh 1204 → 1209)
    ~ method handleKey(I)Z (lệnh 2768 → 2770)
    ~ method tick()V (lệnh 4325 → 4335)
- META-INF/MANIFEST.MF (không phải class)
```

Lệnh thêm (không lệnh gốc nào bị đổi/xoá):
* `createEnemyActors` +5: `aload_0, iload_2, invokestatic adjustLevel` trước `GameUtil.a` đầu tiên; `aload_0, invokestatic onEnemiesCreated` trước `return`.
* `tick` +10: `petGate` ở đầu (2), `planRound` ở lối ra vòng lập kế hoạch quái pha 2 (2), `onTurn` sau 3 `putfield actingSlot` (6).
* `handleKey` +2: `petGate` ở đầu.
* `ai$confirm()V` mới: `aload_0; invokespecial confirmCommand()V; return`.

## Bản debug: `base/SVTH_SFX_debug_renamed_1.7.0.jar` (dist 1.7.0) → `dist/SVTH_SFX_debug_renamed.jar`

```
entries: old=974 new=977 identical=971 changed=3 added=3 removed=0

## Entry thêm mới
- EnemyAI.class (32414 B)
- ai/config.txt (2760 B)
- f18.bin (3018 B)

## Entry bị xoá

## Entry khác nội dung
- Battle.class:
    + method ai$confirm()V
    ~ method createEnemyActors()V (lệnh 1204 → 1209)
    ~ method handleKey(I)Z (lệnh 2768 → 2770)
    ~ method tick()V (lệnh 4369 → 4379)
- META-INF/MANIFEST.MF (không phải class)
- font.bin (không phải class)
```

`font.bin` (+ `f18.bin` thêm) khác vì bản debug 1.7.0 chưa có chỉnh sửa font của người dùng — phiên 8 đưa vào `res/` nên cả hai bản giờ cùng font.

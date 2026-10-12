# BINDIFF — phiên 1 (JAR gốc → JAR mới)

Công cụ: `tools/src/BinDiff.java` (so từng entry; với class đổi thì so field/method và **chuỗi lệnh bytecode** đã bỏ StackMap/nhãn/số dòng,
"lệnh a → b" là số lệnh trước → sau). JAR gốc: `d6030039-S_ng_V_t_Thi_n_H__-___i_L_c_Th_n__i_u.jar` (928 entry).

## Kết luận

* **922/928 entry giống hệt từng byte** (toàn bộ ảnh, `.av`, `bin/*.txt`, `music/*.mid`, 108 class không đụng).
* Entry khác nội dung có chủ đích: `MANIFEST.MF` (đổi `MIDlet-Version` → 1.1.0; bản debug thêm `SFX-Debug: 1`) và 4 class game bị hook
  (`bn`, `bx`, `da`, `e`) — **bản debug** còn `cd`, `ci`.
* `fontcus.class` đổi byte nhưng **không đổi field/method/lệnh**: JAR gốc không có StackMap (class 47 chưa preverify);
  ProGuard `-microedition` bổ sung 13 bản ghi StackMap. Không phải thay đổi logic.
* Không xoá entry nào. Không đổi chữ ký (descriptor) method/field có sẵn; chỉ **thêm** field `bx.sfx$name`, `bx.sfx$last` và method `sfx$*` / `dbg$*`.
* Số lệnh tăng ở method bị hook = số lệnh chèn: mỗi hook là `DUP2 [ALOAD_0] INVOKESTATIC` đặt trước lệnh gốc, hoặc `INVOKESTATIC`/`IFEQ-RETURN` ở đầu hàm.
  Release 62 site hook (`Patch`: 4 `putfield e.g:Z`, 44 `putfield e.f:I`, `bn.a(e,int,int)`, `bn.c/h/e/g/b`, `bx.a(Z)` x2, `bx.a/b(I)`, 3 hàm nạp `.av`, `da.hideNotify`);
  debug 92 site.

## Bản release (`dist/SVTH_SFX.jar`)

```
entries: old=928 new=938 identical=922 changed=6 added=10 removed=0

## Entry thêm mới
- AnimSfxMap.class (4209 B)
- BeepBackend.class (5381 B)
- BuildFlags.class (243 B)
- SFXManager.class (6817 B)
- SFXType.class (986 B)
- SfxHooks$EndSink.class (183 B)
- SfxHooks$SummarySink.class (200 B)
- SfxHooks.class (7089 B)
- SfxLog.class (3523 B)
- sfx/map.txt (3716 B)

## Entry bị xoá

## Entry khác nội dung
- META-INF/MANIFEST.MF (không phải class)
- bn.class:
    + method sfx$actor(I)Le;
    + method sfx$count()I
    + method sfx$cur()I
    ~ method a()V (lệnh 49 → 52)
    ~ method a(Le;II)V (lệnh 2483 → 2488)
    ~ method a(Ljavax/microedition/lcdui/Graphics;)V (lệnh 4229 → 4250)
    ~ method b()V (lệnh 1047 → 1049)
    ~ method c()V (lệnh 7 → 9)
    ~ method e()V (lệnh 84 → 86)
    ~ method f()V (lệnh 4214 → 4325)
    ~ method g()V (lệnh 1317 → 1325)
    ~ method h()V (lệnh 9 → 11)
    ~ method v()V (lệnh 149 → 152)
- bx.class:
    + field sfx$last:I
    + field sfx$name:Ljava/lang/String;
    + method sfx$anim()I
    + method sfx$frame()I
    + method sfx$frames(I)I
    + method sfx$last()I
    + method sfx$name()Ljava/lang/String;
    + method sfx$setLast(I)V
    ~ method a(I)V (lệnh 42 → 45)
    ~ method a(Ljava/lang/String;)Z (lệnh 26 → 29)
    ~ method a(Ljava/lang/String;I)Z (lệnh 28 → 31)
    ~ method a(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[I[II)Z (lệnh 87 → 90)
    ~ method a(Z)V (lệnh 25 → 29)
    ~ method b(I)V (lệnh 14 → 17)
- da.class:
    ~ method hideNotify()V (lệnh 21 → 22)
- e.class:
    + method sfx$bx()Lbx;
    + method sfx$crit()Z
    + method sfx$dead()Z
    + method sfx$dodge()Z
    + method sfx$hitFrames()[B
    + method sfx$hp()I
    + method sfx$hpMax()I
    + method sfx$hpReal()I
    + method sfx$mp()I
    + method sfx$role()I
    + method sfx$slot()I
    + method sfx$state()I
    + method sfx$target()I
    + method sfx$targets()[B
    + method sfx$visible()Z
- fontcus.class:
```

## Bản debug (`dist/SVTH_SFX_debug.jar`)

```
entries: old=928 new=943 identical=920 changed=8 added=15 removed=0

## Entry thêm mới
- AnimSfxMap.class (4209 B)
- BeepBackend.class (5381 B)
- BuildFlags.class (243 B)
- DebugBattle$EndWatch.class (975 B)
- DebugBattle$Sink.class (560 B)
- DebugBattle$Starter.class (2042 B)
- DebugBattle$Watchdog.class (1617 B)
- DebugBattle.class (24391 B)
- SFXManager.class (6817 B)
- SFXType.class (986 B)
- SfxHooks$EndSink.class (183 B)
- SfxHooks$SummarySink.class (200 B)
- SfxHooks.class (7089 B)
- SfxLog.class (3482 B)
- sfx/map.txt (3716 B)

## Entry bị xoá

## Entry khác nội dung
- META-INF/MANIFEST.MF (không phải class)
- bn.class:
    + method dbg$clearPlan(I)V
    + method dbg$go()V
    + method dbg$phase()I
    + method dbg$plan(IIIII)V
    + method sfx$actor(I)Le;
    + method sfx$count()I
    + method sfx$cur()I
    ~ method a()V (lệnh 49 → 52)
    ~ method a(Le;II)V (lệnh 2483 → 2488)
    ~ method a(Le;Le;I)Z (lệnh 45 → 46)
    ~ method a(Ljavax/microedition/lcdui/Graphics;)V (lệnh 4229 → 4252)
    ~ method b()V (lệnh 1047 → 1049)
    ~ method c()V (lệnh 7 → 9)
    ~ method e()V (lệnh 84 → 86)
    ~ method f()V (lệnh 4214 → 4369)
    ~ method g()V (lệnh 1317 → 1325)
    ~ method h()V (lệnh 9 → 11)
    ~ method v()V (lệnh 149 → 152)
- bx.class:
    + field sfx$last:I
    + field sfx$name:Ljava/lang/String;
    + method sfx$anim()I
    + method sfx$frame()I
    + method sfx$frames(I)I
    + method sfx$last()I
    + method sfx$name()Ljava/lang/String;
    + method sfx$setLast(I)V
    ~ method a(I)V (lệnh 42 → 45)
    ~ method a(Ljava/lang/String;)Z (lệnh 26 → 29)
    ~ method a(Ljava/lang/String;I)Z (lệnh 28 → 31)
    ~ method a(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[I[II)Z (lệnh 87 → 90)
    ~ method a(Z)V (lệnh 25 → 29)
    ~ method b(I)V (lệnh 14 → 17)
- cd.class:
    + method dbg$battle([Ljava/lang/String;)V
    + method dbg$bn()Lbn;
    + method dbg$game()Lcd;
    + method dbg$join(I)V
    + method dbg$state()I
    ~ method a([S[S[S)V (lệnh 71 → 75)
    ~ method e()V (lệnh 86 → 88)
    ~ method l()V (lệnh 212 → 214)
- ci.class:
    ~ method a()V (lệnh 134 → 138)
- da.class:
    ~ method hideNotify()V (lệnh 21 → 22)
    ~ method keyPressed(I)V (lệnh 264 → 269)
    ~ method paint(Ljavax/microedition/lcdui/Graphics;)V (lệnh 426 → 428)
- e.class:
    + method dbg$giveSkills()V
    + method dbg$mpFull()V
    + method dbg$noSkills()Z
    + method dbg$setCrit(Z)V
    + method dbg$setHp(I)V
    + method dbg$setHpReal(I)V
    + method dbg$takeSkills()V
    + method sfx$bx()Lbx;
    + method sfx$crit()Z
    + method sfx$dead()Z
    + method sfx$dodge()Z
    + method sfx$hitFrames()[B
    + method sfx$hp()I
    + method sfx$hpMax()I
    + method sfx$hpReal()I
    + method sfx$mp()I
    + method sfx$role()I
    + method sfx$slot()I
    + method sfx$state()I
    + method sfx$target()I
    + method sfx$targets()[B
    + method sfx$visible()Z
- fontcus.class:
```

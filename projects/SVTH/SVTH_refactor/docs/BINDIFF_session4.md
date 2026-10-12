# BINDIFF — phiên 4 (JAR gốc → JAR 1.4.0)

Công cụ: `tools/src/BinDiff.java`. So với JAR gốc (928 entry).

## Kết luận

* **919/928 entry giống hệt từng byte** (release) — giống phiên 3. Class game đổi vẫn là `bn bx da e av as ab` (+ `cd ci` ở debug), `MANIFEST.MF`, `fontcus` (chỉ thêm StackMap). **`at.class`, `o.class`, `bv.class`, `Main.class` không đổi.**
* Phiên 4 **không thêm hook mới vào class game** (75 site release / 105 debug như 1.3.0). Việc hạ nhạc nền chỉ *gọi* `Main.music.b(int)` (public, đã có) và đọc `o.g`/`at.b` từ class mới `BgmDuck`; stub `stubs/bv.java`, `Main.java` chỉ để biên dịch.
* Entry thêm: +1 class so với 1.3.0 (`BgmDuck`); release 40 entry thêm (class SFX + `SfxUi` + `BgmDuck` + `sfx/map.txt` + 27 wav), debug 45.
* Class SFX đổi nội dung so với 1.3.0: `BeepBackend` (bể Player), `SFXManager` (vòng chạy mới, config), `SfxHooks` (đồng thời CRIT/HEAL, `stopAll` khi tạm dừng), `SfxUi`.

## Bản release (`dist/SVTH_SFX.jar`, 1 540 986 B)

```
entries: old=928 new=968 identical=919 changed=9 added=40 removed=0

## Entry thêm mới
- AnimSfxMap.class (4209 B)
- BeepBackend$Inst.class (581 B)
- BeepBackend.class (10437 B)
- BgmDuck.class (1804 B)
- BuildFlags.class (243 B)
- SFXManager.class (9075 B)
- SFXType.class (986 B)
- SfxHooks$EndSink.class (183 B)
- SfxHooks$SummarySink.class (200 B)
- SfxHooks.class (7155 B)
- SfxLog.class (3523 B)
- SfxUi.class (2921 B)
- sfx/CRIT_0.wav (444 B)
- sfx/CRIT_1.wav (444 B)
- sfx/CRIT_2.wav (444 B)
- sfx/EARTH_0.wav (444 B)
- sfx/EARTH_1.wav (444 B)
- sfx/EARTH_2.wav (444 B)
- sfx/FIRE_0.wav (444 B)
- sfx/FIRE_1.wav (444 B)
- sfx/FIRE_2.wav (444 B)
- sfx/HEAL_0.wav (444 B)
- sfx/HEAL_1.wav (444 B)
- sfx/HEAL_2.wav (444 B)
- sfx/HIT_0.wav (444 B)
- sfx/HIT_1.wav (444 B)
- sfx/HIT_2.wav (444 B)
- sfx/MISS_0.wav (444 B)
- sfx/MISS_1.wav (444 B)
- sfx/MISS_2.wav (444 B)
- sfx/SKILL_0.wav (444 B)
- sfx/SKILL_1.wav (444 B)
- sfx/SKILL_2.wav (444 B)
- sfx/WATER_0.wav (444 B)
- sfx/WATER_1.wav (444 B)
- sfx/WATER_2.wav (444 B)
- sfx/WIND_0.wav (444 B)
- sfx/WIND_1.wav (444 B)
- sfx/WIND_2.wav (444 B)
- sfx/map.txt (3716 B)

## Entry bị xoá

## Entry khác nội dung
- META-INF/MANIFEST.MF (không phải class)
- ab.class:
    ~ method h(I)V (lệnh 642 → 647)
- as.class:
    + method sfx$icon(I)V
    ~ method a(BB)V (lệnh 1046 → 1050)
    ~ method h(Ljavax/microedition/lcdui/Graphics;)V (lệnh 1151 → 1157)
    ~ method p()V (lệnh 202 → 204)
- av.class:
    ~ method a(Ljavax/microedition/lcdui/Graphics;)V (lệnh 503 → 503)
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

## Bản debug (`dist/SVTH_SFX_debug.jar`, 1 558 319 B)

```
entries: old=928 new=973 identical=917 changed=11 added=45 removed=0

## Entry thêm mới
- AnimSfxMap.class (4209 B)
- BeepBackend$Inst.class (581 B)
- BeepBackend.class (10437 B)
- BgmDuck.class (1804 B)
- BuildFlags.class (243 B)
- DebugBattle$EndWatch.class (975 B)
- DebugBattle$Sink.class (560 B)
- DebugBattle$Starter.class (2042 B)
- DebugBattle$Watchdog.class (1617 B)
- DebugBattle.class (24390 B)
- SFXManager.class (9075 B)
- SFXType.class (986 B)
- SfxHooks$EndSink.class (183 B)
- SfxHooks$SummarySink.class (200 B)
- SfxHooks.class (7155 B)
- SfxLog.class (3482 B)
- SfxUi.class (2921 B)
- sfx/CRIT_0.wav (444 B)
- sfx/CRIT_1.wav (444 B)
- sfx/CRIT_2.wav (444 B)
- sfx/EARTH_0.wav (444 B)
- sfx/EARTH_1.wav (444 B)
- sfx/EARTH_2.wav (444 B)
- sfx/FIRE_0.wav (444 B)
- sfx/FIRE_1.wav (444 B)
- sfx/FIRE_2.wav (444 B)
- sfx/HEAL_0.wav (444 B)
- sfx/HEAL_1.wav (444 B)
- sfx/HEAL_2.wav (444 B)
- sfx/HIT_0.wav (444 B)
- sfx/HIT_1.wav (444 B)
- sfx/HIT_2.wav (444 B)
- sfx/MISS_0.wav (444 B)
- sfx/MISS_1.wav (444 B)
- sfx/MISS_2.wav (444 B)
- sfx/SKILL_0.wav (444 B)
- sfx/SKILL_1.wav (444 B)
- sfx/SKILL_2.wav (444 B)
- sfx/WATER_0.wav (444 B)
- sfx/WATER_1.wav (444 B)
- sfx/WATER_2.wav (444 B)
- sfx/WIND_0.wav (444 B)
- sfx/WIND_1.wav (444 B)
- sfx/WIND_2.wav (444 B)
- sfx/map.txt (3716 B)

## Entry bị xoá

## Entry khác nội dung
- META-INF/MANIFEST.MF (không phải class)
- ab.class:
    ~ method h(I)V (lệnh 642 → 647)
- as.class:
    + method sfx$icon(I)V
    ~ method a(BB)V (lệnh 1046 → 1050)
    ~ method h(Ljavax/microedition/lcdui/Graphics;)V (lệnh 1151 → 1157)
    ~ method p()V (lệnh 202 → 204)
- av.class:
    ~ method a(Ljavax/microedition/lcdui/Graphics;)V (lệnh 503 → 503)
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

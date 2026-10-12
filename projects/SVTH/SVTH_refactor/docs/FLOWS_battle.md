# FLOWS_battle — luồng trận đánh (đã xác minh bằng bytecode + trace chạy thật)

Tên class là tên bị làm rối của game gốc. Tất cả trạng thái dưới đây đo bằng trace `SFXTEST TRACE` trong
MicroEmulator, đối chiếu với mã giải mã (Vineflower) ở `bn.f()`.

## 1. Vào trận

```
Main (MIDlet) → da (Canvas, tick/vẽ) → cd (trạng thái game, da.a.a)
cd.a(short[],short[],short[])   ← script cốt truyện ci.a() gọi lệnh "战斗" (đánh nhau)
   └ cd.b(String[],0)  tạo bn (trận), bn.c() = khởi tạo (hook battleStart)
bn.f() mỗi tick: pha bn.a:I  0 chọn lệnh ... 2 thực thi ... 3 chuyển lượt ... 5 ...
```

* Slot actor: 0 = Quinn, 1 = Julie (đồng minh, 0–9); 10–19 = quái địch (`bn.a[]` kiểu `e[]`).
* Kế hoạch lượt: `bn.e[slot][1..4] = {lệnh, mục tiêu, id kỹ năng, cấp}`; lệnh 1 = đánh thường, 4 = tung kỹ năng.
  Thứ tự lượt `bn.a[d][0]` theo tốc độ, `bn.j` = slot đang hành động, `bn.d` = chỉ số trong thứ tự.
* `bn.s()` chuyển sang thực thi (pha 2) khi người chơi chọn xong lệnh. Debug Battle gọi `dbg$go()` làm đúng
  việc đó (pha=2, i=0, f=0), nên dùng nguyên hệ thống chiến đấu thật.

## 2. Đòn thường (lệnh 1)

```
0 --(chạy tới)--> 6 → 14 → 1 (anim tấn công) → 2 → 15 (quay về) → 0
```

* Vào 1 (hoặc 13 với pet): mở chuỗi (`atkSeq++`, tính số nhịp kỳ vọng).
* Trong trạng thái 1: mỗi lần `e.g` bật = một nhịp. Hit/miss tính ở `bn.a(e,e,I)Z` (xúc xắc né), chí mạng ở `e.m`.
* Rời 1 → ghi `SUMMARY(kỳ vọng, thực tế)` vào `SfxLog`.
* Đo: Quinn 3 nhịp (frame 7, 12, cuối đòn), Julie 1 nhịp (xem `SFX_MAP.md` mục 4.1).

## 3. Kỹ năng (lệnh 4)

```
bn.a(e,id,lv)   dựng mảng bx[] hiệu ứng lên mục tiêu; trừ MP; (hook onSkillApply → SKILL)
0 → 5 (pose tung) ──┬─ nhắm địch:            10 (bx hiệu ứng chạy trên từng mục tiêu) → 0
                    └─ nhắm bản thân/đồng minh: 17 (bx chạy trên đồng minh) → 0
```

* `d.b[]`: 1 = nhắm phe mình, 2 = nhắm phe địch; `d.c[]`: 1 = đơn mục tiêu.
* Trong 10/17, `bx.a(boolean)` tiến frame 1 lần mỗi 3 tick → hook `onFrame`; frame trùng marker
  (`map.txt effect`) = một nhịp. Kỹ năng 4 lặp lại 10 nhiều lần (mỗi nhịp một lần vào 10).
* Mã EXP kỹ năng (`bn.f()` ~ sau khi vào 17/5) duyệt bảng kỹ năng `e.a:d[]` của người tung. Nhân vật chưa học
  kỹ năng nào (Julie ở đầu game) có `e.a == null` ⇒ **game gốc NPE nếu ép tung** — đường này không bao giờ xảy
  ra khi chơi thường (UI chỉ cho tung từ danh sách). Debug build tạm thời gán bảng rỗng (và trả lại `null` khi thoát).

## 4. Kết thúc trận

| Hàm | Ý nghĩa | Hook |
|---|---|---|
| `bn.h()` | thắng | `battleEnd(1)` |
| `bn.e()` | thua | `battleEnd(2)` |
| `bn.g()` | hết vòng (bình thường, không kết thúc trận) | `battleEnd(3)` |
| `bn.b()` | bỏ chạy | `battleEnd(4)` |

`battleEnd` xoá sự kiện SFX chưa phát (không dừng tiếng đang phát) và xoá trạng thái theo slot.

## 5. Luồng của Debug Battle (chỉ bản debug)

```
da.keyPressed('#') trên màn cover (cd.dbg$state()==3) → DebugBattle.enter()  (thread Starter)
  └ Main.newGame()  (ván MỚI; quan sát: RMS `cwzw3SmsSave.rs` của emulator không đổi sau mọi lần chạy debug)  → chờ cd.dbg$state()==4
  └ bỏ qua prologue (phím SR giả, cờ injecting)  → cd.dbg$join(2) thêm Julie vào đội
  └ cd.dbg$battle({"133|5|1" x3})  3 quái role 133 → trận thật (cd.q=true; cd.b(String[],0))
bn.f() entry → DebugBattle.tick (máy trạng thái ca thử); bn.f() RETURN/bn.a(Graphics) entry → clamp (bất tử)
bn.a(e,e,I)Z IRETURN → hitRoll (ép trúng/trượt)    putfield bn.a:I → onPhase
ci.a() entry → blockScript (đóng băng script cốt truyện)    cd.a(short[]x3) entry → blockStart (chặn trận script)
da.paint RETURN → vẽ overlay      da.keyPressed entry → onKey (true = nuốt phím)
```

Thoát (`exit()`): dừng SFX (`stopAll`), reset mọi cờ debug, trả bảng kỹ năng đã gán tạm, rồi `Main.destroyApp(true)` (thoát hẳn
ứng dụng). Thử về logo + "Chơi mới" cho thấy trạng thái tĩnh của game bị rò từ ván debug (đội hình có thêm sủng vật) nên không dùng đường đó. Cờ sạch được in ra: `SFXTEST EXIT flagsClean=true`.

Bất tử: sau mỗi tick (`clamp`), nếu HP chờ áp `e.m` hoặc HP thật `a[3]` < 1 thì ghi lại 1 — **sau** khi sự kiện hit/crit/hồi đã
chạy. Chỉ tồn tại trong bản debug; bản release không có mã này.

## 6. Menu hệ thống: "Cài đặt SFX" (từ 1.3.0)

```
mở menu (phím mềm trái ở màn chơi) → tab "Hệ thống" (as.o == 9, as.p == 0): as.p() dựng as.a[] = {Dịch vụ ưu đãi, Lưu game, Tải game,
   Cài đặt nhạc, [Cài đặt SFX ← SfxUi.menuRows], Tải điểm QQ, Thoát game}
ab.h(): OK trên dòng → so String.equals("Cài đặt nhạc") được thay bằng SfxUi.isSoundRow(): dòng nhạc → mode 0, dòng SFX → mode 1;
   cả hai gọi as.a(9, 3) (trang âm lượng, as.p == 3)
as.h(Graphics) trang 3: o.g / at.b được thay bằng SfxUi.vol() / SfxUi.on() (mode 1 → SFXManager.volume); SfxUi.paint vẽ nhãn "Hiệu ứng (SFX)"
ab.h() entry: SfxUi.key(k): mode 1 và k = trái(2)/phải(5) → đổi SFXManager.volume ±10 (+ tiếng thử), nuốt phím; phím khác chạy mã gốc (Về…)
as.a(byte,byte) entry: SfxUi.page(): rời trang 3 → mode = 0
```

Màn logo (`av`): hai chỗ `if (at.a) { hỏi nhạc }` đọc `getstatic at.a:Z`; hook thay bằng `SfxUi.skipPrompt()` — đặt `at.b = true; o.g = at.d` (như câu trả lời "Có" của game gốc) rồi trả `false` để đi thẳng `showCover()`. Máy `allowMusic = 0` giữ nguyên hành vi gốc.

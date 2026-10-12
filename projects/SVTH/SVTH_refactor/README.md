# SVTH_refactor — Sủng Vật Thiên Hạ: SFX (beep) + Debug Battle

Bản vá cho game J2ME **Sủng Vật Thiên Hạ – Đại Lục Thần Điểu** (NAM.P, CLDC-1.0 / MIDP-2.0, 114 class bị làm rối tên).
Không dựng lại mã nguồn: dùng **hook bytecode (ASM) + class mới đọc được** (đường B của quy trình j2modp).

## Hai JAR

| JAR | Dùng để | Có mã debug? |
|---|---|---|
| `dist/SVTH_SFX_renamed.jar` + `.jad` | **Bản phát hành chính (từ 1.6.0)**: game gốc + SFX đồng bộ animation, class/field/hàm mang tên có nghĩa | Không (mã Debug Battle không có trong JAR) |
| `dist/SVTH_SFX_debug_renamed.jar` + `.jad` | Bản thử: thêm **Debug Battle / SFX Test Arena** | Có (`BuildFlags.DEBUG = true`, manifest `SFX-Debug: 1`) |
| `dist/SVTH_SFX.jar` / `SVTH_SFX_debug.jar` + `.jad` | Cùng nội dung, giữ tên rối gốc (đối chiếu; `RENAME=0`) | Như bản tương ứng |

Các bản cùng `MIDlet-Version: 1.7.0`, đều qua `Verify` (0 lỗi) và ProGuard `-microedition` (có StackMap). Từ phiên 6, **bản đã đặt tên là bản gốc chính**
(người dùng xác nhận chạy ổn định); đảo tên lại bằng `mapping/inverse.map` thì giống hệt bản tên rối (release 969/0, debug 974/0).

## Khôi phục mã nguồn (Phiên 5)

Game gốc bị làm rối tên (114 class `a`, `ab`, `bn`…; field/hàm `a`, `b`… trùng tên khác kiểu). Phiên 5 khôi phục **tên có nghĩa** bằng bảng ánh xạ viết tay có bằng chứng:

* `mapping/part_*.map` — 111 class, 1 626 field, 893 hàm được đặt tên (HIGH ≈ 85 %); mỗi dòng ghi bằng chứng (khoá cấu hình, chuỗi hiển thị, định dạng dữ liệu, nơi gọi). Thành viên chưa rõ giữ tên giữ chỗ `<cũ>_<kiểu>` (40 thành viên). `mapping/resolved.map`, `reverse.tsv` (tên mới → cũ), `inverse.map` do công cụ sinh. `Main`, `vh`, `fontcus` vốn đã đọc được.
* `tools/src/Remap.java` (ASM) áp bảng lên bytecode: khoá theo (owner, tên, descriptor), phân giải qua phân cấp, đổi tên theo nhóm override, **đóng băng** hàm ghi đè API CLDC/MIDP, kiểm phân giải lại mọi tham chiếu. Chứng minh **khứ hồi không mất**: tên mới → bảng đảo → JAR gốc: 928/928 class giống hệt (`tools/roundtrip_check.sh`).
* `docs/RENAME_MAP.md` (bằng chứng từng dòng), `docs/rename_notes/<cụm>.md` (từng cụm: lõi, trận đấu, dữ liệu, thế giới, menu, SDK, bao gồm định dạng dữ liệu và lỗi phát hiện).
* `restored/src/*.java` — bản giải mã (Vineflower) của JAR đã đặt tên, để **đọc** (biến cục bộ vẫn `varN` vì JAR gốc không có bảng biến; không chứng minh biên dịch lại được/tương đương — đó là tài liệu tham chiếu). `restored/raw/` là bản giải mã tên giữ chỗ.
* Dựng bản đã đặt tên: `RENAME=1 ./build_jar.sh release|debug`. Bản chuẩn vẫn vá bằng tên gốc (`Patch.java`, `stubs/`, `src/` giữ tên cũ); đổi tên là bước **sau cùng** nên không phá hook.

## SFX (cả hai bản)

* Loại: `HIT`, `MISS`, `CRIT` (chồng lên HIT, phát đồng thời), `SKILL`, `FIRE/WATER/EARTH/WIND`, `HEAL`.
* Mỗi nhịp của đòn liên hoàn / kỹ năng nhiều nhịp có một sự kiện riêng; đồng bộ theo **frame animation thật**
  (chuyển frame trong tick logic, không theo paint) nên tự theo tốc độ và tạm dừng của game.
* Hủy animation, chết, hết trận, mất focus → xoá sự kiện đang chờ. Không trùng sự kiện dù logic cập nhật cùng frame nhiều lần.
* Quinn (thương) và Julie (súng) có biến thể âm riêng (beep trầm/cao). Quái và sủng vật dùng biến thể 0.
* **Độ trễ thấp, chồng tiếng, không cắt tiếng** (từ 1.4.0): mỗi (loại, giọng) có bể `Player` nạp sẵn (đã prefetch, đã tua về 0) nên phát là `start()`
  ngay — không stop/tua/lấy control ở lúc phát; thread SFX ưu tiên cao hơn thread game. Tiếng cùng loại chồng lên nhau bằng Player rảnh kế
  tiếp (bể tự lớn tới 3 Player/tiếng, tổng ≤ 40 để chừa tài nguyên cho nhạc nền). **Không bao giờ stop() tiếng đang phát để nhường chỗ**; bể
  đầy thì hẹn phát lại khi Player rảnh (tối đa 2 lần, ≤150 ms), quá nữa mới bỏ tiếng *mới*. Kết thúc trận/hủy animation chỉ xoá sự kiện
  chưa phát — tiếng đang phát chạy hết đuôi; chỉ tạm dừng game/tắt SFX mới dừng tiếng.
* **Nhạc nền tự hạ khi SFX phát** (ducking): nhạc xuống 45% âm lượng bạn đã chọn ngay khi tiếng bắt đầu, giữ 200 ms sau tiếng cuối rồi trả
  lại dần trong 150 ms (4 bước). Tôn trọng cài đặt: nhạc 0/tắt thì không đụng. Chỉnh bằng `/sfx/config.txt` (`duck`, `duckhold`, `duckramp`).
* **1.7.0 — bộ phát viết lại** (`docs/AUDIO_ENGINE.md` §0): (1) pet và quái không còn mượn giọng nhân vật — trước đây pet có petId 1
  kêu đúng `SKILL_1`/`HIT_1` của Quinn ("pet hành động → skill 1 kêu lại"); chỉ nhân vật chính có giọng 1/2; (2) Player được **làm ấm**
  (đã prepare, dừng ở 0) trên thread nạp riêng, nên trên J2ME Loader tiếng phát ngay, không còn prepare lúc phát; (3) SKILL/HEAL/nguyên tố
  mỗi nhân vật một tiếng một lúc, nhân vật khác vẫn kêu; buff không phát lại SKILL ở frame trúng. Cấu hình thêm `warm`, `prime`.
* **1.6.0 — máy có độ trễ (J2ME Loader/Android)**: Player chỉ được tua/dùng lại khi đã thật sự dừng (`getState()`), nên tiếng dài không còn bị
  phát lại chồng lên nhau ("SKILL loạn"); SKILL/HEAL/nguyên tố mỗi giọng một tiếng một lúc (gọi lại khi còn kêu thì bỏ qua, không cắt);
  không tạo Player lúc phát; nạp sẵn 36 Player từ màn logo; tiếng va chạm của kỹ năng đặt sớm `lead` ms (mặc định 60) để bù độ trễ máy;
  bỏ im lặng ở đầu file (trong bộ nhớ). Chỉnh trong `sfx/config.txt` kèm sẵn trong JAR: `lead`, `solo`, `gap`, `trim` (`docs/AUDIO_ENGINE.md` §2b).
* Mọi lỗi SFX bị bắt: **không bao giờ làm gián đoạn lượt đánh**.
* **27 file `.wav` tiếng thật của người dùng** trong `res/sfx/` (từ 1.6.0; `HIT_0.wav` … `HEAL_2.wav`: 9 loại × 3 giọng — 0 quái/sủng vật,
  1 Quinn, 2 Julie). Thay tiếng: ghi đè file cùng tên rồi `./build_jar.sh` (hoặc thay thẳng trong JAR);
  không cần động đến logic chiến đấu. Bảng tên ↔ lúc phát: `docs/RESOURCES.md`. Beep số nguyên (WAV 8-bit 8 kHz, không
  float/double — CLDC 1.0) chỉ còn là dự phòng khi thiếu file.
* **Tách khỏi nhạc nền** (từ 1.3.0): SFX có công tắc/âm lượng riêng, độc lập với nhạc. Menu hệ thống của game có dòng
  **"Cài đặt SFX"** ngay dưới **"Cài đặt nhạc"**; mở ra trang âm lượng quen thuộc (thanh 10 nấc, trái/phải ±10%, 0 = "Tắt")
  nhưng chỉnh SFX (tiêu đề phụ "Hiệu ứng (SFX)", có tiếng thử khi tăng). Hai bên không ảnh hưởng nhau: nhạc 0 mà SFX 70 vẫn có
  SFX và ngược lại. SFX mặc định **bật (70)** mỗi lần vào game; không lưu qua lần chạy sau.
* **Bỏ hộp thoại "Bật âm nhạc?"** lúc khởi động: nhạc tự bật (như chọn "Có": âm lượng mặc định 80) và SFX tự bật. Muốn tắt, vào
  menu → Hệ thống → Cài đặt nhạc / Cài đặt SFX. Ngoài ra `/sfx/config.txt` (`enabled`, `volume`, `maxvoices`, `pool`, `players`, `duck`, `duckhold`, `duckramp`, `critdelay`, `healdelay`) hoặc API
  `SFXManager.setEnabled/setVolume`; menu Debug Battle có hàng bật/tắt và âm lượng riêng.

## Debug Battle (chỉ `SVTH_SFX_debug.jar`)

1. Ở màn cover/menu chính nhấn `#` → tạo ván mới, đưa Julie vào đội, dựng trận thật với 3 quái giả.
2. Menu chồng lên trận (phím **lên/xuống** chọn hàng, **trái/phải** đổi giá trị, **OK** thực hiện):
   Nhân vật (Quinn/Julie) · Chiêu (đánh thường / ép trượt / ép chí mạng / skill 1..39) · Cấp · Chạy-chạy lại · Chiêu kế ·
   **Auto-test** · Xem log SFX · Bất tử (`#`) · Trúng (ngẫu nhiên/luôn trúng/luôn trượt) · SFX bật/tắt · Âm lượng · Thoát (`0` hoặc SR).
3. Hai bên bất tử bằng cách kẹp HP ≥ 1 *sau* khi sự kiện hit/crit/hồi đã chạy (animation, SFX, nguyên tố vẫn chạy đủ).
4. Auto-test duyệt toàn bộ ca (đòn thường, trượt, chí mạng, SFX lỗi, SFX tắt, mọi kỹ năng tung được ở cấp 1,
   skill 4 ở cấp 1/5/9, từng kỹ năng ép trượt), chờ animation + hàng đợi SFX xong mới sang ca kế, in
   `SFXTEST PASS|FAIL …` và `SFXTEST RESULT`.
5. Log SFX (`SfxLog`, vòng 512 bản ghi): actor, role, skill, loại, frame, nhịp, thời gian, nhịp kỳ vọng/thực tế
   và **trạng thái**: `QUEUED → REQUESTED (đã gọi API, chưa xác nhận) → PLAYED (Player.getState()==STARTED)`,
   hoặc `DROPPED / FLUSHED / FAILED / OFF / DUP`. "Đã gọi API" và "đã phát" tách bạch.
6. Thoát: xả SFX, reset mọi cờ (bất tử, ép trúng, tiêm lỗi, SFX bật), trả bảng kỹ năng tạm gán, rồi **thoát hẳn ứng dụng** (đường `destroyApp` của game). Lý do: ván debug ghi đè trạng thái tĩnh của game (quan sát: sau khi thoát về logo và chơi mới, đội hình có sủng vật lạ so với ván mới sạch); chỉ thoát tiến trình mới bảo đảm không rò. Mở lại game để chơi.
   Bản release **không chứa** mã này nên không thể làm đổi cân bằng game thường.

Giới hạn debug: mục `kỹ năng không tung được` (bị động, không animation): 7, 29–32, 35–38 — báo trong log.

## Giới hạn đã biết (đọc `REFACTOR_LOG.md` §8)

* **Chưa chạy máy/KVM thật.** Kiểm chứng bằng MicroEmulator 2.0.4; sandbox không có thiết bị âm thanh nên `PLAYED` chưa quan sát được (log dừng ở `API_CALLED`).
* Điểm va chạm của hiệu ứng kỹ năng là heuristic (`res/sfx/map.txt`, sửa tay được).
* Làm ấm Player = phát im lặng (âm lượng 0) rồi dừng ngay lúc vào game/sau mỗi tiếng; máy bỏ qua VolumeControl có thể nghe tiếng "tách" → đặt `prime=0`.
  Máy Android cũ lỗi/thiếu tiếng khi nhiều Player → giảm `warm` (16, 20).
* `lead` (bù độ trễ) mặc định 60 ms là mức thận trọng, chưa đo trên máy của bạn: tiếng vẫn trễ → tăng; tới trước hình → giảm (`sfx/config.txt`).
* Đòn thường vẫn phát đúng frame trúng (chưa đặt sớm); tiếng "nở chậm" sẵn trong file (HEAL ~250 ms, SKILL_1 ~170 ms tới 10% năng lượng) không bù được bằng mã.
* Debug Battle bắt đầu bằng ván mới; 9 kỹ năng bị động/không animation (7, 29–32, 35–38) không tung được.

## Dựng lại

```
cd SVTH_refactor
ORIG=/đường/dẫn/JAR_gốc.jar ./build_jar.sh release   # → dist/SVTH_SFX_renamed.jar + .jad (RENAME=0 → dist/SVTH_SFX.jar)
ORIG=/đường/dẫn/JAR_gốc.jar ./build_jar.sh debug     # → dist/SVTH_SFX_debug_renamed.jar + .jad
```

Cần JDK 17+ (chỉ để chạy công cụ; mã game biên dịch bằng ECJ `-1.3 -target 1.1`), Python 3. Thư viện nằm ở `tools/lib/`.
JAR gốc **không** nằm trong repo (không phân phối lại tài nguyên game). Xem `tools/README.md`.

## Tài liệu

`REFACTOR_LOG.md` (nhật ký phiên) · `docs/FLOWS_battle.md` · `docs/SFX_MAP.md` (hook, marker, sai lệch) ·
`docs/RESOURCES.md` · `docs/AUDIO_ENGINE.md` (bộ phát âm thanh) · `docs/BINDIFF_session7.md` (khác biệt nhị phân so với 1.6.0; phiên 1–6 cũng còn) · `tools/README.md`.

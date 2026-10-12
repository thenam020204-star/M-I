# Ghi chú đặt tên — cụm battle (m cj be x cz bx co e bn)

Bảng: `mapping/part_battle.map` (9 class, ~380 dòng). Luồng chi tiết: `docs/FLOWS_battle.md`, `docs/SFX_MAP.md` (đã kiểm ở phiên 1–4).

* **bn → Battle** (HIGH): trận đánh. `tick()` (= `bn.f()`) chạy mỗi tick khi `World` ở chế độ trận (`cd.b_B==5`); `turnIndex`/`actingSlot`/`actionPlans`
  khớp bảng trong FLOWS (`{slot, lệnh 1 đánh/2 thủ/4 kỹ năng/6 vật phẩm, mục tiêu, id kỹ năng, cấp}`); `rollHit` là công thức né: trúng nếu
  `rand(1..100) <= 20 + spdA*100/(spdA+spdD) + spdA + bonus`. Hàng loạt field vẽ (ảnh `D05/D23/D25`, màu chữ) đặt LOW/MED theo tài nguyên nạp.
* **e → BattleActor** (HIGH): một ô chiến đấu (`slot` 0–9 phe ta, 10–19 địch); `h_I` = `expFactor` (LOW): EXP thưởng = `h_I*h_I*level`.
* **cz → SpriteBase** (MED): lớp trừu tượng chung của `RoleSprite` (bl), vật thể bản đồ `ax` và `AnimSprite` (bx): vị trí, kích thước, hiện/ẩn.
* **bx → AnimSprite**, **co → AvAnimation** (HIGH): bộ phát animation `.av` / dữ liệu `.av` (ảnh, tile, frame, hộp va chạm, anim; parser trong `tools/avparse.py`).
  Ba field ảnh phụ của `co` (`altImages1/2`, `imageSet`) chỉ đọc, không bao giờ gán → mã chết (LOW).
* **cj → BattleMotion** (MED): helper tĩnh hướng + vận tốc fixed-point (×100000) cho actor chạy tới/về; **m → Point** (HIGH, x,y).
* **be → PngRecolor** (HIGH): tìm chunk `PLTE` trong PNG, đổi màu bảng màu, tính lại CRC32 (đổi màu quái theo `colorTable.bin`).
* **x → BattleLoader** (LOW): `Runnable` nạp trận; **không có `new x(` nào** trong game (mã chết).

Ghi chú/rủi ro: `e.a_cj motion` không dùng (cj chỉ có hàm tĩnh). `bn.smsContext` (b_I) là cờ bán hàng SMS (1 thua, 2 thắng, 3 "Hạ ngay") — SMS thanh toán luôn thành công trong bản này (xem `sdk.md`).

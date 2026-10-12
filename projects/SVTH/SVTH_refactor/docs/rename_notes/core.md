# Ghi chú đặt tên — cụm lõi (core)

Phạm vi: `Main` `vh` `fontcus` (đã đọc được, giữ nguyên) và `da at ct cq cu cg b bv cc k h`.

* **da → GameCanvas** (HIGH): `Canvas` + `Runnable`. `run()` ngủ bù đến `frameIntervalMs`=16 ms (~60 khung/s), `frameCounter` quay vòng 0..126.
  `screenState` (`da.a_B`): 0 trống, 1 hỏi nhạc (đã bị bỏ ở phiên 3), 2 logo, 3 cover, 4 game, 6 "chạy trên điện thoại", 7 tạm dừng.
  `showToast(String,int)` vẽ hộp thông báo N khung. `a_AI` (không dùng) để tên giữ chỗ.
* **at → DeviceConfig** (HIGH): `load()` đọc `/bin/config.txt`; tên field lấy đúng khoá cấu hình (`cutMapImg`, `allowMusic`, `cacheTxt`…).
  `playMusic` (`at.b`) là cờ bật/tắt nhạc thực tế (ghi đè bằng `allowMusic` khi nạp). `crackClassNames` (MED): `c.b()` gọi `Class.forName` từng tên để phát hiện môi trường crack.
  `defaultVolume` (MED): `da` gán `o.g_B = at.d_B` (=80) khi vào game. `f_B` (=8) không đọc từ cấu hình, chỉ dùng trong `cd`: để trống.
* **ct → GameSettings** (HIGH): `/bin/myConfig.txt`. `goodOrBad` (MED) → `k.drawPanel` dùng khung đặc thay cho khung mờ khi =1 (máy yếu). `maxItemTypes` (MED, khoá `goodsMaxNum`=36).
* **cq → ChannelConfig** (HIGH): `/jzy.bin` + JAD (`cid`, `useFreeCode`): logo, thanh toán SMS, menu thêm, kênh phát hành.
* **cu → PackFile** (HIGH): gói `jzy` chia nhiều tệp phần (`/00`, `/01`…); bảng entry (tên, kích thước, tệp phần đầu, offset). `cg.readResource` rơi về `cu` khi `getResourceAsStream` không có.
* **cg → Utils** (HIGH): mảng (append/removeAt/contains), tách và phân tích văn bản `khoá=giá trị` (CRLF), tài nguyên (`.gf` → PNG: cộng khoá `gfKey` mỗi 10 byte và ghép
  `pngHeader`; `.bin` → UTF-8: trừ `configKey` {7} hoặc +10), vẽ số bằng dải ảnh chữ số, chữ có viền.
* **b → KeyMap** (HIGH): `/bin/key.txt`; `normalizeKey` → 1 lên 6 xuống 2 trái 5 phải 8 OK, `softLeftKey`=998, `softRightKey`=999.
* **bv → MusicPlayer** (HIGH): MMAPI `Player` mid/wav/amr, lặp vô hạn, `release()` có `System.gc()` + sleep 20 ms.
* **cc → ResourceCache** (HIGH): bộ đệm hoạt ảnh/ảnh/byte/văn bản theo "nhóm" (0 = map, 1 = bản đồ/ô…; `releaseGroup(n)` gỡ cả nhóm). Các bảng là mảng song song (tên + dữ liệu + nhóm).
* **k → GameUtil** (MED): tập hợp tiện ích rời (ngẫu nhiên, bảng màu quái, cuộn chữ, khung UI). Không đặt tên hàm khi chưa rõ (`k.a_I()`, `k.a_Acv()`, `k.a(String[][],String[][],int,int,int)`, `k.a(int)` đổi nhân vật, số hiển thị có dấu): giữ placeholder.
* **h → RankEntry** (LOW): lớp rỗng, chỉ `new h()` trong nhánh parse bảng xếp hạng của `an`; tên theo nơi dùng.

Rủi ro/ghi chú phát hiện (không sửa): `k.replaceAll` bỏ qua một ký tự sau mỗi lần thay; `cg.a_String(String,boolean)` gán `var1=false` ngay đầu (tham số bị bỏ qua);
`da.a_AI` không được dùng; `cu.loadIndex` trả `true` dù đầu tệp không phải "jzy".

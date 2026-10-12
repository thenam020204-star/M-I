# SFX_MAP — bản đồ sự kiện âm thanh chiến đấu

Tài liệu này là **bằng chứng** cho từng sự kiện SFX: nó bắt ở đâu trong bytecode, dựa vào dữ liệu nào,
và chỗ nào là suy luận (heuristic) chứ không phải đọc thẳng từ game. Tên class/field của game bị làm
rối (ProGuard), nên dưới đây dùng đúng tên rối (`bn`, `e`, `bx`, …) kèm nghĩa đã giải mã.

## 1. Mô hình thời gian

* Logic và vẽ chạy chung **một luồng**: `da.paint` → `cd.a` → `cd.b` → `bn.f()`. `bn.f()` chạy một lần mỗi
  tick vẽ (`da.a.a` là bộ đếm tick). Animation `bx` chỉ tiến frame khi `da.a.a % 3 == 0`, tức **một frame
  mỗi 3 tick**. Mọi sự kiện SFX gắn vào *bước chuyển frame / chuyển trạng thái trong tick logic*, không gắn
  vào lần vẽ lại → không lặp khi repaint, tự theo tốc độ animation thật, và đứng yên khi game tạm dừng
  (tick không chạy).
* Chống trùng: mỗi đòn thường khử trùng theo `(slot, frame)` trong một chuỗi tấn công (`atkSeq`); mỗi nhịp
  kỹ năng khử trùng theo `(fxSeq, frame)`; AoE có nhiều `bx` cùng marker chỉ phát một sự kiện/nhịp.
* Huỷ/kết thúc: khi actor về trạng thái 0/128 (rảnh/chết) → `SFXManager.flushActor(slot)`; kết thúc trận
  (thắng/thua/bỏ chạy) → `flushAll()` (chỉ xoá sự kiện *chưa phát*, tiếng đang phát chạy hết đuôi); `da.hideNotify` (tạm dừng) → `stopAll()` (dừng cả tiếng đang phát và trả nhạc nền).

## 2. Điểm hook (Patch.java; đo từ `build/release/patch.log`)

Hook luôn có dạng `DUP2 / [ALOAD_0] / INVOKESTATIC SfxHooks.x` đặt **trước** lệnh gốc nên không đổi stack
và không đổi hành vi. 62 vị trí hook SFX ở bản release (92 ở bản debug; 75/105 tính cả hook giao diện từ 1.3.0).

| Hook | Vị trí trong game | Ý nghĩa |
|---|---|---|
| `SfxHooks.onState(e,int,bn)` | trước 44 lệnh `putfield e.f:I` trong `bn` | `e.f` = trạng thái actor (0 rảnh, 1/13 đánh thường, 5/6 chạy tới, 10 hiệu ứng lên địch, 17 hiệu ứng lên bản thân/đồng minh, 2/15 trở về, 128 chết…). Mở/đóng chuỗi tấn công, tính SUMMARY |
| `SfxHooks.onBlow(e,boolean,bn)` | trước 4 lệnh `putfield e.g:Z` trong `bn.f()` | `e.g` = cờ "đòn nổ" của đòn thường: **mỗi lần cờ bật lên là một nhịp trúng/hụt** |
| `SfxHooks.onSkillApply(bn,e,id,lv)` | đầu `bn.a(e,int,int)` | bắt đầu kỹ năng id/cấp: phát SKILL, nạp số nhịp kỳ vọng |
| `SfxHooks.onFrame(bx)` | trước 2 `RETURN` của `bx.a(boolean)` | frame animation hiệu ứng đã tiến; so với bảng marker |
| `SfxHooks.battleStart/battleEnd` | đầu `bn.c()`; đầu `bn.h/e/g/b()` | khởi tạo, nạp trước `Player`; xả hàng đợi |
| `SfxHooks.paused()` | đầu `da.hideNotify()` | `stopAll()` khi mất focus |
| `bx.a(String…)` x3 | đầu các hàm nạp `.av` | ghi tên file `.av` vào `bx.sfx$name` (field mới) |
| `bx.a(I)`, `bx.b(I)` | đầu hàm đổi animation | `sfx$last = -1` để frame đầu mỗi anim được tính |

Từ 1.3.0 có thêm hook **giao diện** (không thuộc sự kiện chiến đấu): `av` (bỏ hộp thoại nhạc), `as`/`ab` (dòng "Cài đặt SFX" và trang âm lượng SFX) — xem `docs/FLOWS_battle.md` §6 và `docs/BINDIFF_session3.md`. Tổng 75 site ở bản release (13 site giao diện).

Accessor `sfx$*` (sinh bằng ASM, class `e`, `bx`, `bn`) cho phép mã Java mới đọc field game bị trùng tên
mà không cần reflection (CLDC 1.0 không có).

## 3. Bảng sự kiện

| Sự kiện | Điều kiện phát | Lớp chồng | Ưu tiên |
|---|---|---|---|
| `HIT` | mỗi nhịp trúng của đòn thường / kỹ năng vật lý (P) / hút máu (D) | + `CRIT` đồng thời (`critdelay`, mặc định 0) nếu `e.m`(chí mạng) | 2 (CRIT 5) |
| `MISS` | nhịp mà **mọi** mục tiêu né (`e.o`); hoặc kỹ năng trạng thái (B) bị kháng/miễn | — | 1 |
| `SKILL` | lúc tung kỹ năng (`onSkillApply`, mọi lớp trừ X); kỹ năng trạng thái trúng ở mỗi nhịp marker | — | 3 |
| `FIRE/WATER/EARTH/WIND` | lúc vào trạng thái 10 của kỹ năng nguyên tố (E) theo `skill.txt` | cùng với `HIT/MISS` của nhịp | 3 |
| `HEAL` | mỗi nhịp marker của kỹ năng hồi phục (H); cùng lúc với `HIT` (`healdelay`, mặc định 0) với hút máu (D) khi trúng | — | 3 |

Biến thể giọng theo nhân vật (`AnimSfxMap.variantOf(role)`): role 1 → 1 (Quinn, thương, tông trầm);
role 2, 3, 39, 40 → 2 (Julie, súng, tông cao); còn lại (quái, sủng vật) → 0.

Mỗi nhịp của kỹ năng nhiều đòn phát một sự kiện riêng: kỹ năng 4 "Linh Đạn Liên Kích" có
`2 + (cấp-1)/4` nhịp (cấp 1 = 2, cấp 5 = 3, cấp 9 = 4; đọc từ `bn.a(e,int,int)` skill 4). Đòn thường:
số nhịp = số frame thoả điều kiện trong `e.c[]` **+ 1** (xem 4.1).

## 4. Sai lệch / điều không hiển nhiên (phải đọc)

### 4.1 Đòn thường luôn nổ thêm một nhịp "cuối đòn"
Sau khi animation tấn công quay về frame 0, `bn.f()` bật `e.g` thêm một lần nữa. Nên số nhịp thực tế của
đòn thường = (số frame va chạm trong `e.c[]`) + 1. Quinn: frame 7, 12 + cuối đòn = 3 sự kiện (đo được 3/3);
Julie: không có frame va chạm trong `e.c[]`, chỉ còn nhịp cuối đòn = 1 sự kiện (đo được 1/1). Hệ thống phát SFX đúng theo số nhịp **engine thực sự
tính sát thương**, không theo số lần "trông thấy" va chạm. Nếu nhóm âm thanh muốn bỏ nhịp thừa, đó là
quyết định thiết kế riêng (cấu hình ở `SfxHooks.expectedBlows`), không đổi ở phiên này.

### 4.2 Điểm va chạm của hiệu ứng kỹ năng là **heuristic**
File `.av` không có "marker âm thanh" hay tag sự kiện. `tools/gen_sfx_map.py` lấy **frame có tổng diện
tích tile lớn nhất** làm điểm va chạm (`effect <file>.av <frame>` trong `res/sfx/map.txt`). Đây là suy luận;
sửa tay được ngay trong file (chỉ cần đổi số, không build lại logic). Ghi chú nguồn: `tools/avparse.py` đọc
định dạng `.av` (đã kiểm tra tái tạo từng byte).

| effect | frame | tổng | effect | frame | tổng |
|---|---|---|---|---|---|
| buff1 | 2 | 12 | huo1 | 5 | 10 |
| buff2 | 7 | 18 | jian1 | 15 | 31 |
| di1 | 20 | 25 | shui1 | 10 | 14 |
| du | 7 | 17 | xixue | 3 | 12 |
| feng1 | 14 | 19 | xuanyun1 | 8 | 16 |
| fengying | 21 | 28 | zhencha | 1 | 13 |
| gongji1 | 2 | 4 | zhiyu | 4 | 12 |
| gongji2 | 2 | 4 | zhufu | 6 | 13 |
| guang1 | 11 | 19 | guang2 | 19 | 43 |
| hunluan | 14 | 24 | | | |

### 4.3 Hiệu ứng lên bản thân dùng trạng thái 17, không phải 10
Kỹ năng nhắm đồng minh/bản thân (hồi phục 13, 14; buff 15, 17, …) đặt `e.f = 17`, không qua trạng thái 10.
Phiên này sửa `SfxHooks` để coi 10 **và** 17 là "đang phát hiệu ứng" (trước đó các kỹ năng này im lặng; auto-test
bắt được lỗi này).

### 4.4 Kỹ năng không có SFX (bị động / không hiệu ứng)
`skill.txt` có 39 kỹ năng. 9 kỹ năng là bị động hoặc không có animation (`[-1]`) nên không có sự kiện để đồng
bộ: **7 (Phản Kích), 29–32 (Cường Kích Địa/Phong/Hỏa/Thủy), 35–38 (Chỉ Huy, Ý Chí, Động Sát, Đại Thiểm Tị)**.
Debug Battle báo "không tung được" cho các id này (lý do: `AnimSfxMap` phân lớp X). Chúng không bị mất âm
thanh vì thiếu hook; đơn giản là không có gì để nghe.

### 4.5 Khác với giả định ban đầu của yêu cầu
* Game có **sủng vật chiến đấu** (pet, `petdata.txt`) ngoài hai nhân vật chính. Chúng dùng biến thể 0 và đi
  qua cùng hook (không có config riêng).
* Nguyên tố trong dữ liệu: Phong, Địa, Hỏa, Thủy (+ ánh sáng "Thần Quang", `guang*.av`, không lớp nguyên tố).
  "Thổ" của yêu cầu = **Địa**.
* Hồi máu = "Hồi phục"/"Trị Liệu"; hút máu (28) kết hợp HIT + HEAL.
* Game **không có animation thương/súng riêng**: `chr1.av` (Quinn) và `chr2.av` (Julie) là sprite nhân vật; việc
  phân thương/súng dựa vào nhân vật (Quinn = thương, Julie = súng) như yêu cầu, chứ không đọc từ trang bị.

## 5. Tệp cấu hình & tài nguyên

Chi tiết và bảng 27 tên tệp tiếng: `docs/RESOURCES.md`.

| Tệp | Vai trò |
|---|---|
| `/sfx/map.txt` | role→biến thể; skill→lớp/nguyên tố/số nhịp; effect→frame va chạm. Sinh bằng `tools/gen_sfx_map.py`, sửa tay được |
| `/sfx/<TÊN>_<v>.wav` (27 tệp im lặng kèm sẵn) | tiếng của từng (loại, giọng); thay bằng tiếng thật. Có tệp ⇒ dùng tệp, không phát beep |
| `/sfx/config.txt` (tuỳ chọn) | `enabled`, `volume`, `maxvoices`, `pool`, `players`, `duck`, `duckhold`, `duckramp`, `critdelay`, `healdelay` (mặc định và ý nghĩa: `docs/RESOURCES.md`) |
| menu game "Cài đặt SFX" | chỉnh `SFXManager.volume` (0 = tắt), độc lập nhạc nền (`at.b`/`o.g`); xem `src/SfxUi.java` |

# Quét API của bộ game người dùng (2026-10-30)

Nguồn: bộ `vip.zip` người dùng cung cấp (JAR **không commit**, nằm ở `tests/games/vip/` — gitignore). Bỏ thư mục `WIPI/` (game port WIPI) theo yêu cầu người dùng; bỏ `Gangstar_v107.zip` (là mã nguồn mod, không phải JAR). 68 JAR được quét bằng `tools/jarscan` với profile `cldc11-midp20`.

Kết quả: 25/68 sạch theo CLDC 1.1 + MIDP 2.0.

## API ngoài CLDC 1.1 + MIDP 2.0 (số game dùng)

| Gói | Số game | Task |
|---|---|---|
| `javax.wireless.messaging` | 33 | T32 (JSR-120 WMA) |
| `com.nokia.mid.ui` | 12 | T28 |
| `javax.microedition.io.file` | 9 | T32 (JSR-75 FC) |
| `javax.microedition.pim` | 3 | T32 (JSR-75 PIM) |
| `javax.microedition.media.control` | 1 | T27 (JSR-135 đầy đủ: MIDIControl, TempoControl) |
| `com.nokia.mid.sound` | 1 | T28 |

## Lỗi không phải API thiếu (do bản mod/Việt hoá đóng gói lại)

- Thiếu `StackMap` (chưa preverify sau khi sửa): máy thật dùng verifier CLDC sẽ từ chối (VerifyError) — jl-kvm strict cũng từ chối; chơi được ở chế độ lenient (debug). Đây chính là loại lỗi ⚠️ NOT J2ME-SAFE.
- Class version 50 (biên dịch bằng Java 6, vượt mức CLDC).
- `MISSING_METHOD` trên lớp của chính game (3 game): chưa kết luận — cả 3 game đều dùng API Nokia/WMA/FileConnection chưa có trong DB, nên rất có thể method được kế thừa từ lớp cha/interface ngoài profile mà jarscan chưa phân giải được. Quét lại khi có DB Nokia UI (V1) và JSR; nếu vẫn còn thì mới là lỗi bản mod.

## Từng game

| Game | Sạch | Method thiếu StackMap | Gói thiếu | Lỗi khác |
|---|---|---|---|---|
| Gameloft/The Oregon Trail Gold Rush.jar | ✘ |  | javax.microedition.media.control |  |
| Gameloft/Splinter Cell Conviction.jar | ✔ |  |  |  |
| Gameloft/Chuck Norris.jar | ✔ |  |  |  |
| Heroes Lore /Heroes Lore - Wind of Soltia.jar | ✔ |  |  |  |
| Heroes Lore /Heroes Lore - Phantasmal Mask.jar | ✔ |  |  |  |
| Heroes Lore /Heroes Lore - Covenant of Darkness.jar | ✔ |  |  |  |
| Heroes Lore /Heroes Lore - Zero.jar | ✘ |  | javax.microedition.io.file, javax.wireless.messaging | method ac.a(Lcm;III[BIII)V |
| Heroes Lore /Heroes Lore - Stigmata Of Gaia.jar | ✔ |  |  |  |
| Heroes Lore /Heroes Lore - Phantasmal Mask_1.01.jar | ✔ |  |  |  |
| Heroes Lore /Heroes Lore - Knight Of Frozen Sea.jar | ✔ |  |  |  |
| Rss/Bí Mật Khải Huyền Thư.jar | ✘ | 850 | javax.microedition.io.file |  |
| Rss/Dũng Binh Thiên Hạ - Vương Giả Trở Về.jar | ✘ | 1 | javax.wireless.messaging |  |
| Rss/Prehistoric_Tribes_VI_mod.jar | ✘ |  | javax.microedition.io.file |  |
| Rss/Dị Thế Tà Quân.jar | ✔ |  |  |  |
| Rss/Thiên Kiếm Vấn Tình – Ngọc Nữ Trần Duyên.jar | ✔ |  |  |  |
| Rss/Thần Kiếm Kỳ Đàm – Thiên Tru.jar | ✘ |  | com.nokia.mid.ui, javax.wireless.messaging |  |
| Rss/Kiếm Linh – Cổ Kiếm Hồn Mộng.jar | ✘ |  | com.nokia.mid.ui |  |
| Rss/Huyễn Ma Lục.jar | ✘ |  | javax.wireless.messaging |  |
| Rss/NongTraiVuiVe2_VH_tele.jar | ✘ |  | javax.microedition.io.file, javax.wireless.messaging |  |
| Rss/Biên Niên Sử Elven.jar | ✘ |  | javax.microedition.io.file |  |
| Rss/Tiên Kiếm Kỳ Hiệp Truyện – Vấn Tâm.jar | ✘ |  | javax.wireless.messaging |  |
| Rss/Huyễn Tưởng Chiến Quốc 3 – Thất Bi.jar | ✘ |  | javax.microedition.io.file, javax.wireless.messaging |  |
| Rss/DaoCaKiemNgu_VI.jar | ✘ |  | javax.wireless.messaging |  |
| Rss/Võ Lâm.jar | ✘ |  | javax.wireless.messaging |  |
| Rss/Farm2Vip.jar | ✔ |  |  |  |
| Rss/Kiem Hon Thuong - Hong Nhan Chi Ton (VH NAMP).jar | ✘ |  | javax.wireless.messaging |  |
| Rss/Total Tournament.jar | ✘ |  | com.nokia.mid.ui |  |
| Rss/Tiên Sủng Kỳ Duyên_320x240.jar | ✔ |  |  |  |
| Rss/ThienSuChiLuyen_VI_moc2e.jar | ✔ |  |  |  |
| Rss/PhongChiHai.jar | ✔ |  |  |  |
| Rss/Kiếm Linh - Tiên Lữ Tình Duyên.jar | ✘ |  | javax.wireless.messaging |  |
| Rss/TucMenhChiKiem_VI.jar | ✔ |  |  |  |
| Rss/Tu Tiên Luyện Yêu Truyện 3.jar | ✘ |  | javax.wireless.messaging |  |
| Rss/Ma Giới Chiến Ký.jar | ✘ |  | com.nokia.mid.ui |  |
| Rss/Linh Thức Huyễn Tưởng – Thần Chi Ước.jar | ✘ | 12 | javax.wireless.messaging |  |
| Rss/Biệt Đội Siêu ROBOT - ZERO.jar | ✘ |  | javax.microedition.io.file, javax.wireless.messaging |  |
| Rss/Ma Đồ - Sơ Nhập Dị Giới.jar | ✘ | 266 | com.nokia.mid.ui, javax.microedition.io.file, javax.wireless.messaging |  |
| Rss/Thương Thần Lục 2 Hạ hồi - Long Lân Chi Nộ.jar | ✘ | 279 | javax.wireless.messaging |  |
| Rss/Đại Náo Thiên Cung.jar | ✘ |  | javax.wireless.messaging |  |
| Rss/Tiên Sủng Kỳ Duyên_240x320.jar | ✔ |  |  |  |
| Rss/Red Alert Mobile.jar | ✔ |  |  |  |
| Rss/Kiếm Linh - Chư Tiên Chi Chiến.jar | ✘ |  | javax.wireless.messaging |  |
| Zen/Zenonia 2 -  The Lost Memories.jar | ✔ |  |  |  |
| Zen/Zenonia.jar | ✔ |  |  |  |
| Zen/Zenonia 3 - The Midgard Story.jar | ✔ |  |  |  |
| Hiên Viên Kiếm/hs2_vi.jar | ✔ |  |  |  |
| Hiên Viên Kiếm/hs4_vi.jar | ✘ |  | javax.microedition.pim, javax.wireless.messaging |  |
| Hiên Viên Kiếm/hs1_vi.jar | ✘ |  | javax.wireless.messaging |  |
| Hiên Viên Kiếm/hs3_vi.jar | ✘ |  | javax.microedition.pim |  |
| Hiên Viên Kiếm/hs5_vi.jar | ✘ |  | javax.microedition.pim, javax.wireless.messaging |  |
| Joymeng/Hùng Bá Tam Quốc - Nữ Thần Giáng Lâm_240x320.jar | ✘ |  | com.nokia.mid.ui, javax.wireless.messaging |  |
| Joymeng/Hùng Bá Tam Quốc - Nữ Thần Giáng Lâm_320x240.jar | ✘ |  | com.nokia.mid.ui, javax.wireless.messaging |  |
| Joymeng/BMKHT2_VH_240x320.jar | ✘ |  | javax.wireless.messaging |  |
| Joymeng/BMKHT2_VH_320x240.jar | ✘ |  | javax.wireless.messaging |  |
| Joymeng/TienNghich_VH_320x240.jar | ✔ |  |  |  |
| Joymeng/TienNghich_VH_240x320.jar | ✔ |  |  |  |
| SV/Sủng Vật Chi Chiến.jar | ✔ |  |  |  |
| SV/Vương Quốc Sủng Vật 2 - Tím.jar | ✘ |  | com.nokia.mid.sound, com.nokia.mid.ui, javax.wireless.messaging | method MM.wap()V |
| SV/Sủng Vật Thiên Hạ – Truyền Thuyết Long Linh.jar | ✘ |  | javax.wireless.messaging |  |
| SV/Sủng Vật Thiên Hạ - Linh Thú Hồi Sinh.jar | ✘ |  | javax.wireless.messaging | class version 50 |
| SV/Sủng Vật Thiên Hạ - Đại Lục Thần Điểu.jar | ✔ |  |  |  |
| SV/Vương Quốc Sủng Vật 3 - Kim.jar | ✘ |  | com.nokia.mid.ui | method e.<init>(Ljavax/microedition/lcdui/Graphics;)V |
| SV/Sủng Vật Thiên Hạ - Linh Thú Chiến Ký.jar | ✘ |  | javax.wireless.messaging |  |
| SV/Vương Quốc Sủng Vật 1 - Lam.jar | ✘ |  | com.nokia.mid.ui, javax.wireless.messaging |  |
| SV/Vương Quốc Sủng Vật 4 - Bạch Kim.jar | ✘ |  | com.nokia.mid.ui, javax.microedition.io.file |  |
| SV/Vương Quốc Sủng Vật - Liệt Hoả.jar | ✘ |  | javax.wireless.messaging |  |
| SV/Vương Quốc Sủng Vật 5 - Cầu Vồng.jar | ✘ |  | com.nokia.mid.ui, javax.wireless.messaging |  |
| SV/Sủng Vật Thiên Hạ – Truyền Thuyết Long Linh_320.jar | ✘ |  | javax.wireless.messaging |  |

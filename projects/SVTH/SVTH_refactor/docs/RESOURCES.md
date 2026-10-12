# RESOURCES — tài nguyên thêm/sửa tay (phiên 1–2)

Tài nguyên gốc (`/ft/*.av`, `/av/*.av`, `bin/*.txt`, ảnh, `music/*.mid`) **được chép nguyên byte-for-byte**
vào JAR mới (`tools/pack.py`; 922/928 entry giống hệt JAR gốc, xem `BINDIFF_session1.md`). Chỉ **thêm** các tệp sau.

| Tệp trong JAR | Nguồn trong repo | Ghi chú |
|---|---|---|
| `/sfx/map.txt` | `res/sfx/map.txt` | Bảng ánh xạ role→biến thể, skill→lớp/nguyên tố/số nhịp, effect→frame va chạm. Sinh bởi `tools/gen_sfx_map.py` từ `bin/skill.txt` + các `.av`; **sửa tay được** (cột `effect <tệp>.av <frame>` là heuristic). Thiếu tệp → kỹ năng im lặng (lớp 0), đòn thường vẫn có HIT/MISS/CRIT và mọi nhân vật dùng biến thể 0; không lỗi |
| `/sfx/<TÊN>_<v>.wav` (27 tệp; **từ 1.6.0 là tiếng thật của người dùng**, lấy từ `SVTH_SFX_renamed.jar` họ gửi) | `res/sfx/*.wav` | WAV PCM 16-bit mono 8/11,025/16 kHz, 0,2–1,1 s. Có tệp thì dùng tệp — beep chỉ còn là dự phòng khi thiếu tệp. (≤ 1.5.0: 27 tệp im lặng 50 ms sinh bởi `tools/gen_silent_wavs.py`) |
| `/sfx/<TÊN>.wav` (tuỳ chọn, không kèm) | — | Dự phòng dùng chung cho mọi giọng nếu thiếu `<TÊN>_<v>.wav` |
| `/sfx/config.txt` (kèm sẵn từ 1.6.0, sửa thẳng trong JAR) | — | Dòng `key=số`, `#` đầu dòng = chú thích (không ghi chú cùng dòng với số). Mặc định: `enabled=1`, `volume=70` (0–100), `maxvoices=12` (1–16, số tiếng đồng thời), `pool=3` (1–6, Player/tiếng), `players=40` (tổng Player SFX), `duck=45` (% nhạc nền khi hạ; 100 = không hạ), `duckhold=200` ms, `duckramp=150` ms, `critdelay=0`, `healdelay=0` ms; 1.6.0: `lead=60` (ms bù trễ tiếng va chạm kỹ năng), `solo=1`, `gap=40`, `trim=150`; 1.7.0: `warm=26` (số Player làm ấm sẵn), `prime=1` (xem `docs/AUDIO_ENGINE.md` §0, §2b, §4) |

`MANIFEST.MF`: giữ mọi thuộc tính gốc, đổi `MIDlet-Version` (1.4.0), bản debug thêm `SFX-Debug: 1`. `.jad` ghi lại `MIDlet-Jar-Size` thật.

## Bảng 27 tệp tiếng cần thay

Giọng: `_0` = quái / sủng vật, `_1` = **Quinn** (thương), `_2` = **Julie** (súng). Tên viết HOA đúng như bảng (không đúng chữ hoa/thường = không dùng).

| Tên | Phát khi | Gợi ý |
|---|---|---|
| `HIT_v.wav` | mỗi nhịp **trúng** của đòn thường / kỹ năng vật lý / hút máu | ngắn, ≤ 150 ms; đòn liên hoàn Quinn phát 3 lần liền |
| `MISS_v.wav` | nhịp mà mọi mục tiêu **né**; hoặc kỹ năng trạng thái bị kháng/miễn | gió vụt, nhẹ |
| `CRIT_v.wav` | **chồng lên** HIT khi chí mạng (phát đồng thời với HIT; `critdelay=` để trễ) → nên là tiếng sắc, không che HIT |
| `SKILL_v.wav` | lúc **tung** kỹ năng; và mỗi nhịp kỹ năng trạng thái (buff/độc/choáng…) trúng |
| `FIRE_v.wav` `WATER_v.wav` `EARTH_v.wav` `WIND_v.wav` | kỹ năng nguyên tố (Hỏa/Thủy/Địa/Phong) lúc hiệu ứng nổ, cùng với HIT/MISS của nhịp |
| `HEAL_v.wav` | mỗi nhịp hồi phục (kỹ năng 13, 14) và nhịp hút máu trúng (28; `healdelay=` để trễ sau HIT) |

Định dạng khuyến nghị: **WAV PCM 8-bit (hoặc 16-bit) mono, 8–11 kHz, ≤ ~300 ms**, tiếng **đánh mạnh ngay từ đầu** (attack ngắn): phần im lặng/nhỏ ở đầu file là độ trễ cộng thẳng vào lúc nghe (1.6.0 tự bỏ phần im lặng thật ≤ 150 ms trong bộ nhớ, nhưng tiếng "nở chậm" như HEAL ~250 ms, SKILL_1 ~170 ms thì không bù được). SKILL/HEAL/nguyên tố phát một tiếng một lúc mỗi giọng (1.6.0), nên tiếng dài sẽ nuốt lần gọi lại trong lúc còn kêu. Độ dài thật được đọc từ header WAV (dùng để biết Player nào còn bận và để hạ nhạc nền đúng lúc) nên **header phải đúng**. Mỗi (loại, giọng) giữ 1–3 `Player` dùng lại (1.6.0: 36 nạp sẵn từ màn logo — 2 cho HIT/MISS/CRIT — + tối đa 4 thêm khi có chồng tiếng, `players=`); tệp càng nhỏ càng tốt cho máy yếu. Máy báo hết tài nguyên MMAPI thì bể ngừng lớn (không lỗi).

## Công tắc / âm lượng SFX (tách khỏi nhạc, từ 1.3.0)

SFX **không còn phụ thuộc** công tắc nhạc của game (`at.b`/`o.g`). Người chơi chỉnh SFX ở menu hệ thống → **"Cài đặt SFX"** (dưới "Cài đặt nhạc"): trái/phải đổi `SFXManager.volume` theo bước 10, `0` = tắt (log `OFF`, không gọi API; không nạp `Player` nếu tắt từ đầu). Tăng âm lượng có tiếng thử (HIT giọng 0 — im lặng nếu `HIT_0.wav` vẫn là tệp rỗng). Mặc định 70, luôn bật khi vào game, không lưu qua lần chạy sau. `enabled=0` trong `config.txt` vẫn tắt hẳn.

## Thay tiếng

1. Ghi đè `res/sfx/<TÊN>_<v>.wav` bằng tệp của bạn (cùng tên). Tạo lại tệp im lặng: `python3 -I tools/gen_silent_wavs.py --force`.
2. `ORIG=<JAR gốc> ./build_jar.sh release` (pack.py tự gói mọi thứ trong `res/`) — hoặc dùng công cụ zip thay trực tiếp `sfx/<TÊN>_<v>.wav` trong JAR rồi cập nhật `MIDlet-Jar-Size` trong JAD.
3. `SFXManager`/`SfxHooks` giữ nguyên — `BeepBackend.create()` thấy tệp thì dùng. Beep sinh số nguyên chỉ còn là dự phòng khi **thiếu** tệp; nếu `Manager.createPlayer` lỗi trên máy (WAV hỏng/không hỗ trợ) thì rơi về `Manager.playTone` (có thể nghe beep).

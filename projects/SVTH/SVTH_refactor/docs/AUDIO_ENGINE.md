# AUDIO_ENGINE — bộ phát SFX (viết lại ở 1.7.0)

## 0. 1.7.0 — kiến trúc mới (BeepBackend viết lại), và hai lỗi gốc người dùng gặp

Người dùng báo (1.6.0): *"Quinn dùng skill lên địch → phát SKILL_1.wav; kết thúc hành động, pet hành động → SKILL_1 phát lại thêm phát nữa"* và *"vẫn khá delay"*.

**Lỗi 1 — giọng chọn theo `roleId` (SfxHooks).** Giọng (0 quái/pet, 1 Quinn, 2 Julie) tra theo `roleId` trong `map.txt`. Nhưng sủng vật có
`roleId = petId` (`GameData.createPetSprite`: `var1.roleId = (short) var0.petId`) và quái tra cùng bảng pet (`petTable[roleId - 1]`), nên pet/quái
có id 1, 2, 3, 39, 40 **trùng số** với nhân vật: pet có petId 1 kêu đúng SKILL_1/HIT_1 của Quinn — đúng triệu chứng. Sửa: `SfxHooks.voiceOf(e)` —
chỉ actor slot 0–9 có `partyUid == 0` (nhân vật chính; pet luôn có `petUid ≥ 1`, xác nhận bằng bytecode: `bn` gán `e.a:S ← bl.c:S`) mới có giọng
riêng; còn lại giọng 0. Accessor mới `e.sfx$partyUid()` (Patch). Log SFX ghi thêm `voice=`.
Thêm: kỹ năng trạng thái (lớp B) không phát lại SKILL ở frame va chạm (cast đã phát) — chỉ MISS nếu mọi mục tiêu kháng.

**Lỗi 2 — "nạp sẵn" không có tác dụng trên J2ME Loader (BeepBackend).** Đọc mã nguồn J2ME Loader
([MicroPlayer](https://github.com/nikita36078/J2ME-Loader/blob/master/app/src/main/java/javax/microedition/media/MicroPlayer.java),
[BasePlayer](https://github.com/nikita36078/J2ME-Loader/blob/master/app/src/main/java/javax/microedition/media/BasePlayer.java),
[AndroidPlayer](https://github.com/nikita36078/J2ME-Loader/blob/master/app/src/main/java/javax/microedition/media/AndroidPlayer.java),
[InternalDataSource](https://github.com/nikita36078/J2ME-Loader/blob/master/app/src/main/java/javax/microedition/media/InternalDataSource.java)):
`realize()`/`prefetch()` **không** chuẩn bị `MediaPlayer`; `start()` mới `setDataSource + prepare()` (đồng bộ); phát hết → `complete()` →
`MediaPlayer.reset()` nên lần sau lại `prepare()`; `stop()` = `pause()` (vẫn nạp); `createPlayer(InputStream)` ghi file tạm + chạy FFprobe.
⇒ ở ≤ 1.6.0 **mọi tiếng đều prepare ngay lúc phát**. Mô phỏng (prepare 80 ms): bộ phát 1.6.0 prepare trong `start()` ở 45/45 lần phát, 48/51 tiếng
bắt đầu trễ > 20 ms; bộ phát 1.7.0: 0/48, trễ post→start 0 ms.

**Kiến trúc 1.7.0** (mượn khái niệm của engine âm thanh game: nạp/giải mã trước như SoundPool của Android; giới hạn số tiếng theo đối tượng
và "bỏ tiếng mới" thay vì cắt như playback limit của Wwise / max instances của FMOD — tham khảo khái niệm, không phải tài liệu chuẩn):

| Lớp | Việc |
|---|---|
| Asset | 27 WAV đọc một lần (thread nạp), bỏ im lặng đầu file trong bộ nhớ, độ dài từ header |
| Voice | Player với trạng thái `COLD` (đã tạo) → `READY` (**đã làm ấm**: âm lượng 0 → `start` → `stop` → tua 0 → trả âm lượng: MediaPlayer đã prepare, dừng ở 0) → `PLAYING` → `DONE` (hết tiếng, `getState() != STARTED`) → làm ấm lại → `READY`; `WORK` = thread nạp đang xử lý |
| Policy | SKILL/HEAL/nguyên tố: tối đa 1 tiếng **mỗi actor** (gọi lại khi tiếng của chính actor đó còn kêu → bỏ qua; actor khác vẫn kêu). HIT/MISS/CRIT: chồng tự do, trùng (loại, giọng) trong 40 ms gộp một. Không bao giờ cắt tiếng đang kêu |
| Warm set | Giữ ấm tối đa `warm` = 26 Player (mỗi Player ấm trên Android giữ một AudioTrack; máy cũ ~32 cho cả nhạc), ưu tiên giọng có mặt trong trận (`touch` từ onState) và tiếng hay dùng (HIT MISS CRIT SKILL HEAL, Player thứ 2 của HIT/MISS/CRIT, SKILL_0 ×2, rồi nguyên tố). Ngoài tập ấm: `deallocate()` |
| Threads | **Thread phát** (ưu tiên NORM+2): chỉ chọn Player `READY` và `start()` dưới một khoá ngắn. **Thread nạp** (NORM): tạo / làm ấm / hạ ấm từng Player, việc nặng làm NGOÀI khoá → tiếng tới hạn không bao giờ chờ prepare |

Cấu hình mới trong `sfx/config.txt`: `warm=26`, `prime=1` (0 = không làm ấm, dùng cho máy không có VolumeControl hay nghe tiếng "tách").

---

# (lịch sử) bộ phát độ trễ thấp, chồng tiếng, hạ nhạc nền (1.4.0; sửa cho máy có độ trễ ở 1.6.0)

## 1. Đường đi của một tiếng

```
hook trong logic game (SfxHooks.fire)
  └ SFXManager.post()        giữ khóa vài lệnh: ghi vào hàng đợi cố định 32 ô, notify  (không cấp phát, không I/O)
      └ thread SFX (ưu tiên NORM+2) thức dậy
          ├ BeepBackend.play()   chọn Player RẢNH của (loại, giọng) → [vol nếu đổi] → start()      ← đường nóng
          ├ BgmDuck.onSfx()      hạ nhạc nền (Main.music.b(vol)) ngay sau start()
          └ bg()                 việc nền: tua lại Player đã hết tiếng, nạp thêm Player, trả nhạc nền
```

Việc nặng (tạo/prefetch Player, tua lại, đọc WAV) nằm ở `preloadStep()` (từ màn logo, từng Player một, nhường tiếng tới hạn) và `maintain()` (lúc rảnh), không ở lúc phát (1.6.0: kể cả khi bể đầy — xem §2b).

## 2. Vì sao bản cũ có độ trễ / cắt tiếng, và đã đổi gì

| Vấn đề (≤ 1.3.0) | Cách xử lý (1.4.0) |
|---|---|
| Mỗi lần phát: `getState` → `stop()` nếu đang phát → `setMediaTime(0)` → `getControl("VolumeControl")` → `setLevel` → `start()` | Player được tua về 0 ngay sau khi hết tiếng (nền); `VolumeControl` lấy một lần; `setLevel` chỉ khi âm lượng đổi → lúc phát chỉ còn `start()` |
| 1 Player/tiếng: tiếng thứ hai cùng loại **stop() tiếng đầu** (cắt) | Bể 1–3 Player/tiếng (`pool=`); chồng bằng Player rảnh kế tiếp; **không bao giờ stop() để nhường chỗ** |
| Giới hạn voice mặc định 3, quá thì bỏ/giành voice | `maxvoices=12` (1–16); quá mức chỉ bỏ tiếng *mới*; CRIT được thêm 2 |
| CRIT trễ 35 ms, HEAL trễ 60 ms sau HIT | Mặc định phát đồng thời (`critdelay=0`, `healdelay=0`); đặt lại nếu máy không trộn được |
| `flushAll()` ở hết trận/hủy animation `stop()` mọi tiếng đang phát | `flushAll()` chỉ xoá sự kiện chưa phát; `stopAll()` (tạm dừng game, SFX tắt, âm lượng 0) mới dừng tiếng |
| Thread SFX cùng ưu tiên với thread game | `NORM_PRIORITY + 2` |
| Player đầu tiên của tiếng tạo lúc phát nếu SFX vừa bật lại | `setVolume` 0→>0 và mở trang "Cài đặt SFX" kích hoạt nạp sẵn |

Khi bể của một tiếng đầy: hẹn phát lại khi Player sớm nhất rảnh (tối đa 2 lần, mỗi lần chỉ nếu ≤ 150 ms), không thì bỏ tiếng mới (log `DROPPED`). Tiếng đang phát không bị đụng.

## 2b. 1.6.0 — máy có độ trễ (J2ME Loader/Android…): tiếng skill "chồng loạn", vẫn trễ

Người dùng thay 27 wav thật (SKILL_1 1047 ms, HIT_2 871 ms, HEAL 1081 ms…) và báo: tiếng skill (SKILL_1/SKILL_2) loạn, chồng lên nhau; vẫn khá trễ.
Tái hiện bằng MMAPI giả có độ trễ L (Player còn `STARTED` tới `start + L + độ dài`, `createPlayer` 60 ms) với đúng 27 wav của người dùng:

| Nguyên nhân (≤ 1.5.0) | Bằng chứng (L = 150 ms) | Sửa (1.6.0) |
|---|---|---|
| Coi Player rảnh theo đồng hồ (`start + độ dài + 25 ms`) rồi `setMediaTime(0)` → trên máy trễ, Player **còn đang kêu** bị tua về đầu: tiếng chạy lại từ đầu, chồng lên tiếng sau ("ghost replay"); Player đó bị coi là rảnh nên lần `start()` sau là lệnh rỗng (mất tiếng) | 72/93 lần tua rơi vào Player đang kêu, 21 `start()` lên Player đang kêu | `getState() != STARTED` là sự thật về "còn kêu" (`sounding`); chỉ tua Player đã dừng; Player báo `STARTED` quá 1,5 s sau hạn thì coi là kẹt → `stop()` + tua (đã hết tiếng từ lâu) |
| Kỹ năng buff (lớp B) phát SKILL lúc tung **và** ở frame va chạm → hai bản của một tiếng 1 s cách nhau ~0,4 s | 2 tiếng/lần tung | SKILL/HEAL/nguyên tố là **solo**: mỗi (loại, giọng) một tiếng một lúc; gọi lại khi tiếng cũ còn kêu thì **bỏ qua** (log `DUP`) — không chồng, cũng **không cắt** tiếng đang kêu (`solo=0` để chồng như cũ) |
| Bể đầy → **tạo Player ngay lúc phát** (create + realize + prefetch: hàng chục–trăm ms trên máy) → tiếng tới muộn, rơi chồng lên tiếng sau | — | Không bao giờ tạo Player khi bể đã có Player; nạp thêm ở `maintain()` lúc rảnh. Nạp sẵn 2 Player cho HIT/MISS/CRIT (hay chồng: đòn nhiều nhịp), 1 cho tiếng khác = 36 Player |
| Hẹn phát lại tới 2 lần × 150 ms → bản muộn của tiếng dài | — | Hẹn tối đa 1 lần, chỉ khi Player rảnh trong ≤ 60 ms; không thì bỏ tiếng mới |
| Nạp 27 Player **khi vào trận đầu tiên** trên thread SFX, tiếng của mấy giây đầu trận phải chờ | — | Nạp từ màn logo (`SfxUi.skipPrompt` → `SFXManager.init()`), **từng Player một**, tiếng tới hạn luôn được phát trước |
| HIT trùng giọng cùng lúc (nhiều quái đánh cùng frame) → nhiều bản trùng pha, "vỡ" | — | HIT/MISS/CRIT cùng giọng start cách nhau < `gap` (40 ms) thì gộp làm một |
| Im lặng ở đầu file (SKILL_1 có 41 ms) | — | Cắt im lặng thật (< −40 dBFS, tối đa `trim` = 150 ms) **trong bộ nhớ**; file trong JAR giữ nguyên |
| Độ trễ thiết bị (start() → loa) | — | **Đặt sớm tiếng va chạm của kỹ năng `lead` ms** (mặc định 60): frame va chạm đã biết (map.txt), trúng/né/chí mạng đã tính lúc tung; độ dài một frame đo trực tiếp (mặc định 48 ms = 3 tick × 16 ms). Hủy animation trước đó → `flushActor` xoá tiếng chưa tới hạn |

Không đổi: đòn thường vẫn phát đúng frame trúng (không dự đoán nhịp, tránh lệch số nhịp); tiếng lúc tung kỹ năng phát khi lệnh tung chạy.

**Độ trễ nằm sẵn trong file** (đo năng lượng đạt 10% tổng): HIT_0 25 ms, HIT_1 **96 ms**, HIT_2 27 ms, MISS 31 ms, CRIT 20 ms, SKILL_0 42 ms, SKILL_1 **169 ms**, SKILL_2 81 ms,
FIRE 57 ms, WATER 33 ms, EARTH 24 ms, WIND 60 ms, HEAL **252 ms**. Đây là âm "nở chậm" của chính file (không phải im lặng) — mã không bù được;
muốn nhanh hơn hãy dùng bản có tiếng đánh mạnh ngay đầu (attack ngắn).

## 3. Hạ nhạc nền (ducking)

* Dùng đúng lời gọi game dùng ở "Cài đặt nhạc": `Main.music.b(mức)` (đặt `VolumeControl` của bộ phát nhạc `bv`), mức = âm lượng nhạc người chơi chọn `o.g` × `duck`% (mặc định 45%).
* Hạ ngay sau khi SFX bắt đầu; giữ `duckhold` (200 ms) sau tiếng cuối; trả lại trong `duckramp` (150 ms) bằng 4 bước; SFX mới đến giữa chừng thì hạ lại ngay. `duck=100` = tắt.
* Nhạc tắt (`at.b = false`), `o.g = 0`, hoặc chưa có bộ phát nhạc → không làm gì. `stopAll()` (tạm dừng, tắt SFX) trả nhạc về mức đầy ngay.
* Mỗi lần hạ đều đặt lại mức (không tin trạng thái nhớ) vì game có thể đặt nhạc về mức đầy lúc vào trận / đổi bài.

## 4. Cấu hình `/sfx/config.txt` (từ 1.6.0 kèm sẵn trong JAR, sửa trực tiếp được)

| Khoá | Mặc định | Ý nghĩa |
|---|---|---|
| `enabled` | 1 | tắt hẳn SFX |
| `volume` | 70 | âm lượng SFX 0–100 (người chơi chỉnh ở menu) |
| `maxvoices` | 12 | số tiếng đồng thời tối đa (1–16) |
| `pool` | 3 | Player tối đa cho mỗi (loại, giọng) (1–6) |
| `players` | 40 | tổng Player SFX tối đa (1.6.0: 36 nạp sẵn + 4 để chồng) — chừa tài nguyên MMAPI cho nhạc nền |
| `duck` | 45 | % âm lượng nhạc giữ lại khi hạ (100 = không hạ) |
| `duckhold` / `duckramp` | 200 / 150 | ms giữ / ms trả nhạc |
| `critdelay` / `healdelay` | 0 / 0 | trễ lớp chồng (ms) |
| `lead` | 60 | (1.6.0) đặt tiếng va chạm của kỹ năng sớm bao nhiêu ms (0–300). Tiếng vẫn trễ hình → tăng (80, 100, 120); tiếng tới trước hình → giảm; 0 = như ≤ 1.5.0 |
| `solo` | 1 | (1.6.0) SKILL/HEAL/nguyên tố một tiếng một lúc cho mỗi giọng |
| `gap` | 40 | (1.6.0) ms gộp HIT/MISS/CRIT trùng giọng |
| `trim` | 150 | (1.6.0) ms im lặng đầu file tối đa được bỏ (chỉ trong bộ nhớ); 0 = tắt |

## 5. Kiểm thử (`tools/test/run_audio_test.sh`, kết quả: `docs/AUDIO_TEST_session4.txt`, `docs/AUDIO_TEST_session6.txt`)

Chạy trên JVM thường với MMAPI giả (`tools/test/fake`) ghi thời điểm start/stop/rewind của từng Player và mọi lần đặt âm lượng nhạc.
Đo được: độ trễ phần mềm post→`start()` (trung vị vài trăm µs), 3 tiếng cùng loại start trong ~20 µs trên 3 Player khác nhau, 5 loại khác nhau cùng lúc,
300 sự kiện ngẫu nhiên → 0 `stop()` và 0 `start()` lên Player đang phát, `flushAll` không dừng tiếng, `stopAll` dừng, hạ nhạc 80→36 ngay sau start rồi trả
36→46→57→68→80 sau hết tiếng + 200 ms, không đụng nhạc khi tắt/0/`duck=100`, máy báo hết Player sau 30 vẫn chạy, `players=29` được tôn trọng.
Trên emulator: `Main.music` thật hạ 80→36 khi SFX phát và về 80 sau giữ + trả; `stopAll` trả về 80.

1.6.0 thêm `SfxAudioTest lat0|lat150|lat300` (wav thật trong `res/`): buff 1 tiếng/lần tung, chuỗi HIT 871 ms chồng nhau đủ tiếng và post→start ≤ 15 ms,
200 sự kiện ngẫu nhiên → 0 tua lên Player đang kêu, 0 `start()` lên Player đang kêu, 0 `stop()`. Mã 1.5.0 trượt 3/5 ca ở L = 150 (72 tua sai, 21 start sai).

## 6. Giới hạn trung thực

* **Độ trễ đo được chỉ là phần mềm** (post→gọi `start()`). Độ trễ từ `start()` đến khi loa phát, và việc **máy có trộn được nhiều `Player` cùng lúc hay không**, phụ thuộc thiết bị/hãng; sandbox không có thiết bị âm thanh nên chưa kiểm được trên máy thật. Máy chỉ phát được một tiếng một lúc sẽ cắt hoặc từ chối tiếng thứ hai dù mã không cắt — khi đó đặt `pool=1`, `critdelay=35`, `healdelay=60` để quay về nối tiếp.
* `lead` mặc định 60 ms là mức thận trọng: máy có độ trễ nhỏ hơn 60 ms (máy Nokia, MicroEmulator) sẽ nghe tiếng va chạm kỹ năng tới sớm vài chục ms; máy Android/J2ME Loader thường trễ hơn thế. Chỉnh `lead` theo máy.
* Sandbox không có thiết bị âm thanh: trên emulator mọi `createPlayer` lỗi (`players=0`) nên đường phát thật chỉ được kiểm bằng MMAPI giả.
* Ducking dựa vào `VolumeControl` của bộ phát nhạc; trên máy mà `VolumeControl` của nhạc MIDI không có tác dụng thì không nghe thấy hạ nhạc (không gây lỗi).

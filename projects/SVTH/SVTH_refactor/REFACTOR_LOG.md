# REFACTOR_LOG — Sủng Vật Thiên Hạ – Đại Lục Thần Điểu

## Phiên 8 — 2026-10-12 (UTC+7): AI quái mạnh và thông minh hơn, cấp/chỉ số quái bám cấp TB đội, bộ kỹ năng ngẫu nhiên, sủng vật do AI điều khiển

**Yêu cầu (3 lượt trong phiên):**
1. "Mod a.i quái mạnh hơn và thông minh hơn, và cấp độ, chỉ số của quái luôn bám theo cấp trung bình của cả đội hero" — kèm `res.jar` ("Mod bản này").
2. "Cấp phát cho quái ngoài tự nhiên 1 bộ skill khác nhau mỗi trận đánh, 10 cấp tăng thêm 1 skill, tối đa 1 quái có thể có 7 skill ngẫu nhiên mỗi trận".
3. "Rebuild hệ thống 2 pet đồng đội … người chơi chỉ có thể điều khiển hành động của nhân vật chính, 2 pet còn lại sẽ được a.i điều khiển … nếu có kĩ năng heal
   mà có đồng đội thấp hơn 30% máu chúng sẽ ưu tiên dùng, khi máu bản thân pet quá thấp và nếu ko có skill heal chúng sẽ tự thủ tới chết hoặc tự động rút lui …
   không thể gọi nó ra lại trong trận hiện tại, chế độ tấn công giống a.i quái hoang dã. Bạn tự mở rộng thêm cho sinh động".

**Cách hiểu (giả định):**
* "cả đội hero" = các **nhân vật chính** trong trận (`partyUid == 0`: Quinn, Julie…), không tính sủng vật (`pets=1` để tính cả). "Luôn bám theo" áp cho mọi trận
  trừ trận hướng dẫn và trận bắt buộc thua; trận cốt truyện/boss không xuống thấp hơn cấp game thiết kế (`bossfloor=1`, tắt được).
* "chỉ số bám theo" = ngoài việc game tính chỉ số từ cấp mới, còn **sàn/ngân sách theo sức mạnh thật của đội** (đo được: ở cùng cấp quái gốc gần như vô hại).
* "ngoài tự nhiên" = **ngoài kỹ năng tự nhiên** của loài (bảng `petdata`): mỗi quái mỗi trận thêm `1 + cấp/10` kỹ năng ngẫu nhiên, tối đa 7. Áp cho mọi quái
  (`randboss=0` → chỉ quái gặp ngẫu nhiên ngoài bản đồ — cách hiểu khác "quái ngoài tự nhiên = quái hoang dã").
* "2 pet đồng đội" = sủng vật xuất chiến ở ô 1, 2. Nhân vật chính khác (Julie) vẫn do người chơi điều khiển. "Rút lui" = rời trận **còn sống**.

**Bản build:** `dist/SVTH_SFX_renamed.jar` **v1.8.0** (1 947 352 B, 972 entry) + `.jad`; `dist/SVTH_SFX_debug_renamed.jar` v1.8.0 (1 964 802 B). Preverified CLDC 1.0 / MIDP 2.0,
class 45.3 — `Verify` 0 lỗi trước và sau ProGuard, StackMap đủ (127 class bản phát hành, 132 bản debug).
**Nền:** `res.jar` người dùng gửi = `SVTH_SFX_renamed.jar` 1.7.0 + **2 chỉnh sửa tay**: `font.bin` (đổi) và `f18.bin` (thêm; = `fonts/f15c.bin` của repo) — 968/970 entry
giống md5 bản 1.7.0. Đã đưa vào `res/` (commit riêng) và lưu `base/SVTH_SFX_renamed_1.7.0_user.jar`. JAR gốc của game không có trong phiên → phiên 8 dựng **trên bản 1.7.0
đã đặt tên** (`build_s8.sh`, đường B: hook bytecode + class mới), không qua `build_jar.sh`.

### 1. Hạng mục đã xử lý

| # | Yêu cầu | Kết quả |
|---|---|---|
| 1 | Cấp quái bám cấp TB đội | `EnemyAI.adjustLevel` (hook trước lời gọi tính chỉ số đầu tiên của `Battle.createEnemyActors`): gặp ngẫu nhiên = TB + 0..2, trận script = TB + 3 (≥ cấp thiết kế). Game tự tính HP/MP/ATK/DEF/SPD/EXP thưởng từ cấp mới. Log: `AILOG level … orig=28 avg=30 -> 32` |
| 2 | Chỉ số quái mạnh hơn, bám đội | `onEnemiesCreated`: HP ×1.05 + sàn (đội cần ~2.2 vòng đánh thường, boss 3.5), SPD ≥ 70 % đội, DEF ≤ 45 % của 2×ATK đội, **ATK tính ngược từ ngân sách sát thương**: đòn mạnh nhất của quái (kỹ năng + tương khắc hệ thật) ≈ 8 % HP hero (boss 13 %). Trước: ở cùng cấp quái đánh thường chỉ gây 10 (tối thiểu) |
| 3 | AI thông minh hơn | Chấm điểm mọi phương án theo công thức thật của `dealDamage`/`applySkill`: kết liễu, dồn đòn không đánh thừa, hợp kích (cùng mục tiêu, tốc độ ±150), tương khắc hệ, né người phòng thủ, ưu tiên kẻ gây sát thương lớn/hồi máu, hồi máu đồng đội < 50 %, khống chế không chồng + chống khoá đội, phòng thủ khi < 30 % HP, chi phí MP; **quyết định lại đúng lượt** theo HP/trạng thái hiện tại. Gốc: mục tiêu ngẫu nhiên, 50 % kỹ năng ngẫu nhiên |
| 4 | Bộ kỹ năng ngẫu nhiên mỗi trận | `assignSkills`: +`1 + cấp/10` kỹ năng (≤ 7), không trùng, cấp kỹ năng `1 + cấp/20`, ≤ 1 kỹ năng hỗ trợ (cấp ≤ 2), kho trừ 39/16/8. Log: `AILOG skills slot=10 role=49 lv=60 +7 34:4 2:4 33:4 19:4 18:4 9:4 31:p` |
| 5 | Pet do AI điều khiển | `petGate` (hook đầu `tick`/`handleKey`): khi menu hỏi lệnh cho sủng vật, AI trả lời và gọi `confirmCommand()` của game (cầu nối `ai$confirm`). Cấp cứu đồng đội < 30 % nếu có kỹ năng hồi; < 25 % HP không hồi được → thủ tới cùng (trung thành / chủ nguy / còn 1 quái / sắp được hồi) hoặc 60 % **rút lui** (rời trận còn sống, không gọi lại được trong trận); còn lại đánh như quái hoang dã + kết liễu/hợp kích theo mục tiêu của nhân vật chính; quyết định lại đúng lượt; thông báo ngắn ("X cấp cứu Y!", "X bị thương nặng, rút lui khỏi trận!", "X liều chết bảo vệ chủ nhân!") |
| 6 | Chỉnh không cần build | `ai/config.txt` trong JAR (24 khoá, `docs/RESOURCES.md`); cờ harness `-Dai.log=1`, `-Dai.enabled=0` |

#### 1a. Điều chỉnh sau khi đo (mỗi bước có số đo — `docs/AI_TEST_session8.txt`)
| Bản | Vấn đề đo được | Sửa |
|---|---|---|
| nháp 1 | Sàn ATK theo đòn thường: Quinn cấp 1 một mình và đội cấp 30/60 bị diệt trong 1–2 vòng — kỹ năng nhân `2×ATK` **trước** khi trừ DEF + tương khắc hệ (+50 %) làm đòn mạnh nhất gấp 5–10 lần dự tính | ATK theo **ngân sách** đòn mạnh nhất của chính quái |
| nháp 2 | Cấp 60: quái có hồi máu toàn đội cấp 4 dùng mỗi lượt + khống chế liên hoàn → bế tắc ~60 s rồi thua | ≤ 1 kỹ năng hỗ trợ cấp ≤ 2; hồi khi < 50 %; khống chế giảm 70 % khi đã có hero bị khống chế hoặc còn 1 hero |
| cuối−1 | 3/3 trận thua nhanh nhất (11–24 s) do kỹ năng 8 (15 % giết ngay) nhắm leader → thua tức thì | Bỏ 8 khỏi kho ngẫu nhiên; AI không dùng 8 lên leader |
| AI pet | Sói 14 % HP rút lui dù pet hồi máu đã định cứu nó cùng vòng | Có đồng đội định hồi cho mình → ở lại thủ (`healComing=true` trong log) |

**Số đo (người chơi giả lập chỉ bấm đánh thường — không kỹ năng/vật phẩm/hồi máu):**

| Cấp đội | 1.7.0 | 1.8.0 cuối |
|---|---|---|
| 1 (Quinn một mình, 1–3 quái) | 2/2 thắng, HP thấp nhất 62–100 % | 6/6 thắng, 61–92 % (trung vị 88 %) |
| 10 (Quinn + Julie) | 5/5 thắng, trung vị 100 % | 3/4 thắng, trung vị 86 % (trận thua do kỹ năng 8 — đã sửa sau đó) |
| 30 | 5/5 thắng, trung vị 77 %, ~21 s | 4/4 thắng, 65–77 % (trung vị 69 %), ~38 s |
| 60 | 5/5 thắng, trung vị 79 %, ~17 s | 1/2 thắng (66 %), 1 thua sau 68 s |

Sủng vật (bản phát hành, 5 ca): đủ máu thắng; Quinn 24 % → pet hồi máu cứu ngay (3 lần); sói 14–18 % + pet hồi đã định cứu → sói thủ, được hồi; cả hai pet ~15 % →
pet hồi tự cứu, sói **rút lui** (sau trận còn 218 HP, `isFainted=false`); 5/5 thắng. Bản 1.7.0: người chơi phải chọn lệnh cho cả 2 pet.

**Pipeline kiểm tra (bản cuối):**

| Bước | Kết quả |
|---|---|
| ECJ `-1.3 -target 1.1` + Peephole | 0 lỗi |
| `Verify` trước / sau ProGuard `-microedition` (`--require-stackmap`) | 0 lỗi cả 2 bản; không float/double, không `ldc` class |
| Patch8 khẳng định | 5 lời gọi `GameUtil.a`, đúng 1 vòng lập kế hoạch pha 2 (thoát sau `goto`), 3 `putfield actingSlot`, local 0 không bị gán lại, `confirmCommand` tồn tại |
| BinDiff 1.7.0 (`res.jar`) → 1.8.0 (`docs/BINDIFF_session8.md`) | khác duy nhất `Battle.class` (+`ai$confirm`, `createEnemyActors` +5 lệnh, `tick` +10, `handleKey` +2 — không lệnh gốc nào đổi), `MANIFEST.MF`; thêm `EnemyAI.class`, `ai/config.txt`; 968 entry còn lại (cả font của bạn) giống hệt |
| Quét cấp phát | `new` trong `EnemyAI` trên đường chạy trận chỉ ở nhánh log (`log=1`), `catch`, sự kiện hiếm (thông báo/rút lui/lọc danh sách); còn lại lúc tạo trận / đọc cấu hình |
| MicroEmulator: smoke bản phát hành (trận script 3 quái) | chạy hết, 0 ngoại lệ từ mã game |
| MicroEmulator: 4 mức cấp × nhiều trận, cũ và mới | bảng trên; 0 dòng `AILOG error` trong mọi lượt |
| MicroEmulator: kịch bản sủng vật (bản phát hành) | 5/5 ca đúng hành vi |
| Debug Battle auto-test (bản debug 1.8.0, `-Dai.enabled=0`) | **pass 114 / fail 0** (`unreachable=9`, `autoDone=true`) — như 1.7.0: SFX/hook phiên 1–7 không đổi |
| J2ME Loader / máy thật | ⚠️ chưa chạy |

### 2. Các file đã chỉnh sửa / tạo mới
* Mới: `src8/EnemyAI.java`, `tools/src/Patch8.java`, `build_s8.sh`, `res/ai/config.txt`, `res/font.bin` + `res/f18.bin` (của người dùng), `base/*.jar` (nền 1.7.0),
  `tools/sim/balance_sim.py`, `tools/test/me/gen_balance.py`, `tools/test/me/gen_pets.py`, `tools/test/me/s8/*.txt`, `docs/ENEMY_AI.md`, `docs/AI_TEST_session8.txt`, `docs/BINDIFF_session8.md`.
* Sửa: `tools/me/MeRun.java` (lệnh `sets`, `battle`, `probe` có chỉ số mảng), `docs/FLOWS_battle.md` (§7), `docs/RESOURCES.md`, `README.md`, `tools/README.md`, `.gitignore`.
* Gỡ: `dist/SVTH_SFX.jar`, `dist/SVTH_SFX_debug.jar` (+ `.jad`) — bản tên rối 1.7.0, không dựng lại ở 1.8.0.

### 3. Class đổi tên / thêm / xoá
* Thêm `EnemyAI`. Không đổi tên/xoá class nào.

### 4. Method / field (so bytecode với 1.7.0)
* `Battle`: `+ ai$confirm()V` (cầu nối `invokespecial confirmCommand`); hook trong `createEnemyActors()V`, `tick()V`, `handleKey(I)Z`.
* `Battle` field `actionPlans [[I`, `actingSlot I`, `enemyCount B`, `commandSlot I`, `targetCursor I`: `private` → package (chỉ field; không đổi descriptor; mọi truy cập cũ vẫn trong `Battle`).

### 5. Biến quan trọng mới (`EnemyAI`)
| Biến | Ý nghĩa |
|---|---|
| `projDmg[20]`, `ccPlanned[20]` | sát thương / khống chế đã định trên từng ô trong vòng (chống đánh thừa, không khống chế chồng) |
| `fLo/fHi/aLo/aHi`, `enemySide` | phe của người đang quyết định (quái hoặc sủng vật) |
| `ccLock` | đã có hero bị khống chế / còn 1 hero → hạn chế khống chế |
| `retreated[]`, `petBattle`, `lastStand[]` | uid sủng vật đã rút lui trong trận (chặn gọi lại), trận hiện tại, pet đã báo "quyết thủ" |
| khoá cấu hình | xem `docs/RESOURCES.md` / `res/ai/config.txt` |

### 6. Luồng thực thi cập nhật
`docs/FLOWS_battle.md` §7 (tạo quái → `adjustLevel`/`onEnemiesCreated`; pha 0 chọn lệnh → `petGate`; pha 2 → `planRound`; mỗi lượt → `onTurn`), `docs/ENEMY_AI.md` (chi tiết).

### 7. Tài nguyên đã chuyển đổi
| File | Trước | Sau | Lý do |
|---|---|---|---|
| `font.bin`, `f18.bin` | trong `res.jar` của người dùng | `res/` | giữ chỉnh sửa tay qua các bản dựng |
| `ai/config.txt` | — | mới, ASCII | chỉnh AI trong JAR |

### 8. Vấn đề còn tồn đọng
* ⚠️ Chưa chạy J2ME Loader / máy thật. Đo cân bằng dùng chỉ số hero **mô hình** (10 điểm/cấp chia đều + trang bị theo cấp) và người chơi chỉ đánh thường — số trận nhỏ
  (harness mất đồng bộ sau trận thua: dòng `nostart`). Cấp cao (60) với người chơi chỉ bấm đánh thường thua 1/2 trận: **cần chơi thật để chỉnh** `maxhit`/`rounds`/`hpmul` trong `ai/config.txt`.
* ⚠️ Đổi gameplay đáng kể (theo yêu cầu): quái mạnh hơn rõ (trận dài hơn ~2×, mất nhiều máu hơn), EXP thưởng theo cấp quái mới (bám TB đội), pet không còn do người chơi điều khiển. Đảo lại từng phần bằng `ai/config.txt` (`ai=0`, `scale=0`, `randskill=0`, `petai=0`, `enabled=0`).
* ⚠️ Sủng vật rút lui: không nhận EXP của trận; thanh máu ô đó vẫn có thể hiện trên bảng trạng thái (chưa kiểm hình từng khung).
* ⚠️ Lỗi chữ có sẵn: `TextBlock.parseColorTags` ném `StringIndexOutOfBounds` (đã được `try/catch` của game bắt) với một dòng thoại có `>` mà không có `<` — gặp khi harness bấm qua hội thoại sau trận thua; không do phiên 8.
* ⏸ Quái vẫn không bao giờ chí mạng (mã gốc chỉ xét `actingSlot < 9`). ⏸ Đòn thường SFX chưa đặt sớm (từ phiên 6).

### 9. Kế hoạch tiếp theo
* Người dùng chơi thử (máy/J2ME Loader nào) và báo độ khó ở các mốc cấp; chỉnh `ai/config.txt` theo cảm nhận.
* Có thể thêm: quái biết chí mạng, menu "Chiến thuật" cho sủng vật (tấn công / phòng thủ / hồi máu ưu tiên), hiển thị ô sủng vật đã rút lui.

## Phiên 7 — 2026-10-10 (UTC+7): SKILL_1 kêu lại khi pet hành động; viết lại bộ phát SFX theo chuẩn engine hiện đại

**Yêu cầu:** "Vẫn lỗi, nhân vật quinn dùng skill lên địch -> phát skill 01.wav, kết thúc hành động, pet hành động - wav skill 01 phát lại thêm phát nữa, kiểm tra lại, dc thì đập cái class đó đi xây lại chuẩn game aaa hiện đại đi, tham khảo tài liệu mạng".
**Cách hiểu:** "skill 01.wav" = `SKILL_1.wav` (giọng Quinn); "class đó" = bộ phát `BeepBackend` (+ vòng phát `SFXManager`); "tài liệu mạng" = mã nguồn J2ME Loader (nền tảng người dùng nhiều khả năng dùng) và khái niệm engine âm thanh game (SoundPool, Wwise, FMOD).

**Bản build (đều `Verify` 0 lỗi trước và sau preverify, StackMap đủ):** `dist/SVTH_SFX_renamed.jar` / `SVTH_SFX_debug_renamed.jar` **v1.7.0** (1 927 948 B / 1 945 414 B) — bản chính; `SVTH_SFX.jar` / `SVTH_SFX_debug.jar` cùng nội dung, tên rối.

### 1. Hạng mục đã xử lý

| # | Yêu cầu | Kết quả |
|---|---|---|
| 1 | SKILL_1 kêu lại khi pet hành động | **Nguyên nhân (có bằng chứng trong mã game)**: giọng SFX tra theo `roleId`; sủng vật có `roleId = petId` (`GameData.createPetSprite`) và quái tra cùng bảng pet, nên pet có petId 1 (hoặc 2/3/39/40) **mượn giọng Quinn (Julie)** → pet tung kỹ năng kêu đúng `SKILL_1.wav`. Sửa: `SfxHooks.voiceOf` — chỉ nhân vật chính (slot 0–9, `partyUid == 0`; pet luôn có `petUid ≥ 1`, xác nhận bằng bytecode `bn`: `e.a:S ← bl.c:S`) có giọng 1/2. Accessor mới `e.sfx$partyUid()` (Patch). Kèm: buff không phát lại SKILL ở frame trúng (chỉ MISS khi bị kháng); log SFX ghi `voice=`, CASEINFO của Debug Battle in `:u<partyUid>:v<giọng>` |
| 2 | "Đập class đó xây lại chuẩn hiện đại" | `BeepBackend` **viết lại**: asset → voice (`COLD/READY/PLAYING/DONE/WORK`) → chính sách theo sự kiện (SKILL/HEAL/nguyên tố 1 tiếng mỗi actor, HIT/MISS/CRIT chồng + gộp 40 ms, không cắt) → tập ấm (`warm=26`, ưu tiên giọng có mặt + tiếng hay dùng) → **hai thread**: phát (chỉ `start()` dưới khoá ngắn) và nạp (tạo / làm ấm / hạ ấm, việc nặng ngoài khoá). Giao diện công khai giữ nguyên |
| 3 | Vẫn trễ | **Nguyên nhân (đọc mã J2ME Loader)**: `realize/prefetch` không chuẩn bị MediaPlayer, `start()` mới `prepare()`, phát hết → `reset()` ⇒ ≤ 1.6.0 mọi tiếng prepare ngay lúc phát (`createPlayer` còn ghi file tạm + FFprobe). Sửa: **làm ấm** (âm lượng 0 → start → stop → tua 0 → trả âm lượng) từ màn logo và sau mỗi tiếng, trên thread nạp |

### 2. Kiểm chứng

| Kiểm | Kết quả |
|---|---|
| `Verify` + StackMap | 0 lỗi cả 4 JAR; không float/double |
| BinDiff 1.6.0 → 1.7.0 (`docs/BINDIFF_session7.md`) | class game khác duy nhất `BattleActor` (+ accessor `sfx$partyUid`, không đổi logic); còn lại class SFX, `DebugBattle` (debug), `sfx/config.txt`, MANIFEST |
| Khứ hồi tên | 928/0; 1.7.0 đặt tên ↔ tên rối: release **969/0**, debug **974/0** |
| MMAPI giả (`docs/AUDIO_TEST_session7.txt`) | bộ cũ 22/22 + cap + total + lat0/150/300 5/5 + **j2mel 6/6** (mô phỏng J2ME Loader, kịch bản có sủng vật): 0/48 tiếng phải prepare lúc phát, post→start 0 ms, buff 1 tiếng, SKILL giọng 0 của pet và quái sát nhau đều kêu, 0 tua/stop lên tiếng đang kêu. **Bộ phát 1.6.0 trên cùng mô phỏng: prepare trong `start()` 45/45, 48/51 tiếng trễ > 20 ms** — bộ thử ≤ 1.6.0 không mô phỏng reset-sau-khi-phát nên không bắt được lỗi này |
| Emulator smoke (bản đặt tên) | trận thật chạy hết, **11/11 SUMMARY OK**, 0 MISMATCH; log ghi đúng giọng (quái `voice=0`, Quinn `voice=1`); đặt sớm 6/6 nhịp kỹ năng (`frameMs` 46); ngoại lệ chỉ từ thiết bị âm thanh sandbox |
| Debug auto-test | **pass 114 / fail 0** (`unreachable=9`, 0 MISMATCH, `autoDone=true`) trên bản cuối; CASEINFO in `0:r1:u0:v1 1:r2:u0:v2 10:r133:u0:v0` (getter `partyUid` đọc đúng). Lượt đầu 112/2: kỹ năng 10 (hỗn loạn) ở ca ép trúng vẫn MISS vì mục tiêu miễn hiệu ứng — sự kiện giống hệt 1.6.0, do cách chấm mới quá chặt; đã nới (MISS ≤ số nhịp). Emulator không có thiết bị âm thanh (`players=0`, tone dự phòng) nên lượt này chứng minh hook / giọng / số nhịp; bộ phát mới được chứng minh bằng MMAPI giả |

### 3. File
* Viết lại: `src/BeepBackend.java`. Sửa: `src/SFXManager.java` (vòng phát gọn, thread nạp, `touchVoice`, khoá `warm`/`prime`), `src/SfxHooks.java` (`voiceOf`, buff), `src/SfxLog.java` (`voice=`), `debug/DebugBattle.java` (chấm ca buff, in giọng), `stubs/e.java`, `tools/src/Patch.java` (getter `sfx$partyUid`), `res/sfx/config.txt`, `build_jar.sh` (1.7.0), `tools/test/fake/.../{Manager,Player}.java` (chế độ J2ME Loader, tách lệnh làm ấm), `tools/test/SfxAudioTest.java` (`j2mel`), `tools/test/run_audio_test.sh`.
* Mới: `docs/AUDIO_TEST_session7.txt`, `docs/BINDIFF_session7.md`. Cập nhật: `docs/AUDIO_ENGINE.md` (§0), `README.md`, `docs/RESOURCES.md`.

### 4. Vấn đề còn tồn đọng
* ⚠️ Chưa thử trên máy/J2ME Loader thật; sandbox không có thiết bị âm thanh. Hành vi J2ME Loader lấy từ mã nguồn trên GitHub (nhánh master) — bản J2ME Loader cũ hơn có thể khác.
* ⚠️ Làm ấm = phát im lặng rồi dừng: máy bỏ qua VolumeControl có thể nghe tiếng "tách" (`prime=0`). Mỗi Player ấm giữ một AudioTrack: máy Android cũ có thể thiếu track khi `warm=26` + nhạc nền (giảm `warm`).
* ⏸ Đòn thường chưa đặt sớm (`lead` chỉ cho kỹ năng); tiếng "nở chậm" trong file không bù được.

### 5. Kế hoạch tiếp theo
* Người dùng cho biết nền tảng (J2ME Loader phiên bản nào / máy nào) và cảm nhận; nếu còn trễ, cân nhắc giảm `lead`/tăng `warm`, hoặc gom tiếng ngắn.

## Phiên 6 — 2026-10-10 (UTC+7): lấy bản đã đặt tên làm gốc, nhận 27 wav của người dùng, sửa tiếng skill "chồng loạn", giảm trễ SFX

**Yêu cầu:** "Bản này [SVTH_SFX_renamed.jar] chạy ổn định, lấy làm gốc, rebuild lại phần sfx, vẫn khá delay, tự suy luận phương án tốt nhất, có lỗi khiến âm thanh skill 1 2.wav loạn chồng lên nhau".
**Cách hiểu:** bản đã đặt tên là bản phát hành chính từ nay (`build_jar.sh` mặc định `RENAME=1`); "skill 1 2.wav" = `SKILL_1.wav` (Quinn) và `SKILL_2.wav` (Julie); sửa ở bộ phát SFX, không đổi gameplay, sát thương, animation, hay file âm thanh của người dùng.

**Bản build (đều `Verify` 0 lỗi trước và sau preverify, StackMap đủ):**
* `dist/SVTH_SFX_renamed.jar` / `SVTH_SFX_debug_renamed.jar` **v1.6.0** (1 924 606 B / 1 941 997 B) — **bản chính**.
* `dist/SVTH_SFX.jar` / `SVTH_SFX_debug.jar` v1.6.0 (1 880 675 B / 1 898 008 B) — cùng nội dung, tên rối gốc (đối chiếu).

### 0. Nhận chỉnh sửa tay của người dùng (§1.3)
JAR người dùng gửi khác bản 1.5.0 đúng **27 entry `sfx/*.wav`** (class giống hệt). Đã chép vào `res/sfx/` (commit riêng "Adopt user resource edits"); dựng lại `RENAME=1` 1.5.0 → **968/968 entry giống md5** với JAR người dùng. Định dạng an toàn: WAV PCM (tag 1) 16-bit mono 8 000/11 025/16 000 Hz, 3–35 KB, 0,2–1,1 s.

### 1. Hạng mục đã xử lý

| # | Yêu cầu | Kết quả |
|---|---|---|
| 1 | Lấy bản đã đặt tên làm gốc | `build_jar.sh`: `RENAME=1` mặc định, `VERSION=1.6.0`; JAR gốc của game chọn theo md5 (thư mục upload có cả bản người dùng); `roundtrip_check.sh` cũng vậy |
| 2 | Tiếng skill (SKILL_1/SKILL_2) loạn, chồng nhau | **Nguyên nhân chính (tái hiện được)**: bộ phát coi Player rảnh theo đồng hồ (`start + độ dài + 25 ms`) rồi tua về 0 — trên máy có độ trễ (J2ME Loader/Android) Player vẫn đang kêu nên **tiếng chạy lại từ đầu, chồng lên tiếng sau**, và lần `start()` sau trên Player đó thành lệnh rỗng. Ở độ trễ 150 ms: 72/93 lần tua rơi vào Player đang kêu, 21 `start()` lên Player đang kêu. **Phụ**: kỹ năng buff phát SKILL hai lần/lần tung (lúc tung + nhịp va chạm, tiếng 1 s cách ~0,4 s). **Sửa**: `getState()` quyết định "còn kêu"; chỉ tua Player đã dừng; SKILL/HEAL/nguyên tố một tiếng một lúc mỗi giọng (gọi lại khi còn kêu → bỏ qua, không chồng, **không cắt**) |
| 3 | Vẫn khá trễ | (a) không bao giờ tạo Player lúc phát khi bể đã có (trước đây create + realize + prefetch ngay lúc cần → tiếng tới muộn hàng chục–trăm ms); (b) nạp sẵn 36 Player **từ màn logo**, từng cái một, tiếng tới hạn luôn đi trước (trước đây nạp 27 Player khi vào trận đầu, chặn tiếng mấy giây đầu); (c) hẹn phát lại ≤ 1 lần trong 60 ms (trước 2 × 150 ms → bản muộn); (d) **đặt sớm tiếng va chạm của kỹ năng `lead` ms** (mặc định 60) bù độ trễ máy — frame va chạm biết trước (map.txt), trúng/né/chí mạng đã tính lúc tung, độ dài frame đo trực tiếp (emulator: 47 ms); (e) bỏ im lặng thật ở đầu wav trong bộ nhớ (SKILL_1: 41 ms); (f) HIT/MISS/CRIT trùng giọng trong 40 ms gộp một |
| 4 | Tự chọn phương án, chỉnh được | `sfx/config.txt` **kèm sẵn trong JAR** (sửa thẳng): `lead`, `solo`, `gap`, `trim` + khoá cũ. Đòn thường **chưa** đặt sớm (phải dự đoán nhịp theo luật `expectedBlows` — dễ lệch số nhịp) |

**Độ trễ có sẵn trong file** (thời điểm đạt 10% năng lượng): HIT_1 96 ms, SKILL_1 169 ms, SKILL_2 81 ms, HEAL 252 ms, các tệp khác 20–60 ms. Phần "nở chậm" này không phải im lặng nên mã không bù được — dùng tiếng có attack ngắn sẽ nhanh hơn rõ.

### 2. Kiểm chứng

| Kiểm | Kết quả |
|---|---|
| `Verify` + StackMap | 0 lỗi cả 4 JAR; không float/double; `play/post/onFrame/preFire` không cấp phát; Player chỉ được tạo lúc nạp sẵn hoặc lúc rảnh trên thread SFX (`preloadStep`/`maintain`), không bao giờ trong vòng lặp game hay đường phát (trừ khi một tiếng chưa có Player nào — trước khi nạp sẵn xong) |
| BinDiff bản người dùng → 1.6.0 (`docs/BINDIFF_session6.md`) | **mọi class game giống hệt**; khác: 6 class SFX, `MANIFEST.MF`, thêm `sfx/config.txt`; 27 wav giống bản người dùng |
| Khứ hồi tên | gốc → đặt tên → `inverse.map` 928/0; 1.6.0 đặt tên → đảo tên = 1.6.0 tên rối: release **969/0**, debug **974/0** |
| MMAPI giả (`docs/AUDIO_TEST_session6.txt`) | bộ cũ 22/22 (cập nhật 4 ca theo chính sách mới: nạp 36, gộp trong 40 ms, solo) + cap + total + **lat0/lat150/lat300 5/5 mỗi mức** (wav thật: buff 1 tiếng/lần tung; chuỗi HIT 871 ms đủ tiếng, post→start ≤ 15 ms; 200 sự kiện ngẫu nhiên → 0 tua/0 start lên Player đang kêu, 0 stop). Mã 1.5.0 trượt 3/5 ở lat150 |
| Emulator, smoke bản đặt tên | trận thật chạy hết, **11/11 SUMMARY OK**, ngoại lệ chỉ từ thiết bị âm thanh sandbox; `preFired = markEvents` (7/7 trên bản WIP, 2/2 trên bản cuối — trận ngẫu nhiên; mọi nhịp va chạm kỹ năng được đặt sớm), `frameMs` 46–47, `lead = 60` |
| Debug auto-test (bản debug đã đặt tên) | **112 ca + 2 kiểm bền: pass 114 / fail 0** (`unreachable=9`, 0 MISMATCH, `autoDone=true`; giống 1.5.0). Tập ngoại lệ giống mốc 1.5.0 (chỉ thiết bị âm thanh sandbox + bẫy debug cố ý). Chạy trên bản trước thay đổi một dòng `trBx = null` ở `battleEnd`; bản cuối chạy lại smoke + MMAPI giả + Verify + khứ hồi. Lưu ý: emulator không có thiết bị âm thanh (`players=0`, mọi tiếng đi đường tone dự phòng) nên lượt này chứng minh hook, sổ sách đặt sớm và số nhịp còn nguyên — hành vi bộ phát mới chỉ được chứng minh bằng MMAPI giả có độ trễ |

### 3. File đã chỉnh sửa / tạo mới
* Sửa: `src/BeepBackend.java` (`sounding/unstick`, `preloadStep`, solo/gap, không tạo Player lúc phát, `trimLead`), `src/SFXManager.java` (vòng phát + nạp từng bước, `ST_DUP`, hẹn 1×60 ms, khoá cấu hình mới, `lead`), `src/SfxHooks.java` (`preFire/fireMark`, đo `frameMs`), `src/AnimSfxMap.java` (`nextMarker`), `src/SfxUi.java` (`skipPrompt` khởi động SFX), `build_jar.sh`, `tools/roundtrip_check.sh`, `tools/test/run_audio_test.sh`, `tools/test/SfxAudioTest.java`, `tools/test/fake/javax/microedition/media/{Manager,Player}.java` (độ trễ, createPlayer chậm, wav header).
* Mới: `res/sfx/config.txt`, `res/sfx/*.wav` (27, của người dùng), `docs/AUDIO_TEST_session6.txt`, `docs/BINDIFF_session6.md`, `tools/test/me/renamed/smoke_pf.txt`. Cập nhật: `README.md`, `docs/AUDIO_ENGINE.md` (§2b), `docs/RESOURCES.md`.
* Không đổi: `Patch.java`, `DebugPatch.java`, mapping, mọi class game.

### 4. Vấn đề còn tồn đọng
* ⚠️ Chưa thử trên máy của người dùng; sandbox không có thiết bị âm thanh (emulator: `createPlayer` lỗi, `players=0`) → đường phát thật chỉ kiểm bằng MMAPI giả có độ trễ. **`lead=60` là mức thận trọng chưa hiệu chỉnh**: máy trễ ít hơn 60 ms sẽ nghe tiếng va chạm kỹ năng sớm vài chục ms.
* ⏸ Đòn thường chưa đặt sớm; tiếng lúc tung kỹ năng không đặt sớm được (là quyết định của logic).
* ⏸ Solo nghĩa là gọi lại SKILL/HEAL/nguyên tố trong lúc tiếng cũ còn kêu sẽ im (vd. hai quái cùng giọng 0 tung kỹ năng trong < 0,6 s).
* Các mục phiên 5 §3 (lỗi gameplay phát hiện khi đặt tên) vẫn chưa sửa.

### 5. Kế hoạch tiếp theo
* Người dùng cho biết máy/emulator đang chơi (J2ME Loader? máy Nokia?) và cảm nhận sau khi chỉnh `lead`; nếu cần, thêm dòng "Bù trễ SFX" trong menu (lưu RMS) để chỉnh bằng tai.
* Cân nhắc đặt sớm đòn thường khi có số liệu trên máy thật.

## Phiên 5 — 2026-10-10 (UTC+7): đặt tên khôi phục mã nguồn (class / field / hàm)

**Yêu cầu:** "Bắt đầu rename biến, đặt tên chức năng của các hàm để khôi phục mã nguồn".
**Đường chọn (§4):** đường A-lite — đổi tên **trên bytecode** bằng bảng ánh xạ (ASM `Remap`), áp **sau cùng** trong pipeline; `Patch.java`, `stubs/`, `src/` giữ tên gốc nên hook không đổi. Bản chuẩn 1.5.0 không đổi hành vi; bản đã đặt tên cung cấp thêm.

**Bản build (đều `Verify` 0 lỗi trước và sau preverify, StackMap đủ):**
* `dist/SVTH_SFX.jar` / `SVTH_SFX_debug.jar` **v1.5.0** (1 540 986 B / 1 558 319 B) — giống 1.4.0 từng byte trừ `MANIFEST.MF`.
* `dist/SVTH_SFX_renamed.jar` / `SVTH_SFX_debug_renamed.jar` **v1.5.0** (1 584 896 B / 1 602 287 B) — cùng nội dung, tên có nghĩa (+44 KB do tên dài).

### 1. Hạng mục đã xử lý

| # | Yêu cầu | Kết quả |
|---|---|---|
| 1 | Đổi tên class | **111/114** class có tên role-based (`GameCanvas`, `Battle`, `World`, `GameData`, `Equip`, `Skill`, `RoleSprite`, `PetInfo`, `MenuList`, `GameMenu`, `Utils`, `KeyMap`…); `Main`, `vh`, `fontcus` vốn đã đọc được |
| 2 | Đổi tên field / hàm | **1 626 field + 893 hàm** có tên (HIGH 2 248 / MED 335 / LOW 47 dòng, mỗi dòng có bằng chứng: khoá cấu hình, chuỗi, định dạng dữ liệu, nơi gọi, công thức); 40 thành viên chưa rõ giữ tên giữ chỗ `<cũ>_<kiểu>` (vd. `as.b_Image`, `bn.e_I`) |
| 3 | Tra ngược / truy vết | `mapping/reverse.tsv` (tên mới → gốc + độ tin cậy), `resolved.map`, `inverse.map`, `docs/RENAME_MAP.md` (bằng chứng), `docs/rename_notes/<cụm>.md` |
| 4 | Khôi phục mã nguồn đọc được | `restored/src/*.java` (Vineflower trên bản đã đặt tên, 114 file) + `restored/raw/` (tên giữ chỗ); biến cục bộ vẫn `varN` (JAR gốc không có LocalVariableTable). Không chứng minh biên dịch lại được — là tài liệu tham chiếu |

Công cụ mới: `tools/src/Remap.java` (+ `remap_check.sh`, `roundtrip_check.sh`, `gen_rename_doc.py`), `build_jar.sh RENAME=1`, `tools/test/me/{orig,renamed,translate.sh}`. Sửa `Verify`: tên class bắt đầu bằng `L` (vd. `LogoScreen`) bị nhận nhầm là descriptor `L…;`.
Phân việc: 6 agent đặt tên theo cụm (battle, data, world, ui_as, ui_ab, sdk) + phần lõi do chính phiên này làm; 4 agent bị giới hạn lượt gọi giữa chừng — phần đã ghi vẫn qua kiểm tra; cụm `data` chia lại cho 3 agent hoàn tất.

### 2. Kiểm chứng

| Kiểm | Kết quả |
|---|---|
| Quy tắc Java của `Remap` | khoá (owner,tên,desc); phân giải qua phân cấp; nhóm override; 27 nhóm ghi đè API CLDC/MIDP đóng băng; **mọi 39 542 lệnh field/hàm phân giải lại đúng thành viên cũ, 0 lỗi**; không trùng tên trong class; tên class không trùng (kể cả hoa/thường) |
| Khứ hồi (JAR gốc) | gốc → đặt tên → `inverse.map` → so với gốc: **928 giống / 0 khác** (`tools/roundtrip_check.sh`) |
| Khứ hồi (JAR 1.5.0) | bản đã đặt tên → `inverse.map` → so với bản chuẩn 1.5.0: **release 968/0, debug 973/0** (cả StackMap + md5 tài nguyên) (`docs/BINDIFF_session5.md`) |
| BinDiff 1.4.0 → 1.5.0 | 1 entry khác (`MANIFEST.MF`); mọi `.class`/tài nguyên giống hệt |
| Emulator, bản đã đặt tên (release, kịch bản smoke dịch tên) | `probe`/`callm` trúng đúng field/hàm mới; trận thật chạy hết, mọi `SUMMARY` SFX `OK`, 0 ngoại lệ từ code game (ngoại lệ còn lại là thiết bị âm thanh của sandbox, như các phiên trước); stack trace in tên mới (`GameCanvas.hideNotify`) |
| Debug auto-test (bản debug đã đặt tên) | **112 ca + 2 kiểm bền: pass 114 / fail 0** (`unreachable=9`, giống hệt v1.4.0; `DebugBattle.autoDone=true`) |
| Ngoại lệ trong log auto-test | bản chuẩn và bản đặt tên có cùng tập: `IllegalArgumentException` SourceDataLine ×276/×278 (chênh theo thời gian), `MidiUnavailableException` ×3, `Throwable` `trap cd.e()` ×1 (bẫy debug cố ý); không có frame code game nào ngoài chuỗi gọi nhạc/SFX → **ngoại lệ chỉ từ thiết bị âm thanh sandbox, giống bản chuẩn** (khung hình bản đặt tên in tên mới, vd `MusicPlayer.load`, `World.playSceneMusic`) |
| Tham chiếu chuỗi | không có `Class.forName`/`getName` trên class game (chỉ dò class nền tảng trong `c.b()`); tài nguyên truy cập bằng đường dẫn nên không bị ảnh hưởng |

### 3. Phát hiện khi đặt tên (chưa sửa — để các phiên sau, xem `docs/rename_notes/*.md`)

> ⚠️ **Báo cáo từ phân tích tự động (subagent đọc bytecode/decompile), chưa xác minh từng mục bằng `javap`.** Đã tự xác minh: *SMS thanh toán luôn thành công* (`bh.run()` gọi `cx.a(0,null)`, không đọc hai tham số chuỗi) ✅. Các mục còn lại ghi ⚠️ cho tới khi có phiên sửa kiểm lại.

* **Lưu game mất HP từ đá khảm**: `Equip.gemHp` (`ck.b_S`) không được ghi/đọc trong save (túi và trang bị đang mặc) → nạp lại mất +HP, ô khảm vẫn bị dùng.
* **Huỷ nhiệm vụ trong menu** xoá `activeQuests`/`activeQuestIds` nhưng không xoá `questDoneFlags` → mảng song song lệch.
* **`QuestDetailBox` (cs) vẽ thưởng trang bị** dùng `o.d_I` (bảng vật phẩm) thay `o.f_I` (bảng trang bị) → sai toạ độ/có thể crash.
* **`bl.isBlocked`** kiểm tile bằng hộp va chạm của bước trước thay vì tham số.
* **SMS thanh toán luôn "thành công"** (`SmsSendTask` không đọc nội dung/số, gọi `onResult(0,null)`); `SmsFeeConfig` là mã chết.
* Trang âm lượng gọi `Main.music.b(...)` không kiểm null (NPE nếu nhạc chưa bắt đầu); vòng "Game hay đề cử" kiểm `var2` nhưng tăng `var4`; `removeEquip` không tìm thấy id thì xoá món đầu; `resetGameData` không reset vài trường (điểm, tầng phụ bản…); `ck.loadTable` nối cột 9 hai lần; `Skill`/`Item` dùng sai `useItemInBattle` kiểm sống bằng giá trị vào trận.
* Mã chết: `BattleLoader`, `SmsFeeConfig`, `da.a_AI`, `cr` (chỉ field tĩnh), nhiều ảnh bị gán null.

### 4. File đã chỉnh sửa / tạo mới

| File | Thay đổi |
|---|---|
| `mapping/part_{core,battle,world,data_o,data_hold,data_cdi,ui_as,ui_ab,sdk}.map` | bảng đặt tên viết tay (có bằng chứng) |
| `mapping/{resolved.map,reverse.tsv,inverse.map}`, `mapping/README.md` | bảng sinh ra + mô tả định dạng |
| `tools/src/Remap.java`, `tools/{remap_check,roundtrip_check}.sh`, `tools/gen_rename_doc.py` | công cụ đổi tên + chứng minh khứ hồi + sinh tài liệu |
| `tools/src/Verify.java` | sửa nhận diện tên class bắt đầu bằng `L` |
| `build_jar.sh` | `VERSION` 1.5.0; `RENAME=1` thêm bước `Remap` trước ProGuard |
| `tools/test/me/*` | kịch bản MeRun gốc + bản dịch tên |
| `restored/src`, `restored/raw`, `restored/outline_raw.txt`, `restored/class_graph_raw.txt` | mã giải mã tham chiếu |
| `docs/RENAME_MAP.md`, `docs/rename_notes/*.md`, `docs/BINDIFF_session5.md`, `README.md`, `tools/README.md` | tài liệu |

### 5. Vấn đề còn tồn đọng

* ⚠️ Bản đã đặt tên **chưa chạy trên máy/KVM thật**; kiểm trên MicroEmulator + chứng minh bytecode tương đương (968/0). Bản giao chính vẫn là `SVTH_SFX.jar` (giống 1.4.0).
* 40 thành viên chưa đặt tên (cố ý — không đủ bằng chứng) và ~47 dòng LOW cần xem lại khi gặp bằng chứng mới.
* Biến cục bộ/tham số vẫn `varN`; `restored/src` là kết quả giải mã (có thể sai ở vài hàm khó, như mọi decompiler) — đối chiếu bytecode khi sửa logic.
* `Patch.java`, `stubs/`, `src/` dùng tên gốc: muốn đổi sang tên mới phải sửa cả `Patch.java` (cố tình chưa làm để không đụng hook đã kiểm).
* Giới hạn Phiên 1–4 giữ nguyên.

### 6. Kế hoạch tiếp theo

1. Đọc `restored/src` theo cụm, nâng LOW lên MED/HIGH, đặt nốt 40 thành viên; thêm tên biến cục bộ ở những hàm cần sửa.
2. Sửa các lỗi ở §3 (từng cái một, mỗi cái kèm kịch bản chứng minh) khi người dùng đồng ý đổi hành vi.
3. Khi đủ tin cậy: chuyển `Patch.java`/`stubs`/`src` sang tên mới (dùng `reverse.tsv`) và cho phép dựng từ nguồn đã đặt tên.

## Phiên 4 — 2026-10-10 (UTC+7): SFX độ trễ thấp, chồng tiếng không cắt, nhạc nền tự hạ khi SFX phát

**Bản build:**
* `dist/SVTH_SFX.jar` **v1.4.0** (1 540 986 B, 968 entry) + `.jad`, `Verify` 126 class 0 lỗi trước và sau preverify, StackMap đủ.
* `dist/SVTH_SFX_debug.jar` **v1.4.0** (1 558 319 B, 973 entry) + `.jad`, `Verify` 131 class 0 lỗi.
**Nền:** bản 1.3.0 (commit `0a79a66`). Không đổi gameplay/sát thương/animation, không thêm nhạc nền, không đưa âm thật vào game, **không thêm hook mới vào class game** (75/105 site như 1.3.0). Chi tiết thiết kế: `docs/AUDIO_ENGINE.md`.

### 1. Hạng mục đã xử lý

| # | Yêu cầu | Kết quả |
|---|---|---|
| 1 | Âm thanh không delay như game hiện đại | Lúc phát chỉ còn `start()` trên Player đã prefetch + đã tua về 0 (trước đây mỗi lần: `getState`, `stop`, `setMediaTime`, `getControl`, `setLevel`, `start`). Việc nặng (tạo Player, tua lại, nạp thêm) chuyển sang nền. `VolumeControl` lấy một lần, `setLevel` chỉ khi âm lượng đổi. Thread SFX `NORM_PRIORITY+2`. CRIT/HEAL phát **đồng thời** với HIT (trước đây trễ 35/60 ms; `critdelay`/`healdelay` đặt lại được). Đo trên JVM (MMAPI giả): post→`start()` trung vị ~0,2 ms, p95 ~0,5 ms |
| 2 | Nhiều tiếng chồng lên nhau | Bể 1–3 Player/(loại, giọng) (`pool=3`), tổng ≤ 40 (`players=`), tự lớn khi thấy chồng (nạp nền ngay sau lần phát cuối cùng còn Player rảnh); 3 tiếng cùng loại start trong ~20 µs trên 3 Player khác nhau; 5 loại khác nhau cùng lúc đều start. `maxvoices` 3 → 12 (1–16) |
| 3 | Không âm nào bị cắt | Bỏ mọi đường `stop()` để nhường chỗ (trước: tiếng thứ hai cùng loại stop tiếng đầu). `flushAll()` (hết trận/hủy animation) chỉ xoá sự kiện **chưa phát**; `stopAll()` chỉ ở tạm dừng game / tắt SFX / âm lượng 0. Bể đầy: hẹn phát lại khi Player rảnh (≤ 2 lần, ≤ 150 ms), nếu vẫn đầy mới bỏ tiếng **mới** (log `DROPPED`); tiếng đang phát không bị đụng. 300 sự kiện ngẫu nhiên → 0 `stop()`, 0 `start()` lên Player đang phát |
| 4 | Nhạc nền tự giảm âm lượng khi SFX phát | Mới `BgmDuck`: ngay sau khi SFX bắt đầu, `Main.music.b(o.g × duck%)` (mặc định 45%); giữ 200 ms sau tiếng cuối; trả lại 4 bước trong 150 ms; SFX đến giữa chừng hạ lại ngay; tôn trọng cài đặt nhạc (tắt / âm lượng 0 / chưa có nhạc → không đụng); `stopAll()` trả đầy ngay |

Sửa kèm: (a) `SFXManager.init()` chỉ nạp `config.txt` một lần (trước: mỗi trận nạp lại có thể đè âm lượng người chơi chỉnh); (b) tiếng thử ở menu "Cài đặt SFX" trước đây chỉ phát được nếu đã vào trận (thread SFX chưa chạy) — nay mở trang SFX / `preview()` tự khởi động thread và nạp sẵn Player.

### 2. Kiểm chứng

| Kiểm | Kết quả |
|---|---|
| Build | `Verify` + `--require-stackmap` 0 lỗi cả hai JAR; không float/double (CLDC 1.0); release không chứa `DebugBattle` |
| `BinDiff` vs JAR gốc | 919/928 entry giống hệt; class game đổi như 1.3.0; `at/o/bv/Main` không đổi (`docs/BINDIFF_session4.md`) |
| `tools/test/run_audio_test.sh` (JVM, MMAPI giả) | **24/24 PASS** (`docs/AUDIO_TEST_session4.txt`): độ trễ; chồng 3 tiếng cùng loại và 5 loại khác nhau; 7 tiếng vượt bể → 3 + hẹn lại, 0 stop; flush không dừng, stopAll dừng; bão 300 sự kiện 0 stop; hạ 80→36 ngay sau start, trả 36→46→57→68→80 sau tiếng + 200 ms; không đụng nhạc khi tắt / `o.g=0` / `duck=100` / không có nhạc; createPlayer lỗi sau 30 Player vẫn chạy; trần `players=29` được giữ |
| Emulator, nhạc thật | Cover đang phát nhạc `bv`: `Main.music` volume byte 80 → **36** khi SFX phát → **80** sau giữ + trả; `stopAll` trả 80 ngay |
| Emulator, trận thật (`-Dsfx.log=1`) | 3 vòng SFX 70 / 0 / 70: `API_CALLED` 8 → 8 → 16, 11 `OFF` ở vòng 0, 11/11 `SUMMARY` OK; `duckCalls` tăng đúng theo số lần phát, không tăng khi SFX 0 |
| Debug auto-test (v1.4.0) | **112 ca + 2 kiểm bền: pass 114 / fail 0** (`unreachable=9`) |

### 3. File đã chỉnh sửa / tạo mới

| File | Thay đổi |
|---|---|
| `src/BeepBackend.java` | viết lại: bể Player, đọc WAV một lần, độ dài từ header, `maintain()` tua/nạp thêm nền, `busy()`, `retryAt`, `stopAll()` chỉ khi được gọi |
| `src/SFXManager.java` | vòng chạy mới (hàng đợi + việc nền + hẹn lại), `flushAll`/`stopAll`, `maxVoices=12`, `critDelay/healDelay`, khoá cấu hình mới, `configLoaded`, `preview()` tự khởi động |
| `src/BgmDuck.java` | mới |
| `src/SfxHooks.java`, `src/SfxUi.java`, `debug/DebugBattle.java` | `stopAll` ở tạm dừng/thoát/âm lượng 0; CRIT/HEAL theo `critDelay/healDelay`; `SFXManager.init()` khi mở trang SFX |
| `stubs/bv.java`, `stubs/Main.java` | stub biên dịch (không đóng gói) |
| `tools/test/*` (`SfxAudioTest`, `fake/*`, `run_audio_test.sh`) | mới |
| `docs/AUDIO_ENGINE.md`, `docs/AUDIO_TEST_session4.txt`, `docs/BINDIFF_session4.md`, `docs/{RESOURCES,SFX_MAP,FLOWS_battle}.md`, `README.md`, `tools/README.md` | tài liệu |

### 4. Vấn đề còn tồn đọng

* ⚠️ **Độ trễ 0,2 ms chỉ là phần mềm** (post → gọi `start()`) đo trên JVM với MMAPI giả. Độ trễ `start()` → loa, và việc **máy có trộn được nhiều `Player` cùng lúc hay không**, phụ thuộc thiết bị; sandbox không có thiết bị âm thanh nên **chưa kiểm trên máy/KVM thật**. Máy chỉ phát được một tiếng một lúc sẽ tự cắt/từ chối tiếng thứ hai dù mã không cắt: đặt `pool=1`, `critdelay=35`, `healdelay=60` trong `/sfx/config.txt` để quay về nối tiếp.
* ⚠️ Máy yếu có thể không chịu nổi tới 40 Player: bể ngừng lớn khi `createPlayer` lỗi (đã kiểm với giả lập hết Player) nhưng nếu thấy nhạc nền bị đói tài nguyên thì giảm `players=`/`pool=`.
* Ducking dựa vào `VolumeControl` của bộ phát nhạc; trên máy mà `VolumeControl` của MIDI không có tác dụng thì không nghe thấy hạ nhạc (không lỗi).
* Wav kèm sẵn vẫn rỗng 50 ms → chưa có tiếng thật; ducking vẫn chạy theo độ dài file nên nhạc vẫn bị hạ ~0,45 s mỗi tiếng dù wav im lặng — nếu không muốn thì `duck=100` hoặc thay wav.
* Giới hạn Phiên 1–3 giữ nguyên (marker heuristic, thoát debug = thoát app, 9 kỹ năng bị động, SFX không lưu qua lần chạy, dòng SFX chỉ ở menu trong game…).

### 5. Kế hoạch tiếp theo

1. Thay 27 wav bằng tiếng thật, nghe thử trên máy/KVM thật: kiểm tra có trộn được nhiều Player không, độ trễ thật, và ducking có nghe được không; chỉnh `pool`, `duck`, `duckhold`.
2. Nếu máy không trộn được: thử một thiết bị khác hoặc dựng bộ trộn PCM riêng (cộng mẫu thành một luồng) — chi phí CPU/bộ nhớ lớn hơn, chỉ làm nếu cần.

## Phiên 3 — 2026-10-10 (UTC+7): tách SFX khỏi nhạc, dòng menu "Cài đặt SFX", bỏ hộp thoại hỏi nhạc

**Bản build:**
* `dist/SVTH_SFX.jar` **v1.3.0** (1 535 569 B, 966 entry) + `.jad`, `Verify` 124 class 0 lỗi trước và sau preverify, StackMap đủ.
* `dist/SVTH_SFX_debug.jar` **v1.3.0** (1 552 899 B, 971 entry) + `.jad`, `Verify` 129 class 0 lỗi.
**Nền:** bản 1.2.0 của Phiên 2 (commit `dd9d097`). Không đổi gameplay/sát thương/animation, không thêm nhạc nền, không đưa âm thật vào game.

### 1. Hạng mục đã xử lý

| # | Yêu cầu | Kết quả |
|---|---|---|
| 1 | Tách music và SFX | SFX không còn đọc `at.b`/`o.g` (công tắc/âm lượng nhạc). Có công tắc/âm lượng riêng `SFXManager.volume` (0..100, 0 = tắt), thread phát riêng như trước. Nhạc 0 + SFX 70 vẫn có SFX và ngược lại |
| 2 | Thêm dòng cài đặt SFX dưới cài đặt nhạc trong menu | Menu hệ thống: `... Cài đặt nhạc / Cài đặt SFX / Tải điểm QQ / Thoát game`. Mở trang âm lượng của game (thanh 10 nấc, trái/phải ±10%, 0 = "Tắt", có tiếng thử khi tăng) nhưng chỉnh SFX; nhãn phụ "Hiệu ứng (SFX)" phân biệt với trang nhạc. Máy không có mục nhạc (`allowMusic=0`) thì dòng SFX chèn trước "Thoát game" |
| 3 | Loại bỏ hộp thoại hỏi nhạc lúc vào game | Hai chỗ `getstatic at.a:Z` trong màn logo `av` bị thay bằng `SfxUi.skipPrompt()`: đặt `at.b = true; o.g = at.d` (đúng như câu trả lời "Có" của game gốc) rồi đi thẳng `showCover()` |
| 4 | Âm thanh tự bật khi vào game | Nhạc: bật, âm lượng mặc định 80 (`at.d`). SFX: bật, 70, mỗi lần mở game. Không lưu cài đặt SFX qua lần chạy sau (nhạc cũng không lưu ngoài bản save của game) |

### 2. Kiểm chứng (MicroEmulator 2.0.4)

| Kiểm | Kết quả |
|---|---|
| Build | `Verify` + `--require-stackmap` 0 lỗi cả hai JAR; 27 wav trong mỗi JAR; release không chứa `DebugBattle` |
| `BinDiff` vs JAR gốc | 919/928 entry giống hệt; class game đổi: `bn bx da e` + **`av as ab`** (+`cd ci` ở debug); **`at.class`, `o.class` không đổi** (`docs/BINDIFF_session3.md`) |
| Không còn hộp thoại | Release: từ logo qua cover → "Chơi mới" không có màn "Bật âm nhạc?" (kịch bản cũ còn phím `-6` vô hại). Ngay khi vào game: `at.b = true`, `o.g = 80`, `SFXManager.volume = 70` |
| Menu | Ảnh `screenshots/session3/menu_sfx_rows_and_pages.png`: dòng "Cài đặt SFX" nằm ngay dưới "Cài đặt nhạc"; trang SFX có nhãn "Hiệu ứng (SFX)", icon loa và thanh 10 nấc |
| Độc lập | Probe: trang SFX phải → 80, trái ×3 → 50, ×10 → 0 ("Tắt"), phải → 10, **`o.g` giữ 80**. Qua trang nhạc, trái → `o.g = 70`, **`SFXManager.volume` giữ 10**. `SfxUi.mode` về 0 khi thoát trang |
| Trận thật (release, `-Dsfx.log=1`) | Vòng 1 (SFX 70): 8 bản ghi → 8 `API_CALLED`. Vòng 2 (SFX 0, nhạc vẫn 80): +11 bản ghi, **API không tăng** (11 `OFF`). Vòng 3 (SFX 70): +11 bản ghi, +11 `API_CALLED`. 11/11 `SUMMARY` OK, 0 ngoại lệ code game |
| Debug auto-test (v1.3.0, vào bằng luồng không có hộp thoại) | **112 ca + 2 kiểm bền: pass 114 / fail 0** (`unreachable=9`), `SFXManager.volume = 70` trước/sau |

### 3. File đã chỉnh sửa / tạo mới

| File | Thay đổi |
|---|---|
| `src/SfxUi.java` | mới: `skipPrompt`, `menuRows`, `isSoundRow`, `page`, `vol`, `on`, `key`, `paint` (mọi hàm bắt `Throwable`) |
| `src/SFXManager.java` | bỏ `gameSoundOn()`/`ignoreGameSwitch`; preload khi `enabled && volume > 0`; thêm `preview()` |
| `debug/DebugBattle.java` | bỏ `ignoreGameSwitch` |
| `tools/src/Patch.java` | `patchUi()`: hook `av`, `as`, `ab` (có khẳng định số site: 2 `at.a:Z`, 2 `putstatic as.a`, 1 `equals("Cài đặt nhạc")`, 2 `o.g` + 1 `at.b` trong `as.h`…) |
| `stubs/{at,o,as,vh}.java` | stub biên dịch (không đóng gói) |
| `tools/me/MeRun.java` | lệnh `setf`, `spam` |
| `build_jar.sh` | `VERSION` 1.3.0 |
| `README.md`, `docs/{RESOURCES,SFX_MAP,FLOWS_battle,BINDIFF_session3}.md`, `tools/README.md`, `screenshots/session3/` | tài liệu |

### 4. Class / method / biến / luồng

* Class mới: `SfxUi`. Class game vá thêm: `av` (2 site), `as` (+accessor `sfx$icon`, 9 site), `ab` (2 site). Tổng hook 75 (release) / 105 (debug).
* Biến: `SfxUi.mode` (0 = trang âm lượng đang chỉnh nhạc, 1 = SFX); `SFXManager.volume` là âm lượng SFX duy nhất.
* Luồng chi tiết: `docs/FLOWS_battle.md` §6.

### 5. Vấn đề còn tồn đọng

* ⚠️ Vẫn **chưa chạy máy/KVM thật**; sandbox không có thiết bị âm thanh (`Player` không tạo được → `players=0 createFailed=27`, rơi về `playTone`), nên `PLAYED` và việc đọc 27 wav (rỗng hoặc có tiếng) chưa kiểm được. Bản này vẫn **im lặng** nếu wav còn rỗng.
* Không còn cách bật câu hỏi nhạc; nhạc chỉ tắt bằng "Cài đặt nhạc" (âm lượng 0) hoặc từ bản lưu cũ có cờ nhạc tắt (`at.b = false` nạp từ save: vào "Cài đặt nhạc" tăng âm lượng để bật lại — hành vi gốc của game).
* Trang SFX dùng chung bố cục trang nhạc nên tiêu đề ảnh vẫn là "Âm lượng"; phân biệt bằng nhãn "Hiệu ứng (SFX)" vẽ thêm (vị trí cố định theo giữa màn hình, đã xem ở 240x320; chưa xem ở màn hình khác).
* Chỉ thêm dòng ở menu hệ thống **trong game**; màn cover vẫn chỉ có "Cài đặt" (nhạc) của game, chưa có SFX.
* Cảm ứng (pointer) trên trang SFX không làm đổi âm lượng (game gốc cũng không làm vậy cho trang nhạc).
* Các giới hạn Phiên 1–2 (heuristic marker, thoát debug = thoát app, 9 kỹ năng bị động…) giữ nguyên.

### 6. Kế hoạch tiếp theo

1. Thay 27 wav bằng tiếng thật, nghe thử trên máy/KVM thật; chỉnh `maxvoices`, độ ồn tiếng thử.
2. Quyết định có lưu cài đặt SFX qua lần chạy (RMS riêng) và có thêm dòng SFX ở màn cover hay không.
3. Tinh chỉnh marker va chạm (`map.txt`) theo tai nghe.

## Phiên 2 — 2026-10-10 (UTC+7): SFX theo công tắc nhạc của game + 27 file .wav im lặng gắn sẵn

**Bản build:**
* `dist/SVTH_SFX.jar` **v1.2.0** (1 534 564 B, 965 entry) + `.jad`, `Verify` 123 class 0 lỗi trước và sau preverify, StackMap đủ.
* `dist/SVTH_SFX_debug.jar` **v1.2.0** (1 551 937 B, 970 entry) + `.jad`, `Verify` 128 class 0 lỗi.
**Nền:** bản 1.1.0 của Phiên 1 (commit `813a41c`). Không đổi hook trong class game, không đổi gameplay/sát thương/animation, không thêm nhạc nền, không đưa âm thật vào game.

### 1. Hạng mục đã xử lý

| # | Yêu cầu | Kết quả |
|---|---|---|
| 1 | Gắn SFX vào công tắc nhạc của game | `SFXManager.gameSoundOn()` đọc cờ tĩnh `at.b` (cũng là cờ chặn `Main.playMusic`; đo `probe at b:boolean`: `true` khi chọn "Có", `false` khi chọn "Không") **tại lúc phát** → đổi "Cài đặt nhạc" giữa trận có hiệu lực ngay. Nhạc tắt: `post()` ghi `OFF`, xả hàng đợi, **không gọi API**, và `init()` **không preload 27 `Player`** (giảm rủi ro tranh tài nguyên MMAPI với nhạc nền). Không vá class `at` (`BinDiff`: `at.class` giống hệt gốc); stub `stubs/at.java` chỉ để biên dịch |
| 2 | 27 file .wav rỗng có tên để thay tiếng | `res/sfx/<TÊN>_<v>.wav`, TÊN ∈ HIT MISS CRIT SKILL FIRE WATER EARTH WIND HEAL, v ∈ 0 quái/sủng vật, 1 Quinn, 2 Julie. WAV PCM 8-bit mono 8 kHz, 50 ms im lặng (444 B/tệp). Sinh bằng `tools/gen_silent_wavs.py` (`--force` để tạo lại). Cả hai JAR đều chứa đủ 27 tệp. Bảng tên ↔ lúc phát ↔ gợi ý định dạng: `docs/RESOURCES.md`. Theo lựa chọn của bạn: **có file là dùng file ⇒ im lặng cho tới khi thay** |
| 3 | Debug không bị công tắc nhạc cản | `SFXManager.ignoreGameSwitch` do `DebugBattle.enter()` bật, `resetFlags()/exit()` tắt, `dirtyFlags()` kiểm. Release không có mã nào đặt cờ này |

**Kiểm chứng (MicroEmulator 2.0.4, `-Dsfx.log=1`):**

| Kiểm | Kết quả |
|---|---|
| `Verify` + `--require-stackmap`; JAD khớp byte; `unzip -l` | 123/128 class 0 lỗi; 27 wav trong mỗi JAR; size JAD = size thật |
| `BinDiff` vs JAR gốc | 922/928 entry giống hệt; đổi: MANIFEST, `bn bx da e` (+`cd ci` ở debug), `fontcus`; **`at.class` không đổi** (`docs/BINDIFF_session2.md`) |
| Release, trả "Có" ("Bật âm nhạc?" → `key -6`): `at.b = true` | trận thật: 25 bản ghi `API_CALLED`, có dòng `SFXLOG BACKEND` (preload), 11/11 `SUMMARY` OK |
| Release, trả "Không" (`key -7`): `at.b = false` | cùng kịch bản: **27 bản ghi `OFF`, 0 `API_CALLED`, không có dòng `BACKEND` (không preload)**, 11/11 `SUMMARY` OK (sự kiện hook vẫn đếm đúng) |
| Debug auto-test với câu nhạc trả "Không" (`at.b = false`) | **112 ca + 2 kiểm bền: pass 114 / fail 0** (nhờ `ignoreGameSwitch`) |
| Thoát debug | `SFXTEST EXIT flagsClean=true`; `probe`: `ignoreGameSwitch=false`, `invincible=true`, `forceHit=-1`, `passive=true`, `SFXManager.enabled=true`, `injectFailure=false` |

### 2. Các file đã chỉnh sửa / tạo mới

| File | Thay đổi |
|---|---|
| `src/SFXManager.java` | `gameSoundOn()`, `ignoreGameSwitch`, gate trong `post()`, preload có điều kiện |
| `debug/DebugBattle.java` | đặt/xoá `ignoreGameSwitch`, thêm vào `dirtyFlags()` |
| `stubs/at.java` | mới (chỉ biên dịch) |
| `res/sfx/*.wav` (27), `tools/gen_silent_wavs.py` | mới |
| `build_jar.sh` | `VERSION` 1.2.0 |
| `README.md`, `docs/RESOURCES.md`, `docs/SFX_MAP.md` §5, `docs/BINDIFF_session2.md`, `GIT_LOG_session2.txt` | tài liệu |

### 3–7. Class / method / biến / luồng / tài nguyên

* Class: không đổi tên/xoá. Không thêm class mới (chỉ thêm method vào `SFXManager`). Class game vá giữ nguyên như Phiên 1.
* Biến mới: `SFXManager.ignoreGameSwitch` (debug), phương thức `gameSoundOn()`.
* Luồng: `post()` → (enabled/volume) → **công tắc nhạc `at.b`** → hàng đợi; `init()` chỉ preload khi công tắc bật.
* Tài nguyên: +27 `/sfx/<TÊN>_<v>.wav` (xem `docs/RESOURCES.md`).

### 8. Vấn đề còn tồn đọng

* ⚠️ Vẫn **chưa chạy máy/KVM thật**. Sandbox không có thiết bị âm thanh nên **chưa thấy `Player` đọc 27 wav rỗng và phát (`PLAYED`)**; chỉ thấy tên tệp có trong JAR và đường `API_CALLED`/`OFF`. Cơ chế "có tệp thì dùng tệp" đã có từ Phiên 1 nhưng **chưa được thử bằng tệp có tiếng thật** — hãy thay một tệp rồi nghe thử trên thiết bị.
* ⚠️ **Bản này im lặng hoàn toàn** cho tới khi bạn thay wav (beep chỉ còn khi thiếu tệp, hoặc khi `Manager.createPlayer` lỗi trên máy → rơi về `playTone`).
* Người chơi tắt nhạc ⇒ SFX cũng tắt (không có công tắc riêng cho SFX trong game; `enabled`/`volume` ở `/sfx/config.txt`). Nếu muốn SFX độc lập với nhạc nền: bỏ dòng `gameSoundOn()` trong `post()`/`init()`.
* Các giới hạn Phiên 1 (heuristic marker, thoát debug = thoát app, 9 kỹ năng bị động…) giữ nguyên.

### 9. Kế hoạch tiếp theo

1. Thay 27 wav bằng tiếng thật, nghe thử trên máy/KVM thật (độ trễ, số `Player` đồng thời, xung đột MIDI nền); chỉnh `maxvoices`.
2. Tinh chỉnh marker va chạm (`map.txt`) theo tai nghe.
3. Quyết định có cho SFX một công tắc riêng, độc lập nhạc nền.

## Phiên 1 — 2026-10-10 (UTC+7): SFX beep đồng bộ animation + Debug Battle / SFX Test Arena

**Bản build:**
* `dist/SVTH_SFX.jar` **v1.1.0** (1 530 407 B, 938 entry) + `.jad` — bản phát hành, CLDC-1.0 / MIDP-2.0, class 45.3 (123/123), `Verify` 123 class 0 lỗi trước và sau preverify, mọi entry Deflate, StackMap đủ.
* `dist/SVTH_SFX_debug.jar` **v1.1.0** (1 547 737 B, 943 entry) + `.jad` — thêm Debug Battle, `Verify` 128 class 0 lỗi.

**Nền:** JAR do người dùng gửi (`d6030039-…Đại_Lục_Thần_Điểu.jar`, 928 entry, 114 class bị làm rối). Không có chỉnh sửa tay trước đó.
**Đường patch:** B (hook ASM + class mới) — class game tên 1–2 ký tự, field/method trùng tên khác kiểu, không thể viết Java trỏ trực tiếp. Không tái cấu trúc mã nguồn.
**Commit:** `7494701` (toolchain, parser `.av`) → `2def9e7` (SFX skeleton, pipeline) → `720af66` (Debug Battle, auto-test) → `d7bf16a` (tài liệu) → `789a85c` (README tools) → `61b55f1` (thoát debug = thoát ứng dụng) → commit bàn giao cuối cùng (`git log`).

### 1. Hạng mục đã xử lý

| # | Yêu cầu | Kết quả |
|---|---|---|
| A1 | Loại SFX: Hit, Miss, Critical (chồng Hit), Skill, Fire/Water/Earth/Wind, Heal | `SFXType` 9 loại. CRIT là lớp trễ 35 ms phát cùng HIT; nguyên tố phát ở nhịp kỹ năng cùng HIT/MISS; HEAL ở nhịp marker. "Thổ" của yêu cầu = **Địa** (EARTH) theo `skill.txt` |
| A2 | Nhiều lớp, ưu tiên, giới hạn voice | Hàng đợi 32 ô + một thread; ưu tiên CRIT 5 > SKILL/nguyên tố/HEAL 3 > HIT 2 > MISS 1; `maxvoices` mặc định 3 (1–8), hết voice thì đuổi voice ưu tiên thấp |
| A3 | Mỗi đòn liên hoàn / mỗi nhịp kỹ năng có sự kiện riêng | Quinn đòn thường **3/3** nhịp (frame 7, 12, cuối đòn); skill 4 cấp 1/5/9 = **2/3/4** nhịp, đo 2/2, 3/3, 4/4 |
| A4 | Đồng bộ frame animation, không phụ thuộc paint | Hook tại chuyển trạng thái/frame trong tick logic (`bn.f()`, `bx.a(Z)`); khử trùng theo (slot, frame) → 0 sự kiện trùng khi logic cập nhật cùng frame (đo `dupSuppressed` 2–6/ca, 0 sự kiện thừa); tự theo tốc độ/tạm dừng thật |
| A5 | Hủy / chết / hết animation / hết trận → xoá sự kiện chờ | `flushActor` khi về trạng thái 0/128; `flushAll` ở `battleEnd` và `da.hideNotify`. Kiểm: `robustness flush` và `flushActor` (PASS) |
| A6 | Quinn (thương) và Julie (súng) cấu hình riêng | `AnimSfxMap.variantOf`: role 1 → thương (tông trầm), 2/3/39/40 → súng (tông cao), còn lại 0. Game không có animation thương/súng riêng — phân theo nhân vật (xem `docs/SFX_MAP.md` §4.5) |
| A7 | Thay âm thật không động logic | `/sfx/<TÊN>_<v>.wav` hoặc `/sfx/<TÊN>.wav` trong JAR được ưu tiên hơn beep (`docs/RESOURCES.md`) |
| A8 | Không cấp phát trong vòng animation; lỗi SFX không phá lượt | Quét `javap -c`: hook (`onState/onBlow/onFrame/onSkillApply/fire`) và `SFXManager.post` **0** `new`; cấp phát chỉ ở `static{}`, `init`, nạp map, preload `Player`, và nhánh in log. `catch Throwable` mọi hook; ca "SFX ném lỗi" (xem B-test) trận vẫn chạy y hệt |
| A9 | Bật/tắt, âm lượng | `/sfx/config.txt` (`enabled`, `volume`, `maxvoices`), `SFXManager.setEnabled/setVolume`, hàng menu debug. **Không** gắn vào menu "Cài đặt nhạc" của game (xem §8) |
| B1 | Debug Battle qua hệ thống chiến đấu thật, vào nhanh | Bản debug, `#` ở màn cover → ván mới, Julie vào đội, 3 quái thật (role 133) bằng đường script "战斗" của game (`cd.b(String[],0)`) |
| B2 | Hai bên bất tử nhưng sự kiện vẫn chạy | Kẹp HP ≥ 1 sau tick (`clamp`) nên hit/crit/nguyên tố/hồi vẫn nổ. Đo: HP quái 1/1, hai hero đầy HP, `HP<1` = 0 lần trong 112 ca |
| B3 | Phủ MỌI kỹ năng thật, báo cái không tung được | 30/39 kỹ năng tung được và đã chạy cho cả hai hero ở cấp 1 (+ skill 4 cấp 5, 9) và dạng ép trượt; **9 không tung được**: bị động/không animation — 7, 29–32, 35–38 (log in `SFXTEST PLAN … unreachable … ids`) |
| B4 | Menu bàn phím | Nhân vật · Chiêu (đánh thường/trượt/chí mạng/skill 1–39) · Cấp · Chạy-chạy lại · Chiêu kế · Auto-test · Log · Bất tử · Ép trúng · SFX bật/tắt · Âm lượng · Thoát. Đã thử bằng phím thật trong MicroEmulator (`screenshots/session1/debug_menu_over_real_battle.png`) |
| B5 | Auto-test chờ animation + SFX xong rồi ghi kết quả | **112 ca + 2 kiểm bền: pass 114, fail 0** (`docs/SFXTEST_session1.txt`, ~25 phút/MicroEmulator) |
| B6 | Log SFX: actor, skill, loại, frame, thời gian, nhịp kỳ vọng/thực tế, trạng thái; tách "gọi API" và "đã phát" | `SfxLog` vòng 512 bản ghi, màn hình xem log trong menu, in `SFXLOG …` ra stdout. Trạng thái `QUEUED → API_CALLED(REQUESTED) → PLAYED` hoặc `DROPPED/FLUSHED/FAILED/OFF`; sandbox không có thiết bị âm thanh nên chỉ tới `API_CALLED` (xem §8) |
| B7 | Thoát không để lại cờ | `exit()` reset 7 cờ + trả bảng kỹ năng tạm gán rồi **thoát ứng dụng** (`destroyApp`) vì ván debug làm rò trạng thái tĩnh của game sang ván mới nếu chỉ quay về logo (thử thấy đội hình có sủng vật lạ); đo `SFXTEST EXIT flagsClean=true` và đọc lại bằng `probe`: `mode=0, invincible=true, forceHit=-1, forceHitMenu=-1, passive=true, SFX enabled=true, injectFailure=false` |
| B8 | Bản debug tách riêng | Hai JAR; bản release **không chứa** `DebugBattle` và 30 site hook debug (92 vs 62; BinDiff: 943 vs 938 entry, class `cd`, `ci` chỉ đổi ở bản debug) |
| — | Ràng buộc: không nhạc nền, không đổi gameplay/sát thương/animation, không âm thanh thật | Chỉ thêm lời gọi tĩnh; JAR release chạy cùng kịch bản trận với JAR gốc cho cùng kết cấu hình ảnh (xem pipeline #9); bất tử chỉ trong bản debug |

#### 1a. Lỗi tìm thấy bởi auto-test và đã sửa

| Nhóm | Lỗi | Sửa |
|---|---|---|
| SFX | Kỹ năng nhắm bản thân/đồng minh (hồi 13/14, buff 15/17…) im lặng: engine dùng trạng thái `e.f=17`, hook chỉ biết 10 | `onState/onFrame` coi 10 và 17 là "đang phát hiệu ứng" |
| SFX | Đòn thường của **quái/sủng vật** (trạng thái 13) báo thiếu 1 nhịp — kỳ vọng tính cả "nhịp cuối đòn" chỉ có ở trạng thái 1 | `expectedBlows(a, st13)`: trạng thái 13 không +1. Release chạy thật (không debug): 11/11 SUMMARY OK (trước: 4/11 MISMATCH) |
| Debug | Case hero chưa học kỹ năng: ép tung → NPE trong mã EXP kỹ năng của game (`e.a:d[] == null`, Julie đầu game) — **không xảy ra khi chơi thường** | Bản debug gán bảng rỗng tạm rồi trả `null` khi thoát |
| Debug | Skill 39 (Trinh sát) mở bảng thông tin chờ phím → ca treo 60 s | Harness bấm OK giúp ở pha 5 (log `SFXTEST NOTE`) |
| Debug | Skill 6 luôn trúng (force-hit trong `bn.a`) nên ca "ép trượt" không khả thi; đòn thường có chí mạng ngẫu nhiên | Bỏ ca trượt của skill 6 (ghi chú); chí mạng tự nhiên ≤ số nhịp được chấp nhận ở ca đánh thường |

**Pipeline kiểm tra (bản cuối, mục §7 của quy trình):**

| # | Bước | Kết quả |
|---|---|---|
| 1 | ECJ `-1.3 -target 1.1` → Peephole | 0 lỗi |
| 2 | `Verify` JAR chưa preverify | release 123 class, debug 128 class: 0 lỗi, 0 cảnh báo |
| 3 | Đóng gói (`pack.py`) | manifest đầu, tài nguyên chép nguyên, 938/943 entry Deflate |
| 4 | ProGuard `-microedition` → `Verify --require-stackmap` | 0 lỗi, StackMap đủ |
| 5 | JAD | `MIDlet-Jar-Size` khớp byte thật (1 530 407 / 1 547 737), `MIDlet-Version` 1.1.0 |
| 6 | Quét cấp phát | Hook nóng 0 `new*` (§1 A8); không float/double/`Class.forName`/class literal trong 7 class mới (chỉ `ldc2_w` kiểu `long`) |
| 7 | Alpha ảnh | Không áp dụng (không đụng ảnh/phát lại) |
| 8 | Diff nhị phân (`BinDiff`) | `docs/BINDIFF_session1.md`: 922/928 entry giống hệt; đổi: MANIFEST, `bn bx da e` (+ `cd ci` ở debug), `fontcus` (chỉ thêm StackMap) |
| 9 | MicroEmulator 2.0.4 (FreeJ2ME không tải được trong sandbox): cùng kịch bản (ván mới → trận thật 3 quái → đánh thường/kỹ năng) chạy **JAR gốc và JAR release** | 0 ngoại lệ từ code game ở cả hai (cả hai chỉ có lỗi MIDI/âm thanh của emulator vì sandbox không có thiết bị âm thanh; bản mới thêm các dòng `IllegalArgumentException` của `Manager.playTone`, bắt ở `BeepBackend`); ảnh khác nhau chỉ do nhịp animation/hạt nền và HP (xác suất): `screenshots/session1/smoke_orig_vs_new_diff.jpg`; SFX thật trong trận: 11/11 SUMMARY OK |
| 10 | Test đặc thù | Auto-test 112 ca: nhịp kỳ vọng == thực tế, 0 `hookErrors`, 0 sự kiện treo sau ca, tiêm lỗi SFX và tắt SFX trong trận thật PASS, kiểm bền flush PASS |
| 11 | MicroEmulator kịch bản chính + combat | như #9/#10; fps/RMS không đo (không đổi vẽ); RMS của emulator không đổi sau mọi lần chạy debug |
| 12 | J2ME Loader / KEmulator / máy thật | **Chưa chạy** — ⚠️ xem §8 |

### 2. Các file đã chỉnh sửa / tạo mới

| File | Thay đổi |
|---|---|
| `src/SFXType.java, BuildFlags.java, SfxLog.java, BeepBackend.java, SFXManager.java, AnimSfxMap.java, SfxHooks.java` | Mới: hệ SFX (đọc được, không phụ thuộc field game) |
| `res/sfx/map.txt` | Mới: bảng ánh xạ (sinh bằng `tools/gen_sfx_map.py`) |
| `debug/DebugBattle.java` | Mới (chỉ bản debug): Debug Battle, auto-test |
| `stubs/*.java` | Mới (chỉ để biên dịch): khai báo accessor ASM sinh |
| `tools/src/{Patch,DebugPatch,Peephole,Verify,BinDiff}.java`, `tools/pack.py`, `tools/{avparse,avrender,gen_sfx_map}.py`, `tools/me/*`, `build_jar.sh` | Mới: công cụ dựng/kiểm/chạy thử |
| `dist/SVTH_SFX.{jar,jad}`, `dist/SVTH_SFX_debug.{jar,jad}` | Bản build |
| `README.md`, `docs/*`, `tools/README.md`, `screenshots/session1/` | Tài liệu |

### 3. Class đổi tên / thêm / xoá

Không đổi tên, không xoá. Thêm 7 class SFX (+2 interface lồng) ở bản release; bản debug thêm `DebugBattle` và các lớp lồng (Starter, Watchdog, EndWatch, Sink). Class game bị vá: `e, bx, bn, da` (release) và `cd, ci` (debug).

### 4. Method / field (so bytecode với bản gốc)

Chi tiết từng method ở `docs/BINDIFF_session1.md`. Tóm tắt: **chỉ thêm** field `bx.sfx$name:String`, `bx.sfx$last:I`; thêm accessor `sfx$*` (`e` 15, `bx` 6, `bn` 3) và `dbg$*` (bản debug); chèn lời gọi `INVOKESTATIC` trước lệnh gốc ở `bn.f()` (4 `putfield e.g:Z`, 44 `putfield e.f:I`), `bn.a(e,int,int)`, `bn.c/h/e/g/b`, `bx.a(Z)`, `bx.a/b(I)`, 3 hàm nạp `.av`, `da.hideNotify`. Không đổi descriptor có sẵn nên không cần rà caller.

### 5. Biến quan trọng mới

| Biến | Ý nghĩa |
|---|---|
| `SfxHooks.fxActor/fxSeq/lastMarkKey` | actor đang phát hiệu ứng kỹ năng; số thứ tự chuỗi hiệu ứng; khử trùng marker AoE |
| `SfxHooks.blowExpected/blowCount/lastBlowFrame/atkSeq[slot]` | kỳ vọng/thực tế số nhịp đòn thường và khử trùng theo frame |
| `SfxHooks.skillId/beatNo/markCount/beatExpected[slot]` | trạng thái kỹ năng đang tung |
| `SFXManager.enabled/volume/maxVoices/injectFailure` | điều khiển SFX và tiêm lỗi (test) |
| `SfxLog.count[]` | bộ đếm theo trạng thái (QUEUED…OFF) |
| `bx.sfx$name/sfx$last` | tên `.av` đang chạy / frame đã báo gần nhất |
| `DebugBattle.invincible/passive/forceHit/forceHitMenu` | cờ debug; reset khi thoát |

### 6. Luồng thực thi cập nhật

`docs/FLOWS_battle.md` (trận, đòn thường, kỹ năng, luồng debug) và `docs/SFX_MAP.md` (bảng hook và sự kiện).

### 7. Tài nguyên đã chuyển đổi

Không đổi tài nguyên gốc. Thêm `/sfx/map.txt` (xem `docs/RESOURCES.md`).

### 8. Vấn đề còn tồn đọng

* ⚠️ **Chưa chạy trên máy/KVM thật, J2ME Loader, KEmulator.** Mọi kiểm chứng trong MicroEmulator 2.0.4 (JDK 21, Xvfb). Độ trễ phát beep, số `Player` đồng thời cho phép, và việc `Player` MIDI nền bị cắt trên máy chỉ cho 1 player là **chưa biết**; mặc định `maxvoices=3` và 27 `Player` dùng lại (3 biến thể x 9 loại) có thể quá nhiều trên máy yếu — chỉnh `maxvoices` trong `/sfx/config.txt`.
* ⚠️ **Không có thiết bị âm thanh trong sandbox:** backend đi đường `Manager.playTone` rồi bị emulator từ chối (`IllegalArgumentException` in ra stderr, đã bắt) → log dừng ở `API_CALLED`; trạng thái `PLAYED` (cần `Player.getState()==STARTED`) **chưa quan sát được**. Mã đã có, chưa chạy thật.
* **Điểm va chạm hiệu ứng là heuristic** (frame có diện tích lớn nhất của `.av`); có thể lệch vài frame với cảm giác "đúng lúc". Sửa trong `res/sfx/map.txt`, không cần build lại logic.
* Quái/sủng vật (trạng thái 13) không có nhịp "cuối đòn" như hero (trạng thái 1) — là hành vi engine, không phải lỗi SFX.
* ~~**SFX chưa theo công tắc nhạc của game:** cờ `at.b` ("Bật âm nhạc?"/"Cài đặt nhạc") không ảnh hưởng SFX; người chơi chọn "Tắt nhạc" vẫn nghe beep. Muốn tắt: `enabled=0` trong `/sfx/config.txt`. Gắn vào `at.b` cần thêm một accessor ASM — chưa làm để không đổi hành vi khi chưa được xác nhận (⏸ hỏi người dùng).~~ → **đã xử lý ở Phiên 2.**
* **Thoát Debug Battle = thoát ứng dụng** (phải mở lại game). Đã thử quay về logo và "Chơi mới": trạng thái tĩnh (đội hình/sủng vật) bị rò từ ván debug, nên không dùng. Chỉ ảnh hưởng bản debug.
* ~~**Bản release không có công tắc SFX người chơi chạm được**~~ → từ Phiên 2 SFX theo công tắc nhạc của game; `/sfx/config.txt` vẫn cần đóng gói lại JAR. Lớp phát nhạc gốc `bv` bắt `Exception` quanh `createPlayer/realize/prefetch` nên việc preload 27 `Player` của SFX không làm game ném lỗi, nhưng trên máy ít tài nguyên MMAPI có thể làm nhạc nền không tạo được — chưa thử.
* Debug Battle bắt đầu bằng **ván mới** (`Main.newGame`), không tải save; `Julie` được thêm vào đội bằng đường `cd.dbg$join` tương đương lệnh script "加入队伍". Ép tung kỹ năng cho nhân vật chưa học (Julie đầu game) dùng bảng kỹ năng rỗng tạm.
* 9 kỹ năng bị động/không animation (7, 29–32, 35–38) không có SFX vì không có gì để đồng bộ.
* Đồng thời nhiều lần "tiếng trúng" của AoE nhiều mục tiêu: gộp thành một sự kiện/nhịp (theo thiết kế).
* ⏸ Không đổi `MIDlet-Name`/icon/permission. JAD sinh mới (JAR gốc không đi kèm JAD).

### 9. Kế hoạch tiếp theo

1. Thử trên thiết bị thật/KVM: đo độ trễ, số voice, xung đột với MIDI nền; chỉnh `maxvoices`.
2. Cắm âm thật `res/sfx/*.wav`, giữ nguyên logic (đã chuẩn bị).
3. Tinh chỉnh marker va chạm theo tai nghe từng hiệu ứng (`map.txt`); xác nhận có bỏ "nhịp cuối đòn" thừa của hero hay không.
4. Quyết định có gắn SFX vào công tắc nhạc của game (`at.b`) hay thêm mục riêng.

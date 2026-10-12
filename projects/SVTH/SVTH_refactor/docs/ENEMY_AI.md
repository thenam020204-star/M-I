# ENEMY_AI — AI quái, bám cấp đội hero, sàn chỉ số, bộ kỹ năng ngẫu nhiên (phiên 8)

Mã: `src8/EnemyAI.java` (class mới, CLDC 1.0, chỉ số nguyên). Hook: `tools/src/Patch8.java` (ASM, trên JAR đã đặt tên).
Chỉnh mà không cần build lại: `ai/config.txt` trong JAR (`res/ai/config.txt`). Dựng: `./build_s8.sh release|debug`.

## 1. Điểm hook (đã kiểm khẳng định trong Patch8 — lệch là dừng build)

| Vị trí (bytecode `Battle`) | Lời gọi | Việc |
|---|---|---|
| `createEnemyActors()`, ngay trước lệnh `GameUtil.a(petTable, starTable, idx, 3, stats[0])` đầu tiên của thân vòng lặp (1/5 lời gọi) | `EnemyAI.adjustLevel(this, i)` | đặt cấp quái `stats[0]`; **game tự tính** HP/MP/ATK/DEF/SPD/EXP thưởng từ cấp mới (bảng `stardata` + tăng trưởng của loài) |
| `createEnemyActors()`, trước `return` | `EnemyAI.onEnemiesCreated(this)` | cấp bộ kỹ năng ngẫu nhiên, nhân hệ số, sàn chỉ số theo đội |
| `tick()`, lối ra vòng lập kế hoạch quái của pha 2 (`for slot 10..` → nhãn thoát) | `EnemyAI.planRound(this)` | thay kế hoạch của mọi quái bằng hành động tốt nhất |
| `tick()`, sau 3 lệnh `putfield actingSlot` | `EnemyAI.onTurn(this)` | quái sắp hành động quyết định lại theo trạng thái hiện tại |

Ba field private của `Battle` được nới thành package (`actionPlans`, `actingSlot`, `enemyCount`) — chỉ field, không đổi ngữ nghĩa
`invokespecial`. Mọi hàm `EnemyAI` bọc `try/catch Throwable`: lỗi trong AI không bao giờ làm hỏng trận (giữ kế hoạch của mã gốc).

Không áp dụng cho: trận hướng dẫn (`World.isTutorialBattle`, lệnh script `教学战斗`) và trận bắt buộc thua (`ScriptEngine.isMustLoseBattle`, `必败战斗`).

## 2. Cấp quái bám cấp trung bình đội hero

* `avg` = trung bình cấp (làm tròn) của **nhân vật chính** trong trận (slot 0–9, `partyUid == 0`; sủng vật có `partyUid ≥ 1`).
  `pets=1` → tính cả sủng vật xuất chiến.
* Gặp ngẫu nhiên (`enemyIds == null`): `cấp = avg + rand(lvmin..lvmax)` = **avg..avg+2**. Thay cho luật gốc (bảng cấp của cảnh,
  hoặc cấp Quinn ±3).
* Trận theo script (cốt truyện / phó bản / boss — `enemyIds != null`): `cấp = avg + bosslv` (= avg+3); `bossfloor=1` giữ **không thấp hơn
  cấp game thiết kế** (boss cấp 99/150 vẫn nguyên). `bossfloor=0` → luôn đúng avg+3.
* Chặn `1..lvcap(200)`. Vì cấp đặt **trước** khi game tính chỉ số, mọi chỉ số + EXP thưởng (`expFactor² × cấp`) theo cấp mới.

## 3. Chỉ số: hệ số + sàn theo sức mạnh thật của đội

Ở cùng cấp, quái gốc gần như vô hại (mô phỏng `tools/sim/balance_sim.py`: hero cấp 30 trang bị theo cấp đánh 1 đòn ≈ 1–2 quái sao 1–5
gục; quái đánh thường chỉ gây 10 sát thương tối thiểu vì `2×ATK < DEF hero`). Vì vậy ngoài bám cấp còn có **sàn** tính từ đội thật:

1. Nhân hệ số trên chỉ số theo cấp: HP ×1.05, DEF ×1.00, SPD ×1.05 (`hpmul/defmul/spdmul`).
2. DEF ≤ 45 % của `2 × ATK TB đội` (`defcap`) — đòn thường của hero luôn có tác dụng.
3. **ATK theo ngân sách sát thương** (`maxhit=8`, trận script `bosshit=13`): ATK được tính **ngược** sao cho đòn mạnh nhất
   của *chính quái đó* — xét đánh thường và mọi kỹ năng gây sát thương nó có (kể cả bộ kỹ năng ngẫu nhiên ở §4), ở cấp kỹ năng của nó,
   với tương khắc hệ tốt nhất trước các hero trong trận — gây khoảng `maxhit %` HP tối đa của một hero trung bình:
   `ATK = min_kỹ_năng (ngân_sách + DEF_TB_đội) / hệ_số_kỹ_năng` (kỹ năng 6 bỏ qua DEF: `ngân_sách / hệ_số`; kỹ năng 4 đánh 2–3 lần:
   tổng ≈ 1.2 ngân sách). Nhân `atkmul` và ngẫu nhiên ±10 %. Bị động 29–32 (cộng ATK theo hệ) được trừ trước để ATK hiệu dụng vẫn đúng ngân sách.
   *Lý do*: sát thương của game là `hệ_số × 2×ATK − DEF` (`dealDamage`, `applySkill`) — hệ số nhân **trước** khi trừ DEF, nên một kỹ năng
   ×1.5 với tương khắc hệ (+50 %) có thể gây gấp 5–10 lần đòn thường. Đặt sàn ATK theo đòn thường (bản nháp đầu) làm đội cấp 60 bị diệt trong
   2 vòng (đo bằng harness, `REFACTOR_LOG.md` phiên 8); ngân sách theo đòn mạnh nhất giữ mọi đòn của quái trong giới hạn, AI vẫn chọn đòn tốt nhất.
4. **Sàn HP**: đội cần khoảng `rounds/10` vòng đánh thường để hạ hết quái (`rounds=22` → 2.2 vòng; script `bossrounds=35`):
   `HP ≥ Σ_hero max(10, 2×ATK_hero − DEF_quái) × rounds/10 / số quái × hệ_số_loài`. HP **không bao giờ thấp hơn** game gốc tạo ra.
5. **Sàn SPD**: ≥ 70 % tốc độ TB đội × hệ số loài (`spdfloor`); không thấp hơn gốc.
6. Chặn trần 999 999. Tính bằng `long`.

Với 3 quái dồn đòn vào một hero: ~24 %/vòng (boss ~39 %), trong khi đội cần ~2.2 vòng (boss ~3.5) để thắng — trận thường thắng được bằng
đánh thường nhưng mất máu đáng kể; boss cần hồi máu/phòng thủ.

*Hệ số loài* (HP, SPD) giữ cá tính từng loại quái: chỉ số theo cấp của loài so với quái sao 5 cùng cấp, kẹp 0.75–1.35
(loài trâu nhiều HP hơn, loài nhanh nhanh hơn…). ATK mang cá tính qua bộ kỹ năng: quái có kỹ năng mạnh thì ATK gốc thấp hơn, quái chỉ đánh thường thì ATK cao hơn.

## 4. Bộ kỹ năng ngẫu nhiên mỗi trận

Ngoài kỹ năng tự nhiên (bảng `petdata`), **mỗi quái, mỗi trận** nhận thêm một bộ kỹ năng ngẫu nhiên (khác nhau giữa các quái và giữa các trận):

* Số kỹ năng thêm = `1 + cấp/10` (`skillstep`), tối đa **7** (`skillmax`): cấp 1–9 → 1, cấp 10–19 → 2, …, cấp ≥ 60 → 7.
* Không trùng kỹ năng đã có. Cấp kỹ năng = `1 + cấp_quái/20` (`skilllvstep`), không vượt số cấp có giá MP thật của kỹ năng
  (kỹ năng 9–12 chỉ có 5 cấp; 33 chỉ 5 dòng bảng độc).
* Kho kỹ năng: mọi kỹ năng chủ động trừ 39 (trinh sát — chỉ người chơi) và 16 (cấm phép: cấp 1–5 cấm cả phe mình); thêm bị động
  có tác dụng với quái: 7 (phản kích → `hasCounter`), 29–32 (cường kích nguyên tố → cộng ATK theo hệ).
* Tối đa **1 kỹ năng hỗ trợ** ngẫu nhiên (2, 13, 14, 15, 17) mỗi quái, cấp ≤ 2 — đo thật: quái cấp 60 có hồi máu toàn đội cấp 4 dùng mỗi lượt
  (hồi ~30 %/lần cho cả 3 quái) làm trận bế tắc và đội thua sau ~60 s.
* Áp cho cả quái script (`randboss=1`); `randboss=0` → chỉ quái gặp ngẫu nhiên. `randskill=0` → tắt.
* An toàn hoạt ảnh: sprite quái chỉ có 5 action (0–4); đường tung kỹ năng phe mình (hồi máu, state 17) gọi `setAction(5)` — đã có sẵn
  ở quái gốc (Mộc Tinh dùng kỹ năng 13) và `setAction` bỏ qua chỉ số ngoài khoảng, `getActionFrameCount(5)` trả 0 → kết thúc ngay. Hiệu ứng
  kỹ năng nạp từ `/ft/<skill>.av` không phụ thuộc loài; SFX tra theo id kỹ năng (`sfx/map.txt`).

## 5. AI chọn hành động

Mã gốc (pha 2): 50 % đánh thường / 50 % kỹ năng ngẫu nhiên khi HP > ½, mục tiêu **ngẫu nhiên**; HP ≤ ½: ~70 % kỹ năng, ~30 % phòng thủ.

Mới: chấm điểm **mọi** phương án, đơn vị 1000 = một thanh HP đầy của mục tiêu:

| Phương án | Điểm |
|---|---|
| Đánh thường hero t | sát thương kỳ vọng (mô phỏng đúng `dealDamage`: `ATK×(200+tương khắc)/100 − DEF`, phòng thủ /5 — kể cả hero **đã chọn phòng thủ** lượt này, `halfDamage` /2, tỉ lệ trúng `rollHit`) × tỉ lệ máu còn lại; **+600 nếu hạ được** (+800 nếu là leader/slot 0 — leader gục là thua trận); ×(0.8..1.2) theo ATK của hero (đánh kẻ gây sát thương lớn trước); ×1.05 leader; ×1.2 hero có kỹ năng hồi máu; **+150 nếu đồng đội đã nhắm cùng mục tiêu với tốc độ ±150** (kích hoạt đòn hợp kích của game, ×1.1); −40 nếu hero có phản kích |
| Kỹ năng sát thương đơn/diện | công thức thật của từng kỹ năng trong `applySkill` (5, 6 bỏ qua DEF, 8 +15 % tất sát, 18, 20–27 theo hệ của phép so với hệ mục tiêu, 3/19 diện rộng, 4 đánh 2–3 lần, 28 hút máu…) |
| Khống chế (1, 11, 12 choáng; 10, 34 hỗn loạn; 9, 33 độc) | chỉ khi mục tiêu chưa có trạng thái xấu (game bỏ qua nếu đã có) và chưa có quái khác định khống chế nó lượt này; **chống khoá đội**: khi đã có một hero bị choáng/hỗn loạn (hoặc chỉ còn một hero), điểm khống chế chỉ còn 30 % — hiếm khi khoá liên hoàn |
| Hồi máu 13 / 14, giải trạng thái 15, buff 2 / 17 | chỉ hồi đồng đội HP < 50 %; ưu tiên < 50 % (×1.6), < 25 % (+250); 15 chỉ khi có đồng đội dính trạng thái xấu; buff chỉ khi chưa có |
| Phòng thủ | HP < 30 % (50 % số lần xét), điểm 220 + … — game hồi 80 % HP đã mất cho người phòng thủ mỗi khi có kỹ năng được dùng trong vòng |
| Chi phí MP | trừ `giá × 25 / MP_tối_đa` (dùng MP có cân nhắc) |

* Cấp kỹ năng: cao nhất đủ MP (như mã gốc).
* **Không đánh thừa**: sát thương đã định của các quái trước trong cùng vòng được trừ vào máu dự kiến của mục tiêu (quái sau chuyển
  sang mục tiêu khác nếu hero kia đã chắc chết, hoặc dồn để hạ).
* **Quyết định lại đúng lượt** (`jit=1`): khi tới lượt quái (thứ tự theo tốc độ), nó chọn lại với HP/trạng thái thật lúc đó
  (hero đã được hồi máu, đã chết, đã bị choáng…). Không đổi sang phòng thủ ở bước này (phòng thủ chỉ áp ở pha 2).
* 10 % chọn phương án tốt thứ 2 (`noise`) để không đoán trước hoàn toàn.
* Quái bị choáng/hỗn loạn: để mã gốc xử lý (bỏ lượt / đánh ngẫu nhiên). Bị cấm phép: chỉ xét đánh thường.

## 5b. Sủng vật đồng đội do AI điều khiển

Game gốc: sau khi chọn lệnh cho nhân vật chính (slot 0), menu lần lượt hỏi lệnh cho từng đồng đội (slot 1, 2 — sủng vật xuất chiến hoặc
nhân vật chính khác như Julie) qua `Battle.confirmCommand()` (`commandSlot` = ô đang được hỏi, `phase` 0 = đang chọn lệnh).

Mới: hook đầu `Battle.tick()` và `Battle.handleKey()` gọi `EnemyAI.petGate`: khi menu sắp hỏi lệnh cho một **sủng vật** (`partyUid ≠ 0`),
AI trả lời ngay rồi gọi lại `confirmCommand()` của game (cầu nối `ai$confirm`) — game tự chuyển sang ô kế tiếp hoặc pha 2 như khi người
chơi bấm. **Người chơi chỉ chọn lệnh cho nhân vật chính** (`partyUid == 0`: Quinn, Julie…); sủng vật bị choáng/hỗn loạn vẫn theo luật gốc.
Lệnh "Gọi sủng vật" của nhân vật chính giữ nguyên.

Thứ tự quyết định của sủng vật:
1. **Cấp cứu**: có kỹ năng hồi máu (13 / 14) đủ MP và có đồng đội (kể cả chính nó) dưới **30 %** HP (`petheal`) → hồi máu ngay
   (14 nếu ≥ 2 người nguy kịch, không thì 13 vào người thấp máu nhất). Thông báo "X cấp cứu Y!".
2. **Máu bản thân < 25 %** (`petlow`) và không hồi được:
   * sủng vật "trung thành" (`uid % 3 == 0`), hoặc chủ nhân (slot 0) < 35 % HP, hoặc chỉ còn 1 quái → **thủ tới cùng** (lệnh phòng thủ
     mỗi vòng; game giảm sát thương /5 và hồi 80 % máu đã mất cho người phòng thủ mỗi khi có kỹ năng được dùng). Thông báo một lần
     "X liều chết bảo vệ chủ nhân!" / "X quyết thủ tới cùng!".
   * còn lại: 60 % (`petflee`) **rút lui**: rời trận còn sống (`isDead = true` để mọi vòng lặp của game bỏ qua — lượt, mục tiêu, menu;
     `isPresent = false` để không vẽ; xoá trạng thái để độc không trừ máu tiếp), HP giữ nguyên → sau trận **không** bị tính là ngất.
     **Không gọi lại được trong trận này**: uid vào danh sách rút lui; nếu sau đó một sủng vật khác thế chỗ (làm nó trở lại danh sách dự bị),
     `petRoster` bị lọc mỗi tick. Không có EXP thưởng của trận (đã rời trận). Thông báo "X bị thương nặng, rút lui khỏi trận!". 40 % còn lại: thủ.
3. **Chiến đấu** bằng đúng bộ chấm điểm của quái hoang dã (§5) nhưng đổi phe: nhắm quái (slot 10–19), hồi/buff phe ta. Thêm: biết lệnh
   của nhân vật chính và sủng vật chọn trước trong vòng → ưu tiên **kết liễu** mục tiêu đã bị nhắm và **hợp kích** (cùng mục tiêu, tốc độ ±150
   → đòn hợp kích x1.1 của game). Nhiễu chọn phương án 2 chỉ 5 %.
4. **Quyết định lại đúng lượt** (`jit`): tới lượt sủng vật (theo tốc độ) nó chọn lại mục tiêu/kỹ năng với HP thật lúc đó (quái đã chết, đồng
   đội vừa tụt máu → cấp cứu).

## 6. Bộ nhớ / hiệu năng

Không cấp phát trong vòng lặp trận: mảng `projDmg[10]`, `ccPlanned[10]` tĩnh; ứng viên lưu trong field tĩnh. Cấp phát chỉ khi tạo
trận (mảng kỹ năng mới của quái: ≤ 3 mảng `short[≤ 9]` mỗi quái) và khi in log (`log=1`, mặc định tắt).
Mỗi lần quyết định: ≤ (10 hero × (1 + số kỹ năng)) phép tính nguyên — vài trăm phép, một lần mỗi lượt quái.

## 7. Kiểm chứng

Xem `REFACTOR_LOG.md` phiên 8 (số đo trước/sau) và `tools/test/me/s8/` (kịch bản), `tools/sim/balance_sim.py` (mô phỏng).

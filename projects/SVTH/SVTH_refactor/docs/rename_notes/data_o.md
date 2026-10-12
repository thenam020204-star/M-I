# Cụm "data_o" — ghi chú đặt tên (mapping/part_data_o.map)

Chỉ một lớp: `o` = **GameData** (lớp tĩnh `final`, không có instance). Đã đặt 1 class, 107/110 field, 79/79 hàm.
`remap_check` (chạy riêng file này) sạch: 0 lỗi, 0 cảnh báo.
Không đặt tên (giữ placeholder): `c_S` (chỉ được gán 0 ở `a_V()`/`a_V(String)`, không ai đọc), `s_AS`, `t_AS`
(cột 4 và 6 của item.bin, chỉ ghi, không đọc. Cột 6 = 0 ở vật phẩm nhiệm vụ, có thể là "bán được", nhưng không đoán).

Các lớp dữ liệu mà `o` dùng (`bl`, `cv`, `ao`, `au`, `bp`, `ck`=Equip, `d`=Skill, `e`=BattleActor) thuộc cụm khác. Trong map, chú thích
vẫn ghi tên cũ của field (`cv.a_AI[1]`…). Chỉ số `a_AI` dùng trong các hàm hồi máu:
`bl.a_AI` = [0] cấp, [1] EXP, [2] EXP lên cấp, [3] HP, [4] HP gốc, [5] MP, [6] MP gốc, [7] ATK gốc, [8] DEF gốc, [9] SPD gốc,
[10] HPmax, [11] MPmax, [12] ATK, [13] DEF, [14] SPD, [15] EXP mốc trước, [16] điểm cộng (team.bin cột 3..12 → [0..9]).
`cv.a_AI` (pet) = [0] cấp, [1] HP, [2] HPmax, [3] tăng HP/cấp, [4] MP, [5] MPmax, [6] tăng MP/cấp, [7] ATK, [8] tăng ATK, [9] DEF,
[10] tăng DEF, [11] SPD, [12] tăng SPD, [13] EXP, [14] EXP lên cấp, [15] 0, [16] 60.

## Bảng dữ liệu → field

### /bin/sn.bin `data:` (đọc trong `loadSceneInfo(int)` = `a_Z(int)`)
| cột | ví dụ | field | ý nghĩa |
|---|---|---|---|
| 0 | 8 | (khoá) | id cảnh |
| 1 | Ngoại ô Kuze | `sceneName` (`b_String`) | tên hiện góc trên trái (+ số tầng nếu ở phó bản) |
| 2 | yewai1 / 1 | `sceneMapName` (`a_String`) | tên tệp /map/*.map. Với phó bản thì đây là id fuben |
| 3 | 1 / 2 | `sceneKind` (`k_B`) | 1 cảnh thường, 2 phó bản (dòng 68–81) |
| 4 | 2 | `sceneMusic` (`c_String`) | /music/<n>.mid |
| 5 | 19,33,35,87 | `sceneEnemyIds` (`a_AS`) | id quái gặp ngẫu nhiên |
| 6 | 1,3 | `sceneEnemyCountRange` (`a_AB`) | số quái min,max. null = không gặp quái (bl) |
| 7 | 25,30 | `sceneEnemyLevelRange` (`d_String`) | cấp quái min,max. null = cấp nhân vật ±3 |
| 8 | 7,1 | `townReturnPoint` (`b_AS`) | cảnh + điểm vào cho "Cuộn hồi thành" |
| 9 | -1 | `recolorImageNames` (`a_AString`) | ảnh .gf đổi palette (MED, dữ liệu hiện không dùng) |
| 10 | -1 | `recolorSpecs` (`b_AString`) | "bảng,hàng" tra `tablename` + colorTable.bin (MED) |
| 11 | 3 | `rideFlags` (`c_B`) | bit0 cho cưỡi thú (0/2 chặn), bit1 cho biến thân/bay (0/1 chặn) |
| 12 | 1/2/3 | `battleBackground` (`b_B`) | /ftmap/<n>.jpg; thiếu cột → "File Sn thiếu loại cảnh chiến đấu" |

Khoá ngoài khối: `heroId`→`heroId`(`e_S`), `default`→`startSceneId`(`a_S`), `battleMusic:` `inbattle/win/lose`→`battleMusic/victoryMusic/defeatMusic`,
`liveBackScene`/`liveBackPos`→`reviveSceneId`/`revivePoint` (chỉ nạp khi đang là 0).

### /bin/mazefile.bin
`fuben:` cột 0 id (= sn cột 2), 1 `dungeonName`, 2 `dungeonGateMode` (1 chỉ cổng tiến, 2 có cổng lùi; cd chỉ vẽ sdt1.gf khi 2),
3 id maze, 4 `dungeonFloorCount`, 5 `dungeonLinkScenes` ("41,67": [0] cảnh lối ra ngoài khi thua/nạp save/nhận thưởng xong, [1] cảnh cuối 67),
6 `dungeonBosses` ("id,cấp|…", cấp -1 = cấp nhân vật+5, đánh ở cảnh 67 qua sự kiện 6701 "副本战"), 7 `dungeonBossPos`,
8 `dungeonExitPos` (MED: chỉ thấy dùng ở ae.java:372, dịch chuyển về `dungeonLinkScenes[0]`), 9 `dungeonRewards` ("loại,id,số,tỉ lệ|…", loại 5 = EXP).
`maze:` cột 1 danh sách map (chọn ngẫu nhiên → `sceneMapName`), 2 building id song song (→ `buildingFileId`), 3 chỉ số dòng `mazepos:`.
`mazepos:` dòng chẵn = điểm vào "x,y,hướng" (lấy 2 điểm ngẫu nhiên → `mazeEntryPoints`), dòng lẻ = cổng "x,y" (→ `mazeGatePoints`).
`dungeonFloor` (`j_B`): vào từ ngoài = 1, vào từ điểm 2 (`cd.c_S==2`) = tầng cuối. ci 换场景 tăng, 换场景2 giảm.

### /bin/item.bin `item` (đọc trong `loadItemTable` = `d()`)
cột 0 `itemIds`, 1 `itemNames`, 2 `itemIconNames` (/icon/<x>.gf → `itemIcons`), 3 `itemTypes` (1 dùng được, 3 nhiệm vụ, 4 đặc biệt,
5 đá khảm, 6 gen cấy; as.j() chia 3 túi: {1}, {2,5,6}, {3,4}), 4 (không dùng), 5 `itemBattleUsable`, 6 (không dùng), 7 `itemConsumable` (MED: =1 thì trừ 1 sau khi dùng),
8 `itemDescriptions`, 9 `itemBuyPrices`, 10 `itemSellPrices`, 11 `itemStatBonus` ("chỉ số,giá trị" cho đá khảm/gen).
`countBagSlotsInGroup` (`a_I(int)`, MED) đếm số ô cùng nhóm {1}, {2,5}, {3,4} để so với `ct.maxItemTypes` (nhóm 6 không được đếm).

### Bảng cache khác
`sceneTable`(sn data), `roleTable`(role.bin role:, cột 1 tên→`roleNames`, 2 anim→`roleAnimPaths`, 3 mặt→`rolePortraits`),
`partyRoleTable`(team.bin role:, chỉ số gốc 2 nhân vật chính), `itemTable`, `skillTable`(skill.bin), `equipTable`(equip.bin), `questTable`(task.bin), `mazeTable`.
role.bin còn có `roleFlyBody`→`flyAnimPath` (bl/ci đổi qua lại long1/long2 khi bay) và `roleRunBodys`→`mountAnimPath`. `boatAnimPath` là hằng.

## Lưu game (RMS)
* `saveIndexStore` "cwzw3save": 3 × { byte `slotUsed`; nếu 1: UTF `slotSceneNames`, short `slotHeroIds`, short `slotHeroLevels`, short năm,
  byte tháng/ngày/giờ/phút/giây } (giờ lấy theo `TimeZone "GMT+8"`). Ghi bằng `saveSlotSummary(byte)`, đọc bằng `loadSlotSummaries()`.
* `saveSlot1..3Store` "cwzw3_save1..3" (`saveGame(byte)` ghi, `readSaveRecord(String)` đọc), theo thứ tự:
  byte `storyPhase`; short `currentSceneId`; byte bật nhạc (`at.b_Z`); [short n + short] `removedRoleIds`, `openedChestIds`,
  `eventFlags` (**ghi short** dù là int[]), `questDoneFlags`, `activeQuestIds`, `finishedQuestIds`; [byte n + byte] `partyRoleIds`;
  [byte n + short] `myPetSpeciesIds`; [byte n + int] `myPetUids`; [short n + (short id, short số)] `bagItems`;
  6 × [short n + (byte id; nếu >0: byte, byte, 12 short)] túi trang bị; int `money`; [byte n] `roleRecords` (byte id, chỉ số int[], 4 byte, 4×5 byte,
  kỹ năng {short id, byte, int, int, byte}, trang bị như trên, short x, short y, byte hướng, byte); [byte n] `petRecords` (int id, UTF tên,
  int[], bool, 4 byte, kỹ năng, int uid, UTF, byte chủ, 2×2 byte); short `heroId`; [short n] `discoveredPetIds`; [short n + byte] `newPetFlags`;
  short `reviveSceneId`; byte `revivePoint`; bool `df.a_Z`; int `statMultiplier`; UTF tên nhân vật chính (`roleNames[0]`); int `score`.
* `purchaseStore` "cwzw3SmsSave" (`savePurchaseState`/`loadPurchaseState`): UTF df.a_String, UTF df.b_String, 3 byte cờ df (d_Z, c_Z, b_Z),
  byte df.c_B, int df.a_I, byte `musicVolume`. Được đọc ở Main.java:64 lúc khởi động; được ghi lại mỗi lần `saveGame`.
* `isRestoringSave` (`a_Z`): bật khi `readSaveRecord` xong. cd dùng nó để đặt nhân vật ở vị trí đã lưu (hoặc ra khỏi phó bản) rồi tắt.

## Ghi chú về các tên MED / LOW
* `rollBiasedHalf` (`a(int,int,boolean)`): `k.a(1,3)` chọn nửa khoảng. true → 2/3 nửa trên, false → 2/3 nửa dưới.
  `loadPetData` dùng để tăng trưởng/cấp tương quan với chỉ số gốc. k.java:565 dùng cho quái.
* `sceneNpcs` (`b_Abl`): cd.a_Abl() xoá rồi `addSceneNpc` thêm từng vai của cảnh. cd vẽ dấu nhiệm vụ trên đầu vai có `h_S` = cột 9 task (người giao).
* `followDistance`/`followStepGap`/`updateFollowSpacing`/`placeFollowers`: bl ghi vết di chuyển dài `(đội-1)*gap+1`, đồng đội i đứng ở vết `i*gap-1`.
* `buildBattleParty` (`a_Abl()`): **nối** sprite của pet xuất chiến vào `party` rồi trả ≤ 4 vai `d_Z`. Sau trận bn gọi `syncPetsFromBattle` + `keepLeaderOnly`
  để cắt `party` về `{[0]}`.
* `isEndingPending` (`b_Z`): ci 游戏结局1 bật, cd vẽ màn kết, phím tiếp theo → `resetGameData` + về màn tiêu đề.
* `newPetFlags` (`a_AZ`, MED): mảng 140 cờ "mới" của đồ giám. `addPet` bật, ab/as tắt khi xem chi tiết loài.
* `growPetStats`/`growPetSpriteStats`: cộng tăng trưởng mỗi cấp (cv.a_AI[3/6/8/10/12]) khi lên cấp, bản cho cv và cho sprite trong trận.
* `dungeonGateMode`, `dungeonExitPos`, `recolorImageNames`, `recolorSpecs`, `itemConsumable`, `refreshRoleStats`, `loadRoleData`, `loadPetData`:
  MED, đọc từ logic, chưa thấy dữ liệu dùng thật (cột recolor toàn -1).

## Mã chết / lỗi khả nghi (không sửa)
1. (Không phải lỗi) ci `移除任务` (ci.java:613) gọi `finishQuest` (`a(short,boolean)`): trao thưởng, cộng `score`+5. Đây là thao tác **nộp nhiệm vụ** bình thường
   (26 lần trong s*e.txt, sau hội thoại hoàn thành + `去除事件`). Bảng opcode trong world.md gọi là "bỏ", dễ hiểu nhầm.
2. ab.java:1199–1200 (huỷ nhiệm vụ trong menu) xoá `activeQuests` và `activeQuestIds` nhưng **không xoá `questDoneFlags`** (`m_AS`).
   Hai mảng song song bị lệch chỉ số, nên cờ hoàn thành của các nhiệm vụ sau bị lệch.
3. `removeEquip` (`c_V`): nếu id không có trong ô, chỉ số mặc định 0 → xoá nhầm món đầu tiên cùng loại.
4. `useItemInBattle`: kiểm "còn sống" bằng `a_AI[3]`/`a_AI[5]` (giá trị lúc vào trận) thay vì `e.hp` (`m_I`), riêng "Thuốc hồi toàn phần" lại dùng `m_I`.
   Vì vậy có thể hồi máu cho actor đã gục. "Thuốc Lạc Thiên" chỉ báo "Trung thành +5" mà không đổi gì. "Sách Ma Công/Ma Kháng" không có tác dụng nhưng vẫn bị trừ.
5. `initNewGame`: vòng lặp trên mảng rỗng `new short[0]` (mã chết); nhánh `println("false"); cd.j()` không thể tới (đã kiểm `at.b_Z` ở điều kiện ngoài).
6. `loadPetData` (`b(cv)`): `println("Sủng vật … lấy từ dữ liệu bộ nhớ")` không nằm trong `at.e_Z`, nên luôn in. `addPet` gọi `b(cv)` lần 2 khi `a_AI==null` (thừa).
7. `loadItemTable` tách khối `itemDefaultAni:` rồi bỏ kết quả. `createQuest` parse task cột 9 rồi bỏ (cột này cd đọc riêng).
8. `resetGameData` không reset `discoveredPetIds`, `score`, `statMultiplier`, `isInDungeon`, `dungeonFloor`, `reviveSceneId/revivePoint`.
   Nếu về màn tiêu đề rồi "Chơi mới" thì các giá trị cũ còn lại (`newPetFlags` thì được `initNewGame` tạo lại).
9. `saveGame`: `eventFlags` (int[]) ghi bằng `writeShort` (id > 32767 sẽ hỏng; các id thấy trong code/task như 301, 1502, 6701 đều nhỏ hơn). `partyRoleIds.length` không kiểm null.
   `removePet` dùng `myPetUids` không kiểm null, `removeQuestById` dùng `activeQuests` không kiểm null (trong luồng hiện tại không xảy ra).

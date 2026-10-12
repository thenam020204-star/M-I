# Cụm "data_hold" — ghi chú đặt tên (mapping/part_data_hold.map)

Lớp: `bl`=RoleSprite, `cv`=PetInfo, `au`=PetSaveRecord, `ao`=RoleSaveRecord, `bz`=ScriptEvent, `bp`=Quest, `bw`=PetListEntry,
`bu`=DexEntry, `bm`=MaterialEntry, `bd`=MainQuestEntry, `cn`=SideQuestEntry, `dd`=MenuTab, `bi`=RainDrop, `cm`=TouchButton,
`cr`=CloseUpAnim. Đã đặt 15 class, 132 field, 32 hàm. Mọi field/hàm khai báo trong các lớp này đều có tên (trừ constructor).
`remap_check` sạch (0 lỗi, 0 cảnh báo) khi chạy riêng và khi chạy chung với core/battle/world/ui_as/ui_ab/sdk (+ data_o, data_cdi bản hiện tại).
Không đặt: các override của `cz` trong `bl` (`a(Graphics)` draw, `a_AS()` getBounds, `a_AS(int)` getCollisionBoxes, `b(Graphics)` drawDebugBoxes)
và field kế thừa của `cz` (`d_S` x, `e_S` y, `h_S` roleId, `i_B` kind…) — thuộc part_battle.map.

## bl — RoleSprite (extends cz)
Nhân vật trên bản đồ: hero, đồng đội, NPC, sủng vật xuất chiến (dạng sprite). Hai chế độ vẽ (`renderMode`): 0 ảnh tĩnh `.png/.gf`
(ctor `bl(Image)`), 1 animation `.av` (ctor `bl(bx)`). Tạo bởi `o.a_bl(int)` từ `/bin/role.bin` (`role:` id, tên, ảnh/av, face hoặc -1),
bởi `cd` từ `/bin/s<N>r.bin` (role<id>: group<k>: phase, pos=x,y,dir, walk), và `o.a_bl(cv)` cho sủng vật.
Hướng `direction` (`d_B`): 0 xuống (y+), 1 lên (y−), 2 trái (x−), 3 phải (x+); anim .av 0..3 là đi, 4..7 là đứng (`setIdleAnim`, khi có 8 anim).
Bảng chỉ số `stats` (`a_AI`, độ dài `statCount`=21) — với hero/đồng đội (team.bin cột 3..12 → [0..9]):

| chỉ số | nghĩa | nguồn |
|---|---|---|
| 0 | cấp | team.bin / cv[0] |
| 1, 2 | EXP, EXP cần | cv[13], cv[14]; as vẽ cặp [1]/[2] |
| 3 | HP hiện tại | cv[1]; kẹp ≤ [10] |
| 4, 6, 7, 8, 9 | maxHP/maxMP/ATK/DEF/SPD gốc | cộng trang bị trong getMaxHp/getMaxMp/getAttack/getDefense/getSpeed |
| 5 | MP hiện tại | cv[4]; kẹp ≤ [11] |
| 10..14 | maxHP, maxMP, ATK, DEF, SPD tổng | `o.d(bl)` ghi từ các getter; pet: cv[2],[5],[7],[9],[11] |
| 15 | = cv[15] (=60 khi tạo pet) | chưa rõ |
| 16 | điểm tự do | `getFreePoints`; lên cấp "Nhận 10 điểm" +10 |
| 17..20 | điểm đã cộng vào thuộc tính 1..4 | ab.java:143; mỗi điểm thuộc tính k cộng `attrGrowth[k][HP,MP,ATK,DEF,SPD]` |

Ánh xạ pet → sprite trong `o.a_bl(cv)` (và ngược lại `o.b(bl[])`): bl[0]=cv[0], bl[3]=cv[1], bl[10]=cv[2], bl[5]=cv[4], bl[11]=cv[5],
bl[12]=cv[7], bl[13]=cv[9], bl[14]=cv[11], bl[1]=cv[13], bl[2]=cv[14], bl[15]=cv[15].
Di chuyển: `moveTo(x,y,dir)` → `updateMoveTo` mỗi tick (`isMovingToTarget`), xong thì quay `targetDirection` và `ci.a(5)` tiếp script.
`stepForward` thử hộp va chạm dịch một bước (`stepBoxes`) qua `isBlocked` (tile ca, biên bản đồ, actor khác qua bj); với hero đếm
`encounterSteps` (random 100..199 bước → `cd.a(false)` vào trận) trừ khi đang bay/cưỡi/script. Đồng đội đi theo `followTrail`
(`initFollowTrail`/`resetFollowTrail`, khoảng cách `o.d_B` điểm). NPC lang thang: `canWander` (khoá walk) + `isWandering` +
hẹn giờ `wanderLastTime`/`wanderInterval` (1–4 s). Bay: `startFlying`/`land` ("Không thể hạ cánh tại đây", "Kết thúc bay"), khi bay
`noClip` bật (chỉ kiểm biên bản đồ). Opcode 播放动作 (play action): `actionId`, `actionRepeat`, `returnAction`, `isPlayingAction`, `updateScriptAction`.
Ba hàm tĩnh tra quanh hero: `findTalkableNear` (NPC để nói chuyện), `findChestAhead` (rương ax phía trước), `findRoleOnMap(roleId)`.
MED/LOW: `actorType` (`g_B`: 1 hero id 1/2, 2 NPC — ngữ nghĩa giá trị khác chưa thấy), `isCombatant` (d_Z), `isRiding` (c_Z: thú cưỡi hoặc thuyền
theo `cd.h_B`), `elementValues` cho hero (team.bin cột 15, 4 số giống pet), `getAttrPoints1..4` (tên thuộc tính thật chưa rõ, vẽ bằng ảnh).
LOW: `mapWidth`/`mapHeight` (`a_S`/`b_S` protected, chỉ được ghi từ `cd.a_S/b_S`, không ai đọc).

## cv — PetInfo / au — PetSaveRecord
`cv` = sủng vật sở hữu (`o.a_Acv`). Tạo `o.a_V(petId, uid)`; dữ liệu từ `/bin/petdata.bin` (`petdata`…`end`, `stardata` bảng khoảng
chỉ số theo sao): cột 0 tên, 2 hệ "3007" (4 chữ số, 'a'=10, thứ tự Địa/Thủy/Hỏa/Phong theo item 48..51 "Tinh hoa"), 3..12 hạng sao
cho HP/MP/ATK/DEF/SPD (giá trị + tăng trưởng), 13 kỹ năng "id,cấp|…" (chỉ lấy id), 18 vật phẩm khi tinh luyện (`refineItemIds`), 19 `dropSpec`.
`stats` của pet: [0] cấp, [1] HP, [2] maxHP, [3] tăng HP/cấp, [4] MP, [5] maxMP, [6] tăng MP, [7]/[9]/[11] ATK/DEF/SPD, [8]/[10]/[12] tăng
tương ứng (`o.a_V(cv)` cộng khi lên cấp), [13] EXP, [14] EXP cần = (L+1)³+(L+1)², [15] 60.
`ownerHeroId` (`a_B`) = `o.e_S` (khoá "heroId"); mọi danh sách lọc theo hero hiện tại. `uid` (`b_I`) = "onlyId", khớp `bl.petUid`.
`implantSlots` [2][2] = chức năng "Cấy": [k][0] đã cấy (1), [k][1] id vật phẩm; `isFainted` (`b_Z`) đặt khi HP ≤ 0 sau trận.
`au` là bản lưu (RMS) của `cv`, cùng tên field; `o.b(cv)` ưu tiên đọc từ `au` ("lấy từ dữ liệu bộ nhớ").
LOW: `dropSpec` (petdata cột 19 "36,5|51,3" = id vật phẩm,số lượng — chỉ được chép và lưu, không có chỗ đọc).

## ao — RoleSaveRecord
Bản lưu của nhân vật đội (team.bin role: tên, id, sprite chiến đấu, 10 chỉ số, kỹ năng, 6 trang bị, hệ, 4 dòng `attrGrowth`).
Field trùng tên với `bl`: `roleId`, `stats`, `attrGrowth`, `skillIds`, `skills`, `equipIds`, `equips`, `elementValues`, `jobLevel`, cộng
vị trí lưu `x`/`y`/`direction` (ghi từ hero `d_S/e_S/d_B`, `writeShort`, cd:371 khôi phục).

## bz — ScriptEvent
Một sự kiện trong `/bin/s<N>e.bin` (`event<k>:`): `id`, `name`, `area` x,y,w,h, `isAuto` (tự chạy khi hero vào vùng), `isLoop`
(không loop thì xong đánh dấu `o.b_V(id)` và không nạp lại), `scriptLines` (`script:`…`scriptEnd`). Khoá `type` được đọc rồi bỏ.
Ngoài ra ci tự dựng sự kiện "换场景k" (cửa chuyển cảnh từ sceneChange.bin), "副本战" (phó bản), k.a(...) "Dịch chuyển phó bản".
`speaker` (`a_bl`): NPC được nói chuyện (script từ `/bin/s<N>c.bin` role<id>), cd quay mặt NPC về hero và giao cho DialogBox.

## bp — Quest
Một dòng `/bin/task.bin`: 0 id, 1 tên, 2 điều kiện nhận (đọc ở chỗ khác), 3 `type` (1 chính tuyến, 2 Thường, 3 Công hội), 4 `repeatable`
(1 cho mọi nhiệm vụ Công hội: có menu Xem/Bỏ, không ghi vào danh sách đã làm), 5 mô tả, 6 `conditions` (有装备/道具数量/有宠物/有技能/已发生事件),
7 `completionCost` (扣除道具/扣除装备/移除宠物), 8 `reward` ("5,500|…"), 9 bị parse rồi bỏ, 10 `turnInRoleId` (NPC có dấu nhiệm vụ).
`isCompleted` bật khi cd kiểm đủ điều kiện ("… hoàn thành!"). MED: `repeatable` (tên theo hành vi, cột không có tiêu đề).

## Dòng danh sách UI (bw, bu, bm, bd, cn, dd)
Khớp với tham số đã đặt ở part_ui_as: `bw` cho `l.addEntry(petId,index,deployed,name,level)` (petId bị bỏ), `bu` cho `bc.addEntry(id,name)`,
`bm` cho `db.addMaterial(icon,name,owned,required)`, `bd` cho `cy.addQuest(id,status,name)` (id bị bỏ), `cn` cho `bf.addQuest(index,type,name,done)`,
`dd` cho `de.addTab(tabId,iconFrame)`.

## bi — RainDrop, cm — TouchButton, cr — CloseUpAnim
`bi`: 100 hạt mưa (`cd.n()`, opcode 开始下雨/结束下雨) — vị trí x,y, vận tốc (-5,10), đoạn vẽ (-3,6). `cm`: vùng chạm 26×26 của `i`
(TouchSoftKeys): `buttonId` 1 trái, 2 phải, 5 giữa (ảnh 9.gf). `cr`: trạng thái tĩnh của hoạt cảnh đặc tả (cd mode 6): `anim`, `isPlaying`,
`x`/`y`, `animPath`, `waitForKey` (1 = nền đen + "Nhấn phím bất kỳ để trở về").

## Lỗi / mã chết thấy được (không sửa)
* `bl.isBlocked(short[])`: kiểm tile map bằng `this.e_AS` (stepBoxes cũ) thay vì tham số → `land()` (gọi với `a_AS(1)`) kiểm tile bằng hộp của bước trước.
* `bl(bx)` ctor: `if (a_bx != null) …` rồi vẫn gọi `a_bx.a(0,0,2)` không kiểm null.
* `bl.startFlying()`: hai nhánh N73/N7610 và máy khác gần giống hệt (chỉ khác nhóm cache -1/1).
* `bl.a_S/b_S` chỉ ghi, không đọc. `cv.b_String`/`au.b_String` chỉ chép/lưu.
* `cr`: mọi field là static nhưng `cd` vẫn `new cr()` và giữ `a_cr`.
* Ctor `bw`, `bd` bỏ qua tham số đầu (petId / id). `o.a_bl(int)` gán `a_B=1` cho đối tượng sắp bị thay (lớp o, chỉ ghi lại).

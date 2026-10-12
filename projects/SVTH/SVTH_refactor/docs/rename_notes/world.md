# Cụm "world" — ghi chú đặt tên (mapping/part_world.map)

Lớp: `cd`=World, `ci`=ScriptEngine, `ca`=TileMap, `bj`=SceneLayer, `ax`=TreasureChest, `ae`=FieldInput, `cf`=DialogBox,
`cl`=StoryCrawl, `bb`=ScreenWipe, `j`=SceneLoader. Đã đặt 10 class, 292 field, 118 hàm; `remap_check` sạch (0 lỗi, 0 cảnh báo).
Phần không đặt tên (giữ tên placeholder): `cd.m_B` (cờ nhấp nháy 0/1 mỗi 3 khung, không ai đọc), `cd.a_Image` (chỉ gán null),
`ci.a_AString` + `ci.b()` (mảng không bao giờ được gán → `b()` không làm gì), `bb.e_B`/`bb.f_B` (hằng 2, 3 không dùng).
Các override của `cz` trong `ax` (`a(Graphics)`, `a_AS()`, `a_AS(int)`, `b(Graphics)`) và các field kế thừa của `cz` thuộc cụm battle.

## cd — World
Trạng thái thế giới trong màn chơi (`da.a_B == 4`). `worldState` (`a_B`): 1 đang tải (thanh tiến độ `long2.av`, câu gợi ý `o.l_AString`),
2 đang chạy, 3 màn thua/kết thúc ("Ta... bị đánh bại rồi sao?"). `mode` (`b_B`): 1 đi trên bản đồ, 2 hội thoại (cf), 3 hộp thông báo
(`messageLines`, "Nhấn OK để tiếp tục"), 4 menu (as), 5 chiến đấu (bn), 6 hoạt cảnh đặc tả (cr, opcode 特写动画), 7 menu SMS (df),
8 chiến lợi phẩm phó bản, 9 bảng nhiệm vụ (task.bin, trang `questDialogPage`), 10 ảnh `/pic/order.jpg`.
Tải cảnh (`loadScene`, chạy trong luồng `j`): `/map/<o.a_String>.map` → TileMap; `/building/s<o.f_String>sp.dat` → SceneLayer
(nếu cảnh có bảng màu `sn.bin` thì tô lại màu: `loadPaletteTables`/`recolorTilesets`); rương `/bin/box.bin`; nhân vật
`/bin/s<N>r.bin` (roles=, role<id>: amount, group<k>: phase, pos=x,y,dir, walk); script `/bin/s<N>e.txt`; điểm xuất hiện từ
`/bin/sceneChange.bin` (`S<N>P:` danh sách x,y,hướng; `S<N>DP` điểm mặc định; `entryPointIndex` = `c_S` chọn điểm).
Hiệu ứng do script điều khiển: rung (`isShaking`), chớp trắng (`isScreenFlashing`/`whiteFlashOn`), dải đen trên-dưới (`letterbox*`),
rèm đen 6 sọc (`blinds*`), mưa (`rainDrops`), lia camera (`startCameraPan`, `panDirection` 0 xuống 1 lên 2 trái 3 phải, `cameraReturning`),
dòng chữ chạy dưới màn (`ticker*`), màn nghỉ ("Nghỉ ngơi một chút"). Công thức lên cấp (`checkLevelUp`, `levelUpPets`): EXP cần cho cấp L+1 = L³/2 + L² (L = cấp mới + 1).
`dungeonLoot` (`a_AI`, do bn ghi) = {loại, id, số lượng, tỉ lệ}: loại 1 vật phẩm, 2 trang bị, 3 vàng, 4 sủng vật; EXP lấy từ dòng loại 5 của `o.n_AString`.
`transformKind` (`h_B`): 0 biến thân bay, 1 thú cưỡi, 2 thuyền (cảnh 1) — MED, chuỗi tiếng Việt lẫn "biến thân bay"/"thú cưỡi".
LOW: `c_AS` → `battleEnemyCounts` (cột thứ 3 của "id|cấp|n", luôn = 1 trong script); `findRoleById` (gọi `bl.a_bl(int)` của cụm data);
`resetSceneNpcs` (gọi `bl.g_V()`, chưa rõ nghĩa chính xác); `mazeGateImage1/2` (sdt1/sdt2.gf, chỉ khi `o.k_AString` có 2 điểm — mê cung ngẫu nhiên).
Lỗi thấy được: trong `drawEnding` (phần credits) nhánh máy "D608" trừ `d_I` (y của chữ kết thúc) thay vì `e_I` (y credits) → credits
cuộn chậm hơn trên D608 (có lẽ ý định là `e_I -= 5`). `scratchRole` (`c_bl`) được `new bl()` nhưng không bao giờ dùng.

## ci — ScriptEngine
Đọc `/bin/s<N>e.txt`: `eventAmount=`, mỗi `event<k>:` … `event<k>End` có `id`, `type` (bỏ qua), `name`, `area=x,y,w,h`, `auto`, `loop`,
`phase` (danh sách pha được phép), `script:` … `scriptEnd` (mỗi dòng tách bằng dấu phẩy → `bz.a_AAString`). Sự kiện đã chạy (id trong
`o` "đã xảy ra") bị bỏ qua; `loop=0` thì cuối script gọi `o.b_V(id)` đánh dấu đã xảy ra. Ngoài ra sinh sự kiện tự động: "副本战"
(cảnh 67, phó bản), cổng mê cung "换场景1/2" (`o.k_AString`), và các vùng chuyển cảnh từ `sceneChange.bin` (`S<N>A:` vùng,
`Connect:` dòng {cảnh, ?, cảnh đích, điểm vào}). Script vai NPC (`/bin/s<N>c.bin`, `role<id>:` → `group<k>:` → `script:`) do `ae` chạy khi nhấn OK cạnh NPC.
Máy thực thi: `step()` (mỗi 4 khung) chạy dòng `lineIndex` nếu > `lastExecutedLine`; `advance(mode)`: 0 sang dòng kế; 1 dừng chờ
(trừ khi còn `continuousLeft` từ 连续执行 N); ≥2 (thường 5, do cd/cr/cl gọi khi hành động bất đồng bộ xong; 3 khi đóng hộp thoại)
giảm `continuousPending`, về 0 thì sang dòng kế. Điều kiện: `lineCondition` (`b_B`) 0 chưa xét / 1 đúng / 2 sai, chốt vào `lastCondition`
(`f_Z`) khi advance; `{` đẩy (độ sâu, `lastCondition`) vào `blockConditions`, `}` bật ra; dòng chỉ chạy khi mọi khối bao ngoài đúng
(`isBlockActive`). `/*` … `*/` là chú thích. Nếu điều kiện đúng và còn token sau nó thì phần còn lại của dòng chạy như một lệnh (vd.
`已发生事件,2901,换场景,6,4`). `battleResult` (`a_B`, do bn ghi): 0 thua, 1 bỏ chạy, 2 thắng.

### Bảng opcode (token đầu dòng; "chờ" = gọi `advance(1)` hoặc return chờ cd/cf trả `advance(≥2)`)
| Opcode | Tham số | Tác dụng | Chờ |
|---|---|---|---|
| `{` / `}` | — | mở/đóng khối điều kiện | |
| `对话` | dòng thoại… (`N$id.text`, `&` = tên nhân vật chính, `@` = `randomValue`) | hộp thoại cf, NPC quay mặt | chờ |
| `对话中选择` | lựa chọn… | danh sách chọn (cf modeChoice), lưu vào `cd.chosenOption` | chờ |
| `选择了` | text [, lệnh…] | điều kiện: lựa chọn = text | |
| `提示信息` | text | hộp thông báo (mode 3) | chờ |
| `战斗` / `必败战斗` / `教学战斗` / `副本战斗` | `id|cấp|n`… | trận thường / bắt buộc thua / hướng dẫn / phó bản | chờ |
| `战斗胜利`/`胜利动作`, `战斗失败`/`失败动作`, `战斗逃跑`/`逃跑动作` | [lệnh…] | điều kiện theo `battleResult` 2/0/1 | |
| `清除BOSS`, `清除敌人` | roleId | nhấp nháy rồi xoá vai khỏi cảnh | chờ |
| `复活角色` | roleId | bỏ khỏi danh sách đã xoá `o.c_AS` | |
| `增加阶段` / `更新阶段` | — / pha | tăng / đặt pha cốt truyện `o.a_B` | |
| `去除事件` / `恢复事件` | eventId | đánh dấu đã xảy ra / gỡ đánh dấu | |
| `已发生事件` / `未发生事件` | eventId [, lệnh…] | điều kiện sự kiện đã/chưa xảy ra | |
| `获取道具` / `获取装备` / `获取技能` | id,số / id / id,cấp | nhận vật phẩm/trang bị/kỹ năng | |
| `扣除道具` / `扣除装备` / `扣除技能` | id,số / id / _,id | trừ (扣除技能 đọc tham số thứ 2) | |
| `有道具`/`无道具`, `有装备`/`无装备` (_,id), `有技能`/`无技能`, `有宠物`, `有战宠`/`无战宠`, `有同伴` | id | điều kiện sở hữu | |
| `道具数量` | id, 大于/小于, n | điều kiện số lượng vật phẩm | |
| `金钱数量` | 大于/小于, n | điều kiện vàng `o.a_I` | |
| `宠物数量` | 大于等于/小于, (n) | so số sủng vật thuộc nhân vật chính với `ct.c_B` | |
| `判断属性` | roleId, chỉ số, 大于/小于, n | so `o.a_Aao[*].a_AI[chỉ số]` | |
| `技能等级` | skillId, 大于/小于, n | so cấp kỹ năng của nhân vật chính | |
| `随机数` / `随机结果` | min,max / 大于/小于/等于, n | đặt / so `randomValue` | (advance 5) |
| `有标志` / `无标志` | — | điều kiện `df.d_Z` (cờ SMS đã mua) | |
| `是否装备` | — | nhân vật chính có trang bị ô 0 và 2 | |
| `主角ID` | id | điều kiện nhân vật chính hiện tại `o.e_S` | |
| `改变主角` | id | đổi nhân vật chính | |
| `职业等级` / `改变职业等级` | n / — | điều kiện / tăng `bl.f_B` (cấp nghề) | |
| `加入任务` / `移除任务` | questId | nhận nhiệm vụ / nộp nhiệm vụ và trao thưởng (gọi `finishQuest`, không phải bỏ nhiệm vụ — xác nhận ở data_o) | |
| `有任务` / `无任务` / `任务完成` | questId | điều kiện nhiệm vụ | |
| `随机任务` / `显示任务` | pool 1–3 / [questId] | chọn ngẫu nhiên từ `randomQuestPools` (37–50) / mở bảng nhiệm vụ (mode 9) | chờ (显示任务) |
| `有随机任务`/`无随机任务`/`完成随机任务`/`移除随机任务` | — | nhiệm vụ loại 3 / id 37–50 | |
| `换场景` / `换场景2` | cảnh, điểm vào | chuyển cảnh (换场景2: lùi tầng mê cung, dùng điểm vào thứ 2) | |
| `瞬移` | x,y | dịch cả đội | |
| `转向` / `NPC转向` | hướng / roleId,hướng | quay mặt (0 xuống 1 lên 2 trái 3 phải) | |
| `走动` | hướng, khoảng / (4,x,y) | nhân vật chính (`o.e_S`) đi | chờ |
| `人物走动` | roleId, hướng, khoảng/x,y | vai khác đi | chờ |
| `变速走动` | roleId, tốc độ, hướng, … | đi với tốc độ chỉ định | chờ |
| `平移` | roleId, hướng, px | dời tức thời | |
| `播放动作` | roleId, động tác, lần, … | chạy hoạt hình vai | (bl báo xong) |
| `载入人物` | roleId,x,y,hướng | đặt/thêm vai vào cảnh | |
| `移除NPC` | roleId | xoá vai | |
| `加入队伍` / `离开队伍` | roleId | thêm/bớt thành viên đội | |
| `载入宠物` / `移除宠物` | petId | thêm / bớt sủng vật | |
| `改变属性` | roleId, chỉ số, delta | cộng chỉ số `bl.a_AI[i]` | |
| `人物洗点` | thứ tự đội | reset điểm thuộc tính | |
| `改变金钱` | delta | cộng vàng (không âm) | |
| `改复活点` | cảnh, điểm | điểm hồi sinh `o.d_S`/`o.e_B` | |
| `休息` / `高级休息` | — | hồi HP (cao cấp: cả MP), màn "Nghỉ ngơi" | chờ |
| `属性加点` / `元素加点` / `进入图鉴` / `技能学习` / `寄宠物` / `取宠物` / `买道具` / `买装备` | … | mở menu as tương ứng | |
| `装备合成` / `镶嵌` | … | gọi `cd.openShop` nhưng cd không xử lý 2 tên này (xem dưới) | |
| `短信购买` | 99 | ảnh order.jpg (mode 10) hoặc menu SMS | |
| `失败处理` | — | `o.g()` xử lý thua | |
| `游戏存档` | — | lưu game | (advance 5) |
| `游戏结局` / `游戏结局1` | endingId | kết thúc xấu: worldState 3, màn "Ta... bị đánh bại rồi sao?" / chữ kết `end<N>Story` + credits | |
| `结束脚本` / `结束任务` | — | dừng script | |
| `屏幕闪烁` | — | chớp màn 5 lần | chờ |
| `场景抖动` / `场景抖动停` | — | bật / tắt rung | |
| `控制抖动` | roleId | đảo cờ `bl.g_Z` của vai (tên gợi ý rung vai — chưa kiểm) | |
| `黑屏` / `恢复黑屏` | — | rèm đen đóng / mở | chờ |
| `上下拉屏` / `恢复拉屏` | chiều cao / — | dải đen điện ảnh | chờ |
| `切换焦点` / `还原焦点` | hướng, số bước / — | lia camera / trả camera | chờ |
| `特写动画` | file.av, x, y, chế độ | hoạt cảnh đặc tả (mode 6) | chờ |
| `绘画章节` | storyId,x,y,h,kiểu | chữ chạy cốt truyện (cl) | chờ |
| `飞行` / `降落` | — | đổi sprite rồng long1/long2.av / hạ cánh | (advance 5) |
| `开始下雨` / `结束下雨` | — | mưa | |
| `开启副本` / `是否开启` | x,y,cảnh / — | mở cổng phó bản (fazheng.av) / điều kiện cổng đang mở | |
| `连续执行` | N | N dòng kế không dừng chờ | |
| `继承条件` | lệnh… | chạy phần còn lại nếu `lastCondition` đúng | |
Tần suất trong s*.txt: 对话 584, 已发生事件 244, 选择了 193, 提示信息 191, 人物走动 126, 对话中选择 125, 去除事件 125 …

## ca — TileMap
Định dạng `/map/*.map` (DataInputStream): 3 byte đầu bỏ qua, byte `mapFormat` (1/2), short rộng, short cao (px), byte rộng ô, byte cao ô;
byte n + n×UTF tên ảnh tileset (`/map/pics/`); short số ô định nghĩa + 4 byte cờ (1 = trường dạng short) + mỗi ô {byte ảnh, x, y, w, h, byte transform};
các lớp (format 1: danh sách {cột,hàng,ô}; format 2: mỗi lớp có kiểu 0 danh sách / 1 lưới đầy / 2 lấp kín bằng một ô, rồi int màu nền);
byte `backgroundLayerCount` (lớp < số này là hậu cảnh, còn lại tiền cảnh; âm = tất cả là hậu cảnh); short số vùng va chạm + 4 byte cờ +
{x,y,w,h, byte loại} (loại 1 = chặn, dùng trong `hitsBlock`). Nếu mọi ô của lớp đúng kích thước ô thì dựng lưới `[hàng][cột]`
("tăng tốc ma trận"). Hậu cảnh vẽ qua bộ đệm cuộn vòng (`bufferImage`, lề 2 ô) khi `at.r_Z`.
MED: `useScrollBuffer` (luôn true), `isLayerTileAligned`, `redrawBufferColumns/Rows` (vẽ lại dải cột/hàng mới lộ ra khi cuộn).

## bj — SceneLayer
`/building/s<N>sp.dat`: short n + n×UTF tên tài nguyên (.gf/.png ảnh hoặc .av hoạt hình) ; short n + mỗi tài nguyên byte k + k short
(`resourceRects`, truyền vào ctor `c`); 3 danh sách đặt {res, x, y}: giữa (`midPlacements`, vẽ xen với diễn viên theo y), nền
(`groundPlacements`), trên cùng (`topPlacements`, vẽ sau tiền cảnh tile); 3 bảng điều kiện sự kiện (int: phần tử 0 = chỉ số đặt, +id phải đã xảy ra,
−id phải chưa) và 3 bảng pha (short: phần tử 0 = chỉ số đặt, còn lại = các pha cho phép). `computeVisibility` tính một lần.
Diễn viên `actors` (cz: bl nhân vật, ax rương) sắp theo y (`sortByY`, shell sort); `skyActors` = nhân vật đang bay (bl thêm, ae bỏ).
LOW: `instance` (static `a_bj` chỉ gán trong ctor, không ai đọc), `groundActors` (`c_Acz` được vẽ nhưng không bao giờ được thêm phần tử).

## ax — TreasureChest
Một dòng `/bin/box.bin` (tab): 0 cảnh, 1 id rương, 2 pha, 3 loại (0 `/av/box.av`, 1 `/av/box1.av` cần vật phẩm 19 "Chìa vạn năng"),
4 "x,y", 5 hiển thị (1 = có hình), 6 ? (`column6`, luôn 0, không dùng — LOW), 7 vàng, 8 vật phẩm "id,số|…", 9 trang bị "id,…|…".
Rương đã mở lưu ở `o.d_AS`. Đáng ngờ: `loadChests` lọc `cảnh == hiện tại || pha == pha hiện tại` (dùng `||` chứ không `&&`), nên ở pha 0
mọi rương có cột 2 = 0 đều được nạp vào mọi cảnh (có lẽ lỗi gốc; không sửa).

## ae — FieldInput
Bộ xử lý phím của World (các mode trừ 4/5/6/7, những mode đó chuyển cho as/bn/cr/df). Mode 1: OK → bỏ qua/tăng tốc chữ chạy,
chạy sự kiện tay (`ci.manualEvent`), mở rương ("Nhận vàng", "Rương trống rỗng", "Không có Chìa vạn năng"), nói chuyện NPC (script
`/bin/s<N>c.bin`); soft trái → menu chính; soft phải → menu SMS; phím 9 → biến thân bay; '#' (debug) → bật cờ bl.f_Z. Mode 2: lật trang/chọn
trong DialogBox; mode 3: cuộn hộp thông báo; mode 8: nhận chiến lợi phẩm phó bản (+EXP, kiểm tra lên cấp); mode 9: nhận nhiệm vụ.
MED: tên class (không chỉ là "rương"); `startFlyTransform`, `applyTransform` (0 bay/hạ cánh, 1 thú cưỡi `o.g_String`, 2 thuyền `o.i_String`).
Đáng ngờ: khi đọc script vai, `phase` được đọc từ khối role chứ không phải khối group, và vòng lặp `break` ở group đầu tiên khác null
→ chỉ group 1 được xét.

## cf — DialogBox
Hai kiểu (`boxMode`): `modeTalk` (0) khung thoại có chân dung (ảnh/.av từ `o.d_AString[id-1]`, tên `o.c_AString[id-1]` tô màu
`<0x7ff5e8>`), chữ hiện dần (`typingLine`/`typingChar`, mỗi 3 khung), 3 dòng/trang; `modeChoice` (1) danh sách chọn 3 hàng có mũi tên
`/pic/arrow.gf`. Tiền tố dòng thoại: `N$id.` (N = khung chân dung 0–6, id = vai nói), `@` → `ci.randomValue`.
Lỗi nhỏ: ctor gọi `cc.a("/pic/btpc.gf")`, `cc.a_Image("/pic/jiao1.gf")`, `jiao2.gf` nhưng bỏ kết quả → `tailImage`, `cornerImage`,
`cornerImage2` luôn null (các nhánh vẽ chúng là chết). `dockBottom` (`b_Z`) luôn true → nhánh bong bóng có đuôi cũng chết (LOW cho các field đuôi).

## cl — StoryCrawl
`/bin/story.bin` khối `Story<N>:` … `Story<N>End`, `&` → tên nhân vật chính. `style` 1 = chữ cuộn lên (tốc độ `World.crawlSpeed` = 2),
khác = chữ đánh máy từng dòng giữa màn (tốc độ 8, 4 dòng). "Bỏ qua" (soft phải) bỏ qua; xong thì `ci.advance(5)`.

## bb — ScreenWipe
Hiệu ứng khi vào cảnh, kiểu chọn ngẫu nhiên trong `typeCount` (2): 0 sọc dọc trượt sang phải, 1 lưới ô vuông 30px co lại.
Trong lúc chạy đặt `cd.playerControlEnabled=false`. Mã chết: `isCovering` (`b_Z`) không bao giờ được đặt true → nhánh "che màn"
(gọi `cd.startGame()`) không bao giờ chạy; hằng `typeStripes` (0) và `e_B`/`f_B` (2, 3) không dùng. Tốc độ lớn hơn trên các máy yếu (D608, N7610, E398, L6, L7, K1, N6230i).

## j — SceneLoader
Runnable tạo và start Thread trong ctor; `run()` (đóng băng) gọi `World.loadScene()` rồi đặt `World.loadFinished = true`.

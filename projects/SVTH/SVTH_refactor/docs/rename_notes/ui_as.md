# Ghi chú đặt tên — cụm ui_as

Bảng: `mapping/part_ui_as.map` (13 class, 244 field, 89 hàm). Kiểm: `LINES_MAX=300 tools/remap_check.sh mapping/part_ui_as.map` → 0 lỗi.
Nguồn chính: `restored/raw/as.java` (vẽ), `ab.java` (xử lý phím của menu: `a_V(int)`, `c/d/e/f/g/h(int)`), `cd.java`/`ci.java`/`ae.java` (mở menu).

## ⚠ Phụ thuộc Patch/SfxUi (không sửa ở đây)
`tools/src/Patch.java` và `src/SfxUi.java` gắn theo **tên làm rối** vào: `as.o:B` (→ `menuPage`), `as.p:B` (→ `systemPage`),
`as.b:Lbx;` (→ `speakerAnim`), `as.x:I`/`as.y:I` (→ `screenW`/`screenH`), `putstatic as.a:[String` (→ `systemMenuRows`),
`as.p()V` (→ `buildSystemMenu`), `as.a(BB)V` (→ `showPage`), `as.h(Graphics)V` (→ `drawSystemTab`), lớp `as` (→ `GameMenu`).
Nếu áp remap thì phải patch trước rồi mới remap, hoặc cập nhật các tham chiếu này (cả `ab.h(I)V` của cụm ui_ab).

## GameMenu (as)
Màn menu trong game (cd.a_as), cũng dùng cho tiệm kỹ năng/vật phẩm/trang bị mở từ script. `draw(Graphics)` vẽ khung 2 tầng
(`setPanelHeights(top,bottom)`), chân phím mềm + vàng, thanh tab `MenuTabBar`, rồi dispatch theo `menuPage` (o_B):

| o_B | trang | trang con (byte tĩnh) |
|---|---|---|
| 1 | đang chọn tab (vẽ thử theo `previewPage` = z_I) | — |
| 2 | Trạng thái | `statRowIndex`, `pendingStatPoints`/`pendingStatBonus` |
| 3 | Sủng vật | `petPage` q_B: 0 list,1 menu,2 Thuộc tính,3 thả,4 thông báo,5 kỹ năng (`petSkillPage`),7 Cấy (`implantStep` r_B 0/1/2),8 Tinh luyện |
| 4 | Kỹ năng | `skillPage` x_B: 0 list,1 mô tả |
| 5 | Trang bị | `equipPage` t_B: 0 ô,1 túi,2 menu Thay/Tháo/Bỏ/Khảm,3 khảm (`socketResult` u_B) |
| 6 | Túi đồ | `bagPage` v_B: 0 tab,1 lưới,2 menu,3 bán,4 bỏ |
| 7 | Nhiệm vụ | `questPage` w_B: 0 chọn chính/phụ,1 list chính,2 list phụ,3 chi tiết,4 Xem/Bỏ,5 xác nhận xoá |
| 8 | Đồ giám | `dexPage` z_B: 0 list (lọc `b_I` 0 Mới nhất/1 Nguyên tố/2 Tất cả),1 chi tiết,2 hợp thành |
| 9 | Hệ thống | `systemPage` p_B: 0 menu,1 Lưu,2 Tải,3 âm lượng,4 thoát,5 xác nhận (`saveConfirmMode`),6 điểm QQ |
| 12 | đổi hình dạng pet (vật phẩm id 52) | `formList` |
| 13 | chọn nhân vật/pet để dùng vật phẩm | `useTargetIndex`/`useTargetScroll` |
| 14 | Tiệm kỹ năng | `skillShopPage` A_B 0/1/2, `skillLearnerType` c_I (4 = pet) |
| 15 | Tiệm vật phẩm/trang bị | `shopPage` B_B, `shopType` y_B 0 item/1 equip(/2 skill) |

Tab index của `MenuTabBar` + 2 = o_B (ab.java:41). Dữ liệu: `/bin/petdata.bin` (`petData`): cột 0 tên, 2 bốn chữ số nguyên tố
('a'=10), 4/6/8/10/12 năm chỉ số sao, 18 gen đặc trưng, 19 công thức hợp thành `id,n|…`. `/bin/shop.bin`: khoá `itemShop<n>:`,
`equipShop<n>:`, `skillShop<n>:` … `end`. Bộ trang bị (`setItemIds`) theo nghề h_S: 2/4/6 món cộng DEF/MP/HP hoặc HP/SPD/ATK.
Hộp thông báo: `showToast(text, frames)`. Ảnh đặt tên theo file .gf + chỗ dùng; nhiều ảnh bị gán null trong bản gốc
(imgmenu1/2, setup, tzjy) → luôn dùng nhánh vẽ chữ.
LOW: `G_Image`→`statSelectImg` (number02.gf vẽ nguyên ảnh ở hàng đang cộng điểm, chưa xem ảnh). Bỏ trống: `b_Image` (D23 nạp nhưng
không dùng), `j_B` (chỉ ghi 0), `a(int,String)` (callback của interface cx — gốc ở cụm ui_ab, gọi khi đổi tên pet xong).
Câu hỏi mở: `partyList` (b_l) được dựng trong `buildLearnerList` nhưng không thấy vẽ; `drawLearnerList` tự vẽ từ `hero`/`pets`.

## ListPanel (bg) và lớp con
`bg` = panel danh sách trừu tượng: hình chữ nhật `x,y,w,h`, `padX`=4/`padY`=2, `rowHeight`, `visibleRows`=h/rowHeight,
`selectedIndex`, `scrollTop`, `items` (Vector). `ab` (cụm ui_ab) ghép nhiều panel làm các cột của một danh sách và gọi
`selectNext/selectPrev/draw/clear/getSelectedIndex` cho tất cả. `resetScroll(int)` bỏ qua tham số.
- `DexListPanel` (bc): đồ giám phân trang "#  001 tên", `page`/`pageCount`, trái/phải lật trang; '???' = chưa phát hiện.
- `SideQuestListPanel` (bf): nhiệm vụ loại 2 "[Thường]" / 3 "[Công hội]", dấu hoàn thành D15. `MainQuestListPanel` (cy): loại 1, trạng thái 1 = đã nộp (xám).
- `MenuTabBar` (de): 8 tab (D03 nền, xttb biểu tượng, D12 tiêu đề, tbbj gạch chân), cuộn vòng; `isSelectMode` chỉ hiện "Chọn"; `hideArrows` khi không ở o_B 1. `setLayout` bỏ qua w/h (cố định 193x28).
- `MaterialListPanel` (db): nguyên liệu hợp thành (icon, tên, có/cần). `PetListPanel` (l): tên, "N cấp", "Xuất chiến".
- `IconListPanel` (g): cột ảnh có khung chọn. `TextListPanel` (bo): cột chữ, style 0 màu đơn / 1 chữ viền.
- `ValueListPanel` (cp, MED): cột chữ hoặc số vẽ bằng ảnh chữ số (giá ở tiệm), `showTimesPrefix` vẽ "x  " (bn).

## TextBlock (dc)
Hộp chữ dùng chung (as, bn, bs, cd, cf, df). `addText(text,width,unused,color)` ngắt dòng bằng `cg.a`; `setBounds(x,y,w,h,visibleLines)`;
`draw(g, src, firstLine, useScrollBox, centered)` (true → nhờ `k.a(...)` vẽ). Đường thẻ màu (cf hội thoại NPC): `addTaggedText`
→ `vh.wrapParts` → `parseColorTags`: cú pháp `<n>đoạn</>`, n = 1..3 chỉ vào `tagColors` {đỏ,lục,lam} hoặc literal `0x…`;
`drawTaggedLine` tô lại các đoạn. Không đặt tên `TextBox` vì trùng `javax.microedition.lcdui.TextBox`. LOW: `lineIds` (a_AS, không thấy đọc).

## QuestDetailBox (cs)
Hộp chi tiết nhiệm vụ (w_B 3): tiêu đề "Mô tả nhiệm vụ" + mô tả, "Thưởng nhiệm vụ" + danh sách thưởng `loại,id,sl|…`
(1 vật phẩm `o`, 2 trang bị `ck`, 3 vàng D10, 5 EXP /icon/13.gf), cuộn bằng `scrollLine` (ab phím lên/xuống).
Brief gọi là "item detail/tooltip box" nhưng bằng chứng (chuỗi, `o.a_Abp[p_I].b_String/e_String`) cho thấy là nhiệm vụ.

## Lỗi / mã chết quan sát được (không sửa)
- `cs.draw` thưởng loại 2: tính x bằng `o.a_AImage[o.d_I(id)]` và độ rộng chữ bằng `ck.a_AString[o.d_I(id)]` — dùng chỉ số vật phẩm thay vì trang bị (lệch vị trí/NPE tiềm ẩn).
- `as.showPreview` (a(byte)): `if (0 == 3)` hằng sai (t_B vừa gán 0). `bg.resetScroll(int)`, `de.setSelectMode(boolean)` luôn true, `de.setLayout` bỏ qua w/h, tham số thứ 3 của `dc.addText` không dùng.
- `as.l()` mở InputStream D41.av chỉ để kiểm tồn tại, không đóng.
- `actionLabels` có "Bỏ" hai lần (12 và 16). Các `System.out.println` debug ("Tiệm kỹ năng={", "sDetailStr", "petID").
- (cụm ui_ab) `ab.h` trang âm lượng phím trái gọi `Main.music.b()` không kiểm null.

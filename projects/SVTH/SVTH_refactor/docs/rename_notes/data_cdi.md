# Ghi chú đặt tên — cụm data_cdi (ck, d, c, i)

Map: `mapping/part_data_cdi.map` (4 class, 49 field, 26 hàm; không có mục LOW, 4 mục MED). `remap_check` sạch (0 lỗi), chạy riêng và chạy chung với core/battle/world/ui_as/ui_ab/sdk.

Gợi ý ban đầu trong đề ("ck = bảng chữ/tên?", "c có thể là tầng SDK/UI") không khớp mã: ck là **trang bị**, c là **công trình trên bản đồ**. Tên đặt theo mã.

## ck → `Equip`
* Vừa là **bảng tĩnh** của `/bin/equip.bin` (khối `equip`..`equipend`, cột tách bằng tab; mẫu ở `o2/bin/equip.txt`), vừa là **một món trang bị** cụ thể (`o.a_AAck` = túi đồ 6 ô theo loại, `bl.a_Ack` = đồ đang mặc).
* Cột: 0 id · 1 tên · 2 loại ô (1 vũ khí, 2 mũ, 3 áo, 4 giày, 5 dây chuyền, 6 nhẫn — đoán theo tên icon q/z, m, y, xz, xl, jz) · 3 `loại,min,max|…` chỉ số random · 4 icon · 5 nghề được mặc (`0` = mọi nghề, hoặc `1,2`) · 6 mô tả · 7 giá · 8 (?) · 9 cấp yêu cầu.
* Chỉ số random (`rollStats`, loại 1..7): 1 HP → `hp`, 2 MP → `mp`, 3 ATK → `attack`, 4 DEF → `defense`, 5 SPD → `speed`, 6 số lỗ khảm → `socketCount`, 7 CRIT → `crit`.
  Đá khảm (as.java, "Khảm") cộng vào `gemHp/gemMp/gemAttack/gemDefense/gemSpeed/gemCrit` (b_S..g_S) và tăng `gemCount` (a_B).
  `getTotalX()` = chỉ số random + đá; `bl` cộng dồn trên 6 ô.
* Chưa đặt tên: `ck.b_B` (ctor gán 1, ghi vào save nhưng khi đọc bị bỏ qua — không rõ nghĩa); `ck.e_AS` (cột 8, giá trị 30..150 tăng theo cấp, chỉ ghi không đọc ở đâu).

## d → `Skill`
* Bảng tĩnh `/bin/skill.bin` (khối `skill:`..`skillend`; mẫu `o2/bin/skill.txt`) + một kỹ năng đã học (`bl.a_Ad`).
* Cột: 0 id · 1 tên · 2 icon · 3 file anim .av (không đọc ở đây) · 4 phe mục tiêu (`targetSides`: 0 bản thân, 1 phe ta, 2 địch, 3 bị động) · 5 phạm vi (`targetScopes`: 1 đơn, 2 toàn nhóm) · 6 MP theo cấp (10 số) · 7 mô tả · 8 (?) · 9 điều kiện học `loại,cấp,cấpMax|…` (loại 1..3 = nghề nhân vật, 4 = sủng vật) · 10 giá học.
* Instance: `skillId`, `level`, `exp`, `expToNext` = (level+1)²·2, `maxLevel` (mặc định 10, lấy từ learnRules theo loại người học).
* MED: `targetSides`/`targetScopes` — suy từ AI trận (bn: phe 1 chọn ô 10..20 với kẻ địch = phe của chính nó) và mô tả kỹ năng. Chưa đặt tên `d.e_AS` (cột 8, toàn 0..4, không đọc ở đâu).

## c → `Building` (MED)
* Phần tử của `bj.a_Ac` (part_world gọi là "buildings"): hoặc ảnh `.gf/.png` (`kind`=0, anchor đáy-giữa, `bounds`={-w/2,-h,w,h}), hoặc anim `.av` (`kind`=1, `bx`, bounds lấy từ `co.getBounds`). `hitRects` = bộ 4 x,y,w,h từ `bj.d_AAS`.
* Tên class MED vì đây là "vật thể cảnh" chung (cả nhà, cây, đồ trang trí) — dùng "Building" cho thống nhất với part_world.
* Lớp này lại chứa **hàm tĩnh kiểm tra bản quyền** `isGenuineCopy()` (khi `at.needCopyRight`): trả false nếu `Runtime.totalMemory()==8000000` (dấu hiệu giả lập) hoặc (`cq.isKemu` và tìm thấy một lớp trong `at.crackClassNames`). bs: false → `da.a_B=6` (màn "chạy trên máy thật"), chặn "Chơi mới"/"Chơi tiếp".
* `c` là `public class` không final nhưng không có lớp con và không ghi đè API → đổi tên tự do.

## i → `TouchSoftKeys`
* Lớp tĩnh giữ danh sách nút cảm ứng (`cm`: a_B = id, a_AS = rect). Id 1 = phím mềm trái (đồng ý), 2 = phải (huỷ/trở về), 5 = nút giữa (thêm từ cd khi `cd.b_B==1` trên máy cảm ứng E6/E680/W958C/S1/N95/N5800).
* `setSoftKeys(mode)`: 0 xoá hết, 1 chỉ trái, 2 chỉ phải, 3 cả hai. Ảnh `/pic/D23.gf` (khung 0/1 cho trái/phải), `/pic/9.gf` cho nút giữa. Model E2/V8 đảo vị trí trái-phải.
* `hitTest(x,y)` được `pointerPressed` của da/cd/bs/df dùng. `drawingKeyId` (a_B) chỉ là biến tạm static trong `draw` (MED).

## Lỗi / mã lạ thấy được (không sửa)
1. **Mất HP từ đá khảm khi lưu**: save ghi `b_B,a_B,d_S,e_S,f_S,g_S,c_S,h_S..n_S` của trang bị — thiếu `b_S` (`gemHp`), cả túi đồ (o.java ~2033 ghi / ~1657 đọc) lẫn đồ đang mặc của nhân vật `a_Aao[].a_Ack` (~2103 / ~1737). Không chỗ nào khác lưu `b_S`.
   Lỗi người chơi gặp thật: item.txt có đá "Hậu Thổ Thạch" (hiệu ứng `1,250` = +250 HP). Sau khi lưu/tải, +HP mất nhưng `gemCount` vẫn giữ (lỗ đã dùng).
2. **cs.java ~104 và ~109** (dòng thưởng/vật phẩm loại 2 = trang bị): dùng `o.d_I(id)` (tra bảng **vật phẩm**) thay vì `o.f_I(id)` (tra bảng trang bị) ở `o.a_AImage[o.d_I(..)].getWidth()` và `stringWidth(ck.a_AString[o.d_I(..)])` — chép nhầm; sai độ rộng, hoặc AIOOBE/NPE nếu id trang bị không có trong bảng vật phẩm.
3. ab.java ~1661: `if (ck.b_AString != null) ck.a_V();` — điều kiện ngược (đúng ra `== null`); vô hại vì a_V() tự kiểm tra, nhưng nếu bảng chưa nạp sẽ NPE ngay dòng sau.
4. `ck.c_V()` khi `d_AString` còn null lại nối cột 9 vào `d_AS` lần nữa → `requiredLevels` dài gấp đôi nếu `loadTable()` đã chạy trước (chỉ số đầu vẫn đúng). Biến `var1` trong đó là mã chết.
5. `c.a(int,int,int)` (getBoxesAt): `case 0` thiếu `break` → rơi xuống `case 1`; với ảnh và type 0/1 tính lại y hệt, với type khác trả kết quả case 0. Hành vi đúng nhưng do ngẫu nhiên.
6. `ck.b_B` ghi vào save nhưng bị `readByte()` bỏ qua khi đọc; `ck.e_AS`, `d.e_AS` chỉ được ghi.

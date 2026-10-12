# RENAME_MAP — bảng đặt tên khôi phục mã nguồn (Phiên 5)

Sinh tự động bởi `tools/gen_rename_doc.py` từ `mapping/part_*.map` (viết tay, có bằng chứng từng dòng). Tên cũ ở đây theo không gian tên GỐC (như `restored/raw/*.java`: field/hàm trùng tên có đuôi kiểu `_Z`, `_AB`…). Tra ngược: `mapping/reverse.tsv`. Giải thích chung và rủi ro: `REFACTOR_LOG.md` (Phiên 5); ghi chú từng cụm: `docs/rename_notes/<cụm>.md`.

## Thống kê

| Cụm | Class | Field | Hàm | HIGH | MED | LOW |
|---|---:|---:|---:|---:|---:|---:|
| battle | 9 | 235 | 112 | 214 | 126 | 16 |
| core | 11 | 129 | 117 | 233 | 23 | 1 |
| data_cdi | 4 | 49 | 26 | 75 | 4 | 0 |
| data_hold | 15 | 132 | 32 | 148 | 27 | 4 |
| data_o | 1 | 107 | 79 | 159 | 28 | 0 |
| sdk | 41 | 370 | 281 | 668 | 17 | 7 |
| ui_ab | 7 | 68 | 39 | 98 | 14 | 2 |
| ui_as | 13 | 244 | 89 | 299 | 45 | 2 |
| world | 10 | 292 | 118 | 354 | 51 | 15 |
| **Tổng** | 111 | 1626 | 893 | 2248 | 335 | 47 |

Thành viên không có dòng trong bảng giữ **tên giữ chỗ** `<cũ>_<kiểu>` (vd. `a_Z`, `b_AI`); hàm chỉ thêm `_<kiểu trả về>` khi cần phân biệt. `Main`, `vh`, `fontcus` vốn đã đọc được. Hàm ghi đè API CLDC/MIDP (27 nhóm) bị đóng băng.

## Cụm `battle`


### `m` → **Point** (HIGH) — 2 int x,y; cj.a_m trả m(x,y); e.a_m/b_m/c_m là vị trí actor

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `m.a_I` | `x` | HIGH | cj.a_m: var5.a_I = x; so với c_m.a_I (toạ độ ngang) |
| field | `m.b_I` | `y` | HIGH | cj.a_m: var5.b_I = y |

### `cj` → **BattleMotion** (MED) — helper tĩnh tính hướng + vận tốc fixed-point (x100000) cho actor chạy tới/về (bn.f state 6/14/15)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| hàm | `cj.a(int,int)` | `directionTo` | HIGH | from-to>0 → 2 (sang trái) ngược lại 3; dùng làm e.g_I |
| hàm | `cj.a_m(int,int,int,int,int)` | `step` | HIGH | x∓vx/100000 theo hướng 2/3, y+vy/100000 → m mới (mỗi 3 tick) |
| hàm | `cj.a_AI(int,int,int,int,int)` | `velocity` | HIGH | /dx/*100000/steps, dy*100000/steps; steps = số frame anim 2 |

### `be` → **PngRecolor** (HIGH) — tìm chunk PLTE ("50 4c 54 45") trong PNG, đổi màu palette, tính lại CRC32

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `be.a_AI` | `crcTable` | HIGH | bảng CRC32 đa thức 0xEDB88320 (-306674912) |
| hàm | `be.a(byte[],int[],int[])` | `replaceColors` | HIGH | thay màu palette from[i]→to[i] (to null-lấy phần tử cuối); co.a(String,int[],int[]) gọi |
| hàm | `be.a(byte[],int)` | `scaleBrightness` | HIGH | nhân RGB palette *percent/100 (bn: D26.gf 50% làm icon tối) |
| hàm | `be.a(byte[])` | `findPalette` | HIGH | trả {offset dữ liệu PLTE, độ dài}; gặp IDAT trước → null |
| hàm | `be.a(byte[],int,int)` | `crc32` | HIGH | CRC32 chuẩn trên đoạn [off, off+len) |

### `x` → **BattleLoader** (LOW) — Runnable gọi bn.a_Z() (nạp trận) rồi bn.a_Z=true; KHÔNG có 'new x(' nào trong game (mã chết)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `x.a_Thread` | `thread` | MED | new Thread(this).start() trong constructor |
| field | `x.a_bn` | `battle` | HIGH | bn được nạp |

### `cz` → **SpriteBase** (MED) — lớp trừu tượng chung bl (nhân vật bản đồ), ax (vật thể bản đồ), bx (sprite .av): vị trí, kích thước, hiện/ẩn, khung va chạm

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cz.d_S` | `x` | HIGH | a(int,int) gán d_S=x; bx.draw dùng d_S làm toạ độ ngang |
| field | `cz.e_S` | `y` | HIGH | a(int,int) gán e_S=y |
| field | `cz.f_S` | `width` | MED | bl: = d_AS[2] (rộng ảnh); ax: 18 |
| field | `cz.g_S` | `height` | MED | bl: = d_AS[3] (cao ảnh); ax: 17 |
| field | `cz.j_Z` | `isVisible` | HIGH | bx.a(Graphics) chỉ vẽ khi j_Z; bn ẩn actor chết bằng a_bx.j_Z=false |
| field | `cz.k_Z` | `isSolid` | MED | bj.a(...) chỉ xét va chạm với object j_Z && k_Z |
| field | `cz.h_S` | `roleId` | MED | o.java:672 var1.h_S = role; o.e_AString[h_S-1]; bn: actor.d_I = bl.h_S |
| field | `cz.i_B` | `kind` | MED | 1 = nhân vật/NPC (o.java:675, cd:462), 12 = ax vật thể (cd:635) |
| field | `cz.i_S` | `headHeight` | MED | bl: chiều cao ảnh / hộp va chạm loại 2; cd vẽ biểu tượng trên đầu e_S - i_S |
| hàm | `cz.a(int,int)` | `setPosition` | HIGH | d_S=x; e_S=y |
| hàm | `cz.a(Graphics)` | `draw` | HIGH | abstract; bx/bl/ax vẽ sprite (gốc nhóm override) |
| hàm | `cz.a_AS()` | `getBounds` | MED | abstract; bx: khung bao toàn animation (co.a_AS), bl: d_AS |
| hàm | `cz.a_AS(int)` | `getCollisionBoxes` | MED | abstract; bx: hộp va chạm loại var1 tại frame hiện tại (co.a(...)); bj dùng a_AS(1) kiểm va chạm |
| hàm | `cz.b(Graphics)` | `drawDebugBoxes` | MED | abstract; bx vẽ drawRect hộp loại 1/2/3 màu đỏ/xanh/tím |

### `bx` → **AnimSprite** (HIGH) — một instance animation .av (con trỏ anim+frame) trên AvAnimation; SFX_MAP: "bộ phát animation"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bx.a_co` | `anim` | HIGH | AvAnimation dữ liệu .av (cc.a(...) cache hoặc new co()) |
| field | `bx.a_S` | `action` | HIGH | chỉ số anim (Patch sfx$anim <- bx.a:S); a_V(int) đổi |
| field | `bx.b_S` | `frame` | HIGH | frame trong anim (Patch sfx$frame <- c()); a(boolean) tăng |
| field | `bx.a_Z` | `isMirrored` | HIGH | truyền làm cờ lật ngang cho co.draw/co.getBoxes; a_AS() đảo x khi a_Z |
| hàm | `bx.a_I()` | `getAction` | HIGH | return a_S |
| hàm | `bx.b()` | `getActionCount` | HIGH | co.a_I() = số anim (h_AAS.length) |
| hàm | `bx.a(int,int,int)` | `getBoxesAt` | HIGH | co.a(action,frame,x,y,type,mirror) — hộp va chạm của anim/frame cho trước |
| hàm | `bx.c()` | `getFrame` | HIGH | return b_S (Patch sfx$frame) |
| hàm | `bx.d()` | `getFrameCount` | HIGH | số frame của anim hiện tại co.a(a_S) |
| hàm | `bx.a_I(int)` | `getActionFrameCount` | HIGH | số frame của anim var1 (Patch sfx$frames <- bx.a(I)I) |
| hàm | `bx.a(boolean)` | `nextFrame` | HIGH | b_S++ ; hết anim → về 0 nếu loop (hook onFrame trước RETURN) |
| hàm | `bx.a(Graphics,boolean)` | `drawFlipped` | MED | co.a(...,flipX=var2, flipY=false) |
| hàm | `bx.a(Graphics,int,int)` | `drawAt` | HIGH | vẽ tại (x,y) cho trước thay vì d_S/e_S |
| hàm | `bx.a(String)` | `load` | HIGH | cc.a(path,...) nạp .av (Patch loaders[0]: sfx$name) |
| hàm | `bx.a(String,int)` | `loadInGroup` | MED | như load nhưng nhóm cache var2 (cc.a(...,group)); cacheAvGroup |
| hàm | `bx.a(String,String,String[],String[],int[],int[],int)` | `loadRecolored` | MED | .av + thư mục ảnh + đổi tên ảnh + đổi màu palette (be) ; bj dùng |
| hàm | `bx.a_V(int)` | `setAction` | HIGH | đổi anim (kẹp frame); Patch: hook "đổi animation" bx.a(I)V |
| hàm | `bx.b(int)` | `setFrame` | HIGH | b_S = var1 nếu hợp lệ; Patch hook bx.b(I)V |

### `co` → **AvAnimation** (HIGH) — dữ liệu file .av: ảnh, tile, frame, hộp va chạm, anim; parser a(byte[]) (tools/avparse.py)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `co.a_AString` | `imageNames` | HIGH | nImg x readUTF |
| field | `co.b_AImage` | `images` | HIGH | ảnh gốc theo imageNames (loadImages) |
| field | `co.c_AImage` | `tileImages` | HIGH | ảnh tile cắt sẵn (cutAvImg = at.b_B 1/2) trong b() |
| field | `co.a_AS` | `tileImage` | HIGH | tile: chỉ số ảnh |
| field | `co.b_AS` | `tileX` | HIGH | tile: x trong ảnh (drawRegion src x) |
| field | `co.c_AS` | `tileY` | HIGH | tile: y |
| field | `co.d_AS` | `tileW` | HIGH | tile: w |
| field | `co.e_AS` | `tileH` | HIGH | tile: h |
| field | `co.a_AB` | `tileTransform` | HIGH | tile: byte transform (drawRegion ^ flip) |
| field | `co.a_AAS` | `frameTileX` | HIGH | frame: x từng tile |
| field | `co.b_AAS` | `frameTileY` | HIGH | frame: y từng tile |
| field | `co.c_AAS` | `frameTileIds` | HIGH | frame: chỉ số tile |
| field | `co.d_AAS` | `boxX` | HIGH | hộp va chạm: x |
| field | `co.e_AAS` | `boxY` | HIGH | hộp: y |
| field | `co.f_AAS` | `boxW` | HIGH | hộp: w |
| field | `co.g_AAS` | `boxH` | HIGH | hộp: h |
| field | `co.a_AAB` | `boxType` | HIGH | hộp: loại (lọc theo var5 trong a(int,...,boolean)) |
| field | `co.h_AAS` | `actionFrames` | HIGH | anim → danh sách chỉ số frame (avparse 'anims') |
| field | `co.c_B` | `formatVersion` | HIGH | =1; a(byte[]) đòi readByte()==c_B |
| field | `co.a_String` | `imageDir` | HIGH | thư mục ảnh (a_V(String)); bj gán trực tiếp |
| field | `co.b_String` | `path` | HIGH | đường dẫn .av; cc so khớp cache bằng b_String |
| field | `co.a_B` | `cutMode` | MED | = at.b_B (cutAvImg) lúc cắt tile; cc nạp lại ảnh nếu khác at.b_B |
| field | `co.f_AS` | `boundsCache` | HIGH | cache kết quả a_AS() {x,y,w,h} |
| field | `co.a_Z` | `flipX` | MED | cờ lật ngang khi cắt tile (var2 += 2) |
| field | `co.b_Z` | `flipY` | MED | cờ lật dọc khi cắt tile (var2++) |
| field | `co.b_AAB` | `imageBytes` | HIGH | byte PNG thô để đổi màu (be) trước createImage |
| field | `co.d_AImage` | `altImages1` | LOW | chỉ đọc khi b_B==1; không bao giờ gán (mã chết) |
| field | `co.a_AImage` | `altImages2` | LOW | public, chỉ đọc khi b_B==2; không nơi nào gán (mã chết) |
| field | `co.b_B` | `imageSet` | LOW | chọn bộ ảnh 0/1/2 khi vẽ; không nơi nào gán ≠0 |
| field | `co.c_String` | `lastImageName` | MED | tên ảnh tile đang vẽ, chỉ để in lỗi "Lỗi cắt khung‚tên ảnh=" |
| hàm | `co.a(String,int[],int[])` | `recolorImage` | HIGH | tìm ảnh theo tên → be.a(bytes, from, to) |
| hàm | `co.a_V()` | `createImages` | HIGH | Image.createImage(b_AAB[i]) rồi b_AAB=null, cutTiles() |
| hàm | `co.b()` | `cutTiles` | HIGH | cắt tile thành Image riêng theo at.b_B (cutAvImg) |
| hàm | `co.a_V(String)` | `resolveImageDir` | HIGH | null → thư mục của path; tương đối → nối; thêm '/' |
| hàm | `co.a_I()` | `getActionCount` | HIGH | h_AAS.length |
| hàm | `co.a(byte[])` | `parse` | HIGH | parser .av (brief: co.a(byte[]); tools/avparse.py) |
| hàm | `co.a(int)` | `getFrameCount` | HIGH | h_AAS[action].length |
| hàm | `co.a_AS()` | `getBounds` | HIGH | min/max của mọi tile → {x,y,w,h}, cache f_AS |
| hàm | `co.a(int,int,int,int,int,boolean)` | `getBoxes` | HIGH | hộp loại type (0=mọi) của action/frame, dời theo x,y, lật |
| hàm | `co.a(String[],String[],int)` | `loadImageBytes` | HIGH | cc.a_AB(dir+tên) → b_AAB (đổi tên ảnh theo cặp) |
| hàm | `co.a(String,String[],String[],int[],int[],int,boolean,boolean,int)` | `loadImages` | HIGH | chuỗi "Av loadImages không tìm thấy ảnh" |
| hàm | `co.a(Graphics,int,int,int,int,boolean)` | `draw` | HIGH | vẽ action/frame tại x,y, lật ngang |
| hàm | `co.a(Graphics,int,int,int,int,boolean,boolean)` | `drawFlipped` | HIGH | vẽ với flipX/flipY (anchor 8/4/32/16) |
| hàm | `co.a_Z(String)` | `load` | HIGH | đọc file (cacheAv → cc.a_AB) rồi parse; "không tồn tại" |

### `e` → **BattleActor** (HIGH) — một ô chiến đấu bn.a[slot] (0–9 phe ta, 10–19 địch); FLOWS_battle/SFX_MAP

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `e.a_I` | `slot` | HIGH | Patch sfx$slot <- e.a:I ; bn.k: a_I = var1 |
| field | `e.b_I` | `targetSlot` | HIGH | Patch sfx$target <- e.b:I |
| field | `e.c_I` | `skillId` | HIGH | bn: c_I = plan[3]; d.a_AString[c_I-1] tên kỹ năng; ==39 trinh sát |
| field | `e.a_AB` | `comboPartners` | MED | hợp kích: slot các đồng đội cùng đánh một mục tiêu (bn.f phase 3, "Hợp kích") |
| field | `e.d_I` | `roleId` | HIGH | Patch sfx$role <- e.d:I ; = bl.h_S / id petdata |
| field | `e.a_S` | `partyUid` | MED | = bl.c_S; bn.t so khớp bl.c_S để chép chỉ số về đội |
| field | `e.a_AI` | `stats` | HIGH | bảng chỉ số (Patch: [3]=HP thật, [10]=HP max); [0]lv [1]exp [2]expNext [5]MP [11]MPmax [12]ATK [13]DEF [14]SPD |
| field | `e.b_S` | `spriteW` | MED | a_bx.a(0,0,0)[2] (rộng hộp frame 0) |
| field | `e.c_S` | `spriteH` | MED | a_bx.a(0,0,0)[3]; số sát thương vẽ ở c_m.y - c_S |
| field | `e.a_String` | `name` | HIGH | tên actor (bl.a_String / petdata[0]); " Bắt đầu tấn công" |
| field | `e.e_I` | `moveType` | MED | 0 = chạy tới mục tiêu (anim 2 có >1 frame), 1 = ẩn rồi hiện (không anim chạy) |
| field | `e.a_m` | `attackPos` | HIGH | điểm đứng đánh = bn.a_m(target, self); state 14 chạy tới a_m |
| field | `e.f_I` | `state` | HIGH | Patch sfx$state <- e.f:I (0 rảnh,1/13 đánh,5 tung,6/14 chạy,10/17 hiệu ứng,2/15 về,128 chết) |
| field | `e.a_Z` | `showStatusIcon` | MED | bn vẽ icon độc/hỗn loạn/choáng khi a_Z; xoá khi hết hiệu ứng (bn.a_Z(e)) |
| field | `e.b_AB` | `targets` | HIGH | Patch sfx$targets <- e.b:[B ; mục tiêu kỹ năng |
| field | `e.c_AB` | `hitFrames` | HIGH | Patch sfx$hitFrames <- e.c:[B ; team.txt "frame=7,12,20" |
| field | `e.b_Z` | `isPresent` | HIGH | Patch sfx$visible <- e.b:Z ; bn.l: địch ẩn (a_AS[i]==0) → false |
| field | `e.c_Z` | `effectDone` | MED | bn.a(Graphics,byte[]): mọi bx hiệu ứng kỹ năng chạy xong → true |
| field | `e.d_Z` | `resultShown` | MED | sau khi hiện xong số sát thương (s_I hết bảng) → true |
| field | `e.e_Z` | `castIntroDone` | MED | ảnh chân dung b_bx tung chiêu đã chạy xong (bn.a(Graphics) f_I==5) / bỏ qua |
| field | `e.f_Z` | `isDead` | HIGH | Patch sfx$dead <- e.f:Z ; HP<=0 → f_Z=true, f_I=128 |
| field | `e.g_Z` | `blowFlag` | HIGH | Patch onBlow: putfield e.g:Z = một nhịp trúng của đòn thường; điều khiển c_bx actEffect |
| field | `e.h_Z` | `hasArrived` | MED | state 15: về tới c_m → true; a_V() đặt false |
| field | `e.i_Z` | `inCombo` | MED | hợp kích: bn đặt i_Z=true cho các thành viên; vẽ "Hợp kích" |
| field | `e.j_Z` | `hasCounter` | HIGH | có kỹ năng 7 Phản Kích (bn.k/l: a_AS[i]==7) |
| field | `e.k_Z` | `alwaysCrit` | HIGH | có kỹ năng 37 Động Sát Lực "đánh thường 100% CRIT" → m_Z=true |
| field | `e.l_Z` | `isCountering` | MED | mục tiêu có j_Z, 50% → l_Z; state 8 chờ phản đòn; nhận sát thương 15% giảm nửa |
| field | `e.m_Z` | `isCrit` | HIGH | Patch sfx$crit <- e.m:Z ; sát thương x2; vẽ "CRIT" |
| field | `e.n_Z` | `halfDamage` | HIGH | kỹ năng 36 Ý Chí Kiên Định "sát thương nhận vào giảm một nửa" → dmg/=2 |
| field | `e.o_Z` | `dodged` | HIGH | Patch sfx$dodge <- e.o:Z ; vẽ "Né" |
| field | `e.p_Z` | `isDefending` | HIGH | lệnh 2 (Phòng thủ) → p_Z=true, f_I=3; sát thương /5 |
| field | `e.a_cj` | `motion` | LOW | instance cj không dùng (cj chỉ có hàm tĩnh) |
| field | `e.g_I` | `moveDir` | HIGH | = cj.a(...) hướng 2/3 |
| field | `e.b_AI` | `moveVelocity` | HIGH | = cj.a_AI(...) vận tốc fixed-point {vx,vy} |
| field | `e.a_bx` | `sprite` | HIGH | Patch sfx$bx <- e.a:Lbx; sprite nhân vật (chr*.av / k.a(role)) |
| field | `e.b_bx` | `castPortrait` | MED | /face/zdtx1.av, zdtx2.av: chân dung khi tung kỹ năng (vẽ tên kỹ năng) |
| field | `e.a_Abx` | `effectSprites` | HIGH | bx hiệu ứng kỹ năng trên từng mục tiêu ("/ft/"+skill.txt cột 3) |
| field | `e.c_bx` | `hitEffect` | HIGH | team.txt actEffect (gongji1/2.av) vẽ trên mục tiêu khi g_Z |
| field | `e.d_bx` | `defendEffect` | HIGH | /ft/defense.av |
| field | `e.e_bx` | `poisonEffect` | HIGH | /ft/zhongdu.av (trúng độc) vẽ khi f_AB[0]>0 |
| field | `e.f_bx` | `confuseEffect` | HIGH | /ft/zhunluan.av (hỗn loạn) vẽ khi b_B>0 |
| field | `e.g_bx` | `stunEffect` | HIGH | /ft/zhongyun.av (choáng) vẽ khi a_B>0 |
| field | `e.a_AS` | `skillIds` | HIGH | mọi id kỹ năng (bl.a_AS / petdata cột 13) |
| field | `e.b_m` | `pos` | HIGH | vị trí vẽ hiện tại (a(m), cj.step cập nhật) |
| field | `e.c_m` | `homePos` | HIGH | vị trí đứng gốc (battlepos.txt pos x/y) |
| field | `e.b_AS` | `skillLevels` | MED | cấp kỹ năng của địch (petdata "id,cấp"); AI chọn cấp theo MP |
| field | `e.a_Ad` | `skills` | MED | đối tượng kỹ năng d (cấp a_B, exp a_I/b_I) của kỹ năng chủ động |
| field | `e.c_AS` | `activeSkillIds` | HIGH | a_AS trừ bị động bn.i_AS {7,29-32,35-38}; rỗng → "Không có kỹ năng khả dụng" |
| field | `e.h_I` | `expFactor` | LOW | petdata cột 1; exp thưởng a_AI[1] = h_I*h_I*level |
| field | `e.d_AB` | `elements` | HIGH | 4 nguyên tố [Địa,Thủy,Hỏa,Phong] dùng trong công thức khắc chế bn.a(byte[],byte[]) |
| field | `e.j_I` | `attack` | HIGH | = a_AI[12] (+%); công thức dmg = atk*... - def; buff kỹ năng 2/17 |
| field | `e.k_I` | `defense` | HIGH | = a_AI[13]; trừ trong công thức sát thương; kỹ năng 6 giảm |
| field | `e.l_I` | `speed` | HIGH | = a_AI[14]; bn.m sắp thứ tự lượt theo l_I; xúc xắc trúng/né; kỹ năng 38 +20% |
| field | `e.d_S` | `critRate` | HIGH | 5 + trang bị; k.a(1,100) <= d_S → m_Z |
| field | `e.e_AB` | `baseElements` | HIGH | nguyên tố gốc (vẽ icon Địa/Thủy/Hỏa/Phong) |
| field | `e.f_AB` | `poison` | HIGH | {số lượt, % HP mỗi lượt} kỹ năng 9/33; g() trừ a_AI[10]*f_AB[1]/100 |
| field | `e.a_B` | `stunTurns` | HIGH | kỹ năng 1/11/12 "choáng"; >0 bỏ lượt; icon zhongyun |
| field | `e.b_B` | `confuseTurns` | HIGH | kỹ năng 10/34 "hỗn loạn"; >0 đánh mục tiêu ngẫu nhiên |
| field | `e.c_B` | `silenceTurns` | HIGH | kỹ năng 16 Thiên Địa Mạc Ngôn; "cấm dùng kỹ năng" |
| field | `e.d_B` | `hitsLeft` | HIGH | kỹ năng 4: d_B = 2+(lv-1)/4 ("mainRole.DUOCHONG"); state 5 giảm |
| field | `e.g_AB` | `attackBuff` | HIGH | kỹ năng 2: {3 lượt, 25+lv*5 %ATK} |
| field | `e.h_AB` | `rallyBuff` | HIGH | kỹ năng 17 Chiến Ý Cao Ngang: {3/5 lượt, 12+lv*3 % ATK+DEF} |
| field | `e.i_AB` | `defenseDebuff` | HIGH | kỹ năng 6 Bối Thủy: {1 lượt, % DEF tự giảm}, trả lại khi hết |
| field | `e.m_I` | `hp` | HIGH | Patch sfx$hp <- e.m:I ; HP trong trận (trừ dần), commit vào a_AI[3] |
| field | `e.n_I` | `mp` | HIGH | Patch sfx$mp <- e.n:I ; trừ MP kỹ năng d.a_AAS |
| field | `e.a_Image` | `missImage` | HIGH | /pic/miss.gf: nửa trên "Né", nửa dưới "CRIT" |
| field | `e.o_I` | `hiddenTicks` | MED | moveType 1: đếm tick ẩn (>6 thì hiện ở đích) |
| field | `e.q_Z` | `actionAnimDone` | MED | anim hành động (đánh/tung/dùng đồ) đã xong → bắt đầu hiệu ứng/số sát thương |
| hàm | `e.a(m)` | `setPos` | HIGH | b_m = var1 |
| hàm | `e.a_I()` | `getMoveType` | HIGH | return e_I |
| hàm | `e.a(Graphics)` | `draw` | HIGH | vẽ a_bx tại b_m theo state, tiến frame mỗi 3 tick |
| hàm | `e.a_V()` | `startReturn` | HIGH | tính hướng/vận tốc từ b_m về c_m; h_Z=false |
| hàm | `e.a(int,int)` | `setAction` | HIGH | a_bx.a_V(action); a_bx.b(0) |
| hàm | `e.a(bx,int,int)` | `setSpriteAction` | HIGH | tĩnh: bx.a_V(action); bx.b(0) |

### `bn` → **Battle** (HIGH) — trận đánh: cd.a_bn, cd gọi bn.f() + bn.a(Graphics) mỗi tick khi cd.b_B==5 (FLOWS_battle.md)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bn.a_I` | `phase` | HIGH | Patch putfield bn.a:I → onPhase; 0 nhập lệnh, 2 AI+chuẩn bị, 3 thực thi lượt, 4 hội thoại, 5 kết quả trinh sát, 6 chờ dùng đồ/phòng thủ, 8 hợp kích, 24 thắng, 25 thua, 27 mở màn, 28 đang nạp |
| field | `bn.a_Ae` | `actors` | HIGH | Patch bn.a:[Le; ; slot 0–9 phe ta, 10–19 địch |
| field | `bn.a_Z` | `isLoaded` | HIGH | x.run/c_V() đặt true sau a_Z(); pha 28 chờ a_Z && tiến độ>=100 |
| field | `bn.d_I` | `turnIndex` | HIGH | FLOWS: chỉ số trong thứ tự lượt; >= a_AAS.length → g() hết vòng |
| field | `bn.j_I` | `actingSlot` | HIGH | FLOWS: slot đang hành động = a_AAS[d_I][0] |
| field | `bn.a_AAS` | `turnOrder` | HIGH | m(): {slot, speed} sắp giảm dần theo e.l_I |
| field | `bn.e_AAI` | `actionPlans` | HIGH | FLOWS bn.e[slot] = {slot, lệnh(1 đánh,2 thủ,4 kỹ năng,6 vật phẩm), mục tiêu, id kỹ năng, cấp} |
| field | `bn.i_I` | `commandSlot` | HIGH | slot phe ta đang được chọn lệnh (0 nhân vật chính, 1/2 sủng vật); "X Bắt đầu tấn công" |
| field | `bn.f_I` | `commandIndex` | HIGH | vòng lệnh: 0 Đánh 1 Phòng thủ 2 Kỹ năng 3 Vật phẩm 4 Sủng vật 5 Hạ ngay 6 Chạy / 7–10 Đánh/Phòng thủ/Kỹ năng/Lui (sủng vật) |
| field | `bn.g_I` | `menuMode` | MED | 0 vòng lệnh, 1 chọn địch, 21 chọn mục tiêu kỹ năng, 2 danh sách kỹ năng, 3 chọn cấp, 4 vật phẩm, 41 chọn đồng minh, 5 sủng vật, 51 chọn vị trí, 66 thu hồi |
| field | `bn.c_I` | `targetCursor` | HIGH | slot mục tiêu đang trỏ (g_I 1/21), ghi vào plan[2] trong s() |
| field | `bn.n_I` | `allyTargetIndex` | MED | g_I==41: chỉ số trong aliveSlots(0,10) cho vật phẩm |
| field | `bn.f_Z` | `isRingTurningRight` | MED | phím phải (5) → f_Z; vẽ vòng lệnh xoay |
| field | `bn.g_Z` | `isRingTurningLeft` | MED | phím trái (2) → g_Z |
| field | `bn.k_I` | `cursorBob` | MED | 0/3 đổi mỗi 3 tick; cộng vào y mũi tên chọn mục tiêu |
| field | `bn.b_Z` | `isStoryBattle` | MED | cd đặt true khi có danh sách địch cố định; "Không thể bỏ chạy", không phạt khi thua |
| field | `bn.b_I` | `smsContext` | LOW | 1 = mời hồi sinh khi thua, 3 = "Hạ ngay"; df dùng khi mua SMS thất bại (1→thua, 2→thắng, 3→g()) |
| field | `bn.c_B` | `tutorialStep` | HIGH | teachMode: teach<c_B>; "Hãy chọn kỹ năng trinh sát!" khi c_B==1 |
| field | `bn.d_B` | `dialogMode` | LOW | 1 = hội thoại đầu trận/dạy (xong → pha 0); 4 trả true |
| field | `bn.b_AString` | `dialogLines` | MED | enemy.bin "start:" hoặc "teach1:" → a(String[]) |
| field | `bn.a_cf` | `dialog` | MED | hộp thoại cf (pha 4) |
| field | `bn.e_Z` | `petSummoned` | MED | q() đặt true khi gọi sủng vật trong vòng; g() xoá; chặn quay lại slot 0 |
| field | `bn.i_Z` | `isSummoning` | MED | đang chạy anim tanshe.av gọi/thu hồi sủng vật (g_I 51/66) |
| field | `bn.v_I` | `summonSlot` | MED | vị trí đặt sủng vật 0/2 (g_I==51) |
| field | `bn.B_I` | `chosenPetIndex` | MED | = d_ab.b_I() khi chọn sủng vật |
| field | `bn.t_I` | `comboIndex` | MED | pha 8: duyệt thành viên hợp kích |
| field | `bn.u_I` | `scoutedSlot` | MED | kỹ năng 39 Trinh sát: slot mục tiêu (pha 5 "Thêm vào đồ giám") |
| field | `bn.c_Z` | `showBackground` | MED | vẽ nền w_Image + hạt bụi khi true; kỹ năng nhiều mục tiêu đặt false (nền đen) |
| field | `bn.d_Z` | `critFlash` | MED | actor m_Z chí mạng → nháy đỏ toàn màn (A_I đếm) |
| field | `bn.A_I` | `critFlashTicks` | MED | đếm 10 lần nháy rồi tắt d_Z |
| field | `bn.h_Z` | `roundEffectsShown` | MED | pha 0: hiện số trừ HP (độc…) sau g() rồi commit, kiểm thắng/thua |
| field | `bn.s_I` | `popupFrame` | MED | chỉ số khung trong a_AAB (bảng nhảy số sát thương), +1 mỗi 3 tick |
| field | `bn.a_AAB` | `popupOffsets` | MED | skill.bin "headNum:" → offset nhảy của số sát thương |
| field | `bn.a_Abl` | `party` | HIGH | cd: a_bn.a_Abl = o.a_Abl() (đội: nhân vật + sủng vật) |
| field | `bn.a_Acv` | `petRoster` | MED | cd: o.a_Acv(); danh sách "Sủng vật" để gọi |
| field | `bn.b_AS` | `enemyIds` | HIGH | cd.a(short[]x3) tham số 1 (id petdata); "enemy"+b_AS[0] |
| field | `bn.c_AS` | `enemyLevels` | HIGH | tham số 2 → a_AI[0] của địch |
| field | `bn.a_AS` | `enemyVisible` | MED | tham số 3; ==0 → e.b_Z=false (địch ẩn) |
| field | `bn.a_B` | `enemyCount` | HIGH | = b_AS.length hoặc random; battlepos "amount"+a_B |
| field | `bn.a_AString` | `enemyNames` | HIGH | petdata[id-1][0] |
| field | `bn.h_AS` | `enemyRoleIds` | MED | = id petdata từng địch (chỉ ghi) |
| field | `bn.a_AAString` | `petTable` | HIGH | petdata.bin "petdata" |
| field | `bn.b_AAString` | `starTable` | HIGH | petdata.bin "stardata" |
| field | `bn.a_String` | `teamData` | HIGH | nội dung /bin/team.bin (attack1/attack2/attackDefault) |
| field | `bn.d_AS` | `allyX` | HIGH | battlepos.bin team pos x |
| field | `bn.e_AS` | `allyY` | HIGH | battlepos.bin team pos y |
| field | `bn.f_AS` | `enemyX` | HIGH | battlepos.bin enemy pos x theo amountN |
| field | `bn.g_AS` | `enemyY` | HIGH | battlepos.bin enemy pos y |
| field | `bn.b_AAS` | `dropItemIds` | MED | petdata cột 14; thưởng o.b(id,1) "X 1" |
| field | `bn.c_AAS` | `dropItemRates` | MED | petdata cột 15; % (x2 khi df.b_Z) |
| field | `bn.d_AAS` | `dropEquipIds` | MED | petdata cột 16; "Nhận "+ck.a_AString |
| field | `bn.e_AAS` | `dropEquipRates` | MED | petdata cột 17; phần nghìn |
| field | `bn.f_AAS` | `usableItems` | HIGH | j(): vật phẩm o.g_AS==1 {id, số lượng}; "Không có vật phẩm dùng được" |
| field | `bn.i_AS` | `passiveSkillIds` | HIGH | {7,29,30,31,32,35,36,37,38} — bị động, không vào danh sách kỹ năng |
| field | `bn.a_AB` | `scoutedStars` | LOW | petdata cột 4,6,..,12 (giá trị đầu) của địch bị trinh sát; vẽ sao |
| field | `bn.c_AString` | `rewardItemLines` | MED | "Nhận vật phẩm": "Thịt ngon X 1"… |
| field | `bn.d_AString` | `rewardExpLines` | MED | "Nhận EXP": "<tên> EXP+n" |
| field | `bn.a_S` | `rewardScroll` | MED | cuộn danh sách vật phẩm thưởng (>6 dòng) |
| field | `bn.e_B` | `resultPage` | MED | pha 24: 0 vật phẩm, 1 EXP, 2 lên cấp |
| field | `bn.a_Vector` | `levelUps` | MED | bản sao bl trước khi lên cấp (so chỉ số cũ/mới) |
| field | `bn.o_I` | `levelUpIndex` | MED | phần tử a_Vector đang hiện |
| field | `bn.e_AString` | `defeatLines` | HIGH | {"Mất một nửa vàng","Tự về làng"} |
| field | `bn.f_B` | `defeatLineIndex` | MED | dòng thua đang hiện (pha 25) |
| field | `bn.m_I` | `loadProgress` | HIGH | 10..100 trong a_Z() |
| field | `bn.l_I` | `shownLoadProgress` | MED | tiến độ hiển thị; c_bx đặt theo l_I/100 |
| field | `bn.c_bx` | `loadingSprite` | HIGH | /av/long1.av chạy theo tiến độ ở pha 28 |
| field | `bn.q_I` | `introSlide` | MED | pha 27: trượt ảnh E020q03 lên 5px/tick |
| field | `bn.z_Image` | `introImage` | MED | /pic/E020q03.gf (pha 27) |
| field | `bn.w_Image` | `background` | HIGH | /ftmap/<o.b_B>.jpg |
| field | `bn.A_Image` | `grainImage1` | MED | /pic/grain1.gf hạt bay (c(Graphics)) |
| field | `bn.B_Image` | `grainImage2` | MED | /pic/grain2.gf |
| field | `bn.g_AAS` | `particles` | MED | {x,y,loại} hạt bay lên trong c(Graphics) |
| field | `bn.a_AI` | `menuRect` | HIGH | {x,y,193,28} khung menu giữa màn hình |
| field | `bn.h_I` | `menuHeight` | LOW | 194/200; c_V(n, h_I-n) |
| field | `bn.z_I` | `menuWidth` | MED | 191; độ rộng nền/khung |
| field | `bn.w_I` | `listHeight` | MED | c_V(): chiều cao vùng danh sách (r_Image) |
| field | `bn.x_I` | `infoHeight` | MED | c_V(): chiều cao vùng mô tả (s_Image) |
| field | `bn.y_I` | `footerHeight` | MED | 28; chân menu (t_Image + phím mềm) |
| field | `bn.r_Image` | `listPanel` | MED | k.a_Image(193, h, …) nền vùng danh sách |
| field | `bn.s_Image` | `infoPanel` | MED | nền vùng mô tả |
| field | `bn.t_Image` | `footerPanel` | MED | nền chân menu |
| field | `bn.o_Image` | `menuHeader` | MED | /pic/D03.gf |
| field | `bn.p_Image` | `menuTitle` | MED | /pic/D46.gf vẽ giữa header |
| field | `bn.q_Image` | `softKeyLabels` | MED | /pic/D06.gf (3 hàng: OK/Về; đảo cho E2/V8) |
| field | `bn.a_ab` | `itemList` | HIGH | g_I==4 danh sách vật phẩm |
| field | `bn.b_ab` | `skillList` | HIGH | g_I==2 danh sách kỹ năng (mô tả d.c_AString) |
| field | `bn.c_ab` | `skillLevelList` | HIGH | g_I==3 "… cấp", "Cấp này cần MP" |
| field | `bn.d_ab` | `petList` | HIGH | g_I==5 danh sách sủng vật |
| field | `bn.a_dc` | `infoText` | MED | dc: khung chữ mô tả vật phẩm o.g_AString |
| field | `bn.d_bx` | `petPreview` | MED | sprite sủng vật đang chọn (k.a(cv.a_I,true)) |
| field | `bn.e_bx` | `summonEffect` | MED | /ft/tanshe.av gọi/thu hồi sủng vật |
| field | `bn.f_bx` | `scoutedSprite` | MED | sprite địch bị trinh sát (pha 5) |
| field | `bn.a_bx` | `heroFace` | MED | /face/1f.av, /face/2f.av vẽ ở bảng trạng thái |
| field | `bn.b_bx` | `ringCenter` | LOW | /pic/D25.av vẽ giữa vòng lệnh |
| field | `bn.a_AAI` | `heroRing` | MED | 7 vị trí icon vòng lệnh nhân vật chính |
| field | `bn.b_AAI` | `heroRingTurning` | MED | 7 vị trí trung gian khi xoay |
| field | `bn.c_AAI` | `petRing` | MED | 4 vị trí vòng lệnh sủng vật |
| field | `bn.d_AAI` | `petRingTurning` | MED | 4 vị trí trung gian khi xoay |
| field | `bn.k_Image` | `commandIcons` | MED | /pic/D26.gf (11 icon lệnh) |
| field | `bn.l_Image` | `commandIconsDim` | MED | D26.gf qua be.a(…,50) (tối 50%) |
| field | `bn.m_Image` | `commandLabels` | MED | /pic/D27.gf tên lệnh (11 hàng) |
| field | `bn.a_AImage` | `statusPanelImages` | MED | D28 nền, D32 nhãn HP/MP/EXP, D33 thanh |
| field | `bn.a_Image` | `digits` | MED | /pic/number01.gf số trên bảng trạng thái |
| field | `bn.n_Image` | `digitsSmall` | MED | /pic/number01.gf số ở bảng sủng vật |
| field | `bn.b_Image` | `digitsBig` | MED | /pic/number03.gf số nguyên tố/chỉ số |
| field | `bn.j_Image` | `damageDigits` | HIGH | /pic/battleNumber1.gf số sát thương nảy (k.a) |
| field | `bn.c_Image` | `elementIcons` | HIGH | /pic/D02.gf; "Ảnh Địa Thủy Hỏa Phong trống" |
| field | `bn.d_Image` | `barImage` | LOW | /pic/D05.gf thanh HP/MP/EXP sủng vật |
| field | `bn.g_Image` | `statIcons` | MED | /pic/D13.gf 10 hàng icon chỉ số |
| field | `bn.h_Image` | `gainArrow` | MED | /pic/up_down.gf mũi tên tăng chỉ số khi lên cấp |
| field | `bn.i_Image` | `namePlate` | MED | /pic/D29.gf nền tên actor |
| field | `bn.e_Image` | `roleNameImage` | LOW | luôn null (dòng D09 bị bỏ); có thì vẽ tên dạng ảnh |
| field | `bn.u_Image` | `silenceIcon` | MED | /icon/12.gf vẽ khi e.c_B>0 (cấm kỹ năng) |
| field | `bn.v_Image` | `buffIcon` | MED | /icon/10.gf vẽ khi g_AB/h_AB còn lượt |
| field | `bn.x_Image` | `arrowImage` | HIGH | /pic/arrow.gf mũi tên chọn mục tiêu/danh sách |
| field | `bn.y_Image` | `starImage` | HIGH | /pic/star.gf sao (drawStars) |
| field | `bn.C_Image` | `touchBackImage` | LOW | /pic/D23.gf nút "về" cho máy cảm ứng (E6/N5800…) |
| field | `bn.p_I` | `textColor` | LOW | 0xFFFFFF |
| field | `bn.r_I` | `textOutlineColor` | LOW | 556710 |
| hàm | `bn.a_Z()` | `loadResources` | HIGH | nạp ảnh/anim/bảng, tạo actor; m_I 10..100; x.run gọi |
| hàm | `bn.c_V()` | `init` | HIGH | Patch hook battleStart (đầu bn.c()V): a_Z() rồi isLoaded=true |
| hàm | `bn.a_V(int)` | `playBattleMusic` | HIGH | tĩnh; 0 o.j_String trận, 1 thắng, 2 thua (da: pha 24→1, 25→2) |
| hàm | `bn.f()` | `tick` | HIGH | FLOWS: cập nhật mỗi tick (máy trạng thái actor + pha 2/3/6) |
| hàm | `bn.a(Graphics)` | `draw` | HIGH | cd gọi sau f(); vẽ mọi pha |
| hàm | `bn.a_Z(int)` | `handleKey` | HIGH | cd chuyển phím khi b_B==5; xử lý vòng lệnh/danh sách |
| hàm | `bn.b_V(int,int)` | `handlePointer` | MED | cd chuyển chạm (x,y); đổi thành phím mềm |
| hàm | `bn.h()` | `onVictory` | HIGH | Patch battleEnd(1); n(); o.c_I+=2; b_V() |
| hàm | `bn.e_V()` | `onDefeat` | HIGH | Patch battleEnd(2); nhạc thua, "Mất một nửa vàng", pha 25 |
| hàm | `bn.g()` | `endRound` | HIGH | Patch battleEnd(3) "hết vòng": commit HP/MP, trừ độc, giảm lượt hiệu ứng, sang lượt mới |
| hàm | `bn.b_V()` | `buildVictoryRewards` | HIGH | EXP/lên cấp, rơi đồ ("Thịt ngon X 1", "Nhận …"), pha 24 "Thắng trận"; Patch gọi là battleEnd(4)/"bỏ chạy" — SAI, xem notes |
| hàm | `bn.a_V()` | `killAllEnemies` | HIGH | mọi địch HP=0, chết, f_I=128 (df "Hạ ngay") |
| hàm | `bn.a(String[])` | `startDialog` | MED | thay '&' bằng tên nhân vật, mở cf, pha 4 |
| hàm | `bn.i()` | `loadSkillTables` | HIGH | tĩnh: skill.bin "skill:" + "headNum:" |
| hàm | `bn.j()` | `buildUsableItemList` | HIGH | f_AAS = vật phẩm có o.g_AS==1 |
| hàm | `bn.k()` | `createPartyActors` | HIGH | slot 0..n từ a_Abl: sprite, actEffect, chỉ số, kỹ năng |
| hàm | `bn.l()` | `createEnemyActors` | HIGH | slot 10+ từ petdata: chỉ số theo cấp, rơi đồ, kỹ năng, nguyên tố |
| hàm | `bn.b_V(int)` | `createPetActor` | HIGH | dựng actor cho sủng vật vừa gọi vào slot var1 |
| hàm | `bn.q()` | `summonPet` | HIGH | đưa cv đã chọn vào đội ở vị trí v_I (thay sủng vật cũ) |
| hàm | `bn.a(e,e,int)` | `rollHit` | HIGH | tĩnh: trúng nếu rand(1..100) <= 20 + spdA*100/(spdA+spdD) + spdA + bonus (FLOWS: xúc xắc né) |
| hàm | `bn.a_V(int,int)` | `normalAttack` | HIGH | dealDamage(a[x], a[y], atk, def, 0, false) |
| hàm | `bn.a(e,e,int,int,int,boolean)` | `dealDamage` | HIGH | trượt → o_Z; dmg = atk(x2 crit)*(200+khắc)/100 - def + rand(0..5), /5 thủ, /2 kỹ năng 36, min 10 → m_I-= |
| hàm | `bn.a(byte[],byte[])` | `elementAdvantage` | HIGH | tĩnh: khắc chế Địa→Thủy→Hỏa→Phong→Địa; >=10 "[Khắc]", <=-10 "[Bị khắc]" |
| hàm | `bn.a_I(e)` | `elementPassiveBonus` | HIGH | tĩnh: kỹ năng 29–32 +nguyên tố*ATK/10 |
| hàm | `bn.a_I(int,int)` | `countAlive` | HIGH | số actor sống trong [from,to) |
| hàm | `bn.a_AB(int,int)` | `aliveSlots` | HIGH | mảng slot sống trong [from,to) |
| hàm | `bn.b_I(int,int)` | `firstAliveSlot` | HIGH | slot sống đầu tiên trong [from,to), không có → from-1 |
| hàm | `bn.c_I(int,int)` | `prevAliveEnemy` | MED | lùi từ var1 tới 10 tìm địch sống, không có → var1+1 |
| hàm | `bn.b_I(int)` | `retargetIfDead` | HIGH | mục tiêu chết → chọn ngẫu nhiên actor sống cùng phe |
| hàm | `bn.m()` | `buildTurnOrder` | HIGH | a_AAS = {slot, l_I} sắp theo tốc độ |
| hàm | `bn.a_V(e)` | `clearStatus` | HIGH | xoá độc/choáng/hỗn loạn/cấm/buff + plan của actor |
| hàm | `bn.n()` | `clearAllStatus` | HIGH | a_V(e) cho mọi actor |
| hàm | `bn.o()` | `resetActionPlans` | HIGH | e_AAI[i] = {i,-1,-1,-1,-1} |
| hàm | `bn.s()` | `confirmCommand` | HIGH | FLOWS: chốt lệnh, sang sủng vật kế hoặc pha 2 |
| hàm | `bn.a_m(int,int)` | `attackPosition` | HIGH | điểm đứng trước mục tiêu var1 (± rộng sprite var2) |
| hàm | `bn.p()` | `resetActorPoses` | MED | i_Z=false, anim 0, về c_m |
| hàm | `bn.v()` | `commitHpMp` | HIGH | m_I/n_I → a_AI[3]/[5], kẹp max, chết khi HP<=0 |
| hàm | `bn.t()` | `saveStatsToParty` | HIGH | chép e.a_AI về bl tương ứng (roleId+uid) |
| hàm | `bn.u()` | `saveSkillProgressToParty` | HIGH | chép cấp/exp kỹ năng d về bl |
| hàm | `bn.a(bl[])` | `returnToMap` | HIGH | tĩnh: lưu đội, hồi HP nếu trận phải thua, thoát trận (cd.l(), ci.a(0)) |
| hàm | `bn.b_Z()` | `hasEnemySource` | MED | b_AS != null // o.a_AS != null (cd chỉ tạo trận khi true) |
| hàm | `bn.d_V()` | `dispose` | HIGH | cd gọi khi rời trận: xoá actor, ab |
| hàm | `bn.c_Z()` | `allAlliesStunned` | MED | mọi đồng minh sống có a_B (choáng) ≠ 0 |
| hàm | `bn.d_Z()` | `allAlliesConfused` | MED | mọi đồng minh sống có b_B (hỗn loạn) ≠ 0 |
| hàm | `bn.a_I(int)` | `controlStatus` | MED | 0 choáng, 1 hỗn loạn, -1 bình thường |
| hàm | `bn.e_Z()` | `allEnemiesDead` | HIGH | mọi slot 10+ f_I==128 |
| hàm | `bn.a_Z(e)` | `hasNegativeStatus` | HIGH | tĩnh: độc/choáng/hỗn loạn (không chồng hiệu ứng mới) |
| hàm | `bn.a(e,int,int)` | `applySkill` | HIGH | Patch onSkillApply(bn,e,id,lv): chọn mục tiêu, tạo bx, trừ MP, hiệu ứng từng id |
| hàm | `bn.a_e(int)` | `getActor` | HIGH | return a_Ae[slot] (SfxHooks sfx$actor) |
| hàm | `bn.a(byte[])` | `allInComboWait` | MED | mọi slot trong mảng có f_I==16 (chờ hợp kích) |
| hàm | `bn.r()` | `loadMenuImages` | MED | D03/D46/D06 + t_Image |
| hàm | `bn.c_V(int,int)` | `createMenuPanels` | MED | tạo r_Image/s_Image cao var1/var2, đặt w_I/x_I |
| hàm | `bn.b(Graphics)` | `drawScoutResult` | HIGH | "Thêm vào đồ giám thành công", "Gen đặc trưng" |
| hàm | `bn.a(Graphics,Image,int,int,int,int)` | `drawStars` | MED | tĩnh: n/2 sao đầy + nửa sao |
| hàm | `bn.a(Graphics,int,int,int,int)` | `drawStatGain` | MED | icon, giá trị cũ → mới (màn lên cấp) |
| hàm | `bn.a(Graphics,e)` | `drawStatusIcon` | HIGH | tĩnh: zhongdu/zhunluan/zhongyun theo hiệu ứng |
| hàm | `bn.a(Graphics,byte[])` | `drawSkillEffects` | HIGH | vẽ e.a_Abx trên mục tiêu; xong → effectDone |
| hàm | `bn.c(Graphics)` | `drawParticles` | MED | hạt grain bay lên |
| hàm | `bn.b(Graphics,e)` | `drawStatusPanel` | HIGH | bảng HP/MP/EXP + nguyên tố của actor đang chọn lệnh (dưới màn) |
| hàm | `bn.a(Graphics,int,int,int,int,int,int)` | `drawBarFrame` | MED | tĩnh: drawRect + drawRoundRect viền thanh |
| hàm | `bn.d(Graphics)` | `drawCommandMenu` | HIGH | theo g_I: vòng lệnh, chọn mục tiêu, danh sách kỹ năng/đồ/sủng vật |
| hàm | `bn.a(Graphics,int,int,int,int[][])` | `drawPetBars` | MED | 3 thanh HP/MP/EXP sủng vật |
| hàm | `bn.e(Graphics)` | `drawMenuPanel` | MED | khung menu: header, nền danh sách/mô tả, phím mềm |
| hàm | `bn.a(Graphics,int,int,byte[])` | `drawElements` | MED | icon Địa/Thủy/Hỏa/Phong + số |
| hàm | `bn.a(Graphics,int)` | `drawTargetInfo` | HIGH | thanh trên: nguyên tố, "[Khắc]"/"[Bị khắc]", tên, cấp, HP/MP mục tiêu |
| hàm | `bn.f(Graphics)` | `drawTopBar` | MED | tĩnh: dải nền tiêu đề trên cùng |

## Cụm `core`


### `da` → **GameCanvas** (HIGH) — extends Canvas implements Runnable, run() = vòng lặp ~60 fps, paint() chuyển theo trạng thái

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `da.a_da` | `instance` | HIGH | singleton da.a_da = this trong constructor |
| field | `da.a_B` | `screenState` | HIGH | paint(): switch 0 trống,1 hỏi nhạc,2 logo,3 cover,4 game,6 chạy trên ĐT,7 tạm dừng |
| field | `da.a_S` | `frameCounter` | HIGH | run(): ++ mỗi khung, quay về 0 ở 127 |
| field | `da.b_Z` | `loopRunning` | HIGH | while(b_Z) trong run(); a() đặt true, b() đặt false |
| field | `da.a_Z` | `paused` | HIGH | hideNotify=true / showNotify=false; run() ngủ 2s khi true |
| field | `da.c_Z` | `threadAlive` | HIGH | đặt true khi start Thread, false khi run() thoát |
| field | `da.a_Thread` | `gameThread` | HIGH | new Thread(this) trong a() |
| field | `da.a_J` | `frameStartMs` | HIGH | currentTimeMillis đầu khung |
| field | `da.b_J` | `frameEndMs` | HIGH | sau repaint+serviceRepaints |
| field | `da.c_J` | `frameAfterSleepMs` | MED | sau khi ngủ bù |
| field | `da.d_J` | `fps` | HIGH | 1000/(c_J-a_J), vẽ chữ "FPS:" ở chế độ debug |
| field | `da.d_S` | `frameIntervalMs` | HIGH | =16, Thread.sleep(d_S-(b_J-a_J)) |
| field | `da.a_av` | `logoScreen` | MED | paint case 2 -> a_av.a(g); lớp av hiển thị logo |
| field | `da.a_bs` | `titleScreen` | MED | paint case 3 -> a_bs.a(g); lớp bs là menu bìa (Chơi mới/Chơi tiếp) |
| field | `da.a_cd` | `world` | HIGH | paint case 4 -> a_cd.a(g); thế giới game |
| field | `da.a_Random` | `random` | HIGH | Random seed currentTimeMillis |
| field | `da.b_S` | `screenWidth` | HIGH | getWidth() trả b_S, khởi tạo super.getWidth() |
| field | `da.c_S` | `screenHeight` | HIGH | getHeight() trả c_S |
| field | `da.a_Display` | `display` | HIGH | setCurrent(...) |
| field | `da.a_String` | `toastText` | HIGH | a(String,int): thông báo, vẽ trong paint |
| field | `da.d_Z` | `toastActive` | HIGH | đặt true trong a(String,int); khoá keyPressed khi true |
| field | `da.a_I` | `toastFrames` | HIGH | bộ đếm lùi mỗi paint |
| hàm | `da.a(Displayable)` | `setDisplayable` | HIGH | a_Display.setCurrent(d) |
| hàm | `da.a()` | `startLoop` | HIGH | b_Z=true, tạo Thread, setCurrent(this) |
| hàm | `da.b()` | `stopLoop` | HIGH | b_Z=false |
| hàm | `da.a(String,int)` | `showToast` | HIGH | hộp thoại ngắn, N khung (vd. "Số vật phẩm đã đạt giới hạn 99",10) |

### `at` → **DeviceConfig** (HIGH) — a() đọc /bin/config.txt bằng cg.a_String/cg.b_Z

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| hàm | `at.a()` | `load` | HIGH | đọc toàn bộ khoá cấu hình |
| field | `at.a_B` | `cutMapImg` | HIGH | khoá "cutMapImg" |
| field | `at.b_B` | `cutAvImg` | HIGH | khoá "cutAvImg" |
| field | `at.c_B` | `cutImg` | HIGH | khoá "cutImg"; cg.drawFrame rẽ nhánh cắt ảnh theo nó |
| field | `at.a_Z` | `allowMusic` | HIGH | khoá "allowMusic" |
| field | `at.b_Z` | `playMusic` | HIGH | khoá "playMusic" rồi ghi đè = allowMusic; cờ bật/tắt nhạc (Patch.java đọc at.b) |
| field | `at.c_Z` | `notUseMusicClose` | HIGH | khoá "notUseMusicClose"; MusicPlayer.release bỏ close() khi true |
| field | `at.d_Z` | `allowDebug` | HIGH | khoá "alowDebug" |
| field | `at.e_Z` | `debugMode` | HIGH | khoá "debug"; GameCanvas.paint vẽ bộ nhớ + FPS |
| field | `at.a_String` | `modelName` | HIGH | khoá "model", so sánh với "N7610","L6"... |
| field | `at.a_AS` | `screenSize` | HIGH | khoá "screenSize"; at.a_AS[0]=rộng,[1]=cao |
| field | `at.e_B` | `rmsCount` | HIGH | khoá "rmsCount" (mặc định 3) |
| field | `at.a_AString` | `crackClassNames` | MED | khoá "crack"; c.b() Class.forName từng tên để phát hiện bản crack |
| field | `at.o_Z` | `useGlassMirror` | HIGH | khoá "useGlassMirror" |
| field | `at.p_Z` | `useAlphaMirror` | HIGH | khoá "useAlphaMirror" |
| field | `at.q_Z` | `useMoonicNetPay` | HIGH | khoá "useMoonicNetPay"; df.java rẽ nhánh thanh toán |
| field | `at.r_Z` | `useCarmarkCache` | HIGH | khoá "useCarmarkCache" |
| field | `at.s_Z` | `fillMapBackColor` | HIGH | khoá "fillMapBackColor" |
| field | `at.i_Z` | `cacheTxt` | HIGH | khoá "cacheTxt"; cg.loadText dùng cc.loadText khi true |
| field | `at.g_B` | `cacheTxtGroup` | HIGH | khoá "cacheTxtGroup" |
| field | `at.j_Z` | `cacheMap` | HIGH | khoá "cacheMap" |
| field | `at.h_B` | `cacheMapGroup` | HIGH | khoá "cacheMapGroup" |
| field | `at.k_Z` | `cacheAv` | HIGH | khoá "cacheAv" |
| field | `at.i_B` | `cacheAvGroup` | HIGH | khoá "cacheAvGroup" |
| field | `at.l_Z` | `cacheMusic` | HIGH | khoá "cacheMusic"; MusicPlayer.load đọc qua cc khi true |
| field | `at.j_B` | `cacheMusicGroup` | HIGH | khoá "cacheMusicGroup" |
| field | `at.m_Z` | `needCopyRight` | HIGH | khoá "needCopyRight"; c.a_Z() kiểm tra bản quyền khi true |
| field | `at.n_Z` | `useImgColorTable` | HIGH | khoá "useImgColorTable" |
| field | `at.f_Z` | `unloadBeforeBattle` | HIGH | khoá "unloadBeforeBattle" |
| field | `at.g_Z` | `moreSleep` | HIGH | khoá "moreSleep" |
| field | `at.d_B` | `defaultVolume` | MED | =80; da gán o.g_B = at.d_B khi vào game (âm lượng mặc định) |

### `ct` → **GameSettings** (HIGH) — a() đọc /bin/myConfig.txt

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| hàm | `ct.a()` | `load` | HIGH | đọc myConfig.txt |
| field | `ct.a_AString` | `firstLoadFiles` | HIGH | khoá "Firstload"; Main.firstload nạp từ danh sách này |
| field | `ct.a_B` | `skipNeedle` | MED | khoá "skipNeedle" |
| field | `ct.a_String` | `titleMusic` | HIGH | khoá "TitlesMusic" |
| field | `ct.b_B` | `goodOrBad` | MED | khoá "goodorbad"; k.drawPanel vẽ khung đặc khi ==1 (máy yếu), trong suốt khi khác |
| field | `ct.a_S` | `maxItemTypes` | MED | khoá "goodsMaxNum" (mặc định 36) |
| field | `ct.c_B` | `maxPetCount` | HIGH | khoá "maxPetNumber" (mặc định 5) |

### `cq` → **ChannelConfig** (HIGH) — Main.startApp gán tất cả từ /jzy.bin

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cq.a_AString` | `logoFiles` | HIGH | khoá "logo"; av.a_AString = cq.a_AString |
| field | `cq.a_AI` | `logoBgColors` | HIGH | khoá "logoBgColor" |
| field | `cq.a_AS` | `logoTimes` | HIGH | khoá "logoTime" |
| field | `cq.a_String` | `aboutText` | HIGH | đoạn "aboutus:".."aboutusEnd" (bs wrap 170 px) |
| field | `cq.b_String` | `smsPrice` | HIGH | khoá "smsPrice" |
| field | `cq.c_String` | `smsMessage` | HIGH | khoá "smsMsg"; df.java vẽ chữ |
| field | `cq.d_String` | `channelId` | HIGH | khoá "channelId" (JAD "cid" ghi đè nếu dài 2) |
| field | `cq.e_String` | `gameId` | HIGH | khoá "gameId"; truyền cg.a_I(gameId) cho bk.a |
| field | `cq.a_Z` | `isKemu` | HIGH | khoá "isKemu"; c.a_Z() kiểm tra crack khi true |
| field | `cq.b_Z` | `isFree` | HIGH | khoá "isFree" |
| field | `cq.c_Z` | `isBbx` | HIGH | khoá "isBbx"; bn.java:2990 rẽ nhánh |
| field | `cq.d_Z` | `useFreeCode` | HIGH | JAD "useFreeCode" |
| field | `cq.f_String` | `addMenuText` | HIGH | khoá "addMenu"; bs thêm mục menu |
| field | `cq.g_String` | `addMenuLink` | HIGH | khoá "addMenuLink"; Main.platformRequest(link) |
| field | `cq.h_String` | `cpId` | HIGH | khoá "cpId" |

### `cu` → **PackFile** (HIGH) — đọc đầu "jzy"+ver, bảng entry, ghép byte từ các tệp phần

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| hàm | `cu.a_Z(String)` | `loadIndex` | HIGH | Main gọi cu.a_Z("/00"); đọc đầu "jzy", số entry, bảng |
| hàm | `cu.a_AB(String)` | `readEntry` | HIGH | ghép dữ liệu qua các tệp phần b_String+"/"+a_String+n |
| hàm | `cu.b(String)` | `hasEntry` | HIGH | tìm tên trong bảng entry |
| field | `cu.a_String` | `partNamePrefix` | MED | readUTF từ đầu tệp; tên tệp phần = dir/prefix+số |
| field | `cu.b_String` | `packDir` | HIGH | var0.substring(0, lastIndexOf('/')) |
| field | `cu.a_I` | `entryCount` | HIGH | readInt số entry |
| field | `cu.a_AString` | `entryNames` | HIGH | readUTF mỗi entry; so khớp tên tệp |
| field | `cu.a_AI` | `entrySizes` | HIGH | readInt; số byte còn phải đọc |
| field | `cu.a_AS` | `entryFirstPart` | HIGH | readByte; chỉ số tệp phần đầu tiên |
| field | `cu.b_AI` | `entryOffsets` | HIGH | readInt; offset trong tệp phần đầu |
| field | `cu.a_Object` | `classAnchor` | HIGH | new Object().getClass().getResourceAsStream |

### `b` → **KeyMap** (HIGH) — đọc /bin/key.txt, chuẩn hoá mã phím

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| hàm | `b.a()` | `loadKeyConfig` | HIGH | khoá u,d,l,r,ok,sl,sr,number:,star,pound |
| hàm | `b.a(int)` | `normalizeKey` | HIGH | mã máy -> 1 lên,6 xuống,2 trái,5 phải,8 OK,42 *,35 # |
| field | `b.a_S` | `softLeftKey` | HIGH | =998; trả về từ normalizeKey cho phím mềm trái |
| field | `b.b_S` | `softRightKey` | HIGH | =999 |
| field | `b.a_AS` | `softLeftCodes` | HIGH | khoá "sl", mặc định {-6} |
| field | `b.b_AS` | `softRightCodes` | HIGH | khoá "sr", mặc định {-7} |
| field | `b.c_AS` | `upCodes` | HIGH | khoá "u", mặc định {-1} |
| field | `b.d_AS` | `downCodes` | HIGH | khoá "d", mặc định {-2} |
| field | `b.e_AS` | `leftCodes` | HIGH | khoá "l", mặc định {-3} |
| field | `b.f_AS` | `rightCodes` | HIGH | khoá "r", mặc định {-4} |
| field | `b.g_AS` | `okCodes` | HIGH | khoá "ok", mặc định {-5} |
| field | `b.a_AAS` | `digitCodes` | HIGH | khoá "number:" 0..9, mặc định {48..57} |
| field | `b.h_AS` | `starCodes` | HIGH | khoá "star", mặc định {42} |
| field | `b.i_AS` | `poundCodes` | HIGH | khoá "pound", mặc định {35} |

### `bv` → **MusicPlayer** (HIGH) — Player + VolumeControl, midi/wav/amr, lặp vô hạn

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bv.a_Player` | `player` | HIGH | Manager.createPlayer |
| field | `bv.a_VolumeControl` | `volumeControl` | HIGH | getControl("VolumeControl") |
| field | `bv.a_B` | `volume` | HIGH | mặc định 100; setLevel |
| field | `bv.a_String` | `fileName` | HIGH | đường dẫn đang nạp |
| hàm | `bv.a(String)` | `load` | HIGH | createPlayer theo đuôi .mid/.wav/.amr, realize, setLoopCount(-1), prefetch |
| hàm | `bv.a(int)` | `setLoopCount` | HIGH | Player.setLoopCount |
| hàm | `bv.b(int)` | `setVolume` | HIGH | VolumeControl.setLevel |
| hàm | `bv.a()` | `start` | HIGH | Player.start |
| hàm | `bv.b()` | `stop` | HIGH | Player.stop |
| hàm | `bv.c()` | `release` | HIGH | deallocate, close (trừ notUseMusicClose), gc, sleep 20ms |

### `cc` → **ResourceCache** (HIGH) — các mảng song song tên + dữ liệu + nhóm; at.cache*Group chọn nhóm

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| hàm | `cc.a(int)` | `releaseGroup` | HIGH | gỡ mọi mục có nhóm == var0 (k.a_V gọi cc.a(1), cc.a(0)) |
| hàm | `cc.a()` | `clearAll` | HIGH | null mọi bảng, cg.a() xoá cache cắt ảnh |
| hàm | `cc.a(String,String,String[],String[],int[],int[],int,boolean,boolean,int)` | `getAnimation` | HIGH | tìm/tạo co theo đường dẫn, thêm vào bảng |
| hàm | `cc.a(String)` | `loadImage` | HIGH | = a_Image(path,0) |
| hàm | `cc.a_Image(String,int)` | `loadImage` | HIGH | tìm theo tên trong b_AString, nếu không có thì cg.a_Image rồi thêm vào bảng nhóm var1 |
| hàm | `cc.b(String,int)` | `createImageUncached` | MED | Image.createImage(a_AB(path,2)); dùng cho /pic/number05.gf |
| hàm | `cc.a_AB(String,int)` | `loadBytes` | HIGH | sao chép byte từ bộ đệm hoặc cg.a_AB/cg.b_AB rồi thêm vào bảng |
| hàm | `cc.a(String,boolean,int)` | `loadText` | HIGH | tìm/đọc văn bản .txt/.bin giải mã (+10 hoặc khoá cg.a_AB) |
| field | `cc.a_Aco` | `animCache` | HIGH | bảng co (hoạt ảnh) |
| field | `cc.a_AString` | `animCachePaths` | HIGH | tên song song với a_Aco |
| field | `cc.a_AB` | `animCacheGroups` | HIGH | nhóm song song với a_Aco; releaseGroup so sánh |
| field | `cc.a_AImage` | `imageCache` | HIGH | bảng Image |
| field | `cc.b_AString` | `imageCachePaths` | HIGH | tên song song với a_AImage |
| field | `cc.b_AB` | `imageCacheGroups` | HIGH | nhóm song song với a_AImage |
| field | `cc.a_I` | `imageCachePixels` | MED | cộng w*h mỗi ảnh mới (theo dõi bộ nhớ) |
| field | `cc.a_AAB` | `bytesCache` | HIGH | byte đã đọc |
| field | `cc.c_AString` | `bytesCachePaths` | HIGH | tên song song |
| field | `cc.c_AB` | `bytesCacheGroups` | HIGH | nhóm song song |
| field | `cc.e_AString` | `textCachePaths` | HIGH | tên văn bản đã nạp |
| field | `cc.d_AString` | `textCacheValues` | HIGH | nội dung văn bản đã nạp |

### `cg` → **Utils** (HIGH) — ~80 hàm tĩnh dùng khắp game

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cg.a_Font` | `defaultFont` | HIGH | Font.getFont(0,0,8): face/style/size mặc định, truyền setFont mọi paint |
| field | `cg.a_S` | `fontWidthMM` | MED | vh.stringWidth(font,"MM") |
| field | `cg.b_S` | `lineHeight` | HIGH | vh.getHeight(font); dùng xếp dòng khắp nơi |
| field | `cg.a_AB` | `configKey` | HIGH | {7}: byte trừ khi giải mã tệp .bin |
| field | `cg.b_AB` | `gfKey` | HIGH | {7,3,8,...}: cộng vào mỗi byte thứ 10 của .gf |
| field | `cg.c_AB` | `pngHeader` | HIGH | 89 50 4E 47 0D 0A 1A 0A + IHDR: ghép trước dữ liệu .gf |
| field | `cg.a_Hashtable` | `imageSliceCache` | HIGH | Image -> Image[] các ô đã cắt |
| field | `cg.a_Vector` | `existingResources` | HIGH | đường dẫn đã xác nhận tồn tại |
| field | `cg.b_Vector` | `missingResources` | HIGH | đường dẫn đã xác nhận không tồn tại |
| field | `cg.a_Object` | `classAnchor` | HIGH | getClass().getResourceAsStream |
| hàm | `cg.a(boolean[],boolean)` | `append` | HIGH | tạo mảng dài hơn 1, chép, gán phần tử cuối |
| hàm | `cg.a_AB(byte[],int)` | `append` | HIGH | như trên |
| hàm | `cg.a(byte[][],byte[])` | `append` | HIGH | như trên |
| hàm | `cg.a_AI(int[],int)` | `append` | HIGH | như trên |
| hàm | `cg.a(int[][],int[])` | `append` | HIGH | như trên |
| hàm | `cg.a_AS(short[],int)` | `append` | HIGH | như trên |
| hàm | `cg.a_AS(short[],short[])` | `appendAll` | HIGH | nối cả mảng var1 vào cuối var0 |
| hàm | `cg.a(short[][],short[])` | `append` | HIGH | như trên |
| hàm | `cg.a(String[],String)` | `append` | HIGH | như trên |
| hàm | `cg.a(String[][],String[])` | `append` | HIGH | như trên |
| hàm | `cg.a_Z(byte[],int)` | `contains` | HIGH | duyệt tuyến tính |
| hàm | `cg.a_Z(int[],int)` | `contains` | HIGH | duyệt tuyến tính |
| hàm | `cg.a_Z(short[],int)` | `contains` | HIGH | duyệt tuyến tính |
| hàm | `cg.b(byte[],int)` | `removeValue` | HIGH | tìm phần tử == var1 rồi gọi removeAt |
| hàm | `cg.b(int[],int)` | `removeValue` | HIGH | như trên |
| hàm | `cg.b(short[],int)` | `removeValue` | HIGH | như trên |
| hàm | `cg.c(byte[],int)` | `removeAt` | HIGH | dịch trái, trả mảng ngắn hơn 1 (null nếu còn 1 phần tử) |
| hàm | `cg.a(byte[][],int)` | `removeAt` | HIGH | như trên |
| hàm | `cg.c(int[],int)` | `removeAt` | HIGH | như trên |
| hàm | `cg.c(short[],int)` | `removeAt` | HIGH | như trên |
| hàm | `cg.a(short[][],int)` | `removeAt` | HIGH | như trên |
| hàm | `cg.a(String[],int)` | `removeAt` | HIGH | như trên |
| hàm | `cg.a(int,int,int,int,int,int,int,int)` | `rectsOverlap` | HIGH | x1<x2+w2 && y1<y2+h2 && x1+w1>x2 && y1+h1>y2 |
| hàm | `cg.a_Z(short[],short[])` | `rectsOverlap` | HIGH | cùng công thức, hình chữ nhật dạng short[4] |
| hàm | `cg.a()` | `clearImageSliceCache` | HIGH | a_Hashtable.clear() |
| hàm | `cg.a_Z(String,String)` | `isSameImagePath` | HIGH | coi .png và .gf cùng tên là một |
| hàm | `cg.c_Z(String)` | `resourceExists` | HIGH | getResourceAsStream + cache a_Vector/b_Vector |
| hàm | `cg.a_Z(String)` | `imageExists` | HIGH | thử .png<->.gf, cả trong cu pack |
| hàm | `cg.a_AB(String,boolean)` | `readResource` | HIGH | thử path, /pub/<tên>, rồi gói cu nếu var1 |
| hàm | `cg.a_AB(String)` | `readResource` | HIGH | = a_AB(path,true) |
| hàm | `cg.c_AB(String)` | `readGfAsPng` | HIGH | giải mã .gf: +gfKey mỗi 10 byte, ghép pngHeader |
| hàm | `cg.a(byte[])` | `createImage` | HIGH | Image.createImage(bytes,0,len) |
| hàm | `cg.a_Image(String)` | `loadImage` | HIGH | b_AB(path) rồi createImage |
| hàm | `cg.b_AB(String)` | `loadImageBytes` | HIGH | rẽ nhánh .png/.gf/.jpg, thử đổi đuôi |
| hàm | `cg.a(Graphics,Image,int,int,int,int,int,int,int)` | `drawFrame` | HIGH | cắt ô (w,h) theo chỉ số khung, vẽ tại (x,y,anchor); có cache ô khi cutImg!=0 |
| hàm | `cg.a(Graphics,int,int,int,Image,int,int,int)` | `drawNumber` | HIGH | vẽ số bằng dải ảnh chữ số, ký tự 10 là dấu |
| hàm | `cg.a(int)` | `digitCount` | HIGH | String.valueOf(n).length() |
| hàm | `cg.a(Graphics,String,int,int,int,int,int)` | `drawOutlinedText` | HIGH | viền 4 hướng màu var5, chữ màu var4 |
| hàm | `cg.b(Graphics,String,int,int,int,int,int)` | `drawOutlinedText8` | HIGH | viền 8 hướng |
| hàm | `cg.a(String,int)` | `wrapText` | HIGH | = vh.wrap |
| hàm | `cg.a(String,String,int)` | `splitAndWrap` | HIGH | tách theo dấu phân cách rồi wrap từng đoạn |
| hàm | `cg.a_String(String)` | `loadConfigText` | HIGH | = a_String(path, !allowDebug) |
| hàm | `cg.a_String(String,boolean)` | `loadText` | HIGH | đọc .txt/.bin, giải mã byte (trừ configKey hoặc +10), UTF-8; cache cc khi cacheTxt |
| hàm | `cg.a_String(String,String)` | `getValue` | HIGH | dòng bắt đầu "khoá=" -> phần sau |
| hàm | `cg.a(String,String,String)` | `substringBetween` | HIGH | đoạn giữa hai dấu mốc |
| hàm | `cg.b_AString(String,String)` | `split` | HIGH | tách theo dấu phân cách, bỏ phần rỗng |
| hàm | `cg.a_AString(String,String)` | `getStringArray` | HIGH | split(getValue(text,key), ",") |
| hàm | `cg.a_AString(String,String,String,String)` | `getStringBlock` | HIGH | khối giữa hai mốc, mỗi dòng tách bởi "\r\n" |
| hàm | `cg.a_AString(String,String,String,String,String)` | `getStringBlock` | HIGH |  |
| hàm | `cg.a_AAString(String,String,String,String)` | `getStringTable` | HIGH | mảng 2 chiều theo dòng và dấu phân cách "," |
| hàm | `cg.a_AAString(String,String,String,String,String)` | `getStringTable` | HIGH |  |
| hàm | `cg.a_AAS(String,String,String,String)` | `getShortTable` | HIGH | như trên nhưng short[][] |
| hàm | `cg.a_AB(String,String)` | `getByteArray` | HIGH | b_AB(getValue,",") |
| hàm | `cg.a_AAB(String,String,String,String)` | `getByteTable` | HIGH |  |
| hàm | `cg.a_AI(String,String)` | `getIntArray` | HIGH | b_AI(getValue,",") |
| hàm | `cg.a_AS(String,String)` | `getShortArray` | HIGH | b_AS(getValue,",") |
| hàm | `cg.a_B(String,String)` | `getByte` | HIGH | a_B(getValue) |
| hàm | `cg.a_I(String,String)` | `getInt` | HIGH | a_I(getValue) |
| hàm | `cg.a_S(String,String)` | `getShort` | HIGH | a_S(getValue) |
| hàm | `cg.b_Z(String,String)` | `getBool` | HIGH | b_Z(getValue) : "1" -> true |
| hàm | `cg.b_AB(String,String)` | `parseByteArray` | HIGH | tách theo dấu phân cách rồi parse |
| hàm | `cg.b_AI(String,String)` | `parseIntArray` | HIGH |  |
| hàm | `cg.b_AS(String,String)` | `parseShortArray` | HIGH |  |
| hàm | `cg.b_Z(String)` | `parseBool` | HIGH | a_I(s)==1 |
| hàm | `cg.a_B(String)` | `parseByte` | HIGH |  |
| hàm | `cg.a_I(String)` | `parseInt` | HIGH |  |
| hàm | `cg.a_S(String)` | `parseShort` | HIGH |  |

### `k` → **GameUtil** (MED) — tập hợp hàm tĩnh lẫn lộn; không có trạng thái lõi

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `k.a_Random` | `random` | HIGH | Random lazy trong k.a(int,int) |
| field | `k.a_Image` | `scrollArrowImage` | HIGH | nạp /pic/arrowhead.gf |
| field | `k.b_Image` | `scrollThumbImage` | HIGH | nạp /pic/arrow1.gf |
| field | `k.c_Image` | `panelImage` | MED | Image.createRGBImage cho khung mờ (drawPanel) |
| field | `k.a_B` | `scrollLine` | HIGH | dòng đầu tiên trong cuộn chữ |
| field | `k.a_I` | `scrollTick` | HIGH | đếm khung; sau 10 thì bắt đầu cuộn |
| field | `k.b_B` | `scrollOffset` | HIGH | offset pixel +6 mỗi 3 khung, về 0 khi qua 1 dòng |
| field | `k.a_Z` | `scrollMoving` | HIGH | true khi đang trượt |
| field | `k.b_I` | `marqueeX` | HIGH | vị trí x chữ chạy ngang |
| hàm | `k.a_V()` | `freeImageCaches` | HIGH | cc.a(1), cc.a(0), System.gc() |
| hàm | `k.a(String,String,String)` | `replaceAll` | MED | thay chuỗi (bỏ qua 1 ký tự sau mỗi lần thay: lỗi nhỏ) |
| hàm | `k.a(byte[])` | `sortBytes` | HIGH | bubble sort |
| hàm | `k.a(bl)` | `isInTeam` | MED | duyệt o.a_Abl (đội hình) so sánh tham chiếu |
| hàm | `k.a(String[],String)` | `parseShortRows` | HIGH | mỗi phần tử -> cg.b_AS(s, delim/",") |
| hàm | `k.a(int,int)` | `randomRange` | HIGH | base + /rand/ % range |
| hàm | `k.b(int,int)` | `randomBetween` | HIGH | [min,max] gồm cả hai đầu (a(min, max-min+1)) |
| hàm | `k.a(int,int,int)` | `pickTwoDistinct` | MED | lấy ngẫu nhiên 2 giá trị khác nhau từ 0..n |
| hàm | `k.a(int[],int)` | `pickRandomExcept` | MED | bỏ var1 rồi chọn ngẫu nhiên một phần tử |
| hàm | `k.a(Graphics,String[],int,int,int,int,int,int,int[],boolean)` | `drawScrollingText` | HIGH | cuộn dọc từng dòng, clip, màu theo dòng |
| hàm | `k.b()` | `resetScroll` | HIGH | đặt 0 các biến cuộn |
| hàm | `k.a(String[][],String[])` | `append` | HIGH | nối phần tử cuối |
| hàm | `k.a(short[][][],short[][])` | `append` | HIGH | nối phần tử cuối |
| hàm | `k.a(int[])` | `maxValue` | HIGH | lớn nhất, -1 nếu null |
| hàm | `k.a(Graphics,Image,Image,int,int,int,int,int,int)` | `drawScrollBar` | HIGH | thanh cuộn: arrowhead + mũi tên theo vị trí |
| hàm | `k.a(String,String,String,String,String)` | `getShortList` | MED | khối giữa hai mốc -> khoá -> tách -> short[] |
| hàm | `k.a(Graphics,String,int)` | `drawMarquee` | MED | chữ chạy ngang, quay lại khi ra khỏi màn |
| hàm | `k.a(Graphics,String,int,int)` | `drawMessageBox` | HIGH | hộp thoại nền chữ trắng giữa màn |
| hàm | `k.a(Graphics,int,int,int,int,int,int,int,boolean)` | `drawPanel` | HIGH | khung mờ; ct.goodOrBad==1 -> khung đặc |
| hàm | `k.c()` | `clearPanelImages` | HIGH | null ảnh khung/mũi tên (trừ vài máy Nokia) |
| hàm | `k.a_Image(int,int,int,int)` | `createTranslucentImage` | HIGH | createRGBImage(buffer alpha 216) |
| hàm | `k.a(Graphics,String,int,int,int,int)` | `drawBoldText` | HIGH | vẽ chữ 2 lần lệch 1px |
| hàm | `k.a(short,short,short)` | `addDungeonPortal` | MED | thêm cổng "Dịch chuyển phó bản" -> map var2 ("换场景") |
| hàm | `k.a(Graphics,bx,int,int,boolean,int)` | `drawAnimation` | MED | đặt vị trí + setAction + vẽ, nhấp nháy theo da.frameCounter |
| hàm | `k.a(int,boolean)` | `loadBattleAnimation` | MED | "/ft/N.av" kèm bảng màu quái; N=(id/4 làm tròn lên) |

### `h` → **RankEntry** (LOW) — lớp rỗng; an.java khởi tạo new h() trong nhánh parse "uid/age/nickName/score" (bảng xếp hạng)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `vh.H_I` | `fontHeight` | HIGH | vh.getHeight(Font) trả H_I (=15); dùng neo toạ độ chữ trong drawString |

## Cụm `data_cdi`


### `ck` → **Equip** (HIGH) — new ck(id) đọc /bin/equip.bin; "createEquip specialField logic err"; o.a_AAck = túi trang bị 6 ô, bl.a_Ack = đồ đang mặc

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ck.a_AS` | `ids` | HIGH | cột 0 (id); o.f_I(id) tìm chỉ số trong mảng này |
| field | `ck.a_AString` | `names` | HIGH | cột 1; "Mua "+ck.a_AString[..], "Nhận "+..., "Trang bị ... bộ [ ... ]" |
| field | `ck.b_AS` | `slotTypes` | HIGH | cột 2 (1 vũ khí .. 6 nhẫn); o.c_V: ck.b_AS[id-1]-1 = ô túi o.b_AAS |
| field | `ck.d_AString` | `statRollSpecs` | HIGH | cột 3 "loại,min,max/..." -> c_V() random cộng chỉ số (loại 1 HP..7 CRIT) |
| field | `ck.e_AString` | `iconNames` | HIGH | cột 4; b_V(): "/icon/"+tên+".gf" |
| field | `ck.b_AString` | `allowedJobs` | HIGH | cột 5 "0" hoặc "1,2"; ab: 0 hoặc ==bl.h_S mới mặc được, không thì "Nhân vật không phù hợp" |
| field | `ck.c_AString` | `descriptions` | HIGH | cột 6 mô tả; as hiển thị dưới tên |
| field | `ck.c_AS` | `prices` | HIGH | cột 7; ab: o.a_I < c_AS*k -> "Không đủ vàng", trừ vàng khi mua |
| field | `ck.d_AS` | `requiredLevels` | HIGH | cột 9; a(ck): "Cấp yêu cầu "; ab so với bl.a_AI[0] (cấp) |
| field | `ck.a_AImage` | `icons` | HIGH | b_V() nạp /icon/*.gf; Main giải phóng |
| field | `ck.a_S` | `equipId` | HIGH | ctor: chỉ số+1 trong bảng (= cột id); o.f_I(a_S); lưu/đọc RMS qua id |
| field | `ck.a_B` | `gemCount` | HIGH | số lỗ đã khảm: "Hết lượt khảm" khi a_B>=m_S; as: a_B++ sau khi khảm; a(ck): "Lỗ a_B/m_S" |
| field | `ck.m_S` | `socketCount` | HIGH | loại 6 trong statRollSpecs; ==0 -> "Trang bị này không thể khảm" |
| field | `ck.b_S` | `gemHp` | HIGH | as khảm đá loại 1 cộng vào; d() = h_S+b_S (" HP+") |
| field | `ck.c_S` | `gemMp` | HIGH | đá loại 2; e() (" MP +") |
| field | `ck.d_S` | `gemAttack` | HIGH | đá loại 3; b_I() (" ATK+") |
| field | `ck.e_S` | `gemDefense` | HIGH | đá loại 4; c_I() (" DEF+") |
| field | `ck.f_S` | `gemSpeed` | HIGH | đá loại 5; a_I() (" SPD+") |
| field | `ck.g_S` | `gemCrit` | HIGH | đá loại 6; bn cộng n_S+g_S vào chí mạng (" CRIT+") |
| field | `ck.h_S` | `hp` | HIGH | c_V(): random loại 1 |
| field | `ck.i_S` | `mp` | HIGH | loại 2 |
| field | `ck.j_S` | `attack` | HIGH | loại 3 (vũ khí: "3,60,80") |
| field | `ck.k_S` | `defense` | HIGH | loại 4 (mũ/áo/giày) |
| field | `ck.l_S` | `speed` | HIGH | loại 5 (giày) |
| field | `ck.n_S` | `crit` | HIGH | loại 7 (nhẫn) |
| hàm | `ck.a_V()` | `loadTable` | HIGH | parse /bin/equip.bin "equip".."equipend" vào các mảng tĩnh |
| hàm | `ck.b_V()` | `loadIcons` | HIGH | "/icon/"+e_AString[i]+".gf" |
| hàm | `ck.c_V()` | `rollStats` | HIGH | random theo statRollSpecs (k.b(min,max)) vào h_S..n_S |
| hàm | `ck.a(ck)` | `describeStats` | HIGH | " HP+".." CRIT+"," Lỗ ", " Cấp yêu cầu " |
| hàm | `ck.a_I()` | `getTotalSpeed` | HIGH | f_S+l_S; bl cộng dồn (SPD) |
| hàm | `ck.b_I()` | `getTotalAttack` | HIGH | d_S+j_S (ATK) |
| hàm | `ck.c_I()` | `getTotalDefense` | HIGH | e_S+k_S (DEF) |
| hàm | `ck.d()` | `getTotalHp` | HIGH | h_S+b_S (HP) |
| hàm | `ck.e()` | `getTotalMp` | HIGH | i_S+c_S (MP) |

### `d` → **Skill** (HIGH) — new d(id,cấp,loại) đọc /bin/skill.bin "skill:".."skillend"; bl.a_Ad = kỹ năng đã học; " cấp"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `d.a_AS` | `ids` | HIGH | cột 0; o.c_I(id) tìm chỉ số |
| field | `d.a_AString` | `names` | HIGH | cột 1; bn vẽ tên kỹ năng khi dùng |
| field | `d.b_AString` | `iconNames` | HIGH | cột 2; "/icon/"+b_AString[i]+".gf" |
| field | `d.b_AS` | `targetSides` | MED | cột 4: 0 bản thân, 1 phe ta (bn AI chọn ô 10..20 của địch = phe mình), 2 địch, 3 bị động ("Phản Kích") |
| field | `d.c_AS` | `targetScopes` | MED | cột 5: 1 đơn mục tiêu, 2 toàn nhóm (mô tả "cả nhóm", "toàn bộ phe ta") |
| field | `d.a_AAS` | `mpCostsPerLevel` | HIGH | cột 6 (10 giá trị); bn: "Cấp này cần MP : " + a_AAS[skill][level] |
| field | `d.c_AString` | `descriptions` | HIGH | cột 7 "[Vật lý] ..." |
| field | `d.a_AAAS` | `learnRules` | HIGH | cột 9 "loại,cấp,cấpMax/...": [0]=nghề 1..3 / 4=sủng vật, [1]=cấp cần (as "Cấp:"), [2]=cấp tối đa (ctor -> b_B) |
| field | `d.d_AS` | `learnPrices` | HIGH | cột 10; as "Cần vàng:"; ab trừ o.a_I khi học |
| field | `d.a_AImage` | `icons` | HIGH | b() nạp /icon/*.gf; Main giải phóng |
| field | `d.a_S` | `skillId` | HIGH | ctor tham số 1; so với d.a_AS[...] trong ab |
| field | `d.a_B` | `level` | HIGH | as: name+" "+a_B+" cấp"; bn tăng khi a_I>=b_I |
| field | `d.a_I` | `exp` | HIGH | bn cộng kinh nghiệm kỹ năng sau khi dùng; lưu RMS readInt |
| field | `d.b_I` | `expToNext` | HIGH | (level+1)^2*2 ở ctor và khi lên cấp (bn) |
| field | `d.b_B` | `maxLevel` | HIGH | mặc định 10, lấy learnRules[..][2] theo loại; bn: chỉ cộng exp khi a_B<=b_B |
| hàm | `d.a()` | `loadTable` | HIGH | parse /bin/skill.bin |
| hàm | `d.b()` | `loadIcons` | HIGH | "/icon/"+b_AString[i]+".gf" |

### `c` → **Building** (MED) — phần tử bj.a_Ac (part_world: "buildings"): ảnh .gf/.png hoặc anim .av (bx) + hình chữ nhật va chạm; còn chứa kiểm tra bản quyền tĩnh a_Z()

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `c.a_B` | `kind` | HIGH | 0 = ảnh (ctor Image), 1 = anim (ctor bx); switch trong draw/getBoxesAt |
| field | `c.a_Image` | `image` | HIGH | drawImage(..., 33) anchor đáy-giữa |
| field | `c.a_bx` | `anim` | HIGH | bx; draw đặt d_S/e_S rồi bx.a(g) |
| field | `c.b_AS` | `hitRects` | HIGH | bj.d_AAS (resourceRects); bộ 4 x,y,w,h; vẽ đỏ trong b(..) |
| field | `c.a_AS` | `bounds` | HIGH | ảnh: {-w/2,-h,w,h}; anim: co.a_AS() (getBounds) |
| hàm | `c.a_AS()` | `getBounds` | HIGH | trả a_AS; bj dùng để cull theo viewport |
| hàm | `c.a(Graphics,int,int)` | `draw` | HIGH | vẽ ảnh/anim tại x,y (bj drawMiddle/drawTop) |
| hàm | `c.b(Graphics,int,int)` | `drawDebugRects` | HIGH | setColor(0xFF0000)+drawRect; bj.a(Graphics)=drawDebugRects |
| hàm | `c.a(int,int,int)` | `getBoxesAt` | HIGH | hitRects dời theo x,y (anim: co.getBoxes); bj.collides dùng |
| hàm | `c.a_V()` | `nextFrame` | HIGH | anim: bx.a(true); bj.animateBuildings |
| hàm | `c.a_Z()` | `isGenuineCopy` | HIGH | at.needCopyRight; false nếu totalMemory==8000000 hoặc (cq.isKemu và có lớp crack); bs: false -> da.a_B=6 ("chạy trên máy thật") |
| hàm | `c.b()` | `noCrackClassFound` | HIGH | Class.forName từng at.crackClassNames; true khi không tìm thấy lớp nào |

### `i` → **TouchSoftKeys** (HIGH) — danh sách nút cảm ứng cm (id 1 trái, 2 phải, 5 giữa); da/cd/bs/df pointerPressed gọi i.a(x,y)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `i.a_Vector` | `keys` | HIGH | Vector<cm>; a(cm) thêm, a_V(byte) xoá |
| field | `i.a_B` | `drawingKeyId` | MED | biến tạm trong draw: id nút đang vẽ |
| field | `i.a_Image` | `softKeyImage` | HIGH | /pic/D23.gf, vẽ ở góc dưới trái/phải (khung 0/1) |
| field | `i.b_Image` | `centerKeyImage` | HIGH | /pic/9.gf, nút id 5 giữa đáy khi cd.b_B==1 (máy cảm ứng E6/N5800...) |
| hàm | `i.a(int)` | `setSoftKeys` | HIGH | 0 xoá hết, 1 chỉ nút trái, 2 chỉ nút phải, 3 cả hai |
| hàm | `i.a(Graphics)` | `draw` | HIGH | vẽ các nút; E2/V8 đảo trái-phải |
| hàm | `i.a()` | `addLeftKey` | HIGH | cm id 1, rect 26x26 góc dưới trái (E2/V8: phải) |
| hàm | `i.b()` | `addRightKey` | HIGH | cm id 2, góc dưới phải (E2/V8: trái) |
| hàm | `i.a_Z(byte)` | `hasKey` | HIGH | có cm với a_B==id; cd kiểm id 5 |
| hàm | `i.a_V(byte)` | `removeKey` | HIGH | removeElementAt theo id |
| hàm | `i.a(cm)` | `addKey` | HIGH | addElement; cd thêm nút giữa id 5 |
| hàm | `i.a(int,int)` | `hitTest` | HIGH | cg.a(rect, x,y) -> cm bị chạm; pointerPressed |

## Cụm `data_hold`


### `bl` → **RoleSprite** (MED) — extends cz; nhân vật trên bản đồ (hero/NPC/pet), tạo bởi o.a_bl(int) từ /bin/role.bin, stats a_AI[21]

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bl.e_B` | `statCount` | HIGH | static =21; new int[bl.e_B] cho a_AI (o.a(bl), o.a_bl(cv), bn) |
| field | `bl.b_B` | `lastMoveDirection` | MED | static; b_V() gán = d_B sau mỗi bước; cd khôi phục o.a_Abl[0].a_V(bl.b_B) |
| field | `bl.a_B` | `renderMode` | HIGH | 0 = ảnh tĩnh (ctor Image), 1 = animation .av (ctor bx); a(Graphics)/a_AS(int) switch |
| field | `bl.a_Image` | `image` | HIGH | ctor bl(Image): drawImage(a_Image, x, y, 33) |
| field | `bl.a_bx` | `animSprite` | HIGH | ctor bl(bx); a(Graphics) vẽ a_bx tại d_S/e_S |
| field | `bl.c_B` | `moveSpeed` | HIGH | mặc định 4; b_V() dịch d_S/e_S thêm c_B mỗi bước; biến thân chạy =5, NPC =1 |
| field | `bl.a_String` | `name` | HIGH | role.bin cột 1 (o.c_AString); bn/as hiển thị tên |
| field | `bl.b_String` | `facePath` | HIGH | role.bin cột 3 "/face/1.av" (o.d_AString); ab nạp "<face>s.av" |
| field | `bl.c_String` | `battleSpritePath` | HIGH | bn: o.d_AAString[id-1][2] = team.bin cột 2 "/ft/chr1.av", nạp làm sprite chiến đấu |
| field | `bl.d_B` | `direction` | HIGH | 0 xuống(y+) 1 lên(y-) 2 trái(x-) 3 phải(x+); a_V(int) gán, b_V() di chuyển theo |
| field | `bl.h_B` | `savedDirection` | MED | cd:2356 lưu d_B trước khi NPC quay mặt nói chuyện; ae:268 a_V(h_B) khôi phục |
| field | `bl.a_AI` | `stats` | HIGH | int[21]: [0]lv [1]exp [2]expNext [3]hp [4]baseMaxHp [5]mp [6]baseMaxMp [7]baseAtk [8]baseDef [9]baseSpd [10..14]tổng maxHp/maxMp/atk/def/spd [16]điểm tự do [17..20]điểm thuộc tính |
| field | `bl.f_B` | `jobLevel` | HIGH | ci 职业等级 so sánh o.a_Abl[0].f_B; 改变职业等级 f_B++ |
| field | `bl.a_AB` | `elementValues` | MED | team.bin cột 15 "0,0,10,0" (4 hệ); o.a_bl(cv) = cv.a_AB; bn chép vào actor |
| field | `bl.a_AAB` | `attrGrowth` | HIGH | [4][5] team.bin cột 16..19; ab: cộng 1 điểm thuộc tính k -> stats HP/MP/ATK/DEF/SPD += a_AAB[k][i] |
| field | `bl.g_B` | `actorType` | MED | 1 = hero id 1/2 (o.a(bl)), 2 = NPC/khác; cd: chỉ g_B==2 đi lang thang, g_B<=2 quay mặt khi nói |
| field | `bl.a_S` | `mapWidth` | LOW | = cd.a_S (mapWidth) khi tạo/rời đội; chỉ ghi, không đọc |
| field | `bl.b_S` | `mapHeight` | LOW | = cd.b_S (mapHeight); chỉ ghi, không đọc |
| field | `bl.a_AAS` | `followTrail` | HIGH | [n][x,y,dir] d_V()/e_V() dựng; o.c() đặt đồng đội theo vết a_AAS[(k+1)*o.d_B-1] |
| field | `bl.a_Z` | `canWander` | HIGH | s<N>r.bin khoá "walk"==1; o.c(bl) đặt false cho thành viên đội; g_V() chỉ bật khi a_Z |
| field | `bl.b_Z` | `isFlying` | HIGH | i_V() bật, j_V() tắt "Kết thúc bay"; ae "Đang bay không thể nói chuyện" |
| field | `bl.c_Z` | `isRiding` | MED | ae.b(int): kiểu 1 thú cưỡi (body o.g_String roleRunBodys, tốc độ 5) / 2 thuyền (tốc độ 2) bật c_Z; b_V() không gặp quái khi c_Z |
| field | `bl.d_Z` | `isCombatant` | MED | true khi có trong team.bin hoặc là pet (o.a_bl(cv)); o.a_Abl() chỉ lấy d_Z vào trận |
| field | `bl.a_Ad` | `skills` | MED | d[] dựng từ a_AS (o.a_Ad(bl,int)); o.a_Z(bl,id) tìm kỹ năng |
| field | `bl.a_AS` | `skillIds` | HIGH | team.bin cột 13 / cv.a_AS; o.a(bl,int,int) thêm id + d mới |
| field | `bl.b_AS` | `equipIds` | HIGH | team.bin cột 14 "0,0,0,0,0,0"; o.a(short[],int) -> ck[] |
| field | `bl.a_Ack` | `equips` | HIGH | ck[6] trang bị; b_I()/c_I()... cộng chỉ số ck |
| field | `bl.a_I` | `encounterSteps` | MED | seed; b_V(): random 100..199 bước rồi cd.a(false) gặp quái |
| field | `bl.e_Z` | `isWandering` | MED | g_V() bật, f_V() tắt; cd gọi h_V() khi g_B==2 && e_Z && !h_Z |
| field | `bl.f_Z` | `noClip` | HIGH | b_V(): khi f_Z chỉ kiểm biên bản đồ, bỏ va chạm; i_V() bật khi bay; ae phím 35 (debug) đảo |
| field | `bl.c_AS` | `footBox` | MED | ctor Image: {-w/2,-8,w,8}; chỉ b(Graphics) vẽ debug |
| field | `bl.d_AS` | `bounds` | HIGH | ctor: khung ảnh / bx.a_AS(); a_AS() trả về; f_S/g_S = d_AS[2]/[3] |
| field | `bl.c_S` | `petUid` | HIGH | o.a_bl(cv): = cv.b_I; o.b(bl[]) so với "GameData.myPet[j].onlyId" |
| field | `bl.g_Z` | `isAnimating` | MED | a(Graphics): g_Z && tick%2==0 -> a_bx.a(true) sang frame; ci 控制抖动 đảo g_Z |
| field | `bl.j_S` | `targetX` | HIGH | a(int,int,int) moveTo; a_V() so d_S với j_S |
| field | `bl.k_S` | `targetY` | HIGH | a(int,int,int) moveTo; a_V() so e_S với k_S |
| field | `bl.j_B` | `targetDirection` | HIGH | moveTo var3; tới đích quay về j_B nếu 0..3 ("quay hướng") |
| field | `bl.h_Z` | `isMovingToTarget` | HIGH | moveTo bật; a_V() tắt khi tới; cd mỗi tick gọi a_V() nếu h_Z |
| field | `bl.i_Z` | `isPlayingAction` | HIGH | ci 播放动作 bật; cd gọi k() khi i_Z; k() tắt khi xong |
| field | `bl.b_I` | `actionRepeat` | MED | seed; 播放动作 tham số 3 (mặc định 1), k() giảm dần |
| field | `bl.c_I` | `actionId` | HIGH | 播放动作 tham số 2 = chỉ số anim; k() so frame với a_bx.a_I(c_I) |
| field | `bl.d_I` | `returnAction` | HIGH | 播放动作 tham số 4; k() xong -> a_bx.a_V(d_I) |
| field | `bl.e_AS` | `stepBoxes` | MED | b_V(): hộp va chạm a_AS(1) dịch trước một bước để thử va chạm |
| field | `bl.a_J` | `wanderLastTime` | HIGH | h_V(): System.currentTimeMillis() lần đổi hướng trước |
| field | `bl.b_J` | `wanderInterval` | HIGH | h_V(): 2000 rồi k.b(1000,4000) ms |
| field | `bl.l_Z` | `isWanderStepping` | HIGH | h_V(): true -> b_V() mỗi tick; đổi ngẫu nhiên |
| hàm | `bl.a_V(int)` | `setDirection` | HIGH | d_B = var1; animation a_bx.a_V(var1) |
| hàm | `bl.a_V()` | `updateMoveTo` | HIGH | đi tới (j_S,k_S); xong: h_Z=false, quay j_B, ci.a(5) tiếp script |
| hàm | `bl.b_V()` | `stepForward` | HIGH | dịch c_B theo d_B nếu không va chạm; đếm encounterSteps cho hero |
| hàm | `bl.c_V()` | `setIdleAnim` | MED | nếu bx có 8 anim: anim 0..3 (đi) -> 4..7 (đứng) |
| hàm | `bl.a(int,int,int)` | `moveTo` | MED | seed; target x,y,dir; h_Z=true |
| hàm | `bl.d_V()` | `initFollowTrail` | HIGH | a_AAS = (số người-1)*o.d_B+1 điểm lùi sau lưng theo d_B |
| hàm | `bl.e_V()` | `resetFollowTrail` | MED | giữ điểm đầu a_AAS[0], dựng lại vết từ vị trí hiện tại (o.f_V(int) rời đội) |
| hàm | `bl.a_I()` | `getSpeed` | HIGH | a_AI[9] + ck.a_I() (ck "SPD+"); o.d(bl) -> a_AI[14] |
| hàm | `bl.b_I()` | `getAttack` | MED | seed; a_AI[7] + ck.b_I() ("ATK+"); o.d(bl) -> a_AI[12] |
| hàm | `bl.c_I()` | `getDefense` | HIGH | a_AI[8] + ck.c_I() ("DEF+"); -> a_AI[13] |
| hàm | `bl.d_I()` | `getMaxHp` | HIGH | a_AI[4] + ck.d() ("HP+"); -> a_AI[10] |
| hàm | `bl.j_I()` | `getMaxMp` | HIGH | a_AI[6] + ck.e() ("MP +"); -> a_AI[11] |
| hàm | `bl.e_I()` | `getFreePoints` | HIGH | a_AI[16]; lên cấp "Nhận 10 điểm" +10 |
| hàm | `bl.f_I()` | `getAttrPoints1` | MED | a_AI[17]; ab.java:143 a_AI[k+17] += điểm cộng vào thuộc tính k |
| hàm | `bl.g_I()` | `getAttrPoints2` | MED | a_AI[18] |
| hàm | `bl.h_I()` | `getAttrPoints3` | MED | a_AI[19] |
| hàm | `bl.i_I()` | `getAttrPoints4` | MED | a_AI[20] |
| hàm | `bl.a(short[])` | `isBlocked` | HIGH | va chạm tile (ca.a(...,1)), ra ngoài bản đồ, hoặc va actor khác (bj.a) |
| hàm | `bl.f_V()` | `stopWander` | HIGH | e_Z = false |
| hàm | `bl.g_V()` | `resumeWander` | HIGH | e_Z = true nếu a_Z |
| hàm | `bl.h_V()` | `updateWander` | HIGH | hẹn giờ ngẫu nhiên đổi hướng / đứng, gọi b_V() |
| hàm | `bl.i_V()` | `startFlying` | HIGH | nạp o.h_String (/av/long1/2.av, roleFlyBody), b_Z=f_Z=true |
| hàm | `bl.j_V()` | `land` | HIGH | "Không thể hạ cánh tại đây" nếu va chạm; "Kết thúc bay" |
| hàm | `bl.k()` | `updateScriptAction` | HIGH | phát anim c_I b_I lần rồi về d_I, ci.a(2) |
| hàm | `bl.a_bl(bl)` | `findTalkableNear` | HIGH | quét bj actor kind 1 (k.a(bl) loại trừ) giao hộp va chạm nới 5px; cd.b_bl -> nói chuyện |
| hàm | `bl.a_ax(bl)` | `findChestAhead` | HIGH | hộp phía trước theo d_B giao vật kind 12 (ax); cd.a_ax |
| hàm | `bl.a_bl(int)` | `findRoleOnMap` | HIGH | actor kind 1 có h_S == roleId; cd.a_bl(int) |

### `cv` → **PetInfo** (HIGH) — o.a_Acv "GameData.myPet[j]"; o.b(cv) đọc /bin/petdata.bin

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cv.a_I` | `petId` | HIGH | petdata hàng a_I-1; "Sủng vật "+a_I; k.a(a_I,true) nạp sprite |
| field | `cv.a_String` | `name` | HIGH | petdata cột 0; "Tìm thấy sủng vật "+a_String |
| field | `cv.a_AI` | `stats` | HIGH | [0]lv [1]hp [2]maxHp [3]tăng hp [4]mp [5]maxMp [6]tăng mp [7..12] atk/def/spd + tăng [13]exp [14]expNext [15]=60 |
| field | `cv.a_Z` | `isDeployed` | HIGH | ab "Xuất chiến" -> true, "Nghỉ" -> false; o.a_Abl() chỉ lấy a_Z |
| field | `cv.a_AB` | `elementValues` | HIGH | petdata cột 2 "3007": 4 chữ số (a=10) theo thứ tự Địa/Thủy/Hỏa/Phong; as/bn vẽ 4 hệ |
| field | `cv.a_AS` | `skillIds` | HIGH | petdata cột 13 "5,1/20,2" lấy id đầu mỗi cặp; o.a(cv,int,int) thêm |
| field | `cv.a_Ad` | `skills` | MED | d[]: new d(id,1,4) (o.a_Ad(cv,int)) |
| field | `cv.b_I` | `uid` | HIGH | "GameData.myPet[j].onlyId"; o.a_V(int,int) cấp k.a(b_AI)+1 |
| field | `cv.b_String` | `dropSpec` | LOW | petdata cột 19 "36,5/51,3" (id vật phẩm,số lượng); chỉ chép/lưu, không đọc |
| field | `cv.b_AB` | `refineItemIds` | HIGH | petdata cột 18; ab "Tinh luyện": o.b(b_AB[0],1) rồi xoá pet |
| field | `cv.b_Z` | `isFainted` | MED | bn: hp<=0 sau trận -> true; o.a_Acv() loại b_Z; cd hồi sinh -> false |
| field | `cv.a_B` | `ownerHeroId` | HIGH | = o.e_S ("heroId" sn.bin); mọi lọc so a_B == o.e_S |
| field | `cv.a_AAB` | `implantSlots` | HIGH | [2][2] "Cấy": [k][0]=đã cấy, [k][1]=id vật phẩm; "Số lần cấy sủng vật đã đạt giới hạn" |

### `au` → **PetSaveRecord** (HIGH) — o.a_Aau: o.a(cv[]) chép từ cv, o.b(cv) "lấy từ dữ liệu bộ nhớ"; ghi RMS

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `au.a_I` | `petId` | HIGH | = cv.a_I |
| field | `au.a_String` | `name` | HIGH | = cv.a_String |
| field | `au.a_AI` | `stats` | HIGH | = cv.a_AI |
| field | `au.a_Z` | `isDeployed` | HIGH | = cv.a_Z |
| field | `au.a_AB` | `elementValues` | HIGH | = cv.a_AB |
| field | `au.a_AS` | `skillIds` | HIGH | = cv.a_AS |
| field | `au.a_Ad` | `skills` | MED | = cv.a_Ad |
| field | `au.b_I` | `uid` | HIGH | khoá tìm kiếm: a_Aau[i].b_I == cv.b_I |
| field | `au.b_String` | `dropSpec` | LOW | = cv.b_String |
| field | `au.a_B` | `ownerHeroId` | HIGH | = cv.a_B |
| field | `au.a_AAB` | `implantSlots` | HIGH | = cv.a_AAB |

### `ao` → **RoleSaveRecord** (HIGH) — o.a_Aao: o.a(bl[]) chép từ hero, o.a(bl) "Nhân vật lấy từ dữ liệu bộ nhớ"; ghi RMS

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ao.a_B` | `roleId` | HIGH | = bl.h_S; o.a_ao(int) tìm theo a_B |
| field | `ao.a_AI` | `stats` | HIGH | = bl.a_AI |
| field | `ao.a_AAB` | `attrGrowth` | HIGH | = bl.a_AAB |
| field | `ao.a_AS` | `skillIds` | HIGH | = bl.a_AS |
| field | `ao.a_Ad` | `skills` | MED | = bl.a_Ad |
| field | `ao.b_AS` | `equipIds` | HIGH | = bl.b_AS |
| field | `ao.a_Ack` | `equips` | HIGH | = bl.a_Ack |
| field | `ao.a_S` | `x` | HIGH | = bl.d_S, writeShort; cd:371 khôi phục vị trí |
| field | `ao.b_S` | `y` | HIGH | = bl.e_S |
| field | `ao.b_B` | `direction` | HIGH | = bl.d_B |
| field | `ao.a_AB` | `elementValues` | HIGH | = bl.a_AB |
| field | `ao.c_B` | `jobLevel` | HIGH | = bl.f_B |

### `bz` → **ScriptEvent** (HIGH) — ci.a(String,int) dựng từ /bin/s<N>e.bin event<k>: id/name/area/auto/loop/script; "执行事件:"+name

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bz.a_S` | `id` | HIGH | khoá "id"; o.b_Z(id) đã xảy ra thì bỏ; xong (không loop) o.b_V(id) |
| field | `bz.a_String` | `name` | HIGH | khoá "name"; "换场景k", "Dịch chuyển phó bản" |
| field | `bz.a_AS` | `area` | HIGH | khoá "area" x,y,w,h; ci giao với hộp va chạm hero |
| field | `bz.a_Z` | `isAuto` | HIGH | khoá "auto"; chỉ kích hoạt khi vào vùng nếu a_Z |
| field | `bz.b_Z` | `isLoop` | HIGH | khoá "loop"; !b_Z && id>0 -> đánh dấu đã xảy ra |
| field | `bz.a_AAString` | `scriptLines` | HIGH | "script:".."scriptEnd"; ci.a_V() chạy từng dòng |
| field | `bz.a_bl` | `speaker` | HIGH | ae: NPC nói chuyện (s<N>c.bin role<id>); cd quay mặt NPC, cf.a_bl |

### `bp` → **Quest** (HIGH) — o.a_bp(int,int) đọc /bin/task.bin; o.a_Abp

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bp.a_S` | `id` | HIGH | task.bin cột 0 |
| field | `bp.a_String` | `name` | HIGH | cột 1; "“ name \" hoàn thành!" |
| field | `bp.a_Z` | `isCompleted` | HIGH | cd đủ điều kiện -> true + m_AS=1 "hoàn thành" |
| field | `bp.a_B` | `type` | HIGH | cột 3: 1 chính tuyến, 2 'Thường', 3 'Công hội' (as/bf) |
| field | `bp.b_B` | `repeatable` | MED | cột 4 (=1 cho nhiệm vụ Công hội); ab: 1 -> menu Xem/Bỏ; o: chỉ ghi vào danh sách đã làm khi 0 |
| field | `bp.b_String` | `description` | HIGH | cột 5; as "Mô tả nhiệm vụ" |
| field | `bp.c_String` | `conditions` | HIGH | cột 6 "有装备/道具数量/有宠物/有技能/已发生事件,..." |
| field | `bp.d_String` | `completionCost` | HIGH | cột 7 "扣除道具/扣除装备/移除宠物,..." |
| field | `bp.e_String` | `reward` | HIGH | cột 8; as "Thưởng nhiệm vụ" |
| field | `bp.b_S` | `turnInRoleId` | HIGH | cột 10; cd:1410 vẽ dấu nhiệm vụ trên NPC có h_S == b_S |

### `bw` → **PetListEntry** (HIGH) — dòng của l (PetListPanel): new bw(petId,index,deployed,name,level)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bw.a_I` | `slotIndex` | HIGH | ctor var2; l.a_I() trả chỉ số trong a_Acv |
| field | `bw.a_Z` | `deployed` | HIGH | ctor var3; l vẽ 'Xuất chiến' |
| field | `bw.a_String` | `name` | HIGH | ctor var4; l vẽ tên |
| field | `bw.b_I` | `level` | HIGH | ctor var5 = a_AI[0]; l vẽ '<cấp> cấp' |
| hàm | `bw.a_I()` | `getSlotIndex` | HIGH | return a_I |
| hàm | `bw.b()` | `getLevel` | HIGH | return b_I |
| hàm | `bw.a_Z()` | `isDeployed` | HIGH | return a_Z |

### `bu` → **DexEntry** (HIGH) — dòng của bc (DexListPanel): (petId, tên hoặc '???')

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bu.a_I` | `petId` | HIGH | bc.a_I() getSelectedPetId |
| field | `bu.a_String` | `name` | HIGH | bc.a_String() |

### `bm` → **MaterialEntry** (HIGH) — dòng của db (MaterialListPanel): icon, tên, có/cần

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bm.a_Image` | `icon` | HIGH | db vẽ drawImage |
| field | `bm.a_String` | `name` | HIGH | tên nguyên liệu |
| field | `bm.a_I` | `ownedCount` | HIGH | db.a(...) owned = o.b_I(id); vẽ trước '/' |
| field | `bm.b_I` | `requiredCount` | HIGH | vẽ sau '/' |

### `bd` → **MainQuestEntry** (HIGH) — dòng của cy (MainQuestListPanel): new bd(id bỏ qua, status, name)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bd.a_String` | `name` | HIGH | cy.a_String() |
| field | `bd.a_I` | `status` | HIGH | 1 = đã nộp (xám) — cy.a_I() |
| hàm | `bd.a_String()` | `getName` | HIGH | return a_String |
| hàm | `bd.a_I()` | `getStatus` | HIGH | return a_I |

### `cn` → **SideQuestEntry** (HIGH) — dòng của bf (SideQuestListPanel): (index, type, name, done)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cn.a_I` | `questIndex` | HIGH | bf.a_I() = chỉ số o.a_Abp |
| field | `cn.b_I` | `type` | HIGH | bf.a(Graphics,type,...) '[Công hội]'/'[Thường]' |
| field | `cn.a_String` | `name` | HIGH | bf vẽ tên |
| field | `cn.a_Z` | `isDone` | HIGH | bf vẽ dấu D15 khung 0 khi xong |

### `dd` → **MenuTab** (HIGH) — de.a(int,int) addTab: new dd(tabId, iconFrame)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `dd.a_I` | `tabId` | HIGH | khung tiêu đề D12 / de.a_AString[a_I] |
| field | `dd.b_I` | `iconFrame` | HIGH | khung trong /pic/xttb.gf (8 biểu tượng) |

### `bi` → **RainDrop** (HIGH) — cd.a_Abi = 100 bi (开始下雨); vẽ drawLine trắng

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bi.a_S` | `x` | HIGH | random 0..screenW+150; reset khi < -10 |
| field | `bi.b_S` | `y` | HIGH | -random % screenH; reset khi > screenH |
| field | `bi.a_B` | `dx` | HIGH | -5 mỗi khung |
| field | `bi.b_B` | `dy` | HIGH | 10 mỗi khung |
| field | `bi.c_B` | `lineDx` | HIGH | -3: drawLine(x,y,x+c_B,y+d_B) |
| field | `bi.d_B` | `lineDy` | HIGH | 6 |
| field | `bi.a_Random` | `random` | HIGH | static Random dùng cho y ban đầu |

### `cm` → **TouchButton** (HIGH) — i.a(cm) thêm vùng chạm; i.a(int,int) hit-test; vẽ D23 (phím mềm) / 9.gf

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cm.a_B` | `buttonId` | HIGH | 1 phím mềm trái, 2 phím mềm phải, 5 nút giữa (cd); bs/da xử lý theo a_B |
| field | `cm.a_AS` | `rect` | HIGH | {x,y,26,26}; cg.a(rect..., px,py,0,0) |

### `cr` → **CloseUpAnim** (HIGH) — cd.a(String,int,int,int) 特写动画: cd.b_B = 6 phát .av rồi về chế độ 1

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cr.a_bx` | `anim` | HIGH | new bx().a(a_String); cd vẽ và tăng frame |
| field | `cr.a_Z` | `isPlaying` | HIGH | true khi bắt đầu; false khi xong -> ci.a(5) |
| field | `cr.a_S` | `x` | HIGH | toạ độ màn hình (đã trừ camera) |
| field | `cr.b_S` | `y` | HIGH | như trên |
| field | `cr.a_String` | `animPath` | HIGH | đường dẫn .av |
| field | `cr.a_I` | `waitForKey` | HIGH | 1: nền đen, "Nhấn phím bất kỳ để trở về" chờ phím; khác: tự kết thúc |

## Cụm `data_o`


### `o` → **GameData** (HIGH) — debug string "GameData.myPet[j].onlyId" (o.java:1331)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `o.a_S` | `startSceneId` | HIGH | sn.bin khoá "default"; cd.a_V(): a_V(o.a_S) khi bắt đầu game |
| field | `o.b_S` | `currentSceneId` | HIGH | cd.a_V(int) gán; "/bin/s"+o.b_S+"e.txt", sceneChange "S"+b_S+"A:"; lưu RMS |
| field | `o.k_B` | `sceneKind` | HIGH | sn cột 3: 1 cảnh thường, khác (2) = phó bản/mê cung (cột 2 = id fuben) |
| field | `o.a_String` | `sceneMapName` | HIGH | sn cột 2 hoặc map ngẫu nhiên từ mazefile "maze:"; cd: "/map/"+a_String+".map" |
| field | `o.f_String` | `buildingFileId` | HIGH | = id cảnh hoặc mazefile maze cột 2; cd: "/building/s"+f_String+"sp.dat" |
| field | `o.b_String` | `sceneName` | HIGH | sn cột 1 (+ số tầng phó bản); cd vẽ góc trên trái; ô lưu i_AString |
| field | `o.c_String` | `sceneMusic` | HIGH | sn cột 4; cd.m(): "/music/"+c_String+".mid" |
| field | `o.a_AS` | `sceneEnemyIds` | HIGH | sn cột 5 "19,33,35"; bn.java:874 chọn ngẫu nhiên địch |
| field | `o.a_AB` | `sceneEnemyCountRange` | HIGH | sn cột 6 "1,3"; bn: a_B=enemyCount random [0..1]; bl: null = không gặp quái |
| field | `o.d_String` | `sceneEnemyLevelRange` | HIGH | sn cột 7 "25,30"; bn.java:931 cấp địch ngẫu nhiên |
| field | `o.b_AS` | `townReturnPoint` | HIGH | sn cột 8 "cảnh,điểm"; "Cuộn hồi thành": cd.c_S=b_AS[1]; a_V(b_AS[0]) |
| field | `o.a_AString` | `recolorImageNames` | MED | sn cột 9; cd.p(): a_ca.a(name+".gf", màu gốc, màu mới) (đổi palette) |
| field | `o.b_AString` | `recolorSpecs` | MED | sn cột 10 "bảng,hàng"; tra "tablename" sn.bin + colorTable |
| field | `o.c_B` | `rideFlags` | HIGH | sn cột 11 (=3): 0/2 chặn thú cưỡi, 0/1 chặn biến thân "Hiện không thể dùng ..." (bit0 cưỡi, bit1 biến thân) |
| field | `o.b_B` | `battleBackground` | HIGH | sn cột 12; "File Sn thiếu loại cảnh chiến đấu"; bn: "/ftmap/"+b_B+".jpg" |
| field | `o.e_String` | `colorTableText` | HIGH | cg.a_String("/bin/colorTable.bin"); cd.o()/p(), k.java:247 "enemy"+n+":" |
| field | `o.q_String` | `sceneChangeText` | HIGH | cache "/bin/sceneChange.bin" (cd/ci: "S<n>P:", "S<n>A:", "Connect:") |
| field | `o.d_Z` | `isInDungeon` | HIGH | true sau khi nạp cảnh sceneKind!=1; cd/ci: điểm vào mê cung, 换场景 đổi tầng |
| field | `o.s_String` | `dungeonName` | HIGH | fuben cột 1; cd: s_String+" [Phó bản]" |
| field | `o.h_B` | `dungeonGateMode` | MED | fuben cột 2 (1/2): 1 chỉ 1 cổng tiến (k_AString[1]), 2 hai cổng lùi/tiến (ci.java:157) |
| field | `o.i_B` | `dungeonFloorCount` | HIGH | fuben cột 4 (5,6,8); ci: j_B >= i_B -> ra cảnh m_AString[1] |
| field | `o.j_B` | `dungeonFloor` | HIGH | tầng hiện tại: ci 换场景 ++ / 换场景2 --; vào từ điểm 2 = i_B; nối vào sceneName |
| field | `o.m_AString` | `dungeonLinkScenes` | HIGH | fuben cột 5 "41,67": [0] cảnh lối ra ngoài (thua/nạp save), [1] cảnh cuối (67 boss) |
| field | `o.c_AAS` | `dungeonBosses` | HIGH | fuben cột 6 "id,cấp/..."; ci: 副本战斗 khi b_S==67, cấp -1 = cấp nhân vật+5 |
| field | `o.q_AS` | `dungeonBossPos` | HIGH | fuben cột 7 "133,133"; ci vùng 副本战 (x-80,y-24,160,48), cd đặt sprite boss |
| field | `o.r_AS` | `dungeonExitPos` | MED | fuben cột 8 "280,126"; ae.java:372 k.a(r_AS[0],r_AS[1],m_AString[0]) sau khi nhận thưởng |
| field | `o.n_AString` | `dungeonRewards` | HIGH | fuben cột 9 "loại,id,số,tỉ lệ/..."; loại 5 = EXP (ae/cd/bn) |
| field | `o.h_AAString` | `mazeTable` | HIGH | mazefile.bin "maze:".."mazeEnd" (danh sách map, building id, chỉ số mazepos) |
| field | `o.j_AString` | `mazeEntryPoints` | HIGH | 2 điểm "x,y,hướng" chọn ngẫu nhiên từ "mazepos:"; cd đặt nhân vật |
| field | `o.k_AString` | `mazeGatePoints` | HIGH | 2 điểm "x,y" cổng; ci tạo vùng "换场景1/2", cd vẽ sdt1/sdt2.gf |
| field | `o.f_AAString` | `sceneTable` | HIGH | sn.bin "data:".."dataEnd" |
| field | `o.g_AAString` | `roleTable` | HIGH | role.bin "role:".."roleEnd" (id, tên, anim, chân dung) |
| field | `o.d_AAString` | `partyRoleTable` | HIGH | team.bin "role:".."roleEnd" (chỉ số gốc nhân vật đội); bn cột 2 anim chiến đấu |
| field | `o.a_AAString` | `itemTable` | HIGH | item.bin "item".."itemend"; ae lấy cột 1 tên |
| field | `o.b_AAString` | `skillTable` | HIGH | skill.bin "skill:".."skillend" (bn, d nạp) |
| field | `o.c_AAString` | `equipTable` | HIGH | equip.bin "equip".."equipend" (ck, ae, d(int,int)) |
| field | `o.e_AAString` | `questTable` | HIGH | task.bin "task".."taskend" (a_bp, cd vẽ nhiệm vụ) |
| field | `o.c_AString` | `roleNames` | HIGH | role.bin cột 1; cf tên người nói; [0] tên nhân vật chính lưu RMS (đổi tên) |
| field | `o.d_AString` | `rolePortraits` | HIGH | role.bin cột 3 "/face/1.av" ("-1" -> ""); cf chân dung |
| field | `o.e_AString` | `roleAnimPaths` | HIGH | role.bin cột 2 "/av/chr1.av"; a_bl(int), cd tạo sprite |
| field | `o.g_String` | `mountAnimPath` | HIGH | role.bin "roleRunBodys" (/av/long1.av); ae: thú cưỡi |
| field | `o.h_String` | `flyAnimPath` | HIGH | role.bin "roleFlyBody"; bl/ci đổi long1/long2 khi bay |
| field | `o.i_String` | `boatAnimPath` | HIGH | = "/av/boat.av"; ae.java:481 lên thuyền |
| field | `o.j_String` | `battleMusic` | HIGH | sn.bin "battleMusic:" khoá "inbattle"; bn.a_V(0) |
| field | `o.k_String` | `victoryMusic` | HIGH | khoá "win" |
| field | `o.l_String` | `defeatMusic` | HIGH | khoá "lose" |
| field | `o.l_AString` | `loadingTips` | HIGH | about.bin "tips:".."tipsEnd"; cd/bn/bs vẽ khi tải |
| field | `o.f_B` | `tipIndex` | HIGH | random k.a(0, l_AString.length); l_AString[f_B] |
| field | `o.e_AS` | `itemIds` | HIGH | item cột 0; d_I(id) tra chỉ số |
| field | `o.f_AString` | `itemNames` | HIGH | item cột 1 "Thuốc hồi HP"...; so chuỗi trong useItem* |
| field | `o.o_AString` | `itemIconNames` | HIGH | item cột 2; e(): "/icon/"+name+".gf" |
| field | `o.f_AS` | `itemTypes` | HIGH | item cột 3: 1 dùng,3 nhiệm vụ,4 đặc biệt,5 đá khảm,6 gen; as.j() chia túi |
| field | `o.g_AS` | `itemBattleUsable` | HIGH | item cột 5; bn.j(): chỉ vật phẩm ==1 dùng trong trận |
| field | `o.u_AS` | `itemConsumable` | MED | item cột 7; ==1 -> c(id,1) trừ sau khi dùng |
| field | `o.g_AString` | `itemDescriptions` | HIGH | item cột 8 "Hồi 1000 HP"; as/bn khung mô tả |
| field | `o.h_AS` | `itemBuyPrices` | HIGH | item cột 9; ab: o.a_I -= h_AS*số lượng "Mua " |
| field | `o.i_AS` | `itemSellPrices` | HIGH | item cột 10; ab.java:475 o.a_I += i_AS*số lượng |
| field | `o.h_AString` | `itemStatBonus` | HIGH | item cột 11 "chỉ số,giá trị" (đá khảm "3,40", gen "8,4"); a(cv,String) |
| field | `o.a_AImage` | `itemIcons` | HIGH | e(): cc.a_Image("/icon/"+o_AString[i]+".gf") |
| field | `o.e_S` | `heroId` | HIGH | sn.bin "heroId"; ci 主角ID/改变主角; cv.a_B == e_S = pet của nhân vật chính |
| field | `o.a_Abl` | `party` | HIGH | đội trên bản đồ, [0] nhân vật chính; c(bl) thêm, f_V(int) bớt |
| field | `o.b_AB` | `partyRoleIds` | HIGH | id vai trong đội (c(bl) thêm h_S); lưu RMS; a_V(String) dựng lại đội |
| field | `o.b_Abl` | `sceneNpcs` | MED | cd.a_Abl() reset rồi b(bl) thêm vai của cảnh; cd vẽ dấu nhiệm vụ trên đầu (so h_S với task cột 9) |
| field | `o.a_Aao` | `roleRecords` | HIGH | ao[]: bản lưu chỉ số/kỹ năng/trang bị/vị trí vai; ghi RMS ("roleDatas[i].equip==null") |
| field | `o.a_Aau` | `petRecords` | HIGH | au[]: bản lưu pet; "mypetdatas[i].id ... petName" |
| field | `o.a_Acv` | `myPets` | HIGH | "GameData.myPet[j].onlyId" |
| field | `o.v_AS` | `myPetSpeciesIds` | HIGH | song song b_AI; a_V(String): a_V(v_AS[i], b_AI[i]) dựng lại pet |
| field | `o.b_AI` | `myPetUids` | HIGH | onlyId (cv.b_I): uid mới = max+1; song song v_AS |
| field | `o.p_AS` | `discoveredPetIds` | HIGH | đồ giám: thêm khi có pet (a_V(int,int)) / gặp trong trận (bn:4072); as 'Mới nhất' |
| field | `o.a_AZ` | `newPetFlags` | MED | boolean[140]; a_V(int,int) đặt [id-1]=true, ab/as xoá khi xem trong đồ giám |
| field | `o.c_AS` | `removedRoleIds` | HIGH | cd.d(int) thêm (清除BOSS/敌人), cd.b_V(int) bỏ (复活角色); cd không tạo lại vai này |
| field | `o.l_B` | `followDistance` | MED | =15; a_V(byte): khoảng cách bám theo (đơn vị bước) |
| field | `o.d_B` | `followStepGap` | MED | ceil(l_B/tốc độ); c(): đồng đội i đứng ở vết a_AAS[(i)*d_B-1]; bl: kích thước vết |
| field | `o.a_AAS` | `bagItems` | HIGH | {itemId, số lượng}; "Túi đồ đã đầy", tối đa 99 "Số vật phẩm đã tối đa"; team.bin "item:" |
| field | `o.b_AAS` | `equipBagIds` | HIGH | [6 loại ô][] id trang bị trong túi; team.bin "bag:" (Vũ khí/Mũ/Áo/Giày/Dây chuyền/Nhẫn) |
| field | `o.a_AAck` | `equipBag` | HIGH | [6][] ck song song b_AAS (as.equipBagIndex) |
| field | `o.a_I` | `money` | HIGH | team.bin "money"; "Nhận vàng+"; âm -> 99999 khi lưu |
| field | `o.c_I` | `score` | HIGH | +5 mỗi nhiệm vụ, bn +2 khi thắng; bk.uploadScore "Điểm hiện tại" |
| field | `o.b_I` | `statMultiplier` | HIGH | df "Nhận thưởng" (trang 8) đặt =2; bn nhân ATK/DEF/SPD actor phe ta; lưu RMS |
| field | `o.g_B` | `musicVolume` | HIGH | da: = at.defaultVolume; ab +/-10 (0..100), playMusic(..., g_B); lưu bản ghi SMS |
| field | `o.a_B` | `storyPhase` | HIGH | ci 增加阶段/更新阶段; cd vẽ "phase:"; bj.phase; ci.a(file, a_B) |
| field | `o.a_AI` | `eventFlags` | HIGH | id sự kiện đã xảy ra; b_V thêm, a_V(int) bỏ, b_Z(int) kiểm; "ID sự kiện đã xảy ra" |
| field | `o.d_AS` | `openedChestIds` | HIGH | ax/ae: rương đã mở; lưu RMS |
| field | `o.a_Abp` | `activeQuests` | HIGH | bp[] dựng bởi a_bp; as danh sách nhiệm vụ |
| field | `o.n_AS` | `activeQuestIds` | HIGH | id nhiệm vụ đang nhận (song song a_Abp/m_AS); ci 有任务/无任务 |
| field | `o.m_AS` | `questDoneFlags` | HIGH | song song n_AS; cd.java:1044 đặt 1 khi đạt mục tiêu; a_bp -> bp.a_Z |
| field | `o.o_AS` | `finishedQuestIds` | HIGH | thêm trong a(short,boolean) khi bp.b_B==0 (không lặp lại); cd không cho nhận lại |
| field | `o.d_S` | `reviveSceneId` | HIGH | sn.bin "liveBackScene" (//Cảnh hồi sinh); ci 改复活点; bn thua -> a_V(d_S) |
| field | `o.e_B` | `revivePoint` | HIGH | sn.bin "liveBackPos" (//Vị trí hồi sinh); bn: cd.c_S = e_B |
| field | `o.b_Z` | `isEndingPending` | MED | ci 游戏结局1 đặt true; cd vẽ e() và phím tiếp theo -> o.f() + showCover |
| field | `o.a_Z` | `isRestoringSave` | HIGH | c(String) đặt true khi nạp xong; cd dùng vị trí đã lưu (ao.a_S/b_S) rồi xoá |
| field | `o.m_String` | `saveIndexStore` | HIGH | "cwzw3save": 3 ô tóm tắt; a_Z(byte) ghi, a_Z() đọc |
| field | `o.n_String` | `saveSlot1Store` | HIGH | "cwzw3_save1" ("Tiến độ 1") |
| field | `o.o_String` | `saveSlot2Store` | HIGH | "cwzw3_save2" ("Tiến độ 2") |
| field | `o.p_String` | `saveSlot3Store` | HIGH | "cwzw3_save3" ("Tiến độ 3") |
| field | `o.r_String` | `purchaseStore` | HIGH | "cwzw3SmsSave": cờ dịch vụ df + âm lượng; Main a_Z, df b(String) |
| field | `o.c_Z` | `hasSaveIndex` | HIGH | ab/bs: đặt true khi thấy RecordStore m_String rồi a_Z(); as/bs mới vẽ ô lưu |
| field | `o.c_AB` | `slotUsed` | HIGH | ô 0..2: 1 = có dữ liệu (ghi 1 khi lưu); ab/as/bs chỉ vẽ/nạp ô ==1 |
| field | `o.i_AString` | `slotSceneNames` | HIGH | = sceneName lúc lưu; bs/as vẽ |
| field | `o.j_AS` | `slotHeroIds` | HIGH | = heroId; bs/ab "/av/chr"+j_AS+".av" |
| field | `o.k_AS` | `slotHeroLevels` | HIGH | = a_Abl[0].a_AI[0] (cấp); bs vẽ số |
| field | `o.l_AS` | `slotYears` | HIGH | Calendar GMT+8 get(1) |
| field | `o.d_AB` | `slotMonths` | HIGH | get(2)+1 |
| field | `o.e_AB` | `slotDays` | HIGH | get(5) |
| field | `o.f_AB` | `slotHours` | HIGH | get(11) |
| field | `o.g_AB` | `slotMinutes` | HIGH | get(12) |
| field | `o.h_AB` | `slotSeconds` | HIGH | get(13); ghi/đọc nhưng không ai vẽ |
| hàm | `o.a_V()` | `initNewGame` | HIGH | cd.a_V(): reset cờ đồ giám, thêm nhân vật heroId, team.bin "item:"/"bag:"/"money" |
| hàm | `o.b()` | `loadStaticTables` | HIGH | Main.java:63 khởi động: sn.bin nhạc trận/default/heroId/hồi sinh, role.bin tên/anim/chân dung, about.bin tips |
| hàm | `o.f()` | `resetGameData` | HIGH | xoá đội/pet/túi/sự kiện/bảng cache; ab trước showCover / trước nạp "Tiến độ n", cd sau kết thúc |
| hàm | `o.a_Z(int)` | `loadSceneInfo` | HIGH | tra sn.bin "data:" theo id; phó bản đọc mazefile; "Dữ liệu cảnh không tồn tại" |
| hàm | `o.d()` | `loadItemTable` | HIGH | item.bin -> itemIds/itemNames/itemTypes/... |
| hàm | `o.e()` | `loadItemIcons` | HIGH | "/icon/"+itemIconNames[i]+".gf" |
| hàm | `o.d_I(int)` | `findItemIndex` | HIGH | vị trí id trong itemIds, -1 nếu không có |
| hàm | `o.c_I(int)` | `findSkillIndex` | HIGH | vị trí id trong d.a_AS (bảng kỹ năng) |
| hàm | `o.e_I(int)` | `findQuestIndex` | HIGH | hàng questTable có cột 0 == id |
| hàm | `o.f_I(int)` | `findEquipIndex` | HIGH | vị trí id trong ck.a_AS (bảng trang bị) |
| hàm | `o.a_ao(int)` | `findRoleRecord` | HIGH | roleRecords có a_B == roleId |
| hàm | `o.a(int,int,boolean)` | `rollBiasedHalf` | MED | random [min,max]: true -> 2/3 nửa trên, false -> 2/3 nửa dưới; tăng trưởng pet theo chỉ số gốc; k.java:565 |
| hàm | `o.a_V(int)` | `removeEventFlag` | HIGH | cg.b remove from a_AI; ci 恢复事件 (ci.java:419), ci.java:135 xoá 6701 |
| hàm | `o.b_V(int)` | `addEventFlag` | HIGH | "Thêm id ... sự kiện"; ci 去除事件 (ci.java:417), kết thúc script |
| hàm | `o.b_Z(int)` | `hasEventFlag` | HIGH | cg.a_Z(eventFlags, id); ci 已发生事件 |
| hàm | `o.a_bl(int)` | `createRoleSprite` | HIGH | reads /bin/role.bin, new bl |
| hàm | `o.a(bl)` | `loadRoleData` | MED | team.bin "role:" hoặc roleRecords: chỉ số, kỹ năng, trang bị; "Nhân vật ... lấy từ dữ liệu bộ nhớ/văn bản" |
| hàm | `o.c(bl)` | `addPartyMember` | HIGH | nối vào party, nạp dữ liệu nếu chưa có, thêm h_S vào partyRoleIds; ci 加入队伍, k đổi nhân vật chính |
| hàm | `o.b(bl)` | `addSceneNpc` | MED | nối vào sceneNpcs (cd.java:531 khi dựng vai của cảnh) |
| hàm | `o.f_V(int)` | `removePartyMember` | HIGH | ci 离开队伍: bỏ vai khỏi party và partyRoleIds, đặt lại vị trí |
| hàm | `o.c(bl[])` | `keepLeaderOnly` | MED | a_Abl = {var0[0]}; bn gọi sau trận (đội trận có thêm pet) |
| hàm | `o.d(bl)` | `refreshRoleStats` | MED | a_AI[10..20] = getter của bl (gồm trang bị); kẹp HP/MP hiện tại |
| hàm | `o.a(bl[])` | `storeRoleRecords` | HIGH | chép chỉ số/kỹ năng/trang bị của vai trong team.bin vào roleRecords |
| hàm | `o.a_V(byte)` | `updateFollowSpacing` | MED | followStepGap = ceil(15/tốc độ); bl gọi với c_B (bước) |
| hàm | `o.c()` | `placeFollowers` | MED | đặt đồng đội 1.. lên vết di chuyển của nhân vật chính (cd.java:448) |
| hàm | `o.a_Abl()` | `buildBattleParty` | MED | thêm sprite pet xuất chiến vào party, trả tối đa 4 vai d_Z; cd -> bn.a_Abl |
| hàm | `o.a_V(int,int)` | `addPet` | HIGH | new cv(speciesId, uid/-1), petdata, ghi đồ giám; ci 载入宠物, "Nhận sủng vật" |
| hàm | `o.b(cv)` | `loadPetData` | MED | petRecords theo uid hoặc petdata.bin/stardata (random chỉ số + tăng trưởng) |
| hàm | `o.a_bl(cv)` | `createPetSprite` | HIGH | bl từ cv: chép cấp/HP/MP/ATK/DEF/SPD/EXP, h_S=loài, c_S=uid |
| hàm | `o.a_Acv()` | `getReservePets` | MED | pet của heroId, chưa xuất chiến (a_Z false), còn HP, !b_Z; bn danh sách gọi pet |
| hàm | `o.a(cv[])` | `storePetRecords` | HIGH | chép pet vào petRecords (theo uid b_I) |
| hàm | `o.b(bl[])` | `syncPetsFromBattle` | HIGH | "Cập nhật dữ liệu chiến đấu": chép chỉ số bl (trừ [0]) về cv cùng uid |
| hàm | `o.a_V(cv)` | `growPetStats` | MED | cộng tăng trưởng mỗi cấp a_AI[3/6/8/10/12] vào HPmax/MPmax/ATK/DEF/SPD; cd.a(cv[]) lên cấp |
| hàm | `o.e(bl)` | `growPetSpriteStats` | MED | như trên cho sprite pet trong trận (tìm cv theo uid c_S); bn lên cấp |
| hàm | `o.a(cv,String)` | `applyPetStatBonus` | HIGH | "chỉ số,giá trị/..." cộng vào cv.a_AI; ab cấy gen (itemStatBonus) |
| hàm | `o.d_V(int)` | `removePetRecord` | MED | xoá petRecords theo uid; ab (as.q_B==3) thả pet / đổi pet lấy vật phẩm, luôn đi cùng e_V |
| hàm | `o.e_V(int)` | `removePet` | HIGH | xoá myPets theo uid + myPetSpeciesIds/myPetUids; ci/cd 移除宠物, ab thả pet |
| hàm | `o.d_Z(int)` | `hasPetSpecies` | MED | pet của heroId có loài == id; ci/cd điều kiện 有宠物 |
| hàm | `o.a_Ad(cv,int)` | `buildPetSkills` | MED | d[] từ cv.a_AS (new d(id,1,4)) |
| hàm | `o.a_Ad(bl,int)` | `buildRoleSkills` | MED | d[] từ bl.a_AS (new d(id,1,h_S)) |
| hàm | `o.a(d[],d)` | `appendSkill` | HIGH | nối phần tử vào mảng d[] |
| hàm | `o.a(cv,int,int)` | `learnPetSkill` | HIGH | ab học kỹ năng cho pet (trả d.d_AS vàng); bỏ qua tham số cấp |
| hàm | `o.a(bl,int,int)` | `learnRoleSkill` | HIGH | ci 获取技能 id,cấp; ab học kỹ năng nhân vật |
| hàm | `o.a_V(bl,int)` | `forgetRoleSkill` | HIGH | ci 扣除技能: bỏ id khỏi a_AS và a_Ad |
| hàm | `o.a_Z(bl,int)` | `hasSkill` | HIGH | a_Ad có a_S == id; ci 有技能/无技能, cd |
| hàm | `o.a_I(int)` | `countBagSlotsInGroup` | MED | số ô túi cùng nhóm loại (1 / 2+5 / 3+4) với itemId; so ct.maxItemTypes |
| hàm | `o.b(int,int)` | `addItem` | HIGH | "Túi đồ đã đầy", "Số vật phẩm đã tối đa" (99) |
| hàm | `o.c(int,int)` | `removeItem` | HIGH | trừ số lượng, xoá ô <=0; ci 扣除道具 |
| hàm | `o.b_I(int)` | `getItemCount` | HIGH | tổng số lượng id trong bagItems; ci 道具数量 |
| hàm | `o.a_AAB()` | `getGeneItems` | HIGH | {id,số} các vật phẩm loại 6 trong túi; as.implantMaterials |
| hàm | `o.a_V(cv,int)` | `useItemOnPet` | HIGH | "Thuốc hồi HP"... "HP +1000", sách cộng chỉ số pet |
| hàm | `o.b(bl,int)` | `useItemOnRole` | HIGH | như trên cho nhân vật + "Vé số sủng vật", "Cuộn hồi thành" |
| hàm | `o.a(e,int)` | `useItemInBattle` | HIGH | bn.java:4276; thuốc/thịt/"Cola vạn năng" lên actor |
| hàm | `o.b(e,int)` | `healActorHp` | HIGH | e.m_I += n, kẹp a_AI[10] |
| hàm | `o.c(e,int)` | `healActorMp` | HIGH | e.n_I += n, kẹp a_AI[11] |
| hàm | `o.c(bl,int)` | `healRoleHp` | HIGH | bl.a_AI[3] += n, kẹp [10] |
| hàm | `o.d(bl,int)` | `healRoleMp` | HIGH | bl.a_AI[5] += n, kẹp [11] |
| hàm | `o.b(cv,int)` | `healPetHp` | HIGH | cv.a_AI[1] += n, kẹp [2] |
| hàm | `o.c(cv,int)` | `healPetMp` | HIGH | cv.a_AI[4] += n, kẹp [5] |
| hàm | `o.d(int,int)` | `addEquip` | HIGH | equip.bin cột 2 = loại ô; "Túi đồ đã đầy"; ci 获取装备 |
| hàm | `o.c_V(int)` | `removeEquip` | HIGH | ci 扣除装备: bỏ id khỏi equipBagIds/equipBag (ô theo ck.b_AS) |
| hàm | `o.c_Z(int)` | `hasEquip` | HIGH | id có trong equipBagIds; ci 有装备/无装备 |
| hàm | `o.a(short[],int)` | `createEquips` | MED | ck[] từ danh sách id (id<=0 -> null); "Trang bị của tinh linh này là null" |
| hàm | `o.a(ck[],int)` | `removeEquipAt` | HIGH | xoá phần tử chỉ số i (mảng 1 phần tử -> null) |
| hàm | `o.a(ck[],ck)` | `appendEquip` | HIGH | nối phần tử vào mảng ck[] |
| hàm | `o.a_bp(int,int)` | `createQuest` | HIGH | bp từ task.bin; tham số 2 = chỉ số trong questDoneFlags (-1 = mới) |
| hàm | `o.b(bp)` | `appendQuest` | HIGH | nối vào activeQuests |
| hàm | `o.a(short)` | `acceptQuest` | HIGH | ci 加入任务, ae: thêm id vào activeQuestIds + questDoneFlags 0 |
| hàm | `o.a(short,boolean)` | `finishQuest` | HIGH | trao thưởng "Nhận ..." (loại 1 vật phẩm,2 trang bị,3 vàng,4 pet,5 EXP), score+=5; ci 移除任务 = nộp nhiệm vụ (s*e.txt), hoàn thành nhiệm vụ ngẫu nhiên |
| hàm | `o.g(int)` | `removeQuestById` | HIGH | tìm activeQuests theo id rồi a(bp) |
| hàm | `o.a(bp)` | `removeQuest` | HIGH | xoá bp khỏi activeQuests (ab huỷ nhiệm vụ) |
| hàm | `o.g()` | `applyDefeatPenalty` | HIGH | ci 失败处理: mất nửa vàng (trừ khi df.hasLuckyPack), hồi đầy HP/MP đội |
| hàm | `o.a_V(String)` | `loadGame` | HIGH | "Tiến độ 1/2/3" -> c(store); dựng lại đội, nhiệm vụ, pet |
| hàm | `o.c(String)` | `readSaveRecord` | HIGH | đọc RecordStore ô lưu: pha, cảnh, sự kiện, túi, vai, pet...; đặt isRestoringSave |
| hàm | `o.b(byte)` | `saveGame` | HIGH | ghi RecordStore cwzw3_save1..3 (+ purchaseStore); ci 游戏存档, ab "Lưu thành công" |
| hàm | `o.a_Z(byte)` | `saveSlotSummary` | HIGH | cập nhật ô tóm tắt (cảnh, hero, cấp, giờ GMT+8) và ghi cwzw3save |
| hàm | `o.a_Z()` | `loadSlotSummaries` | HIGH | đọc cwzw3save vào slotUsed/slotSceneNames/... |
| hàm | `o.a_Z(String)` | `loadPurchaseState` | HIGH | đọc cwzw3SmsSave: df tên/cờ/tiền đã tiêu + musicVolume; Main.java:64 |
| hàm | `o.b(String)` | `savePurchaseState` | HIGH | ghi cwzw3SmsSave; df sau khi mua |

## Cụm `sdk`


### `u` → **PortalClient** (HIGH) — ngữ cảnh SDK: MIDlet/Display/Displayable game, RMS "mboxclient", header Q-UA "SN2010-<platform>/4", tạo ac

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `u.a_RecordStore` | `store` | HIGH | RecordStore.openRecordStore("mboxclient") |
| field | `u.b_String` | `qua` | HIGH | "SN2010-"+microedition.platform+"/4"; gửi header "Q-UA" (ap) |
| field | `u.c_String` | `screenSize` | HIGH | a_I+"X"+b_I; header "screen" |
| field | `u.d_String` | `bigFontSize` | HIGH | cỡ chữ "Quốc" font SIZE_LARGE(16); header "bigchar" |
| field | `u.e_String` | `midFontSize` | HIGH | font size 0 (MEDIUM); header "midchar" |
| field | `u.f_String` | `smallFontSize` | HIGH | font size 8 (SMALL); header "smlchar" |
| field | `u.a_I` | `screenWidth` | HIGH | cw truyền da.b_S; ai.e_I = a_u.a_I |
| field | `u.b_I` | `screenHeight` | HIGH | da.c_S; ai.f_I = a_u.b_I |
| field | `u.a_Z` | `hasPointer` | HIGH | ai ctor: a_u.a_Z = hasPointerEvents() |
| field | `u.a_Hashtable` | `settings` | HIGH | khoá _uid/_sid/_a8/_server/_vertify*; lưu RMS qua ak.a(Hashtable) |
| field | `u.a_Display` | `display` | HIGH | Display của MIDlet; setCurrent(canvas/TextBox) |
| field | `u.a_Displayable` | `gameScreen` | HIGH | Displayable game (da) để quay về khi đóng cổng |
| field | `u.a_MIDlet` | `midlet` | HIGH | platformRequest / notifyDestroyed |
| field | `u.c_I` | `leftSoftKeyCode` | HIGH | ai.keyPressed: ==c_I -> -506 (phím mềm trái); cw truyền b.a_AS[0] |
| field | `u.d_I` | `rightSoftKeyCode` | HIGH | ==d_I -> -507; b.b_AS[0] |
| field | `u.e_I` | `gameId` | HIGH | cg.a_I(cq.e_String) (Main: "gameId"); ghi offset 19 của gói ak |
| field | `u.g_I` | `platformId` | LOW | hằng 888888; ak: ==888888 chọn URL sngame, ghi offset 11 gói tin |
| field | `u.a_ai` | `canvas` | HIGH | new ac(this); hiển thị bằng display.setCurrent |
| field | `u.a_v` | `listener` | HIGH | PortalListener nhận kết quả (bk) |
| field | `u.h_I` | `pendingCommand` | MED | lệnh đang mở (a(int,int)); ac.d() nút OK gửi lại b_V(a_u.h_I) sau đăng nhập |
| field | `u.b_Z` | `isPortalShown` | HIGH | true khi setCurrent(canvas) (a_V()), false ở ai.g(); an dùng để quyết định vẽ trang |
| hàm | `u.a_String()` | `getQua` | HIGH | trả b_String |
| hàm | `u.b()` | `getScreenSize` | HIGH | trả c_String |
| hàm | `u.c()` | `getBigFontSize` | HIGH | trả d_String |
| hàm | `u.d()` | `getMidFontSize` | HIGH | trả e_String |
| hàm | `u.e()` | `getSmallFontSize` | HIGH | trả f_String |
| hàm | `u.a_Z()` | `isLoggedIn` | HIGH | settings "_uid" khác rỗng |
| hàm | `u.a_V()` | `showPortal` | HIGH | setCurrent(canvas), khởi thread repaint, an.a_V() khởi hàng đợi |
| hàm | `u.a(byte[])` | `saveSettings` | HIGH | setRecord(1,...) vào "mboxclient" |
| hàm | `u.a_RecordStore()` | `openStore` | HIGH | mở/khởi tạo "mboxclient" với record rỗng |
| hàm | `u.a_Hashtable()` | `loadSettings` | HIGH | đọc record 1: short n, (byte len,key)(short len,value); mặc định _vCInterval=5... |
| hàm | `u.a_AB()` | `readSettingsRecord` | HIGH | getRecord(1), null nếu rỗng |
| hàm | `u.a(byte[],int)` | `decodeUtf` | HIGH | ghép 2 byte độ dài + readUTF |
| hàm | `u.a(int,DataInputStream)` | `readBytes` | HIGH | đọc n byte |
| hàm | `u.a(int,int)` | `openPortal` | HIGH | kiểm 17 loginInput/18 verify qua a_I(int), hiện canvas, ac.b_V(cmd) |
| hàm | `u.a_I(int)` | `checkPrecondition` | MED | 17: thiếu _uid/_sid -> cần đăng nhập; 18: đếm _vertifyValue theo ngày; -1 = không cần |
| hàm | `u.a_V(int)` | `sendCommand` | HIGH | an.a_V(); an.a(cmd) — gửi lệnh không hiện trang (cw.a(2005) xếp hạng) |
| hàm | `u.a(int,String,int,int,String,String,String)` | `uploadScore` | HIGH | an.a(...) lệnh 2100 "invoke/uploadData.m" |
| hàm | `u.a_ac()` | `getBrowser` | HIGH | (ac) a_ai |
| hàm | `u.a_ai()` | `getCanvas` | HIGH | a_ai |
| hàm | `u.a_cb()` | `getUserProfile` | HIGH | an.a_cb() |

### `cw` → **PortalApi** (HIGH) — mặt tiền mỏng bọc u (PortalClient); bk dùng: mở trang, gửi lệnh, tải điểm, lấy hồ sơ

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cw.a_u` | `client` | HIGH | new u(..., 888888, ...) |
| hàm | `cw.a(int,int)` | `openPage` | HIGH | u.a(1, cmd) — tham số đầu bị bỏ qua |
| hàm | `cw.a(int)` | `sendCommand` | HIGH | u.a_V(cmd) |
| hàm | `cw.a(int,String,int,int,String,String,String)` | `uploadScore` | HIGH | u.a(score,...) -> uploadData.m |
| hàm | `cw.a()` | `getUserProfile` | HIGH | u.a_cb() |

### `v` → **PortalListener** (HIGH) — interface a(int cmd, by result); u.a_v; bk cài đặt

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| hàm | `v.a(int,by)` | `onPortalResult` | HIGH | an.a(Hashtable[]) tag "return": a_v.a(id, by(code,msg)); u.a(...) báo 1/2 khi huỷ |

### `by` → **PortalResult** (HIGH) — (code, subCode bỏ, msg) từ tag "return" (an)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `by.a_I` | `code` | HIGH | an: a(var28,"code",-1) |
| field | `by.a_String` | `message` | HIGH | "msg" |
| hàm | `by.a_I()` | `getCode` | HIGH | bk: ==0 thành công, -1 chưa đăng nhập |
| hàm | `by.a_String()` | `getMessage` | HIGH | bk lưu thông báo |

### `cb` → **QQUserProfile** (HIGH) — struct id=7: nickName, score, level; bk vẽ "Tên QQ","Cấp","Điểm cao nhất"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cb.a_String` | `nickName` | HIGH | "nickName" |
| field | `cb.a_I` | `score` | HIGH | "score" -> "Điểm cao nhất" |
| field | `cb.b_I` | `level` | HIGH | "level" -> "Cấp" |

### `dg` → **LeaderboardEntry** (HIGH) — struct id=9 uid/nickName/score (an); bk.a(String) dùng cho "Bảng xếp hạng"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `dg.a_String` | `uid` | HIGH | "uid"; an khử trùng theo uid |
| field | `dg.b_String` | `nickName` | HIGH | "nickName"; cột "Tên" |
| field | `dg.a_I` | `score` | HIGH | "score"; cột "Điểm" |

### `ai` → **PortalCanvasBase** (HIGH) — abstract Canvas+Runnable+CommandListener của UI cổng: khung, phím mềm, hộp thoại, thanh tiến trình

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ai.a_Z` | `isMenuOpen` | HIGH | ac: vẽ/điều khiển menu "1.Trang chủ..6.Về trò chơi" |
| field | `ai.f_Z` | `isDialogOpen` | HIGH | a(String,int) bật; b(Graphics) vẽ "Gợi ý : " |
| field | `ai.b_Z` | `isLogoutPending` | HIGH | ac đặt khi hỏi "đăng xuất"; a() -> an.a(102) logout.m |
| field | `ai.c_Z` | `isEmbeddedMode` | LOW | true -> thoát thì quay về game (setCurrent gameScreen), false -> notifyDestroyed; ac luôn true |
| field | `ai.b_AString` | `dialogLines` | HIGH | ad.a(text,font,w-20) |
| field | `ai.d_Z` | `isRunning` | HIGH | vòng run() và cổng paint |
| field | `ai.a_u` | `client` | HIGH | PortalClient |
| field | `ai.a_an` | `manager` | HIGH | new an(u) |
| field | `ai.b_Vector` | `pageImages` | HIGH | ảnh decode từ tag img (az Base64); t/ac lấy theo id-1 |
| field | `ai.d_I` | `mode` | HIGH | a_V(int): 0 yêu cầu đăng nhập, 1 duyệt trang |
| field | `ai.e_I` | `screenWidth` | HIGH | = a_u.a_I |
| field | `ai.f_I` | `screenHeight` | HIGH | = a_u.b_I |
| field | `ai.a_Font` | `font` | HIGH | Font.getFont(0,0,8) SMALL |
| field | `ai.g_I` | `keyCode` | HIGH | keyPressed -> -501..-507 (lên/xuống/trái/phải/OK/mềm trái/mềm phải), -9999 = không có |
| field | `ai.h_I` | `gameAction` | HIGH | getGameAction(g_I) |
| field | `ai.g_Z` | `isPointerPressed` | HIGH | pointerPressed |
| field | `ai.a_I` | `pointerX` | HIGH | pointerPressed x |
| field | `ai.b_I` | `pointerY` | HIGH | pointerPressed y |
| field | `ai.i_I` | `headerHeight` | HIGH | = k_I; vùng đầu |
| field | `ai.j_I` | `footerHeight` | HIGH | = k_I+10; thanh phím mềm |
| field | `ai.k_I` | `lineHeight` | HIGH | font height + 2 |
| field | `ai.a_String` | `cancelLabel` | HIGH | "Hủy" khi đang tải |
| field | `ai.a_AString` | `softKeyLabels` | HIGH | "Có","Không","Đăng nhập",...,"Trở về"; [10]/[11] nút tuỳ biến lv=1/2 |
| field | `ai.a_B` | `leftSoftKey` | HIGH | chỉ số softKeyLabels vẽ bên trái |
| field | `ai.b_B` | `rightSoftKey` | HIGH | bên phải |
| field | `ai.c_B` | `prevLeftSoftKey` | HIGH | a(byte,byte) lưu cũ |
| field | `ai.d_B` | `prevRightSoftKey` | HIGH | a(byte,byte) lưu cũ |
| field | `ai.b_AI` | `footerGradient` | MED | a(12,...) vẽ trong a(Graphics,int,int,int) |
| field | `ai.c_AI` | `footerEdgeGradient` | MED | chấm hai mép thanh |
| field | `ai.d_AI` | `progressGradient` | HIGH | thanh "Đang kết nối...%" |
| field | `ai.e_AI` | `dialogGradient` | HIGH | nền hộp "Gợi ý" |
| field | `ai.a_AI` | `highlightGradient` | HIGH | ac vẽ dòng menu đang chọn |
| field | `ai.f_AI` | `bodyGradient` | HIGH | nền vùng nội dung |
| field | `ai.a_TextBox` | `textBox` | HIGH | TextBox "Hãy nhập" cho input lv!=0 |
| field | `ai.a_Command` | `okCommand` | HIGH | Command "OK" |
| field | `ai.b_Command` | `backCommand` | HIGH | Command "Trở về" |
| field | `ai.a_Thread` | `repaintThread` | HIGH | new Thread(ai) trong u.a_V |
| field | `ai.e_Z` | `isInitialized` | HIGH | ac.f() chỉ chạy một lần |
| hàm | `ai.a(boolean)` | `initMetrics` | HIGH | tính kích thước màn, chiều cao dòng, các dải gradient |
| hàm | `ai.f()` | `initialize` | HIGH | abstract; ac.f(): Vector, error.png, kích thước cuộn |
| hàm | `ai.a(v)` | `setListener` | HIGH | a_an.a_v = listener |
| hàm | `ai.a(Graphics)` | `drawContent` | HIGH | abstract, paint() gọi giữa khung |
| hàm | `ai.a(Graphics,int,int,int)` | `drawBar` | HIGH | dải ngang (đầu/chân) với gradient |
| hàm | `ai.a(Graphics,int,int,int,int,int[])` | `fillGradient` | HIGH | vẽ từng dòng màu theo mảng |
| hàm | `ai.a(byte,byte)` | `setSoftKeys` | HIGH | lưu cũ, đặt trái/phải |
| hàm | `ai.a_V(int)` | `setMode` | HIGH | abstract; ac: 0 "phải đăng nhập", 1 duyệt |
| hàm | `ai.a(Graphics,String,int,int,int,int,int)` | `drawOutlinedText` | HIGH | bóng 6177536 + chữ 16770602 |
| hàm | `ai.a(int,int,int)` | `makeGradient` | HIGH | nội suy RGB n bước |
| hàm | `ai.a()` | `processKey` | HIGH | paint gọi: hộp thoại (Có/Không thoát/đăng xuất) hoặc d() |
| hàm | `ai.d()` | `handleKey` | HIGH | abstract; ac xử lý -501..-507 |
| hàm | `ai.b()` | `clearPointer` | HIGH | g_Z=false, x=y=-1 |
| hàm | `ai.e()` | `closePortal` | HIGH | abstract; ac.e(): lưu settings, giải phóng |
| hàm | `ai.b(String)` | `showMessage` | HIGH | abstract; ac -> a_V(String) trang thông báo |
| hàm | `ai.a(String,int)` | `showConfirm` | HIGH | setSoftKeys(Có,Không), dialogLines, isDialogOpen |
| hàm | `ai.b(Graphics)` | `drawDialog` | HIGH | hộp "Gợi ý : " |
| hàm | `ai.a(int,int,int,int,int,int)` | `isInSoftKeyArea` | MED | hit-test 25px cho chạm phím mềm |
| hàm | `ai.g()` | `releaseResources` | HIGH | d_Z=false, xoá vector/mảng |

### `ac` → **PortalBrowser** (HIGH) — Canvas duyệt trang cổng: dựng widget từ tag, cuộn, focus link, menu "1.Trang chủ..6.Về trò chơi", lịch sử 5 trang

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ac.a_I` | `focusedLinkIndex` | HIGH | so với ag.d_I (linkIndex) để tô focus; dùng index vào c_Vector |
| field | `ac.l_I` | `focusedWidgetIndex` | HIGH | = b_I(a_I) chỉ số trong a_Vector |
| field | `ac.B_I` | `activeInputIndex` | HIGH | widget input (r) đang sửa; b_Z(): B_I==l_I |
| field | `ac.m_I` | `menuIndex` | HIGH | dòng menu b_AString |
| field | `ac.n_I` | `scrollBarW` | HIGH | =5 |
| field | `ac.o_I` | `scrollBarH` | HIGH | chiều cao rãnh |
| field | `ac.p_I` | `scrollBarX` | HIGH | e_I - n_I - 1 |
| field | `ac.q_I` | `scrollBarY` | HIGH | headerHeight+1 |
| field | `ac.r_I` | `thumbW` | HIGH | n_I - 2 |
| field | `ac.s_I` | `thumbH` | HIGH | tỉ lệ viewHeight/contentHeight |
| field | `ac.t_I` | `scrollY` | HIGH | ag.k_I = ag.j_I - t_I |
| field | `ac.u_I` | `cursorY` | HIGH | y dòng đang dựng; a_aq lưu theo dòng |
| field | `ac.v_I` | `lineMaxHeight` | HIGH | cao nhất trong dòng hiện tại |
| field | `ac.b_I` | `contentWidth` | HIGH | e_I - n_I - 2; vượt -> xuống dòng |
| field | `ac.c_I` | `cursorX` | HIGH | x đang dựng; an tag "blank" cộng pixel |
| field | `ac.w_I` | `viewHeight` | HIGH | f_I - header - footer |
| field | `ac.x_I` | `currentLine` | HIGH | ++ ở a_V() newLine; truyền làm ag.e_I |
| field | `ac.y_I` | `firstVisibleLine` | MED | cuộn lên/xuống theo dòng |
| field | `ac.z_I` | `lastVisibleLine` | MED | dòng cuối còn thấy (addWidget) |
| field | `ac.A_I` | `historyIndex` | HIGH | vị trí trong d_Vector (Tiến/Trở về) |
| field | `ac.f_Z` | `showScrollBar` | HIGH | u_I > w_I |
| field | `ac.g_Z` | `isEditing` | HIGH | input đang bật bàn phím |
| field | `ac.a_Vector` | `widgets` | HIGH | ag[] của trang |
| field | `ac.c_Vector` | `links` | HIGH | al (href/cache/addField) theo linkIndex |
| field | `ac.d_Vector` | `history` | HIGH | aa (tối đa 5) |
| field | `ac.a_al` | `currentRequest` | HIGH | ak.a(al,...) gán; "Làm mới" gửi lại |
| field | `ac.a_aq` | `lineYs` | HIGH | aq: y của từng dòng (a_V) |
| field | `ac.a_Image` | `errorImage` | HIGH | "/com/tencent/mbox/error.png" khi thiếu ảnh |
| field | `ac.h_Z` | `hasLeftButton` | HIGH | button lv=1 -> phím mềm trái nhãn [10] |
| field | `ac.i_Z` | `hasRightButton` | HIGH | button lv=2 -> phím mềm phải nhãn [11] |
| field | `ac.b_al` | `leftButtonRequest` | HIGH | al của nút lv=1 |
| field | `ac.c_al` | `rightButtonRequest` | HIGH | al của nút lv=2 |
| field | `ac.b_AString` | `menuItems` | HIGH | "1.Trang chủ","2.Làm mới","3.Tiến","4.Trợ giúp","5.Đăng xuất","6.Về trò chơi" |
| field | `ac.C_I` | `menuBorderColor` | MED | drawRect viền trong menu |
| field | `ac.D_I` | `menuBgColor` | HIGH | fillRect nền menu |
| field | `ac.E_I` | `menuTextColor` | HIGH | màu dòng thường |
| field | `ac.F_I` | `menuSelTextColor` | HIGH | màu dòng chọn |
| field | `ac.j_Z` | `isLoggedIn` | HIGH | u.a_Z(); menu "5.Đăng xuất" vs "5.Đăng nhập QQ" |
| hàm | `ac.a_V(String)` | `showMessagePage` | HIGH | dựng trang tag br... + a "value" (null -> "Không có dữ liệu") |
| hàm | `ac.a_Ar(String)` | `findInputs` | HIGH | tách '&' và tìm r có tên tương ứng |
| hàm | `ac.a(String,String,String)` | `addLink` | HIGH | al(href, cache, addField) vào links |
| hàm | `ac.a(String,String,String,int)` | `setSoftKeyLink` | HIGH | al cho nút lv 1/2 |
| hàm | `ac.a(ag)` | `addWidget` | HIGH | thêm widget, cập nhật lastVisibleLine |
| hàm | `ac.a(Hashtable)` | `addButton` | HIGH | tag "button" (q), lv 0/1/2 |
| hàm | `ac.b(Hashtable)` | `addInput` | HIGH | tag "input": name/prompt/len/size/imp/lv -> r |
| hàm | `ac.c(Hashtable)` | `addImage` | HIGH | tag "img": href/id/align/valign -> t |
| hàm | `ac.d(Hashtable)` | `addText` | HIGH | tag "a": value/href/size/style/color/space, tự xuống dòng |
| hàm | `ac.a(String,boolean,int,int,int,int)` | `addTextWidget` | HIGH | new am(...), tiến cursorX |
| hàm | `ac.a_V()` | `newLine` | HIGH | tag "br": cursorX=5, lineYs.add, cursorY += lineMaxHeight+1 |
| hàm | `ac.b_V()` | `finishLayout` | HIGH | sau khi dựng trang: bật thanh cuộn nếu cao hơn view |
| hàm | `ac.e(Hashtable)` | `addPartLine` | HIGH | tag "partline" -> ah |
| hàm | `ac.a(Hashtable[])` | `onPageLoaded` | HIGH | đẩy aa vào lịch sử (tối đa 5), đặt phím mềm |
| hàm | `ac.a_Z()` | `canGoBack` | HIGH | historyIndex > 0 |
| hàm | `ac.c()` | `clearPage` | HIGH | xoá widget/link, đặt lại cuộn |
| hàm | `ac.b_V(int)` | `requestCommand` | HIGH | "Đang kết nối..." + an.a(cmd) |
| hàm | `ac.a_I(int)` | `findNextFocusLine` | MED | hướng 0/1/2/3/5 tìm dòng widget link kế |
| hàm | `ac.b_I(int)` | `widgetIndexOfLink` | HIGH | widget có d_I == linkIndex |
| hàm | `ac.a(int,int)` | `lineY` | MED | lineYs.get(line) |
| hàm | `ac.c_I(int)` | `linkIndexAtLine` | HIGH | linkIndex của widget link đầu tiên trên dòng |
| hàm | `ac.c_V(int)` | `moveFocusUp` | HIGH | -501/-503 (lên/trái), cuộn lên |
| hàm | `ac.d(int)` | `moveFocusDown` | HIGH | -502/-504 (xuống/phải), cuộn xuống |
| hàm | `ac.h()` | `applyScroll` | HIGH | t_I = lineY(firstVisibleLine); ag.k_I |
| hàm | `ac.i()` | `updateFocus` | HIGH | đặt activeInputIndex nếu focus là input |
| hàm | `ac.b_Z()` | `isInputFocused` | HIGH | B_I>=0 && B_I==l_I |

### `an` → **PortalManager** (HIGH) — bảng lệnh -> URL (index.m, login.m, invoke/uploadData.m...), dựng trang từ thẻ, tiến trình tải, phiên _uid/_sid/_a8

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `an.a_v` | `listener` | HIGH | ai.a(v); gọi a_v.a(id, by) |
| field | `an.a_I` | `loadState` | HIGH | 1 = đang tải (vẽ "Đang kết nối...%"), 0 xong |
| field | `an.b_I` | `progressPercent` | HIGH | 0..100 |
| field | `an.c_I` | `progressTick` | HIGH | tăng chậm tới 40% |
| field | `an.a_Z` | `isResponseReceived` | HIGH | b(Hashtable[]) / a(String) đặt true |
| field | `an.d_I` | `waitTicks` | HIGH | >300 -> "Hết thời gian kết nối" |
| field | `an.a_Vector` | `structRecords` | HIGH | cb/h/dg từ tag "struct" |
| field | `an.a_cb` | `userProfile` | HIGH | id 7 cuối cùng |
| field | `an.b_Vector` | `leaderboard` | HIGH | id 9, khử trùng uid, tối đa 50 |
| field | `an.a_ak` | `protocol` | HIGH | new ak(u,this) |
| field | `an.a_u` | `client` | HIGH | PortalClient |
| hàm | `an.a_V()` | `startNetwork` | HIGH | ak.a() -> ap.a() khởi worker |
| hàm | `an.b()` | `stopNetwork` | HIGH | ak.c() -> ap.b() |
| hàm | `an.c()` | `cancelRequest` | HIGH | ak.b() -> ap.c() |
| hàm | `an.a(Hashtable[])` | `renderPage` | HIGH | switch theo ag.a_AString: a/input/button/cache/br/img/return/partline/blank/redirect/struct |
| hàm | `an.b(Hashtable[])` | `onPageReceived` | HIGH | isResponseReceived=true, ac.a(Hashtable[]) |
| hàm | `an.a(Hashtable,String)` | `getString` | HIGH | get hoặc "" |
| hàm | `an.a(Hashtable,String,int)` | `getInt` | HIGH | parseInt hoặc mặc định |
| hàm | `an.d()` | `tickProgress` | HIGH | paint gọi; tăng %; timeout 300 |
| hàm | `an.e()` | `beginLoading` | HIGH | a_I=1, b_I=0... |
| hàm | `an.a(String)` | `onError` | HIGH | showMessage(msg) hoặc dừng |
| hàm | `an.a(al,int)` | `setCommandUrl` | HIGH | 1 pay.m?_type=1, 10 index.m, 17 loginInput.m, 2100 uploadData.m... |
| hàm | `an.a(int)` | `sendCommand` | HIGH | al cho lệnh + session params, ak.a(al,null) |
| hàm | `an.a(al,r[])` | `request` | HIGH | gửi nếu href khác rỗng |
| hàm | `an.a(al)` | `addSessionParams` | HIGH | _uid/_sid/_a8 |
| hàm | `an.a(int,String,int,int,String,String,String)` | `uploadScore` | HIGH | 2100: _score,_oppUin,_oppScore,_winner,_dataKey,_dataValue,_extInfo |
| hàm | `an.a(int,by)` | `storeResultData` | HIGH | 7 -> userProfile, 9 -> leaderboard |
| hàm | `an.a_cb()` | `getUserProfile` | HIGH | trả a_cb |
| hàm | `an.f()` | `clearUserData` | HIGH | a_cb=null, leaderboard rỗng |

### `ak` → **PortalProtocol** (HIGH) — implements p: dựng gói nhị phân + mã TEA khoá "TT@Game911&007**", parse phản hồi bằng z

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ak.a_ap` | `queue` | HIGH | new ap(u) |
| field | `ak.a_an` | `manager` | HIGH | an |
| field | `ak.a_u` | `client` | HIGH | u |
| hàm | `ak.a()` | `start` | HIGH | ap.a() |
| hàm | `ak.a(af)` | `onRequestDone` | HIGH | p.a: parse -> an.a/an.b |
| hàm | `ak.b(af)` | `onRequestFailed` | HIGH | p.b: an.a(errorMessage) |
| hàm | `ak.a(byte[])` | `parsePage` | HIGH | UTF-8 -> z -> Hashtable[] {"type": tên thẻ, thuộc tính} |
| hàm | `ak.a(al,r[])` | `sendRequest` | HIGH | href "http..." -> platformRequest; khác: gộp cache/input, POST/GET tới af.a_String/af.b_String + href |
| hàm | `ak.b()` | `cancel` | HIGH | ap.c() |
| hàm | `ak.c()` | `stop` | HIGH | ap.b() |
| hàm | `ak.a(al)` | `buildPacket` | HIGH | 0x02, len, 100, 4, platformId, f_I, gameId, tag, _uuid, 0, version, body, 0x03 -> aj TEA |
| hàm | `ak.a(byte[],int,long)` | `putInt32` | HIGH | big-endian 4 byte |
| hàm | `ak.a(byte[],int,short)` | `putInt16` | HIGH | big-endian 2 byte |
| hàm | `ak.a(byte[],int)` | `getInt16` | HIGH | đọc short big-endian (an "return") |
| hàm | `ak.a(Hashtable)` | `serializeMap` | HIGH | short n, (byte len key)(short len value) UTF-8; cũng lưu RMS |
| hàm | `ak.a(int[],String)` | `dumpArray` | HIGH | System.out "name={...}" |
| hàm | `ak.a(short[][],String)` | `dumpArray` | HIGH | debug in mảng 2 chiều |
| hàm | `ak.a(String[],String)` | `dumpArray` | HIGH | debug |

### `p` → **HttpRequestListener** (HIGH) — interface a(af)/b(af); ap gọi a khi OK, b khi lỗi

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| hàm | `p.a(af)` | `onRequestDone` | HIGH | ap: !var9 -> var4.a(var3) |
| hàm | `p.b(af)` | `onRequestFailed` | HIGH | ap: var9 -> var4.b(var3) |

### `ap` → **HttpRequestQueue** (HIGH) — Vector af (tối đa 20) + worker s; HttpConnection với header Q-UA/screen/..., proxy CMWAP 10.0.0.172, redirect, giới hạn 400KB

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ap.a_s` | `worker` | HIGH | new s(this) |
| field | `ap.a_Z` | `isRunning` | HIGH | vòng worker |
| field | `ap.b_Z` | `useCmwapProxy` | HIGH | true -> "http://10.0.0.172:80"+path + X-Online-Host; tự đảo khi lỗi |
| field | `ap.a_Vector` | `queue` | HIGH | af chờ |
| field | `ap.a_I` | `maxQueueSize` | HIGH | 20 |
| field | `ap.a_u` | `client` | HIGH | lấy header Q-UA... |
| field | `ap.a_AI` | `lock` | HIGH | int[0] làm monitor wait/notifyAll |
| hàm | `ap.a(af)` | `enqueue` | HIGH | addElement + notifyAll |
| hàm | `ap.a()` | `start` | HIGH | tạo và start s |
| hàm | `ap.b()` | `stop` | HIGH | isRunning=false, xoá queue |
| hàm | `ap.c()` | `cancelAll` | HIGH | xoá queue, worker.isCancelled=true |
| hàm | `ap.a_Z(ap)` | `isRunningOf` | HIGH | accessor tĩnh cho s |
| hàm | `ap.a_AI(ap)` | `lockOf` | HIGH | accessor |
| hàm | `ap.a_Vector(ap)` | `queueOf` | HIGH | accessor |
| hàm | `ap.a(ap,af,s)` | `execute` | HIGH | thực hiện HTTP, thông điệp lỗi tiếng Việt, gọi listener |

### `s` → **HttpWorker** (HIGH) — Thread: chờ queue, lấy af, ap.a(ap,af,this)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `s.a_Z` | `isCancelled` | HIGH | ap.c() đặt true; ap.execute bỏ kết quả |
| field | `s.a_ap` | `queue` | HIGH | ap |

### `af` → **HttpRequest** (HIGH) — url, method GET/POST, body/response bytes, listener p, thông báo lỗi

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `af.a_String` | `portalServerUrl` | HIGH | "http://mbsvrt.3g.qq.com/"; ghi đè bởi cache "_server" |
| field | `af.b_String` | `sngameServerUrl` | HIGH | "http://sngame.3g.qq.com/sngame/" khi platformId==888888 |
| field | `af.c_String` | `sngameServerUrl2` | LOW | cùng giá trị; không thấy đọc |
| field | `af.a_I` | `priority` | MED | =5; s: thread prio 5 và a_I()==1 thì chờ |
| field | `af.d_String` | `url` | HIGH | Connector.open |
| field | `af.a_AB` | `data` | HIGH | body POST / dữ liệu phản hồi (af tạm trong ap.execute) |
| field | `af.a_p` | `listener` | HIGH | p |
| field | `af.e_String` | `method` | HIGH | "GET"/"POST" |
| field | `af.f_String` | `errorMessage` | HIGH | ap.execute đặt thông báo lỗi |
| field | `af.a_J` | `bytesReceived` | HIGH | cộng dồn, giới hạn 409600 |
| hàm | `af.a(String)` | `setUrl` | HIGH | redirect Location |
| hàm | `af.a_String()` | `getMethod` | HIGH | setRequestMethod |
| hàm | `af.b(String)` | `setMethod` | HIGH | "GET"/"POST" |
| hàm | `af.b()` | `getUrl` | HIGH | Connector.open |
| hàm | `af.a_p()` | `getListener` | HIGH | p |
| hàm | `af.a_AB()` | `getData` | HIGH | bản sao |
| hàm | `af.a(byte[])` | `setData` | HIGH | bản sao |
| hàm | `af.c()` | `getErrorMessage` | HIGH | ak.b(af) |
| hàm | `af.c(String)` | `setErrorMessage` | HIGH | ap.execute |
| hàm | `af.a_I()` | `getPriority` | MED | s |

### `z` → **MarkupParser** (HIGH) — máy trạng thái <tag a="v">, bỏ <!-- -->, <?..?>, bỏ thẻ "tml"; KHÔNG phải cookie jar

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `z.a_Z` | `isParsing` | LOW | đặt true khi parse, không đọc lại |
| field | `z.b_Z` | `isReentered` | LOW | đặt nếu parse lồng; không đọc |
| field | `z.a_Vector` | `tags` | HIGH | ay |
| hàm | `z.a()` | `tagCount` | HIGH | size |
| hàm | `z.a(int)` | `tagAt` | HIGH | elementAt |
| hàm | `z.a(String)` | `parse` | HIGH | tách thẻ + thuộc tính |

### `ay` → **MarkupTag** (HIGH) — tên thẻ + Vector thuộc tính (ar)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ay.a_String` | `name` | HIGH | ak: put("type", a_String) |
| field | `ay.a_Vector` | `attributes` | HIGH | ar |
| hàm | `ay.a(ar)` | `addAttribute` | HIGH | addElement |

### `ar` → **MarkupAttribute** (HIGH) — cặp name=value

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ar.a_String` | `name` | HIGH | trước '=' |
| field | `ar.b_String` | `value` | HIGH | trong nháy |
| hàm | `ar.a(String)` | `setValue` | HIGH | z gán |
| hàm | `ar.a()` | `getName` | HIGH | ak put key |
| hàm | `ar.b()` | `getValue` | HIGH | ak put value |

### `aq` → **IntList** (HIGH) — mảng int tự tăng 30

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `aq.a_AI` | `values` | HIGH | dữ liệu |
| field | `aq.a_I` | `count` | HIGH | số phần tử |
| hàm | `aq.a_V(int)` | `add` | HIGH | mở rộng +30 |
| hàm | `aq.a_I(int)` | `get` | HIGH | values[i] |
| hàm | `aq.a_I()` | `size` | HIGH | count |
| hàm | `aq.a_V()` | `clear` | HIGH | count=0 |

### `aa` → **PageSnapshot** (HIGH) — Hashtable[] một trang, lưu trong lịch sử ac.d_Vector (Tiến/Trở về)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `aa.a_AHashtable` | `elements` | HIGH | an.a(var.a_AHashtable) dựng lại |

### `al` → **PageRequest** (HIGH) — link/yêu cầu: href, tham số, tên ô input gửi kèm (addField), khoá cache gửi kèm

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `al.a_Z` | `isRedirect` | HIGH | tag "redirect" đặt true; ac không lưu lịch sử |
| field | `al.a_AString` | `cacheKeys` | HIGH | a(String) = ad.a(split '&'); ak lấy giá trị từ settings |
| field | `al.a_String` | `inputFields` | HIGH | "addField"; ac.a_Ar(...) tìm r |
| field | `al.a_S` | `protocolVersion` | LOW | =1, ghi short vào gói ak sau _uuid |
| field | `al.a_Hashtable` | `params` | HIGH | thân POST (serializeMap) |
| field | `al.b_String` | `href` | HIGH | "href"/URL lệnh (an.a(al,int)) |
| hàm | `al.a(String)` | `setCacheKeys` | HIGH | tách '&' |

### `ad` → **PortalTextUtil** (HIGH) — tiện ích chuỗi cho cổng: '*', cắt theo độ rộng, tách '&', ngắt dòng

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| hàm | `ad.a(int)` | `maskString` | HIGH | n dấu '*' (ô mật khẩu) |
| hàm | `ad.a(String,int,Font)` | `clipToWidth` | HIGH | lấy tiền tố vừa chiều rộng |
| hàm | `ad.a(String)` | `splitAmpersand` | HIGH | tách theo '&' |
| hàm | `ad.a(String,Font,int)` | `wrapText` | HIGH | ngắt dòng theo chiều rộng |
| hàm | `ad.a(String[],int)` | `resizeArray` | HIGH | cắt/nới mảng |

### `ag` → **PageWidget** (HIGH) — abstract widget trang; a_B = chỉ số trong a_AString (tên thẻ)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ag.a_AString` | `TAG_NAMES` | HIGH | "a","input","button","cache","br","img","tab","return","partline","blank","redirect","struct" |
| field | `ag.b_Z` | `isLink` | HIGH | có href / chọn được; d_I=-1 nếu không |
| field | `ag.d_I` | `linkIndex` | HIGH | = links.size(); so với ac.a_I để focus |
| field | `ag.e_I` | `lineIndex` | HIGH | ac.x_I; t dùng ac.a_aq |
| field | `ag.a_B` | `type` | HIGH | 0 a,1 input,2 button,5 img,8 partline |
| field | `ag.f_I` | `w` | HIGH | a(x,y,w,h) |
| field | `ag.g_I` | `h` | HIGH | a(x,y,w,h) |
| field | `ag.h_I` | `x` | HIGH | bố cục |
| field | `ag.i_I` | `y` | HIGH | bố cục |
| field | `ag.j_I` | `contentTop` | HIGH | = headerHeight + 2 |
| field | `ag.k_I` | `drawOffsetY` | HIGH | = contentTop - scrollY |
| field | `ag.l_I` | `drawX` | HIGH | x khi vẽ |
| field | `ag.m_I` | `drawY` | HIGH | y + drawOffsetY |
| field | `ag.n_I` | `textColor` | HIGH | am thường / ah |
| field | `ag.o_I` | `linkFocusColor` | HIGH | am focus |
| field | `ag.p_I` | `linkColor` | HIGH | am link không focus |
| field | `ag.q_I` | `inputBorderColor` | HIGH | r không focus |
| field | `ag.r_I` | `inputFocusBorderColor` | HIGH | r focus |
| field | `ag.s_I` | `inputBgColor` | HIGH | r không focus |
| field | `ag.t_I` | `inputFocusBgColor` | HIGH | r focus không sửa |
| field | `ag.u_I` | `inputTextColor` | HIGH | r không focus |
| field | `ag.v_I` | `inputFocusTextColor` | HIGH | r focus |
| field | `ag.w_I` | `buttonTextColor` | HIGH | q thường |
| field | `ag.x_I` | `buttonFocusTextColor` | HIGH | q focus |
| field | `ag.y_I` | `imageFocusColor` | HIGH | t viền/gạch focus |
| hàm | `ag.a(int,int,int,int)` | `setBounds` | HIGH | x,y,w,h |
| hàm | `ag.b(int,int)` | `setPosition` | HIGH | x,y |
| hàm | `ag.a(Graphics)` | `draw` | HIGH | abstract; ac vẽ từng widget |

### `am` → **PageTextWidget** (HIGH) — thẻ "a" (type 0): chữ/link có màu, style, cỡ

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `am.a_I` | `fontStyle` | HIGH | Font.getFont(0, a_I, b_I) |
| field | `am.b_I` | `fontSize` | HIGH | 8/16/0 |
| field | `am.c_I` | `color` | HIGH | -16777216 = mặc định |
| field | `am.a_String` | `text` | HIGH | chữ |
| hàm | `am.a(int,int,int)` | `setStyle` | HIGH | color, style, size -> đo w/h |
| hàm | `am.b(Graphics)` | `applyStyle` | HIGH | setColor/setFont |

### `r` → **PageInputWidget** (HIGH) — thẻ "input" (type 1): ô nhập, bàn phím đa nhấn (w) hoặc TextBox; mật khẩu -> MD5

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `r.a_w` | `keypad` | HIGH | new w(constraints,maxLen,u) khi inputMode==0 |
| field | `r.a_I` | `maxLength` | HIGH | "len"; TextBox.setMaxSize |
| field | `r.b_I` | `constraints` | HIGH | "imp" -> TextField constraints (65536 PASSWORD, 2 NUMERIC...) |
| field | `r.c_I` | `inputMode` | HIGH | "lv": 0 bàn phím trong trang, khác -> TextBox |
| field | `r.z_I` | `textWidth` | HIGH | stringWidth(displayText) |
| field | `r.a_String` | `prompt` | HIGH | "prompt"; TextBox.setTitle |
| field | `r.b_String` | `fieldName` | HIGH | "name"; ak gửi params |
| field | `r.a_Z` | `isEditing` | HIGH | ac bật bằng OK |
| field | `r.c_String` | `displayText` | HIGH | chữ hiển thị ('*' nếu mật khẩu) |
| field | `r.d_String` | `value` | HIGH | giá trị thật |
| field | `r.A_I` | `charWidth` | HIGH | charWidth('a') |
| hàm | `r.a(String,String)` | `setPromptAndName` | HIGH | prompt, name |
| hàm | `r.b(Graphics)` | `drawEditor` | HIGH | con trỏ + bảng ký hiệu w |
| hàm | `r.a(int,int)` | `handleKey` | HIGH | chuyển cho w khi đang sửa |
| hàm | `r.a(String)` | `setText` | HIGH | cập nhật value/displayText |
| hàm | `r.a_String()` | `getValue` | HIGH | mật khẩu -> n.a_String (MD5 hex) |
| hàm | `r.a_Z()` | `isSymbolPickerOpen` | HIGH | w.a_Z() |
| hàm | `r.b()` | `getPrompt` | HIGH | a_String |
| hàm | `r.c()` | `getFieldName` | HIGH | b_String |

### `q` → **PageButtonWidget** (HIGH) — thẻ "button" (type 2): khung bo tròn + nhãn

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `q.a_String` | `label` | HIGH | value |
| hàm | `q.a(String)` | `setLabel` | HIGH | ac.a(Hashtable) |

### `t` → **PageImageWidget** (HIGH) — thẻ "img" (type 5): ảnh từ pageImages hoặc error.png, căn lề

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `t.a_I` | `imageId` | HIGH | 1-based vào ai.b_Vector |
| field | `t.b_I` | `align` | HIGH | "align" 2 giữa, 3 phải |
| field | `t.c_I` | `valign` | HIGH | "valign" 2 giữa, 3 dưới |
| hàm | `t.a(int,int,int)` | `setImage` | HIGH | id, align, valign |

### `ah` → **PageSeparatorWidget** (HIGH) — thẻ "partline" (type 8): lặp chuỗi mẫu hoặc 2 đường kẻ

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ah.a_String` | `patternText` | HIGH | value lặp theo chiều rộng |
| field | `ah.a_I` | `rowHeight` | HIGH | "height" |
| field | `ah.b_I` | `lineThickness` | MED | lineHeight nếu có chữ, 2 nếu kẻ |
| field | `ah.c_I` | `color` | HIGH | "color" |
| hàm | `ah.a(String,int,int,int)` | `setup` | HIGH | value, space, height, color |

### `w` → **MultiTapInput** (HIGH) — bàn phím đa nhấn T9 (2=abc...), '#' đổi 123/abc/ABC, '*' bảng "Chọn ký hiệu:"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `w.a_String` | `text` | HIGH | chuỗi đã nhập |
| field | `w.a_I` | `inputType` | HIGH | 0 tự do, 2 số; 65536(PASSWORD) -> 0 |
| field | `w.b_I` | `tapIndex` | HIGH | lần nhấn trên cùng phím |
| field | `w.c_I` | `currentKey` | HIGH | hàng a_AAC |
| field | `w.d_I` | `commitTimer` | HIGH | 16/14 đếm lùi, ==1 thì chốt ký tự |
| field | `w.e_I` | `caseMode` | HIGH | 0 "123", 1 "abc", 2 "ABC" |
| field | `w.a_Z` | `isSymbolPickerOpen` | HIGH | phím '*' (42) |
| field | `w.f_I` | `symbolIndex` | HIGH | ô chọn trong bảng ký hiệu |
| field | `w.g_I` | `symbolCols` | HIGH | số cột bảng |
| field | `w.h_I` | `symbolRows` | HIGH | số hàng bảng |
| field | `w.a_AAC` | `keyChars` | HIGH | {'2','2','a','b','c'}... |
| field | `w.a_AC` | `symbols` | HIGH | bảng ký hiệu |
| field | `w.b_String` | `selectLabel` | HIGH | "Chọn" |
| field | `w.c_String` | `cancelLabel` | HIGH | "Hủy" |
| field | `w.i_I` | `maxLength` | HIGH | giới hạn độ dài |
| field | `w.a_u` | `client` | HIGH | lấy kích thước màn |
| hàm | `w.a_String()` | `getText` | HIGH | a_String |
| hàm | `w.a_Z()` | `isSymbolPickerOpen` | HIGH | a_Z |
| hàm | `w.a_Z(int,int)` | `handleKey` | HIGH | keyCode, action |
| hàm | `w.b(int,int)` | `handleTapKey` | HIGH | '#', xoá (-8/-12), phím số |
| hàm | `w.a_V()` | `tick` | HIGH | đếm lùi commitTimer |
| hàm | `w.b()` | `commitPending` | HIGH | thêm ký tự đang chờ |
| hàm | `w.a_C()` | `pendingChar` | HIGH | ký tự hiện tại theo caseMode |
| hàm | `w.a_C(int,int)` | `keyCharAt` | HIGH | a_AAC[k][i+1] |
| hàm | `w.a(Graphics,int,int,int)` | `drawCursor` | HIGH | nháy + chỉ báo 123/abc/ABC |
| hàm | `w.a(Graphics)` | `drawSymbolPicker` | HIGH | "Chọn ký hiệu:" |
| hàm | `w.a_I()` | `getCommitTimer` | HIGH | r: ==1 -> setText |
| hàm | `w.a_e()` | `newBattleActor` | MED | factory tĩnh lạc chỗ: return new e(); bn gán a_Ae[i] |

### `aj` → **QQTeaCipher** (HIGH) — TEA kiểu QQ: delta 2654435769(0x9E3779B9), 16 vòng, đệm rand&0xF8|pad + 2 salt + 7 byte 0, CBC kiểu QQ

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `aj.a_AB` | `plainBlock` | HIGH | khối 8 byte đang điền |
| field | `aj.b_AB` | `prevPlainBlock` | HIGH | khối trước (XOR) |
| field | `aj.c_AB` | `output` | HIGH | bản mã |
| field | `aj.a_I` | `outPos` | HIGH | vị trí ghi |
| field | `aj.b_I` | `prevOutPos` | HIGH | khối mã trước |
| field | `aj.c_I` | `blockPos` | HIGH | vị trí trong khối / số đệm |
| field | `aj.d_I` | `padCount` | HIGH | đếm salt/0 |
| field | `aj.d_AB` | `key` | HIGH | khoá 16 byte |
| field | `aj.a_Z` | `isFirstBlock` | HIGH | XOR với 0 cho khối đầu |
| field | `aj.a_Random` | `random` | HIGH | đệm ngẫu nhiên |
| hàm | `aj.a(byte[],int)` | `readUInt32` | HIGH | big-endian |
| hàm | `aj.a(byte[],byte[])` | `encrypt` | HIGH | ak: new aj().a(packet, key) |
| hàm | `aj.a(byte[])` | `encipherBlock` | HIGH | 16 vòng TEA |
| hàm | `aj.a()` | `encryptNextBlock` | HIGH | XOR + encipher + XOR prev |

### `bq` → **IdeaCipher** (HIGH) — IDEA: 52 subkey, nhân mod 65537, khoá nghịch đảo (giải mã), đầu vào hex ("Length Not Double Num")

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bq.a_AB` | `key` | HIGH | a_AB(String) đặt |
| hàm | `bq.a_AB(String)` | `setKey` | HIGH | cắt/đệm '0' tới 16 ký tự |
| hàm | `bq.a_String(String)` | `decryptHex` | HIGH | giải mã chuỗi hex, bỏ 6 ký tự cuối |
| hàm | `bq.a_I(byte[],int)` | `readUInt16` | HIGH | big-endian |
| hàm | `bq.a(int,byte[],int)` | `writeUInt16` | HIGH | big-endian |
| hàm | `bq.a(int,int)` | `mulMod` | HIGH | nhân mod 65537 (0 = 65536) |
| hàm | `bq.a_AI(byte[])` | `expandKey` | HIGH | 52 subkey, xoay 25 bit |
| hàm | `bq.a(int)` | `mulInverse` | HIGH | nghịch đảo nhân mod 65537 (Euclid) |
| hàm | `bq.a_AB(byte[],int)` | `pad` | HIGH | đệm kiểu PKCS (giá trị = số byte đệm) |
| hàm | `bq.a(byte[],byte[],boolean)` | `cryptBlocks` | HIGH | 8 vòng + output transform |
| hàm | `bq.a_AB(byte[])` | `hexToBytes` | HIGH | 2 ký tự hex -> byte |

### `n` → **Md5** (HIGH) — MD5 (RFC1321 port): IV 1732584193..., bảng shift {7,12,17,22}..., byte[] -> hex HOA; r dùng băm mật khẩu

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `n.a_AAI` | `shifts` | HIGH | {7,12,17,22},{5,9,14,20},{4,11,16,23},{6,10,15,21} |
| field | `n.a_AB` | `PADDING` | HIGH | 0x80, 0... |
| field | `n.a_AJ` | `state` | HIGH | A,B,C,D |
| field | `n.b_AJ` | `bitCount` | HIGH | số bit |
| field | `n.b_AB` | `buffer` | HIGH | khối 64 byte |
| field | `n.c_AB` | `digest` | HIGH | 16 byte |
| hàm | `n.a()` | `reset` | HIGH | IV |
| hàm | `n.a(long,long,long,long,long,long,long)` | `ff` | HIGH | (b&c)/(~b&d) |
| hàm | `n.b(long,long,long,long,long,long,long)` | `gg` | HIGH | (b&d)/(c&~d) |
| hàm | `n.c(long,long,long,long,long,long,long)` | `hh` | HIGH | b^c^d |
| hàm | `n.d(long,long,long,long,long,long,long)` | `ii` | HIGH | c^(b/~d) |
| hàm | `n.a(byte[],int)` | `update` | HIGH | md5Update |
| hàm | `n.a(byte[],byte[],int,int,int)` | `copyBytes` | HIGH | memcpy |
| hàm | `n.a_V(byte[])` | `transform` | HIGH | 64 bước |
| hàm | `n.a(byte[],long[],int)` | `encode` | HIGH | little-endian |
| hàm | `n.a(byte)` | `toUnsigned` | HIGH | b & 255 |
| hàm | `n.a_String(byte[])` | `hexDigest` | HIGH | r.getValue: MD5 hex HOA |

### `br` → **Md5Text** (HIGH) — MD5 thứ hai (kiểu JS safe_add): chuỗi -> hex thường; a dùng kiểm tra giá trị JAD

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `br.a_String` | `HEX_CHARS` | HIGH | "0123456789abcdef" |
| hàm | `br.a(int)` | `wordToHex` | HIGH | little-endian |
| hàm | `br.a(int,int)` | `safeAdd` | HIGH | cộng 32 bit tách bit dấu |
| hàm | `br.a(int,int,int,int,int,int)` | `cmn` | HIGH | rotl(a+q+x+t,s)+b |
| hàm | `br.a(int,int,int,int,int,int,int)` | `ff` | HIGH | (b&c)/(~b&d) |
| hàm | `br.b(int,int,int,int,int,int,int)` | `gg` | HIGH | (b&d)/(c&~d) |
| hàm | `br.c(int,int,int,int,int,int,int)` | `hh` | HIGH | b^c^d |
| hàm | `br.d(int,int,int,int,int,int,int)` | `ii` | HIGH | c^(b/~d) |
| hàm | `br.a(String)` | `md5Hex` | HIGH | hằng -680876936... |

### `az` → **Base64Decoder** (HIGH) — bảng 43->62, 47->63, '=' kết thúc; an giải ảnh thẻ img

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `az.a_AB` | `DECODE_TABLE` | HIGH | ASCII -> 6 bit |
| hàm | `az.a(String)` | `decode` | HIGH | Image.createImage(az.a(value)) |

### `bk` → **QQCommunityOverlay** (HIGH) — lớp phủ "Cộng đồng QQ": hồ sơ QQ, "Bảng xếp hạng", tải điểm; ab/bs/da gọi; KHÔNG phải form nhập

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bk.a_B` | `viewType` | HIGH | 0 none, 1 hồ sơ, 2 xếp hạng, 3 tải điểm |
| field | `bk.b_B` | `VIEW_NONE` | HIGH | 0 |
| field | `bk.c_B` | `VIEW_PROFILE` | HIGH | 1 "Tên QQ/Cấp/Điểm cao nhất" |
| field | `bk.d_B` | `VIEW_RANKING` | HIGH | 2 "Bảng xếp hạng" |
| field | `bk.e_B` | `VIEW_UPLOAD` | HIGH | 3 tải điểm |
| field | `bk.f_B` | `status` | HIGH | 0 idle, 1 tải, 2 chưa đăng nhập, 3 xong, 4 hết giờ, 5 tải lên lỗi |
| field | `bk.g_B` | `STATUS_IDLE` | HIGH | 0 |
| field | `bk.h_B` | `STATUS_LOADING` | HIGH | "Đang đọc dữ liệu" |
| field | `bk.i_B` | `STATUS_NOT_LOGGED_IN` | HIGH | "Chưa đăng nhập, đăng nhập ngay?" |
| field | `bk.j_B` | `STATUS_DONE` | HIGH | kết quả code 0 |
| field | `bk.k_B` | `STATUS_TIMEOUT` | HIGH | "Hết thời gian lấy dữ liệu" (>10s) |
| field | `bk.l_B` | `STATUS_UPLOAD_FAILED` | HIGH | "Tải lên thất bại, thử lại?" |
| field | `bk.a_S` | `screenWidth` | HIGH | da.b_S |
| field | `bk.b_S` | `screenHeight` | HIGH | da.c_S |
| field | `bk.a_Z` | `swapSoftKeys` | HIGH | đảo vị trí nhãn/vùng chạm phím mềm |
| field | `bk.c_S` | `textWidth` | HIGH | 4/5 chiều rộng để ngắt dòng |
| field | `bk.a_I` | `uploadedScore` | HIGH | thử lại a_V(a_I) |
| field | `bk.b_I` | `rankColW` | HIGH | cột "Hạng" |
| field | `bk.c_I` | `nameColW` | HIGH | cột "Tên" |
| field | `bk.d_I` | `scoreColW` | HIGH | cột "Điểm" |
| field | `bk.e_I` | `tableWidthWanted` | MED | tổng 3 cột |
| field | `bk.f_I` | `tableWidth` | HIGH | min(e_I, c_S) |
| field | `bk.g_I` | `tableX` | HIGH | căn giữa |
| field | `bk.h_I` | `rowCount` | HIGH | số dòng hiển thị |
| field | `bk.i_I` | `tableY` | HIGH | y tiêu đề cột |
| field | `bk.a_cb` | `userProfile` | HIGH | cw.a() |
| field | `bk.a_Adg` | `leaderboard` | HIGH | a(String) parse "/#/" và "/" |
| field | `bk.a_J` | `requestStartTime` | HIGH | timeout 10000ms |
| field | `bk.a_String` | `message` | HIGH | thông báo tải điểm |
| field | `bk.a_bk` | `instance` | HIGH | singleton |
| field | `bk.a_cw` | `api` | HIGH | new cw(...) |
| field | `bk.a_AB` | `nameScrollX` | HIGH | cuộn tên dài |
| field | `bk.b_AB` | `nameScrollPause` | HIGH | dừng 30 nhịp |
| field | `bk.a_StringBuffer` | `loadingDots` | HIGH | "." -> "....." |
| hàm | `bk.b_Z(int)` | `onKey` | HIGH | phím mềm theo status; true = đã xử lý |
| hàm | `bk.a(int,by)` | `onPortalResult` | HIGH | v; 7 hồ sơ, 12 xếp hạng (chuỗi), 3 thông báo |
| hàm | `bk.b_V(int)` | `openPage` | HIGH | cw.a(1, cmd) |
| hàm | `bk.c()` | `reset` | HIGH | về idle |
| hàm | `bk.a(Graphics,String,int,int)` | `drawWrappedText` | HIGH | cg.a(text, c_S), căn đáy |
| hàm | `bk.a(String)` | `parseLeaderboard` | HIGH | "score/name/#/..." |
| hàm | `bk.b(Graphics)` | `drawLeaderboard` | HIGH | "Bảng xếp hạng","Hạng","Tên","Điểm" |
| hàm | `bk.c(Graphics)` | `drawYesNo` | HIGH | "Có","Không" |
| hàm | `bk.a(Graphics,String,String)` | `drawSoftKeyLabels` | HIGH | trái/phải theo swapSoftKeys |
| hàm | `bk.a(MIDlet,Display,Displayable,short,short,boolean,int,int)` | `init` | HIGH | ab/bs: tạo bk + cw(gameId,120) |
| hàm | `bk.a(Graphics)` | `drawOverlay` | HIGH | da.paint gọi; nền ô cờ + nội dung theo status |
| hàm | `bk.a_Z(int)` | `handleKey` | HIGH | da.keyPressed: !bk.a_Z(b.a(k)) |
| hàm | `bk.a(int,int)` | `handlePointer` | HIGH | da.pointerPressed; luôn trả false (lỗi) |
| hàm | `bk.a()` | `openHomePage` | HIGH | bs: b_V(10) index.m |
| hàm | `bk.b()` | `showRanking` | HIGH | bs: cw.a(2005) getRankData _type=3 |
| hàm | `bk.a_V(int)` | `uploadScore` | HIGH | ab: bk.a_V(o.c_I); "Điểm hiện tại của bạn là 0" |

### `a` → **SmsFeeConfig** (HIGH) — đọc JAD ChannelFirst/Second, Term, QQ, FreeCode/Dest, SmsCode1/2, SmsDest1/2, FeeMode, DoCode/Dest, /cpid.txt; MÃ CHẾT (không ai new a)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `a.a_Hashtable` | `cpidTable` | HIGH | cpid -> Vector{code,dest} từ /cpid.txt |
| field | `a.a_AAString` | `smsCodes` | HIGH | SmsCode1/2 tách ';' (mặc định "62","63"/"JG") |
| field | `a.b_AAString` | `smsDests` | HIGH | SmsDest1/2 (mặc định 106662267...) |
| field | `a.c_AAString` | `doCodes` | HIGH | DoCode |
| field | `a.d_AAString` | `doDests` | HIGH | DoDest |
| field | `a.a_String` | `channelFirst` | HIGH | "Fr" / ChannelFirst |
| field | `a.b_String` | `channelSecond` | HIGH | "Sec" / ChannelSecond |
| field | `a.c_String` | `terminal` | HIGH | "N7610" / Term |
| field | `a.d_String` | `freeCode` | HIGH | "299" / FreeCode |
| field | `a.e_String` | `freeDest` | HIGH | "10661700299" / FreeDest |
| field | `a.f_String` | `interChannel` | HIGH | "Idx" / InterChannel |
| field | `a.g_String` | `qq` | HIGH | "0" / QQ |
| field | `a.a_AI` | `logoLinkReco` | HIGH | LogoLinkReco (3 số) |
| field | `a.a_I` | `feeMode` | HIGH | FeeMode |
| field | `a.a_AAI` | `smsCodeUseCounts` | HIGH | a(int) chọn code ít dùng nhất |
| hàm | `a.a(String,String)` | `split` | HIGH | " split. aStr == null" |
| hàm | `a.a_String(String)` | `decryptJadValue` | HIGH | IDEA "a4s3d5f1" + kiểm MD5 32 ký tự cuối |
| hàm | `a.a_I(String)` | `parseInt` | HIGH | Integer.parseInt an toàn |
| hàm | `a.a(byte[])` | `utf8String` | HIGH | new String(..,"utf-8") |
| hàm | `a.a(int,String)` | `getCpidEntry` | HIGH | Vector theo cpid |
| hàm | `a.a_V()` | `loadCpidList` | HIGH | "/cpid.txt" "cpid=..,code=..,dest=.." |
| hàm | `a.a(MIDlet)` | `loadJadProperties` | HIGH | getAppProperty(...) |
| hàm | `a.a(int)` | `pickSmsCode` | HIGH | code/dest ít dùng nhất của nhóm 1/2 |
| hàm | `a.a_String()` | `getChannelFirst` | HIGH | a_String |
| hàm | `a.b()` | `getChannelSecond` | HIGH | b_String |
| hàm | `a.c()` | `getTerminal` | HIGH | c_String |
| hàm | `a.a_AString()` | `getFreeCodeDest` | HIGH | {d_String,e_String} |
| hàm | `a.d()` | `getInterChannel` | HIGH | f_String |
| hàm | `a.e()` | `getQQ` | HIGH | g_String |
| hàm | `a.a_I()` | `getFeeMode` | HIGH | a_I |
| hàm | `a.a(int,int)` | `getDoCodeDest` | HIGH | DoCode/DoDest hoặc cpid |
| hàm | `a.b(int,int)` | `getCpidCodeDest` | HIGH | a(idx, String(cpid)) |

### `aw` → **PayServerRequest** (HIGH) — abstract: POST nhị phân tới "http://wap.moonic.cn/payServer.do" (hoặc proxy 10.0.0.172), timeout bt

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `aw.a_String` | `host` | HIGH | "wap.moonic.cn"; X-Online-Host |
| field | `aw.b_String` | `proxyUrl` | HIGH | "http://10.0.0.172:80/payServer.do" |
| field | `aw.c_String` | `directUrl` | HIGH | "http://"+host+"/payServer.do" |
| field | `aw.a_y` | `connection` | HIGH | new y(this) |
| field | `aw.a_Z` | `isWaiting` | HIGH | bt chạy khi true; y đặt false khi xong |
| hàm | `aw.a(int,int,String)` | `onPayResponse` | HIGH | abstract (cmd, status 0 ok/1 từ chối/2 lỗi mạng/3 khác, msg) |
| hàm | `aw.a(String,String,int)` | `sendRequest` | HIGH | short 12 + UTF gid + UTF cid + short; khởi y và bt |
| hàm | `aw.a()` | `getDirectUrl` | HIGH | c_String |
| hàm | `aw.b()` | `getProxyUrl` | HIGH | b_String |
| hàm | `aw.c()` | `getHost` | HIGH | a_String |

### `y` → **PayServerConnection** (HIGH) — Runnable: POST tới payServer.do (thử trực tiếp rồi proxy), đọc short cmd, byte ok, UTF msg

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `y.a_HttpConnection` | `http` | HIGH | Connector.open |
| field | `y.a_I` | `commandId` | HIGH | =12 |
| field | `y.a_aw` | `callback` | HIGH | nhận a(cmd,status,msg) |
| field | `y.a_AB` | `body` | HIGH | gói gửi |
| field | `y.b_aw` | `owner` | HIGH | đặt owner.isWaiting=false |
| hàm | `y.a(boolean,String,String,String)` | `openConnection` | HIGH | POST, header User-Agent/X-Online-Host |

### `bt` → **PayServerTimeout** (HIGH) — Runnable: 20000ms -> "Hết thời gian kết nối"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bt.a_J` | `startTime` | HIGH | currentTimeMillis lúc gửi |
| field | `bt.b_J` | `timeoutMs` | HIGH | 20000 |
| field | `bt.a_aw` | `request` | HIGH | aw |

### `f` → **LotteryRequest** (HIGH) — aw cho ch: cmd 12 -> "lotteryResult","lotteryMsg","needBuyAmount","handselAmount"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `f.a_I` | `amount` | LOW | luôn 0; truyền ch.a_Z(ch,int) (số tiền tính số tin) |
| field | `f.a_ch` | `flow` | HIGH | ch |

### `ch` → **SmsPaymentFlow** (HIGH) — màn "Thông tin thanh toán": bốc thăm ưu đãi (payServer), tra vùng theo SMSC, gửi SMS (bh), RMS lotteryDate/handselRecord/sendtimes/onegateinfo; KHÔNG được khởi tạo

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ch.a_B` | `payMode` | MED | =2 khi bắt đầu; ==1 thì ghép tiền tố + nội dung |
| field | `ch.a_String` | `productDesc` | HIGH | mô tả hàng hiển thị đầu trang |
| field | `ch.a_S` | `screenWidth` | HIGH | 240; a(int,int) |
| field | `ch.b_S` | `screenHeight` | HIGH | 320 |
| field | `ch.a_AString` | `smsPrefixes` | HIGH | "precmd" trong gate |
| field | `ch.b_AString` | `smsNumbers` | HIGH | "number" |
| field | `ch.c_AString` | `smsMessages` | HIGH | "msg"; số phần tử = số tin cần gửi |
| field | `ch.b_B` | `sentCount` | HIGH | "Đã gửi N tin" |
| field | `ch.b_String` | `gameIdProp` | HIGH | JAD "gid" (mặc định "100") |
| field | `ch.c_String` | `channelIdProp` | HIGH | JAD "cid" ("18") |
| field | `ch.d_String` | `productIdProp` | HIGH | JAD "pid" ("01") |
| field | `ch.c_B` | `state` | HIGH | 1 mời bốc thăm, 3 đang lấy, 6 xác nhận SMS, 7 đang gửi, 9 thành công, 10/11 lỗi, 12 không SIM, 13 miễn phí |
| field | `ch.a_Z` | `lotteryWon` | HIGH | "lotteryResult" |
| field | `ch.e_String` | `lotteryMsg` | HIGH | "lotteryMsg" |
| field | `ch.f_String` | `msgLotteryPending` | HIGH | "Lấy ưu đãi chưa xong..." |
| field | `ch.g_String` | `msgLotteryFailed` | HIGH | "Lấy ưu đãi qua mạng thất bại..." |
| field | `ch.h_String` | `msgRetryFailed` | HIGH | "Thử lại thất bại..." |
| field | `ch.i_String` | `msgPaymentIncomplete` | HIGH | "Bạn chưa hoàn tất thanh toán..." |
| field | `ch.j_String` | `msgSuccess` | HIGH | "Chúc mừng bạn mua thành công..." |
| field | `ch.a_cx` | `resultListener` | HIGH | a(0,"") thành công, a(1,"Người dùng hủy...") |
| field | `ch.d_AString` | `smsContents` | HIGH | tham số 1 của a(String[],...) |
| field | `ch.a_AB` | `scrollOffsets` | HIGH | dòng cuộn theo state (14) |
| field | `ch.c_S` | `visibleLines` | HIGH | (cao - 62)/cg.b_S |
| field | `ch.e_AString` | `wrappedLines` | HIGH | cg.a(text,"\n",w) |
| field | `ch.a_I` | `retryCount` | HIGH | >0 -> msgRetryFailed |
| field | `ch.a_f` | `lotteryRequest` | HIGH | new f(this) |
| field | `ch.b_I` | `textColor` | HIGH | 16777215 |
| field | `ch.d_I` | `disabledColor` | HIGH | 7368816 mũi tên/OK mờ |
| field | `ch.b_Z` | `showScrollHint` | HIGH | "Hãy cuộn xuống đọc hết rồi mới bấm OK" |
| field | `ch.k_String` | `scrollHint` | HIGH | chuỗi trên |
| field | `ch.f_AString` | `scrollHintLines` | HIGH | cg.a(k_String,..) |
| field | `ch.l_String` | `productId` | HIGH | khoá đếm số tin đã gửi (sendtimes) |
| field | `ch.g_AString` | `sentProductIds` | HIGH | RMS "sendtimes" |
| field | `ch.b_AB` | `sentCounts` | HIGH | RMS "sendtimes" |
| field | `ch.m_String` | `GATE_STORE` | HIGH | "onegateinfo" |
| field | `ch.n_String` | `SENDTIMES_STORE` | HIGH | "sendtimes" |
| field | `ch.a_Image` | `topbarLogo` | HIGH | /ui/topbar_logo.gf |
| field | `ch.b_Image` | `topbarBack` | HIGH | /ui/topbar_back.gf |
| field | `ch.c_Image` | `topbarUrl` | HIGH | /ui/topbar_url.gf |
| field | `ch.d_Image` | `bottomBack` | HIGH | /ui/bottom_back.gf |
| field | `ch.d_B` | `topBarHeight` | HIGH | topbarBack h+2 hoặc 31 |
| field | `ch.o_String` | `gateInfo` | HIGH | khối gateN từ /ui/gate.bin hoặc RMS/lottery |
| field | `ch.a_AAString` | `smscAreaTable` | HIGH | {"Bắc Kinh","1380010"}... tỉnh -> tiền tố SMSC |
| field | `ch.p_String` | `smscNumber` | HIGH | wireless.messaging.sms.smsc |
| field | `ch.q_String` | `areaName` | HIGH | tỉnh/"Telecom"/"Không rõ" |
| field | `ch.e_I` | `needBuyAmount` | HIGH | "needBuyAmount" |
| field | `ch.f_I` | `handselAmount` | HIGH | "handselAmount" (tặng) |
| field | `ch.g_I` | `boughtCount` | HIGH | đã mua (handselRecord) |
| field | `ch.h_I` | `handselCount` | HIGH | đã nhận tặng |
| hàm | `ch.a_Z()` | `detectArea` | HIGH | "Xác định vùng của máy" |
| hàm | `ch.b_Z()` | `isLotteryToday` | HIGH | RMS "lotteryDate" == hôm nay |
| hàm | `ch.a_Z(int)` | `loadGate` | HIGH | RMS onegateinfo / /ui/gate.bin; precmd/number/msg/price |
| hàm | `ch.a_I(String)` | `getSentCount` | HIGH | sendtimes[productId] |
| hàm | `ch.a_V(int)` | `handleKey` | HIGH | df: a_ch.a_V(key) |
| hàm | `ch.a(Graphics)` | `draw` | HIGH | "Thông tin thanh toán" |
| hàm | `ch.a(Graphics,String,boolean)` | `drawScrollText` | HIGH | ngắt dòng + mũi tên cuộn |
| hàm | `ch.a_V()` | `loadHandselRecord` | HIGH | RMS "handselRecord" |
| hàm | `ch.b_V()` | `loadSendTimes` | HIGH | RMS "sendtimes" |
| hàm | `ch.c()` | `saveHandselRecord` | HIGH | RMS "handselRecord" |
| hàm | `ch.a(String,int)` | `saveSentCount` | HIGH | RMS "sendtimes" |
| hàm | `ch.a(int,int)` | `setScreenSize` | HIGH | a_S/b_S |
| hàm | `ch.a(String,String,String,String,String,cx)` | `startPayment` | HIGH | bọc 1 tin |
| hàm | `ch.a(String[],String[],String[],String,String,cx)` | `startPayments` | HIGH | state 6 hoặc 12 (không SIM) |
| hàm | `ch.a_V(String)` | `setGateInfo` | HIGH | f: ghi o_String |
| hàm | `ch.a_Z(ch,int)` | `loadGateOf` | HIGH | accessor cho f |
| hàm | `ch.a_String(ch)` | `productIdOf` | HIGH | accessor |
| hàm | `ch.a_I(ch,String)` | `sentCountOf` | HIGH | accessor |
| hàm | `ch.a(ch,byte)` | `setSentCountOf` | HIGH | accessor |
| hàm | `ch.a_V(ch)` | `saveGateInfo` | HIGH | RMS "onegateinfo" |
| hàm | `ch.a_String()` | `getGateInfo` | HIGH | o_String |
| hàm | `ch.a(ch,boolean)` | `setLotteryWon` | HIGH | accessor |
| hàm | `ch.a_V(ch,String)` | `setLotteryMsg` | HIGH | accessor |
| hàm | `ch.a_Z(ch)` | `isLotteryWon` | HIGH | accessor |
| hàm | `ch.a_V(ch,int)` | `setNeedBuyAmount` | HIGH | accessor |
| hàm | `ch.b(ch,int)` | `setHandselAmount` | HIGH | accessor |
| hàm | `ch.c(ch,int)` | `resetBoughtCount` | HIGH | gán 0, bỏ qua tham số |
| hàm | `ch.d(ch,int)` | `resetHandselCount` | HIGH | gán 0, bỏ qua tham số |
| hàm | `ch.b(ch)` | `saveLotteryDate` | HIGH | RMS "lotteryDate" y/m/d |
| hàm | `ch.c(ch)` | `saveHandselRecordOf` | HIGH | accessor c() |
| hàm | `ch.a(byte)` | `setState` | HIGH | c_B |

### `df` → **PaidServiceMenu** (HIGH) — "Dịch vụ ưu đãi": 'Vô Song Bí Kỹ','Tiệm Bí Bảo','Siêu trang bị','Nhận thưởng'; /bin/smsmenu.bin; mua KHÔNG qua SMS

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `df.a_B` | `page` | HIGH | 0 menu chính,1 danh sách,2 xác nhận,3 thành công,5 chọn cách trả,7 ch,8 thưởng 12 tệ |
| field | `df.a_AI` | `itemFeatureIds` | HIGH | cột 0 smsLib: 1 vàng,2 EXP x4,3 +5 cấp,4 biến thân,5 lễ bao; 6 bản chính,7 hồi phục,8 hạ gục (bn) |
| field | `df.c_AString` | `confirmNotes` | HIGH | khối "sureNote:" |
| field | `df.d_AString` | `itemNames` | HIGH | cột 1 smsLib |
| field | `df.e_AString` | `successMessages` | HIGH | cột 2 smsLib |
| field | `df.a_Z` | `rewardClaimed` | HIGH | "Nhận thưởng" 12 tệ; o lưu |
| field | `df.b_B` | `itemIndex` | HIGH | bn ghi df.a_AI[a_df.b_B] |
| field | `df.d_B` | `mainMenuIndex` | HIGH | f_AString |
| field | `df.e_B` | `payMethodIndex` | HIGH | "Thanh toán qua ĐT","Trả bằng Xu Tiêu Dao" |
| field | `df.a_ch` | `smsFlow` | HIGH | ch, không bao giờ gán (null) |
| field | `df.a_dc` | `textBox` | HIGH | dc (TextBlock) |
| field | `df.f_AString` | `mainMenuItems` | HIGH | 'Vô Song Bí Kỹ','Tiệm Bí Bảo','Siêu trang bị','Nhận thưởng' |
| field | `df.a_a` | `smsFeeConfig` | MED | kiểu a; không bao giờ gán |
| field | `df.b_Z` | `hasLuckyPack` | HIGH | id 5 "Siêu Lễ Bao": ab:1686 bỏ giới hạn cấp trang bị, bn rơi đồ x2, o:2689 không mất vàng |
| field | `df.c_B` | `expMultiplier` | HIGH | id 2 -> 4; bn: "x EXP", var3 *= c_B |
| field | `df.c_Z` | `hasTransform` | HIGH | id 4 "Ma pháp biến thân" (bay) |
| field | `df.d_Z` | `isFullVersion` | HIGH | id 6 "Kích hoạt bản chính hãng"; bn/ci chặn cốt truyện khi false |
| field | `df.a_I` | `totalSpent` | HIGH | tệ đã tiêu (+2/+4); >=12 nhận thưởng; o lưu |
| field | `df.b_I` | `pendingFeatureId` | HIGH | b(int) đặt, a() áp dụng |
| hàm | `df.a(String)` | `open` | HIGH | df.a("/bin/smsmenu.bin"): smsLib/sureNote, cd.b_B=7 |
| hàm | `df.a(Graphics)` | `draw` | HIGH | "Dịch vụ ưu đãi" |
| hàm | `df.a(int)` | `handleKey` | HIGH | cd gọi a_df.a(key) |
| hàm | `df.b(int)` | `purchase` | HIGH | ae/bn gọi b(6/7/8); gọi thẳng this.a(0,null) = thành công |
| hàm | `df.a()` | `applyFeature` | HIGH | 1 +8888 vàng,2 EXP x4,3 +5 cấp,4 biến thân,5 lễ bao,6 bản chính,7 hồi phục,8 hạ gục; o.b(o.r_String) lưu |
| hàm | `df.b()` | `close` | HIGH | dọn mảng, cd.b_B = 1 hoặc 5 |
| hàm | `df.a(int,int)` | `handlePointer` | HIGH | i.a(x,y) -> nút mềm |

## Cụm `ui_ab`


### `cx` → **ResultListener** (HIGH) — interface a(int,String); implemented by as, ch, df; called by ba (OK), bh (thread), ch (SMS result)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| hàm | `cx.a(int,String)` | `onResult` | HIGH | ch: a(1,"Không có SIM"), a(1,"Người dùng hủy thanh toán"), a(0,""); df: 0 -> buy ok else "Mua thất bại"; as: 0 -> rename pet |

### `ba` → **TextInputDialog** (HIGH) — CommandListener with Form+TextField, OK/"Trở về"; built in ab.g for "Đổi tên sủng vật"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ba.a_TextField` | `textField` | HIGH | getString() passed to listener on OK |
| field | `ba.a_Form` | `form` | HIGH | new Form(title); append(textField); setCommandListener(this) |
| field | `ba.a_cx` | `listener` | HIGH | a_cx.a(0, text) on OK |

### `bh` → **SmsSendTask** (MED) — only caller ch (SMS payment: smsc, precmd/number/msg keys); Runnable that just reports success

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bh.a_String` | `smsText` | MED | 1st arg of bh.a: ch passes precmd+cmd / cmd string ("msg" side); never read (stub) |
| field | `bh.b_String` | `phoneNumber` | MED | 2nd arg: ch b_AString = cg "number" key; never read (stub) |
| field | `bh.a_cx` | `listener` | HIGH | run(): a_cx.a(0,null) |
| hàm | `bh.a(String,String,cx)` | `send` | MED | static: new bh, fill fields, new Thread(it).start() |

### `av` → **LogoScreen** (HIGH) — Main.showLogo: new av, a_B=2; plays cq logo list (.av/.png/.gf) then music prompt "Bật âm nhạc?"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `av.a_B` | `mode` | HIGH | 2 = logo sequence (Main.showLogo), 1 = music on/off prompt (da key handler checks a_B==1) |
| field | `av.a_Image` | `logoImage` | HIGH | cc.a(logo .png/.gf) drawn centered |
| field | `av.a_bx` | `logoAnim` | HIGH | new bx().a(logo .av) |
| field | `av.a_AZ` | `logoLoaded` | HIGH | per-logo "already loaded" flags (length = logo count) |
| field | `av.b_B` | `logoIndex` | HIGH | index into logo list, ++ when one finishes |
| field | `av.a_AString` | `logoFiles` | HIGH | = cq.a_AString, file names checked for .av/.png/.gf |
| field | `av.a_AI` | `logoBackColors` | MED | = cq.a_AI, fill color behind .av logo |
| field | `av.a_AS` | `logoDurations` | MED | = cq.a_AS (+2000 default via cg.a_AS), ms compared with currentTimeMillis-a_J |
| field | `av.a_J` | `logoStartTime` | HIGH | System.currentTimeMillis() at logo start |
| field | `av.a_Z` | `isFinished` | HIGH | true after last logo / after prompt answered (da sets it) |
| field | `av.b_Image` | `musicIcon` | HIGH | cc.a("/pic/musicIcon.gf") |
| hàm | `av.a(Graphics)` | `draw` | HIGH | da paint: a_av.a(g) |

### `bs` → **TitleScreen** (HIGH) — cover menu "Chơi mới/Chơi tiếp/Cài đặt/Hướng dẫn/Giới thiệu/Thoát game"; Main.showCover

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bs.a_B` | `page` | HIGH | 0 loading, 6 start anim, 1 main menu, 2 help, 3 music volume, 4 about, 5 load slot, 7 recommended games, 8 QQ center confirm |
| field | `bs.b_B` | `menuIndex` | HIGH | selected menu item (left/right keys wrap) |
| field | `bs.a_AString` | `menuItems` | HIGH | {"Chơi mới","Chơi tiếp",...} |
| field | `bs.e_AString` | `shownMenuItems` | MED | = a_AString after load; drawn/compared |
| field | `bs.c_B` | `loadTargetPercent` | HIGH | set 20/70/90/100 by loader b() |
| field | `bs.d_B` | `loadPercent` | HIGH | displayed %, ++ towards c_B; text "Đang tải dữ liệu..." + progress bar |
| field | `bs.a_Z` | `isLoaded` | HIGH | set by ce after b() returns |
| field | `bs.c_Z` | `isEnteringGame` | MED | set when a save is chosen ("Chơi tiếp"), blocks further keys |
| field | `bs.a_bx` | `startAnim` | HIGH | "/pic/start/start.av" |
| field | `bs.b_bx` | `logoAnim` | HIGH | "/pic/start/logo.av" |
| field | `bs.c_bx` | `loadingAnim` | HIGH | "/av/long1.av" moved along the progress bar |
| field | `bs.d_bx` | `volumeAnim` | HIGH | "/pic/D41.av" on settings page, frame 1 = on, 0 = off |
| field | `bs.a_Image` | `backgroundImage` | HIGH | "/pic/menuBack.jpg" |
| field | `bs.b_Image` | `menuLabelImage` | MED | "/pic/hk2.gf" path dropped, always null -> image label branch dead |
| field | `bs.c_Image` | `menuArrowsImage` | HIGH | "/pic/D04.gf" = yellow left/right arrows around menu label |
| field | `bs.d_Image` | `topBarImage` | LOW | "/pic/topbj.gf" loaded, never drawn |
| field | `bs.e_Image` | `levelLabelImage` | HIGH | "/pic/D08.gf" = "Lv" label on save slot |
| field | `bs.f_Image` | `softKeyIcons` | HIGH | "/pic/D23.gf" = tick/cross soft key icons |
| field | `bs.g_Image` | `digitsImage` | HIGH | "/pic/number01.gf", cg number drawing 6x7 digits |
| field | `bs.h_Image` | `heroNamesImage` | HIGH | "/pic/D09.gf" = "Quinn/Julie" (fallback strings) |
| field | `bs.i_Image` | `cursorImage` | HIGH | "/pic/arrow.gf" 15x15 drawn at selected slot |
| field | `bs.j_Image` | `musicIcon` | HIGH | "/pic/musicIcon.gf" on settings page |
| field | `bs.k_Image` | `volumeArrowsImage` | HIGH | "/pic/D11.gf" small arrows beside volume value |
| field | `bs.e_B` | `blinkFrame` | LOW | toggled 0/2 every 3 frames, never read |
| field | `bs.a_I` | `cursorBob` | HIGH | 3 every 12th frame else 0, offsets arrows/cursor |
| field | `bs.a_String` | `helpText` | MED | about.bin section "help:".."helpEnd" (temp, nulled) |
| field | `bs.b_AString` | `helpLines` | HIGH | wrapped help text drawn on page 2 |
| field | `bs.c_AString` | `aboutLines` | HIGH | wrapped cq.a_String drawn on page 4 ("Giới thiệu") |
| field | `bs.a_S` | `helpScrollY` | HIGH | +-cg.b_S on up/down in page 2 |
| field | `bs.b_Z` | `hasSave` | HIGH | RecordStore names o.n/o/p_String found |
| field | `bs.a_AB` | `saveSlots` | HIGH | slot numbers 1..3 present in RMS |
| field | `bs.d_AString` | `saveSlotNames` | HIGH | "Tiến độ 1/2/3", passed to cd.a(name) |
| field | `bs.f_B` | `slotIndex` | HIGH | selected slot on page 5 |
| field | `bs.a_Abx` | `slotHeroAnims` | HIGH | "/av/chr"+o.j_AS[i]+".av" per slot |
| field | `bs.f_AString` | `recommendedGames` | HIGH | "Tải {Linh Thú Chi Vương}"... from Tj<n>Url |
| field | `bs.g_AString` | `recommendedUrls` | HIGH | getAppProperty("Tj"+n+"Url"), platformRequest |
| field | `bs.g_B` | `recommendedIndex` | HIGH | selected entry page 7 |
| field | `bs.h_B` | `recommendedScroll` | HIGH | first visible entry page 7 |
| field | `bs.b_I` | `recommendedRows` | HIGH | 188/cg.b_S visible rows |
| field | `bs.a_dc` | `noticeBox` | MED | dc text box with fee notice "Lưu ý: tải miễn phí..." |
| hàm | `bs.a()` | `startLoading` | HIGH | page=0; new ce(this)  (Main.showCover) |
| hàm | `bs.b()` | `loadResources` | HIGH | run by ce thread: images, data, about.bin, save list, progress % |
| hàm | `bs.c()` | `releaseResources` | HIGH | null anims/images before game |
| hàm | `bs.a(Graphics)` | `draw` | HIGH | da paint state 3 |
| hàm | `bs.a(int)` | `handleKey` | HIGH | da keyPressed state 3 |
| hàm | `bs.a(int,int)` | `handlePointer` | HIGH | da pointerPressed; soft-key 30x30 hit boxes |
| hàm | `bs.b(Graphics)` | `drawPanelBackground` | HIGH | background + menu label + 176x208 framed panel + soft key icons |

### `ce` → **TitleLoader** (HIGH) — Runnable started in ctor: bs.b(); bs.a_Z=true

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ce.a_Thread` | `thread` | HIGH | new Thread(this).start() |
| field | `ce.a_bs` | `titleScreen` | HIGH | target bs |

### `ab` → **MenuList** (MED) — instance: list of bg columns with shared cursor/scrollbar (as/bn); statics: in-game menu key dispatcher

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ab.a_Vector` | `columns` | HIGH | Vector of bg, addElement in a(bg) |
| field | `ab.a_AI` | `rowIds` | HIGH | as: skill/item id per row; read by a_I(int) |
| field | `ab.a_I` | `x` | HIGH | ctor arg 1, scrollbar x = a_I+c_I-15 |
| field | `ab.b_I` | `y` | HIGH | ctor arg 2 |
| field | `ab.c_I` | `w` | HIGH | ctor arg 3 |
| field | `ab.d_I` | `h` | HIGH | ctor arg 4 |
| field | `ab.a_Image` | `cursorImage` | HIGH | a(Image,int); drawRegion 15x15 at selected row |
| field | `ab.e_I` | `cursorX` | HIGH | a(Image,int) 2nd arg; cursor x = e_I+8+bob |
| field | `ab.f_I` | `cursorBob` | HIGH | 3 every 12th frame else 0 |
| hàm | `ab.a(Image,int)` | `setCursor` | HIGH | sets cursor image + x |
| hàm | `ab.a_I(int)` | `getRowId` | HIGH | return a_AI[row]; callers o.c_I(...) lookup |
| hàm | `ab.a_I()` | `getColumnCount` | HIGH | a_Vector.size() |
| hàm | `ab.b_I()` | `getSelectedRow` | HIGH | column 0 bg.c() (selected index) |
| hàm | `ab.a(bg)` | `addColumn` | HIGH | a_Vector.addElement |
| hàm | `ab.a_V()` | `clearColumns` | HIGH | bg.a_V() on each then removeAllElements |
| hàm | `ab.b_V()` | `selectPrev` | HIGH | bg.f() on each (key up) |
| hàm | `ab.c_V()` | `selectNext` | HIGH | bg.e() on each (key down) |
| hàm | `ab.a(Graphics)` | `draw` | HIGH | scrollbar k.a + columns + cursor |
| hàm | `ab.b(int)` | `resetSelection` | HIGH | bg.a(0) on each column (param unused) |
| hàm | `ab.d()` | `scrollToTop` | HIGH | bg.b(0) -> h_I=0 |
| hàm | `ab.a_V(int)` | `handleMenuKey` | HIGH | cd.c: b_B==4 -> ab.a_V(key); switch on as.o_B page |
| hàm | `ab.a_V(int,int)` | `handleMenuPointer` | HIGH | cd pointer: b_B==4 -> ab.a_V(x,y); soft-key boxes -> handleMenuKey |
| hàm | `ab.a(int,int,int,int,int,int)` | `isInButton` | HIGH | px,py inside 30x30 box at x,y (w,h ignored); used by bs, bn, cd |
| hàm | `ab.a_Z()` | `closeMenu` | HIGH | as.e(), a_as=null, cd.b_B=1; also o "Cuộn hồi thành" |
| hàm | `ab.c(int)` | `handleShopBuyKey` | HIGH | page 15 B_B==1: quantity 1..99, "Không đủ vàng", "Mua "+name |
| hàm | `ab.b_Z()` | `canLearnerLearnSkill` | HIGH | d.a_AAAS[skill][i][0]==as.c_I; else "Nghề này không thể học kỹ năng này" |
| hàm | `ab.a_Z(int,int)` | `meetsSkillLevel` | HIGH | level req vs hero/pet level; "không đủ cấp yêu cầu" |
| hàm | `ab.a(int,int,int)` | `hasLearnedSkill` | HIGH | skill id already in a_Ad; "Đã học kỹ năng này" |
| hàm | `ab.d(int)` | `handlePetBookKey` | MED | page 8: petdata.bin, 3 tabs (b_I%3), pet synthesis "Không đủ nguyên liệu" |
| hàm | `ab.e(int)` | `handleQuestPageKey` | HIGH | page 7: "nhiệm vụ chính tuyến/phụ tuyến" |
| hàm | `ab.a(short[][][],int,int,int)` | `removeBagItems` | HIGH | o.c(id,n); decrement count or remove slot |
| hàm | `ab.f(int)` | `handleEquipPageKey` | HIGH | page 5: "Vũ khí/Mũ/Áo/Giày/Dây chuyền/Nhẫn", Thay/Tháo/Bỏ/Khảm |
| hàm | `ab.a(bl,int,int)` | `equipFromBag` | HIGH | class check "Nhân vật không phù hợp", level "Cấp không đủ!", swap into slot |
| hàm | `ab.a(bl,int)` | `applySetBonus` | MED | piece count >=2/4/6 adds stats by job h_S |
| hàm | `ab.g(int)` | `handlePetPageKey` | HIGH | page 3: "Thuộc tính/Kỹ năng/Xuất chiến/Thả/Đổi tên/Cấy/Tinh luyện" |
| hàm | `ab.e()` | `loadSaveSlotInfo` | HIGH | RecordStore o.m_String -> o.a_Z(); slot hero anims |
| hàm | `ab.h(int)` | `handleSystemPageKey` | HIGH | page 9: "Lưu game/Tải game/Cài đặt nhạc/Thoát game" (Patch hook ab.h(I)V) |
| hàm | `ab.c_Z()` | `saveToSelectedSlot` | HIGH | o.a_Z(as.l_B) + o.b(as.l_B); "Lưu thành công" |

## Cụm `ui_as`


### `as` → **GameMenu** (HIGH) — tab 'Trạng thái','Sủng vật'...'Hệ thống' (de), paint dispatch theo as.o_B, cd.a_as

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `as.o_B` | `menuPage` | HIGH | 1 tab bar, 2..9 = tab index+2 (ab.java:41), 12/13/14/15 trang phụ; Patch/SfxUi 'as.o' group==9 hệ thống |
| field | `as.p_B` | `systemPage` | HIGH | trang của tab Hệ thống: 0 menu,1 Lưu,2 Tải,3 âm lượng (SfxUi PAGE_VOLUME),4 'Trở về menu chính?',5 xác nhận ghi/tải,6 'Tải điểm QQ' |
| field | `as.q_B` | `petPage` | HIGH | tab Sủng vật: 0 list,1 menu thao tác,2 Thuộc tính,3 'Chắc chắn thả',4 thông báo,5 Kỹ năng,7 Cấy,8 Tinh luyện (ab.g) |
| field | `as.t_B` | `equipPage` | HIGH | tab Trang bị: 0 ô trang bị,1 túi trang bị,2 menu Thay/Tháo/Bỏ/Khảm,3 khảm đá quý (ab.f) |
| field | `as.w_B` | `questPage` | HIGH | tab Nhiệm vụ: 0 chọn chính/phụ tuyến,1 list chính,2 list phụ,3 chi tiết,4 menu Xem/Bỏ,5 'Xóa nhiệm vụ đang chọn?' (ab.e) |
| field | `as.z_B` | `dexPage` | HIGH | tab Đồ giám: 0 danh sách,1 chi tiết pet,2 hợp thành (ab.d, 'Đang hợp thành') |
| field | `as.v_B` | `bagPage` | HIGH | tab Túi đồ: 0 chọn loại,1 lưới,2 menu Dùng/Bán/Bỏ,3 bán (số lượng+giá),4 bỏ (số lượng) |
| field | `as.x_B` | `skillPage` | HIGH | tab Kỹ năng: 0 danh sách,1 mô tả kỹ năng (ab.a_V o_B==4) |
| field | `as.s_B` | `petSkillPage` | MED | q_B==5: 0 danh sách kỹ năng pet,1 mô tả |
| field | `as.A_B` | `skillShopPage` | HIGH | o_B 14 'Tiệm kỹ năng': 0 list,1 chọn người học,2 'Xác nhận học kỹ năng' |
| field | `as.B_B` | `shopPage` | HIGH | o_B 15: 0 list,1 hộp số lượng mua (ab.c) |
| field | `as.u_B` | `socketResult` | MED | 0 đang chọn đá, 1 hiện 'Chúc mừng‚khảm trang bị thành công' |
| field | `as.y_B` | `shopType` | HIGH | 0 itemShop,1 equipShop,2 skillShop (as.b(int) /bin/shop.bin) |
| field | `as.D_B` | `saveConfirmMode` | HIGH | 1 xác nhận ghi đè (Lưu), 2 xác nhận tải; quay lại p_B = D_B |
| field | `as.a_B` | `petActionIndex` | HIGH | con trỏ menu thao tác pet (e_AString[a_B]) |
| field | `as.b_B` | `equipSlotIndex` | HIGH | 0..5 ô 'Vũ khí','Mũ','Áo','Giày','Dây chuyền','Nhẫn', 6..11 món bộ trang bị |
| field | `as.a_S` | `equipBagIndex` | HIGH | con trỏ trong lưới trang bị cùng loại o.a_AAck[b_B][a_S] |
| field | `as.c_B` | `equipBagScrollRow` | HIGH | hàng cuộn lưới trang bị (a_S - c_B*f_S) |
| field | `as.d_B` | `equipActionIndex` | HIGH | con trỏ d_AString 'Thay','Tháo','Bỏ','Khảm' |
| field | `as.e_B` | `bagTabIndex` | HIGH | 0 'Vật phẩm',1 'Nguyên liệu',2 'Khác' (j_AString) |
| field | `as.f_B` | `bagScrollRow` | HIGH | hàng cuộn lưới túi đồ |
| field | `as.g_B` | `bagItemIndex` | HIGH | ô đang chọn a_AAAS[e_B][g_B] |
| field | `as.h_B` | `bagActionIndex` | HIGH | con trỏ c_AString 'Dùng','Bán','Bỏ' |
| field | `as.i_B` | `questTabIndex` | HIGH | 0 'NV chính tuyến',1 'NV phụ tuyến' |
| field | `as.k_B` | `questActionIndex` | HIGH | menu {15 'Xem',16 'Bỏ'} của nhiệm vụ phụ |
| field | `as.l_B` | `saveSlotIndex` | HIGH | ô ghi đang chọn (< at.e_B), o.a_Z(l_B) |
| field | `as.m_B` | `useTargetIndex` | HIGH | o_B 13: 0 nhân vật chính, 1.. pet a_Acv[m_B-1] |
| field | `as.n_B` | `useTargetScroll` | HIGH | dòng đầu danh sách o_B 13 |
| field | `as.C_B` | `useTargetRows` | MED | số dòng hiển thị o_B 13 (= chiều cao/2 dòng chữ) |
| field | `as.a_I` | `dexPetIndex` | HIGH | = petId-1, chỉ số dòng petdata đang xem |
| field | `as.a_AString` | `systemMenuRows` | HIGH | 'Dịch vụ ưu đãi','Lưu game','Tải game','Cài đặt nhạc'...; Patch hook putstatic as.a:[String |
| field | `as.a_bl` | `hero` | HIGH | = o.a_Abl[0] nhân vật chính (ae/cd gán) |
| field | `as.a_Acv` | `pets` | HIGH | = k.a_Acv() danh sách sủng vật; 'Không có sủng vật' |
| field | `as.c_I` | `skillLearnerType` | MED | cd.a('技能学习',shop,type): 1/2 nhân vật, 4 pet; 'Nghề này không thể học kỹ năng này' |
| field | `as.l_I` | `learnerRows` | MED | số dòng hiển thị danh sách người học (d(Graphics)) |
| field | `as.m_I` | `learnerIndex` | HIGH | người học đang chọn (a_Acv[m_I] khi c_I==4) |
| field | `as.n_I` | `learnerScroll` | HIGH | dòng đầu danh sách người học |
| field | `as.k_I` | `buyCount` | HIGH | số lượng mua 1..99 ('Mua '+tên+k_I) |
| field | `as.a_AS` | `shopItemIds` | HIGH | id hàng của tiệm, đọc từ /bin/shop.bin itemShopN/equipShopN/skillShopN |
| field | `as.a_AAS` | `gems` | HIGH | [itemId,count] đá quý khảm được ('Không có đá quý để chọn') |
| field | `as.d_I` | `gemIndex` | HIGH | con trỏ danh sách đá quý |
| field | `as.e_I` | `gemScroll` | HIGH | dòng đầu danh sách đá quý |
| field | `as.f_I` | `gemRows` | HIGH | số dòng đá quý hiển thị (=3) |
| field | `as.g_I` | `selectedGemId` | HIGH | id đá quý đã chọn để khảm (o.c(g_I,1) khi khảm) |
| field | `as.g_Z` | `socketPending` | HIGH | đã bấm khảm, chờ 1000ms rồi áp dụng |
| field | `as.a_J` | `socketStartTime` | HIGH | System.currentTimeMillis() lúc bấm khảm |
| field | `as.a_AAString` | `petData` | HIGH | bảng /bin/petdata.bin (petdata..end, tab) |
| field | `as.g_AString` | `dexNames` | HIGH | tên pet theo đồ giám, '???' nếu chưa phát hiện |
| field | `as.a_AB` | `dexElements` | HIGH | 4 chữ số nguyên tố cột 2 petdata ('a'=10), vẽ quanh vòng nguyên tố |
| field | `as.b_AB` | `dexStatStars` | MED | 5 giá trị cột 4,6,8,10,12 petdata, vẽ bằng star.gf (nửa sao) |
| field | `as.b_S` | `heroSkillCount` | HIGH | = a_bl.a_Ad.length; 0 -> quay về thanh tab |
| field | `as.b_AString` | `questNames` | MED | tên nhiệm vụ gom lại khi dựng list (ab xoá phần tử khi bỏ NV) |
| field | `as.c_S` | `bagCols` | HIGH | lưới túi đồ 7 cột (25px) |
| field | `as.d_S` | `bagRows` | HIGH | lưới túi đồ 5 hàng |
| field | `as.a_AAAS` | `bagItems` | HIGH | [loại 0..2][i][itemId,count] dựng bởi j() theo o.f_AS |
| field | `as.c_AString` | `bagActionNames` | HIGH | {'Dùng','Bán','Bỏ'} / {'Bán','Bỏ'} |
| field | `as.b_AI` | `bagActionIds` | HIGH | chỉ số nhãn trong actionLabels {13,14,12} |
| field | `as.d_AString` | `equipActionNames` | HIGH | {'Thay','Tháo','Bỏ','Khảm'}... |
| field | `as.c_AI` | `equipActionIds` | HIGH | {10,9,12,11} |
| field | `as.e_AString` | `petActionNames` | HIGH | {'Thuộc tính','Kỹ năng','Xuất chiến'/'Nghỉ','Thả','Đổi tên','Cấy','Tinh luyện'} |
| field | `as.d_AI` | `petActionIds` | HIGH | {0,1,2,4,5,6,7}, [2]=2/3 Xuất chiến/Nghỉ |
| field | `as.e_S` | `bagQuantity` | HIGH | số lượng bán/bỏ (1..count) |
| field | `as.f_S` | `gridCols` | HIGH | lưới trang bị/nguyên liệu cấy 6 cột (26px) |
| field | `as.g_S` | `gridRows` | HIGH | lưới 2 hàng |
| field | `as.b_AS` | `setItemIds` | HIGH | 6 id trang bị bộ theo nghề (a_bl.h_S), 'DEF+300 (2 món)' |
| field | `as.q_I` | `setPiecesWorn` | HIGH | số món bộ đang mặc (>=2/4/6 kích hoạt thưởng) |
| field | `as.b_String` | `petMessage` | MED | thông báo q_B==4 ('Số sủng vật xuất chiến đã đủ!...') |
| field | `as.a_String` | `confirmText` | HIGH | 'Xác nhận ghi đè bản lưu hiện tại?' / 'Xác nhận tải...' (p_B==5) |
| field | `as.d_bx` | `elementWheelAnim` | MED | /pic/D01.av vẽ giữa các số nguyên tố (drawElements) |
| field | `as.b_Abx` | `setGlowAnims` | HIGH | 6 x /pic/equipstar/tbss.av, hiện trên món bộ đang mặc |
| field | `as.c_bx` | `previewAnim` | HIGH | hoạt hình nhân vật ('...s.av') hoặc pet (k.a(id)) đang xem |
| field | `as.a_Abx` | `saveSlotAnims` | HIGH | /av/chr<j>.av cho mỗi bản lưu (ab.e) |
| field | `as.a_bx` | `petAnim` | HIGH | hoạt hình pet đang chọn trong list (k.a(a_Acv[..].a_I)) |
| field | `as.b_bx` | `speakerAnim` | HIGH | /pic/D41.av biểu tượng loa trang âm lượng (Patch: as.b:bx, sfx$icon) |
| field | `as.e_bx` | `dexFilterCursorAnim` | MED | /pic/D24.av vẽ trên tab lọc đồ giám b_I |
| field | `as.f_bx` | `heroFigureAnim` | HIGH | /ft/chr<h_S>.av hình nhân vật ở tab Trang bị |
| field | `as.g_bx` | `implantFxLeft` | HIGH | /pic/uiav/D28.av hiệu ứng cấy ô 0 |
| field | `as.h_bx` | `implantFxRight` | HIGH | D28.av lật (a_Z=true) hiệu ứng cấy ô 1 |
| field | `as.a_dc` | `textBox` | HIGH | dc dùng chung vẽ mô tả |
| field | `as.a_bf` | `sideQuestList` | HIGH | bf: nhiệm vụ loại 2/3 (phụ tuyến) |
| field | `as.a_cy` | `mainQuestList` | HIGH | cy: nhiệm vụ loại 1 (chính tuyến) |
| field | `as.a_cs` | `questDetail` | HIGH | cs 'Mô tả nhiệm vụ'/'Thưởng nhiệm vụ' |
| field | `as.a_de` | `tabBar` | HIGH | de 8 tab |
| field | `as.a_db` | `fusionMaterialList` | HIGH | db nguyên liệu hợp thành (m()) |
| field | `as.a_bc` | `formList` | HIGH | bc o_B 12 'Sẽ đổi thành hình dạng sau' |
| field | `as.b_bc` | `dexAllList` | HIGH | filter 2 'Tất cả' (140 pet, '???') |
| field | `as.c_bc` | `dexElementList` | HIGH | filter 1 'Nguyên tố' (pet có nguyên tố >5) |
| field | `as.d_bc` | `dexFoundList` | HIGH | filter 0 'Mới nhất' (pet đã phát hiện o.p_AS) |
| field | `as.b_AAS` | `fusionRecipe` | HIGH | [itemId,count] cột 19 petdata, ak.a(...,'Hợp thành') |
| field | `as.i_I` | `dexPetId` | HIGH | pet id đang xem; o.a_V(i_I,-1) thêm pet khi hợp thành xong |
| field | `as.a_AI` | `tabBarBounds` | HIGH | = a_de.a() {x,y,w,h}, gốc toạ độ mọi trang |
| field | `as.a_ab` | `skillShopList` | HIGH | ab của o_B 14 |
| field | `as.b_ab` | `shopList` | HIGH | ab của o_B 15 |
| field | `as.c_ab` | `systemMenuList` | HIGH | ab dòng menu Hệ thống (p()) |
| field | `as.d_ab` | `heroSkillList` | HIGH | ab kỹ năng nhân vật (o()) |
| field | `as.e_ab` | `petSkillList` | HIGH | ab kỹ năng pet (q_B 5) |
| field | `as.a_l` | `petList` | HIGH | l danh sách sủng vật |
| field | `as.b_l` | `partyList` | MED | l danh sách o.a_Abl (g() khi c_I!=4); không thấy được vẽ |
| field | `as.a_ck` | `shopEquip` | HIGH | ck tạo từ id món đang chọn ở tiệm trang bị |
| field | `as.r_B` | `implantStep` | HIGH | q_B 7: 0 chọn ô,1 chọn nguyên liệu,2 'Cấy ghép thành công' |
| field | `as.r_I` | `implantScrollRow` | HIGH | hàng cuộn lưới nguyên liệu cấy |
| field | `as.s_I` | `implantMaterialIndex` | HIGH | con trỏ nguyên liệu cấy (<18) |
| field | `as.t_I` | `implantSlot` | HIGH | ô cấy 0/1 (a_AAB[t_I]) |
| field | `as.a_AAB` | `implantMaterials` | HIGH | = o.a_AAB() [itemId,count]; 'Tạm không có nguyên liệu để cấy' |
| field | `as.c_Z` | `showImplantFxLeft` | HIGH | bật g_bx khi cấy ô 0 |
| field | `as.d_Z` | `showImplantFxRight` | HIGH | bật h_bx khi cấy ô 1 |
| field | `as.u_I` | `geneSlotCount` | HIGH | 1 + số ô đã cấy (q_B 2 Thuộc tính) |
| field | `as.v_I` | `geneIndex` | HIGH | con trỏ ô gen ở trang Thuộc tính pet |
| field | `as.w_I` | `statRowIndex` | HIGH | tab Trạng thái: hàng thuộc tính đang cộng điểm (0..3) |
| field | `as.e_AI` | `pendingStatPoints` | HIGH | điểm cộng tạm cho 4 thuộc tính (trừ a_bl.a_AI[16]) |
| field | `as.f_AI` | `pendingStatBonus` | HIGH | chỉ số tăng xem trước (+= a_bl.a_AAB[w_I][i]) |
| field | `as.a_Z` | `isDexFromScript` | HIGH | ci '进入图鉴' mở thẳng đồ giám; Về -> đóng menu, ci.a(5) |
| field | `as.b_Z` | `isStatsFromScript` | HIGH | ci '属性加点' mở thẳng tab Trạng thái; Về -> đóng menu |
| field | `as.e_Z` | `isFusionDone` | HIGH | o_I >= 100 |
| field | `as.f_Z` | `isFusing` | HIGH | đang chạy thanh 'Đang hợp thành' |
| field | `as.o_I` | `fusionProgress` | HIGH | 0..100, +5 mỗi khung |
| field | `as.p_I` | `questIndex` | HIGH | chỉ số o.a_Abp đang xem chi tiết |
| field | `as.E_I` | `softKeyFrame` | MED | khung ảnh D06 cho nhãn phím mềm trái (c(int)) |
| field | `as.E_B` | `cursorBob` | HIGH | 3 px mỗi 12 khung (da.a_S%12) làm mũi tên nhún |
| field | `as.h_Z` | `isToastVisible` | HIGH | hộp thông báo a(String,int) |
| field | `as.c_String` | `toastText` | HIGH | nội dung thông báo |
| field | `as.F_I` | `toastTimer` | HIGH | số khung còn hiện |
| field | `as.x_I` | `screenW` | HIGH | = at.a_AS[0]; Patch đọc as.x |
| field | `as.y_I` | `screenH` | HIGH | = at.a_AS[1]; Patch đọc as.y |
| field | `as.z_I` | `previewPage` | HIGH | trang vẽ thử khi đứng ở thanh tab (o_B==1), a(byte) |
| field | `as.A_I` | `contentW` | HIGH | 193 = rộng thanh tab/vùng nội dung |
| field | `as.B_I` | `topPanelH` | HIGH | a(int,int): chiều cao khung trên -2 |
| field | `as.C_I` | `bottomPanelH` | HIGH | a(int,int): chiều cao khung dưới -2 |
| field | `as.D_I` | `frameW` | HIGH | 191 rộng khung viền |
| field | `as.h_I` | `footerH` | HIGH | 28 dải phím mềm cuối |
| field | `as.j_I` | `bodyH` | HIGH | 200 tổng chiều cao 2 khung (a(90, j_I-90)) |
| field | `as.f_AString` | `actionLabels` | HIGH | 'Thuộc tính','Kỹ năng','Xuất chiến','Nghỉ','Thả',... chỉ số dùng bởi *ActionIds |
| field | `as.h_AString` | `dexFilterNames` | HIGH | 'Mới nhất','Nguyên tố','Tất cả' |
| field | `as.i_AString` | `questTabNames` | HIGH | 'NV chính tuyến','NV phụ tuyến' |
| field | `as.j_AString` | `bagTabNames` | HIGH | 'Vật phẩm','Nguyên liệu','Khác' |
| field | `as.a_Image` | `cursorArrow` | HIGH | /pic/arrow.gf, vẽ vùng 15x15 ở dòng chọn |
| field | `as.c_Image` | `coinIcon` | MED | /pic/D10.gf cạnh tổng giá ở hộp số lượng (cs dùng D10 cho thưởng vàng) |
| field | `as.D_Image` | `footerCoinIcon` | MED | /pic/D10.gf cạnh số vàng o.a_I ở chân menu |
| field | `as.d_Image` | `digits` | HIGH | /pic/number01.gf font số 6x7 (cg.a(...,6,7,...)) |
| field | `as.e_Image` | `equipSlotLabels` | MED | /pic/partInfo.gf 6 khung theo ô trang bị b_B |
| field | `as.f_Image` | `starImg` | HIGH | /pic/star.gf, drawStars |
| field | `as.g_Image` | `questTabsImg` | MED | /pic/taskNameArr1.gf tab NV chưa chọn |
| field | `as.i_Image` | `questTabsSelImg` | MED | /pic/taskNameArr.gf tab NV đang chọn |
| field | `as.h_Image` | `geneLabelImg` | MED | /pic/tzjy.gf (bị null), thay bằng chữ 'Gen đặc trưng:' |
| field | `as.j_Image` | `questDoneImg` | MED | /pic/D15.gf 2 khung theo o.a_Abp[].a_Z |
| field | `as.k_Image` | `bagTabsImg` | MED | /pic/goodsPc.gf tab túi đồ thường |
| field | `as.l_Image` | `bagTabsSelImg` | MED | /pic/goodsPc1.gf tab túi đồ đang chọn |
| field | `as.m_Image` | `setLabelImg` | MED | /pic/taoz.gf (套装), khung theo nghề h_S cạnh 6 món bộ |
| field | `as.n_Image` | `implantBoxBg` | MED | k.a_Image(190,90,..) nền lưới nguyên liệu cấy |
| field | `as.o_Image` | `dexHeaderImg` | MED | /pic/tjitem.gf thay chữ 'Đã phát hiện:' |
| field | `as.p_Image` | `topPanelBg` | HIGH | k.a_Image(193,top,..) trong a(int,int) |
| field | `as.q_Image` | `bottomPanelBg` | HIGH | k.a_Image(193,bottom,..) |
| field | `as.r_Image` | `footerBg` | HIGH | k.a_Image(193,30,..) dải phím mềm |
| field | `as.s_Image` | `elementIcons` | MED | /pic/D02.gf 4 phần tư cạnh 4 số nguyên tố pet |
| field | `as.t_Image` | `leftRightArrows` | HIGH | /pic/D11.gf 2 nửa trái/phải (âm lượng, số lượng) |
| field | `as.u_Image` | `confirmCancelImg` | MED | /pic/D23.gf 2 nửa ở góc hộp 'Xác nhận học kỹ năng' |
| field | `as.v_Image` | `statBarsImg` | HIGH | /pic/D05.gf 4 dải, vẽ theo tỉ lệ cur/max |
| field | `as.w_Image` | `systemTitleImg` | MED | /pic/setup.gf (bị null) -> chữ 'Chọn bản ghi'/'Âm lượng' |
| field | `as.x_Image` | `softKeyImg` | MED | /pic/D06.gf 3 khung nhãn phím mềm ở chân menu |
| field | `as.y_Image` | `attrIcons` | HIGH | /pic/D13.gf 10 biểu tượng thuộc tính (HP,MP,...) |
| field | `as.z_Image` | `proficiencyImg` | MED | /pic/shulian.gf (熟练) trước số độ thành thạo kỹ năng |
| field | `as.A_Image` | `freePointsImg` | MED | /pic/D07.gf trước a_bl.a_AI[16] (điểm tự do) |
| field | `as.B_Image` | `levelImg` | MED | /pic/D08.gf sau số cấp |
| field | `as.C_Image` | `classNameImg` | MED | /pic/D09.gf khung = nghề h_S-1 (fallback a_bl.a_String) |
| field | `as.E_Image` | `dexArrowsImg` | MED | /pic/D20.gf 2 nửa trái/phải ở hàng lọc đồ giám |
| field | `as.F_Image` | `dexFilterImg` | MED | /pic/D21.gf 3 khung = h_AString |
| field | `as.G_Image` | `statSelectImg` | LOW | /pic/number02.gf vẽ nguyên ảnh ở hàng w_I khi cộng điểm |
| field | `as.H_Image` | `goldDigits` | MED | /pic/number03.gf số 6x8: tiền vàng chân menu, cột giá tiệm |
| field | `as.I_Image` | `plusDigits` | HIGH | /pic/number04.gf số tăng (var>0) |
| field | `as.J_Image` | `minusDigits` | HIGH | /pic/number05.gf số giảm (var<0, Math.abs) |
| field | `as.a_AImage` | `actionMenuImgs` | MED | imgmenu1/imgmenu2.gf (bị null) nhãn menu thao tác thường/chọn |
| hàm | `as.a(int,int)` | `setPanelHeights` | HIGH | B_I=top-2,C_I=bottom-2, tạo p_Image/q_Image |
| hàm | `as.l()` | `loadAnims` | HIGH | nạp D01/tbss/D41/D24/D28.av |
| hàm | `as.a(byte,byte)` | `showPage` | HIGH | o_B=tab, gán trang con theo tab; Patch hook SfxUi.page(as,group,page) |
| hàm | `as.m()` | `buildFusionMaterialList` | HIGH | db từ b_AAS (o.b_I số đang có) |
| hàm | `as.a()` | `buildPetList` | HIGH | l từ a_Acv |
| hàm | `as.n()` | `initEquipTab` | HIGH | /ft/chr.av, bộ trang bị b_AS, đếm q_I |
| hàm | `as.o()` | `buildHeroSkillList` | HIGH | d_ab từ a_bl.a_Ad |
| hàm | `as.b()` | `buildQuestLists` | HIGH | cy (loại 1) + bf (loại 2/3) từ o.a_Abp |
| hàm | `as.c()` | `reloadBag` | HIGH | o.d();o.e();j() |
| hàm | `as.p()` | `buildSystemMenu` | HIGH | c_ab từ systemMenuRows; Patch hook menuRows |
| hàm | `as.a(byte)` | `showPreview` | HIGH | z_I = trang đang trỏ trên thanh tab, chuẩn bị dữ liệu |
| hàm | `as.a(int)` | `loadDexPet` | HIGH | đọc petdata dòng id: nguyên liệu 'Hợp thành', nguyên tố, sao |
| hàm | `as.d()` | `buildDexLists` | HIGH | b_bc/c_bc/d_bc |
| hàm | `as.b(int)` | `loadShopStock` | HIGH | /bin/shop.bin itemShop/equipShop/skillShop<n> |
| hàm | `as.e()` | `releaseData` | HIGH | null các mảng tĩnh/list khi đóng menu (ab.a_Z) |
| hàm | `as.f()` | `resetListCursors` | HIGH | chọn dòng 0 cho mọi list |
| hàm | `as.g()` | `buildLearnerList` | MED | l pet (c_I==4) hoặc l party |
| hàm | `as.h()` | `releaseImages` | HIGH | null p/q/r_Image, petData... |
| hàm | `as.i()` | `initPages` | HIGH | dựng shop (o_B 14/15) + mọi list; gọi khi mở menu |
| hàm | `as.a(Graphics)` | `draw` | HIGH | khung, chân, thanh tab, dispatch theo o_B/z_I, toast |
| hàm | `as.b(Graphics)` | `drawShop` | HIGH | o_B 15 'Tên'/'Giá' |
| hàm | `as.c(Graphics)` | `drawSkillShop` | HIGH | o_B 14 'Xác nhận học kỹ năng' |
| hàm | `as.d(Graphics)` | `drawLearnerList` | HIGH | người học kỹ năng (c_I) |
| hàm | `as.e(Graphics)` | `drawUseTargetList` | HIGH | o_B 13 nhân vật + pet |
| hàm | `as.f(Graphics)` | `drawDexTab` | HIGH | 'Đã phát hiện:', 'Gen đặc trưng', 'Đang hợp thành' |
| hàm | `as.a(Graphics,Image,int,int,int,int)` | `drawStars` | HIGH | n/2 sao đầy + 1 nửa |
| hàm | `as.g(Graphics)` | `drawSkillTab` | HIGH | d_ab, độ thành thạo |
| hàm | `as.h(Graphics)` | `drawSystemTab` | HIGH | 'Chọn bản ghi','Âm lượng','Trở về menu chính?'; Patch hook as.h(G) |
| hàm | `as.i(Graphics)` | `drawQuestTab` | HIGH | 'Tên nhiệm vụ','Danh sách nhiệm vụ trống' |
| hàm | `as.j(Graphics)` | `drawBagTab` | HIGH | 'Không có vật phẩm liên quan', lưới 7x5 |
| hàm | `as.j()` | `buildBagItems` | HIGH | chia o.a_AAS theo o.f_AS vào 3 loại |
| hàm | `as.a(Graphics,int,int,int,int)` | `drawSlotFrame` | HIGH | ô vuông 20x20 viền 2 màu |
| hàm | `as.k(Graphics)` | `drawEquipTab` | HIGH | 6 ô trang bị, bộ, so sánh chỉ số, khảm |
| hàm | `as.a(Graphics,int,int,int,int[][])` | `drawStatBars` | HIGH | 3 thanh D05 tỉ lệ cur/max + số cur/max |
| hàm | `as.a(Graphics,int,int,int,int,int[])` | `drawStatValues` | MED | 3 dòng biểu tượng D13 (khung 3..5) + số |
| hàm | `as.k()` | `resetImplant` | HIGH | r_I/s_I/t_I/a_AAB/r_B = 0, reset D28.av |
| hàm | `as.l(Graphics)` | `drawPetTab` | HIGH | 'Không có sủng vật','Chắc chắn thả sủng vật'... |
| hàm | `as.a(Graphics,cv)` | `drawPetAttributes` | HIGH | q_B 2: 'Gen đặc trưng:', ô gen |
| hàm | `as.m(Graphics)` | `drawStatusTab` | HIGH | tab Trạng thái, cộng điểm (o_B==2) |
| hàm | `as.b(Graphics,int,int,int,int)` | `drawHeroPortrait` | HIGH | c_bx trong khung + nghề + cấp |
| hàm | `as.a(Graphics,byte[])` | `drawElements` | HIGH | 4 số nguyên tố quanh D01.av |
| hàm | `as.c(int)` | `setSoftKeyFrame` | MED | E_I = khung D06 |
| hàm | `as.a(Graphics,boolean,int,int,int)` | `drawQuantityDialog` | HIGH | 'Số lượng:' qty/max, giá = đơn giá*qty |
| hàm | `as.a(Graphics,int[],int,int,int,int,int,int)` | `drawActionMenu` | HIGH | hộp menu thao tác từ actionLabels |
| hàm | `as.a(String,int)` | `showToast` | HIGH | c_String/F_I/h_Z; 'Không có sủng vật', 'Học thành công'... |

### `bg` → **ListPanel** (HIGH) — lớp trừu tượng: Vector dòng, chọn/cuộn; ab.a(bg) gom nhiều cột

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bg.a_I` | `x` | HIGH | a(int,int,int,int) |
| field | `bg.b_I` | `y` | HIGH | a(int,int,int,int) |
| field | `bg.c_I` | `w` | HIGH | a(int,int,int,int) |
| field | `bg.d_I` | `h` | HIGH | a(int,int,int,int); i_I = h/j_I |
| field | `bg.e_I` | `innerW` | HIGH | = w - 2*f_I |
| field | `bg.f_I` | `padX` | HIGH | 4, lề trái chữ |
| field | `bg.g_I` | `selectedIndex` | HIGH | e()/f() tăng/giảm, c() trả về |
| field | `bg.h_I` | `scrollTop` | HIGH | dòng đầu hiển thị |
| field | `bg.i_I` | `visibleRows` | HIGH | = h / rowHeight |
| field | `bg.j_I` | `rowHeight` | HIGH | cg.b_S+4, c(int) |
| field | `bg.k_I` | `padY` | HIGH | 2, lề trên |
| field | `bg.a_Vector` | `items` | HIGH | các dòng (bu/cn/bd/bw/bm/Image/String) |
| hàm | `bg.a(Graphics)` | `draw` | HIGH | abstract, ab gọi cho từng cột |
| hàm | `bg.b()` | `itemCount` | HIGH | a_Vector.size() |
| hàm | `bg.a_V()` | `clear` | HIGH | removeAllElements ở lớp con |
| hàm | `bg.a(int)` | `setSelectedIndex` | HIGH | g_I = i (IndexOutOfBounds nếu vượt) |
| hàm | `bg.e()` | `selectNext` | HIGH | g_I++, cuộn |
| hàm | `bg.f()` | `selectPrev` | HIGH | g_I--, cuộn |
| hàm | `bg.b(int)` | `resetScroll` | HIGH | h_I = 0 (bỏ qua tham số) |
| hàm | `bg.a_AI()` | `getBounds` | HIGH | {x,y,w,h} |
| hàm | `bg.c()` | `getSelectedIndex` | HIGH | return g_I; ab.b_I() |
| hàm | `bg.c(int)` | `setRowHeight` | HIGH | j_I = v |
| hàm | `bg.a(int,int,int,int)` | `setBounds` | HIGH | x,y,w,h, tính visibleRows |

### `bc` → **DexListPanel** (HIGH) — danh sách đồ giám phân trang '#  001' + tên pet (bu)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bc.l_I` | `page` | HIGH | trang hiện tại (1-based), c()/d_V() |
| field | `bc.m_I` | `pageCount` | HIGH | b(): ceil(size/visibleRows) |
| field | `bc.n_I` | `cursorBob` | HIGH | 3 px mỗi 12 khung |
| field | `bc.a_Image` | `cursorImg` | HIGH | arrow.gf (a(Image,Image)) |
| field | `bc.b_Image` | `digitsImg` | HIGH | number01.gf vẽ 'trang/tổng' |
| hàm | `bc.b()` | `updatePageCount` | HIGH | m_I = ceil(size / i_I) |
| hàm | `bc.d_I()` | `absoluteIndex` | HIGH | g_I + (page-1)*visibleRows |
| hàm | `bc.a_I()` | `getSelectedPetId` | HIGH | bu.a_I = pet id (k.a(id) anim) |
| hàm | `bc.a_String()` | `getSelectedName` | HIGH | bu.a_String, '???' = chưa phát hiện |
| hàm | `bc.c()` | `nextPage` | HIGH | l_I++ (phím phải ab.d) |
| hàm | `bc.d_V()` | `prevPage` | HIGH | l_I-- (phím trái) |
| hàm | `bc.a(int,String)` | `addEntry` | HIGH | new bu(id,name) |
| hàm | `bc.a(Image,Image)` | `setImages` | HIGH | cursor + digits |

### `bf` → **SideQuestListPanel** (HIGH) — list nhiệm vụ loại 2 'Thường'/3 'Công hội', dấu hoàn thành D15

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bf.a_Image` | `cursorImg` | HIGH | mũi tên chọn (as gán a_Image) |
| field | `bf.b_Image` | `digitsImg` | MED | number01.gf; chỉ để bật vẽ nhãn loại |
| field | `bf.c_Image` | `doneMarkImg` | HIGH | /pic/D15.gf khung 0 = cn.a_Z (xong) |
| field | `bf.l_I` | `cursorBob` | HIGH | nhún mũi tên |
| hàm | `bf.a_String()` | `getSelectedName` | HIGH | cn.a_String |
| hàm | `bf.a_I()` | `getSelectedQuestIndex` | HIGH | cn.a_I = chỉ số o.a_Abp |
| hàm | `bf.a(int,int,String,boolean)` | `addQuest` | HIGH | (index,type,name,done) |
| hàm | `bf.a(Graphics,int,int,int)` | `drawQuestTypeTag` | HIGH | '[Công hội]' (type 3) / '[Thường]' |

### `cy` → **MainQuestListPanel** (HIGH) — list nhiệm vụ chính tuyến (bd), trạng thái 1 = đã nộp (xám)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cy.a_Image` | `cursorImg` | HIGH | mũi tên chọn |
| field | `cy.l_I` | `cursorBob` | HIGH | nhún mũi tên |
| hàm | `cy.a_String()` | `getSelectedName` | HIGH | bd.a_String() |
| hàm | `cy.a_I()` | `getSelectedStatus` | HIGH | 1 -> 'Đã nộp nhiệm vụ hoàn thành!' |
| hàm | `cy.a(int,int,String)` | `addQuest` | HIGH | (id(bỏ qua),status,name) |

### `de` → **MenuTabBar** (HIGH) — 8 tab 'Trạng thái'..'Hệ thống', D03 nền, xttb biểu tượng, D12 tiêu đề

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `de.a_Image` | `barBg` | HIGH | /pic/D03.gf (fallback vẽ hình chữ nhật) |
| field | `de.b_Image` | `tabIcons` | HIGH | /pic/xttb.gf 8 khung (dd.b_I) |
| field | `de.c_Image` | `tabTitles` | HIGH | /pic/D12.gf 8 khung tiêu đề (fallback a_AString) |
| field | `de.d_Image` | `tabUnderline` | MED | /pic/tbbj.gf dưới tab đang chọn |
| field | `de.e_Image` | `arrowsImg` | HIGH | /pic/D04.gf 2 nửa mũi tên trái/phải |
| field | `de.f_Image` | `selectTitleImg` | MED | /pic/D46.gf (bị null) -> chữ 'Chọn' |
| field | `de.a_Z` | `isSelectMode` | HIGH | a(boolean): chỉ hiện 'Chọn' (ci '进入图鉴') |
| field | `de.b_Z` | `hideArrows` | HIGH | as.a(byte,byte): false khi o_B==1 (đang chọn tab) |
| field | `de.l_I` | `cursorBob` | HIGH | nhún mũi tên |
| field | `de.a_AString` | `tabNames` | HIGH | 'Trạng thái','Sủng vật','Kỹ năng','Trang bị','Túi đồ','Nhiệm vụ','Đồ giám','Hệ thống' |
| hàm | `de.a(boolean)` | `setSelectMode` | HIGH | a_Z = true |
| hàm | `de.a(int,int,int,int,int)` | `setLayout` | HIGH | x,y cố định w=193 h=28 tabW=27 5 tab |
| hàm | `de.a(int,int)` | `addTab` | HIGH | new dd(tabId, iconFrame) |

### `db` → **MaterialListPanel** (HIGH) — nguyên liệu hợp thành: icon, tên, 'có/cần' (bm)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `db.a_Image` | `cursorImg` | HIGH | mũi tên chọn |
| field | `db.b_Image` | `digitsImg` | HIGH | /pic/number01.gf số có/cần |
| field | `db.l_I` | `cursorBob` | HIGH | nhún mũi tên |
| hàm | `db.a(Image,String,int,int)` | `addMaterial` | HIGH | (icon,name,owned=o.b_I(id),required) |

### `l` → **PetListPanel** (HIGH) — dòng: tên, '<cấp> cấp', 'Xuất chiến' (bw); dùng cho pet và party

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `l.a_Image` | `cursorImg` | HIGH | mũi tên chọn |
| field | `l.b_Image` | `deployedBadge` | MED | /pic/D19.gf (bị null) -> chữ 'Xuất chiến' |
| field | `l.l_I` | `cursorBob` | HIGH | nhún mũi tên |
| hàm | `l.a(int,int,boolean,String,int)` | `addEntry` | HIGH | (petId,index,deployed,name,level) |
| hàm | `l.a_I()` | `getSelectedSlot` | HIGH | bw.a_I = chỉ số trong a_Acv của dòng chọn |

### `g` → **IconListPanel** (HIGH) — cột ảnh (icon kỹ năng/vật phẩm) với khung chọn

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `g.a_Z` | `showSelection` | HIGH | vẽ khung 'chọn' cho dòng g_I |
| hàm | `g.a(Image)` | `addIcon` | HIGH | a_Vector.addElement(Image) |

### `bo` → **TextListPanel** (HIGH) — cột chữ (String) căn giữa, màu chọn/thường

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bo.a_B` | `style` | HIGH | 0 màu đơn a_AI, 1 chữ viền a_AAI |
| field | `bo.a_AI` | `colors` | HIGH | {chọn, thường} |
| field | `bo.a_AAI` | `outlineColors` | HIGH | {{fg,viền} chọn, {fg,viền} thường} |
| hàm | `bo.a(String)` | `addText` | HIGH | thêm dòng |

### `cp` → **ValueListPanel** (MED) — cột chữ hoặc số vẽ bằng ảnh chữ số (giá tiệm, số lượng)

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cp.a_B` | `style` | HIGH | 0 chữ, 1 số bằng ảnh a_Image |
| field | `cp.l_I` | `digitFrames` | HIGH | số khung chữ số trong ảnh (10) |
| field | `cp.a_Image` | `digitsImg` | HIGH | number03.gf ở tiệm |
| field | `cp.a_Z` | `showTimesPrefix` | HIGH | vẽ 'x  ' trước số (bn: số lượng) |
| hàm | `cp.a(String)` | `addText` | HIGH | thêm dòng |

### `dc` → **TextBlock** (HIGH) — hộp chữ tự xuống dòng, thẻ màu <n>..</>; dùng ở as/bn/bs/cd/cf/df

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `dc.a_AS` | `lineIds` | LOW | chỉ số dòng (-1 khi text null), không thấy đọc |
| field | `dc.a_AString` | `lines` | HIGH | dòng đã ngắt (cd đọc .length) |
| field | `dc.b_AI` | `lineColors` | HIGH | màu mỗi dòng |
| field | `dc.a_S` | `x` | HIGH | a(int,int,int,int,int) |
| field | `dc.b_S` | `y` | HIGH | a(int,int,int,int,int) |
| field | `dc.c_S` | `w` | HIGH | a(int,int,int,int,int) |
| field | `dc.d_S` | `h` | HIGH | a(int,int,int,int,int) |
| field | `dc.e_S` | `visibleLines` | HIGH | số dòng hiện |
| field | `dc.a_AI` | `tagColors` | HIGH | {đỏ,lục,lam} cho thẻ <1>,<2>,<3> |
| field | `dc.b_AString` | `plainLines` | HIGH | dòng đã bỏ thẻ (cf đọc) |
| field | `dc.a_AAString` | `colorSpans` | HIGH | {dòng,đầu,cuối,màu} từ thẻ <màu>..</> |
| hàm | `dc.a(String,int,String,int)` | `addText` | HIGH | (text,width,unused,color) ngắt dòng cg.a(text,width) |
| hàm | `dc.a()` | `clear` | HIGH | null lines/colors |
| hàm | `dc.a(int,int,int,int,int)` | `setBounds` | HIGH | x,y,w,h,visibleLines |
| hàm | `dc.a(Graphics,dc,int,boolean,boolean)` | `draw` | HIGH | (g,src,firstLine,useScrollBox,centered) |
| hàm | `dc.a(String,String,int)` | `addTaggedText` | HIGH | vh.wrapParts rồi parse thẻ màu (cf hội thoại) |
| hàm | `dc.a(String,int)` | `parseColorTags` | HIGH | '<'..'>' ... '</>' -> colorSpans, plainLines |
| hàm | `dc.a(Graphics,String,int,int,int,int)` | `drawTaggedLine` | HIGH | vẽ dòng + tô lại đoạn có thẻ màu |

### `cs` → **QuestDetailBox** (HIGH) — 'Mô tả nhiệm vụ' + 'Thưởng nhiệm vụ' (o.a_Abp[].b_String/e_String), cuộn

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cs.d_I` | `x` | HIGH | ctor |
| field | `cs.e_I` | `y` | HIGH | ctor cs(x,y,w,h) |
| field | `cs.f_I` | `w` | HIGH | ctor cs(x,y,w,h) |
| field | `cs.g_I` | `h` | HIGH | ctor cs(x,y,w,h) |
| field | `cs.a_String` | `title` | HIGH | 'Mô tả nhiệm vụ' |
| field | `cs.a_AString` | `descLines` | HIGH | mô tả ngắt dòng (w-20) |
| field | `cs.b_String` | `rewardTitle` | HIGH | 'Thưởng nhiệm vụ' |
| field | `cs.b_AString` | `rewards` | HIGH | 'loại,id,sl' tách '/': 1 vật phẩm,2 trang bị,3 vàng,5 EXP |
| field | `cs.a_AI` | `colors` | HIGH | {fg,viền tiêu đề, fg,viền tiêu đề thưởng} |
| field | `cs.h_I` | `lineHeight` | HIGH | cg.b_S+4 |
| field | `cs.a_I` | `visibleLines` | HIGH | h / lineHeight (ab cuộn) |
| field | `cs.b_I` | `totalLines` | HIGH | mô tả + thưởng + 2 |
| field | `cs.c_I` | `scrollLine` | HIGH | ab phím lên/xuống |
| field | `cs.a_Image` | `expIcon` | HIGH | /icon/13.gf cạnh 'EXP' |
| field | `cs.b_Image` | `goldIcon` | MED | /pic/D10.gf thưởng loại 3 |
| hàm | `cs.a(String,String,String,String)` | `setContent` | HIGH | (title,desc,rewardTitle,rewards) |
| hàm | `cs.a(Graphics)` | `draw` | HIGH | vẽ có clip và thanh cuộn |

## Cụm `world`


### `cd` → **World** (HIGH) — da.a_cd; loads /map/<name>.map, /building/s*sp.dat, /bin/s<N>e.txt; owns ca/bj/ci/bn/cf

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cd.a_B` | `worldState` | HIGH | 1 loading (b()), 2 running, 3 game-over/ending text; 0 after leaving |
| field | `cd.b_B` | `mode` | HIGH | 1 field 2 dialog(cf) 3 msgbox 4 menu(as) 5 battle(bn) 6 close-up anim(cr) 7 sms(df) 8 dungeon loot 9 quest 10 order.jpg |
| field | `cd.c_B` | `questDialogPage` | MED | mode 9: 1 = "Mô tả nhiệm vụ", else "Phần thưởng"; ae sets 2 |
| field | `cd.i_B` | `loadTargetPercent` | HIGH | set 10/20/50/90/100 during c(); loading bar |
| field | `cd.j_B` | `loadShownPercent` | HIGH | increments toward i_B; bar x = 15+(w-30)*j_B/100 |
| field | `cd.a_S` | `mapWidth` | HIGH | = ca.b_S() (pixel width); camera clamp i_S <= a_S-screenW |
| field | `cd.b_S` | `mapHeight` | HIGH | = ca.a_S() (pixel height) |
| field | `cd.i_S` | `cameraX` | HIGH | follows player d_S - screenW/2, clamped |
| field | `cd.j_S` | `cameraY` | HIGH | follows player e_S - screenH/2 |
| field | `cd.b_I` | `mapOffsetX` | HIGH | (screenW-mapWidth)/2 when map narrower than screen |
| field | `cd.c_I` | `mapOffsetY` | HIGH | (screenH-mapHeight)/2 |
| field | `cd.a_ca` | `tileMap` | HIGH | new ca(); a_Z("/map/"+name+".map") |
| field | `cd.a_bj` | `sceneLayer` | HIGH | bj: buildings + actors (cz[]) |
| field | `cd.a_cf` | `dialogBox` | HIGH | cf; mode 2 |
| field | `cd.a_bn` | `battle` | HIGH | new bn(); mode 5 |
| field | `cd.a_ax` | `nearbyChest` | MED | bl.a_ax(player) result; opened by OK in ae |
| field | `cd.a_as` | `menu` | HIGH | as; mode 4 |
| field | `cd.a_bb` | `screenWipe` | HIGH | bb transition on map enter |
| field | `cd.a_bl` | `dialogSpeaker` | MED | set by cf.e() = a_bl(speakerId); cleared in k() |
| field | `cd.c_bl` | `scratchRole` | LOW | new bl() created lazily, never read |
| field | `cd.d_bl` | `roleBeingRemoved` | HIGH | d(int): blinks l_B times then bj.c(d_bl) |
| field | `cd.b_bl` | `nearbyNpc` | HIGH | bl.a_bl(player); its name drawn top-right; ae talks to it |
| field | `cd.a_cr` | `closeUpAnim` | MED | cr; 特写动画 opcode, mode 6 |
| field | `cd.a_cl` | `storyCrawl` | HIGH | cl; 绘画章节 opcode |
| field | `cd.a_ci` | `script` | HIGH | ci; /bin/s<N>e.txt events |
| field | `cd.a_dc` | `textLayout` | MED | dc used for msgbox/quest text |
| field | `cd.a_Abi` | `rainDrops` | HIGH | 100 bi lines; 开始下雨 |
| field | `cd.a_df` | `smsMenu` | MED | df (smsmenu.bin), mode 7 |
| field | `cd.d_String` | `pendingMapName` | HIGH | o.a_String copied before loading |
| field | `cd.a_Z` | `loadFinished` | HIGH | set by j thread after c() |
| field | `cd.w_Z` | `isSameMapReload` | MED | a_String==d_String && c_S<0: keep roles/script |
| field | `cd.c_S` | `entryPointIndex` | HIGH | 换场景 arg 3; index into sceneChange S<n>P list |
| field | `cd.b_Z` | `nearChest` | HIGH | a_ax != null |
| field | `cd.c_Z` | `nearNpc` | HIGH | b_bl != null |
| field | `cd.a_AString` | `messageLines` | HIGH | mode 3 box, "Nhấn OK để tiếp tục"; 提示信息 |
| field | `cd.d_B` | `messageScroll` | HIGH | first visible line, ae up/down |
| field | `cd.e_B` | `endingId` | HIGH | 游戏结局 arg; "end"+e_B+"Story:" |
| field | `cd.d_Z` | `isShaking` | HIGH | 场景抖动 / 场景抖动停 |
| field | `cd.k_B` | `shakeOffsetY` | HIGH | 4 every 4th frame while d_Z |
| field | `cd.e_String` | `endingStoryText` | MED | story.bin "end<N>Story:" raw text |
| field | `cd.c_AString` | `endingLines` | HIGH | wrapped ending text |
| field | `cd.d_I` | `endingTextY` | HIGH | scroll y of ending text |
| field | `cd.e_I` | `creditsY` | HIGH | y of "Đội ngũ phát triển" credits |
| field | `cd.e_Z` | `isResting` | HIGH | "Nghỉ ngơi một chút"; 休息/高级休息 |
| field | `cd.f_B` | `restFrames` | HIGH | 30 frames of rest screen |
| field | `cd.f_Z` | `battlePending` | HIGH | set in a(short[]x3); bl stops moving while true |
| field | `cd.a_AS` | `battleEnemyIds` | MED | "id/lvl/n" field 0 -> bn.b_AS |
| field | `cd.b_AS` | `battleEnemyLevels` | MED | field 1 -> bn.c_AS |
| field | `cd.c_AS` | `battleEnemyCounts` | LOW | field 2 (always 1 in scripts) -> bn.a_AS |
| field | `cd.g_B` | `flashCounter` | HIGH | countdown of i_Z / battle flash x_Z |
| field | `cd.x_Z` | `battleFlashing` | HIGH | white flash then new bn() |
| field | `cd.y_Z` | `isRemovingRole` | HIGH | blink and remove d_bl |
| field | `cd.l_B` | `removeBlinkCount` | HIGH | 10 blinks |
| field | `cd.a_String` | `currentMapName` | HIGH | compared with d_String |
| field | `cd.z_Z` | `endScreenDone` | HIGH | credits finished / defeat text shown -> key returns to cover |
| field | `cd.g_Z` | `playerControlEnabled` | HIGH | gates direction keys; false while script runs |
| field | `cd.h_Z` | `fieldHudEnabled` | HIGH | sms icon + touch buttons; key/touch input gate |
| field | `cd.f_I` | `enterFrames` | MED | counts 10 frames of A_Z |
| field | `cd.A_Z` | `isEnteringMap` | HIGH | tanshe.av appear effect, then h_Z=true |
| field | `cd.B_Z` | `endingTextDone` | HIGH | switch from ending text to credits |
| field | `cd.b_Image` | `debugDigitsImage` | MED | /pic/number01.gf, phase display when at.e_Z |
| field | `cd.c_Image` | `boxCornerImage` | MED | /pic/jiao1.gf corners of msgbox |
| field | `cd.d_S` | `messageVisibleLines` | HIGH | 3 lines in msgbox |
| field | `cd.i_Z` | `isScreenFlashing` | HIGH | 屏幕闪烁 |
| field | `cd.j_Z` | `letterboxOpening` | HIGH | 上下拉屏 bars grow l_S -> h_S |
| field | `cd.k_Z` | `letterboxClosing` | HIGH | 恢复拉屏 |
| field | `cd.a_I` | `crawlSpeed` | HIGH | cl: scroll step / typing period; OK key changes it |
| field | `cd.l_Z` | `storyCrawlActive` | HIGH | cl drawn; "Bỏ qua" skip |
| field | `cd.m_Z` | `isRaining` | HIGH | 开始下雨 / 结束下雨 |
| field | `cd.n_Z` | `levelUpEffect` | HIGH | set in a(bl) on level up, douqi.av on player |
| field | `cd.b_bx` | `levelUpAnim` | HIGH | /skill/douqi.av |
| field | `cd.c_bx` | `enterMapAnim` | HIGH | /ft/tanshe.av |
| field | `cd.d_Image` | `mazeGateImage1` | LOW | /map/pics/sdt1.gf at o.k_AString[0] (random maze 2 exits) |
| field | `cd.e_Image` | `mazeGateImage2` | LOW | /map/pics/sdt2.gf at o.k_AString[1] |
| field | `cd.d_bx` | `smsIconAnim` | HIGH | /av/sms.av bottom corner |
| field | `cd.e_bx` | `loadingAnim` | HIGH | /av/long2.av on loading bar |
| field | `cd.o_Z` | `useSecondEntry` | MED | 换场景2 in random maze: spawn at j_AString[1] |
| field | `cd.b_String` | `chosenOption` | HIGH | cf.b_AString[selected]; 选择了 compares it |
| field | `cd.a_bx` | `portalAnim` | HIGH | /map/pics/fazheng.av set by k (开启副本); 是否开启 tests it |
| field | `cd.f_bx` | `questAvailableMarker` | MED | /pic/task/task1.av above quest NPC |
| field | `cd.g_bx` | `questActiveMarker` | MED | task2.av: quest taken, not done |
| field | `cd.h_bx` | `questDoneMarker` | MED | task3.av: quest done (bp.a_Z) |
| field | `cd.e_S` | `portalX` | HIGH | set with a_bx by k |
| field | `cd.f_S` | `portalY` | HIGH |  |
| field | `cd.p_Z` | `isDungeonBattle` | HIGH | 副本战斗; bn -> loot mode 8 |
| field | `cd.q_Z` | `isNormalBattle` | HIGH | 战斗 |
| field | `cd.a_AI` | `dungeonLoot` | HIGH | bn: {type,id,count,chance}; type 1 item 2 equip 3 gold 4 pet |
| field | `cd.b_AString` | `lootNotices` | HIGH | "Nhận ..." strings -> ticker |
| field | `cd.f_Image` | `goldIcon` | HIGH | /pic/D10.gf "Vàng" |
| field | `cd.g_Image` | `softKeyImage` | MED | /pic/D23.gf two halves for accept/cancel |
| field | `cd.h_Image` | `expIcon` | HIGH | /icon/13.gf "EXP" |
| field | `cd.r_Z` | `isTutorialBattle` | HIGH | 教学战斗 |
| field | `cd.s_Z` | `isTransforming` | HIGH | /av/bianxing.av then ae.b(h_B) |
| field | `cd.i_bx` | `transformAnim` | HIGH | /av/bianxing.av |
| field | `cd.h_B` | `transformKind` | MED | 0 fly (ae.b), 1 mount (touch 4), 2 boat (scene 1) |
| field | `cd.g_S` | `shownQuestId` | HIGH | 显示任务 arg -> task.bin row |
| field | `cd.i_Image` | `orderImage` | HIGH | /pic/order.jpg mode 10 (短信购买 99) |
| field | `cd.c_String` | `lootPetName` | HIGH | petdata.bin name for loot type 4 |
| field | `cd.t_Z` | `cameraReturning` | HIGH | 还原焦点: undo pan offsets h_I/i_I |
| field | `cd.g_I` | `panTravelled` | HIGH | pixels panned so far, compared to m_S*at.f_B |
| field | `cd.h_I` | `panOffsetX` | HIGH | accumulated camera pan x |
| field | `cd.i_I` | `panOffsetY` | HIGH |  |
| field | `cd.j_I` | `dragStartX` | MED | pointer press map x (debug rect) |
| field | `cd.k_I` | `dragStartY` | MED |  |
| field | `cd.l_I` | `dragCurX` | MED | pointer drag/release map x |
| field | `cd.m_I` | `dragCurY` | MED |  |
| field | `cd.C_Z` | `tickerPaused` | HIGH | 1 s pause per line |
| field | `cd.a_J` | `tickerPauseStart` | HIGH | currentTimeMillis |
| field | `cd.n_B` | `tickerLine` | HIGH | next pause line |
| field | `cd.D_Z` | `tickerActive` | HIGH | bottom scrolling notice |
| field | `cd.d_AString` | `tickerLines` | HIGH | a(String[]) |
| field | `cd.k_S` | `tickerScroll` | HIGH |  |
| field | `cd.h_S` | `letterboxTarget` | HIGH | 上下拉屏 arg |
| field | `cd.l_S` | `letterboxSize` | HIGH | bars height = l_S<<2 |
| field | `cd.u_Z` | `blindsClosing` | HIGH | 黑屏: 6 black stripes grow |
| field | `cd.v_Z` | `blindsOpening` | HIGH | 恢复黑屏 |
| field | `cd.n_I` | `blindsWidth` | HIGH |  |
| field | `cd.E_Z` | `cameraPanning` | HIGH | 切换焦点 |
| field | `cd.m_S` | `panSteps` | HIGH | 切换焦点 arg 2 |
| field | `cd.o_B` | `panDirection` | HIGH | 0 down 1 up 2 left 3 right |
| field | `cd.F_Z` | `whiteFlashOn` | HIGH | fills screen white |
| field | `cd.a_AAI` | `paletteFromColors` | MED | sn.bin colour table column 1 -> bj recolour |
| field | `cd.b_AAI` | `paletteToColors` | MED | sn.bin selected column |
| hàm | `cd.a_V()` | `startGame` | HIGH | Main new/continue: o.a_V() then a_V(o.a_S) |
| hàm | `cd.a(String)` | `loadSaveSlot` | HIGH | "Tiến độ 1/2" -> o.a_V(String) then scene |
| hàm | `cd.a_V(int)` | `changeScene` | HIGH | 换场景 / bn respawn; sets o.b_S then b() |
| hàm | `cd.b()` | `beginLoading` | HIGH | a_B=1, new j(this) |
| hàm | `cd.c()` | `loadScene` | HIGH | run by j thread: map, buildings, chests, roles, script |
| hàm | `cd.a_Abl()` | `buildSceneRoles` | HIGH | party + /bin/s<N>r.bin roles; "returnRoles" |
| hàm | `cd.a_Aax()` | `loadChests` | HIGH | /bin/box.bin -> ax[] |
| hàm | `cd.a(Graphics)` | `updateAndDraw` | HIGH | da paint for state 4: logic tick + b(Graphics) |
| hàm | `cd.b_V(int)` | `reviveRole` | MED | 复活角色: remove id from o.c_AS |
| hàm | `cd.a(boolean)` | `triggerRandomEncounter` | HIGH | bl countdown -> a(null,null,null) |
| hàm | `cd.a_bl(int)` | `findRoleById` | LOW | bl.a_bl(int); used by cf for speaker |
| hàm | `cd.b(Graphics)` | `drawWorld` | HIGH | per-mode rendering |
| hàm | `cd.c(Graphics)` | `drawDungeonLoot` | HIGH | "[Phó bản]" "Chiến lợi phẩm" |
| hàm | `cd.d(Graphics)` | `drawDebugOverlay` | HIGH | only at.e_Z: collision/event rects |
| hàm | `cd.c(int)` | `handleKey` | HIGH | da key dispatch for state 4 |
| hàm | `cd.a(int,int)` | `onPointerPress` | HIGH | da.pointerPressed; touch buttons i.a(x,y) |
| hàm | `cd.b(int,int)` | `onPointerRelease` | HIGH | da.pointerReleased |
| hàm | `cd.c(int,int)` | `onPointerDrag` | HIGH | da.pointerDragged |
| hàm | `cd.d()` | `reloadOrPlayMusic` | MED | at.f_Z ? b() : m() |
| hàm | `cd.a(String[])` | `showTicker` | HIGH |  |
| hàm | `cd.a(int,int,int,int,int)` | `startStoryCrawl` | HIGH | 绘画章节 storyId,x,y,h,mode |
| hàm | `cd.e()` | `releaseResources` | HIGH | "clearResource()" |
| hàm | `cd.a(String[],int)` | `startDialog` | HIGH | 对话: lines, NPC turns to player |
| hàm | `cd.b_bl(int)` | `findSceneRole` | HIGH | a_bj actors with h_S == id |
| hàm | `cd.a(bl,int,int,int,int,int)` | `moveRole` | HIGH | 走动: dir 0-3 by dist, else to x,y |
| hàm | `cd.a(String,int,int,int)` | `playCloseUpAnim` | HIGH | 特写动画 path,x,y,mode |
| hàm | `cd.d(int,int)` | `startCameraPan` | HIGH | 切换焦点 dir,steps |
| hàm | `cd.a_Z(int)` | `leaderHasSkill` | MED | 无技能; o.a_Abl[0].a_AS contains id |
| hàm | `cd.f()` | `loadEndingStory` | HIGH | story.bin end<N>Story |
| hàm | `cd.g()` | `resetSceneNpcs` | LOW | bl.g_V() on every non-party NPC |
| hàm | `cd.h()` | `openPetDepot` | MED | 寄宠物 / 取宠物 |
| hàm | `cd.i()` | `openBestiary` | MED | 进入图鉴 -> as.o_B = 8 |
| hàm | `cd.a(String,int,int)` | `openShop` | HIGH | "技能学习" "买道具" "买装备" |
| hàm | `cd.b(String[],int)` | `startScriptBattle` | HIGH | 战斗 args "id/lvl/n" |
| hàm | `cd.a(short[],short[],short[])` | `beginBattle` | HIGH | flash x_Z then bn |
| hàm | `cd.j()` | `stopMusic` | HIGH | Main.music stop/close |
| hàm | `cd.d(int)` | `removeRole` | HIGH | 清除BOSS / 清除敌人 |
| hàm | `cd.k()` | `closeDialog` | HIGH |  |
| hàm | `cd.l()` | `returnFromBattle` | HIGH | bn end paths; revives HP, mode 1 |
| hàm | `cd.a(cv[])` | `levelUpPets` | HIGH | exp>=next -> level+1, next=l^3/2+l^2 |
| hàm | `cd.a(bl)` | `checkLevelUp` | HIGH | same formula, sets n_Z |
| hàm | `cd.m()` | `playSceneMusic` | HIGH | /music/<o.c_String>.mid |
| hàm | `cd.e(Graphics)` | `drawEnding` | HIGH | ending text + credits |
| hàm | `cd.o()` | `loadPaletteTables` | MED | /bin/sn.bin colour tables for buildings |
| hàm | `cd.p()` | `recolorTilesets` | MED | ca.a(name.gf, from, to) |
| hàm | `cd.n()` | `startRain` | HIGH | 开始下雨 |

### `ci` → **ScriptEngine** (HIGH) — parses /bin/s<N>e.txt events, executes Chinese opcode lines

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ci.a_Z` | `isRunning` | HIGH | set in a(bz), cleared in d(); blocks input |
| field | `ci.a_Abz` | `events` | HIGH | bz[] from eventAmount/eventN |
| field | `ci.c_bz` | `lastAreaEvent` | HIGH | avoids re-trigger while standing in area |
| field | `ci.d_bz` | `areaEventHit` | HIGH | auto event overlapping player rect |
| field | `ci.a_bz` | `currentEvent` | HIGH | executing event |
| field | `ci.e_Z` | `allowRetrigger` | MED | set by 瞬移 |
| field | `ci.b_Z` | `hasManualEvent` | HIGH | non-auto event under player; OK starts b_bz |
| field | `ci.b_bz` | `manualEvent` | HIGH |  |
| field | `ci.a_B` | `battleResult` | HIGH | bn: 0 lost, 1 escaped, 2 won (战斗失败/逃跑/胜利) |
| field | `ci.a_S` | `continuousLeft` | HIGH | 连续执行 N: lines that do not pause |
| field | `ci.b_S` | `continuousPending` | HIGH | async actions to wait for (a(>=2)) |
| field | `ci.f_Z` | `lastCondition` | HIGH | result of last test; 继承条件, { |
| field | `ci.b_B` | `lineCondition` | HIGH | 0 none 1 true 2 false for current line |
| field | `ci.c_Z` | `isMustLoseBattle` | HIGH | 必败战斗; bn checks |
| field | `ci.d_Z` | `isExecuting` | HIGH | a_V() steps only when true |
| field | `ci.c_S` | `lineIndex` | HIGH | program counter |
| field | `ci.d_S` | `lastExecutedLine` | HIGH |  |
| field | `ci.g_Z` | `inComment` | HIGH | /* ... */ |
| field | `ci.a_AS` | `blockConditions` | HIGH | pairs (depth, cond) for { } |
| field | `ci.e_S` | `blockDepth` | HIGH |  |
| field | `ci.a_I` | `randomValue` | HIGH | 随机数; '@' in dialog text |
| field | `ci.f_S` | `randomQuestId` | HIGH | 随机任务 pick; 显示任务 default |
| field | `ci.a_AAS` | `randomQuestPools` | HIGH | ids 37..50, chosen by 随机任务 arg |
| hàm | `ci.a(boolean)` | `pushBlockCondition` | HIGH |  |
| hàm | `ci.c()` | `popBlockCondition` | HIGH |  |
| hàm | `ci.a_Z()` | `isBlockActive` | HIGH | all enclosing blocks true |
| hàm | `ci.a(String,int)` | `loadEvents` | HIGH | file, phase |
| hàm | `ci.a(short[])` | `checkAutoEvents` | HIGH | player rect vs auto events |
| hàm | `ci.b(bz)` | `triggerAutoEvent` | MED |  |
| hàm | `ci.a(bz)` | `startEvent` | HIGH |  |
| hàm | `ci.d()` | `endEvent` | HIGH | 结束脚本 |
| hàm | `ci.a_V()` | `step` | HIGH | called every 4th frame from cd |
| hàm | `ci.a(String[],int)` | `executeLine` | HIGH | opcode dispatch |
| hàm | `ci.a(int)` | `advance` | HIGH | 0 next, 1 pause/continue, >=2 async done |

### `ca` → **TileMap** (HIGH) — /map/*.map parser + tile layers + collision rects

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ca.a_S` | `viewX` | HIGH | set by a(int,int,int,int) |
| field | `ca.b_S` | `viewY` | HIGH |  |
| field | `ca.c_S` | `viewW` | HIGH |  |
| field | `ca.d_S` | `viewH` | HIGH |  |
| field | `ca.e_S` | `mapWidth` | HIGH | header short, clamp a_S <= e_S - w |
| field | `ca.f_S` | `mapHeight` | HIGH |  |
| field | `ca.a_B` | `tileW` | HIGH | header byte |
| field | `ca.b_B` | `tileH` | HIGH |  |
| field | `ca.a_AString` | `tilesetNames` | HIGH | readUTF list |
| field | `ca.a_AImage` | `tilesetImages` | HIGH |  |
| field | `ca.a_AB` | `tileImageIndex` | HIGH | per tile def |
| field | `ca.a_AS` | `tileSrcX` | HIGH |  |
| field | `ca.b_AS` | `tileSrcY` | HIGH |  |
| field | `ca.c_AS` | `tileSrcW` | HIGH |  |
| field | `ca.d_AS` | `tileSrcH` | HIGH |  |
| field | `ca.b_AB` | `tileTransform` | HIGH | drawRegion transform |
| field | `ca.b_AImage` | `tileImages` | HIGH | pre-cut per tile (at.a_B 1/2) |
| field | `ca.a_AAB` | `layerCols` | HIGH | per layer cell x |
| field | `ca.b_AAB` | `layerRows` | HIGH |  |
| field | `ca.a_AAS` | `layerTiles` | HIGH |  |
| field | `ca.c_AB` | `layerEncoding` | HIGH | 0 list 1 dense 2 fill |
| field | `ca.c_B` | `backgroundLayerCount` | HIGH | <0 all background; "hậu cảnh"/"tiền cảnh" |
| field | `ca.e_AS` | `blockX` | HIGH | collision rects |
| field | `ca.f_AS` | `blockY` | HIGH |  |
| field | `ca.g_AS` | `blockW` | HIGH |  |
| field | `ca.h_AS` | `blockH` | HIGH |  |
| field | `ca.d_AB` | `blockType` | HIGH | 1 solid (a(short[],int)), debug red/blue |
| field | `ca.a_String` | `imageDir` | HIGH |  |
| field | `ca.b_String` | `mapPath` | HIGH |  |
| field | `ca.c_AAB` | `tilesetBytes` | HIGH | raw bytes before createImage |
| field | `ca.a_I` | `backgroundColor` | HIGH |  |
| field | `ca.a_Image` | `bufferImage` | HIGH | scroll buffer |
| field | `ca.a_Graphics` | `bufferGraphics` | HIGH |  |
| field | `ca.a_Z` | `useScrollBuffer` | MED |  |
| field | `ca.b_Z` | `bufferReady` | HIGH |  |
| field | `ca.g_S` | `bufferOriginX` | HIGH |  |
| field | `ca.h_S` | `bufferOriginY` | HIGH |  |
| field | `ca.i_S` | `bufferW` | HIGH |  |
| field | `ca.j_S` | `bufferH` | HIGH |  |
| field | `ca.k_S` | `bufferCols` | HIGH |  |
| field | `ca.l_S` | `bufferRows` | HIGH |  |
| field | `ca.m_S` | `bufferMargin` | HIGH | 2 tiles |
| field | `ca.c_Z` | `useGridAccel` | HIGH | "tăng tốc ma trận" |
| field | `ca.a_AAAS` | `backgroundGrid` | HIGH | [layer][row][col] tile+1 |
| field | `ca.b_AAAS` | `foregroundGrid` | HIGH |  |
| field | `ca.d_B` | `mapFormat` | HIGH | 1 sparse layers, 2 encoded layers + bg colour |
| hàm | `ca.a(String,int[],int[])` | `recolorTileset` | HIGH | be.a palette swap |
| hàm | `ca.a(short[],int)` | `hitsBlock` | HIGH | type 1 rects vs player rects |
| hàm | `ca.a(int)` | `isLayerTileAligned` | MED | needed for grid accel |
| hàm | `ca.a_V()` | `clear` | HIGH |  |
| hàm | `ca.b_V()` | `releaseImages` | HIGH |  |
| hàm | `ca.b(int,int,int,int)` | `redrawBufferColumns` | MED |  |
| hàm | `ca.c(int,int,int,int)` | `redrawBufferRows` | MED |  |
| hàm | `ca.c()` | `createImages` | HIGH |  |
| hàm | `ca.a_V(String)` | `setImageDir` | HIGH |  |
| hàm | `ca.a(byte[])` | `parseMap` | HIGH |  |
| hàm | `ca.a_S()` | `getMapHeight` | HIGH |  |
| hàm | `ca.b_S()` | `getMapWidth` | HIGH |  |
| hàm | `ca.a(String[],String[],String,int)` | `loadTilesetBytes` | HIGH | cc.a_AB(dir+name) |
| hàm | `ca.a(Graphics,int,int)` | `drawBackground` | HIGH |  |
| hàm | `ca.a(Graphics,int,int,short[])` | `drawBackgroundArea` | HIGH |  |
| hàm | `ca.a(Graphics)` | `drawDebugBlocks` | HIGH |  |
| hàm | `ca.b(Graphics,int,int)` | `drawForeground` | HIGH |  |
| hàm | `ca.a(Graphics,short[],int,int,short[])` | `drawLayers` | HIGH |  |
| hàm | `ca.a_Z(String)` | `loadMap` | HIGH | "Không tìm thấy tệp" |
| hàm | `ca.a(int,int,int,int)` | `setViewport` | HIGH |  |

### `bj` → **SceneLayer** (HIGH) — /building/s*sp.dat buildings + y-sorted actors (cz[])

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bj.a_bj` | `instance` | LOW | set in ctor, never read |
| field | `bj.a_S` | `viewX` | HIGH |  |
| field | `bj.b_S` | `viewY` | HIGH |  |
| field | `bj.c_S` | `viewW` | HIGH |  |
| field | `bj.d_S` | `viewH` | HIGH |  |
| field | `bj.a_Ac` | `buildings` | HIGH | c (image or anim) |
| field | `bj.a_Acz` | `actors` | HIGH | roles, chests; y-sorted |
| field | `bj.a_AAS` | `midPlacements` | HIGH | {res,x,y} drawn between actors by y |
| field | `bj.b_AAS` | `groundPlacements` | HIGH | drawn before actors |
| field | `bj.c_AAS` | `topPlacements` | HIGH | drawn after tile foreground |
| field | `bj.b_Acz` | `skyActors` | HIGH | flying player (bl adds, ae removes) |
| field | `bj.c_Acz` | `groundActors` | LOW | drawn before mid; never filled |
| field | `bj.a_String` | `imageDir` | HIGH |  |
| field | `bj.b_String` | `animDir` | HIGH |  |
| field | `bj.a_AString` | `resourceNames` | HIGH |  |
| field | `bj.d_AAS` | `resourceRects` | MED | per resource shorts -> c ctor |
| field | `bj.a_I` | `phase` | HIGH | o.a_B |
| field | `bj.a_AI` | `occurredEvents` | HIGH | o.a_AI |
| field | `bj.a_AAI` | `midEventConds` | HIGH | +id must have happened, -id must not |
| field | `bj.b_AAI` | `groundEventConds` | HIGH |  |
| field | `bj.c_AAI` | `topEventConds` | HIGH |  |
| field | `bj.e_AAS` | `midPhases` | HIGH | visible only in listed phases |
| field | `bj.f_AAS` | `groundPhases` | HIGH |  |
| field | `bj.g_AAS` | `topPhases` | HIGH |  |
| field | `bj.a_AZ` | `midVisible` | HIGH |  |
| field | `bj.b_AZ` | `groundVisible` | HIGH |  |
| field | `bj.c_AZ` | `topVisible` | HIGH |  |
| hàm | `bj.a(cz)` | `addActor` | HIGH |  |
| hàm | `bj.a(short[],cz,int)` | `collides` | HIGH |  |
| hàm | `bj.a(short[],cz[],int)` | `collidesExcept` | HIGH |  |
| hàm | `bj.a()` | `clearBuildings` | HIGH |  |
| hàm | `bj.b()` | `compactActors` | HIGH | sort + trim trailing nulls |
| hàm | `bj.a_V(String,String,String)` | `loadBuildings` | HIGH |  |
| hàm | `bj.a(String,String,String,String[],String[],String[],int[][],int[][],int,int)` | `loadBuildingsRecolored` | HIGH | be.a palette swap |
| hàm | `bj.a_Z(String,String,String)` | `readBuildingFile` | HIGH |  |
| hàm | `bj.a_Z(int)` | `isMidVisible` | HIGH |  |
| hàm | `bj.a(Graphics,int,int)` | `drawMiddle` | HIGH |  |
| hàm | `bj.a(Graphics)` | `drawDebugRects` | HIGH |  |
| hàm | `bj.b(Graphics,int,int)` | `drawSkyActors` | HIGH |  |
| hàm | `bj.c(Graphics,int,int)` | `drawTop` | HIGH |  |
| hàm | `bj.c()` | `animateBuildings` | HIGH |  |
| hàm | `bj.b(cz)` | `removeSkyActor` | HIGH |  |
| hàm | `bj.c(cz)` | `removeActor` | HIGH |  |
| hàm | `bj.a_V(int)` | `setPhase` | HIGH |  |
| hàm | `bj.a(int[])` | `setOccurredEvents` | HIGH |  |
| hàm | `bj.a(int,int,int,int)` | `setViewport` | HIGH |  |
| hàm | `bj.d()` | `sortAllActors` | HIGH |  |
| hàm | `bj.a(cz[])` | `sortByY` | HIGH | shell sort on e_S |
| hàm | `bj.e()` | `computeVisibility` | HIGH |  |

### `ax` → **TreasureChest** (HIGH) — /bin/box.bin rows, /av/box.av, box1.av, "Rương đã mở"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ax.a_bx` | `anim` | HIGH |  |
| field | `ax.a_S` | `chestId` | HIGH | box col 1; o.d_AS opened list |
| field | `ax.d_B` | `column6` | LOW | box col 6 (always 0), never read |
| field | `ax.a_B` | `openFrame` | HIGH | 1 opened, 0 closed; anim frame |
| field | `ax.b_S` | `gold` | HIGH | box col 7, "Nhận vàng" |
| field | `ax.a_AAS` | `items` | HIGH | col 8 "id,count/..." |
| field | `ax.b_AAS` | `equips` | HIGH | col 9 |
| field | `ax.b_B` | `chestKind` | HIGH | col 3: 1 needs item 19 "Chìa vạn năng" |
| field | `ax.c_B` | `alreadyOpened` | HIGH | "Rương đã mở" |
| field | `ax.a_AS` | `drawRect` | MED | returned by a_AS() |
| field | `ax.b_AS` | `hitRect` | MED | used by a_AS(int) |
| hàm | `ax.a_V(int)` | `setColumn6` | LOW | stores d_B only |

### `ae` → **FieldInput** (MED) — world key dispatcher: talk, chests, events, dialog/choice, loot, quest

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `ae.a_bl` | `talkTarget` | HIGH | NPC whose s<N>c.bin role script is run |
| hàm | `ae.a(int)` | `handleKey` | HIGH | cd.c(int) for modes other than 4/5/6/7 |
| hàm | `ae.a()` | `openMainMenu` | HIGH | new as, mode 4 |
| hàm | `ae.b()` | `startFlyTransform` | MED | "Chưa mở biến thân bay!" |
| hàm | `ae.b(int)` | `applyTransform` | MED | 0 fly/land, 1 mount, 2 boat |

### `cf` → **DialogBox** (HIGH) — 对话 speech bubble + 对话中选择 choice list

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cf.a_dc` | `layout` | HIGH |  |
| field | `cf.a_Z` | `isVisible` | HIGH |  |
| field | `cf.a_AString` | `lines` | HIGH | dialog entries |
| field | `cf.a_B` | `lineIndex` | HIGH |  |
| field | `cf.a_String` | `speakerName` | HIGH | o.c_AString[id-1] |
| field | `cf.b_AString` | `choices` | HIGH | 对话中选择 args |
| field | `cf.b_B` | `selectedChoice` | HIGH |  |
| field | `cf.a_S` | `choiceScroll` | HIGH |  |
| field | `cf.c_B` | `boxMode` | HIGH | modeTalk(0) / modeChoice(1) |
| field | `cf.d_B` | `modeTalk` | HIGH | 0 |
| field | `cf.e_B` | `modeChoice` | HIGH | 1 |
| field | `cf.c_S` | `boxWidth` | HIGH | screenW - d_S |
| field | `cf.f_B` | `linesPerPage` | HIGH | 3 |
| field | `cf.d_S` | `boxMargin` | HIGH | 4 |
| field | `cf.g_B` | `lastLine` | HIGH |  |
| field | `cf.h_B` | `visibleLines` | HIGH |  |
| field | `cf.i_B` | `firstVisibleLine` | HIGH |  |
| field | `cf.j_B` | `typingLine` | HIGH |  |
| field | `cf.k_B` | `typingChar` | HIGH |  |
| field | `cf.a_bl` | `speaker` | HIGH | NPC turned back after dialog |
| field | `cf.l_B` | `lineHeight` | HIGH | font height >= 16 |
| field | `cf.e_S` | `speakerId` | HIGH | "$id." prefix |
| field | `cf.m_B` | `portraitFrame` | HIGH | "N$" prefix, 0..6 |
| field | `cf.b_S` | `choiceRows` | HIGH | 3 |
| field | `cf.a_Image` | `tailImage` | LOW | btpc.gf load result discarded; always null |
| field | `cf.b_Image` | `arrowImage` | HIGH | /pic/arrow.gf |
| field | `cf.c_Image` | `portraitImage` | HIGH | o.d_AString not .av |
| field | `cf.d_Image` | `cornerImage` | LOW | jiao1.gf load discarded; always null |
| field | `cf.e_Image` | `cornerImage2` | LOW | jiao2.gf load discarded; always null |
| field | `cf.a_I` | `speakerHeight` | LOW | bl.i_S |
| field | `cf.b_Z` | `dockBottom` | MED | always true |
| field | `cf.a_bx` | `portraitAnim` | HIGH | .av portrait |
| field | `cf.b_I` | `boxX` | HIGH |  |
| field | `cf.c_I` | `boxY` | HIGH |  |
| field | `cf.d_I` | `tailSide` | MED | 1/2 |
| field | `cf.e_I` | `tailOffset` | MED |  |
| field | `cf.c_Z` | `tailOnTop` | MED |  |
| field | `cf.d_Z` | `isTyping` | HIGH |  |
| hàm | `cf.a_V()` | `closeDialog` | HIGH |  |
| hàm | `cf.b()` | `scrollUp` | HIGH | key up |
| hàm | `cf.c()` | `scrollDown` | HIGH | key down / OK |
| hàm | `cf.a_Z()` | `isTextComplete` | HIGH |  |
| hàm | `cf.d()` | `computeBoxWidth` | HIGH |  |
| hàm | `cf.e()` | `layoutLine` | HIGH | parses N$id. prefix, '@' |
| hàm | `cf.a(Graphics)` | `draw` | HIGH |  |
| hàm | `cf.f()` | `nextLine` | HIGH |  |

### `cl` → **StoryCrawl** (HIGH) — /bin/story.bin "Story<N>:"; "Bỏ qua"

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `cl.a_String` | `rawText` | HIGH |  |
| field | `cl.a_AString` | `lines` | HIGH |  |
| field | `cl.b_S` | `clipH` | HIGH |  |
| field | `cl.c_S` | `endY` | HIGH |  |
| field | `cl.d_S` | `x` | HIGH |  |
| field | `cl.e_S` | `y` | HIGH |  |
| field | `cl.f_S` | `scrollY` | HIGH |  |
| field | `cl.a_S` | `style` | HIGH | 1 scroll-up, else typewriter |
| field | `cl.a_B` | `lineIndex` | HIGH |  |
| field | `cl.b_B` | `charIndex` | HIGH |  |
| field | `cl.a_Z` | `showPreviousLines` | MED |  |
| field | `cl.g_S` | `typeScrollY` | MED |  |
| field | `cl.h_S` | `visibleLines` | HIGH | 4 |
| hàm | `cl.a(Graphics)` | `draw` | HIGH |  |
| hàm | `cl.a(int,int,int,int,int)` | `begin` | HIGH | storyId,x,y,h,style |

### `bb` → **ScreenWipe** (HIGH) — map-enter transition, sets cd.g_Z

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `bb.b_B` | `wipeType` | HIGH | 0 stripes, 1 grid |
| field | `bb.c_B` | `typeStripes` | LOW | 0, unused |
| field | `bb.d_B` | `typeGrid` | HIGH | 1 |
| field | `bb.a_B` | `typeCount` | HIGH | 2 -> random type |
| field | `bb.a_Z` | `isRevealing` | HIGH |  |
| field | `bb.b_Z` | `isCovering` | MED | never set true |
| field | `bb.a_I` | `progress` | HIGH |  |
| field | `bb.b_I` | `cellSize` | HIGH | 30 |
| hàm | `bb.a(int)` | `setType` | HIGH |  |
| hàm | `bb.a_V()` | `begin` | HIGH |  |
| hàm | `bb.a_Z()` | `isActive` | HIGH |  |
| hàm | `bb.a(Graphics)` | `draw` | HIGH |  |

### `j` → **SceneLoader** (HIGH) — Runnable: cd.c() then cd.a_Z = true

| Loại | Tên gốc | Tên mới | Độ tin cậy | Bằng chứng |
|---|---|---|---|---|
| field | `j.a_Thread` | `thread` | HIGH |  |
| field | `j.a_cd` | `world` | HIGH |  |

# Dự án Việt hoá — 霸王怒与美人泪 (Bá Vương Nộ Dữ Mỹ Nhân Lệ), bản N73 240x320

## Quyết định đã chốt
- Phong cách dịch: kiếm hiệp Hán Việt (tại hạ / huynh / đệ / sư phụ), tên riêng phiên âm Hán Việt.
- Font: **15c** (15px, chân chọn lọc) qua `BitmapFont` cho mọi chữ trong game; ảnh vá bằng pixeltext 13c/15c (cùng bộ font).
- Phương án: **1** (sửa text trong constant pool + hook class `VH`), không dùng lớp dịch runtime.
- Khung thoại làm lại theo ảnh mẫu của người dùng: nền tối bán trong suốt viền vàng, tab tên nhân vật, 3 dòng/trang,
  mũi tên ▼ khi còn trang; giữ 5 chân dung nhỏ có sẵn của game (không thêm chân dung mới).
- Splash "Việt hoá bởi NAMP" (assets/vh_logo.png) 2,5 giây, chỉ lần khởi động đầu.

## Bản đồ thư mục
```
goc/            game.jar gốc, text_can_dich_goc.tsv (trích từ jar gốc)
out/            BaVuongNo_VH.jar/.jad (bàn giao), text_can_dich.tsv đã dịch, thuat-ngu.tsv, vai.md, shots/ (ảnh FreeJ2ME)
dp_work/        dữ liệu dịch dp (work/out/map/meta, thuat-ngu, vai, ghi-chu) — chạy tiếp/sửa bằng scripts/dp.py
src/cfr/        mã decompile (CFR) của jar gốc (đọc tham khảo; o.java là class chính)
src/hook/       VH.java (font/ngắt dòng/khung thoại), VL.java (splash)
scripts/        extract.py, inject.py, patch_classes.py, vh_images.py, build_hook.sh, strip_inner.py, preverify.sh,
                jar_check.py, run_emu.py, shots.sh, make_jad.py, dp.py, cpool.py, bcpatch.py, bands.py, inventory.py
assets/         vh_logo.png (logo splash)
logs/           inventory, jar_check, vh_images, run_emu
tools/          ecj.jar (+ VERSIONS.md); bộ tool NAM.P giải nén ở work/tools bằng scripts/setup_tools.sh
rebuild.sh      jar gốc -> jar Việt hoá (inject -> patch class -> vẽ ảnh -> hook -> preverify -> jar_check -> jad)
```

## Thứ tự các bước
1. `bash scripts/setup_tools.sh <Tool_Viet_Hoa_NAM_P.zip> work` (tạo `work/tools/env.sh`), tải `tools/ecj.jar` (xem tools/VERSIONS.md).
2. `python3 -I scripts/extract.py <jar_giải_nén> goc/text_can_dich_goc.tsv` — chỉ text trong constant pool .class
   (thoại = mảng String[][] {chân dung, sprite, cảm xúc, "[Tên]:câu"} trong a b c e f g h i u; UI trong o).
3. Dịch: `python3 -I scripts/dp.py next/add/check/merge` -> `out/text_can_dich.tsv`.
4. `bash rebuild.sh` -> `out/BaVuongNo_VH.jar` (+ .jad), `logs/jar_check.txt` phải `RESULT: PASS`.
5. Test: `bash scripts/shots.sh out/BaVuongNo_VH.jar work/sX "wait 9; key q; ..."` (q = phím mềm trái, w = phải,
   Return = OK, r = #, mũi tên).

## Ghi chú kỹ thuật (đọc khi debug)
- `o.aI()` nhánh `eS==1` (vẽ khung thoại): pc 122..367 -> `VH.box(o)`, pc 797..1166 -> `VH.dlg(o, màu)`, phần thừa nop
  (patch_classes.py kiểm biên vùng + không có nhánh nhảy vào giữa vùng). Phân trang `o.ag()`: `t.e > 2`, `F*2` -> 3.
- 3 hàm ngắt dòng theo ký tự của `o` (eO / eP / bP) và `o.a(String,soKyTu,...)` thay thân bằng VH.wA/wD/wC/dsn.
- Tên người nói tách từ "[Tên]:câu" (nhiều tên "[A][B]" -> "A, B"); câu không có "]:" thì không có tab tên.
- `o.o` (= stringWidth("宽")) giữ 14px cho các bố cục tính theo bề rộng chữ Hán (hộp cảnh báo, hộp xác nhận).
- Lớp `t`/`p` bị obfuscate thành inner class của `o` -> `build_hook.sh` biên dịch với bản class đã bỏ InnerClasses.
- Ảnh có chữ được vẽ bằng `setClip` theo toạ độ cố định trong `o.java` -> `vh_images.py` vẽ đúng ô, chỉ nới ảnh tới
  bề rộng clip của code. Bảng ánh xạ: `al[0..6]=game_012..018`, `al[7]=game_053`, `ai[0..19]=game_029..048`,
  `ai[20]=game_059`, `T[0..3]=game_006..009`, `aa[0..30]=menu_000..030`, `ae[0..1]=intro_000..001`,
  `I/J/K = game_menu/status/map`, `v = money`.
- `_bb` là logo nhà phát hành mã hoá (SOGOYO), không kiểm tra jar.

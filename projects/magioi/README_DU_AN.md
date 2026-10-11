# Dự án Việt hoá: 魔界战纪-魔女奇缘 (Ma Giới Chiến Ký – Ma Nữ Kỳ Duyên), bản N73 240×320

Việt hoá bởi NAMP. Jar cuối: `out/MaGioiChienKy_VH.jar` + `.jad` (MIDlet-Jar-Size đã khớp).

## Quyết định đã chốt
| Mục | Chọn | Lý do |
|---|---|---|
| Phong cách dịch | Hán Việt nhẹ, tên riêng Latin (阿鲁鲁 → Aruru, 萨琳 → Salin, 宇春 → Yuchun) | người dùng chọn |
| Font | BitmapFont **13c** (13px chân chọn lọc), `font/f13c.bin` | gần cỡ font hệ thống gốc (~12px); game đã dùng API MIDP 2 nên dùng được BitmapFont |
| Phương án | Phương án 2 (hook bytecode tĩnh): đổi đích lời gọi vẽ/đo chữ sang class `VH` | chữ vẽ bằng `Graphics.drawString` rải ở 5 class; font hệ thống máy thật thiếu dấu |
| Khung thoại | làm lại 3 dòng theo ảnh tham khảo: nền xanh đen bán trong suốt, viền vàng, tab tên vàng, chân dung vẽ chìm sau khung | người dùng yêu cầu |
| Logo | splash 2,5 s "Việt hoá bởi NAMP" (bấm phím để bỏ qua) | người dùng chọn |

## Bản đồ thư mục
```
README_DU_AN.md  PROGRESS.md  BAO_CAO.md  rebuild.sh
out/      jar + jad cuối, text_can_dich.tsv (đã dịch), thuat-ngu.tsv, vai.md, shots/ (ảnh FreeJ2ME)
goc/      game.jar gốc, text_can_dich_goc.tsv, anh_goc/ (ảnh trước khi vá)
src/hook/ VH.java (font + ngắt dòng + khung thoại), VL.java (splash)
src/cfr/  mã decompile CFR của các class đã đụng tới (chỉ để đọc)
assets/   vh_logo.png, src/head_0_src.png (chân dung gốc người dùng gửi), jar/ + res/ (ảnh đã vá, do script sinh)
scripts/  mọi script (xem dưới) + bản sao script của skill (dp.py, run_emu.py đã sửa, jar_check.py, preverify.sh...)
dp_work/  dữ liệu dịch của dp (out.tsv, vai.md, thuat-ngu.tsv, map.json)
logs/     inventory, jar_check, rebuild, check_all, run_emu_final (+ emu log)
tools/    fonts/ fontgen/ java/ pixeltext_kit/ check_all.sh env.sh freej2me_plus.jar cfr.jar proguard.jar ecj.jar
```

## Thứ tự các bước (rebuild.sh làm hết)
1. Giải nén `goc/game.jar`; `respack.py unpack` gói `level-9` + `res0..30` (khúc 10000 byte).
2. `vh_images.py` vẽ lại `menu_string.png`, `menu_ingamestring.png` (pixeltext 13c) → `vh_res.py` chép vào gói.
3. `inject.py out/text_can_dich.tsv` → nạp map*.map (`mapfmt.py`), l9.pro (`profmt.py`) rồi đóng lại gói res, chuỗi trong .class (`cpool.py`).
4. `patch_classes.py`: đổi lời gọi `Graphics.drawString`, `Font.stringWidth/charWidth/getHeight` → `VH.*`; `d.a(String,Font,int)` → `VH.wrap`;
   thay thân `k.a(BBB)/k.w()/k.d(Graphics)` bằng lời gọi `VH.open/tick/paint`; `XMessage`: `Display.setCurrent` → `VL.show`;
   `d.class` "测" → "MM" (đơn vị bề rộng 12px); TextBox nhập tên tối đa 3 → 5 ký tự.
5. `build_hook.sh` (ecj -source/-target 1.3) biên dịch VH, VL; thêm BitmapFont.class, font/f13c.bin, vh_logo.png, head_0.png (`vh_head.py`).
6. `preverify.sh` (ProGuard -microedition) → `jar_check.py` (PASS) → `make_jad.py`.

Dịch lại/sửa câu: sửa cột `dich` trong `out/text_can_dich.tsv` (hoặc `dp fix` trong `dp_work` rồi `dp merge` + `scripts/post_merge.py`), chạy `bash rebuild.sh`.
Thay chân dung khác: đặt `assets/src/head_<id>_src.png` (nền trong suốt) rồi rebuild; id theo mảng `l.I` (0 = nhân vật chính).

Cần: bash, python3 + Pillow (+ python-xlib cho run_emu), JDK đầy đủ có `jmods/java.base.jmod`, zip/unzip, Xvfb + ImageMagick (test).

# Tiến độ: Việt hoá 霸王怒与美人泪 (Bá Vương Nộ Dữ Mỹ Nhân Lệ) – N73 240x320

## Quyết định (Bước 0, người dùng chốt)
- Phong cách: kiếm hiệp Hán Việt (tại hạ/huynh/đệ/sư phụ), tên riêng phiên âm Hán Việt (小风 → Tiểu Phong).
- Font: **15c** (15px chân chọn lọc) qua BitmapFont. Không thêm chân dung mới (giữ 5 chân dung nhỏ em_001..005 có sẵn).
- Khung thoại theo ảnh mẫu: nền tối bán trong suốt viền vàng, tab tên trên-trái, 3 dòng/trang. Splash logo "Việt hoá bởi NAMP".

## Mapping / phát hiện
- Text chỉ nằm trong constant pool .class: a b c e f g h i u = bảng thoại String[][] {portrait, sprite, emo, "[Tên]:câu"}; o = UI.
  data/*.dat là dữ liệu nhị phân (không chữ). `_bb` = blob mã hoá logo nhà phát hành, không kiểm jar.
- Vẽ chữ: o dùng Font.getFont(0,0,8); o.o = stringWidth("宽") (đơn vị bề rộng chữ Hán), o.p = getHeight().
- Ngắt dòng theo ký tự: o.a(String,int,int)->eO[].a ; o.a(String[],int,int)->eP[] (t: a,b,c byte, d String[6], e int) ; o.c(String,int)->bP[].
- Vẽ nhiều dòng theo số ký tự cố định: o.a(String,nChars,x,y,color,anchor).
- Khung thoại: o.aI() nhánh eS==1: vùng vẽ khung pc 122..367, vùng chân dung+chữ+nhãn skip pc 797..1166 (màu chữ = local 8).
  Phân trang o.ag(int): `eP.e > 2`, `F*2 < e` -> đổi 3.
- Splash: ForeverHero._d_d() Display.setCurrent -> VL.show (chỉ lần đầu).

## Cách chạy
- `source work/tools/env.sh`; `bash rebuild.sh` (inject -> patch_classes -> hook -> preverify -> jar_check -> jad).
- Test: `python3 -I scripts/run_emu.py --fj $FJ --jar out/... --size 240x320 --steps "..."`; phím: q = soft trái, Return = chọn.

## Trạng thái
- [x] Kiểm kê, decompile, trích text (work/text_goc.tsv, 3637 dòng / 3618 duy nhất / 89k chữ Hán), roundtrip byte OK.
- [x] Hook VH (font 15c, wrap theo từ, dsn) + khung thoại mới + splash VL — rebuild.sh PASS
- [x] Dịch dp 3618/3618 (out/text_can_dich.tsv); dp check chỉ còn 33 'dài' ước lượng ở nhãn UI o.class -> soát bằng ảnh chụp
- [x] Ảnh có chữ: 29 ảnh (scripts/vh_images.py, toạ độ ô từ o.java); giữ logo tiêu đề/nhà phát hành/biển hiệu bản đồ
- [x] Test FreeJ2ME các màn chính (out/shots), dp check OK 3618/3618, rebuild tái lập, bàn giao zip

## Lỗi đã gặp
- run_emu.py của skill truyền sai thứ tự tham số FreeJ2ME -> dùng bản sửa từ dự án magioi.

- dp.py: CODE0 thẻ <...> chỉ nhận ASCII (để <Người giao>/<Vị trí> không bị coi là mã); '~' không phải mã (font không có '~', dịch '1~5' -> '1-5').
- class t (eP) bị đánh dấu inner của o -> build_hook.sh biên dịch với bản class đã bỏ InnerClasses (scripts/strip_inner.py).

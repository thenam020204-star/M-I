# Bộ tool — lệnh cụ thể

Sau `bash scripts/setup_tools.sh <zip> work && source work/tools/env.sh` (cần bash, không phải sh):

```
work/tools/
  fonts/           f18 (gốc), f15 f13 (có chân), f15c f13c (chân chọn lọc), f15s f13s (không chân): nguồn font duy nhất
  fontgen/         build_fonts.py: sinh lại f15/f13 từ f18 (chỉ khi đổi f18 / sửa glyph)
  java/            BitmapFont.java, BitmapFont.class, build_class.sh, test/ (FontDemo + stubs)
  pixeltext-kit/   vẽ chữ pixel tiếng Việt vào ảnh (PYTHONPATH đã trỏ sẵn; đọc ../fonts)
  check_all.sh     kiểm tra từng phần: font (+ sinh lại khớp f18), selftest, ví dụ, class, FontDemo, jar freej2me
  freej2me_plus.jar  ($FJ)   cfr.jar ($CFR)   proguard.jar ($PROGUARD)
```

`env.sh` còn đặt `$BITMAPFONT_CLASS`, `$BITMAPFONT_JAVA`, `$FONT_DIR`. Chạy `sh work/tools/check_all.sh` sau khi setup để biết tool lành. Bộ tool cũ dùng `fontcus` (`fontcus.java`, `font_15px.bin`, `gen_class.py`, `stripsmt.py`, `fontbin.py`) không còn được hỗ trợ: `setup_tools.sh` dừng với thông báo nếu zip không có `BitmapFont.class`.

Font (giá trị `--size` của pixeltext): **`18`, `15`, `13`** (có chân), **`15c`, `13c`** (chân chọn lọc), **`15s`, `13s`** (không chân). Bảng đặc tính, API Java, yêu cầu MIDP 2.0 và bộ nhớ: [bitmapfont.md](bitmapfont.md).

## Font trong game: BitmapFont

Đưa `$BITMAPFONT_CLASS` vào gốc jar và `$FONT_DIR/f<cỡ><kiểu>.bin` (đúng font cần, vd. `f13c.bin`) vào `font/` trong jar, rồi gọi API trong [bitmapfont.md](bitmapfont.md). Không có bước biên dịch nào cho tới khi cần đổi `CACHE` (xem mục Bộ nhớ ở file đó). Sau khi đóng jar luôn chạy `scripts/preverify.sh` rồi `scripts/jar_check.py`.

## pixeltext (chữ trong ảnh)

Kit trong cây sạch đã đọc đúng `f*.bin` ở `../fonts` (biến `PIXELTEXT_FONTS` ghi đè), `selftest.py` chạy được (72 kiểm tra). Mọi chỗ nhận `size` dùng `13`, `'13c'`, `'13s'`… `setup_tools.sh` chỉ còn vá `font.py`/`cli.py` khi gặp bộ zip lồng cũ.

```bash
python3 -m pixeltext check   --text "Bắt đầu" --size 13c      # thiếu glyph / glyph trùng -> exit 1 (mặc định: mọi font)
python3 -m pixeltext measure --text "Bắt đầu" --size 15c       # --size all: đủ 7 font
python3 -m pixeltext fit     --text "Nhiệm vụ" --box 70x17 --sizes 13c,15c   # font lớn nhất lọt ô
python3 -m pixeltext show    --text "Bắt đầu" --size 13s       # xem bằng ASCII
python3 -m pixeltext audit                                     # glyph trùng nhau theo từng cỡ
python3 -m pixeltext patch   --image tab.png --box 6,4,70,17 --text "Nhiệm vụ" --size 13c --out tab_vi.png
```

Trong Python: `pt.patch(img, box, text, fill_rule="mode")` lấy một màu thân chữ (bỏ viền), đúng quy tắc mặc định. `fill_rule="bright"` giữ gradient, chỉ dùng khi người dùng muốn. Ép font đã chọn bằng `size=` (vd. `size='13c'`); `fit()` chỉ dùng để kiểm xem có lọt ô không (nó ưu tiên cỡ lớn > có chân > chọn lọc > không chân, nên truyền `sizes=` đúng kiểu người dùng chọn). Khi ảnh có nền bán trong suốt, giữ nguyên kênh alpha của pixel nền.

## FreeJ2ME headless — `scripts/run_emu.py`

- Phím: `q`/`w` = soft trái/phải, `Return` = OK, mũi tên, `0`–`9`. Màn hỏi bật nhạc: chọn tắt để tránh lỗi MIDI của môi trường.
- `--size 240x320` phải khớp độ phân giải game (xem kích thước ảnh nền, ảnh bìa, hoặc hằng số trong canvas).
- Kết quả: ảnh trong `--out` (đã cắt đúng màn game) + `emu.log`; JSON in ra có danh sách exception.
- Muốn đi sâu vào game: xâu chuỗi `key ...; wait ...; shot ...`, chụp từng màn để soát chữ.

## Đọc code

- `env -u JAVA_TOOL_OPTIONS java -jar $CFR game.jar --outputdir src --silent true` để decompile.
- `javap -c -p -constants X.class` để xem bytecode thật (tin vào đây khi decompile nghi sai). Cần JDK đầy đủ; `preverify.sh` cũng cần JDK có `jmods/java.base.jmod` (JRE thiếu thì cài `openjdk-21-jdk-headless`).
- Sửa code: viết lại method rồi biên dịch theo cách của `java/build_class.sh` (ecj `-source 1.3 -target 1.3`, rồi ProGuard `-microedition`), hoặc sửa bytecode bằng ASM (`/opt/*/lib/asm-*.jar` nếu có). Class mới phải có version ≤ 48, không `StackMapTable`, và được preverify.

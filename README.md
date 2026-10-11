# Tool Việt Hoá J2ME — NAM.P (cây sạch)

Bộ font lấy **`f18.bin` làm chuẩn**; mọi cỡ nhỏ hơn được nén ra từ nó, mỗi cỡ có 2 biến thể (ảnh: `fonts_preview.png`):

| File | Cỡ | Kiểu | Chữ hoa | x-height | Rộng | Dấu cách | `"Trò chơi mới"` |
|---|---|---|---|---|---|---|---|
| `f18.bin`  | 18 | có chân — **bản gốc** | 10 | 6 | cố định 7 | 5 | 80 px |
| `f15.bin`  | 15 | có chân (nét f18)     | 8  | 5 | cố định 7 | 5 | 80 px |
| `f13.bin`  | 13 | có chân (nét f18)     | 7  | 5 | cố định 7 | 5 | 80 px |
| `f15s.bin` | 15 | không chân, gọn       | 8  | 5 | theo nét  | 4 | 56 px |
| `f13s.bin` | 13 | không chân, gọn       | 7  | 5 | theo nét  | 3 | 54 px |

```
fonts/            f18.bin (gốc) + f15/f13 (có chân) + f15s/f13s (không chân)
fontgen/          build_fonts.py (sinh f15/f13 từ f18), sans18.py (glyph không chân vẽ tay ở lưới 18px)
java/             BitmapFont.java, BitmapFont.class (Java 1.3, preverify), build_class.sh, test/ (FontDemo + stubs)
pixeltext_kit/    pixeltext/ (Python vẽ chữ pixel), examples/, selftest.py
freej2me/         freej2me_plus.jar, freej2me_plus-lr.jar, Win32Pad.exe
legacy/           script/tài liệu CŨ của bộ độ rộng cố định 6 (không còn dùng)
check_all.sh      chạy kiểm tra từng phần
```

## Định dạng `.bin`
`writeUTF(charset)` · `byte height` · `byte width[227]` · atlas 1bpp (theo hàng, LSB-first, rộng = tổng width, liên tục giữa các hàng).
Cả 5 file dùng chung charset 227 ký tự của f18.

## Cách sinh f15 / f13 (`fontgen/build_fonts.py`)
```
python3 fontgen/build_fonts.py            # ghi lại fonts/f15.bin f13.bin f15s.bin f13s.bin
python3 fontgen/build_fonts.py --check18  # tự kiểm: ghép lại f18 từ chữ gốc + dấu -> 227/227 khớp f18.bin
python3 fontgen/build_fonts.py --sheet    # in bảng ASCII mọi glyph vừa sinh
```
- Lưới 18 hàng chia vùng `dấu (0-4) | trên x (5-8) | x-height (9-14) | phần dưới (15-17)`,
  nén thành `5|3|5|2` (15px) và `4|2|5|2` (13px). Mỗi chữ gốc chọn tổ hợp hàng bỏ đi ít mất nét nhất
  (bỏ hàng lặp trước, không làm đứt/dính nét, chia đều hai nửa của B, H, 8…). Chữ hoa, số, ký hiệu
  được bỏ tự do trong thân chữ (10 hàng → 8 / 7). Vài chữ chọn tay trong `BASE_OVERRIDES`.
- Chữ có dấu được **ghép lại**: chữ gốc đã nén + dấu sắc/huyền/hỏi/ngã/nặng, mũ, trăng, móc của f18,
  đặt theo bố cục từng cỡ (13px chữ hoa chỉ còn 4 hàng cho dấu nên dấu kép sát đầu chữ, dấu hỏi rút còn 2 hàng).
- Bản **không chân**: glyph sans vẽ tay ở lưới 18px (`fontgen/sans18.py`), nén cùng thuật toán, cắt sát
  rồi cộng 1 cột cách; chữ số giữ cùng độ rộng để canh điểm số.
- Muốn sửa glyph: sửa `sans18.py` / `BASE_OVERRIDES` rồi chạy lại script và `check_all.sh`
  (bước 1b báo lỗi nếu file `.bin` trong `fonts/` khác với bản sinh lại).

## Dùng trong game
Chép `java/BitmapFont.class` vào gốc jar và các `fonts/*.bin` cần dùng vào `/font/` trong jar.
```java
BitmapFont.setDefault(BitmapFont.SIZE_13, BitmapFont.SANS);   // 13px không chân (f13s.bin)
BitmapFont f = BitmapFont.get(BitmapFont.SIZE_15);             // 15px có chân (f15.bin)
```
Cỡ khác được làm tròn về cỡ gần nhất (≥17 → 18, 14–16 → 15, ≤13 → 13); 18 chưa có bản không chân nên `SANS` ở 18 dùng f18.
Mã mẫu đầy đủ: comment đầu `BitmapFont.java`.

## Dùng pixeltext (vẽ chữ lên ảnh)
```
cd pixeltext_kit
python3 -m pixeltext check   --text "Trò chơi mới"
python3 -m pixeltext fit     --text "Trò chơi mới" --box 64x16
python3 -m pixeltext patch   --image tab.png --box 6,4,70,17 --text "Nhiệm vụ" --size 13s --out tab_vi.png
python3 selftest.py
```
`--size` nhận `18`, `15`, `13`, `15s`, `13s` hoặc `all`. Font được tìm ở `../fonts` (hoặc biến môi trường `PIXELTEXT_FONTS`).
Hướng dẫn đầy đủ: `pixeltext_kit/README.md`.

## Biên dịch lại BitmapFont.class
`sh java/build_class.sh` (cần JDK 9+; tự dùng ecj/proguard có sẵn hoặc tải từ GitHub/Maven). Tự kiểm class version = 47.

## Lưu ý
- Bản có chân giữ nguyên thiết kế f18: móc của `ơ ư Ơ Ư` và gạch `đ` chạm cột thứ 7 (cột cách chữ).
- Chỉ còn một cặp glyph trùng nhau: `,` / `‚`.

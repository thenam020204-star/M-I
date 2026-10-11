# Font bitmap tiếng Việt 18 / 15 / 14 / 13 / 12 / 11 / 10 px

| File | Chiều cao | Chữ hoa | x-height | Đuôi | Độ rộng ký tự |
|---|---|---|---|---|---|
| `f18.bin` | 18 | 10 | 6 | 3 | 7 (dấu cách 5) |
| `f15.bin` (gốc) | 15 | 8 | 5 | 2 | 6 (dấu cách 4) |
| `f14.bin` | 14 | 8 | 5 | 2 | 6 (dấu cách 4) |
| `f13.bin` | 13 | 7 | 5 | 2 | 6 (dấu cách 4) |
| `f12.bin` | 12 | 6 | 5 | 2 | 6 (dấu cách 4) |
| `f11.bin` | 11 | 6 | 5 | 2 | 6 (dấu cách 4) |
| `f10.bin` | 10 | 6 | 5 | 1 | 6 (dấu cách 4) |

Mọi file có cùng bảng 227 ký tự. Font 15px giữ nguyên bản gốc.

### 14 / 13 / 12 / 11 / 10 px — nén từ f15, giữ nguyên nét (`tools/derive_from15.py`)
Không vẽ lại chữ, không đổi độ rộng: chỉ bỏ hàng theo chiều dọc.

| Cỡ | Vùng dấu | Chữ hoa | x-height | Đuôi | Thay đổi so với f15 |
|---|---|---|---|---|---|
| 15 | 5 | 8 | 5 | 2 | gốc |
| 14 | 4 | 8 | 5 | 2 | thân chữ y nguyên; dấu chồng của chữ hoa (Ấ Ở…) đặt sát đỉnh chữ |
| 13 | 4 | 7 | 5 | 2 | bỏ 1 hàng lặp (nét đứng) ở chữ hoa/số; phần trên của b d h k l… thấp 1 hàng |
| 12 | 4 | 6 | 5 | 2 | bỏ 2 hàng lặp; dấu hỏi chồng dùng dạng 2 hàng như Ẩ của f15 |
| 11 | 3 | 6 | 5 | 2 | như 12; 16 chữ hoa 2 dấu chồng (ẮẰẲẴ ẤẦẨẪ ẾỀỂỄ ỐỒỔỖ) có thân thấp 1 hàng; dấu chồng chữ thường sát đỉnh chữ |
| 10 | 3 | 6 | 5 | 1 | như 11; đuôi g j p q y , ; Q J… bớt 1 hàng lặp; dấu nặng sát chân chữ |

12px là cỡ nhỏ nhất mà mọi chữ vẫn giữ y nét và vị trí dấu của f15.
11/10px vẫn đủ dấu, tách rời, không vẽ dấu mới; đánh đổi là chữ hoa thấp (11, 10) và đuôi ngắn (10).

- Chữ thường giữ nguyên x-height và phần đuôi ở mọi cỡ; dấu là bộ dấu tách thẳng từ f15.
- Hàng bỏ được chọn tự động (hàng trùng hàng kề), các chữ cần mắt người thì chọn tay
  trong `OVERRIDE` / `MERGE` (vd. E, G, K, t, đ, i, j).
- Script tự kiểm: dựng lại f15 từ chữ gốc + dấu phải trùng 100% file gốc.
- Bản 13px cũ (vẽ tay, `glyphs_13.py`) đã thay bằng bản nén từ f15.
- Bản 11px cũ (vẽ tay, x-height 4) vẫn sinh được bằng `tools/build_fonts.py`; nó ghi ra `f11.bin`
  ở gốc kit, không đụng `dist/font/f11.bin` (bản nén từ f15).

## Định dạng `.bin`
`writeUTF(charset)` · `byte height` · `byte width[n]` · atlas bit (theo hàng, LSB-first, rộng = tổng width).

## Bản đã biên dịch (`dist/`)
- `dist/BitmapFont.class` — Java 1.3 (class 47), đã preverify (StackMap) cho CLDC 1.0 / MIDP 2.0, giống các class trong game.
- `dist/font/f15.bin, f14.bin, f13.bin, f12.bin, f11.bin, f10.bin` (f18: chạy `tools/build_fonts.py`)

Chép `BitmapFont.class` vào gốc jar (package mặc định) và thư mục `font/` vào gốc jar.
Biên dịch lại: `sh tools/build_class.sh`.

## Dùng trong game
Đặt các file `.bin` vào jar ở `/font/` (hoặc gọi `BitmapFont.setResourceDir("/data/")`), thêm `BitmapFont.java` vào mã nguồn.

```java
BitmapFont.setDefault(BitmapFont.SIZE_13);      // 18, 15, 14, 13, 12, 11 hoặc 10
BitmapFont f = BitmapFont.current();
g.setColor(0xFFFFFF);
f.drawString(g, "Xin chào", x, y, Graphics.TOP | Graphics.LEFT);
f.drawString(g, "Giữa", w / 2, y, Graphics.TOP | Graphics.HCENTER);
f.drawOutlined(g, "Viền", x, y, 0, 0xFFFFFF, 0x000000);
int h = f.drawWrapped(g, hoiThoai, x, y, 160, 2, Graphics.LEFT); // tự xuống dòng
BitmapFont.get(BitmapFont.SIZE_18).drawString(g, "TIÊU ĐỀ", x, y, 0); // dùng song song nhiều cỡ
```

## Sửa / sinh lại
```sh
python3 tools/derive_from15.py      # tạo dist/font/f14..f10.bin + preview_14_13_12.png, preview_12_11_10.png
python3 tools/derive_from15.py dump # in glyph dạng #/. để soát
python3 tools/build_fonts.py        # tạo f18/f11.bin + preview_charset.png
python3 tools/build_fonts.py dump   # in toàn bộ glyph dạng #/. để soát
javac -encoding UTF-8 --release 8 -d out test/stubs/javax/microedition/lcdui/*.java BitmapFont.java test/FontDemo.java
java -cp out FontDemo . preview_demo.png
```
Sửa 14..10: đổi hàng bị bỏ trong `OVERRIDE` / `DESC_OVERRIDE` / `LOWER` (toạ độ hàng của f15) ở `tools/derive_from15.py`.
Sửa 18/11: đổi các dòng `#`/`.` trong `tools/glyphs_18.py`, `glyphs_11.py`; dấu ở `MARKS_*` trong `tools/build_fonts.py`.

# BitmapFont — font tiếng Việt chạy trong game

Bộ font hiện hành (thay `fontcus`). Sau `setup_tools.sh`, mọi thứ nằm ở `$J2ME_TOOLS/font/`:

| Đường dẫn | Là gì |
|---|---|
| `java/BitmapFont.class` (`$BITMAPFONT_CLASS`) | Class đã biên dịch, class version 47, đã preverify (có `StackMap`, không `StackMapTable`). Đưa thẳng vào gốc jar (package mặc định) |
| `java/BitmapFont.java` (`$BITMAPFONT_JAVA`) | Mã nguồn, chỉ sửa khi phải đổi `CACHE` (xem Bộ nhớ) |
| `fonts/*.bin` (`$FONT_DIR`) | Dữ liệu font, 227 ký tự giống nhau ở mọi file. **Nguồn duy nhất**: `pixeltext-kit` cũng đọc đúng thư mục này. 7 file: `f18` (gốc) · `f15 f13` (có chân) · `f15c f13c` (chân chọn lọc) · `f15s f13s` (không chân) |
| `fontgen/build_fonts.py` | Sinh lại f15/f13 cả 3 kiểu từ `f18.bin` (nén chiều cao + ghép dấu). Chỉ chạy khi người dùng đổi f18 hoặc nhờ sửa glyph; `--check18` tự kiểm (dựng lại f18 khớp 227/227) |
| `java/build_class.sh` | Biên dịch + preverify lại `BitmapFont.class` (chỉ khi sửa `BitmapFont.java`); tự dùng ecj/ProGuard có sẵn hoặc tải từ GitHub/Maven, tự kiểm class version 47 |

`pixeltext-kit` đọc cùng thư mục `fonts/`, nên chữ vẽ trong ảnh và chữ chạy trong game trùng nhau. Không còn bản font thứ hai để lệch nhau.

Bộ font cũ `f10 f11 f12 f14` và f13/f15 tỉ lệ cũ đã bị xoá; dự án cũ còn gọi `SIZE_14`, `SIZE_12`… sẽ không biên dịch được với `BitmapFont.java` mới, đổi sang 13/15. Bộ tool cũ hơn (`dist/font/`, `tools/derive_from15.py`) cũng bỏ.

## Các font

`f18.bin` là chuẩn; f15/f13 nén từ nó (số liệu từ `python3 -m pixeltext audit` / `measure`):

| Font (`--size`) | Kiểu | Chữ hoa | x-height | Rộng ký tự | Dấu cách | `"Trò chơi mới"` |
|---|---|---|---|---|---|---|
| `18` | có chân, bản gốc | 10 | 6 | cố định 7 | 5 | 80 px |
| `15` | có chân (nét f18) | 8 | 5 | cố định 7 | 5 | 80 px |
| `13` | có chân (nét f18) | 7 | 5 | cố định 7 | 5 | 80 px |
| `15c` | chân chọn lọc | 8 | 5 | theo nét 2–7 | 4 | 58 px |
| `13c` | chân chọn lọc | 7 | 5 | theo nét 2–7 | 3 | 56 px |
| `15s` | không chân | 8 | 5 | theo nét 2–7 | 4 | 56 px |
| `13s` | không chân | 7 | 5 | theo nét 2–7 | 3 | 54 px |

- **Chân chọn lọc** = không chân, riêng `I` (mũ + đế), `i ı j l` (cờ trên-trái), `J 1 4` (cờ/đế trong khung) giữ chân. Dễ đọc hơn không chân (`Il1` không lẫn), gần như không tốn chỗ. Là lựa chọn mặc định hợp lý cho khung thoại.
- **Có chân** đẹp và giống bản gốc nhất nhưng rộng hơn ~40%; hợp tiêu đề, menu ngắn, game có font gốc rộng.
- **Không chân** gọn nhất; dùng khi ô chữ chật.
- Chữ số của bản `s`/`c` cùng độ rộng (canh điểm số được).
- Mọi font vẽ được dấu trên chữ HOA. Ở 13px phần dấu chữ hoa chỉ có 4 hàng: dấu kép (Ấ Ầ Ẩ Ẫ, Ắ…) sát đỉnh chữ, dấu hỏi trên mũ rút còn 2 hàng.
- Glyph trùng: chỉ `,`/`‚`. `0` có gạch chéo, khác `O`.
- Bản có chân giữ thiết kế f18: móc `ơ ư Ơ Ư` và gạch `đ` chạm cột thứ 7 (cột cách chữ). Vá vào bảng glyph 8×16 cần cột 7 trống thì dùng bản `s`/`c`.

Lấy **file làm chuẩn**: đo bằng `pixeltext measure --size <font>` hoặc `stringWidth`. Nếu độ rộng làm vỡ bố cục thì đổi kiểu (`c`/`s`) hoặc rút gọn câu và báo người dùng, đừng tự sửa font.

## Đưa vào game

```java
BitmapFont.setDefault(BitmapFont.SIZE_13, BitmapFont.SELECTIVE);   // f13c.bin; SERIF = f13.bin, SANS = f13s.bin
BitmapFont f = BitmapFont.current();
g.setColor(0xFFFFFF);
f.drawString(g, "Xin chào", x, y, Graphics.TOP | Graphics.LEFT);
f.drawOutlined(g, "Viền", x, y, 0, 0xFFFFFF, 0x000000);
String[] lines = f.wrap(hoiThoai, 160);                   // ngắt dòng theo px
int w = f.stringWidth("Chuỗi");   int n = f.lineCount(hoiThoai, 160);
BitmapFont.get(BitmapFont.SIZE_18).drawString(g, "TIÊU ĐỀ", x, y, 0);   // nhiều font song song; get(size) = có chân
```

- Jar cần: `BitmapFont.class` ở gốc, `font/f<cỡ><kiểu>.bin` cho đúng các font dùng (vd. chỉ `font/f13c.bin`; hoặc `BitmapFont.setResourceDir("/data/")`). Chỉ đóng font nào thực sự dùng.
- `get(size, style)` (style = `SERIF` / `SANS` / `SELECTIVE`; `get(size)` = `SERIF`) nạp lười lần đầu, trả `null` nếu thiếu file. Cỡ lạ làm tròn về cỡ gần nhất (≥17 → 18, 14–16 → 15, ≤13 → 13); kiểu chưa có (18px chỉ có bản có chân) rơi về có chân. Kiểm `null` ở chỗ gọi.
- `drawString` nhận anchor như `Graphics.drawString` và hiểu `\n`; `drawOutlined` vẽ viền theo cả chuỗi (5 lần `drawString`), nên không còn vấn đề viền đè nét theo từng ký tự như renderer cũ.
- Ký tự không có trong bảng: `charWidth` trả bề rộng dấu cách và `drawChar` không vẽ gì. Luôn chạy `pixeltext check` trên bản dịch để bắt ký tự thiếu trước khi build.
- `wrap` ngắt ở dấu cách, cắt từ dài hơn `maxW`, bỏ dấu cách đầu dòng và hiểu `\n`. Thay hàm ngắt dòng theo từng ký tự của game bằng `wrap`/`lineCount`; phân trang khung thoại dùng `lineCount` hoặc `wrap().length`.

## Tương thích

- **Cần MIDP 2.0**: class gọi `Graphics.drawRegion`, `Graphics.drawRGB`, `Image.createRGBImage`. Game MIDP 1.0 thuần (chỉ `javax.microedition.lcdui` bản 1.0) không dùng được; khi đó ghép glyph Việt vào font gốc của game. `jar_check` sẽ in `WARN` cho các API này, đó là bình thường, nhưng phải đối chiếu với profile của máy đích.
- Định dạng `.bin`: `writeUTF(charset)`, `byte height`, `byte width[n]`, atlas 1bpp theo hàng, LSB-first, liên tục giữa các hàng. `pixeltext/container.py` (`decode`/`encode`) đọc ghi được định dạng này; dùng khi viết script ghép glyph vào font gốc của game (font gốc thường có định dạng khác, phải đọc theo code game).
- Không vẽ hay tạo glyph mới. Ký tự thiếu thì liệt kê và xin người dùng.

## Bộ nhớ (đọc trước khi chọn font cho máy cũ)

Mỗi lần cần một màu chữ mới, `BitmapFont` dựng một ảnh atlas ARGB cả bảng chữ (`atlasW × height × 4` byte) và giữ trong cache LRU `CACHE = 4` màu **cho mỗi font đã nạp**. Lúc dựng còn cần thêm một `int[]` tạm cùng kích thước.

| Font | 1 màu (KB) | đỉnh khi dựng (KB) | đủ 4 màu (KB) |
|---|---|---|---|
| 13s / 13c | 63 | 126 | 252 |
| 15s / 15c | 73 | 146 | 292 |
| 13 | 81 | 161 | 322 |
| 15 | 93 | 186 | 372 |
| 18 | 112 | 223 | 446 |

(Bản có chân rộng cố định 7 nên atlas to hơn bản `s`/`c` ~22%.) Trên máy heap vài trăm KB thì `CACHE = 4` là quá nhiều:

- Thiếu heap thì đổi sang bản `c`/`s` cùng cỡ trước (nhẹ hơn ~22%, chữ vẫn cao như cũ), rồi mới tính giảm cỡ.

- Dùng ít màu chữ nhất có thể, và ít font nạp cùng lúc; gọi `BitmapFont.unload(size)` (bỏ mọi kiểu của cỡ đó) cho cỡ không còn dùng, `clearCache()` khi đổi sang màn dùng bộ màu khác.
- Hạ `CACHE` xuống 1–2 trong `BitmapFont.java` rồi `sh java/build_class.sh` (cần JDK 9+ có `jmod`; script tự tìm ecj ở `/usr/share/java/ecj.jar` hoặc tải, lấy ProGuard từ GitHub release; đã kiểm: dựng lại `BitmapFont.java` gốc ra class trùng từng byte với bản đang giao). Sau đó `preverify.sh` + `jar_check.py` như mọi lần.
- Thiếu bộ nhớ thì ưu tiên chuyển sang bản `s`/`c` trước khi giảm cỡ. `atlas()` bắt `Throwable`, đặt `atlasOk = false` và chuyển sang vẽ từng glyph bằng `drawRGB`. Đường này **cấp phát một `int[]` cho mỗi ký tự vẽ**, rất dễ gây GC giật hình; coi nó là dấu hiệu phải giảm cỡ hoặc số màu, không phải trạng thái chấp nhận được.
- `wrap`, `lineCount`, `drawWrapped` cấp phát `Vector`, `String[]` và `substring` mỗi lần gọi. Gọi **một lần khi chuỗi hoặc độ rộng đổi**, giữ `String[]` trong field, rồi `paint()` chỉ `drawString` từng dòng. `drawWrapped` chỉ dùng cho màn tĩnh.

# pixeltext — bộ công cụ vẽ chữ pixel tiếng Việt cho AI

Vẽ chữ tiếng Việt có dấu bằng font bitmap `fontcus`, **3 cỡ 18 / 15 / 13 px, 3 biến thể**:
`18` `15` `13` = có chân (nét của f18, rộng cố định 7) · `15s` `13s` = không chân (rộng theo nét, gọn hơn) · `15c` `13c` = chân chọn lọc (như không chân, riêng `I i j l J 1 4` giữ chân).
Đo trước khi vẽ, tự chọn cỡ vừa ô, xem bằng ASCII art, và vá chữ vào ảnh có sẵn mà giữ màu gốc.
File font nằm ở `../fonts/f{cỡ}.bin` (vd. `f13s.bin`). Ở mọi chỗ nhận `size`, dùng `13` hoặc `'13s'`.

Yêu cầu: Python 3.8+, Pillow. Không cần cài đặt, `pixeltext/` nằm cạnh script.

```
pixeltext/   container.py (đọc/ghi font.bin) · font.py · metrics.py · paint.py · cli.py
examples/    demo.py, game_menu.py
selftest.py  72 kiểm tra
```

## 1. Các cỡ font

| cỡ | kiểu | cao | chữ hoa | x-height | baseline | rộng | dấu cách |
|---|---|---|---|---|---|---|---|
| 18 | có chân (gốc) | 18 | 10 | 6 | 14 | cố định 7 | 5 |
| 15 | có chân | 15 | 8 | 5 | 12 | cố định 7 | 5 |
| 13 | có chân | 13 | 7 | 5 | 10 | cố định 7 | 5 |
| 15s | không chân | 15 | 8 | 5 | 12 | theo nét (2–7) | 4 |
| 13s | không chân | 13 | 7 | 5 | 10 | theo nét (2–7) | 3 |
| 15c | chân chọn lọc | 15 | 8 | 5 | 12 | theo nét (2–7) | 4 |
| 13c | chân chọn lọc | 13 | 7 | 5 | 10 | theo nét (2–7) | 3 |

Bề rộng `Trò chơi mới`: 80 px (18 / 15 / 13) · 56 px (15s) · 54 px (13s) · 58 px (15c) · 56 px (13c).
Chữ hoa có dấu, `0`/`O`, `I`/`l`/`1` đều phân biệt được ở mọi font. Chỉ còn trùng glyph `,`/`‚` — luôn chạy `check` trước khi chốt chuỗi.
`fit` ưu tiên: cỡ lớn hơn > khoảng cách chuẩn (metric) > có chân > chân chọn lọc > không chân.

---

## 2. Dùng bằng dòng lệnh (khuyên dùng cho AI)

```bash
# chữ này có vẽ được không? có bị nhầm glyph không?
python3 -m pixeltext check   --text "Trò chơi mới"

# rộng bao nhiêu px ở mọi cỡ?
python3 -m pixeltext measure --text "Trò chơi mới" --size all

# xem thẳng bằng ASCII, không cần mở ảnh
python3 -m pixeltext show    --text "Trò chơi mới" --size 13s

# cỡ nào lớn nhất mà vẫn lọt ô 64x16?
python3 -m pixeltext fit     --text "Trò chơi mới" --box 64x16
# -> {"size": "15c", "mode": "metric", "gap": 1, "w": 58, "h": 15, ...}

# xuất PNG
python3 -m pixeltext draw    --text "Trò chơi mới" --size 13 --scale 4 \
        --gradient "#9ef7ff,#1f7fd0" --outline "#04121f" --shadow "#000000a0" \
        --out title.png

# mọi cỡ cạnh nhau
python3 -m pixeltext compare --text "Trò chơi mới" --out compare.png

# bảng ký tự đầy đủ
python3 -m pixeltext sheet   --size 13 --out sheet13.png

# thay chữ trong ô của một ảnh có sẵn, tự lấy lại màu nền/màu chữ/viền của ảnh
python3 -m pixeltext patch --image tab.png --box 6,4,70,17 --text "Nhiệm vụ" --auto --out tab_vi.png
```

Mọi lệnh trả JSON ra stdout nên agent parse được. `check` và `fit` trả **exit code 1**
khi có vấn đề (thiếu glyph / glyph trùng / không lọt ô).

---

## 3. Dùng bằng Python

```python
import pixeltext as pt

pt.measure("Trò chơi mới", 13)          # {'w': 80, 'h': 13, 'lines': 1, ...}
pt.measure("Trò chơi mới", '13s')       # {'w': 54, ...}  -> bản không chân gọn hơn
pt.Font.load(13).missing("Xin chào ★")  # ['★']
pt.Font.load(13).ambiguous_in(",‚")     # [(',', '‚'), ('‚', ',')]

# 1) ảnh mới
img = pt.render("Trò chơi mới", size=15, scale=3,
                fill=["#ffe9a3", "#ff8a3c"],       # gradient trên->dưới
                outline="#2b1200", shadow="#00000080")
img.save("title.png")

# 2) vẽ vào ảnh có sẵn, canh trong ô
info = pt.draw(canvas, box=(8, 40, 120, 16), text="Bắt đầu", size=13,
               align="center", valign="center", fill="#7CFF6B", outline="#0a3d14")
info["overflow_x"]        # > 0 nghĩa là chữ tràn ô

# 3) tự chọn cỡ cho vừa ô rồi vẽ
plan = pt.fit("Nhiệm vụ chính", (70, 17))
pt.draw(canvas, (6, 4, 70, 17), plan["text"], size=plan["size"],
        mode=plan["mode"], gap=plan["gap"], scale=plan["scale"])

# 4) thay chữ trong artwork, giữ nguyên bảng màu + gradient + viền của ảnh gốc
pt.patch(ui, (6, 4, 70, 17), "Nhiệm vụ", fill_rule="mode")

# 5) xuống dòng theo bề rộng pixel
print(pt.wrap("Đội Red Hawk đã sẵn sàng, chờ lệnh xuất kích.", 200, 13))
```

### Ba chế độ giãn chữ (`mode`)

| mode | ý nghĩa | dùng khi |
|---|---|---|
| `metric` *(mặc định)* | dùng đúng bề rộng advance mà người vẽ font đặt | văn bản bình thường |
| `tight` | xếp sát theo nét thật + `gap` px | ô UI chật, cần nhét thêm 1–2 ký tự |
| `fixed` | mọi ký tự chiếm đúng `cell` px, canh giữa | bảng số, HUD cần thẳng cột |

### Màu

`fill` nhận: `"#RRGGBB"` / `"#RRGGBBAA"` / tuple RGBA / **list = gradient dọc**
(`["#fff","#08f"]`) / dict `{hàng: màu}` / hàm `f(y) -> RGBA`.
`outline`, `shadow` nhận màu đơn. `outline_diag=True` để viền cả 8 hướng.

---

## 4. Vá chữ vào artwork game (giữ nguyên bảng màu)

`sample_style()` đọc ngược từ chính ô ảnh: màu nền (pixel góc), màu chữ **theo từng hàng**
(giữ gradient dọc), màu viền (màu tối nhất). `patch()` xoá ô rồi vẽ lại bằng đúng bộ màu đó,
nên chữ mới trông như do cùng một hoạ sĩ vẽ.

```python
pt.patch(img, (0, 0, 38, 14), "Công", fill_rule="bright")  # giữ gradient
pt.patch(img, (0, 0, 199, 46), "THẤT BẠI", fill_rule="flat", scale=3,
         fill="#c60c0c", outline="bright")                 # banner lớn
```

`fill_rule`: `bright` (sáng nhất mỗi hàng — giữ gradient) · `mode` (màu phổ biến nhất —
bỏ qua viền) · `flat` (một màu cho cả khối).

---

## 5. Lưu ý khi đưa font vào máy J2ME thật

Nếu vẽ chữ để nhét vào bảng glyph 8×16 của engine J2ME (2bpp, đọc `w = (b0<<8)|b1`):
**cột 7 phải để trống**. `b0` bị sign-extend nên nếu bit 15 bật thì engine vẽ thừa
một cột đè lên ký tự kế tiếp. Mọi font ở đây rộng tối đa 7 px. Riêng bản có chân (18/15/13)
giữ nguyên thiết kế f18: móc của `ơ ư Ơ Ư` (và gạch `đ`) chạm cột 7 — dùng bản `s` hoặc `c` nếu cần cột 7 trống.

---

## 6. Tự kiểm tra

```bash
python3 selftest.py          # 72 assert: codec, metric, wrap, fit, render, patch
python3 examples/demo.py     # "Trò chơi mới" ở mọi cỡ + ASCII
python3 examples/game_menu.py  # màn hình menu game 240x320 dựng ở mọi cỡ
```

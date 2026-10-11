# font_10px_v2 — bộ 10px vẽ lại dấu tiếng Việt

Dựng từ `font_10px.bin` cũ. **Cùng container `fontcus` (font.bin), cùng chiều cao 10,
cùng charset và cùng thứ tự ký tự (227 glyph)** → nạp thẳng vào chỗ font 10px cũ.

| | 10px cũ | **10v2** |
|---|---|---|
| height / baseline / cap / x-height | 10 / 7 / 7 / 5 | **10 / 7 / 7 / 5 (giữ nguyên)** |
| glyph trùng nhau | **71 glyph, 17 nhóm** | **0** |
| chữ HOA có dấu | không vẽ dấu (`Á` = `A`) | có dấu đầy đủ |
| `ắ ằ ẳ ẵ` | vẽ y hệt nhau | 4 glyph khác nhau |
| `â=ẩ`, `ầ=ẫ`, `ê=ễ`, `ô=ỗ` | trùng | tách hết |
| atlas / file | 1264 px / 2264 B | 1303 px / 2312 B |
| glyph giữ nguyên bit-for-bit | — | 122 / 227 (A–Z, a–z, số, dấu câu, `đ Đ`) |
| glyph vẽ lại | — | 105 (toàn bộ nguyên âm có dấu) |

`Trò chơi mới` vẫn rộng **51 px** như cũ. Chỉ 27 glyph đổi advance (xem `diff_10px_v2.json`).

---

## 1. Ngân sách hàng — vì sao font 10px cũ không vẽ được dấu

Ô cao 10 hàng, và bố cục cũ đã dùng hết:

```
hàng 0-1   vùng dấu            (2 hàng)
hàng 2     hàng đệm bắt buộc   -> dấu không được chạm thân chữ
hàng 3-7   x-height            (a e o u ...)
hàng 1-7   chữ HOA             (7 hàng)  <-- chiếm luôn hàng 0? không, nhưng chỉ chừa 1 hàng
hàng 8-9   phần đuôi           (g p q y j)
```

Hệ quả: chữ HOA cao 7 hàng (1–7) chỉ còn **1 hàng** phía trên → không đủ chỗ cho dấu,
nên font cũ bỏ hẳn dấu chữ hoa. Chữ thường có 2 hàng nhưng không đủ để **xếp chồng**
mũ + thanh (`â` + sắc = `ấ`), nên font cũ vẽ `ấ`, `ầ`, `ẩ`, `ẫ` gần như giống nhau.

## 2. Ba quyết định thiết kế của v2

**(a) Chữ HOA có dấu rút xuống 6 hàng (2–7).**
Chữ HOA *không* dấu vẫn 7 hàng như cũ (`A B C D ...` không đổi 1 pixel).
Chỉ `Á À Ả Ã Ă Ắ ... Ý Ỹ` dùng bản 6 hàng để giải phóng hàng 0–1 cho dấu.
Chữ HOA chỉ có dấu nặng (`Ạ Ẹ Ị Ọ Ụ`) hoặc chỉ có râu (`Ơ Ư`) thì **không** rút —
dấu của chúng nằm ở hàng 9 / bên phải.
Đã thử bản rút 5 hàng (`v2_cap5`) và loại: chữ hoa tụt xuống bằng chữ thường,
`NƯỚC` đọc thành `NuớC`.

**(b) Mũ (`^` `˘`) đặt thấp nhất có thể mà không chạm thân chữ.**
Thuật toán `place_hat()` thử từng vị trí (hàng 1 trước, rồi hàng 0; lệch 0, ±1, ±2, ±3)
và chấm điểm `số_pixel_chạm + 2×độ_lệch_khỏi_tâm`, chồng lên thân chữ bị phạt gấp đôi.
Nhờ vậy chữ thường `â ê ô` được mũ rộng 4 px ở hàng 1–2 (chân mũ rơi đúng cột trống
của thân chữ → không dính), còn chữ HOA dùng mũ gọn 3 px ở hàng 0–1.

**(c) Thanh điệu chồng lên mũ đặt chếch trên-phải, không nằm cùng hàng với mũ.**
`ấ` = mũ (hàng 1–2) + sắc (hàng 0–1, dịch sang phải sau mũ). Hai dấu tách nhau
theo *cả* chiều dọc lẫn chiều ngang nên mắt đọc được thành hai nét, không thành đám pixel.
Cái giá: 16 glyph chồng dấu rộng thêm 1–2 px.

### Bộ nét dấu

```
sắc  .#     huyền  #.     hỏi  ##     ngã  ##.#     mũ   .##.     trăng  #..#
     #.            .#          .#          #.##          #..#            .##.
```
(`ngã` dùng bản hẹp `.##` / `##.` khi chồng lên mũ, và khi glyph quá hẹp như `ĩ`.)

## 3. Bất biến được ép trong lúc dựng

* baseline / x-height / cap-height / hàng đuôi **không đổi** → thay font không xô lệch layout.
* Không bao giờ có pixel ở **(hàng 1, cột cuối)** của glyph → dấu không thể dính vào
  chữ đứng sau (kiểm tra: chỉ `/` và `đ` cũ còn vi phạm, cả hai là glyph gốc không sửa).
* Hàng 2 luôn trống với mọi glyph chữ thường có dấu → dấu không chạm thân chữ.
* `audit` phải trả về **0 nhóm glyph trùng**.

## 4. Dùng

```bash
python3 -m pixeltext audit                       # 10v2 -> ambiguous_count: 0
python3 -m pixeltext check   --text "ĐẤT NƯỚC" --size 10v2
python3 -m pixeltext measure --text "Trò chơi mới" --size all
python3 -m pixeltext show    --text "Bắt đầu"  --size 10v2
python3 -m pixeltext sheet   --size 10v2 --scale 3 --out sheet.png
```

```python
import pixeltext as pt
pt.render("Nhiệm vụ chính", size='10v2', scale=3).save('a.png')
pt.Font.load('10v2').ambiguous()      # {}
```

Font cũ vẫn còn nguyên ở size `10`; `fit()` vẫn chỉ chọn trong `(10, 13, 15)` để không
đổi hành vi cũ — muốn dùng bản mới thì chỉ định `size='10v2'`.

## 5. Lưu ý khi nhồi vào máy J2ME thật

* File vẫn là container `fontcus` chuẩn: `u2 utfLen · mUTF8 charset · s1 height ·
  s1[n] widths · bitstream 1bpp liên tục`. Không thêm field, không đổi thứ tự ký tự,
  height = 10 và mọi width ≤ 10 nên vừa `s1` (không âm, không sign-extend).
* Advance lớn nhất là **10** (glyph `%`, giữ từ bản cũ). Các glyph chồng dấu rộng 6–8.
  Nếu engine của bạn *không* đọc font.bin mà nhồi glyph vào bảng cố định 8×16 2bpp
  (`w = (b0<<8)|b1`) thì các glyph 8 px trở lên phải kiểm tra lại cột 7 như README gốc §5.
* Chưa chạy trên KVM/thiết bị thật — mới kiểm ở mức codec + render. Trước khi đóng JAR
  vẫn phải: decode lại bằng chính code `fontcus.<clinit>` của game, so byte, rồi preverify.

## 6. Dựng lại

```bash
python3 build_v2.py            # -> font_10px_v2.bin
CAPROW=3 python3 build_v2.py   # biến thể chữ hoa 5 hàng (đã loại)
NARROWCAP=0 python3 build_v2.py# biến thể mũ chữ hoa rộng 4 px
```

`spec.py` giữ toàn bộ dữ liệu thiết kế: nét dấu, bảng chữ HOA 6 hàng / 5 hàng,
và bảng 134 dòng `(ký tự, thân chữ, mũ, thanh, dấu nặng)`.

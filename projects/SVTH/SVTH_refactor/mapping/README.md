# mapping/ — bảng đặt tên khôi phục mã nguồn (Phiên 5)

Game gốc bị làm rối (ProGuard-kiểu): class/field/hàm tên `a`, `b`, `ac`…, nhiều field cùng tên khác kiểu trong một class.
Thư mục này chứa **bảng ánh xạ do con người/Claude viết** (`*.map`) và các bảng sinh tự động.
Công cụ áp dụng: `tools/src/Remap.java` (ASM). Kiểm nhanh: `tools/remap_check.sh mapping/part_X.map [...]`.

## Định dạng `.map`

Mỗi dòng một mục, `#` là chú thích (bắt buộc ghi bằng chứng ngắn sau `#`). Tên cũ theo **không gian tên gốc** (đúng như trong
bản giải mã `restored/raw/*.java`, nơi mọi field/hàm trùng tên đã được gắn đuôi kiểu cho khỏi mơ hồ).

```
C   <cũ> <mới> [HIGH|MED|LOW]                      # class
F   <class>.<tên_cũ>_<kiểu> <mới> [conf]           # field: tên như trong bản giải mã, vd. at.b_Z   da.a_cd   bl.a_AAB
M   <class>.<tên_cũ>(<tham,số>) <mới> [conf]       # hàm: tham số kiểu Java đơn giản, cách nhau dấu phẩy, không khoảng trắng
```

* **Field**: `<tên_cũ>_<kiểu>`: `Z`=boolean `B`=byte `S`=short `I`=int `J`=long `C`=char; `String`; class khác = tên class gốc (`bl`, `Image`);
  mảng thêm `A` phía trước mỗi chiều (`AI`=int[], `AAS`=short[][], `AString`, `Abl`=bl[]). Ví dụ `at.b_Z`, `da.a_cd`, `o.a_AAS`.
  Field tên lạ nhưng không phải 1–2 chữ cái (vd. `Main.music`, `vh.boxImg`) ghi nguyên tên.
* **Hàm**: `<tên_cũ>(<kiểu tham số>)`, ví dụ `bv.a(int)`, `cg.a(Graphics,String,int,int,int,int,int)`, `da.a()`.
  Nếu bản giải mã hiển thị tên có đuôi (vd. `cg.a_AB(String var0)`) vì có hàm khác cùng tham số khác kiểu trả về, ghi cả đuôi: `cg.a_AB(String)`.
  Hàm không tham số: `bn.f()`.
* Một thành viên của **nhóm override** (hàm ảo cùng tên+descriptor trong lớp cha/con/interface) là đủ — cả nhóm đổi chung.
  Hàm ghi đè API CLDC/MIDP (`paint`, `run`, `keyPressed`, `commandAction`, `hideNotify`…) bị **đóng băng**, tool báo lỗi nếu cố đổi.
* Độ tin cậy: `HIGH` = chứng minh bằng chuỗi/khoá cấu hình/định dạng dữ liệu hoặc logic rõ ràng; `MED` = suy luận mạnh từ cách dùng;
  `LOW` = đoán có cơ sở (vẫn ghi lý do). Không biết thì **bỏ qua** (thành viên giữ tên giữ chỗ `a_Z`, `b_AI`… — vẫn tra ngược được).
* Tên mới: ASCII, class `PascalCase`, field/hàm `camelCase`, không `$`, không trùng từ khoá Java. Không trùng tên trong cùng class; không đặt
  tên trùng hàm API (`run`, `toString`, `paint`…); tên class không trùng nhau kể cả khác hoa/thường, không trùng class thư viện (`Image`, `Player`…).
  `Main` giữ nguyên (MIDlet-1).

## File

| File | Nội dung |
|---|---|
| `part_*.map` | bảng viết tay theo cụm class (core, battle, data, world, ui_as, ui_ab, sdk) |
| `resolved.map` | SINH RA: toàn bộ ánh xạ đã áp (gồm placeholder), khoá theo tên gốc đầy đủ owner.tên:desc |
| `reverse.tsv` | SINH RA: tên mới → tên gốc + độ tin cậy |
| `inverse.map` | SINH RA: bản đảo (dùng chứng minh khứ hồi) |

Không sửa tay các file sinh ra. Giải thích từng quyết định: `docs/RENAME_MAP.md`.

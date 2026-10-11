# Phương án 2 — lớp hook dịch ở tầng bytecode

Đọc khi text nằm rải rác ở nhiều chỗ gọi, có nguy cơ chuỗi dính `==`/khoá tra cứu, hoặc cần sửa bản dịch nhiều vòng. Toàn bộ `SKILL.md` vẫn áp dụng; chỉ khác cách patch: **không sửa constant pool gốc**, mà chèn lớp trung gian thay chuỗi ở điểm hiển thị.

## Kiến trúc

- **Class mới** (ví dụ `Lang`): bảng chuỗi gốc (hoặc ID/hash) → tiếng Việt, đọc từ một resource riêng trong jar, nạp một lần lúc khởi động. Hàm `Lang.t(String)` trả lại đúng object tiếng Việt đã cache, không cấp phát mỗi khung hình.
- **Chỗ redirect** (chèn `invokestatic Lang.t` ngay trước lời gọi):
  - `Graphics.drawString/drawSubstring/drawChar(s)` và `Font.stringWidth/substringWidth/charWidth(s)`;
  - UI có sẵn: `Form.append`, `StringItem`, `TextField`, `TextBox`, `Alert.setString`, `List`, `ChoiceGroup`, `Ticker`;
  - renderer hoặc hàm word-wrap tự viết của game: redirect tại đầu hàm đó. Cách này thường gọn hơn redirect từng `drawString`.
- **Ảnh**: chặn `Image.createImage(String)` để trả ảnh đã việt hoá, không sửa ảnh gốc.

## Công cụ & bẫy

- Dùng ASM/Javassist để sửa class tĩnh. Không dùng `java.lang.instrument`/Byte Buddy agent vì CLDC không có.
- Không để ASM tự tính frame (`COMPUTE_FRAMES` nạp class qua JRE, sai với CLDC). Dùng `COMPUTE_MAXS`, ghi version cũ, bỏ `StackMapTable`, rồi `scripts/preverify.sh` tính lại `StackMap`.
- Mỗi method có chỗ gọi bị sửa đều làm tăng rủi ro verifier: `jar_check.py` phải PASS và phải chạy qua màn có method đó trong `run_emu.py`.
- Liệt kê đủ chỗ gọi bằng cả `javap` lẫn decompiler; bỏ sót sẽ ra màn lẫn chữ Hán và chữ Việt.
- Phần "Đổi" trong `jar_check` chỉ được gồm các class có chỗ gọi đã redirect + class/resource mới. Có class khác bị đổi thì dừng lại, xem lại bước transform.

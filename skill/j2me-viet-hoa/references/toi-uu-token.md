# Tối ưu context và usage (Claude Code)

(Tham chiếu của skill `j2me-viet-hoa`: đọc khi bắt đầu dự án Việt hoá lớn, khi người dùng nhắc usage/context/compact, hoặc khi context phình. Các lệnh `/compact`, `/autocompact`, `/context`... chỉ có trong Claude Code.)

Mục tiêu: làm xong việc với ít token nhất mà không mất mạch. Mỗi lượt chat,  phải xử lý lại toàn bộ context, nên context càng phình thì mỗi tin nhắn càng tốn usage và chất lượng càng giảm (dễ quên chi tiết ở giữa, lặp lại việc cũ). Vì vậy ưu tiên: đọc ít, ghi ra file, tách session.

## 1. Khi bắt đầu một task

1. Xem có file tiến độ chưa (`PROGRESS.md`, `PHAN_TICH_KY_THUAT.md` hoặc tương tự). Nếu có, đọc nó trước, không đọc lại cả codebase.
2. Xác định giai đoạn hiện tại và gợi ý ngưỡng autocompact phù hợp (bảng ở mục 5).
3. Nếu task rõ ràng là nhiều giai đoạn, đề nghị chia mỗi giai đoạn một session và nói ngắn gọn lý do.

## 2. Đọc file tiết kiệm

Đọc nguyên file dài là cách đốt context nhanh nhất. Làm theo thứ tự:

- Dò trước, đọc sau: `grep -rn "từ khoá" thư_mục | head -50`, `wc -l`, `ls` để biết kích thước.
- File dài thì đọc theo dải dòng (`sed -n '120,200p'`, hoặc view với range), không đọc cả file.
- Output lệnh lớn (decompile, dump strings, log) thì chuyển ra file rồi xem từng phần:
  `lệnh > /tmp/out.txt; wc -l /tmp/out.txt; head -40 /tmp/out.txt`
- Lọc trước khi in: `sort -u`, `uniq -c | sort -rn | head`, `grep -v` để bỏ nhiễu.
- Không đọc lại file đã đọc trong session này. Ghi điều cần nhớ vào file ghi chú thay vì đọc lại.
- Tránh đọc thư mục sinh ra: output decompile đã đọc xong, jar giải nén, `build/`, `node_modules/`, log.

## 3. Giao việc rộng cho subagent

Câu hỏi kiểu "tìm tất cả chỗ gọi hàm X", "class nào load string" nên giao cho subagent (chỉ cho phân tích/tìm kiếm; **tuyệt đối không giao việc dịch text cho subagent**, vì `dich-pro` cấm): nó chạy context riêng và chỉ trả kết quả tóm tắt về. Dặn subagent trả về danh sách ngắn (đường dẫn, số dòng, một câu mô tả), không dán lại code dài.

## 4. Ghi tiến độ ra file (quan trọng nhất)

Sau mỗi mốc (xong một class, xong một nhóm string, tìm ra cơ chế), cập nhật file tiến độ ngắn gọn. Dùng mẫu trong `references/progress-template.md`. Nguyên tắc:

- Chỉ ghi thứ không suy ra lại được rẻ: mapping tên đã deobfuscate, vị trí code quan trọng, quyết định và lý do, cách chạy/test, lỗi đã gặp và cách sửa.
- Không dán code dài vào file ghi chú; ghi đường dẫn + số dòng.
- Giữ file dưới khoảng 150 dòng; gộp mục cũ khi phình.

Nhờ vậy sau compact hoặc mở session mới, chỉ cần đọc một file là nối tiếp được.

## 5. Compact và ngưỡng autocompact

Trước khi compact (tay hoặc sắp tự động): cập nhật file tiến độ trước, rồi mới chạy `/compact` kèm hướng dẫn giữ gì, ví dụ:
`/compact giữ mapping class, vị trí load string, lỗi đang debug, bước tiếp theo`

Ngưỡng gợi ý (đặt bằng `/autocompact <số token>` nếu bản đang dùng có lệnh này; nếu không, biến môi trường `_AUTOCOMPACT_PCT_OVERRIDE` là phần trăm cửa sổ context):

| Việc | Ngưỡng |
|---|---|
| Sửa nhỏ, hỏi nhanh | 100-150k |
| Trích text, dịch (`dp`), nhét lại bằng script | 150-200k |
| Patch bytecode, test, sửa lỗi hiển thị | 200k |
| Phân tích, decompile, deobfuscate nhiều class | 300k |
| Cần giữ nhiều file lớn cùng lúc, đang debug dở | 400k trở lên |

Đây là ngưỡng kích hoạt, không phải giới hạn cứng: context có thể vượt qua trong một lượt (đọc file lớn). Nếu bị compact giữa lúc đang debug thì nâng lên một mức. Cảnh báo người dùng: nếu context đầy sát giới hạn cứng thì cả `/compact` cũng có thể thất bại, nên đặt ngưỡng thấp hơn một chút để còn dư chỗ.

## 6. Chia session theo giai đoạn

Mỗi giai đoạn một session, bắt đầu bằng việc đọc file tiến độ. Với Việt hoá J2ME, khớp với quy trình của SKILL.md chính:
1. Kiểm kê / decompile / tìm đường đi của chữ (Bước 0–1)
2. Ghép font, trích text (Bước 2–3)
3. Dịch bằng `dp` (Bước 4; hết lượt thì dừng, `out.tsv` lưu tiến độ nên session sau chạy tiếp)
4. Patch text + code hiển thị, vá ảnh (Bước 5–6)
5. Build, `jar_check`, `run_emu` và sửa lỗi (Bước 7)

Dùng `/clear` khi chuyển sang việc không liên quan. Dùng `/resume` để mở lại session cũ khi còn nguyên mạch.

## 7. Thói quen làm việc tốn ít token

- Sửa bằng chỉnh sửa có mục tiêu (thay đúng đoạn cần đổi), không ghi lại cả file để đổi vài dòng.
- Đọc `.md` của dự án để biết quy ước thay vì hỏi lại; nếu chưa có, đề nghị tạo (mẫu trong `references/progress-template.md`).
- Dùng plan mode cho thay đổi lớn: chốt kế hoạch rồi mới sửa, tránh làm sai rồi làm lại.
- Trả lời ngắn: không lặp lại code vừa viết, không tóm tắt lại điều người dùng đã biết. Báo kết quả, file đã đổi, bước tiếp theo.
- Việc lặp đi lặp lại thì viết script một lần rồi chạy, thay vì xử lý thủ công từng mục qua chat.
- Việc nhẹ (dịch text, sửa lẻ) có thể dùng model nhẹ hơn qua `/model`; để model mạnh cho phần phân tích khó.
- Có thể rút ngắn độ dài phản hồi tối đa bằng `_CODE_MAX_OUTPUT_TOKENS` (ví dụ 16000).
- Có thể chặn đọc thư mục nặng bằng quy tắc `deny` trong `settings.json` của  Code.

## 8. Theo dõi usage

Dùng `/context` để xem context đang bị chiếm bởi gì, `/cost` hoặc `/usage` để xem mức tiêu thụ (tên lệnh có thể khác theo bản và gói; gõ `/` để xem danh sách thực tế). Khi thấy cảnh báo sắp chạm giới hạn tuần: giảm ngưỡng autocompact, dồn việc nặng vào ít session, và ưu tiên các bước chạy bằng script.

## Lưu ý về độ chắc chắn

Tên lệnh và biến môi trường thay đổi theo bản  Code. Trước khi hướng dẫn người dùng gõ một lệnh chưa chắc, nói rõ là chưa kiểm chứng và gợi ý gõ `/` hoặc `/help` để xác nhận. Không bịa số liệu về mức usage; nếu người dùng muốn so sánh, bảo họ xem mức usage trước và sau một buổi làm việc.

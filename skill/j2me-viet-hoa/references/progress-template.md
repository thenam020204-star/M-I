# Mẫu file tiến độ (PROGRESS.md)

Giữ ngắn, dưới ~150 dòng. Ghi đường dẫn + số dòng thay vì dán code.

```markdown
# Tiến độ: <tên dự án>

## Mục tiêu
<một hai câu>

## Trạng thái hiện tại
- Giai đoạn: <phân tích | trích text | patch | test>
- Đang làm: <việc dang dở>
- Bước tiếp theo: <việc kế tiếp, cụ thể>

## Mapping / phát hiện quan trọng
- <tên obfuscate> = <ý nghĩa> (file:dòng)
- <cơ chế load string / font / resource> (file:dòng)

## Quyết định và lý do
- <quyết định>: <vì sao>

## Cách chạy / test
- <lệnh build, lệnh test, công cụ dùng>

## Lỗi đã gặp
- <triệu chứng> -> <nguyên nhân / cách sửa>
```

# Mẫu CLAUDE.md ngắn

```markdown
# Quy ước dự án
- Đọc PROGRESS.md đầu mỗi session.
- Không đọc nguyên file dài: dùng grep/sed theo dải dòng.
- Output lệnh lớn: ghi ra /tmp rồi xem head/tail.
- Cập nhật PROGRESS.md sau mỗi mốc, trước khi compact.
- Sửa có mục tiêu, không ghi lại cả file.
- Công cụ: <liệt kê đường dẫn và lệnh thường dùng>
```

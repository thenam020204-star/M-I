# Báo cáo Việt hoá Heroes Lore Zero

## Đã làm
- Font: chuyển renderer sang BitmapFont **f13c** (13px, chân chọn lọc), file `/font/f13c.bin`.
- Text: dịch tay toàn bộ resource (`t/*.tdf`, `itm/*`, `en/edf`, script map `m/m*`): 5046 dòng trong `text_can_dich.tsv`, 1454 mục text cộng chuỗi script map. Phong cách hiện đại, giữ tên riêng (thuật ngữ ở `dp_work/thuat-ngu.tsv`, xưng hô ở `dp_work/vai.md`).
- Chuỗi trong class (`mapping/class_strings.tsv`): 22 chuỗi hiển thị trong `rpg/d` và `j` (hồi sinh, thoát, lên cấp, vàng, "Nhấn phím bất kỳ để tiếp tục", Có/Không…). `String.getBytes()` được đổi thành `TextRenderer.utf8()` để chữ Việt ra đúng UTF-8 trên mọi máy. Không dịch `rpg/d.a_` ("yes"), vì giá trị này được dùng để so sánh trong code.
- Khung thoại làm lại giống Heroes Lore 2: rộng hết màn, 3 dòng, ngắt theo từ, tính theo px (`DialogBox`).
- Gỡ rối mã: đặt lại tên các class và method liên quan đến text bằng `mapping/rename.map` (ASM Renamer) gồm TextRenderer, TextTable, ScriptRunner/Loader, MapScript, Item, GameGraphics…
- Bản ghi `itm` và `en/edf` có byte checksum ở cuối; checksum được tính lại khi đóng gói. `Item.parseRecord` đọc độ dài kiểu không dấu (`& 0xFF`).

## Kiểm tra
- `dp check`: không có lỗi chặn. Còn hai loại cảnh báo đã biết:
  - `~` không có glyph trong f13c; khi đóng jar ký tự này được thay bằng `!`.
  - 4 chỗ lệch thuật ngữ cố ý (Potion, Crusader).
- `jar_check` (so với jar đã rename): **RESULT: PASS** (`logs/jar_check.txt`). Khi so với jar gốc, công cụ báo REJECT về descriptor; lý do là việc đổi tên class theo yêu cầu, không phải lỗi KVM. Class version 45 kèm StackMap; các API mới đều có trong MIDP2/CLDC1.1.
- `tools/check_all.sh`: OK (`logs/check_all.txt`).
- FreeJ2ME (`out/shots`, `shots2/all.png`): đã chạy qua các màn bật âm thanh, Nhấn phím, Chơi mới, chọn bản lưu, chọn lối đánh và các câu thoại đầu. Không có exception của game. Chỉ có lỗi MIDI `Sequencer null`, do môi trường headless không có bộ phát nhạc.

## Debug
- NPE `bj.a(Item)` khi bấm Chơi mới: do checksum bản ghi itm chưa được tính lại. Đã sửa trong `scripts/textfmt.py`.
- `dp merge --out` tạo ra thư mục thay vì file. Đã chuyển file ra ngoài.

## Còn tồn
- Ảnh title là logo "HEROES LORE ZERO", giữ nguyên. Chưa rà hết từng ảnh UI khác.
- Chuỗi mua bán RMB/SMS và credits pháp lý (EA) giữ tiếng Anh.
- Chưa chơi hết game trên emulator, mới qua các màn đầu.

# Tiến độ: Việt hoá 魔界战纪-魔女奇缘 (Ma Giới Chiến Ký – Ma Nữ Kỳ Duyên), N73 240x320

## Mục tiêu
Jar tiếng Việt chạy trên KVM thật; font BitmapFont 13c; khung thoại 3 dòng kiểu ảnh tham khảo; splash logo NAMP.

## Quyết định (Bước 0, người dùng chốt)
- Phong cách: Hán Việt nhẹ, tên riêng Latin (阿鲁鲁 → Aruru).
- Font: f13c (13px chân chọn lọc). Logo: màn splash khi mở game.

## Trạng thái
- Xong: trích text (1666 dòng: map 967 thoại, l9.pro, .class), round-trip byte-identical.
- Tiếp: hook font (VH.class), khung thoại, dịch, ảnh, build.

## Phát hiện quan trọng
- Font hệ thống: d.a = Font(0,0,8); d.b = stringWidth("测") (đơn vị bề rộng); d.c = height; d.d = 2 (giãn dòng).
- d.a(String,Font,int) = ngắt dòng theo ký tự; mã: `|` xuống dòng, `[C:RRGGBB]..[/C]` màu, `[N/]` = tên người chơi (j.aS). Gán d.e.
- j.a(Graphics,String,x,y,anchor,bool) vẽ chuỗi có mã màu. h.a(Graphics,String,...) = vẽ chữ có bóng (y.a trong k).
- Khung thoại: k.a(byte nv,byte câu,byte phía) mở; k.w() tick (aR: 1 mở,2 gõ chữ,3 chờ,4 đóng); k.d(Graphics) vẽ 2 dòng.
  y (h) : a(g,x,y,w,h)=hộp xanh; a(g,side,y,id)=chân dung head_N.png 54x54 + tab tên; b(g)=mũi tên.
- Map: mapfmt.py (block = short len + [byte n, n UTF, lệnh]); opcode 0 = [nv, câu, phía].
- Gói res: index `level-9` + res0..30 (khúc 10000 byte) -> respack.py; l9.pro (profmt.py) = vật phẩm/trang bị/kỹ năng.
- Tên người nói l.H[35]; tên map l.D[45].

## Cách chạy
- scripts/extract.py work/jar work/res work/text_goc.tsv ; scripts/inject.py <tsv> <jar_dir> work/res
- Emulator: scripts/run_emu.py (đã sửa: tham số FreeJ2ME plus = jar 1 W H, cắt ảnh full-screen).

## Lỗi đã gặp
- run_emu gốc truyền `W H 1` -> canvas 320x1; sửa thành `1 W H`.
- mapfmt: dò chuỗi block sai (chọn chuỗi giả) -> chọn chuỗi kết thúc đúng EOF có nhiều byte thoại nhất.

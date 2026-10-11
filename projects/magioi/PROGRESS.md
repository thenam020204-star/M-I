# Tiến độ: Việt hoá 魔界战纪-魔女奇缘 (N73 240x320) — HOÀN THÀNH bản 1

## Trạng thái
- Xong: dịch 1466/1466 (dp check OK), hook font 13c, khung thoại 3 dòng mới, chân dung head_0 mới chìm sau khung, splash NAMP, vá 2 ảnh menu, jar_check PASS, FreeJ2ME qua mở đầu + menu.
- Có thể làm tiếp: chân dung cho head_3/5/8/9/17, logo tiêu đề tiếng Việt, chơi thử các map sau.

## Phát hiện quan trọng (xem README_DU_AN.md cho pipeline)
- d.a = Font(0,0,8); d.b = stringWidth("测") (đơn vị 12px, đã đổi hằng thành "MM"); d.c = height; d.d = 2.
- d.a(String,Font,int) ngắt dòng; mã `|`, `[C:RRGGBB]..[/C]`, `[N/]` (= j.aS tên người chơi).
- Khung thoại: k.a(BBB) mở, k.w() tick (aR 1 mở/2 gõ/3 chờ/4 đóng), k.d(Graphics) vẽ → nay gọi VH.open/tick/paint.
- Chân dung: l.I[nv] = id head_<id>.png (jar root), tên l.H[nv]; nv 0 = người chơi.
- Map: block = short len + [byte n, n×UTF, lệnh]; opcode 0 = [nv, câu, phía].
- Gói res: `level-9` + res0..30; l9.pro = vật phẩm/trang bị/kỹ năng.
- menu_ingamestring.png: ô 30x16, 0-4 / 150+ / 300+ (tab).
- TextBox tên: h.a(l.d[3], j.aS, 3, 0) → đã sửa 3→5.

## Cách chạy
- `source tools/env.sh; bash rebuild.sh` ; test: `python3 scripts/run_emu.py --fj $FJ --jar out/MaGioiChienKy_VH.jar --out shots --steps "..."`

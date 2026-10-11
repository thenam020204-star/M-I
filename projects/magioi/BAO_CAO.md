# Báo cáo Việt hoá — Ma Giới Chiến Ký – Ma Nữ Kỳ Duyên (N73)

## Đã làm
- **Text**: 1666 dòng (1466 câu duy nhất) dịch tay toàn bộ, Hán Việt nhẹ, tên Latin: 967 câu thoại trong 45 file map, 326 dòng vật phẩm/trang bị/kỹ năng (`l9.pro` trong gói res), 378 chuỗi UI trong `.class` (h, i, l). `dp check`: **OK 1466/1466**. Không còn ký tự ngoài bảng glyph f13c.
- **Font**: mọi chữ vẽ qua BitmapFont 13c (hook `VH`); bề rộng thật từng glyph; ngắt dòng theo từ (giữ mã `|`, `[C:RRGGBB]..[/C]`, `[N/]`).
- **Khung thoại** làm lại theo ảnh tham khảo: 3 dòng/trang (gốc 2), nền xanh đen bán trong suốt (trên trong hơn dưới), viền vàng 2 lớp, tab tên nền tối chữ vàng, hiệu ứng gõ chữ, mũi tên ▼ nhấp nháy ở viền dưới.
- **Chân dung**: `head_0.png` thay bằng ảnh người dùng gửi (181×150, PNG 256 màu có alpha, 9,3 KB); vẽ trước khung nên chìm sau nền bán trong suốt, đáy ảnh trùng cạnh đáy khung, bỏ khung vuông; tab tên nằm phía đối diện chân dung. Các chân dung cũ 54×54 (head_3/5/8/9/17) cũng vẽ chìm, nhô lên trên khung 42px, không khung.
- **Splash** logo "Việt hoá bởi NAMP" 2,5 giây trước màn hỏi âm thanh.
- **Ảnh**: `menu_string.png` (7 mục menu chính), `menu_ingamestring.png` (17 nhãn chỉ số/tab), một màu thân chữ gốc; nhãn chỉ số giữ viền đen như gốc (nền vàng nhạt), tab không viền.
- **Sửa lỗi phát sinh**: TextBox nhập tên giới hạn 3 ký tự (tên Hán) → "Aruru" ném `IllegalArgumentException` → nâng lên 5 (`iconst_3`→`iconst_5`); nhãn phím mềm phải đặt theo bề rộng 1 chữ Hán → hook tự lùi chữ vào trong khi tràn mép.

## Kiểm tra
- `jar_check.py goc/game.jar out/MaGioiChienKy_VH.jar`: **RESULT: PASS** (class version ≤ 48, có StackMap, không StackMapTable). WARN chỉ là API CLDC 1.0/MIDP 2.0 chuẩn (StringBuffer, Vector.copyInto, Graphics.fillTriangle...). Mục "Đổi" gồm mọi class vì `preverify.sh` sinh lại StackMap cho cả jar; nội dung code chỉ đổi ở XMessage, d, h, i, j, k, l.
- FreeJ2ME (`logs/run_emu_final.json`, ảnh `out/shots/`): splash → hỏi âm thanh → tiêu đề → nhập tên → intro → thoại trong nhà/ngoài làng (chân dung mới) → hết đoạn mở đầu (~170 câu) → bản đồ → menu Chỉ số/Túi đồ/Vũ khí/Kỹ năng/Bản đồ/Hệ thống → Shop VIP. Exception chỉ còn đúng các lỗi có sẵn ở game gốc/môi trường: thiếu class `XConnection` (game gốc không đóng gói class này), RMS chưa có lần đầu, MIDI headless.
- `rebuild.sh` chạy 2 lần cho jar trùng từng file nội dung (md5 jar khác vì timestamp zip). `tools/check_all.sh`: 7/7 OK.

## Debug — lỗi đã gặp
- `run_emu.py` của skill truyền `W H 1` → FreeJ2ME plus hiểu canvas 320×1; sửa thành `1 W H` và cắt ảnh full-screen theo tỉ lệ (bản trong `scripts/`).
- Dò khối kịch bản trong map bị khớp chuỗi giả → chọn chuỗi khối kết thúc đúng EOF chứa nhiều byte thoại nhất (`mapfmt.py`), kiểm khớp 100% chữ Hán từng map.
- Splash không hiện trên FreeJ2ME (canvas chưa được repaint) → `repaint()` trong vòng chờ + `showNotify`.
- `dp` coi `~` là mã điều khiển nhưng game không dùng và font không có glyph `~` → bỏ `~` khỏi CODE0 trong bản `scripts/dp.py`; câu gốc có `~` dịch không kèm `~`.
- `dp` tự cắt khoảng trắng đầu/cuối → chuỗi game tự ghép ("(3"+"孔)", "ATK："+số) dính chữ → `scripts/post_merge.py` sửa 12 dòng sau `dp merge`.
- Giới hạn độ dài trong TSV là ước lượng (ký tự); đã nới cho tên vật phẩm (16), nhãn UI ngắn (10/12/18) sau khi soát bề rộng thật trên ảnh chụp.

## Còn tồn / chưa làm
- **Logo tiêu đề** `menu_bg.png` (魔界战记 / 魔界奇缘, chữ thư pháp lớn) giữ nguyên: font 13–18px nét đơn vẽ đè sẽ xấu hơn bản gốc; cần ảnh logo tiếng Việt nếu muốn thay.
- `cp.png` (logo nhà phát hành 当乐 D.cn hiện lúc khởi động) giữ nguyên — không phải nội dung game.
- MIDlet-Name/Description trong manifest giữ tiếng Trung (đổi tên sẽ làm máy coi là app khác, mất save cũ).
- Chân dung các nhân vật khác vẫn là ảnh 54×54 nền trắng; gửi ảnh nền trong suốt dạng `head_<id>` (3 Salin, 5 Atare, 8 Tata, 9 Hanako, 17 Yukimaru) để thay như head_0.
- Chưa chơi hết toàn bộ game trong emulator (đã đi tới hết đoạn mở đầu + menu); các map sau dùng cùng renderer/ngắt dòng nên rủi ro thấp. Nên thử trên máy thật N73 (heap, tốc độ vẽ ảnh alpha).
- Ký tự font thiếu: không có.

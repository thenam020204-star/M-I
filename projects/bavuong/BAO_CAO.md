# Báo cáo Việt hoá — 霸王怒与美人泪 (Bá Vương Nộ Dữ Mỹ Nhân Lệ), N73 240x320

## Đã làm
- **Text**: dịch tay toàn bộ **3618 câu duy nhất** (3637 dòng, 89 nghìn chữ Hán) — thoại cốt truyện + nhiệm vụ Thiên Đạo,
  lời NPC, nhiệm vụ/vật phẩm/trang bị/công thức/quái vật/võ công, tên bản đồ, trợ giúp, UI. Phong cách kiếm hiệp Hán Việt,
  tên phiên âm Hán Việt (bảng `out/thuat-ngu.tsv`, xưng hô `out/vai.md`). `dp check`: **OK 3618/3618**.
- **Font**: mọi chữ vẽ qua `BitmapFont` 15c (hook `VH`: drawString/stringWidth/substringWidth/getHeight), bề rộng thật
  từng glyph; 3 hàm ngắt dòng theo ký tự + hàm chia dòng theo số ký tự của game đổi sang ngắt theo từ (giữ `|` xuống dòng).
- **Khung thoại** làm lại theo ảnh mẫu: nền xanh đen bán trong suốt, viền vàng 2 lớp bo góc, **tab tên nhân vật** (tách từ
  "[Tên]:câu"), chữ trắng có bóng, **3 dòng/trang** (gốc 2, đã sửa phân trang trong `o.ag`), mũi tên ▼ nhấp nháy khi còn
  trang. Chân dung nhỏ có sẵn của game vẫn đứng trên khung; tab tên tự sang phía đối diện chân dung; nhãn "#Bỏ qua" đặt vào
  khoảng trống giữa tab và chân dung, thiếu chỗ thì ẩn. Không thêm chân dung mới (theo lựa chọn).
- **Splash** "Việt hoá bởi NAMP" 2,5 giây (chỉ lần khởi động đầu, bấm phím bất kỳ để bỏ qua).
- **Ảnh**: vẽ lại 29 ảnh có chữ bằng pixeltext (cùng bộ font): menu chính, menu con "Chơi mới/Tiếp tục", "Cách chơi/Cốt
  truyện/Trợ giúp", menu tạm dừng, cài đặt (Âm/Tốc/Đi/Rung – Tắt/Bật/Auto/Tay), nhãn chỉ số (Lv EXP ATK DEF HP MP DEX,
  STR AGI HP MP, Điểm, C.hóa Nặng Đồ Hệ), tab Trang bị/Phím tắt/NV chính/NV phụ/Tài sản, Nhân vật/Võ công/Nhiệm vụ,
  "Lên cấp!", "Né!", "Bạo kích!", "#Tin địch", "Phải:Về", "Tiếp/Bỏ qua", "Nhấn OK để tiếp tục", nút "Menu/Chỉ số",
  "Bản đồ", "Tiền", thuộc tính "- H B L" (Không/Hỏa/Băng/Lôi). Toạ độ từng ô lấy từ lệnh cắt-vẽ (setClip) trong `o.java`;
  một màu thân + màu viền của ảnh gốc; chỉ nới ảnh tới bề rộng clip của code.

## Kiểm tra
- `jar_check.py goc/game.jar out/BaVuongNo_VH.jar`: **RESULT: PASS** (class ≤ 48, có StackMap, không StackMapTable).
  WARN chỉ là API MIDP 2.0 mà `VH` dùng (`Image.createRGBImage`, `Graphics.fillTriangle`, `String.substring(int)`) — game
  gốc đã dùng `setFullScreenMode` (MIDP 2) nên N73 chạy được; manifest gốc ghi MIDP-1.0, giữ nguyên.
- FreeJ2ME (`out/shots/`): splash → hỏi âm thanh → menu chính → intro cuộn → thoại mở đầu (ngoài trời, mộng cảnh, trong
  nhà, tên dài) → menu tạm dừng → Nhân vật → Bản đồ → Trang bị → Túi đồ → Võ công → Nhiệm vụ. Exception duy nhất: MIDI
  `Sequencer ... is null` (môi trường headless, không phải lỗi game).
- `rebuild.sh` chạy lại cho jar trùng từng file nội dung. `check_all.sh` của bộ tool trong repo: 7/7 OK.

## Debug — lỗi đã gặp
- `run_emu.py` của skill truyền sai thứ tự `W H 1` → dùng bản đã sửa (`1 W H`, cắt ảnh theo tỉ lệ).
- Lớp `t` (dữ liệu câu thoại) bị obfuscate thành "inner class" của `o` → ecj từ chối tham chiếu → `build_hook.sh` biên dịch
  hook với bản class đã bỏ attribute InnerClasses (`scripts/strip_inner.py`, không đóng vào jar).
- Thứ tự constant pool không theo cốt truyện → `extract.py` sắp chuỗi theo thứ tự lệnh `ldc` trong `<clinit>`.
- `dp` coi `<Người giao>` là thẻ HTML → regex thẻ chỉ nhận ASCII; `~` không có trong font → dịch "1~5" thành "1-5".
- Ô chữ trong ảnh chỉ cao 11–14px: chữ có dấu chồng (ế, ậ) cao 13px → cho phép viền trên/dưới bị cắt 1px khi thân chữ
  vẫn lọt ô, chọn từ khác khi vẫn không vừa (vd "Vật dụng", "Rèn đúc", "Lv").
- Nút "Menu/Chỉ số" có nền hoa văn → dựng lại nền bằng 4 cột mép trái (chu kỳ 4px).
- Selftest trong `Tool_Viet_Hoa_NAM_P.zip` báo lỗi với `f15c.bin` của chính zip: bản này đã chỉnh riêng các glyph
  `e , á ạ ặ ậ é è ẻ ẽ ẹ ê ế ề ể ễ ệ ị` (cùng độ rộng). Dự án dùng đúng bản trong zip của bạn.

## Còn tồn / chưa làm
- **Logo tiêu đề** `intro_000.png` (chữ thư pháp 霸王怒与美人泪) giữ nguyên — cần ảnh logo tiếng Việt nếu muốn thay.
- Logo nhà phát hành (`_bb` SOGOYO mã hoá, `logo0/1` quảng cáo d.cn, `logo2` Alioth) giữ nguyên.
- Biển hiệu trang trí trên bản đồ trong gói `obj.dat` (武, 魔, 客栈, 大道, 房县…) giữ nguyên như chi tiết bối cảnh.
- Ảnh tiếng Hàn còn sót của bản gốc (`menu_017`, `menu_019`–`025`) không được code vẽ → không đụng tới.
- Nhãn ô nhỏ dùng viết tắt: "C.hóa" (cường hoá), "Ch.luyện", "NV" (nhiệm vụ), thuộc tính "H/B/L"; nghề trong ô 25px là
  "Kiếm"/"Võ" (Kiếm Hiệp/Võ Thánh).
- Chưa chơi hết game trong emulator (đã qua mở đầu + các menu chính); các màn sau dùng cùng renderer/ngắt dòng. Nên thử
  trên máy thật N73 (heap: atlas 15c ~73 KB/màu).
- Ký tự font thiếu: không có (đã đổi `~`, `《》`, `·`, `_`, `=` trong bản dịch).

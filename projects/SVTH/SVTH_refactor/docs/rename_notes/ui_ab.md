# Ghi chú đặt tên — cụm ui_ab (`ab av bs ce ba bh cx`)

Bảng: `mapping/part_ui_ab.map` (7 class, 68 field, 39 hàm; kiểm `tools/remap_check.sh` 0 lỗi, cả khi ghép với `part_core.map`).

## cx → ResultListener
Interface một hàm `a(int,String)` → `onResult(status, message)`. Đặt tên ở gốc nên đổi chung cho cả `as`, `ch`, `df`.
Quy ước mã: `0` = thành công (ba: chữ nhập vào; df: mua xong; as: đổi tên sủng vật), `1` = thất bại kèm thông báo
("Không có SIM", "Người dùng hủy thanh toán"), `2` = ch chuyển `c_B=10` (chưa rõ nghĩa, có lẽ "chờ/hủy").

## ba → TextInputDialog
`Form` + `TextField` + lệnh "OK"/"Trở về". OK với chuỗi khác rỗng → `listener.onResult(0, text)` rồi `da.a(da)` (trả màn hình về canvas).
Không có hàm dựng riêng: `ab.g` (trang sủng vật, mục "Đổi tên") tự dựng Form "Đổi tên sủng vật", TextField "1-4 ký tự" dài 4, listener = `as`.

## bh → SmsSendTask (MED)
Lớp tĩnh `send(smsText, phoneNumber, listener)` tạo đối tượng, chạy Thread; `run()` **chỉ gọi `listener.onResult(0, null)`** —
hai chuỗi không bao giờ được đọc. Tức là bản này đã bỏ gửi SMS thật: thanh toán SMS (ch → df "smsmenu.bin") **luôn thành công**.
Tên lấy theo nơi gọi duy nhất `ch` (đọc `wireless.messaging.sms.smsc`, khoá `precmd`/`number`/`msg`); thứ tự tham số: nội dung trước, số đích sau.

## av → LogoScreen
`Main.showLogo()` tạo `av` với `mode=2`: chạy lần lượt danh sách logo `cq.a_AString` (đuôi `.av` = hoạt ảnh, `.png/.gf` = ảnh tĩnh
hiển thị `logoDurations[i]` ms, màu nền `logoBackColors`). Hết logo: nếu `at.a_Z` (hỏi nhạc) → `mode=1` = hộp "Bật âm nhạc?"
("Có"/"Không", đảo phím mềm cho máy E2/V8), ngược lại `Main.showCover()`. Câu trả lời do `da` xử lý (đặt `at.b_Z`, `o.g_B`, `isFinished`).
Lưu ý: hộp hỏi nhạc thực ra vẽ khi `da.a_B == 2` với `av.mode == 1`; `da.a_B == 1` (Main.showSoundSet) không còn ai dùng — khác với brief.
Bản mod SFX thay hai lệnh `getstatic at.a:Z` trong `av` bằng `SfxUi.skipPrompt()`.

## bs → TitleScreen, ce → TitleLoader
Màn bìa (da trạng thái 3). `startLoading()` đặt `page=0` và tạo `ce` (Thread) chạy `loadResources()` rồi bật `isLoaded`.
Trang (`bs.page`, static): 0 đang tải (thanh %, chữ "Đang tải dữ liệu ..."), 6 hoạt ảnh start.av, 1 menu chính (trái/phải đổi mục),
2 Hướng dẫn (about.bin `help:`..`helpEnd`, cuộn `helpScrollY`), 3 Cài đặt âm lượng (±10, D41.av), 4 Giới thiệu (`cq.a_String`),
5 chọn bản lưu "Tiến độ n" (RMS `o.n/o/p_String`), 7 "Game hay đề cử" (`Tj<n>Url` trong JAD → platformRequest), 8 xác nhận vào QQ Game.
Ảnh: D04 = mũi tên trái/phải quanh tên mục, D08 = chữ "Lv", D09 = tên "Quinn/Julie", D23 = biểu tượng ✓/✗ phím mềm, D11 = mũi tên nhỏ, number01 = chữ số 6x7.
LOW: `topBarImage` (topbj.gf nạp nhưng không vẽ), `blinkFrame` (đổi 0/2 nhưng không đọc).

## ab → MenuList (MED)
Hai vai trò trong một class:
* **Đối tượng**: danh sách nhiều cột (`bg`) chung con trỏ và thanh cuộn; dùng ở `as` (kỹ năng, cửa hàng, hệ thống) và `bn` (trận đấu).
  `rowIds[i]` = id (kỹ năng/vật phẩm) của dòng i, `getSelectedRow()` = chỉ số chọn của cột 0. `resetSelection(int)` bỏ qua tham số.
* **Tĩnh**: bộ xử lý phím cho menu trong game (`cd.b_B == 4`), `handleMenuKey` rẽ nhánh theo `as.o_B` (trang, thuộc cụm ui_as):
  1 thanh tab (tab 4 = kỹ năng — báo "Chưa học kỹ năng nào"; tab 2 có lẽ là nhân vật, chưa kiểm; 3 = sủng vật, 7 = nhiệm vụ), 2 cộng điểm thuộc tính (`bl.a_AI[16]` = điểm tự do),
  3 sủng vật → `handlePetPageKey`, 4 xem kỹ năng, 5 trang bị → `handleEquipPageKey`, 6 túi đồ (Dùng/Bán/Bỏ), 7 nhiệm vụ → `handleQuestPageKey`,
  8 đồ giám/ghép sủng vật → `handlePetBookKey`, 9 hệ thống → `handleSystemPageKey` (= `ab.h(I)V` mà Patch.java móc, chỗ "Cài đặt nhạc"),
  12 đổi hình dạng sủng vật, 13 chọn đối tượng dùng vật phẩm, 14 học kỹ năng, 15 cửa hàng (`handleShopBuyKey` khi chọn số lượng).
  Các trạng thái con `as.q_B/t_B/v_B/w_B/z_B/p_B/A_B/B_B` để cụm ui_as đặt tên.
* `applySetBonus` MED: cộng chỉ số theo số món cùng bộ (≥2/4/6), theo nghề `h_S`; giả định `o.d(bl)` tính lại chỉ số trước đó.
* `handlePetBookKey` MED: "Đồ giám" là cách gọi trong game; trang 8 vừa xem đồ giám (3 tab) vừa ghép sủng vật từ nguyên liệu.

## Lỗi / mã chết nhận thấy (không sửa)
* `ab.isInButton(px,py,x,y,w,h)` bỏ qua `w,h`, cố định 30x30 (mọi nơi gọi đều truyền 30 nên vô hại).
* `bs.handleKey` mục "Game hay đề cử": vòng `for (var4 = 0; var2 < len; var4++)` kiểm sai biến; chỉ thoát khi gặp dấu `&` thứ 3 —
  URL có ít hơn 3 `&` sẽ ném `StringIndexOutOfBoundsException`.
* Trang âm lượng (bs trang 3 và ab trang hệ thống 3): phím trái gọi `Main.music.b(...)` không kiểm null → NPE nếu nhạc chưa từng phát.
* `bs.menuLabelImage` luôn null (đường dẫn hk2.gf bị bỏ) → nhánh vẽ menu bằng ảnh là mã chết.
* `ab.g` mục "Thuộc tính" in debug ra System.out ("Cấy={...}"); `ab.d` in "petID".
* Sau khi đổi tên, móc `ab.h(I)V` trong Patch.java (và `av` `at.a:Z`) phải áp lên JAR gốc hoặc cập nhật theo tên mới.

---
name: "j2me-viet-hoa"
description: "Việt hoá trọn gói game Java ME (J2ME) .jar/.jad/MIDlet: kiểm kê, ghép font tiếng Việt, trích xuất – tự dịch – patch text trong resource và .class, vẽ lại ảnh có chữ, sửa khung thoại/xuống dòng, preverify và test headless bằng FreeJ2ME để bản vá chạy được trên KVM/CLDC thật. Đã gồm sẵn phần dịch tay tiết kiệm token (`dp`, thay skill dich-pro) và cách quản lý context/usage cho dự án dài (thay skill code-toi-uu). Dùng skill này mỗi khi người dùng muốn việt hoá, dịch, patch font/chữ/ảnh của game điện thoại cũ, game 'cục gạch', file .jar/.jad, MIDlet, J2ME, CLDC, MIDP — kể cả khi chỉ gửi file .jar kèm một câu ngắn như 'việt hoá game này'. Cũng dùng khi người dùng nhờ dịch file text trích từ game (text_can_dich, tsv/xlsx/json, 'dịch hộ', 'dịch pro', 'dịch tay'), hoặc nhắc tới tiết kiệm usage/token, context đầy, compact, autocompact, session dài trong lúc làm dự án J2ME."
---

# Việt hoá game J2ME

**Ba phần của skill:** quy trình patch (file này) · dịch tay bằng `dp` ([references/dich-pro.md](references/dich-pro.md)) · quản lý context/usage ([references/toi-uu-token.md](references/toi-uu-token.md)). Việc gì chỉ là dịch file text thì vào thẳng dich-pro.md, bỏ qua Bước 0 nếu người dùng đã nêu phong cách.

Mục tiêu: một file .jar tiếng Việt chạy đúng trên máy KVM/CLDC thật. FreeJ2ME dùng để test; J2ME Loader chạy qua Android/ART nên chỉ dùng để đối chiếu, không lấy làm chuẩn. Quy trình tự động từ đầu đến cuối: AI tự dịch, tự patch, tự test và tự sửa lỗi, chỉ dừng lại ở hai câu hỏi bắt buộc dưới đây hoặc khi thiếu thứ chỉ người dùng mới cung cấp được.

## Bước 0 — Hỏi trước khi làm (bắt buộc)

Phong cách dịch và font (cỡ + kiểu) quyết định toàn bộ phần còn lại (độ dài câu, chỗ xuống dòng, cách ghép font, cỡ chữ trong ảnh). Đoán sai thì phải làm lại từ đầu, nên **luôn hỏi cả hai trong cùng một tin nhắn trước khi dịch hay patch bất cứ thứ gì**, kể cả khi người dùng đã nói "tự làm hết". Chỉ bỏ qua nếu chính tin nhắn của họ đã trả lời rõ.

Để câu hỏi có gợi ý cụ thể, có thể chạy trước `setup_tools.sh` + `inventory.py` (chỉ đọc, không sửa gì):

1. **Phong cách dịch.** Nêu vài lựa chọn hợp với thể loại game, ví dụ: cổ trang/kiếm hiệp dùng Hán Việt (huynh – đệ, tại hạ); Hán Việt nhẹ, dễ đọc; hiện đại/đời thường; giữ tên riêng gốc hay phiên âm Hán Việt.
2. **Font: cỡ (px) và kiểu.** Nêu chiều cao font gốc của game (lấy từ inventory/font.bin) và các font có file trong bộ tool: cỡ **18, 15, 13px**, mỗi cỡ 15/13 có **3 kiểu**: *có chân* (`f15`, `f13`: nét của f18, rộng cố định 7, rộng nhất), *chân chọn lọc* (`f15c`, `f13c`: như không chân nhưng `I i j l J 1 4` giữ chân, dễ đọc, chỉ rộng hơn không chân ~2px mỗi câu ngắn) và *không chân* (`f15s`, `f13s`: gọn nhất). 18px chỉ có bản có chân. Gợi ý cỡ gần chiều cao font gốc; ô chữ hẹp, khung thoại nhiều chữ thì gợi ý `c` hoặc `s` (bản có chân rộng hơn ~40%). Nhắc rằng 13px là cỡ nhỏ nhất: chữ hoa có hai dấu (Ấ, Ẩ…) sát đỉnh chữ. Đặc tính từng font: [references/bitmapfont.md](references/bitmapfont.md). Không đưa ra cỡ/kiểu không có file; người dùng muốn cỡ khác thì xin font của họ.

Trong lúc chờ trả lời: chỉ được kiểm kê và đọc code, chưa sửa file nào.

## Chuẩn bị

```bash
bash scripts/setup_tools.sh <Tool_Viet_Hoa.zip> work   # giải nén tool (cây sạch NAM.P), tải CFR + ProGuard
source work/tools/env.sh
python3 scripts/inventory.py game.jar                  # manifest, API thật, class vẽ chữ, font riêng, file có chữ Hán
env -u JAVA_TOOL_OPTIONS java -jar $CFR game.jar --outputdir src --silent true
```

Cần bash và JDK đầy đủ (có `jmods/java.base.jmod`, dùng cho preverify và `javap`). Lệnh chi tiết của từng tool (BitmapFont, pixeltext, FreeJ2ME): [references/toolkit.md](references/toolkit.md); font chạy trong game: [references/bitmapfont.md](references/bitmapfont.md).

## Quy trình

1. **Tìm đường đi của chữ.** Từ inventory, xác định:
   - class nào load từng file text, định dạng file ra sao (`readUTF`, bảng offset, độ dài cố định...);
   - chữ được vẽ bằng font hệ thống (`Graphics.drawString`) hay renderer bitmap riêng (class đọc `font.bin`);
   - hàm đo độ rộng và hàm xuống dòng/phân trang nằm ở đâu.

   Chỉ đọc kỹ các class liên quan tới text. Không cần dựng call-graph cả game hay đổi tên class.
2. **Font.** Chỉ dùng font trong bộ tool người dùng gửi, ở cỡ và kiểu đã chọn. Không tự vẽ hay tạo glyph mới: font tự chế lệch nét với bộ công cụ của họ và khó bảo trì. Thiếu ký tự thì liệt kê ra và xin người dùng. Hai cách ghép:
   - **Ghép glyph Việt vào font gốc của game** (giữ nguyên glyph cũ). Ưu tiên khi game có renderer riêng và cỡ chọn gần bằng chiều cao font gốc.
   - **Chuyển renderer sang `BitmapFont`** (một class + file `f<cỡ><kiểu>.bin` trong `/font/` của jar, vd. `f13c.bin`; chọn bằng `BitmapFont.setDefault(13, BitmapFont.SELECTIVE)`; API `drawString`/`stringWidth`/`wrap`/`lineCount`...). Đổi cỡ/kiểu chỉ là đổi file `.bin` và tham số, không phải build lại class. `BitmapFont` dùng `drawRegion`/`drawRGB`/`createRGBImage` (MIDP 2): kiểm inventory xem game đã dùng API MIDP 2 chưa; game thuần MIDP 1 thì phải ghép vào font gốc. Mỗi màu chữ mỗi font tốn 63–112 KB heap (cache 4 màu; bản không chân/chọn lọc nhẹ hơn bản có chân ~22%): chọn cỡ và số màu theo heap máy đích, xem bảng bộ nhớ ở file tham chiếu.

   Dùng advance width thật của từng glyph (`stringWidth`/`charWidth`), không hardcode. API, bảng cỡ, bộ nhớ: [references/bitmapfont.md](references/bitmapfont.md).
3. **Trích xuất text** ra `work/text_can_dich.tsv` với các cột: `id`, `file`, `offset/khoá`, `gốc`, `dịch`, `ghi chú ngữ cảnh`, `giới hạn (px hoặc ký tự/dòng)`. Giữ nguyên và đánh dấu mã điều khiển (`\n`, mã màu, `%s`, `{0}`, ký tự lệnh của engine).
   - Trước khi dịch, viết bộ đóng gói và kiểm tra **trích xuất → đóng gói lại khi chưa sửa** ra file giống hệt từng byte. Chưa đạt điều này thì chưa patch được.
   - Chuỗi trong `.class` chỉ được dịch khi chứng minh được nó chỉ để hiển thị. Chuỗi dùng làm khoá `Hashtable`, ID, tên resource, hoặc tham gia `==`/`equals` để rẽ nhánh thì giữ nguyên.
4. **Dịch.** Đọc [references/dich-pro.md](references/dich-pro.md) rồi dịch bằng `scripts/dp.py` (`dp prep → next/add → check → sample → merge`), truyền kèm phong cách đã chốt ở Bước 0 và cột giới hạn. Luật cứng của phần này: Claude tự dịch tay từng dòng, **cấm subagent và dịch máy**, hết lượt thì dừng báo `M/N` rồi chạy tiếp từ `out.tsv`. Các quy tắc chính:
   - thống nhất xưng hô theo quan hệ nhân vật (`vai.md`), lập bảng thuật ngữ cho tên riêng, địa danh, kỹ năng, vật phẩm (`thuat-ngu.tsv`);
   - chỉ số/UI dùng viết tắt tiếng Anh thông dụng (HP, MP, EXP, ATK, DEF, SPD, MENU);
   - giữ nguyên mã điều khiển, không vượt giới hạn (đo bằng `pixeltext measure`/`check` ở cỡ đã chọn), không có ký tự ngoài bảng glyph (`dp prep --charset`).

   `dp check` phải ra `OK`; lỗi nào cũng tự sửa bằng `dp fix`.
5. **Patch text + code hiển thị.** Đây là các bẫy của game Trung, gần như game nào cũng dính:
   - **Độ rộng theo ngưỡng mã ký tự**, kiểu `c < 0x7F ? h/2 : h` hoặc `len * 12`. Chữ Việt có dấu (≥ 0x80) sẽ bị tính rộng gấp đôi, làm chữ thưa và lệch canh giữa. Sửa hàm đo và hàm vẽ để dùng advance thật của từng glyph.
   - **Xuống dòng theo từng ký tự**: đúng với chữ Hán nhưng cắt đôi từ tiếng Việt. Sửa để ngắt tại dấu cách và tính theo px; phân trang phải sửa đồng bộ. Với `BitmapFont` dùng `wrap(text, maxW)`/`lineCount`, gọi một lần khi chuỗi đổi rồi giữ kết quả.
   - **Font hệ thống trên máy thật thường thiếu dấu tiếng Việt.** Mọi `drawString` hiển thị chữ Việt phải đi qua font bitmap.
   - **Độ dài trong resource**: `readUTF` có tiền tố u2 tính theo byte (UTF-8 biến thể; chữ Việt chiếm 2–3 byte). Mọi bảng offset, bộ đếm và tiền tố độ dài phải tính lại.
   - **Khung thoại** chứa tối đa 2 dòng hoặc không có nền trong suốt: làm lại tối thiểu 3 dòng, nền bán trong suốt, sửa cả word-wrap lẫn phân trang.
   - Không tạo `String`/object mới trong `paint()` hay game loop: heap máy thật rất nhỏ, GC giật hình. Cache kết quả đo và ngắt dòng.
6. **Ảnh có chữ.** Tìm bằng cách xem ảnh (inventory liệt kê PNG) và đối chiếu ảnh chụp màn hình. Hiểu nghĩa chữ trước, rồi dùng `pixeltext patch`/`draw` ở cỡ đã chọn:
   - xoá hết chữ gốc;
   - một màu duy nhất = màu thân chữ gốc (không lấy màu viền);
   - nét đơn đúng glyph: không tự làm đậm, không tự thêm viền, bóng hay gradient nếu người dùng không yêu cầu;
   - nhãn dài hơn ô thì rút gọn chữ, không ép chữ;
   - chữ bị chìm vào nền thì báo người dùng.

   Lưu ý riêng của font: chỉ còn `,` và `‚` trùng glyph (mọi font); `0`/`O`, `I`/`l`/`1` đều phân biệt được. Ở 13px chữ hoa có hai dấu sát đỉnh chữ. Bản có chân giữ thiết kế f18 nên móc `ơ ư Ơ Ư`, gạch `đ` chạm cột thứ 7 (cột cách chữ); vá vào bảng glyph 8×16 cần cột 7 trống thì dùng bản `s`/`c`. Luôn chạy `pixeltext check --size <font đã chọn>` trước khi chốt.
7. **Build → kiểm → chạy** sau mỗi lần patch. Không bỏ bước nào: "game vẫn chạy" chưa chứng minh được bản vá an toàn trên KVM.

   ```bash
   bash scripts/preverify.sh patched_raw.jar patched.jar      # sinh StackMap CLDC
   python3 scripts/jar_check.py game.jar patched.jar          # phải RESULT: PASS
   python3 scripts/run_emu.py --fj $FJ --jar patched.jar --out shots \
       --steps "wait 10; shot boot; key w; wait 4; shot s1; key Return; wait 3; shot s2"
   ```

   `jar_check` dùng để so sánh trước/sau và chặn các lỗi: class version > 48, còn `StackMapTable` (KVM sẽ ném `VerifyError` dù emulator chạy được), thiếu `StackMap`, API ngoài CLDC/MIDP, public member đổi descriptor. Xem ảnh chụp để soát chữ thưa, đè, tràn khung, xuống dòng sai, sót chữ Hán. Có exception thì truy tận gốc, sửa rồi chạy lại. Lỗi MIDI `Sequencer ... is null` là do môi trường headless không có bộ phát nhạc, không phải lỗi game.

## Quản lý context và usage (dự án dài)

Việt hoá trọn gói là việc nhiều giai đoạn, rất dễ phình context. Quy tắc tối thiểu, chi tiết ở [references/toi-uu-token.md](references/toi-uu-token.md):

- Đầu mỗi session: đọc `PROGRESS.md` của dự án (mẫu: [references/progress-template.md](references/progress-template.md)), không đọc lại cả codebase. Chưa có thì tạo ngay sau Bước 0.
- Dò bằng `grep`/`sed -n` theo dải dòng, output lớn ghi ra file rồi `head`; không đọc nguyên file dài, không đọc lại thứ đã đọc.
- Cập nhật `PROGRESS.md` sau mỗi mốc và trước khi compact; mỗi giai đoạn (kiểm kê → font → dịch → patch → test) một session.
- Subagent chỉ cho tìm kiếm/phân tích. Việc dịch luôn do Claude tự làm bằng `dp`.
- Việc lặp lại thì viết script một lần, không xử lý tay từng mục qua chat.

## Ràng buộc tương thích

- Chỉ dùng API có trong CLDC/MIDP của máy đích; thứ chỉ chạy được nhờ Android/Java SE (reflection, nạp class động...) phải gắn nhãn **⚠️ NOT J2ME-SAFE** và không tính là xong.
- Không đổi tên/descriptor/access flag của class, field, method có sẵn. Không đổi kiểu dữ liệu (`byte↔short↔int`) nếu chưa rà hết các chỗ dùng, kể cả save/load.
- "Hook" luôn là sửa bytecode tĩnh trước khi preverify; CLDC không có live-patching.
- Decompiler dùng để đọc; khi kết quả decompile mâu thuẫn với `javap -c`, tin bytecode. Class mới phải biên dịch kiểu CLDC (ecj `-source 1.3 -target 1.3`, như `font/tools/build_class.sh`) rồi mới preverify; class `javac` mặc định (version ≥ 49, có `StackMapTable`) sẽ bị `jar_check` chặn.
- Text rải rác ở nhiều chỗ gọi, có nguy cơ dính `==`, hoặc phải sửa bản dịch nhiều vòng: cân nhắc làm lớp hook dịch, xem [references/phuong-an-2-hook-bytecode.md](references/phuong-an-2-hook-bytecode.md).

## Bàn giao

Mỗi mốc là một file **.zip** gồm: jar, jad (cập nhật `MIDlet-Jar-Size` cho khớp jar mới), `text_can_dich.tsv` đã dịch (kèm `thuat-ngu.tsv`, `vai.md` từ bước dịch), ảnh chụp FreeJ2ME và `BAO_CAO.md` ngắn (đã sửa gì, kết quả `dp check`, kết quả `jar_check`, việc còn tồn, ký tự font thiếu nếu có). Gửi mốc rồi làm tiếp ngay, không chờ phản hồi:

1. **Mốc 1**: font đã ghép, màn hình đầu tiên có chữ (text hoặc ảnh) hiện tiếng Việt.
2. **Mốc 2**: toàn bộ text trong resource và `.class` đã được dịch và patch.
3. **Mốc cuối = bàn giao toàn bộ dự án** (không chỉ gửi jar): ảnh, khung thoại, xuống dòng đã xong; đã đi qua các màn chính bằng `run_emu.py` mà không có exception. Gói thành `<ten_game>_du_an.zip` để sau này người khác (hoặc phiên sau) debug mà không phải dựng lại từ đầu:
   ```
   <ten_game>_du_an/
     README_DU_AN.md      bản đồ thư mục, thứ tự các bước, quyết định đã chọn (cỡ + kiểu font, phương án 1/2, vì sao)
     PROGRESS.md          tiến độ ngắn cho session sau (mẫu ở references/progress-template.md)
     BAO_CAO.md           như mọi mốc + mục "Debug": lỗi đã gặp và cách xử lý, ký tự font thiếu, việc còn tồn
     rebuild.sh           chạy lại toàn bộ từ jar gốc tới jar cuối (patch → preverify → jar_check → run_emu)
     out/                 jar + jad cuối (MIDlet-Jar-Size đã khớp), text_can_dich.tsv đã dịch, ảnh chụp FreeJ2ME từng màn
     goc/                 jar + jad gốc, text_can_dich.tsv gốc, ảnh gốc trước khi vá (để so sánh/hoàn tác)
     src/                 mã decompile (CFR) của các class đã đụng tới + bản class đã sửa/hook
     scripts/             MỌI script đã viết hoặc chạy cho game này (patch text, vá ảnh, ghép font, sửa class, đóng jar)
     logs/                đầu ra `inventory.py`, `jar_check.py`, `run_emu.py` (kể cả lần lỗi), danh sách file/class đã sửa
     tools/               đúng bộ tool đã dùng: fonts/, fontgen/, java/ (BitmapFont), pixeltext-kit/, check_all.sh, env.sh,
                          freej2me_plus.jar, cfr.jar, proguard.jar; thiếu file nào thì ghi tên + phiên bản + URL tải vào tools/VERSIONS.md
   ```
   Trước khi nén: chạy `rebuild.sh` một lần để chắc nó tái tạo đúng jar cuối, và `tools/check_all.sh` để chắc tool lành; ghi kết quả hai lệnh này vào `BAO_CAO.md`. Đường dẫn trong script/README dùng đường dẫn tương đối theo thư mục dự án, không hardcode `/home/...`. Xong thì gửi zip và báo ngắn đã làm gì, còn gì tồn.

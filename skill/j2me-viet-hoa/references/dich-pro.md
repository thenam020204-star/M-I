# Dịch Pro — dịch tay, tiết kiệm token

(Tham chiếu của skill `j2me-viet-hoa`, dùng cho Bước 4 — Dịch. Mục lục: Luật cứng · Tiết kiệm token · Văn phong · Quy trình bước 0–5.)

Phần này chỉ lo **một việc**: biến file text gốc thành file text tiếng Việt nạp lại được ngay. Patch font/bytecode/ảnh nằm ở SKILL.md chính. Bản dịch hỏng thường do **phá cấu trúc** (mất `\n`/mã màu/`%s`, tràn khung, thiếu glyph) hoặc **lệch xưng hô/thuật ngữ** — quy trình dưới chặn đúng các lỗi đó.

Ký hiệu: `dp` = `python3 <thư mục skill j2me-viet-hoa>/scripts/dp.py`, chạy trong thư mục chứa file cần dịch (dữ liệu làm việc ở `dp_work/`). Cần python3; xlsx cần `openpyxl`. `<TAB>` là ký tự tab thật.

## Luật cứng

1. **Dịch tay**: mọi chuỗi tiếng Việt do chính Claude viết, trực tiếp trong lệnh `dp add`. **Cấm**: subagent/agent/Task (kể cả "chia khối dịch song song"); API hoặc thư viện dịch máy (googletrans, deep-translator, argos, curl tới dịch vụ…); script dùng từ điển/regex để sinh bản dịch; điền tạm "dịch sau"; tóm tắt thay cho dịch. Script chỉ làm việc cơ học: tách, hiển thị, ghi, kiểm, ghép.
2. **Không bịa**: không thêm câu, bỏ ý, đổi số/tên/giá trị. Gốc mơ hồ → dịch sát nhất, ghi 1 dòng vào `dp_work/ghi-chu.txt`.
3. **Hết lượt/ngân sách giữa chừng → dừng**, báo tiến độ `M/N`; lần sau chạy tiếp từ `out.tsv`. Không đổi sang cách rẻ hơn (agent, dịch máy) cho xong.

## Tiết kiệm token

- Đọc mỗi dòng gốc **đúng một lần** (qua `dp next`) rồi dịch ngay. Không đọc trước cả file, không đọc lại.
- Không viết lời dẫn, không in lại bản dịch hay giải thích trong chat. Mỗi lô = **một lệnh bash**: ghi lô vừa dịch **và** lấy lô kế.
- Không suy luận dài theo từng dòng; chỉ cân nhắc ở dòng thực sự mơ hồ.
- Chỉ trao đổi dạng `k<TAB>dịch` (k là số thứ tự ngắn script cấp); không chép lại gốc.
- Sửa lỗi bằng `dp fix` đúng dòng lỗi, không viết lại file.
- `vai.md` ≤ 40 dòng; thuật ngữ chỉ thêm mục thật cần (tên riêng, chiêu thức/vật phẩm/địa danh lặp, chỉ số).

## Văn phong mặc định

Phong cách đã chốt ở Bước 0 của SKILL.md chính và yêu cầu riêng của dự án (vd. "hài hước", "trung cổ", "chữ thường") luôn thắng các mặc định dưới đây:

- **Câu thuần Việt**, tự nhiên như người Việt nói, không dịch word-by-word kiểu convert.
- **Danh từ, thuật ngữ, chiêu thức, môn phái, chức danh → Hán Việt ngắn gọn** (天剑诀 → Thiên Kiếm Quyết); khung 240x320 rất chật.
- **Tên người, địa danh → chữ Latin** (Hán Việt không dấu hoặc tên Latin; dự án đã chốt kiểu Tây như Angela/Zeke thì theo). Thống nhất tuyệt đối toàn game.
- **Chỉ số/UI → viết tắt Anh**: Sinh mệnh → HP, 法力 → MP, ATK, DEF, SPD, EXP. Nút/lệnh dùng từ ngắn nhất ("Tấn công" → "Đánh").
- **Có dấu đầy đủ** trừ khi charset font thiếu glyph. Game tiếng Anh in hoa toàn bộ → dịch viết thường bình thường.
- Mã điều khiển giữ đúng vị trí tương ứng về nghĩa; placeholder giữ nguyên số lượng, thứ tự.
- Giới hạn độ dài: tôn trọng `≤N` nếu có. Không có → lấy độ dài gốc làm mốc cho UI/menu (tiếng Việt dài hơn chữ Hán 2–3 lần nên rút gọn chủ động); hội thoại được dài hơn vì có wrap.
- Thuật ngữ/tên đã chốt ở dự án trước cùng series là nguồn ưu tiên cao nhất.

## Quy trình

### Bước 0 — Chuẩn bị (1 lệnh)

`dp prep FILE [--lang zh|en] [--glossary cũ.tsv] [--charset glyph.txt] [--codes REGEX]`

Script tự: nhận cột theo header (id / gốc / dịch / ghi chú / giới hạn), gộp dòng trùng, đánh số `k`, loại dòng **KHÔNG DỊCH** (key, ID, tên file → `dp_work/skipped.tsv`, giữ nguyên), liệt kê mã điều khiển tìm thấy. Nhận sai cột → thêm `--src N --dst N --ctx N --lim N` (đếm từ 0), `--force` để làm lại. File không UTF-8 → `--enc`. JSON chỉ nhận dạng `{id: text}`; dạng khác đổi sang TSV trước. Chỉ mở `skipped.tsv` khi số dòng bỏ qua bất thường.

### Bước 1 — Khởi tạo vai (rẻ)

`dp peek 60` (60 dòng rải đều) → viết `dp_work/vai.md`: nhân vật gốc→dịch, giới tính/vai vế, **xưng hô theo cặp** (sư phụ→đồ đệ: ta/con; ngang hàng thân: tôi/cậu; kẻ thù: ta/ngươi…). Thêm thuật ngữ đầu vào `dp_work/thuat-ngu.tsv` (`gốc<TAB>dịch<TAB>loại<TAB>ghi chú`; loại `~` = không ép kiểm tra). Không đọc cả truyện: gặp nhân vật/thuật ngữ mới hoặc quan hệ đổi thì **append** ngay vào `vai.md`/`thuat-ngu.tsv` trong chính lệnh bash của lô đó.

### Bước 2 — Vòng dịch

Lô đầu: `dp next`. Mỗi lô sau (một lệnh bash duy nhất):

```
cat >> dp_work/thuat-ngu.tsv <<'EOF'      # chỉ khi có thuật ngữ mới
gốc<TAB>dịch<TAB>loại
EOF
dp add <<'EOF' && dp next
12<TAB>bản dịch dòng 12
13<TAB>bản dịch dòng 13
EOF
```

`dp next` in: tiến độ, thuật ngữ đã chốt khớp lô, `#ctx` (khi đổi người nói/ngữ cảnh), rồi `k<TAB>gốc[<TAB>≤giới hạn]` (lô ≈ 5000 ký tự; chỉnh `--chars`, `-n`). Sau khi mất ngữ cảnh dùng `dp next --vai` để nạp lại `vai.md`.

`dp add` tự khớp khoảng trắng đầu/cuối, **từ chối cả lô** nếu k không liền mạch, và cảnh báo ⚠ ngay khi lệch mã/placeholder, còn chữ Hán, vượt giới hạn, thiếu glyph. Có cảnh báo → sửa ngay ở lô sau:

```
dp fix <<'EOF'
17<TAB>bản sửa
EOF
```

### Bước 3 — Kiểm toàn bộ

`dp check` — chỉ in lỗi (hoặc `OK`): đủ dòng, mã/placeholder, chữ Hán sót, độ dài, glyph, thuật ngữ lệch. Sửa bằng `dp fix` đến khi `OK`.

### Bước 4 — Soát mẫu

`dp sample 30`: đọc gốc→dịch, bắt lỗi xưng hô, giọng nhân vật, câu bịa/sót; sửa bằng `dp fix`. Chỉ rà toàn bộ khi người dùng yêu cầu ("dịch kỹ", "rà soát toàn bộ") — vẫn tự đọc, không dùng agent.

### Bước 5 — Bàn giao

Trong dự án Việt hoá trọn gói: chạy `dp merge`, chép file ra `out/` rồi đưa kết quả `dp check` + danh sách KHÔNG DỊCH + chỗ gốc mơ hồ vào `BAO_CAO.md` theo mục Bàn giao của SKILL.md chính (không đóng zip riêng, `thuat-ngu.tsv` và `vai.md` để trong `out/`). Khi người dùng chỉ nhờ dịch một file text rời, làm như sau:

`dp merge` → `dp_work/final/<tên file gốc>` (cùng định dạng, cột dịch đã điền; dòng KHÔNG DỊCH được chép nguyên; chưa dịch hết thì cần `--partial`). Đóng gói **một .zip** gồm: file dịch, `thuat-ngu.tsv`, `vai.md`, `bao-cao.md` (≤ 25 dòng: số dòng dịch / giữ nguyên / trùng, danh sách KHÔNG DỊCH ≤ 30 mục, chỗ gốc mơ hồ từ `ghi-chu.txt`, chuỗi rút gọn mạnh, glyph thiếu, kết quả `check`). Trả lời người dùng ≤ 5 dòng tiếng Việt: đã dịch bao nhiêu dòng, còn gì cần họ quyết; không liệt kê lại bản dịch.

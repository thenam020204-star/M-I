# HANDOFF — MMF (SMAF MA-3/MA-5) → FLAC + SoundFont SF2

Dán đoạn "PROMPT" bên dưới vào Claude Code, mở tại thư mục đã giải nén gói này.

---

## PROMPT

Bạn tiếp quản một dự án dở dang: chuyển nhạc SMAF `.mmf` của game J2ME sang FLAC, và đóng gói âm sắc FM của chip Yamaha MA-3 thành soundfont `.sf2`.

**Đọc trước:**
- `PLAN.md`: nguồn sự thật. Đọc kỹ các mục "Sự thật đã xác minh", "Quyết định thiết kế", "GIAI ĐOẠN 2", "SỬA GĐ3", toàn bộ "Nhật ký" và "Vướng mắc".
- `HANDOFF.md`: file này. Xem mục "Trạng thái" và "Môi trường".

**Cách làm việc** (skill `opus-plan-sonnet-build` nếu có): Opus làm quản đốc (lập kế hoạch, giao việc, nghiệm thu). Sonnet 5.5 làm công nhân và implement từng task. Mỗi lúc chỉ có 1 công nhân chạy thật. Mọi tiến độ ghi vào PLAN.md: tick task, viết Nhật ký, ghi Vướng mắc.

**Việc cần làm tiếp, theo thứ tự:**
1. **T7.** Hoàn thiện `tools/make_sf2.py` (đang là bản nháp WIP, chưa chạy thử, chưa kiểm chứng) để xuất `out/MA3_bgm20.sf2` từ `in/hl5/bgm_20.mmf`.
   - Mẫu lấy từ chính synth của V1 (`tools/ma3_synth.py` + `tools/ma3_voice.py`).
   - Melody lấy mẫu mỗi 3 nửa cung. Voice có sustain thì loop đúng bội số chu kỳ.
   - Writer SF2 tự viết.
   - Đây là task quản đốc phải nghiệm thu.
2. **T8.** Render demo `out/bgm_20_v7_sf2_demo.flac` bằng FluidSynth + sf2 mới. So phổ log-mel V7↔V1 và V3↔V1.
3. **T9.** Cho `make_sf2.py` nhận nhiều file:
   - Khử trùng voice theo hash byte.
   - Cùng số program mà voice khác nhau thì đẩy sang bank 1, 2, …
   - Phím trống trùng mà voice khác nhau thì đặt vào bank 128 preset 1, 2, …
   - Xuất kèm `sf2_map_*.csv`.
4. **T10 (bản đã sửa ở mục "SỬA GĐ3").** Xuất 3 bộ, mỗi bộ kèm map csv, và render 2 demo cho mỗi game:
   - `out/MA3_HL5.sf2` từ 21 file `in/hl5/`.
   - `out/MA3_HL4.sf2` từ 20 file `in/hl4/`.
   - `out/MA3_HL4_HL5.sf2` gộp: HL4 dời lên melody bank +16, drum preset +16.

**Lưu ý kỹ thuật quan trọng:**
- **Bytes là sự thật.** Không đoán. Chỗ nào không chắc thì ghi mức HIGH/MEDIUM/LOW vào Nhật ký.
- **HL4 khác định dạng HL5.** HL4 (`BGM_xx.MMF`, chip "Y933…") có header MTR và SysEx voice trong Mtsu khác bgm_20. Phải kiểm chứng lại parser bằng byte trước khi dùng.
- **Một số file HL5 nhúng sóng PCM.** Các file bgm_01, 02, 06, 13, 14 có SysEx rất dài trong Mtsu, là sóng PCM/ADPCM nhúng cho voice WT. Giải mã được thì dùng; không được thì bỏ voice đó, ghi cảnh báo, không làm hỏng cả bộ.
- **Trống bank 7D là voice PCM** dùng sóng ROM nội chip, không có trong file. Synth V1 đang xấp xỉ, độ tin cậy LOW.
- **Map GM của V3–V5 là đoán**: số program SMAF được dùng thẳng làm số GM.
- **Script đặt trong `tools/`.** Chạy python với `-I` và truyền đường dẫn qua tham số. Coi `in/` là dữ liệu chỉ đọc.

Mỗi task xong, báo người dùng 1–2 câu, rồi gửi file `.sf2` và demo FLAC.

---

## Trạng thái lúc bàn giao (2026-11-13)

**Đã xong (GĐ1).** Bài `bgm_20` có 6 bản FLAC, người dùng chọn **V1 (MA-3 FM gốc)** là ưng nhất. Trong gói chỉ kèm `out/bgm_20_v1_ma3_fm_goc.flac` để tham chiếu, cùng file `out/bgm_20.mid`. Các bản V2–V6 render lại được bằng script trong `tools/`.

**Chưa làm:** T7, T8, T9, T10. Công nhân trước bị dừng giữa chừng, `tools/make_sf2.py` là WIP.

## Cấu trúc gói

```
PLAN.md            kế hoạch + nhật ký đầy đủ
HANDOFF.md         file này
tools/             smaf_parse.py, smaf2mid.py, ma3_voice.py, ma3_synth.py (V1/V2),
                   render_fluid.py (V3/V4), adl_render.c + adl_finish.py (V5),
                   chiptune.py (V6), finish.py, check_audio.py, make_sf2.py (WIP)
in/bgm_20.mmf      bài gốc đầu tiên (= in/hl5/bgm_20.mmf)
in/hl5/            bgm_00..bgm_20.mmf  (game "HL5", chip YG32, © Dch 2008)
in/hl4/            BGM_00..BGM_19.MMF  (game "HL4", chip Y933)
out/bgm_20.mid, out/bgm_20_v1_ma3_fm_goc.flac   tham chiếu
```

## Môi trường cần có

**Gói pip:**
```
pip install numpy scipy numba mido soundfile pyfluidsynth
```
Cần thêm `libfluidsynth` (trên Debian/Ubuntu là `apt install libfluidsynth3`) và `ffmpeg`.

**Tài liệu tham khảo** (không kèm trong gói, tự clone):
- `github.com/but80/smaf825`: bố cục `VM3Exclusive` dùng để giải voice FM. Độ tin cậy HIGH.
- `github.com/but80/fmfm`: bảng envelope, DT, KSL, LFO cho synth. Đo trên YMF825/MA-5, độ tin cậy MEDIUM.

**Chỉ cần nếu render lại V4/V5:**
- `github.com/mrbumpy409/GeneralUser-GS`: soundfont cho V4.
- `github.com/Wohlstand/libADLMIDI`: cho V5. Build static, rồi link với `tools/adl_render.c`.

**Lệnh mẫu:**
- `python3 -I tools/smaf2mid.py in/bgm_20.mmf out/bgm_20.mid`
- `python3 -I tools/ma3_synth.py in/bgm_20.mmf out both`, ra V1 + V2.

Đường dẫn trong PLAN.md có thể còn trỏ tới `/home/claude/mmf/...` hay `/home/claude/ref/...` của phiên cũ. Đổi sang đường dẫn tương đối của gói này.

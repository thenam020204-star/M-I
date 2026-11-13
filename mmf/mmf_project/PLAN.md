# PLAN — Chuyển bgm_20.mmf (SMAF MA-3) sang FLAC, nhiều biến thể

## Mục tiêu
Từ `in/bgm_20.mmf` (chỉ đọc) tạo nhiều bản FLAC khác nhau để người dùng nghe và chọn. Kèm 1 file MIDI trung gian.

## Sự thật đã xác minh từ byte (quản đốc)
- `MMMD` 11309 B → `CNTI`, `OPDA` (© 2008 Dch), `MTR\x05` @107 len 11200.
- Header MTR 20 byte: format=0x02 (Mobile Standard, KHÔNG nén Huffman), seqtype=0x00, timebase dur=0x02, gate=0x02 (0x02 = 4 ms/tick).
- Chunk con MTR: `Mtsu` @135 len 820 (SysEx định nghĩa voice FM), `MspI` @963 len 16 (`st:`=0x0000000D, `sp:`=0x00002849 → offset BYTE trong dữ liệu Mtsq: vòng lặp từ byte 13 đến byte 10313), `Mtsq` @987 len 10320.
- KHÔNG có `Mtsp` (không có PCM) → mọi voice là FM.
- Voice SysEx trong Mtsu: `F0 <len> 43 79 06 7F 01 7C 01 <prog> ...` (bank 7C, melody, len 0x2F ≈ 4-op hoặc 0x19 ≈ 2-op) và `F0 1E 43 79 06 7F 01 7D 00 02 <key> ...` (bank 7D = drum, mỗi phím 1 voice FM). Có cả voice ID chung đầu tiên `F0 06 43 79 06 7F 7F F7`.
- Mtsq dạng giống MIDI: delta = varlen 7-bit; `9n key vel gate(varlen)`, `8n key gate(varlen)`, `Bn cc val`, `Cn prog`, `En lsb msb`, `F0 len .. F7`, `FF 2F 00` kết thúc, `FF 00` NOP. (Công nhân phải kiểm chứng bằng cách parse hết đúng 10320 byte, không lỗi, kết thúc bằng FF 2F 00.)
- Có sẵn: ffmpeg, python3+numpy/scipy, libfluidsynth3 (2.3.4, chưa có CLI → dùng pyfluidsynth qua pip hoặc build), `/usr/share/sounds/sf2/TimGM6mb.sf2`. pip và GitHub truy cập được; apt không có soundfont.

## Quyết định thiết kế
1. Script đặt ở `tools/` (không chạy code bên trong `in/`). Python chạy với `-I`.
2. `tools/smaf_parse.py`: parse MMF → danh sách event tuyệt đối (ms), voice FM, loop st/sp (map offset byte → thời điểm). Đây là nguồn chung cho mọi biến thể.
3. `tools/smaf2mid.py` → `out/bgm_20.mid` (480 PPQ, tempo cố định sao cho 1 tick SMAF = 4 ms; gate time → note-off; bank 7D/kênh drum → kênh 10 GM; program SMAF → map GM hợp lý, ghi bảng map vào Nhật ký).
4. Bố cục thời lượng mọi biến thể: phát hết 1 lượt, rồi lặp lại đoạn [st, sp] 1 lần nữa, fade-out 8 s ở cuối lượt 2. Đầu/đuôi không có im lặng thừa (> 0.3 s).
5. Master: peak chuẩn hoá −1 dBFS (không clip), FLAC mức nén 8. Mặc định 44.1 kHz/16-bit; riêng V2 48 kHz/24-bit.
6. Biến thể (đặt tên `out/bgm_20_vN_<ten>.flac`):
   - **V1 `ma3_fm_goc`**: synth FM numpy tự viết mô phỏng YMU762/MA-3 dùng ĐÚNG tham số voice trong Mtsu (bố cục tham số kiểu YMF825/SD-1: BO, LFO, ALG 0–7; mỗi op SR, XOF, KSR, RR, DR, AR, SL, TL, KSL, DAM, EAM, DVB, EVB, MULTI, DT, WS, FB). Mono, khô, giống loa điện thoại nhất. Phải tự xác định cách giải gói 7-bit/bố cục byte từ dữ liệu thật (đối chiếu nguồn mở trên GitHub nếu có: tìm "SMAF MA-3 voice", "YMF825 tone", "mmf2mid"). Không chắc chỗ nào thì ghi rõ vào Nhật ký, không bịa.
   - **V2 `ma3_fm_hifi`**: cùng synth V1 + pan theo CC10, reverb nhẹ, 48k/24-bit.
   - **V3 `gm_timgm6mb`**: MIDI + FluidSynth + TimGM6mb.sf2.
   - **V4 `gm_generaluser`**: MIDI + FluidSynth + GeneralUser GS (tải từ GitHub, vd repo mrbumpy409/GeneralUser-GS), bật reverb/chorus. Không tải được thì thay FluidR3_GM hoặc soundfont GM khác từ GitHub và ghi lại.
   - **V5 `opl3_fm_retro`**: MIDI render qua libADLMIDI (build từ GitHub, CLI `adlmidi` xuất WAV) với bank OPL3 mặc định.
   - **V6 `chiptune_8bit`**: synth numpy sóng vuông/tam giác/noise (drum = noise), phong cách NES.
7. Ngoài phạm vi: không sửa file gốc, không tạo bản MP3/OGG.

## Task
- [x] T1 (A1) Viết `tools/smaf_parse.py` + `tools/smaf2mid.py`. Hoàn thành khi: parse hết 10320 byte Mtsq không lỗi, in ra số note/kênh/thời lượng; `out/bgm_20.mid` mở được bằng `mido` và thời lượng khớp tổng delta×4ms; in bảng voice (bank/prog/2op/4op) từ Mtsu.
- [x] T2 (A1) V3 + V4 (FluidSynth). Hoàn thành khi: 2 file FLAC hợp lệ (`ffprobe`), thời lượng = lượt1 + đoạn lặp, peak ≤ −0.9 dBFS, không im lặng thừa.
- [x] T3 (A1) V5 (libADLMIDI). Tiêu chí như T2.
- [x] T4 (A1, **Quản đốc nghiệm thu**) Synth MA-3 FM → V1 + V2. Hoàn thành khi: dùng tham số voice thật của từng program/drum key; ghi trong Nhật ký bố cục byte đã giải và mức độ chắc chắn (HIGH/MEDIUM/LOW) từng trường; nghe được (không rỗng, không NaN, không clip); tiêu chí chung như T2.
- [x] T5 (A1) V6 chiptune. Tiêu chí như T2.
- [x] T6 (A1) Kiểm tra cuối: bảng tóm tắt mọi file trong `out/` (tên, thời lượng, sample rate, bit depth, peak dBFS, kích thước) ghi vào Nhật ký; tạo thêm ảnh phổ nhỏ không bắt buộc.

## Nhật ký
- T1 xong: tools/smaf_parse.py (+ layout()/two_pass_notes()/channel_setup() dùng chung), tools/smaf2mid.py -> out/bgm_20.mid (480 PPQ, tempo 1.92e6 µs/beat => 1 tick MIDI = 1 tick SMAF = 4 ms). Kiểm tra: parse đúng 10320 B, kết thúc FF 2F 00, 2270 event, 2189 note (16 kênh), mido length 71.468 s = 17867×4 ms. Hiệu chỉnh so với PLAN: (a) varlen dùng MIDI chuẩn (cách "SMAF +128" cho gate ra bội số beat lẻ 401/264, chuẩn cho 273=6×45.5, 546=12×45.5 -> chuẩn đúng); (b) st=byte 13 -> tick 0, sp=byte 10313 (delta byte của FF 2F 00) -> tick 17867: vòng lặp = TOÀN BÀI 71.468 s; (c) mọi voice melody len 0x2F (38 byte dữ liệu sau prog, KHÔNG phải 2op/4op theo len; 0x19 chỉ là prog số 25), drum len 0x1E (20 byte dữ liệu sau key); 12 voice melody + 7 drum key (35,36,40,41,45,49,52 đều được dùng); ch9 = drum (bank 7D/00), ch khác bank 7C/01. Layout chung: đầu giữ 100 ms (note đầu ở 544 ms gốc, cắt 444 ms), cuối = note-off cuối pass 2 + 50 ms (TAIL_MS, đã chỉnh từ 300 để đuôi <-60 dB ≤0.3 s) => tổng 141.984 s (thay vì 2L=142.936 s, để không có im lặng thừa; fade 8 s). Bảng map GM: prog SMAF giữ nguyên làm prog GM 0-based (25,47,69,18,40,79,62,113,35,54,10,101; ch9 -> drum kit 0); là suy đoán (voice là FM tuỳ biến), không có sự thật nào khác.

- T2 xong: tools/render_fluid.py (pyfluidsynth, mô phỏng sequencer theo mido, 44.1k/16-bit TPDF dither, fade tuyến tính 8 s trong tools/finish.py, peak -1 dBFS), MIDI 2 lượt tạm /tmp/two.mid (smaf2mid.py --two-pass). V3 TimGM6mb (reverb/chorus tắt), V4 GeneralUser GS (clone mrbumpy409/GeneralUser-GS qua add_repo, ~/mrbumpy409/generaluser-gs/GeneralUser-GS.sf2 32 MB, reverb/chorus bật). tools/check_audio.py: cả 2 file 141.984 s, peak -1.00 dBFS, đầu im 0.10 s, đuôi <-60 dB 0.28/0.20 s, không NaN.

- T4 xong (cần quản đốc nghiệm thu): tools/ma3_voice.py (giải voice), tools/ma3_synth.py (synth numba 48 kHz), out/bgm_20_v1_ma3_fm_goc.flac (44.1k/16, mono, 141.732 s, peak -1.00 dBFS), out/bgm_20_v2_ma3_fm_hifi.flac (48k/24, stereo pan theo CC10 + reverb Schroeder nhẹ, 141.732 s, peak -1.00). Không NaN, không clip, DC-block 20 Hz, mọi giây RMS > -44 dBFS. Chạy: python3 -I tools/ma3_synth.py in/bgm_20.mmf out both.
  NGUỒN ĐỐI CHIẾU (chỉ đọc, đã clone vào /home/claude/ref, KHÔNG build/chạy): but80/smaf825 (smaf/voice/vm35fm.go, smaf/subtypes/exclusive.go – bố cục byte voice MA-3 'VM3Exclusive') và but80/fmfm (sim/*.go, ymf/ymfdata/data.go – mô hình đo của YMF825/MA-5: envelope, tần số, DT, KSL, LFO, vibrato/tremolo, 29 dạng sóng, thuật toán ALG).
  BỐ CỤC BYTE đã giải. SysEx voice: F0 len 43 79 06 7F 01 | bankMSB bankLSB PC drumNote voiceType(0=FM,1=PCM) | 36 byte dữ liệu (FM) rồi F7. 36 byte = 4 byte global + 4 op x 8 byte, byte đầu mỗi nhóm chứa bit 7 (MSB) “gói” của các byte kế (7-bit safe): raw[2]|=raw[0]<<2&0x80 (PAN), raw[3]|=raw[0]<<3&0x80 (LFO bit), với op k: raw[4+8k..7+8k] nhận bit 4..7 từ raw[8k] (SR,RR,AR,TL), raw[10+8k],raw[11+8k] nhận bit từ raw[8+8k] (MULTI,WS). Sau giải gói: global = drumkey, PAN<<3|BO, LFO<<6|PE<<5|ALG; mỗi op 7 byte: [SR<<4|XOF<<3|SUS<<1|KSR] [RR<<4|DR] [AR<<4|SL] [TL<<2|KSL] [DAM<<5|EAM<<4|DVB<<1|EVB] [MULTI<<4|DT] [WS<<3|FB].
  MỨC CHẮC CHẮN: bố cục byte/giải gói = HIGH (khớp 2 nguồn độc lập về tên trường, và dữ liệu thật nhất quán: cả 12 voice cho PAN=15 (giữa), BO=1 (bình thường), PE=0, WS luôn hợp lệ (0,1,4,6,8-10,12,16,18,20,21,29; không có 15/23/31), TL của carrier nhỏ, ALG∈{3,5,7}); ALG/BO/LFO = HIGH (bố cục) / MEDIUM (ý nghĩa tần số LFO 1->4.0 Hz, 2->5.9 Hz lấy từ YMF825); MULTI/DT/AR/DR/SR/RR/SL/TL/KSL/FB/WS/EAM/DAM/EVB/DVB = HIGH về bố cục, MEDIUM về ý nghĩa thời gian/mức (bảng của YMF825/MA-5, MA-3 thật chưa đo); XOF = MEDIUM (key-off bị bỏ qua); KSR = MEDIUM; SUS (=1 ở mọi op) = LOW: bỏ qua (YMF825 không dùng); PE/PANPOT trong voice = LOW (tất cả = giữa nên không ảnh hưởng); thuật toán nối op (ALG 0-7) = MEDIUM-HIGH (sơ đồ của fmfm); phase LFO reset mỗi nốt, vibrato qua CC1=1 bỏ qua (ngưỡng 40) = LOW; độ lớn: CC7 x CC11(127) x velocity qua bảng VolumeTable YMF825 = MEDIUM; 8n dùng velocity 9n gần nhất của kênh (mặc định 64) = HIGH (theo smaf825 event.go) — đã sửa smaf_parse.py và render lại V3/V4 + MIDI.
  VƯỚNG MẮC LỚN (xem mục Vướng mắc): 7 voice DRUM (bank 7D) có voiceType=01 = PCM, KHÔNG phải FM như PLAN giả định; WaveID tham chiếu sóng nội chip (không có Mtsp) -> không thể tái tạo mẫu thật. Drum trong V1/V2 là synth xấp xỉ (kick/snare/tom/crash-china) theo WaveID (0x03 kick,0x0b snare,0x0d tom,0x15 cymbal) và độ dài EP, LOW; gain tăng x2.5 để RMS trống ≈ -9 dB so với phần FM. Không có giới hạn đa âm 16 note của chip (render không hạn chế). Reverb V2 là hậu kỳ thêm, không phải của chip.
- T5 xong: tools/chiptune.py -> out/bgm_20_v6_chiptune_8bit.flac (mono 44.1k/16, pulse 12.5/25/50% + tam giác cho bass ch2/ch6 + noise/tam giác cho trống, bước biên độ 4-bit, DC-block, cắt đuôi im; ~141.7 s, peak -1.00 dBFS, đầu im 0.10 s, đuôi ≤0.3 s).
- T3 xong: build libADLMIDI (cmake; adlmidiplay lỗi thiếu ALSA nên không dùng) -> libADLMIDI.a; viết tools/adl_render.c (adl_init, bank 0 embedded, 2 chip, soft-pan) build bằng gcc ra /tmp/adl_render; tools/adl_finish.py trim/fade/chuẩn hoá. Output out/bgm_20_v5_opl3_fm_retro.flac 141.732 s, peak -1.00, đầu 0.10 s, đuôi 0.23 s.
- T6 xong: bảng kiểm tra cuối (tools/check_audio.py; lead/tail = im lặng dưới -60 dBFS; out/bgm_20.mid = 71.468 s, 1 lượt, 480 PPQ):
| bgm_20_v1_ma3_fm_goc.flac | 141.732 s | 44100 Hz | 16-bit | 1ch | peak -1.00 dBFS | lead 0.10s tail 0.19s (<-60dB) | 8358009 B | nan=False |
| bgm_20_v2_ma3_fm_hifi.flac | 141.732 s | 48000 Hz | 24-bit | 2ch | peak -1.00 dBFS | lead 0.10s tail 0.22s (<-60dB) | 30692762 B | nan=False |
| bgm_20_v3_gm_timgm6mb.flac | 141.732 s | 44100 Hz | 16-bit | 2ch | peak -1.00 dBFS | lead 0.10s tail 0.22s (<-60dB) | 9668281 B | nan=False |
| bgm_20_v4_gm_generaluser.flac | 141.732 s | 44100 Hz | 16-bit | 2ch | peak -1.00 dBFS | lead 0.10s tail 0.22s (<-60dB) | 12806454 B | nan=False |
| bgm_20_v5_opl3_fm_retro.flac | 141.732 s | 44100 Hz | 16-bit | 2ch | peak -1.00 dBFS | lead 0.10s tail 0.23s (<-60dB) | 12497706 B | nan=False |
| bgm_20_v6_chiptune_8bit.flac | 141.384 s | 44100 Hz | 16-bit | 1ch | peak -1.00 dBFS | lead 0.10s tail 0.10s (<-60dB) | 8500377 B | nan=False |

## Vướng mắc
- [Quản đốc 13:18] Người dùng ĐÃ CHO PHÉP build libADLMIDI từ GitHub (Wohlstand/libADLMIDI) để làm T3. Làm T3 sau khi xong T4–T5, trước T6.
- T3 CHƯA LÀM (bị chặn): đã `git clone --depth 1 Wohlstand/libADLMIDI` vào /home/claude/adl/libADLMIDI (chưa xem/ chưa build). Lệnh `cmake`/`make` trên mã tải từ GitHub bị bộ phân loại quyền của phiên từ chối ("Code from External"), tôi không tìm cách lách. Cần người dùng/quản đốc cho phép build (hoặc chỉ định cách khác) rồi chạy: cmake -DWITH_MIDIPLAY=OFF + make, sau đó render /tmp/two.mid (smaf2mid.py --two-pass) -> WAV -> tools/finish.py. Đã làm tiếp T4, T5, T6 (không có V5).
- (T4) Drum SysEx bank 7D không phải FM: voiceType=01 (PCM, 19 byte: Fs, PANPOT, LFO, SR/RR/DR/AR/SL/TL, DAM/EAM/DVB/EVB, LP, EP, RM|WaveID...), không có chunk Mtsp -> dùng sóng ROM nội chip chưa biết. Mâu thuẫn với "KHÔNG có Mtsp -> mọi voice là FM" trong PLAN; V1/V2 chỉ xấp xỉ trống. Quản đốc cân nhắc: chấp nhận, hoặc thay trống bằng mẫu GM (V3-style) cho V1/V2.

# GIAI ĐOẠN 2 — SoundFont SF2 mang âm sắc V1 (người dùng chọn V1, 13:23)

## Quyết định thiết kế (GĐ2)
1. Nguồn âm: CHÍNH synth MA-3 FM dùng cho V1 (tools/ hiện có), mono 44.1 kHz/16-bit, không reverb.
2. Preset: bank 0, số program = đúng 12 program SMAF đang dùng (25,47,69,18,40,79,62,113,35,54,10,101), tên preset dạng `MA3 P025` …; bank 128 preset 0 = `MA3 Drums` gồm 7 phím 35,36,40,41,45,49,52 (dùng đúng trống xấp xỉ của V1).
3. Multisample: mỗi voice melody lấy mẫu mỗi 3 nửa cung, phủ dải nốt bài dùng ±1 quãng tám (tối thiểu MIDI 24–96 nếu hợp lý), mỗi mẫu gán keyrange quanh root. Một lớp velocity (render ở velocity 127, SF2 tự giảm âm lượng theo velocity mặc định).
4. Envelope: attack/decay/sustain "nướng" vào mẫu. Voice có sustain > 0 thì đặt loop trên đoạn ổn định: độ dài loop = bội số nguyên chu kỳ f0 (cắt tại zero-crossing, crossfade nếu cần), sampleMode=1. Voice tắt dần (sustain = 0 hoặc tắt hẳn) thì không loop, mẫu dài đến khi < −60 dB (tối đa ~4 s). Release của SF2 (volEnv release) quy đổi từ RR của từng voice.
5. Writer SF2 tự viết (RIFF sfbk: INFO, sdta smpl, pdta phdr/pbag/pmod/pgen/inst/ibag/imod/igen/shdr, đủ bản ghi terminal EOP/EOI/EOS, mỗi mẫu có 46 sample 0 đệm sau), `tools/make_sf2.py`. Không dùng gói ngoài để ghi, có thể dùng gói pip (vd `sf2utils`) chỉ để kiểm tra.
6. Đầu ra: `out/MA3_bgm20.sf2` + `out/bgm_20_v7_sf2_demo.flac` (render MIDI 2 lượt bằng FluidSynth với sf2 mới, reverb/chorus tắt, cùng bố cục thời lượng/master như V1).

## Task GĐ2
- [ ] T7 (A1, **Quản đốc nghiệm thu**) Tạo `tools/make_sf2.py` → `out/MA3_bgm20.sf2`. Hoàn thành khi: FluidSynth nạp không lỗi/cảnh báo, sf2utils (hoặc parser khác) đọc được đủ 13 preset; loop không click (kiểm tra bằng số: chênh biên độ/độ dốc tại điểm nối nhỏ); ghi Nhật ký: số mẫu, kích thước file, voice nào loop/không loop.
- [ ] T8 (A1) Render `out/bgm_20_v7_sf2_demo.flac` và so với V1: ghi vào Nhật ký khoảng cách phổ log-mel trung bình V7–V1 và V3–V1 (để thấy V7 gần V1 hơn hẳn V3), peak −1 dBFS, tiêu chí thời lượng như T2.

## [Quản đốc 13:24] Thay đổi thiết kế GĐ2: hỗ trợ NHIỀU file MMF
Người dùng sẽ gửi thêm nhiều bài để có thêm nhạc cụ. Bổ sung (không làm hỏng T7/T8 cho bgm_20):
7. `tools/make_sf2.py` nhận NHIỀU file .mmf (`make_sf2.py out.sf2 a.mmf b.mmf ...`). Trích voice từ Mtsu từng file, khử trùng lặp theo nội dung byte voice (voice giống hệt = 1 preset).
8. Xung đột: cùng số program nhưng voice khác → voice gặp đầu tiên ở bank 0, các biến thể sau ở bank 1, 2, … cùng số program. Tên preset: `P025 <tênbài>` (cắt ≤ 20 ký tự). Trống: gộp các phím vào bank 128 preset 0; phím trùng nhưng voice khác → bộ trống bank 128 preset 1, 2, …
9. Xuất kèm `out/sf2_map.csv`: bài, kênh, program SMAF, bank/preset tương ứng trong sf2, hash voice. Để chơi đúng từng bài.
10. File không đọc được/định dạng khác (vd MA-2, có Huffman, có PCM Mtsp) → bỏ qua voice không hỗ trợ, ghi cảnh báo, không làm hỏng cả bộ.
- [ ] T9 (A1) Làm 7–10 sau T7/T8; kiểm tra bằng bgm_20.mmf truyền 2 lần (phải khử trùng thành đúng 13 preset như T7).

## [Quản đốc 13:27] GĐ3 — Bộ sf2 đầy đủ từ 21 bài
Người dùng đã gửi thêm 20 bài: `in/songs/BGM_00.MMF` … `BGM_19.MMF`, cộng `BGM_20.MMF` (= bgm_20.mmf) → tổng 21 file trong `in/songs/`. Có thể khác timebase/kênh so với bgm_20; Mtsu đều có voice bank 7C (melody) và 7D (drum).
- [ ] T10 (A1, **Quản đốc nghiệm thu**) Chạy make_sf2.py trên cả 21 file → `out/MA3_full.sf2` + `out/sf2_map.csv`. Hoàn thành khi: tất cả 21 file parse Mtsq không lỗi (file nào lỗi thì ghi rõ lý do, không làm hỏng cả bộ); FluidSynth nạp sf2 không lỗi; ghi Nhật ký: số voice melody/drum duy nhất, số preset theo bank, số xung đột, kích thước file; render thử 3 bài bất kỳ (gồm 1 bài có xung đột bank) bằng MIDI + sf2 đúng bank/preset theo map → `out/demo_BGM_xx_sf2.flac`, so phổ với render synth trực tiếp (như V1) của chính bài đó, khoảng cách phải cùng cỡ với V7–V1.
- Kích thước: nếu sf2 > ~150 MB thì giảm mật độ mẫu (mỗi 4 nửa cung) hoặc rút ngắn mẫu tắt dần, ghi lại lựa chọn.

## [Quản đốc 13:30] SỬA GĐ3 — hai bộ nhạc, hai game
Người dùng gửi 2 lô, đánh nhãn "Hl4" và "Hl5" (= 2 game khác nhau):
- `in/hl4/BGM_00.MMF … BGM_19.MMF` (20 file, chữ hoa; chip ghi "Y933…", định dạng voice/Mtsu KHÁC bgm_20 — tự kiểm chứng byte).
- `in/hl5/bgm_00.mmf … bgm_20.mmf` (21 file, cùng game với bgm_20, "YG32…", © Dch). Một số file (vd bgm_01, 02, 06, 13, 14) có SysEx rất dài trong Mtsu = dữ liệu sóng PCM/ADPCM nhúng cho voice WT/PCM. Giải được thì dùng; không chắc thì bỏ qua voice đó + cảnh báo.
- (`in/songs/` cũ đã xoá; T10 dùng 2 thư mục trên.)
T10 sửa lại: xuất `out/MA3_HL5.sf2` (21 bài hl5), `out/MA3_HL4.sf2` (20 bài hl4), và `out/MA3_HL4_HL5.sf2` gộp (hl5 giữ bank như file riêng; hl4 dời lên bank +16 cho melody và drum preset +16 trong bank 128, ghi rõ trong map). Mỗi bộ có `sf2_map_<tên>.csv`. Demo: 2 bài mỗi game. Tiêu chí còn lại như T10.

# GIAI ĐOẠN 4 — Phiên mới (2026-11-13): tiếp quản T7–T10
Gói đặt tại `/home/user/M-I/mmf/mmf_project` (mọi đường dẫn dưới đây tương đối với thư mục này). `/home/claude/...` trong nhật ký cũ = phiên trước, bỏ qua. `in/songs/` không tồn tại; dùng `in/hl5/`, `in/hl4/`.
Môi trường đã cài: numpy scipy numba mido soundfile pyfluidsynth, libfluidsynth3 + CLI `fluidsynth`, ffmpeg. TimGM6mb.sf2: kiểm tra `/usr/share/sounds/sf2/` (nếu thiếu, V3 chỉ dùng làm mốc so phổ → có thể tải lại hoặc ghi rõ).

## [Quản đốc] Kết quả chạy thử WIP make_sf2.py (bgm_20)
Chạy được (43 s): 308 mẫu (202 loop), 20 instrument, 13 preset, 76.3 MB. Vấn đề phải sửa:
1. Quyết định loop theo ngưỡng RMS (ratio 0.9–1.1 trong 2.0–3.1 s) → cùng 1 voice lúc loop lúc không (vd 9/25), voice tắt dần chậm bị loop "đóng băng" mức → sai. Voice tắt dần mà nốt giữ > 4 s thì bị cắt.
2. Loop luôn bắt đầu ở 2.0 s → tốn dung lượng (76 MB/1 bài; gộp 41 bài sẽ vượt 150 MB).
3. Độ dài loop chọn theo NCC thuần, chưa ràng buộc bội số chu kỳ f0 (yêu cầu PLAN).
## Thiết kế sửa T7 (bắt buộc)
- Envelope synth (ma3_synth._op): stage 3 nhân `P[i,3]` mỗi mẫu = giảm tuyến tính theo dB; SR=0 → giữ nguyên. SF2 volEnv decay cũng tuyến tính theo dB ⇒ mô phỏng CHÍNH XÁC được.
- Mỗi mẫu (voice, root): tìm điểm a sớm nhất (≥ ~0.1 s) mà sau đó đường RMS (dB, khối 10–20 ms) là đường thẳng (sai số ≤ ~0.5 dB) trong ≥ 0.6 s. Đo độ dốc s (dB/s), hoặc tính trực tiếp từ P của carrier trội.
  * |s| nhỏ (≈ 0): loop thường.
  * s < 0 và tuyến tính: "làm phẳng" vùng loop bằng bù gain 10^(−s·(t−a)/20), loop vùng phẳng; SF2: holdVolEnv = a (giây, timecents), decayVolEnv = 96/|s| s (FluidSynth: decay = thời gian tụt 96 dB), sustainVolEnv = 960 cB (hoặc 1440). Như vậy nốt giữ dài tắt dần đúng tốc độ.
  * Không tuyến tính (nhiều carrier khác SR) hoặc tắt hết trước a: không loop, cắt ở −60 dB (trần 4 s); ghi lý do.
- Độ dài loop L: bội số nguyên của chu kỳ f0 = sr/freq(root) (chọn k sao cho phần lẻ k·T nhỏ, |k·T − round| ≤ 0.1 mẫu nếu được); nếu voice có EAM/EVB (LFO) thì L ≈ bội số chu kỳ LFO, chỉnh về bội số f0 gần nhất. Trong các ứng viên chọn NCC lớn nhất. Crossfade ngắn (≤ 1 chu kỳ LFO/≤ 30 ms) nếu cần; kiểm tra click bằng số: |x[b]−x[a]| và |Δx| tại nối so với trung vị |Δx| quanh đó.
- Release SF2 từ RR (giữ release_tc hiện có, MEDIUM).
- Velocity: V1 dùng VOLTAB[vel>>2]; ghi lại khác biệt với đường cong velocity mặc định của SF2 (chỉ ghi, không cần modulator riêng trừ khi T8 cho thấy lệch lớn).
- Mục tiêu dung lượng bgm_20 ≤ ~30 MB.

# tools/ — công cụ dựng, kiểm và chạy thử

Mọi công cụ chạy từ thư mục `SVTH_refactor/`. Thư viện ở `tools/lib/` (ASM 9.7, ECJ 3.26, ProGuard 7.4, MicroEmulator 2.0.4,
API CLDC 1.0 / MIDP 2.0 dạng stub biên dịch). `cfr` và `vineflower` (giải mã nguồn để *đọc*) bị `.gitignore`; tải lại khi cần.

## Dựng

| Tệp | Việc |
|---|---|
| `../build_jar.sh [release\|debug]` | Toàn bộ pipeline: ECJ stub → ECJ `src/`(+`debug/`) (`-1.3 -target 1.1`) → `Patch` → `Peephole` → `pack.py` → `Verify` → ProGuard `-microedition` → `Verify --require-stackmap` → JAD. Biến môi trường `ORIG` (JAR gốc), `VERSION` |
| `src/Patch.java` | ASM: thêm accessor `sfx$*` và chèn hook vào `e`, `bx`, `bn`, `da` (SFX) và `av`, `as`, `ab` (menu: `SfxUi`). Có kiểm khẳng định (đúng 4 site `e.g:Z`, 44 site `e.f:I` trong `bn`, 2 site `at.a:Z` trong `av`…); lệch → dừng. `--debug` gọi `DebugPatch` |
| `src/DebugPatch.java` | Chỉ bản debug: accessor/setter `dbg$*`, hook phím, vẽ overlay, tick, kẹp HP, ép trúng, chặn script cốt truyện |
| `src/Peephole.java` | Bỏ `checkcast` thừa liền kề (ECJ cũ sinh ra) trước khi verify |
| `src/Verify.java` | Verifier kiểu CLDC: phân tích luồng dữ liệu bằng ASM, quy tắc cấm float/double, kiểm StackMap (`--require-stackmap`) |
| `src/BinDiff.java` | So JAR gốc/mới: entry thêm/xoá/đổi, và field/method/số lệnh bytecode của class đổi → `docs/BINDIFF_session1.md` |
| `pack.py` | Đóng gói JAR chưa preverify: giữ nguyên tài nguyên gốc byte-for-byte và mọi thuộc tính manifest, đổi `MIDlet-Version`, thêm `SFX-Debug` |
| `../stubs/*.java` | Chỉ để **biên dịch** mã mới (khai báo `native` các accessor ASM sinh ra); không đóng gói |

## Khôi phục tên (Phiên 5)

| Tệp | Việc |
|---|---|
| `src/Remap.java` | `apply` đổi tên class/field/hàm trên JAR theo `mapping/*.map` (ASM `ClassRemapper`): khoá (owner, tên, desc), phân giải qua phân cấp (JVMS 5.4.3), nhóm override (union-find), **đóng băng** nhóm ghi đè API CLDC/MIDP, kiểm phân giải lại mọi lệnh field/hàm, tên không trùng trong class, tên class không trùng (kể cả khác hoa/thường); `--placeholders` đặt `<cũ>_<kiểu>` cho thành viên chưa có tên; `--lenient` cho khứ hồi về tên làm rối; `compare a.jar b.jar` so từng class (qua ASM) + md5 tài nguyên |
| `remap_check.sh [-o out.jar] map...` | Kiểm nhanh một/nhiều `.map` trên JAR gốc (in lỗi theo dòng map); `KEEP=1` giữ `resolved/inverse/reverse` |
| `roundtrip_check.sh` | Sinh `mapping/{resolved.map,reverse.tsv,inverse.map}` + chứng minh khứ hồi (gốc → đặt tên → bảng đảo → so với gốc: phải 928/0) |
| `gen_rename_doc.py` | Sinh `docs/RENAME_MAP.md` từ `mapping/part_*.map` (`python3 -I tools/gen_rename_doc.py`) |
| `test/me/translate.sh` | Dịch kịch bản MeRun (`tools/test/me/orig/*.txt`) sang tên đã đặt tên (`renamed/*.txt`); chỉ 2 kiểu tham chiếu tên làm rối |
| `../build_jar.sh` `RENAME=1` | Thêm bước `Remap` sau `Verify` đầu và trước ProGuard → `dist/<tên>_renamed.jar` |

## Dữ liệu animation

| Tệp | Việc |
|---|---|
| `avparse.py` | Đọc `.av` (định dạng ở `co.a(byte[])`); kiểm tái tạo từng byte |
| `avrender.py file.av out.png anim [res]` | Vẽ contact sheet các frame |
| `gen_sfx_map.py <jar đã giải nén> <out>` | Sinh `res/sfx/map.txt` (lớp/nguyên tố/nhịp kỹ năng + frame va chạm hiệu ứng — heuristic, sửa tay được) |

## Kiểm thử bộ phát âm thanh (JVM thường, MMAPI giả)

`tools/test/run_audio_test.sh` biên dịch `SFXManager`/`BeepBackend`/`BgmDuck` cùng `tools/test/fake/` (Manager/Player giả ghi thời điểm start/stop/rewind, `bv`/`Main`/`o`/`at` giả ghi mọi lần đặt âm lượng nhạc) và chạy `SfxAudioTest` (24 kiểm: độ trễ, chồng tiếng, không cắt, bể Player, hạ/trả nhạc nền, hết Player, trần tổng). Kết quả: `docs/AUDIO_TEST_session4.txt`.

## Chạy thử (không cần máy thật)

```
tools/me/run_me.sh <jar> <script> <outdir> [timeout_s]     # JOPTS="-Dsfx.log=1 -Dsfxtest.only=2,13" tuỳ chọn
```

Chạy MicroEmulator 2.0.4 dưới Xvfb + openbox với máy ảo 240x320 (`tools/me/dev240*`). `MeRun.java` là harness phản chiếu
(emulator chạy trên JDK đầy đủ, nên reflection được dù game là CLDC): lệnh kịch bản

`wait ms · show · key K · keys K… ms · shot f.png · say text · until Lớp trường giá_trị ms · probe Lớp trường · callm/calls … · setf … -- trường i:n · spam K ms timeout Lớp trường … -- trường giá_trị · quit`

`spam` bấm một phím lặp lại đến khi field đạt giá trị (dùng để lướt prologue: `spam -5 700 240000 da a:da a:cd -- b:byte 4` dừng khi menu hệ thống mở); `setf` đặt field instance (ví dụ `setf da a:da a:cd -- a:int i:2`).

* stdout của game vào `<outdir>/out.txt`: dòng `SFXLOG` (nhật ký từng sự kiện; bật bằng `-Dsfx.log=1` hoặc bản debug),
  `SFXTEST` (kết quả auto-test), `MERUN` (harness).
* `JOPTS=-Dsfxtest.only=2,13,28` chỉ chạy các kỹ năng liệt kê (chẩn đoán nhanh).
* `run_micro.sh` là biến thể bơm phím bằng Robot (không dùng reflection).

Lưu ý: emulator không có thiết bị âm thanh trong sandbox → `Manager.playTone` ném `IllegalArgumentException` (in ra stderr);
backend bắt lỗi và ghi `REQUESTED` (đã gọi API) chứ không `PLAYED`. Đây đúng là phân biệt "gọi API" và "đã phát".

## Thư viện (`tools/lib/`)

Trong repo có sẵn; **gói zip bàn giao không kèm** (không phân phối lại JDK/ASM/ProGuard/emulator). Lấy từ Maven Central:
`org.ow2.asm:asm|asm-tree|asm-analysis|asm-commons|asm-util:9.7`, `org.eclipse.jdt:ecj:3.26.0`, `com.guardsquare:proguard-base:7.4.2`
(+ `proguard-core:9.1.1`, `gson:2.9.1`, `kotlin-stdlib:1.8.21`, `log4j-api|log4j-core:2.17.2`),
`org.microemu:microemulator|microemu-cldc|microemu-midp|midpapi20|cldcapi10:2.0.4`, và (tuỳ chọn, đọc mã) `org.vineflower:vineflower:1.10.1`, `cfr:0.152`.
Tên tệp phải đúng như `build_jar.sh` tham chiếu (ví dụ `asm-9.7.jar`, `ecj-3.26.0.jar`).

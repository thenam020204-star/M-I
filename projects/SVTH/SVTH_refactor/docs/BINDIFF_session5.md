# BINDIFF — phiên 5 (đặt tên khôi phục mã nguồn)

Công cụ: `tools/src/BinDiff.java` (so entry), `tools/src/Remap.java compare` (so từng class sau khi đọc-ghi qua ASM + md5 tài nguyên).

## Kết luận

* **Bản chuẩn không đổi hành vi so với 1.4.0:** `dist/SVTH_SFX.jar` / `_debug.jar` **v1.5.0** khác 1.4.0 đúng **1 entry** (`META-INF/MANIFEST.MF`, số phiên bản); mọi `.class` và tài nguyên giống hệt từng byte (release 967/968, debug 972/973 entry giống hệt).
  Do đó mọi so sánh với JAR gốc của phiên 4 (`docs/BINDIFF_session4.md`: 919/928 entry giống hệt; class game đổi `bn bx da e av as ab` (+ `cd ci` ở debug), `MANIFEST.MF`, `fontcus`) vẫn đúng nguyên vẹn.
* **Bản đã đặt tên** (`dist/SVTH_SFX_renamed.jar`, `dist/SVTH_SFX_debug_renamed.jar`) là bản chuẩn + đổi tên bằng `Remap` (sau Patch/Peephole/Verify, trước ProGuard preverify). Chứng minh tương đương:
  áp **bảng đảo** `mapping/inverse.map` lên bản đã đặt tên → so với bản chuẩn: **release giống=968 khác=0; debug giống=973 khác=0** (so cả StackMap, tài nguyên md5).
* **Khứ hồi trên JAR gốc:** JAR gốc → đặt tên (111 class, 1 664 field, 946 hàm có tên/placeholder) → áp bảng đảo → so với JAR gốc: **giống=928 khác=0** (`tools/roundtrip_check.sh`).
* Kiểm tham chiếu sau đổi tên: toàn bộ lệnh field/hàm (39 542 ở JAR gốc; 40 588 release; 41 618 debug) phân giải lại trong không gian tên mới **đúng thành viên cũ, 0 lỗi**; không có hai tên trùng trong một class; 27 nhóm hàm ghi đè API CLDC/MIDP bị đóng băng (28 ở bản release, 30 ở bản debug vì class SFX/debug thêm vào cũng ghi đè API, ví dụ `run()`; chúng không bị đổi tên).
* Kích thước: bản đã đặt tên lớn hơn bản chuẩn ~44 KB (3%) vì tên dài (release 1 584 896 B so với 1 540 986 B).

## Số liệu đặt tên

Xem `docs/RENAME_MAP.md` (thống kê và bảng bằng chứng từng dòng). Không có dòng `.map` nào đổi nghĩa hay tham số của code; thành viên chưa có tên giữ tên giữ chỗ `<cũ>_<kiểu>`.

## Entry khác nhau 1.4.0 → 1.5.0

```
release: entries: old=968 new=968 identical=967 changed=1 added=0 removed=0 — META-INF/MANIFEST.MF
debug:   entries: old=973 new=973 identical=972 changed=1 added=0 removed=0 — META-INF/MANIFEST.MF
```

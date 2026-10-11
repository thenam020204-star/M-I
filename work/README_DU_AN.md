# Heroes Lore Zero – dự án Việt hoá
Font f13c (13px chân chọn lọc), phương án 1: sửa resource + patch bytecode tĩnh (ASM), preverify ProGuard.
- `rebuild.sh`: jar gốc → `scripts/build.sh` (Renamer theo `mapping/rename.map` → Patcher + `mapping/class_strings.tsv` → class mới `patch/src` (ecj 1.3) → `scripts/apply_text.sh` chèn `text_can_dich.tsv` → preverify) → jar_check → emu.
- `out/` jar+jad cuối, ảnh chụp · `goc/` jar gốc, text gốc, ảnh gốc · `src/` mã CFR · `scripts/`, `skscripts/` script · `mapping/` bảng đổi tên/chuỗi class · `patch/` class mới (TextRenderer, DialogBox, ScriptLoader, BitmapFont) · `dp_work/` dữ liệu dịch · `logs/` · `tools/`.
Yêu cầu: JDK đầy đủ, `source tools/env.sh`.

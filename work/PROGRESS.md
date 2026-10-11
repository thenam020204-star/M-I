# PROGRESS — Heroes Lore Zero việt hoá

## Cập nhật cuối
- Xong: font, dịch resource + class strings, khung thoại 3 dòng, jar_check PASS, đóng gói dự án. Tồn: xem BAO_CAO.md.

Quyết định: phong cách hiện đại, giữ tên riêng tiếng Anh; font f13c (13px chân chọn lọc), BitmapFont, text UTF-8.
## Đã xong
- Gỡ rối: scripts/renamer/Renamer.java + mapping/rename.map (ac→TextRenderer, cm→GameGraphics, ch→TextTable, df→MapScript, db→ScriptRunner…)
- Patcher.java: MapScript dialog → DialogBox (240x64, 3 dòng, 220px), ScriptRunner.loadScript → ScriptLoader (bảng chuỗi u2)
- patch/src: TextRenderer (UTF-8 + f13c), DialogBox, ScriptLoader, BitmapFont (CACHE 5)
- scripts/build.sh: clean.jar → build/game.jar (preverify). Test: scripts/emu.py (đã sửa cỡ màn + crop)
- Trích text: scripts/extract.py → text_goc.tsv (5046 dòng): tdf, map (.m), itm, en/edf
## Đang làm
- Dịch bằng dp (dp_work/)
## Còn lại
- chuỗi trong class (rpg/d, j credits…), ảnh có chữ, as.a đọc độ dài unsigned, kiểm tràn UI

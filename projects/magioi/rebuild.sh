#!/usr/bin/env bash
# Tái tạo jar Việt hoá từ jar gốc: inject text -> vá class -> thêm hook/font/logo -> preverify -> jar_check -> jad.
#   bash rebuild.sh [tsv_dịch]   (mặc định out/text_can_dich.tsv, không có thì dùng work/text_goc.tsv)
# Cần: tools/env.sh (FJ, PROGUARD, BITMAPFONT_CLASS, FONT_DIR) và ECJ=đường dẫn ecj.jar (tools/ecj.jar).
set -e
P=$(cd "$(dirname "$0")" && pwd); cd "$P"
[ -f tools/env.sh ] && source tools/env.sh
ECJ=${ECJ:-$P/tools/ecj.jar}; export ECJ
TSV=${1:-out/text_can_dich.tsv}; [ -f "$TSV" ] || TSV=work/text_goc.tsv
B=work/build; rm -rf "$B"; mkdir -p "$B/raw" "$B/res"
( cd "$B/raw" && unzip -q "$P/goc/game.jar" )
python3 -I scripts/respack.py unpack "$B/raw" "$B/res" >/dev/null
python3 -I scripts/vh_images.py "$B/res" assets/res          # vẽ lại ảnh có chữ (pixeltext 13c)
python3 -I scripts/vh_res.py "$B/res"                      # chép ảnh đã vá vào gói
python3 -I scripts/inject.py "$TSV" "$B/raw" "$B/res"
python3 -I scripts/patch_classes.py "$B/raw"
bash scripts/build_hook.sh "$B/raw" "$B/hook" >/dev/null
cp "$B"/hook/*.class "$B/raw/"; cp "$BITMAPFONT_CLASS" "$B/raw/"
mkdir -p "$B/raw/font"; cp "$FONT_DIR/f13c.bin" "$B/raw/font/"
cp assets/vh_logo.png "$B/raw/vh_logo.png"
python3 -I scripts/vh_head.py >/dev/null                    # chân dung mới (assets/src -> assets/jar)
[ -d assets/jar ] && cp -r assets/jar/. "$B/raw/"            # ảnh trong gốc jar đã vá (head_0...)
( cd "$B/raw" && rm -f ../raw.jar && zip -q -X ../raw.jar META-INF/MANIFEST.MF && zip -q -X -r ../raw.jar . -x META-INF/MANIFEST.MF )
mkdir -p out
bash scripts/preverify.sh "$B/raw.jar" out/MaGioiChienKy_VH.jar
python3 -I scripts/jar_check.py goc/game.jar out/MaGioiChienKy_VH.jar | tee logs/jar_check.txt | tail -5
python3 -I scripts/make_jad.py out/MaGioiChienKy_VH.jar out/MaGioiChienKy_VH.jad
echo "OK -> out/MaGioiChienKy_VH.jar"

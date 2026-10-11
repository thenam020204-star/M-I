#!/usr/bin/env bash
# Tái tạo jar Việt hoá từ jar gốc: inject text -> vá class -> hook/font/logo/ảnh -> preverify -> jar_check -> jad.
#   bash rebuild.sh [tsv_dịch]   (mặc định out/text_can_dich.tsv, không có thì work/text_goc.tsv = chưa dịch)
# Cần: tools/env.sh hoặc work/tools/env.sh (FJ, PROGUARD, BITMAPFONT_CLASS, FONT_DIR) và tools/ecj.jar.
set -e
P=$(cd "$(dirname "$0")" && pwd); cd "$P"
if [ -f tools/env.sh ]; then source tools/env.sh; else source work/tools/env.sh; fi
ECJ=${ECJ:-$P/tools/ecj.jar}; export ECJ
TSV=${1:-out/text_can_dich.tsv}; [ -f "$TSV" ] || TSV=work/text_goc.tsv
NAME=BaVuongNo_VH
B=work/build; rm -rf "$B"; mkdir -p "$B/raw" logs
( cd "$B/raw" && unzip -q "$P/goc/game.jar" )
python3 -I scripts/inject.py "$TSV" "$B/raw"
python3 -I scripts/patch_classes.py "$B/raw"
bash scripts/build_hook.sh "$B/raw" "$B/hook" >/dev/null
cp "$B"/hook/*.class "$B/raw/"; cp "$BITMAPFONT_CLASS" "$B/raw/"
mkdir -p "$B/raw/font"; cp "$FONT_DIR/f15c.bin" "$B/raw/font/"
cp assets/vh_logo.png "$B/raw/vh_logo.png"
[ -d assets/jar ] && cp -r assets/jar/. "$B/raw/"            # ảnh đã vá chữ (đường dẫn như trong jar)
( cd "$B/raw" && rm -f ../raw.jar && zip -q -X ../raw.jar META-INF/MANIFEST.MF && zip -q -X -r ../raw.jar . -x META-INF/MANIFEST.MF )
mkdir -p out
bash scripts/preverify.sh "$B/raw.jar" out/$NAME.jar
python3 -I scripts/jar_check.py goc/game.jar out/$NAME.jar | tee logs/jar_check.txt | tail -4
python3 -I scripts/make_jad.py out/$NAME.jar out/$NAME.jad
echo "OK -> out/$NAME.jar"

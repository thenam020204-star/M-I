#!/usr/bin/env bash
# Chuẩn bị môi trường làm việc từ bộ tool người dùng gửi (zip lồng zip):
#   - Font_full_tiếng_Việt  : BitmapFont.class/.java + dist/font/f10..f15.bin (+ tools/ sinh font)
#   - Tool_vẽ_chữ_lên_ảnh   : pixeltext-kit (vẽ/vá chữ Việt vào ảnh)
#   - freej2me_plus-nightly : freej2me_plus.jar (emulator headless để test)
# Nhận 2 kiểu bộ tool: CÂY SẠCH NAM.P (fonts/ fontgen/ java/ pixeltext_kit/ freej2me/, dùng thẳng) hoặc bộ zip-lồng-zip cũ.
# Cây sạch hiện hành: f18 (gốc) + f15/f13 có chân, f15c/f13c chân chọn lọc, f15s/f13s không chân.
# Việc script làm: giải nén, đặt font bộ mới vào pixeltext (đồng bộ với font chạy trong game),
# vá font.py/cli.py của pixeltext cho khớp tên file fNN.bin, tải CFR + ProGuard, cài Pillow + python-xlib.
#   bash setup_tools.sh <Tool_Viet_Hoa.zip hoặc thư mục tool> <thư_mục_làm_việc>
# Kết quả: <work>/tools/{font,pixeltext-kit,freej2me_plus.jar,cfr.jar,proguard.jar} + tools/env.sh
set -e
SRC="$1"; WORK="${2:-$PWD/work}"; T="$WORK/tools"; mkdir -p "$T"
[ -n "$SRC" ] || { echo "Cách dùng: setup_tools.sh <zip|thư mục> [work]"; exit 1; }
if [ -f "$SRC" ]; then unzip -oq "$SRC" -d "$T/_src"; SRC="$T/_src"; fi
# zip lồng zip: giải hết các zip con (lặp tới khi hết, phòng zip trong zip trong zip)
for _ in 1 2 3; do
  find "$SRC" -name '*.zip' | while read -r z; do
    d="${z%.zip}"; [ -d "$d" ] || { mkdir -p "$d"; unzip -oq "$z" -d "$d"; }
  done
done

# ---- cây sạch NAM.P (fonts/ fontgen/ java/ pixeltext_kit/ freej2me/ check_all.sh) -> dùng thẳng, không vá gì
CLEANROOT=$(find "$SRC" -name check_all.sh | head -1); CLEANROOT=${CLEANROOT:+$(dirname "$CLEANROOT")}
if [ -n "$CLEANROOT" ] && [ -s "$CLEANROOT/fonts/f15.bin" ] && [ -s "$CLEANROOT/java/BitmapFont.class" ]; then
  for d in fonts fontgen java pixeltext_kit; do rm -rf "$T/$d"; [ -d "$CLEANROOT/$d" ] && cp -r "$CLEANROOT/$d" "$T/$d"; done
  rm -rf "$T/pixeltext-kit"; mv "$T/pixeltext_kit" "$T/pixeltext-kit"   # giữ tên cũ; font.py tự tìm ../fonts
  rm -rf "$T/java/build_tmp"
  cp "$CLEANROOT/check_all.sh" "$T/"
  [ -d "$CLEANROOT/freej2me" ] && SRC="$CLEANROOT/freej2me"
  CLEAN=1
  echo "tool: cây sạch NAM.P, font (--size) =" $(ls "$T/fonts" | sed -n 's/^f\([0-9]*[sc]\{0,1\}\)\.bin$/\1/p' | sort -n | tr '\n' ' ')
  [ -s "$T/fonts/f13c.bin" ] || echo "WARN: bộ tool chưa có f15c/f13c (chân chọn lọc) - bộ cũ hơn bản skill mô tả"
else
# ---- font kit: gốc kit = thư mục cha của dist/ chứa BitmapFont.class
BF=$(find "$SRC" -name BitmapFont.class | head -1)
if [ -z "$BF" ]; then
  echo "LỖI: không thấy BitmapFont.class trong bộ tool. Bộ font cũ (fontcus) không còn được skill hỗ trợ;"
  echo "     xin người dùng bộ tool NAM.P mới (fonts/f18.bin + f15/f13 có chân, chọn lọc c, không chân s; java/BitmapFont.class)."; exit 1
fi
FONTKIT=$(dirname "$(dirname "$BF")")
rm -rf "$T/font" && cp -r "$FONTKIT" "$T/font"
FD="$T/font/dist/font"
for s in 10 11 12 13 14 15; do [ -s "$FD/f$s.bin" ] || echo "WARN: thiếu $FD/f$s.bin"; done

# ---- pixeltext-kit
CLI=$(find "$SRC" -path '*pixeltext/cli.py' | head -1)
if [ -n "$CLI" ]; then
  KIT=$(dirname "$(dirname "$CLI")")
  rm -rf "$T/pixeltext-kit" && cp -r "$KIT" "$T/pixeltext-kit"
  F="$T/pixeltext-kit/pixeltext/fonts"; mkdir -p "$F"
  # font cho ảnh phải TRÙNG font chạy trong game -> lấy f10..f15 từ dist/font của font kit
  rm -f "$F"/font_*.bin
  for s in 10 11 12 13 14 15; do [ -s "$FD/f$s.bin" ] && cp "$FD/f$s.bin" "$F/f$s.bin"; done
  # f18 không có trong dist/: ưu tiên file kit đã giao, không có thì sinh bằng tools/build_fonts.py
  if [ ! -s "$F/f18.bin" ] && [ -f "$T/font/tools/build_fonts.py" ]; then
    ( cd "$T/font" && python3 tools/build_fonts.py >/dev/null 2>&1 || true )
    [ -s "$T/font/f18.bin" ] && cp "$T/font/f18.bin" "$F/f18.bin"
  fi
  # f18 cũng phải nằm cạnh các fNN.bin khác để đóng vào jar (/font/f18.bin)
  [ -s "$F/f18.bin" ] && cp "$F/f18.bin" "$FD/f18.bin"
  # vá font.py (map tên file + danh sách cỡ) và cli.py (mặc định --sizes) theo các file fNN.bin có thật
  python3 - "$T/pixeltext-kit/pixeltext" <<'PY'
import os, re, sys
pkg = sys.argv[1]; fonts = os.path.join(pkg, 'fonts')
sizes = sorted(int(m.group(1)) for n in os.listdir(fonts) if (m := re.fullmatch(r'f(\d+)\.bin', n)))
p = os.path.join(pkg, 'font.py'); s = open(p, encoding='utf-8').read()
mapping = "FONTS = {\n" + "".join("    %d: 'f%d.bin',\n" % (z, z) for z in sizes) + "}\n"
tup = "(" + ", ".join(map(str, sizes)) + ")"
s = re.sub(r"FONTS = \{.*?\n\}\n", mapping, s, count=1, flags=re.S)
s = re.sub(r"^SIZES = .*$", "SIZES = %s          # cỡ fit() được chọn (đã vá theo fNN.bin)" % tup, s, count=1, flags=re.M)
s = re.sub(r"^ALL = .*$", "ALL = %s" % tup, s, count=1, flags=re.M)
open(p, 'w', encoding='utf-8').write(s)
p = os.path.join(pkg, 'cli.py'); s = open(p, encoding='utf-8').read()
s = re.sub(r"(--sizes', default=')[0-9,]+(')", r"\g<1>%s\2" % ",".join(map(str, sizes)), s)
open(p, 'w', encoding='utf-8').write(s)
print("pixeltext: cỡ có sẵn =", sizes)
PY
fi
fi

# ---- FreeJ2ME (đúng tên freej2me_plus.jar, không lấy bản -lr)
FJ=$(find "$SRC" -name 'freej2me_plus.jar' | head -1)
[ -n "$FJ" ] && cp "$FJ" "$T/freej2me_plus.jar"

pip install -q pillow python-xlib 2>/dev/null || true
# tải có thử lại; mỗi file thử lần lượt các URL (Maven Central rồi GitHub release)
get() { out="$1"; shift; [ -s "$out" ] && return 0
  for u in "$@"; do for i in 1 2 3; do curl -sSfL -o "$out" "$u" && [ -s "$out" ] && return 0; sleep $((i*2)); done; done
  rm -f "$out"; echo "WARN: không tải được $out"; return 0; }
get "$T/cfr.jar" https://repo1.maven.org/maven2/org/benf/cfr/0.152/cfr-0.152.jar \
                 https://github.com/leibnitz27/cfr/releases/download/0.152/cfr-0.152.jar
if [ ! -s "$T/proguard.jar" ]; then
  get "$T/pg.tgz" https://github.com/Guardsquare/proguard/releases/download/v7.4.2/proguard-7.4.2.tar.gz
  [ -s "$T/pg.tgz" ] && tar xzf "$T/pg.tgz" -C "$T" && cp "$T"/proguard-7.4.2/lib/proguard.jar "$T/proguard.jar"
fi
if [ -n "$CLEAN" ]; then
cat > "$T/env.sh" <<E
export J2ME_TOOLS="$T"
export FJ="$T/freej2me_plus.jar" CFR="$T/cfr.jar" PROGUARD="$T/proguard.jar"
export BITMAPFONT_CLASS="$T/java/BitmapFont.class" BITMAPFONT_JAVA="$T/java/BitmapFont.java" FONT_DIR="$T/fonts"
export PYTHONPATH="$T/pixeltext-kit\${PYTHONPATH:+:\$PYTHONPATH}"
E
else
cat > "$T/env.sh" <<E
export J2ME_TOOLS="$T"
export FJ="$T/freej2me_plus.jar" CFR="$T/cfr.jar" PROGUARD="$T/proguard.jar"
export BITMAPFONT_CLASS="$T/font/dist/BitmapFont.class" BITMAPFONT_JAVA="$T/font/BitmapFont.java" FONT_DIR="$T/font/dist/font"
export PYTHONPATH="$T/pixeltext-kit\${PYTHONPATH:+:\$PYTHONPATH}"
E
fi
echo "OK -> source $T/env.sh"; ls "$T"

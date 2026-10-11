# nguồn: source tools/env.sh (đường dẫn tương đối theo thư mục dự án)
T="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
export J2ME_TOOLS="$T" FJ="$T/freej2me_plus.jar" CFR="$T/cfr.jar" PROGUARD="$T/proguard.jar" ECJ="$T/ecj.jar"
export BITMAPFONT_CLASS="$T/java/BitmapFont.class" BITMAPFONT_JAVA="$T/java/BitmapFont.java" FONT_DIR="$T/fonts"
export PYTHONPATH="$T/pixeltext_kit${PYTHONPATH:+:$PYTHONPATH}"

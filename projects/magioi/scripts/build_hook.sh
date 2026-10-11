#!/usr/bin/env bash
# Biên dịch src/hook/*.java kiểu CLDC (ecj -source 1.3 -target 1.3) -> build/hook/*.class (chưa preverify;
# preverify.sh chạy trên cả jar sau). Cần: ECJ (đường dẫn ecj.jar), FJ (freej2me_plus.jar), BITMAPFONT_CLASS.
#   build_hook.sh <thư_mục_class_game> <out_dir>
set -e
HERE=$(cd "$(dirname "$0")/.." && pwd)
GAME="$1"; OUT="$2"; mkdir -p "$OUT"
JH=$(dirname "$(dirname "$(readlink -f "$(which java)")")")
JB="$HERE/work/jbase"
[ -d "$JB/classes" ] || jmod extract --dir "$JB" "$JH/jmods/java.base.jmod" >/dev/null
CP="$FJ:$GAME:$(dirname "$BITMAPFONT_CLASS")"
env -u JAVA_TOOL_OPTIONS java -cp "${ECJ:?ecj.jar}" org.eclipse.jdt.internal.compiler.batch.Main \
  -source 1.3 -target 1.3 -encoding UTF-8 -nowarn -bootclasspath "$JB/classes" -cp "$CP" \
  -d "$OUT" "$HERE"/src/hook/*.java
ls -la "$OUT"

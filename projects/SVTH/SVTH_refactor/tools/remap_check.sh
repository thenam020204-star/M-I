#!/bin/bash
# remap_check.sh [-o out.jar] map1.map [map2.map ...]
# Áp mapping lên JAR gốc (+placeholder cho thành viên chưa đặt tên), kiểm quy tắc Java/ghi đè/phân giải, in lỗi theo dòng map.
# Không ghi JAR nếu có lỗi. Chạy từ SVTH_refactor/. ORIG = JAR gốc (mặc định: $ORIG hoặc JAR trong /root/.claude/uploads).
# Kết quả trung gian ở build/remap_<pid>/ (chạy song song được); biến KEEP=1 để giữ lại resolved/inverse/reverse.
unset JAVA_TOOL_OPTIONS
L=tools/lib
ASM=$L/asm-9.7.jar:$L/asm-tree-9.7.jar:$L/asm-analysis-9.7.jar:$L/asm-commons-9.7.jar:$L/asm-util-9.7.jar
ORIG=${ORIG:-$(ls /root/.claude/uploads/*/*.jar 2>/dev/null | head -1)}
BD=build/remap_$$
mkdir -p build/tools $BD
OUT=$BD/check.jar
if [ "$1" = "-o" ]; then OUT=$2; shift 2; fi
if [ ! -f build/tools/Remap.class ] || [ tools/src/Remap.java -nt build/tools/Remap.class ]; then
  javac -nowarn -cp $ASM -d build/tools tools/src/Remap.java || exit 1
fi
ARGS=""
for m in "$@"; do ARGS="$ARGS --map $m"; done
java -Dstdout.encoding=UTF-8 -cp build/tools:$ASM Remap apply "$ORIG" "$OUT" --lib $L/cldcapi10-2.0.4.jar:$L/midpapi20-2.0.4.jar \
  --scope "$ORIG" --placeholders $ARGS --resolved $BD/resolved.map --inverse $BD/inverse.map --reverse $BD/reverse.tsv 2>&1 | head -${LINES_MAX:-60}
RC=${PIPESTATUS[0]}
[ -n "$KEEP" ] && echo "kết quả giữ ở $BD" || rm -rf $BD
exit $RC

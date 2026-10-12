#!/bin/bash
# roundtrip_check.sh — sinh mapping/{resolved.map,reverse.tsv,inverse.map} từ mapping/part_*.map trên JAR gốc,
# rồi chứng minh khứ hồi: JAR đã đặt tên -> áp inverse.map -> so với JAR gốc (class qua ASM + md5 tài nguyên) phải GIỐNG HỆT.
# Chạy từ SVTH_refactor/. ORIG = JAR gốc.
set -e
unset JAVA_TOOL_OPTIONS
L=tools/lib
ASM=$L/asm-9.7.jar:$L/asm-tree-9.7.jar:$L/asm-analysis-9.7.jar:$L/asm-commons-9.7.jar:$L/asm-util-9.7.jar
if [ -z "$ORIG" ]; then   # session 6: thư mục upload có cả bản SVTH_SFX*.jar người dùng sửa -> chọn đúng JAR gốc (md5 f776d09d…)
  for j in /root/.claude/uploads/*/*.jar; do [ "$(md5sum < "$j" | cut -c1-32)" = f776d09d158981a043a443a15c5bbee3 ] && ORIG=$j && break; done
fi
LIBS=$L/cldcapi10-2.0.4.jar:$L/midpapi20-2.0.4.jar
mkdir -p build/tools build/roundtrip
javac -nowarn -cp $ASM -d build/tools tools/src/Remap.java
ARGS=""; for m in mapping/part_*.map; do ARGS="$ARGS --map $m"; done
J="java -Dstdout.encoding=UTF-8 -cp build/tools:$ASM Remap"
$J apply "$ORIG" build/roundtrip/named.jar --lib $LIBS --scope "$ORIG" --placeholders $ARGS \
   --resolved mapping/resolved.map --inverse mapping/inverse.map --reverse mapping/reverse.tsv | tail -3
$J apply build/roundtrip/named.jar build/roundtrip/back.jar --lib $LIBS --lenient --map mapping/inverse.map | tail -2
$J compare "$ORIG" build/roundtrip/back.jar | tail -3

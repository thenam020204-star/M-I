#!/bin/bash
# build_jar.sh [release|debug]   — dựng JAR SVTH + SFX. Chạy từ SVTH_refactor/.
# Đường B: ECJ(-1.3 -target 1.1) class mới → Patch (ASM) class gốc → Peephole → Verify → pack → ProGuard preverify → Verify --require-stackmap → JAD
set -e
unset JAVA_TOOL_OPTIONS
MODE=${1:-release}
VERSION=${VERSION:-1.7.0}
RENAME=${RENAME:-1}   # session 6: mặc định dựng bản đã đặt tên (bản gốc chính từ phiên 6) -> dist/<tên>_renamed.jar; RENAME=0: tên rối gốc
# session 6: the uploads dir now also holds user-edited SVTH_SFX*.jar builds -> pick the original game JAR only
if [ -z "$ORIG" ]; then   # bản gốc của game: md5 f776d09d158981a043a443a15c5bbee3
  for j in /root/.claude/uploads/*/*.jar; do [ "$(md5sum < "$j" | cut -c1-32)" = f776d09d158981a043a443a15c5bbee3 ] && ORIG=$j && break; done
  ORIG=${ORIG:-$(ls /root/.claude/uploads/*/*.jar | grep -v SVTH_SFX | head -1)}
fi
L=tools/lib
LIBS=$L/cldcapi10-2.0.4.jar:$L/midpapi20-2.0.4.jar
ASM=$L/asm-9.7.jar:$L/asm-tree-9.7.jar:$L/asm-analysis-9.7.jar:$L/asm-commons-9.7.jar:$L/asm-util-9.7.jar
B=build/$MODE; D=dist
NAME=SVTH_SFX; [ "$MODE" = debug ] && NAME=SVTH_SFX_debug
rm -rf $B && mkdir -p $B/classes $B/stubs $B/tools $B/src $D
java -cp $ASM -version >/dev/null 2>&1 || true
# công cụ
javac -nowarn -cp $ASM -d $B/tools tools/src/*.java
# nguồn: src/ (+ debug/ nếu debug); BuildFlags theo chế độ
cp src/*.java $B/src/
if [ "$MODE" = debug ]; then
  cp debug/*.java $B/src/
  sed -i 's/DEBUG = false/DEBUG = true/' $B/src/BuildFlags.java
fi
java -jar $L/ecj-3.26.0.jar -1.3 -target 1.1 -nowarn -bootclasspath $LIBS -d $B/stubs stubs/*.java
java -jar $L/ecj-3.26.0.jar -1.3 -target 1.1 -nowarn -encoding UTF-8 -bootclasspath $LIBS -cp $B/stubs -d $B/classes $B/src/*.java
# vá class gốc
PARGS=""; [ "$MODE" = debug ] && PARGS="--debug" && MANIFEST_EXTRA="SFX-Debug: 1"
java -Dstdout.encoding=UTF-8 -cp $B/tools:$ASM Patch "$ORIG" $B/patched $PARGS 2>&1 | tee $B/patch.log | tail -3
java -cp $B/tools:$ASM Peephole $B/classes
# tài nguyên: sfx/*
python3 -I tools/pack.py "$ORIG" $B/patched $B/classes res $B/unverified.jar $VERSION ${MANIFEST_EXTRA:+--manifest "$MANIFEST_EXTRA"}
java -cp $B/tools:$ASM Verify $B/unverified.jar $LIBS
if [ "$RENAME" = 1 ]; then
  # session 5: đổi tên class/field/hàm trên bytecode theo mapping/part_*.map (Patch/stubs/src vẫn dùng tên gốc); thành viên chưa đặt tên nhận placeholder <cũ>_<kiểu>
  MAPS=""; for m in mapping/part_*.map; do MAPS="$MAPS --map $m"; done
  java -Dstdout.encoding=UTF-8 -cp $B/tools:$ASM Remap apply $B/unverified.jar $B/renamed.jar --lib $LIBS --scope "$ORIG" --placeholders $MAPS 2>&1 | tail -3
  mv $B/renamed.jar $B/unverified.jar
  NAME=${NAME}_renamed
  java -cp $B/tools:$ASM Verify $B/unverified.jar $LIBS
fi
cat > $B/pg.pro <<PG
-injars $PWD/$B/unverified.jar
-outjars $PWD/$B/preverified.jar
-libraryjars $PWD/$L/cldcapi10-2.0.4.jar
-libraryjars $PWD/$L/midpapi20-2.0.4.jar
-microedition
-dontshrink
-dontoptimize
-dontobfuscate
-dontwarn
-dontnote
-keepattributes *
PG
rm -f $B/preverified.jar
java -cp $L/proguard-base-7.4.2.jar:$L/proguard-core-9.1.1.jar:$L/log4j-api-2.17.2.jar:$L/log4j-core-2.17.2.jar:$L/kotlin-stdlib-1.8.21.jar:$L/gson-2.9.1.jar proguard.ProGuard @$B/pg.pro > $B/proguard.log 2>&1 || { cat $B/proguard.log; exit 1; }
java -cp $B/tools:$ASM Verify $B/preverified.jar $LIBS --require-stackmap
cp $B/preverified.jar $D/$NAME.jar
SZ=$(stat -c %s $D/$NAME.jar)
python3 -I - <<PY
import zipfile
m = zipfile.ZipFile('$D/$NAME.jar').read('META-INF/MANIFEST.MF').decode('utf-8').replace('\r\n','\n').strip().split('\n')
m = [l for l in m if l and not l.startswith('Manifest-Version') ]
m.append('MIDlet-Jar-URL: $NAME.jar'); m.append('MIDlet-Jar-Size: $SZ')
open('$D/$NAME.jad','w',encoding='utf-8').write('\r\n'.join(m)+'\r\n')
PY
echo "OK $D/$NAME.jar ($SZ bytes) version $VERSION mode $MODE"

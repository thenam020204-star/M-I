#!/bin/bash
# build_s8.sh [release|debug]  — phiên 8: dựng trên bản 1.7.0 đã đặt tên (không cần JAR gốc của game).
# base JAR → Patch8 (ASM: hook EnemyAI vào Battle) → ECJ src8/ (-1.3 -target 1.1) → Peephole → pack.py (+ res/) → Verify
#   → ProGuard -microedition (StackMap) → Verify --require-stackmap → JAD.  Chạy từ SVTH_refactor/.
set -e -o pipefail
unset JAVA_TOOL_OPTIONS
MODE=${1:-release}
VERSION=${VERSION:-1.8.0}
L=tools/lib
LIBS=$L/cldcapi10-2.0.4.jar:$L/midpapi20-2.0.4.jar
ASM=$L/asm-9.7.jar:$L/asm-tree-9.7.jar:$L/asm-analysis-9.7.jar:$L/asm-commons-9.7.jar:$L/asm-util-9.7.jar
if [ "$MODE" = debug ]; then BASE=${BASE:-base/SVTH_SFX_debug_renamed_1.7.0.jar}; NAME=SVTH_SFX_debug_renamed
else BASE=${BASE:-base/SVTH_SFX_renamed_1.7.0_user.jar}; NAME=SVTH_SFX_renamed; fi
B=build/s8_$MODE; D=dist
rm -rf $B && mkdir -p $B/classes $B/tools $B/patched $D
javac -nowarn -cp $ASM -d $B/tools tools/src/Patch8.java tools/src/Verify.java tools/src/Peephole.java
java -cp $B/tools:$ASM Patch8 "$BASE" $B/patched | tee $B/patch.log
java -jar $L/ecj-3.26.0.jar -1.3 -target 1.1 -nowarn -encoding UTF-8 -bootclasspath $LIBS -cp $B/patched:"$BASE" -d $B/classes src8/*.java
java -cp $B/tools:$ASM Peephole $B/classes
python3 -I tools/pack.py "$BASE" $B/patched $B/classes res $B/unverified.jar $VERSION
java -cp $B/tools:$ASM Verify $B/unverified.jar $LIBS
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

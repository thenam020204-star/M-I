#!/bin/bash
# Build toan bo: jar goc -> doi ten -> va bytecode -> class moi -> du lieu dich -> preverify -> kiem tra.
set -e
cd "$(dirname "$0")/.."
source tools/env.sh
L=tools/lib; CP=scripts/renamer:$L/asm.jar:$L/asm-commons.jar:$L/asm-tree.jar
B=build; rm -rf $B; mkdir -p $B/jar
env -u JAVA_TOOL_OPTIONS java -cp $CP Renamer clean.jar mapping/rename.map $B/named.jar
env -u JAVA_TOOL_OPTIONS java -cp $CP Patcher $B/named.jar $B/patched_cls.jar mapping/class_strings.tsv
sh scripts/compile.sh $B/patched_cls.jar
(cd $B/jar && unzip -q ../patched_cls.jar)
cp patch/out/*.class $B/jar/
mkdir -p $B/jar/font $B/jar/META-INF; cp tools/fonts/f13c.bin $B/jar/font/
cp mapping/MANIFEST.MF $B/jar/META-INF/
[ -x scripts/apply_text.sh ] && bash scripts/apply_text.sh $B/jar
(cd $B/jar && rm -f ../raw.jar && zip -qr -X ../raw.jar META-INF/MANIFEST.MF . )
bash skscripts/preverify.sh $B/raw.jar $B/game.jar

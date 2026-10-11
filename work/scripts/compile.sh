#!/bin/sh
# Bien dich class moi (CLDC: Java 1.3, class 47) vao patch/out. Dung: sh scripts/compile.sh <classpath jar>
set -e
cd "$(dirname "$0")/.."
L=tools/lib
rm -rf patch/out; mkdir -p patch/out
env -u JAVA_TOOL_OPTIONS java -cp $L/ecj.jar org.eclipse.jdt.internal.compiler.batch.Main -source 1.3 -target 1.3 \
  -encoding UTF-8 -nowarn -bootclasspath $L/jbase/classes -cp "$1:tools/freej2me_plus.jar" -d patch/out patch/src/*.java

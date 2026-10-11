#!/bin/sh
# Bien dich BitmapFont.java -> BitmapFont.class (Java 1.3, class 47) va preverify cho CLDC/MIDP.
# Can: JDK 9+ (javac, jmod, java). ecj + proguard duoc tim san, neu khong thi tu tai:
#   ecj      : /usr/share/java/ecj.jar (apt: libecj-java) hoac Maven Central
#   proguard : $PROGUARD_JAR, hoac GitHub release 7.4.2 (proguard.jar da gom core)
# Ket qua: java/BitmapFont.class (ghi de), tu kiem class version == 47.
set -e
cd "$(dirname "$0")"
W="$(pwd)/build_tmp"; mkdir -p "$W/lib" "$W/stubs" "$W/out" "$W/pv"

ECJ_JAR=/usr/share/java/ecj.jar
if [ ! -f "$ECJ_JAR" ]; then
  ECJ_JAR="$W/lib/ecj-3.26.0.jar"
  [ -f "$ECJ_JAR" ] || curl -sSfL -o "$ECJ_JAR" https://repo1.maven.org/maven2/org/eclipse/jdt/ecj/3.26.0/ecj-3.26.0.jar
fi
PG_JAR="${PROGUARD_JAR:-$W/lib/proguard.jar}"
if [ ! -f "$PG_JAR" ]; then
  curl -sSfL -o "$W/lib/pg.zip" https://github.com/Guardsquare/proguard/releases/download/v7.4.2/proguard-7.4.2.zip
  unzip -q -o -j "$W/lib/pg.zip" proguard-7.4.2/lib/proguard.jar -d "$W/lib"
fi
JH=$(dirname $(dirname $(readlink -f $(which javac))))
[ -d "$W/jbase" ] || jmod extract --dir "$W/jbase" "$JH/jmods/java.base.jmod"

ECJ="java -cp $ECJ_JAR org.eclipse.jdt.internal.compiler.batch.Main -source 1.3 -target 1.3 -encoding UTF-8 -nowarn -bootclasspath $W/jbase/classes"
$ECJ -d "$W/stubs" test/stubs/javax/microedition/lcdui/*.java
$ECJ -cp "$W/stubs" -d "$W/out" BitmapFont.java
cat > "$W/pv.pro" <<P
-injars $W/out
-outjars $W/pv
-libraryjars $W/jbase/classes
-libraryjars $W/stubs
-microedition
-dontshrink
-dontoptimize
-dontobfuscate
-dontwarn
-keep class * { *; }
P
rm -rf "$W/pv"
java -jar "$PG_JAR" @"$W/pv.pro" >/dev/null
cp "$W/pv/BitmapFont.class" BitmapFont.class
V=$(od -An -tu1 -j7 -N1 BitmapFont.class | tr -d ' ')
[ "$V" = 47 ] || { echo "SAI: class version $V, can 47"; exit 1; }
echo "OK -> java/BitmapFont.class (version 47, $(wc -c < BitmapFont.class) byte)"

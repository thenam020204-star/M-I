#!/usr/bin/env bash
# j2mec.sh SRC_DIR MANIFEST OUT_JAR [cldc10|cldc11]
# Compile J2ME sources with -source 1.3 -target 1.3 against CLDC/MIDP stubs (MicroEmulator, Apache-2.0),
# then preverify with ProGuard -microedition (adds StackMap attributes), then jar with manifest.
set -euo pipefail
. "$(dirname "$0")/lib.sh"; . "$ENV_DIR/versions.sh"
SRC="$1"; MF="$2"; OUT="$(realpath -m "$3")"; CLDC="${4:-cldc11}"
SRC="$(realpath "$SRC")"; MF="$(realpath "$MF")"
M="$CACHE/maven"
STUBS="${J2MEC_LIBS:-$M/${CLDC/cldc/cldcapi}-$MICROEMU_VER.jar:$M/midpapi20-$MICROEMU_VER.jar}"   # J2MEC_LIBS: override (negative tests only)
W="$(mktemp -d)"; trap 'rm -rf "$W"' EXIT
mkdir -p "$W/raw" "$W/pv"
find "$SRC" -name '*.java' > "$W/srcs.txt"
"$JDK8_HOME/bin/javac" -encoding UTF-8 -nowarn -source 1.3 -target 1.3 -bootclasspath "$STUBS" -extdirs "" -d "$W/raw" @"$W/srcs.txt" >"$W/javac.log" 2>&1 \
  || { grep -v '^Picked up' "$W/javac.log" >&2; echo "javac failed" >&2; exit 1; }
PG="$M/proguard-base-$PROGUARD_VER.jar:$M/proguard-core-$PROGUARD_CORE_VER.jar:$M/kotlin-stdlib-$KOTLIN_STDLIB_VER.jar:$M/gson-$GSON_VER.jar:$M/log4j-api-$LOG4J_VER.jar:$M/log4j-core-$LOG4J_VER.jar:$M/json-$JSON_VER.jar"
cat > "$W/pg.conf" <<PGC
-injars $W/raw
-outjars $W/pv
-libraryjars ${STUBS}
-microedition
-dontshrink
-dontoptimize
-dontobfuscate
-dontnote
-ignorewarnings
PGC
java -cp "$PG" proguard.ProGuard @"$W/pg.conf" >"$W/pg.log" 2>&1 || { cat "$W/pg.log"; echo "preverify (ProGuard) failed" >&2; exit 1; }
mkdir -p "$(dirname "$OUT")"; rm -f "$OUT"
(cd "$W/pv" && "$JDK8_HOME/bin/jar" cfm "$OUT" "$MF" . 2>&1 | grep -v '^Picked up' || true)
echo "built $OUT"

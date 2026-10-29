#!/usr/bin/env bash
# build.sh [OUT_JAR]  -- builds the TEMPORARY guest stub ROM (PLAN T11; replaced by tools/romgen + vm-classlib in T17/T18).
# javac -source 1.3 -target 1.3 with an EMPTY bootclasspath (D11: the library only sees itself), ProGuard -microedition preverify.
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; ROOT="$HERE/../../../.."
. "$ROOT/tools/env/lib.sh"; . "$ROOT/tools/env/versions.sh"
OUT="$(realpath -m "${1:-$ROOT/vm-core/build/guest-stub/rom.jar}")"
W="$(mktemp -d)"; trap 'rm -rf "$W"' EXIT
mkdir -p "$W/raw" "$W/pv" "$W/empty"
find "$HERE/src" -name '*.java' > "$W/srcs.txt"
env -u JAVA_TOOL_OPTIONS "$JDK8_HOME/bin/javac" -encoding UTF-8 -nowarn -source 1.3 -target 1.3 -bootclasspath "$W/empty" -extdirs "" -d "$W/raw" @"$W/srcs.txt" 2>&1 | grep -v '^Picked up\|^warning\|^1 warning\|^[0-9]* warnings' || true
[ -f "$W/raw/java/lang/Object.class" ] || { echo "stub ROM javac failed" >&2; exit 1; }
M="$CACHE/maven"
PG="$M/proguard-base-$PROGUARD_VER.jar:$M/proguard-core-$PROGUARD_CORE_VER.jar:$M/kotlin-stdlib-$KOTLIN_STDLIB_VER.jar:$M/gson-$GSON_VER.jar:$M/log4j-api-$LOG4J_VER.jar:$M/log4j-core-$LOG4J_VER.jar:$M/json-$JSON_VER.jar"
cat > "$W/pg.conf" <<PGC
-injars $W/raw
-outjars $W/pv
-microedition
-dontshrink
-dontoptimize
-dontobfuscate
-dontnote
-ignorewarnings
PGC
env -u JAVA_TOOL_OPTIONS java -cp "$PG" proguard.ProGuard @"$W/pg.conf" >"$W/pg.log" 2>&1 || { cat "$W/pg.log"; echo "preverify failed" >&2; exit 1; }
mkdir -p "$(dirname "$OUT")"; rm -f "$OUT"
(cd "$W/pv" && env -u JAVA_TOOL_OPTIONS "$JDK8_HOME/bin/jar" cf "$OUT" .)
echo "built $OUT ($(unzip -l "$OUT" | grep -c '\.class') classes)"

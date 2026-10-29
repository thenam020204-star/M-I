#!/usr/bin/env bash
# Builds extra real-world CLDC-preverified class sets (PLAN T08 stress): third-party Java classes from the toolchain cache are
# preverified with ProGuard -microedition (adds StackMap; -target 1.3 lowers the class version to 47). These are NOT CLDC-API
# conformant programs, they only exercise the verifier on large amounts of real bytecode (hierarchy from the host JVM in the test).
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; ROOT="$HERE/../.."
. "$ROOT/tools/env/lib.sh"; . "$ROOT/tools/env/versions.sh"
M="$CACHE/maven"; OUT="$HERE/build/stress"; mkdir -p "$OUT"
PG="$M/proguard-base-$PROGUARD_VER.jar:$M/proguard-core-$PROGUARD_CORE_VER.jar:$M/kotlin-stdlib-$KOTLIN_STDLIB_VER.jar:$M/gson-$GSON_VER.jar:$M/log4j-api-$LOG4J_VER.jar:$M/log4j-core-$LOG4J_VER.jar:$M/json-$JSON_VER.jar"
RT="$JDK8_HOME/jre/lib/rt.jar"
for j in microemu-midp-2.0.4 microemu-cldc-2.0.4 microemu-javase-2.0.4 microemu-javase-swing-2.0.4 ecj-3.12.3 gson-2.9.0; do
  W="$(mktemp -d)"
  cat > "$W/pg.conf" <<PGC
-injars $M/$j.jar
-outjars $OUT/$j.pv.jar
-libraryjars $RT
$(for l in microemu-cldc-2.0.4 microemu-midp-2.0.4 microemu-javase-2.0.4; do [ "$l" = "$j" ] || echo "-libraryjars $M/$l.jar"; done)
-microedition
-target 1.3
-dontshrink
-dontoptimize
-dontobfuscate
-dontnote
-ignorewarnings
PGC
  rm -f "$OUT/$j.pv.jar"
  java -cp "$PG" proguard.ProGuard @"$W/pg.conf" > "$W/pg.log" 2>&1 || { tail -5 "$W/pg.log"; echo "preverify of $j failed" >&2; }
  rm -rf "$W"
  [ -f "$OUT/$j.pv.jar" ] && echo "built $OUT/$j.pv.jar"
done

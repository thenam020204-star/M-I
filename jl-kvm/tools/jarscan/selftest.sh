#!/usr/bin/env bash
# jarscan selftest (PLAN T04): bad jar must be NOT J2ME-SAFE (exit != 0), clean jar must pass (exit 0).
# Requires: tools/env/setup_toolchain.sh done, DBs in tools/api-sigs/db (cldc-1.1, midp-2.0), gradle installDist.
set -euo pipefail
D="$(cd "$(dirname "$0")" && pwd)"; ROOT="$D/../.."
. "$ROOT/tools/env/lib.sh"; . "$ROOT/tools/env/versions.sh"
W="${1:-$D/build/selftest}"; rm -rf "$W"; mkdir -p "$W/bad-raw"
# bad jar: compiled against the full JDK 8 class library on purpose, no preverify
# the bad MIDlet needs MIDlet from the stub: compile with stub appended to classpath
"$JDK8_HOME/bin/javac" -nowarn -source 1.3 -target 1.3 -classpath "$CACHE/maven/midpapi20-$MICROEMU_VER.jar" -d "$W/bad-raw" "$D/testdata/bad/bad/BadMIDlet.java" 2>&1 | grep -v -e '^Picked up' -e 'warning' || true
(cd "$W/bad-raw" && "$JDK8_HOME/bin/jar" cfm "$W/bad.jar" "$D/testdata/bad/MANIFEST.MF" . 2>&1 | grep -v '^Picked up' || true)
"$ROOT/tools/env/j2mec.sh" "$D/testdata/clean" "$D/testdata/clean/MANIFEST.MF" "$W/clean.jar" cldc11 >/dev/null
LIB="${JARSCAN_LIB:-$D/build/install/jarscan/lib}"
[ -d "$LIB" ] || [ -n "${JARSCAN_CP:-}" ] || (cd "$ROOT" && ./gradlew -q :tools:jarscan:installDist >&2)
run() { java -cp "${JARSCAN_CP:-$LIB/*}" jlkvm.tools.jarscan.Main --profile "$D/profiles/cldc11-midp20.json" --db-dir "$ROOT/tools/api-sigs/db" "$@" 2> >(grep -v '^Picked up' >&2); }
rc=0
set +e
run --out-dir "$W/bad-report" "$W/bad.jar" > "$W/bad.out"; bad_rc=$?
run --out-dir "$W/clean-report" "$W/clean.jar" > "$W/clean.out"; clean_rc=$?
set -e
echo "bad.jar   exit=$bad_rc (expected != 0)"; grep -E 'MISSING|NO_STACKMAP|NOT J2ME' "$W/bad.out" | head
echo "clean.jar exit=$clean_rc (expected 0)"; grep -E 'Result' "$W/clean.out"
grep -q 'isEmpty' "$W/bad.out" && grep -q 'java.util.ArrayList' "$W/bad.out" || { echo "FAIL: isEmpty/ArrayList not flagged"; rc=1; }
[ "$bad_rc" -ne 0 ] || { echo "FAIL: bad jar exit 0"; rc=1; }
[ "$clean_rc" -eq 0 ] || { echo "FAIL: clean jar exit != 0"; rc=1; }
[ $rc -eq 0 ] && echo "SELFTEST PASS"
exit $rc

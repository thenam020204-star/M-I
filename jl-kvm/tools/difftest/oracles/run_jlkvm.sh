#!/usr/bin/env bash
# run_jlkvm.sh GAME.jar [RunJar options: --cldc10 --lenient --debug --noverify --timeout SEC]
# Runs the MIDlet on THIS project's VM (vm-core) with the test ROM = real CLDC ROM (vm-classlib, T17/T18) + real MIDP layer (vm-classlib/midp) + test overlay (vm-core/src/test/guest-stub).
# JLKVM_MAIN=jlkvm.tmp.Bench runs the benchmark driver instead (tests/bench/run_bench.sh).
# Guest console output on stdout, VM diagnostics on stderr. Rebuilds the engine classes / stub ROM when sources changed.
set -uo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; ROOT="$HERE/../../.."
. "$ROOT/tools/env/lib.sh"; . "$ROOT/tools/env/versions.sh"
JAR="$(realpath "$1")"; shift
# JLKVM_RELEASE=1: compile vm-core with the RELEASE DebugFlags (D15/T16: every debug hook compiled out) into a separate class dir
B="$ROOT/vm-core/build/jlkvm-cli"; [ -n "${JLKVM_RELEASE:-}" ] && B="$ROOT/vm-core/build/jlkvm-cli-release"; ROM="$ROOT/vm-core/build/guest-stub/rom.jar"
mkdir -p "$B"
if [ -n "$(find "$ROOT/vm-core/src/main" "$ROOT/vm-core/src/release" "$ROOT/vm-core/src/test/java/jlkvm/tmp" "$ROOT/host-common/src/main/java" -name '*.java' -newer "$B/.stamp" 2>/dev/null | head -1)" ] || [ ! -f "$B/.stamp" ]; then
  rm -rf "$B/classes"; mkdir -p "$B/classes"
  env -u JAVA_TOOL_OPTIONS "$JDK8_HOME/bin/javac" -encoding UTF-8 -nowarn -d "$B/classes" \
    $(if [ -n "${JLKVM_RELEASE:-}" ]; then find "$ROOT/vm-core/src/main" -name '*.java' ! -path '*/jlkvm/debug/DebugFlags.java'; find "$ROOT/vm-core/src/release" "$ROOT/vm-core/src/test/java/jlkvm/tmp" "$ROOT/host-common/src/main/java" -name '*.java'; else find "$ROOT/vm-core/src/main" "$ROOT/vm-core/src/test/java/jlkvm/tmp" "$ROOT/host-common/src/main/java" -name '*.java'; fi) 2>&1 | grep -v '^Picked up' >&2
  [ -f "$B/classes/jlkvm/tmp/RunJar.class" ] || { echo "engine build failed" >&2; exit 2; }
  touch "$B/.stamp"
fi
if [ ! -f "$ROM" ] || [ -n "$(find "$ROOT/vm-core/src/test/guest-stub/src" "$ROOT/vm-classlib/src" "$ROOT/vm-classlib/midp/src" "$ROOT/vm-classlib/jsr135/src" "$ROOT/vm-classlib/nokiaui/src" "$ROOT/vm-classlib/wma/src" "$ROOT/vm-classlib/jsr75-fc/src" "$ROOT/vm-classlib/jsr75-pim/src" "$ROOT/vm-classlib/jsr82/src" "$ROOT/vm-classlib/jsr179/src" "$ROOT/vm-classlib/jsr256/src" "$ROOT/vm-classlib/jsr234/src" -name '*.java' -newer "$ROM" | head -1)" ]; then
  "$ROOT/vm-core/src/test/guest-stub/build.sh" "$ROM" >&2 || exit 2
fi
# JLKVM_ARGS (e.g. "--gc-stress 1 --gc-verify") is added to every run except --noverify ones: without a verified method there is no GC type map
EXTRA="${JLKVM_ARGS:-}"; case " $* " in *" --noverify "*) EXTRA="" ;; esac
exec env -u JAVA_TOOL_OPTIONS "${JLKVM_JAVA:-$JDK8_HOME/bin/java}" ${JLKVM_JAVA_OPTS:-} -cp "$B/classes:$ROOT/host-common/src/main/resources" ${JLKVM_MAIN:-jlkvm.tmp.RunJar} "$ROM" "$JAR" "$@" $EXTRA

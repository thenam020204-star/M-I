#!/usr/bin/env bash
# run_microemu.sh GAME.jar [MIDletClass]   -> guest stdout/stderr on stdout, framework noise removed.
# MicroEmulator 2.0.4 org.microemu.app.Headless (no X needed). Without MIDletClass the jar is opened normally
# (manifest, getAppProperty, getResourceAsStream work); with MIDletClass the jar is only put on the app classpath
# (no manifest: getAppProperty returns null).
# Env: ORACLE_TIMEOUT (s, default 60). Exit code: 0 ok, 124 timeout.
set -uo pipefail
. "$(dirname "$0")/common.sh"
JAR="$(realpath "$1")"; CLS="${2:-}"
[ -f "$MICROEMU_JAR" ] || { echo "run setup_oracles.sh first" >&2; exit 2; }
W="$(mktemp -d)"; trap 'rm -rf "$W"' EXIT
( cd "$W" && oracle_java timeout "$ORACLE_TIMEOUT" "$JDK8_HOME/bin/java" -Djava.awt.headless=true -cp "$MICROEMU_JAR" \
    org.microemu.app.Headless --quit --logCallLocation false $( [ -n "$CLS" ] && echo --appclasspath ) "$JAR" $CLS ) > "$W/raw" 2>&1
rc=$?
# drop MicroEmulator's own lines (headless arguments / notifyDestroyed / destroyMIDletContext)
grep -v -e '^headless arguments ' -e '^notifyDestroyed$' -e '^destroyMIDletContext$' -e '^openJar ' -e '^[[:space:]]*org\.microemu\.app\.' "$W/raw"
exit $rc

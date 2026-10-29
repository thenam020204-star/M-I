#!/usr/bin/env bash
# run_freej2me.sh GAME.jar [WxH]   -> guest stdout on stdout; framework "[INFO]/[ERROR] javax.microedition..." lines removed.
# FreeJ2ME-Plus under Xvfb (it has no real headless mode). Stops when the MIDlet reports destroy, or after ORACLE_TIMEOUT.
set -uo pipefail
. "$(dirname "$0")/common.sh"
JAR="$(realpath "$1")"; SZ="${2:-240x320}"
command -v xvfb-run >/dev/null || { echo "xvfb-run missing" >&2; exit 2; }
[ -f "$FREEJ2ME_JAR" ] || { echo "missing $FREEJ2ME_JAR" >&2; exit 2; }
W="$(mktemp -d)"; trap 'kill $(jobs -p) 2>/dev/null; rm -rf "$W"' EXIT
export HOME="$W/home"; mkdir -p "$HOME"     # keep FreeJ2ME prefs/config out of the real home
( cd "$W" && oracle_java xvfb-run -a timeout "$ORACLE_TIMEOUT" java -Dfile.encoding=ISO_8859_1 -jar "$FREEJ2ME_JAR" "$JAR" "${SZ%x*}" "${SZ#*x}" 1 ) > "$W/raw" 2>&1 &
PID=$!
for _ in $(seq 1 $((ORACLE_TIMEOUT * 5))); do
  grep -q 'MIDlet sent Destroyed Notification' "$W/raw" 2>/dev/null && { sleep 0.5; break; }
  kill -0 $PID 2>/dev/null || break
  sleep 0.2
done
pkill -P $PID 2>/dev/null; pkill -f "freej2me_plus.jar $JAR" 2>/dev/null
wait $PID 2>/dev/null
grep -v -e '^\[INFO\] ' -e '^\[ERROR\] javax.microedition.media' -e '^INFO: Created user preferences' \
        -e 'java.util.prefs.FileSystemPreferences' -e '^Terminated$' "$W/raw"
exit 0

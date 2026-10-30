#!/usr/bin/env bash
# run_kemulator.sh GAME.jar  -> guest stdout on stdout (KEmulator's own log lines removed).
# KEmulator nnmod (tools/env/build_kemulator.sh, local clone, licence unknown) under Xvfb with the JDK 21 `java`, SWT GTK. It has no real headless
# mode (SWT window), so a private copy of the build dir holds a property.txt (in the private copy) that suppresses the first-start dialogs (AutoUpdate=2 + `-updated`, ShowAppSettingsOnStart=false).
# Stops when the MIDlet reports notifyDestroyed / the process ends, or after ORACLE_TIMEOUT seconds (exit 124). Exit 2: not built.
set -uo pipefail
. "$(dirname "$0")/common.sh"
JAR="$(realpath "$1")"
KEM="${KEMULATOR_DIR:-$CACHE/kemulator}"; JAVA21="${KEMULATOR_JAVA:-java}"
command -v xvfb-run >/dev/null || { echo "xvfb-run missing" >&2; exit 2; }
[ -f "$KEM/KEmulator.jar" ] || { echo "KEmulator not built: run tools/env/build_kemulator.sh" >&2; exit 2; }
W="$(mktemp -d)"; trap 'kill $(jobs -p) 2>/dev/null; pkill -f "KEmulator.jar -jar $JAR" 2>/dev/null; rm -rf "$W"' EXIT
# KEmulator is "portable" when its own directory is writable (running as root / from a checkout): settings, rms/ and midlets.ini then live NEXT TO
# KEmulator.jar. So every run gets a private copy (13 MB) of the build dir: no state leaks between cases (RMS!) and runs may be parallel.
export HOME="$W/home"; mkdir -p "$HOME"; cp -a "$KEM" "$W/kem"; KEM="$W/kem"
printf 'AutoUpdate=2\nShowAppSettingsOnStart=false\n' > "$KEM/property.txt"
( cd "$KEM" && env -u JAVA_TOOL_OPTIONS xvfb-run -a timeout "$ORACLE_TIMEOUT" "$JAVA21" -Xmx512M -Djna.nosys=true -Djava.library.path="$KEM" \
    -Dfile.encoding=UTF-8 -Dswt.autoScale=false -javaagent:KEmulator.jar -XX:+IgnoreUnrecognizedVMOptions \
    --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.lang.ref=ALL-UNNAMED \
    --add-opens java.base/java.io=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED \
    --add-opens jdk.unsupported/sun.misc=ALL-UNNAMED --add-opens java.desktop/com.sun.media.sound=ALL-UNNAMED --add-opens java.desktop/javax.sound.midi=ALL-UNNAMED \
    -jar KEmulator.jar -jar "$JAR" -s -updated ) > "$W/raw" 2>&1 &
PID=$!
for _ in $(seq 1 $((ORACLE_TIMEOUT * 5))); do
  grep -q 'notifyDestroyed\|destroyApp' "$W/raw" 2>/dev/null && { sleep 0.5; break; }
  kill -0 $PID 2>/dev/null || break
  sleep 0.2
done
pkill -P $PID 2>/dev/null; pkill -f "KEmulator.jar -jar $JAR" 2>/dev/null
wait $PID 2>/dev/null
# drop KEmulator's own noise (startup banner, jinput/dbus warnings, "Destroy stack trace", exit message); guest output is untouched
awk '
  /^SWT SessionManagerDBus/ || /^INFO: Loading: net\.java\.games\.input/ || /net\.java\.games\.input\.DefaultControllerEnvironment/ { next }
  /^java\.lang\.NullPointerException: Cannot read the array length because "a" is null/ { skip = "at"; next }
  /^java\.nio\.file\.NoSuchFileException: .*midlets\.ini/ { skip = "at"; next }
  /^KEmulator nnx64 / || /^Get class/ || /^Launch MIDlet class:/ || /^Exiting Emulator/ { next }
  /^Destroy stack trace \(not an error\):/ { skip = "frame"; next }
  skip == "at" && /^[ \t]+at / { next }
  skip == "frame" && /^[A-Za-z_][A-Za-z0-9_.$\/]*\(.*\)$/ { next }
  { skip = ""; print }' "$W/raw"
exit 0

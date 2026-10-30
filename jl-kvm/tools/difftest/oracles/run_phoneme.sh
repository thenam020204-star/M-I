#!/usr/bin/env bash
# run_phoneme.sh GAME.jar [MIDletClass]  -> guest stdout/stderr on stdout (the VM's "REPORT: ..." framework lines removed).
# phoneME Feature (tools/env/build_phoneme.sh, local build of the GPLv2 archive, never committed). Two modes:
#   midp (default when built): headless MIDP 2.0 `runMidlet -classpathext GAME.jar -1 MIDletClass` of the "linux_stubs_gcc" port: real CLDC 1.1 + MIDP
#       classes (rms, io, lcdui objects, Timer...), nothing is drawn (no screen); each run uses a private copy of midp-out, so RMS/appdb state never leaks.
#   cldc (PHONEME_MODE=cldc, or MIDP not built): bare `cldc_vm` + the project's own shim (phoneme-shim: MIDlet + ShimRun) -> CLDC-only cases;
#       javax.microedition.* other than midlet fails with NoClassDefFoundError.
# (a hung case is killed after ORACLE_TIMEOUT; its lines up to the hang are kept.) MIDletClass defaults to the first MIDlet-1 of the jar manifest. Exit: 0 ok, 124/137 timeout (SIGKILL: the VM ignores SIGTERM while hung), 2 not built.
set -uo pipefail
. "$(dirname "$0")/common.sh"
JAR="$(realpath "$1")"; CLS="${2:-}"
VM="$PHONEME_DIR/bin/cldc_vm"; MIDP="$PHONEME_DIR/midp-out/bin/i386/runMidlet"
[ -x "$VM" ] || { echo "phoneME not built: run tools/env/build_phoneme.sh" >&2; exit 2; }
[ -n "$CLS" ] || CLS="$(unzip -p "$JAR" META-INF/MANIFEST.MF | tr -d '\r' | sed -n 's/^MIDlet-1:[^,]*,[^,]*,[ ]*//p' | head -1)"
W="$(mktemp -d)"; trap 'rm -rf "$W"' EXIT
if [ -x "$MIDP" ] && [ "${PHONEME_MODE:-midp}" != cldc ]; then
  cp -a "$PHONEME_DIR/midp-out" "$W/mp"
  # MIDP's System.out is block-buffered on a pipe (output of a hung/killed run would be lost): run it on a pty so it is line-buffered.
  ( cd "$W/mp/bin/i386" && script -qfec "timeout -s KILL $ORACLE_TIMEOUT ./runMidlet -classpathext '$JAR' -1 '$CLS'" /dev/null ) 2>&1 | tr -d '\r' | grep -av '^REPORT: '
else
  [ -f "$PHONEME_DIR/shim.jar" ] || { echo "phoneME shim missing: run tools/env/build_phoneme.sh" >&2; exit 2; }
  ( cd "$W" && timeout -s KILL "$ORACLE_TIMEOUT" "$VM" -classpath "$PHONEME_DIR/shim.jar:$JAR" javax.microedition.midlet.ShimRun "$CLS" ) 2>&1 | grep -v '^# shim: notifyDestroyed$'
fi
exit "${PIPESTATUS[0]}"

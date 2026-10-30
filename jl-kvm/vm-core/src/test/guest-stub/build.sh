#!/usr/bin/env bash
# build.sh [OUT_JAR]  -- builds the TEST ROM = the REAL CLDC ROM (vm-classlib via tools/romgen, PLAN T17/T18) + a small TEMPORARY overlay
# (this directory's src/: MIDlet, MIDletBoot and tiny lcdui classes that M4 (T21-T23) replaces by the real MIDP classes).
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; ROOT="$HERE/../../../.."
OUT="$(realpath -m "${1:-$ROOT/vm-core/build/guest-stub/rom.jar}")"
BASE="$(dirname "$OUT")/cldc-rom.jar"
mkdir -p "$(dirname "$OUT")"
"$ROOT/tools/romgen/romgen" --src "$ROOT/vm-classlib/src" --out "$BASE" --api-db "$ROOT/tools/api-sigs/db/cldc-1.1.json" --api-ignore jlkvm/internal/ >&2
"$ROOT/tools/romgen/romgen" --src "$HERE/src" --base "$BASE" --out "$OUT" >&2
echo "built $OUT ($(unzip -l "$OUT" | grep -c '\.class') classes)"

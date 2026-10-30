#!/usr/bin/env bash
# build.sh [OUT_JAR]  -- builds the TEST ROM = the REAL CLDC ROM + the REAL MIDP 2.0 layer (vm-classlib, PLAN T17/T18/T21-T23, both through
# tools/romgen) + a small test-only overlay (this directory's src/: the "testnet" protocol handler of the Connector policy test, T20).
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; ROOT="$HERE/../../../.."
OUT="$(realpath -m "${1:-$ROOT/vm-core/build/guest-stub/rom.jar}")"
D="$(dirname "$OUT")"
mkdir -p "$D"
"$ROOT/tools/romgen/romgen" --src "$ROOT/vm-classlib/src" --out "$D/cldc-rom.jar" --api-db "$ROOT/tools/api-sigs/db/cldc-1.1.json" --api-ignore jlkvm/internal/ >&2
"$ROOT/tools/romgen/romgen" --src "$ROOT/vm-classlib/midp/src" --base "$D/cldc-rom.jar" --out "$D/midp-rom.jar" >&2   # surface check: gradle :vm-classlib:buildMidpRom
# JSR-135 layer (MIDIControl...): present in the flat test ROM; the profile (api "jsr-135") decides whether Manager hands the controls out
"$ROOT/tools/romgen/romgen" --src "$ROOT/vm-classlib/jsr135/src" --base "$D/midp-rom.jar" --out "$D/jsr135-rom.jar" >&2
"$ROOT/tools/romgen/romgen" --src "$HERE/src" --base "$D/jsr135-rom.jar" --out "$OUT" >&2
echo "built $OUT ($(unzip -l "$OUT" | grep -c '\.class') classes)"

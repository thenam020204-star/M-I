#!/usr/bin/env bash
# build.sh MIDLET_DIR OUT.jar   : compiles MIDLET_DIR/src together with tests/midlets/framework/src (CLDC 1.1, MIDP 2.0,
# -source 1.3 + ProGuard preverify) using the manifest MIDLET_DIR/MANIFEST.MF.
set -euo pipefail
D="$(cd "$(dirname "$0")" && pwd)"; M="$(realpath "$1")"; OUT="$2"
W="$(mktemp -d)"; trap 'rm -rf "$W"' EXIT
mkdir -p "$W/src"; cp -r "$D/framework/src/." "$W/src/"; cp -r "$M/src/." "$W/src/"
"$D/../../tools/env/j2mec.sh" "$W/src" "$M/MANIFEST.MF" "$OUT" "${3:-cldc11}"

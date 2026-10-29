#!/usr/bin/env bash
# build.sh CASE_DIR OUT_JAR : builds the normal jar, then removes the classes that must be missing at run time.
set -euo pipefail
C="$1"; OUT="$2"; ROOT="$(cd "$C/../../../.." && pwd)"
MF="$(mktemp)"; trap 'rm -f "$MF"' EXIT
printf 'MIDlet-Name: l01\nMIDlet-Version: 1.0.0\nMIDlet-Vendor: jl-kvm\nMIDlet-1: T,,vmspec.L01Main\nMicroEdition-Configuration: CLDC-1.1\nMicroEdition-Profile: MIDP-2.0\n' > "$MF"
"$ROOT/tools/env/j2mec.sh" "$C/src" "$MF" "$OUT" cldc11
zip -q -d "$OUT" vmspec/Gone.class vmspec/GoneSuper.class

#!/usr/bin/env bash
# build.sh CASE_DIR OUT_JAR : builds the normal jar, then damages it with Python's zipfile (see mutate.py)
set -euo pipefail
C="$1"; OUT="$2"; ROOT="$(cd "$C/../../../.." && pwd)"
MAIN="$(basename "$(grep -l 'extends MIDlet' "$C"/src/vmspec/*.java | head -1)" .java)"
MF="$(mktemp)"; trap 'rm -f "$MF"' EXIT
printf 'MIDlet-Name: %s\nMIDlet-Version: 1.0.0\nMIDlet-Vendor: jl-kvm\nMIDlet-1: T,,vmspec.%s\nMicroEdition-Configuration: CLDC-1.1\nMicroEdition-Profile: MIDP-2.0\n' "$(basename "$C")" "$MAIN" > "$MF"
"$ROOT/tools/env/j2mec.sh" "$C/src" "$MF" "$OUT" cldc11
python3 -I "$C/mutate.py" "$OUT"

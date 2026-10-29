#!/usr/bin/env bash
# Generates the verifier cases (PLAN T08) with ASM into tests/vmspec/build/verify/ (cases.txt + vf/*.class).
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; ROOT="$HERE/../.."
. "$ROOT/tools/env/lib.sh"; . "$ROOT/tools/env/versions.sh"
ASM="$CACHE/maven/asm-$ASM_VER.jar"
env -u JAVA_TOOL_OPTIONS java -cp "$ASM" "$HERE/gen/VerifyGen.java" "$HERE/build/verify"

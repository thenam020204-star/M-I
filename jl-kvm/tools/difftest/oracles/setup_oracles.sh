#!/usr/bin/env bash
# Download MicroEmulator 2.0.4 (Maven Central) into tools/env/cache (gitignored). FreeJ2ME comes from ../freej2me in the M-I repo.
set -euo pipefail
. "$(dirname "$0")/common.sh"
mvn_fetch org.microemu microemulator "$MICROEMU_VER" >/dev/null
[ -f "$FREEJ2ME_JAR" ] || { echo "missing $FREEJ2ME_JAR (set FREEJ2ME_JAR)" >&2; exit 1; }
echo "microemulator: $MICROEMU_JAR"
echo "freej2me:      $FREEJ2ME_JAR"

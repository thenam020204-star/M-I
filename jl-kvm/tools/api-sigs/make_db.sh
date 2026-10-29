#!/usr/bin/env bash
# make_db.sh : (re)generate tools/api-sigs/db/*.json from the stub jars in tools/env/cache/maven (PLAN T03, V1).
# Sources: MicroEmulator 2.0.4 stubs (org.microemu:cldcapi10/cldcapi11/midpapi20 on Maven Central).
# Nokia UI API DBs are NOT generated: stub jars from the S40 SDKs have not been supplied (V1: UNVERIFIED / empty).
set -euo pipefail
D="$(cd "$(dirname "$0")" && pwd)"; ROOT="$D/../.."
. "$ROOT/tools/env/lib.sh"; . "$ROOT/tools/env/versions.sh"
M="$CACHE/maven"
for j in cldcapi10 cldcapi11 midpapi20; do mvn_fetch org.microemu "$j" "$MICROEMU_VER" >/dev/null; done
SRC="MicroEmulator $MICROEMU_VER stub jar from Maven Central (org.microemu), see PLAN V1"
"$D/api-sigs" extract --name cldc-1.0 --source "$SRC: cldcapi10-$MICROEMU_VER.jar" -o "$D/db/cldc-1.0.json" "$M/cldcapi10-$MICROEMU_VER.jar"
"$D/api-sigs" extract --name cldc-1.1 --source "$SRC: cldcapi11-$MICROEMU_VER.jar" -o "$D/db/cldc-1.1.json" "$M/cldcapi11-$MICROEMU_VER.jar"
"$D/api-sigs" extract --name midp-2.0 --source "$SRC: midpapi20-$MICROEMU_VER.jar" -o "$D/db/midp-2.0.json" "$M/midpapi20-$MICROEMU_VER.jar"

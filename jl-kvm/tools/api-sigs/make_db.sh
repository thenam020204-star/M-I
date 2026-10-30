#!/usr/bin/env bash
# make_db.sh : (re)generate tools/api-sigs/db/*.json from the stub jars in tools/env/cache/maven (PLAN T03, V1).
# Sources: MicroEmulator 2.0.4 stubs (org.microemu:cldcapi10/cldcapi11/midpapi20 on Maven Central).
# Nokia UI API DB: tools/api-sigs/db/s40dp2-nokia-ui.json comes from the S40 DP2.0 SDK classes.zip (PLAN V14b), not generated here.
set -euo pipefail
D="$(cd "$(dirname "$0")" && pwd)"; ROOT="$D/../.."
. "$ROOT/tools/env/lib.sh"; . "$ROOT/tools/env/versions.sh"
M="$CACHE/maven"
for j in cldcapi10 cldcapi11 midpapi20; do mvn_fetch org.microemu "$j" "$MICROEMU_VER" >/dev/null; done
SRC="MicroEmulator $MICROEMU_VER stub jar from Maven Central (org.microemu), see PLAN V1"
"$D/api-sigs" extract --name cldc-1.0 --source "$SRC: cldcapi10-$MICROEMU_VER.jar" -o "$D/db/cldc-1.0.json" "$M/cldcapi10-$MICROEMU_VER.jar"
"$D/api-sigs" extract --name cldc-1.1 --source "$SRC: cldcapi11-$MICROEMU_VER.jar" -o "$D/db/cldc-1.1.json" "$M/cldcapi11-$MICROEMU_VER.jar"
"$D/api-sigs" extract --name midp-2.0 --source "$SRC: midpapi20-$MICROEMU_VER.jar" -o "$D/db/midp-2.0.json" "$M/midpapi20-$MICROEMU_VER.jar"
# WMA 1.1 DB: tools/api-sigs/db/s40dp2-wma-1.1.json comes from the S40 DP2.0 SDK classes.zip (PLAN V14b), not generated here.
# JSR-75 DBs (PLAN T32a, UNVERIFIED, no SDK has JSR-75; see docs/jsr.md), generated once by hand, not by this script:
#   jsr75-fc.json  : api-sigs extract --name jsr75-fc ... over the javax/** classes of org.microemu:microemu-jsr-75:2.0.4 (org/microemu/** LGPL implementation excluded)
#   jsr75-pim.json : api-sigs extract --name jsr75-pim ... over javax/microedition/pim/** of freej2me_plus.jar (local; no PIM stub exists on Maven Central)
# JSR-82 DB: tools/api-sigs/db/s40dp2-jsr82.json comes from the S40 DP2.0 SDK classes.zip (PLAN V14b), not generated here.
# JSR-179 / 256 / 234 DBs (PLAN T32, V18, UNVERIFIED), generated once by hand and then CORRECTED by hand (the "source" field of each says how), not by this script:
#   jsr179-location.json, jsr256-sensor.json, jsr234-amms.json : api-sigs extract over javax/microedition/{location,sensor,amms}/** of freej2me_plus.jar (local; no Maven Central stub)

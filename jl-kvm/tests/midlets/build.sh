#!/usr/bin/env bash
# build.sh MIDLET_DIR OUT.jar   : compiles MIDLET_DIR/src together with tests/midlets/framework/src (CLDC 1.1, MIDP 2.0,
# -source 1.3 + ProGuard preverify) using the manifest MIDLET_DIR/MANIFEST.MF.
set -euo pipefail
D="$(cd "$(dirname "$0")" && pwd)"; M="$(realpath "$1")"; OUT="$2"
W="$(mktemp -d)"; trap 'rm -rf "$W"' EXIT
mkdir -p "$W/src"; cp -r "$D/framework/src/." "$W/src/"; cp -r "$M/src/." "$W/src/"
# MIDlets that use JSR-135 interfaces (MIDIControl...) have a file `jsr135` next to MANIFEST.MF: their stubs are the interfaces of vm-classlib/jsr135/src
if [ -f "$M/jsr135" ]; then
  . "$D/../../tools/env/lib.sh"; . "$D/../../tools/env/versions.sh"
  mkdir -p "$W/j135"
  "$JDK8_HOME/bin/javac" -nowarn -source 1.3 -target 1.3 -bootclasspath "$CACHE/maven/cldcapi11-$MICROEMU_VER.jar:$CACHE/maven/midpapi20-$MICROEMU_VER.jar" -extdirs "" \
    -d "$W/j135" $(find "$D/../../vm-classlib/jsr135/src/javax" -name '*.java') 2>&1 | grep -v '^Picked up\|warning' || true
  export J2MEC_EXTRA="$W/j135"
fi
# MIDlets that use the Nokia UI API (FullCanvas...) have a file `nokiaui` next to MANIFEST.MF: compile against the MicroEmulator Nokia UI stub (compile time only)
if [ -f "$M/nokiaui" ]; then
  . "$D/../../tools/env/lib.sh"; . "$D/../../tools/env/versions.sh"
  export J2MEC_EXTRA="${J2MEC_EXTRA:+$J2MEC_EXTRA:}$(mvn_fetch org.microemu microemu-nokiaui "$MICROEMU_VER")"
fi
# T32a: files `wma`, `jsr75-fc`, `jsr75-pim` next to MANIFEST.MF add the stubs of those APIs (MicroEmulator jsr-120 / jsr-75 jars: compile time only; PIM: the interfaces of this project's own ROM layer)
. "$D/../../tools/env/lib.sh"; . "$D/../../tools/env/versions.sh"
[ -f "$M/wma" ] && export J2MEC_EXTRA="${J2MEC_EXTRA:+$J2MEC_EXTRA:}$(mvn_fetch org.microemu microemu-jsr-120 "$MICROEMU_VER")"
[ -f "$M/jsr75-fc" ] && export J2MEC_EXTRA="${J2MEC_EXTRA:+$J2MEC_EXTRA:}$(mvn_fetch org.microemu microemu-jsr-75 "$MICROEMU_VER")"
[ -f "$M/jsr75-pim" ] && export J2MEC_EXTRA="${J2MEC_EXTRA:+$J2MEC_EXTRA:}$D/../../vm-classlib/build/rom/rom-jsr75-pim.jar"
"$D/../../tools/env/j2mec.sh" "$W/src" "$M/MANIFEST.MF" "$OUT" "${3:-cldc11}"

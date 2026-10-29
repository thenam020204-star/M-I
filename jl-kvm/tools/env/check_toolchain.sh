#!/usr/bin/env bash
# Prints toolchain versions; exit 0 only if everything required is present. Runs setup if --setup.
# Also performs the T02 proof (hello MIDlet -source 1.3 -target 1.3 + preverify -> StackMap) into proof.txt.
. "$(dirname "$0")/lib.sh"; . "$ENV_DIR/versions.sh"
[ "${1:-}" = "--setup" ] && "$ENV_DIR/setup_toolchain.sh"
M="$CACHE/maven"; fail=0
ok()  { printf '%-22s %s\n' "$1" "$2"; }
bad() { printf '%-22s MISSING (%s)\n' "$1" "$2"; fail=1; }
chk() { [ -s "$2" ] && ok "$1" "$3" || bad "$1" "$2"; }
nopick() { grep -v '^Picked up' ; }

ok "JDK (gradle/host)" "$(java -version 2>&1 | nopick | head -1)"
if [ -x "$JDK8_HOME/bin/javac" ]; then ok "JDK 8 javac" "$("$JDK8_HOME/bin/javac" -version 2>&1 | nopick) ($JDK8_HOME; supports -source 1.3 -target 1.3)"; else bad "JDK 8" "$JDK8_HOME"; fi
chk "ECJ (fallback)" "$M/ecj-$ECJ_VER.jar" "ecj $ECJ_VER"
ok  "javap"          "$(command -v javap) (JDK21), $JDK8_HOME/bin/javap"
chk "ProGuard preverify" "$M/proguard-base-$PROGUARD_VER.jar" "proguard $PROGUARD_VER -microedition (replaces WTK preverify, PLAN V1)"
chk "CLDC 1.0 stub"  "$M/cldcapi10-$MICROEMU_VER.jar" "microemu cldcapi10 $MICROEMU_VER"
chk "CLDC 1.1 stub"  "$M/cldcapi11-$MICROEMU_VER.jar" "microemu cldcapi11 $MICROEMU_VER"
chk "MIDP 2.0 stub"  "$M/midpapi20-$MICROEMU_VER.jar" "microemu midpapi20 $MICROEMU_VER"
chk "ASM"            "$M/asm-$ASM_VER.jar" "asm $ASM_VER"
chk "ASM util"       "$M/asm-util-$ASM_VER.jar" "asm-util $ASM_VER (Textifier)"
chk "CFR"            "$M/cfr-$CFR_VER.jar" "cfr $CFR_VER"
chk "FernFlower"     "$M/fernflower-$FERNFLOWER_VER.jar" "fernflower $FERNFLOWER_VER"
chk "Procyon"        "$CACHE/procyon-decompiler-$PROCYON_VER.jar" "procyon $PROCYON_VER"
chk "JADX"           "$CACHE/jadx/bin/jadx" "jadx $JADX_VER"
chk "Jasmin"         "$M/jasmin-$JASMIN_VER-jar-with-dependencies.jar" "jasmin $JASMIN_VER"
[ $fail = 0 ] || { echo "toolchain INCOMPLETE (run tools/env/setup_toolchain.sh)"; exit 1; }

# --- proof: compile + preverify, then dump
P="$ENV_DIR/proof.txt"; mkdir -p "$CACHE/out" "$CACHE/x"
"$ENV_DIR/j2mec.sh" "$ROOT_DIR/tests/midlets/hello/src" "$ROOT_DIR/tests/midlets/hello/MANIFEST.MF" "$CACHE/out/hello.jar" cldc11 2>&1 | nopick || exit 1
javac -d "$CACHE/x" "$ENV_DIR/DumpAttrs.java" 2>&1 | nopick
{
  echo "# T02 proof: hello MIDlet, javac(JDK8) -source 1.3 -target 1.3, ProGuard -microedition"
  echo "## class version + Code sub-attributes (StackMap must be present on methods with branches)"
  java -cp "$CACHE/x" DumpAttrs "$CACHE/out/hello.jar" 2>&1 | nopick
  echo "## javap -v (JDK8) excerpt"
  rm -rf "$CACHE/out/hx"; mkdir -p "$CACHE/out/hx"; (cd "$CACHE/out/hx" && unzip -qo ../hello.jar)
  "$JDK8_HOME/bin/javap" -v -p -cp "$CACHE/out/hx" hello.HelloMIDlet 2>&1 | nopick | grep -E "major version|StackMap|number_of_entries|frame_type|Method |startApp" | head -20
} > "$P"
cat "$P"
grep -q 'StackMap=' "$P" && grep -q 'version=47.0' "$P" || { echo "PROOF FAILED: no StackMap / wrong version"; exit 1; }
echo "toolchain OK"

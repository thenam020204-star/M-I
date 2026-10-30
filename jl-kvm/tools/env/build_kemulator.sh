#!/usr/bin/env bash
# build_kemulator.sh -> builds KEmulator nnmod (https://github.com/shinovon/KEmulator, licence unknown: local oracle only, never committed - T05b)
# from the gitignored clone tools/env/cache/oracles/KEmulator into tools/env/cache/kemulator/ (KEmulator.jar + the runtime files of its home/ dir).
# The upstream build is an IntelliJ artifact (build.xml only stamps the version), so this script does what the KEmulator_x64 artifact does with
# plain javac + jar: compile src/{main,media,midp,nnapi,nokia,oem,3d} + x64/src + m3g_lwjgl + micro3d_gl (JDK 21 javac; JDK 17+ works),
# merge lib/*.jar (non-Windows) and src/res into KEmulator.jar with the manifest of src/main/META-INF. SWT (GTK x86_64), the LWJGL natives
# and the language files come from the clone's home/ dir, and KEmulator loads them from the directory of KEmulator.jar at start.
# Compile-time only: home/swt-win32-x86_64.jar (the source uses the Windows-only FontData.data; never loaded on Linux).
set -euo pipefail
. "$(dirname "$0")/lib.sh"
SRC="$CACHE/oracles/KEmulator"; OUT="$CACHE/kemulator"; JAVAC="${KEM_JAVAC:-javac}"; JAR="${KEM_JAR_TOOL:-jar}"
[ -d "$SRC/src/main" ] || { echo "missing $SRC (git clone --depth 1 https://github.com/shinovon/KEmulator $SRC)" >&2; exit 2; }
command -v xvfb-run >/dev/null || echo "WARNING: xvfb-run missing (apt install xvfb): run_kemulator.sh needs it" >&2
[ -f "$OUT/KEmulator.jar" ] && { echo "KEmulator ready: $OUT/KEmulator.jar"; exit 0; }
W="$(mktemp -d)"; trap 'rm -rf "$W"' EXIT
cd "$SRC"
CP="$SRC/home/swt-win32-x86_64.jar:$(ls "$SRC"/lib/*.jar | grep -v win32 | tr '\n' ':')"
find src/main src/media src/midp src/nnapi src/nokia src/oem src/3d x64/src/main m3g_lwjgl/src micro3d_gl/src -name '*.java' > "$W/srcs.txt"
mkdir -p "$W/cls"
env -u JAVA_TOOL_OPTIONS "$JAVAC" -nowarn -encoding UTF-8 -proc:none --add-exports java.desktop/com.sun.media.sound=ALL-UNNAMED \
  -cp "$CP" -d "$W/cls" @"$W/srcs.txt" > "$W/javac.log" 2>&1 || { grep -A3 'error:' "$W/javac.log" | head -30 >&2; echo "KEmulator javac failed (log kept in $CACHE/kemulator-javac.log)" >&2; cp "$W/javac.log" "$CACHE/kemulator-javac.log"; exit 1; }
cp -r src/res/. "$W/cls/"
cp -r src/main/META-INF "$W/cls/" 2>/dev/null || true
for j in $(ls lib/*.jar | grep -v win32 | grep -v 'swt.*source'); do (cd "$W/cls" && unzip -qo "$SRC/$j" -x "META-INF/*.SF" "META-INF/*.RSA" "META-INF/*.DSA" "META-INF/MANIFEST.MF" 2>/dev/null || true); done
printf 'Manifest-Version: 1.0\nMain-Class: emulator.Emulator\nPremain-Class: emulator.Agent\nLauncher-Agent-Class: emulator.Agent\n' > "$W/mf.txt"
mkdir -p "$OUT"
( cd "$W/cls" && "$JAR" cfm "$OUT/KEmulator.jar" "$W/mf.txt" . )
# runtime files next to the jar (SWT GTK x86_64, LWJGL natives, language files); no Windows binaries
( cd "$SRC/home" && cp -r lang lwjgl-*natives-linux.jar lwjgl3-swt-linux.jar swt-gtk-linux-x86_64.jar libjinput-linux64.so "$OUT/" 2>/dev/null; cp -r lang "$OUT/" )
echo "KEmulator ready: $OUT/KEmulator.jar"

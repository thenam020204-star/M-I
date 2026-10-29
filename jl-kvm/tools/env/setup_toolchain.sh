#!/usr/bin/env bash
# Downloads every tool into tools/env/cache (git-ignored) or /opt. Idempotent.
set -euo pipefail
. "$(dirname "$0")/lib.sh"; . "$ENV_DIR/versions.sh"

# --- JDK 8 (Temurin): javac supports -source 1.3 -target 1.3 (with obsolete warnings)
if [ ! -x "$JDK8_HOME/bin/javac" ]; then
  fetch "https://api.adoptium.net/v3/binary/latest/8/ga/linux/x64/jdk/hotspot/normal/eclipse" "$CACHE/jdk8.tgz"
  mkdir -p "$JDK8_HOME"; tar xzf "$CACHE/jdk8.tgz" -C "$JDK8_HOME" --strip-components=1
fi

for m in asm asm-tree asm-util asm-analysis asm-commons; do mvn_fetch org.ow2.asm $m $ASM_VER >/dev/null; done
mvn_fetch org.benf cfr $CFR_VER >/dev/null
mvn_fetch org.jboss.windup.decompiler.fernflower fernflower $FERNFLOWER_VER >/dev/null
mvn_fetch ca.mcgill.sable jasmin $JASMIN_VER jar-with-dependencies >/dev/null
mvn_fetch org.eclipse.jdt ecj $ECJ_VER >/dev/null || true   # optional fallback compiler

# CLDC/MIDP API stubs (MicroEmulator, Apache-2.0) - used as -bootclasspath and as API-signature source (PLAN V1)
for m in cldcapi10 cldcapi11 midpapi20; do mvn_fetch org.microemu $m $MICROEMU_VER >/dev/null; done

# ProGuard (provides -microedition preverifier; replaces WTK preverify, see PLAN V1) - from Maven Central
mvn_fetch com.guardsquare proguard-base $PROGUARD_VER >/dev/null
mvn_fetch com.guardsquare proguard-core $PROGUARD_CORE_VER >/dev/null
mvn_fetch org.jetbrains.kotlin kotlin-stdlib $KOTLIN_STDLIB_VER >/dev/null
mvn_fetch com.google.code.gson gson $GSON_VER >/dev/null
mvn_fetch org.apache.logging.log4j log4j-api $LOG4J_VER >/dev/null
mvn_fetch org.apache.logging.log4j log4j-core $LOG4J_VER >/dev/null
mvn_fetch org.json json $JSON_VER >/dev/null
fetch "https://github.com/mstrobel/procyon/releases/download/v$PROCYON_VER/procyon-decompiler-$PROCYON_VER.jar" "$CACHE/procyon-decompiler-$PROCYON_VER.jar"
if [ ! -x "$CACHE/jadx/bin/jadx" ]; then
  fetch "https://github.com/skylot/jadx/releases/download/v$JADX_VER/jadx-$JADX_VER.zip" "$CACHE/jadx.zip"
  mkdir -p "$CACHE/jadx"; unzip -qo "$CACHE/jadx.zip" -d "$CACHE/jadx"; chmod +x "$CACHE/jadx/bin/"*
fi
echo "toolchain ready in $CACHE (JDK8 at $JDK8_HOME)"

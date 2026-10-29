#!/usr/bin/env bash
# Shared helpers for oracle runners (PLAN T05). Source this file.
ORA_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$ORA_DIR/../../.." && pwd)"
. "$ROOT_DIR/tools/env/lib.sh"
. "$ROOT_DIR/tools/env/versions.sh"
FREEJ2ME_JAR="${FREEJ2ME_JAR:-$ROOT_DIR/../freej2me/freej2me_plus.jar}"
MICROEMU_JAR="${MICROEMU_JAR:-$CACHE/maven/microemulator-$MICROEMU_VER.jar}"
ORACLE_TIMEOUT="${ORACLE_TIMEOUT:-60}"
# phoneME is built locally by build_phoneme.sh into the (gitignored) cache; never committed (GPLv2, D12).
PHONEME_DIR="${PHONEME_DIR:-$CACHE/phoneme}"
# JAVA_TOOL_OPTIONS adds "Picked up ..." noise to stderr and proxies that oracles do not need.
oracle_java() { env -u JAVA_TOOL_OPTIONS "$@"; }

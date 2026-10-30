#!/usr/bin/env bash
# run_vmspec.sh [--record] [--keep DIR] [case_id ...]
#
# Builds every case in tests/vmspec/cases (javac -source 1.3 + ProGuard preverify, plus generated raw classes and
# res/), runs it on the oracles (tools/difftest/oracles) and compares the lines starting with "> " against
# expected.txt. Lines starting with "# " are informational and never compared.
#   --record   write expected.txt from the oracles when ALL oracles agree (else write oracle-diff.txt and flag the case)
#   --engine jlkvm   run on this project's VM (tools/difftest/oracles/run_jlkvm.sh, TEMPORARY stub ROM) instead of the oracles;
#                    spec-only cases are run too, `diverges:` is ignored (the VM must match expected.txt exactly)
#   --keep DIR keep jars and oracle outputs in DIR (default: build/vmspec under this directory)
# meta.txt keys (one "key: value" per line):
#   mode: oracle (default) | spec-only        spec-only = oracles cannot run it; expected.txt comes from the cited spec
#   source: where expected.txt comes from (oracle names, or spec citation)  [required]
#   diverges: microemu|freej2me|...          oracles known to deviate from expected.txt (reason in notes:)
#   jlkvm-args: extra options for run_jlkvm.sh (e.g. --noverify for jsr/ret classes)
#   driven-by-test: <test name>   the case needs a host driver (input events...): the jar is only built here, a vm-core test runs it
#   no-gc-stress: yes   do not add env JLKVM_ARGS (e.g. "--gc-stress 1 --gc-verify") to this case
#   notes: free text
# Exit status: 0 when every case passes, 1 otherwise.
set -uo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; ROOT="$HERE/../.."
. "$ROOT/tools/env/lib.sh"; . "$ROOT/tools/env/versions.sh"
ORA="$ROOT/tools/difftest/oracles"
ENGINE=oracle; RECORD=0; KEEP="$HERE/build/vmspec"; IDS=()
while [ $# -gt 0 ]; do
  case "$1" in
    --record) RECORD=1 ;;
    --keep) KEEP="$2"; shift ;;
    --engine) ENGINE="$2"; shift ;;
    *) IDS+=("$1") ;;
  esac; shift
done
mkdir -p "$KEEP"
if [ ${#IDS[@]} -eq 0 ]; then for d in "$HERE"/cases/*/; do IDS+=("$(basename "$d")"); done; fi
meta() { sed -n "s/^$2:[ ]*//p" "$HERE/cases/$1/meta.txt" 2>/dev/null | head -1; }
norm() { grep '^> ' || true; }

# regenerate raw bytecode classes (ASM) once
ASM="$CACHE/maven/asm-$ASM_VER.jar"
env -u JAVA_TOOL_OPTIONS java -cp "$ASM" "$HERE/gen/RawGen.java" "$HERE/cases" || { echo "RawGen failed" >&2; exit 2; }

pass=0; fail=0; flagged=0; specs=0
for id in "${IDS[@]}"; do
  C="$HERE/cases/$id"; W="$KEEP/$id"; rm -rf "$W"; mkdir -p "$W"
  [ -d "$C/src" ] || { echo "SKIP  $id (no src)"; continue; }
  mode="$(meta "$id" mode)"; mode="${mode:-oracle}"
  cldc="$(meta "$id" cldc)"; cldc="${cldc:-cldc11}"
  MF="$C/MANIFEST.MF"
  if [ ! -f "$MF" ]; then
    MAIN="$(grep -l 'extends MIDlet' -r "$C/src" | head -1)"; MAIN="$(basename "${MAIN%.java}")"
    MF="$W/MANIFEST.MF"
    printf 'MIDlet-Name: %s\nMIDlet-Version: 1.0.0\nMIDlet-Vendor: jl-kvm\nMIDlet-1: %s,,vmspec.%s\nMicroEdition-Configuration: CLDC-1.1\nMicroEdition-Profile: MIDP-2.0\n' "$id" "$id" "$MAIN" > "$MF"
  fi
  JAR="$W/$id.jar"
  if [ -x "$C/build.sh" ]; then
    "$C/build.sh" "$C" "$JAR" > "$W/build.log" 2>&1 || { echo "FAIL  $id (build.sh failed, see $W/build.log)"; fail=$((fail+1)); continue; }
  else
    "$ROOT/tools/env/j2mec.sh" "$C/src" "$MF" "$JAR" "$cldc" > "$W/build.log" 2>&1 || { echo "FAIL  $id (build failed, see $W/build.log)"; fail=$((fail+1)); continue; }
    [ -d "$C/raw" ] && (cd "$C/raw" && "$JDK8_HOME/bin/jar" uf "$JAR" . 2>&1 | grep -v '^Picked up')
    [ -d "$C/res" ] && (cd "$C/res" && "$JDK8_HOME/bin/jar" uf "$JAR" . 2>&1 | grep -v '^Picked up')
  fi
  MAINCLS="$(unzip -p "$JAR" META-INF/MANIFEST.MF | tr -d '\r' | sed -n 's/^MIDlet-1:[^,]*,[^,]*,[ ]*//p' | head -1)"
  if [ "$mode" = spec-only ] && [ "$ENGINE" != jlkvm ]; then
    if [ -s "$C/expected.txt" ] && [ -n "$(meta "$id" source)" ]; then echo "SPEC  $id (expected from: $(meta "$id" source))"; specs=$((specs+1))
    else echo "FAIL  $id (spec-only case needs expected.txt and meta source)"; fail=$((fail+1)); fi
    continue
  fi
  if [ -n "$(meta "$id" driven-by-test)" ]; then echo "BUILT $id (driven by a vm-core test: $(meta "$id" driven-by-test))"; continue; fi   # needs a host that posts input; only built here
  outs=()
  if [ "$ENGINE" = jlkvm ]; then
    o=jlkvm
    ja="${JLKVM_ARGS:-}"; [ -n "$(meta "$id" no-gc-stress)" ] && ja=""   # env JLKVM_ARGS="--gc-stress 1 --gc-verify": GC torture for the whole corpus
    JLKVM_ARGS="$ja" "$ORA/run_jlkvm.sh" "$JAR" $( [ "$cldc" = cldc10 ] && echo --cldc10 ) $(meta "$id" jlkvm-args) > "$W/$o.raw" 2> "$W/$o.err"
    norm < "$W/$o.raw" > "$W/$o.out"; outs+=("$o")
  else
  for o in microemu freej2me; do
    if [ $o = microemu ]; then "$ORA/run_microemu.sh" "$JAR" > "$W/$o.raw" 2> "$W/$o.err"
    else "$ORA/run_freej2me.sh" "$JAR" > "$W/$o.raw" 2> "$W/$o.err"; fi
    norm < "$W/$o.raw" > "$W/$o.out"; outs+=("$o")
  done
  fi
  div="$(meta "$id" diverges)"; [ "$ENGINE" = jlkvm ] && div=""
  if [ $RECORD -eq 1 ]; then
    # record from the oracles not listed in "diverges:" (all of them must agree and print something)
    ref=""; agree=1
    for o in "${outs[@]}"; do
      [[ "|$div|" == *"|$o|"* ]] && continue
      if [ -z "$ref" ]; then ref="$o"; elif ! cmp -s "$W/$ref.out" "$W/$o.out"; then agree=0; fi
    done
    if [ -n "$ref" ] && [ $agree -eq 1 ] && [ -s "$W/$ref.out" ]; then
      cp "$W/$ref.out" "$C/expected.txt"; rm -f "$C/oracle-diff.txt"
      echo "REC   $id ($(wc -l < "$C/expected.txt") lines, from ${ref}${div:+; known divergent: $div})"
    else
      diff -u "$W/microemu.out" "$W/freej2me.out" > "$C/oracle-diff.txt"
      echo "DIFF  $id oracles disagree or no output: see cases/$id/oracle-diff.txt"; flagged=$((flagged+1))
    fi
    continue
  fi
  [ -s "$C/expected.txt" ] || { echo "FAIL  $id (no expected.txt; run --record)"; fail=$((fail+1)); continue; }
  ok=1; msg=""
  for o in "${outs[@]}"; do
    if diff -q "$C/expected.txt" "$W/$o.out" >/dev/null; then msg="$msg $o=ok"
    elif [[ "|$div|" == *"|$o|"* ]]; then msg="$msg $o=known-diverge"
    else msg="$msg $o=MISMATCH"; ok=0; diff -u "$C/expected.txt" "$W/$o.out" > "$W/$o.diff"; fi
  done
  if [ $ok -eq 1 ]; then echo "PASS  $id [$msg ]"; pass=$((pass+1)); else echo "FAIL  $id [$msg ] (diffs in $W/*.diff)"; fail=$((fail+1)); fi
done
echo "summary: pass=$pass fail=$fail spec-only=$specs flagged=$flagged"
[ $fail -eq 0 ] && [ $flagged -eq 0 ]

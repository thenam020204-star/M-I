#!/usr/bin/env bash
# run_vmspec.sh [--record] [--keep DIR] [case_id ...]
#
# Builds every case in tests/vmspec/cases (javac -source 1.3 + ProGuard preverify, plus generated raw classes and
# res/), runs it on the oracles (tools/difftest/oracles) and compares the lines starting with "> " against
# expected.txt. Lines starting with "# " are informational and never compared.
#   --record   write expected.txt from the oracles when ALL oracles agree (else write oracle-diff.txt and flag the case)
#   --engine jlkvm   run on this project's VM (tools/difftest/oracles/run_jlkvm.sh, TEMPORARY stub ROM) instead of the oracles;
#                    spec-only cases are run too, `diverges:` is ignored (the VM must match expected.txt exactly)
#   --oracle NAME  add an oracle to the default pair microemu+freej2me (NAME = phoneme | kemulator, T05b; may be repeated; ignored with --engine jlkvm).
#                  An oracle that is not built (tools/env/build_phoneme.sh, build_kemulator.sh) is skipped with a notice, never a FAIL. Output that
#                  shows the oracle lacks an API (an uncaught/startApp NoClassDefFoundError of a javax.* class or java.util.Timer, printed outside the "> " lines) counts as "unsupported", not as a mismatch.
#                  Env VMSPEC_ORACLES="microemu freej2me phoneme" replaces the whole list. --record only ever uses microemu+freej2me.
#   --probe    (oracle mode) also run the spec-only cases on the oracles, for information: PROBE lines, never counted as a failure (T05b: re-check what
#              the extra oracles say about cases whose expected.txt comes from the spec, e.g. rms01, mm01, nk01, wma*, fc*, pim*)
#   --keep DIR keep jars and oracle outputs in DIR (default: build/vmspec under this directory)
# meta.txt keys (one "key: value" per line):
#   mode: oracle (default) | spec-only        spec-only = oracles cannot run it; expected.txt comes from the cited spec
#   source: where expected.txt comes from (oracle names, or spec citation)  [required]
#   diverges: microemu|freej2me|...          oracles known to deviate from expected.txt (reason in notes:)
#   jlkvm-args: extra options for run_jlkvm.sh (e.g. --noverify for jsr/ret classes)
#   driven-by-test: <test name>   the case needs a host driver (input events...): the jar is only built here, a vm-core test runs it
#   no-gc-stress: yes   do not add env JLKVM_ARGS (e.g. "--gc-stress 1 --gc-verify") to this case
#   realtime: yes   the case measures wall-clock time: the in-process vm-core VmSpecTest skips it; --engine jlkvm runs it with --deterministic (D14 virtual
#                   clock, T22a-f1) so the result does not depend on machine load (the real-time oracles still depend on it)
#   stubs: nokia-ui wma jsr75-fc jsr75-pim   (any of them) compile also against that API stub (MicroEmulator microemu-nokiaui / jsr-120 / jsr-75 jars, compile-time only;
#                    jsr75-pim = vm-classlib/build/rom/rom-jsr75-pim.jar of this project; the VM uses its own ROM layers)
#   (file profile.json in the case directory: --engine jlkvm runs the case with that device profile, e.g. sms.mode)
#   notes: free text
# Exit status: 0 when every case passes, 1 otherwise.
set -uo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; ROOT="$HERE/../.."
. "$ROOT/tools/env/lib.sh"; . "$ROOT/tools/env/versions.sh"
ORA="$ROOT/tools/difftest/oracles"
ENGINE=oracle; RECORD=0; PROBE=0; KEEP="$HERE/build/vmspec"; IDS=()
while [ $# -gt 0 ]; do
  case "$1" in
    --record) RECORD=1 ;;
    --keep) KEEP="$2"; shift ;;
    --engine) ENGINE="$2"; shift ;;
    --probe) PROBE=1 ;;
    --oracle) EXTRA_ORA="${EXTRA_ORA:-} $2"; shift ;;
    *) IDS+=("$1") ;;
  esac; shift
done
ORACLES="${VMSPEC_ORACLES:-microemu freej2me${EXTRA_ORA:-}}"
[ $RECORD -eq 1 ] && ORACLES="microemu freej2me"
# an extra oracle that is not built is dropped once, with a notice
for o in $ORACLES; do case $o in
  phoneme) [ -x "${PHONEME_DIR:-$CACHE/phoneme}/bin/cldc_vm" ] || { echo "NOTE  oracle phoneme not built (tools/env/build_phoneme.sh): skipped"; ORACLES="${ORACLES/phoneme/}"; } ;;
  kemulator) [ -f "${KEMULATOR_DIR:-$CACHE/kemulator}/KEmulator.jar" ] || { echo "NOTE  oracle kemulator not built (tools/env/build_kemulator.sh): skipped"; ORACLES="${ORACLES/kemulator/}"; } ;;
esac; done
mkdir -p "$KEEP"; [ "$ENGINE" = jlkvm ] || : > "$KEEP/results.tsv"   # results.tsv: case<TAB>oracle<TAB>ok|known-diverge|unsupported|MISMATCH
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
  J2MEC_EXTRA=""
  for st in $(meta "$id" stubs); do
    case "$st" in
      nokia-ui) j="$(mvn_fetch org.microemu microemu-nokiaui "$MICROEMU_VER")" ;;
      wma) j="$(mvn_fetch org.microemu microemu-jsr-120 "$MICROEMU_VER")" ;;
      jsr75-fc) j="$(mvn_fetch org.microemu microemu-jsr-75 "$MICROEMU_VER")" ;;
      jsr75-pim) j="$ROOT/vm-classlib/build/rom/rom-jsr75-pim.jar" ;;   # no open-source PIM stub on Maven Central: this project's own interfaces (checked against jsr75-pim.json by romgen)
      *) echo "unknown stubs: $st" >&2; j="" ;;
    esac
    J2MEC_EXTRA="${J2MEC_EXTRA:+$J2MEC_EXTRA:}$j"
  done
  if [ -n "$J2MEC_EXTRA" ]; then export J2MEC_EXTRA; else unset J2MEC_EXTRA; fi
  JAR="$W/$id.jar"
  if [ -x "$C/build.sh" ]; then
    "$C/build.sh" "$C" "$JAR" > "$W/build.log" 2>&1 || { echo "FAIL  $id (build.sh failed, see $W/build.log)"; fail=$((fail+1)); continue; }
  else
    "$ROOT/tools/env/j2mec.sh" "$C/src" "$MF" "$JAR" "$cldc" > "$W/build.log" 2>&1 || { echo "FAIL  $id (build failed, see $W/build.log)"; fail=$((fail+1)); continue; }
    [ -d "$C/raw" ] && (cd "$C/raw" && "$JDK8_HOME/bin/jar" uf "$JAR" . 2>&1 | grep -v '^Picked up')
    [ -d "$C/res" ] && (cd "$C/res" && "$JDK8_HOME/bin/jar" uf "$JAR" . 2>&1 | grep -v '^Picked up')
  fi
  MAINCLS="$(unzip -p "$JAR" META-INF/MANIFEST.MF | tr -d '\r' | sed -n 's/^MIDlet-1:[^,]*,[^,]*,[ ]*//p' | head -1)"
  # a case driven by a host test (input events...) is only built here; in oracle mode it has no expected.txt of its own (the test holds the
  # expectation), so it must not fall into the expected.txt checks below. A spec-only one must still cite its source.
  if [ -n "$(meta "$id" driven-by-test)" ] && [ "$ENGINE" != jlkvm ]; then
    if [ "$mode" = spec-only ]; then
      if [ -n "$(meta "$id" source)" ]; then echo "SPEC  $id (driven by a host test: $(meta "$id" driven-by-test); expected from: $(meta "$id" source))"; specs=$((specs+1))
      else echo "FAIL  $id (spec-only case needs a meta source)"; fail=$((fail+1)); fi
    else echo "BUILT $id (driven by a host test: $(meta "$id" driven-by-test))"; fi
    continue
  fi
  if [ "$mode" = spec-only ] && [ "$ENGINE" != jlkvm ] && [ $PROBE -eq 0 ]; then
    if [ -s "$C/expected.txt" ] && [ -n "$(meta "$id" source)" ]; then echo "SPEC  $id (expected from: $(meta "$id" source))"; specs=$((specs+1))
    else echo "FAIL  $id (spec-only case needs expected.txt and meta source)"; fail=$((fail+1)); fi
    continue
  fi
  if [ -n "$(meta "$id" driven-by-test)" ]; then echo "BUILT $id (driven by a vm-core test: $(meta "$id" driven-by-test))"; continue; fi   # needs a host that posts input; only built here
  outs=()
  if [ "$ENGINE" = jlkvm ]; then
    o=jlkvm
    ja="${JLKVM_ARGS:-}"; [ -n "$(meta "$id" no-gc-stress)" ] && ja=""   # env JLKVM_ARGS="--gc-stress 1 --gc-verify": GC torture for the whole corpus
    JLKVM_ARGS="$ja" "$ORA/run_jlkvm.sh" "$JAR" $( [ "$cldc" = cldc10 ] && echo --cldc10 ) $(meta "$id" jlkvm-args) $( [ -n "$(meta "$id" realtime)" ] && echo --deterministic ) $( [ -f "$C/profile.json" ] && echo --profile-json "$C/profile.json" ) > "$W/$o.raw" 2> "$W/$o.err"
    norm < "$W/$o.raw" > "$W/$o.out"; outs+=("$o")
  else
  for o in $ORACLES; do
    "$ORA/run_$o.sh" "$JAR" > "$W/$o.raw" 2> "$W/$o.err"
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
    if diff -q "$C/expected.txt" "$W/$o.out" >/dev/null; then st=ok
    elif [[ "|$div|" == *"|$o|"* ]]; then st=known-diverge
    elif [ "$o" != microemu ] && [ "$o" != freej2me ] && grep -v '^> ' "$W/$o.raw" | grep -qE 'NoClassDefFoundError: (javax[./]|java/util/Timer)|ClassNotFoundException: javax'; then st=unsupported
    else st=MISMATCH; ok=0; diff -u "$C/expected.txt" "$W/$o.out" > "$W/$o.diff"; fi
    msg="$msg $o=$st"; printf '%s\t%s\t%s\n' "$id" "$o" "$st" >> "$KEEP/results.tsv"
  done
  if [ "$mode" = spec-only ] && [ "$ENGINE" != jlkvm ]; then echo "PROBE $id [$msg ] (spec-only case run on the oracles for information)"
  elif [ $ok -eq 1 ]; then echo "PASS  $id [$msg ]"; pass=$((pass+1)); else echo "FAIL  $id [$msg ] (diffs in $W/*.diff)"; fail=$((fail+1)); fi
done
echo "summary: pass=$pass fail=$fail spec-only=$specs flagged=$flagged"
[ $fail -eq 0 ] && [ $flagged -eq 0 ]

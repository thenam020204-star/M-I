#!/usr/bin/env bash
# run_all.sh GAME.jar [OUT_DIR]  -> runs every available oracle, writes OUT_DIR/<oracle>.out, compares them.
# Exit 0 when >= 2 oracles ran and all outputs are identical; 1 when they differ; 2 when fewer than 2 oracles ran.
set -uo pipefail
D="$(cd "$(dirname "$0")" && pwd)"
JAR="$(realpath "$1")"; OUT="${2:-$(mktemp -d)}"; mkdir -p "$OUT"
ran=()
run() { # name script...
  local n="$1"; shift
  if "$@" > "$OUT/$n.out" 2> "$OUT/$n.err"; then ran+=("$n"); else echo "oracle $n failed (rc=$?), see $OUT/$n.err" >&2; fi
}
run microemu "$D/run_microemu.sh" "$JAR"
run freej2me "$D/run_freej2me.sh" "$JAR"
[ -x "$D/run_phoneme.sh" ] && [ -d "${PHONEME_DIR:-$D/../../env/cache/phoneme}" ] && run phoneme "$D/run_phoneme.sh" "$JAR"
[ -x "$D/run_kemulator.sh" ] && [ -f "${KEMULATOR_DIR:-$D/../../env/cache/kemulator}/KEmulator.jar" ] && run kemulator "$D/run_kemulator.sh" "$JAR"
echo "oracles ran: ${ran[*]:-none}"
[ ${#ran[@]} -ge 2 ] || { echo "need >= 2 oracles" >&2; exit 2; }
rc=0
for ((i = 1; i < ${#ran[@]}; i++)); do
  if diff -u "$OUT/${ran[0]}.out" "$OUT/${ran[$i]}.out" > "$OUT/diff-${ran[0]}-${ran[$i]}.txt"; then
    echo "SAME: ${ran[0]} == ${ran[$i]}"
  else
    echo "DIFF: ${ran[0]} != ${ran[$i]} (see $OUT/diff-${ran[0]}-${ran[$i]}.txt)"; rc=1
  fi
done
exit $rc

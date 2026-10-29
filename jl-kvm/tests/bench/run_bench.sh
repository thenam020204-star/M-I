#!/usr/bin/env bash
# run_bench.sh [workload ...]   (T13b) throughput of the interpreter in million bytecodes per second (Mbc/s) on the desktop JVM.
# Builds tests/bench/src with the J2ME toolchain, runs each workload on the project VM (3 measured runs after a warm-up) with
# the temporary stub ROM and a 4 MB heap, then the same code directly on the host JVM (JDK 8) as the reference for the
# interpretation overhead. Results go to stdout; copy the table into docs/bench.md.
# Env: BENCH_REPS (default 3), BENCH_HEAP_KB (default 4096), JLKVM_JAVA (java used for the VM, default JDK 8 from tools/env),
#      JLKVM_JAVA_OPTS (e.g. -XX:-DontCompileHugeMethods: HotSpot does not JIT a method over 8000 bytecodes and Interpreter.run is 9.7 KB).
set -uo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; ROOT="$HERE/../.."
. "$ROOT/tools/env/lib.sh"; . "$ROOT/tools/env/versions.sh"
REPS="${BENCH_REPS:-3}"; HEAP="${BENCH_HEAP_KB:-4096}"
mkdir -p "$HERE/build"
MF="$HERE/build/MANIFEST.MF"
printf 'MIDlet-Name: bench\nMIDlet-Version: 1.0.0\nMIDlet-Vendor: jl-kvm\nMIDlet-1: bench,,bench.BenchMain\nMicroEdition-Configuration: CLDC-1.1\nMicroEdition-Profile: MIDP-2.0\n' > "$MF"
"$ROOT/tools/env/j2mec.sh" "$HERE/src" "$MF" "$HERE/build/bench.jar" cldc11 > "$HERE/build/build.log" 2>&1 || { cat "$HERE/build/build.log"; exit 2; }
# workload:scale  (scale chosen so that a run takes about 1-3 s on the project VM)
ALL="arith:20 long:20 calls:20 arrays:20 alloc:20 strings:20 fp:20"
if [ $# -gt 0 ]; then SEL=""; for w in "$@"; do for e in $ALL; do [ "${e%%:*}" = "$w" ] && SEL="$SEL $e"; done; done; ALL="$SEL"; fi
echo "# jlkvm interpreter, $(env -u JAVA_TOOL_OPTIONS ${JLKVM_JAVA:-$JDK8_HOME/bin/java} ${JLKVM_JAVA_OPTS:-} -version 2>&1 | grep -v Picked | head -1), heap ${HEAP} KB, reps ${REPS}"
for e in $ALL; do
  w="${e%%:*}"; n="${e##*:}"
  JLKVM_MAIN=jlkvm.tmp.Bench "$ROOT/tools/difftest/oracles/run_jlkvm.sh" "$HERE/build/bench.jar" "$w" "$n" "$REPS" "$HEAP" 2>&1 | grep -v "^\[" 
  M="$CACHE/maven"; CP="$HERE/build/bench.jar:$M/cldcapi11-$MICROEMU_VER.jar:$M/midpapi20-$MICROEMU_VER.jar"
  mkdir -p "$HERE/build/native"; env -u JAVA_TOOL_OPTIONS "$JDK8_HOME/bin/javac" -nowarn -d "$HERE/build/native" "$HERE/NativeRef.java" 2>&1 | grep -v "^Picked\|^Note"
  env -u JAVA_TOOL_OPTIONS "$JDK8_HOME/bin/java" -cp "$HERE/build/native:$CP" NativeRef "$w" "$n" 2>&1 | grep -v "^Picked"
done

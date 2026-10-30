# Debug and trace hooks (PLAN T16, D14, D15)

## Compile-time switch (D15)
Every hook is written `if (DebugFlags.ENABLED && ...)` (`vm-core/src/main/java/jlkvm/debug/DebugFlags.java`, `ENABLED = true`).
The release build compiles `vm-core/src/release/java/jlkvm/debug/DebugFlags.java` (`ENABLED = false`) instead, so javac drops the
hook statements: the bytecode of `Interpreter.run()` is identical to the code without hooks except for 4 bytes (one extra argument
of a helper call). Gradle: `-Pjlkvm.release=true`; scripts: `JLKVM_RELEASE=1 tools/difftest/oracles/run_jlkvm.sh ...`
(separate class dir `vm-core/build/jlkvm-cli-release`). The unit tests of T14/T15/T16 need the debug build.

## What a debug VM can do (`VmConfig`, `RunJar` options in brackets)
| Feature | Switch | Output |
|---|---|---|
| class load log, missing native at class load, GC line per collection, deadlock / stall report | `debug` [`--debug`] | host.log tags `link`, `gc`, `vm` |
| exceptions with guest stack at the throw | `logExceptions` [`--log-exceptions`] | tag `exception` |
| bytecodes executed per method | `countBytecodes` [`--profile`] | `Debug.report(n)`: top methods |
| allocation sites by pc (class, count, bytes, source line) | `profileAlloc` [`--profile`] | `Debug.report(n)`: top sites |
| ring buffer of the last N instructions (thread, method, pc, opcode) | `traceRing` = N [`--trace-ring N`] | dumped (24 entries) before an uncaught exception, `Debug.ringText(n)` |
| deterministic mode: virtual clock (`nanosPerBytecode` per bytecode, jumps to the next timer when all threads wait), fixed epoch, fixed `hashSeed` | `deterministic` [`--deterministic`, `--seed N`] | same schedule on every run |
| record / replay of the input events | `recordInput` / `replayInput` file [`--record F`, `--replay F`] | lines `virtualNanos type a b c` |

Record/replay (D14): needs the deterministic mode. The guest takes events through `Vm.pollEvent()` (the EventLoop native of T22);
the log holds the virtual time of each take. A replaying VM ignores `postEvent` and hands the same events to the guest at the same
virtual times (when every thread waits, the clock jumps to the next timer or recorded event), so the run is reproduced exactly
(vm-core `InputReplayTest`). While recording, a waiting VM runs ahead in virtual time (timers do not wait for real time).

## Cost (T16 acceptance: release within 2% of "no hooks at all")
A/B on the desktop, JDK 8, 7 workloads x 3 rounds (best of 4 runs each, interleaved), Mbc/s:
code before T16 (no hook sites) vs release build (hooks compiled out) vs debug build (hooks compiled in, every feature off).
Geometric mean over the workloads of release/base: **1.0045 (best-of) / 0.9943 (mean)**, i.e. within +-0.6% (noise); debug/base: 0.962 / 0.954.
Raw table: see the Nhật ký entry of T16 in PLAN.md.

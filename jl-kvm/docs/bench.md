# Interpreter benchmark (PLAN T13b, gate of D2)

**Status: desktop numbers only. The Android part of T13b is NOT measured (no Android device in the build container), so T13b stays
unticked; the decision "keep the Java core or open the C++ branch" (D2/R1) is the manager's, see the findings at the end.**

## What is measured

`tests/bench/run_bench.sh [workload...]` builds `tests/bench/src/bench/BenchMain.java` (J2ME, `-source 1.3`, preverified) and runs each
workload on the project VM (`jlkvm.tmp.Bench`, temporary stub ROM, 4 MB device heap, strict verification): one warm-up run (JIT of the host
JVM) and three measured runs, best reported, in **million executed bytecodes per second (Mbc/s)** (`Scheduler.bytecodes`, natives'
charges included). The same Java code is then run directly on the host JVM (`NativeRef`) and the checksums are compared: all seven
checksums are identical on the VM and on HotSpot, so the interpreter computes the right results.

| Workload | Content | Bytecodes per run |
|---|---|---|
| arith | int loop: mul/add/xor/shift/branches | 65.0 M |
| long | long mul/xor/shift/compare | 58.0 M |
| calls | virtual (3 receivers), interface (2), static calls, field access | 41.6 M |
| arrays | int[]/byte[]/char[]/int[][] fill + sum | 79.1 M |
| alloc | `new` of small objects and arrays, short lists, 4 MB heap (5 collections per run) | 46.2 M |
| strings | StringBuffer append/toString/charAt/indexOf in the guest library (15 collections) | 141.5 M |
| fp | float arithmetic | 46.0 M |

Machine: Intel Xeon 2.1 GHz (4 vCPU, container), JDK 8u504 and JDK 21. One thread; the vm-thread is the only Java thread that works.

## Results (Mbc/s, best of 3)

| Workload | JDK 8, default flags | JDK 21, default flags | JDK 8, `-XX:-DontCompileHugeMethods` | JDK 21, `-XX:-DontCompileHugeMethods` | host JVM running the Java code directly (ms) |
|---|---|---|---|---|---|
| arith | 22.8 | 21.4 | 126.8 | 143.7 | 4 |
| long | 13.9 | 13.1 | 111.2 | 109.1 | 12 |
| calls | 13.7 | 13.0 | 96.3 | 88.1 | 5 |
| arrays | 21.7 | 19.9 | 130.0 | 143.2 | 2 |
| alloc | 13.4 | 12.6 | 99.3 | 106.3 | 6 |
| strings | 12.0 | 11.9 | 103.1 | 118.2 | 24 |
| fp | 12.8 | 12.3 | 77.7 | 148.6 | 10 |

Raw output: `tests/bench/build/result-*.txt` (not committed). Collector: 26 collections and a maximum pause of about 8-20 ms in the
1M-allocation stress with a 3 MB heap (docs/gc.md).

## Findings

1. **`Interpreter.run` is 9744 bytes of bytecode and HotSpot never JIT-compiles a method above 8000 bytes
   (`-XX:+DontCompileHugeMethods` is the default).** With default flags the whole interpreter runs in the JVM's own bytecode
   interpreter: 12-23 Mbc/s. Allowing the compilation (`-XX:-DontCompileHugeMethods`) gives 78-149 Mbc/s (5-10x). The same kind of
   limit exists in ART (`dex2oat` skips "huge" methods, `CompilerOptions::kDefaultHugeMethodThreshold` = 10000 dex code units, and
   the JIT has size limits too) - **unverified on a device**, but the flag cannot be used on Android, so the realistic way to get the
   second column on Android is to keep the hot loop below the thresholds (split `run()`: rare opcodes, invoke/return, conversions and
   long/double/float arithmetic into helper methods). This is a code-structure change of T11's interpreter (T37 territory); it was
   NOT made in T13b. Recommendation: do it before taking any D2 decision, and re-measure with the default flags.
2. Even in the best column the VM is about 100x slower than the same code compiled by HotSpot (as expected for an interpreter on a JIT
   host); the figures say nothing about a phone CPU.
3. Allocation-heavy workloads lose little to collection (alloc 99-106 vs arith 127-144 Mbc/s with the flag).
4. Not measured here: Android/ART (any device), a Nokia device (V2) for the speed the games were written for, the cost of `paint`
   natives. Threshold for "enough" must come from the games' needs and the device speed (T38); the manager fixes it.

Reproduce: `tests/bench/run_bench.sh`; with the flag: `JLKVM_JAVA_OPTS=-XX:-DontCompileHugeMethods tests/bench/run_bench.sh`; JDK 21:
`JLKVM_JAVA=/usr/bin/java tests/bench/run_bench.sh`.

## After T11a (split of `Interpreter.run`)

Rare opcodes moved out of `run()` (pure code motion): long/float/double arithmetic, conversions and comparisons, `dup_x*`/`swap`,
`tableswitch`/`lookupswitch` (`RareOps`); `ldc*`, `wide`, `multianewarray`, `monitorenter/exit`, return-time monitor checks and the whole
native-call path (private methods of `Interpreter`). `javap -c`: `run()` **6826 bytes** (was 9774), every other method < 460 bytes,
so HotSpot JIT-compiles it with default flags. Same seven checksums as HotSpot.

| Workload | JDK 8, default flags (was) | JDK 8, default flags (now) | JDK 8, `-XX:-DontCompileHugeMethods` (now) | JDK 21, default flags (now) |
|---|---|---|---|---|
| arith | 22.8 | 132.3 | 140.1 | 151.3 |
| long | 13.9 | 113.2 | 112.0 | 123.5 |
| calls | 13.7 | 108.2 | 105.7 | 96.9 |
| arrays | 21.7 | 144.8 | 123.5 | 151.1 |
| alloc | 13.4 | 108.9 | 109.6 | 113.4 |
| strings | 12.0 | 100.7 | 98.8 | 111.5 |
| fp | 12.8 | 111.9 | 114.6 | 132.9 |

Default flags now reach 100% of the flag-on figures (>= 70% required). Android/ART is still not measured (V6).

# Oracle rig (PLAN T05)

Runs a MIDlet jar on independent third-party J2ME implementations and captures guest stdout/exceptions.
Only scripts live here: no binaries, no GPL code (D12). `phoneme-shim/` is the project's own ~30-line MIDlet shim for the bare-CLDC mode. Oracles are downloaded/built into `tools/env/cache` (gitignored).

| Oracle | Status | Runner | Notes |
|---|---|---|---|
| MicroEmulator 2.0.4 | working | `run_microemu.sh JAR [Class]` | `org.microemu.app.Headless`, JDK 8, no X server. Maven Central jar via `setup_oracles.sh`. |
| FreeJ2ME-Plus | working | `run_freej2me.sh JAR [WxH]` | `../freej2me/freej2me_plus.jar` in the M-I repo; needs `xvfb-run` (no true headless mode); stops on MIDlet destroy. |
| phoneME Feature (CLDC 1.1 + headless MIDP 2.0) | working (T05b) | `run_phoneme.sh JAR [Class]` | local build of the GPLv2 archive `magicus/phoneME` by `tools/env/build_phoneme.sh` (needs `gcc-multilib g++-multilib`); `PHONEME_MODE=cldc` = bare `cldc_vm` + own shim (`phoneme-shim/`), default = `runMidlet` of the MIDP "stubs" port (no display: lcdui drawing/Image cases crash or run out of memory). |
| KEmulator nnmod | working (T05b) | `run_kemulator.sh JAR` | local build (javac, no ant) by `tools/env/build_kemulator.sh` (licence unknown, never committed); JVM-hosted, SWT GTK under `xvfb-run`, JDK 21. |
| JL-Mod (dex) | NOT available | - | needs an Android runtime/emulator; planned for T35/T39 (A/B). See V4. |

Usage:

    tools/difftest/oracles/setup_oracles.sh
    tools/difftest/oracles/run_all.sh tools/env/cache/out/hello.jar /tmp/oracle-out   # exit 0 = >=2 oracles, same output

Output conventions: stdout of the guest is the oracle output. Framework log lines are filtered; raw logs are not kept.
Uncaught exceptions: MicroEmulator and FreeJ2ME print stack traces to stderr, merged into the output (differences in
trace format are expected; compare the first line `ExceptionClass: message` only — see `tests/vmspec` runner).
Environment: `ORACLE_TIMEOUT` (seconds, default 60), `FREEJ2ME_JAR`, `MICROEMU_JAR`, `JDK8_HOME`.

`tests/vmspec/run_vmspec.sh --oracle phoneme --oracle kemulator [--probe]` runs the corpus on them too (see `docs/oracles.md` for the per-case table).

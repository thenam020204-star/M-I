# Oracle rig (PLAN T05)

Runs a MIDlet jar on independent third-party J2ME implementations and captures guest stdout/exceptions.
Only scripts live here: no binaries, no GPL code (D12). Oracles are downloaded/built into `tools/env/cache` (gitignored).

| Oracle | Status | Runner | Notes |
|---|---|---|---|
| MicroEmulator 2.0.4 | working | `run_microemu.sh JAR [Class]` | `org.microemu.app.Headless`, JDK 8, no X server. Maven Central jar via `setup_oracles.sh`. |
| FreeJ2ME-Plus | working | `run_freej2me.sh JAR [WxH]` | `../freej2me/freej2me_plus.jar` in the M-I repo; needs `xvfb-run` (no true headless mode); stops on MIDlet destroy. |
| phoneME | NOT built | - | see PLAN Vướng mắc V4 (source not reachable from this sandbox). Place a runner `run_phoneme.sh` here when built; `run_all.sh` picks it up. |
| KEmulator | NOT available | - | Windows GUI app, no redistributable headless build. See V4. |
| JL-Mod (dex) | NOT available | - | needs an Android runtime/emulator; planned for T35/T39 (A/B). See V4. |

Usage:

    tools/difftest/oracles/setup_oracles.sh
    tools/difftest/oracles/run_all.sh tools/env/cache/out/hello.jar /tmp/oracle-out   # exit 0 = >=2 oracles, same output

Output conventions: stdout of the guest is the oracle output. Framework log lines are filtered; raw logs are not kept.
Uncaught exceptions: MicroEmulator and FreeJ2ME print stack traces to stderr, merged into the output (differences in
trace format are expected; compare the first line `ExceptionClass: message` only — see `tests/vmspec` runner).
Environment: `ORACLE_TIMEOUT` (seconds, default 60), `FREEJ2ME_JAR`, `MICROEMU_JAR`, `JDK8_HOME`.

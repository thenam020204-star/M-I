# Guest class library (vm-classlib) — T17..T20

## Build (T17, D11)
`tools/romgen` (also `:vm-classlib:buildRom`, part of `check`/`assemble`):
1. JDK 8 `javac -source 1.3 -target 1.3`, `-bootclasspath` = an EMPTY directory (the library only sees itself, never rt.jar/android.jar);
   an overlay build (`--base ROM.jar`) uses the base ROM as bootclasspath.
2. ProGuard 7.5 `-microedition` (preverify, adds StackMap).
3. Closure check: every class named by a constant pool, super/interface list, field/method descriptor or throws clause must be in the ROM.
4. The project's strict verifier (D6, CLDC 1.1) on every compiled class.
5. Optional `--api-db DB.json [--api-ignore PREFIX]...`: public/protected surface == DB (D9). Any failure = exit 1, no jar.

Test ROM of vm-core = real CLDC ROM + the real MIDP 2.0 layer (`vm-classlib/midp/src`, see docs/midp.md) + a small test overlay `vm-core/src/test/guest-stub/src` (handler `testnet`),
built by `vm-core/src/test/guest-stub/build.sh` -> `vm-core/build/guest-stub/rom.jar` (T21-T23 replaced the temporary MIDlet/lcdui overlay).

## API surface (T18, D9)
* `api-sigs diff --ignore jlkvm/internal/ tools/api-sigs/db/cldc-1.1.json ROM.json` = 0. Without `--ignore` exactly three public
  ROM-private classes show up as `+ class` (`jlkvm/internal/Encoding`, `Encoding$Decoder`, `io/ConnectionBase`).
* ROM-private helpers live in package `jlkvm.internal`: public inside the ROM (the library's own packages use them) but NOT API.
  Handling: (1) excluded from the diff by prefix; (2) hidden from application classes by the linker — a game that names one gets
  `NoClassDefFoundError` / `ClassNotFoundException` as on a device without the class (`Linker.isRomPrivate`, case `cl08_romprivate`);
  (3) package-private helpers (`java.lang.ConsoleOutputStream`, `java.util.CalendarImpl`...) are invisible to the extractor and
  inaccessible to games anyway (applications cannot add or read `java.*` internals).
* `Differ.IMPL_BITS`: `synchronized`/`native`/`strict` method bits are ignored in the diff (implementation details the javadoc never shows;
  the guest library implements methods natively where the stub jars have Java bodies, D8). Everything else is compared.
* Source of truth: official CLDC 1.1 javadoc (`tools/env/cache/spec/javadocs`, not committed) over the MicroEmulator-stub DB.
  `tools/api-sigs/javadoc_crosscheck.py [DB.json]` compares a DB with the javadoc: `db/cldc-1.1.json` = 0 differences and the DB
  extracted from the built ROM (internal classes removed) = 0 differences, so no DB correction was necessary.
* Tests: `tests/vmspec/cases/cl01..cl11` (strings, numbers, float/double, util, io, encodings, system, ROM-private, lang/thread/ref,
  character, default encoding) — expected output recorded from the MicroEmulator / FreeJ2ME oracles where they agree, else spec
  (`meta.txt` `source:`/`diverges:`); `ClasslibSystemTest` (profile driven), `FloatTextTest`, `RomGenTest`.

## Text conversion (T19)
* `Float/Double.toString` and `parseFloat/parseDouble` are VM natives (`interp/FloatText`), independent of the host JDK:
  shortest digits that round-trip (CLDC 1.1 javadoc: "as many, but only as many, digits as needed to uniquely distinguish"),
  Java layout (plain for 1e-3 <= |v| < 1e7), at least two digits (`4.9E-324`, `1.4E-45` as the JDK 19+ algorithm).
  Verified against 120 000 random values + special cases of the JDK 21 `Double/Float.toString` (`FloatTextTest`).
  **Oracle differences:** MicroEmulator runs on JDK 8 whose `FloatingDecimal` is NOT shortest (`1.0E23` prints `9.999999999999999E22`,
  `8.41E21` -> `8.409999999999999E21`, `Float.MIN_NORMAL` -> `1.17549435E-38`; JDK bug 4511638); FreeJ2ME on JDK 21 agrees with
  this VM. `cl03_float` expects the javadoc/FreeJ2ME form (`diverges: microemu`). What a real Nokia KVM prints is UNVERIFIED (R2).
  Parser grammar = JDK 1.3 (`NaN`, `Infinity`, optional sign, optional `f/F/d/D` suffix, surrounding blanks; no hex floats).
* Encodings: ISO-8859-1, UTF-8 (required by CLDC), plus US-ASCII and UTF-16/BE/LE (extras, aliases per `Encoding.lookup`).
  Default = profile property `microedition.encoding` (ISO-8859-1 when absent/unsupported); used by `new String(byte[])`, `getBytes()`,
  `InputStreamReader`, `OutputStreamWriter`, `PrintStream`/console. No bytecode rewriting. Unmappable chars encode as `?`;
  malformed UTF-8 decodes to U+FFFD (CLDC does not specify; UNVERIFIED on Nokia).
* String literals are interned by the VM (`s01_string_intern`, `String.intern()` native).
* `Character` covers Basic Latin, Latin-1 (identical to the JDK, `cl10_character`), plus Latin Extended-A/Additional (Vietnamese),
  Greek, Cyrillic (partial, informational; the CLDC javadoc only requires the first two).

## System / Runtime / GCF (T20)
* Profile JSON (`profiles/jlkvm-min.json`, loader `jlkvm.profile.ProfileLoader`, schema in its javadoc; T33 extends it).
  `System.getProperty(key)`: value from `properties` of the profile, unknown key (case sensitive) -> `null`, `null` key -> NPE,
  empty key -> IllegalArgumentException. CLDC 1.1 defines only `microedition.platform/encoding/configuration/profiles`
  (spelling `CLDC-1.1`; JL-Mod had `CDLC-1.1`); `microedition.locale` is a MIDP property. All values UNVERIFIED until T33.
* `Runtime.totalMemory/freeMemory/gc` = arena figures / full collection; `System.currentTimeMillis` = host clock, or the virtual
  clock in deterministic mode (D14); default `TimeZone` from `timeZone` of the profile.
* `Connector.open(url[,mode[,timeouts]])`: scheme validation (IllegalArgumentException), then the profile's network policy per
  scheme: `deny` -> `SecurityException`, `offline` -> `IOException`, `allow` -> handler class `jlkvm.internal.io.<scheme>.Protocol`
  (`ConnectionBase.openPrim`), `ConnectionNotFoundException` when the ROM has no handler. `file`, `comm`, `resource`, `storage`
  skip the policy. No real network traffic exists in this phase (PLAN scope); MIDP's `HttpConnection` etc. arrive with M4.

# Oracles (PLAN T05 / T05b)

How the corpus `tests/vmspec` looks on each independent implementation. Oracle sources and builds live only in `tools/env/cache/` (gitignored);
nothing of phoneME (GPLv2) or KEmulator nnmod (licence unknown) is committed or copied into the repo (D12). Date of the runs: 2026-10-30.

| Oracle | Nature | Run | Built by |
|---|---|---|---|
| MicroEmulator 2.0.4 | JVM-hosted | `run_microemu.sh` | `setup_oracles.sh` |
| FreeJ2ME-Plus | JVM-hosted | `run_freej2me.sh` | repo `../freej2me` |
| phoneME Feature, CLDC 1.1 + headless MIDP 2.0 ("phm") | **real CLDC VM** (Sun/Oracle reference port, linux_i386) | `run_phoneme.sh` (`runMidlet -classpathext JAR -1 MIDlet`) | `tools/env/build_phoneme.sh` |
| phoneME bare CLDC ("phc") | same VM without MIDP + own 30-line MIDlet shim | `PHONEME_MODE=cldc run_phoneme.sh` | same |
| KEmulator nnmod 2.22 ("kem") | JVM-hosted (JDK 21, SWT under Xvfb), NokiaUI 1.4, lcdui, RMS, MMAPI, JSR-75/120... | `run_kemulator.sh` | `tools/env/build_kemulator.sh` |

phoneME is the only oracle that is a CLDC VM: where it differs from the JVM oracles on core language/library behaviour, that is real CLDC behaviour of the
reference implementation (not necessarily of Nokia's KVM, V2). Its MIDP port is the "stubs" one (no screen): anything that needs Image/Graphics/Canvas crashes
or runs out of heap; JSR-75/120/135/179/Nokia UI are not part of the build.

Run: `tests/vmspec/run_vmspec.sh --oracle phoneme --oracle kemulator [--probe]` (`--probe` also runs the spec-only cases, informational PROBE lines).
Cases that differ are marked `diverges: ...phoneme|kemulator` in their `meta.txt`, so the oracle run stays green; `n/a` = the oracle lacks an API (not a mismatch).

Totals over 96 cases run (oracle-mode cases and spec-only probes): phm ok 46 / DIFF 45 / n/a 5;
phc ok 46 / DIFF 25 / n/a 25; kem ok 59 / DIFF 37 / n/a 0.
(cases driven by a host test - d01, cl11, ... - cannot run on an oracle and are not listed.)

## Findings that may concern JL-KVM (the VM was NOT changed; for the foreman to decide)

1. **CLDC reference behaviour that JVM oracles hide** (phoneME, real CLDC): failing `<clinit>` raises plain `java.lang.Error` (i05, i06; JL-KVM already does so in i03 while the
   expected.txt of i05/i06 is JVM-style - check consistency), `jsr/ret` rejected (o03, neg02: confirms the spec-only neg02), no `IllegalAccessError` (l02), eager NoClassDefFoundError for
   null instanceof/checkcast (l01), Calendar/TimeZone GMT only (cl04), Double.toString formatting `0.0010` (cl03), `Integer.parseInt("+7")` NFE (cl02), `indexOf(String,int)` clamping (cl01).
   SUGGEST task "T05c: review cl01-cl06, cl10, l01, l02, i05, i06 expectations against the phoneME output (decide per case: JL-KVM follows CLDC reference or JVM)".
2. **rms01** (spec-only) is contradicted on different lines by both phoneME and KEmulator: SUGGEST a line-by-line review against the RecordStore javadoc.
3. **nk01/nk04 vs KEmulator**: differences in DirectGraphics pixel formats and concurrent sounds - SUGGEST rechecking those two expectations against the Nokia UI javadoc (UNVERIFIED stays).
4. **j04** (duplicate jar entry): phoneME takes the first entry, JVM oracles the last - unknown on a Nokia device; no action.
5. No case showed an obvious JL-KVM bug; every DIFF is either an oracle limitation (below) or a question of expectation (above).

## Oracle limitations seen
* KEmulator is JVM-hosted: JVM-only semantics leak (ExceptionInInitializerError, no ACC_SUPER dispatch, jsr/ret/no-StackMap accepted, java.* loadable, case-insensitive resources) and several lcdui/MMAPI/Nokia UI bugs (see rows).
* phoneME MIDP runs with `-classpathext`, so there is no MIDlet manifest (j02), a small heap (img01, g05, ui01/02) and no display.
* Hangs: phoneME hangs in cl03 after the float formatting lines; killed after `ORACLE_TIMEOUT` (SIGKILL), partial output kept.
* Build notes: phoneME needs `gcc-multilib`; the 2008 makefiles are patched around at build time by wrappers in the cache (not in the repo); the stubs port needs a one-function local patch (display device list). See `build_phoneme.sh`.

## Per-case table

| case | phoneME MIDP | phoneME CLDC | KEmulator | note |
|---|---|---|---|---|
| a01_div_zero | ok | ok | ok |  |
| a02_min_div_neg1 | ok | ok | ok |  |
| a03_rem_sign | ok | ok | ok |  |
| a04_shift_mask | ok | ok | ok |  |
| a05_narrowing | ok | ok | ok |  |
| a06_baload_caload | ok | ok | ok |  |
| b01_busy_loop | ok | ok | ok |  |
| c01_checkcast_instanceof | ok | ok | ok |  |
| c02_null_dispatch | ok | ok | ok |  |
| cl01_string | DIFF | DIFF | ok | phoneME: `indexOf("", 50)` returns -1 (JVMs return the length 12). CLDC-specific; SUGGEST checking the CLDC 1.1 javadoc of indexOf(String,int). |
| cl02_numbers | DIFF | DIFF | ok | phoneME: one Integer.parseInt input that the JVMs accept (probably a leading "+") throws NumberFormatException, and a Latin-1 Character.toUpperCase result differs (223 vs 376). CLDC-specific; SUGGEST comparing with the CLDC javadoc. |
| cl03_float | DIFF | DIFF | ok | phoneME: Double/Float.toString prints 0.0010 / 9.999999999999999E22 / 1.17549435E-38 (KVM float formatting differs from the JDK) and parseDouble("NaN"/"Infinity") -> NFE, then the VM hangs in a later line. CLDC-specific; real Nokia KVM output is unknown (V2). |
| cl04_util | DIFF | DIFF | ok | phoneME stops after the 26th line (at cal3, Calendar after setTimeZone/field arithmetic); remaining 9 lines missing. Cause not analysed (Calendar in CLDC is minimal). known-diverge. |
| cl05_io | DIFF | DIFF | ok | phoneME: BufferedReader/InputStreamReader.markSupported() = true (JVM oracles false). CLDC 1.1 javadoc: markSupported is implementation-defined for InputStreamReader. known-diverge. |
| cl06_encoding | DIFF | DIFF | ok | phoneME rejects the alias "UTF8" (UnsupportedEncodingException; CLDC only guarantees "UTF-8"/"ISO8859_1"), plus later StringIndexOutOfBounds message text. known-diverge. |
| cl08_romprivate (spec-only) | ok | ok | DIFF | KEmulator: java.util.ArrayList is loadable (JVM-hosted; no CLDC class hiding). known-diverge (JVM artefact). |
| cl09_lang | DIFF | DIFF | DIFF | weak/exception message texts: phoneME has no IndexOutOfBounds messages ("3" instead of "Array index out of range: 3"); KEmulator (JDK 21) System.gc() did not clear the weak ref in time. Oracle quirks, known-diverge. |
| cl10_character | DIFF | DIFF | ok | phoneME: Character.hashCode/other values of the Latin-1 loop and one case-mapping entry (U+00DF: bf vs df) differ from the JDK tables; CLDC-specific. known-diverge. |
| e01_uncaught_exception | DIFF | n/a | ok | phoneME MIDP: no paint calls without a screen (paint count 0). Headless limitation. |
| enc01_encoding | ok | DIFF | ok | phoneME CLDC mode only (shim, no MIDP encodings): UnsupportedEncodingException; MIDP mode ok. |
| ev01_display_order | DIFF | n/a | DIFF | phoneME: no display, Canvas events not delivered, prints 5/17 lines. KEmulator: Canvas.isShown() is true inside showNotify() (MIDP: not yet shown) - KEmulator quirk. |
| f01_fp_to_int_sat | ok | ok | ok |  |
| f02_fcmp_nan | ok | ok | ok |  |
| fc01_sandbox (spec-only) | DIFF | DIFF | DIFF | FileConnection (JSR-75) not in phoneME build; KEmulator uses the real file system (roots "root/"), so the sandbox-specific lines differ. Cannot confirm the spec-only expectations. |
| fc02_ops (spec-only) | DIFF | DIFF | DIFF | as fc01: phoneME has no JSR-75; KEmulator stops after one line (real FS root differs). |
| fc03_readonly (spec-only) | n/a | n/a | DIFF | as fc01 (no JSR-75 on phoneME; KEmulator real FS). |
| fc04_deny (spec-only) | n/a | n/a | DIFF | as fc01; KEmulator does not deny (no permission model). |
| g01_gc_stress | ok | ok | ok |  |
| g02_gc_identity_hash | ok | ok | ok |  |
| g03_gc_weak_reference | ok | ok | ok |  |
| g04_gc_handles (spec-only) | DIFF | DIFF | DIFF | needs the project test native (attach0): UnsatisfiedLinkError on every oracle that has no ROM natives. Not applicable. |
| g05_gc_threads | DIFF | ok | ok | phoneME MIDP: heap of the MIDP build is too small (no output); CLDC mode ok. |
| g06_gc_oom (spec-only) | ok | ok | DIFF | KEmulator (JVM, 512 MB heap): filling the heap takes more than the oracle timeout - no output. Not applicable. |
| gfx01_graphics | DIFF | n/a | DIFF | phoneME stubs port has no screen: Image/Graphics crash. KEmulator: Graphics.getColor() returns 0xff000000 (alpha set), different rasterisation hashes, negative empty-clip size (-36x-52). |
| gm01_sprite_model (spec-only) | DIFF | n/a | ok | phoneME: lcdui Image needs the missing display backend -> OutOfMemoryError. KEmulator ok. |
| gm02_collision (spec-only) | DIFF | n/a | DIFF | phoneME: IllegalArgumentException (stubs port). KEmulator: pixel-level collision map differs (alpha 0x01 treated as opaque). |
| gm03_tiled_layer | DIFF | n/a | DIFF | KEmulator: TiledLayer.fillCells negative size throws IndexOutOfBoundsException (MIDP javadoc: IllegalArgumentException). KEmulator bug. phoneME: stubs port. |
| gm04_layer_manager (spec-only) | DIFF | n/a | DIFF | KEmulator: LayerManager.append(existing) throws ArrayIndexOutOfBoundsException (javadoc: moves it). KEmulator bug. phoneME: stubs port. |
| gm05_sprite_paint | DIFF | n/a | ok | phoneME stubs port has no screen (crash). KEmulator ok. |
| i01_clinit_order | ok | ok | ok |  |
| i02_clinit_recursive | ok | ok | ok |  |
| i03_clinit_failure | ok | ok | DIFF | KEmulator (JVM): ExceptionInInitializerError exists (CLDC has only Error). JVM artefact, known-diverge. |
| i04_constant_inline | ok | ok | ok |  |
| i05_clinit_threads | DIFF | DIFF | ok | phoneME CLDC: failing <clinit> throws a plain Error carrying the message ("Static initializer") instead of ExceptionInInitializerError. This is real CLDC behaviour (CLDC 1.1 has no EIIE): the oracle JVMs are the ones that differ. SUGGEST: expected.txt for i05/i06 stays JVM-style until the Nokia KVM is measured (V2); JL-KVM currently follows CLDC (java.lang.Error) in i03. |
| i06_clinit_errors | DIFF | DIFF | ok | same as i05 (CLDC raises Error, not ExceptionInInitializerError). |
| img01_image_memory | DIFF | n/a | ok | phoneME: Image.createImage(200,200) -> OutOfMemoryError in the small MIDP heap. |
| io01_gcf_midp (spec-only) | DIFF | n/a | DIFF | phoneME MIDP: HTTPS site certificate error message, PushRegistry.unregister returns true. KEmulator: only 4/18 lines (no PushRegistry/certificate model). Spec-level items stay UNVERIFIED. |
| j01_resource_paths | ok | ok | DIFF | KEmulator: resource lookup is case-insensitive ("/Data.TXT" found). KEmulator quirk (Windows origin). |
| j02_manifest | DIFF | DIFF | ok | phoneME `-classpathext` run: no MIDlet manifest, getAppProperty returns null. Runner limitation (no JAD/manifest installation). |
| j03_class_names | ok | ok | ok |  |
| j04_jar_duplicate_entry | DIFF | DIFF | ok | phoneME reads the FIRST duplicate jar entry, JVM oracles the LAST. Real device behaviour unknown; JL-KVM follows expected (SECOND). |
| j05_jar_bad_crc | ok | ok | ok |  |
| k01_no_acc_super | ok | ok | DIFF | KEmulator (JVM): class without ACC_SUPER dispatches to B.m. JVM artefact. |
| k02_constantvalue_getstatic | ok | ok | ok |  |
| k03_acc_super_set | ok | ok | ok |  |
| k04_constantvalue_in_clinit | ok | ok | ok |  |
| l01_ncdfe_timing | DIFF | DIFF | ok | phoneME resolves classes of instanceof/checkcast on null eagerly (NoClassDefFoundError where JVM does not). Real KVM behaviour is an open question (D9): SUGGEST task to decide link timing; no change to JL-KVM now. |
| l02_access_checks | DIFF | DIFF | ok | phoneME reports plain java.lang.Error for access violations (IllegalAccessError missing in CLDC). CLDC-specific; JL-KVM expected matches JVM. known-diverge. |
| l03_protected_packages | DIFF | DIFF | DIFF | phoneME loads javax.microedition.* and com.nokia.* classes from the app jar (no package protection on the -classpathext path); KEmulator: error class text differs. JL-KVM refuses (spec). known-diverge. |
| m01_monitor_state | ok | ok | ok |  |
| m02_sync_method | ok | ok | ok |  |
| m03_wait_notify | ok | ok | ok |  |
| m04_interrupt | ok | ok | ok |  |
| m05_monitor_imbalance | ok | ok | ok |  |
| mm01_player_states (spec-only) | DIFF | n/a | DIFF | phoneME: MMAPI not built (MediaException: Cannot create Player). KEmulator: players exist but state/exception order differ (26/69 lines). Spec-only expectation not confirmed. |
| mm02_tone (spec-only) | DIFF | n/a | DIFF | phoneME: no tone support. KEmulator: getSupportedContentTypes for the device protocol is empty, not [audio/x-tone-seq]. Not confirmed. |
| n01_long_native | DIFF | n/a | ok | phoneME stubs port: Image crash. Needs a project test native otherwise. Not applicable. |
| neg01_no_stackmap (spec-only) | ok | ok | DIFF | JVM-hosted oracles (KEmulator) accept classes without StackMap and run them; phoneME MIDP rejects (ok). Expected: error. JL-KVM matches phoneME. |
| neg02_jsr_ret (spec-only) | ok | ok | DIFF | KEmulator runs jsr/ret; phoneME rejects (ok). JL-KVM rejects like phoneME - phoneME CONFIRMS the spec-only expectation. |
| neg03_float_in_cldc10 (spec-only) | DIFF | DIFF | DIFF | both phoneME (CLDC 1.1 build) and KEmulator execute float code in a CLDC 1.0 jar: no oracle enforces CLDC 1.0. Expectation stays spec-only. |
| nk01_directgraphics_pixels (spec-only) | DIFF | n/a | DIFF | phoneME: no com.nokia.mid.ui; KEmulator (NokiaUI 1.4): 29/69 lines, int888/int8888 getPixels differ from the S40 spec-only expectation. SUGGEST task: re-read the DirectGraphics javadoc for the KEmulator differences before trusting nk01. |
| nk02_transforms (spec-only) | DIFF | n/a | DIFF | KEmulator: bad manipulation argument gives no exception (the other 7 lines agree). phoneME: no Nokia UI. |
| nk04_sound_ott (spec-only) | DIFF | DIFF | DIFF | phoneME: no com.nokia.mid.sound. KEmulator: concurrent-sound limit not enforced (10/47 lines). |
| o01_opcodes | ok | ok | ok |  |
| o02_raw_ops | ok | ok | ok |  |
| o03_jsr_ret | DIFF | DIFF | ok | phoneME CLDC rejects jsr/ret (Error) - consistent with the CLDC spec (and neg02), JVM oracles run it. known-diverge: CLDC rejection is correct. |
| p01_producer_consumer | ok | ok | ok |  |
| p02_deadlock (spec-only) | ok | ok | ok |  |
| p03_threads_schedule (spec-only) | ok | ok | ok |  |
| pim01_minimal (spec-only) | n/a | n/a | DIFF | phoneME: no JSR-75 PIM. KEmulator: PIM present but behaves differently (2/33 lines). |
| pim02_deny (spec-only) | n/a | n/a | DIFF | as pim01. |
| q01_natives (spec-only) | DIFF | DIFF | DIFF | project test natives only (UnsatisfiedLinkError on every oracle). Not applicable. |
| r01_negative_array_size | ok | ok | ok |  |
| r02_array_store | ok | ok | ok |  |
| r03_multianewarray | ok | ok | ok |  |
| r04_array_bounds | ok | ok | ok |  |
| rms01_store (spec-only) | DIFF | n/a | DIFF | phoneME RMS (real CLDC/MIDP implementation): openRecordStore with an invalid authmode succeeds and leaves the store behind; later lines differ in 30 places (e.g. previous/next after delete). KEmulator: listRecordStores() returns "" instead of null for no stores. Two independent oracles disagree with each other and with the spec-only expectation on different lines - UNVERIFIED stays; SUGGEST task: review rms01 expectations line by line against the RecordStore javadoc. |
| s01_string_intern | ok | ok | ok |  |
| t01_stack_overflow | ok | ok | ok |  |
| tm01_timer | DIFF | n/a | DIFF | phoneME and KEmulator: a throwing TimerTask does not end the Timer (javadoc: ends it). Both deviate like MicroEmulator. |
| ui01_form_items (spec-only) | DIFF | n/a | DIFF | phoneME: ImageItem/Image cases need the display backend (OutOfMemoryError). KEmulator: all 87 lines produced, differences in NPE/ok handling for null append - KEmulator lenient. |
| ui02_choice (spec-only) | DIFF | n/a | DIFF | phoneME: image copy OutOfMemoryError. KEmulator: ChoiceGroup(EXCLUSIVE) selects item 0 on an empty group (idx=0; spec: -1). |
| ui03_text (spec-only) | DIFF | n/a | DIFF | phoneME and KEmulator accept a bad TextField constraint bit and some oversize texts (lenient); javadoc says IllegalArgumentException. |
| wma01_policy (spec-only) | DIFF | DIFF | DIFF | phoneME: no WMA, ConnectionNotFoundException instead of the project policy SecurityException; KEmulator: SecurityException lines same except the offline variant. Policy is project-specific (not testable on an oracle). |
| wma01b_policy_offline (spec-only) | DIFF | DIFF | DIFF | as wma01. |
| wma02_messages (spec-only) | n/a | n/a | DIFF | phoneME: no WMA. KEmulator: Message.getTimestamp() is the current time (javadoc: null when not set) - KEmulator bug. |

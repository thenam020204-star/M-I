# WMA 1.1 and JSR-75 (FileConnection, PIM) layers (PLAN T32a)

Three small ROM layers, each in its own jar, loaded by the desktop host (`RomLayers`) only when the profile lists the api in `apis` (D10);
without it the classes do not exist for the game (`Class.forName` fails; `Connector.open("sms://...")` / `("file:///...")` is a
`ConnectionNotFoundException` once the policy lets it pass, because there is no handler class). Why three jars and not one: a device can have the
SMS API without a file system, the three have different permission models (`sms.mode`, `file.permission`, `pim.permission`), and a layer a game
does not use costs nothing.

| profile api | jar (`vm-classlib/build/rom`) | sources | gradle task | surface == (romgen fails the build on any difference) |
|---|---|---|---|---|
| `wma-1.1` | `rom-wma.jar` | `vm-classlib/wma/src` | `buildWmaRom` | cldc-1.1 + midp-2.0 + `s40dp2-wma-1.1.json` (Nokia S40 DP2.0 SDK 1.1, **verified source**, PLAN V14b) |
| `jsr75-fc` | `rom-jsr75-fc.jar` | `vm-classlib/jsr75-fc/src` | `buildJsr75FcRom` | cldc-1.1 + midp-2.0 + `jsr75-fc.json` (**UNVERIFIED**, below) |
| `jsr75-pim` | `rom-jsr75-pim.jar` | `vm-classlib/jsr75-pim/src` | `buildJsr75PimRom` | cldc-1.1 + midp-2.0 + `jsr75-pim.json` (**UNVERIFIED**, below) |

Sample profile: `profiles/jlkvm-nokia.json` (= jlkvm-min + nokia-ui + the three layers + the blocks `sms{}`, `file{}`, `pim{}`; every value UNVERIFIED).
`jlkvm-min.json` has none of them. The flat test ROM of the vmspec runner (`vm-core/src/test/guest-stub/build.sh`) contains all three, the policy then
comes from the profile of the case (`profile.json` next to `meta.txt`, read by `--engine jlkvm`).

## Signature sources (UNVERIFIED for JSR-75)

* WMA: `tools/api-sigs/db/s40dp2-wma-1.1.json`, taken from the SDK `classes.zip` (not touched). Javadoc of the SDK (local, gitignored) is the behaviour reference.
* `jsr75-fc.json`: `org.microemu:microemu-jsr-75:2.0.4` (Maven Central; fetched through `maven-central.storage-download.googleapis.com`), only
  `javax/microedition/io/file/**` (the `org/microemu/**` implementation classes are LGPL and were not used). Cross-checked against the stubs in
  `freej2me_plus.jar` (GPL, local): the five classes agree except that FreeJ2ME's `FileSystemRegistry` has a public constructor and its listener method
  declares unchecked exceptions; the MicroEmulator form was kept (no public constructor), as the JSR-75 API has none.
  Both stubs make `ConnectionClosedException` and `IllegalModeException` `RuntimeException`s.
* `jsr75-pim.json`: **no PIM stub exists on Maven Central** (`microemu-jsr-75` has none). Taken from the abstract/interface stub classes of `freej2me_plus.jar`
  (local GPL jar, signatures only, 14 classes). Nothing was copied from it.
* No JSR-75 SDK / javadoc is available (the Nokia S40 DP2.0 SDK predates JSR-75): every JSR-75 behaviour below is the project's own reading of the API
  (UNVERIFIED), to be re-checked when an SDK with JSR-75 (S40 3rd edition and later) is supplied; then replace the two DBs and run the diff again.

## WMA (`javax.wireless.messaging`)

* **Profile `sms{mode}`**: `deny` (default) | `simulate` | `offline`, for the schemes `sms` and `cbs` (`DeviceProfile.networkPolicy` answers for them from this key, not
  from `network{}`; `mms` is not WMA 1.1 and follows `network{}`).
  * `deny`: `Connector.open` throws `SecurityException` (the user refused). `offline`: `IOException`. Nothing is created, nothing can be sent.
  * `simulate`: the connection opens; `send` returns normally and **nothing leaves the host** - the message is appended to `SmsNatives.sent()` and reported
    to the sink (`HeadlessRunner` logs `[sms] sent to=...;text=...;segments=N`, binary as `data=HEX`).
  * The host natives (`SmsNatives`) check the mode again on every call (`bind0`, `sendText0`, `sendBinary0`): only `simulate` succeeds, so a guest that
    bypassed `Connector` still cannot send (`PolicyNativesTest`). `SmsNatives` has no network code at all.
* Classes: `MessageConnection` (client `sms://+number[:port]`, server `sms://:port`, `cbs://:channel`), `TextMessage`, `BinaryMessage`, `MessageListener`
  (`jlkvm.internal.wma.Conn`, `SmsText`, `SmsBinary`). Text and binary messages are different classes (`instanceof` is exact).
* **Incoming messages exist only when the test host pushes them**: `SmsNatives.deliverText/deliverBinary(cbs, port, from, ..., time)` from any thread (accepted
  only in `simulate` mode and only when a connection holds the port, else dropped), or the script line `<ms> sms text|cbs PORT FROM TEXT...` /
  `<ms> sms binary PORT FROM HEX` of `HeadlessRunner`. `receive()` parks only the calling guest thread (`AsyncBlocker`); closing wakes it with `InterruptedIOException`.
* **The listener runs only on a guest thread** (one per connection, started by `setMessageListener(l)`, never inside the call that caused it, never on a host
  thread); it is notified once per arrived message, including messages that were queued before the listener was registered; the application then calls `receive()`.
  An exception thrown by the listener is printed and does not stop the delivery.
* Errors per the javadoc: `newMessage` bad type -> `IllegalArgumentException`; `send(null)` -> `NullPointerException`; missing / bad address, payload too long ->
  `IllegalArgumentException`; closed -> `IOException`; `receive` / `setMessageListener` on a client connection -> `IOException`.
* UNVERIFIED (the SMS adapter appendix of JSR-120 is not in the SDK javadoc): URL syntax and validation (digits with an optional leading `+`, port 0..65535, no address
  and no port -> `IllegalArgumentException`), addresses of messages must be `sms://` URLs, the port reservation (second open of a reserved port -> `IOException`),
  `cbs` only in server mode and `send` on it -> `IOException`, `numberOfSegments`: GSM 7-bit text 160 / 153 septets per segment (extension table characters count 2),
  any other character makes the text UCS-2 (70 / 67 characters), binary 140 / 134 bytes without a port and 133 / 128 with a port, at most 255 segments (more: `numberOfSegments`
  = 0, `send` -> `IllegalArgumentException`), `getTimestamp()` is `null` for a message that was not received, `null` text is sent as the empty text, refusing at open (not at
  send) when the policy is `deny`.
* Not done: MMS / `MultipartMessage` (WMA 2.0), sender addresses other than the test host's, real message routing, PushRegistry delivery of SMS.

## JSR-75 FileConnection (`javax.microedition.io.file`)

* **Profile `file{roots[], permission, quotaBytes}`**: `roots` names like `c:/` (one name and a slash; default `c:/ e:/ root1/` - the roots that games call in the jarscan
  strings: `file:///root1/`, `e:/`, `c:/`; UNVERIFIED), `permission` `readwrite` (default) | `read` | `deny`, `quotaBytes` per root (default 8 MiB).
  `deny`: every call is a `SecurityException` (also `listRoots`); `read`: opening for WRITE and every change is. Checked again by the host natives (`FileNatives`).
* **Host side `jlkvm.host.fs.HostFs`**: `MemoryFs` (default of the runners, lost at exit) and `DirFs` (a directory of the host: root `c:/` is `base/c_`, `root1/` is `base/root1`;
  the desktop runner puts `base/<vendor_name>` of the suite there: option `--fs-dir DIR`, `Options.fsDir`). Both validate every path again: a known root, then no empty, `.`,
  `..`, backslash, NUL or `:` segment. `DirFs` additionally compares the CANONICAL path of the target with the canonical root, so a **symlink** inside a root that points outside
  is refused (`IOException`, also for the parent of a file to create) and is not listed. Attributes (not readable / not writable / hidden) are kept in memory.
* **Sandbox in the URL**: `file://[localhost]/root/dir/file`; `..`, `.`, empty segments, backslash, `:`, NUL, also percent-encoded (`%2e%2e`), are `IllegalArgumentException`;
  another host name -> `IllegalArgumentException`; a name that is not a root (`file:///etc/passwd`) -> `ConnectionNotFoundException`. `setFileConnection("..")` stops at the root (`IOException`);
  names with a `/` are refused. `file:///` alone is the virtual list of roots (exists, directory, `list()` = the roots).
* Methods: `exists isDirectory fileSize directorySize lastModified create mkdir delete rename truncate list(filter, includeHidden) setFileConnection
  openInputStream/openOutputStream(offset)/openData*Stream canRead/canWrite/isHidden/set* totalSize/availableSize/usedSize getName/getPath/getURL isOpen close`,
  `FileSystemRegistry.listRoots/add|removeFileSystemListener` (roots never change, a listener is never called). Streams own a host handle (at most 64 open), outlive the connection and buffer 1 KiB.
* UNVERIFIED (no JSR-75 text here): default root names; `IllegalArgumentException` for a bad URL and `ConnectionNotFoundException` for an unknown root; root names case-insensitive;
  `openOutputStream()` overwrites from the start and does NOT truncate, `openOutputStream(offset)` with offset > size = `IOException`; `fileSize()` = -1 for a missing path or a directory,
  `lastModified()` = 0 for a missing path; `list()` sorted, directories end with `/`, hidden entries only with `includeHidden`, filter wildcard only `*`; `rename` takes a bare name;
  `delete` of a non-empty directory = `IOException`; `canRead/canWrite/isHidden` false for a missing path; READ-mode connections refuse changes and WRITE-mode ones refuse reading and
  `fileSize/list` with `IllegalModeException`; a not-readable / not-writable attribute -> `SecurityException` on open of a stream; every method except `isOpen getName getPath getURL close`
  throws `ConnectionClosedException` on a closed connection; names are percent-decoded as UTF-8 and `getURL()` re-encodes them; a reserved quota is per root.

## JSR-75 PIM (`javax.microedition.pim`)

The three games that use it (jarscan, `docs/games-api-scan.md`) call only `PIM.getInstance()`, `listPIMLists(type)`, `openPIMList(type, mode, name)`, `ContactList.items()`,
`Contact.getString(field, index)` and `close()`. The implementation is the whole API in memory (`jlkvm.internal.pim`): one default list per type (`Contacts`, `Calendar`, `ToDo`),
**empty at start** and kept for the run only; working copies and `commit()` (`importX`, `removeX`, `items()`, `items(String)` = case-insensitive substring of any string value,
`items(template)`, event ranges `STARTING/ENDING/OCCURRING`, to-do completion and due range), typed fields with the field tables of the three item types, `RepeatRule` stored but not expanded.
* **Profile `pim{permission}`**: `allow` (default) | `deny` -> `SecurityException` from `listPIMLists` / `openPIMList` (list type is validated first); checked by the host native `Pim.perm0`.
* Not done / answers: serial formats (vCard, vCalendar) unsupported (`supportedSerialFormats` empty, `fromSerialFormat/toSerialFormat` -> `PIMException(FEATURE_NOT_SUPPORTED)`), categories
  (`maxCategories()` = 0, `addCategory` -> `PIMException(MAX_CATEGORIES_EXCEEDED)`), no persistence, no real address book; `maxValues(field)` 0 for a field the list does not know.
  Everything (names of the lists, field and attribute tables, labels, exception kinds) is UNVERIFIED.

## Tests

* vmspec (`--engine jlkvm`, spec-only, `stubs:` wma / jsr75-fc / jsr75-pim = the open jars at compile time): `wma01_policy` (deny), `wma01b_policy_offline`, `wma02_messages` (simulate: types,
  segments, send errors, server mode, cbs), `fc01_sandbox`, `fc02_ops`, `fc03_readonly`, `fc04_deny`, `pim01_minimal`, `pim02_deny`. The oracles are not run (host disk / host contacts).
* host-desktop `JsrHostTest` (MIDlet `tests/midlets/jsr`, script `tests/midlets/scripts/jsr.inp`): layers only with the profile api; sms deny/offline: nothing sent, nothing accepted; simulate: host log,
  `receive`, listener on a guest thread, message queued before the listener; FileConnection with a real symlink inside the suite directory and `..` URLs (outside file untouched, nothing created outside);
  `HostFs` (both implementations) against ~13 bad paths, rename out of the directory and the quota.
* host-common `PolicyNativesTest`: the natives refuse by themselves (sms deny/offline, file read/deny).

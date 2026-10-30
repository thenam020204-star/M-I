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


# JSR-82 Bluetooth, JSR-179 Location, JSR-256 Sensor, JSR-234 AMMS (PLAN T32 rest, A9)

Four more ROM layers in the same style as above (own jar each, only loaded when the profile lists the api, D10; `RomLayers`; flat test ROM = all of them;
`stubs:` of the vmspec runner = this project's own layer jar, whose surface romgen already checked against the DB):

| profile api | jar | sources | gradle task | surface == | DB source |
|---|---|---|---|---|---|
| `jsr82` | `rom-jsr82.jar` | `vm-classlib/jsr82/src` | `buildJsr82Rom` | cldc-1.1 + midp-2.0 + `s40dp2-jsr82.json` | Nokia S40 DP2.0 SDK 1.1 `classes.zip`, **verified source** (javadoc local: `tools/env/cache/nokia-sdk/sdk/api/Bluetooth_1.0`, not copied) |
| `jsr179` | `rom-jsr179.jar` | `vm-classlib/jsr179/src` | `buildJsr179Rom` | cldc-1.1 + midp-2.0 + `jsr179-location.json` (**UNVERIFIED**) | `javax/microedition/location/**` of the stubs in `freej2me_plus.jar` (local, GPL, signatures only, 13 classes), **corrected by hand** (below) |
| `jsr256` | `rom-jsr256.jar` | `vm-classlib/jsr256/src` | `buildJsr256Rom` | cldc-1.1 + midp-2.0 + `jsr256-sensor.json` (**UNVERIFIED**) | `javax/microedition/sensor/**` of the same jar (16 classes) |
| `jsr234` | `rom-jsr234.jar` | `vm-classlib/jsr234/src` | `buildJsr234Rom` | cldc-1.1 + midp-2.0 + `jsr234-amms.json` (**UNVERIFIED**) | `javax/microedition/amms/**` of the same jar (43 classes) |

The SDK DB of JSR-82 has no `javax.obex` and no `RFCOMM` classes of its own (RFCOMM = `StreamConnectionNotifier`/`StreamConnection` of CLDC): scheme `btgoep://` has no
handler (`ConnectionNotFoundException`). The two public inner thread classes of `DiscoveryAgent` (`InquiryListenerThread`, `ServiceSearchListenerThread`, an artefact of the
SDK implementation that is in its DB) are real inner classes here and are what delivers the events.

## JSR-82 (`javax.bluetooth`)

* **Profile `bluetooth{mode, address, name}`** (`DeviceProfile.bluetoothMode/Address/Name`, `ProfileLoader`): `none` (default) | `simulate`.
  The schemes `btspp`, `btl2cap`, `btgoep` are `LOCAL_SCHEMES` (they never go through the `network{}` policy, there is no network code behind them).
* **No hardware (`none`)** - javadoc: `LocalDevice.getLocalDevice()` "BluetoothStateException - if the Bluetooth system could not be initialized": thrown on every call, so
  the application never reaches any other instance method; `LocalDevice.getProperty` "null if the property is not defined": null; `Connector.open("btspp://..." | "btl2cap://...")`
  -> `BluetoothStateException` (an `IOException`, so the usual `catch (IOException)` of games handles it). Test: vmspec `bt01_nohw`.
* **Simulated adapter (`simulate`)**: ONE local adapter with the profile's address/name, **no peers**. `getLocalDevice()` returns one object; discoverable mode kept per javadoc
  (valid: GIAC, LIAC, NOT_DISCOVERABLE, 0x9E8B00..0x9E8B3F, else `IllegalArgumentException`; always granted); `getProperty` answers the ten mandatory property names
  (api.version "1.0", master.switch "false", sd.attr.retrievable.max "8", connected.devices.max "7", l2cap.receiveMTU.max "672", sd.trans.max "1", the four connected.* "false";
  values UNVERIFIED). Inquiry: runs 1000 ms of (virtual) time on a guest thread and ends `inquiryCompleted(INQUIRY_COMPLETED)` without a `deviceDiscovered`; `cancelInquiry(listener)`
  only for the starting listener, ends with `INQUIRY_TERMINATED`; a second inquiry while one runs is a `BluetoothStateException`; `retrieveDevices` is `null` ("null if no devices meet
  the criteria"). Service search: argument checks per javadoc (null/empty/duplicate/out of range), positive transaction id, at most 1 at a time (`bluetooth.sd.trans.max`), ends after
  200 ms with `SERVICE_SEARCH_DEVICE_NOT_REACHABLE` (a peer is only an address, nothing answers); `cancelServiceSearch` -> `SERVICE_SEARCH_TERMINATED`; `selectService` -> `null`
  ("null if no service could be found"). `RemoteDevice`: 12 hex characters, equals/hashCode on the address, no security state ever set, `getFriendlyName(false)` null /
  `(true)` IOException, `getRemoteDevice(conn)`/`authorize`/`encrypt`/`isAuthorized` -> `IllegalArgumentException` (no Bluetooth connection exists). `Connector.open` client URL ->
  `BluetoothConnectionException(TIMEOUT)`, server URL (`btspp://localhost:UUID`) -> `ConnectionNotFoundException` (accepting connections is not simulated).
  Value classes `UUID`, `DataElement` (full javadoc tables), `DeviceClass` are complete and need no hardware. Tests: vmspec `bt02_values`, `bt03_sim` (profile.json).
* **Host interface for the real adapter (M6) - NOT done**: `jlkvm.host.BluetoothNatives` (`mode0/address0/name0` of the ROM-private `jlkvm.internal.bt.Hw`) is the single plug point;
  a mode `host` would be added there together with natives for inquiry / service search / connections (`ServiceRecord` is only an interface here, no implementation exists, no
  service can be registered, `L2CAPConnection(Notifier)` have no implementation). Nothing touches a radio.
* UNVERIFIED (not in the SDK javadoc, the project's own reading): everything listed as "values UNVERIFIED" above; the order NPE before IAE in `startInquiry`; the durations 1000/200 ms;
  `UUID.toString()` = 32 upper-case digits (also for 16/32-bit UUIDs) and left zero padding of a short 128-bit string; `DeviceClass` accessors keep the bits in place (masks 0xFFE000 /
  0x1F00 / 0xFC); the class of device of the local phone 0x20020C; `DataElement.clone()` (in the SDK DB, no javadoc) = deep copy; `BluetoothConnectionException(int)` accepts 1..6.

## JSR-179 Location (`javax.microedition.location`)

* **Source of the API: no spec, javadoc or SDK for JSR-179/256/234 is in the repo** (the Nokia S40 DP2.0 SDK predates them; PLAN V15). Signatures come from the freej2me stubs (GPL jar,
  local; nothing copied; no JSR-179 stub exists on Maven Central under `org.microemu`). Everything is **UNVERIFIED** and to be redone when an SDK with the API is supplied
  (S40 6th edition / S60 SDKs carry JSR-179 and JSR-256): replace the DB, run the diff again, re-read the lists below.
* The stub looked like a copy of an implementation, so `jsr179-location.json` was corrected by hand against the API as remembered (the `source` field of the DB says so):
  `Coordinates` has no public fields, `setLatitude/setLongitude(double)` (the stub had `float`: games call them with doubles, a `float` DB would have been a `NoSuchMethodError`), `convert(double,int)`
  static; `Location()` takes no arguments; `LandmarkStore` has no public constructor.
* **No position source** (this is every device the project can describe today; the Android host, M6, would add one - NOT done): `LocationProvider.getInstance(criteria)` answers **`null`**
  (the contract: "null if no provider ... can be found"; `LocationException` is for providers that exist but are all out of service), `getLastKnownLocation()` `null`, `Orientation.getOrientation()` `null`,
  `addProximityListener` -> `LocationException` after the argument checks (NPE, `IllegalArgumentException` for a radius <= 0), `removeProximityListener(null)` NPE. `Location` is concrete with a protected
  constructor: the instance a host would hand out subclasses it; the base one is the invalid location (`isValid()` false, speed and course NaN, no coordinates). Test: vmspec `loc01_nohw`.
* Data classes complete (vmspec `loc02_values`): `Coordinates` (range checks, `convert` both directions with `DD:MM:SS.sss` / `DD:MM.mmm`, spherical `distance` radius 6378137 m and `azimuthTo` with
  an `atan` written in the guest because CLDC 1.1 `Math` has none, `jlkvm.internal.loc.Geo`), `QualifiedCoordinates`, `Criteria` (defaults NO_REQUIREMENT, cost allowed), `AddressInfo`, `Orientation`, `Landmark`.
  `LandmarkStore` is in memory for the run only (no persistence, the default store always exists, `listLandmarkStores()` has it as entry `null`, `getLandmarks` returns `null` when nothing matches).
* UNVERIFIED: every point above (text format of `convert`, spherical distance, in-memory stores, `null` rather than `LocationException` from `getInstance`, accuracy checks).

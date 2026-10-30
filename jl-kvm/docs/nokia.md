# Nokia UI API layer (PLAN T28)

Layer `rom-nokiaui.jar` (`vm-classlib/nokiaui/src`, task `:vm-classlib:buildNokiaUiRom`), loaded by the desktop host only when the profile lists
`"nokia-ui"` in `apis` (D10, `RomLayers`); without it the classes do not exist for the game (`Class.forName` fails). Public surface ==
cldc-1.1 + midp-2.0 + **`tools/api-sigs/db/s40dp2-nokia-ui.json`** (signatures from the Nokia S40 DP2.0 SDK 1.1 `classes.zip`, PLAN V14b); romgen
fails the build on any difference (`api surface == ... + s40dp2-nokia-ui.json`). Behaviour is taken from the Nokia UI API 1.1 javadoc of that SDK
(local, gitignored, not copied into the repo; cited in each test `meta.txt`).

## Signature sources (V14 / V14b)

* Standard: `s40dp2-nokia-ui.json` (SDK). 
* Reference only (not used any more): `org.microemu:microemu-nokiaui:2.0.4` differs in 4 members (public constructors of `DeviceControl` and
  `DirectUtils`, `FullCanvas.getWidth/getHeight`) and ships implementation classes (`DirectGraphicsImp`, `NokiaCommand*`) that are not API.
  JL-Mod (`app/.../com/nokia/mid/{ui,sound}`, Apache-2.0) has `DirectGraphics`, `FullCanvas` (+ `getWidth/getHeight` missing, inherited) and
  `SoundListener` identical to the SDK; its `Sound` / `DeviceControl` / `DirectUtils` carry S40 6th edition extras (`getUserInactivityTime`,
  `resetUserInactivityTime`, `DirectUtils.getFont/setHeader`) that belong to T29 and are NOT in this layer.
* Only signatures (facts) were used; no MicroEmulator (LGPL) or FreeJ2ME (GPL) code is in the repo. The two emulators and JL-Mod were read as
  behaviour references (secondary oracles) for the points the javadoc leaves open; every such point is listed under UNVERIFIED.

## What is where

| Class | Notes |
|---|---|
| `FullCanvas` | `Canvas` that is full screen from the constructor on (`CanvasAccess.initFull`: the size known to the application is the full one, no `sizeChanged` at the first show). `addCommand`, `setCommandListener` always throw `IllegalStateException` (javadoc). Soft keys and command keys arrive as plain `keyPressed/Released/Repeated` with the profile `keys{}` codes (Nokia: -6 -7 -5 -10 -11, arrows -1..-4; constants `KEY_SOFTKEY1..3`, `KEY_SEND`, `KEY_END`, `KEY_*_ARROW` per javadoc). |
| `DirectUtils` | `getDirectGraphics(g)` (one wrapper per `Graphics`, stored in a ROM-private slot: no allocation per paint), `createImage(w,h,argb)` (mutable image WITH alpha channel; `SurfaceFactory.createArgb`), `createImage(byte[],off,len)` (mutable copy of the decoded image, alpha kept). |
| `DirectGraphics` | state shared with `Graphics` (colour, alpha, clip, translation). See below. |
| `DeviceControl` | `setLights(0, 0..100)`, `flashLights`, `startVibra(freq 0..100, ms)` (freq 0 = detect: `IllegalStateException` when the device has no vibrator), `stopVibra` -> host `NokiaDevice` (`host-common`, headless `RecordingNokiaDevice` log: `vibra start F MS`, `vibra stop`, `lights N L`, `flash MS`). |
| `Sound`, `SoundListener` | built on the MMAPI player of T27 (below). |
| `jlkvm.internal.nokia.Ott` | Smart Messaging ringing tone parser -> `jlkvm.internal.media.tone.Seq` (same sequence type as RTTTL / tone sequences). |

Host: `NokiaNatives` (`jlkvm/internal/nokia/Host`), profile block `nokia{pixelFormat, soundFormats[], concurrentTone, concurrentWav}`
(`DeviceProfile.nokia*`; defaults 565, [1,5], 1, 1; UNVERIFIED), profile `profiles/jlkvm-nokia.json` (= jlkvm-min + `nokia-ui` + `nokia{}`).

## DirectGraphics

* **Alpha is real**: the colour argument of the host `Surface` natives carries `255 - alpha` in its high byte (0 = opaque, so every old caller is
  unchanged); `SoftRaster` blends (source over) when it is not 0. `setARGBColor(argb)` sets colour + alpha of the `Graphics` and every later
  `Graphics` primitive (fillRect, drawLine, arcs, text...) uses them until `setColor/setGrayScale` (alpha back to 255) - javadoc "Note on alpha use".
  `getAlphaComponent` returns it. Alpha 0 draws nothing. Hosts that cannot blend may ignore the high byte (opaque fallback).
* **drawImage(img,x,y,anchor,manipulation)**, **drawPixels(..., manipulation, format)**: manipulation = `FLIP_HORIZONTAL (0x2000)` /
  `FLIP_VERTICAL (0x4000)` OR one of `ROTATE_90/180/270` (counter-clockwise); javadoc order: rotation first, then vertical flip, then horizontal flip;
  the anchor is applied after. Mapped to the 8 MIDP transforms (table in `DirectGraphicsImp`, checked for all 16 combinations by `nk02_transforms`).
  Anything else -> `IllegalArgumentException`.
* **drawPixels/getPixels formats** (javadoc: `offset` and `scanlength` are in PIXELS for every format; array too small / negative index ->
  `ArrayIndexOutOfBoundsException`; width/height/x/y negative, wrong or unsupported format for the array type -> `IllegalArgumentException`;
  `null` -> `NullPointerException`): `int[]`: `TYPE_INT_8888_ARGB`, `TYPE_INT_888_RGB` (alpha byte ignored); `short[]`: `4444_ARGB`, `444_RGB`,
  `565_RGB`, `555_RGB`, `1555_ARGB`; `byte[]` (+ optional mask in the same format; mask 0 = transparent): `1_GRAY`, `1_GRAY_VERTICAL`, `2_GRAY`,
  `4_GRAY`, `8_GRAY`, `332_RGB`. 1-bit horizontal: pixel 0 in the most significant bit of byte 0 (javadoc 9x9 example reproduced by `nk01`);
  1-bit vertical: byte index `((offset/scanlength + y)/8) * scanlength + offset%scanlength + x`, least significant bit = topmost pixel (javadoc
  formula and both examples reproduced by `nk01`); bits of a byte that belong to no pixel are left unchanged by `getPixels`.
  `drawPixels` honours clip + translation, `getPixels` ignores the clip but adds the translation; the part of the region outside the surface is
  not read; on-screen contexts report alpha 255. Scratch arrays are kept in the wrapper (no allocation in a steady loop).
* **Polygons/triangles** take their own ARGB colour and leave the current colour of the `Graphics` unchanged (javadoc); `drawPolygon/drawTriangle` are
  closed; `fillPolygon` is an even-odd scan conversion (left crossing inclusive, right exclusive), `fillTriangle` is `Graphics.fillTriangle`;
  `nPoints < 3` fills nothing.
* `getNativePixelFormat()` = profile `nokia.pixelFormat` (default 565).

## Sound / SoundListener

`getSupportedFormats()` / `getConcurrentSoundCount(type)` come from the profile (`IllegalArgumentException` for an unsupported type). `FORMAT_TONE`:
`Sound(freq, ms)` (nearest MIDI note of the 128-note table; freq 0 = silence; 0..13288 Hz, the top of the javadoc table; duration <= 0 or freq out of
range -> `IllegalArgumentException`) and `Sound(byte[], FORMAT_TONE)` = Smart Messaging ringing tone ("basic song" and "temporary song") parsed by
`Ott` and played by `TonePlayer` (T27: notes timed on a guest thread, played on the host tone generator; never through a media decoder).
`FORMAT_WAV`: `Manager.createPlayer(stream, null)` (content sniffed; MIDI is accepted too) on the host `AudioDevice`. State machine per javadoc:
`SOUND_STOPPED` after construction/init, `play(loop)` (0 = forever, negative -> `IllegalArgumentException`, silent no-op when uninitialized, restart
generates STOPPED then PLAYING), `stop`, `resume` (restarts from the beginning), `release` -> `SOUND_UNINITIALIZED`, a failed `init` leaves the sound
uninitialized, `setGain` clamps to 0..255 (any gain > 0 gives volume >= 1%). One concurrent sound per format by default: a new `play` stops the
sound that started earliest. The `SoundListener` is called only on the guest thread `media-events` (never inside the causing call, never on a host
thread), proven by `NokiaHostTest`.

### Smart Messaging bit layout used by `Ott` (UNVERIFIED against the specification)

The javadoc only names the format; the layout below is what the three reference decoders agree on and is NOT quoted from the specification (not in
the repo): `command length (8)`, `0x4A` ringing-tone-programming (8), `0x1D` sound (7) [or `0x22` unicode (7) + filler (1) + `0x1D` (7), titles in 16-bit
characters], song type (3: 1 basic with title length (4) + title, 2 temporary; 3..5 unsupported), pattern count (8), per pattern header id `000` (3),
pattern id (2), loop (4), instruction count (8); instructions (3-bit id): `001` note (value 4: 0 pause, 1..12 = C..B; duration 3: 1 whole .. 6 1/32;
specifier 2: none, dotted x1.5, double dotted x1.75, 2/3), `010` scale (2: value v -> MIDI `60 + 12v + semitone`, default v = 1 = C5), `011` style
(2: 0 natural = sounds 20/21 of the note, 1 continuous, 2 staccato = half), `100` tempo (5: table 25 28 31 35 40 45 50 56 63 70 80 90 100 112 125 140 160
180 200 225 250 285 320 355 400 450 500 565 635 715 800 900 bpm), `101` volume (4: table 0 48 56 64 72 80 88 92 100 104 108 112 116 120 124 127 of 127).
Choices that are mine and UNVERIFIED: default tempo 63 bpm, loop value n plays a pattern n+1 times and 15 ("endless") once, instruction count 0 repeats the
last defined pattern, scale and style restart with every newly defined pattern while tempo and volume continue, duration 0/7 and note value 13..15 are
treated as quarter note / pause, an unknown instruction or data ending after at least one note ends the tone (notes so far play; JL-Mod has a fix for
real tones with a wrong instruction count) and without any note is an `IllegalArgumentException`. Constructor errors: unknown type or undecodable data
-> `IllegalArgumentException` immediately (the javadoc allows delaying it until `play`), null data -> `NullPointerException`.

## Tests

* `tests/vmspec/cases/nk01_directgraphics_pixels` (69 lines), `nk02_transforms` (7 lines + 16-combination check), `nk04_sound_ott` (47 lines): spec-only,
  `stubs: nokia-ui` (compile against the MicroEmulator Nokia UI jar, runtime ROM is ours). Oracles are not run: MicroEmulator's DirectGraphics prints
  "public void drawPixels..." (unimplemented) and ignores manipulations, its Sound is an empty stub; FreeJ2ME differs on 555/1555 and swallows bad
  OTT data. Where the javadoc is silent the expectation is marked UNVERIFIED in `meta.txt`.
* `host-common` `OttParserTest` (8 tests: values, tempo/volume/scale/style, durations, loops, repeat, unicode, 15 error cases, many notes).
* `host-desktop` `NokiaHostTest` (MIDlet `tests/midlets/nokia`, script `tests/midlets/scripts/nokia.inp`): layer absent without `nokia-ui`; full screen;
  soft keys as key events; `addCommand` -> `IllegalStateException`; DeviceControl log; Sound events on a guest thread; one-slot concurrency.
* `jarscan` profile `tools/jarscan/profiles/cldc11-midp20-nokiaui.json` (adds `s40dp2-nokia-ui`): 12 games that use `com.nokia.mid.*` -> 0 `com.nokia`
  MISSING (see `docs/games-api-scan.md`).

## UNVERIFIED / not done

UNVERIFIED: polarity of the gray formats (set bit = black; 2/4/8-bit 0 = white), channel expansion by bit replication, 1555 alpha bit = 255, rounding of the
blend, polygon edge rasterisation, `getNativePixelFormat` default, frequency range upper bound, default gain 255, all OTT choices above, behaviour of the
real key codes when the profile uses non-Nokia codes (the FullCanvas constants are fixed, the events carry the profile codes).
Not done: `Graphics` primitives other than the ones through `Surface` natives ignore alpha only on hosts that cannot blend (android `SurfaceFactory`
is M6); `DirectUtils.getFont/setHeader`, `DeviceControl.getUserInactivityTime` (S40 6th, T29); real vibration/backlight on host-android (M6); WAV
through a real decoder (host AudioDevice, M6).

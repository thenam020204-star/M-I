# MIDP layer, hosts, lcdui natives (PLAN T21, T22, T23)

## 1. ROM layers and modules (D10)

| Piece | Where | Notes |
|---|---|---|
| CLDC 1.1 guest library | `vm-classlib/src` -> `build/rom/rom.jar` | unchanged (T17-T20) |
| MIDP 2.0 guest layer | `vm-classlib/midp/src` -> `build/rom/rom-midp.jar` | `:vm-classlib:buildMidpRom`; the jar holds ONLY the MIDP classes (romgen `--delta`); checked on base + layer: closure, strict verifier, and the surface == union of `cldc-1.1.json` + `midp-2.0.json` (romgen accepts several `--api-db`) |
| Profile selects layers | `profile.apis` (`profiles/*.json`, default `["cldc-1.1","midp-2.0"]`) | host-desktop `RomLayers` searches the layer jars in that order: a class of an undeclared layer does not exist (D10) |
| Portable host code | module `host-common` (pure Java 8, no `android.*`, checked by `checkNoAndroidImports`) | `jlkvm.host.ams.{Descriptor,MidletSuite,AmsHost}`, `jlkvm.host.{MidpNatives,PlatformServices,HostEvents,RecordingServices}`, `jlkvm.host.ui.{Surface,SurfaceFactory,SoftRaster,PngDecoder,BitmapFonts}` |
| Desktop host | module `host-desktop` | `DesktopHost`, `HeadlessRunner` (CLI + API), `ScriptedInput`, `RomLayers`, `JarClassSource`, `Clock` |

`host-common` is a new module (not in the PLAN tree): the code that host-desktop AND host-android must share (JAD parser, natives glue, raster, PNG)
cannot live in vm-core (D2: the core stays the VM) and must not be copied. host-android will depend on it the same way.

MIDP classes of the stub DB that later tasks write are listed in `pending` in `vm-classlib/build.gradle.kts` and ignored by the surface check until their task ticks:
T24 (Choice*, CustomItem, DateField, Form, ImageItem, ItemStateListener, List, Spacer, StringItem, TextBox, TextField), T25 (`lcdui/game/`), T26 (`rms/`), T27 (`media/`),
and three groups no task of T21-T27 owns: `javax.microedition.io.*` of MIDP (HttpConnection... the GCF part), `javax.microedition.pki`, `java.util.Timer/TimerTask`.
Item, Gauge, Alert, Screen, AlertType, Ticker, Command, CommandListener, ItemCommandListener exist with their full API surface but are only the MODEL (no layout / drawing, no Alert timer UI): T24.

## 2. T21 - MIDlet life cycle, JAD / manifest, AMS

* Guest: `javax.microedition.midlet.MIDlet` (full API), package-private `Bridge` (reaches the protected `startApp/pauseApp/destroyApp` for the AMS), `jlkvm.internal.Ams` (ROM-private).
  The VM launches `jlkvm/internal/Ams.run(String className)` as the main thread: it starts the event thread, constructs the MIDlet (state Paused), calls `startApp` (state Active).
* States and order (UNVERIFIED on Nokia; the oracles never pause):
  * host pause: `hideNotify` of the shown Canvas, then `pauseApp()`; host resume: `startApp()`, then `showNotify` + repaint. Both run on the event thread.
  * `notifyPaused()` -> Paused; a later system resume does NOT restart a MIDlet that paused itself; `resumeRequest()` queues `startApp()` on the event thread.
  * `startApp` throwing `MIDletStateChangeException` -> stays Paused; any other exception -> `destroyApp(true)` and exit. `destroyApp(false)` throwing `MIDletStateChangeException` = refused, the MIDlet lives on; `destroyApp(true)` always ends it.
  * `notifyDestroyed()` -> `Runtime.exit(0)` (no `destroyApp`). Lifecycle requests that arrive while `startApp` is still running wait until it returns.
* `getAppProperty`: attributes of manifest merged with JAD (JAD wins), name lookup case-insensitive, `null` key -> NullPointerException. `platformRequest(url)`: host decides (`PlatformServices`: 0 handled, 1 exit first, -1 -> `ConnectionNotFoundException`); `checkPermission`: host policy (1/0/-1).
* JAD / manifest (`Descriptor`, `MidletSuite`): port of JL-Mod's `ru.woesss.j2me.jar.Descriptor` (Apache-2.0) without the Android UI code; continuation lines by leading space (the original broke on a continuation containing ':'), BOM, CR/LF/CRLF; required attributes checked (JAD: Jar-URL, Jar-Size); Name/Vendor/Version of JAD and manifest must agree; `MIDlet-n` -> `Entry(name, icon, class)`; `--midlet N` selects.
* Host API for Android (`AmsHost`): `pause()`, `resume()`, `destroy(boolean)`, `repaint()`, `displaySize(w,h)`, key / pointer methods - every one only calls `Vm.postEvent` (thread-safe). **Not yet wired to an Activity**: the mapping `onPause -> pause()`, `onResume -> resume()` is documented in `AmsHost` and exercised on the desktop with scripted `pause` / `resume` / `destroy` lines (`DesktopHostTest.lifecycleOrder`); checking it on a real Activity is M6 (T35).

## 3. T22 - EventLoop, Display, Canvas (D13)

* One guest thread `event` (`jlkvm.internal.EventLoop`) runs `paint`, `keyPressed/Released/Repeated`, `pointer*`, `commandAction`, `callSerially` runnables, `showNotify/hideNotify`, `sizeChanged`, and the MIDlet pause/resume/destroy callbacks. Per round it runs at most one guest job, then at most one host event (fair).
  Host events come from `Vm.postEvent` (any thread) through the native `EventLoop.next0`, which blocks only the event thread; guest threads post jobs with `EventLoop.post` (native `wake0`). The host never calls guest code.
* Debug assertion (D15, `DebugFlags.ENABLED`): `Vm.runSlice` records the first calling thread as the vm-thread; a guest entry from any other thread logs, counts (`Vm.threadViolations`) and throws `IllegalStateException`; `postEvent/requestExit` stay legal everywhere (`guestEntryFromAnotherThreadIsRejected`).
* Event numbers (`HostEvents` == `EventLoop` constants): 1-3 key pressed/released/repeated (a = code), 4-6 pointer (a,b = x,y), 7 display size (a,b = w,h), 8 repaint all, 20-22 pause / resume / destroy.
* `Display`: `setCurrent` changes `getCurrent()` at once; hideNotify (old), then showNotify (new), then the first paint are delivered on the event thread in order. `Canvas.isShown()` is false inside `showNotify` (MIDP: "prior to being made visible"), true afterwards, false again inside `hideNotify`.
  Repaint: `repaint(x,y,w,h)` requests are coalesced to the union rectangle (the clip of `paint`'s Graphics); `serviceRepaints()` from the event thread paints at once, from another thread blocks until the pending paint is done (returns at once when the Canvas is not shown); an exception in `paint` / any callback is printed with its guest stack and the event thread continues. `callSerially` is FIFO with the other jobs, i.e. after the pending paint, and repaint requests made before it are merged.
* Key codes come from the profile (`keys{}`): Nokia defaults soft -6/-7, select -5, up/down/left/right -1..-4, clear -8, send -10, end -11 (UNVERIFIED, T33). `getKeyCode`, `getGameAction` (arrows and 2/4/6/8/5, 1/3/7/9 = GAME_A..D), `getKeyName` use them. Soft keys with commands (normal mode): left = most important command, right = a BACK/CANCEL/EXIT/STOP command or the second one; consumed (no key events); in full-screen mode (and with no commands) they reach `keyPressed`. A menu for more than two commands is T24.
* Pointer events are dropped when the profile says `pointerEvents: false`; `keyRepeated` only when `repeatEvents`. Key repeat timing (`keyRepeat{delayMs,intervalMs}`) is applied by the host driver (script `key hold`).
* Oracle comparison (`tests/vmspec/cases/ev01_display_order`, MicroEmulator 2.0.4 and FreeJ2ME-Plus; neither is a device, V4):

  | Point | jl-kvm | MicroEmulator | FreeJ2ME | Decision |
  |---|---|---|---|---|
  | setCurrent(A) then (B): order of callbacks | hideNotify(A), showNotify(B) | showNotify(B), hideNotify(A) | same as MicroEmu | RI order; MIDP leaves it open |
  | repaint(); callSerially(R1); repaint(); callSerially(R2) | paint, R1, R2 | paint, R1, paint, R2 | paint, R1, R2 | coalescing is allowed by MIDP |
  | repaint(); repaint(); repaint(5,5,3,3) | one paint, clip = union | same | one paint with the LAST rectangle only | union |
  | Canvas after showNotify | paints by itself | paints by itself | does not | device behaviour |
  | getCurrent() right after setCurrent | new displayable | old one (display events wait for the end of `startApp`) | same | spec: "unlikely to return the new one"; games on devices expect the new one |
  | hideNotify when the MIDlet ends | not called | called | called | UNVERIFIED |
  | display events while `startApp` still runs | delivered | held back until `startApp` returns | same | games with a loop in `startApp` need them |

## 4. T23 - Graphics / Image / Font

* Guest (`Graphics`, `Image`, `Font`): `Graphics` keeps colour, translate, clip (absolute box), stroke, font; every draw native gets `(target handle, colour, clip box, absolute coordinates)`, so the host keeps no per-Graphics state. Anchors and argument checks (IllegalArgumentException / NullPointerException / IllegalStateException / ArrayIndexOutOfBounds) are done in the guest, as are the 8 `drawRegion` transforms' sizes. An `Image` owns a host handle (`peer`), released by the collector when the object dies (`Handles` owner, D8). Canvas painting goes to a persistent back-buffer Image; `Display.present0` hands the finished frame to the host (`PlatformServices.present`).
* `imagesCountInHeap` (profile): when true the guest Image also holds a `byte[w*h*4]`, so the pixel memory is charged to the guest heap, freed by the collector, and a too small heap gives the guest `OutOfMemoryError` (`GraphicsHostTest`). Default false (host memory only). UNVERIFIED which one a Nokia does.
* Common native interface (what host-android has to implement): `jlkvm.host.ui.Surface` (draw primitives with absolute coordinates + clip box, `drawSurface` with the 8 transforms, `drawRgb`, `copyArea`, `getRgb`, `copyRegion`) and `SurfaceFactory` (create / decode / fromRgb / font metrics). The ~30 native keys (`javax/microedition/lcdui/Graphics.fillRect0(IIIIIIIIII)V` ...) are registered once in `MidpNatives`; the full list is the `register` method (descriptors == the `private static native` declarations of `Graphics`, `Image`, `Font`, `Display`, `jlkvm.internal.Device/EventLoop/Ams`).
* `SoftRaster` (pure Java, deterministic: integers, StrictMath only for trigonometry) is the desktop implementation and also usable on Android; PNG: `PngDecoder` (all colour types / depths, tRNS, Adam7); fonts: `BitmapFonts` over `jlkvm/fonts.bin` baked from DejaVu Sans / Sans Mono (`tools/fontgen/BakeFont.java`, licence note next to the resource), sizes 10/13/17 px, bold = +1 px smear, italic = shear, underline; Latin-1, Latin Extended-A, Vietnamese; unknown chars draw `?`. No AWT at run time.
* Rasterisation rules: lines Bresenham incl. both end points, DOTTED = every second pixel; `fillRect` covers `[x,x+w) x [y,y+h)`; `drawRect` covers `x..x+w, y..y+h`; ellipses / round rects / triangles sample pixel centres (triangle edges also drawn); alpha blend `(s*a + d*(255-a) + 127)/255` onto an opaque destination; mutable images are opaque white initially; `drawImage` onto itself reads from a snapshot.
* Oracle comparison of `tests/vmspec/cases/gfx01_graphics` (64 fingerprint / value lines): identical to MicroEmulator on 38, to FreeJ2ME on 36. The differences:

  | Line(s) | jl-kvm | MicroEmulator | FreeJ2ME | Reason |
  |---|---|---|---|---|
  | `dotted` | every 2nd pixel | solid (stroke ignored) | 2 on / 2 off | MIDP leaves the pattern open |
  | `offscreen+empty` | nothing outside the image | differs | differs | only inherited: the step draws on the same image as `dotted` (clipping of off-image rectangles alone matches MicroEmulator, checked separately) |
  | `emptyclip` | 0x0 | negative size (-36x-52) | same | oracle bug |
  | `drawRoundRect / fillRoundRect / drawArc / fillArc / fillTriangle` | own rasteriser | AWT | AWT | edge pixels differ by design |
  | `text` | baked DejaVu bitmaps | AWT fonts | AWT fonts | fonts differ by design |
  | `drawRegion t0..t7` | anchor applied like drawImage | BOTTOM/RIGHT anchor of `drawRegion` off by one pixel (bug: `drawRegion(.., BOTTOM|RIGHT)` lands 1 px right/down; `drawImage` is right) | same as jl-kvm | spec: same anchor rules as drawImage |
  | `drawImage anchors` | exact | exact | differs: its destination keeps an alpha channel, blended pixels carry alpha (`befe7e7e`) and round differently | MIDP: blend onto an opaque surface |
  | `drawRGB alpha / opaque / offset`, `getRGB` | blends | ignores per-pixel alpha | alpha channel in destination | same |
  | `drawImage self` | correct copy (snapshot) | smears (reads while writing) | same | overlap is undefined in MIDP |
  | `iae ...` (anchor 0, BASELINE for images, colour 256, transform 9, drawRegion onto itself) | IllegalArgumentException as the javadoc says | no exception for several | no exception for several | the oracles skip checks |
  | `/gfx/*.png` (RGBA, palette + tRNS, grey, RGB), `createImage` 8 transforms, `getRGB`, clip, translate, fillRect, drawRect, drawLine, exceptions of `createImage/getGraphics/drawString(null)` | identical | identical | identical, except the rgba PNG: FreeJ2ME returns 0 for fully transparent pixels and off-by-one colours for alpha 0x80 (premultiplied storage) | |

* **Android**: not done and not tested in this container. `host-android` has to provide `PlatformServices` (present = blit to the SurfaceView / GLES texture, vibrate, platformRequest via Intent), a `SurfaceFactory` (either `SoftRaster` - same pixels as the desktop by construction - or a Bitmap/Canvas backend moved from JL-Mod; the latter cannot match the software raster bit for bit and needs the M6 comparison), and the Activity wiring of `AmsHost`/keys. The acceptance "same pixel hash on desktop and Android" of T23 is therefore NOT claimed.

## 5. How to run

    env -u JAVA_HOME ./gradlew --no-daemon :vm-classlib:buildMidpRom :host-common:test :host-desktop:test
    tests/midlets/build.sh tests/midlets/events tests/midlets/build/events.jar
    java -cp host-desktop/build/classes/java/main:host-common/build/classes/java/main:host-common/build/resources/main:vm-core/build/classes/java/main \
         -Djlkvm.rom.dir=vm-classlib/build/rom jlkvm.host.desktop.HeadlessRunner --script tests/midlets/scripts/events.inp tests/midlets/build/events.jar
    tests/vmspec/run_vmspec.sh --engine jlkvm          # ROM = cldc + midp + test overlay (vm-core/src/test/guest-stub/build.sh)

Script format: `tests/games/README.md` (+ `destroy [0|1]`, `size W H`, `repaint`, `key hold C ms`, `key repeat C`).
